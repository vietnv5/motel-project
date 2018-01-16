/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "contract_service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContractService.findAll", query = "SELECT c FROM ContractService c")
    , @NamedQuery(name = "ContractService.findByContractServiceId", query = "SELECT c FROM ContractService c WHERE c.contractServiceId = :contractServiceId")
    , @NamedQuery(name = "ContractService.findByInsertTime", query = "SELECT c FROM ContractService c WHERE c.insertTime = :insertTime")})
public class ContractService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CONTRACT_SERVICE_ID")
    private Integer contractServiceId;
    @Column(name = "INSERT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertTime;
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "CONTRACT_ID")
    @ManyToOne(optional = false)
    private Contract contractId;
    @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
    @ManyToOne(optional = false)
    private Service serviceId;

    public ContractService() {
    }

    public ContractService(Integer contractServiceId) {
        this.contractServiceId = contractServiceId;
    }

    public Integer getContractServiceId() {
        return contractServiceId;
    }

    public void setContractServiceId(Integer contractServiceId) {
        this.contractServiceId = contractServiceId;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Contract getContractId() {
        return contractId;
    }

    public void setContractId(Contract contractId) {
        this.contractId = contractId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractServiceId != null ? contractServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContractService)) {
            return false;
        }
        ContractService other = (ContractService) object;
        if ((this.contractServiceId == null && other.contractServiceId != null) || (this.contractServiceId != null && !this.contractServiceId.equals(other.contractServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ContractService[ contractServiceId=" + contractServiceId + " ]";
    }
    
}
