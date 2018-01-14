package com.slook.model;

import javax.persistence.*;

/**
 * Created by xuanh on 6/4/2017.
 */
@Entity
@Table(name = "CAT_COMMUNE")
public class CatCommune {
    private Long id;
    private String communeCode;
    private String communeName;
    private Long districtId;

    CatDistrict district;

    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "COMMUNE_CODE")
    public String getCommuneCode() {
        return communeCode;
    }

    public void setCommuneCode(String communeCode) {
        this.communeCode = communeCode;
    }

    @Basic
    @Column(name = "COMMUNE_NAME")
    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    @Basic
    @Column(name = "DISTRICT_ID")
    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @ManyToOne
    @JoinColumn(name = "DISTRICT_ID", insertable = false, updatable = false)
    public CatDistrict getDistrict() {
        return district;
    }

    public void setDistrict(CatDistrict district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatCommune that = (CatCommune) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (communeCode != null ? !communeCode.equals(that.communeCode) : that.communeCode != null) return false;
        if (communeName != null ? !communeName.equals(that.communeName) : that.communeName != null) return false;
        if (districtId != null ? !districtId.equals(that.districtId) : that.districtId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (communeCode != null ? communeCode.hashCode() : 0);
        result = 31 * result + (communeName != null ? communeName.hashCode() : 0);
        result = 31 * result + (districtId != null ? districtId.hashCode() : 0);
        return result;
    }
}
