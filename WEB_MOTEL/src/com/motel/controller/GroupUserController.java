/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.motel.controller;

import com.motel.model.GroupUser;
import com.motel.model.GroupUser;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.GroupUserServiceImpl;
import com.slook.persistence.GroupUserServiceImpl;
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
 * @author VietNV May 22, 2018
 */
@ManagedBean
@ViewScoped 
public class GroupUserController {

    protected static final Logger logger = LoggerFactory.getLogger(GroupUserController.class);

    LazyDataModel<GroupUser> lazyDataModel;
    GroupUser currGroupUser = new GroupUser();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
//    List<GroupUser> lstGroupUser;
//    Long groupUserId = null;

    // customerGroupUser
    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {

        try {
            CatUser catUser = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
//                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("name", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            
            lazyDataModel = new LazyDataModelBase<GroupUser, Long>(GroupUserServiceImpl.getInstance(), filter, order);

           
        } catch (Exception e) {
            e.printStackTrace();
        }
//        init();
        columnVisibale = Arrays.asList(true, true, true, false, true,
                true, true, true, false,false, true
        );
    }

    public void preAdd() {
        this.currGroupUser = new GroupUser();
        currGroupUser.setCreateTime(new Date());
        currGroupUser.setCode(DateTimeUtils.format(new Date(), "yyyyMMddHHmmss"));
        isEdit = false;
    }

    public void preEdit(GroupUser groupUser) {
        try {
            currGroupUser = GroupUserServiceImpl.getInstance().findById(groupUser.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currGroupUser.toString();
    }

    public void onSaveOrUpdate() {
        try {

            // validate
           
         
            GroupUserServiceImpl.getInstance().saveOrUpdate(currGroupUser);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currGroupUser.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currGroupUser.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('groupUserDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(GroupUser groupUser) {
        try {
            groupUser.setStatus(Constant.STATUS.DELETE);
            GroupUserServiceImpl.getInstance().saveOrUpdate(groupUser);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<GroupUser> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<GroupUser> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public GroupUser getCurrGroupUser() {
        return currGroupUser;
    }

    public void setCurrGroupUser(GroupUser currGroupUser) {
        this.currGroupUser = currGroupUser;
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
