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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "electric_water")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ElectricWater.findAll", query = "SELECT e FROM ElectricWater e")
    , @NamedQuery(name = "ElectricWater.findByElectricWaterId", query = "SELECT e FROM ElectricWater e WHERE e.electricWaterId = :electricWaterId")
    , @NamedQuery(name = "ElectricWater.findByElectricOld", query = "SELECT e FROM ElectricWater e WHERE e.electricOld = :electricOld")
    , @NamedQuery(name = "ElectricWater.findByWaterOld", query = "SELECT e FROM ElectricWater e WHERE e.waterOld = :waterOld")
    , @NamedQuery(name = "ElectricWater.findByElectricNew", query = "SELECT e FROM ElectricWater e WHERE e.electricNew = :electricNew")
    , @NamedQuery(name = "ElectricWater.findByWaterNew", query = "SELECT e FROM ElectricWater e WHERE e.waterNew = :waterNew")
    , @NamedQuery(name = "ElectricWater.findByCreateTime", query = "SELECT e FROM ElectricWater e WHERE e.createTime = :createTime")
    , @NamedQuery(name = "ElectricWater.findByTimeLine", query = "SELECT e FROM ElectricWater e WHERE e.timeLine = :timeLine")
    , @NamedQuery(name = "ElectricWater.findByStatus", query = "SELECT e FROM ElectricWater e WHERE e.status = :status")
    , @NamedQuery(name = "ElectricWater.findByMonth", query = "SELECT e FROM ElectricWater e WHERE e.month = :month")})
public class ElectricWater implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "electric_water_id")
    private Long electricWaterId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ELECTRIC_OLD")
    private Double electricOld;
    @Column(name = "WATER_OLD")
    private Double waterOld;
    @Column(name = "ELECTRIC_NEW")
    private Double electricNew;
    @Column(name = "WATER_NEW")
    private Double waterNew;
    @Column(name = "CREATE_TIME")//thoi gian tao
    @Temporal(TemporalType.DATE)
    private Date createTime;
    @Column(name = "TIME_LINE")//thoi gian chot dien
    @Temporal(TemporalType.DATE)
    private Date timeLine;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "MONTH")
    @Temporal(TemporalType.DATE)
    private Date month;
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Room room;
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Transient
    private Long homeId;

    public ElectricWater() {
    }

    public ElectricWater(Long electricWaterId) {
        this.electricWaterId = electricWaterId;
    }

    public Long getElectricWaterId() {
        return electricWaterId;
    }

    public void setElectricWaterId(Long electricWaterId) {
        this.electricWaterId = electricWaterId;
    }

    public Double getElectricOld() {
        return electricOld;
    }

    public void setElectricOld(Double electricOld) {
        this.electricOld = electricOld;
    }

    public Double getWaterOld() {
        return waterOld;
    }

    public void setWaterOld(Double waterOld) {
        this.waterOld = waterOld;
    }

    public Double getElectricNew() {
        return electricNew;
    }

    public void setElectricNew(Double electricNew) {
        this.electricNew = electricNew;
    }

    public Double getWaterNew() {
        return waterNew;
    }

    public void setWaterNew(Double waterNew) {
        this.waterNew = waterNew;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(Date timeLine) {
        this.timeLine = timeLine;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (electricWaterId != null ? electricWaterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ElectricWater)) {
            return false;
        }
        ElectricWater other = (ElectricWater) object;
        if ((this.electricWaterId == null && other.electricWaterId != null) || (this.electricWaterId != null && !this.electricWaterId.equals(other.electricWaterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ElectricWater[ electricWaterId=" + electricWaterId + " ]";
    }
    
}
