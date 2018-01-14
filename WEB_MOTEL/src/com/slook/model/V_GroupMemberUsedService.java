/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "V_GROUP_MEMBER_USED_SERVICE")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class V_GroupMemberUsedService {

    private String serviceName;
    private Long groupMemberId;
    private String groupPackName;
    private Date endDate;
    private Date startDate;
    private Long totalNumber;
    private Long available;
    private Long status;
    private Long groupMembershipId;
    private Long groupPackId;
    private Long usedNumber;
    private Long serviceId;
    private Long id;
    private Date createTime;
    private String statusName;

    @Column(name = "SERVICE_NAME", length = 50, updatable = false)
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7, updatable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", length = 7, updatable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "TOTAL_NUMBER", precision = 22, scale = 0, updatable = false)
    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    @Column(name = "AVAILABLE", precision = 22, scale = 0, updatable = false)
    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    @Column(name = "STATUS", precision = 22, scale = 0, updatable = false)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "GROUP_MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Column(name = "USED_NUMBER", precision = 22, scale = 0, updatable = false)
    public Long getUsedNumber() {
        return usedNumber;
    }

    public void setUsedNumber(Long usedNumber) {
        this.usedNumber = usedNumber;
    }

    @Column(name = "SERVICE_ID", precision = 22, scale = 0, updatable = false)
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Id
    @Column(name = "ID", precision = 22, scale = 0, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", length = 11, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Transient
    public String getStatusName() {
        if (status != null && status.equals(1L)) {
            statusName = "Hiệu lực";
        } else {
            statusName = "Hết hiệu lực";
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public V_GroupMemberUsedService() {
    }
}
