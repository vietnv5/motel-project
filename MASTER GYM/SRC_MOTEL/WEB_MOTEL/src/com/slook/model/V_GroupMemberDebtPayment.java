package com.slook.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_GROUP_MEMBER_DEBT_PAYMENT")
public class V_GroupMemberDebtPayment implements Serializable{

    private Long groupMemberId;
    private Long groupMembershipId;
    private String groupPackName;
    private Long sumDept;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Id
    @Column(name = "GROUP_MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false)
    public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    @Column(name = "GROUP_PACK_NAME", length = 500, updatable = false)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Column(name = "SUM_DEPT", precision = 22, scale = 0, updatable = false)
    public Long getSumDept() {
        return sumDept;
    }

    public void setSumDept(Long sumDept) {
        this.sumDept = sumDept;
    }

    public V_GroupMemberDebtPayment() {
    }
}
