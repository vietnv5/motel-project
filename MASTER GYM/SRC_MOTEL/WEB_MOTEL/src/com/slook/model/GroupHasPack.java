package com.slook.model;

import javax.persistence.*;

/**
 * Created by xuanh on 5/27/2017.
 */
@Entity
@Table(name = "GROUP_HAS_PACK")
@IdClass(GroupHasPackPK.class)
public class GroupHasPack {
    @Id
    private Long groupPackId;
    @Id
    private Long packId;
    private Double sale;
    private Long saleType;
    private CatPack pack;
    Boolean promotionPack;
    boolean promotionPack2;


    @Id
    @Column(name = "GROUP_PACK_ID")
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Id
    @Column(name = "PACK_ID")
    public Long getPackId() {
        return packId;
    }

    public void setPackId(Long packId) {
        this.packId = packId;
    }

    @Basic
    @Column(name = "SALE")
    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    @Basic
    @Column(name = "SALE_TYPE")
    public Long getSaleType() {
        return saleType;
    }

    public void setSaleType(Long saleType) {
        this.saleType = saleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupHasPack that = (GroupHasPack) o;

        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null) return false;
        if (packId != null ? !packId.equals(that.packId) : that.packId != null) return false;
        if (sale != null ? !sale.equals(that.sale) : that.sale != null) return false;
        if (saleType != null ? !saleType.equals(that.saleType) : that.saleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupPackId != null ? groupPackId.hashCode() : 0;
        result = 31 * result + (packId != null ? packId.hashCode() : 0);
        result = 31 * result + (sale != null ? sale.hashCode() : 0);
        result = 31 * result + (saleType != null ? saleType.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "PACK_ID", insertable = false, updatable = false)
    public CatPack getPack() {
        return pack;
    }

    public void setPack(CatPack pack) {
        this.pack = pack;
    }

    @Column(name = "IS_PROMOTION_PACK")
    public Boolean getPromotionPack() {
        return promotionPack;
    }

    public void setPromotionPack(Boolean promotionPack) {
        this.promotionPack = promotionPack;
        if (promotionPack!=null)
            promotionPack2 = promotionPack;
    }

    @Transient
    public boolean isPromotionPack2() {
        return promotionPack2;
    }

    public void setPromotionPack2(boolean promotionPack2) {
        this.promotionPack2 = promotionPack2;
        promotionPack = promotionPack2;
    }
}
