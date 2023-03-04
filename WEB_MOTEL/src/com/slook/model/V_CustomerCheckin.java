package com.slook.model;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name = "V_CUSTOMER_CHECKIN")
public class V_CustomerCheckin
{

    private String groupMemberName;
    private Long groupMemberId;
    private Long status;
    private Date checkTime;
    private String cardCode;
    private Date checkoutTime;
    private Long customerId;
    private Long id;
    private Long type;
    private String name;
    private String typeName, statusName;
    private Long membershipId;
    private Membership membership;
    private GroupMembership groupMembership;
    private ClientUsePack clientUsePack;
    private CatGroupPack catGroupPack;
    private Long groupPackId;
    private String groupPackName;

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

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECK_TIME", length = 11, updatable = false)
    public Date getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Date checkTime)
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

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECKOUT_TIME", length = 11, updatable = false)
    public Date getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime)
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

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    @Column(name = "GROUP_PACK_ID", precision = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Basic
    @Column(name = "GROUP_PACK_NAME", nullable = true, length = 50)
    public String getGroupPackName()
    {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName)
    {
        this.groupPackName = groupPackName;
    }

    @Transient
    public Membership getMembership()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type) && groupMemberId == null)
        {
            try
            {
                membership = new GenericDaoImplNewV2<Membership, Long>()
                {
                }.findById(membershipId);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return membership;
    }

    public void setMembership(Membership membership)
    {
        this.membership = membership;
    }

    @Transient
    public GroupMembership getGroupMembership()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type) && groupMemberId != null)
        {
            try
            {
                groupMembership = new GenericDaoImplNewV2<GroupMembership, Long>()
                {
                }.findById(membershipId);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return groupMembership;
    }

    public void setGroupMembership(GroupMembership groupMembership)
    {
        this.groupMembership = groupMembership;
    }

    @Transient
    public ClientUsePack getClientUsePack()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(type))
        {
            try
            {
                clientUsePack = new GenericDaoImplNewV2<ClientUsePack, Long>()
                {
                }.findById(membershipId);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return clientUsePack;
    }

    public void setClientUsePack(ClientUsePack clientUsePack)
    {
        this.clientUsePack = clientUsePack;
    }

    @Transient
    public CatGroupPack getCatGroupPack()
    {
        if (catGroupPack == null)
        {
            if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type))
            {
                if (groupMemberId != null && getGroupMembership() != null)
                {
                    catGroupPack = groupMembership.getCatGroupPack();
                }
                else if (getMembership() != null)
                {
                    catGroupPack = membership.getGroupPack();
                }
            }
            else if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(type) && getClientUsePack() != null)
            {
                catGroupPack = clientUsePack.getGroupPack();
            }
        }
        return catGroupPack;
    }

    public void setCatGroupPack(CatGroupPack catGroupPack)
    {
        this.catGroupPack = catGroupPack;
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

    public V_CustomerCheckin()
    {
    }
}
