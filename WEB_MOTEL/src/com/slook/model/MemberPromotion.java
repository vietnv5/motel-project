package com.slook.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "MEMBER_PROMOTION")
public class MemberPromotion
{

    private Long promotionId;
    private Long memberId;
    private Long groupPackId;
    private CatGroupPack groupPack;
    private String description;
    private Date createDate;
    private Double value;
    private CatPromotion catPromotion;
    private Long catPromotionId;
    private Long membershipId;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_PROMOTION_SEQ", allocationSize = 1)
    @Column(name = "PROMOTION_ID", nullable = false, precision = 0)
    public Long getPromotionId()
    {
        return promotionId;
    }

    public void setPromotionId(Long promotionId)
    {
        this.promotionId = promotionId;
    }

    @Basic
    @Column(name = "MEMBER_ID", nullable = true, precision = 0)
    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "GROUP_PACK_ID", nullable = true, precision = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 300)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date dateCreate)
    {
        this.createDate = dateCreate;
    }

    @Column(name = "VALUE")
    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "GROUP_PACK_ID", insertable = false, updatable = false)
    public CatGroupPack getGroupPack()
    {
        return groupPack;
    }

    public void setGroupPack(CatGroupPack groupPack)
    {
        this.groupPack = groupPack;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MemberPromotion that = (MemberPromotion) o;

        if (promotionId != null ? !promotionId.equals(that.promotionId) : that.promotionId != null)
        {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null)
        {
            return false;
        }
        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null)
        {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = promotionId != null ? promotionId.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (groupPackId != null ? groupPackId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Transient
    public CatPromotion getCatPromotion()
    {
        return catPromotion;
    }

    public void setCatPromotion(CatPromotion catPromotion)
    {
        this.catPromotion = catPromotion;
    }

    @Column(name = "CAT_PROMOTION_ID", nullable = true, precision = 0)
    public Long getCatPromotionId()
    {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId)
    {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "MEMBERSHIP_ID", nullable = true, precision = 0)
    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

}
