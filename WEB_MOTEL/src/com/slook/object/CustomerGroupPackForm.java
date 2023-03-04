/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.object;

import java.util.Date;

/**
 * @author VietNV on Nov 16, 2017
 */
public class CustomerGroupPackForm extends PaymentGroupPackForm
{

    private Long totalNumber;
    private Long usedNumber;
    private Long membershipId;
    private Date joinDate;
    private Date endDate;
    private Long status;
    private String key;

    public Long getTotalNumber()
    {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber)
    {
        this.totalNumber = totalNumber;
    }

    public Long getUsedNumber()
    {
        return usedNumber;
    }

    public void setUsedNumber(Long usedNumber)
    {
        this.usedNumber = usedNumber;
    }

    public Long getMembershipId()
    {
        return membershipId;
    }

    public void setMembershipId(Long membershipId)
    {
        this.membershipId = membershipId;
    }

    public Date getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Override
    public String getKey()
    {
        key = super.getKeyCustomerId() + "_" + (membershipId != null ? membershipId : "");
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

}
