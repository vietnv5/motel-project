/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.CatService;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatItemBO;
import com.slook.model.CatUser;
import com.slook.persistence.CatServiceServiceImpl;
import com.slook.util.CommonUtil;
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
 * @author VietNV Jun 9, 2018
 */
@ManagedBean
@ViewScoped
public class CatServiceController {

    protected static final Logger logger = LoggerFactory.getLogger(CatServiceController.class);

    LazyDataModel<CatService> lazyDataModel;
    CatService currService = new CatService();
    private String oldObjectStr = null;
    private Long groupUserId;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    private List<CatItemBO> listUnit = new ArrayList<>();
    Map<Long, CatItemBO> mapUnit = new HashMap<>();

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
                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("serviceName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0) {//phan quyen
                filter.put("groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<CatService, Long>(CatServiceServiceImpl.getInstance(), filter, order);

            listUnit = CommonUtil.getItemBOList(Constant.CAT_CODE.UNIT, "name");
            mapUnit = CommonUtil.getMapCatItemByKeyId(Constant.CAT_CODE.UNIT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, false, true, true,
                true, true,false
        );
    }

    public void preAdd() {
        this.currService = new CatService();
        if (groupUserId != null && groupUserId > 0) {
            currService.setGroupUserId(groupUserId);
        }
        currService.setStatus(Constant.UNIT_STATUS.NORMAL);
        isEdit = false;
    }

    public void preEdit(CatService customer) {
        try {
            currService = CatServiceServiceImpl.getInstance().findById(customer.getServiceId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currService.toString();
    }

    public void onSaveOrUpdate() {
        try {
            if (currService.getStatus() == null) {
                currService.setStatus(Constant.UNIT_STATUS.NORMAL);
            }
            if (groupUserId != null && groupUserId > 0) {
                currService.setGroupUserId(groupUserId);
            }
            CatServiceServiceImpl.getInstance().saveOrUpdate(currService);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currService.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currService.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('catServiceDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(CatService catRole) {
        try {
            CatServiceServiceImpl.getInstance().delete(catRole);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<CatService> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatService> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public CatService getCurrService() {
        return currService;
    }

    public void setCurrService(CatService currService) {
        this.currService = currService;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }

    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
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

    public List<CatItemBO> getListUnit() {
        return listUnit;
    }

    public void setListUnit(List<CatItemBO> listUnit) {
        this.listUnit = listUnit;
    }

    public Map<Long, CatItemBO> getMapUnit() {
        return mapUnit;
    }

    public void setMapUnit(Map<Long, CatItemBO> mapUnit) {
        this.mapUnit = mapUnit;
    }

    
}
