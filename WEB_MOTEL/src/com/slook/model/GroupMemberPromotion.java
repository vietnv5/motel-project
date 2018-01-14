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
@Table(name = "GROUP_MEMBER_PROMOTION")
public class GroupMemberPromotion {

    private Long groupMemberId;
    private Date createDate;
    private String description;
    private Long catPromotionId;
    private Long groupPackId;
    private Long promotionId;
    private Double value;
    private GroupMember groupMember;
    private CatGroupPack catGroupPack;
    private Long groupMembershipId;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE", length = 7)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "DESCRIPTION", length = 300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CAT_PROMOTION_ID", precision = 22, scale = 0)
    public Long getCatPromotionId() {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId) {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_MEMBER_PROMOTION_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "PROMOTION_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    @Column(name = "VALUE", precision = 22, scale = 2)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_MEMBER_ID", insertable = false, updatable = false)
    public GroupMember getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_PACK_ID", insertable = false, updatable = false)
    public CatGroupPack getCatGroupPack() {
        return catGroupPack;
    }

    public void setCatGroupPack(CatGroupPack catGroupPack) {
        this.catGroupPack = catGroupPack;
    }

    public GroupMemberPromotion() {
    }

    public GroupMemberPromotion(Long promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    @Column(name = "GROUP_MEMBERSHIP_ID", precision = 22, scale = 0)
    public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

}
