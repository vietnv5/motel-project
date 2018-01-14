package com.slook.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "V_MEMBER_PAYMENT")
public class V_MemberPayment implements Serializable {

    private String memberName;
    private Date joinDate;
    private Long price;
    private Long daNop;
    private String groupPackName;
    private Long groupPackId;
    private Long memberId;
    private Date endDate;
    private String createTime;
    private String phoneNumber;
    private Long debt;
    private Long type;
    private Long paymentId;

    @Column(name = "MEMBER_NAME", length = 200, updatable = false)
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7, updatable = false)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Column(name = "PRICE", precision = 22, scale = 0, updatable = false)
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Column(name = "DA_NOP", precision = 22, scale = 0, updatable = false)
    public Long getDaNop() {
        return daNop;
    }

    public void setDaNop(Long daNop) {
        this.daNop = daNop;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Column(name = "MEMBER_ID", precision = 22, scale = 0, updatable = false)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7, updatable = false)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "CREATE_TIME", length = 11, updatable = false)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "PHONE_NUMBER", length = 30, updatable = false)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "DEBT", precision = 22, scale = 0, updatable = false)
    public Long getDebt() {
        return debt;
    }

    public void setDebt(Long debt) {
        this.debt = debt;
    }

    @Column(name = "TYPE", precision = 22, scale = 0, updatable = false)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public V_MemberPayment() {
    }

    @Id
    @Column(name = "PAYMENT_ID", nullable = false, precision = 0)
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
