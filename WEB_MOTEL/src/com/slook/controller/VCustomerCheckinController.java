package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.Client;
import com.slook.model.Member;
import com.slook.model.V_CustomerCheckin;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.VCustomerCheckinServiceImpl;
import com.slook.util.Constant;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.*;
import java.util.logging.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class VCustomerCheckinController {

    protected static final Logger logger = LoggerFactory.getLogger(VCustomerCheckinController.class);
    @ManagedProperty(value = "#{vCustomerCheckinService}")
    private VCustomerCheckinServiceImpl vCustomerCheckinService;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private LazyDataModel<V_CustomerCheckin> lazyDataModel;
    private LazyDataModel<V_CustomerCheckin> lazyCheckInGroup;

    private boolean showGroupTable = false;
//    private List<V_CustomerCheckin> listCustomerCheckinGroup = new ArrayList<>();

    @PostConstruct
    public void onStart() {
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> oder = new LinkedHashMap<>();
        oder.put("checkTime", Constant.ORDER.DESC);
        try {
            lazyDataModel = new LazyDataModelBase<>(vCustomerCheckinService, filter, oder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestContext.getCurrentInstance().execute("PF('tblWidgetId').filter();");

    }

    public void preShowGroupTable(V_CustomerCheckin customerCheckin) {
//        listCustomerCheckinGroup.clear();
        if (customerCheckin.getGroupMemberName() == null) {
            return;
        }
        this.showGroupTable = true;
        Map<String, Object> filter = new HashMap<>();
        filter.put("groupMemberId", customerCheckin.getGroupMemberId());
//        filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("checkTime", Constant.ORDER.DESC);
        try {
//            listCustomerCheckinGroup = vCustomerCheckinService.findList(filter, order);
            lazyCheckInGroup= new LazyDataModelBase<>(vCustomerCheckinService, filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeGroupTable() {
        this.showGroupTable = false;
//        listCustomerCheckinGroup.clear();
    }

    public Client getClient(V_CustomerCheckin customCheckin) {
        Client bo = null;
        try {
            bo = new GenericDaoImplNewV2<Client, Long>() {
            }.findById(customCheckin.getCustomerId());
        } catch (AppException ex) {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SysException ex) {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bo;
    }

    public Member getMember(V_CustomerCheckin customCheckin) {
        Member bo = null;
        try {
            bo = new GenericDaoImplNewV2<Member, Long>() {
            }.findById(customCheckin.getCustomerId());
        } catch (AppException ex) {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SysException ex) {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bo;
    }

    public void onRowSelect(SelectEvent event) {
        V_CustomerCheckin customerCheckin = (V_CustomerCheckin) event.getObject();
        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(customerCheckin.getType()) && customerCheckin.getGroupMemberId()!=null) {
            preShowGroupTable(customerCheckin);
        } else {
            closeGroupTable();
        }
    }

    public VCustomerCheckinServiceImpl getvCustomerCheckinService() {
        return vCustomerCheckinService;
    }

    public void setvCustomerCheckinService(VCustomerCheckinServiceImpl vCustomerCheckinService) {
        this.vCustomerCheckinService = vCustomerCheckinService;
    }

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public LazyDataModel<V_CustomerCheckin> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<V_CustomerCheckin> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public static Logger getLogger() {
        return logger;
    }

    public boolean getShowGroupTable() {
        return showGroupTable;
    }

    public void setShowGroupTable(boolean showGroupTable) {
        this.showGroupTable = showGroupTable;
    }

//    public List<V_CustomerCheckin> getListCustomerCheckinGroup() {
//        return listCustomerCheckinGroup;
//    }
//
//    public void setListCustomerCheckinGroup(List<V_CustomerCheckin> listCustomerCheckinGroup) {
//        this.listCustomerCheckinGroup = listCustomerCheckinGroup;
//    }

    public LazyDataModel<V_CustomerCheckin> getLazyCheckInGroup() {
        return lazyCheckInGroup;
    }

    public void setLazyCheckInGroup(LazyDataModel<V_CustomerCheckin> lazyCheckInGroup) {
        this.lazyCheckInGroup = lazyCheckInGroup;
    }
    
}
