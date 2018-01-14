/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "V_SERVICE_TICKET")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class V_ServiceTicket {

    private Long employeeId;
    private Date joinDate;
    private String cardCode;
    private String groupPackName;
    private String serviceTicketCode;
    private Long membershipId;
    private Date endDate;
    private String employeeName;
    private Long serviceTicketId;
    private String employeeCode;
    private Long status;
    private Long groupPackId;
    private Date createTime;
    private Date usedTime;
    private String statusName;

    private Long memberId;
    private String memberCode;
    private String memberName;
    private String phoneNumber;
    private Long branchId;
    private CatBranch branch;

    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0, updatable = false)
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7, updatable = false)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Column(name = "CARD_CODE", length = 100, updatable = false)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Column(name = "SERVICE_TICKET_CODE", length = 255, updatable = false)
    public String getServiceTicketCode() {
        return serviceTicketCode;
    }

    public void setServiceTicketCode(String serviceTicketCode) {
        this.serviceTicketCode = serviceTicketCode;
    }

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7, updatable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "EMPLOYEE_NAME", length = 1020, updatable = false)
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Column(name = "SERVICE_TICKET_ID", precision = 22, scale = 0, updatable = false)
    @Id
    public Long getServiceTicketId() {
        return serviceTicketId;
    }

    public void setServiceTicketId(Long serviceTicketId) {
        this.serviceTicketId = serviceTicketId;
    }

    @Column(name = "EMPLOYEE_CODE", length = 1020, updatable = false)
    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    @Column(name = "STATUS", precision = 22, scale = 0, updatable = false)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME", length = 11, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "USED_TIME", length = 11, updatable = false)
    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public V_ServiceTicket() {
    }

    @Transient
    public String getStatusName() {
        if (Constant.SERVICE_TICKET.STATUS_NEW.equals(status)) {
            statusName = "Chưa sử dụng";
        }
        if (Constant.SERVICE_TICKET.STATUS_USED.equals(status)) {
            statusName = "Đã sử dụng";
        }
        return statusName;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "BRANCH_ID", insertable = false, updatable = false)
    public CatBranch getBranch() {
        return branch;
    }

    public void setBranch(CatBranch branch) {
        this.branch = branch;
    }

    @Column(name = "MEMBER_ID", nullable = false, precision = 0)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Column(name = "MEMBER_CODE", nullable = true, length = 50)
    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    @Column(name = "MEMBER_NAME", nullable = true, length = 200)
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    @Basic
    @Column(name = "PHONE_NUMBER", nullable = true, length = 30)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Basic
    @Column(name = "BRANCH_ID", nullable = true, precision = 0)
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    
}
