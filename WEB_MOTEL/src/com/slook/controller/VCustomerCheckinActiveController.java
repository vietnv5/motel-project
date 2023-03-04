/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.Client;
import com.slook.model.Member;
import com.slook.model.V_CustomerCheckin;
import com.slook.model.V_CustomerCheckinActive;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.VCustomerCheckinActiveServiceImpl;
import com.slook.persistence.VCustomerCheckinServiceImpl;
import com.slook.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SLOOK. JSC on Nov 3, 2017 2:59:25 PM
 */
@ManagedBean
@ViewScoped
public class VCustomerCheckinActiveController
{

    protected static final Logger logger = LoggerFactory.getLogger(VCustomerCheckinActiveController.class);
    @ManagedProperty(value = "#{vCustomerCheckinService}")
    private VCustomerCheckinServiceImpl vCustomerCheckinService;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private LazyDataModel<V_CustomerCheckinActive> lazyDataModel;
    private LazyDataModel<V_CustomerCheckin> lazyCheckInGroup;

    @ManagedProperty(value = "#{vCustomerCheckinActiveService}")
    private VCustomerCheckinActiveServiceImpl vCustomerCheckinActiveService;

    private boolean showGroupTable = false;
//    private List<V_CustomerCheckin> listCustomerCheckinGroup = new ArrayList<>();

    @PostConstruct
    public void onStart()
    {
        Map<String, Object> filter = new HashMap<>();
        Map<String, String> oder = new LinkedHashMap<>();
        oder.put("checkTime", Constant.ORDER.DESC);
        try
        {
            lazyDataModel = new LazyDataModelBase<>(vCustomerCheckinActiveService, filter, oder);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        RequestContext.getCurrentInstance().execute("PF('tblWidgetId').filter();");

    }

    public void preShowGroupTable(V_CustomerCheckinActive customerCheckin)
    {
//        listCustomerCheckinGroup.clear();
        if (customerCheckin.getGroupMemberName() == null)
        {
            return;
        }
        this.showGroupTable = true;
        Map<String, Object> filter = new HashMap<>();
        filter.put("groupMemberId", customerCheckin.getGroupMemberId());
//        filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("checkTime", Constant.ORDER.DESC);
        try
        {
//            listCustomerCheckinGroup = vCustomerCheckinService.findList(filter, order);
            lazyCheckInGroup = new LazyDataModelBase<>(vCustomerCheckinService, filter, order);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void closeGroupTable()
    {
        this.showGroupTable = false;
//        listCustomerCheckinGroup.clear();
    }

    public Client getClient(V_CustomerCheckinActive customCheckin)
    {
        Client bo = null;
        try
        {
            bo = new GenericDaoImplNewV2<Client, Long>()
            {
            }.findById(customCheckin.getCustomerId());
        }
        catch (AppException ex)
        {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SysException ex)
        {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bo;
    }

    public Member getMember(V_CustomerCheckinActive customCheckin)
    {
        Member bo = null;
        try
        {
            bo = new GenericDaoImplNewV2<Member, Long>()
            {
            }.findById(customCheckin.getCustomerId());
        }
        catch (AppException ex)
        {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SysException ex)
        {
            java.util.logging.Logger.getLogger(VCustomerCheckinController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bo;
    }

    public void onRowSelect(SelectEvent event)
    {
        V_CustomerCheckinActive customerCheckin = (V_CustomerCheckinActive) event.getObject();
        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(customerCheckin.getType()) && customerCheckin.getGroupMemberId() != null)
        {
            preShowGroupTable(customerCheckin);
        }
        else
        {
            closeGroupTable();
        }
    }

    public VCustomerCheckinServiceImpl getvCustomerCheckinService()
    {
        return vCustomerCheckinService;
    }

    public void setvCustomerCheckinService(VCustomerCheckinServiceImpl vCustomerCheckinService)
    {
        this.vCustomerCheckinService = vCustomerCheckinService;
    }

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public LazyDataModel<V_CustomerCheckinActive> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<V_CustomerCheckinActive> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public boolean getShowGroupTable()
    {
        return showGroupTable;
    }

    public void setShowGroupTable(boolean showGroupTable)
    {
        this.showGroupTable = showGroupTable;
    }

    //    public List<V_CustomerCheckin> getListCustomerCheckinGroup() {
//        return listCustomerCheckinGroup;
//    }
//
//    public void setListCustomerCheckinGroup(List<V_CustomerCheckin> listCustomerCheckinGroup) {
//        this.listCustomerCheckinGroup = listCustomerCheckinGroup;
//    }
    public LazyDataModel<V_CustomerCheckin> getLazyCheckInGroup()
    {
        return lazyCheckInGroup;
    }

    public void setLazyCheckInGroup(LazyDataModel<V_CustomerCheckin> lazyCheckInGroup)
    {
        this.lazyCheckInGroup = lazyCheckInGroup;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public VCustomerCheckinActiveServiceImpl getvCustomerCheckinActiveService()
    {
        return vCustomerCheckinActiveService;
    }

    public void setvCustomerCheckinActiveService(VCustomerCheckinActiveServiceImpl vCustomerCheckinActiveService)
    {
        this.vCustomerCheckinActiveService = vCustomerCheckinActiveService;
    }

}
