/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "SERVICE_TICKET_EMP")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ServiceTicketEmp
{

    private Long employeeId;
    private Date createTime;
    private Long serviceTicketId;
    private Long serviceTicketEmpId;


    @Column(name = "EMPLOYEE_ID", precision = 22, scale = 0)
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
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

    @Column(name = "SERVICE_TICKET_ID", precision = 22, scale = 0)
    public Long getServiceTicketId()
    {
        return serviceTicketId;
    }

    public void setServiceTicketId(Long serviceTicketId)
    {
        this.serviceTicketId = serviceTicketId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "SERVICE_TICKET_EMP_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "SERVICE_TICKET_EMP_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getServiceTicketEmpId()
    {
        return serviceTicketEmpId;
    }

    public void setServiceTicketEmpId(Long serviceTicketEmpId)
    {
        this.serviceTicketEmpId = serviceTicketEmpId;
    }


    public ServiceTicketEmp()
    {
    }

    public ServiceTicketEmp(Long serviceTicketEmpId)
    {
        this.serviceTicketEmpId = serviceTicketEmpId;
    }
}