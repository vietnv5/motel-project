package com.slook.controller;


import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatService;
import com.slook.persistence.CatServiceImpl;
import com.slook.util.MessageUtil;
import org.primefaces.component.message.Message;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.*;

@ManagedBean
@ViewScoped
public class CatServiceController {
    protected static final Logger logger = LoggerFactory.getLogger(VCustomerCheckinController.class);
    @ManagedProperty(value = "#{catServiceImpl}")
    private CatServiceImpl catService;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private LazyDataModel<CatService> lazyDataModel;
    private CatService catServiceCurr = new CatService();
    private List<CatService> catServiceList = new ArrayList<>();
    private Long catServiceId;

    @PostConstruct
    public void onStart() {
        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> oder = new LinkedHashMap<>();
        lazyDataModel = new LazyDataModelBase<>(catService, filter, oder);
        try {
            catServiceList = catService.findList(filter, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preAdd() {
        this.catServiceId = Long.valueOf((catServiceList.size() > 0) ? (catServiceList.size() + 1):1);
        this.catServiceCurr=new CatService();
        this.catServiceCurr.setServiceId(this.catServiceId);
    }

    public void preEdit(CatService catService) {
        this.catServiceCurr = catService;
    }

    public void onSaveOrUpdate() {
        if (catServiceCurr == null) {
            return;
        }
        Map<String, Object> filterCodeExist = new HashMap<>();
        filterCodeExist.put("serviceCode",catServiceCurr.getServiceCode());
        Map<String, Object> filterIdExist = new HashMap<>();
        filterIdExist.put("serviceId",catServiceCurr.getServiceId());
        try {
            List<CatService> listIdExist = catService.findList(filterIdExist, null);
            if (listIdExist.size() > 0) {
                catService.saveOrUpdate(catServiceCurr);
                MessageUtil.setInfoMessageFromRes("common.message.success");
                return;
            }
            List<CatService> listExist = catService.findList(filterCodeExist, null);
            if (listExist.size() > 0) {
                MessageUtil.setWarnMessageFromRes("catService.code.exist");
                Message message=new Message();
                message.setDisplay("VUI LONG NHAP");
                message.setFor("cServiceCode");
                return;
            }else {
                catService.save(catServiceCurr);
            }

            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.setInfoMessageFromRes("common.message.fail");

        }

    }

    public void onDelete(CatService cat) {
        try {
            catService.delete(cat);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public static Logger getLogger() {
        return logger;
    }

    public CatServiceImpl getCatService() {
        return catService;
    }

    public void setCatService(CatServiceImpl catService) {
        this.catService = catService;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public LazyDataModel<CatService> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatService> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public CatService getCatServiceCurr() {
        return catServiceCurr;
    }

    public void setCatServiceCurr(CatService catServiceCurr) {
        this.catServiceCurr = catServiceCurr;
    }

    public List<CatService> getCatServiceList() {
        return catServiceList;
    }

    public void setCatServiceList(List<CatService> catServiceList) {
        this.catServiceList = catServiceList;
    }

    public Long getCatServiceId() {
        return catServiceId;
    }

    public void setCatServiceId(Long catServiceId) {
        this.catServiceId = catServiceId;
    }
}
