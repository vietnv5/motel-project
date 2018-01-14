package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "GROUP_MEMBER_PAYMENT")
public class GroupMemberPayment {

    private Long groupMembershipId;
    private Long employeeId;
    private Long price;
    private Long catPromotionId;
    private Long groupMemberId;
    private String vat;
    private Long debt;
    private Long paymentValue;
    private String description;
    private Long paymentId;
    private Date createTime;
    private String reason;
    private String paymentCode;
    private CatPromotion catPromotion;
    private GroupMembership groupMembership;

    private boolean isVAT = false;
    boolean editPrice = false;
    boolean isPromotion;

    private Long type;
    private String typeName;
    private Long guestDeposit;
    private Long status;
    private Long totalDeposit;
    private String statusName;
    private Long debtTotal;
    private V_GroupMemberDebtPayment curDebt;

    @Column(name = "GROUP_MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0)
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name = "PRICE", precision = 22, scale = 0)
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Column(name = "CAT_PROMOTION_ID", precision = 22, scale = 0)
    public Long getCatPromotionId() {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId) {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "Group_MEMBER_ID", precision = 22, scale = 0)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Column(name = "VAT", length = 255)
    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    @Column(name = "DEBT", precision = 22, scale = 0)
    public Long getDebt() {
        return debt;
    }

    public void setDebt(Long debt) {
        this.debt = debt;
    }

    @Column(name = "PAYMENT_VALUE", precision = 22, scale = 0)
    public Long getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(Long paymentValue) {
        this.paymentValue = paymentValue;
    }

    @Column(name = "DESCRIPTION", length = 300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_MEMBER_PAYMENT_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "PAYMENT_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "REASON", length = 200)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "PAYMENT_CODE", length = 30)
    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT_PROMOTION_ID", insertable = false, updatable = false)
    public CatPromotion getCatPromotion() {
        return catPromotion;
    }

    public void setCatPromotion(CatPromotion catPromotion) {
        this.catPromotion = catPromotion;
    }

    public GroupMemberPayment() {
    }

    public GroupMemberPayment(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Transient
    public boolean getIsVAT() {
        return isVAT;
    }

    public void setIsVAT(boolean isVAT) {
        this.isVAT = isVAT;
    }

    @Transient
    public boolean getEditPrice() {
        return editPrice;
    }

    public void setEditPrice(boolean editPrice) {
        this.editPrice = editPrice;
    }

    @Transient
    public boolean getIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }
//
//    public void createPaymentCode() {
//
//        String hqlCheckCode = "select count(*) from GroupMemberPayment where paymentCode like ?||'-%'";
//        String code = "GMEM";
//        if (code.isEmpty()) {
//            return;
//        }
//        List<?> counts = new GenericDaoImplNewV2<CatPack, Long>() {
//        }.findListAll(hqlCheckCode, code);
//        if (counts.size() > 0) {
//            paymentCode = code + "-" + (Long.valueOf(counts.get(0).toString()) + 1);
//        }
//    }

    public void createPaymentCode(Long barchId) {

        String hqlCheckCode = "select max(to_number( substr(PAYMENT_CODE,5))) from Group_Member_Payment where payment_Code like ?||'%'";
        String code = Constant.PAYMENT_TYPE.GROUP_MEMBER.toString();
        String barch = "000";
        if (barchId != null) {
            barch = barch + barchId.toString();
            code += barch.substring(barch.length() - 3);
        }

        List<?> counts = new GenericDaoImplNewV2<CatPack, Long>() {
        }.findListSQLAll(hqlCheckCode, code);
        String numberStr = "00000";
        if (counts.size() > 0 && counts.get(0) != null) {
            Long number = (Long.valueOf(counts.get(0).toString()) + 1);
            numberStr += number;
            numberStr = numberStr.substring(numberStr.length() - 5);
        }
        paymentCode = code + numberStr;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "TYPE", precision = 22, scale = 0)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "guest_deposit", precision = 22, scale = 0)
    public Long getGuestDeposit() {
        return guestDeposit;
    }

    public void setGuestDeposit(Long guestDeposit) {
        this.guestDeposit = guestDeposit;
    }

    @Column(name = "total_deposit", precision = 22, scale = 0)
    public Long getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(Long totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    @Transient
    public String getTypeName() {
        if (type != null && type.equals(Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI)) {
            typeName = MessageUtil.getResourceBundleMessage("memberPayment.type1");
        } else if (type != null && type.equals(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO)) {
            typeName = MessageUtil.getResourceBundleMessage("memberPayment.type2");
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Transient
    public String getStatusName() {
        if (Constant.CLIENT_PAYMENT.STATUS_NOT_USE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("payment.status0");
        } else if (Constant.CLIENT_PAYMENT.STATUS_USED.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("payment.status1");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Transient
    public Long getDebtTotal() {
        return debtTotal;
    }

    public void setDebtTotal(Long debtTotal) {
        this.debtTotal = debtTotal;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "GROUP_MEMBERSHIP_ID", insertable = false, updatable = false)
    public GroupMembership getGroupMembership() {
        return groupMembership;
    }

    public void setGroupMembership(GroupMembership groupMembership) {
        this.groupMembership = groupMembership;
    }

    @Transient
    public V_GroupMemberDebtPayment getCurDebt() {
        return curDebt;
    }

    public void setCurDebt(V_GroupMemberDebtPayment curDebt) {
        this.curDebt = curDebt;
    }
}
