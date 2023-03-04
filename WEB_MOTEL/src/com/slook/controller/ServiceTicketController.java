/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CustomerCheckin;
import com.slook.model.MemberUsedService;
import com.slook.model.Membership;
import com.slook.model.ServiceTicket;
import com.slook.model.V_ServiceTicket;
import com.slook.persistence.CustomerCheckinServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.MemberUsedServiceServiceImpl;
import com.slook.persistence.MembershipServiceImpl;
import com.slook.persistence.ServiceTicketServiceImpl;
import com.slook.persistence.VServiceTicketServiceImpl;
import com.slook.util.Constant;
import com.slook.util.DateTimeUtils;
import com.slook.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

/**
 * @author VietNV on Nov 30, 2017
 */
@ManagedBean
@ViewScoped
public class ServiceTicketController
{

    GenericDaoImplNewV2<ServiceTicket, Long> serviceTicketService;
    LazyDataModel<V_ServiceTicket> lazyDataModel;
    ServiceTicket currServiceTicket = new ServiceTicket();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart()
    {

        serviceTicketService = new GenericDaoImplNewV2<ServiceTicket, Long>()
        {
        };
        try
        {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("status", Constant.ORDER.ASC);
            order.put("serviceTicketCode", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();

            lazyDataModel = new LazyDataModelBase<V_ServiceTicket, Long>(VServiceTicketServiceImpl.getInstance(), filter, order);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true, false,
                true, false, true, true, false,
                true, true, true
        );
    }

    public void preCheckin(V_ServiceTicket vServiceTicket)
    {
        try
        {
            currServiceTicket = serviceTicketService.findById(vServiceTicket.getServiceTicketId());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        oldObjectStr = currServiceTicket.toString();
    }

    public void checkin()
    {
        try
        {
            currServiceTicket.setUsedTime(new Date());
            currServiceTicket.setStatus(Constant.SERVICE_TICKET.STATUS_USED);
            serviceTicketService.saveOrUpdate(currServiceTicket);

            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currServiceTicket.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currServiceTicket.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            // chekin dich vu cho khach hang
            Membership membership = MembershipServiceImpl.getInstance().findById(currServiceTicket.getMembershipId());
            List<ServiceTicket> lstCheckinTicket = Arrays.asList(currServiceTicket);
            List<CustomerCheckin> lstCheckins = new ArrayList<>();
            for (ServiceTicket checkinTicket : lstCheckinTicket)
            {
                CustomerCheckin cs = new CustomerCheckin();
                cs.setCardCode(currServiceTicket.getCardCode());
                cs.setCheckTime(new Date());
                cs.setCustomerId(membership.getMemberId());
                cs.setMembershipId(currServiceTicket.getMembershipId());

                cs.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
                cs.setType(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
                cs.setTypeAccess(Constant.WS_C_METHOD.TYPE_CARD);
                lstCheckins.add(cs);
            }
            //filter ds service su dung cho goi
            Map<String, Object> filter = new HashMap();
            filter.put("membershipId", currServiceTicket.getMembershipId());
            filter.put("status", Constant.MEMBER_USED_SERVICE.VALID);
            filter.put("startDate-LE", new Date());
            filter.put("endDate-GE", DateTimeUtils.trunc(new Date()));
            List<MemberUsedService> lst = MemberUsedServiceServiceImpl.getInstance().findList(filter);
            if (lst != null && lst.size() > 0)
            {
                for (MemberUsedService bo : lst)
                {
                    if (bo.getAvailable() != null && bo.getAvailable() >= lstCheckins.size())
                    {
                        bo.setAvailable(bo.getAvailable() - lstCheckins.size());
                    }
                    else if (bo.getTotalNumber() != null && bo.getTotalNumber() > 0)
                    {
                        MessageUtil.setErrorMessage("Dịch vụ " + bo.getCatService().getServiceName() + " của gói không còn đủ lượt sử dụng");
                        return;
                    }
                    //luon ghi lai so luot su dung
                    if (bo.getUsedNumber() == null)
                    {
                        bo.setUsedNumber(0l);
                    }
                    bo.setUsedNumber(bo.getUsedNumber() + lstCheckins.size());

                }
                // cap nhat thong tin chung cho goi
                if (membership.getAvailable() != null && membership.getAvailable() > 0)
                {
                    membership.setAvailable(membership.getAvailable() - lstCheckins.size());
                }
                else if (membership.getAvailable() != null && membership.getAvailable().equals(0l))
                {
                    MessageUtil.setErrorMessage("Gói đã hết lượt sử dụng");
                    return;
                }
                if (membership.getUsedNumber() == null)
                {
                    membership.setUsedNumber(Long.valueOf(lstCheckins.size()));
                }
                else
                {
                    membership.setUsedNumber(membership.getUsedNumber() + lstCheckins.size());
                }
            }
            else
            {

                MessageUtil.setErrorMessage("Không có dịch vụ còn hiệu lực để sử dụng");
                return;
            }

            //cap nhat quyen vao may quet
            List<String> lstCardCode = new ArrayList<>();
            for (CustomerCheckin customerCheckin : lstCheckins)
            {
                // KIEM TRA NEU THE SU DUNG LAN DAU TRONG NGAY THI OFF TAT CAC O CAC MAY
                MemberController.initCheckOutAll(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER, membership.getMemberId(), customerCheckin.getCardCode());

                MemberController.updateAccessCatMachineNew(customerCheckin.getMember(), membership, Constant.METHOD.INSERT, customerCheckin.getCardCode());
                lstCardCode.add(customerCheckin.getCardCode());
            }
            ServiceTicketServiceImpl.getInstance().saveOrUpdate(lstCheckinTicket);
            MemberUsedServiceServiceImpl.getInstance().saveOrUpdate(lst);
            MembershipServiceImpl.getInstance().saveOrUpdate(membership);

            CustomerCheckinServiceImpl.getInstance().saveOrUpdate(lstCheckins);
            MessageUtil.setInfoMessageFromRes("info.checkin.success");
            RequestContext.getCurrentInstance().execute("PF('checkinDlg').hide();");

// show result
/*
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {
                showResultAccessStatus(lstCardCode);
                RequestContext.getCurrentInstance().execute("PF('checkinResultDlg').show();");
                RequestContext.getCurrentInstance().update("@widgetVar('checkinResultDlg')");
            }*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveEmployeeService()
    {
        try
        {

            if (currServiceTicket.getEmployee() != null && currServiceTicket.getEmployee().getEmployeeId() != null)
            {
                currServiceTicket.setEmployeeId(currServiceTicket.getEmployee().getEmployeeId());
            }
            serviceTicketService.saveOrUpdate(currServiceTicket);

            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currServiceTicket.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currServiceTicket.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("common.success");
            RequestContext.getCurrentInstance().execute("PF('employeeServiceDlg').hide();");

// show result
/*
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {
                showResultAccessStatus(lstCardCode);
                RequestContext.getCurrentInstance().execute("PF('checkinResultDlg').show();");
                RequestContext.getCurrentInstance().update("@widgetVar('checkinResultDlg')");
            }*/
        }
        catch (Exception e)
        {

            MessageUtil.setErrorMessageFromRes("common.fail");
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public GenericDaoImplNewV2<ServiceTicket, Long> getServiceTicketService()
    {
        return serviceTicketService;
    }

    public void setServiceTicketService(GenericDaoImplNewV2<ServiceTicket, Long> serviceTicketService)
    {
        this.serviceTicketService = serviceTicketService;
    }

    public LazyDataModel<V_ServiceTicket> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<V_ServiceTicket> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public ServiceTicket getCurrServiceTicket()
    {
        return currServiceTicket;
    }

    public void setCurrServiceTicket(ServiceTicket currServiceTicket)
    {
        this.currServiceTicket = currServiceTicket;
    }

    public String getOldObjectStr()
    {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr)
    {
        this.oldObjectStr = oldObjectStr;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }
    //</editor-fold>
}
