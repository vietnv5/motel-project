package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatBranch;
import com.slook.model.CatCommune;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.common.ConditionQuery;
import com.slook.persistence.common.OrderBy;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by xuanh on 4/23/2017.
 */
@ManagedBean
@ViewScoped
public class BranchController implements Serializable
{

    List<CatBranch> branchs;
    GenericDaoServiceNewV2 branchService;
    CatBranch currBranch;
    LazyDataModel<CatBranch> lazyBranch;
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart()
    {
        branchService = new GenericDaoImplNewV2<CatBranch, Long>()
        {
        };
        Map<String, Object> filterItem = new HashMap<String, Object>();
        filterItem.put("status", Constant.STATUS.ACTIVE);
        try
        {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branchName", "ASC");
            branchs = branchService.findList(filterItem, order);
            lazyBranch = new LazyDataModelBase<CatBranch, Long>(branchService);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        currBranch = new CatBranch();
    }

    public List<CatCommune> completeAddress(String key)
    {
        ;
        try
        {
            ConditionQuery query = new ConditionQuery();
            Criterion criterion = Restrictions.or(Restrictions.ilike("communeName", key, MatchMode.ANYWHERE),
                    Restrictions.ilike("district.districtName", key, MatchMode.ANYWHERE),
                    Restrictions.ilike("district.province.provinceName", key, MatchMode.ANYWHERE)
            );
            query.add(criterion);
            return new GenericDaoImplNewV2<CatCommune, Long>()
            {
            }.findList(query, new OrderBy(), 1, 100);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void preAddBranch()
    {
        currBranch = new CatBranch();
        currBranch.setStatus(1l);
        oldObjectStr = null;

    }

    public void saveOrUpdate()
    {
        try
        {
            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("branchCode-EXAC_IGNORE_CASE", currBranch.getBranchCode());
            if (currBranch.getBranchId() != null && currBranch.getBranchId() > 0)
            {
                filters.put("branchId-NEQ", currBranch.getBranchId());
            }
            List lst = branchService.findList(filters);
            if (!lst.isEmpty() && lst.size() > 0)
            {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                        MessageUtil.getResourceBundleMessage("datatable.header.branch.code")
                ));
                return;
            }
            Map<String, Object> filtersN = new HashMap<String, Object>();
            filtersN.put("branchName-EXAC_IGNORE_CASE", currBranch.getBranchName());
            if (currBranch.getBranchId() != null && currBranch.getBranchId() > 0)
            {
                filtersN.put("branchId-NEQ", currBranch.getBranchId());
            }
            List lstN = branchService.findList(filtersN);
            if (!lstN.isEmpty() && lstN.size() > 0)
            {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                        MessageUtil.getResourceBundleMessage("datatable.header.branch.name")
                ));
                return;
            }
            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currBranch.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currBranch.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            branchService.saveOrUpdate(currBranch);
            MessageUtil.setInfoMessageFromRes("info.save.success");
            preAddBranch();
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CatBranch catBranch)
    {
        currBranch = catBranch;
        oldObjectStr = currBranch.toString();
    }

    public void delete(CatBranch currBranch)
    {
        try
        {
            branchService.delete(currBranch);

            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currBranch.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        }
        catch (AppException e)
        {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public GenericDaoServiceNewV2 getBranchService()
    {
        return branchService;
    }

    public void setBranchService(GenericDaoServiceNewV2 branchService)
    {
        this.branchService = branchService;
    }

    public CatBranch getCurrBranch()
    {
        return currBranch;
    }

    public void setCurrBranch(CatBranch currBranch)
    {
        this.currBranch = currBranch;
    }

    public List<CatBranch> getBranchs()
    {
        return branchs;
    }

    public void setBranchs(List<CatBranch> branchs)
    {
        this.branchs = branchs;
    }

    public LazyDataModel<CatBranch> getLazyBranch()
    {
        return lazyBranch;
    }

    public void setLazyBranch(LazyDataModel<CatBranch> lazyBranch)
    {
        this.lazyBranch = lazyBranch;
    }

    public void updateStatus(CatBranch catBranch, Long newStatus)
    {
        try
        {
            String old = catBranch.toString();
            catBranch.setStatus(newStatus);
            branchService.saveOrUpdate(catBranch);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, old, catBranch.toString(),
                    this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
}
