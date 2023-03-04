package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "MEMBER_PAYMENT", catalog = "")
public class MemberPayment
{

    private Long paymentId;
    private Long memberId;
    private String paymentCode;
    private Long paymentValue;
    private Long point;
    private String reason;
    private String description;
    private String vat;
    private Long price;
    private Long debt;
    private Long catPromotionId;
    private Long employeeId;
    private Long membershipId;
    private Date createTime;
    private Membership membership;

    private boolean isVAT = false;
    boolean editPrice = false;
    boolean isPromotion;
    private CatPromotion catPromotion;

    private Long type;
    private String typeName;
    private Long guestDeposit;
    private Long status;
    private Long totalDeposit;
    private String statusName;
    private Long debtTotal;
    private V_MemberDebtPayment curDebt;
    private String voucherCode;
    private Long numberPack;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_PAYMENT_SEQ", allocationSize = 1)
    @Column(name = "PAYMENT_ID", nullable = false, precision = 0)
    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    @Basic
    @Column(name = "MEMBER_ID", nullable = true, precision = 0)
    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "PAYMENT_CODE", nullable = true, length = 30)
    public String getPaymentCode()
    {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode)
    {
        this.paymentCode = paymentCode;
    }

    @Basic
    @Column(name = "PAYMENT_VALUE", nullable = true, precision = 0)
    public Long getPaymentValue()
    {
        return paymentValue;
    }

    public void setPaymentValue(Long paymentValue)
    {
        this.paymentValue = paymentValue;
    }

    @Basic
    @Column(name = "POINT", nullable = true, precision = 0)
    public Long getPoint()
    {
        return point;
    }

    public void setPoint(Long point)
    {
        this.point = point;
    }

    @Basic
    @Column(name = "REASON", nullable = true, length = 200)
    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 300)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "VAT", nullable = true, length = 200)
    public String getVat()
    {
        return vat;
    }

    public void setVat(String vat)
    {
        this.vat = vat;
    }

    @Column(name = "price", nullable = true, precision = 0)
    public Long getPrice()
    {
        return price;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    @Column(name = "debt", nullable = true, precision = 0)
    public Long getDebt()
    {
        return debt;
    }

    public void setDebt(Long debt)
    {
        this.debt = debt;
    }

    @Column(name = "cat_promotion_id", nullable = true, precision = 0)
    public Long getCatPromotionId()
    {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId)
    {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "employee_id", nullable = true, precision = 0)
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @Column(name = "MEMBERSHIP_ID", nullable = true, precision = 0)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MemberPayment that = (MemberPayment) o;

        if (paymentId != null ? !paymentId.equals(that.paymentId) : that.paymentId != null)
        {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null)
        {
            return false;
        }
        if (paymentCode != null ? !paymentCode.equals(that.paymentCode) : that.paymentCode != null)
        {
            return false;
        }
        if (paymentValue != null ? !paymentValue.equals(that.paymentValue) : that.paymentValue != null)
        {
            return false;
        }
        if (point != null ? !point.equals(that.point) : that.point != null)
        {
            return false;
        }
        if (reason != null ? !reason.equals(that.reason) : that.reason != null)
        {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = paymentId != null ? paymentId.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (paymentCode != null ? paymentCode.hashCode() : 0);
        result = 31 * result + (paymentValue != null ? paymentValue.hashCode() : 0);
        result = 31 * result + (point != null ? point.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Transient
    public boolean getIsVAT()
    {
        return isVAT;
    }

    public void setIsVAT(boolean isVAT)
    {
        this.isVAT = isVAT;
    }

    @Transient
    public boolean getEditPrice()
    {
        return editPrice;
    }

    public void setEditPrice(boolean editPrice)
    {
        this.editPrice = editPrice;
    }

    @Transient
    public boolean getIsPromotion()
    {
        return isPromotion;
    }

    public void setIsPromotion(boolean isPromotion)
    {
        this.isPromotion = isPromotion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "cat_promotion_id", insertable = false, updatable = false)
    public CatPromotion getCatPromotion()
    {
        return catPromotion;
    }

    public void setCatPromotion(CatPromotion catPromotion)
    {
        this.catPromotion = catPromotion;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "MEMBERSHIP_ID", insertable = false, updatable = false)
    public Membership getMembership()
    {
        return membership;
    }

    public void setMembership(Membership membership)
    {
        this.membership = membership;
    }

    public void createPaymentCode(Long barchId)
    {

//        String hqlCheckCode = "select count(*) from MemberPayment where paymentCode like ?||'%'";
        String hqlCheckCode = "select max(to_number( substr(PAYMENT_CODE,5))) from Member_Payment where payment_Code like ?||'%'";
        String code = Constant.PAYMENT_TYPE.MEMBER.toString();
        String barch = "000";
        if (barchId != null)
        {
            barch = barch + barchId.toString();
            code += barch.substring(barch.length() - 3);
        }

        List<?> counts = new GenericDaoImplNewV2<CatPack, Long>()
        {
        }.findListSQLAll(hqlCheckCode, code);
        String numberStr = "00000";
        if (counts.size() > 0 && counts.get(0) != null)
        {
            Long number = (Long.valueOf(counts.get(0).toString()) + 1);
            numberStr += number;
            numberStr = numberStr.substring(numberStr.length() - 5);

//            paymentCode = code + "-" + (Long.valueOf(counts.get(0).toString()) + 1);
        }
        paymentCode = code + numberStr;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "TYPE", precision = 22, scale = 0)
    public Long getType()
    {
        return type;
    }

    public void setType(Long type)
    {
        this.type = type;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "guest_deposit", precision = 22, scale = 0)
    public Long getGuestDeposit()
    {
        return guestDeposit;
    }

    public void setGuestDeposit(Long guestDeposit)
    {
        this.guestDeposit = guestDeposit;
    }

    @Column(name = "total_deposit", precision = 22, scale = 0)
    public Long getTotalDeposit()
    {
        return totalDeposit;
    }

    public void setTotalDeposit(Long totalDeposit)
    {
        this.totalDeposit = totalDeposit;
    }

    @Transient
    public String getTypeName()
    {
        if (type != null && type.equals(Constant.PAYMENT_TYPE.DAT_COC_MUA_GOI))
        {
            typeName = MessageUtil.getResourceBundleMessage("memberPayment.type1");
        }
        else if (type != null && type.equals(Constant.PAYMENT_TYPE.THANH_TOAN_TRA_NO))
        {
            typeName = MessageUtil.getResourceBundleMessage("memberPayment.type2");
        }
        else if (type != null && type.equals(Constant.PAYMENT_TYPE.FEE_TRANSFER))
        {
            typeName = MessageUtil.getResourceBundleMessage("memberPayment.type3");
        }
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    @Transient
    public String getStatusName()
    {
        if (Constant.CLIENT_PAYMENT.STATUS_NOT_USE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("payment.status0");
        }
        else if (Constant.CLIENT_PAYMENT.STATUS_USED.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("payment.status1");
        }
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    @Transient
    public Long getDebtTotal()
    {
        return debtTotal;
    }

    public void setDebtTotal(Long debtTotal)
    {
        this.debtTotal = debtTotal;
    }

    @Transient
    public V_MemberDebtPayment getCurDebt()
    {
        return curDebt;
    }

    public void setCurDebt(V_MemberDebtPayment curDebt)
    {
        this.curDebt = curDebt;
    }

    @Column(name = "voucher_code", nullable = true, length = 250)
    public String getVoucherCode()
    {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }

    @Transient
    public Long getNumberPack()
    {
        return numberPack;
    }

    public void setNumberPack(Long numberPack)
    {
        this.numberPack = numberPack;
    }

}
