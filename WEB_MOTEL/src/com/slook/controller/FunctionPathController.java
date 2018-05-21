/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.FunctionPath;
import com.slook.persistence.FunctionPathServiceImpl;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.Constant.ORDER;
import com.slook.util.MessageUtil;
import com.slook.util.ValidatorUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;


/**
 *
 * @author Van Anh 14/5/2018
 */

@ManagedBean
@ViewScoped
public class FunctionPathController extends CommonUtil implements Serializable, Constant{
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FunctionPathController.class);

    public static org.apache.log4j.Logger getLogger() {
        return logger;
    }

    public static void setLogger(org.apache.log4j.Logger logger) {
        FunctionPathController.logger = logger;
    }
    
    @ManagedProperty(value = "#{functionPathService}")
    private FunctionPathServiceImpl functionPathService;
    @ManagedProperty(value = "#{commonUtil}")
    private CommonUtil commonUtil;
    
    private LazyDataModel<FunctionPath> lazyFunctionPath;
    private FunctionPath selectedFunctionPath = new FunctionPath();
    
    private List<FunctionPath> listAllFunctionPath;
    private List<String> listFunctionPathID;
    boolean hasError = false;
    boolean isRemove = false;
    boolean isView = false;
    boolean isInsert = false;
    boolean isEdit = false;
    String empTitle = "Danh sách chức năng";
    //vietnv bo sung
    private Map<Long,String> mapNameFuncionPath=new HashMap<>();
    

    
    @PostConstruct
    public void onStart() {
        try {
            Map<String, String> orders = new LinkedHashMap<>();
            orders.put("url", ORDER.DESC);
            Map<String, Object> mapFilterFunctionPath = new HashMap<String, Object>();
            mapFilterFunctionPath.put("status", 1L);
            lazyFunctionPath = new LazyDataModelBase<>(functionPathService, mapFilterFunctionPath, orders);
            LinkedHashMap<String, String> ordersFunctionPathEntity = new LinkedHashMap<>();
            listAllFunctionPath = functionPathService.findList(mapFilterFunctionPath, ordersFunctionPathEntity);
            //
        //    selectedFunctionPath = new FunctionPath();
            colVisible = new LinkedHashMap<>();
            colVisible.put("index", Boolean.TRUE);
            colVisible.put("functionPathId", Boolean.FALSE);
            colVisible.put("url", Boolean.TRUE);
            colVisible.put("name", Boolean.TRUE);
            colVisible.put("typeName", Boolean.TRUE);
            colVisible.put("parentId", Boolean.TRUE);
            if(listAllFunctionPath!=null)for(FunctionPath f:listAllFunctionPath)mapNameFuncionPath.put(f.getFunctionPathId(), f.getName());
        } catch (AppException ex) {
            java.util.logging.Logger.getLogger(FunctionPathController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SysException ex) {
            java.util.logging.Logger.getLogger(FunctionPathController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FunctionPathController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    public void preData(){
        Map<String, String> orders = new LinkedHashMap<>();
        Map<String, Object> mapFilterFuncPath = new HashMap<String, Object>();
        orders.put("functionPathId", ORDER.ASC);
        mapFilterFuncPath.put("status", 1L);
        LinkedHashMap<String, String> orderFunctionPath = new LinkedHashMap<>();
        try {
            listAllFunctionPath= functionPathService.findList(mapFilterFuncPath, orderFunctionPath);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FunctionPathController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
    public void preAdd() {
        this.selectedFunctionPath = setDefaultFunctionPath();
        System.out.println("add");
        isRemove = false;
        isEdit = false;
        isView = false;
        isInsert = true;
        empTitle = MessageUtil.getKey("category.functionPath.insertFunctionPath");
    }
    public void preRemove(FunctionPath funcPath) {
        this.selectedFunctionPath = funcPath;
        System.out.println("remove");
        isRemove = true;
        isView = false;
        isInsert = false;
        isEdit = false;
        empTitle = MessageUtil.getKey("category.functionPath.removeFunctionPath");
    }
    public void preEdit(FunctionPath functionPath) {
        this.selectedFunctionPath = functionPath;
        System.out.println("edit??");
        isRemove = false;
        isView = false;
        isInsert = false;
        isEdit = true;
        empTitle = MessageUtil.getKey("category.functionPath.updateFunctionPath");
    }

    public void preView(FunctionPath funcPath) {
        this.selectedFunctionPath = funcPath;
        System.out.println("View??");
        isRemove = false;
        isView = true;
        isInsert = false;
        isEdit = false;
        empTitle = MessageUtil.getKey("category.functionPath.viewFunctionPath");
    }
        
    private FunctionPath setDefaultFunctionPath() {
        FunctionPath funcPath = new FunctionPath();
        funcPath.setStatus(1L);
        return funcPath;
    }
    
    public void save() {
        try{
            String result = validate(selectedFunctionPath);
            if(!ValidatorUtil.isNotNull(result)) {
                functionPathService.saveOrUpdate(selectedFunctionPath);
                MessageUtil.setInfoMessageFromRes("common.message.success");
            } else {
                MessageUtil.setInfoMessage(result);
                 RequestContext.getCurrentInstance().execute("PF('block').hide();");
            }
        } catch(Exception ex) {
            MessageUtil.setUpdateFailMsg();
            RequestContext.getCurrentInstance().execute("PF('block').hide();");
            logger.error(ex.getMessage(), ex);
        }
        
        
    }
    
    public void delete() {
        try {
            selectedFunctionPath.setStatus(0L);
            functionPathService.saveOrUpdate(selectedFunctionPath);
            RequestContext.getCurrentInstance().execute("PF('block').hide();");

        } catch (Exception ex) {
            MessageUtil.setUpdateFailMsg();
            RequestContext.getCurrentInstance().execute("PF('block').hide();");
            logger.error(ex.getMessage(), ex);
        }
    }
    
    private String validate(FunctionPath temp) {
        String rs = "";
        //Nếu k nhập URL
        if(temp.getUrl() == null || "".equals(temp.getUrl())) {
            return MessageUtil.getKey("category.functionPath.urlCannotEmpty");
        }
        //Nếu 
        if(temp.getName() == null || "".equals(temp.getName())) {
            return MessageUtil.getKey("category.functionPath.parentIDCannotEmpty");
        }
        return rs;
    }
    /*Setter - getter methods */
    public FunctionPathServiceImpl getFunctionPathService() {
        return functionPathService;
    }

    public void setFunctionPathService(FunctionPathServiceImpl functionPathService) {
        this.functionPathService = functionPathService;
    }

    public LazyDataModel<FunctionPath> getLazyFunctionPath() {
        return lazyFunctionPath;
    }

    public void setLazyFunctionPath(LazyDataModel<FunctionPath> lazyFunctionPath) {
        this.lazyFunctionPath = lazyFunctionPath;
    }

    public FunctionPath getSelectedFunctionPath() {
        return selectedFunctionPath;
    }

    public void setSelectedFunctionPath(FunctionPath selectedFunctionPath) {
        this.selectedFunctionPath = selectedFunctionPath;
    }

    public List<FunctionPath> getListAllFunctionPath() {
        return listAllFunctionPath;
    }

    public void setListAllFunctionPath(List<FunctionPath> listAllFunctionPath) {
        this.listAllFunctionPath = listAllFunctionPath;
    }

    public List<String> getListFunctionPathID() {
        return listFunctionPathID;
    }

    public void setListFunctionPathID(List<String> listFunctionPathID) {
        this.listFunctionPathID = listFunctionPathID;
    }

    public boolean isIsRemove() {
        return isRemove;
    }

    public void setIsRemove(boolean remove) {
        this.isRemove = remove;
    }

    public boolean isIsView() {
        return isView;
    }

    public void setIsView(boolean view) {
        this.isView = view;
    }

    public boolean isIsInsert() {
        return isInsert;
    }

    public void setIsInsert(boolean insert) {
        this.isInsert = insert;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean edit) {
        this.isEdit = edit;
    }

    public String getEmpTitle() {
        return empTitle;
    }

    public void setEmpTitle(String empTitle) {
        this.empTitle = empTitle;
    }
    public CommonUtil getCommonUtil() {
        return commonUtil;
    }

    public void setCommonUtil(CommonUtil commonUtil) {
        this.commonUtil = commonUtil;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Map<Long, String> getMapNameFuncionPath() {
        return mapNameFuncionPath;
    }

    public void setMapNameFuncionPath(Map<Long, String> mapNameFuncionPath) {
        this.mapNameFuncionPath = mapNameFuncionPath;
    }
    
}
