package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.*;
import com.slook.object.PaymentPackObj;
import com.slook.object.PrintPaymentForm;
import com.slook.persistence.CatMachineServiceImpl;
import com.slook.persistence.CfgWsTimekeeperServiceImpl;
import com.slook.persistence.CustomerCheckinServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.ServiceTicketServiceImpl;
import com.slook.persistence.VCustomerCheckinServiceImpl;
import com.slook.persistence.VMemberDebtPaymentServiceImpl;
import com.slook.persistence.common.ConditionQuery;
import com.slook.persistence.common.OrderBy;
import com.slook.util.*;
import com.slook.webservice.GymFingerprintImpl;
import com.slook.webservice.GymWsMCCImpl;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

import static org.apache.log4j.Logger.getLogger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import static org.omnifaces.util.Faces.getRequest;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 * Created by xuanh on 4/22/2017.
 */
@ManagedBean
@ViewScoped
public class MemberController {

    private static final Logger logger = getLogger(MemberController.class);

    LazyDataModel<Member> lazyMember;
    Member selectedMember;
    Member curMember;
    Member memberAssign;
    private GenericDaoServiceNewV2<Member, Long> memberService;
    private GenericDaoServiceNewV2<Membership, Long> memberShipService;
    private GenericDaoServiceNewV2<MemberHealth, Long> memberHealthService;
    private GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService;
    private GenericDaoServiceNewV2<MemberCheckin, Long> memberCheckinService;
    private GenericDaoServiceNewV2<MemberPayment, Long> memberPaymentService;
    private GenericDaoServiceNewV2<MemberPromotion, Long> memberPromotionService;
    private GenericDaoServiceNewV2<GroupHasMember, Long> groupHasMemberService;
    private GenericDaoServiceNewV2<V_MemberUsedServiceFull, Long> vMemberUserServiceFullService;
    private GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService;
    private GenericDaoImplNewV2<MemberUsedService, Long> memberUsedServiceService;

    @ManagedProperty(value = "#{vCustomerCheckinService}")
    private VCustomerCheckinServiceImpl vCustomerCheckinService;
    @ManagedProperty(value = "#{cfgWsTimekeeperService}")
    private CfgWsTimekeeperServiceImpl cfgWsTimekeeperService;

    private List<CatItemBO> lstHealthLevel;
    private Map<String, CatItemBO> mapHealthLevel;
    private int memberEndToDay, memberJoinDateToDay, numMemberBirthDateOfTheMonth;
    StreamedContent fileExported;
    private String oldObjectStr = null;
    private String oldObjectMeberShipStr = null;
    private String oldObjectMemberHealthStr = null;
    private String oldObjectMemberPaymentStr = null;
    private boolean isEdit = false;
    //
    private List<CatPromotion> lstCatPromotions;
    private List<CatPromotion> lstCatPromotionsFull;

    private List<CatItemBO> lstReason;

    private Long typeMember = Constant.MEMBER_TYPE.MEMBER;
    private List<GroupHasMember> lstDelGroupHasMembers = new ArrayList<>();

    private Long membershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;

    private LazyDataModel<V_MemberUsedServiceFull> lazyVMemberUsedServiceFull;

    private List<Boolean> columnVisibale = new ArrayList<>();

    private CustomerCheckin customerCheckin = new CustomerCheckin();
    private LazyDataModel<V_CustomerCheckin> lazyDataVCusCheckin;
    private boolean isAddUsePack = false;

    GenericDaoImplNewV2<V_CustomerAccessStatus, Long> customerAccessStatusService;
    LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel;
    V_CustomerAccessStatus currVcustomerAccessStatus = new V_CustomerAccessStatus();
    String billImgPath;
    String fingerprintPathNew = "";
    boolean isChangeFingerprint = false;
    private File fingerprintFile;
    private boolean IS_LOCAL = false;
    List<V_MemberDebtPayment> lstMemberDebtPayments = new ArrayList<>();
    V_MemberDebtPayment debtAll = new V_MemberDebtPayment();
    private String memberIdStr = "";
    private String memberNameStr = "";
    private List<ServiceTicket> lstCheckinTicket;
    private String pathRealFile = "";
//    private LazyDataModel<MemberPayment> lazyMemberPayments;

    @PostConstruct
    public void onStart() {
        catGroupPackService = new GenericDaoImplNewV2<CatGroupPack, Long>() {
        };
        memberService = new GenericDaoImplNewV2<Member, Long>() {
        };
        memberShipService = new GenericDaoImplNewV2<Membership, Long>() {
        };
        memberHealthService = new GenericDaoImplNewV2<MemberHealth, Long>() {
        };
        memberCheckinService = new GenericDaoImplNewV2<MemberCheckin, Long>() {
        };
        memberPaymentService = new GenericDaoImplNewV2<MemberPayment, Long>() {
        };
        memberPromotionService = new GenericDaoImplNewV2<MemberPromotion, Long>() {
        };
        groupHasMemberService = new GenericDaoImplNewV2<GroupHasMember, Long>() {
        };
        vMemberUserServiceFullService = new GenericDaoImplNewV2<V_MemberUsedServiceFull, Long>() {
        };
        customerCheckinService = new GenericDaoImplNewV2<CustomerCheckin, Long>() {
        };
        memberUsedServiceService = new GenericDaoImplNewV2<MemberUsedService, Long>() {
        };
        LinkedHashMap<String, String> orderMember = new LinkedHashMap<>();
        orderMember.put("joinDate", Constant.ORDER.DESC);
        Map<String, Object> filterDefault = new HashMap<String, Object>();
        filterDefault.put("status-NEQ", -1l);//xoa
        lazyMember = new LazyDataModelBase<>(memberService, filterDefault, orderMember);
        curMember = new Member();

        lstHealthLevel = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.HEALTH_LEVEL, "name");
        mapHealthLevel = CommonUtil.getMapCatItemByKeyValue(Constant.CAT_CODE.HEALTH_LEVEL);
        removeTempImg();

        memberEndToDay = memberEndToDay();
        memberJoinDateToDay = memberJoinDateToDay();
        numMemberBirthDateOfTheMonth = numMemberBirthDateOfTheMonth();
        lstReason = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.REASON_DEBT, "name");
        lstReason.remove(0);
        Map<String, Object> filterCatPromotion = new HashMap();
        filterCatPromotion.put("status", 1l);
        IS_LOCAL = Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"));
        try {
            lstCatPromotionsFull = (new GenericDaoImplNewV2<CatPromotion, Long>() {
            }).findList(filterCatPromotion);
//            if (IS_LOCAL) {
//                GymFingerprintImpl.getInstance().closeCurrentSensor();
//            }
            lstCatPromotions = new ArrayList<>(lstCatPromotionsFull);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //init
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, false, true, true);
        isChangeFingerprint = false;
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
        if (curMember != null && curMember.getMemberId() != null) {
            query.add(Restrictions.not(Restrictions.eq("memberId", curMember.getMemberId())));
        }
        //vietnv bo sung chi lay ds member
        Criterion criType = Restrictions.eq("type", Constant.MEMBER_TYPE.MEMBER);
        query.add(criType);
        query.add(Restrictions.ne("status", Constant.MEMBER_STATUS.DELETE));

        OrderBy orderBy = new OrderBy();
        return memberService.findList(query, orderBy, 1, 100);
    }

    public void preAddMember(Long type) {
        removeTempImg();
        curMember = new Member();
        curMember.setType(type);
        typeMember = type;
        lstDelGroupHasMembers = new ArrayList<>();
        if (Constant.MEMBER_TYPE.MEMBER.equals(type)) {
            curMember.setSex(MessageUtil.getKey("view.label.sexMale"));
        }
        //set chi nhanh mac dinh theo chi nhanh cua nhan vien dang nhap
        CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
        if (user != null && user.getEmployee() != null) {
            curMember.setBranchId(user.getEmployee().getBranchId());
            //set nhan vien phuc vu mac dinh theo account
            curMember.setEmployee(user.getEmployee());
        }
        curMember.setJoinDate(new Date());
        curMember.setStatus(Constant.MEMBER_STATUS.NEW);
//        curMember.setMemberType(Constant.MEMBER_TYPE.GRADE_COPPER);

        preAddMembership();

        oldObjectStr = null;
        isEdit = false;

        callFingerprintPerUpdateNew(curMember, isEdit);

    }

//    public void preEditMember() {
//        curMember = selectedMember;
//        removeTempImg();
//        oldObjectStr = selectedMember.toString();
//        isEdit = true;
//
//    }
    public void transferMembership(Membership membership) {
        try {
            String oldMeberShip = membership.toString();
            membershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.TRANSFER;

            curMember.setNewMembership(memberShipService.findById(membership.getMembershipId()));
            curMember.setMemberPayment(new MemberPayment());
            curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());
//            curMember.getMemberPayment().setPrice(Long.valueOf(DataConfig.getConfigByKey("PRICE_TRANSFER")));
            onChangeComputingPrice();
            curMember.getMemberPayment().setType(Constant.PAYMENT_TYPE.FEE_TRANSFER);//xem nhu la phat sinh 1 goi
            curMember.getMemberPayment().setDescription("Phí chuyển nhượng gói tập");
            if (memberAssign == null) {
                MessageUtil.setErrorMessageFromRes("error.transter.unsuccess");
                return;
            };
            RequestContext.getCurrentInstance().execute("PF('paymentDlg').show();");
            RequestContext.getCurrentInstance().update(":paymentDlgId");

//            Membership membership1 = new Membership();
            /*
            Membership membership1 = CommonUtil.mapper.map(membership, Membership.class);
            membership.setMemberId(null);//tao moi
            membership1.setStatus(4L);
            membership1.setGroupPackId(membership.getGroupPackId());
            membership1.setMemberId(memberAssign.getMemberId());
            membership1.setMemberGrantorId(curMember.getMemberId());
            membership1.setJoinDate(membership.getJoinDate());
            membership1.setEndDate(membership.getEndDate());
            membership.setStatus(3L);
            membership.setMemberAssignId(memberAssign.getMemberId());

//            updateAccessCatMachine(memberAssign, membership1, Constant.METHOD.INSERT);
            memberShipService.saveOrUpdate(membership1);
            memberShipService.saveOrUpdate(membership);
            //write log
            // cap nhat
//            updateAccessCatMachine(curMember, membership, Constant.METHOD.DELETE);

            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, membership1.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.transter.success");
             */
        } catch (Exception e) {
            MessageUtil.setInfoMessageFromRes("error.transter.unsuccess");
            e.printStackTrace();
        }
    }

    public void pausingMembership(Membership membership) {
        try {
            String oldMeberShip = membership.toString();

            membership.setFreezeTime(new Date());
            membership.setStatus(2L);
            memberShipService.saveOrUpdate(membership);
//            updateAccessCatMachine(curMember, membership, Constant.METHOD.DELETE);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.pausing.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.pausing.unsuccess");
            e.printStackTrace();
        }
    }

    public void playMembership(Membership membership) {
        try {
            String oldMeberShip = membership.toString();

            if (membership.getEndDate() != null && membership.getFreezeTime() != null) {
                Long periodTime = membership.getEndDate().getTime() - membership.getFreezeTime().getTime();
                if (periodTime > 0) {
                    Date newEndDate = new Date((new Date()).getTime() + periodTime);
                    membership.setEndDate(newEndDate);
                }
            }
            membership.setFreezeTime(null);

            membership.setStatus(1L);

//            updateAccessCatMachine(curMember, membership, Constant.METHOD.INSERT);
            memberShipService.saveOrUpdate(membership);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.play.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.play.unsuccess");
            e.printStackTrace();
        }
    }

    public void extendMembership(Membership membership) {
        try {
            String oldMeberShip = membership.toString();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(membership.getEndDate());
            CatGroupPack groupPack = membership.getGroupPack();
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            membership.setEndDate(endate);

//            updateAccessCatMachine(curMember, membership, Constant.METHOD.INSERT);
            memberShipService.saveOrUpdate(membership);

            //log
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.extend.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.extend.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEditMember(Member member) {
        try {
            curMember = memberService.findById(member.getMemberId());
            oldObjectStr = member.toString();

            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("memberId", member.getMemberId());

            LinkedHashMap<String, String> orderPay = new LinkedHashMap<>();
            orderPay.put("createTime", Constant.ORDER.DESC);
            Map<String, Object> sqlRes = new HashMap<>();
            String hql = "( this_.membership_Id in (select a.membership_Id from membership a where a.member_id= ? and a.status!=-1) )";
            sqlRes.put(hql, curMember.getMemberId());

            List<MemberPayment> lstPay = memberPaymentService.findList(filterPay, sqlRes, orderPay);
            curMember.setMemberPayments(lstPay);

//            lazyMemberPayments = new LazyDataModelBase<>(memberPaymentService, filterPay,  sqlRes, orderPay);
            Map<String, Object> filterMbs = new HashMap<>();
            filterMbs.put("memberId", member.getMemberId());

            LinkedHashMap<String, String> orderMs = new LinkedHashMap<>();
            orderMs.put("endDate", Constant.ORDER.DESC);
            filterMbs.put("status-NEQ", Constant.MEMBER_STATUS.DELETE);
            List<Membership> lstMs = memberShipService.findList(filterMbs, orderMs);
            curMember.setMemberships(lstMs);
            // lay tong cong no
            curMember.setTotalPayment(computinDebt(curMember.getMemberId()));

            Map<String, Object> filterV = new HashMap<String, Object>();
            filterV.put("memberId", member.getMemberId());//xoa
            LinkedHashMap<String, String> orderV = new LinkedHashMap<>();
            orderV.put("endDate", Constant.ORDER.DESC);
            orderV.put("serviceName", Constant.ORDER.ASC);
            lazyVMemberUsedServiceFull = new LazyDataModelBase<>(vMemberUserServiceFullService, filterV, orderV);

            //
            typeMember = curMember.getType();
            if (Constant.MEMBER_TYPE.GROUP_MEMBER.equals(curMember.getType())) {
                Map<String, Object> filterGroupMember = new HashMap<>();
                filterGroupMember.put("groupMemberId", member.getMemberId());
                List lst = groupHasMemberService.findList(filterGroupMember);
                curMember.setLstGroupHasMembers(lst);
                lstDelGroupHasMembers = new ArrayList<>();
            }
            //
            preAddMembership();
            //lay file van tay hien len
            callFingerprintPerUpdateNew(curMember, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        removeTempImg();
        isEdit = true;
    }

    public void preAddMembership() {
        curMember.setNewMembership(new Membership());
        curMember.getNewMembership().setNumberPack(1L);

        //set gia tri mac dinh
        curMember.setMemberPayment(new MemberPayment());
        curMember.setMemberPromotion(new MemberPromotion());
        curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());

        oldObjectMeberShipStr = null;
    }

    public void saveOrUpdateMembership() {
        try {
            Membership newMembership = curMember.getNewMembership();
            newMembership.setMemberId(curMember.getMemberId());
            newMembership.setStatus(1L);
            memberShipService.saveOrUpdate(newMembership);
            curMember = memberService.findById(curMember.getMemberId());
            //ghi log
            if (oldObjectMeberShipStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteMembership(Membership membership) {
        try {
            membership.setStatus(Constant.MEMBER_STATUS.DELETE);

            memberShipService.saveOrUpdate(membership);
//            memberShipService.delete(membership);
            //set lai cho may cham cong
            updateAccessCatMachine(curMember, membership, Constant.METHOD.DELETE);

            //curMember = memberService.findById(curMember.getMemberId());
            //lay lai thong in
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("memberId", curMember.getMemberId());
            LinkedHashMap<String, String> orderMs = new LinkedHashMap<>();
            orderMs.put("endDate", Constant.ORDER.DESC);
            filterPay.put("status-NEQ", Constant.MEMBER_STATUS.DELETE);
            List<Membership> lstMs;
            try {
                lstMs = memberShipService.findList(filterPay, orderMs);
                curMember.setMemberships(lstMs);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MemberController.class.getName()).log(Level.SEVERE, null, ex);
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, membership.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            deleteCustomerSchedulePack(membership);//xoa bang lien quan

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void updateEndDate(SelectEvent event) {
        try {
            Date date = (Date) event.getObject();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(curMember.getNewMembership().getGroupPackId());
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curMember.getNewMembership().setEndDate(endate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeTempImg() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("previewProfile");
    }

    public void deleteMember() {
        try {
            memberService.delete(curMember);

            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, curMember.getMemberCode(), curMember.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            e.printStackTrace();
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
        }
    }

    public void onDelete(Member member) {
        try {
            String old = member.toString();
            member.setStatus(Constant.MEMBER_STATUS.DELETE);

            memberService.saveOrUpdate(member);

            //update may cham cong
            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(Constant.METHOD.DELETE);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : list) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("MEM_" + member.getMemberId() + "|" + member.getMemberName() + "|" + member.getCardCode() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, member.getMemberCode(), old, member.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            e.printStackTrace();
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
        }
    }

    public void saveOrUpdateWithFingerprint() {
        saveOrUpdate();
        //luu ma van tay lam key may quyet van tay
        if (!IS_LOCAL) {
            GymWsMCCImpl.getInstance().registerUserFingerprint(memberIdStr, memberNameStr);
        }

    }

    public boolean validateMember() {
        if (isEdit) {
            if (Constant.MEMBER_STATUS.STOP.equals(curMember.getStatus())) {
                if (curMember.getMembershipAvailbles() != null && !curMember.getMembershipAvailbles().isEmpty()) {
                    MessageUtil.setErrorMessage("Không được cập nhật cho khách hàng đang có gói còn hiện lực thành trạng thái " + MessageUtil.getResourceBundleMessage("member.status.STOP"));
                    return false;
                }
                if (curMember.getTotalPayment() != null && curMember.getTotalPayment() < 0) {
                    MessageUtil.setErrorMessage("Khách hàng phải thanh toán hết nợ mới được phép cập nhật trạng thái " + MessageUtil.getResourceBundleMessage("member.status.STOP"));
                    return false;
                }
            }
            if (Constant.MEMBER_STATUS.STOP.equals(curMember.getStatus())
                    || Constant.MEMBER_STATUS.PAUSE.equals(curMember.getStatus())) {
                // lay ds dich vu dang active
                Map<String, Object> filter = new HashMap<>();
                filter.put("customerId", curMember.getMemberId());
                filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
                filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
                List lst = new ArrayList();
                try {
                    lst = vCustomerCheckinService.findList(filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lst != null && !lst.isEmpty()) {
                    MessageUtil.setErrorMessage("Phải ngừng sử dụng dịch vụ của khách hàng trước khi cập nhật trạng thái "
                            + MessageUtil.getResourceBundleMessage("member.status.PAUSE") + " hoặc " + MessageUtil.getResourceBundleMessage("member.status.STOP"));
                    preCheckout();
                    RequestContext.getCurrentInstance().update("@widgetVar(checkoutDlg)");
                    RequestContext.getCurrentInstance().execute("PF('checkoutDlg').show();");
                    return false;
                }
            }
        }
        return true;
    }

    public void saveOrUpdate() {
        try {
            // validate
            if (!validateMember()) {
                return;
            }

            Map<String, Object> filterExist = new HashMap<>();
//            filterExist.put("memberCode-EXAC_IGNORE_CASE", curMember.getMemberCode());
            filterExist.put("phoneNumber-EXAC_IGNORE_CASE", curMember.getPhoneNumber());
            filterExist.put("memberName-EXAC_IGNORE_CASE", curMember.getMemberName());
            if (curMember.getMemberId() != null) {
                filterExist.put("memberId-NEQ", curMember.getMemberId());
            }
            List<Member> lstExist = memberService.findList(filterExist);
            if (lstExist != null && lstExist.size() > 0) {
//                MessageUtil.setErrorMessageFromRes("member.existMemberCode");
                MessageUtil.setErrorMessageFromRes("member.existMemberNameAndPhone");
                return;
            }
            if (curMember.getImageFile() != null) {
                String prefixImgPath = new Date().getTime() + "";
                if (Util.storeFile(Config.PROFILE_IMAGE_FOLDER, curMember.getImageFile(), prefixImgPath)) {
                    curMember.setImgPath(prefixImgPath + curMember.getImageFile().getFileName());
                } else {
                    MessageUtil.setErrorMessageFromRes("error.save.image.unsuccess");
                }
            }
            // luu van tay
//            if (curMember.getFingerprintPath() != null && StringUtil.isNotNull(fingerprintPathNew)) {
//                saveNewFingerprint();
//            }

            // luu thong tin nhom
            //            if (curMember.getDeputationMember() != null && curMember.getDeputationMember().getMemberId() != null) {
            //                curMember.setDeputationMemberId(curMember.getDeputationMember().getMemberId());
            //            }
            if (curMember.getEmployee() != null) {
                curMember.setEmployeeId(curMember.getEmployee().getEmployeeId());
            }
            memberService.saveOrUpdate(curMember);
            //lay thong tin de lay dau van tay
            memberIdStr = curMember.getMemberId().toString();
            memberNameStr = curMember.getMemberName();

            if (StringUtil.isNullOrEmpty(curMember.getMemberCode())) {
                curMember.setMemberCode(curMember.getMemberId().toString());
            }

            if (StringUtil.isNullOrEmpty(curMember.getCardCode())) {
                curMember.setCardCode(curMember.getMemberId().toString());
                memberService.saveOrUpdate(curMember);
            }
            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);

            // Them ban ghi vao bang cfg_ws_timekeeper de ws c# biet la can them user
            /*
            if (!Constant.MEMBER_TYPE.GROUP_MEMBER.equals(curMember.getType())) {
                if (!isEdit) {
                    CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                    cfg.setInsertTime(new Date());
                    cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                    cfg.setMethod(Constant.METHOD.INSERT);
                    cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                    for (CatMachine catMachine : list) {
                        // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                        cfg.setContent("MEM_" + curMember.getMemberId() + "|" + curMember.getMemberName() + "|" + curMember.getCardCode() + "|1|" + catMachine.getIp());
                        cfgWsTimekeeperService.save(cfg);
                    }
                } else {
                    CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                    cfg.setInsertTime(new Date());
                    cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                    cfg.setMethod(Constant.METHOD.UPDATE);
                    cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                    for (CatMachine catMachine : list) {
                        // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                        cfg.setContent("MEM_" + curMember.getMemberId() + "|" + curMember.getMemberName() + "|" + curMember.getCardCode() + "|1|" + catMachine.getIp());
                        cfgWsTimekeeperService.save(cfg);
                    }
                }
            }
             */
            // neu luu nhom thi luu luon thong tin thanh vien
            if (Constant.MEMBER_TYPE.GROUP_MEMBER.equals(curMember.getType())) {
                saveGroupHasMember();
            }

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, String.valueOf(curMember.getMemberId()), oldObjectStr, curMember.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, String.valueOf(curMember.getMemberId()), oldObjectStr, curMember.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            if (!isEdit) {
                RequestContext.getCurrentInstance().execute("PF('tabViewMemberInfo').select(1)");
            } else {
                RequestContext.getCurrentInstance().execute("PF('memberInfoDlg').hide();");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void oncapture(CaptureEvent captureEvent) {

        final byte[] data = captureEvent.getData();

        curMember.setImageFile(new UploadedFile() {
            @Override
            public String getFileName() {
                return "capture.jpeg";
            }

            @Override
            public InputStream getInputstream() throws IOException {
                return null;
            }

            @Override
            public long getSize() {
                return data.length;
            }

            @Override
            public byte[] getContents() {
                return data;
            }

            @Override
            public String getContentType() {
                return "jpeg";
            }

            @Override
            public void write(String s) throws Exception {

            }
        });
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previewProfile", data);
        RequestContext.getCurrentInstance().update("@widgetVar(lighbox)");

//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" +
//                File.separator + "images" + File.separator + "photocam" + File.separator + filename + ".jpeg";
//
//        FileImageOutputStream imageOutput;
//        try {
//            imageOutput = new FileImageOutputStream(new File(newFileName));
//            imageOutput.write(data, 0, data.length);
//            imageOutput.close();
//        }
//        catch(IOException e) {
//            throw new FacesException("Error in writing captured image.", e);
//        }
    }

    public void handleImageUpload(FileUploadEvent event) {
        curMember.setImageFile(event.getFile());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("previewProfile", event.getFile());
        RequestContext.getCurrentInstance().update("@widgetVar(lighbox)");
    }

    public Member getCurMember() {
        return curMember;
    }

    public void setCurMember(Member curMember) {
        this.curMember = curMember;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

    public LazyDataModel<Member> getLazyMember() {
        return lazyMember;
    }

    public void setLazyMember(LazyDataModel<Member> lazyMember) {
        this.lazyMember = lazyMember;
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

    public List<CatItemBO> getLstHealthLevel() {
        return lstHealthLevel;
    }

    public void setLstHealthLevel(List<CatItemBO> lstHealthLevel) {
        this.lstHealthLevel = lstHealthLevel;
    }

    public int getMemberEndToDay() {
        return memberEndToDay;
    }

    public void setMemberEndToDay(int memberEndToDay) {
        this.memberEndToDay = memberEndToDay;
    }

    public int getMemberJoinDateToDay() {
        return memberJoinDateToDay;
    }

    public void setMemberJoinDateToDay(int memberJoinDateToDay) {
        this.memberJoinDateToDay = memberJoinDateToDay;
    }

    public int getNumMemberBirthDateOfTheMonth() {
        return numMemberBirthDateOfTheMonth;
    }

    public void setNumMemberBirthDateOfTheMonth(int numMemberBirthDateOfTheMonth) {
        this.numMemberBirthDateOfTheMonth = numMemberBirthDateOfTheMonth;
    }

    public StreamedContent getFileExported() {
        return fileExported;
    }

    public void setFileExported(StreamedContent fileExported) {
        this.fileExported = fileExported;
    }

    public CfgWsTimekeeperServiceImpl getCfgWsTimekeeperService() {
        return cfgWsTimekeeperService;
    }

    public void setCfgWsTimekeeperService(CfgWsTimekeeperServiceImpl cfgWsTimekeeperService) {
        this.cfgWsTimekeeperService = cfgWsTimekeeperService;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public GenericDaoServiceNewV2<MemberCheckin, Long> getMemberCheckinService() {
        return memberCheckinService;
    }

    public void setMemberCheckinService(GenericDaoServiceNewV2<MemberCheckin, Long> memberCheckinService) {
        this.memberCheckinService = memberCheckinService;
    }

    public List<V_MemberDebtPayment> getLstMemberDebtPayments() {
        return lstMemberDebtPayments;
    }

    public void setLstMemberDebtPayments(List<V_MemberDebtPayment> lstMemberDebtPayments) {
        this.lstMemberDebtPayments = lstMemberDebtPayments;
    }

    public V_MemberDebtPayment getDebtAll() {
        return debtAll;
    }

    public void setDebtAll(V_MemberDebtPayment debtAll) {
        this.debtAll = debtAll;
    }

    //    vietnv add start
    public void preAddMemberHealth() {
        curMember.setNewMemberHealth(new MemberHealth());
        oldObjectMemberHealthStr = null;
    }

    public void preEditMemberHealth(MemberHealth memberHealth) {
        curMember.setNewMemberHealth(memberHealth);
        oldObjectMemberHealthStr = memberHealth.toString();
    }

    public void saveOrUpdateMemberHealth() {
        try {
            MemberHealth newMemberHealth = curMember.getNewMemberHealth();
            newMemberHealth.setMemberId(curMember.getMemberId());
            memberHealthService.saveOrUpdate(newMemberHealth);

            //ghi log
            if (oldObjectMemberHealthStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectMemberHealthStr, newMemberHealth.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectMemberHealthStr, newMemberHealth.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            curMember = memberService.findById(curMember.getMemberId());
            MessageUtil.setInfoMessageFromRes("info.save.success");
            curMember.setNewMemberHealth(new MemberHealth());
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void deleteMemberHealth(MemberHealth memberHealth) {
        try {
            memberHealthService.delete(memberHealth);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, memberHealth.toString(), null, this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            curMember = memberService.findById(curMember.getMemberId());
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public String getNameHealthLevel(String value) {
        if (mapHealthLevel != null && mapHealthLevel.get(value) != null) {
            return mapHealthLevel.get(value).getName();
        }
        return "";
    }

    public int memberEndToDay() {
        int num = 0;
        try {
            Map<String, Object> filter = new HashMap<>();
            Date d = new Date();
            filter.put("endDate", new Date(d.getYear(), d.getMonth(), d.getDate()));
            filter.put("status-NEQ", -1l);//xoa
            num = memberService.count2(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return num;
    }

    public int memberJoinDateToDay() {
        int num = 0;
        try {
            Map<String, Object> filter = new HashMap<>();
            Date d = new Date();
            filter.put("joinDate", new Date(d.getYear(), d.getMonth(), d.getDate()));
            filter.put("status-NEQ", -1l);//xoa
            num = memberService.count2(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return num;
    }

    public int numMemberBirthDateOfTheMonth() {
        int num = 0;
        try {
            Map<String, Object> filter = new HashMap<>();
            Date d = new Date();
            filter.put("birthDay", new Date(d.getYear(), d.getMonth(), d.getDate()));
            filter.put("status-NEQ", -1l);//xoa
            num = memberService.count(null, "select count(*) numMember from MEMBER  where EXTRACT(month from BIRTH_DAY)=EXTRACT(month from sysdate) and status!=-1", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return num;
    }

    public void exportMember() {
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branch.branchName", "ASC");
            order.put("memberName", "ASC");
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", -1l);//xoa
            List<Member> empList = memberService.findList(filter, order);

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "Template_member.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "member.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "member.xls";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }
            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            Map<Long, String> m = new HashMap<>();
            m.put(0L, "Nữ");
            m.put(1L, "Nam");
            context.putVar("members", empList);
            context.putVar("timeNow", new Date());
            context.putVar("sex", m);

            JxlsHelper.getInstance().processTemplate(is, os, context);

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "memberEndToday.xls");

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public void exportMemberEndToday() {
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branch.branchName", "ASC");
            order.put("memberName", "ASC");
            Map<String, Object> filter = new HashMap<>();
            Date d = new Date();
            filter.put("endDate", new Date(d.getYear(), d.getMonth(), d.getDate()));
            filter.put("status-NEQ", -1l);//xoa
            List<Member> empList = memberService.findList(filter, order);

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "Template_memberEndToday.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "memberEndToday.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "memberEndToday.xls";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }
            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            Map<Long, String> m = new HashMap<>();
            m.put(0L, "Nữ");
            m.put(1L, "Nam");
            context.putVar("members", empList);
            context.putVar("timeNow", new Date());
            context.putVar("sex", m);

            JxlsHelper.getInstance().processTemplate(is, os, context);

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "memberEndToday.xls");

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public void exportMemberJoinDateToDay() {
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branch.branchName", "ASC");
            order.put("memberName", "ASC");
            Map<String, Object> filter = new HashMap<>();
            Date d = new Date();
            filter.put("joinDate", new Date(d.getYear(), d.getMonth(), d.getDate()));
            filter.put("status-NEQ", -1l);//xoa
            List<Member> empList = memberService.findList(filter, order);

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "Template_memberJoinDateToDay.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "memberJoinDateToDay.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "memberJoinDateToDay.xls";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }
            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            Map<Long, String> m = new HashMap<>();
            m.put(0L, "Nữ");
            m.put(1L, "Nam");
            context.putVar("members", empList);
            context.putVar("timeNow", new Date());
            context.putVar("sex", m);

            JxlsHelper.getInstance().processTemplate(is, os, context);

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "memberJoinDateToDay.xls");

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public void exportMemberBirthDateOfTheMonth() {
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branch.branchName", "ASC");
            order.put("memberName", "ASC");
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", -1l);//xoa
//            Date d = new Date();
//            filter.put("joinDate", new Date(d.getYear(), d.getMonth(), d.getDate()));

            Map<String, Object> sqlRes = new HashMap<>();
            String hql = "( EXTRACT(month from BIRTH_DAY)=EXTRACT(month from sysdate) )";
            sqlRes.put(hql, null);

            List<Member> empList = memberService.findList(filter, sqlRes, order);

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "Template_memberBirthDateOfTheMonth.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "memberBirthDateOfTheMonth.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "memberBirthDateOfTheMonth.xls";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }
            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            Map<Long, String> m = new HashMap<>();
            m.put(0L, "Nữ");
            m.put(1L, "Nam");
            context.putVar("members", empList);
            context.putVar("timeNow", new Date());
            context.putVar("sex", m);

            JxlsHelper.getInstance().processTemplate(is, os, context);

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "memberBirthDateOfTheMonth.xls");

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public void paymentMembership() {
        try {
            //validate
            if (curMember.getMemberPayment().getDebt() < 0 && StringUtil.isNullOrEmpty(curMember.getMemberPayment().getReason())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("memberPayment.reason")));
                return;
            }
            //on print

            Membership newMembership = curMember.getNewMembership();

            Long numberPerPack = null;
            boolean createTicket = false;
            if (newMembership.getMembershipId() == null && !Constant.MEMBERSHIP_ACTION_TYPE.EXTEND.equals(membershipActionType)) {
                //them thong tin user using service
                CatGroupPack catGroupPack = newMembership.getGroupPack();
                List<CatPack> lstCatPacks = catGroupPack.getPacks();
                if (lstCatPacks != null) {
                    for (CatPack bo : lstCatPacks) {
                        if (bo != null && bo.getCatService() != null) {
                            if (bo.getNumberOfTime() != null && (numberPerPack == null || bo.getNumberOfTime() < numberPerPack)) {
                                numberPerPack = bo.getNumberOfTime();
                            }
                        }
                    }
                }
                //tao so lan su dung dich vu
                if (numberPerPack != null && newMembership.getGroupPack() != null && !Constant.GROUP_PACK_TYPE.TYPE_HV_TIME.equals(newMembership.getGroupPack().getType())) {
                    newMembership.setAvailable(numberPerPack * newMembership.getNumberPack());
                    createTicket = true;
                }

            }

            //memberPayment
            MemberPayment memberPayment = curMember.getMemberPayment();
            memberPayment.setNumberPack(newMembership.getNumberPack());
            CatPromotion catPromotion = memberPayment.getCatPromotion();
            //km them thoi gian
            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType())
                    && newMembership.getEndDate() != null && catPromotion.getValue() != null) {
                Date date = newMembership.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, catPromotion.getValue().intValue());
                Date endate = calendar.getTime();
                newMembership.setEndDate(endate);
            } else if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                    && newMembership.getAvailable() != null) {
                newMembership.setAvailable(newMembership.getAvailable() + Math.round(catPromotion.getValue()));//them so luot km
            }
            // in hoa don
            onPrintBill();
            activeCustomerSchedule(newMembership);

            newMembership.setMemberId(curMember.getMemberId());
            newMembership.setStatus(1L);

            //hlv
            if (newMembership.getTrainer() != null) {
                newMembership.setTrainerId(newMembership.getTrainer().getEmployeeId());
            }

//            updateAccessCatMachine(curMember, newMembership, Constant.METHOD.INSERT);
            memberShipService.saveOrUpdate(newMembership);
            //tao ticket
            if (createTicket) {
                List<ServiceTicket> lstTicket = ServiceTicketServiceImpl.createListTicket(newMembership.getMembershipId(), Integer.valueOf(newMembership.getAvailable().toString()));
                ServiceTicketServiceImpl.getInstance().saveOrUpdate(lstTicket);
            }
            memberPayment.setMemberId(curMember.getMemberId());
            memberPayment.setMembershipId(newMembership.getMembershipId());
            if (catPromotion != null) {
                memberPayment.setCatPromotionId(catPromotion.getCatPromotionId());
            }
            if (getRequest().getSession().getAttribute("user") != null) {
                memberPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị am 
            if (memberPayment.getDebt() > 0) {
                memberPayment.setDebt(0l);
            }
            memberPayment.setType(Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI);
            memberPayment.setCreateTime(new Date());
            memberPaymentService.saveOrUpdate(memberPayment);
            //memberPromotion
            if (catPromotion != null && catPromotion.getCatPromotionId() != null) {

                MemberPromotion memberPromotion = new MemberPromotion();
                memberPromotion.setMemberId(curMember.getMemberId());
                memberPromotion.setCatPromotionId(curMember.getMemberPayment().getCatPromotion().getCatPromotionId());
                memberPromotion.setGroupPackId(newMembership.getGroupPackId());
                String desc = catPromotion.getTypeName() + ": " + catPromotion.getValue()
                        + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                        ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "VNĐ" : "");
                memberPromotion.setDescription(desc);
                memberPromotion.setValue(catPromotion.getValue());
                memberPromotion.setCreateDate(new Date());
                memberPromotion.setMembershipId(newMembership.getMembershipId());

                memberPromotionService.saveOrUpdate(memberPromotion);
            }
            if (!Constant.MEMBERSHIP_ACTION_TYPE.EXTEND.equals(membershipActionType)) {
                //them thong tin user using service
                CatGroupPack catGroupPack = newMembership.getGroupPack();
                List<MemberUsedService> lstMemberUsedServices = new ArrayList<MemberUsedService>();
//            List<CatService> lstCatServices = new ArrayList<>();
                try {
                    List<CatPack> lstCatPacks = catGroupPack.getPacks();
                    if (lstCatPacks != null) {
                        for (CatPack bo : lstCatPacks) {
                            if (bo != null && bo.getCatService() != null) {
                                CatService boService = bo.getCatService();

                                MemberUsedService mus = new MemberUsedService();
                                mus.setCreateTime(new Date());
                                mus.setMemberId(curMember.getMemberId());
                                mus.setServiceId(boService.getServiceId());
                                mus.setMembershipId(newMembership.getMembershipId());
                                mus.setStatus(1l);
                                mus.setStartDate(newMembership.getJoinDate());
                                mus.setEndDate(newMembership.getEndDate());
                                Long numberOfTime = null;
                                if (bo.getNumberOfTime() != null) {
                                    numberOfTime = bo.getNumberOfTime() * newMembership.getNumberPack();
                                }
                                mus.setAvailable(numberOfTime);
                                mus.setTotalNumber(numberOfTime);
                                if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                                        && mus.getAvailable() != null) {
                                    mus.setAvailable(mus.getAvailable() + Math.round(catPromotion.getValue()));
                                    mus.setTotalNumber(mus.getAvailable());
                                }
                                lstMemberUsedServices.add(mus);

                                if (bo.getNumberOfTime() != null && bo.getNumberOfTime() < numberPerPack) {
                                    numberPerPack = bo.getNumberOfTime();
                                }
                            }
                        }
                    }

                    //tao user using service
                    GenericDaoImplNewV2 musService = new GenericDaoImplNewV2<MemberUsedService, Long>() {
                    };
                    musService.saveOrUpdate(lstMemberUsedServices);
                    //log
                    LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, lstMemberUsedServices.toArray().toString(), this.getClass().getSimpleName(),
                            (new Exception("get Name method").getStackTrace()[0].getMethodName()));

//                                Map<String,Object> filterMus=new HashMap();
//                filterMus.put("memberId", newMembership.getMemberId());
//                filterMus.put("membershipId", newMembership.getMembershipId());
//                filterMus.put("status", 1l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //cap nhat lai thoi gian khi gia han
                List<MemberUsedService> lstMemberUsedServices = new ArrayList<MemberUsedService>();
                try {
                    Map<String, Object> filter = new HashMap();
                    filter.put("membershipId", curMember.getNewMembership().getMembershipId());
                    GenericDaoImplNewV2 musService = new GenericDaoImplNewV2<MemberUsedService, Long>() {
                    };

                    lstMemberUsedServices = musService.findList(filter);
                    if (lstMemberUsedServices != null && lstMemberUsedServices.size() > 0) {
                        for (MemberUsedService bo : lstMemberUsedServices) {
                            bo.setEndDate(curMember.getNewMembership().getEndDate());
                        }
                        musService.saveOrUpdate(lstMemberUsedServices);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            curMember = memberService.findById(curMember.getMemberId());
            //ghi log
            if (oldObjectMeberShipStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, memberPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('paymentDlg').hide();");
            //show thanh toan
            if (billImgPath != null) {
                RequestContext.getCurrentInstance().update(":cfImgMemberFormLocal");
                RequestContext.getCurrentInstance().execute("PF('cfImgMemberDlgLocal').show();");
            }
            //clear form
            preEditMember(curMember);
            preAddMembership();

        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void computingPrice() {
        try {
            membershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;
            curMember.setMemberPayment(new MemberPayment());
            curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());
            curMember.getMemberPayment().setPrice(curMember.getTotalPayment());
            oldObjectMeberShipStr = null;

            if (curMember.getNewMembership().getGroupPackId() == null) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("datatable.header.group.pack.name")));
                return;
            }
            if (curMember.getNewMembership().getJoinDate() == null) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("datatable.header.joinDate")));
                return;
            }
            membershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.NEW;
            CatGroupPack catGroupPack = catGroupPackService.findById(curMember.getNewMembership().getGroupPackId());
            curMember.getNewMembership().setGroupPack(catGroupPack);
            Long price = Math.round(catGroupPack.getPrice());
            if (curMember.getNewMembership().getNumberPack() > 1) {
                price *= curMember.getNewMembership().getNumberPack();
            }
            curMember.getMemberPayment().setPrice(price);
            RequestContext.getCurrentInstance().execute("PF('paymentDlg').show();");

            try {
                Map<String, Object> filterHasPro = new HashMap<>();
                filterHasPro.put("groupPackId", catGroupPack.getGroupPackId());
                List<PackHasPromotion> lstPackHasPromotions = new GenericDaoImplNewV2<PackHasPromotion, Long>() {
                }.findList(filterHasPro);
                List<CatPromotion> lstCatPromotionsForPack = new ArrayList<>();//lay khuyen mai danh cho goi
                if (lstPackHasPromotions != null && !lstPackHasPromotions.isEmpty()) {
                    for (PackHasPromotion bo : lstPackHasPromotions) {
                        if (bo.getCatPromotion() != null && Constant.CAT_PROMOTION.ENABLE.equals(bo.getCatPromotion().getStatus())) {
                            lstCatPromotionsForPack.add(bo.getCatPromotion());
                        }
                    }
                }
                //set lai danh sach theo so luong goi
                lstCatPromotions = new ArrayList<>();
                for (CatPromotion bo : lstCatPromotionsForPack) {
                    if (StringUtil.isNullOrEmpty(bo.getOperator()) || StringUtil.isNullOrEmpty(bo.getValueCompare())
                            || CommonUtil.compareValue(String.valueOf(curMember.getNewMembership().getNumberPack()), bo.getOperator(), bo.getValueCompare())) {
                        lstCatPromotions.add(bo);
                    };
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preAddUsePack() {
        try {
            computingPrice();
            curMember.getMemberPayment().setPaymentValue(0l);
            onChangeComputingPrice();
            curMember.getMemberPayment().setReason(((Map<String, String>) CommonUtil.convertListToMap((List) lstReason, "code", "name")).get(Constant.CAT_ITEM.REASON_DEBT.USING_SERVICE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangeComputingPrice() {
        try {
            CatGroupPack catGroupPack = curMember.getNewMembership().getGroupPack();
            Double price = catGroupPack.getPrice();
            if (Constant.MEMBERSHIP_ACTION_TYPE.TRANSFER.equals(membershipActionType)) {// bo sung fix gia cho chuyen nhuong
                price = Double.valueOf(DataConfig.getConfigByKey("PRICE_TRANSFER"));
            }

            if (curMember.getNewMembership().getNumberPack() > 1 && !Constant.MEMBERSHIP_ACTION_TYPE.TRANSFER.equals(membershipActionType)) {
                price *= curMember.getNewMembership().getNumberPack();
            }
            CatPromotion catPromotion = curMember.getMemberPayment().getCatPromotion();
            if (catPromotion != null && catPromotion.getValue() != null) {
                if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType())) {
                    price = price - catPromotion.getValue();
                } else if (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())) {
                    price = price - price * catPromotion.getValue() / 100;
                }
            }
            if (curMember.getMemberPayment().getIsVAT()) {
                price = price * 1.1;
            }
            curMember.getMemberPayment().setPrice(Math.round(price));

            if (curMember.getMemberPayment().getPaymentValue() != null) {
                curMember.getMemberPayment().setDebt(curMember.getMemberPayment().getPaymentValue() - curMember.getMemberPayment().getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Long computinDebt(Long memberId) {
        // lay tong cong no
        String sqlDebt = "select sum(debt) debt from member_payment where member_id=:memberId and ( membership_Id in (select a.membership_Id from membership a where a.member_id= :memberId"
                + " and a.status!=-1 "
                + ") )";
        Session session = null;
        Long debt = 0l;
        try {
            session = HibernateUtil.openSession();
            SQLQuery sQLQuery = session.createSQLQuery(sqlDebt);
            sQLQuery.setParameter("memberId", memberId);
            BigDecimal res = (BigDecimal) sQLQuery.list().get(0);
            if (res != null) {
                debt = ((BigDecimal) sQLQuery.list().get(0)).longValue();
//                curMember.setTotalPayment(debt);
            } else {
//                curMember.setTotalPayment(0l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return debt;

    }

    public void preAddLiquidate() {
        //set gia tri mac dinh
        curMember.setMemberPayment(new MemberPayment());
        curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());
        curMember.getMemberPayment().setPrice(curMember.getTotalPayment());

        //lay ds thong tin thanh toan cac goi dang no
        Map<String, Object> filterPayment = new HashMap<>();
        filterPayment.put("memberId", curMember.getMemberId());
        try {
            debtAll.setMembershipId(-1l);
            debtAll.setGroupPackName(MessageUtil.getResourceBundleMessage("totalDebt"));
            debtAll.setSumDept(curMember.getTotalPayment());
            curMember.getMemberPayment().setCurDebt(debtAll);

            lstMemberDebtPayments = VMemberDebtPaymentServiceImpl.getInstance().findList(filterPayment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        oldObjectMeberShipStr = null;
    }

    public void onChangeComputingLiquidate() {
        try {
            if (curMember.getMemberPayment().getPaymentValue() != null) {
                curMember.getMemberPayment().setDebt(curMember.getMemberPayment().getPaymentValue() + curMember.getMemberPayment().getPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liquidate() {
        try {
            // xu ly tao thong tin tra no cho tat ca cac khoan

            //memberPayment
            MemberPayment memberPayment = curMember.getMemberPayment();
            memberPayment.setMemberId(curMember.getMemberId());

            if (getRequest().getSession().getAttribute("user") != null) {
                memberPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị duong la phan thu tien cua khach hang
            if (memberPayment.getPaymentValue() <= -memberPayment.getPrice()) {
                memberPayment.setDebt(memberPayment.getPaymentValue());
            } else {
                memberPayment.setDebt(-memberPayment.getPrice());
            }
            //set lai gia tien bang 0 vi day la tra no
            memberPayment.setCreateTime(new Date());
            memberPayment.setType(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO);
            // xu ly tao thong tin tra no cho tat ca cac khoan
            List<MemberPayment> paymetPerPacks = new ArrayList<>();
            if (memberPayment.getCurDebt() == null || memberPayment.getCurDebt().getMembershipId() == null
                    || memberPayment.getCurDebt().getMembershipId().equals(-1L)) {

                if (lstMemberDebtPayments != null && lstMemberDebtPayments.size() > 0) {
                    Long paymentDebt = memberPayment.getDebt();
                    for (V_MemberDebtPayment bo : lstMemberDebtPayments) {
                        if (paymentDebt > 0 && bo.getMembershipId() != null) {
                            MemberPayment newPay = new MemberPayment();
                            newPay.setMemberId(memberPayment.getMemberId());
                            newPay.setEmployeeId(memberPayment.getEmployeeId());
                            newPay.setPaymentCode(memberPayment.getPaymentCode());
                            newPay.setReason(memberPayment.getReason());
                            newPay.setDescription(memberPayment.getDescription());
                            newPay.setCreateTime(memberPayment.getCreateTime());
                            newPay.setType(memberPayment.getType());

                            newPay.setMembershipId(bo.getMembershipId());
                            newPay.setPrice(0l);
                            newPay.setDebtTotal(-bo.getSumDept());

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
                memberPaymentService.saveOrUpdate(paymetPerPacks);
            } else {// truong hop chi lam 1 goi
                memberPayment.setMembershipId(memberPayment.getCurDebt().getMembershipId());
                memberPayment.setDebtTotal(-memberPayment.getPrice());
                paymetPerPacks.add(memberPayment);

                onPrintBillLiquidate(paymetPerPacks);
                memberPayment.setPrice(0l);//set lai de luu sau khi in file thanh toan
                memberPaymentService.saveOrUpdate(memberPayment);// luu theo tung goi dang no
            }

//            curMember = memberService.findById(curMember.getMemberId());
            //ghi log
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, memberPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('liquidateDlg').hide();");
            //show thanh toan
            if (billImgPath != null) {
                RequestContext.getCurrentInstance().update(":cfImgMemberFormLocal");
                RequestContext.getCurrentInstance().execute("PF('cfImgMemberDlgLocal').show();");
            }
            //clear form
            preAddMembership();

            preEditMember(curMember);
//            computinDebt();
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void addGroupHasMember() {
        if (curMember.getLstGroupHasMembers() == null) {
            curMember.setLstGroupHasMembers(new ArrayList<>());
        }
        GroupHasMember ghm = new GroupHasMember();
        ghm.setMember(memberAssign);
        ghm.setGroupMemberId(curMember.getMemberId());
        ghm.setMemberId(memberAssign.getMemberId());
        ghm.setStatus(1l);

        if (!curMember.getLstGroupHasMembers().contains(ghm)) {
            curMember.getLstGroupHasMembers().add(0, ghm);
            MessageUtil.setInfoMessageFromRes("msg.insert.succ");
        } else {
            MessageUtil.setErrorMessageFromRes("error.member.exist");
        }
        memberAssign = new Member();
    }

    public void onDeleteGroupMemberInList(GroupHasMember ob) {

        try {
            int size = curMember.getLstGroupHasMembers().size();

            int i = 0;
            for (int j = 0; j < size; j++) {
                if (curMember.getLstGroupHasMembers().get(i).equals(ob)) {
                    lstDelGroupHasMembers.add(curMember.getLstGroupHasMembers().get(i));
                    curMember.getLstGroupHasMembers().remove(curMember.getLstGroupHasMembers().get(i));
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

            if (curMember.getLstGroupHasMembers() == null || curMember.getLstGroupHasMembers().size() == 0) {
                curMember.setLstGroupHasMembers(new ArrayList<>());
            }

            if (curMember.getLstGroupHasMembers() != null && curMember.getLstGroupHasMembers().size() > 0) {
                for (GroupHasMember bo : curMember.getLstGroupHasMembers()) {
                    bo.setGroupMemberId(curMember.getMemberId());
                }
            }
            Map<String, Object> filterGroup = new HashMap<>();
            filterGroup.put("groupMemberId", curMember.getMemberId());
            List<GroupHasMember> lstCurrent = groupHasMemberService.findList(filterGroup);
            List<Long> lstMemId = new ArrayList<>();
            if (lstCurrent != null && lstCurrent.size() > 0) {
                for (GroupHasMember bo : lstCurrent) {
                    lstMemId.add(bo.getMemberId());
                }
            }
            // loai trung 
            Set<Long> setIdMember = new HashSet<>(lstMemId);
            for (GroupHasMember bo : curMember.getLstGroupHasMembers()) {
                if (!setIdMember.contains(bo.getMemberId())) {
                    setIdMember.add(bo.getMemberId());
                    groupHasMemberService.saveOrUpdate(bo);
                }
            }
            // luu thanh vien dai dien
            if (!setIdMember.contains(curMember.getDeputationMemberId())) {
                GroupHasMember ghm = new GroupHasMember();
                ghm.setGroupMemberId(curMember.getMemberId());
                ghm.setMemberId(curMember.getDeputationMemberId());
                ghm.setStatus(1l);
                groupHasMemberService.saveOrUpdate(ghm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.setErrorMessage("Lỗi lưu danh sách thành viên!");

        }

    }

    public void preExtendMembership(Membership membership) {
        try {
            membershipActionType = Constant.MEMBERSHIP_ACTION_TYPE.EXTEND;
            membership = memberShipService.findById(membership.getMembershipId());
            curMember.setNewMembership(membership);

            Calendar calendar = Calendar.getInstance();
            if (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime()) {
                calendar.setTime(membership.getEndDate());
            } else {
                calendar.setTime(new Date());
            }
            CatGroupPack groupPack = membership.getGroupPack();
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            membership.setEndDate(endate);

            CatGroupPack catGroupPack = catGroupPackService.findById(curMember.getNewMembership().getGroupPackId());
            curMember.getNewMembership().setGroupPack(catGroupPack);
            Long price = Math.round(catGroupPack.getPrice());
            curMember.getMemberPayment().setPrice(price);
            curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());

//            memberShipService.saveOrUpdate(membership);
            //log
//            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
//                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//
//            MessageUtil.setInfoMessageFromRes("info.extend.success");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.extend.unsuccess");
            e.printStackTrace();
        }
    }

    public void updateAccessCatMachine(Member member, Membership membershipDel, String method) {
        // khong thuc hien neu insert start time lon hon ngay hien tai hoac endDate nho hon ngay hien tai
        if (!Constant.METHOD.DELETE.equals(method)
                && ((membershipDel != null
                && membershipDel.getJoinDate() != null
                && DateTimeUtils.trunc(membershipDel.getJoinDate()).getTime() > (new Date()).getTime())
                || (membershipDel != null
                && membershipDel.getEndDate() != null
                && membershipDel.getEndDate().getTime() < DateTimeUtils.trunc((new Date())).getTime()))) {
            return;
        }
        Map<String, Object> filterPay = new HashMap<>();
        filterPay.put("memberId", member.getMemberId());
        List<Long> lstStatus = Arrays.asList(1l, 4l);
        filterPay.put("status", lstStatus);
        filterPay.put("joinDate-LE", new Date());
        filterPay.put("endDate-GE", new Date());
        List<Membership> lstMemberships;
        List<CatGroupPack> lstON = new ArrayList<>();
        List<CatGroupPack> lstOFF = new ArrayList<>();
        if (membershipDel != null && membershipDel.getGroupPack() != null) {
            lstOFF.add(membershipDel.getGroupPack());
        }

        //lst grouppack in group
        Map<String, Object> filterGms = new HashMap<>();
        filterGms.put("groupMemberId", lstGroupMemberId(member));
        filterGms.put("status", 1l);
        filterGms.put("endDate-GE", new Date());

        try {
            //lay ton tai trong group member
            List<GroupMembership> lstGroupMemberships = new GenericDaoImplNewV2<GroupMembership, Long>() {
            }.findList(filterGms);
            if (lstGroupMemberships != null && lstGroupMemberships.size() > 0) {
                for (GroupMembership bo : lstGroupMemberships) {
                    if (bo.getCatGroupPack() != null) {
                        lstON.add(bo.getCatGroupPack());
                    }
                }
            }

            lstMemberships = memberShipService.findList(filterPay);
            if (lstMemberships != null) {
                for (Membership bo : lstMemberships) {
                    if (bo.getGroupPack() != null) {
                        lstON.add(bo.getGroupPack());
                    }
                }
            }
            List<CatMachine> lstCatMachineOff = CatGroupPackController.getListMachineOffNotExistInON(lstON, lstOFF);
// cap nhat xoa khoi may quet the
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(method);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : lstCatMachineOff) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("MEM_" + member.getMemberId() + "|" + member.getMemberName() + "|" + member.getCardCode() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Long> lstGroupMemberId(Member member) {
        List<Long> lstGroupMemberId = new ArrayList<>();
        try {

            Map<String, Object> filters = new HashMap<>();
            filters.put("memberId", member.getMemberId());
            List<GroupHasMember> lst = groupHasMemberService.findList(filters);
            if (lst != null && lst.size() > 0) {
                for (GroupHasMember bo : lst) {
                    lstGroupMemberId.add(bo.getGroupMemberId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstGroupMemberId;
    }

    public static void updateAccessCatMachineNew(Member member, Membership membershipDel, String method, String cardCode) {
        // khong thuc hien neu insert start time lon hon ngay hien tai hoac endDate nho hon ngay hien tai
        /*if (!Constant.METHOD.DELETE.equals(method)
                && ((membershipDel != null
                && membershipDel.getJoinDate() != null
                && DateTimeUtils.trunc(membershipDel.getJoinDate()).getTime() > (new Date()).getTime())
                || (membershipDel != null
                && membershipDel.getEndDate() != null
                && membershipDel.getEndDate().getTime() < DateTimeUtils.trunc((new Date())).getTime()))) {
            return;
        }
        Map<String, Object> filterPay = new HashMap<>();
        filterPay.put("memberId", member.getMemberId());
        List<Long> lstStatus = Arrays.asList(1l, 4l);
        filterPay.put("status", lstStatus);
        filterPay.put("joinDate-LE", new Date());
        filterPay.put("endDate-GE", new Date());
        List<Membership> lstMemberships;
         */
        List<CatGroupPack> lstON = new ArrayList<>();
        List<CatGroupPack> lstOFF = new ArrayList<>();
        if (membershipDel != null && membershipDel.getGroupPack() != null) {
            lstOFF.add(membershipDel.getGroupPack());
        }

        //lst grouppack in group
        /*
        Map<String, Object> filterGms = new HashMap<>();
        filterGms.put("groupMemberId", lstGroupMemberId(member));
        filterGms.put("status", 1l);
        filterGms.put("endDate-GE", new Date());
         */
        try {
            //lay ton tai trong group member
            /*List<GroupMembership> lstGroupMemberships = new GenericDaoImplNewV2<GroupMembership, Long>() {
            }.findList(filterGms);
            if (lstGroupMemberships != null && lstGroupMemberships.size() > 0) {
                for (GroupMembership bo : lstGroupMemberships) {
                    if (bo.getCatGroupPack() != null) {
                        lstON.add(bo.getCatGroupPack());
                    }
                }
            }

            lstMemberships = memberShipService.findList(filterPay);
            if (lstMemberships != null) {
                for (Membership bo : lstMemberships) {
                    if (bo.getGroupPack() != null) {
                        lstON.add(bo.getGroupPack());
                    }
                }
            }*/
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
                CfgWsTimekeeperServiceImpl.getInstance().save(cfg);
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

    public void updateAccessCatMachineOFFAll(Member member, String cardCode) {
        // khong thuc hien neu insert start time lon hon ngay hien tai hoac endDate nho hon ngay hien tai
        /*if (!Constant.METHOD.DELETE.equals(method)
                && ((membershipDel != null
                && membershipDel.getJoinDate() != null
                && DateTimeUtils.trunc(membershipDel.getJoinDate()).getTime() > (new Date()).getTime())
                || (membershipDel != null
                && membershipDel.getEndDate() != null
                && membershipDel.getEndDate().getTime() < DateTimeUtils.trunc((new Date())).getTime()))) {
            return;
        }
         */
        Map<String, Object> filterPay = new HashMap<>();
        filterPay.put("memberId", member.getMemberId());
        List<Long> lstStatus = Arrays.asList(1l, 4l);
        filterPay.put("status", lstStatus);
        filterPay.put("joinDate-LE", new Date());
        filterPay.put("endDate-GE", new Date());
        List<Membership> lstMemberships;
        List<CatGroupPack> lstON = new ArrayList<>();
//        List<CatGroupPack> lstOFF = new ArrayList<>();
//        if (membershipDel != null && membershipDel.getGroupPack() != null) {
//            lstOFF.add(membershipDel.getGroupPack());
//        }

        //lst grouppack in group
        Map<String, Object> filterGms = new HashMap<>();
        filterGms.put("groupMemberId", lstGroupMemberId(member));
        filterGms.put("status", 1l);
        filterGms.put("endDate-GE", new Date());

        try {
            //lay ton tai trong group member
            List<GroupMembership> lstGroupMemberships = new GenericDaoImplNewV2<GroupMembership, Long>() {
            }.findList(filterGms);
            if (lstGroupMemberships != null && lstGroupMemberships.size() > 0) {
                for (GroupMembership bo : lstGroupMemberships) {
                    if (bo.getCatGroupPack() != null) {
                        lstON.add(bo.getCatGroupPack());
                    }
                }
            }

            lstMemberships = memberShipService.findList(filterPay);
            if (lstMemberships != null) {
                for (Membership bo : lstMemberships) {
                    if (bo.getGroupPack() != null) {
                        lstON.add(bo.getGroupPack());
                    }
                }
            }
            List<CatMachine> lstCatMachineOff = CatGroupPackController.getListMachineOffNotExistInON(new ArrayList<>(), lstON);
            List<String> lstIp = new ArrayList<>();

// cap nhat xoa khoi may quet the
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(Constant.METHOD.DELETE);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : lstCatMachineOff) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("MEM_" + member.getMemberId() + "|" + member.getMemberName() + "|" + cardCode + "|1|" + catMachine.getIp());
                CfgWsTimekeeperServiceImpl.getInstance().save(cfg);
                if (!lstIp.contains(catMachine.getIp())) {
                    lstIp.add(catMachine.getIp());
                }
            }

            //goi ws online
//            if (Constant.METHOD.INSERT.equals(method)) {
//                CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.ACTIVE);
//            } else {
            CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_CARD);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void preCheckin(Membership membership) {
        customerCheckin = new CustomerCheckin();
        customerCheckin.setMembershipId(membership.getMembershipId());
        if ((Constant.MEMBERSHIP_STATUS.ACTIVE.equals(membership.getStatus())
                || Constant.MEMBERSHIP_STATUS.RECEIVE.equals(membership.getStatus()))
                && (membership.getJoinDate() == null || membership.getJoinDate().getTime() <= (new Date()).getTime())
                && (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime())) {
            RequestContext.getCurrentInstance().execute("PF('checkinDlg').show();");
            curMember.setNewMembership(membership);
        } else {
            MessageUtil.setErrorMessage("Gói phải ở trạng thái đang hoạt động hoặc dược chuyển nhượng và khoảng thời gian hoạt động là hợp lệ!");
        }

    }

    public void checkin() {
        try {
            customerCheckin.setCustomerId(curMember.getMemberId());
            customerCheckin.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
            customerCheckin.setType(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
            customerCheckin.setCheckTime(new Date());
            customerCheckin.setTypeAccess(Constant.WS_C_METHOD.TYPE_CARD);
            //filter ds service su dung cho goi
            Map<String, Object> filter = new HashMap();
            filter.put("memberId", curMember.getMemberId());
            filter.put("membershipId", customerCheckin.getMembershipId());
            filter.put("status", Constant.MEMBER_USED_SERVICE.VALID);
            filter.put("startDate-LE", new Date());
            filter.put("endDate-GE", DateTimeUtils.trunc(new Date()));
            List<MemberUsedService> lst = memberUsedServiceService.findList(filter);
            if (lst != null && lst.size() > 0
                    || (curMember.getNewMembership().getAvailable() == null || curMember.getNewMembership().getAvailable() > 0)) {
//            if (lst != null && lst.size() > 0) {
                if (lst != null && lst.size() > 0) {
                    for (MemberUsedService bo : lst) {
                        if (bo.getAvailable() != null && bo.getAvailable() > 0) {
                            bo.setAvailable(bo.getAvailable() - 1);
                        } else if (bo.getTotalNumber() != null && bo.getTotalNumber() > 0) {
                            MessageUtil.setErrorMessage("Dịch vụ " + bo.getCatService().getServiceName() + " của gói đã hết lượt sử dụng");
                            return;
                        }
                        //luon ghi lai so luot su dung
                        if (bo.getUsedNumber() == null) {
                            bo.setUsedNumber(0l);
                        }
                        bo.setUsedNumber(bo.getUsedNumber() + 1);
                    }
                }
                // cap nhat thong tin chung cho goi
                if (curMember.getNewMembership().getAvailable() != null && curMember.getNewMembership().getAvailable() > 0) {
                    curMember.getNewMembership().setAvailable(curMember.getNewMembership().getAvailable() - 1);
                } else if (curMember.getNewMembership().getAvailable() != null && curMember.getNewMembership().getAvailable().equals(0l)) {
                    MessageUtil.setErrorMessage("Gói đã hết lượt sử dụng");
                    return;
                }
                if (curMember.getNewMembership().getUsedNumber() == null) {
                    curMember.getNewMembership().setUsedNumber(1l);
                } else {
                    curMember.getNewMembership().setUsedNumber(curMember.getNewMembership().getUsedNumber() + 1l);
                }
            } else {

                MessageUtil.setErrorMessage("Không có dịch vụ còn hiệu lực để sử dụng");
                return;
            }
            // KIEM TRA NEU THE SU DUNG LAN DAU TRONG NGAY THI OFF TAT CAC O CAC MAY
            MemberController.initCheckOutAll(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER, curMember.getMemberId(), customerCheckin.getCardCode());
            //cap nhat quyen vao may quet

            updateAccessCatMachineNew(curMember, curMember.getNewMembership(), Constant.METHOD.INSERT, customerCheckin.getCardCode());
            memberUsedServiceService.saveOrUpdate(lst);
            memberShipService.saveOrUpdate(curMember.getMemberships());

            customerCheckinService.saveOrUpdate(customerCheckin);
            MessageUtil.setInfoMessageFromRes("info.checkin.success");
            RequestContext.getCurrentInstance().execute("PF('checkinDlg').hide();");

            // show result
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {

                showResultAccessStatus(customerCheckin.getCardCode());
                RequestContext.getCurrentInstance().execute("PF('checkinResultDlg').show();PF('tableMemberCustomerAccessStatusWidget').filter()");
                RequestContext.getCurrentInstance().update("@widgetVar('checkinResultDlg')");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    public void preCheckout() {
        customerCheckin = new CustomerCheckin();

        Map<String, Object> filter = new HashMap<>();
        filter.put("customerId", curMember.getMemberId());
        filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
        filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
        Map<String, String> oder = new LinkedHashMap<>();
        oder.put("cardCode", Constant.ORDER.ASC);
        oder.put("groupMemberName", Constant.ORDER.ASC);
        try {
            lazyDataVCusCheckin = new LazyDataModelBase<>(vCustomerCheckinService, filter, oder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkout() {
        try {
            //khi thuc hien checkout thi checkout toan bo cac ban ghi check in theo the deo cua khach hang cua khach hang
            Set<Long> setMembershipId = new HashSet();
            Map<String, Object> filter = new HashMap<>();
            filter.put("cardCode", customerCheckin.getCardCode());
            filter.put("customerId", curMember.getMemberId());
            filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
            filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
            filter.put("typeAccess", Constant.WS_C_METHOD.TYPE_CARD);
            List<CustomerCheckin> lstCus = customerCheckinService.findList(filter);
            if (lstCus != null && !lstCus.isEmpty()) {
                for (CustomerCheckin bo : lstCus) {
                    bo.setStatus(Constant.CUSTOMER_CHECKIN.CHECKOUT);
                    bo.setCheckoutTime(new Date());
                    if (bo.getMembershipId() != null) {
                        setMembershipId.add(bo.getMembershipId());
                    }
                }
            } else {
                MessageUtil.setErrorMessage("Không tồn tại thẻ đang sử dụng cho khách hàng!");
                return;
            }
            customerCheckinService.saveOrUpdate(lstCus);

            updateAccessCatMachineOFFAll(curMember, customerCheckin.getCardCode());
            MessageUtil.setInfoMessageFromRes("info.checkout.success");
            RequestContext.getCurrentInstance().execute("PF('checkoutDlg').hide();");
            //cap nhatlai lich trinh ve hoan thanh
            if (setMembershipId.size() > 0) {
                List<Long> lstMembershipId = new ArrayList<>(setMembershipId);
                Map<String, Object> filterMembership = new HashMap();
                filterMembership.put("membershipId", lstMembershipId);
                List<Membership> lstMs = memberShipService.findList(filterMembership);
                Set<Long> setCustomerScheduleId = new HashSet<>();
                if (lstMs != null && lstMs.size() > 0) {
                    for (Membership m : lstMs) {
                        if (m.getCustomerScheduleId() != null) {
                            setCustomerScheduleId.add(m.getCustomerScheduleId());
                        }
                    }
                }
                for (Long id : setCustomerScheduleId) {
                    completedCustomerSchedule(id);//cap nhat lai lich trinh
                }
            }

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    //
    public void showResultAccessStatus(Object cardCode) {

        customerAccessStatusService = new GenericDaoImplNewV2<V_CustomerAccessStatus, Long>() {
        };
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
//            order.put("name", "ASC");
//            order.put("cardCode", "ASC");
//            order.put("groupPackName", "ASC");
//            order.put("roomName", "ASC");
//            order.put("ip", "ASC");
            Map<String, Object> filter = new HashMap<>();
            if (cardCode instanceof String) {
                filter.put("cardCode-EXAC_IGNORE_CASE", cardCode);
            } else if (cardCode instanceof List) {
                filter.put("cardCode", cardCode);
            }
            lazyDataCustomerAccessModel = new LazyDataModelBase<V_CustomerAccessStatus, Long>(customerAccessStatusService, filter, order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preActiveMembership(Membership bo) {
        curMember.setNewMembership(bo);

        //set gia tri mac dinh
        curMember.setMemberPayment(new MemberPayment());
        curMember.setMemberPromotion(new MemberPromotion());
        curMember.getMemberPayment().createPaymentCode(curMember.getBranchId());

        oldObjectMeberShipStr = null;

        try {
            Long selectId = bo.getGroupPackId();
            Date date = new Date();
            curMember.getNewMembership().setJoinDate(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CatGroupPack groupPack = catGroupPackService.findById(selectId);
            calendar.add(Calendar.DAY_OF_YEAR, groupPack.getAmong().intValue());
            Date endate = calendar.getTime();
            curMember.getNewMembership().setEndDate(endate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomerSchedulePack(Membership clientship) {
        try {
            if (Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE.equals(clientship.getStatus())) {
                GenericDaoImplNewV2 cspService = new GenericDaoImplNewV2<CustomerSchedulePack, Long>() {
                };
                Map<String, Object> filter = new HashMap<>();
                filter.put("customerScheduleId", clientship.getCustomerScheduleId());
                filter.put("groupPackId", clientship.getGroupPackId());
                List<CustomerSchedulePack> lst = cspService.findList(filter);
                cspService.delete(lst);
                LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, clientship.toString(), null, this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            }
//            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (Exception e) {
//            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void activeCustomerSchedule(Membership bo) throws AppException {
        if (Constant.MEMBERSHIP_STATUS.STATUS_SCHEDULE.equals(bo.getStatus())) {
//            curMember.setStatus(Constant.MEMBER_STATUS.ACTIVE);
//            memberService.saveOrUpdate(curMember);
            if (bo.getCustomerScheduleId() != null) {
                GenericDaoImplNewV2<CustomerSchedule, Long> csService = new GenericDaoImplNewV2<CustomerSchedule, Long>() {
                };
                CustomerSchedule c = csService.findById(bo.getCustomerScheduleId());
                c.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_ACTIVE);
                csService.saveOrUpdate(c);
            }
        }
    }

    public void completedCustomerSchedule(Long customerScheduleId) {
        try {
            if (customerScheduleId != null) {
                GenericDaoImplNewV2<CustomerSchedule, Long> csService = new GenericDaoImplNewV2<CustomerSchedule, Long>() {
                };
                CustomerSchedule c = csService.findById(customerScheduleId);
                c.setStatus(Constant.CUSTOMER_SCHEDULE.STATUS_COMPLETED);
                csService.saveOrUpdate(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initCheckOutAll(Long type, Long customerId, String cardCode) {

        boolean isActive = false;
        Map<String, Object> filter = new HashMap<>();
        filter.put("customerId", customerId);
        filter.put("cardCode", cardCode);
        filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
        filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
        try {
            long count = CustomerCheckinServiceImpl.getInstance().count2(filter);
            if (count > 0) {
                isActive = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // da active thi bo qua
        if (isActive) {
            return;
        }
        // su dung bien toan cuc de lan thuc hien kich hoat tiep theo ko phai goi lai
        try {
            List<CatMachine> lst = CatMachineServiceImpl.getInstance().findList();
            List<String> lstIp = new ArrayList<>();
            for (CatMachine catMachine : lst) {
                if (!lstIp.contains(catMachine.getIp())) {
                    lstIp.add(catMachine.getIp());
                }
            }
            CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_CARD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }

    public String getOldObjectMeberShipStr() {
        return oldObjectMeberShipStr;
    }

    public void setOldObjectMeberShipStr(String oldObjectMeberShipStr) {
        this.oldObjectMeberShipStr = oldObjectMeberShipStr;
    }

    public String getOldObjectMemberHealthStr() {
        return oldObjectMemberHealthStr;
    }

    public void setOldObjectMemberHealthStr(String oldObjectMemberHealthStr) {
        this.oldObjectMemberHealthStr = oldObjectMemberHealthStr;
    }

    public GenericDaoImplNewV2<V_CustomerAccessStatus, Long> getCustomerAccessStatusService() {
        return customerAccessStatusService;
    }

    public void setCustomerAccessStatusService(GenericDaoImplNewV2<V_CustomerAccessStatus, Long> customerAccessStatusService) {
        this.customerAccessStatusService = customerAccessStatusService;
    }

    public LazyDataModel<V_CustomerAccessStatus> getLazyDataCustomerAccessModel() {
        return lazyDataCustomerAccessModel;
    }

    public void setLazyDataCustomerAccessModel(LazyDataModel<V_CustomerAccessStatus> lazyDataCustomerAccessModel) {
        this.lazyDataCustomerAccessModel = lazyDataCustomerAccessModel;
    }

    public V_CustomerAccessStatus getCurrVcustomerAccessStatus() {
        return currVcustomerAccessStatus;
    }

//    vietnv add end
    public void setCurrVcustomerAccessStatus(V_CustomerAccessStatus currVcustomerAccessStatus) {
        this.currVcustomerAccessStatus = currVcustomerAccessStatus;
    }

    public boolean getIsAddUsePack() {
        return isAddUsePack;
    }

    public void setIsAddUsePack(boolean isAddUsePack) {
        this.isAddUsePack = isAddUsePack;
    }

    public VCustomerCheckinServiceImpl getvCustomerCheckinService() {
        return vCustomerCheckinService;
    }

    public void setvCustomerCheckinService(VCustomerCheckinServiceImpl vCustomerCheckinService) {
        this.vCustomerCheckinService = vCustomerCheckinService;
    }

    public LazyDataModel<V_CustomerCheckin> getLazyDataVCusCheckin() {
        return lazyDataVCusCheckin;
    }

    public void setLazyDataVCusCheckin(LazyDataModel<V_CustomerCheckin> lazyDataVCusCheckin) {
        this.lazyDataVCusCheckin = lazyDataVCusCheckin;
    }

    public GenericDaoImplNewV2<MemberUsedService, Long> getMemberUsedServiceService() {
        return memberUsedServiceService;
    }

    public void setMemberUsedServiceService(GenericDaoImplNewV2<MemberUsedService, Long> memberUsedServiceService) {
        this.memberUsedServiceService = memberUsedServiceService;
    }

    public GenericDaoServiceNewV2<CustomerCheckin, Long> getCustomerCheckinService() {
        return customerCheckinService;
    }

    public void setCustomerCheckinService(GenericDaoServiceNewV2<CustomerCheckin, Long> customerCheckinService) {
        this.customerCheckinService = customerCheckinService;
    }

    public Long getMembershipActionType() {
        return membershipActionType;
    }

    public void setMembershipActionType(Long membershipActionType) {
        this.membershipActionType = membershipActionType;
    }

    public CustomerCheckin getCustomerCheckin() {
        return customerCheckin;
    }

    public void setCustomerCheckin(CustomerCheckin customerCheckin) {
        this.customerCheckin = customerCheckin;
    }

    public GenericDaoServiceNewV2<Membership, Long> getMemberShipService() {
        return memberShipService;
    }

    public void setMemberShipService(GenericDaoServiceNewV2<Membership, Long> memberShipService) {
        this.memberShipService = memberShipService;
    }

    public GenericDaoServiceNewV2<MemberHealth, Long> getMemberHealthService() {
        return memberHealthService;
    }

    public void setMemberHealthService(GenericDaoServiceNewV2<MemberHealth, Long> memberHealthService) {
        this.memberHealthService = memberHealthService;
    }

    public GenericDaoServiceNewV2<CatGroupPack, Long> getCatGroupPackService() {
        return catGroupPackService;
    }

    public void setCatGroupPackService(GenericDaoServiceNewV2<CatGroupPack, Long> catGroupPackService) {
        this.catGroupPackService = catGroupPackService;
    }

    public Map<String, CatItemBO> getMapHealthLevel() {
        return mapHealthLevel;
    }

    public void setMapHealthLevel(Map<String, CatItemBO> mapHealthLevel) {
        this.mapHealthLevel = mapHealthLevel;
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

    public GenericDaoServiceNewV2<MemberPayment, Long> getMemberPaymentService() {
        return memberPaymentService;
    }

    public void setMemberPaymentService(GenericDaoServiceNewV2<MemberPayment, Long> memberPaymentService) {
        this.memberPaymentService = memberPaymentService;
    }

    public GenericDaoServiceNewV2<MemberPromotion, Long> getMemberPromotionService() {
        return memberPromotionService;
    }

    public void setMemberPromotionService(GenericDaoServiceNewV2<MemberPromotion, Long> memberPromotionService) {
        this.memberPromotionService = memberPromotionService;
    }

    public Long getTypeMember() {
        return typeMember;
    }

    public void setTypeMember(Long typeMember) {
        this.typeMember = typeMember;
    }

    public GenericDaoServiceNewV2<GroupHasMember, Long> getGroupHasMemberService() {
        return groupHasMemberService;
    }

    public void setGroupHasMemberService(GenericDaoServiceNewV2<GroupHasMember, Long> groupHasMemberService) {
        this.groupHasMemberService = groupHasMemberService;
    }

    public GenericDaoServiceNewV2<V_MemberUsedServiceFull, Long> getvMemberUserServiceFullService() {
        return vMemberUserServiceFullService;
    }

    public void setvMemberUserServiceFullService(GenericDaoServiceNewV2<V_MemberUsedServiceFull, Long> vMemberUserServiceFullService) {
        this.vMemberUserServiceFullService = vMemberUserServiceFullService;
    }

    public List<GroupHasMember> getLstDelGroupHasMembers() {
        return lstDelGroupHasMembers;
    }

    public void setLstDelGroupHasMembers(List<GroupHasMember> lstDelGroupHasMembers) {
        this.lstDelGroupHasMembers = lstDelGroupHasMembers;
    }

    public LazyDataModel<V_MemberUsedServiceFull> getLazyVMemberUsedServiceFull() {
        return lazyVMemberUsedServiceFull;
    }

    public void setLazyVMemberUsedServiceFull(LazyDataModel<V_MemberUsedServiceFull> lazyVMemberUsedServiceFull) {
        this.lazyVMemberUsedServiceFull = lazyVMemberUsedServiceFull;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public boolean getIS_LOCAL() {
        return IS_LOCAL;
    }

    public void setIS_LOCAL(boolean IS_LOCAL) {
        this.IS_LOCAL = IS_LOCAL;
    }

//</editor-fold>
    /**
     * onPrint: in anh
     */
    public void onPrintBill() {
        PrintPaymentForm printForm = getObjectBillImg();
//        billImgPath = ImageStreamerBill.generateBillImg(printForm);
        billImgPath = PdfUtil.generateBillPdf(printForm);
    }

    public PrintPaymentForm getObjectBillImg() {
        PrintPaymentForm paymentForm = new PrintPaymentForm();
        try {

            Membership newMembership = curMember.getNewMembership();

            MemberPayment memberPayment = curMember.getMemberPayment();
            CatPromotion catPromotion = memberPayment.getCatPromotion();

            String customerName = "";
            String customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type1");
            paymentForm.setCustomerType(Constant.PAYMENT_TYPE.MEMBER);
            Long numMember = 1L;

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
            if (curMember.getMemberName() != null) {
                customerName = curMember.getMemberName();
            }

            // custPays payment
            Long paymentValue = memberPayment.getPaymentValue();
            //cong no chi co gia trị am 
            Long debt = memberPayment.getDebt();
            if (memberPayment.getDebt() > 0) {
                paymentValue = memberPayment.getPrice();
                debt = 0l;
            }
            //lay gia trị duong
            if (debt < 0) {
                debt = -debt;
            }
            //
            if (curMember.getTotalPayment() != null) {
                oldDept = Math.abs(curMember.getTotalPayment());
                totalDept = oldDept;
            }
            if (debt != null) {
                totalDept += debt;
            }
            paymentForm.setPaymentTime(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY));
            // Bill code
            paymentForm.setPaymentCode(memberPayment.getPaymentCode());
            // customer
            paymentForm.setCustomerName(customerName);
            paymentForm.setCustomerTypeName(customerTypeName);
            paymentForm.setNumCustomer(numMember != null ? numMember : 1l);
            paymentForm.setEmployeeName(employeeName);
            paymentForm.setBarCode("");

            List<MemberPayment> lstMemberPayments = new ArrayList<>();
            lstMemberPayments.add(memberPayment);
            List<PaymentPackObj> lstPaymentPackObjs = new ArrayList<>();

            if (lstMemberPayments != null) {
                for (MemberPayment bo : lstMemberPayments) {
                    PaymentPackObj payPack = new PaymentPackObj();
                    // item
//                    String itemStr = iPack.get("item");
                    // num
                    Long numPack = 1L;
                    if (bo.getNumberPack() != null) {
                        numPack = bo.getNumberPack();
                    }
                    // price dong gia
                    CatGroupPack catGroupPack = newMembership.getGroupPack();
                    Long price = null;
                    String groupPackName = "";
                    if (catGroupPack != null && catGroupPack.getPrice() != null) {
                        price = catGroupPack.getPrice().longValue();
                        groupPackName = catGroupPack.getGroupPackName();

                    }
                    // tinh tong vat
                    if (bo.getIsVAT() && bo.getPrice() != null) {
                        vatPrice += bo.getPrice() / 11;
                        discount += price * numPack - bo.getPrice() * 10 / 11;
                    } else {
                        discount += price * numPack - bo.getPrice();
                    }
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
            paymentForm.setTotal(memberPayment.getPrice());
            // Total khach phai tra
            paymentForm.setMustPay(memberPayment.getPrice());
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

    public String getFingerprintPathNew() {
        return fingerprintPathNew;
    }

    public void setFingerprintPathNew(String fingerprintPathNew) {
        this.fingerprintPathNew = fingerprintPathNew;
    }

    public boolean isIsChangeFingerprint() {
        return isChangeFingerprint;
    }

    public void setIsChangeFingerprint(boolean isChangeFingerprint) {
        this.isChangeFingerprint = isChangeFingerprint;
    }

    public void onPrintBillLiquidate(List<MemberPayment> paymetPerPacks) {
        PrintPaymentForm printForm = getObjectBillImg(paymetPerPacks);
//        billImgPath = ImageStreamerBill.generateBillImg(printForm);
        billImgPath = PdfUtil.generateBillPdf(printForm);
    }

    public PrintPaymentForm getObjectBillImg(List<MemberPayment> paymetPerPacks) {
        PrintPaymentForm paymentForm = new PrintPaymentForm();
        try {

//            Membership newMembership = curMember.getNewMembership();
            MemberPayment memberPayment = curMember.getMemberPayment();
//            CatPromotion catPromotion = memberPayment.getCatPromotion();

            String customerName = "";
            String customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type1");
            paymentForm.setCustomerType(Constant.PAYMENT_TYPE.MEMBER);
            Long numMember = 1L;

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
            if (curMember.getMemberName() != null) {
                customerName = curMember.getMemberName();
            }

            // custPays payment
            Long paymentValue = memberPayment.getDebt();
            //cong no chi co gia trị am 
            Long debt = 0l;// tra no nen ko co them cong no
//            oldDept = -memberPayment.getPrice();
            oldDept = -curMember.getTotalPayment();
            totalDept = oldDept - paymentValue;
            if (totalDept < 0) {
                totalDept = -totalDept;
            }

            paymentForm.setPaymentTime(DateTimeUtils.format(new Date(), Constant.PATTERN.DATETIME_COMMON_YY));
            // Bill code
            paymentForm.setPaymentCode(memberPayment.getPaymentCode());
            // customer
            paymentForm.setCustomerName(customerName);
            paymentForm.setCustomerTypeName(customerTypeName);
            paymentForm.setNumCustomer(numMember != null ? numMember : 1l);
            paymentForm.setEmployeeName(employeeName);
            paymentForm.setBarCode("");

//            List<MemberPayment> lstMemberPayments = new ArrayList<>();
//            lstMemberPayments.add(memberPayment);
            List<PaymentPackObj> lstPaymentPackObjs = new ArrayList<>();

            if (paymetPerPacks != null) {
                for (MemberPayment bo : paymetPerPacks) {
                    PaymentPackObj payPack = new PaymentPackObj();
                    // item
//                    String itemStr = iPack.get("item");
                    // price dong gia
                    Membership membership = new Membership();
                    try {

                        membership = memberShipService.findById(bo.getMembershipId());
                    } catch (Exception e) {
                    }
                    CatGroupPack catGroupPack = membership.getGroupPack();
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
            paymentForm.setTotal(memberPayment.getPrice());
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
        if (curMember.getMemberPayment().getCurDebt() != null) {
            curMember.getMemberPayment().setPrice(curMember.getMemberPayment().getCurDebt().getSumDept());
            onChangeComputingLiquidate();
        }
    }

    public void synCallbackFingerprint() {
        // isChangeFingerprint = true;
//        synFingerprintPath();

        if (IS_LOCAL) {
            if (isEdit && curMember.getMemberId() != null) {
                String memberName = curMember.getMemberName() != null ? curMember.getMemberName() : "";
                GymWsMCCImpl.getInstance().registerUserFingerprint(curMember.getMemberId().toString(), memberName);
            }
        }

    }

    public void synFingerprintPath() {
        try {
            //mac dinh them moi luon lang nghe lay dau van tay
            if (!isChangeFingerprint && isEdit || !IS_LOCAL) {
                //goi giai phong
                return;
            }
//            ExternalContext externalContext = FacesContext.getCurrentInstance()
//                    .getExternalContext();
////local
//            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
//                    .getExternalContext().getContext();
            String fileName = "fingerprint_" + DateTimeUtils.format(new Date(), "yyMMddHHmmss") + ".bmp";
            FileInputStream input = null;
            String realpath = Util.getRealPath(Constant.FINGERPRINT_FOLDER_TEMP + "/" + "fingerprint_" + DateTimeUtils.format(new Date(), "yyMMddHHmm") + ".bmp");
//             input= new FileInputStream(new File(realpath));
            File newfile = new File(realpath);
            // neu noi dung 2 file giong nhau thi ko can load lai
            if (!newfile.exists()) {
                return;
            }
            if (fingerprintFile != null
                    && FileUtils.contentEquals(fingerprintFile, newfile)) {
                return;
            }

//            fingerprintFile = newfile;// thay bang copy file
            fingerprintFile = new File(Util.getRealPath(Constant.FINGERPRINT_FOLDER_TEMP), fileName);
            FileUtils.copyFile(newfile, fingerprintFile);
//            fingerprintFile = new File(Util.getRealPath(Constant.FINGERPRINT_FOLDER_TEMP), fileName);

            if (fingerprintFile != null && fingerprintFile.exists()) {
//                OutputStream outputStream = new FileOutputStream(fingerprintFile);
//                outputStream.write(input.getContents());
//                outputStream.flush();
//                outputStream.close();
                fingerprintPathNew = fingerprintFile.getPath();
                curMember.setFingerprintPath(fileName);
            } else {
                fingerprintPathNew = "";
//                curMember.setFingerprintPath("Không có file anh");

            }

        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }
        RequestContext.getCurrentInstance().update("insertForm:panelFingerpirnt");

    }

    public void callFingerprintPerUpdate(Member m, boolean isEdit) {
        if (IS_LOCAL) {
            GymFingerprintImpl.getInstance().listenerFinger();
        }

        if (isEdit) {
            isChangeFingerprint = false;
            if (StringUtil.isNotNullAndNullStr(m.getFingerprintPath())) {
                String realpath = Util.getRealPath(Constant.FINGERPRINT_FOLDER + "/" + m.getFingerprintPath());
                fingerprintFile = new File(realpath);
                if (fingerprintFile != null && fingerprintFile.exists()) {
                    fingerprintPathNew = realpath;
                } else {
                    fingerprintPathNew = "";
                }
            } else {
                fingerprintPathNew = "";
                fingerprintFile = null;
//                isChangeFingerprint = true;
            }
        } else {
            fingerprintPathNew = "";
            fingerprintFile = null;
            isChangeFingerprint = true;
        }

    }

    public void callFingerprintPerUpdateNew(Member m, boolean isEdit) {
        if (IS_LOCAL) {
            if (isEdit && m.getMemberId() != null && StringUtil.isNullOrEmpty(m.getFingerprintPath())) {
                DoorAccessStatus fingerprint = GymWsMCCImpl.getInstance().getInforUserFingerprint(m.getMemberId().toString());
                if (StringUtil.isNotNullAndNullStr(fingerprint.getData())) {
                    m.setFingerprintPath(fingerprint.getData());
                }
            }

        }

        if (isEdit) {
            isChangeFingerprint = false;

        } else {
            fingerprintPathNew = "";
            fingerprintFile = null;
            isChangeFingerprint = true;
        }

    }

    public void saveNewFingerprint() throws IOException {
        if (fingerprintFile != null && fingerprintFile.exists() && StringUtil.isNotNullAndNullStr(fingerprintPathNew)) {
            File tmpTemplateFile = new File(Util.getRealPath(Constant.FINGERPRINT_FOLDER), curMember.getFingerprintPath());
            if (!fingerprintFile.getPath().equals(tmpTemplateFile.getPath())) {
                FileUtils.copyFile(fingerprintFile, tmpTemplateFile);
            }
        }

        if (IS_LOCAL) {
            GymFingerprintImpl.getInstance().closeCurrentSensor();
        }

    }

    // quyet van tay
    public void preCheckinFingerprint(Membership membership) {
        customerCheckin = new CustomerCheckin();
        customerCheckin.setMembershipId(membership.getMembershipId());
        if ((Constant.MEMBERSHIP_STATUS.ACTIVE.equals(membership.getStatus())
                || Constant.MEMBERSHIP_STATUS.RECEIVE.equals(membership.getStatus()))
                && (membership.getJoinDate() == null || membership.getJoinDate().getTime() <= (new Date()).getTime())
                && (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime())) {
//            RequestContext.getCurrentInstance().execute("PF('fingerprintDlg').show();");

            // dung ma nhap vao la ma the nhan vien
            customerCheckin.setCardCode(curMember.getCardCode());
            curMember.setNewMembership(membership);
            checkinFingerprint();

        } else {
            MessageUtil.setErrorMessage("Gói phải ở trạng thái đang hoạt động hoặc dược chuyển nhượng và khoảng thời gian hoạt động là hợp lệ!");
        }

    }

    public void checkinFingerprint() {
        try {
            customerCheckin.setCustomerId(curMember.getMemberId());
            customerCheckin.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
            customerCheckin.setType(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
            customerCheckin.setCheckTime(new Date());
            customerCheckin.setTypeAccess(Constant.WS_C_METHOD.TYPE_FINGERPRINT);
            customerCheckin.setCardCode(String.valueOf(curMember.getMemberId()));
            //filter ds service su dung cho goi
            Map<String, Object> filter = new HashMap();
            filter.put("memberId", curMember.getMemberId());
            filter.put("membershipId", customerCheckin.getMembershipId());
            filter.put("status", Constant.MEMBER_USED_SERVICE.VALID);
            filter.put("startDate-LE", new Date());
            filter.put("endDate-GE", DateTimeUtils.trunc(new Date()));
            List<MemberUsedService> lst = memberUsedServiceService.findList(filter);
            if (lst != null && lst.size() > 0) {
                for (MemberUsedService bo : lst) {
                    if (bo.getAvailable() != null && bo.getAvailable() > 0) {
                        bo.setAvailable(bo.getAvailable() - 1);
                    } else if (bo.getTotalNumber() != null && bo.getTotalNumber() > 0) {
                        MessageUtil.setErrorMessage("Dịch vụ " + bo.getCatService().getServiceName() + " của gói đã hết lượt sử dụng");
                        return;
                    }
                    //luon ghi lai so luot su dung
                    if (bo.getUsedNumber() == null) {
                        bo.setUsedNumber(0l);
                    }
                    bo.setUsedNumber(bo.getUsedNumber() + 1);
                }
            } else {

                MessageUtil.setErrorMessage("Không có dịch vụ còn hiệu lực để sử dụng");
                return;
            }
            // KIEM TRA NEU THE SU DUNG LAN DAU TRONG NGAY THI OFF TAT CAC O CAC MAY
//            MemberController.initCheckOutAll(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER, curMember.getMemberId(), customerCheckin.getCardCode());
            //cap nhat quyen vao may quet

            updateAccessCatMachineFingerprint(curMember, curMember.getNewMembership(), Constant.METHOD.INSERT);
            memberUsedServiceService.saveOrUpdate(lst);

            customerCheckinService.saveOrUpdate(customerCheckin);
            MessageUtil.setInfoMessageFromRes("info.checkin.success");
//            RequestContext.getCurrentInstance().execute("PF('checkinDlg').hide();");

            // show result
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {

                showResultAccessStatus(customerCheckin.getCardCode());
                RequestContext.getCurrentInstance().execute("PF('checkinFingerprintResultDlg').show();PF('tableMemberCustomerFingerprintAccessStatusWidget').filter()");
                RequestContext.getCurrentInstance().update("@widgetVar('checkinFingerprintResultDlg')");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    public void updateAccessCatMachineFingerprint(Member member, Membership membershipDel, String method) {
        String cardCode = String.valueOf(member.getMemberId());
        List<CatGroupPack> lstON = new ArrayList<>();
        List<CatGroupPack> lstNew = new ArrayList<>();// danh sách goi moi can tac dong
        if (membershipDel != null && membershipDel.getGroupPack() != null) {
            lstNew.add(membershipDel.getGroupPack());
        }

        try {

            List<CatMachine> lstCatMachineAccess = CatGroupPackController.getListMachineOffNotExistInON(lstON, lstNew);
            List<String> lstIp = new ArrayList<>();
// cap nhat xoa khoi may quet the
/*            
CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(method);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
             */
            for (CatMachine catMachine : lstCatMachineAccess) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                /*cfg.setContent("MEM_" + member.getMemberId() + "|" + member.getMemberName() + "|" + cardCode + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
                 */
                if (!lstIp.contains(catMachine.getIp())) {
                    lstIp.add(catMachine.getIp());
                }
            }
            //goi ws online
            if (Constant.METHOD.INSERT.equals(method)) {
                CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.ACTIVE, Constant.WS_C_METHOD.TYPE_FINGERPRINT);
            } else {
                CustomerAccessStatusController.callWSUpdateAccess(cardCode, lstIp, Constant.STATUS.DISABLE, Constant.WS_C_METHOD.TYPE_FINGERPRINT);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checkoutFingerprint(Membership membership) {
        try {
            //khi thuc hien checkout thi checkout toan bo cac ban ghi check in theo the deo cua khach hang cua khach hang
            Set<Long> setMembershipId = new HashSet();
            Map<String, Object> filter = new HashMap<>();
            filter.put("cardCode-EXAC_IGNORE_CASE", String.valueOf(curMember.getMemberId()));
            filter.put("customerId", curMember.getMemberId());
            filter.put("status", Constant.CUSTOMER_CHECKIN.CHECKIN);
            filter.put("type", Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
            filter.put("typeAccess", Constant.WS_C_METHOD.TYPE_FINGERPRINT);
            List<CustomerCheckin> lstCus = customerCheckinService.findList(filter);
            if (lstCus != null && !lstCus.isEmpty()) {
                for (CustomerCheckin bo : lstCus) {
                    bo.setStatus(Constant.CUSTOMER_CHECKIN.CHECKOUT);
                    bo.setCheckoutTime(new Date());
                    if (bo.getMembershipId() != null) {
                        setMembershipId.add(bo.getMembershipId());
                    }
                }
            } else {
                MessageUtil.setErrorMessage("Không tồn tại vân tay đang sử dụng cho khách hàng!");
                return;
            }
            customerCheckinService.saveOrUpdate(lstCus);

            updateAccessCatMachineFingerprint(curMember, curMember.getNewMembership(), Constant.METHOD.DELETE);

            MessageUtil.setInfoMessageFromRes("info.checkout.success");
//            RequestContext.getCurrentInstance().execute("PF('checkoutDlg').hide();");
            //cap nhatlai lich trinh ve hoan thanh
            /*
            if (setMembershipId.size() > 0) {
                List<Long> lstMembershipId = new ArrayList<>(setMembershipId);
                Map<String, Object> filterMembership = new HashMap();
                filterMembership.put("membershipId", lstMembershipId);
                List<Membership> lstMs = memberShipService.findList(filterMembership);
                Set<Long> setCustomerScheduleId = new HashSet<>();
                if (lstMs != null && lstMs.size() > 0) {
                    for (Membership m : lstMs) {
                        if (m.getCustomerScheduleId() != null) {
                            setCustomerScheduleId.add(m.getCustomerScheduleId());
                        }
                    }
                }
                for (Long id : setCustomerScheduleId) {
                    completedCustomerSchedule(id);//cap nhat lai lich trinh
                }
            }
             */
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    public void exportTicket(Membership membership) {
        Workbook wbTemplate = null;
//        FileInputStream file = null;
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put("status", Constant.SERVICE_TICKET.STATUS_NEW);
            filter.put("membershipId", membership.getMembershipId());
            List<ServiceTicket> lstTicket = ServiceTicketServiceImpl.getInstance().findList(filter, order);
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("membershipId", membership.getMembershipId());
            filterPay.put("type", Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI);
            List<MemberPayment> lstPay = memberPaymentService.findList(filterPay);
            MemberPayment mp = new MemberPayment();
            if (lstPay != null && lstPay.size() > 0) {
                mp = lstPay.get(0);
            }

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String tempFile = File.separator + "templates" + File.separator + "Template_serviceTicket.xls";

            String desFileName = "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
            String desFileNamePdf = "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
//            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + desFileName;
//            String desPath = "resources" + File.separator + "exported" + File.separator + desFileName;
//            String des = ctx.getRealPath("/") + desPath;
            String resultPath = Constant.OUT_FOLDER;

            if (!CommonUtil.makeDirectory(Util.getRealPath(resultPath))) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }

            InputStream file = ctx.getResourceAsStream(tempFile);

            if (tempFile.endsWith("xls")) {
                wbTemplate = new HSSFWorkbook(file);

            } else if (tempFile.endsWith("xlsx")) {
                wbTemplate = WorkbookFactory.create(file);

            } else {
                return;
            }
            Sheet referSheet = wbTemplate.getSheetAt(0);
            ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
            int rowStt = 0;
            Row r1 = excelWriterUtils.getOrCreateRow(referSheet, rowStt);
            Row r2 = excelWriterUtils.getOrCreateRow(referSheet, rowStt + 1);
            Row r3 = excelWriterUtils.getOrCreateRow(referSheet, rowStt + 2);

            Row r9 = excelWriterUtils.getOrCreateRow(referSheet, rowStt + 8);
            //Row r10 = excelWriterUtils.getOrCreateRow(referSheet, rowStt + 9);

            CellStyle sTop = r1.getCell(0).getCellStyle();
            CellStyle sMidTop = r2.getCell(0).getCellStyle();
            CellStyle sMid = r3.getCell(0).getCellStyle();
            CellStyle sBot = r9.getCell(0).getCellStyle();
            // CellStyle sPadding = wbTemplate.createCellStyle();

            //in dam
            Font font = wbTemplate.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);

            //tao file ma
            String centerName = "Trung Tâm Hoa Sen Spa";
            String perserviceName = "Phiếu Dịch Vụ: ";
            String serviceName = perserviceName + membership.getGroupPack().getGroupPackName();
            String customerName = "Khách Hàng : " + curMember.getMemberName() + "";
            String customerUsed = "Khách hàng Sử Dụng DV:…………………………";
            String paymentValue = "Số Tiền: " + DataUtil.getStringNumber(mp.getPaymentValue()) + " VNĐ";
            String paymentCode = "Mã Thanh Toán: " + mp.getPaymentCode();
            String paymentDate = "Ngày Thanh Toán: " + DateTimeUtils.format(mp.getCreateTime(), "dd/MM/yyyy");
            String paymentDateUsed = "Ngày Sử dụng:……………………………………";
            int perRow = 0;
            if (lstTicket == null || lstTicket.isEmpty()) {
                MessageUtil.setInfoMessage("Không còn vé để sử dụng!");
//                fileExported=null;
//                return;
            } else {
                for (int i = 0; i < lstTicket.size(); i++) {

                    int rowCur = perRow;
//                Row row1 = excelWriterUtils.getOrCreateRow(referSheet, rowCur);
                    int colIndex = (i % 2) * 2;// cot A va cot C
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, centerName, sTop);

                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, serviceName, sMidTop)
                            .getRichStringCellValue().applyFont(perserviceName.length(), serviceName.length(), font);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, customerName, sMid);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, customerUsed, sMid);
                    String seria = "Seri phiếu: " + lstTicket.get(i).getServiceTicketCode() + "…… Số Lần: ………………";
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, seria, sMid)
                            .getRichStringCellValue().applyFont("Seri phiếu: ".length(), ("Seri phiếu: " + lstTicket.get(i).getServiceTicketCode()).length(), font);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, paymentValue, sMid);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, paymentCode, sMid);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, paymentDate, sMid);
                    excelWriterUtils.createCell(referSheet, colIndex, rowCur++, paymentDateUsed, sBot);
                    if (i % 2 == 1) {// sau khi da insert cot 2
                        excelWriterUtils.getOrCreateRow(referSheet, rowCur++).setHeight((short) 100);
                        perRow = rowCur;

                    }
                }

//                PdfUtil.createPdfTicket(curMember, membership
//                        , Util.getRealPath(resultPath) + desFileNamePdf
//                        , mp, lstTicket,Util.getRealPath(Constant.FONT_TIMES));
            }

            File tmpTemplateFile = new File(Util.getRealPath(resultPath), desFileName);
            OutputStream outputStream = new FileOutputStream(tmpTemplateFile);
            wbTemplate.write(outputStream);
            outputStream.flush();
            outputStream.close();

            InputStream stream = new FileInputStream(Util.getRealPath(resultPath + desFileName));
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", desFileName);

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public void exportTicketPdf(Membership membership) {
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            Map<String, Object> filter = new HashMap<>();
            filter.put("status", Constant.SERVICE_TICKET.STATUS_NEW);
            filter.put("membershipId", membership.getMembershipId());
            List<ServiceTicket> lstTicket = ServiceTicketServiceImpl.getInstance().findList(filter, order);
            Map<String, Object> filterPay = new HashMap<>();
            filterPay.put("membershipId", membership.getMembershipId());
            filterPay.put("type", Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI);
            List<MemberPayment> lstPay = memberPaymentService.findList(filterPay);
            MemberPayment mp = new MemberPayment();
            if (lstPay != null && lstPay.size() > 0) {
                mp = lstPay.get(0);
            }

            String desFileNamePdf = "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";

            String resultPath = Constant.OUT_FOLDER;

            if (!CommonUtil.makeDirectory(Util.getRealPath(resultPath))) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }

            if (lstTicket == null || lstTicket.isEmpty()) {
                MessageUtil.setInfoMessage("Không còn vé để sử dụng!");
                pathRealFile = "";
            } else {

                PdfUtil.createPdfTicket(curMember, membership,
                        Util.getRealPath(resultPath + desFileNamePdf),
                        mp, lstTicket, Util.getRealPath(Constant.FONT_TIMES));
                pathRealFile = Util.getRealPath(resultPath + desFileNamePdf);
            }

//            InputStream stream = new FileInputStream(Util.getRealPath(resultPath + desFileNamePdf));
//            fileExported = new DefaultStreamedContent(stream, "application/pdf", desFileNamePdf);
            if (pathRealFile != null && !pathRealFile.equals("")) {
                MessageUtil.setInfoMessage("Export thành công");
                RequestContext.getCurrentInstance().update(":showPdfForm");
                RequestContext.getCurrentInstance().execute("PF('showPdfDlg').show();");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public List<ServiceTicket> getLstCheckinTicket() {
        return lstCheckinTicket;
    }

    public void setLstCheckinTicket(List<ServiceTicket> lstCheckinTicket) {
        this.lstCheckinTicket = lstCheckinTicket;
    }

    public String getPathRealFile() {
        return pathRealFile;
    }

    public void setPathRealFile(String pathRealFile) {
        this.pathRealFile = pathRealFile;
    }

    public void preCheckinTicket(Membership membership) {
        if ((Constant.MEMBERSHIP_STATUS.ACTIVE.equals(membership.getStatus()) || Constant.MEMBERSHIP_STATUS.RECEIVE.equals(membership.getStatus()))
                && (membership.getJoinDate() == null || membership.getJoinDate().getTime() <= (new Date()).getTime())
                && (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime())) {
            RequestContext.getCurrentInstance().execute("PF('checkinTicketDlg').show();");
            curMember.setNewMembership(membership);
        } else {
            MessageUtil.setErrorMessage("gói phải ở trạng thái đang hoạt động hoặc dược chuyển nhượng và khoảng thời gian hoạt động là hợp lệ!");
        }
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("status", "ASC");
            Map<String, Object> filter = new HashMap<>();
            filter.put("membershipId", membership.getMembershipId());
            lstCheckinTicket = ServiceTicketServiceImpl.getInstance().findList(filter, order);
        } catch (Exception e) {
        }

    }

    public void checkinTicket() {
        try {
            List<CustomerCheckin> lstCheckins = new ArrayList<>();
            for (ServiceTicket checkinTicket : lstCheckinTicket) {
                //cap nhat checkin cho ve su dung
                if (StringUtil.isNotNull(checkinTicket.getCardCode()) && Constant.SERVICE_TICKET.STATUS_NEW.equals(checkinTicket.getStatus())) {
                    checkinTicket.setUsedTime(new Date());
                    checkinTicket.setStatus(Constant.SERVICE_TICKET.STATUS_USED);

                    CustomerCheckin cs = new CustomerCheckin();
                    cs.setCardCode(checkinTicket.getCardCode());
                    cs.setCheckTime(new Date());
                    cs.setCustomerId(curMember.getMemberId());
                    cs.setMembershipId(checkinTicket.getMembershipId());

                    cs.setStatus(Constant.CUSTOMER_CHECKIN.CHECKIN);
                    cs.setType(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER);
                    cs.setTypeAccess(Constant.WS_C_METHOD.TYPE_CARD);
                    lstCheckins.add(cs);
                }
            }
            if (lstCheckins.size() == 0) {
                MessageUtil.setErrorMessage("Chưa nhập mã khóa đeo tay để Checkin!");
                return;
            }
            //filter ds service su dung cho goi
            Map<String, Object> filter = new HashMap();
            filter.put("memberId", curMember.getMemberId());
            filter.put("membershipId", curMember.getNewMembership().getMembershipId());
            filter.put("status", Constant.MEMBER_USED_SERVICE.VALID);
            filter.put("startDate-LE", new Date());
            filter.put("endDate-GE", DateTimeUtils.trunc(new Date()));
            List<MemberUsedService> lst = memberUsedServiceService.findList(filter);
            if (lst != null && lst.size() > 0
                    || (curMember.getNewMembership().getAvailable() != null && curMember.getNewMembership().getAvailable() > 0)) {
                if (lst != null && lst.size() > 0) {
                    for (MemberUsedService bo : lst) {
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
                }
                // cap nhat thong tin chung cho goi
                if (curMember.getNewMembership().getAvailable() != null && curMember.getNewMembership().getAvailable() > 0) {
                    curMember.getNewMembership().setAvailable(curMember.getNewMembership().getAvailable() - lstCheckins.size());
                } else if (curMember.getNewMembership().getAvailable() != null && curMember.getNewMembership().getAvailable().equals(0l)) {
                    MessageUtil.setErrorMessage("Gói đã hết lượt sử dụng");
                    return;
                }
                if (curMember.getNewMembership().getUsedNumber() == null) {
                    curMember.getNewMembership().setUsedNumber(Long.valueOf(lstCheckins.size()));
                } else {
                    curMember.getNewMembership().setUsedNumber(curMember.getNewMembership().getUsedNumber() + lstCheckins.size());
                }
            } else {

                MessageUtil.setErrorMessage("Không có dịch vụ còn hiệu lực để sử dụng");
                return;
            }
            //cap nhat quyen vao may quet
            List<String> lstCardCode = new ArrayList<>();
            for (CustomerCheckin customerCheckin : lstCheckins) {
                // KIEM TRA NEU THE SU DUNG LAN DAU TRONG NGAY THI OFF TAT CAC O CAC MAY
                MemberController.initCheckOutAll(Constant.CUSTOMER_CHECKIN.TYPE_MEMBER, curMember.getMemberId(), customerCheckin.getCardCode());

                updateAccessCatMachineNew(customerCheckin.getMember(), curMember.getNewMembership(), Constant.METHOD.INSERT, customerCheckin.getCardCode());
                lstCardCode.add(customerCheckin.getCardCode());
            }
            ServiceTicketServiceImpl.getInstance().saveOrUpdate(lstCheckinTicket);
            memberUsedServiceService.saveOrUpdate(lst);
            memberShipService.saveOrUpdate(curMember.getNewMembership());
            curMember.setNewMembership(new Membership());

            customerCheckinService.saveOrUpdate(lstCheckins);
            MessageUtil.setInfoMessageFromRes("info.checkin.success");
            RequestContext.getCurrentInstance().execute("PF('checkinTicketDlg').hide();");
            RequestContext.getCurrentInstance().execute(" PF('tabViewMemberInfo').select(1);");

// show result
            if (Constant.CONFIG.IS_LOCAL.equals(DataConfig.getConfigByKey("IS_LOCAL"))) {
                showResultAccessStatus(lstCardCode);
                RequestContext.getCurrentInstance().execute("PF('checkinResultDlg').show();");
                RequestContext.getCurrentInstance().update("@widgetVar('checkinResultDlg')");
            }
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");

            e.printStackTrace();
        }
    }

    public void paymentTransferMembership() {
        try {
            //validate
            if (curMember.getMemberPayment().getDebt() < 0 && StringUtil.isNullOrEmpty(curMember.getMemberPayment().getReason())) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                        MessageUtil.getResourceBundleMessage("memberPayment.reason")));
                return;
            }
            //on print

            Membership membership = curMember.getNewMembership();

            String oldMeberShip = membership.toString();

            Membership membership1 = CommonUtil.mapper.map(membership, Membership.class);
            membership1.setMemberId(null);//tao moi
            membership1.setStatus(4L);
            membership1.setGroupPackId(membership.getGroupPackId());
            membership1.setMemberId(memberAssign.getMemberId());
            membership1.setMemberGrantorId(curMember.getMemberId());
            membership1.setJoinDate(membership.getJoinDate());
            membership1.setEndDate(membership.getEndDate());
            membership.setStatus(3L);
            membership.setMemberAssignId(memberAssign.getMemberId());

//            updateAccessCatMachine(memberAssign, membership1, Constant.METHOD.INSERT);
            //write log
            // cap nhat
//            updateAccessCatMachine(curMember, membership, Constant.METHOD.DELETE);
            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, membership1.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldMeberShip, membership.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            Long numberPerPack = null;
            boolean createTicket = false;
            if (membership1.getMembershipId() == null && !Constant.MEMBERSHIP_ACTION_TYPE.EXTEND.equals(membershipActionType)) {
                //them thong tin user using service
                CatGroupPack catGroupPack = membership1.getGroupPack();
                List<CatPack> lstCatPacks = catGroupPack.getPacks();
                if (lstCatPacks != null) {
                    for (CatPack bo : lstCatPacks) {
                        if (bo != null && bo.getCatService() != null) {
                            if (bo.getNumberOfTime() != null && (numberPerPack == null || bo.getNumberOfTime() < numberPerPack)) {
                                numberPerPack = bo.getNumberOfTime();
                            }
                        }
                    }
                }
                //tao so lan su dung dich vu
                if (numberPerPack != null && membership1.getGroupPack() != null && !Constant.GROUP_PACK_TYPE.TYPE_HV_TIME.equals(membership1.getGroupPack().getType())) {
                    membership1.setAvailable(numberPerPack * membership1.getNumberPack());
                    createTicket = true;
                }

            }

            //memberPayment thong tin thanh toan chuyen nhuong
            MemberPayment memberPayment = curMember.getMemberPayment();
            memberPayment.setNumberPack(membership.getNumberPack());
            CatPromotion catPromotion = memberPayment.getCatPromotion();
            //km them thoi gian
            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType())
                    && membership1.getEndDate() != null && catPromotion.getValue() != null) {
                Date date = membership1.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, catPromotion.getValue().intValue());
                Date endate = calendar.getTime();
                membership1.setEndDate(endate);
            }
            // in hoa don
            onPrintBill();

            //hlv
            if (membership1.getTrainer() != null) {
                membership1.setTrainerId(membership1.getTrainer().getEmployeeId());
            }

            memberShipService.saveOrUpdate(membership1);
            memberShipService.saveOrUpdate(membership);
            //tao ticket
            if (createTicket) {
                List<ServiceTicket> lstTicket = ServiceTicketServiceImpl.createListTicket(membership1.getMembershipId(), Integer.valueOf(membership1.getAvailable().toString()));
                ServiceTicketServiceImpl.getInstance().saveOrUpdate(lstTicket);
            }
            memberPayment.setMemberId(curMember.getMemberId());
            memberPayment.setMembershipId(membership.getMembershipId());
            if (catPromotion != null) {
                memberPayment.setCatPromotionId(catPromotion.getCatPromotionId());
            }
            if (getRequest().getSession().getAttribute("user") != null) {
                memberPayment.setEmployeeId(((CatUser) getRequest().getSession().getAttribute("user")).getEmpId());
            }
            //cong no chi co gia trị am 
            if (memberPayment.getDebt() > 0) {
                memberPayment.setDebt(0l);
            }
            memberPayment.setType(Constant.PAYMENT_TYPE.FEE_TRANSFER);
            memberPayment.setCreateTime(new Date());
            memberPaymentService.saveOrUpdate(memberPayment);
            //memberPromotion
            if (catPromotion != null && catPromotion.getCatPromotionId() != null) {

                MemberPromotion memberPromotion = new MemberPromotion();
                memberPromotion.setMemberId(curMember.getMemberId());
                memberPromotion.setCatPromotionId(curMember.getMemberPayment().getCatPromotion().getCatPromotionId());
                memberPromotion.setGroupPackId(membership.getGroupPackId());
                String desc = catPromotion.getTypeName() + ": " + catPromotion.getValue()
                        + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                        ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType()) ? "VNĐ" : "");
                memberPromotion.setDescription(desc);
                memberPromotion.setValue(catPromotion.getValue());
                memberPromotion.setCreateDate(new Date());
                memberPromotion.setMembershipId(membership.getMembershipId());

                memberPromotionService.saveOrUpdate(memberPromotion);
            }
            //them thong tin user using service
            CatGroupPack catGroupPack = membership1.getGroupPack();
            List<MemberUsedService> lstMemberUsedServices = new ArrayList<MemberUsedService>();
//            List<CatService> lstCatServices = new ArrayList<>();
            try {
                List<CatPack> lstCatPacks = catGroupPack.getPacks();
                if (lstCatPacks != null) {
                    for (CatPack bo : lstCatPacks) {
                        if (bo != null && bo.getCatService() != null) {
                            CatService boService = bo.getCatService();

                            MemberUsedService mus = new MemberUsedService();
                            mus.setCreateTime(new Date());
                            mus.setMemberId(membership1.getMemberId());
                            mus.setServiceId(boService.getServiceId());
                            mus.setMembershipId(membership1.getMembershipId());
                            mus.setStatus(1l);
                            mus.setStartDate(membership1.getJoinDate());
                            mus.setEndDate(membership1.getEndDate());
                            Long numberOfTime = null;
                            if (bo.getNumberOfTime() != null) {
                                numberOfTime = bo.getNumberOfTime() * membership1.getNumberPack();
                            }
                            mus.setAvailable(numberOfTime);
                            mus.setTotalNumber(numberOfTime);
                            if (catPromotion != null && Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                                    && mus.getAvailable() != null) {
                                mus.setAvailable(mus.getAvailable() + Math.round(catPromotion.getValue()));
                                mus.setTotalNumber(mus.getAvailable() + Math.round(catPromotion.getValue()));
                            }
                            lstMemberUsedServices.add(mus);

                            if (bo.getNumberOfTime() != null && bo.getNumberOfTime() < numberPerPack) {
                                numberPerPack = bo.getNumberOfTime();
                            }
                        }
                    }
                }

                //tao user using service
                GenericDaoImplNewV2 musService = new GenericDaoImplNewV2<MemberUsedService, Long>() {
                };
                musService.saveOrUpdate(lstMemberUsedServices);
                //log
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, lstMemberUsedServices.toArray().toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            curMember = memberService.findById(curMember.getMemberId());
            //ghi log
//            if (oldObjectMeberShipStr != null) {
//                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
//                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//            } else {
//                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectMeberShipStr, newMembership.toString(), this.getClass().getSimpleName(),
//                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
//            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, memberPayment.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('paymentDlg').hide();");
            //show thanh toan
            if (billImgPath != null) {
                RequestContext.getCurrentInstance().update(":cfImgMemberFormLocal");
                RequestContext.getCurrentInstance().execute("PF('cfImgMemberDlgLocal').show();");
            }
            //clear form
            preEditMember(curMember);
            preAddMembership();

        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEditPaymentTime(MemberPayment memberPayment) {
        curMember.setMemberPayment(memberPayment);
        oldObjectMemberPaymentStr = memberPayment.toString();
    }

    public void saveEditPaymentTime() {
        try {

            if (curMember.getMemberPayment().getCreateTime() == null) {
                MessageUtil.setErrorMessage("Thời gian thanh toán bắt buộc nhập!");

                return;
            }
            memberPaymentService.saveOrUpdate(curMember.getMemberPayment());
            //ghi log
            if (oldObjectMemberPaymentStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectMemberPaymentStr, curMember.getMemberPayment().toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectMemberPaymentStr, curMember.getMemberPayment().toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("common.success");
            RequestContext.getCurrentInstance().execute("PF('editPaymentTimeDlg').hide();");
            preEditMember(curMember);

        } catch (Exception e) {

            MessageUtil.setErrorMessageFromRes("common.fail");
            e.printStackTrace();
        }
    }

}
