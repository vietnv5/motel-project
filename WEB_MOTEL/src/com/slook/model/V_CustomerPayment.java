/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "V_CUSTOMER_PAYMENT")
/**
 *
 * @author: vietnv
 * @version: 1.0
 * @since: 1.0
 */
public class V_CustomerPayment
{

    private Long numberPack;
    private Long employeeId;
    private Date joinDate;
    private Long price;
    private String groupPackName;
    private Long membershipId;
    private Long customerType;
    private Date endDate;
    private String rowkey;
    private String employeeName;
    private Long debt;
    private String employeeCode;
    private Long danop;
    private Long totalNumber;
    private String empTelephone;
    private Long status;
    private Long customerId;
    private String customerName;
    private Long groupPackId;
    private Long usedNumber;
    private Long paymentId;
    private Date createTime;
    private String phoneNumber;
    private Long type;

    private String customerTypeName;
    private String typeName;

    @Column(name = "NUMBER_PACK", precision = 22, scale = 0, updatable = false)
    public Long getNumberPack()
    {
        return numberPack;
    }

    public void setNumberPack(Long numberPack)
    {
        this.numberPack = numberPack;
    }

    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0, updatable = false)
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7, updatable = false)
    public Date getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
    }

    @Column(name = "PRICE", precision = 22, scale = 0, updatable = false)
    public Long getPrice()
    {
        return price;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName()
    {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName)
    {
        this.groupPackName = groupPackName;
    }

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    @Column(name = "CUSTOMER_TYPE", precision = 22, scale = 0, updatable = false)
    public Long getCustomerType()
    {
        return customerType;
    }

    public void setCustomerType(Long customerType)
    {
        this.customerType = customerType;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7, updatable = false)
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    @Column(name = "ROWKEY", length = 81, updatable = false)
    @Id
    public String getRowkey()
    {
        return rowkey;
    }

    public void setRowkey(String rowkey)
    {
        this.rowkey = rowkey;
    }

    @Column(name = "EMPLOYEE_NAME", length = 1020, updatable = false)
    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    @Column(name = "DEBT", precision = 22, scale = 0, updatable = false)
    public Long getDebt()
    {
        return debt;
    }

    public void setDebt(Long debt)
    {
        this.debt = debt;
    }

    @Column(name = "EMPLOYEE_CODE", length = 1020, updatable = false)
    public String getEmployeeCode()
    {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode)
    {
        this.employeeCode = employeeCode;
    }

    @Column(name = "DANOP", precision = 22, scale = 0, updatable = false)
    public Long getDanop()
    {
        return danop;
    }

    public void setDanop(Long danop)
    {
        this.danop = danop;
    }

    @Column(name = "TOTAL_NUMBER", precision = 22, scale = 0, updatable = false)
    public Long getTotalNumber()
    {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber)
    {
        this.totalNumber = totalNumber;
    }

    @Column(name = "EMP_TELEPHONE", length = 1020, updatable = false)
    public String getEmpTelephone()
    {
        return empTelephone;
    }

    public void setEmpTelephone(String empTelephone)
    {
        this.empTelephone = empTelephone;
    }

    @Column(name = "STATUS", precision = 22, scale = 0, updatable = false)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "CUSTOMER_ID", precision = 22, scale = 0, updatable = false)
    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    @Column(name = "CUSTOMER_NAME", length = 255, updatable = false)
    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Column(name = "USED_NUMBER", precision = 22, scale = 0, updatable = false)
    public Long getUsedNumber()
    {
        return usedNumber;
    }

    public void setUsedNumber(Long usedNumber)
    {
        this.usedNumber = usedNumber;
    }

    @Column(name = "PAYMENT_ID", precision = 22, scale = 0, updatable = false)
    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", length = 11, updatable = false)
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Column(name = "PHONE_NUMBER", length = 50, updatable = false)
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "TYPE", precision = 22, scale = 0, updatable = false)
    public Long getType()
    {
        return type;
    }

    public void setType(Long type)
    {
        this.type = type;
    }

    @Transient
    public String getCustomerTypeName()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type2");
        }
        else if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type1");
        }
        else if (Constant.CUSTOMER_CHECKIN.TYPE_GROUP_MEMBER.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type3");
        }

        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName)
    {
        this.customerTypeName = customerTypeName;
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

    public V_CustomerPayment()
    {
    }
}
