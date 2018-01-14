/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatGroupPack;
import com.slook.model.CatItemBO;
import com.slook.model.CatMachine;
import com.slook.model.CatPack;
import com.slook.model.CatPromotion;
import com.slook.model.CatService;
import com.slook.model.CatUser;
import com.slook.model.CfgWsTimekeeper;
import com.slook.model.CustomerCheckin;
import com.slook.model.GroupHasMember;
import com.slook.model.GroupMember;
import com.slook.model.GroupMemberPayment;
import com.slook.model.GroupMemberPromotion;
import com.slook.model.GroupMemberUsedService;
import com.slook.model.GroupMembership;
import com.slook.model.Member;
import com.slook.model.V_CustomerAccessStatus;
import com.slook.model.V_GroupMemberDebtPayment;
import com.slook.model.V_GroupMemberUsedService;
import com.slook.object.PaymentPackObj;
import com.slook.object.PrintPaymentForm;
import com.slook.persistence.CfgWsTimekeeperServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.VCustomerAccessStatusServiceImpl;
import com.slook.persistence.VGroupMemberDebtPaymentServiceImpl;
import com.slook.persistence.common.ConditionQuery;
import com.slook.persistence.common.OrderBy;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.HibernateUtil;
import com.slook.util.MessageUtil;
import com.slook.util.PdfUtil;
import com.slook.util.StringUtil;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import static org.omnifaces.util.Faces.getRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

/**
 *
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class GroupMemberController {

    private static final Logger logger = getLogger(GroupMemberController.class);
    LazyDataModel<GroupMember> lazyGroupMember;
    GroupMember curGroupMember;
    Member memberAssign = new Member();
    private GenericDaoServiceNewV2<Member, Long> memberService;
    private GenericDaoServiceNewV2<GroupMember, Long> groupMemberService;
    private GenericDaoServiceNewV2<GroupMembership, Long> groupMemberShipService;
    private GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService;
    private GenericDaoServiceNewV2<GroupMemberPayment, Long> groupMemberPaymentService;
    private GenericDaoServiceNewV2<GroupMemberPromotion, Long> groupMemberPromotionService;
    private GenericDaoServiceNewV2<GroupHasMember, Long> groupHasMemberService;
    private GenericDaoServiceNewV2<V_GroupMemberUsedService, Long> vGroupMemberUsedServiceService;

    @ManagedProperty(value = "#{cfgWsTimekeeperService}")
    private CfgWsTimekeeperServiceImpl cfgWsTimekeeperService;
    private String oldObjectStr = null;
    private String oldObjectGroupMemberShipStr = null;
    private boolean isEdit = false;
    private List<CatPromotion> lstCatPromotions;
    private List<CatItemBO> lstReason;
    private List<GroupHasMember> lstDelGroupHasMembers = new ArrayList<>();
    private Long groupMembershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;

    private LazyDataModel<V_GroupMemberUsedService> lazyVGroupMemberUsedServices;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService;
    private GenericDaoImplNewV2<GroupMemberUsedService, Long> groupMemberUsedServiceService;
    private List<CustomerCheckin> lstCustomerCheckins;

    LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel;
    String billImgPath;
    List<V_GroupMemberDebtPayment> lstGroupMemberDebtPayments = new ArrayList<>();
    V_GroupMemberDebtPayment debtAll = new V_GroupMemberDebtPayment();

    @PostConstruct
    public void onStart() {
        catGroupPackService = new GenericDaoImplNewV2<CatGroupPack, Long>() {
        };
        memberService = new GenericDaoImplNewV2<Member, Long>() {
        };
        groupMemberService = new GenericDaoImplNewV2<GroupMember, Long>() {
        };
        groupMemberShipService = new GenericDaoImplNewV2<GroupMembership, Long>() {
        };
        groupMemberPaymentService = new GenericDaoImplNewV2<GroupMemberPayment, Long>() {
        };
        groupMemberPromotionService = new GenericDaoImplNewV2<GroupMemberPromotion, Long>() {
        };
        groupHasMemberService = new GenericDaoImplNewV2<GroupHasMember, Long>() {
        };
        vGroupMemberUsedServiceService = new GenericDaoImplNewV2<V_GroupMemberUsedService, Long>() {
        };

        groupMemberUsedServiceService = new GenericDaoImplNewV2<GroupMemberUsedService, Long>() {
        };
        customerCheckinService = new GenericDaoImplNewV2<CustomerCheckin, Long>() {
        };
        Map<String, Object> filterDefault = new HashMap<String, Object>();
        filterDefault.put("status-NEQ", -1l);//xoa
        lazyGroupMember = new LazyDataModelBase<>(groupMemberService, filterDefault);
        curGroupMember = new GroupMember();

        removeTempImg();

        lstReason = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.REASON_DEBT, "name");
        lstReason.remove(0);
        Map<String, Object> filterCatPromotion = new HashMap();
        filterCatPromotion.put("status", 1l);
        try {
            lstCatPromotions = (new GenericDaoImplNewV2<CatPromotion, Long>() {
            }).findList(filterCatPromotion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, false, true, true);
    }

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public List<Member> searchMemberInherit(String key) {
        ConditionQuery query = new ConditionQuery();
        Criterion criterion = Restrictions.or(Restrictions.ilike("memberCode", key, MatchMode.ANYWHERE),
                Restrictions.ilike("memberName", key, MatchMode.ANYWHERE),
                Restrictions.ilike("phoneNumber", key, MatchMode.ANYWHERE),
                Restrictions.ilike("email", key, MatchMode.ANYWHERE),
                Restrictions.ilike("cardCode", key, MatchMode.ANYWHERE));
        query.add(criterion);
//        if (curGroupMember != null && curGroupMember.getGroupMemberId() != null) {
//            query.add(Restrictions.not(Restrictions.eq("memberId", curGroupMember.getGroupMemberId())));
//        }
        //vietnv bo sung chi lay ds member
//        Criterion criType = Restrictions.eq("type", Constant.MEMBER_TYPE.MEMBER);
//        query.add(criType);
        query.add(Restrictions.ne("status", Constant.MEMBER_STATUS.DELETE));

        OrderBy orderBy = new OrderBy();
        return memberService.findList(query, orderBy, 1, 100);
    }

    public void preAddGroupMember() {
        removeTempImg();
        curGroupMember = new GroupMember();
        lstDelGroupHasMembers = new ArrayList<>();
        //set chi nhanh mac dinh theo chi nhanh cua nhan vien dang nhap
        CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
        if (user != null && user.getEmployee() != null) {
            curGroupMember.setBranchId(user.getEmployee().getBranchId());
            //set nhan vien phuc vu mac dinh theo account
            curGroupMember.setEmployee(user.getEmployee());
        }
        oldObjectStr = null;
        isEdit = false;

    }

    public void pausingGroupMembership(GroupMembership groupMembership) {
        try {
            String oldMeberShip = groupMembership.toString();

            groupMembership.setFreezeTime(new Date());
            groupMembership.setStatus(2L);
            groupMemberShipService.saveOrUpdate(groupMembership);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, groupMembership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.pausing.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.pausing.unsuccess");
            e.printStackTrace();
        }
    }

    public void playGroupMembership(GroupMembership groupMembership) {
        try {
            String oldMeberShip = groupMembership.toString();

            if (groupMembership.getEndDate() != null && groupMembership.getFreezeTime() != null) {
                Long periodTime = groupMembership.getEndDate().getTime() - groupMembership.getFreezeTime().getTime();
                if (periodTime > 0) {
                    Date newEndDate = new Date((new Date()).getTime() + periodTime);
                    groupMembership.setEndDate(newEndDate);
                }
            }
            groupMembership.setFreezeTime(null);

            groupMembership.setStatus(1L);
            groupMemberShipService.saveOrUpdate(groupMembership);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, groupMembership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.play.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.play.unsuccess");
            e.printStackTrace();
        }
    }

    public void extendGroupMembership(GroupMembership groupMembership) {
        try {
            String oldMeberShip = groupMembership.toString();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(groupMembership.getEndDate());
            CatGroupPack groupPack = groupMembership.getCatGroupPack();
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            groupMembership.setEndDate(endate);
            groupMemberShipService.saveOrUpdate(groupMembership);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, groupMembership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.extend.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.extend.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEditGroupMember(GroupMember groupMember) {
        try {
            curGroupMember = groupMemberService.findById(groupMember.getGroupMemberId());
            oldObjectStr = groupMember.toString();

            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("groupMemberId", groupMember.getGroupMemberId());
            LinkedHashMap<String, String> orderPay = new LinkedHashMap<>();
            orderPay.put("createTime", Constant.ORDER.DESC);

            Map<String, Object> sqlRes = new HashMap<>();
            String hql = "( this_.group_Membership_Id in (select GROUP_MEMBERSHIP_ID from GROUP_MEMBERSHIP where GROUP_MEMBER_ID= ?) )";
            sqlRes.put(hql, curGroupMember.getGroupMemberId());

            List<GroupMemberPayment> lstPay = groupMemberPaymentService.findList(filterPay, sqlRes, orderPay);
            curGroupMember.setGroupMemberPayments(lstPay);

            LinkedHashMap<String, String> orderGMs = new LinkedHashMap<>();
            orderGMs.put("endDate", Constant.ORDER.DESC);
            List<GroupMembership> lstGMs = groupMemberShipService.findList(filterPay, orderGMs);
            curGroupMember.setGroupMemberships(lstGMs);

            LinkedHashMap<String, String> orderPro = new LinkedHashMap<>();
            orderPro.put("createDate", Constant.ORDER.DESC);
            List<GroupMemberPromotion> lstPro = new GenericDaoImplNewV2<GroupMemberPromotion, Long>() {
            }.findList(filterPay, orderPro);
            curGroupMember.setGroupMemberPromotions(lstPro);
            // lay tong cong no
            computinDebt();
            //

            Map<String, Object> filterGroupMember = new HashMap<>();
            filterGroupMember.put("groupMemberId", groupMember.getGroupMemberId());
            List<GroupHasMember> lst = groupHasMemberService.findList(filterGroupMember);
            curGroupMember.setLstGroupHasMembers(lst);
            lstDelGroupHasMembers = new ArrayList<>();
//            RequestContext.getCurrentInstance().execute("PF('tblGroupHasMember').clearFilters();");

            Map<String, Object> filterV = new HashMap<String, Object>();
            filterV.put("groupMemberId", groupMember.getGroupMemberId());//xoa
            LinkedHashMap<String, String> orderV = new LinkedHashMap<>();
            orderV.put("endDate", Constant.ORDER.DESC);
            orderV.put("serviceName", Constant.ORDER.ASC);
            lazyVGroupMemberUsedServices = new LazyDataModelBase<>(vGroupMemberUsedServiceService, filterV, orderV);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        removeTempImg();
        isEdit = true;
    }

    public void preAddGroupMembership() {
        curGroupMember.setNewGroupMembership(new GroupMembership());

        //set gia tri mac dinh
        curGroupMember.setGroupMemberPayment(new GroupMemberPayment());
        curGroupMember.setGroupMemberPromotion(new GroupMemberPromotion());
        curGroupMember.getGroupMemberPayment().createPaymentCode(curGroupMember.getBranchId());

        oldObjectGroupMemberShipStr = null;
    }

    public void saveOrUpdateGroupMembership() {
        try {
            GroupMembership newGroupMembership = curGroupMember.getNewGroupMembership();
            newGroupMembership.setGroupMemberId(curGroupMember.getGroupMemberId());
            newGroupMembership.setStatus(1L);
            groupMemberShipService.saveOrUpdate(newGroupMembership);
            curGroupMember = groupMemberService.findById(curGroupMember.getGroupMemberId());
            //ghi log
            if (oldObjectGroupMemberShipStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectGroupMemberShipStr, newGroupMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectGroupMemberShipStr, newGroupMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteGroupMembership(GroupMembership groupMembership) {
        try {
            groupMemberShipService.delete(groupMembership);
            curGroupMember = groupMemberService.findById(curGroupMember.getGroupMemberId());
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("groupMemberId", curGroupMember.getGroupMemberId());
            LinkedHashMap<String, String> orderGMs = new LinkedHashMap<>();
            orderGMs.put("endDate", Constant.ORDER.DESC);
            List<GroupMembership> lstGMs = groupMemberShipService.findList(filterPay, orderGMs);
            curGroupMember.setGroupMemberships(lstGMs);

            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, groupMembership.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
            preEditGroupMember(curGroupMember);
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void updateEndDate(SelectEvent event) {
        try {
            Date date = (Date) event.getObject();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(curGroupMember.getNewGroupMembership().getGroupPackId());
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curGroupMember.getNewGroupMembership().setEndDate(endate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeTempImg() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("previewProfile");
    }

    public void onDelete(GroupMember groupMember) {
        try {
            String old = groupMember.toString();
            groupMember.setStatus(Constant.MEMBER_STATUS.DELETE);

            groupMemberService.saveOrUpdate(groupMember);

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, groupMember.getGroupMemberCode(), old, groupMember.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            e.printStackTrace();
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
        }
    }

    public void saveOrUpdate() {
        try {
            // validate
            Map<String, Object> filterExist = new HashMap<>();
            filterExist.put("groupMemberCode-EXAC_IGNORE_CASE", curGroupMember.getGroupMemberCode());
            if (curGroupMember.getGroupMemberId() != null) {
                filterExist.put("groupMemberId-NEQ", curGroupMember.getGroupMemberId());
            }
            List<GroupMember> lstExist = groupMemberService.findList(filterExist);
            if (lstExist != null && lstExist.size() > 0) {
                MessageUtil.setErrorMessageFromRes("groupMember.existGroupMemberCode");
                return;
            }
//            if (curGroupMember.getImageFile() != null) {
//                String prefixImgPath = new Date().getTime() + "";
//                if (Util.storeFile(Config.PROFILE_IMAGE_FOLDER, curGroupMember.getImageFile(), prefixImgPath)) {
//                    curGroupMember.setImgPath(prefixImgPath + curGroupMember.getImageFile().getFileName());
//                } else {
//                    MessageUtil.setErrorMessageFromRes("error.save.image.unsuccess");
//                }
//            }
// luu thong tin nhom
            if (curGroupMember.getDeputationMember() != null && curGroupMember.getDeputationMember().getMemberId() != null) {
                curGroupMember.setDeputationMemberId(curGroupMember.getDeputationMember().getMemberId());
            }
            if (curGroupMember.getEmployee() != null) {
                curGroupMember.setEmployeeId(curGroupMember.getEmployee().getEmployeeId());
            }
            groupMemberService.saveOrUpdate(curGroupMember);

            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);

            // neu luu nhom thi luu luon thong tin thanh vien
            saveGroupHasMember();

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, curGroupMember.getGroupMemberCode(), oldObjectStr, curGroupMember.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, curGroupMember.getGroupMemberCode(), oldObjectStr, curGroupMember.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            if (!isEdit) {
                RequestContext.getCurrentInstance().execute("PF('tabViewGroupMemberInfo').select(1)");
            } else {
                RequestContext.getCurrentInstance().execute("PF('groupMemberInfoDlg').hide();");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void paymentGroupMembership() {
        try {
            //validate
            if (curGroupMember.getGroupMemberPayment().getDebt() < 0 && StringUtil.isNullOrEmpty(curGroupMember.getGroupMemberPayment().getReason())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("memberPayment.reason")));
                return;
            }
            //on print
            GroupMembership newGroupMembership = curGroupMember.getNewGroupMembership();
            GroupMemberPayment groupMemberPayment = curGroupMember.getGroupMemberPayment();
            CatPromotion catPromotion = groupMemberPayment.getCatPromotion();
            //km them thoi gian
            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType())
                    && newGroupMembership.getEndDate() != null && catPromotion.getValue() != null) {
                Date date = newGroupMembership.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, catPromotion.getValue().intValue());
                Date endate = calendar.getTime();
                newGroupMembership.setEndDate(endate);
            }

            newGroupMembership.setGroupMemberId(curGroupMember.getGroupMemberId());
            onPrintBill();
            newGroupMembership.setStatus(1L);
            groupMemberShipService.saveOrUpdate(newGroupMembership);
            //groupMemberPayment
            groupMemberPayment.setGroupMemberId(curGroupMember.getGroupMemberId());
            groupMemberPayment.setGroupMembershipId(newGroupMembership.getGroupMembershipId());
            if (catPromotion != null) {
                groupMemberPayment.setCatPromotionId(catPromotion.getCatPromotionId());
            }
            if (getRequest().getSession().getAttribute("user") != null) {
                groupMemberPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị am 
            if (groupMemberPayment.getDebt() > 0) {
                groupMemberPayment.setDebt(0l);
            }
            groupMemberPayment.setCreateTime(new Date());
            groupMemberPayment.setType(Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI);
            groupMemberPaymentService.saveOrUpdate(groupMemberPayment);
            //groupMemberPromotion
            if (catPromotion != null && catPromotion.getCatPromotionId() != null) {

                GroupMemberPromotion groupMemberPromotion = new GroupMemberPromotion();
                groupMemberPromotion.setGroupMemberId(curGroupMember.getGroupMemberId());
                groupMemberPromotion.setCatPromotionId(curGroupMember.getGroupMemberPayment().getCatPromotion().getCatPromotionId());
                groupMemberPromotion.setGroupPackId(newGroupMembership.getGroupPackId());
                String desc = catPromotion.getTypeName() + ": " + catPromotion.getValue()
                        + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                        ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "VNĐ" : "");
                groupMemberPromotion.setDescription(desc);
                groupMemberPromotion.setValue(catPromotion.getValue());
                groupMemberPromotion.setCreateDate(new Date());
                groupMemberPromotion.setGroupMembershipId(newGroupMembership.getGroupMembershipId());
                groupMemberPromotionService.saveOrUpdate(groupMemberPromotion);
            }
            if (!Constant.MEMBERSHIP_ACTION_TYPE.EXTEND.equals(groupMembershipActionType)) {
                //them thong tin user using service
                CatGroupPack catGroupPack = newGroupMembership.getCatGroupPack();
                List<GroupMemberUsedService> lstGroupMemberUsedServices = new ArrayList<GroupMemberUsedService>();
//            List<CatService> lstCatServices = new ArrayList<>();
                try {
                    List<CatPack> lstCatPacks = catGroupPack.getPacks();
                    if (lstCatPacks != null) {
                        for (CatPack bo : lstCatPacks) {
                            if (bo != null && bo.getCatService() != null) {
                                CatService boService = bo.getCatService();

                                GroupMemberUsedService mus = new GroupMemberUsedService();
                                mus.setCreateTime(new Date());
                                mus.setGroupMemberId(curGroupMember.getGroupMemberId());
                                mus.setServiceId(boService.getServiceId());
                                mus.setGroupMembershipId(newGroupMembership.getGroupMembershipId());
                                mus.setStatus(1l);
                                mus.setStartDate(newGroupMembership.getJoinDate());
                                mus.setEndDate(newGroupMembership.getEndDate());
                                mus.setAvailable(bo.getNumberOfTime());
                                mus.setTotalNumber(bo.getNumberOfTime());
                                if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                                        && mus.getAvailable() != null) {
                                    mus.setAvailable(mus.getAvailable() + Math.round(catPromotion.getValue()));
                                    mus.setTotalNumber(mus.getAvailable() + Math.round(catPromotion.getValue()));
                                }
                                lstGroupMemberUsedServices.add(mus);
                            }
                        }
                    }
                    //tao user using service

                    GenericDaoImplNewV2 groupMemberUsedServiceService = new GenericDaoImplNewV2<GroupMemberUsedService, Long>() {
                    };
                    groupMemberUsedServiceService.saveOrUpdate(lstGroupMemberUsedServices);
                    LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, lstGroupMemberUsedServices.toArray().toString(), this.getClass().getSimpleName(),
                            (new Exception("get Name method").getStackTrace()[0].getMethodName()));

//                                Map<String,Object> filterMus=new HashMap();
//                filterMus.put("memberId", newGroupMembership.getGroupMemberId());
//                filterMus.put("membershipId", newGroupMembership.getMembershipId());
//                filterMus.put("status", 1l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //cap nhat lai thoi gian khi gia han
                List<GroupMemberUsedService> lstMemberUsedServices = new ArrayList<GroupMemberUsedService>();
                try {
                    Map<String, Object> filter = new HashMap();
                    filter.put("groupMembershipId", curGroupMember.getNewGroupMembership().getGroupMembershipId());
                    GenericDaoImplNewV2 musService = new GenericDaoImplNewV2<GroupMemberUsedService, Long>() {
                    };

                    lstMemberUsedServices = musService.findList(filter);
                    if (lstMemberUsedServices != null && lstMemberUsedServices.size() > 0) {
                        for (GroupMemberUsedService bo : lstMemberUsedServices) {
                            bo.setEndDate(curGroupMember.getNewGroupMembership().getEndDate());
                        }
                        musService.saveOrUpdate(lstMemberUsedServices);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            curGroupMember = groupMemberService.findById(curGroupMember.getGroupMemberId());
            //ghi log
            if (oldObjectGroupMemberShipStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectGroupMemberShipStr, newGroupMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectGroupMemberShipStr, newGroupMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, groupMemberPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('groupMemberPaymentDlg').hide();");
            //show thanh toan
            if (billImgPath != null) {
                RequestContext.getCurrentInstance().update(":cfImgGroupMemberFormLocal");
                RequestContext.getCurrentInstance().execute("PF('cfImgGroupMemberDlgLocal').show();");
            }
            //clear form
            preEditGroupMember(curGroupMember);
            preAddGroupMembership();

        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void computingPrice() {
        try {
            groupMembershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;
            CatGroupPack catGroupPack = catGroupPackService.findById(curGroupMember.getNewGroupMembership().getGroupPackId());
            curGroupMember.getNewGroupMembership().setCatGroupPack(catGroupPack);
            Long price = Math.round(catGroupPack.getPrice());
            curGroupMember.getGroupMemberPayment().setPrice(price);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangeComputingPrice() {
        try {
            CatGroupPack catGroupPack = curGroupMember.getNewGroupMembership().getCatGroupPack();
            Double price = catGroupPack.getPrice();

            CatPromotion catPromotion = curGroupMember.getGroupMemberPayment().getCatPromotion();
            if (catPromotion != null && catPromotion.getValue() != null) {
                if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType())) {
                    price = price - catPromotion.getValue();
                } else if (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())) {
                    price = price - price * catPromotion.getValue() / 100;
                }
            }
            if (curGroupMember.getGroupMemberPayment().getIsVAT()) {
                price = price * 1.1;
            }
            curGroupMember.getGroupMemberPayment().setPrice(Math.round(price));

            if (curGroupMember.getGroupMemberPayment().getPaymentValue() != null) {
                curGroupMember.getGroupMemberPayment().setDebt(curGroupMember.getGroupMemberPayment().getPaymentValue() - curGroupMember.getGroupMemberPayment().getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void computinDebt() {
        // lay tong cong no
        String sqlDebt = "select sum(debt) debt from group_member_payment where group_member_id=:groupMemberId "
                + " and ( group_Membership_Id in (select GROUP_MEMBERSHIP_ID from GROUP_MEMBERSHIP where GROUP_MEMBER_ID= :groupMemberId) )";
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            SQLQuery sQLQuery = session.createSQLQuery(sqlDebt);
            sQLQuery.setParameter("groupMemberId", curGroupMember.getGroupMemberId());
            BigDecimal res = (BigDecimal) sQLQuery.list().get(0);
            if (res != null) {
                Long debt = ((BigDecimal) sQLQuery.list().get(0)).longValue();
                curGroupMember.setTotalPayment(debt);
            } else {
                curGroupMember.setTotalPayment(0l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    public void preAddLiquidate() {
        //set gia tri mac dinh
        curGroupMember.setGroupMemberPayment(new GroupMemberPayment());
        curGroupMember.getGroupMemberPayment().createPaymentCode(curGroupMember.getBranchId());
        curGroupMember.getGroupMemberPayment().setPrice(curGroupMember.getTotalPayment());

        //lay ds thong tin thanh toan cac goi dang no
        Map<String, Object> filterPayment = new HashMap<>();
        filterPayment.put("groupMemberId", curGroupMember.getGroupMemberId());
        try {
            debtAll.setGroupMembershipId(-1l);
            debtAll.setGroupPackName(MessageUtil.getResourceBundleMessage("totalDebt"));
            debtAll.setSumDept(curGroupMember.getTotalPayment());

            lstGroupMemberDebtPayments = VGroupMemberDebtPaymentServiceImpl.getInstance().findList(filterPayment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        oldObjectGroupMemberShipStr = null;
    }

    public void onChangeComputingLiquidate() {
        try {
            if (curGroupMember.getGroupMemberPayment().getPaymentValue() != null) {
                curGroupMember.getGroupMemberPayment().setDebt(curGroupMember.getGroupMemberPayment().getPaymentValue() + curGroupMember.getGroupMemberPayment().getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liquidate() {
        try {
            //groupMemberPayment
            GroupMemberPayment groupMemberPayment = curGroupMember.getGroupMemberPayment();
            groupMemberPayment.setGroupMemberId(curGroupMember.getGroupMemberId());

            if (getRequest().getSession().getAttribute("user") != null) {
                groupMemberPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị duong la phan thu tien cua khach hang
            if (groupMemberPayment.getPaymentValue() <= -groupMemberPayment.getPrice()) {
                groupMemberPayment.setDebt(groupMemberPayment.getPaymentValue());
            } else {
                groupMemberPayment.setDebt(-groupMemberPayment.getPrice());
            }
            //set lai gia tien bang 0 vi day la tra no
            groupMemberPayment.setCreateTime(new Date());
            groupMemberPayment.setType(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO);

            // xu ly tao thong tin tra no cho tat ca cac khoan
            List<GroupMemberPayment> paymetPerPacks = new ArrayList<>();
            if (groupMemberPayment.getCurDebt() == null || groupMemberPayment.getCurDebt().getGroupMembershipId() == null
                    || groupMemberPayment.getCurDebt().getGroupMembershipId().equals(-1L)) {
                if (lstGroupMemberDebtPayments != null && lstGroupMemberDebtPayments.size() > 0) {
                    Long paymentDebt = groupMemberPayment.getDebt();
                    for (V_GroupMemberDebtPayment bo : lstGroupMemberDebtPayments) {
                        if (paymentDebt > 0 && bo.getGroupMembershipId() != null) {
                            GroupMemberPayment newPay = new GroupMemberPayment();
                            newPay.setGroupMemberId(groupMemberPayment.getGroupMemberId());
                            newPay.setEmployeeId(groupMemberPayment.getEmployeeId());
                            newPay.setPaymentCode(groupMemberPayment.getPaymentCode());
                            newPay.setReason(groupMemberPayment.getReason());
                            newPay.setDescription(groupMemberPayment.getDescription());
                            newPay.setCreateTime(groupMemberPayment.getCreateTime());
                            newPay.setGroupMembershipId(bo.getGroupMembershipId());
                            newPay.setPrice(0l);
                            newPay.setDebtTotal(-bo.getSumDept());

                            newPay.setType(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO);
                            if (paymentDebt >= -bo.getSumDept()) {
                                newPay.setDebt(-bo.getSumDept());

                                //giam tien
                                paymentDebt += bo.getSumDept();
                            } else {
                                newPay.setDebt(paymentDebt);
                                paymentDebt = 0l;
                            }
                            newPay.setPaymentValue(newPay.getDebt());
                            paymetPerPacks.add(newPay);
                        } else {
                            break;
                        }
                    }
                }
                onPrintBillLiquidate(paymetPerPacks);
                groupMemberPaymentService.saveOrUpdate(paymetPerPacks);
            } else {// truong hop chi lam 1 goi
                groupMemberPayment.setGroupMembershipId(groupMemberPayment.getCurDebt().getGroupMembershipId());
                groupMemberPayment.setDebtTotal(-groupMemberPayment.getPrice());
                paymetPerPacks.add(groupMemberPayment);

                onPrintBillLiquidate(paymetPerPacks);
                groupMemberPayment.setPrice(0l);//set lai de luu sau khi in file thanh toan
                groupMemberPaymentService.saveOrUpdate(groupMemberPayment);
            }

//            curGroupMember = groupMemberService.findById(curGroupMember.getGroupMemberId());
            //ghi log
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, groupMemberPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('groupMemberLiquidateDlg').hide();");
            //show thanh toan
            if (billImgPath != null) {
                RequestContext.getCurrentInstance().update(":cfImgGroupMemberFormLocal");
                RequestContext.getCurrentInstance().execute("PF('cfImgGroupMemberDlgLocal').show();");
            }
            //clear form
            preAddGroupMembership();

            preEditGroupMember(curGroupMember);
//            computinDebt();
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void addGroupHasMember() {
        if (curGroupMember.getLstGroupHasMembers() == null) {
            curGroupMember.setLstGroupHasMembers(new ArrayList<>());
        }
        GroupHasMember ghm = new GroupHasMember();
        ghm.setMember(memberAssign);
        ghm.setGroupMemberId(curGroupMember.getGroupMemberId());
        ghm.setMemberId(memberAssign.getMemberId());
        ghm.setStatus(1l);

        if (!curGroupMember.getLstGroupHasMembers().contains(ghm)) {
            curGroupMember.getLstGroupHasMembers().add(0, ghm);
            MessageUtil.setInfoMessageFromRes("msg.insert.succ");
        } else {
            MessageUtil.setErrorMessageFromRes("error.member.exist");
        }
        memberAssign = new Member();
    }

    public void onDeleteGroupMemberInList(GroupHasMember ob) {

        try {
            int size = curGroupMember.getLstGroupHasMembers().size();

            int i = 0;
            for (int j = 0; j < size; j++) {
                if (curGroupMember.getLstGroupHasMembers().get(i).equals(ob)) {
                    lstDelGroupHasMembers.add(curGroupMember.getLstGroupHasMembers().get(i));
                    curGroupMember.getLstGroupHasMembers().remove(curGroupMember.getLstGroupHasMembers().get(i));
                    i--;
                }
                i++;
            }
            MessageUtil.setInfoMessageFromRes("info.delete.success");
            RequestContext.getCurrentInstance().execute("PF('tblGroupHasMember').clearFilters();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuccess");
            logger.error(e.getMessage(), e);
        }
    }

    public void saveGroupHasMember() throws Exception {
        try {

            //del
            if (lstDelGroupHasMembers != null && !lstDelGroupHasMembers.isEmpty()) {
                List<GroupHasMember> lstDel = new ArrayList<>();
                for (GroupHasMember vsubo : lstDelGroupHasMembers) {
                    if (vsubo.getId() != null) {
                        lstDel.add(vsubo);
                    }
                }
                groupHasMemberService.delete(lstDel);
            }

            if (curGroupMember.getLstGroupHasMembers() == null || curGroupMember.getLstGroupHasMembers().size() == 0) {
                curGroupMember.setLstGroupHasMembers(new ArrayList<>());
            }

            if (curGroupMember.getLstGroupHasMembers() != null && curGroupMember.getLstGroupHasMembers().size() > 0) {
                for (GroupHasMember bo : curGroupMember.getLstGroupHasMembers()) {
                    bo.setGroupMemberId(curGroupMember.getGroupMemberId());
                }
            }
            Map<String, Object> filterGroup = new HashMap<>();
            filterGroup.put("groupMemberId", curGroupMember.getGroupMemberId());
            List<GroupHasMember> lstCurrent = groupHasMemberService.findList(filterGroup);
            List<Long> lstMemId = new ArrayList<>();
            if (lstCurrent != null && lstCurrent.size() > 0) {
                for (GroupHasMember bo : lstCurrent) {
                    lstMemId.add(bo.getMemberId());
                }
            }
            // loai trung 
            Set<Long> setIdMember = new HashSet<>(lstMemId);
            for (GroupHasMember bo : curGroupMember.getLstGroupHasMembers()) {
                if (!setIdMember.contains(bo.getMemberId())) {
                    setIdMember.add(bo.getMemberId());
                    groupHasMemberService.saveOrUpdate(bo);
                }
            }
            // luu thanh vien dai dien
            if (!setIdMember.contains(curGroupMember.getDeputationMemberId())) {
                GroupHasMember ghm = new GroupHasMember();
                ghm.setGroupMemberId(curGroupMember.getGroupMemberId());
                ghm.setMemberId(curGroupMember.getDeputationMemberId());
                ghm.setStatus(1l);
                groupHasMemberService.saveOrUpdate(ghm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.setErrorMessage("Lỗi lưu danh sách thành viên!");

        }

    }

    public void preExtendGroupMembership(GroupMembership groupMembership) {
        try {
            groupMembershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.EXTEND;

            curGroupMember.setNewGroupMembership(groupMembership);

            Calendar calendar = Calendar.getInstance();
            if (groupMembership.getEndDate() != null && groupMembership.getEndDate().getTime() >= (new Date()).getTime()) {
                calendar.setTime(groupMembership.getEndDate());
            } else {
                calendar.setTime(new Date());
            }
            CatGroupPack groupPack = groupMembership.getCatGroupPack();
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            groupMembership.setEndDate(endate);

            CatGroupPack catGroupPack = catGroupPackService.findById(curGroupMember.getNewGroupMembership().getGroupPackId());
            curGroupMember.getNewGroupMembership().setCatGroupPack(catGroupPack);
            Long price = Math.round(catGroupPack.getPrice());
            curGroupMember.getGroupMemberPayment().setPrice(price);
            curGroupMember.getGroupMemberPayment().createPaymentCode(curGroupMember.getBranchId());

//            groupMemberShipService.saveOrUpdate(groupMembership);
            //log
//            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, groupMembership.toString(), this.getClass().getSimpleName(),
//                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//
//            MessageUtil.setInfoMessageFromRes("info.extend.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.extend.unsuccess");
            e.printStackTrace();
        }
    }

    //check in cho nhom
    public void preCheckin(GroupMembership membership) {
        List<GroupHasMember> lstGhms = curGroupMember.getLstGroupHasMembers();
        lstCustomerCheckins = new ArrayList<>();
        if (lstGhms != null && lstGhms.size() > 0) {
            for (GroupHasMember bo : lstGhms) {
                CustomerCheckin cu = new CustomerCheckin();
                cu.setCustomerId(bo.getMemberId());
                cu.setGroupMemberId(bo.getGroupMemberId());
                cu.setMembershipId(membership.getGroupMembershipId());
                cu.setType(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
                cu.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
                cu.setCustomerName(bo.getMember().getMemberName());
                cu.setMember(bo.getMember());
                lstCustomerCheckins.add(cu);
            }
        }
        if ((Constant.MEMBERSHIP_STATUS.ACTIVE.equals(membership.getStatus()))
                && (membership.getJoinDate() == null || membership.getJoinDate().getTime() <= (new Date()).getTime())
                && (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime())) {
            RequestContext.getCurrentInstance().execute("PF('groupCheckinDlg').show();");
            curGroupMember.setNewGroupMembership(membership);
        } else {
            MessageUtil.setErrorMessage("gói phải ở trạng thái đang hoạt động hoặc dược chuyển nhượng và khoảng thời gian hoạt động là hợp lệ!");
        }

    }

    public void checkin() {
        try {
            List<CustomerCheckin> lstCheckins = new ArrayList<>();
            for (CustomerCheckin customerCheckin : lstCustomerCheckins) {
                customerCheckin.setCheckTime(new Date());
                if (StringUtil.isNotNull(customerCheckin.getCardCode())) {
                    lstCheckins.add(customerCheckin);
                }
            }
            if (lstCheckins.size() == 0) {
                MessageUtil.setErrorMessage("Chưa nhập mã khóa đeo tay để Checkin!");
                return;
            }
            //filter ds service su dung cho goi
            Map<String, Object> filter = new HashMap();
            filter.put("groupMemberId", curGroupMember.getGroupMemberId());
            filter.put("groupMembershipId", curGroupMember.getNewGroupMembership().getGroupMembershipId());
            filter.put("status", Constant.MEMBER_USED_SERVICE.VALID);
            filter.put("startDate-LE", new Date());
            filter.put("endDate-GE", DateTimeUtils.trunc(new Date()));
            List<GroupMemberUsedService> lst = groupMemberUsedServiceService.findList(filter);
            if (lst != null && lst.size() > 0) {
                for (GroupMemberUsedService bo : lst) {
                    if (bo.getAvailable() != null && bo.getAvailable() >= lstCheckins.size()) {
                        bo.setAvailable(bo.getAvailable() - lstCheckins.size());
                    } else if (bo.getTotalNumber() != null && bo.getTotalNumber() > 0) {
                        MessageUtil.setErrorMessage("Dịch vụ " + bo.getCatService().getServiceName() + " của gói không còn đủ lượt sử dụng");
                        return;
                    }
                    //luon ghi lai so luot su dung
                    if (bo.getUsedNumber() == null) {
                        bo.setUsedNumber(0l);
                    }
                    bo.setUsedNumber(bo.getUsedNumber() + lstCheckins.size());

                }
            } else {

                MessageUtil.setErrorMessage("Không có dịch vụ còn hiệu lực để sử dụng");
                return;
            }
            //cap nhat quyen vao may quet
            List<String> lstCardCode = new ArrayList<>();
            for (CustomerCheckin customerCheckin : lstCheckins) {
                updateAccessCatMachineNew(customerCheckin.getMember(), curGroupMember.getNewGroupMembership(), Constant.METHOD.INSERT, customerCheckin.getCardCode());
                lstCardCode.add(customerCheckin.getCardCode());
            }
            groupMemberUsedServiceService.saveOrUpdate(lst);

            customerCheckinService.saveOrUpdate(lstCheckins);
            MessageUtil.setInfoMessageFromRes("info.checkin.success");
            RequestContext.getCurrentInstance().execute("PF('groupCheckinDlg').hide();");

// show result
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {
                showResultAccessStatus(lstCardCode);
                RequestContext.getCurrentInstance().execute("PF('groupCheckinResultDlg').show();");
                RequestContext.getCurrentInstance().update("@widgetVar('groupCheckinResultDlg')");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    public void updateAccessCatMachineNew(Member member, GroupMembership membershipDel, String method, String cardCode) {

        List<CatGroupPack> lstON = new ArrayList<>();
        List<CatGroupPack> lstOFF = new ArrayList<>();
        if (membershipDel != null && membershipDel.getCatGroupPack() != null) {
            lstOFF.add(membershipDel.getCatGroupPack());
        }

        try {

            List<CatMachine> lstCatMachineOff = CatGroupPackController.getListMachineOffNotExistInON(lstON, lstOFF);
            List<String> lstIp = new ArrayList<>();

// cap nhat xoa khoi may quet the
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(method);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : lstCatMachineOff) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("MEM_" + member.getMemberId() + "|" + member.getMemberName() + "|" + cardCode + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
                if (!lstIp.contains(catMachine.getIp())) {
                    lstIp.add(catMachine.getIp());
                }
            }
            //goi ws online
            if (Constant.METHOD.INSERT.equals(method)) {
                CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.ACTIVE, Constant.WS_C_METHOD.TYPE_CARD);
            } else {
                CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_CARD);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //
    public void showResultAccessStatus(List<String> listCardCode) {

        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put("cardCode", listCardCode);
            lazyDataCustomerAccessModel = new LazyDataModelBase<V_CustomerAccessStatus, Long>(VCustomerAccessStatusServiceImpl.getInstance(), filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public LazyDataModel<V_CustomerAccessStatus> getLazyDataCustomerAccessModel() {
        return lazyDataCustomerAccessModel;
    }

    public void setLazyDataCustomerAccessModel(LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel) {
        this.lazyDataCustomerAccessModel = lazyDataCustomerAccessModel;
    }

    public GenericDaoServiceNewV2<CustomerCheckin, Long> getCustomerCheckinService() {
        return customerCheckinService;
    }

    public void setCustomerCheckinService(GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService) {
        this.customerCheckinService = customerCheckinService;
    }

    public GenericDaoImplNewV2<GroupMemberUsedService, Long> getGroupMemberUsedServiceService() {
        return groupMemberUsedServiceService;
    }

    public void setGroupMemberUsedServiceService(GenericDaoImplNewV2<GroupMemberUsedService, Long> groupMemberUsedServiceService) {
        this.groupMemberUsedServiceService = groupMemberUsedServiceService;
    }

    public List<CustomerCheckin> getLstCustomerCheckins() {
        return lstCustomerCheckins;
    }

    public void setLstCustomerCheckins(List<CustomerCheckin> lstCustomerCheckins) {
        this.lstCustomerCheckins = lstCustomerCheckins;
    }

    public LazyDataModel<GroupMember> getLazyGroupMember() {
        return lazyGroupMember;
    }

    public void setLazyGroupMember(LazyDataModel<GroupMember> lazyGroupMember) {
        this.lazyGroupMember = lazyGroupMember;
    }

    public GroupMember getCurGroupMember() {
        return curGroupMember;
    }

    public void setCurGroupMember(GroupMember curGroupMember) {
        this.curGroupMember = curGroupMember;
    }

    public Member getMemberAssign() {
        return memberAssign;
    }

    public void setMemberAssign(Member memberAssign) {
        this.memberAssign = memberAssign;
    }

    public GenericDaoServiceNewV2<Member, Long> getMemberService() {
        return memberService;
    }

    public void setMemberService(GenericDaoServiceNewV2<Member, Long> memberService) {
        this.memberService = memberService;
    }

    public GenericDaoServiceNewV2<GroupMember, Long> getGroupMemberService() {
        return groupMemberService;
    }

    public void setGroupMemberService(GenericDaoServiceNewV2<GroupMember, Long> groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    public GenericDaoServiceNewV2<GroupMembership, Long> getGroupMemberShipService() {
        return groupMemberShipService;
    }

    public void setGroupMemberShipService(GenericDaoServiceNewV2<GroupMembership, Long> groupMemberShipService) {
        this.groupMemberShipService = groupMemberShipService;
    }

    public GenericDaoServiceNewV2<CatGroupPack, Long> getCatGroupPackService() {
        return catGroupPackService;
    }

    public void setCatGroupPackService(GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService) {
        this.catGroupPackService = catGroupPackService;
    }

    public GenericDaoServiceNewV2<GroupMemberPayment, Long> getGroupMemberPaymentService() {
        return groupMemberPaymentService;
    }

    public void setGroupMemberPaymentService(GenericDaoServiceNewV2<GroupMemberPayment, Long> groupMemberPaymentService) {
        this.groupMemberPaymentService = groupMemberPaymentService;
    }

    public GenericDaoServiceNewV2<GroupMemberPromotion, Long> getGroupMemberPromotionService() {
        return groupMemberPromotionService;
    }

    public void setGroupMemberPromotionService(GenericDaoServiceNewV2<GroupMemberPromotion, Long> groupMemberPromotionService) {
        this.groupMemberPromotionService = groupMemberPromotionService;
    }

    public GenericDaoServiceNewV2<GroupHasMember, Long> getGroupHasMemberService() {
        return groupHasMemberService;
    }

    public void setGroupHasMemberService(GenericDaoServiceNewV2<GroupHasMember, Long> groupHasMemberService) {
        this.groupHasMemberService = groupHasMemberService;
    }

    public CfgWsTimekeeperServiceImpl getCfgWsTimekeeperService() {
        return cfgWsTimekeeperService;
    }

    public void setCfgWsTimekeeperService(CfgWsTimekeeperServiceImpl cfgWsTimekeeperService) {
        this.cfgWsTimekeeperService = cfgWsTimekeeperService;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }

    public String getOldObjectGroupMemberShipStr() {
        return oldObjectGroupMemberShipStr;
    }

    public void setOldObjectGroupMemberShipStr(String oldObjectGroupMemberShipStr) {
        this.oldObjectGroupMemberShipStr = oldObjectGroupMemberShipStr;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public List<CatPromotion> getLstCatPromotions() {
        return lstCatPromotions;
    }

    public void setLstCatPromotions(List<CatPromotion> lstCatPromotions) {
        this.lstCatPromotions = lstCatPromotions;
    }

    public List<CatItemBO> getLstReason() {
        return lstReason;
    }

    public void setLstReason(List<CatItemBO> lstReason) {
        this.lstReason = lstReason;
    }

    public List<GroupHasMember> getLstDelGroupHasMembers() {
        return lstDelGroupHasMembers;
    }

    public void setLstDelGroupHasMembers(List<GroupHasMember> lstDelGroupHasMembers) {
        this.lstDelGroupHasMembers = lstDelGroupHasMembers;
    }

    public Long getGroupMembershipActionType() {
        return groupMembershipActionType;
    }

    public void setGroupMembershipActionType(Long groupMembershipActionType) {
        this.groupMembershipActionType = groupMembershipActionType;
    }

    public GenericDaoServiceNewV2<V_GroupMemberUsedService, Long> getvGroupMemberUsedServiceService() {
        return vGroupMemberUsedServiceService;
    }

    public void setvGroupMemberUsedServiceService(GenericDaoServiceNewV2<V_GroupMemberUsedService, Long> vGroupMemberUsedServiceService) {
        this.vGroupMemberUsedServiceService = vGroupMemberUsedServiceService;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public LazyDataModel<V_GroupMemberUsedService> getLazyVGroupMemberUsedServices() {
        return lazyVGroupMemberUsedServices;
    }

    public void setLazyVGroupMemberUsedServices(LazyDataModel<V_GroupMemberUsedService> lazyVGroupMemberUsedServices) {
        this.lazyVGroupMemberUsedServices = lazyVGroupMemberUsedServices;
    }

    public List<V_GroupMemberDebtPayment> getLstGroupMemberDebtPayments() {
        return lstGroupMemberDebtPayments;
    }

    public void setLstGroupMemberDebtPayments(List<V_GroupMemberDebtPayment> lstGroupMemberDebtPayments) {
        this.lstGroupMemberDebtPayments = lstGroupMemberDebtPayments;
    }

    public V_GroupMemberDebtPayment getDebtAll() {
        return debtAll;
    }

    public void setDebtAll(V_GroupMemberDebtPayment debtAll) {
        this.debtAll = debtAll;
    }

//</editor-fold>
    /**
     * onPrint: in anh
     */
    public void onPrintBill() {
        PrintPaymentForm printForm = getObjectBillImg();
//        billImgPath = ImageStreamerBill.generateBillImg(getObjectBillImg());
        billImgPath = PdfUtil.generateBillPdf(printForm);
    }

    public PrintPaymentForm getObjectBillImg() {
        PrintPaymentForm paymentForm = new PrintPaymentForm();
        try {

            GroupMembership newMembership = curGroupMember.getNewGroupMembership();
            GroupMemberPayment groupMemberPayment = curGroupMember.getGroupMemberPayment();
            CatPromotion catPromotion = groupMemberPayment.getCatPromotion();

            String customerName = "";
            String customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type3");
            paymentForm.setCustomerType(Constant.PAYMENT_TYPE.GROUP_MEMBER);
            Long numMember = curGroupMember.getNumMemberInGroup();

            String employeeName = "";
            Long vatPrice = 0l;
            Long discount = 0l;
            Long totalPrice = 0l;// khong xac dinh chinh xac duoc
            Long oldDept = 0l;
            Long totalDept = 0l;

            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            if (user != null && user.getEmployee() != null) {
                employeeName = user.getEmployee().getEmployeeName();

            }
            if (curGroupMember.getGroupMemberName() != null) {
                customerName = curGroupMember.getGroupMemberName();
            }

            // custPays payment
            Long paymentValue = groupMemberPayment.getPaymentValue();
            //cong no chi co gia trị am 
            Long debt = groupMemberPayment.getDebt();
            if (groupMemberPayment.getDebt() > 0) {
                paymentValue = groupMemberPayment.getPrice();
                debt = 0l;
            }
            //lay gia trị duong
            if (debt < 0) {
                debt = -debt;
            }
            //
            if (curGroupMember.getTotalPayment() != null) {
                oldDept = Math.abs(curGroupMember.getTotalPayment());
                totalDept = oldDept;
            }
            if (debt != null) {
                totalDept += debt;
            }
            paymentForm.setPaymentTime(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY));
            // Bill code
            paymentForm.setPaymentCode(groupMemberPayment.getPaymentCode());
            // customer
            paymentForm.setCustomerName(customerName);
            paymentForm.setCustomerTypeName(customerTypeName);
            paymentForm.setNumCustomer(numMember != null ? numMember : 1l);
            paymentForm.setEmployeeName(employeeName);
            paymentForm.setBarCode("");

            List<GroupMemberPayment> lstMemberPayments = new ArrayList<>();
            List<PaymentPackObj> lstPaymentPackObjs = new ArrayList<>();
            lstMemberPayments.add(groupMemberPayment);

            if (lstMemberPayments != null) {
                for (GroupMemberPayment bo : lstMemberPayments) {
                    PaymentPackObj payPack = new PaymentPackObj();
                    // item
//                    String itemStr = iPack.get("item");
                    // price dong gia
                    CatGroupPack catGroupPack = newMembership.getCatGroupPack();
                    Long price = null;
                    String groupPackName = "";
                    if (catGroupPack != null && catGroupPack.getPrice() != null) {
                        price = catGroupPack.getPrice().longValue();
                        groupPackName = catGroupPack.getGroupPackName();

                    }
                    // tinh tong vat
                    if (bo.getIsVAT() && bo.getPrice() != null) {
                        vatPrice += bo.getPrice() / 11;
                        discount += price - bo.getPrice() * 10 / 11;
                    } else {
                        discount += price - bo.getPrice();
                    }
                    // num
                    Long numPack = 1l;
                    payPack.setQuantity(numPack);
                    payPack.setPrice(price);

                    if (price != null) {
                        totalPrice += numPack * price;
                    }
                    //KM

                    payPack.setProm(ImageStreamerBill.descProm(catPromotion));
                    payPack.setAmount(bo.getPrice());
                    payPack.setExp(DateTimeUtils.format(newMembership.getEndDate(), Constant.PATTERN.DATE_COMMON_YY));
                    payPack.setGroupPackName(groupPackName);
                    lstPaymentPackObjs.add(payPack);
                }
            }
            paymentForm.setLstPaymentPackObjs(lstPaymentPackObjs);
            // giam gia
            paymentForm.setTotalPriceService(totalPrice);
            paymentForm.setDiscount(discount);
            paymentForm.setVatPrice(vatPrice);
            // Total 
            paymentForm.setTotal(groupMemberPayment.getPrice());
            // Total khach phai tra
            paymentForm.setMustPay(groupMemberPayment.getPrice());
            // deposit
            // khach tra
            paymentForm.setCustomerPay(paymentValue);
            // cong no mơi
            paymentForm.setNewDept(debt);
            // debt old
            paymentForm.setOldDept(oldDept);
            // tong cong no total dept
            paymentForm.setTotalDept(totalDept);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return paymentForm;
    }

    public String getBillImgPath() {
        return billImgPath;
    }

    public void setBillImgPath(String billImgPath) {
        this.billImgPath = billImgPath;
    }

    public void onPrintBillLiquidate(List<GroupMemberPayment> paymetPerPacks) {
        PrintPaymentForm printForm = getObjectBillImg(paymetPerPacks);
//        billImgPath = ImageStreamerBill.generateBillImg(getObjectBillImg(paymetPerPacks));
        billImgPath = PdfUtil.generateBillPdf(printForm);
    }

    public PrintPaymentForm getObjectBillImg(List<GroupMemberPayment> paymetPerPacks) {
        PrintPaymentForm paymentForm = new PrintPaymentForm();
        try {

//            Membership newMembership = curGroupMember.getNewMembership();
            GroupMemberPayment groupMemberPayment = curGroupMember.getGroupMemberPayment();
//            CatPromotion catPromotion = groupMemberPayment.getCatPromotion();

            String customerName = "";
            String customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type3");
            paymentForm.setCustomerType(Constant.PAYMENT_TYPE.GROUP_MEMBER);
            Long numMember = curGroupMember.getNumMemberInGroup();

            String employeeName = "";
            Long vatPrice = 0l;
            Long discount = 0l;
            Long totalPrice = 0l;// khong xac dinh chinh xac duoc
            Long oldDept = 0l;
            Long totalDept = 0l;

            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            if (user != null && user.getEmployee() != null) {
                employeeName = user.getEmployee().getEmployeeName();

            }
            if (curGroupMember.getGroupMemberName() != null) {
                customerName = curGroupMember.getGroupMemberName();
            }

            // custPays payment
            Long paymentValue = groupMemberPayment.getDebt();
            //cong no chi co gia trị am 
            Long debt = 0l;// tra no nen ko co them cong no
//            oldDept = -groupMemberPayment.getPrice();
            oldDept = -curGroupMember.getTotalPayment();
            totalDept = oldDept - paymentValue;
            if (totalDept < 0) {
                totalDept = -totalDept;
            }

            paymentForm.setPaymentTime(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY));
            // Bill code
            paymentForm.setPaymentCode(groupMemberPayment.getPaymentCode());
            // customer
            paymentForm.setCustomerName(customerName);
            paymentForm.setCustomerTypeName(customerTypeName);
            paymentForm.setNumCustomer(numMember != null ? numMember : 1l);
            paymentForm.setEmployeeName(employeeName);
            paymentForm.setBarCode("");

//            List<GroupMemberPayment> lstMemberPayments = new ArrayList<>();
//            lstMemberPayments.add(groupMemberPayment);
            List<PaymentPackObj> lstPaymentPackObjs = new ArrayList<>();

            if (paymetPerPacks != null) {
                for (GroupMemberPayment bo : paymetPerPacks) {
                    PaymentPackObj payPack = new PaymentPackObj();
                    // item
//                    String itemStr = iPack.get("item");
                    // price dong gia
                    GroupMembership membership = new GroupMembership();
                    try {
                        membership = groupMemberShipService.findById(bo.getGroupMembershipId());
                    } catch (Exception e) {
                    }
                    CatGroupPack catGroupPack = membership.getCatGroupPack();
//                    Long price = null;
                    String groupPackName = "";
                    if (catGroupPack != null && catGroupPack.getPrice() != null) {
//                        price = catGroupPack.getPrice().longValue();
                        groupPackName = catGroupPack.getGroupPackName();

                    }
                    // tinh tong vat
                    /*if (bo.getIsVAT() && bo.getPrice() != null) {
                        vatPrice += bo.getPrice() / 11;
                        discount += price - bo.getPrice() * 10 / 11;
                    } else {
                        discount += price - bo.getPrice();
                    }*/
                    // num
                    Long numPack = 1l;
                    payPack.setQuantity(numPack);
                    /*
                    payPack.setPrice(price);

                    if (price != null) {
                        totalPrice += numPack * price;
                    }
                    //KM
                    String desc = "";
                    if (catPromotion != null && catPromotion.getValue() != null) {

                        if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType())) {
                            desc = DataUtil.getStringNumber(catPromotion.getValue());
                        } else {
                            desc = catPromotion.getValue().toString();

                        }
                        desc = desc
                                + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                                ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "" : " lần");
                    }
                    payPack.setProm(desc);
                     */
                    payPack.setAmount(bo.getDebtTotal());
                    payPack.setExp(DateTimeUtils.format(membership.getEndDate(), Constant.PATTERN.DATE_COMMON_YY));
                    payPack.setGroupPackName(groupPackName);
                    lstPaymentPackObjs.add(payPack);
                }
            }
            paymentForm.setLstPaymentPackObjs(lstPaymentPackObjs);
            /*
            // giam gia
            paymentForm.setTotalPriceService(totalPrice);
            paymentForm.setDiscount(discount);
            paymentForm.setVatPrice(vatPrice);
            // Total 
            paymentForm.setTotal(groupMemberPayment.getPrice());
             */
            // Total khach phai tra
//            paymentForm.setMustPay(oldDept);
            // deposit
            // khach tra
            paymentForm.setCustomerPay(paymentValue);
            // cong no mơi
            paymentForm.setNewDept(debt);
            // debt old
            paymentForm.setOldDept(oldDept);
            // tong cong no total dept
            paymentForm.setTotalDept(totalDept);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return paymentForm;
    }

    public void onchangeDebtPack() {
        if (curGroupMember.getGroupMemberPayment().getCurDebt() != null) {
            curGroupMember.getGroupMemberPayment().setPrice(curGroupMember.getGroupMemberPayment().getCurDebt().getSumDept());
            onChangeComputingLiquidate();
        }
    }
}
