/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import com.slook.persistence.GenericDaoImplNewV2;
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
import javax.persistence.Transient;
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
    private Long billId;
    @Basic(optional = false)
    @Column(name = "BILL_CODE")
    private String billCode;
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Column(name = "HOME_ID")
    private Long homeId;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "TOTAL_PRICE")
    private Long totalPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billId")
    private List<BillService> billServiceList;
    @JoinColumn(name = "HOME_ID", referencedColumnName = "HOME_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Home home;
//    @Column(name = "MONTH")
//    @Temporal(TemporalType.DATE)
//    private Date month;
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Room room;
    @Column(name = "CONTRACT_ID")
    private Long contractId;
    @Transient
    public String contractCode;

    public Bill() {
    }

    public Bill(Long billId) {
        this.billId = billId;
    }

    public Bill(Long billId, String billCode) {
        this.billId = billId;
        this.billCode = billCode;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
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

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    @XmlTransient
    public List<BillService> getBillServiceList() {
        return billServiceList;
    }

    public void setBillServiceList(List<BillService> billServiceList) {
        this.billServiceList = billServiceList;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
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

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }
//
//    public Date getMonth() {
//        return month;
//    }
//
//    public void setMonth(Date month) {
//        this.month = month;
//    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void createBillCode(Long groupUseId) {

//        String hqlCheckCode = "select count(*) from MemberPayment where paymentCode like ?||'%'";
//        String hqlCheckCode = "select max(CONVERT( substr(CONTRACT_CODE,-5),UNSIGNED INTEGER)) from contract where CONTRACT_CODE like CONCAT(?,'%')";
        String hqlCheckCode = "select max(CONVERT( substr(BILL_CODE,INSTR(BILL_CODE, '-')+1),UNSIGNED INTEGER)) from BILL where BILL_CODE like CONCAT(?,'%')";
        String code = "SP";
        String barch = "000";
        if (groupUseId != null) {
            if (groupUseId.toString().length() < 3) {
                barch = barch + groupUseId.toString();
                code += barch.substring(barch.length() - 3);
            } else {
                code += groupUseId.toString();
            }
        } else {
            code += barch;
        }

        List<?> counts = new GenericDaoImplNewV2<Contract, Long>() {
        }.findListSQLAll(hqlCheckCode, code);
        String numberStr = "00000";
        if (counts.size() > 0 && counts.get(0) != null) {
            Long number = (Long.valueOf(counts.get(0).toString()) + 1);
            numberStr += number;
            numberStr = numberStr.substring(numberStr.length() - 5);
        } else {
            numberStr = "00001";
        }
        billCode = code + "-" + numberStr;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

}
