/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "bill_service")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BillService.findAll", query = "SELECT b FROM BillService b")
    , @NamedQuery(name = "BillService.findByBillServiceId", query = "SELECT b FROM BillService b WHERE b.billServiceId = :billServiceId")
    , @NamedQuery(name = "BillService.findByAmount", query = "SELECT b FROM BillService b WHERE b.amount = :amount")
    , @NamedQuery(name = "BillService.findByPrice", query = "SELECT b FROM BillService b WHERE b.price = :price")
    , @NamedQuery(name = "BillService.findByTotalPrice", query = "SELECT b FROM BillService b WHERE b.totalPrice = :totalPrice")
    , @NamedQuery(name = "BillService.findByStatus", query = "SELECT b FROM BillService b WHERE b.status = :status")})
public class BillService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BILL_SERVICE_ID")
    private Long billServiceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "TOTAL_PRICE")
    private Long totalPrice;
    @Column(name = "STATUS")
    private Long status;
    @JoinColumn(name = "BILL_ID", referencedColumnName = "BILL_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Bill bill;
    
    @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CatService service;

    @Column(name = "BILL_ID")
    private Long billId;
    @Column(name = "SERVICE_ID")
    private Long serviceId;
    @Column(name = "index_old")
    private Double indexOld;
    @Column(name = "index_new")
    private Double indexNew;
    @Transient
    private String rowKey;
    public BillService() {
    }

    public BillService(Long billServiceId) {
        this.billServiceId = billServiceId;
    }

    public Long getBillServiceId() {
        return billServiceId;
    }

    public void setBillServiceId(Long billServiceId) {
        this.billServiceId = billServiceId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public CatService getService() {
        return service;
    }

    public void setService(CatService service) {
        this.service = service;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billServiceId != null ? billServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillService)) {
            return false;
        }
        BillService other = (BillService) object;
        if ((this.billServiceId == null && other.billServiceId != null) || (this.billServiceId != null && !this.billServiceId.equals(other.billServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.BillService[ billServiceId=" + billServiceId + " ]";
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Double getIndexOld() {
        return indexOld;
    }

    public void setIndexOld(Double indexOld) {
        this.indexOld = indexOld;
    }

    public Double getIndexNew() {
        return indexNew;
    }

    public void setIndexNew(Double indexNew) {
        this.indexNew = indexNew;
    }

    public String getRowKey() {
        rowKey="";
        if(billId!=null)rowKey+=billId;
        rowKey+="_";
        if(serviceId!=null)rowKey+=serviceId;
        
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

}
