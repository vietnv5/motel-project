/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.lazy.LazyDataModelBase;
import com.slook.model.V_CustomerCheckin;
import com.slook.model.V_CustomerPayment;
import com.slook.model.V_ServiceTicket;
import com.slook.object.PaymentGroupPackForm;
import com.slook.object.PaymentSearchForm;
import com.slook.persistence.VCustomerCheckinServiceImpl;
import com.slook.persistence.VCustomerPaymentServiceImpl;
import com.slook.persistence.VMemberPaymentServiceImpl;
import com.slook.persistence.VServiceTicketServiceImpl;
import com.slook.util.Constant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author SLOOK. JSC on Nov 7, 2017 2:13:46 PM
 */
@ManagedBean
@ViewScoped
public class ReportPaymentController {

    private static final Logger logger = getLogger(ReportPaymentController.class);
    private List<PaymentGroupPackForm> lstPaymentGroupPackTotal;
    private PaymentGroupPackForm paymentGroupPackTotal = new PaymentGroupPackForm();

    private PaymentSearchForm paymentSearchForm = new PaymentSearchForm();
    private boolean showDetailTable = false;
    private LazyDataModel<V_CustomerPayment> lazyCustomerPaymentDeltail;
    private Long typeReportCurr;

    private LazyDataModel<V_CustomerCheckin> lazyDataModel;
    LazyDataModel<V_ServiceTicket> lazyServiceTicket;

    @PostConstruct
    public void onStart() {
        /*
        if (paymentSearchForm.getType() != null && paymentSearchForm.getType().equals(-1l)) {
            paymentSearchForm.setType(null);
        }
        lstPaymentGroupPackTotal = VMemberPaymentServiceImpl.searchListPaymentGroupPack(paymentSearchForm);
        paymentGroupPackTotal = VMemberPaymentServiceImpl.totalPaymentGroupPack(paymentSearchForm);
         */
    }

    public void closeDetailTable() {
        this.showDetailTable = false;
    }

    public void onRowSelect(SelectEvent event) {
        PaymentGroupPackForm paymentGroup = (PaymentGroupPackForm) event.getObject();
//        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(paymentGroup.getType()) && paymentGroup.getGroupMemberId() != null) {
        preShowGroupTable(paymentGroup);
//        } else {
//            closeGroupTable();
//        }
    }

    public void preShowGroupTable(PaymentGroupPackForm paymentGroup) {

        Map<String, Object> filter = new HashMap<>();
//        filter.put("groupMemberId", paymentGroup.getCustomerId());

        if (paymentSearchForm.getType() != null) {
            filter.put("customerType", paymentSearchForm.getType());
        }
        Date toDate = paymentSearchForm.getToDate();
        Calendar cd = Calendar.getInstance();

        if (Constant.REPORT_TYPE.REPORT_PAYMENT_GROUP_PACK.equals(typeReportCurr)) {
            filter.put("groupPackId", paymentGroup.getGroupPackId());
            if (toDate != null) {
                cd.setTime(toDate);
                cd.add(Calendar.DATE, 1);
                toDate = cd.getTime();
            }
        } else if (Constant.REPORT_TYPE.REPORT_EMPLOYEE_PAYMENT.equals(typeReportCurr)) {
            filter.put("employeeId", paymentGroup.getEmployeeId());
            if (toDate != null) {
                cd.setTime(toDate);
                cd.add(Calendar.DATE, 1);
                toDate = cd.getTime();
            }
        } else if (Constant.REPORT_TYPE.REPORT_CUSTOMER_PAYMENT.equals(typeReportCurr)) {
            filter.put("customerId", paymentGroup.getCustomerId());
            if (toDate != null) {
                cd.setTime(toDate);
                cd.add(Calendar.DATE, 1);
                toDate = cd.getTime();
            }
            if (paymentGroup.getCustomerType() != null) {
                filter.put("customerType", paymentGroup.getCustomerType());
            }
        } else {
            return;
        }
        if (paymentSearchForm.getFromDate() != null) {
            filter.put("createTime-GE", paymentSearchForm.getFromDate());
        }
        if (toDate != null) {
            filter.put("createTime-LE", toDate);
        }

        this.showDetailTable = true;
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("createTime", Constant.ORDER.DESC);
        try {
            lazyCustomerPaymentDeltail = new LazyDataModelBase<>(VCustomerPaymentServiceImpl.getInstance(), filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search() {
        if (paymentSearchForm.getType() != null && paymentSearchForm.getType().equals(-1l)) {
            paymentSearchForm.setType(null);
        }
        lstPaymentGroupPackTotal = VMemberPaymentServiceImpl.searchListPaymentGroupPack(paymentSearchForm);
        paymentGroupPackTotal = VMemberPaymentServiceImpl.totalPaymentGroupPack(paymentSearchForm);
        typeReportCurr = Constant.REPORT_TYPE.REPORT_PAYMENT_GROUP_PACK;
    }

    public void searchEmployeePayment() {
        if (paymentSearchForm.getType() != null && paymentSearchForm.getType().equals(-1l)) {
            paymentSearchForm.setType(null);
        }
        lstPaymentGroupPackTotal = VMemberPaymentServiceImpl.searchListEmployeePayment(paymentSearchForm);
        paymentGroupPackTotal = VMemberPaymentServiceImpl.totalPaymentGroupPack(paymentSearchForm);
        typeReportCurr = Constant.REPORT_TYPE.REPORT_EMPLOYEE_PAYMENT;
    }

    public void searchListUseGroupPack() {
        if (paymentSearchForm.getType() != null && paymentSearchForm.getType().equals(-1l)) {
            paymentSearchForm.setType(null);
        }
        lstPaymentGroupPackTotal = VMemberPaymentServiceImpl.searchListUseGroupPack(paymentSearchForm);
        typeReportCurr = Constant.REPORT_TYPE.REPORT_USE_GROUP_PACK;
    }

    public void searchCustomerPayment() {
        if (paymentSearchForm.getType() != null && paymentSearchForm.getType().equals(-1l)) {
            paymentSearchForm.setType(null);
        }
        lstPaymentGroupPackTotal = VMemberPaymentServiceImpl.searchListCustomerPayment(paymentSearchForm);
        paymentGroupPackTotal = VMemberPaymentServiceImpl.totalPaymentGroupPack(paymentSearchForm);

        typeReportCurr = Constant.REPORT_TYPE.REPORT_CUSTOMER_PAYMENT;
        RequestContext.getCurrentInstance().execute("PF('widTableIpRanges').clearFilters();");
    }

    public List<PaymentGroupPackForm> getLstPaymentGroupPackTotal() {
        return lstPaymentGroupPackTotal;
    }

    public void setLstPaymentGroupPackTotal(List<PaymentGroupPackForm> lstPaymentGroupPackTotal) {
        this.lstPaymentGroupPackTotal = lstPaymentGroupPackTotal;
    }

    public PaymentSearchForm getPaymentSearchForm() {
        return paymentSearchForm;
    }

    public void setPaymentSearchForm(PaymentSearchForm paymentSearchForm) {
        this.paymentSearchForm = paymentSearchForm;
    }

    public PaymentGroupPackForm getPaymentGroupPackTotal() {
        return paymentGroupPackTotal;
    }

    public void setPaymentGroupPackTotal(PaymentGroupPackForm paymentGroupPackTotal) {
        this.paymentGroupPackTotal = paymentGroupPackTotal;
    }

    public boolean isShowDetailTable() {
        return showDetailTable;
    }

    public void setShowDetailTable(boolean showDetailTable) {
        this.showDetailTable = showDetailTable;
    }

    public LazyDataModel<V_CustomerPayment> getLazyCustomerPaymentDeltail() {
        return lazyCustomerPaymentDeltail;
    }

    public void setLazyCustomerPaymentDeltail(LazyDataModel<V_CustomerPayment> lazyCustomerPaymentDeltail) {
        this.lazyCustomerPaymentDeltail = lazyCustomerPaymentDeltail;
    }

    public Long getTypeReportCurr() {
        return typeReportCurr;
    }

    public void setTypeReportCurr(Long typeReportCurr) {
        this.typeReportCurr = typeReportCurr;
    }

    public LazyDataModel<V_CustomerCheckin> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<V_CustomerCheckin> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public LazyDataModel<V_ServiceTicket> getLazyServiceTicket() {
        return lazyServiceTicket;
    }

    public void setLazyServiceTicket(LazyDataModel<V_ServiceTicket> lazyServiceTicket) {
        this.lazyServiceTicket = lazyServiceTicket;
    }

    public void onRowSelectUsePack(SelectEvent event) {
        PaymentGroupPackForm paymentGroup = (PaymentGroupPackForm) event.getObject();
        preShowGroupTableUsePack(paymentGroup);
    }

    public void preShowGroupTableUsePack(PaymentGroupPackForm paymentGroup) {

        Map<String, Object> filter = new HashMap<>();
//        filter.put("groupMemberId", paymentGroup.getCustomerId());

        if (paymentSearchForm.getType() != null) {
            filter.put("type", paymentSearchForm.getType());
        }
        Date toDate = paymentSearchForm.getToDate();

        if (Constant.REPORT_TYPE.REPORT_USE_GROUP_PACK.equals(typeReportCurr)) {
            filter.put("groupPackId", paymentGroup.getGroupPackId());

        } else {
            return;
        }
        if (paymentSearchForm.getFromDate() != null) {
            filter.put("checkTime-GE", paymentSearchForm.getFromDate());
        }
        if (toDate != null) {
            filter.put("checkTime-LE", toDate);
        }

        this.showDetailTable = true;
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("checkTime", Constant.ORDER.DESC);
        try {
            lazyDataModel = new LazyDataModelBase<>(VCustomerCheckinServiceImpl.getInstance(), filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReportPaymentController rp = new ReportPaymentController();
        List<PaymentGroupPackForm> lst = VMemberPaymentServiceImpl.searchListEmployeePayment(new PaymentSearchForm());
        System.out.println("lst:" + lst);
    }

    //report thong ke ky thuat vien
    public void searchListReportServiceTicket() {

        lstPaymentGroupPackTotal = VServiceTicketServiceImpl.searchListEmployeePaymentGroupPack(paymentSearchForm);
        typeReportCurr = Constant.REPORT_TYPE.REPORT_USE_GROUP_PACK;
    }

    public void preShowServiceTicket(PaymentGroupPackForm paymentGroup) {

        Map<String, Object> filter = new HashMap<>();
//        filter.put("groupMemberId", paymentGroup.getCustomerId());

        filter.put("status", Constant.SERVICE_TICKET.STATUS_USED);
        filter.put("employeeId",paymentGroup.getEmployeeId());
        filter.put("groupPackId",paymentGroup.getGroupPackId());

        Date toDate = paymentSearchForm.getToDate();
        if (paymentSearchForm.getFromDate() != null) {
            filter.put("usedTime-GE", paymentSearchForm.getFromDate());
        }
        if (toDate != null) {
            filter.put("usedTime-LE", toDate);
        }

        this.showDetailTable = true;
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("usedTime", Constant.ORDER.DESC);
        try {
            lazyServiceTicket = new LazyDataModelBase<>(VServiceTicketServiceImpl.getInstance(), filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onRowSelectServiceTicket(SelectEvent event) {
        PaymentGroupPackForm paymentGroup = (PaymentGroupPackForm) event.getObject();
        preShowServiceTicket(paymentGroup);

    }

}
