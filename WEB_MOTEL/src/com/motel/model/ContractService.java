/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author VietNV
 */
@Entity
@Table(name = "contract_service")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "ContractService.findAll", query = "SELECT c FROM ContractService c")
        , @NamedQuery(name = "ContractService.findByContractServiceId", query = "SELECT c FROM ContractService c WHERE c.contractServiceId = :contractServiceId")
        , @NamedQuery(name = "ContractService.findByInsertTime", query = "SELECT c FROM ContractService c WHERE c.insertTime = :insertTime")})
public class ContractService implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACT_SERVICE_ID")
    private Long contractServiceId;
    @Column(name = "INSERT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertTime;
    @JoinColumn(name = "CONTRACT_ID", referencedColumnName = "CONTRACT_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Contract contract;
    @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CatService service;
    @Column(name = "SERVICE_ID")
    private Long serviceId;
    @Column(name = "CONTRACT_ID")
    private Long contractId;


    public ContractService()
    {
    }

    public ContractService(Long contractServiceId)
    {
        this.contractServiceId = contractServiceId;
    }

    public Long getContractServiceId()
    {
        return contractServiceId;
    }

    public void setContractServiceId(Long contractServiceId)
    {
        this.contractServiceId = contractServiceId;
    }

    public Date getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(Date insertTime)
    {
        this.insertTime = insertTime;
    }

    public Contract getContract()
    {
        return contract;
    }

    public void setContract(Contract contract)
    {
        this.contract = contract;
    }

    public CatService getService()
    {
        return service;
    }

    public void setService(CatService service)
    {
        this.service = service;
    }

    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
    }

    public Long getContractId()
    {
        return contractId;
    }

    public void setContractId(Long contractId)
    {
        this.contractId = contractId;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.serviceId);
        hash = 53 * hash + Objects.hashCode(this.contractId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ContractService other = (ContractService) obj;
        if (!Objects.equals(this.serviceId, other.serviceId))
        {
            return false;
        }
        if (!Objects.equals(this.contractId, other.contractId))
        {
            return false;
        }
        return true;
    }


    @Override
    public String toString()
    {
        return "model.ContractService[ contractServiceId=" + contractServiceId + " ]";
    }

    public ContractService(Long serviceId, Long contractId)
    {
        this.serviceId = serviceId;
        this.contractId = contractId;
        this.insertTime = new Date();
    }

}
