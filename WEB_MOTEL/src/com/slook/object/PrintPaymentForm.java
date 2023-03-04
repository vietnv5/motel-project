/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.object;

import java.util.List;

/**
 * @author vietnv
 */
public class PrintPaymentForm
{

    String barCode;
    String paymentCode;
    String paymentTime;
    String customerName = "";
    String customerTypeName;
    Long customerType;
    Long numCustomer = 1l;
    String checkinTime;
    String checkoutTime;
    String employeeName = "";
    Long totalPriceService = 0l;// full ban dau tien dich vu
    Long discount = 0l;
    Long vatPrice = 0l;// tien thue vat
    Long total;
    Long deposit;
    Long mustPay;
    Long customerPay;
    Long newDept = 0l;
    Long oldDept = 0l;
    Long totalDept = 0l;
    //    Long paymentValue;
    List<PaymentPackObj> lstPaymentPackObjs;

    public String getBarCode()
    {
        return barCode;
    }

    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public String getPaymentCode()
    {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode)
    {
        this.paymentCode = paymentCode;
    }

    public String getPaymentTime()
    {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime)
    {
        this.paymentTime = paymentTime;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getCustomerTypeName()
    {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName)
    {
        this.customerTypeName = customerTypeName;
    }

    public Long getCustomerType()
    {
        return customerType;
    }

    public void setCustomerType(Long customerType)
    {
        this.customerType = customerType;
    }

    public Long getNumCustomer()
    {
        return numCustomer;
    }

    public void setNumCustomer(Long numCustomer)
    {
        this.numCustomer = numCustomer;
    }

    public String getCheckinTime()
    {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime)
    {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime()
    {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }

    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    public Long getTotalPriceService()
    {
        return totalPriceService;
    }

    public void setTotalPriceService(Long totalPriceService)
    {
        this.totalPriceService = totalPriceService;
    }

    public Long getDiscount()
    {
        return discount;
    }

    public void setDiscount(Long discount)
    {
        this.discount = discount;
    }

    public Long getVatPrice()
    {
        return vatPrice;
    }

    public void setVatPrice(Long vatPrice)
    {
        this.vatPrice = vatPrice;
    }

    public Long getTotal()
    {
        return total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getDeposit()
    {
        return deposit;
    }

    public void setDeposit(Long deposit)
    {
        this.deposit = deposit;
    }

    public Long getMustPay()
    {
        return mustPay;
    }

    public void setMustPay(Long mustPay)
    {
        this.mustPay = mustPay;
    }

    public Long getCustomerPay()
    {
        return customerPay;
    }

    public void setCustomerPay(Long customerPay)
    {
        this.customerPay = customerPay;
    }

    public Long getNewDept()
    {
        return newDept;
    }

    public void setNewDept(Long newDept)
    {
        this.newDept = newDept;
    }

    public Long getOldDept()
    {
        return oldDept;
    }

    public void setOldDept(Long oldDept)
    {
        this.oldDept = oldDept;
    }

    public Long getTotalDept()
    {
        return totalDept;
    }

    public void setTotalDept(Long totalDept)
    {
        this.totalDept = totalDept;
    }

//    public Long getPaymentValue() {
//        return paymentValue;
//    }
//
//    public void setPaymentValue(Long paymentValue) {
//        this.paymentValue = paymentValue;
//    }

    public List<PaymentPackObj> getLstPaymentPackObjs()
    {
        return lstPaymentPackObjs;
    }

    public void setLstPaymentPackObjs(List<PaymentPackObj> lstPaymentPackObjs)
    {
        this.lstPaymentPackObjs = lstPaymentPackObjs;
    }

}
