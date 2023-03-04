package com.slook.model;

import javax.persistence.*;

/**
 * Created by Anonymous on 6/12/2017.
 */
@Entity
@Table(name = "CAT_ADDRESS_DISTRICT", schema = "GYM_SERVICE", catalog = "")
public class CatAddressDistrict
{
    private String districtName;
    private Long districtId;
    private Long cityId;

    @Basic
    @Column(name = "district_name")
    public String getDistrictName()
    {
        return districtName;
    }

    public void setDistrictName(String districtName)
    {
        this.districtName = districtName;
    }

    @Id
    @Column(name = "district_id")
    public Long getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(Long districtId)
    {
        this.districtId = districtId;
    }

    @Basic
    @Column(name = "city_id")
    public Long getCityId()
    {
        return cityId;
    }

    public void setCityId(Long cityId)
    {
        this.cityId = cityId;
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

        CatAddressDistrict that = (CatAddressDistrict) o;

        if (districtName != null ? !districtName.equals(that.districtName) : that.districtName != null)
        {
            return false;
        }
        if (districtId != null ? !districtId.equals(that.districtId) : that.districtId != null)
        {
            return false;
        }
        if (cityId != null ? !cityId.equals(that.cityId) : that.cityId != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = districtName != null ? districtName.hashCode() : 0;
        result = 31 * result + (districtId != null ? districtId.hashCode() : 0);
        result = 31 * result + (cityId != null ? cityId.hashCode() : 0);
        return result;
    }
}
