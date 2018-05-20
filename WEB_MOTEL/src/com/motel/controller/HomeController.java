/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.GroupUser;
import com.motel.model.Home;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.GroupUserServiceImpl;
import com.slook.persistence.HomeServiceImpl;
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
 * @author VietNV May 20, 2018
 */
@ManagedBean
@ViewScoped
public class HomeController {

    protected static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    LazyDataModel<Home> lazyDataModel;
    Home currHome = new Home();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<GroupUser> lstGroupUser;
    Long groupUserId = null;

    // customerHome
    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {

        try {
            CatUser catUser = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("groupUserId", Constant.ORDER.ASC);
            order.put("homeName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<Home, Long>(HomeServiceImpl.getInstance(), filter, order);

            Map<String, Object> filtersGroupUser = new HashMap<>();
            filtersGroupUser.put("status-NEQ", Constant.STATUS.DELETE);
//            if (groupUserId != null && groupUserId > 0) {//phan quyen
//                filtersGroupUser.put("groupUserId", groupUserId);
//            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("name", Constant.ORDER.ASC);
            lstGroupUser = GroupUserServiceImpl.getInstance().findList(filtersGroupUser, orderHome);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        init();
        columnVisibale = Arrays.asList(true, true, true, true, false,
                true, false, true, true, true
        );
    }

    public void preAdd() {
        this.currHome = new Home();
        currHome.setGroupUserId(groupUserId);
        isEdit = false;
    }

    public void preEdit(Home home) {
        try {
            currHome = HomeServiceImpl.getInstance().findById(home.getHomeId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currHome.toString();
    }

    public void onSaveOrUpdate() {
        try {

            // validate
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            filter.put("homeName-EXAC_IGNORE_CASE", currHome.getHomeName());
            filter.put("groupUserId", currHome.getGroupUserId());
            if (currHome.getHomeId() != null) {
                filter.put("homeId-NEQ", currHome.getHomeId());
            }

            List lst = HomeServiceImpl.getInstance().findList(filter);
            if (lst != null && !lst.isEmpty()) {
                MessageUtil.setInfoMessage("Tên khu trọ đã tồn tại!");

                return;
            }
            HomeServiceImpl.getInstance().saveOrUpdate(currHome);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currHome.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currHome.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('homeDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(Home home) {
        try {
            home.setStatus(Constant.STATUS.DELETE);
            HomeServiceImpl.getInstance().saveOrUpdate(home);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public LazyDataModel<Home> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Home> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Home getCurrHome() {
        return currHome;
    }

    public void setCurrHome(Home currHome) {
        this.currHome = currHome;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
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

    public List<GroupUser> getLstGroupUser() {
        return lstGroupUser;
    }

    public void setLstGroupUser(List<GroupUser> lstGroupUser) {
        this.lstGroupUser = lstGroupUser;
    }

    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
    }
//</editor-fold>

}
