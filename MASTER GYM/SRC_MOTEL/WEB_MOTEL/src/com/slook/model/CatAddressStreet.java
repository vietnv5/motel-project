package com.slook.model;

import javax.persistence.*;

/**
 * Created by Anonymous on 6/12/2017.
 */
@Entity
@Table(name = "CAT_ADDRESS_STREET", schema = "GYM_SERVICE", catalog = "")
public class CatAddressStreet {
    private String streetName;
    private Long streetId;
    private Long districtId;

    @Basic
    @Column(name = "street_name")
    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Id
    @Column(name = "street_id")
    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    @Basic
    @Column(name = "district_id")
    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatAddressStreet that = (CatAddressStreet) o;

        if (streetName != null ? !streetName.equals(that.streetName) : that.streetName != null) return false;
        if (streetId != null ? !streetId.equals(that.streetId) : that.streetId != null) return false;
        if (districtId != null ? !districtId.equals(that.districtId) : that.districtId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = streetName != null ? streetName.hashCode() : 0;
        result = 31 * result + (streetId != null ? streetId.hashCode() : 0);
        result = 31 * result + (districtId != null ? districtId.hashCode() : 0);
        return result;
    }
}
