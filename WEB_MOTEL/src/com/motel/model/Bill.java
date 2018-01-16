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
@Table(name = "bill")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bill.findAll", query = "SELECT b FROM Bill b")
    , @NamedQuery(name = "Bill.findByBillId", query = "SELECT b FROM Bill b WHERE b.billId = :billId")
    , @NamedQuery(name = "Bill.findByBillCode", query = "SELECT b FROM Bill b WHERE b.billCode = :billCode")
    , @NamedQuery(name = "Bill.findByRoomId", query = "SELECT b FROM Bill b WHERE b.roomId = :roomId")
    , @NamedQuery(name = "Bill.findByCreateTime", query = "SELECT b FROM Bill b WHERE b.createTime = :createTime")
    , @NamedQuery(name = "Bill.findByDescription", query = "SELECT b FROM Bill b WHERE b.description = :description")
    , @NamedQuery(name = "Bill.findByPaymentDate", query = "SELECT b FROM Bill b WHERE b.paymentDate = :paymentDate")
    , @NamedQuery(name = "Bill.findByTotalPrice", query = "SELECT b FROM Bill b WHERE b.totalPrice = :totalPrice")})
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BILL_ID")
    private Integer billId;
    @Basic(optional = false)
    @Column(name = "BILL_CODE")
    private String billCode;
    @Column(name = "ROOM_ID")
    private Integer roomId;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "TOTAL_PRICE")
    private Integer totalPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billId")
    private List<BillService> billServiceList;
    @JoinColumn(name = "HOME_ID", referencedColumnName = "HOME_ID")
    @ManyToOne(optional = false)
    private Home homeId;

    public Bill() {
    }

    public Bill(Integer billId) {
        this.billId = billId;
    }

    public Bill(Integer billId, String billCode) {
        this.billId = billId;
        this.billCode = billCode;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    @XmlTransient
    public List<BillService> getBillServiceList() {
        return billServiceList;
    }

    public void setBillServiceList(List<BillService> billServiceList) {
        this.billServiceList = billServiceList;
    }

    public Home getHomeId() {
        return homeId;
    }

    public void setHomeId(Home homeId) {
        this.homeId = homeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billId != null ? billId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bill)) {
            return false;
        }
        Bill other = (Bill) object;
        if ((this.billId == null && other.billId != null) || (this.billId != null && !this.billId.equals(other.billId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Bill[ billId=" + billId + " ]";
    }
    
}
