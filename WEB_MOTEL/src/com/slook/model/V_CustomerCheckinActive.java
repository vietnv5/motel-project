package com.slook.model;

import com.slook.controller.MemberController;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "V_CUSTOMER_CHECKIN_ACTIVE")
public class V_CustomerCheckinActive implements Serializable
{

    private String groupMemberName;
    private Long groupMemberId;
    private Long status;
    private String checkTime;
    private String cardCode;
    private String checkoutTime;
    private Long customerId;
    private Long id;
    private Long type;
    private String name;
    private String typeName, statusName;
    private Long totalPayment;

    @Column(name = "GROUP_MEMBER_NAME", length = 255, updatable = false)
    public String getGroupMemberName()
    {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName)
    {
        this.groupMemberName = groupMemberName;
    }

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMemberId()
    {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId)
    {
        this.groupMemberId = groupMemberId;
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

    @Column(name = "CHECK_TIME", length = 11, updatable = false)
    public String getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }

    @Column(name = "CARD_CODE", length = 50, updatable = false)
    public String getCardCode()
    {
        return cardCode;
    }

    public void setCardCode(String cardCode)
    {
        this.cardCode = cardCode;
    }

    @Column(name = "CHECKOUT_TIME", length = 11, updatable = false)
    public String getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime)
    {
        this.checkoutTime = checkoutTime;
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

    @Id
    @Column(name = "ID", precision = 22, scale = 0, updatable = false)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    @Column(name = "NAME", length = 255, updatable = false)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public V_CustomerCheckinActive()
    {
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

    @Transient
    public Long getTotalPayment()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type) && totalPayment == null)
        {
            totalPayment = MemberController.computinDebt(customerId);
        }
        return totalPayment;
    }

    public void setTotalPayment(Long totalPayment)
    {
        this.totalPayment = totalPayment;
    }

}
