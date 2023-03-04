/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "CLIENT_PAYMENT")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ClientPayment
{

    private Long employeeId;
    private Long price;
    private Long catPromotionId;
    private String vat;
    private Long debt;
    private Long paymentValue;
    private Long clientId;
    private String description;
    private Long clientUsePackId;
    private Long paymentId;
    private Date createTime;
    //    private Long point;
    private String reason;
    private String paymentCode;

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

    private ClientUsePack clientUsePack;

    //    private Long numberPack;
    private Long timesUsed;
    private Long statusTimeUsed;
    private String voucherCode;

    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0)
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @Column(name = "PRICE", precision = 22, scale = 0)
    public Long getPrice()
    {
        return price;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    @Column(name = "CAT_PROMOTION_ID", precision = 22, scale = 0)
    public Long getCatPromotionId()
    {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId)
    {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "VAT", length = 255)
    public String getVat()
    {
        return vat;
    }

    public void setVat(String vat)
    {
        this.vat = vat;
    }

    @Column(name = "DEBT", precision = 22, scale = 0)
    public Long getDebt()
    {
        return debt;
    }

    public void setDebt(Long debt)
    {
        this.debt = debt;
    }

    @Column(name = "PAYMENT_VALUE", precision = 22, scale = 0)
    public Long getPaymentValue()
    {
        return paymentValue;
    }

    public void setPaymentValue(Long paymentValue)
    {
        this.paymentValue = paymentValue;
    }

    @Column(name = "CLIENT_ID", precision = 22, scale = 0)
    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    @Column(name = "DESCRIPTION", length = 300)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "CLIENT_USE_PACK_ID", precision = 22, scale = 0)
    public Long getClientUsePackId()
    {
        return clientUsePackId;
    }

    public void setClientUsePackId(Long clientUsePackId)
    {
        this.clientUsePackId = clientUsePackId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CLIENT_PAYMENT_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "PAYMENT_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", length = 11)
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    //    @Column(name = "POINT", precision = 22, scale = 0)
//    public Long getPoint() {
//        return point;
//    }
//
//    public void setPoint(Long point) {
//        this.point = point;
//    }
    @Column(name = "REASON", length = 200)
    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    @Column(name = "PAYMENT_CODE", length = 30)
    public String getPaymentCode()
    {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode)
    {
        this.paymentCode = paymentCode;
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

    public ClientPayment()
    {
    }

    public ClientPayment(Long paymentId)
    {
        this.paymentId = paymentId;
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
    public ClientUsePack getClientUsePack()
    {
        try
        {
            if (clientUsePackId != null)
            {
                clientUsePack = new GenericDaoImplNewV2<ClientUsePack, Long>()
                {
                }.findById(clientUsePackId);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(ClientPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clientUsePack;
    }

    public void setClientUsePack(ClientUsePack clientUsePack)
    {
        this.clientUsePack = clientUsePack;
    }

    //
//    public void createPaymentCode() {
//
//        String hqlCheckCode = "select count(*) from ClientPayment where paymentCode like ?||'-%'";
//        String code = "CLIENT";
//        if (code.isEmpty()) {
//            return;
//        }
//        List<?> counts = new GenericDaoImplNewV2<CatPack, Long>() {
//        }.findListAll(hqlCheckCode, code);
//        if (counts.size() > 0) {
//            paymentCode = code + "-" + (Long.valueOf(counts.get(0).toString()) + 1);
//        }
//    }
    public void createPaymentCode(Long barchId)
    {

        String hqlCheckCode = "select max(to_number( substr(PAYMENT_CODE,5))) from Client_Payment where payment_Code like ?||'%'";
        String code = Constant.PAYMENT_TYPE.CLIENT.toString();
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
        }
        paymentCode = code + numberStr;
    }

    @Transient
    public String getTypeName()
    {
        if (type != null && type.equals(1l))
        {
            typeName = MessageUtil.getResourceBundleMessage("payment.type1");
        }
        else if (type != null && type.equals(2l))
        {
            typeName = MessageUtil.getResourceBundleMessage("payment.type2");
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

    @Column(name = "times_used", precision = 22, scale = 0)
    public Long getTimesUsed()
    {
        return timesUsed;
    }

    public void setTimesUsed(Long timesUsed)
    {
        this.timesUsed = timesUsed;
    }

    @Column(name = "status_time_used", precision = 22, scale = 0)
    public Long getStatusTimeUsed()
    {
        return statusTimeUsed;
    }

    public void setStatusTimeUsed(Long statusTimeUsed)
    {
        this.statusTimeUsed = statusTimeUsed;
    }

    @Column(name = "voucher_code", length = 255)
    public String getVoucherCode()
    {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode)
    {
        this.voucherCode = voucherCode;
    }

}
