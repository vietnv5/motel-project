/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.Bill;
import com.motel.model.BillService;
import com.motel.model.Contract;
import com.motel.model.ContractService;
import com.motel.model.ElectricWater;
import com.motel.model.Home;
import com.motel.model.Room;
import com.motel.model.Service;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatItemBO;
import com.slook.model.CatUser;
import com.slook.persistence.BillServiceImpl;
import com.slook.persistence.ContractServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.RoomServiceImpl;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.ExcelWriterUtils;
import com.slook.util.MessageUtil;
import com.slook.util.PdfUtil;
import com.slook.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import static org.omnifaces.util.Faces.getRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author VietNV Jan 25, 2018
 */
@ManagedBean
@ViewScoped
public class BillController {

    protected static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @ManagedProperty(value = "#{billService}")
    private BillServiceImpl billServiceImpl;

    private GenericDaoImplNewV2<BillService, Long> billServiceService;
    private GenericDaoImplNewV2<Contract, Long> contractService;
    private GenericDaoImplNewV2<ContractService, Long> contractServiceService;

    private GenericDaoImplNewV2<ElectricWater, Long> electricWaterService;
    LazyDataModel<Bill> lazyDataModel;
    Bill currBill = new Bill();
    private String oldObjectStr = null;
    private Long groupUserId;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<Home> lstHome;
    List<Room> lstRoom;
    Contract contract;
    List<BillService> lstBillService;
    BillService currBillService = new BillService();
    Map<Long, CatItemBO> mapUnit = new HashMap<>();

    //bo sung in ve
    private String pathRealFile = "";
    StreamedContent fileExported;
    //export ds hoa don
    private Long homeId;

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {
        billServiceService = new GenericDaoImplNewV2<BillService, Long>() {
        };
        electricWaterService = new GenericDaoImplNewV2<ElectricWater, Long>() {
        };
        contractService = new GenericDaoImplNewV2<Contract, Long>() {
        };
        contractServiceService = new GenericDaoImplNewV2<ContractService, Long>() {
        };

        lstBillService = new ArrayList<>();
        try {
            CatUser catUser = null;
            groupUserId = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("paymentDate", Constant.ORDER.DESC);
            order.put("home.homeName", Constant.ORDER.ASC);
            order.put("room.roomName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
//            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("home.groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<Bill, Long>(billServiceImpl, filter, order);

            //init data
            Map<String, Object> filtersHome = new HashMap<>();
            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filtersHome.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("homeName", Constant.ORDER.ASC);
            lstHome = HomeServiceImpl.getInstance().findList(filtersHome, orderHome);

            //map unit
            mapUnit = CommonUtil.getMapCatItemByKeyId(Constant.CAT_CODE.UNIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, true, false, true, false
        );
    }

    /**
     * Lấy danh sách thông tin điện nước của phòng theo thời gian giảm dần để
     * tính hóa đơn lấy mốc là tháng tính hóa đơn
     *
     * @param roomId id phòng
     * @param month tháng lập hóa đơn
     * @return
     */
    public List<ElectricWater> getListElectricWaterOfRoom(Long roomId, Date month) {
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("timeLine", Constant.ORDER.DESC);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status-NEQ", Constant.STATUS.DELETE);
        if (month != null) {
            filter.put("month", month);
        }
        if (roomId != null) {
            filter.put("roomId", roomId);
        }
        List<ElectricWater> lst = new ArrayList<>();
        try {
            lst = electricWaterService.findList(filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

    public static Contract getContractOfRoom(Long roomId) {
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("startTime", Constant.ORDER.DESC);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status", Constant.CONTRACT.STATUS_ACTIVE);
        filter.put("roomId", roomId);

        List<Contract> lst = new ArrayList<>();
        try {
            lst = ContractServiceImpl.getInstance().findList(filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lst != null && !lst.isEmpty()) {
            return lst.get(0);
        }
        return null;
    }

    public void onChangeHome() {
        if (currBill != null && currBill.getHomeId() != null && currBill.getHomeId() > 0) {
            Map<String, Object> filtersRoom = new HashMap<>();
            filtersRoom.put("status-NEQ", Constant.ROOM_STATUS.DELETE);
            filtersRoom.put("homeId", currBill.getHomeId());
            LinkedHashMap<String, String> orderRoom = new LinkedHashMap<>();
            orderRoom.put("roomName", Constant.ORDER.ASC);
            try {
                lstRoom = RoomServiceImpl.getInstance().findList(filtersRoom, orderRoom);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }

        } else {
            lstRoom = new ArrayList<>();
        }
    }

    /**
     * tự động danh sach dich vu
     */
    public void onSelectRoom() {
        Room room = null;
        if (currBill != null && currBill.getRoomId() != null) {
            //lay thong tin hop dong cua phong danh sach cac dich vu su dung
            contract = getContractOfRoom(currBill.getRoomId());
            List<Service> lstService = new ArrayList<>();
            lstBillService = new ArrayList<>();
            if (contract != null && contract.getContractServiceList() != null) {
                lstService = contract.getContractServiceList().stream().map(ContractService::getService)
                        .collect(Collectors.toList());
                currBill.setContractId(contract.getContractId());
            } else {
            }
            //lay thong tin dien nuoc
            Date month = DateUtils.round(currBill.getPaymentDate(), Calendar.MONTH);
            List<ElectricWater> lstOldEW = getListElectricWaterOfRoom(currBill.getRoomId(), month);
            ElectricWater ew = null;
            if (lstOldEW != null && !lstOldEW.isEmpty()) {
                ew = lstOldEW.get(0);
            }
            // tao gia cac dich vu
            for (Service s : lstService) {
                s.setUnitBO(mapUnit.get(s.getUnit()));

                BillService bs = new BillService();
                bs.setService(s);
                bs.setPrice(s.getPrice());
                Double amount = 0d;
                //dv dien nuoc
                if (Constant.SERVICE.ELECTRIC.equals(s.getServiceCode())) {
                    if (ew != null) {
                        bs.setIndexOld(ew.getElectricOld());
                        bs.setIndexNew(ew.getElectricNew());
                    }
                } else if (Constant.SERVICE.WATER.equals(s.getServiceCode())) {
                    if (ew != null) {
                        bs.setIndexOld(ew.getWaterOld());
                        bs.setIndexNew(ew.getWaterNew());
                    }
                } else {
                    amount = 1d;
                }
                if (bs.getIndexOld() != null && bs.getIndexNew() != null) {
                    amount = bs.getIndexNew() - bs.getIndexOld();
                }

                bs.setAmount(amount);
                bs.setTotalPrice(Math.round(bs.getPrice() * bs.getAmount()));
                bs.setServiceId(s.getServiceId());
                lstBillService.add(bs);
            }

            //tao gia phong
            try {
                room = RoomServiceImpl.getInstance().findById(currBill.getRoomId());
            } catch (Exception e) {
            }

            if (room != null && room.getPrice() != null) {
                Service svDefault = new Service();
                svDefault.setServiceName(room.getRoomName());
                svDefault.setUnitBO(mapUnit.get(Constant.CAT_ITEM.UNIT.ROOM_PER_MONTH_ID));

                BillService bDf = new BillService();
                bDf.setService(svDefault);
                bDf.setPrice(room.getPrice());
                bDf.setAmount(1d);
                bDf.setTotalPrice(Math.round(bDf.getPrice() * bDf.getAmount()));
                bDf.setServiceId(Constant.SERVICE.PRICE_ROOM_ID);
                lstBillService.add(0, bDf);
            }
            onChangeTotalPricePayment(lstBillService);
        }
    }

    public void onChangeTotalPricePayment(List<BillService> lstBillService) {
        Long totalPricePayment = 0l;
        if (lstBillService != null) {
            for (BillService b : lstBillService) {
                totalPricePayment += b.getTotalPrice();
            }
        }
        currBill.setTotalPrice(totalPricePayment);
    }

    public void onchangeBillService(int index) {

        if (lstBillService != null && lstBillService.get(index) != null) {
            BillService billService = lstBillService.get(index);
            if (billService.getAmount() != null && billService.getPrice() != null) {
                billService.setTotalPrice(Math.round(
                        billService.getAmount() * billService.getPrice()));
            }
//            onChangeComputingPrice(goodsDTOChange);
            onChangeTotalPricePayment(lstBillService);
        }
    }

    public void preAdd() {
        this.currBill = new Bill();
        this.lstBillService = new ArrayList<>();
        isEdit = false;

        currBill.setPaymentDate(new Date());
        //xu ly round month
//        Date month = DateUtils.round(new Date(), Calendar.MONTH);
//        currBill.setMonth(month);
        if (lstHome != null && !lstHome.isEmpty()) {
            currBill.setHomeId(lstHome.get(0).getHomeId());
        }
//        this.selectedDate = month;
        currBill.createBillCode(groupUserId);//tao ma hop dong tu dong

        onChangeHome();
    }

    public void preEdit(Bill bill) {
        try {
            currBill = billServiceImpl.findById(bill.getBillId());

//            this.selectedDate = currBill.getMonth();
            onChangeHome();
            lstBillService = currBill.getBillServiceList();
            if (lstBillService != null) {
                for (BillService bo : lstBillService) {
                    if (bo.getService() != null && bo.getService().getUnit() != null) {
                        bo.getService().setUnitBO(mapUnit.get(bo.getService().getUnit()));
                    }
                }
            }
            //dich vu gia phong
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currBill.toString();
    }

    public void onSaveOrUpdate() {
        try {

//xu ly round month
//            currBill.setMonth(selectedDate);
//            Date month = DateUtils.round(currBill.getMonth(), Calendar.MONTH);
//            currBill.setMonth(month);
            Map<String, Object> filter = new HashMap<>();
//            filter.put("status-EQ", Constant.STATUS.ACTIVE);
//            filter.put("roomId", currBill.getRoomId());
//            filter.put("month", currBill.getMonth());
            filter.put("billCode", currBill.getBillCode());
            if (currBill.getBillId() != null) {
                filter.put("billId-NEQ", currBill.getBillId());
            }
            List<Bill> lstElectricWater = billServiceImpl.findList(filter);
            if (lstElectricWater != null && !lstElectricWater.isEmpty()) {
                MessageUtil.setErrorMessage("Mã hóa đơn đã tồn tại!");

                return;
            }
//            if (currBill.getStatus() == null) {
//                currBill.setStatus(Constant.STATUS.ACTIVE);
//            }
            if (currBill.getCreateTime() == null) {
                currBill.setCreateTime(new Date());
            }

            billServiceImpl.saveOrUpdate(currBill);
            //for list bill
            updateBillService(currBill.getBillId(), lstBillService);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currBill.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currBill.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('billDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void updateBillService(Long billId, List<BillService> lstBillService) throws Exception {
        List<Long> lstBillServiceNew = lstBillService.stream().map(BillService::getBillServiceId).collect(Collectors.toList());
        List<BillService> lstDel = new ArrayList<>();
        Map<String, Object> filter = new HashMap<>();
        filter.put("billId", billId);
        List<BillService> lstOld = billServiceService.findList(filter);
        if (lstOld != null) {
            for (BillService bo : lstOld) {
                if (!lstBillServiceNew.contains(bo.getBillServiceId())) {
                    lstDel.add(bo);
                }
            }
        }
        for (BillService b : lstBillService) {
            b.setBillId(billId);
            //fix loi dang tu dong insert ban ghi vao bang service
            if (b.getService() != null && b.getService().getServiceId() == null) {
                b.setService(null);
            }
        }
        billServiceService.delete(lstDel);
        billServiceService.saveOrUpdate(lstBillService);

    }

    public void onDelete(Bill bill) {
        try {
            oldObjectStr = bill.toString();
            Map<String, Object> filters = new HashMap<>();
            filters.put("billId", bill.getBillId());
            List<BillService> lstbs = billServiceService.findList(filters);
            billServiceService.delete(lstbs);
            billServiceImpl.delete(bill);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, oldObjectStr, currBill.toString(),
                    this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void preEditBillService(BillService billService) {
        this.currBillService = billService;
    }

    public void onChangeBillServicePrice() {
        if (currBillService.getAmount() != null && currBillService.getPrice() != null) {
            Double totalPrice = currBillService.getAmount() * currBillService.getPrice();
            currBillService.setTotalPrice(totalPrice.longValue());
        }
    }

    public void onSaveBillServiceInList() {
        //cap nhat lai thay doi
        onchangeBillService(this.lstBillService.indexOf(this.currBillService));
        RequestContext.getCurrentInstance().execute("PF('billServiceDlg').hide();");

    }

    //in hoa don
    public void exportBillPdf(Bill bill) {
        try {
            currBill = billServiceImpl.findById(bill.getBillId());

//            this.selectedDate = currBill.getMonth();
            lstBillService = currBill.getBillServiceList();
            if (lstBillService != null) {
                for (BillService bo : lstBillService) {
                    if (bo.getService() != null && bo.getService().getUnit() != null) {
                        bo.getService().setUnitBO(mapUnit.get(bo.getService().getUnit()));
                    }
                }
            }
            currBill.setBillServiceList(lstBillService);
            if (currBill.getContractId() != null) {
                Contract c = contractService.findById(currBill.getContractId());
                if (c != null) {
                    currBill.setContractCode(c.getContractCode());
                }
            }
//            String desFileNamePdf = currBill.getBillCode() + "_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";

            String resultPath = Constant.OUT_FOLDER;

            if (!CommonUtil.makeDirectory(Util.getRealPath(resultPath))) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }

            if (lstBillService == null || lstBillService.isEmpty()) {
                MessageUtil.setInfoMessage("Không có dịch vụ!");
                pathRealFile = "";
            } else {

                pathRealFile = PdfUtil.createBillPdf(currBill);
//                pathRealFile = Util.getRealPath(resultPath + desFileNamePdf);
            }

            InputStream stream = new FileInputStream(pathRealFile);
            fileExported = new DefaultStreamedContent(stream, "application/pdf", currBill.getBillCode() + ".pdf");
            if (pathRealFile != null && !pathRealFile.equals("")) {
                MessageUtil.setInfoMessage("Export thành công");
                RequestContext.getCurrentInstance().update(":showPdfForm");
                RequestContext.getCurrentInstance().execute("PF('showPdfDlg').show();");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
//xuat danh sach hoa don

    public void preExportBill() {
        this.selectedDate = DateUtils.truncate(new Date(), Calendar.MONTH);
    }

    public List<Bill> getBillForExport() {
        List<Bill> lst = new ArrayList<>();
        try {

            Map<String, Object> filtersHome = new HashMap<>();
//            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filtersHome.put("home.groupUserId", groupUserId);
            }
            if (homeId != null && homeId > 0) {
                filtersHome.put("homeId", homeId);

            }
            if (selectedDate != null) {
                filtersHome.put("paymentDate-GE", selectedDate);
                filtersHome.put("paymentDate-LT", DateUtils.addMonths(selectedDate, 1));
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("home.homeName", Constant.ORDER.ASC);
            orderHome.put("room.roomName", Constant.ORDER.ASC);
            lst = billServiceImpl.findList(filtersHome, orderHome);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lst;
    }

    public StreamedContent onExportBill() {
        DefaultStreamedContent defaultStreamed = null;
        Workbook wbTemplate = null;
        FileInputStream file = null;
        try {
            String templateName = "Template_Bill.xlsx";
            String templateFileName = File.separator + Constant.DIR.TEMPLATES + File.separator + templateName;
//            String resultPath = "/share/";
            String resultPath = Constant.OUT_FOLDER;
            if (!CommonUtil.makeDirectory(Util.getRealPath(resultPath))) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return null;
            }

            String tmpFileName = DateTimeUtils.format(new Date(), "yyyyMMddHHmm") + "_" + templateName;
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext();
            file = new FileInputStream(Util.getRealPath(templateFileName));

            if (templateFileName.endsWith("xls")) {
                wbTemplate = new HSSFWorkbook(file);

            } else if (templateFileName.endsWith("xlsx")) {
                wbTemplate = WorkbookFactory.create(file);

            } else {
                return defaultStreamed;
            }
            Sheet sheet = wbTemplate.getSheetAt(0);
            Row referRow;
            int rowHeader = 0;
            int collHeader = 9;

            int startRow = 2;
            int colData = 0;
            ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();

            // lay style
            Cell cellHeader = excelWriterUtils.getOrCreateRow(sheet, rowHeader).getCell(collHeader);
            CellStyle cellStyleHeader = cellHeader.getCellStyle();

            int withCol = sheet.getColumnWidth(collHeader);
            //style data
            CellStyle cellStyleCenter = excelWriterUtils.getOrCreateRow(sheet, startRow).getCell(0).getCellStyle();
            CellStyle cellStyleRight = excelWriterUtils.getOrCreateRow(sheet, startRow).getCell(2).getCellStyle();

            List<Bill> lstBill = getBillForExport();
            //lay ds dich vu su dung
            List<Service> lstService = new ArrayList<>();
            for (Bill b : lstBill) {
                if (b.getBillServiceList() != null) {
                    for (BillService bs : b.getBillServiceList()) {
                        if (bs.getService() != null && !lstService.contains(bs.getService())) {
                            lstService.add(bs.getService());
                        }
                    }
                }
            }

            Map<String, Service> mapService = new HashMap<>();
            List<Long> lstServiceIdExt = new ArrayList<>();
            List<String> lstServiceNameExt = new ArrayList<>();
            for (Service s : lstService) {
                mapService.put(s.getServiceCode(), s);
                if (!Constant.SERVICE.ELECTRIC.equals(s.getServiceCode())
                        && !Constant.SERVICE.WATER.equals(s.getServiceCode())
                        && !Constant.SERVICE.PRICE_ROOM_ID.equals(s.getServiceId())) {
                    lstServiceIdExt.add(s.getServiceId());
                    lstServiceNameExt.add(s.getServiceName());
                }
            }

            //xu ly tao head
            referRow = excelWriterUtils.getOrCreateRow(sheet, rowHeader);
            int colIndex = collHeader;
            for (String headName : lstServiceNameExt) {
                sheet.setColumnWidth(colIndex, withCol);
                referRow.createCell(colIndex).setCellValue(headName);
                referRow.getCell(colIndex).setCellStyle(cellStyleHeader);
                colIndex++;
                CellRangeAddress cra=new CellRangeAddress(0, 1, colIndex, colIndex);
                sheet.addMergedRegion(cra);//merge cell bo qua cot dau vi da merger
                
                RegionUtil.setBorderLeft(cellStyleHeader.getBorderLeft(), cra, sheet, wbTemplate);
                RegionUtil.setBorderRight(cellStyleHeader.getBorderLeft(), cra, sheet, wbTemplate);
            }
            //cot tong cong
//            sheet.addMergedRegion(new CellRangeAddress(0, 1, colIndex, colIndex));//merge cell
            sheet.setColumnWidth(colIndex, withCol);

            referRow.createCell(colIndex).setCellValue("Tổng cộng");
            referRow.getCell(colIndex).setCellStyle(cellStyleHeader);

            // list data
            for (Bill b : lstBill) {
                int c = 0;
                referRow = excelWriterUtils.getOrCreateRow(sheet, startRow);
                excelWriterUtils.createCell(sheet, c++, startRow, c + "", cellStyleCenter);
                excelWriterUtils.createCell(sheet, c++, startRow, b.getRoom() != null
                        ? b.getRoom().getRoomName() : "", cellStyleCenter);
                //xu ly theo dv cung cap dynamic
                //fix set style cho ca hop dong thieu dich vu
                for (int t = 0; t < (7 + lstServiceIdExt.size()); t++) {
                    excelWriterUtils.createCell(sheet, t + c, startRow,
                            "", cellStyleRight);
                }
                List<BillService> lstBs = b.getBillServiceList();
                for (BillService bs : lstBs) {

                    if (Constant.SERVICE.PRICE_ROOM_ID.equals(bs.getServiceId())) {
                        excelWriterUtils.createCell(sheet, c + 0, startRow,
                                DataUtil.getStringNumber(bs.getTotalPrice()), cellStyleRight);
                    } else if (bs.getService() != null
                            && Constant.SERVICE.ELECTRIC.equals(bs.getService().getServiceCode())) {
                        excelWriterUtils.createCell(sheet, c + 1, startRow,
                                DataUtil.getStringNumber(bs.getIndexOld()), cellStyleRight);
                        excelWriterUtils.createCell(sheet, c + 2, startRow,
                                DataUtil.getStringNumber(bs.getIndexNew()), cellStyleRight);
                        excelWriterUtils.createCell(sheet, c + 3, startRow,
                                DataUtil.getStringNumber(bs.getTotalPrice()), cellStyleRight);

                    } else if (bs.getService() != null
                            && Constant.SERVICE.WATER.equals(bs.getService().getServiceCode())) {
                        excelWriterUtils.createCell(sheet, c + 4, startRow,
                                DataUtil.getStringNumber(bs.getIndexOld()), cellStyleRight);
                        excelWriterUtils.createCell(sheet, c + 5, startRow,
                                DataUtil.getStringNumber(bs.getIndexNew()), cellStyleRight);
                        excelWriterUtils.createCell(sheet, c + 6, startRow,
                                DataUtil.getStringNumber(bs.getTotalPrice()), cellStyleRight);

                    } else if (lstServiceIdExt.contains(bs.getServiceId())) {
                        excelWriterUtils.createCell(sheet, c + 7 + lstServiceIdExt.indexOf(bs.getServiceId()), startRow,
                                DataUtil.getStringNumber(bs.getTotalPrice()), cellStyleRight);
                    }
                }
                //tong gia
                c += 7 + lstServiceIdExt.size();
                excelWriterUtils.createCell(sheet, c++, startRow, DataUtil.getStringNumber(b.getTotalPrice()), cellStyleRight);

                startRow++;
            }

            File tmpTemplateFile = new File(Util.getRealPath(resultPath), tmpFileName);
            OutputStream outputStream = new FileOutputStream(tmpTemplateFile);
            wbTemplate.write(outputStream);
            outputStream.flush();
            outputStream.close();

            InputStream stream = new FileInputStream((Util.getRealPath(resultPath + tmpFileName)));
            defaultStreamed = new DefaultStreamedContent(stream, "xlsx", tmpFileName);
            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.fail"),
                    "Xuất hóa đơn"));
        } finally {
            if (wbTemplate != null) {
                try {
                    wbTemplate.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return defaultStreamed;
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public LazyDataModel<Bill> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Bill> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Bill getCurrBill() {
        return currBill;
    }

    public void setCurrBill(Bill currBill) {
        this.currBill = currBill;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public GenericDaoImplNewV2<ElectricWater, Long> getElectricWaterService() {
        return electricWaterService;
    }

    public void setElectricWaterService(GenericDaoImplNewV2<ElectricWater, Long> electricWaterService) {
        this.electricWaterService = electricWaterService;
    }

    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
    }

    public List<Home> getLstHome() {
        return lstHome;
    }

    public void setLstHome(List<Home> lstHome) {
        this.lstHome = lstHome;
    }

    public List<Room> getLstRoom() {
        return lstRoom;
    }

    public void setLstRoom(List<Room> lstRoom) {
        this.lstRoom = lstRoom;
    }

    public BillServiceImpl getBillServiceImpl() {
        return billServiceImpl;
    }

    public void setBillServiceImpl(BillServiceImpl billServiceImpl) {
        this.billServiceImpl = billServiceImpl;
    }

    public GenericDaoImplNewV2<BillService, Long> getBillServiceService() {
        return billServiceService;
    }

    public void setBillServiceService(GenericDaoImplNewV2<BillService, Long> billServiceService) {
        this.billServiceService = billServiceService;
    }

    public GenericDaoImplNewV2<Contract, Long> getContractService() {
        return contractService;
    }

    public void setContractService(GenericDaoImplNewV2<Contract, Long> contractService) {
        this.contractService = contractService;
    }

    public GenericDaoImplNewV2<ContractService, Long> getContractServiceService() {
        return contractServiceService;
    }

    public void setContractServiceService(GenericDaoImplNewV2<ContractService, Long> contractServiceService) {
        this.contractServiceService = contractServiceService;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<BillService> getLstBillService() {
        return lstBillService;
    }

    public void setLstBillService(List<BillService> lstBillService) {
        this.lstBillService = lstBillService;
    }

    public Map<Long, CatItemBO> getMapUnit() {
        return mapUnit;
    }

    public void setMapUnit(Map<Long, CatItemBO> mapUnit) {
        this.mapUnit = mapUnit;
    }

    public BillService getCurrBillService() {
        return currBillService;
    }

    public void setCurrBillService(BillService currBillService) {
        this.currBillService = currBillService;
    }

    public String getPathRealFile() {
        return pathRealFile;
    }

    public void setPathRealFile(String pathRealFile) {
        this.pathRealFile = pathRealFile;
    }

    public StreamedContent getFileExported() {
        return fileExported;
    }

    public void setFileExported(StreamedContent fileExported) {
        this.fileExported = fileExported;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

//</editor-fold>
    //bo sung
    private Date selectedDate;

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void decrementMonth() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(this.selectedDate);
        c.add(Calendar.MONTH, -1);
        //... business logic to update date dependent data
        selectedDate = c.getTime();
    }

    public void incrementMonth() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(this.selectedDate);
        c.add(Calendar.MONTH, 1);

        //... business logic to update date dependent data
        selectedDate = c.getTime();
    }
}
