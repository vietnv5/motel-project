package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.MessageUtil;
import com.slook.util.Util;
import java.util.ArrayList;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_PACK")
public class CatPack {

    private Long packId;
    private String packName;
    private String packType;
    private Double price;
    private String packCode;
    private Long effect;
    private String description;
    private Long status;
    private Long serviceId;
    private Long numberOfTime;
    private CatService catService;
    private boolean limitNumber;
    private String statusName;

    @Id
    @Column(name = "PACK_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "PACK_SEQ", allocationSize = 1)
    public Long getPackId() {
        return packId;
    }

    public void setPackId(Long packId) {
        this.packId = packId;
    }

    @Basic
    @Column(name = "PACK_NAME", nullable = true, length = 50)
    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    @Basic
    @Column(name = "PACK_TYPE", nullable = true, length = 20)
    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatPack catPack = (CatPack) o;

        if (packId != null ? !packId.equals(catPack.packId) : catPack.packId != null) {
            return false;
        }
        if (packName != null ? !packName.equals(catPack.packName) : catPack.packName != null) {
            return false;
        }
        if (packType != null ? !packType.equals(catPack.packType) : catPack.packType != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = packId != null ? packId.hashCode() : 0;
        result = 31 * result + (packName != null ? packName.hashCode() : 0);
        result = 31 * result + (packType != null ? packType.hashCode() : 0);
        return result;
    }

    @Column
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "PACK_CODE")
    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public void createPackCode() {

        String hqlCheckCode = "select count(*) from CatPack where packCode like ?||'-%'";
        String code = Util.createCode(packName);
        if (code.isEmpty()) {
            return;
        }
        List<?> counts = new GenericDaoImplNewV2<CatPack, Long>() {
        }.findListAll(hqlCheckCode, code);
        if (counts.size() > 0) {
            packCode = code + "-" + (Long.valueOf(counts.get(0).toString()) + 1);
        }
    }

    @Column
    public Long getEffect() {
        return effect;
    }

    public void setEffect(Long effect) {
        this.effect = effect;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "service_id", precision = 22, scale = 0)
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Column(name = "number_of_time", precision = 22, scale = 0)
    public Long getNumberOfTime() {
        return numberOfTime;
    }

    public void setNumberOfTime(Long numberOfTime) {
        this.numberOfTime = numberOfTime;
    }

    @ManyToOne
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    public CatService getCatService() {
        return catService;
    }

    public void setCatService(CatService catService) {
        this.catService = catService;
    }

    @Transient
    public boolean getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(boolean limitNumber) {
        this.limitNumber = limitNumber;
    }

    public void computingPrice() {
        price = 0.0;

        if (catService != null && catService.getPrice() != null) {
            price = Double.valueOf(catService.getPrice());
        }
        if (limitNumber && numberOfTime > 0) {
            price = numberOfTime * price;
        }
    }

    public void computingPrice2() {
        if (catService != null && catService.getPrice() != null) {
            price = Double.valueOf(catService.getPrice());
        }
        if (limitNumber && numberOfTime > 0 && price != null) {
            price = numberOfTime * Double.valueOf(catService.getPrice());
        } else{
            price = Double.valueOf(catService.getPrice());
        }

    }

    @Transient
    public String getStatusName() {
        if(status!=null){
            if(status.equals(0l))statusName=MessageUtil.getResourceBundleMessage("datatable.header.pack.status0");
            else if(status.equals(1l))statusName=MessageUtil.getResourceBundleMessage("datatable.header.pack.status1");
            else if(status.equals(2l))statusName=MessageUtil.getResourceBundleMessage("datatable.header.pack.status2");
            else if(status.equals(3l))statusName=MessageUtil.getResourceBundleMessage("datatable.header.pack.status3");
        }
        
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    
}
