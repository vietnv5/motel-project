/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "CAT_PROMOTION")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class CatPromotion {

    private String typeName;
    private String catPromotionCode;
    private Long status;
    private Date endDate;
    private Long catPromotionId;
    private String catPromotionName;
    private String description;
    private Date startDate;
    private Double value;
    private Long type;
    private String valueStr, statusName;

    private Long requireCode;
    private String operator;
    private String valueCompare;
    private List<PackHasPromotion> packHasPromotions = new ArrayList<>();
    private List<CatGroupPack> lstCatGroupPacks = new ArrayList<>();

    @Column(name = "CAT_PROMOTION_CODE", length = 255)
    public String getCatPromotionCode() {
        return catPromotionCode;
    }

    public void setCatPromotionCode(String catPromotionCode) {
        this.catPromotionCode = catPromotionCode;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE_", length = 7)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CAT_PROMOTION_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CAT_PROMOTION_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getCatPromotionId() {
        return catPromotionId;
    }

    public void setCatPromotionId(Long catPromotionId) {
        this.catPromotionId = catPromotionId;
    }

    @Column(name = "CAT_PROMOTION_NAME", length = 255)
    public String getCatPromotionName() {
        return catPromotionName;
    }

    public void setCatPromotionName(String catPromotionName) {
        this.catPromotionName = catPromotionName;
    }

    @Column(name = "DESCRIPTION", length = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", length = 7)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "VALUE", precision = 22, scale = 2)
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Column(name = "TYPE", precision = 22, scale = 0)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public CatPromotion() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.catPromotionCode);
        hash = 47 * hash + Objects.hashCode(this.catPromotionId);
        hash = 47 * hash + Objects.hashCode(this.catPromotionName);
        hash = 47 * hash + Objects.hashCode(this.description);
        hash = 47 * hash + Objects.hashCode(this.value);
        hash = 47 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatPromotion other = (CatPromotion) obj;
        if (!Objects.equals(this.catPromotionCode, other.catPromotionCode)) {
            return false;
        }
        if (!Objects.equals(this.catPromotionName, other.catPromotionName)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.catPromotionId, other.catPromotionId)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Transient
    public String getTypeName() {
        if (type != null) {
            if (type.equals(Constant.PROMOTION_TYPE.THEM_LUOT)) {
                typeName = MessageUtil.getResourceBundleMessage("catPromotion.type.1");
            } else if (type.equals(Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT)) {
                typeName = MessageUtil.getResourceBundleMessage("catPromotion.type.2");
            } else if (type.equals(Constant.PROMOTION_TYPE.GIAM_TIEN_MAT)) {
                typeName = MessageUtil.getResourceBundleMessage("catPromotion.type.3");
            } else if (type.equals(Constant.PROMOTION_TYPE.THEM_THOI_GIAN)) {
                typeName = MessageUtil.getResourceBundleMessage("catPromotion.type.4");
            }
        }

        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name = "status", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Transient
    public String getValueStr() {
        if (value != null) {
            if (type.equals(Constant.PROMOTION_TYPE.THEM_LUOT)) {
                valueStr = value.longValue() + "";
            } else if (type.equals(Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT)) {
                valueStr = value + " %";
            } else if (type.equals(Constant.PROMOTION_TYPE.GIAM_TIEN_MAT)) {
                valueStr = value + " VND";
            } else if (type.equals(Constant.PROMOTION_TYPE.THEM_THOI_GIAN)) {
                valueStr = value + " ngày";
            } else {
                valueStr = value + "";

            }
        }
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    @Transient
    public String getStatusName() {
        if (status != null) {
            if (status.equals(Constant.CAT_PROMOTION.ENABLE)) {
                statusName = MessageUtil.getResourceBundleMessage("catService.enable");
            }
            if (status.equals(Constant.CAT_PROMOTION.DISABLE)) {
                statusName = MessageUtil.getResourceBundleMessage("catService.enable");
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Column(name = "require_code", precision = 22, scale = 2)
    public Long getRequireCode() {
        return requireCode;
    }

    public void setRequireCode(Long requireCode) {
        this.requireCode = requireCode;
    }

    @Column(name = "operator", length = 20)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "value_compare", length = 20)
    public String getValueCompare() {
        return valueCompare;
    }

    public void setValueCompare(String valueCompare) {
        this.valueCompare = valueCompare;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "catPromotionId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<PackHasPromotion> getPackHasPromotions() {
        return packHasPromotions;
    }

    public void setPackHasPromotions(List<PackHasPromotion> packHasPromotions) {
        this.packHasPromotions = packHasPromotions;
    }

    @Transient
    public List<CatGroupPack> getLstCatGroupPacks() {
        if (packHasPromotions.size() > 0 && lstCatGroupPacks.isEmpty()) {
            for (PackHasPromotion bo : packHasPromotions) {
                if (bo.getGroupPack() != null) {
                    lstCatGroupPacks.add(bo.getGroupPack());
                }
            }
        }
        return lstCatGroupPacks;
    }

    public void setLstCatGroupPacks(List<CatGroupPack> lstCatGroupPacks) {
        this.lstCatGroupPacks = lstCatGroupPacks;
    }

}
