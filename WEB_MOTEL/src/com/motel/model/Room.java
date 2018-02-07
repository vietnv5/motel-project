/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Room.findAll", query = "SELECT r FROM Room r")
    , @NamedQuery(name = "Room.findByRoomId", query = "SELECT r FROM Room r WHERE r.roomId = :roomId")
    , @NamedQuery(name = "Room.findByRoomName", query = "SELECT r FROM Room r WHERE r.roomName = :roomName")
    , @NamedQuery(name = "Room.findByPrice", query = "SELECT r FROM Room r WHERE r.price = :price")
    , @NamedQuery(name = "Room.findByStatus", query = "SELECT r FROM Room r WHERE r.status = :status")})
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Column(name = "ROOM_NAME")
    private String roomName;
    @Column(name = "PRICE")
    private Long price;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "HOME_ID")
    private Long homeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private List<CustomerRoom> customerRoomList;
    @JoinColumn(name = "HOME_ID", referencedColumnName = "HOME_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Home home;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private List<ElectricWater> electricWaterList;

    @Transient
    private String statusName;

    @Transient
    private Contract currContract;

    public Room() {
    }

    public Room(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @XmlTransient
    public List<CustomerRoom> getCustomerRoomList() {
        return customerRoomList;
    }

    public void setCustomerRoomList(List<CustomerRoom> customerRoomList) {
        this.customerRoomList = customerRoomList;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public String getStatusName() {
        if (Constant.ROOM_STATUS.FREE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("room.status.FREE");
        } else if (Constant.ROOM_STATUS.USE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("room.status.USE");
        } else if (Constant.ROOM_STATUS.STOP.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("room.status.STOP");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @XmlTransient
    public List<ElectricWater> getElectricWaterList() {
        return electricWaterList;
    }

    public void setElectricWaterList(List<ElectricWater> electricWaterList) {
        this.electricWaterList = electricWaterList;
    }

    public Contract getCurrContract() {
        return currContract;
    }

    public void setCurrContract(Contract currContract) {
        this.currContract = currContract;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Room[ roomId=" + roomId + " ]";
    }

}
