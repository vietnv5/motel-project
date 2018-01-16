/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import java.io.Serializable;
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
    private Integer billServiceId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "PRICE")
    private Integer price;
    @Column(name = "TOTAL_PRICE")
    private Integer totalPrice;
    @Column(name = "STATUS")
    private Integer status;
    @JoinColumn(name = "BILL_ID", referencedColumnName = "BILL_ID")
    @ManyToOne(optional = false)
    private Bill billId;
    @JoinColumn(name = "SERVICE_ID", referencedColumnName = "SERVICE_ID")
    @ManyToOne
    private Service serviceId;

    public BillService() {
    }

    public BillService(Integer billServiceId) {
        this.billServiceId = billServiceId;
    }

    public Integer getBillServiceId() {
        return billServiceId;
    }

    public void setBillServiceId(Integer billServiceId) {
        this.billServiceId = billServiceId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Bill getBillId() {
        return billId;
    }

    public void setBillId(Bill billId) {
        this.billId = billId;
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
    
}
