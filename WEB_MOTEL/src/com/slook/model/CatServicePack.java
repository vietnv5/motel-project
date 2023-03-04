package com.slook.model;

import javax.persistence.*;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_SERVICE_PACK", catalog = "")
public class CatServicePack
{
    private Long servicePackId;
    private Long packId;
    private Long serviceId;

    @Id
    @Column(name = "SERVICE_PACK_ID", nullable = false, length = 20)
    public Long getServicePackId()
    {
        return servicePackId;
    }

    public void setServicePackId(Long servicePackId)
    {
        this.servicePackId = servicePackId;
    }

    @Basic
    @Column(name = "PACK_ID", nullable = true, length = 20)
    public Long getPackId()
    {
        return packId;
    }

    public void setPackId(Long packId)
    {
        this.packId = packId;
    }

    @Basic
    @Column(name = "SERVICE_ID", nullable = true, length = 20)
    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
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

        CatServicePack that = (CatServicePack) o;

        if (servicePackId != null ? !servicePackId.equals(that.servicePackId) : that.servicePackId != null)
        {
            return false;
        }
        if (packId != null ? !packId.equals(that.packId) : that.packId != null)
        {
            return false;
        }
        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = servicePackId != null ? servicePackId.hashCode() : 0;
        result = 31 * result + (packId != null ? packId.hashCode() : 0);
        result = 31 * result + (serviceId != null ? serviceId.hashCode() : 0);
        return result;
    }
}
