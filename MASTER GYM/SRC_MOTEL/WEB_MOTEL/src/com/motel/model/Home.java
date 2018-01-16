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
@Table(name = "home")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Home.findAll", query = "SELECT h FROM Home h")
    , @NamedQuery(name = "Home.findByHomeId", query = "SELECT h FROM Home h WHERE h.homeId = :homeId")
    , @NamedQuery(name = "Home.findByHomeName", query = "SELECT h FROM Home h WHERE h.homeName = :homeName")
    , @NamedQuery(name = "Home.findByAddress", query = "SELECT h FROM Home h WHERE h.address = :address")
    , @NamedQuery(name = "Home.findByStatus", query = "SELECT h FROM Home h WHERE h.status = :status")
    , @NamedQuery(name = "Home.findByGroupUserId", query = "SELECT h FROM Home h WHERE h.groupUserId = :groupUserId")
    , @NamedQuery(name = "Home.findByDescription", query = "SELECT h FROM Home h WHERE h.description = :description")
    , @NamedQuery(name = "Home.findByHomeCode", query = "SELECT h FROM Home h WHERE h.homeCode = :homeCode")})
public class Home implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HOME_ID")
    private Integer homeId;
    @Column(name = "HOME_NAME")
    private String homeName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "GROUP_USER_ID")
    private Integer groupUserId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "HOME_CODE")
    private String homeCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "homeId")
    private List<Bill> billList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "homeId")
    private List<Room> roomList;

    public Home() {
    }

    public Home(Integer homeId) {
        this.homeId = homeId;
    }

    public Integer getHomeId() {
        return homeId;
    }

    public void setHomeId(Integer homeId) {
        this.homeId = homeId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Integer groupUserId) {
        this.groupUserId = groupUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeCode() {
        return homeCode;
    }

    public void setHomeCode(String homeCode) {
        this.homeCode = homeCode;
    }

    @XmlTransient
    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    @XmlTransient
    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (homeId != null ? homeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Home)) {
            return false;
        }
        Home other = (Home) object;
        if ((this.homeId == null && other.homeId != null) || (this.homeId != null && !this.homeId.equals(other.homeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Home[ homeId=" + homeId + " ]";
    }
    
}
