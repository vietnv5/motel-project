/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "V_CUSTOMER_ACCESS_STATUS")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class V_CustomerAccessStatus {

    private Long groupMemberId;
    private Date checkTime;
    private String cardCode;
    private String port;
    private String groupPackName;
    private Long membershipId;
    private Long accessStatus;
    private String ip;
    private Long customerId;
    private String name;
    private String groupMemberName;
    private Long status;
    private Date checkoutTime;
    private Long groupPackId;
    private String roomName;
    private String machineName;
    private Long id;
    private Long type;
    private Long doorId;
    private String accessStatusName;
    private String rowkey;
    private Date updateTime;
    private Long doorType;
    private String doorTypeName;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECK_TIME", length = 11, updatable = false)
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Column(name = "CARD_CODE", length = 50, updatable = false)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Column(name = "PORT", length = 10, updatable = false)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    @Column(name = "ACCESS_STATUS", precision = 22, scale = 0, updatable = false)
    public Long getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(Long accessStatus) {
        this.accessStatus = accessStatus;
    }

    @Column(name = "IP", length = 200, updatable = false)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "CUSTOMER_ID", precision = 22, scale = 0, updatable = false)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Column(name = "NAME", length = 255, updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "GROUP_MEMBER_NAME", length = 255, updatable = false)
    public String getGroupMemberName() {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        this.groupMemberName = groupMemberName;
    }

    @Column(name = "STATUS", precision = 22, scale = 0, updatable = false)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CHECKOUT_TIME", length = 11, updatable = false)
    public Date getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Column(name = "ROOM_NAME", length = 50, updatable = false)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Column(name = "ID", precision = 22, scale = 0, updatable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TYPE", precision = 22, scale = 0, updatable = false)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "door_id", precision = 22, scale = 0, updatable = false)
    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    @Column(name = "Machine_Name", length = 50, updatable = false)
    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    @Id
    @Column(name = "rowkey", length = 50, updatable = false)
    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "update_time", length = 11, updatable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "door_type", precision = 22, scale = 0, updatable = false)
    public Long getDoorType() {
        return doorType;
    }

    public void setDoorType(Long doorType) {
        this.doorType = doorType;
    }

    @Transient
    public String getDoorTypeName() {
        if (Constant.DOOR_ACCESS.DOOR_TYPE_IN.equals(doorType)) {
            doorTypeName = MessageUtil.getResourceBundleMessage("doorType.in");
        } else if (Constant.DOOR_ACCESS.DOOR_TYPE_OUT.equals(doorType)) {
            doorTypeName = MessageUtil.getResourceBundleMessage("doorType.out");
        }
        return doorTypeName;
    }

    public void setDoorTypeName(String doorTypeName) {
        this.doorTypeName = doorTypeName;
    }

    @Transient
    public String getAccessStatusName() {
        if (accessStatus != null) {
            if (accessStatus.equals(1l)) {
                accessStatusName = MessageUtil.getResourceBundleMessage("common.ACTIVE");
            } else if (accessStatus.equals(2l)) {
                accessStatusName = MessageUtil.getResourceBundleMessage("common.INACTIVE");
            }
        }
        return accessStatusName;
    }

    public void setAccessStatusName(String accessStatusName) {
        this.accessStatusName = accessStatusName;
    }

    public V_CustomerAccessStatus() {
    }
}
