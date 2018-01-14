package com.slook.model;

import javax.persistence.*;

/**
 * Created by xuanh on 6/4/2017.
 */
@Entity
@Table(name = "CAT_DISTRICT")
public class CatDistrict {
    private Long id;
    private String districtCode;
    private String districtName;
    private Long provinceId;
    CatProvince province;

    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "DISTRICT_CODE")
    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Basic
    @Column(name = "DISTRICT_NAME")
    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Basic
    @Column(name = "PROVINCE_ID")
    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    @ManyToOne
    @JoinColumn(name = "PROVINCE_ID", insertable = false, updatable = false)
    public CatProvince getProvince() {
        return province;
    }

    public void setProvince(CatProvince province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatDistrict that = (CatDistrict) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (districtCode != null ? !districtCode.equals(that.districtCode) : that.districtCode != null) return false;
        if (districtName != null ? !districtName.equals(that.districtName) : that.districtName != null) return false;
        if (provinceId != null ? !provinceId.equals(that.provinceId) : that.provinceId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (districtCode != null ? districtCode.hashCode() : 0);
        result = 31 * result + (districtName != null ? districtName.hashCode() : 0);
        result = 31 * result + (provinceId != null ? provinceId.hashCode() : 0);
        return result;
    }
}
