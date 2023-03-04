/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.util.Objects;
import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "CAT_SERVICE")
public class CatServiceOld
{

    private String serviceName;
    private Long status;
    private Long price;
    private String description;
    private String serviceCode;
    private String effect;
    private Long serviceId;
    private String statusName;

    @Column(name = "SERVICE_NAME", length = 50)
    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "PRICE", precision = 22, scale = 0)
    public Long getPrice()
    {
        return price;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    @Column(name = "DESCRIPTION", length = 2000)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "SERVICE_CODE", length = 20)
    public String getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    @Column(name = "EFFECT", length = 500)
    public String getEffect()
    {
        return effect;
    }

    public void setEffect(String effect)
    {
        this.effect = effect;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CAT_SERVICE_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "SERVICE_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
    }


    public CatServiceOld()
    {
    }

    public CatServiceOld(Long serviceId)
    {
        this.serviceId = serviceId;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.serviceName);
        hash = 19 * hash + Objects.hashCode(this.status);
        hash = 19 * hash + Objects.hashCode(this.price);
        hash = 19 * hash + Objects.hashCode(this.description);
        hash = 19 * hash + Objects.hashCode(this.serviceCode);
        hash = 19 * hash + Objects.hashCode(this.effect);
        hash = 19 * hash + Objects.hashCode(this.serviceId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CatServiceOld other = (CatServiceOld) obj;
        if (!Objects.equals(this.serviceName, other.serviceName))
        {
            return false;
        }
        if (!Objects.equals(this.description, other.description))
        {
            return false;
        }
        if (!Objects.equals(this.serviceCode, other.serviceCode))
        {
            return false;
        }
        if (!Objects.equals(this.effect, other.effect))
        {
            return false;
        }
        if (!Objects.equals(this.status, other.status))
        {
            return false;
        }
        if (!Objects.equals(this.price, other.price))
        {
            return false;
        }
        if (!Objects.equals(this.serviceId, other.serviceId))
        {
            return false;
        }
        return true;
    }

    @Transient
    public String getStatusName()
    {
        if (Constant.STATUS.ENABLE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("catService.enable");
        }
        else if (Constant.STATUS.DISABLE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("catService.disable");
        }
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }
}