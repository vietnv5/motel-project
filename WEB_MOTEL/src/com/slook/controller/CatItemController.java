/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatDictionaryBO;
import com.slook.model.CatItemBO;
import com.slook.model.GroupMember;
import com.slook.model.Member;
import com.slook.model.MemberPayment;
import com.slook.model.V_CatItemBO;
import com.slook.model.V_CustomerCheckin;
import com.slook.persistence.CatDictionaryServiceImpl;
import com.slook.persistence.CatItemServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.VCatItemServiceImpl;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.text.MessageFormat;
import java.util.*;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

/**
 *
 * @author vietnv14
 */
@ViewScoped
@ManagedBean
public class CatItemController {

    protected static final Logger logger = LoggerFactory.getLogger(CatItemController.class);

    @ManagedProperty(value = "#{vCatItemService}")
    private VCatItemServiceImpl vCatItemServiceImpl;
    private LazyDataModel<V_CatItemBO> lazyVCatItem;
    private List<Boolean> columnVisibale = new ArrayList<>();

    @ManagedProperty(value = "#{catItemService}")
    private CatItemServiceImpl catItemServiceImpl;
    private V_CatItemBO selectVCatItemBO = new V_CatItemBO();
    private CatItemBO objCatItemBO = new CatItemBO();
    private boolean isEdit = false;
    @ManagedProperty(value = "#{catDictionaryService}")
    private CatDictionaryServiceImpl catDictionaryServiceImpl;
    List<CatDictionaryBO> lstCatDictionary;

    private String regexValue;
    private String messageValid;
    private Long requireValue;
    private String oldObjectStr = null;

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public String getRegexValue() {
        return regexValue;
    }

    public void setRegexValue(String regexValue) {
        this.regexValue = regexValue;
    }

    public String getMessageValid() {
        return messageValid;
    }

    public void setMessageValid(String messageValid) {
        this.messageValid = messageValid;
    }

    public Long getRequireValue() {
        return requireValue;
    }

    public void setRequireValue(Long requireValue) {
        this.requireValue = requireValue;
    }

    public VCatItemServiceImpl getvCatItemServiceImpl() {
        return vCatItemServiceImpl;
    }

    public void setvCatItemServiceImpl(VCatItemServiceImpl vCatItemServiceImpl) {
        this.vCatItemServiceImpl = vCatItemServiceImpl;
    }

    public LazyDataModel<V_CatItemBO> getLazyVCatItem() {
        return lazyVCatItem;
    }

    public void setLazyVCatItem(LazyDataModel<V_CatItemBO> lazyVCatItem) {
        this.lazyVCatItem = lazyVCatItem;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public CatItemServiceImpl getCatItemServiceImpl() {
        return catItemServiceImpl;
    }

    public void setCatItemServiceImpl(CatItemServiceImpl catItemServiceImpl) {
        this.catItemServiceImpl = catItemServiceImpl;
    }

    public V_CatItemBO getSelectVCatItemBO() {
        return selectVCatItemBO;
    }

    public void setSelectVCatItemBO(V_CatItemBO selectVCatItemBO) {
        this.selectVCatItemBO = selectVCatItemBO;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public CatDictionaryServiceImpl getCatDictionaryServiceImpl() {
        return catDictionaryServiceImpl;
    }

    public void setCatDictionaryServiceImpl(CatDictionaryServiceImpl catDictionaryServiceImpl) {
        this.catDictionaryServiceImpl = catDictionaryServiceImpl;
    }

    public List<CatDictionaryBO> getLstCatDictionary() {
        return lstCatDictionary;
    }

    public void setLstCatDictionary(List<CatDictionaryBO> lstCatDictionary) {
        this.lstCatDictionary = lstCatDictionary;
    }

    public CatItemBO getObjCatItemBO() {
        return objCatItemBO;
    }

    public void setObjCatItemBO(CatItemBO objCatItemBO) {
        this.objCatItemBO = objCatItemBO;
    }

//</editor-fold>
    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {
        Map<String, Object> filters = new HashMap<String, Object>();
        LinkedHashMap<String, String> orders = new LinkedHashMap<>();
        orders.put("itemCode", Constant.ORDER.ASC);

        lazyVCatItem = new LazyDataModelBase<>(vCatItemServiceImpl, filters, orders);
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, false, true, true, true);
        try {
            Map<String, Object> filtersD = new HashMap<String, Object>();
            filtersD.put("editable", 1l);
            LinkedHashMap<String, String> ordersD = new LinkedHashMap<>();
            ordersD.put("catName", Constant.ORDER.ASC);
            lstCatDictionary = catDictionaryServiceImpl.findList(filtersD, ordersD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void preInsert() {
        this.objCatItemBO = new CatItemBO();
        this.isEdit = false;
        oldObjectStr = null;
    }

    public void preEdit(V_CatItemBO vcatItem) {
        this.selectVCatItemBO = vcatItem;
        this.isEdit = true;
        if (selectVCatItemBO.getItemId() != null) {
            try {
                this.objCatItemBO = catItemServiceImpl.findById(selectVCatItemBO.getItemId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        oldObjectStr = vcatItem.toString();
        onchangeCatDictionary();
    }

    public void onDelete(V_CatItemBO vcatItem) {
        try {
            this.objCatItemBO = catItemServiceImpl.findById(vcatItem.getItemId());
            catItemServiceImpl.delete(objCatItemBO);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, objCatItemBO.toString(), null, this.getClass().getSimpleName(),
                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.success");
        } catch (Exception e) {
            getRootCauseMessage(e);
            logger.error(e.getMessage(), e);
            if (e.getMessage() != null && e.getMessage().contains("integrity constraint") && e.getMessage().contains("child record found")) {
                MessageUtil.setErrorMessage(MessageUtil.getResourceBundleMessage("error.existTableUsedRecod")
                        + ": " + CommonUtil.getTableInConstraint(e.getMessage()));
            } else {
                MessageUtil.setErrorMessageFromRes("error.existTableUsedRecod");
//                MessageUtil.setErrorMessageFromRes("error.delete.unsuccess");
            }
        }
    }

    public void onSaveOrUpdate() {
        try {
            if (objCatItemBO == null) {
                return;
            }
            if (!StringUtil.isNotNull(objCatItemBO.getCatCode())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("catItem.catCode")));
                return;
            }
            if (!StringUtil.isNotNull(objCatItemBO.getCode())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("catItem.itemCode")));
                return;
            }
            if (!StringUtil.isNotNull(objCatItemBO.getName())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("catItem.itemName")));
                return;
            }
            if (requireValue != null && requireValue.equals(1l) && !StringUtil.isNotNull(objCatItemBO.getValue())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("catItem.value")));
                return;
            }

            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("catCode-EXAC_IGNORE_CASE", objCatItemBO.getCatCode());
            filters.put("code-EXAC_IGNORE_CASE", objCatItemBO.getCode());
            if (objCatItemBO.getItemId() != null) {
                filters.put("itemId-NEQ", objCatItemBO.getItemId());
            }
            List<CatItemBO> lst = catItemServiceImpl.findList(filters);
            if (!lst.isEmpty() && lst.size() > 0) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                        MessageUtil.getResourceBundleMessage("catItem.catName")
                        + ", " + MessageUtil.getResourceBundleMessage("catItem.itemCode")
                ));
                return;
            }

            catItemServiceImpl.saveOrUpdate(objCatItemBO);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, objCatItemBO.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, objCatItemBO.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('catItemDlg').hide();PF('widTableCatItem').clearFilters();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("common.message.fail");
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try {
            String[] ar="../../abv/ttt".split("\\.\\./");
            System.out.println("../../abv/ttt".split("../"));
//            new GenericDaoImplNewV2<V_CustomerCheckin, Long>() {
//            }.findList();
//            GenericDaoServiceNewV2<GroupMember, Long> memberService = new GenericDaoImplNewV2<GroupMember, Long>() {
//            };
//            MemberPayment m=new MemberPayment();
//            m.createPaymentCode(12l);
//            List<GroupMember> lstM = memberService.findList();
////            lstM.get(0).getNumMemberInGroup();
//            VCatItemServiceImpl ipsv = new VCatItemServiceImpl();
//            
//            List lst = ipsv.findList();
//            System.out.println(lst.size());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onchangeCatDictionary() {
        // set lai gia tri
        regexValue = "";
        requireValue = 0l;
        messageValid = "";

        if (objCatItemBO != null && StringUtil.isNotNull(objCatItemBO.getCatCode()) && lstCatDictionary != null) {

            for (CatDictionaryBO bo : lstCatDictionary) {
                if (objCatItemBO.getCatCode().equalsIgnoreCase(bo.getCatCode())) {
                    regexValue = bo.getRegexValue();
                    requireValue = bo.getRequireValue();
                    messageValid = bo.getMessageValid();
                    break;
                }
            }
        }

    }
}
