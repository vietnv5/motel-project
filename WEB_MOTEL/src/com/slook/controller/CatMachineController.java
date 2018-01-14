/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatItemBO;
import com.slook.model.CatMachine;
import com.slook.model.CatMachine;
import com.slook.model.CatMachine;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class CatMachineController {

    GenericDaoImplNewV2<CatMachine, Long> catMachineService;
    LazyDataModel<CatMachine> lazyDataModel;
    CatMachine currCatMachine=new CatMachine();
    boolean isEdit = false;
    List<CatItemBO> listMachineType;
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart() {

        catMachineService = new GenericDaoImplNewV2<CatMachine, Long>() {
        };
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("machineName", "ASC");
            Map<String, Object> filter = new HashMap<>();

            lazyDataModel = new LazyDataModelBase<CatMachine, Long>(catMachineService, filter, order);
            listMachineType = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.MACHINE_TYPE, "name");
            listMachineType.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preAdd() {
        currCatMachine = new CatMachine();
        currCatMachine.setStatus(1l);
        oldObjectStr = null;
    }

    public void saveOrUpdate() {
        try {
                        // validate
            Map<String, Object> filterExist = new HashMap<>();
            filterExist.put("machineCode-EXAC_IGNORE_CASE", currCatMachine.getMachineCode());
            if (currCatMachine.getMachineId() != null) {
                filterExist.put("machineId-NEQ", currCatMachine.getMachineId());
            }
            List<CatMachine> lstExist = catMachineService.findList(filterExist);
            if (lstExist != null && lstExist.size() > 0) {
                MessageUtil.setErrorMessageFromRes("member.existMemberCode");
                return;
            }
            
            catMachineService.saveOrUpdate(currCatMachine);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currCatMachine.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currCatMachine.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            MessageUtil.setInfoMessageFromRes("info.save.success");
            preAdd();
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CatMachine catBranch) {
        currCatMachine = catBranch;
        oldObjectStr = catBranch.toString();
    }

    public void delete(CatMachine currBranch) {
        try {
            catMachineService.delete(currBranch);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currBranch.toString(), null, this.getClass().getSimpleName(),
                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public void updateStatus(CatMachine catRoom, Long newStatus) {
        try {
            String old = catRoom.toString();
            catRoom.setStatus(newStatus);
            catMachineService.saveOrUpdate(catRoom);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, catRoom.toString(), this.getClass().getSimpleName(),
                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

//<editor-fold defaultstate="collapsed" desc="get/set">
    public GenericDaoImplNewV2<CatMachine, Long> getCatMachineService() {
        return catMachineService;
    }

    public void setCatMachineService(GenericDaoImplNewV2<CatMachine, Long> catMachineService) {
        this.catMachineService = catMachineService;
    }

    public LazyDataModel<CatMachine> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatMachine> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public CatMachine getCurrCatMachine() {
        return currCatMachine;
    }

    public void setCurrCatMachine(CatMachine currCatMachine) {
        this.currCatMachine = currCatMachine;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public List<CatItemBO> getListMachineType() {
        return listMachineType;
    }

    public void setListMachineType(List<CatItemBO> listMachineType) {
        this.listMachineType = listMachineType;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }
//</editor-fold>

}
