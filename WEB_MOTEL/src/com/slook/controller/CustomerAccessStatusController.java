/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.lazy.LazyDataModelBase;
import com.slook.model.DoorAccessStatus;
import com.slook.model.V_CustomerAccessStatus;
import com.slook.persistence.DoorAccessStatusServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;
import com.slook.webservice.GymWsMCCImpl;

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

import org.primefaces.model.LazyDataModel;

/**
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class CustomerAccessStatusController
{

    GenericDaoImplNewV2<V_CustomerAccessStatus, Long> customerAccessStatusService;
    LazyDataModel<V_CustomerAccessStatus> lazyDataModel;
    V_CustomerAccessStatus currVcustomerAccessStatus = new V_CustomerAccessStatus();
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart()
    {

        customerAccessStatusService = new GenericDaoImplNewV2<V_CustomerAccessStatus, Long>()
        {
        };
        try
        {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("updateTime", Constant.ORDER.DESC);
//            order.put("cardCode", "ASC");
//            order.put("groupPackName", "ASC");
//            order.put("roomName", "ASC");
//            order.put("ip", "ASC");
            Map<String, Object> filter = new HashMap<>();

            lazyDataModel = new LazyDataModelBase<V_CustomerAccessStatus, Long>(customerAccessStatusService, filter, order);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void refreshUpdateAccessStatus(V_CustomerAccessStatus vCas)
    {
        try
        {
            String old = vCas.toString();
            vCas.setAccessStatus(1l);

            List<String> lstIp = Arrays.asList(vCas.getIp());
            callWSUpdateAccess(vCas.getCardCode(), lstIp, Constant.STATUS.ACTIVE, Constant.WS_C_METHOD.TYPE_CARD);
//            catMachineService.saveOrUpdate(vCas);
//            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, vCas.toString(), this.getClass().getSimpleName(),
//                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public static void callWSUpdateAccess(String cardCode, List<String> lstIp, Long typeAction, Long typeAccess)
    {
        if (!Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL")))
        {
            // khong thuc hien neu web cai tren server tap trung
            return;
        }
        if (lstIp == null || lstIp.size() == 0)
        {
            if (Constant.STATUS.ACTIVE.equals(typeAction))
            {
                MessageUtil.setInfoMessage("Không có cửa để kích hoạt cho vòng khóa!");
            }
            else if (Constant.STATUS.DISABLE.equals(typeAction))
            {
                MessageUtil.setInfoMessage("Không có cửa để checkout cho vòng khóa!");
            }
            return;
        }
        //ket noi toi ws
        int method = Constant.WS_C_METHOD.ADD_ACCESS;
        if (Constant.STATUS.DISABLE.equals(typeAction))
        {
            method = Constant.WS_C_METHOD.REMOVE_ACCESS;
        }
        else if (Constant.STATUS.ACTIVE.equals(typeAction))
        {
            method = Constant.WS_C_METHOD.ADD_ACCESS;
        }
        List<DoorAccessStatus> lstResult = new ArrayList<>();
        Map<String, Long> mapUpdate = new HashMap<>();
        List<DoorAccessStatus> lstResultWS = null;
        try
        {
            GymWsMCCImpl gwsmcc = GymWsMCCImpl.getInstance();
            if (Constant.WS_C_METHOD.TYPE_FINGERPRINT.equals(typeAccess))
            {
                lstResultWS = gwsmcc.userAccessFingerprint(lstIp, cardCode, method);

            }
            else
            {
                lstResultWS = gwsmcc.userAccess(lstIp, cardCode, method);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MessageUtil.setErrorMessage(MessageUtil.getResourceBundleMessage("error.connect.ws") + " " + DataConfig.getConfigByKey("LINK_SERVER_SERVICES"));
        }
        //truong hop khong ket noi dươc insert tat cac the vao ds ip
        if (lstResultWS == null || lstResultWS.isEmpty())
        {
            lstResultWS = new ArrayList<>();
            for (String ip : lstIp)
            {
                if (StringUtil.isNotNull(ip))
                {
                    DoorAccessStatus bo = new DoorAccessStatus();
                    bo.setCardCode(cardCode);
                    bo.setIp(ip);
                    bo.setStatus(2l);
                    bo.setType(typeAccess);
                    bo.setResult(Constant.WS_C_METHOD.RESULT_FAILURE);
                    lstResultWS.add(bo);
                }
            }
        }
        List<DoorAccessStatus> lstUpdate = new ArrayList<>();
        Map<String, Object> filter = new HashMap();
        filter.put("cardCode-EXAC", cardCode);
        filter.put("ip", lstIp);
//        List<String> lstIpExist = new ArrayList<>();
        try
        {
            lstUpdate = DoorAccessStatusServiceImpl.getInstance().findList(filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (lstUpdate != null)
        {
//            lstIpExist = (List<String>) CommonUtil.getListAttributeInList((List) lstUpdate, "ip");
            for (DoorAccessStatus d : lstUpdate)
            {
                mapUpdate.put(d.getCardCode() + "_" + d.getIp(), d.getId());
            }
        }
        for (DoorAccessStatus bo : lstResultWS)
        {
            bo.setUpdateTime(new Date());
            if (Constant.WS_C_METHOD.RESULT_SUCCESS.equals(bo.getResult()))
            {
                bo.setId(mapUpdate.get(bo.getCardCode() + "_" + bo.getIp()));
                lstResult.add(bo);
            }
            else
            {//truong hop cap nhat ko thanh cong va chua co ban ghi trong DB mac dinh them vao la chua active
                if (mapUpdate.get(bo.getCardCode() + "_" + bo.getIp()) == null)
                {
                    bo.setStatus(2l);
                    lstResult.add(bo);
                }

            }
        }
//        for (String ip : lstIp) {
//            if (!lstIpExist.contains(ip)) {
//                DoorAccessStatus d = new DoorAccessStatus();
//                d.setIp(ip);
//                d.setCardCode(cardCode);
//                lstUpdate.add(d);
//            }
//        }
        /*if (Constant.STATUS.ACTIVE.equals(type)) {
            for (DoorAccessStatus bo : lstUpdate) {
                bo.setStatus(1l);
                bo.setUpdateTime(new Date());
            }
        } else {
            for (DoorAccessStatus bo : lstUpdate) {
                bo.setStatus(2l);
                bo.setUpdateTime(new Date());
            }
        }*/
        try
        {
            DoorAccessStatusServiceImpl.getInstance().saveOrUpdate(lstResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public GenericDaoImplNewV2<V_CustomerAccessStatus, Long> getCustomerAccessStatusService()
    {
        return customerAccessStatusService;
    }

    public void setCustomerAccessStatusService(GenericDaoImplNewV2<V_CustomerAccessStatus, Long> customerAccessStatusService)
    {
        this.customerAccessStatusService = customerAccessStatusService;
    }

    public LazyDataModel<V_CustomerAccessStatus> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<V_CustomerAccessStatus> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public V_CustomerAccessStatus getCurrVcustomerAccessStatus()
    {
        return currVcustomerAccessStatus;
    }

    public void setCurrVcustomerAccessStatus(V_CustomerAccessStatus currVcustomerAccessStatus)
    {
        this.currVcustomerAccessStatus = currVcustomerAccessStatus;
    }
//</editor-fold>

    public void refreshUpdateFingerprintAccessStatus(V_CustomerAccessStatus vCas)
    {
        try
        {
            String old = vCas.toString();
            vCas.setAccessStatus(1l);

            List<String> lstIp = Arrays.asList(vCas.getIp());
            callWSUpdateAccess(vCas.getCardCode(), lstIp, Constant.STATUS.ACTIVE, Constant.WS_C_METHOD.TYPE_FINGERPRINT);
//            catMachineService.saveOrUpdate(vCas);
//            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, vCas.toString(), this.getClass().getSimpleName(),
//                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
}
