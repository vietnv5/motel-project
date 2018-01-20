/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import static com.motel.controller.RoomController.logger;
import com.motel.model.Customer;
import com.motel.model.Home;
import com.motel.model.Customer;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.CustomerServiceImpl;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.CustomerServiceImpl;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author VietNV Jan 19, 2018
 */
@ManagedBean
@ViewScoped
public class CustomerController {

    protected static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    LazyDataModel<Customer> lazyDataModel;
    Customer currCustomer = new Customer();
    private String oldObjectStr = null;
    private Long groupUserId;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {

        try {
            CatUser catUser = null;
            groupUserId = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("customerName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<Customer, Long>(CustomerServiceImpl.getInstance(), filter, order);

        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                false,false,false, true, false, false, false
        );
    }

    public void preAdd() {
        this.currCustomer = new Customer();
        if (groupUserId != null && groupUserId > 0) {
            currCustomer.setGroupUserId(groupUserId);
        }
        isEdit = false;
    }

    public void preEdit(Customer customer) {
        try {
            currCustomer = CustomerServiceImpl.getInstance().findById(customer.getCustomerId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currCustomer.toString();
    }

    public void onSaveOrUpdate() {
        try {
            if (currCustomer.getStatus() == null) {
                currCustomer.setStatus(Constant.STATUS.ACTIVE);
            }
            CustomerServiceImpl.getInstance().saveOrUpdate(currCustomer);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currCustomer.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currCustomer.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('customerDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(Customer catRole) {
        try {
            CustomerServiceImpl.getInstance().delete(catRole);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<Customer> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Customer> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Customer getCurrCustomer() {
        return currCustomer;
    }

    public void setCurrCustomer(Customer currCustomer) {
        this.currCustomer = currCustomer;
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

}
