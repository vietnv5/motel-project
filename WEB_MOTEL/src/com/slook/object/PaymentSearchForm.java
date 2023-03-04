/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.object;

import java.util.Date;

/**
 * @author SLOOK. JSC on Nov 7, 2017 4:50:45 PM
 */
public class PaymentSearchForm
{

    private Date fromDate;
    private Date toDate;
    private Long catGroupPackId;
    private Long type;
    private Long loaiNo;


    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

    public Long getCatGroupPackId()
    {
        return catGroupPackId;
    }

    public void setCatGroupPackId(Long catGroupPackId)
    {
        this.catGroupPackId = catGroupPackId;
    }

    public Long getType()
    {
        return type;
    }

    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getLoaiNo()
    {
        return loaiNo;
    }

    public void setLoaiNo(Long khachNo)
    {
        this.loaiNo = khachNo;
    }

}
