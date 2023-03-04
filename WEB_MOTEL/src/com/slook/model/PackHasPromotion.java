/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "PACK_HAS_PROMOTION")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class PackHasPromotion
{

    private Long packHasPromotionId;
    private Long catPromotionId;
    private Long groupPackId;
    private CatPromotion catPromotion;
    private CatGroupPack groupPack;

    @SequenceGenerator(name = "generator", sequenceName = "PACK_HAS_PROMOTION_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "PACK_HAS_PROMOTION_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getPackHasPromotionId()
    {
        return packHasPromotionId;
    }

    public void setPackHasPromotionId(Long packHasPromotionId)
    {
        this.packHasPromotionId = packHasPromotionId;
    }

    @Column(name = "CAT_PROMOTION_ID", precision = 22, scale = 0)
    public Long getCatPromotionId()
    {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId)
    {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "GROUP_PACK_ID", precision = 22, scale = 0)
    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT_PROMOTION_ID", insertable = false, updatable = false)
    public CatPromotion getCatPromotion()
    {
        return catPromotion;
    }

    public void setCatPromotion(CatPromotion catPromotion)
    {
        this.catPromotion = catPromotion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_PACK_ID", insertable = false, updatable = false)
    public CatGroupPack getGroupPack()
    {
        return groupPack;
    }

    public void setGroupPack(CatGroupPack groupPack)
    {
        this.groupPack = groupPack;
    }

    public PackHasPromotion()
    {
    }

    public PackHasPromotion(Long packHasPromotionId)
    {
        this.packHasPromotionId = packHasPromotionId;
    }

    public PackHasPromotion(Long catPromotionId, Long groupPackId)
    {
        this.catPromotionId = catPromotionId;
        this.groupPackId = groupPackId;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.catPromotionId);
        hash = 53 * hash + Objects.hashCode(this.groupPackId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final PackHasPromotion other = (PackHasPromotion) obj;
        if (!Objects.equals(this.catPromotionId, other.catPromotionId))
        {
            return false;
        }
        if (!Objects.equals(this.groupPackId, other.groupPackId))
        {
            return false;
        }
        return true;
    }

}
