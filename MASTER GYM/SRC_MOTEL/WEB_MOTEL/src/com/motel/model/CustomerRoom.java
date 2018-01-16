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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "customer_room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerRoom.findAll", query = "SELECT c FROM CustomerRoom c")
    , @NamedQuery(name = "CustomerRoom.findByCustomerRoomId", query = "SELECT c FROM CustomerRoom c WHERE c.customerRoomId = :customerRoomId")
    , @NamedQuery(name = "CustomerRoom.findByStartTime", query = "SELECT c FROM CustomerRoom c WHERE c.startTime = :startTime")
    , @NamedQuery(name = "CustomerRoom.findByEndTime", query = "SELECT c FROM CustomerRoom c WHERE c.endTime = :endTime")
    , @NamedQuery(name = "CustomerRoom.findByStatus", query = "SELECT c FROM CustomerRoom c WHERE c.status = :status")
    , @NamedQuery(name = "CustomerRoom.findByCreateTime", query = "SELECT c FROM CustomerRoom c WHERE c.createTime = :createTime")
    , @NamedQuery(name = "CustomerRoom.findByType", query = "SELECT c FROM CustomerRoom c WHERE c.type = :type")})
public class CustomerRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CUSTOMER_ROOM_ID")
    private Integer customerRoomId;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.DATE)
    private Date endTime;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "TYPE")
    private Integer type;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID")
    @ManyToOne(optional = false)
    private Room roomId;

    public CustomerRoom() {
    }

    public CustomerRoom(Integer customerRoomId) {
        this.customerRoomId = customerRoomId;
    }

    public Integer getCustomerRoomId() {
        return customerRoomId;
    }

    public void setCustomerRoomId(Integer customerRoomId) {
        this.customerRoomId = customerRoomId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerRoomId != null ? customerRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerRoom)) {
            return false;
        }
        CustomerRoom other = (CustomerRoom) object;
        if ((this.customerRoomId == null && other.customerRoomId != null) || (this.customerRoomId != null && !this.customerRoomId.equals(other.customerRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CustomerRoom[ customerRoomId=" + customerRoomId + " ]";
    }
    
}
