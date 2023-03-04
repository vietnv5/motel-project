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
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "CUSTOMER_CHECKIN")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerCheckin
{

    private Long groupMemberId;
    private Long status;
    private Date checkTime;
    private Date checkoutTime;
    private String cardCode;
    private Long customerId;
    private Long id;
    private Long type;
    private String typeName;
    private String statusName;
    private Long membershipId;

    private String customerName;
    private Member member;
    private Long typeAccess;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0)
    public Long getGroupMemberId()
    {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId)
    {
        this.groupMemberId = groupMemberId;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECK_TIME", length = 11)
    public Date getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Date checkTime)
    {
        this.checkTime = checkTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECKOUT_TIME", length = 11)
    public Date getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }

    @Column(name = "CARD_CODE", length = 50)
    public String getCardCode()
    {
        return cardCode;
    }

    public void setCardCode(String cardCode)
    {
        this.cardCode = cardCode;
    }

    @Column(name = "CUSTOMER_ID", precision = 22, scale = 0)
    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CUSTOMER_CHECKIN_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    @Column(name = "membership_id", precision = 22, scale = 0)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    public CustomerCheckin()
    {
    }

    public CustomerCheckin(Long id)
    {
        this.id = id;
    }

    @Transient
    public String getTypeName()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(type))
        {
            typeName = MessageUtil.getResourceBundleMessage("customerCheckin.type2");
        }
        else if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type))
        {
            typeName = MessageUtil.getResourceBundleMessage("customerCheckin.type1");
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
        if (Constant.CUSTOMER_CHECKIN.CHECKIN.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerCheckin.status1");
        }
        else if (Constant.CUSTOMER_CHECKIN.CHECKOUT.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerCheckin.status2");
        }
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    @Transient
    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    @Transient
    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

    @Column(name = "TYPE_ACCESS", precision = 22, scale = 0)
    public Long getTypeAccess()
    {
        return typeAccess;
    }

    public void setTypeAccess(Long typeAccess)
    {
        this.typeAccess = typeAccess;
    }

}
