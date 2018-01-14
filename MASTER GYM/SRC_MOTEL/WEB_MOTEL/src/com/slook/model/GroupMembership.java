package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "GROUP_MEMBERSHIP")
public class GroupMembership {

    private Long groupMemberId;
    private Long status;
    private Long groupMembershipId;
    private Date joinDate;
    private Long groupPackId;
    private Date freezeTime;
    private Date endDate;
    private CatGroupPack catGroupPack;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_MEMBERSHIP_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "GROUP_MEMBERSHIP_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "FREEZE_TIME", length = 7)
    public Date getFreezeTime() {
        return freezeTime;
    }

    public void setFreezeTime(Date freezeTime) {
        this.freezeTime = freezeTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_PACK_ID", insertable = false, updatable = false)
    public CatGroupPack getCatGroupPack() {
        return catGroupPack;
    }

    public void setCatGroupPack(CatGroupPack catGroupPack) {
        this.catGroupPack = catGroupPack;
    }

    public GroupMembership() {
    }

    public GroupMembership(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
