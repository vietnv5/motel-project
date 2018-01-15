package com.slook.model;/*
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
import com.slook.util.Constant;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import java.util.Date;

@Entity
@Table(name = "LOG_ACTION")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class LogAction {

    private Long logActionId;
    private String clientIp;
    private String newValue;
    private String objectCode;
    private String note;
    private String userName;
    private Date eventTime;
    private String function;
    private String className;
    private Long actionType;
    private String oldValue;
    private String actionTypeName;

//	@SequenceGenerator(name = "generator", sequenceName = "LOG_ACTION_SEQ", allocationSize = 1)
    @Id
//	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ACTION_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getLogActionId() {
        return logActionId;
    }

    public void setLogActionId(Long logActionId) {
        this.logActionId = logActionId;
    }

    @Column(name = "CLIENT_IP", length = 50)
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    @Column(name = "NEW_VALUE", length = 4000)
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Column(name = "OBJECT_CODE", length = 500)
    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    @Column(name = "NOTE", length = 1000)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column(name = "USER_NAME", length = 250)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "EVENT_TIME", length = 11)
    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    @Column(name = "FUNCTION", length = 250)
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Column(name = "CLASS_NAME", length = 250)
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "ACTION_TYPE", precision = 22, scale = 0)
    public Long getActionType() {
        return actionType;
    }

    public void setActionType(Long actionType) {
        this.actionType = actionType;
    }

    @Column(name = "OLD_VALUE", length = 4000)
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public LogAction() {
    }

    public LogAction(Long logActionId) {
        this.logActionId = logActionId;
    }

    @Transient
    public String getActionTypeName() {
        if (Constant.LOG_ACTION.INSERT.equals(actionType)) {
            actionTypeName = "Insert";
        } else if (Constant.LOG_ACTION.UPDATE.equals(actionType)) {
            actionTypeName = "Update";
        } else if (Constant.LOG_ACTION.DELETE.equals(actionType)) {
            actionTypeName = "Delete";
        } else if (Constant.LOG_ACTION.IMPORT.equals(actionType)) {
            actionTypeName = "Import";
        }
        return actionTypeName;
    }

    public void setActionTypeName(String actionTypeName) {
        this.actionTypeName = actionTypeName;
    }
}
