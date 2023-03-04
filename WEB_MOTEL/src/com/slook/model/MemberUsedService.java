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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "MEMBER_USED_SERVICE")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class MemberUsedService
{

    private Long available;
    private Long membershipId;
    private Long memberId;
    private Long serviceId;
    private Date endDate;
    private Date startDate;
    private Long id;
    private Date createTime;
    private Long status;
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

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    @Column(name = "MEMBER_ID", precision = 22, scale = 0)
    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
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

    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_USED_SERVICE_SEQ", allocationSize = 1)
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

    public MemberUsedService()
    {
    }

    public MemberUsedService(Long id)
    {
        this.id = id;
    }

    @Column(name = "status", precision = 22, scale = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
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

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
