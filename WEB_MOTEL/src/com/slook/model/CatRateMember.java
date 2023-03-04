package com.slook.model;

import javax.persistence.*;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_RATE_MEMBER", catalog = "")
public class CatRateMember
{
    private Long rateId;
    private Long point;
    private String memberType;

    @Id
    @Column(name = "RATE_ID", nullable = false, precision = 0)
    public Long getRateId()
    {
        return rateId;
    }

    public void setRateId(Long rateId)
    {
        this.rateId = rateId;
    }

    @Basic
    @Column(name = "POINT", nullable = true, precision = 0)
    public Long getPoint()
    {
        return point;
    }

    public void setPoint(Long point)
    {
        this.point = point;
    }

    @Basic
    @Column(name = "MEMBER_TYPE", nullable = true, length = 20)
    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
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

        CatRateMember that = (CatRateMember) o;

        if (rateId != null ? !rateId.equals(that.rateId) : that.rateId != null)
        {
            return false;
        }
        if (point != null ? !point.equals(that.point) : that.point != null)
        {
            return false;
        }
        if (memberType != null ? !memberType.equals(that.memberType) : that.memberType != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = rateId != null ? rateId.hashCode() : 0;
        result = 31 * result + (point != null ? point.hashCode() : 0);
        result = 31 * result + (memberType != null ? memberType.hashCode() : 0);
        return result;
    }
}
