/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatGroupPack;
import com.slook.model.CatItemBO;
import com.slook.model.CatMachine;
import com.slook.model.CatPack;
import com.slook.model.CatPromotion;
import com.slook.model.CatServiceOld;
import com.slook.model.CatUser;
import com.slook.model.CfgWsTimekeeper;
import com.slook.model.Client;
import com.slook.model.ClientPayment;
import com.slook.model.ClientPromotion;
import com.slook.model.ClientUsePack;
import com.slook.model.ClientUsedService;
import com.slook.model.CustomerCheckin;
import com.slook.model.CustomerSchedule;
import com.slook.model.CustomerSchedulePack;
import com.slook.model.V_ClientUsedService;
import com.slook.model.V_CustomerAccessStatus;
import com.slook.object.PaymentPackObj;
import com.slook.object.PrintPaymentForm;
import com.slook.persistence.CfgWsTimekeeperServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.VCustomerAccessStatusServiceImpl;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.ExcelWriterUtils;
import com.slook.util.HibernateUtil;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import static org.omnifaces.util.Faces.getRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

/**
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class ClientController
{

    private static final Logger logger = getLogger(ClientController.class);

    LazyDataModel<Client> lazyDataModel;
    Client selectedClient;
    Client curClient;
    private GenericDaoServiceNewV2<ClientUsePack, Long> clientUsePackService;
    private GenericDaoServiceNewV2<ClientPayment, Long> clientPaymentService;
    private GenericDaoServiceNewV2<ClientPromotion, Long> clientPromotionService;
    private GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService;
    private GenericDaoServiceNewV2<V_ClientUsedService, Long> vClientUsedServiceService;
    private GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService;

    @ManagedProperty(value = "#{cfgWsTimekeeperService}")
    private CfgWsTimekeeperServiceImpl cfgWsTimekeeperService;

    private List<CatPromotion> lstCatPromotions;
    private List<CatPromotion> lstCatPromotionsFull;
    private List<CatItemBO> lstReason;
    private GenericDaoServiceNewV2<Client, Long> clientService;
    private String oldObjectStr = null;
    private String oldObjectClientUsePackStr = null;
    private boolean isEdit = false;

    private LazyDataModel<V_ClientUsedService> lazyVClientUsedServices;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private List<ClientPayment> lstClientPaymentUsingPack = new ArrayList<>();

    StreamedContent fileExported;
    String desPathImg = "";
    private boolean isAddUsePack = false;
    LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel;

    String billImgPath;

    @PostConstruct
    public void onStart()
    {
        catGroupPackService = new GenericDaoImplNewV2<CatGroupPack, Long>()
        {
        };
        clientService = new GenericDaoImplNewV2<Client, Long>()
        {
        };
        clientUsePackService = new GenericDaoImplNewV2<ClientUsePack, Long>()
        {
        };
        clientPaymentService = new GenericDaoImplNewV2<ClientPayment, Long>()
        {
        };
        clientPromotionService = new GenericDaoImplNewV2<ClientPromotion, Long>()
        {
        };

        vClientUsedServiceService = new GenericDaoImplNewV2<V_ClientUsedService, Long>()
        {
        };
        customerCheckinService = new GenericDaoImplNewV2<CustomerCheckin, Long>()
        {
        };

        Map<String, Object> filterDefault = new HashMap<String, Object>();
        filterDefault.put("status-NEQ", -1l);//xoa
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("startTime", Constant.ORDER.DESC);
        lazyDataModel = new LazyDataModelBase<>(clientService, filterDefault, order);
        curClient = new Client();
        removeTempImg();

        lstReason = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.REASON_DEBT, "name");
        lstReason.remove(0);
        Map<String, Object> filterCatPromotion = new HashMap();
        filterCatPromotion.put("status", 1l);
        try
        {
            lstCatPromotionsFull = (new GenericDaoImplNewV2<CatPromotion, Long>()
            {
            }).findList(filterCatPromotion);
            lstCatPromotions = new ArrayList<>(lstCatPromotionsFull);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, false, true,
                false, true, false, true, true);
    }

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    private void removeTempImg()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("previewProfile");
    }

    //    public void preInsert() {
//        this.selectedClient = new Client();
//        this.isEdit = false;
//        oldObjectStr = null;
//    }
//
//    public void preEdit(Client client) {
//        this.selectedClient = client;
//        this.isEdit = true;
//
//        oldObjectStr = client.toString();
//    }
//    public void onDelete(Client client) {
//        try {
//            clientService.delete(client);
//            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, selectedClient.toString(), null, this.getClass().getSimpleName(),
//                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//            MessageUtil.setInfoMessageFromRes("info.delete.success");
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
//
//        }
//    }
    /*  
    public void onSaveOrUpdate() {
        try {
            if (selectedClient == null) {
                return;
            }

            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("cardCode-EXAC_IGNORE_CASE", selectedClient.getCardCode());
            filters.put("status", Constant.CLIENT_STATUS.ACTIVE);
            if (selectedClient.getClientId() != null) {
                filters.put("id-NEQ", selectedClient.getClientId());
            }
            List<Client> lst = clientService.findList(filters);
            if (!lst.isEmpty() && lst.size() > 0) {
                MessageUtil.setErrorMessage(MessageUtil.getResourceBundleMessage("client.cardCode.usingNow"));
                return;
            }

            clientService.saveOrUpdate(selectedClient);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, selectedClient.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, selectedClient.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('catItemDlg').hide();PF('widTableCatItem').clearFilters();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("common.message.fail");
            logger.error(e.getMessage(), e);
        }
    }
     */
//////////
    public void preAddClient()
    {
        removeTempImg();
        curClient = new Client();
        curClient.setSex(MessageUtil.getKey("view.label.sexMale"));
        curClient.setStartTime(new Date());
        Date endDate = new Date();
        endDate.setDate(endDate.getDate() + 1);
        curClient.setEndTime(endDate);
        curClient.setStatus(Constant.CLIENT_STATUS.ACTIVE);
        //set chi nhanh mac dinh theo chi nhanh cua nhan vien dang nhap
        CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
        if (user != null && user.getEmployee() != null)
        {
            curClient.setBranchId(user.getEmployee().getBranchId());
            //
            curClient.setEmployee(user.getEmployee());

        }
        curClient.setTimesUsed(1L);
        preAddClientUsePack();
        oldObjectStr = null;
        isEdit = false;

    }

    public void preEditClient(Client client)
    {
        try
        {
            curClient = clientService.findById(client.getClientId());
            oldObjectStr = client.toString();

            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("clientId", client.getClientId());
            filterPay.put("status-NEQ", Constant.MEMBER_STATUS.DELETE);
            LinkedHashMap<String, String> orderPay = new LinkedHashMap<>();
            orderPay.put("createTime", Constant.ORDER.DESC);

            Map<String, Object> sqlRes = new HashMap<>();
            String hql = "(  this_.client_Use_Pack_Id  is null or this_.client_Use_Pack_Id in (select CLIENT_USE_PACK_ID from CLIENT_USE_PACK where status!=-1 and CLIENT_ID= ?) )";
            sqlRes.put(hql, curClient.getClientId());

            List<ClientPayment> lstPay = clientPaymentService.findList(filterPay, sqlRes, orderPay);
            curClient.setClientPayments(lstPay);
            LinkedHashMap<String, String> orderUS = new LinkedHashMap<>();
            orderUS.put("joinDate", Constant.ORDER.DESC);
            List<ClientUsePack> lstClientUsePacks = clientUsePackService.findList(filterPay, orderUS);
            curClient.setClientUsePacks(lstClientUsePacks);

            LinkedHashMap<String, String> orderP = new LinkedHashMap<>();
            orderP.put("createDate", Constant.ORDER.DESC);
            List<ClientPromotion> lstClientPromotions = clientPromotionService.findList(filterPay, orderP);
            curClient.setClientPromotions(lstClientPromotions);

            // lay tong cong no
            computinDebt();

            Map<String, Object> filterV = new HashMap<String, Object>();
            filterV.put("clientId", client.getClientId());//xoa
            LinkedHashMap<String, String> orderV = new LinkedHashMap<>();
            orderV.put("endDate", Constant.ORDER.DESC);
            orderV.put("serviceName", Constant.ORDER.ASC);
            lazyVClientUsedServices = new LazyDataModelBase<>(vClientUsedServiceService, filterV, orderV);

            preAddClientUsePack();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        removeTempImg();
        isEdit = true;
    }

    public void saveOrUpdateClientUsePack()
    {
        try
        {
            ClientUsePack newClientUsePack = curClient.getNewClientUsePack();
            newClientUsePack.setClientId(curClient.getClientId());
            newClientUsePack.setStatus(1L);
            clientUsePackService.saveOrUpdate(newClientUsePack);
            curClient = clientService.findById(curClient.getClientId());
            //ghi log
            if (oldObjectClientUsePackStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectClientUsePackStr, newClientUsePack.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectClientUsePackStr, newClientUsePack.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteClientUsePack(ClientUsePack clientship)
    {
        try
        {
            clientship.setStatus(Constant.MEMBER_STATUS.DELETE);
            clientUsePackService.saveOrUpdate(clientship);
//            clientUsePackService.delete(clientship);
            //set lai cho may cham cong
            updateAccessCatMachine(curClient, clientship, Constant.METHOD.DELETE);
            curClient = clientService.findById(curClient.getClientId());
            preEditClient(curClient);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, clientship.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            deleteCustomerSchedulePack(clientship);//xoa bang lien quan
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(Client client)
    {
        try
        {
            String old = client.toString();
            client.setStatus(Constant.MEMBER_STATUS.DELETE);

            clientService.saveOrUpdate(client);

            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);

            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(Constant.METHOD.DELETE);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : list)
            {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("CLIENT_" + client.getClientId() + "|" + client.getName() + "|" + client.getCardCode() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
            }
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, client.getCardCode(), old, client.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
        }
    }

    public void saveOrUpdate()
    {
        try
        {
            if (curClient == null)
            {
                return;
            }

            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("cardCode-EXAC_IGNORE_CASE", curClient.getCardCode());
            filters.put("status", Constant.CLIENT_STATUS.ACTIVE);
            if (curClient.getClientId() != null)
            {
                filters.put("clientId-NEQ", curClient.getClientId());
            }
            List<Client> lst = clientService.findList(filters);
            if (!lst.isEmpty() && lst.size() > 0)
            {
                MessageUtil.setErrorMessage(MessageUtil.getResourceBundleMessage("client.cardCode.usingNow"));
                return;
            }

            if (Constant.CLIENT_STATUS.STOP.equals(curClient.getStatus()))
            {
                curClient.setRealEndTime(new Date());
            }
            if (curClient.getEmployee() != null)
            {
                curClient.setEmployeeId(curClient.getEmployee().getEmployeeId());
            }
            clientService.saveOrUpdate(curClient);

            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);

            // Them ban ghi vao bang cfg_ws_timekeeper de ws c# biet la can them user
            /*
            if (!isEdit) {
                CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                cfg.setInsertTime(new Date());
                cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                cfg.setMethod(Constant.METHOD.INSERT);
                cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                for (CatMachine catMachine : list) {
                    // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                    cfg.setContent("CLIENT_" + curClient.getClientId() + "|" + curClient.getName() + "|" + curClient.getCardCode() + "|1|" + catMachine.getIp());
                    cfgWsTimekeeperService.save(cfg);
                }
            } else {
                CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                cfg.setInsertTime(new Date());
                cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                cfg.setMethod(Constant.METHOD.UPDATE);
                cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                for (CatMachine catMachine : list) {
                    // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                    cfg.setContent("CLIENT_" + curClient.getClientId() + "|" + curClient.getName() + "|" + curClient.getCardCode() + "|1|" + catMachine.getIp());
                    cfgWsTimekeeperService.save(cfg);
                }
            }
             */
            //xoa khi cap nhat off
            if (isEdit && Constant.CLIENT_STATUS.STOP.equals(curClient.getStatus()))
            {
                CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                cfg.setInsertTime(new Date());
                cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                cfg.setMethod(Constant.METHOD.DELETE);
                cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                for (CatMachine catMachine : list)
                {
                    // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                    cfg.setContent("CLIENT_" + curClient.getClientId() + "|" + curClient.getName() + "|" + curClient.getCardCode() + "|1|" + catMachine.getIp());
                    cfgWsTimekeeperService.save(cfg);
                }
            }
            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, curClient.getCardCode(), oldObjectStr, curClient.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, curClient.getCardCode(), oldObjectStr, curClient.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            if (!isEdit)
            {
                RequestContext.getCurrentInstance().execute("PF('tabViewClientInfo').select(1)");
            }
            else
            {
                RequestContext.getCurrentInstance().execute("PF('clientInfoDlg').hide();");
            }
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void paymentClientUsePack()
    {
        try
        {
            //validate
            if (curClient.getClientPayment().getDebt() < 0 && StringUtil.isNullOrEmpty(curClient.getClientPayment().getReason()))
            {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("memberPayment.reason")));
                return;
            }

            ClientUsePack newClientUsePack = curClient.getNewClientUsePack();
            ClientPayment clientPayment = curClient.getClientPayment();
            CatPromotion catPromotion = clientPayment.getCatPromotion();
            //km them thoi gian
            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType())
                    && newClientUsePack.getEndDate() != null && catPromotion.getValue() != null)
            {
                Date date = newClientUsePack.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, catPromotion.getValue().intValue());
                Date endate = calendar.getTime();
                newClientUsePack.setEndDate(endate);
            }

            activeCustomerSchedule(newClientUsePack);

            newClientUsePack.setClientId(curClient.getClientId());
            newClientUsePack.setStatus(1L);
            // KIEM TRA NEU THE SU DUNG LAN DAU TRONG NGAY THI OFF TAT CAC O CAC MAY
            if (Constant.CLIENT_STATUS.ACTIVE.equals(curClient.getStatus()))
            {
                MemberController.initCheckOutAll(Constant.CUSTOMER_CHECKIN.TYPE_CLIENT, curClient.getClientId(), curClient.getCardCode());
            }

            //cap nhat may quet
            updateAccessCatMachine(curClient, newClientUsePack, Constant.METHOD.INSERT);

            clientUsePackService.saveOrUpdate(newClientUsePack);
            //clientPayment
            clientPayment.setClientId(curClient.getClientId());
            clientPayment.setClientUsePackId(newClientUsePack.getClientUsePackId());
            if (catPromotion != null)
            {
                clientPayment.setCatPromotionId(catPromotion.getCatPromotionId());
            }
            if (getRequest().getSession().getAttribute("user") != null)
            {
                clientPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị am 
            if (clientPayment.getDebt() > 0)
            {
                clientPayment.setDebt(0l);
                clientPayment.setGuestDeposit(clientPayment.getPrice());
            }
            else
            {
                clientPayment.setGuestDeposit(clientPayment.getPaymentValue());

            }
            clientPayment.setCreateTime(new Date());
            clientPayment.setType(Constant.CLIENT_PAYMENT.TYPE_DEPOSIT);//dat coc
            clientPayment.setStatus(Constant.CLIENT_PAYMENT.STATUS_USED);
            clientPayment.setTimesUsed(curClient.getTimesUsed());
            clientPayment.setStatusTimeUsed(1l);
            clientPaymentService.saveOrUpdate(clientPayment);
            //clientPromotion
            if (catPromotion != null && catPromotion.getCatPromotionId() != null)
            {

                ClientPromotion clientPromotion = new ClientPromotion();
                clientPromotion.setClientId(curClient.getClientId());
                clientPromotion.setCatPromotionId(curClient.getClientPayment().getCatPromotion().getCatPromotionId());
                clientPromotion.setGroupPackId(newClientUsePack.getGroupPackId());
                String desc = catPromotion.getTypeName() + ": " + catPromotion.getValue()
                        + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                        ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "VNĐ" : "");
                clientPromotion.setDescription(desc);
                clientPromotion.setValue(catPromotion.getValue());
                clientPromotion.setCreateDate(new Date());
                clientPromotion.setClientUsePackId(newClientUsePack.getClientUsePackId());
                clientPromotionService.saveOrUpdate(clientPromotion);
            }

            //them thong tin user using service
            CatGroupPack catGroupPack = newClientUsePack.getGroupPack();
            List<ClientUsedService> lstMemberUsedServices = new ArrayList<ClientUsedService>();
//            List<CatService> lstCatServices = new ArrayList<>();
            try
            {
                List<CatPack> lstCatPacks = catGroupPack.getPacks();
                if (lstCatPacks != null)
                {
                    for (CatPack bo : lstCatPacks)
                    {
                        if (bo != null && bo.getCatService() != null)
                        {
                            CatServiceOld boService = bo.getCatService();

                            ClientUsedService mus = new ClientUsedService();
                            mus.setCreateTime(new Date());
                            mus.setClientId(curClient.getClientId());
                            mus.setServiceId(boService.getServiceId());
                            mus.setMembershipId(newClientUsePack.getClientUsePackId());
                            mus.setStatus(1l);
                            mus.setStartDate(newClientUsePack.getJoinDate());
                            mus.setEndDate(newClientUsePack.getEndDate());
                            mus.setAvailable(bo.getNumberOfTime());
                            mus.setTotalNumber(bo.getNumberOfTime());
                            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                                    && mus.getAvailable() != null)
                            {
                                mus.setAvailable(mus.getAvailable() + Math.round(catPromotion.getValue()));
                                mus.setTotalNumber(mus.getAvailable() + Math.round(catPromotion.getValue()));
                            }
                            lstMemberUsedServices.add(mus);
                        }
                    }
                }
                //tao user using service

                GenericDaoImplNewV2 musService = new GenericDaoImplNewV2<ClientUsedService, Long>()
                {
                };
                musService.saveOrUpdate(lstMemberUsedServices);
                //tao thong tin check in
                CustomerCheckin customerCheckin = new CustomerCheckin();
                customerCheckin.setMembershipId(newClientUsePack.getClientUsePackId());
                customerCheckin.setCustomerId(curClient.getClientId());
                customerCheckin.setCardCode(curClient.getCardCode());
                customerCheckin.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
                customerCheckin.setType(Constant.CUSTOMER_CHECKIN.TYPE_CLIENT);
                customerCheckin.setCheckTime(new Date());
                customerCheckinService.saveOrUpdate(customerCheckin);

                //log
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, lstMemberUsedServices.toArray().toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));

//                                Map<String,Object> filterMus=new HashMap();
//                filterMus.put("memberId", newClientUsePack.getMemberId());
//                filterMus.put("membershipId", newClientUsePack.getMembershipId());
//                filterMus.put("status", 1l);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            curClient = clientService.findById(curClient.getClientId());
            //ghi log
            if (oldObjectClientUsePackStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectClientUsePackStr, newClientUsePack.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectClientUsePackStr, newClientUsePack.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, clientPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('clientPaymentDlg').hide();");
            //clear form
            preEditClient(curClient);
            preAddClientUsePack();
            // show result
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL")))
            {

                showResultAccessStatus(curClient.getCardCode());
                RequestContext.getCurrentInstance().execute("PF('clientCheckinResultDlg').show();PF('tableClientCustomerAccessStatusWidget').filter();");
                RequestContext.getCurrentInstance().update("@widgetVar('clientCheckinResultDlg')");
            }

        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void computingPrice()
    {
        try
        {
//            clientshipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;
            //set gia tri mac dinh neu la them moi
            curClient.setClientPayment(new ClientPayment());
            curClient.setClientPromotion(new ClientPromotion());
            curClient.getClientPayment().createPaymentCode(curClient.getBranchId());

//        curClient.setStartTime(new Date());
            oldObjectClientUsePackStr = null;

            if (curClient.getNewClientUsePack().getGroupPackId() == null)
            {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("datatable.header.group.pack.name")));
                return;
            }
            CatGroupPack catGroupPack = catGroupPackService.findById(curClient.getNewClientUsePack().getGroupPackId());
            curClient.getNewClientUsePack().setGroupPack(catGroupPack);
            Long price = Math.round(catGroupPack.getPrice());
            if (curClient.getNewClientUsePack().getNumberPack() > 1)
            {
                price *= curClient.getNewClientUsePack().getNumberPack();
            }
            curClient.getClientPayment().setPrice(price);
            if (StringUtil.isNullOrEmpty(curClient.getCardCode()))
            {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("client.cardCode")));
            }
            else
            {
                RequestContext.getCurrentInstance().execute("PF('clientPaymentDlg').show();");
            }

            try
            {
                //set lai danh sach theo so luong goi
                lstCatPromotions = new ArrayList<>();
                for (CatPromotion bo : lstCatPromotionsFull)
                {
                    if (StringUtil.isNullOrEmpty(bo.getOperator()) || StringUtil.isNullOrEmpty(bo.getValueCompare())
                            || CommonUtil.compareValue(String.valueOf(curClient.getNewClientUsePack().getNumberPack()), bo.getOperator(), bo.getValueCompare()))
                    {
                        lstCatPromotions.add(bo);
                    }
                    ;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
// them dich vu khi dang su dung

    public void preAddUsePack()
    {
        try
        {
            computingPrice();
            curClient.getClientPayment().setPaymentValue(0l);
            onChangeComputingPrice();
            curClient.getClientPayment().setReason(((Map<String, String>) CommonUtil.convertListToMap((List) lstReason, "code", "name")).get(Constant.CAT_ITEM.REASON_DEBT.USING_SERVICE));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onChangeComputingPrice()
    {
        try
        {
            CatGroupPack catGroupPack = curClient.getNewClientUsePack().getGroupPack();
            Double price = catGroupPack.getPrice();
            if (curClient.getNewClientUsePack().getNumberPack() > 1)
            {
                price *= curClient.getNewClientUsePack().getNumberPack();
            }
            CatPromotion catPromotion = curClient.getClientPayment().getCatPromotion();
            if (catPromotion != null && catPromotion.getValue() != null)
            {
                if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()))
                {
                    price = price - catPromotion.getValue();
                }
                else if (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType()))
                {
                    price = price - price * catPromotion.getValue() / 100;
                }
            }
            if (curClient.getClientPayment().getIsVAT())
            {
                price = price * 1.1;
            }
            curClient.getClientPayment().setPrice(Math.round(price));

            if (curClient.getClientPayment().getPaymentValue() != null)
            {
                curClient.getClientPayment().setDebt(curClient.getClientPayment().getPaymentValue() - curClient.getClientPayment().getPrice());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void computinDebt()
    {
        // lay tong cong no
        // goi huy van cong voi ban ghi tra lai tien cua khach
//        String sqlDebt = "select sum(debt) debt from client_payment where client_id=:clientId and ( status=" + Constant.CLIENT_PAYMENT.STATUS_USED + " or status=" + Constant.CLIENT_PAYMENT.STATUS_NOT_USE + " )";
        String sqlDebt = "select sum(case when ( b.status is null or b.status!=-1 )"
                + " and (a.client_use_pack_id is null or a.client_use_pack_id in ( select CLIENT_USE_PACK_ID from CLIENT_USE_PACK where status!=-1 and CLIENT_ID=:clientId ) ) "
                + " then a.debt else 0 end) debt from client_payment a"
                + " left join client_use_pack b on a.client_use_pack_id=b.client_use_pack_id \n"
                + "   where a.client_id=:clientId and ( a.status=" + Constant.CLIENT_PAYMENT.STATUS_USED + " )";
        Session session = null;
        try
        {
            session = HibernateUtil.openSession();
            SQLQuery sQLQuery = session.createSQLQuery(sqlDebt);
            sQLQuery.setParameter("clientId", curClient.getClientId());
            BigDecimal res = (BigDecimal) sQLQuery.list().get(0);
            if (res != null)
            {
                Long debt = ((BigDecimal) sQLQuery.list().get(0)).longValue();
                curClient.setTotalPayment(debt);
            }
            else
            {
                curClient.setTotalPayment(0l);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (session != null)
            {
                session.close();
            }
        }

    }

    public void preAddLiquidate()
    {
        //set gia tri mac dinh
        curClient.setClientPayment(new ClientPayment());
        curClient.getClientPayment().createPaymentCode(curClient.getBranchId());
        curClient.getClientPayment().setPrice(curClient.getTotalPayment());
        oldObjectClientUsePackStr = null;
    }

    public void onChangeComputingLiquidate()
    {
        try
        {
            if (curClient.getClientPayment().getPaymentValue() != null)
            {
                curClient.getClientPayment().setDebt(curClient.getClientPayment().getPaymentValue() + curClient.getClientPayment().getPrice());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void liquidate()
    {
        try
        {
            //clientPayment
            ClientPayment clientPayment = curClient.getClientPayment();
            clientPayment.setClientId(curClient.getClientId());

//            if (clientPayment.getCatPromotion() != null) {
//                clientPayment.setCatPromotionId(clientPayment.getCatPromotion().getCatPromotionId());
//            }
            if (getRequest().getSession().getAttribute("user") != null)
            {
                clientPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị duong la phan thu tien cua khach hang
            if (clientPayment.getPaymentValue() <= -clientPayment.getPrice())
            {
                clientPayment.setDebt(clientPayment.getPaymentValue());
                clientPayment.setGuestDeposit(clientPayment.getPaymentValue());
            }
            else
            {
                clientPayment.setDebt(-clientPayment.getPrice());
                clientPayment.setGuestDeposit(-clientPayment.getPrice());
            }
            //set lai gia tien bang 0 vi day la tra no
            clientPayment.setPrice(0l);
            clientPayment.setCreateTime(new Date());
            clientPayment.setType(2L);

            clientPaymentService.saveOrUpdate(clientPayment);

//            curClient = clientService.findById(curClient.getClientId());
            //ghi log
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, clientPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('clientLiquidateDlg').hide();");
            //clear form
            preAddClientUsePack();

            preEditClient(curClient);
//            computinDebt();
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onChangeComputingClientPaymentFinal()
    {
        try
        {
            if (curClient.getClientPayment().getPaymentValue() != null)
            {
                curClient.getClientPayment().setDebt(curClient.getClientPayment().getPaymentValue()
                        + curClient.getClientPayment().getTotalDeposit() - curClient.getClientPayment().getPrice());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void preAddClientPaymentFinal()
    {
        //set gia tri mac dinh
        curClient.setClientPayment(new ClientPayment());
        curClient.getClientPayment().createPaymentCode(curClient.getBranchId());
        oldObjectClientUsePackStr = null;

        //tong tien dat coc
        /*String sqlTotalDesposit = "select sum(GUEST_DEPOSIT) debt from client_payment where client_id=:clientId and status=" + Constant.CLIENT_PAYMENT.STATUS_USED
                + " and type=" + Constant.CLIENT_PAYMENT.TYPE_DEPOSIT;
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            SQLQuery sQLQuery = session.createSQLQuery(sqlTotalDesposit);
            sQLQuery.setParameter("clientId", curClient.getClientId());
            BigDecimal res = (BigDecimal) sQLQuery.list().get(0);
            if (res != null) {
                Long debt = ((BigDecimal) sQLQuery.list().get(0)).longValue();
                curClient.getClientPayment().setTotalDeposit(debt);
            } else {
                curClient.getClientPayment().setTotalDeposit(0l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }*/
        //Lay danh sach thong tin thanh toan cac goi dat coc
        lstClientPaymentUsingPack = new ArrayList();
        if (curClient.getClientPayments() != null && curClient.getClientPayments().size() > 0)
        {
            for (ClientPayment bo : curClient.getClientPayments())
            {
//                if (Constant.CLIENT_PAYMENT.TYPE_DEPOSIT.equals(bo.getType())) {
// bo qua cac goi da xoa
                if (bo.getClientUsePack() != null && Constant.MEMBER_STATUS.DELETE.equals(bo.getClientUsePack().getStatus()))
                {
                    continue;
                }
                lstClientPaymentUsingPack.add(bo);
//                }
            }
        }
        curClient.getClientPayment().setPrice(getTotalFromClientPayment(lstClientPaymentUsingPack, Constant.CLIENT_PAYMENT.FIELD_PRICE));
        curClient.getClientPayment().setTotalDeposit(getTotalFromClientPayment(lstClientPaymentUsingPack, Constant.CLIENT_PAYMENT.FIELD_DEPOSIT));
        if (curClient.getTotalPayment() == null || curClient.getTotalPayment().equals(0l))
        {
            curClient.getClientPayment().setPaymentValue(0l);
            onChangeComputingClientPaymentFinal();
        }
    }

    public void onCellClientPaymentUsingPackEdit(CellEditEvent event)
    {
        Long oldValue = (Long) event.getOldValue();
        Long newValue = (Long) event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue))
        {
            curClient.getClientPayment().setPrice(getTotalFromClientPayment(lstClientPaymentUsingPack, Constant.CLIENT_PAYMENT.FIELD_PRICE));
            onChangeComputingClientPaymentFinal();
        }
    }

    public Long getTotalFromClientPayment(List<ClientPayment> lst, Long type)
    {
        Long res = 0l;
        if (lst != null && lst.size() > 0)
        {
            for (ClientPayment bo : lst)
            {
                if (Constant.CLIENT_PAYMENT.STATUS_USED.equals(bo.getStatus()))
                {
                    if (Constant.CLIENT_PAYMENT.FIELD_PRICE.equals(type)
                            && Constant.CLIENT_PAYMENT.TYPE_DEPOSIT.equals(bo.getType()))
                    {
                        if (bo.getPrice() != null)
                        {
                            res += bo.getPrice();
                        }
                    }
                    if (Constant.CLIENT_PAYMENT.FIELD_DEPOSIT.equals(type))
                    {
                        if (bo.getGuestDeposit() != null)
                        {
                            res += bo.getGuestDeposit();
                        }
                    }
                }
            }
        }
        return res;
    }

    public void clientPaymentFinal()
    {
        try
        {
            //validate
            if (curClient.getClientPayment().getDebt() < 0)
            {
                MessageUtil.setErrorMessage(MessageUtil.getResourceBundleMessage("payment.surplus") + " không được âm");
                return;
            }
            //khoi tao fil export
//            exportClientPayment();
//            exportImageClientPayment();
            onPrintBill();
            //clientPayment
            ClientPayment clientPayment = curClient.getClientPayment();
            clientPayment.setClientId(curClient.getClientId());

            if (getRequest().getSession().getAttribute("user") != null)
            {
                clientPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị duong la phan thu tien cua khach hang

            // cong no moi
            Long debtNew = 0l;
            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    if (bo.getDebt() == null || Constant.CLIENT_PAYMENT.STATUS_NOT_USE.equals(bo.getStatus()))
                    {
                        continue; // bo qua loai thanh toan
                    }
                    debtNew += bo.getDebt();
                }
            }
            if (debtNew < 0)
            {
                clientPayment.setDebt(-debtNew);
            }
            else
            {
                clientPayment.setDebt(0l);
            }
            ///

            if (clientPayment.getPaymentValue() <= (clientPayment.getPrice() - clientPayment.getTotalDeposit()))
            {
//                clientPayment.setDebt(clientPayment.getPaymentValue());
                clientPayment.setGuestDeposit(clientPayment.getPaymentValue());
            }
            else
            {
                /*if (clientPayment.getPrice() <= clientPayment.getTotalDeposit()) {//khach khong su dung dich vu
                    // cong no moi
                    Long debtNew = 0l;
                    if (lstClientPaymentUsingPack != null) {
                        for (ClientPayment bo : lstClientPaymentUsingPack) {
                            if (bo.getDebt() == null || Constant.CLIENT_PAYMENT.STATUS_NOT_USE.equals(bo.getStatus())) {
                                continue; // bo qua loai thanh toan
                            }
                            debtNew += bo.getDebt();
                        }
                    }
                    if (debtNew < 0) {
                        clientPayment.setDebt(-debtNew);
                    } else {
                        clientPayment.setDebt(0l);
                    }
                } else {
                    clientPayment.setDebt(clientPayment.getPrice() - clientPayment.getTotalDeposit());
                }*/
                clientPayment.setGuestDeposit(clientPayment.getPrice() - clientPayment.getTotalDeposit());
            }
            //set lai gia tien bang 0 vi day la tra no
//            clientPayment.setPrice(0l);
            clientPayment.setCreateTime(new Date());
            clientPayment.setType(2L);
            clientPayment.setStatus(Constant.CLIENT_PAYMENT.STATUS_USED);
            clientPaymentService.saveOrUpdate(clientPayment);
            clientPaymentService.saveOrUpdate(lstClientPaymentUsingPack);
          /* tach phan check out
            curClient.setStatus(Constant.CLIENT_STATUS.STOP);
            curClient.setEndTime(new Date());
            clientService.saveOrUpdate(curClient);
            // cap nhat off het goi dang su dung
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("clientId", curClient.getClientId());
            filterPay.put("status", 1l);
            filterPay.put("endDate-GE", new Date());
            Long customerScheduleId = null;
            List<ClientUsePack> lstClientUsePacks = clientUsePackService.findList(filterPay);
            if (lstClientUsePacks != null && lstClientUsePacks.size() > 0) {
                for (ClientUsePack bo : lstClientUsePacks) {
                    bo.setStatus(Constant.CLIENT_USE_PACK.STATUS_STOP);
                    if (bo.getCustomerScheduleId() != null) {
                        customerScheduleId = bo.getCustomerScheduleId();
                    }
                }
            }
            clientUsePackService.saveOrUpdate(lstClientUsePacks);
            completedCustomerSchedule(customerScheduleId);//cap nhat lai lich trinh
            checkout();
            */
            //ghi log
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, clientPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('clientPaymentFinalDlg').hide();");
            //neu thanh cong
//            if (fileExported != null) {
//                RequestContext.getCurrentInstance().update("@widgetVar('clientPaymentFinalDlgExport')");
//                RequestContext.getCurrentInstance().execute("PF('clientPaymentFinalDlgExport').show();");
//            }
            if (billImgPath != null)
            {
                RequestContext.getCurrentInstance().update("@widgetVar('cfImgDlgLocal')");
                RequestContext.getCurrentInstance().execute("PF('cfImgDlgLocal').show();");

            }
            //clear form
//            preAddClientUsePack();

            preEditClient(curClient);
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preAddClientUsePack()
    {
        curClient.setNewClientUsePack(new ClientUsePack());
        curClient.getNewClientUsePack().setNumberPack(1L);
        //set gia tri mac dinh
        curClient.setClientPayment(new ClientPayment());
        curClient.setClientPromotion(new ClientPromotion());
        curClient.getClientPayment().createPaymentCode(curClient.getBranchId());
//        curClient.setStartTime(new Date());
        oldObjectClientUsePackStr = null;
    }

    public void updateEndDate(SelectEvent event)
    {
        try
        {
            Date date = (Date) event.getObject();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(curClient.getNewClientUsePack().getGroupPackId());
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curClient.getNewClientUsePack().setEndDate(endate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateDate(ValueChangeEvent event)
    {
        try
        {
            Long selectId = (Long) event.getNewValue();
            Date date = new Date();
            curClient.getNewClientUsePack().setJoinDate(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(selectId);
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curClient.getNewClientUsePack().setEndDate(endate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateAccessCatMachine(Client client, ClientUsePack clientshipDel, String method)
    {
        Map<String, Object> filterPay = new HashMap<>();
        filterPay.put("clientId", client.getClientId());
        filterPay.put("status", 1l);
        filterPay.put("endDate-GE", new Date());
        List<ClientUsePack> lstClientUsePacks;
        List<CatGroupPack> lstON = new ArrayList<>();
        List<CatGroupPack> lstOFF = new ArrayList<>();
        if (clientshipDel != null && clientshipDel.getGroupPack() != null)
        {
            lstOFF.add(clientshipDel.getGroupPack());
        }
        try
        {
            lstClientUsePacks = clientUsePackService.findList(filterPay);
            if (lstClientUsePacks != null)
            {
                for (ClientUsePack bo : lstClientUsePacks)
                {
                    if (bo.getGroupPack() != null)
                    {
                        lstON.add(bo.getGroupPack());
                    }
                }
            }
            List<CatMachine> lstCatMachineOff = CatGroupPackController.getListMachineOffNotExistInON(lstON, lstOFF);
            List<String> lstIp = new ArrayList<>();

// cap nhat xoa khoi may quet the
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(method);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : lstCatMachineOff)
            {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("CLIENT_" + client.getClientId() + "|" + client.getName() + "|" + client.getCardCode() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
                if (!lstIp.contains(catMachine.getIp()))
                {
                    lstIp.add(catMachine.getIp());
                }
            }

            //goi ws online
            if (Constant.METHOD.INSERT.equals(method))
            {
                CustomerAccessStatusController.callWSUpdateAccess(client.getCardCode(), lstIp, Constant.STATUS.ACTIVE, Constant.WS_C_METHOD.TYPE_CARD);
            }
            else
            {
                CustomerAccessStatusController.callWSUpdateAccess(client.getCardCode(), lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_CARD);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void checkout()
    {
        try
        {
            //khi thuc hien checkout thi checkout toan bo cac ban ghi check in theo the deo cua khach hang cua khach hang
            Map<String, Object> filter = new HashMap<>();
            filter.put("cardCode", curClient.getCardCode());
            filter.put("customerId", curClient.getClientId());
            filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
            filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_CLIENT);
            List<CustomerCheckin> lstCus = customerCheckinService.findList(filter);
            if (lstCus != null && !lstCus.isEmpty())
            {
                for (CustomerCheckin bo : lstCus)
                {
                    bo.setStatus(Constant.CUSTOMER_CHECKIN.CHECKOUT);
                    bo.setCheckoutTime(new Date());
                }
            }
            else
            {
                MessageUtil.setErrorMessage("Không tồn tại thẻ đang sử dụng cho khách hàng!");
                return;
            }
            customerCheckinService.saveOrUpdate(lstCus);
            updateAccessCatMachineOFFAll(curClient, curClient.getCardCode());
            MessageUtil.setInfoMessageFromRes("info.checkout.success");
//            RequestContext.getCurrentInstance().execute("PF('checkoutDlg').hide();");

        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("info.checkout.unsuccess");

            e.printStackTrace();
        }
    }

    public void updateAccessCatMachineOFFAll(Client client, String method)
    {
        Map<String, Object> filterPay = new HashMap<>();
        filterPay.put("clientId", client.getClientId());
        filterPay.put("status", 1l);
//        filterPay.put("endDate-GE", new Date());
        List<ClientUsePack> lstClientUsePacks;
        List<CatGroupPack> lstON = new ArrayList<>();
//        List<CatGroupPack> lstOFF = new ArrayList<>();
//        if (clientshipDel != null && clientshipDel.getGroupPack() != null) {
//            lstOFF.add(clientshipDel.getGroupPack());
//        }
        try
        {
            lstClientUsePacks = clientUsePackService.findList(filterPay);
            if (lstClientUsePacks != null)
            {
                for (ClientUsePack bo : lstClientUsePacks)
                {
                    if (bo.getGroupPack() != null)
                    {
                        lstON.add(bo.getGroupPack());
                    }
                }
            }
            List<CatMachine> lstCatMachineOff = CatGroupPackController.getListMachineOffNotExistInON(new ArrayList<>(), lstON);
            List<String> lstIp = new ArrayList<>();

// cap nhat xoa khoi may quet the
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(method);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : lstCatMachineOff)
            {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("CLIENT_" + client.getClientId() + "|" + client.getName() + "|" + client.getCardCode() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
                if (!lstIp.contains(catMachine.getIp()))
                {
                    lstIp.add(catMachine.getIp());
                }
            }
            CustomerAccessStatusController.callWSUpdateAccess(client.getCardCode(), lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_CARD);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void exportClientPayment()
    {
        FileInputStream file = null;
        Workbook wbTemplate = null;

        try
        {
            //lstClientPaymentUsingPack

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "Template_clientPayment.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "clientPayment.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "clientPayment.xls";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported"))
            {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }

            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            context.putVar("timeNow", new Date());
            context.putVar("branchName", curClient.getBranch().getBranchName());
            context.putVar("clientName", curClient.getName());
            context.putVar("cardCode", curClient.getCardCode());

            JxlsHelper.getInstance().processTemplate(is, os, context);

            //put du lieu
//            file = new FileInputStream(ctx.getRealPath(des));
            file = new FileInputStream((des));

            if (path.endsWith("xls"))
            {
                wbTemplate = new HSSFWorkbook(file);

            }
            else if (path.endsWith("xlsx"))
            {
                wbTemplate = WorkbookFactory.create(file);

            }

            CellStyle cellStyleBorder = wbTemplate.createCellStyle();
            cellStyleBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyleBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyleBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyleBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);
            Sheet referSheet = wbTemplate.getSheetAt(0);

            Row referRow;
            int startRow = 6;
            ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    if (!Constant.CLIENT_PAYMENT.TYPE_DEPOSIT.equals(bo.getType()))
                    {
                        continue;
                    }
                    int colRefer = 0;
                    referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow);
                    referRow.createCell(colRefer++).setCellValue(startRow - 5);
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getPaymentCode());
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    String groupPackName = "";
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null)
                    {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                    }
                    referRow.createCell(colRefer++).setCellValue(groupPackName);
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getPrice() + " VNĐ");
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getStatus());
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue((Constant.CLIENT_PAYMENT.STATUS_USED.equals(bo.getStatus()) ? bo.getPrice() : "") + " VNĐ");
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    startRow++;
                }
            }
            // thong tin tong hop
            startRow++;
            referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow++);
            int colRes = 1;
            referRow.createCell(colRes++).setCellValue("Tổng giá tiền:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getPrice() + " VNĐ");

            referRow.createCell(colRes++).setCellValue("Tiền đã đặt cọc:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getTotalDeposit() + " VNĐ");

            referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow++);
            colRes = 1;

            referRow.createCell(colRes++).setCellValue("Khách nộp thêm:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getPaymentValue() + " VNĐ");
            referRow.createCell(colRes++).setCellValue("Tiền trả lại khách:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getDebt() + " VNĐ");

            OutputStream outputStream = new FileOutputStream(des);
            wbTemplate.write(outputStream);
            outputStream.flush();
            outputStream.close();

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "clientPayment.xls");
//            InputStream d = PdfUtil.excel2pdf(stream);
//            fileExported = new DefaultStreamedContent(d, "application/pdf", "clientPayment.pdf");

            MessageUtil.setInfoMessage("Export thành công");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    //
    public void showResultAccessStatus(String cardCode)
    {

        try
        {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put("cardCode-EXAC_IGNORE_CASE", cardCode);
            lazyDataCustomerAccessModel = new LazyDataModelBase<V_CustomerAccessStatus, Long>(VCustomerAccessStatusServiceImpl.getInstance(), filter, order);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void preActiveClientUsePack(ClientUsePack bo)
    {
        curClient.setNewClientUsePack(bo);

        //set gia tri mac dinh
        curClient.setClientPayment(new ClientPayment());
        curClient.setClientPromotion(new ClientPromotion());
        curClient.getClientPayment().createPaymentCode(curClient.getBranchId());
        oldObjectClientUsePackStr = null;

        try
        {
            Long selectId = bo.getGroupPackId();
            Date date = new Date();
            curClient.getNewClientUsePack().setJoinDate(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(selectId);
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curClient.getNewClientUsePack().setEndDate(endate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteCustomerSchedulePack(ClientUsePack clientship)
    {
        try
        {
            if (Constant.CLIENT_USE_PACK.STATUS_SCHEDULE.equals(clientship.getStatus()))
            {
                GenericDaoImplNewV2 cspService = new GenericDaoImplNewV2<CustomerSchedulePack, Long>()
                {
                };
                Map<String, Object> filter = new HashMap<>();
                filter.put("customerScheduleId", clientship.getCustomerScheduleId());
                filter.put("groupPackId", clientship.getGroupPackId());
                List<CustomerSchedulePack> lst = cspService.findList(filter);
                cspService.delete(lst);
                //set lai cho may cham cong
                LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, clientship.toString(), null, this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            }
//            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (Exception e)
        {
//            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void activeCustomerSchedule(ClientUsePack bo) throws AppException
    {
        if (Constant.CLIENT_STATUS.RESERVE.equals(curClient.getStatus()))
        {
            curClient.setStatus(Constant.CLIENT_STATUS.ACTIVE);
            clientService.saveOrUpdate(curClient);
        }
        if (bo.getCustomerScheduleId() != null && Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE.equals(bo.getStatus()))
        {
            GenericDaoImplNewV2<CustomerSchedule, Long> csService = new GenericDaoImplNewV2<CustomerSchedule, Long>()
            {
            };
            CustomerSchedule c = csService.findById(bo.getCustomerScheduleId());
            c.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_ACTIVE);
            csService.saveOrUpdate(c);
        }
    }

    public void completedCustomerSchedule(Long customerScheduleId)
    {
        try
        {
            if (customerScheduleId != null)
            {
                GenericDaoImplNewV2<CustomerSchedule, Long> csService = new GenericDaoImplNewV2<CustomerSchedule, Long>()
                {
                };
                CustomerSchedule c = csService.findById(customerScheduleId);
                c.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_COMPLETED);
                csService.saveOrUpdate(c);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
//<editor-fold defaultstate="collapsed" desc="get/set">

    public LazyDataModel<V_CustomerAccessStatus> getLazyDataCustomerAccessModel()
    {
        return lazyDataCustomerAccessModel;
    }

    public void setLazyDataCustomerAccessModel(LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel)
    {
        this.lazyDataCustomerAccessModel = lazyDataCustomerAccessModel;
    }

    public boolean getIsAddUsePack()
    {
        return isAddUsePack;
    }

    public void setIsAddUsePack(boolean isAddUsePack)
    {
        this.isAddUsePack = isAddUsePack;
    }

    public StreamedContent getFileExported()
    {
        return fileExported;
    }

    public void setFileExported(StreamedContent fileExported)
    {
        this.fileExported = fileExported;
    }

    public GenericDaoServiceNewV2<CustomerCheckin, Long> getCustomerCheckinService()
    {
        return customerCheckinService;
    }

    public void setCustomerCheckinService(GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService)
    {
        this.customerCheckinService = customerCheckinService;
    }

    public LazyDataModel<Client> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Client> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public Client getSelectedClient()
    {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient)
    {
        this.selectedClient = selectedClient;
    }

    public Client getCurClient()
    {
        return curClient;
    }

    public void setCurClient(Client curClient)
    {
        this.curClient = curClient;
    }

    public GenericDaoServiceNewV2<ClientUsePack, Long> getClientUsePackService()
    {
        return clientUsePackService;
    }

    public void setClientUsePackService(GenericDaoServiceNewV2<ClientUsePack, Long> clientUsePackService)
    {
        this.clientUsePackService = clientUsePackService;
    }

    public GenericDaoServiceNewV2<ClientPayment, Long> getClientPaymentService()
    {
        return clientPaymentService;
    }

    public void setClientPaymentService(GenericDaoServiceNewV2<ClientPayment, Long> clientPaymentService)
    {
        this.clientPaymentService = clientPaymentService;
    }

    public GenericDaoServiceNewV2<ClientPromotion, Long> getClientPromotionService()
    {
        return clientPromotionService;
    }

    public void setClientPromotionService(GenericDaoServiceNewV2<ClientPromotion, Long> clientPromotionService)
    {
        this.clientPromotionService = clientPromotionService;
    }

    public GenericDaoServiceNewV2<CatGroupPack, Long> getCatGroupPackService()
    {
        return catGroupPackService;
    }

    public void setCatGroupPackService(GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService)
    {
        this.catGroupPackService = catGroupPackService;
    }

    public CfgWsTimekeeperServiceImpl getCfgWsTimekeeperService()
    {
        return cfgWsTimekeeperService;
    }

    public void setCfgWsTimekeeperService(CfgWsTimekeeperServiceImpl cfgWsTimekeeperService)
    {
        this.cfgWsTimekeeperService = cfgWsTimekeeperService;
    }

    public List<CatPromotion> getLstCatPromotions()
    {
        return lstCatPromotions;
    }

    public void setLstCatPromotions(List<CatPromotion> lstCatPromotions)
    {
        this.lstCatPromotions = lstCatPromotions;
    }

    public List<CatItemBO> getLstReason()
    {
        return lstReason;
    }

    public void setLstReason(List<CatItemBO> lstReason)
    {
        this.lstReason = lstReason;
    }

    public GenericDaoServiceNewV2<Client, Long> getClientService()
    {
        return clientService;
    }

    public void setClientService(GenericDaoServiceNewV2<Client, Long> clientService)
    {
        this.clientService = clientService;
    }

    public String getOldObjectStr()
    {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr)
    {
        this.oldObjectStr = oldObjectStr;
    }

    public String getOldObjectClientUsePackStr()
    {
        return oldObjectClientUsePackStr;
    }

    public void setOldObjectClientUsePackStr(String oldObjectClientUsePackStr)
    {
        this.oldObjectClientUsePackStr = oldObjectClientUsePackStr;
    }

    public boolean isIsEdit()
    {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
    }

    public GenericDaoServiceNewV2<V_ClientUsedService, Long> getvClientUsedServiceService()
    {
        return vClientUsedServiceService;
    }

    public void setvClientUsedServiceService(GenericDaoServiceNewV2<V_ClientUsedService, Long> vClientUsedServiceService)
    {
        this.vClientUsedServiceService = vClientUsedServiceService;
    }

    public LazyDataModel<V_ClientUsedService> getLazyVClientUsedServices()
    {
        return lazyVClientUsedServices;
    }

    public void setLazyVClientUsedServices(LazyDataModel<V_ClientUsedService> lazyVClientUsedServices)
    {
        this.lazyVClientUsedServices = lazyVClientUsedServices;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public List<ClientPayment> getLstClientPaymentUsingPack()
    {
        return lstClientPaymentUsingPack;
    }

    public void setLstClientPaymentUsingPack(List<ClientPayment> lstClientPaymentUsingPack)
    {
        this.lstClientPaymentUsingPack = lstClientPaymentUsingPack;
    }

    public String getDesPathImg()
    {
        return desPathImg;
    }

    public void setDesPathImg(String desPathImg)
    {
        this.desPathImg = desPathImg;
    }

    //</editor-fold>
    public static void main(String[] args)
    {
        try
        {
            String branchName = "GYM Hoằng Hóa";
            String date = "24/10/2017";
            String billCode = "BILL001";
            String floorCode = "Lễ tân tầng 1";
            String total = "500,000";
            String deposit = "200,000";
            String custPays = "400,000";
            String refun = "100,000";

            List<Map<String, String>> lstPackage = new ArrayList<>();

            Map<String, String> iPackage = new HashMap<>();
            iPackage.put("item", "Tẩy tế bào chết");
            iPackage.put("price", "500,000");
            iPackage.put("num", "1");
            iPackage.put("amount", "500,000");
            lstPackage.add(iPackage);

            iPackage = new HashMap<>();
            iPackage.put("item", "Xông hơi");
            iPackage.put("price", "500,000");
            iPackage.put("num", "1");
            iPackage.put("amount", "500,000");
            lstPackage.add(iPackage);

            iPackage = new HashMap<>();
            iPackage.put("item", "Đấm lưng cho Thành Phạm");
            iPackage.put("price", "500,000");
            iPackage.put("num", "1");
            iPackage.put("amount", "500,000");
            lstPackage.add(iPackage);

            iPackage = new HashMap<>();
            iPackage.put("item", "Vặt lông khách hàng");
            iPackage.put("price", "500,000");
            iPackage.put("num", "1");
            iPackage.put("amount", "500,000");
            lstPackage.add(iPackage);

            iPackage = new HashMap<>();
            iPackage.put("item", "Body 6 múi");
            iPackage.put("price", "500,000");
            iPackage.put("num", "1");
            iPackage.put("amount", "500,000");
            lstPackage.add(iPackage);

            String pathFileMain = "D:\\vietnv\\image_temp\\main_card.jpg";
            BufferedImage im = ImageIO.read(new File(pathFileMain));

            Graphics2D gFullName = im.createGraphics();
            gFullName.setColor(Color.BLACK);

            // Branch name
            int branchNameX = im.getWidth() * 197 / 380;
            int branchNameY = im.getHeight() * 112 / 725;
            gFullName.drawString(new String(branchName), branchNameX, branchNameY);

            // Date
            int dateX = im.getWidth() * 212 / 380;
            int dateY = im.getHeight() * 213 / 725;
            gFullName.drawString(new String(date), dateX, dateY);

            // Bill code
            int billCodeX = im.getWidth() * 212 / 380;
            int billCodeY = im.getHeight() * 231 / 725;
            gFullName.drawString(new String(billCode), billCodeX, billCodeY);

            // Floor code
            int floorCodeX = im.getWidth() * 212 / 380;
            int floorCodeY = im.getHeight() * 250 / 725;
            gFullName.drawString(new String(floorCode), floorCodeX, floorCodeY);

            // Total
            int totalX = im.getWidth() * 138 / 380;
            int totalY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(total), totalX, totalY);

            // deposit
            int depositX = im.getWidth() * 325 / 380;
            int depositY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(deposit), depositX, depositY);

            // custPays
            int custPaysX = im.getWidth() * 138 / 380;
            int custPaysY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(custPays), custPaysX, custPaysY);

            // refun
            int refunX = im.getWidth() * 325 / 380;
            int refunY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(refun), refunX, refunY);

            // List package
            int itemStart = 330;
            int distince = 25;
            int idxStart = 0;
            for (Map<String, String> iPack : lstPackage)
            {
                // price
                int priceX = im.getWidth() * 150 / 380;
                int priceY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                gFullName.drawString(new String(iPack.get("price")), priceX, priceY);

                // num
                int numX = im.getWidth() * 265 / 380;
                int numY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                gFullName.drawString(new String(iPack.get("num")), numX, numY);

                // item
                int amountX = im.getWidth() * 320 / 380;
                int amountY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                gFullName.drawString(new String(iPack.get("amount")), amountX, amountY);

                // item
                String itemStr = iPack.get("item");
                if (itemStr.length() > 20)
                {
                    int itemX = im.getWidth() * 10 / 380;
                    int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(itemStr.substring(0, 20)), itemX, itemY);

                    idxStart += 1;
                    int itemX2 = im.getWidth() * 10 / 380;
                    int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(itemStr.substring(21, itemStr.length())), itemX2, itemY2);
                }
                else
                {
                    int itemX = im.getWidth() * 10 / 380;
                    int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(iPack.get("item")), itemX, itemY);
                }

                idxStart++;
            }

            gFullName.dispose();
            ImageIO.write(im, "jpg", new File("D:\\vietnv\\image\\thanhpham_card.jpg"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void exportImageClientPayment()
    {
//        FileInputStream file = null;

        try
        {

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "main_card.jpg";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "clientPayment.jpg";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "clientPayment.jpg";
            desPathImg = desPath;

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported"))
            {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }
            //lstClientPaymentUsingPack

//            String pathFileMain = "D:\\vietnv\\image_temp\\main_card.jpg";
            BufferedImage im = ImageIO.read(new File(ctx.getRealPath("/") + path));

            Graphics2D gFullName = im.createGraphics();
            gFullName.setColor(Color.BLACK);

            // Branch name
            int branchNameX = im.getWidth() * 197 / 380;
            int branchNameY = im.getHeight() * 112 / 725;
            gFullName.drawString(new String(curClient.getBranch().getBranchName()), branchNameX, branchNameY);

            // Date
            int dateX = im.getWidth() * 212 / 380;
            int dateY = im.getHeight() * 213 / 725;
            gFullName.drawString(new String(DateTimeUtils.formatDateTimeCommon(new Date())), dateX, dateY);

            // Bill code
            int billCodeX = im.getWidth() * 212 / 380;
            int billCodeY = im.getHeight() * 231 / 725;
            gFullName.drawString(new String(curClient.getClientPayment().getPaymentCode()), billCodeX, billCodeY);

            // Floor code
            int floorCodeX = im.getWidth() * 212 / 380;
            int floorCodeY = im.getHeight() * 250 / 725;
            gFullName.drawString(new String(""), floorCodeX, floorCodeY);

            // Total
            int totalX = im.getWidth() * 138 / 380;
            int totalY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getPrice())), totalX, totalY);

            // deposit
            int depositX = im.getWidth() * 325 / 380;
            int depositY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getTotalDeposit())), depositX, depositY);

            // custPays
            int custPaysX = im.getWidth() * 138 / 380;
            int custPaysY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getPaymentValue())), custPaysX, custPaysY);

            // refun
            int refunX = im.getWidth() * 325 / 380;
            int refunY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getDebt())), refunX, refunY);

            // List package
            int itemStart = 330;
            int distince = 25;
            int idxStart = 0;
            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    // price
                    int priceX = im.getWidth() * 150 / 380;
                    int priceY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(DataUtil.getStringNumber(bo.getPrice())), priceX, priceY);

                    // num
                    int numX = im.getWidth() * 265 / 380;
                    int numY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(bo.getStatus() + ""), numX, numY);

                    // item
                    int amountX = im.getWidth() * 320 / 380;
                    int amountY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String((Constant.CLIENT_PAYMENT.STATUS_USED.equals(bo.getStatus()) ? DataUtil.getStringNumber(bo.getPrice()) : "")), amountX, amountY);

                    // item
//                    String itemStr = iPack.get("item");
                    String groupPackName = "";
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null)
                    {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                    }
                    if (groupPackName.length() > 20)
                    {
                        int itemX = im.getWidth() * 10 / 380;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName.substring(0, 20)), itemX, itemY);

                        idxStart += 1;
                        int itemX2 = im.getWidth() * 10 / 380;
                        int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName.substring(21, groupPackName.length())), itemX2, itemY2);
                    }
                    else
                    {
                        int itemX = im.getWidth() * 10 / 380;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName), itemX, itemY);
                    }

                    idxStart++;
                }
            }

            gFullName.dispose();
//            ImageIO.write(im, "jpg", new File("D:\\vietnv\\image\\thanhpham_card.jpg"));
            ImageIO.write(im, "jpg", new File(des));

            /*
            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            context.putVar("timeNow", new Date());
            context.putVar("branchName", curClient.getBranch().getBranchName());
            context.putVar("clientName", curClient.getName());
            context.putVar("cardCode", curClient.getCardCode());

            JxlsHelper.getInstance().processTemplate(is, os, context);

            Row referRow;
            int startRow = 6;
            ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
            if (lstClientPaymentUsingPack != null) {
                for (ClientPayment bo : lstClientPaymentUsingPack) {
                    if (!Constant.CLIENT_PAYMENT.TYPE_DEPOSIT.equals(bo.getType())) {
                        continue;
                    }
                    int colRefer = 0;
                    referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow);
                    referRow.createCell(colRefer++).setCellValue(startRow - 5);
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getPaymentCode());
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    String groupPackName = "";
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null) {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                    }
                    referRow.createCell(colRefer++).setCellValue(groupPackName);
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getPrice() + " VNĐ");
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue(bo.getStatus());
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    referRow.createCell(colRefer++).setCellValue((Constant.CLIENT_PAYMENT.STATUS_USED.equals(bo.getStatus()) ? bo.getPrice() : "") + " VNĐ");
                    referRow.getCell(colRefer - 1).setCellStyle(cellStyleBorder);
                    startRow++;
                }
            }
            // thong tin tong hop
            startRow++;
            referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow++);
            int colRes = 1;
            referRow.createCell(colRes++).setCellValue("Tổng giá tiền:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getPrice() + " VNĐ");

            referRow.createCell(colRes++).setCellValue("Tiền đã đặt cọc:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getTotalDeposit() + " VNĐ");

            referRow = excelWriterUtils.getOrCreateRow(referSheet, startRow++);
            colRes = 1;

            referRow.createCell(colRes++).setCellValue("Khách nộp thêm:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getPaymentValue() + " VNĐ");
            referRow.createCell(colRes++).setCellValue("Tiền trả lại khách:");
            referRow.createCell(colRes++).setCellValue(curClient.getClientPayment().getDebt() + " VNĐ");

            OutputStream outputStream = new FileOutputStream(des);
            wbTemplate.write(outputStream);
            outputStream.flush();
            outputStream.close();
             */
            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "image/jpg", "clientPayment.jpg");
//            InputStream d = PdfUtil.excel2pdf(stream);
//            fileExported = new DefaultStreamedContent(d, "application/pdf", "clientPayment.pdf");

            MessageUtil.setInfoMessage("Export thành công");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * onPrint: in anh
     */
    public void onPrintBill()
    {
//        billImgPath = generateBillImg();
//        billImgPath = generateBillImgNew();
        billImgPath = ImageStreamerBill.generateBillImg(getObjectBillImg());
    }

    public String callBillImgPath()
    {
        return billImgPath;
    }

    public String generateBillImg()
    {
        try
        {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "main_card.jpg";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "clientPayment" + DateTimeUtils.format(new Date(), "yyyyMMddHHmm") + ".jpg";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "clientPayment" + DateTimeUtils.format(new Date(), "yyyyMMddHHmm") + ".jpg";
            desPathImg = desPath;

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported"))
            {
                MessageUtil.setErrorMessage("Tạo folder thất bại");
                fileExported = null;
                return null;
            }

            BufferedImage im = ImageIO.read(new File(ctx.getRealPath("/") + path));

            Graphics2D gFullName = im.createGraphics();
            gFullName.setColor(Color.BLACK);

            // Branch name
            int branchNameX = im.getWidth() * 197 / 380;
            int branchNameY = im.getHeight() * 112 / 725;
            gFullName.drawString(new String(curClient.getBranch().getBranchName()), branchNameX, branchNameY);

            // Date
            int dateX = im.getWidth() * 212 / 380;
            int dateY = im.getHeight() * 213 / 725;
            gFullName.drawString(new String(DateTimeUtils.formatDateTimeCommon(new Date())), dateX, dateY);

            // Bill code
            int billCodeX = im.getWidth() * 212 / 380;
            int billCodeY = im.getHeight() * 231 / 725;
            gFullName.drawString(new String(curClient.getClientPayment().getPaymentCode()), billCodeX, billCodeY);

            // Floor code
            int floorCodeX = im.getWidth() * 212 / 380;
            int floorCodeY = im.getHeight() * 250 / 725;
            gFullName.drawString(new String(""), floorCodeX, floorCodeY);

            // Total
            int totalX = im.getWidth() * 138 / 380;
            int totalY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getPrice())), totalX, totalY);

            // deposit
            int depositX = im.getWidth() * 325 / 380;
            int depositY = im.getHeight() * 563 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getTotalDeposit())), depositX, depositY);

            // custPays
            int custPaysX = im.getWidth() * 138 / 380;
            int custPaysY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getPaymentValue())), custPaysX, custPaysY);

            // refun
            int refunX = im.getWidth() * 325 / 380;
            int refunY = im.getHeight() * 587 / 725;
            gFullName.drawString(new String(DataUtil.getStringNumber(curClient.getClientPayment().getDebt())), refunX, refunY);

            // List package
            int itemStart = 330;
            int distince = 25;
            int idxStart = 0;
            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    // price
                    int priceX = im.getWidth() * 150 / 380;
                    int priceY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(DataUtil.getStringNumber(bo.getPrice())), priceX, priceY);

                    // num
                    int numX = im.getWidth() * 265 / 380;
                    int numY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String(bo.getStatus() + ""), numX, numY);

                    // item
                    int amountX = im.getWidth() * 320 / 380;
                    int amountY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                    gFullName.drawString(new String((Constant.CLIENT_PAYMENT.STATUS_USED.equals(bo.getStatus()) ? DataUtil.getStringNumber(bo.getPrice()) : "")), amountX, amountY);

                    // item
//                    String itemStr = iPack.get("item");
                    String groupPackName = "";
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null)
                    {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                    }
                    if (groupPackName.length() > 20)
                    {
                        int itemX = im.getWidth() * 10 / 380;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName.substring(0, 20)), itemX, itemY);

                        idxStart += 1;
                        int itemX2 = im.getWidth() * 10 / 380;
                        int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName.substring(21, groupPackName.length())), itemX2, itemY2);
                    }
                    else
                    {
                        int itemX = im.getWidth() * 10 / 380;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 725;
                        gFullName.drawString(new String(groupPackName), itemX, itemY);
                    }

                    idxStart++;
                }
            }

            gFullName.dispose();
            ImageIO.write(im, "jpg", new File(des));

            return des;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String getBillImgPath()
    {
        return billImgPath;
    }

    public void setBillImgPath(String billImgPath)
    {
        this.billImgPath = billImgPath;
    }

    public String generateBillImgNew()
    {
        try
        {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "payment_tmp_file.jpg";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "clientPayment" + DateTimeUtils.format(new Date(), "yyyyMMddHHmm") + ".jpg";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "clientPayment" + DateTimeUtils.format(new Date(), "yyyyMMddHHmm") + ".jpg";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported"))
            {
                MessageUtil.setErrorMessage("Tạo folder thất bại");
                fileExported = null;
                return null;
            }

            BufferedImage im = ImageIO.read(new File(ctx.getRealPath("/") + path));

            Graphics2D gFullName = im.createGraphics();
            gFullName.setColor(Color.BLACK);
            gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
            // Branch name
/*            int branchNameX = im.getWidth() * 197 / 380;
            int branchNameY = im.getHeight() * 112 / 725;
            gFullName.drawString(new String(curMember.getBranch().getBranchName()), branchNameX, branchNameY);
             */
//            Membership newMembership = curMember.getNewMembership();
            ClientPayment clientPayment = curClient.getClientPayment();
//            CatPromotion catPromotion = clientPayment.getCatPromotion();

            String customerName = "";
            String customerType = MessageUtil.getResourceBundleMessage("customerCheckin.type2");// khach le
            Long numMember = 1l;
            String employeeName = "";
            Long vatPrice = 0l;
            Long discount = 0l;
//            Long totalPrice = 0l;// khong xac dinh chinh xac duoc
//            Long oldDept = 0l;
//            Long totalDept = 0l;

            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            if (user != null && user.getEmployee() != null)
            {
                employeeName = user.getEmployee().getEmployeeName();

            }
            if (curClient.getName() != null)
            {
                customerName = curClient.getName();
            }

            // custPays payment
            Long paymentValue = clientPayment.getPaymentValue();
            Long custPay = 0l;//khach tra them
            //cong no chi co gia trị am 
            Long debt = clientPayment.getDebt();
            if (clientPayment.getDebt() > 0)
            {
//                paymentValue = clientPayment.getPrice();// khach le ko no
                debt = 0l;
            }
            //lay gia trị duong
            if (debt < 0)
            {
                debt = -debt;
            }
//            if (clientPayment.getTotalDeposit() != null) {
//                custPay = clientPayment.getTotalDeposit();
//            }
//            if (clientPayment.getPaymentValue() != null) {
//                custPay += clientPayment.getPaymentValue();
//            }
            custPay = clientPayment.getPrice();
            if (custPay != null && clientPayment.getTotalDeposit() != null)
            {
                custPay = clientPayment.getPrice() - clientPayment.getTotalDeposit();
            }

            //
            /*
            if (curClient.getTotalPayment() != null) {
                oldDept = Math.abs(curClient.getTotalPayment());
                totalDept = oldDept;
            }
            if (debt != null) {
                totalDept += debt;
            }
             */
            // Date
            int dateX = im.getWidth() * 545 / 795;
            int dateY = im.getHeight() * 712 / 2546;//770
            gFullName.drawString(new String(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY)), dateX, dateY);

            // Bill code
            int billCodeX = im.getWidth() * 510 / 795;
            int billCodeY = im.getHeight() * 655 / 2546;
            gFullName.drawString(new String(clientPayment.getPaymentCode()), billCodeX, billCodeY);
            // customer
            int customerX = im.getWidth() * 205 / 795;
            int customerY = im.getHeight() * 780 / 2546;
            gFullName.drawString(customerName, customerX, customerY);
            // customer type
            int customerTypeX = im.getWidth() * 640 / 795;
            int customerTypeY = im.getHeight() * 780 / 2546;
            gFullName.drawString(customerType, customerTypeX, customerTypeY);
            //num customer
            int numCustomerX = im.getWidth() * 500 / 795;
            int numCustomerY = im.getHeight() * 881 / 2546;
            gFullName.drawString(numMember.toString(), numCustomerX, numCustomerY);
            // thu ngan
            int empX = im.getWidth() * 290 / 795;
            int empY = im.getHeight() * 1051 / 2546;
            gFullName.drawString(employeeName, empX, empY);

            // Floor code
            int floorCodeX = im.getWidth() * 157 / 795;
            int floorCodeY = im.getHeight() * 229 / 2546;
            gFullName.drawString(new String(""), floorCodeX, floorCodeY);

            // List package
            int itemStart = 1255;
            int distince = 50;
            int idxStart = 0;
//            List<MemberPayment> lstMemberPayments = new ArrayList<>();
//            lstMemberPayments.add(clientPayment);
            Font oldFont = gFullName.getFont();
            gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    if (bo.getType() != null && bo.getType().equals(2l))
                    {
                        continue; // bo qua loai thanh toan
                    }                    // item
//                    String itemStr = iPack.get("item");
                    // price dong gia
//                    CatGroupPack catGroupPack = newMembership.getGroupPack();
                    Long price = null;
                    String groupPackName = "";
//                    if (catGroupPack != null && catGroupPack.getPrice() != null) {
//                        price = catGroupPack.getPrice().longValue();
//                        groupPackName = catGroupPack.getGroupPackName();
//                    }
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null)
                    {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                        price = bo.getClientUsePack().getGroupPack().getPrice().longValue();
                    }
                    // tinh tong vat
                    if (StringUtil.isNotNull(bo.getVat()) && bo.getPrice() != null)
                    {
                        vatPrice += bo.getPrice() / 11;

                        discount += price - bo.getPrice() * 10 / 11;
                    }
                    else if (price != null && bo.getPrice() != null)
                    {
                        discount += price - bo.getPrice();
                    }
                    // num
                    Long numPack = 1l;
                    int numX = im.getWidth() * 200 / 795;
                    int numY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
//                    gFullName.drawString(new String(bo.getStatus() + ""), numX, numY);
                    gFullName.drawString(new String(numPack.toString()), numX, numY);

//                    totalPrice += numPack * price;
                    int priceX = im.getWidth() * 255 / 795;
                    int priceY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                    gFullName.drawString(new String(DataUtil.getStringNumber(price)), priceX, priceY);

                    //KM
                    String desc = "";
                    CatPromotion catPromotion = bo.getCatPromotion();
                    if (catPromotion != null && catPromotion.getValue() != null)
                    {

                        if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()))
                        {
                            desc = DataUtil.getStringNumber(catPromotion.getValue());
                        }
                        else
                        {
                            desc = catPromotion.getValue().toString();

                        }
                        desc = desc
                                + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                                ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "" : " lần");
                    }
                    int promotionX = im.getWidth() * 385 / 795;
                    int promotionY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                    gFullName.drawString(new String(desc), promotionX, promotionY);

                    int amountX = im.getWidth() * 511 / 795;
                    int amountY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                    gFullName.drawString(new String((DataUtil.getStringNumber(bo.getPrice()))), amountX, amountY);

                    //hsd
                    int endDateX = im.getWidth() * 685 / 795;
                    int endDateY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                    int newidxStart = idxStart;

//                    Font fontOld=gFullName.getFont();
//                    gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                    if (bo.getClientUsePack() != null)
                    {

                        String hsd = DateTimeUtils.format(bo.getClientUsePack().getEndDate(), Constant.PATTERN.DATE_COMMON_YY);
                        if (hsd.length() < 9)
                        {
                            gFullName.drawString(new String(hsd), endDateX, endDateY);
                        }
                        else
                        {
                            gFullName.drawString(new String(hsd.substring(0, 9)), endDateX, endDateY);
                            int h2 = im.getHeight() * (itemStart + (idxStart + 1) * distince - 2) / 2546;
                            gFullName.drawString(new String(hsd.substring(6, hsd.length())), endDateX, h2);
                            newidxStart += 1;
                        }
                    }
//                    gFullName.setFont(fontOld);

                    // item
                    if (groupPackName.length() > 12)
                    {

                        int itemX = im.getWidth() * 13 / 795;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                        gFullName.drawString(new String(groupPackName.substring(0, 12)), itemX, itemY);

                        idxStart += 1;
                        if (groupPackName.length() > 24)
                        {
                            int itemX2 = im.getWidth() * 12 / 795;
                            int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                            gFullName.drawString(new String(groupPackName.substring(13, 24)), itemX2, itemY2);

                            idxStart += 1;
                            int itemY3 = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                            gFullName.drawString(new String(groupPackName.substring(25, groupPackName.length())), itemX2, itemY3);
                        }
                        else
                        {
                            int itemX2 = im.getWidth() * 12 / 795;
                            int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                            gFullName.drawString(new String(groupPackName.substring(13, groupPackName.length())), itemX2, itemY2);
                        }
                    }
                    else
                    {
                        int itemX = im.getWidth() * 13 / 795;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 2546;
                        gFullName.drawString(new String(groupPackName), itemX, itemY);
                    }
                    if (newidxStart > idxStart)
                    {
                        idxStart = newidxStart;
                    }

                    idxStart++;
                }
            }
            gFullName.setFont(oldFont);
            // giam gia
            int discountX = im.getWidth() * 420 / 795;
            int discountY = im.getHeight() * 1711 / 2546;
            gFullName.drawString(new String(!discount.equals(0l) ? DataUtil.getStringNumber(discount) : ""), discountX, discountY);
            // vat
            int vatX = im.getWidth() * 420 / 795;
            int vatY = im.getHeight() * 1761 / 2546;
            gFullName.drawString(new String(!vatPrice.equals(0l) ? DataUtil.getStringNumber(vatPrice) : ""), vatX, vatY);

            // Total 
            int totalX = im.getWidth() * 420 / 795;
            int totalY = im.getHeight() * 1811 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(clientPayment.getPrice())), totalX, totalY);
            // deposit
            int depositX = im.getWidth() * 420 / 795;
            int depositY = im.getHeight() * 1865 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(clientPayment.getTotalDeposit())), depositX, depositY);
            // Total khach phai tra
            int totalMustPayX = im.getWidth() * 420 / 795;
            int totalMustPayY = im.getHeight() * 1919 / 2546;
//            gFullName.drawString(new String(DataUtil.getStringNumber(clientPayment.getPrice())), totalMustPayX, totalMustPayY);
            gFullName.drawString(new String(DataUtil.getStringNumber(custPay)), totalMustPayX, totalMustPayY);

            // khach tra
            int custPaysX = im.getWidth() * 420 / 795;
            int custPaysY = im.getHeight() * 1972 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(custPay)), custPaysX, custPaysY);
            /*
            // cong no mơi
            // debt
            int deptX = im.getWidth() * 131 / 795;
            int deptY = im.getHeight() * 671 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(debt)), deptX, deptY);

            // debt old
            int deptOldX = im.getWidth() * 119 / 795;
            int deptOldY = im.getHeight() * 692 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(oldDept)), deptOldX, deptOldY);

            // tong cong no total dept
            int totalDeptX = im.getWidth() * 108 / 795;
            int totalDeptY = im.getHeight() * 714 / 2546;
            gFullName.drawString(new String(DataUtil.getStringNumber(totalDept)), totalDeptX, totalDeptY);
             */

            gFullName.dispose();
            ImageIO.write(im, "jpg", new File(des));

            return des;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public PrintPaymentForm getObjectBillImg()
    {
        PrintPaymentForm paymentForm = new PrintPaymentForm();
        try
        {

            ClientPayment clientPayment = curClient.getClientPayment();

            String customerName = "";
            String customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type2");
            paymentForm.setCustomerType(Constant.PAYMENT_TYPE.CLIENT);
            Long numMember = 1L;

            String employeeName = "";
            Long vatPrice = 0l;
            Long discount = 0l;
            Long totalPrice = 0l;// khong xac dinh chinh xac duoc
//            Long oldDept = 0l;
//            Long totalDept = 0l;

            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            if (user != null && user.getEmployee() != null)
            {
                employeeName = user.getEmployee().getEmployeeName();

            }
            if (curClient.getName() != null)
            {
                customerName = curClient.getName();
            }

            // custPays payment
            Long paymentValue = clientPayment.getPaymentValue();
            // custPays payment
            Long custPay = 0l;//khach tra them
            //cong no chi co gia trị am 
            Long debt = clientPayment.getDebt();
            if (clientPayment.getDebt() > 0)
            {
//                paymentValue = clientPayment.getPrice();// khach le ko no
                debt = 0l;
            }
            //lay gia trị duong
            if (debt < 0)
            {
                debt = -debt;
            }
            custPay = clientPayment.getPrice();
            if (custPay != null && clientPayment.getTotalDeposit() != null)
            {
                custPay = clientPayment.getPrice() - clientPayment.getTotalDeposit();
            }

            paymentForm.setPaymentTime(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY));
            // Bill code
            paymentForm.setPaymentCode(clientPayment.getPaymentCode());
            // customer
            paymentForm.setCustomerName(customerName);
            paymentForm.setCustomerTypeName(customerTypeName);
            paymentForm.setNumCustomer(numMember != null ? numMember : 1l);
            paymentForm.setEmployeeName(employeeName);
            paymentForm.setBarCode("");

//            List<ClientPayment> lstMemberPayments = new ArrayList<>();
//            lstMemberPayments.add(clientPayment);
            List<PaymentPackObj> lstPaymentPackObjs = new ArrayList<>();

            if (lstClientPaymentUsingPack != null)
            {
                for (ClientPayment bo : lstClientPaymentUsingPack)
                {
                    if (bo.getType() != null && bo.getType().equals(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO))
                    {
                        continue; // bo qua loai thanh toan
                    }
                    PaymentPackObj payPack = new PaymentPackObj();
                    // item
//                    String itemStr = iPack.get("item");
                    // price dong gia
                    Long price = null;
                    String groupPackName = "";
                    if (bo.getClientUsePackId() != null && bo.getClientUsePack() != null && bo.getClientUsePack().getGroupPack() != null)
                    {
                        groupPackName = bo.getClientUsePack().getGroupPack().getGroupPackName();
                        price = bo.getClientUsePack().getGroupPack().getPrice().longValue();
                    }
                    // tinh tong vat
                    if (StringUtil.isNotNull(bo.getVat()) && bo.getPrice() != null)
                    {
                        vatPrice += bo.getPrice() / 11;

                        discount += price - bo.getPrice() * 10 / 11;
                    }
                    else if (price != null && bo.getPrice() != null)
                    {
                        discount += price - bo.getPrice();
                    }
                    // num
                    Long numPack = 1l;
                    payPack.setQuantity(numPack);
                    payPack.setPrice(price);

                    if (price != null)
                    {
                        totalPrice += numPack * price;
                    }
                    //KM
                    CatPromotion catPromotion = bo.getCatPromotion();

                    payPack.setProm(ImageStreamerBill.descProm(catPromotion));
                    payPack.setAmount(bo.getPrice());
                    payPack.setExp(DateTimeUtils.format(bo.getClientUsePack().getEndDate(), Constant.PATTERN.DATE_COMMON_YY));
                    payPack.setGroupPackName(groupPackName);
                    lstPaymentPackObjs.add(payPack);
                }
            }
            paymentForm.setLstPaymentPackObjs(lstPaymentPackObjs);
            // giam gia
            paymentForm.setTotalPriceService(totalPrice);
            paymentForm.setDiscount(discount);
            paymentForm.setVatPrice(vatPrice);
            // Total 
            paymentForm.setTotal(clientPayment.getPrice());
            paymentForm.setDeposit(clientPayment.getTotalDeposit());

            // Total khach phai tra
            paymentForm.setMustPay(custPay);
            // deposit
            // khach tra
            paymentForm.setCustomerPay(custPay);

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return paymentForm;
    }

    public void checkOutClient()
    {
        try
        {

            curClient.setStatus(Constant.CLIENT_STATUS.STOP);
            curClient.setEndTime(new Date());
            clientService.saveOrUpdate(curClient);
            // cap nhat off het goi dang su dung
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("clientId", curClient.getClientId());
            filterPay.put("status", 1l);
            filterPay.put("endDate-GE", new Date());
            Long customerScheduleId = null;
            List<ClientUsePack> lstClientUsePacks = clientUsePackService.findList(filterPay);
            if (lstClientUsePacks != null && lstClientUsePacks.size() > 0)
            {
                for (ClientUsePack bo : lstClientUsePacks)
                {
                    bo.setStatus(Constant.CLIENT_USE_PACK.STATUS_STOP);
                    if (bo.getCustomerScheduleId() != null)
                    {
                        customerScheduleId = bo.getCustomerScheduleId();
                    }
                }
            }
            clientUsePackService.saveOrUpdate(lstClientUsePacks);
            completedCustomerSchedule(customerScheduleId);//cap nhat lai lich trinh
            checkout();
            //ghi log
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, curClient.getName(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");

            preEditClient(curClient);
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

}
