/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CLIENT_USE_PACK")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ClientUsePack
{

    private Long status;
    private Long clientUsePackId;
    private Date joinDate;
    private Long clientId;
    private Long groupPackId;
    private Date endDate;
    CatGroupPack groupPack;
    private Long customerScheduleId;
    private Long numberPack;
    private Long timesUsed;

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CLIENT_USE_PACK_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CLIENT_USE_PACK_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getClientUsePackId()
    {
        return clientUsePackId;
    }

    public void setClientUsePackId(Long clientUsePackId)
    {
        this.clientUsePackId = clientUsePackId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7)
    public Date getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
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

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7)
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public ClientUsePack()
    {
    }

    public ClientUsePack(Long clientUsePackId)
    {
        this.clientUsePackId = clientUsePackId;
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

    @Column(name = "customer_schedule_id")
    public Long getCustomerScheduleId()
    {
        return customerScheduleId;
    }

    public void setCustomerScheduleId(Long customerScheduleId)
    {
        this.customerScheduleId = customerScheduleId;
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

    @Column(name = "times_used", precision = 22, scale = 0)
    public Long getTimesUsed()
    {
        return timesUsed;
    }

    public void setTimesUsed(Long timesUsed)
    {
        this.timesUsed = timesUsed;
    }


}
