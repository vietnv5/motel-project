/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import java.util.Date;
import java.util.List;
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
@Table(name = "CLIENT")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class Client {

    private Long status;
    private Date startTime;
    private Date realEndTime;
    private Date endTime;
    private String cardCode;
    private Long clientId;
    private String cmt;
    private String phoneNumber;
    private String name;
    private String sex;
    private Date birthDay;
    private Long branchId;

    private CatBranch branch;

    private List<ClientUsePack> clientUsePacks = new ArrayList<>();
    List<ClientPayment> clientPayments = new ArrayList<>(0);
    List<ClientPromotion> clientPromotions = new ArrayList<>(0);

    private ClientPayment clientPayment = new ClientPayment();
    private ClientPromotion clientPromotion = new ClientPromotion();
    private ClientUsePack newClientUsePack = new ClientUsePack();

    private Long totalPayment;
    private String statusName;
    private Long employeeId;
    private Employee employee;
    private Long timesUsed;
    
    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME", length = 11)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "REAL_END_TIME", length = 11)
    public Date getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME", length = 11)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "CARD_CODE", length = 50)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CLIENT_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CLIENT_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Column(name = "CMT", length = 50)
    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    @Column(name = "PHONE_NUMBER", length = 50)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "NAME", length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "SEX", nullable = true, length = 20)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_DAY", nullable = true)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Basic
    @Column(name = "BRANCH_ID", nullable = true, precision = 0)
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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

    public Client() {
    }

    public Client(Long clientId) {
        this.clientId = clientId;
    }

    @Transient
    public List<ClientUsePack> getClientUsePacks() {
        return clientUsePacks;
    }

    public void setClientUsePacks(List<ClientUsePack> clientUsePacks) {
        this.clientUsePacks = clientUsePacks;
    }

    @Transient
    public List<ClientPayment> getClientPayments() {
        return clientPayments;
    }

    public void setClientPayments(List<ClientPayment> clientPayments) {
        this.clientPayments = clientPayments;
    }

    @Transient
    public List<ClientPromotion> getClientPromotions() {
        return clientPromotions;
    }

    public void setClientPromotions(List<ClientPromotion> clientPromotions) {
        this.clientPromotions = clientPromotions;
    }

    @Transient
    public ClientPayment getClientPayment() {
        return clientPayment;
    }

    public void setClientPayment(ClientPayment clientPayment) {
        this.clientPayment = clientPayment;
    }

    @Transient
    public ClientPromotion getClientPromotion() {
        return clientPromotion;
    }

    public void setClientPromotion(ClientPromotion clientPromotion) {
        this.clientPromotion = clientPromotion;
    }

    @Transient
    public ClientUsePack getNewClientUsePack() {
        return newClientUsePack;
    }

    public void setNewClientUsePack(ClientUsePack newClientUsePack) {
        this.newClientUsePack = newClientUsePack;
    }

    @Transient
    public Long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Long totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Transient
    public String getStatusName() {
        if (Constant.CLIENT_STATUS.NEW.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status0");
        } else if (Constant.CLIENT_STATUS.ACTIVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status1");
        } else if (Constant.CLIENT_STATUS.STOP.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status2");
        } else if (Constant.CLIENT_STATUS.RESERVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("client.status3");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    @Column(name = "EMPLOYEE_ID", nullable = true, precision = 0)
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
        @Column(name = "times_used", precision = 22, scale = 0)
    public Long getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(Long timesUsed) {
        this.timesUsed = timesUsed;
    }

}
