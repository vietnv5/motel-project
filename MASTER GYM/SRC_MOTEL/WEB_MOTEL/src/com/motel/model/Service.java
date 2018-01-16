/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s")
    , @NamedQuery(name = "Service.findByServiceId", query = "SELECT s FROM Service s WHERE s.serviceId = :serviceId")
    , @NamedQuery(name = "Service.findByServiceName", query = "SELECT s FROM Service s WHERE s.serviceName = :serviceName")
    , @NamedQuery(name = "Service.findByPrice", query = "SELECT s FROM Service s WHERE s.price = :price")
    , @NamedQuery(name = "Service.findByUnit", query = "SELECT s FROM Service s WHERE s.unit = :unit")
    , @NamedQuery(name = "Service.findByGroupUserId", query = "SELECT s FROM Service s WHERE s.groupUserId = :groupUserId")
    , @NamedQuery(name = "Service.findByDefaultStatus", query = "SELECT s FROM Service s WHERE s.defaultStatus = :defaultStatus")})
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SERVICE_ID")
    private Integer serviceId;
    @Column(name = "SERVICE_NAME")
    private Integer serviceName;
    @Column(name = "PRICE")
    private Integer price;
    @Column(name = "UNIT")
    private Integer unit;
    @Column(name = "GROUP_USER_ID")
    private Integer groupUserId;
    @Column(name = "DEFAULT_STATUS")
    private Integer defaultStatus;
    @OneToMany(mappedBy = "serviceId")
    private List<BillService> billServiceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceId")
    private List<ContractService> contractServiceList;

    public Service() {
    }

    public Service(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getServiceName() {
        return serviceName;
    }

    public void setServiceName(Integer serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Integer groupUserId) {
        this.groupUserId = groupUserId;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    @XmlTransient
    public List<BillService> getBillServiceList() {
        return billServiceList;
    }

    public void setBillServiceList(List<BillService> billServiceList) {
        this.billServiceList = billServiceList;
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
        hash += (serviceId != null ? serviceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.serviceId == null && other.serviceId != null) || (this.serviceId != null && !this.serviceId.equals(other.serviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Service[ serviceId=" + serviceId + " ]";
    }
    
}
