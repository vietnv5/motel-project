/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.lazy.LazyScheduleGym;
import com.slook.lazy.lazyCustomerScheduleGym;
import com.slook.model.CatAddressCity;
import com.slook.model.CatBranch;
import com.slook.model.CatGroupPack;
import com.slook.model.CatPack;
import com.slook.model.CatRoom;
import com.slook.model.CatUser;
import com.slook.model.Client;
import com.slook.model.ClientUsePack;
import com.slook.model.CustomerSchedule;
import com.slook.model.CustomerSchedulePack;
import com.slook.model.Employee;
import com.slook.model.Member;
import com.slook.model.Membership;
import com.slook.model.Schedule;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.common.ConditionQuery;
import com.slook.persistence.common.OrderBy;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.Locale.filter;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import static org.omnifaces.util.Faces.getRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class CustomerScheduleController
{

    GenericDaoImplNewV2<Client, Long> clientService;
    GenericDaoImplNewV2<Member, Long> memberService;
    GenericDaoImplNewV2<CustomerSchedule, Long> customerScheduleService;
    GenericDaoImplNewV2<CustomerSchedulePack, Long> customerSchedulePackService;
    GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService;
    GenericDaoImplNewV2<Membership, Long> membershipService;
    GenericDaoImplNewV2<ClientUsePack, Long> clientUsePackService;
    GenericDaoImplNewV2<Employee, Long> employeeService;

    LazyDataModel<CustomerSchedule> lazyDataModel;
    CustomerSchedule currCustomerSchedule = new CustomerSchedule();
    boolean isEdit = false;
    private String oldObjectStr = null;
    List<CatGroupPack> catGroupPacks;
    Long oldStatus;

    //bieu do
    ScheduleModel scheduleGym;
    private ScheduleEvent event = new DefaultScheduleEvent();
    CatBranch currBranch;
    // bo sung dieu kien tim kiem
    private Long catGroupPackId;
    private Date fromDate;
    private Date toDate;

    @PostConstruct
    public void onStart()
    {

        customerScheduleService = new GenericDaoImplNewV2<CustomerSchedule, Long>()
        {
        };
        catGroupPackService = new GenericDaoImplNewV2<CatGroupPack, Long>()
        {
        };
        customerSchedulePackService = new GenericDaoImplNewV2<CustomerSchedulePack, Long>()
        {
        };
        membershipService = new GenericDaoImplNewV2<Membership, Long>()
        {
        };
        clientUsePackService = new GenericDaoImplNewV2<ClientUsePack, Long>()
        {
        };
        clientService = new GenericDaoImplNewV2<Client, Long>()
        {
        };
        memberService = new GenericDaoImplNewV2<Member, Long>()
        {
        };
        employeeService = new GenericDaoImplNewV2<Employee, Long>()
        {
        };

        Date sysdate = new Date();
        int y = sysdate.getYear();
        int m = sysdate.getMonth();
        int d = sysdate.getDate();
        m--;

        fromDate = new Date(y, m, d);
        toDate = new Date();

        try
        {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("startTime", "DESC");
            Map<String, Object> filter = new HashMap<>();

//            if (fromDate != null) {
//                filter.put("startTime-GE", fromDate);
//            }
//            if (toDate != null) {
//                filter.put("startTime-LT", new Date(toDate.getTime()));
//            }
            lazyDataModel = new LazyDataModelBase<CustomerSchedule, Long>(customerScheduleService, filter, order);
            catGroupPacks = catGroupPackService.findList();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        RequestContext.getCurrentInstance().execute("PF('widgetCustomerScheduleTbl').filter();");
        // bieu do lich trinh
        try
        {
            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            Long branchId = null;
            if (user != null && user.getEmployee() != null)
            {
                branchId = user.getEmployee().getBranchId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branchName", "ASC");
            currBranch = new GenericDaoImplNewV2<CatBranch, Long>()
            {
            }.findById(branchId);
            if (currBranch == null)
            {
                currBranch = new CatBranch();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        loadSchedule();
    }

    public void search()
    {
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> orders = new LinkedHashMap<>();
        Map<String, Object> sqlRes = new HashMap<>();

        if (fromDate != null)
        {
            filter.put("startTime-GE", fromDate);
        }
        if (toDate != null)
        {
            filter.put("startTime-LT", new Date(toDate.getTime()));
        }

        if (catGroupPackId != null)
        {
            String sql = "( customer_Schedule_Id in (  select CUSTOMER_SCHEDULE_ID from CUSTOMER_SCHEDULE_PACK \n"
                    + " where GROUP_PACK_ID = ? ))";
            sqlRes.put(sql, catGroupPackId);

        }

        orders.put("startTime", "DESC");
        lazyDataModel = new LazyDataModelBase<CustomerSchedule, Long>(customerScheduleService, filter, sqlRes, orders);

        RequestContext.getCurrentInstance().execute("PF('widgetCustomerScheduleTbl').clearFilters();");
        RequestContext.getCurrentInstance().update("customerScheduleForm");
    }

    public void preAdd()
    {
        currCustomerSchedule = new CustomerSchedule();
        currCustomerSchedule.setStatus(1l);
        currCustomerSchedule.setSex(MessageUtil.getKey("view.label.sexMale"));
        currCustomerSchedule.setBranchId(currBranch.getBranchId());
        CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
        if (user != null && user.getEmployee() != null)
        {
            currCustomerSchedule.setEmployeeSchedule(user.getEmployee());
        }
        oldObjectStr = null;
        isEdit = false;
    }

    public void onchangeMember()
    {
        if (currCustomerSchedule.getMember() != null)
        {
            currCustomerSchedule.setCustomerId(currCustomerSchedule.getMember().getMemberId());
            currCustomerSchedule.setName(currCustomerSchedule.getMember().getMemberName());
            currCustomerSchedule.setSex(currCustomerSchedule.getMember().getSex());
            currCustomerSchedule.setPhoneNumber(currCustomerSchedule.getMember().getPhoneNumber());
        }
    }

    public boolean validate()
    {
        if (Constant.CUSTOMER_SCHEDULE.STATUS_ACTIVE.equals(currCustomerSchedule.getStatus())
                || Constant.CUSTOMER_SCHEDULE.STATUS_COMPLETED.equals(currCustomerSchedule.getStatus()))
        {
            MessageUtil.setErrorMessage("Chỉ được phép cập nhật về trạng thái Hủy bỏ");
            currCustomerSchedule.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_SCHEDULE);// fix loi
            return false;
        }
        if (currCustomerSchedule.getStartTime() == null)
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    MessageUtil.getResourceBundleMessage("customerSchedule.startTime")));
            return false;
        }
        if (StringUtil.isNullOrEmpty(currCustomerSchedule.getName()))
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    MessageUtil.getResourceBundleMessage("customerSchedule.name")));
            return false;
        }
        if (currCustomerSchedule.getLstCatGroupPacks() == null || currCustomerSchedule.getLstCatGroupPacks().size() == 0)
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    MessageUtil.getResourceBundleMessage("customerSchedule.service")));
            return false;
        }
        return true;
    }

    public void saveOrUpdate()
    {
        try
        {
            if (!validate())
            {
                return;
            }
            if (currCustomerSchedule.getEmployeeSchedule() != null)
            {
                currCustomerSchedule.setEmployeeId(currCustomerSchedule.getEmployeeSchedule().getEmployeeId());
            }
            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType()))
            {
                if (currCustomerSchedule.getMember() != null)
                {
                    currCustomerSchedule.setCustomerId(currCustomerSchedule.getMember().getMemberId());
                }
                if (currCustomerSchedule.getCustomerId() == null)
                {

                    MessageUtil.setErrorMessage("Hội viên bắt buộc nhập!");
                    return;
                }
            }
            else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType()))
            {
                Client client = new Client();
                if (currCustomerSchedule.getClient() != null)
                {
                    client = currCustomerSchedule.getClient();
//                    client.setClientId(currCustomerSchedule.getCustomerId());

                }
                else
                {
                    client.setStatus(Constant.CLIENT_STATUS.RESERVE);
                }
                client.setName(currCustomerSchedule.getName());
                client.setPhoneNumber(currCustomerSchedule.getPhoneNumber());
                client.setSex(currCustomerSchedule.getSex());
                client.setStartTime(currCustomerSchedule.getStartTime());
//                CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
//                if (user != null && user.getEmployee() != null) {
                client.setBranchId(currBranch.getBranchId());
//                }
                client.setEmployeeId(currCustomerSchedule.getEmployeeId());

                clientService.saveOrUpdate(client);
                currCustomerSchedule.setCustomerId(client.getClientId());
            }
            if (currCustomerSchedule.getEmployee() != null)
            {
                currCustomerSchedule.setEmpId(currCustomerSchedule.getEmployee().getEmployeeId());
            }
            //bo sung end time mac dinh
            if (currCustomerSchedule.getEndTime() == null && currCustomerSchedule.getLstCatGroupPacks() != null)
            {
                Long period = 0l;
                for (CatGroupPack bo : currCustomerSchedule.getLstCatGroupPacks())
                {
                    if (bo.getPeriod() != null)
                    {
                        period += bo.getPeriod();
                    }
                    else
                    {
                        period += 60L;
                    }
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currCustomerSchedule.getStartTime());
                calendar.add(Calendar.MINUTE, period.intValue());
                Date endate = calendar.getTime();
                currCustomerSchedule.setEndTime(endate);
            }
            customerScheduleService.saveOrUpdate(currCustomerSchedule);
            //
            if (Constant.CUSTOMER_SCHEDULE.STATUS_CANCEL.equals(currCustomerSchedule.getStatus()))
            {
                offUsingPack(currCustomerSchedule);
            }

            List<Membership> lstMemberShipDel = new ArrayList<>();
            List<Membership> lstMemberShipAdd = new ArrayList<>();

            List<ClientUsePack> lstClientUsePackDel = new ArrayList<>();
            List<ClientUsePack> lstClientUsePackAdd = new ArrayList<>();
            List<Long> lstGroupPackIdDel = new ArrayList();

            List<CustomerSchedulePack> lstSelect = new ArrayList<>();
            List<CustomerSchedulePack> lstCspDel = new ArrayList<>();
            List<CustomerSchedulePack> lstCspResult = new ArrayList<>();

            if (currCustomerSchedule.getLstCatGroupPacks() != null)
            {
                for (CatGroupPack bo : currCustomerSchedule.getLstCatGroupPacks())
                {
                    lstSelect.add(new CustomerSchedulePack(null, currCustomerSchedule.getCustomerScheduleId(), bo.getGroupPackId(), null, bo));
                }
            }
            if (currCustomerSchedule.getLstCustomerSchedulePack() != null
                    && currCustomerSchedule.getLstCustomerSchedulePack().size() > 0)
            {
                for (CustomerSchedulePack bo : currCustomerSchedule.getLstCustomerSchedulePack())
                {
                    if (lstSelect.contains(bo))
                    {
                        lstCspResult.add(bo);
                    }
                    else
                    {
                        lstCspDel.add(bo);
                        lstGroupPackIdDel.add(bo.getGroupPackId());
                    }
                }
            }
            for (CustomerSchedulePack bo : lstSelect)
            {
                if (!lstCspResult.contains(bo))
                {
                    lstCspResult.add(bo);

                    if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType()))
                    {
                        Membership m = new Membership();
                        m.setMemberId(currCustomerSchedule.getCustomerId());
                        m.setGroupPackId(bo.getGroupPackId());
                        m.setCustomerScheduleId(currCustomerSchedule.getCustomerScheduleId());
                        m.setStatus(Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);
                        m.setJoinDate(currCustomerSchedule.getStartTime());
                        m.setEndDate(currCustomerSchedule.getEndTime());
                        lstMemberShipAdd.add(m);
                    }
                    else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType()))
                    {
                        ClientUsePack m = new ClientUsePack();
                        m.setClientId(currCustomerSchedule.getCustomerId());
                        m.setGroupPackId(bo.getGroupPackId());
                        m.setCustomerScheduleId(currCustomerSchedule.getCustomerScheduleId());
                        m.setStatus(Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);
                        m.setJoinDate(currCustomerSchedule.getStartTime());
                        m.setEndDate(currCustomerSchedule.getEndTime());
                        lstClientUsePackAdd.add(m);
                    }
                }

            }
            customerSchedulePackService.delete(lstCspDel);
            customerSchedulePackService.saveOrUpdate(lstCspResult);
            // processs using pack
            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType()))
            {
                Map<String, Object> filter = new HashMap<>();
                filter.put("memberId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);
                filter.put("groupPackId", lstGroupPackIdDel);

                try
                {
                    if (lstMemberShipAdd.size() > 0)
                    {
                        membershipService.saveOrUpdate(lstMemberShipAdd);
                    }
                    if (lstGroupPackIdDel.size() > 0)
                    {
                        lstMemberShipDel = membershipService.findList(filter);
                        membershipService.delete(lstMemberShipDel);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType()))
            {
                Map<String, Object> filter = new HashMap<>();
                filter.put("clientId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);
                filter.put("groupPackId", lstGroupPackIdDel);

                try
                {
                    if (lstClientUsePackAdd.size() > 0)
                    {
                        clientUsePackService.saveOrUpdate(lstClientUsePackAdd);
                    }
                    if (lstGroupPackIdDel.size() > 0)
                    {

                        lstClientUsePackDel = clientUsePackService.findList(filter);
                        clientUsePackService.delete(lstClientUsePackDel);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
//bieu do
            addEvent();
            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currCustomerSchedule.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currCustomerSchedule.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            RequestContext.getCurrentInstance().execute("PF('widgetCustomerScheduleFormDlg').hide();");

            MessageUtil.setInfoMessageFromRes("info.save.success");
            preAdd();
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CustomerSchedule customerSchedule)
    {
        isEdit = true;
        currCustomerSchedule = customerSchedule;
        oldObjectStr = customerSchedule.toString();
        try
        {
            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType()))
            {
                Member member = memberService.findById(customerSchedule.getCustomerId());
                currCustomerSchedule.setMember(member);

                currCustomerSchedule.setName(member.getMemberName());
                currCustomerSchedule.setPhoneNumber(member.getPhoneNumber());
                currCustomerSchedule.setSex(member.getSex());
            }
            else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType()))
            {
                Client member = clientService.findById(customerSchedule.getCustomerId());
                currCustomerSchedule.setClient(member);

                currCustomerSchedule.setName(member.getName());
                currCustomerSchedule.setPhoneNumber(member.getPhoneNumber());
                currCustomerSchedule.setSex(member.getSex());
            }
            Map<String, Object> filter = new HashMap<>();
            filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
            List<CustomerSchedulePack> lst = customerSchedulePackService.findList(filter);
            currCustomerSchedule.setLstCustomerSchedulePack(lst);
            List<CatGroupPack> lstCatGroupPack = new ArrayList<>();
            if (lst != null && lst.size() > 0)
            {
                for (CustomerSchedulePack bo : lst)
                {
                    if (bo.getCatGroupPack() != null)
                    {
                        lstCatGroupPack.add(bo.getCatGroupPack());
                    }
                }
            }
            currCustomerSchedule.setLstCatGroupPacks(lstCatGroupPack);

            if (customerSchedule.getEmpId() != null)
            {
                currCustomerSchedule.setEmployee(employeeService.findById(customerSchedule.getEmpId()));
            }
            if (customerSchedule.getEmployeeId() != null)
            {
                currCustomerSchedule.setEmployeeSchedule(employeeService.findById(customerSchedule.getEmployeeId()));
            }
            oldStatus = customerSchedule.getStatus();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void preCancel(CustomerSchedule customerSchedule)
    {
        preEdit(customerSchedule);
        customerSchedule.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_CANCEL);
    }

    public void delete(CustomerSchedule currCustomerSchedule)
    {
        try
        {
            customerScheduleService.delete(currCustomerSchedule);

            //del pack
            offUsingPack(currCustomerSchedule);
            /*
            List<Membership> lstMemberShipDel = new ArrayList<>();
            List<ClientUsePack> lstClientUsePackDel = new ArrayList<>();

            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType())) {
                Map<String, Object> filter = new HashMap<>();
                filter.put("memberId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);

                try {
                    lstMemberShipDel = membershipService.findList(filter);
                    membershipService.delete(lstMemberShipDel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType())) {
                Map<String, Object> filter = new HashMap<>();
                filter.put("memberId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);

                try {
                    lstClientUsePackDel = clientUsePackService.findList(filter);
                    clientUsePackService.delete(lstClientUsePackDel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
             */
            List<CustomerSchedulePack> lstCspDel = new ArrayList<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());

            try
            {
                lstCspDel = customerSchedulePackService.findList(filter);
                if (lstCspDel != null)
                {
                    customerSchedulePackService.delete(lstCspDel);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currCustomerSchedule.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public void offUsingPack(CustomerSchedule currCustomerSchedule)
    {
        try
        {
            //del pack
            List<Membership> lstMemberShipDel = new ArrayList<>();
            List<ClientUsePack> lstClientUsePackDel = new ArrayList<>();

            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(currCustomerSchedule.getCustomerType()))
            {
                Map<String, Object> filter = new HashMap<>();
                filter.put("memberId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);

                try
                {

                    lstMemberShipDel = membershipService.findList(filter);
                    membershipService.delete(lstMemberShipDel);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(currCustomerSchedule.getCustomerType()))
            {
                Map<String, Object> filter = new HashMap<>();
                filter.put("memberId", currCustomerSchedule.getCustomerId());
                filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
                filter.put("status", Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE);

                try
                {
                    Client m = currCustomerSchedule.getClient();
                    if (m != null)
                    {
                        m.setStatus(Constant.CLIENT_STATUS.STOP);
                        clientService.saveOrUpdate(m);
                    }
                    lstClientUsePackDel = clientUsePackService.findList(filter);
                    clientUsePackService.delete(lstClientUsePackDel);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
//            List<CustomerSchedulePack> lstCspDel = new ArrayList<>();
//            Map<String, Object> filter = new HashMap<>();
//            filter.put("customerScheduleId", currCustomerSchedule.getCustomerScheduleId());
//
//            try {
//                lstCspDel = customerSchedulePackService.findList(filter);
//                if (lstCspDel != null) {
//                    customerSchedulePackService.delete(lstCspDel);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currCustomerSchedule.toString(), null, this.getClass().getSimpleName(),
//                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (Exception e)
        {
//            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public void updateStatus(CustomerSchedule customerSchedule, Long newStatus)
    {
        try
        {
            String old = customerSchedule.toString();
            customerSchedule.setStatus(newStatus);
            customerScheduleService.saveOrUpdate(customerSchedule);
            if (Constant.CUSTOMER_SCHEDULE.STATUS_CANCEL.equals(newStatus))
            {
                offUsingPack(customerSchedule);
            }
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, customerSchedule.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public List<CatGroupPack> searchGroupPack(String key)
    {
        ConditionQuery query = new ConditionQuery();
        Criterion criterion = Restrictions.or(Restrictions.ilike("groupPackName", key, MatchMode.ANYWHERE));
        query.add(criterion);

        query.add(Restrictions.ne("status", Constant.MEMBER_STATUS.DELETE));

        OrderBy orderBy = new OrderBy();
        return catGroupPackService.findList(query, orderBy, 1, 100);
    }
// bieu do

    public void loadSchedule()
    {
        scheduleGym = new lazyCustomerScheduleGym(customerScheduleService, currBranch);
//                scheduleGym = new LazyScheduleGym( new GenericDaoImplNewV2<Schedule, Long>() {
//        }, currBranch, new CatRoom(),new CatPack());

    }

    public void onDateSelect(SelectEvent selectEvent)
    {
        Date dateSelected = (Date) selectEvent.getObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateSelected);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        event = new DefaultScheduleEvent("", dateSelected, calendar.getTime());
//        CustomerSchedule schedule = new CustomerSchedule();
        preAdd();
//        CatRoom room = new CatRoom();
//        room.setBranch(currBranch);
//        room.setBranchId(currBranch.getBranchId());
//        schedule.setRoom(room);

//        schedule.setCatBranch(currBranch);
        currCustomerSchedule.setCatBranch(currBranch);
        currCustomerSchedule.setStartTime(dateSelected);
//        currCustomerSchedule.setEndTime(calendar.getTime());
        ((DefaultScheduleEvent) event).setData(currCustomerSchedule);
    }

    public void onEventSelect(SelectEvent selectEvent)
    {
        event = (ScheduleEvent) selectEvent.getObject();
        preEdit((CustomerSchedule) event.getData());
    }

    public void onEventMove(ScheduleEntryMoveEvent event)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

    }

    public void onEventResize(ScheduleEntryResizeEvent event)
    {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

    }

    public void onChangeView()
    {
//        System.out.println("kld");
    }

    public void addEvent()
    {
        try
        {
            if (!isEdit)
            {
                scheduleGym.addEvent(event);
            }
            else
            {
                scheduleGym.updateEvent(event);
            }
            event = new DefaultScheduleEvent();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public GenericDaoImplNewV2<Client, Long> getClientService()
    {
        return clientService;
    }

    public void setClientService(GenericDaoImplNewV2<Client, Long> clientService)
    {
        this.clientService = clientService;
    }

    public GenericDaoImplNewV2<Member, Long> getMemberService()
    {
        return memberService;
    }

    public void setMemberService(GenericDaoImplNewV2<Member, Long> memberService)
    {
        this.memberService = memberService;
    }

    public GenericDaoImplNewV2<CustomerSchedule, Long> getCustomerScheduleService()
    {
        return customerScheduleService;
    }

    public void setCustomerScheduleService(GenericDaoImplNewV2<CustomerSchedule, Long> customerScheduleService)
    {
        this.customerScheduleService = customerScheduleService;
    }

    public GenericDaoImplNewV2<CustomerSchedulePack, Long> getCustomerSchedulePackService()
    {
        return customerSchedulePackService;
    }

    public void setCustomerSchedulePackService(GenericDaoImplNewV2<CustomerSchedulePack, Long> customerSchedulePackService)
    {
        this.customerSchedulePackService = customerSchedulePackService;
    }

    public GenericDaoServiceNewV2<CatGroupPack, Long> getCatGroupPackService()
    {
        return catGroupPackService;
    }

    public void setCatGroupPackService(GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService)
    {
        this.catGroupPackService = catGroupPackService;
    }

    public GenericDaoImplNewV2<Membership, Long> getMembershipService()
    {
        return membershipService;
    }

    public void setMembershipService(GenericDaoImplNewV2<Membership, Long> membershipService)
    {
        this.membershipService = membershipService;
    }

    public GenericDaoImplNewV2<ClientUsePack, Long> getClientUsePackService()
    {
        return clientUsePackService;
    }

    public void setClientUsePackService(GenericDaoImplNewV2<ClientUsePack, Long> clientUsePackService)
    {
        this.clientUsePackService = clientUsePackService;
    }

    public LazyDataModel<CustomerSchedule> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CustomerSchedule> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public CustomerSchedule getCurrCustomerSchedule()
    {
        return currCustomerSchedule;
    }

    public void setCurrCustomerSchedule(CustomerSchedule currCustomerSchedule)
    {
        this.currCustomerSchedule = currCustomerSchedule;
    }

    public boolean isIsEdit()
    {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
    }

    public String getOldObjectStr()
    {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr)
    {
        this.oldObjectStr = oldObjectStr;
    }

    public List<CatGroupPack> getCatGroupPacks()
    {
        return catGroupPacks;
    }

    public void setCatGroupPacks(List<CatGroupPack> catGroupPacks)
    {
        this.catGroupPacks = catGroupPacks;
    }

    public GenericDaoImplNewV2<Employee, Long> getEmployeeService()
    {
        return employeeService;
    }

    public void setEmployeeService(GenericDaoImplNewV2<Employee, Long> employeeService)
    {
        this.employeeService = employeeService;
    }

    public Long getOldStatus()
    {
        return oldStatus;
    }

    public void setOldStatus(Long oldStatus)
    {
        this.oldStatus = oldStatus;
    }

    //</editor-fold>
    public ScheduleModel getScheduleGym()
    {
        return scheduleGym;
    }

    public void setScheduleGym(ScheduleModel scheduleGym)
    {
        this.scheduleGym = scheduleGym;
    }

    public ScheduleEvent getEvent()
    {
        return event;
    }

    public void setEvent(ScheduleEvent event)
    {
        this.event = event;
    }

    public CatBranch getCurrBranch()
    {
        return currBranch;
    }

    public void setCurrBranch(CatBranch currBranch)
    {
        this.currBranch = currBranch;
    }

}
