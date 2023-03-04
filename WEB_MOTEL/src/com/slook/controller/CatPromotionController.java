package com.slook.controller;

import com.google.gson.Gson;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatItemBO;
import com.slook.model.CatPromotion;
import com.slook.model.CatGroupPack;
import com.slook.model.PackHasPromotion;
import com.slook.persistence.CatPromotionServiceImpl;
import com.slook.persistence.CatGroupPackServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;

import java.text.MessageFormat;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

@ManagedBean
@ViewScoped
public class CatPromotionController
{

    protected static final Logger logger = LoggerFactory.getLogger(VCustomerCheckinController.class);
    @ManagedProperty(value = "#{catPromotionService}")
    private CatPromotionServiceImpl catPromotionService;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private LazyDataModel<CatPromotion> lazyDataModel;
    private CatPromotion catPromotionCurr = new CatPromotion();
    private List<CatPromotion> catPromotionList = new ArrayList<>();
    private Long catPromotionId;

    private boolean isEdit = false;
    private List<CatItemBO> lstComparisonOperator = new ArrayList();
    private String guide;
    //promotion for pack
    private List<CatGroupPack> lstCatGroupPacks = new ArrayList<>();
    private DualListModel<CatGroupPack> dualLstCatGroupPack = new DualListModel<>();
    private GenericDaoImplNewV2 packHasPromotionService;
    private String oldObjectStr = null;

    @PostConstruct
    public void onStart()
    {
        packHasPromotionService = new GenericDaoImplNewV2<PackHasPromotion, Long>()
        {
        };

        Map<String, Object> filter = new HashMap<>();
        Map<String, Object> oder = new LinkedHashMap<>();
        lazyDataModel = new LazyDataModelBase<>(catPromotionService, filter, oder);
        try
        {
            catPromotionList = catPromotionService.findList(filter, null);

            //
            Map<String, Object> filtersPack = new HashMap<>();
            filtersPack.put("status-NEQ", Constant.STATUS.DELETE);
            LinkedHashMap<String, String> orderPack = new LinkedHashMap<>();
            orderPack.put("groupPackName", Constant.ORDER.ASC);
            lstCatGroupPacks = CatGroupPackServiceImpl.getInstance().findList(filtersPack, orderPack);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        lstComparisonOperator = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.COMPARISON_OPERATOR, "value");

    }

    public void preAdd()
    {
        this.catPromotionCurr = new CatPromotion();
    }

    public void preEdit(CatPromotion catPromotion)
    {
        this.catPromotionCurr = catPromotion;
        changeComparisonOperator();
    }

    public void onSaveOrUpdate()
    {
        if (catPromotionCurr == null)
        {
            return;
        }
        if (StringUtil.isNullOrEmpty(catPromotionCurr.getCatPromotionCode()))
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    "Mã khuyến mãi"));
            return;
        }
        if (StringUtil.isNullOrEmpty(catPromotionCurr.getCatPromotionName()))
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    "Tên khuyến mãi"));
            return;
        }
        if (catPromotionCurr.getType() == null || catPromotionCurr.getType() < 1)
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    "Loại khuyến mãi"));
            return;
        }
        if (catPromotionCurr.getValue() == null)
        {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    "Giá trị"));
            return;
        }

        try
        {
            catPromotionService.saveOrUpdate(catPromotionCurr);
            preAdd();
            RequestContext.getCurrentInstance().update(":catFormId");
            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MessageUtil.setInfoMessageFromRes("common.message.fail");

        }

    }

    public void onDelete(CatPromotion cat)
    {
        try
        {
            catPromotionService.delete(cat);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void changeComparisonOperator()
    {
        guide = CommonUtil.changeComparisonOperator(catPromotionCurr.getOperator());
    }

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public CatPromotionServiceImpl getCatPromotionService()
    {
        return catPromotionService;
    }

    public void setCatPromotionService(CatPromotionServiceImpl catPromotionService)
    {
        this.catPromotionService = catPromotionService;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public LazyDataModel<CatPromotion> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatPromotion> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public CatPromotion getCatPromotionCurr()
    {
        return catPromotionCurr;
    }

    public void setCatPromotionCurr(CatPromotion catPromotionCurr)
    {
        this.catPromotionCurr = catPromotionCurr;
    }

    public List<CatPromotion> getCatPromotionList()
    {
        return catPromotionList;
    }

    public void setCatPromotionList(List<CatPromotion> catPromotionList)
    {
        this.catPromotionList = catPromotionList;
    }

    public Long getCatPromotionId()
    {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId)
    {
        this.catPromotionId = catPromotionId;
    }

    public boolean isIsEdit()
    {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
    }

    public List<CatItemBO> getLstComparisonOperator()
    {
        return lstComparisonOperator;
    }

    public void setLstComparisonOperator(List<CatItemBO> lstComparisonOperator)
    {
        this.lstComparisonOperator = lstComparisonOperator;
    }

    public String getGuide()
    {
        return guide;
    }

    public void setGuide(String guide)
    {
        this.guide = guide;
    }

    public List<CatGroupPack> getLstCatGroupPacks()
    {
        return lstCatGroupPacks;
    }

    public void setLstCatGroupPacks(List<CatGroupPack> lstCatGroupPacks)
    {
        this.lstCatGroupPacks = lstCatGroupPacks;
    }

    public DualListModel<CatGroupPack> getDualLstCatGroupPack()
    {
        return dualLstCatGroupPack;
    }

    public void setDualLstCatGroupPack(DualListModel<CatGroupPack> dualLstCatGroupPack)
    {
        this.dualLstCatGroupPack = dualLstCatGroupPack;
    }

    public GenericDaoImplNewV2 getPackHasPromotionService()
    {
        return packHasPromotionService;
    }

    public void setPackHasPromotionService(GenericDaoImplNewV2 packHasPromotionService)
    {
        this.packHasPromotionService = packHasPromotionService;
    }

    //promotion for pack
    public void preEditPromotionForPack(CatPromotion role)
    {
        List<CatGroupPack> packSource = new ArrayList<>();
        List<CatGroupPack> packTarget = new ArrayList<>();
        try
        {

            catPromotionCurr = catPromotionService.findById(role.getCatPromotionId());
            packTarget.addAll(catPromotionCurr.getLstCatGroupPacks());
            for (CatGroupPack bo : lstCatGroupPacks)
            {
                if (!packTarget.contains(bo))
                {
                    packSource.add(bo);
                }
            }
            dualLstCatGroupPack = new DualListModel<>(packSource, packTarget);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        isEdit = true;

        oldObjectStr = (new Gson()).toJson(packTarget);
        ;
    }

    public void onTransfer(TransferEvent event)
    {
        StringBuilder builder = new StringBuilder();
        for (Object item : event.getItems())
        {
            String s = (((CatGroupPack) item).getGroupPackName() != null ? ((CatGroupPack) item).getGroupPackName() : "");
            builder.append(s).append("<br />");
        }

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onSaveOrUpdatePromotionForPack()
    {
        try
        {
            List<CatGroupPack> functionTarget = dualLstCatGroupPack.getTarget();

            List<PackHasPromotion> lstSelect = new ArrayList<>();
            List<PackHasPromotion> lstCspDel = new ArrayList<>();
            List<PackHasPromotion> lstCspResult = new ArrayList<>();

            if (functionTarget != null)
            {
                for (CatGroupPack bo : functionTarget)
                {
                    lstSelect.add(new PackHasPromotion(catPromotionCurr.getCatPromotionId(), bo.getGroupPackId()));
                }
            }
            if (catPromotionCurr.getPackHasPromotions() != null
                    && catPromotionCurr.getPackHasPromotions().size() > 0)
            {
                for (PackHasPromotion bo : catPromotionCurr.getPackHasPromotions())
                {
                    if (lstSelect.contains(bo))
                    {
                        lstCspResult.add(bo);
                    }
                    else
                    {
                        lstCspDel.add(bo);// bo chuc nang
                    }
                }
            }
            for (PackHasPromotion bo : lstSelect)
            {
                if (!lstCspResult.contains(bo))
                {
                    lstCspResult.add(bo);//them chuc nang
                }

            }
            packHasPromotionService.delete(lstCspDel);//bo chuc nang
            packHasPromotionService.saveOrUpdate(lstCspResult);// cap nhat, bo sung chuc nang

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('promotionForPackDlg').hide();");

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }

    }

}
