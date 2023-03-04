/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "CLIENT_PROMOTION")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class ClientPromotion
{

    private Date createDate;
    private Long clientId;
    private String description;
    private Long catPromotionId;
    private Long groupPackId;
    private Long promotionId;
    private Double value;
    private CatGroupPack groupPack;
    private Long clientUsePackId;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE", length = 7)
    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    @Column(name = "CLIENT_ID", precision = 22, scale = 0)
    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    @Column(name = "DESCRIPTION", length = 300)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    @Column(name = "PROMOTION_ID", precision = 22, scale = 0)
    @SequenceGenerator(name = "generator", sequenceName = "CLIENT_PROMOTION_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    public Long getPromotionId()
    {
        return promotionId;
    }

    public void setPromotionId(Long promotionId)
    {
        this.promotionId = promotionId;
    }

    @Column(name = "VALUE", precision = 22, scale = 2)
    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public ClientPromotion()
    {
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

    @Column(name = "CLIENT_USE_PACK_ID", precision = 22, scale = 0)
    public Long getClientUsePackId()
    {
        return clientUsePackId;
    }

    public void setClientUsePackId(Long clientUsePackId)
    {
        this.clientUsePackId = clientUsePackId;
    }
}
