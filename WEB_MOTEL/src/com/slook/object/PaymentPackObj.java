/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.object;

/**
 * @author vietnv
 */
public class PaymentPackObj
{

    String groupPackName;
    Long quantity;
    Long price;
    String prom;
    Long amount;
    String exp;

    public String getGroupPackName()
    {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName)
    {
        this.groupPackName = groupPackName;
    }


    public Long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }

    public Long getPrice()
    {
        return price;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    public String getProm()
    {
        return prom;
    }

    public void setProm(String Prom)
    {
        this.prom = Prom;
    }

    public Long getAmount()
    {
        return amount;
    }

    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public String getExp()
    {
        return exp;
    }

    public void setExp(String exp)
    {
        this.exp = exp;
    }


}
