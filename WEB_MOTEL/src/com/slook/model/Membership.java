package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
public class Membership
{

    private Long membershipId;
    private Long memberId;
    private Long memberGrantorId;
    private Long memberAssignId;
    private Long groupPackId;
    CatGroupPack groupPack;
    private Date joinDate;
    private Date endDate;
    private Long status;
    private Date freezeTime;

    private Long customerScheduleId;
    private Long numberPack;
    private Long available;
    private Long usedNumber;
    private Long trainerId;
    private Employee trainer;

    @Id
    @Column(name = "MEMBERSHIP_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBERSHIP_SEQ", allocationSize = 1)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
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
    @Column(name = "GROUP_PACK_ID", nullable = true, precision = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long packId)
    {
        this.groupPackId = packId;
    }

    @ManyToOne
    @JoinColumn(name = "GROUP_PACK_ID", insertable = false, updatable = false)
    public CatGroupPack getGroupPack()
    {
        return groupPack;
    }

    public void setGroupPack(CatGroupPack pack)
    {
        this.groupPack = pack;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JOIN_DATE", nullable = true)
    public Date getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, precision = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "MEMBER_GRANTOR_ID")
    public Long getMemberGrantorId()
    {
        return memberGrantorId;
    }

    public void setMemberGrantorId(Long memberInheritId)
    {
        this.memberGrantorId = memberInheritId;
    }

    @Column(name = "MEMBER_ASSIGNER_ID")
    public Long getMemberAssignId()
    {
        return memberAssignId;
    }

    public void setMemberAssignId(Long memberAssignId)
    {
        this.memberAssignId = memberAssignId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "freeze_time", nullable = true)
    public Date getFreezeTime()
    {
        return freezeTime;
    }

    public void setFreezeTime(Date freezeTime)
    {
        this.freezeTime = freezeTime;
    }

    @Column(name = "customer_schedule_id")
    public Long getCustomerScheduleId()
    {
        return customerScheduleId;
    }

    public void setCustomerScheduleId(Long customerScheduleId)
    {
        this.customerScheduleId = customerScheduleId;
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

        Membership that = (Membership) o;

        if (membershipId != null ? !membershipId.equals(that.membershipId) : that.membershipId != null)
        {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null)
        {
            return false;
        }
        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null)
        {
            return false;
        }
        if (joinDate != null ? !joinDate.equals(that.joinDate) : that.joinDate != null)
        {
            return false;
        }
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null)
        {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = membershipId != null ? membershipId.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (groupPackId != null ? groupPackId.hashCode() : 0);
        result = 31 * result + (joinDate != null ? joinDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    boolean addPromotion = false;

    @Transient
    public boolean getAddPromotion()
    {
        return addPromotion;
    }

    public void setAddPromotion(boolean addPromotion)
    {
        this.addPromotion = addPromotion;
    }

    @Column(name = "number_pack", precision = 22, scale = 0)
    public Long getNumberPack()
    {
        return numberPack;
    }

    public void setNumberPack(Long numberPack)
    {
        this.numberPack = numberPack;
    }

    @Column(name = "available", precision = 22, scale = 0)
    public Long getAvailable()
    {
        return available;
    }

    public void setAvailable(Long available)
    {
        this.available = available;
    }

    @Column(name = "USED_NUMBER", precision = 22, scale = 0)
    public Long getUsedNumber()
    {
        return usedNumber;
    }

    public void setUsedNumber(Long usedNumber)
    {
        this.usedNumber = usedNumber;
    }

    @Column(name = "TRAINER_ID", precision = 22, scale = 0)
    public Long getTrainerId()
    {
        return trainerId;
    }

    public void setTrainerId(Long trainerId)
    {
        this.trainerId = trainerId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAINER_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getTrainer()
    {
        return trainer;
    }

    public void setTrainer(Employee trainer)
    {
        this.trainer = trainer;
    }

}
