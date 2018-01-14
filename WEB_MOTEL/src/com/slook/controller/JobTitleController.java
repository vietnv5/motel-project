package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatJobTitle;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanh on 4/23/2017.
 */
@ManagedBean
@ViewScoped
public class JobTitleController implements Serializable {

    List<CatJobTitle> jobTitles;
    GenericDaoServiceNewV2 jobTitleService;
    CatJobTitle currJobTitle;
    LazyDataModel<CatJobTitle> lazyJobTitle;
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart() {
        jobTitleService = new GenericDaoImplNewV2<CatJobTitle, Long>() {
        };
        Map<String, Object> filterItem = new HashMap<String, Object>();
        filterItem.put("status", Constant.STATUS.ACTIVE);
        try {
            jobTitles = jobTitleService.findList(filterItem);
            lazyJobTitle = new LazyDataModelBase<CatJobTitle, Long>(jobTitleService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currJobTitle = new CatJobTitle();
    }

    public void preAdd() {
        currJobTitle = new CatJobTitle();
        currJobTitle.setStatus(1l);
        oldObjectStr = null;
    }

    public void saveOrUpdate() {
        try {
            jobTitleService.saveOrUpdate(currJobTitle);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currJobTitle.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currJobTitle.toString(),
                         this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CatJobTitle catBranch) {
        currJobTitle = catBranch;
        oldObjectStr = catBranch.toString();
    }

    public void delete(CatJobTitle currBranch) {
        try {
            jobTitleService.delete(currBranch);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currBranch.toString(), null, this.getClass().getSimpleName(),
                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public GenericDaoServiceNewV2 getJobTitleService() {
        return jobTitleService;
    }

    public void setJobTitleService(GenericDaoServiceNewV2 jobTitleService) {
        this.jobTitleService = jobTitleService;
    }

    public CatJobTitle getCurrJobTitle() {
        return currJobTitle;
    }

    public void setCurrJobTitle(CatJobTitle currJobTitle) {
        this.currJobTitle = currJobTitle;
    }

    public List<CatJobTitle> getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(List<CatJobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public LazyDataModel<CatJobTitle> getLazyJobTitle() {
        return lazyJobTitle;
    }

    public void setLazyJobTitle(LazyDataModel<CatJobTitle> lazyJobTitle) {
        this.lazyJobTitle = lazyJobTitle;
    }

    public void updateStatus(CatJobTitle catJobTitle, Long newStatus) {
        try {
            String old = catJobTitle.toString();
            catJobTitle.setStatus(newStatus);
            jobTitleService.saveOrUpdate(catJobTitle);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, catJobTitle.toString(), this.getClass().getSimpleName(),
                     (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
}
