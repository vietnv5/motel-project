package com.slook.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/27/2017.
 */
@Entity
@Table(name = "PROMOTION_GROUP_PACK")
public class PromotionGroupPack
{
    private Long id;
    private Long groupPackId;
    private Long promotionType;
    private Long promotionValue;
    private Long packIdPromotion;


    @Id
    @Column
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "PROMOTION_GROUP_PACK_SEQ", allocationSize = 1)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "GROUP_PACK_ID")
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @Basic
    @Column(name = "PROMOTION_TYPE")
    public Long getPromotionType()
    {
        return promotionType;
    }

    public void setPromotionType(Long promotionType)
    {
        this.promotionType = promotionType;
    }

    @Basic
    @Column(name = "PROMOTION_VALUE")
    public Long getPromotionValue()
    {
        return promotionValue;
    }

    public void setPromotionValue(Long promotionValue)
    {
        this.promotionValue = promotionValue;
    }

    @Basic
    @Column(name = "PACK_ID_PROMOTION")
    public Long getPackIdPromotion()
    {
        return packIdPromotion;
    }

    public void setPackIdPromotion(Long packIdPromotion)
    {
        this.packIdPromotion = packIdPromotion;
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

        PromotionGroupPack that = (PromotionGroupPack) o;

        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null)
        {
            return false;
        }
        if (promotionType != null ? !promotionType.equals(that.promotionType) : that.promotionType != null)
        {
            return false;
        }
        if (promotionValue != null ? !promotionValue.equals(that.promotionValue) : that.promotionValue != null)
        {
            return false;
        }
        if (packIdPromotion != null ? !packIdPromotion.equals(that.packIdPromotion) : that.packIdPromotion != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = groupPackId != null ? groupPackId.hashCode() : 0;
        result = 31 * result + (promotionType != null ? promotionType.hashCode() : 0);
        result = 31 * result + (promotionValue != null ? promotionValue.hashCode() : 0);
        result = 31 * result + (packIdPromotion != null ? packIdPromotion.hashCode() : 0);
        return result;
    }
}
