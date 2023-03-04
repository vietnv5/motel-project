package com.slook.model;

import javax.persistence.*;

/**
 * Created by Anonymous on 6/12/2017.
 */
@Entity
@Table(name = "CAT_ADDRESS_CITY", schema = "GYM_SERVICE", catalog = "")
public class CatAddressCity
{
    private String cityName;
    private Long cityId;

    @Basic
    @Column(name = "city_name")
    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    @Id
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

        CatAddressCity that = (CatAddressCity) o;

        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null)
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
        int result = cityName != null ? cityName.hashCode() : 0;
        result = 31 * result + (cityId != null ? cityId.hashCode() : 0);
        return result;
    }
}
