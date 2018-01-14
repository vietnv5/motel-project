package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatBranch;
import com.slook.model.CatDepartment;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xuanh on 4/23/2017.
 */
@ManagedBean
@ViewScoped
public class DepartmentController implements Serializable {

    List<CatDepartment> departments;
    GenericDaoServiceNewV2 departmentService;
    CatDepartment currentDepartment;
    LazyDataModel<CatDepartment> lazyDepartment;
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart(){
        departmentService = new GenericDaoImplNewV2<CatDepartment,Long>() {};
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("departmentName","ASC");
            departments = departmentService.findList(null,order);
            lazyDepartment = new LazyDataModelBase<CatDepartment, Long>(departmentService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentDepartment = new CatDepartment();
    }

    public void preAdd(){
        currentDepartment = new CatDepartment();
        currentDepartment.setStatus(1l);
        oldObjectStr=null;
    }

    public void saveOrUpdate(){
        try {
            departmentService.saveOrUpdate(currentDepartment);
            //ghi log
            if (oldObjectStr != null)
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currentDepartment.toString()
                        , this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            else
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currentDepartment.toString()
                        , this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
    public void preEdit(CatDepartment department){
        currentDepartment = department;
        oldObjectStr=department.toString();
    }

    public void delete(CatDepartment department){
        try {
            departmentService.delete(department);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, department.toString(), null, this.getClass().getSimpleName()
                    , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public List<CatDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<CatDepartment> departments) {
        this.departments = departments;
    }

    public GenericDaoServiceNewV2 getDepartmentService() {
        return departmentService;
    }

    public void setDepartmentService(GenericDaoServiceNewV2 departmentService) {
        this.departmentService = departmentService;
    }

    public CatDepartment getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(CatDepartment currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public LazyDataModel<CatDepartment> getLazyDepartment() {
        return lazyDepartment;
    }

    public void setLazyDepartment(LazyDataModel<CatDepartment> lazyDepartment) {
        this.lazyDepartment = lazyDepartment;
    }


    public void updateStatus(CatDepartment catDepartment, Long newStatus) {
        try {
            String oldStatus=catDepartment.toString();
            catDepartment.setStatus(newStatus);
            departmentService.saveOrUpdate(catDepartment);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldStatus, catDepartment.toString(), this.getClass().getSimpleName()
                    , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
