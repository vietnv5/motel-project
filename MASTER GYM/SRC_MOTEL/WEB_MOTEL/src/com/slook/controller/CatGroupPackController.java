package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.*;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.SessionUtil;
import com.slook.util.StringUtil;
import com.slook.util.Util;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 * Created by xuanh on 5/4/2017.
 */
@ManagedBean
@ViewScoped
public class CatGroupPackController implements Serializable {

    List<CatGroupPack> catGroupPacks;
    List<CatPack> catPacks;
    LazyDataModel<CatGroupPack> lazyCatGroupPacks;
    LazyDataModel<CatPack> lazyCatPacks;

    CatGroupPack currGroupPack;
    CatPack currPack = new CatPack();
    private GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService;
    private GenericDaoServiceNewV2<CatPack, Long> catPackService;
    private GenericDaoServiceNewV2<CatServiceOld, Long> catServiceService;
    private String oldObjectStr = null;
    private String oldObjectGroupStr = null;
    CatServiceOld catServiceTest = new CatServiceOld();

    private List<CatServiceOld> lstCatService;

    // tim kiem
    private CatGroupPack searchForm;
    //
    List<CatGroupPack> lstCatGroupPacksClient;
    List<CatGroupPack> lstCatGroupPacksMember;
    List<CatGroupPack> lstCatGroupPacksGroup;

    @PostConstruct
    public void onStart() {
        try {
            catGroupPackService = new GenericDaoImplNewV2<CatGroupPack, Long>() {
            };
            catPackService = new GenericDaoImplNewV2<CatPack, Long>() {
            };
            catServiceService = new GenericDaoImplNewV2<CatServiceOld, Long>() {
            };
            catGroupPacks = catGroupPackService.findList();
            catPacks = catPackService.findList();
            Map<String, Object> filterDefault = new HashMap<String, Object>();
            filterDefault.put("status-NEQ", -1l);//xoa
            lazyCatGroupPacks = new LazyDataModelBase<>(catGroupPackService, filterDefault);
            lazyCatPacks = new LazyDataModelBase<>(catPackService, filterDefault);
            searchForm = new CatGroupPack();
            Map<String, Object> filterCatService = new HashMap<String, Object>();
            filterCatService.put("status", 1l);
            LinkedHashMap<String, String> orderService = new LinkedHashMap<String, String>();
            orderService.put("serviceName", Constant.ORDER.ASC);
            try {
                lstCatService = catServiceService.findList(filterCatService, orderService);
            } catch (Exception e) {
                e.printStackTrace();
            }

            preAddCatPack();
            preAddCatGroupPack();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCatGroupPack() {
        try {
            Map<String, Object> filterDefault = new HashMap<String, Object>();
            filterDefault.put("status-NEQ", -1l);//xoa
            if (searchForm != null) {
                if (StringUtil.isNotNullAndNullStr(searchForm.getGroupPackName())) {
                    filterDefault.put("groupPackName", searchForm.getGroupPackName());
                }
                if (searchForm.getStatus() != null && searchForm.getStatus() > -1) {
                    filterDefault.put("status", searchForm.getStatus());
                }
            }
            catGroupPacks = catGroupPackService.findList(filterDefault);
        } catch (Exception e) {
        }
        RequestContext.getCurrentInstance().execute("PF('tableGroupPackWidget').clearFilters();");

    }

    public void addPromotionForGroupPack() {
        PromotionGroupPack promotionGroupPack = new PromotionGroupPack();
        promotionGroupPack.setGroupPackId(currGroupPack.getGroupPackId());
        currGroupPack.getPromotionGroupPacks().add(promotionGroupPack);
    }

    public void preEditCatPack(CatPack catPack) {
        catPack.setLimitNumber(catPack.getNumberOfTime() != null && catPack.getNumberOfTime() > 0);
        currPack = catPack;
        oldObjectStr = catPack.toString();
        if (currPack.getCatService() == null) {
            currPack.setCatService(new CatServiceOld());
        }
    }

    public void preAddCatPack() {
        currPack = new CatPack();
        currPack.setLimitNumber(false);
        currPack.setCatService(new CatServiceOld());
        oldObjectStr = null;
    }

    public void saveCatPack() {
        try {
            if (currPack.getCatService() != null) {
                currPack.setServiceId(currPack.getCatService().getServiceId());
            }
            if (!currPack.getLimitNumber()) {
                currPack.setNumberOfTime(null);
            }
            catPackService.saveOrUpdate(currPack);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, currPack.getPackCode(), oldObjectStr, currPack.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, currPack.getPackCode(), oldObjectStr, currPack.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            catPacks = catPackService.findList();
            preAddCatPack();
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteCatPack(CatPack catPack) {
        try {
            List<?> counts = catPackService.findListAll("select count(*) from GroupHasPack where packId = ?", catPack.getPackId());
            if (counts.size() > 0) {
                if (((Long) (counts.get(0))).intValue() > 0) {
                    MessageUtil.setErrorMessage("Gói tập đã tồn tại trong nhóm gói");
                    return;
                }
            }
            catPackService.delete(catPack);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, catPack.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public void preEditCatGroupPackById(Long catGroupPackId) {
        try {
            preEditCatGroupPack(catGroupPackService.findById(catGroupPackId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preEditCatGroupPack(CatGroupPack catGroupPack) {
        oldObjectGroupStr = catGroupPack.toString();
        currGroupPack = catGroupPack;
        currGroupPack.getPack2s().clear();
        currGroupPack.setGroupPackCode(Util.createCode(currGroupPack.getGroupPackName()));
        currGroupPack.getPack2s().addAll(currGroupPack.getPacks());
        currGroupPack.getRoom2s().clear();
        currGroupPack.getRoom2s().addAll(currGroupPack.getRooms());
        currGroupPack.getGroupHasPack2s().clear();
        currGroupPack.getGroupHasPack2s().addAll(currGroupPack.getGroupHasPacks());
//        currGroupPack.computingPrice();
//        currGroupPack.computingPrice2();
    }

    public void preAddCatGroupPack() {
        currGroupPack = new CatGroupPack();
        currGroupPack.setCreateBy(SessionUtil.getCurrentUsername());
        currGroupPack.setDateStartProvide(new Date());
        currGroupPack.setPeriod(100L);//mac dinh set 100'
        oldObjectGroupStr = null;
    }

    public void saveCatGroupPack() {
        try {
            currGroupPack.getPacks().clear();
            currGroupPack.getPacks().addAll(currGroupPack.getPack2s());
            currGroupPack.getRooms().clear();
            currGroupPack.getRooms().addAll(currGroupPack.getRoom2s());
            currGroupPack.getGroupHasPacks().clear();
            currGroupPack.getGroupHasPacks().addAll(currGroupPack.getGroupHasPack2s());
            catGroupPackService.saveOrUpdate(currGroupPack);
            if (currGroupPack.getGroupHasPack2s() != null && currGroupPack.getGroupHasPack2s().size() > 0) {
                for (GroupHasPack bo : currGroupPack.getGroupHasPack2s()) {
                    bo.setGroupPackId(currGroupPack.getGroupPackId());
                }
            }
            new GenericDaoImplNewV2<GroupHasPack, GroupHasPackPK>() {
            }.saveOrUpdate(currGroupPack.getGroupHasPack2s());

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, currGroupPack.getGroupPackCode(), oldObjectStr, currGroupPack.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, currGroupPack.getGroupPackCode(), oldObjectStr, currGroupPack.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            catGroupPacks = catGroupPackService.findList();
            preAddCatGroupPack();
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteCatGroupPack(CatGroupPack catGroupPack) {
        try {
            new GenericDaoImplNewV2<GroupHasPack, GroupHasPackPK>() {
            }.delete(catGroupPack.getGroupHasPacks());
            catGroupPackService.delete(catGroupPack);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, catGroupPack.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public void computingPrice() {

        currPack.setPrice(0.0);

        if (currPack.getCatService() != null && currPack.getCatService().getPrice() != null) {
            currPack.setPrice(Double.valueOf(currPack.getCatService().getPrice()));
        }
        if (currPack.getLimitNumber() && currPack.getNumberOfTime() > 0) {
            currPack.setPrice(currPack.getPrice() * currPack.getNumberOfTime());
        }
    }
    //<editor-fold defaultstate="collapsed" desc="get/set">

    public List<CatGroupPack> getCatGroupPacks() {
        return catGroupPacks;
    }

    public void setCatGroupPacks(List<CatGroupPack> catGroupPacks) {
        this.catGroupPacks = catGroupPacks;
    }

    public List<CatPack> getCatPacks() {
        return catPacks;
    }

    public void setCatPacks(List<CatPack> catPacks) {
        this.catPacks = catPacks;
    }

    public CatGroupPack getCurrGroupPack() {
        return currGroupPack;
    }

    public void setCurrGroupPack(CatGroupPack currGroupPack) {
        this.currGroupPack = currGroupPack;
    }

    public CatPack getCurrPack() {
        return currPack;
    }

    public void setCurrPack(CatPack currPack) {
        this.currPack = currPack;
    }

    public GenericDaoServiceNewV2<CatGroupPack, Long> getCatGroupPackService() {
        return catGroupPackService;
    }

    public void setCatGroupPackService(GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService) {
        this.catGroupPackService = catGroupPackService;
    }

    public GenericDaoServiceNewV2<CatPack, Long> getCatPackService() {
        return catPackService;
    }

    public void setCatPackService(GenericDaoServiceNewV2<CatPack, Long> catPackService) {
        this.catPackService = catPackService;
    }

    public List<CatServiceOld> getLstCatService() {
        return lstCatService;
    }

    public void setLstCatService(List<CatServiceOld> lstCatService) {
        this.lstCatService = lstCatService;
    }
//</editor-fold>

    public GenericDaoServiceNewV2<CatServiceOld, Long> getCatServiceService() {
        return catServiceService;
    }

    public void setCatServiceService(GenericDaoServiceNewV2<CatServiceOld, Long> catServiceService) {
        this.catServiceService = catServiceService;
    }

    public CatServiceOld getCatServiceTest() {
        return catServiceTest;
    }

    public void setCatServiceTest(CatServiceOld catServiceTest) {
        this.catServiceTest = catServiceTest;
    }

    public LazyDataModel<CatGroupPack> getLazyCatGroupPacks() {
        return lazyCatGroupPacks;
    }

    public void setLazyCatGroupPacks(LazyDataModel<CatGroupPack> lazyCatGroupPacks) {
        this.lazyCatGroupPacks = lazyCatGroupPacks;
    }

    public LazyDataModel<CatPack> getLazyCatPacks() {
        return lazyCatPacks;
    }

    public void setLazyCatPacks(LazyDataModel<CatPack> lazyCatPacks) {
        this.lazyCatPacks = lazyCatPacks;
    }

    public CatGroupPack getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(CatGroupPack searchForm) {
        this.searchForm = searchForm;
    }

    public static List<CatMachine> getListCatMachineInGroupPack(CatGroupPack catGroupPack) {
        List<CatMachine> lstRes = new ArrayList<>();
        List<CatRoom> lstRooms = catGroupPack.getRooms();
        if (lstRooms != null && lstRooms.size() > 0) {
            for (CatRoom c : lstRooms) {
                if (c.getCatMachine() != null && !lstRes.contains(c.getCatMachine())) {
                    lstRes.add(c.getCatMachine());
                }
                //out
                if (c.getCatMachineOut() != null && !lstRes.contains(c.getCatMachineOut())) {
                    lstRes.add(c.getCatMachineOut());
                }
            }
        }
        return lstRes;
    }

    /**
     *
     * @param lstCatGroupPack
     * @return
     */
    public static List<CatMachine> getListCatMachineInListGroupPack(List<CatGroupPack> lstCatGroupPack) {
        List<CatMachine> lstRes = new ArrayList<>();
        if (lstCatGroupPack != null && lstCatGroupPack.size() > 0) {
            for (CatGroupPack catGroupPack : lstCatGroupPack) {
                List<CatRoom> lstRooms = catGroupPack.getRooms();
                if (lstRooms != null && lstRooms.size() > 0) {
                    for (CatRoom c : lstRooms) {
                        if (c.getCatMachine() != null && !lstRes.contains(c.getCatMachine())) {
                            lstRes.add(c.getCatMachine());
                        }
                        //out
                        if (c.getCatMachineOut() != null && !lstRes.contains(c.getCatMachineOut())) {
                            lstRes.add(c.getCatMachineOut());
                        }
                    }
                }
            }
        }
        return lstRes;
    }

    /**
     * Lay danh sach ip may can cap nhat off
     *
     * @param lstCatGroupPackON danh goi dang hoat dong
     * @param lstCatGroupPackOFF danh sach goi off di
     * @return Danh sach may cap nhat off
     */
    public static List<CatMachine> getListMachineOffNotExistInON(List<CatGroupPack> lstCatGroupPackON, List<CatGroupPack> lstCatGroupPackOFF) {
        List<CatMachine> lstRes = new ArrayList<>();
        List<CatMachine> lstON = getListCatMachineInListGroupPack(lstCatGroupPackON);
        List<CatMachine> lstOFF = getListCatMachineInListGroupPack(lstCatGroupPackOFF);
        for (CatMachine bo : lstOFF) {
            if (!lstON.contains(bo)) {
                lstRes.add(bo);
            }
        }
        return lstRes;
    }

    // lay danh sach goi theo loai khach hang
    public List<CatGroupPack> listCatGroupPackByCustomer(Long customerType) {
        if (lstCatGroupPacksMember == null) {
            initListCatGroupPacks();
        }
        if (Constant.PAYMENT_TYPE.CLIENT.equals(customerType)) {
            return lstCatGroupPacksClient;
        } else if (Constant.PAYMENT_TYPE.MEMBER.equals(customerType)) {
            return lstCatGroupPacksMember;
        } else if (Constant.PAYMENT_TYPE.GROUP_MEMBER.equals(customerType)) {
            return lstCatGroupPacksGroup;
        }
        return lstCatGroupPacksClient;
    }

    public void initListCatGroupPacks() {
        try {
            Map<String, Object> filterDefault = new HashMap<String, Object>();
              List<Long> lstTypeClient = Arrays.asList(Constant.GROUP_PACK_TYPE.TYPE_LE,0L);
            filterDefault.put("status-NEQ", -1l);//xoa
            filterDefault.put("type", lstTypeClient);
            lstCatGroupPacksClient = catGroupPackService.findList(filterDefault);

            Map<String, Object> filterMember = new HashMap<String, Object>();
            List<Long> lstTypeMember = Arrays.asList(Constant.GROUP_PACK_TYPE.TYPE_LE,
                    Constant.GROUP_PACK_TYPE.TYPE_HV_LUOT, Constant.GROUP_PACK_TYPE.TYPE_HV_TIME,0L);
            filterMember.put("status-NEQ", -1l);//xoa
            filterMember.put("type", lstTypeMember);
            lstCatGroupPacksMember = catGroupPackService.findList(filterMember);

            Map<String, Object> filterGroup = new HashMap<String, Object>();
            List<Long> lstTypeGroupMember = Arrays.asList( Constant.GROUP_PACK_TYPE.TYPE_GROUP,
                    Constant.GROUP_PACK_TYPE.TYPE_HV_LUOT,0L);
            filterGroup.put("status-NEQ", -1l);//xoa
            filterGroup.put("type", lstTypeGroupMember);
            lstCatGroupPacksGroup = catGroupPackService.findList(filterGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
