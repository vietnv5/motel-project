/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "contract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c")
    , @NamedQuery(name = "Contract.findByContractId", query = "SELECT c FROM Contract c WHERE c.contractId = :contractId")
    , @NamedQuery(name = "Contract.findByContractCode", query = "SELECT c FROM Contract c WHERE c.contractCode = :contractCode")
    , @NamedQuery(name = "Contract.findByHomeId", query = "SELECT c FROM Contract c WHERE c.homeId = :homeId")
    , @NamedQuery(name = "Contract.findByRoomId", query = "SELECT c FROM Contract c WHERE c.roomId = :roomId")
    , @NamedQuery(name = "Contract.findByStartTime", query = "SELECT c FROM Contract c WHERE c.startTime = :startTime")
    , @NamedQuery(name = "Contract.findByEndTime", query = "SELECT c FROM Contract c WHERE c.endTime = :endTime")
    , @NamedQuery(name = "Contract.findByDeposit", query = "SELECT c FROM Contract c WHERE c.deposit = :deposit")
    , @NamedQuery(name = "Contract.findByDescription", query = "SELECT c FROM Contract c WHERE c.description = :description")
    , @NamedQuery(name = "Contract.findByStatus", query = "SELECT c FROM Contract c WHERE c.status = :status")
    , @NamedQuery(name = "Contract.findByCreateTime", query = "SELECT c FROM Contract c WHERE c.createTime = :createTime")})
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONTRACT_ID")
    private Integer contractId;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Basic(optional = false)
    @Column(name = "HOME_ID")
    private int homeId;
    @Basic(optional = false)
    @Column(name = "ROOM_ID")
    private int roomId;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.DATE)
    private Date endTime;
    @Column(name = "DEPOSIT")
    private Integer deposit;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customer customerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractId")
    private List<ContractService> contractServiceList;

    public Contract() {
    }

    public Contract(Integer contractId) {
        this.contractId = contractId;
    }

    public Contract(Integer contractId, int homeId, int roomId) {
        this.contractId = contractId;
        this.homeId = homeId;
        this.roomId = roomId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    @XmlTransient
    public List<ContractService> getContractServiceList() {
        return contractServiceList;
    }

    public void setContractServiceList(List<ContractService> contractServiceList) {
        this.contractServiceList = contractServiceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractId != null ? contractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contract)) {
            return false;
        }
        Contract other = (Contract) object;
        if ((this.contractId == null && other.contractId != null) || (this.contractId != null && !this.contractId.equals(other.contractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Contract[ contractId=" + contractId + " ]";
    }
    
}
