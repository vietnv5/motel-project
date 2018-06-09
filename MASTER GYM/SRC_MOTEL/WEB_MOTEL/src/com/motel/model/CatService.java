/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import com.slook.model.CatItemBO;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "service")
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s")
//    , @NamedQuery(name = "Service.findByServiceId", query = "SELECT s FROM Service s WHERE s.serviceId = :serviceId")
//    , @NamedQuery(name = "Service.findByServiceName", query = "SELECT s FROM Service s WHERE s.serviceName = :serviceName")
//    , @NamedQuery(name = "Service.findByPrice", query = "SELECT s FROM Service s WHERE s.price = :price")
//    , @NamedQuery(name = "Service.findByUnit", query = "SELECT s FROM Service s WHERE s.unit = :unit")
//    , @NamedQuery(name = "Service.findByGroupUserId", query = "SELECT s FROM Service s WHERE s.groupUserId = :groupUserId")
//    , @NamedQuery(name = "Service.findByDefaultStatus", query = "SELECT s FROM Service s WHERE s.defaultStatus = :defaultStatus")})
public class CatService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SERVICE_ID")
    private Long serviceId;
    @Column(name = "SERVICE_CODE")
    private String serviceCode;
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "UNIT")
    private Long unit;
    @Column(name = "GROUP_USER_ID")
    private Long groupUserId;
    @Column(name = "DEFAULT_STATUS")
    private Long defaultStatus;
    @OneToMany(mappedBy = "serviceId")
    private List<BillService> billServiceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceId")
    private List<ContractService> contractServiceList;
    @Column(name = "STATUS")
    private Long status;

    @Transient
    private CatItemBO unitBO;
    @Transient
    private String defaultStatusStr;
    
    public CatService() {
    }

    public CatService(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getUnit() {
        return unit;
    }

    public void setUnit(Long unit) {
        this.unit = unit;
    }

    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
    }

    public Long getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Long defaultStatus) {
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

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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
        if (!(object instanceof CatService)) {
            return false;
        }
        CatService other = (CatService) object;
        if ((this.serviceId == null && other.serviceId != null) || (this.serviceId != null && !this.serviceId.equals(other.serviceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Service[ serviceId=" + serviceId + " ]";
    }

    public CatItemBO getUnitBO() {
        return unitBO;
    }

    public void setUnitBO(CatItemBO unitBO) {
        this.unitBO = unitBO;
    }

    public String getDefaultStatusStr() {
          if (Constant.CONTRACT_SERVICE.DEFAULT_STATUS_ON.equals( defaultStatus)) {
            defaultStatusStr = MessageUtil.getResourceBundleMessage("catService.defaultStatus.YES");
        } else if (Constant.CONTRACT_SERVICE.DEFAULT_STATUS_OFF.equals( defaultStatus)) {
            defaultStatusStr = MessageUtil.getResourceBundleMessage("catService.defaultStatus.NO");
        }
        return defaultStatusStr;
    }

    public void setDefaultStatusStr(String defaultStatusStr) {
        this.defaultStatusStr = defaultStatusStr;
    }
    
}
