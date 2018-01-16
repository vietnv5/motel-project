/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.Home;
import com.motel.model.Room;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.RoomServiceImpl;
import com.slook.persistence.FunctionPathServiceImpl;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.RoomServiceImpl;
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
 * @author VietNV Jan 17, 2018
 */
@ManagedBean
@ViewScoped
public class RoomController {

    protected static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    LazyDataModel<Room> lazyDataModel;
    Room currRoom = new Room();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<Home> lstHome;

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {

        try {
            CatUser catUser = null;
            Long groupUserId = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = Long.valueOf(catUser.getGroupId());
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("homeId.homeName", Constant.ORDER.ASC);
            order.put("roomName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("homeId.groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<Room, Long>(RoomServiceImpl.getInstance(), filter, order);

            Map<String, Object> filtersHome = new HashMap<>();
            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filtersHome.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("homeName", Constant.ORDER.ASC);
            lstHome = HomeServiceImpl.getInstance().findList(filtersHome, orderHome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, true, true, true
        );
    }

    public void preAdd() {
        this.currRoom = new Room();
        isEdit = false;
    }

    public void preEdit(Room role) {
        try {
            currRoom = RoomServiceImpl.getInstance().findById(role.getRoomId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currRoom.toString();
    }

    public void onSaveOrUpdate() {
        try {
            RoomServiceImpl.getInstance().saveOrUpdate(currRoom);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('roomDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(Room catRole) {
        try {
            RoomServiceImpl.getInstance().delete(catRole);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<Room> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Room> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public Room getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(Room currRoom) {
        this.currRoom = currRoom;
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

    public List<Home> getLstHome() {
        return lstHome;
    }

    public void setLstHome(List<Home> lstHome) {
        this.lstHome = lstHome;
    }

}
