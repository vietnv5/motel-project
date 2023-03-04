/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Transient;

@Entity
@Table(name = "CUSTOMER_SCHEDULE_PACK")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerSchedulePack
{

    private Long customerSchedulePackId;
    private Long customerScheduleId;
    private Long groupPackId;
    private Long membershipId;
    private CatGroupPack catGroupPack;


    @SequenceGenerator(name = "generator", sequenceName = "CUSTOMER_SCHEDULE_PACK_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CUSTOMER_SCHEDULE_PACK_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getCustomerSchedulePackId()
    {
        return customerSchedulePackId;
    }

    public void setCustomerSchedulePackId(Long customerSchedulePackId)
    {
        this.customerSchedulePackId = customerSchedulePackId;
    }

    @Column(name = "CUSTOMER_SCHEDULE_ID", precision = 22, scale = 0)
    public Long getCustomerScheduleId()
    {
        return customerScheduleId;
    }

    public void setCustomerScheduleId(Long customerScheduleId)
    {
        this.customerScheduleId = customerScheduleId;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    public CustomerSchedulePack()
    {
    }

    public CustomerSchedulePack(Long customerSchedulePackId)
    {
        this.customerSchedulePackId = customerSchedulePackId;
    }

    public CustomerSchedulePack(Long customerSchedulePackId, Long customerScheduleId, Long groupPackId, Long membershipId, CatGroupPack catGroupPack)
    {
        this.customerSchedulePackId = customerSchedulePackId;
        this.customerScheduleId = customerScheduleId;
        this.groupPackId = groupPackId;
        this.membershipId = membershipId;
        this.catGroupPack = catGroupPack;
    }


    @Transient
    public CatGroupPack getCatGroupPack()
    {
        if (groupPackId != null && catGroupPack == null)
        {
            try
            {
                catGroupPack = new GenericDaoImplNewV2<CatGroupPack, Long>()
                {
                }.findById(groupPackId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return catGroupPack;
    }

    public void setCatGroupPack(CatGroupPack catGroupPack)
    {
        this.catGroupPack = catGroupPack;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CustomerSchedulePack other = (CustomerSchedulePack) obj;
        if (!Objects.equals(this.customerScheduleId, other.customerScheduleId))
        {
            return false;
        }
        if (!Objects.equals(this.groupPackId, other.groupPackId))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.customerScheduleId);
        hash = 79 * hash + Objects.hashCode(this.groupPackId);
        return hash;
    }

}
