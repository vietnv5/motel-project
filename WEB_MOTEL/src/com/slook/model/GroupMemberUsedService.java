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
@Table(name = "GROUP_MEMBER_USED_SERVICE")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class GroupMemberUsedService
{

    private Long available;
    private Long groupMemberId;
    private Long status;
    private Long groupMembershipId;
    private Long serviceId;
    private Date endDate;
    private Date startDate;
    private Long id;
    private Date createTime;

    private Long totalNumber;
    private Long usedNumber;

    private CatServiceOld catService;

    @Column(name = "AVAILABLE", precision = 22, scale = 0)
    public Long getAvailable()
    {
        return available;
    }

    public void setAvailable(Long available)
    {
        this.available = available;
    }

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

    @Column(name = "GROUP_MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getGroupMembershipId()
    {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId)
    {
        this.groupMembershipId = groupMembershipId;
    }

    @Column(name = "SERVICE_ID", precision = 22, scale = 0)
    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", length = 7)
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_MEMBER_USED_SERVICE_SEQ", allocationSize = 1)
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

    @Column(name = "USED_NUMBER", precision = 22, scale = 0)
    public Long getUsedNumber()
    {
        return usedNumber;
    }

    public void setUsedNumber(Long usedNumber)
    {
        this.usedNumber = usedNumber;
    }

    @Column(name = "TOTAL_NUMBER", precision = 22, scale = 0)
    public Long getTotalNumber()
    {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber)
    {
        this.totalNumber = totalNumber;
    }

    @ManyToOne
    @JoinColumn(name = "SERVICE_ID", insertable = false, updatable = false)
    public CatServiceOld getCatService()
    {
        return catService;
    }

    public void setCatService(CatServiceOld catService)
    {
        this.catService = catService;
    }

    public GroupMemberUsedService()
    {
    }

    public GroupMemberUsedService(Long id)
    {
        this.id = id;
    }
}
