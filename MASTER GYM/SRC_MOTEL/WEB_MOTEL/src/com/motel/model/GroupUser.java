/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author VietNV
 */
@Entity
@Table(name = "group_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupUser.findAll", query = "SELECT g FROM GroupUser g")
    , @NamedQuery(name = "GroupUser.findById", query = "SELECT g FROM GroupUser g WHERE g.id = :id")
    , @NamedQuery(name = "GroupUser.findByCode", query = "SELECT g FROM GroupUser g WHERE g.code = :code")
    , @NamedQuery(name = "GroupUser.findByName", query = "SELECT g FROM GroupUser g WHERE g.name = :name")
    , @NamedQuery(name = "GroupUser.findByStatus", query = "SELECT g FROM GroupUser g WHERE g.status = :status")
    , @NamedQuery(name = "GroupUser.findByStartTime", query = "SELECT g FROM GroupUser g WHERE g.startTime = :startTime")
    , @NamedQuery(name = "GroupUser.findByEndTime", query = "SELECT g FROM GroupUser g WHERE g.endTime = :endTime")
    , @NamedQuery(name = "GroupUser.findByCreateTime", query = "SELECT g FROM GroupUser g WHERE g.createTime = :createTime")})
public class GroupUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
//    @Column(name = "JOIN_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date joinDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "MAX_NUMBER_ROOM")
    private Long maxNumberRoom;
    @Transient
    private String statusName;

    public GroupUser() {
    }

    public GroupUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupUser)) {
            return false;
        }
        GroupUser other = (GroupUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

//    public Date getJoinDate() {
//        return joinDate;
//    }
//
//    public void setJoinDate(Date joinDate) {
//        this.joinDate = joinDate;
//    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxNumberRoom() {
        return maxNumberRoom;
    }

    public void setMaxNumberRoom(Long maxNumberRoom) {
        this.maxNumberRoom = maxNumberRoom;
    }

    public String getStatusName() {
        if (Constant.HOME_STATUS.ACTIVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("common.ACTIVE");
        } else if (Constant.HOME_STATUS.DISABLE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("common.INACTIVE");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
