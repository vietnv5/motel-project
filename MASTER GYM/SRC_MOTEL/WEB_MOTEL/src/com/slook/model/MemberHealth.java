package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "MEMBER_HEALTH")
public class MemberHealth {
    private Long healthId;
    private Long memberId;
    private Double weight;
    private Long height;
    private Long heartbeat;
    private Long bloodPressure;
    private String healthLevel;
    private Date dateTime;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_HEALTH_SEQ", allocationSize = 1)
    @Column(name = "HEALTH_ID", nullable = false, precision = 0)
    public Long getHealthId() {
        return healthId;
    }

    public void setHealthId(Long healthId) {
        this.healthId = healthId;
    }

    @Basic
    @Column(name = "MEMBER_ID", nullable = true, precision = 0)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "WEIGHT", nullable = true, precision = 0)
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "HEIGHT", nullable = true, precision = 0)
    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    @Basic
    @Column(name = "HEARTBEAT", nullable = true, precision = 0)
    public Long getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Long heartbeat) {
        this.heartbeat = heartbeat;
    }

    @Basic
    @Column(name = "BLOOD_PRESSURE", nullable = true, precision = 0)
    public Long getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Long bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    @Basic
    @Column(name = "HEALTH_LEVEL", nullable = true, length = 20)
    public String getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(String healthLevel) {
        this.healthLevel = healthLevel;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TIME", nullable = true)
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberHealth that = (MemberHealth) o;

        if (healthId != null ? !healthId.equals(that.healthId) : that.healthId != null) return false;
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (heartbeat != null ? !heartbeat.equals(that.heartbeat) : that.heartbeat != null) return false;
        if (bloodPressure != null ? !bloodPressure.equals(that.bloodPressure) : that.bloodPressure != null)
            return false;
        if (healthLevel != null ? !healthLevel.equals(that.healthLevel) : that.healthLevel != null) return false;
        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = healthId != null ? healthId.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (heartbeat != null ? heartbeat.hashCode() : 0);
        result = 31 * result + (bloodPressure != null ? bloodPressure.hashCode() : 0);
        result = 31 * result + (healthLevel != null ? healthLevel.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
