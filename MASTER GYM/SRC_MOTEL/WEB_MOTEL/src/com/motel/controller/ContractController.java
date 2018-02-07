/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.Contract;
import com.motel.model.ContractService;
import com.motel.model.Customer;
import com.motel.model.CustomerRoom;
import com.motel.model.Home;
import com.motel.model.Room;
import com.motel.model.Service;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.CustomerRoomServiceImpl;
import com.slook.persistence.CustomerServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.RoomServiceImpl;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import static org.omnifaces.util.Faces.getRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author VietNV Jan 21, 2018
 */
@ManagedBean
@ViewScoped
public class ContractController {

    protected static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private GenericDaoImplNewV2<Contract, Long> contractService;
    private GenericDaoImplNewV2<ContractService, Long> contractServiceService;
    private GenericDaoImplNewV2<Service, Long> serviceService;
    LazyDataModel<Contract> lazyDataModel;
    Contract currContract = new Contract();
    private String oldObjectStr = null;
    private Long groupUserId;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<Home> lstHome;
    List<Room> lstRoom;
    List<Service> lstService;
    List<Customer> lstCustomers;

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {
        contractService = new GenericDaoImplNewV2<Contract, Long>() {
        };
        contractServiceService = new GenericDaoImplNewV2<ContractService, Long>() {
        };
        serviceService = new GenericDaoImplNewV2<Service, Long>() {
        };
        try {
            CatUser catUser = null;
            groupUserId = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("home.homeName", Constant.ORDER.ASC);
            order.put("contractCode", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("home.groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<Contract, Long>(contractService, filter, order);

            //init data
            Map<String, Object> filtersHome = new HashMap<>();
            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filtersHome.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("homeName", Constant.ORDER.ASC);
            lstHome = HomeServiceImpl.getInstance().findList(filtersHome, orderHome);

            LinkedHashMap<String, String> orderService = new LinkedHashMap<>();
            orderService.put("serviceName", Constant.ORDER.ASC);
            lstService = serviceService.findList(filtersHome, orderService);

            LinkedHashMap<String, String> orderCustomer = new LinkedHashMap<>();
            orderCustomer.put("customerName", Constant.ORDER.ASC);
            lstCustomers = CustomerServiceImpl.getInstance().findList(filtersHome, orderCustomer);

        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                false, false, true, true, true, false, false, false
        );
        RequestContext.getCurrentInstance().execute("PF('tableContractWidget').filter();");

    }

    public void onChangeHome() {
        if (currContract != null && currContract.getHomeId() != null && currContract.getHomeId() > 0) {
            Map<String, Object> filtersRoom = new HashMap<>();
            filtersRoom.put("status-NEQ", Constant.ROOM_STATUS.DELETE);
            filtersRoom.put("homeId", currContract.getHomeId());
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

    public void preAdd() {
        this.currContract = new Contract();
        isEdit = false;
        List<Long> lstServiceId = new ArrayList<>();
        //set lst service default
        if (lstService != null) {
            for (Service bo : lstService) {
                if (Constant.CONTRACT_SERVICE.DEFAULT_STATUS_ON.equals(bo.getDefaultStatus())) {
                    lstServiceId.add(bo.getServiceId());
                }
            }
        }
        currContract.setLstServiceId(lstServiceId);
        currContract.createContractCode(groupUserId);//tao ma hop dong tu dong
        if (lstHome != null && !lstHome.isEmpty()) {
            currContract.setHomeId(lstHome.get(0).getHomeId());
        }
        onChangeHome();
    }

    public void preEdit(Contract contract) {
        try {
            currContract = contractService.findById(contract.getContractId());
            List<Long> lstServiceId = new ArrayList<>();
            if (currContract != null && currContract.getContractServiceList() != null) {
                for (ContractService bo : currContract.getContractServiceList()) {
                    if (bo.getService() != null) {
                        lstServiceId.add(bo.getService().getServiceId());
                    }
                }
            }
            currContract.setLstServiceId(lstServiceId);
            onChangeHome();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currContract.toString();
    }

    public void onSaveOrUpdate() {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-EQ", Constant.STATUS.ACTIVE);
            filter.put("roomId", currContract.getRoomId());
            if (currContract.getContractId() != null) {
                filter.put("contractId-NEQ", currContract.getContractId());
            }
            List lstCheckRoom = contractService.findList(filter);
            if (lstCheckRoom != null && !lstCheckRoom.isEmpty()) {
                MessageUtil.setErrorMessage("Phòng đã có người thuê!");

                return;
            }

            if (currContract.getStatus() == null) {
                currContract.setStatus(Constant.STATUS.ACTIVE);
            }

            contractService.saveOrUpdate(currContract);
            //xu ly lst dich vu
            List<ContractService> lstSelect = new ArrayList<>();
            List<ContractService> lstDel = new ArrayList<>();
            List<ContractService> lstOld = currContract.getContractServiceList();

            //create new
            for (Object o : currContract.getLstServiceId()) {
                Long serviceId = null;// xu ly cho p:selectManyCheckbox
                if (o instanceof String) {
                    serviceId = Long.valueOf((String) o);
                } else {
                    serviceId = (Long) o;
                }
                lstSelect.add(new ContractService(serviceId, currContract.getContractId()));
            }
            //lay ds xoa
            if (lstOld != null) {
                for (ContractService bo : lstOld) {
                    if (lstSelect.contains(bo)) {
                        lstSelect.get(lstSelect.indexOf(bo))
                                .setContractServiceId(bo.getContractServiceId());
                    } else {
                        lstDel.add(bo);
                    }
                }
            }

            //cap nhat lst service
            contractServiceService.delete(lstDel);
            contractServiceService.saveOrUpdate(lstSelect);
            // xu ly thong tin phong, khach hang
            processCustomerRoom();
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currContract.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currContract.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('contractDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void processCustomerRoom() throws Exception {
        //cap nhat lai trang thai phong
        if (currContract.getRoomId() != null && currContract.getRoomId() > 0) {
            Room room = RoomServiceImpl.getInstance().findById(currContract.getRoomId());
            room.setStatus(Constant.ROOM_STATUS.USE);
            RoomServiceImpl.getInstance().saveOrUpdate(room);
            // khoi tao khach hang cho phong

            Map<String, Object> filterCustomerRoom = new HashMap<>();
            filterCustomerRoom.put("status-NEQ", Constant.STATUS.DELETE);
            filterCustomerRoom.put("roomId", currContract.getRoomId());
            filterCustomerRoom.put("customerId", currContract.getCustomerId());
            List lstCheckRoom = CustomerRoomServiceImpl.getInstance().findList(filterCustomerRoom);
            if (lstCheckRoom == null || lstCheckRoom.isEmpty()) {
                CustomerRoom c = new CustomerRoom();
                c.setCreateTime(new Date());
                c.setCustomerId(currContract.getCustomerId());
                c.setRoomId(currContract.getRoomId());
                c.setStatus(Constant.STATUS.ACTIVE);
                c.setType(Constant.CUSTOMER_ROOM.TYPE_PRIMARY);
                c.setContractId(currContract.getContractId());
                CustomerRoomServiceImpl.getInstance().save(c);
            }
        }

    }

    public void processCustomerRoomOffContract(Contract currContract) throws Exception {
        //cap nhat lai trang thai phong
        if (currContract.getRoomId() != null && currContract.getRoomId() > 0) {
            Room room = RoomServiceImpl.getInstance().findById(currContract.getRoomId());
            if (Constant.ROOM_STATUS.USE.equals(room.getStatus())) {
                room.setStatus(Constant.ROOM_STATUS.FREE);
                RoomServiceImpl.getInstance().saveOrUpdate(room);

                //off toan bo khach hang cua phong
                Map<String, Object> filterCustomerRoom = new HashMap<>();
                filterCustomerRoom.put("status-NEQ", Constant.STATUS.DELETE);
                filterCustomerRoom.put("roomId", currContract.getRoomId());
                List<CustomerRoom> lstCheckRoom = CustomerRoomServiceImpl.getInstance().findList(filterCustomerRoom);
                if (lstCheckRoom != null && !lstCheckRoom.isEmpty()) {
                    for (CustomerRoom c : lstCheckRoom) {
                        c.setEndTime(new Date());
                        c.setStatus(Constant.STATUS.DELETE);
                    }
                    CustomerRoomServiceImpl.getInstance().saveOrUpdate(lstCheckRoom);
                }
            }
        }

    }

    public void onDelete(Contract contract) {
        try {
            Long oldStatus = contract.getStatus();
            if (Constant.CONTRACT.STATUS_ACTIVE.equals(oldStatus)) {
                processCustomerRoomOffContract(contract);
            }
            contract.setStatus(Constant.STATUS.DELETE);
            contractService.saveOrUpdate(contract);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void onOffContract(Contract contract) {
        try {
            Long oldStatus = contract.getStatus();
            if (Constant.CONTRACT.STATUS_ACTIVE.equals(oldStatus)) {
                processCustomerRoomOffContract(contract);
            }
            contract.setStatus(Constant.CONTRACT.STATUS_END);
            contractService.saveOrUpdate(contract);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<Contract> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Contract> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Contract getCurrContract() {
        return currContract;
    }

    public void setCurrContract(Contract currContract) {
        this.currContract = currContract;
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

    public GenericDaoImplNewV2<Service, Long> getServiceService() {
        return serviceService;
    }

    public void setServiceService(GenericDaoImplNewV2<Service, Long> serviceService) {
        this.serviceService = serviceService;
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

    public List<Service> getLstService() {
        return lstService;
    }

    public void setLstService(List<Service> lstService) {
        this.lstService = lstService;
    }

    public List<Customer> getLstCustomers() {
        return lstCustomers;
    }

    public void setLstCustomers(List<Customer> lstCustomers) {
        this.lstCustomers = lstCustomers;
    }

}
