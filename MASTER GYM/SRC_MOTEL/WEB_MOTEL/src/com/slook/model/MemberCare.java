package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by Antz01 on 31/05/2017.
 */
@Entity
@Table(name = "MEMBER_CARE", schema = "GYM_SERVICE", catalog = "")
public class MemberCare {
    private Long memberCareId;
    private Long memberId;
    private Long employeeId;
    private Date startDate;
    private Date endDate;
    private String result;
    private Long statusId;
    private Long type;

    private Member member;
    private Employee employee;
    private MemberCareStatus status;

    @Id
    @Column(name = "MEMBER_CARE_ID")
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_CARE_SEQ", allocationSize = 1)
    public Long getMemberCareId() {
        return memberCareId;
    }

    public void setMemberCareId(Long memberCareId) {
        this.memberCareId = memberCareId;
    }

    @Basic
    @Column(name = "MEMBER_ID")
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Basic
    @Column(name = "EMPLOYEE_ID")
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Basic
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "RESULT")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Basic
    @Column(name = "STATUS_ID")
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    public MemberCareStatus getStatus() {
        return status;
    }

    public void setStatus(MemberCareStatus status) {
        this.status = status;
    }

    @Basic
    @Column(name = "TYPE")
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberCare that = (MemberCare) o;

        if (memberCareId != that.memberCareId) return false;
        if (memberId != that.memberId) return false;
        if (employeeId != that.employeeId) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (statusId != null ? !statusId.equals(that.statusId) : that.statusId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = (int) (memberCareId ^ (memberCareId >>> 32));
        result1 = 31 * result1 + (int) (memberId ^ (memberId >>> 32));
        result1 = 31 * result1 + (int) (employeeId ^ (employeeId >>> 32));
        result1 = 31 * result1 + (startDate != null ? startDate.hashCode() : 0);
        result1 = 31 * result1 + (endDate != null ? endDate.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (statusId != null ? statusId.hashCode() : 0);
        return result1;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
