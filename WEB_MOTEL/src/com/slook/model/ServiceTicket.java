/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "SERVICE_TICKET")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ServiceTicket {

    private Long status;
    private String serviceTicketCode;
    private Long membershipId;
    private Long serviceTicketId;
    private String cardCode;
    private Date createTime;
    private Date usedTime;
    private String statusName;

    private Long employeeId;
    private Employee employee;

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "SERVICE_TICKET_CODE", length = 255)
    public String getServiceTicketCode() {
        return serviceTicketCode;
    }

    public void setServiceTicketCode(String serviceTicketCode) {
        this.serviceTicketCode = serviceTicketCode;
    }

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "SERVICE_TICKET_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "SERVICE_TICKET_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getServiceTicketId() {
        return serviceTicketId;
    }

    public void setServiceTicketId(Long serviceTicketId) {
        this.serviceTicketId = serviceTicketId;
    }

    public ServiceTicket() {
    }

    public ServiceTicket(Long serviceTicketId) {
        this.serviceTicketId = serviceTicketId;
    }

    public ServiceTicket(Long status, String serviceTicketCode, Long membershipId) {
        this.status = status;
        this.serviceTicketCode = serviceTicketCode;
        this.membershipId = membershipId;
        this.createTime = new Date();
    }

    @Column(name = "CARD_CODE", length = 255)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "used_time", nullable = true)
    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
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

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0)
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
