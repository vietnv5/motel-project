/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.object;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.util.Date;
import javax.persistence.Transient;

/**
 * @author SLOOK. JSC on Nov 7, 2017 2:24:47 PM
 */
public class PaymentGroupPackForm
{

    private Long tongTien;
    private Long tongNo;
    private Long daNop;
    private Long soLuong;
    private Long customerType;
    private String customerTypeName;

    //thong tin goi
    private String groupPackName;
    private Long groupPackId;
    private String key;

    // thong tin nhan vien
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String telephone;
    private String keyEmployeeId;

    //thong tin khach hang
    private Long customerId;
    private String customerName;
    private String phoneNumber;
    private String keyCustomerId;

    //THONG KE ky thuat vien
    private String keyServiceTicket;
    private String empTelephone;
    private Long jobTitleId;
    private String jobTitleName;


    public Long getTongTien()
    {
        return tongTien;
    }

    public void setTongTien(Long tongTien)
    {
        this.tongTien = tongTien;
    }

    public Long getTongNo()
    {
        return tongNo;
    }

    public void setTongNo(Long tongNo)
    {
        this.tongNo = tongNo;
    }

    public Long getDaNop()
    {
        return daNop;
    }

    public void setDaNop(Long daNop)
    {
        this.daNop = daNop;
    }

    public String getGroupPackName()
    {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName)
    {
        this.groupPackName = groupPackName;
    }

    public Long getGroupPackId()
    {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId)
    {
        this.groupPackId = groupPackId;
    }

    public Long getSoLuong()
    {
        return soLuong;
    }

    public void setSoLuong(Long soLuong)
    {
        this.soLuong = soLuong;
    }

    public Long getCustomerType()
    {
        return customerType;
    }

    public void setCustomerType(Long customerType)
    {
        this.customerType = customerType;
    }

    public String getKey()
    {
        key = groupPackId + "_" + (customerType != null ? customerType : "");
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    @Transient
    public String getCustomerTypeName()
    {
        if (Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type2");
        }
        else if (Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type1");
        }
        else if (Constant.CUSTOMER_CHECKIN.TYPE_GROUP_MEMBER.equals(customerType))
        {
            customerTypeName = MessageUtil.getResourceBundleMessage("customerCheckin.type3");
        }

        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName)
    {
        this.customerTypeName = customerTypeName;
    }

    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode()
    {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode)
    {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getKeyEmployeeId()
    {
        keyEmployeeId = "";
        if (employeeId != null)
        {
            keyEmployeeId += employeeId;
        }
        keyEmployeeId += "_";
        if (customerType != null)
        {
            keyEmployeeId += customerType;
        }
        keyEmployeeId += "_";
        if (groupPackId != null)
        {
            keyEmployeeId += groupPackId;
        }
        return keyEmployeeId;
    }

    public void setKeyEmployeeId(String keyEmployeeId)
    {

        this.keyEmployeeId = keyEmployeeId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getKeyCustomerId()
    {
        keyCustomerId = "";
        if (customerId != null)
        {
            keyCustomerId += customerId;
        }
        keyCustomerId += "_";
        if (customerType != null)
        {
            keyCustomerId += customerType;
        }
        return keyCustomerId;
    }

    public void setKeyCustomerId(String keyCustomerId)
    {
        this.keyCustomerId = keyCustomerId;
    }

    public String getKeyServiceTicket()
    {
        return keyServiceTicket;
    }

    public void setKeyServiceTicket(String keyServiceTicket)
    {
        this.keyServiceTicket = keyServiceTicket;
    }

    public String getEmpTelephone()
    {
        return empTelephone;
    }

    public void setEmpTelephone(String empTelephone)
    {
        this.empTelephone = empTelephone;
    }

    public Long getJobTitleId()
    {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId)
    {
        this.jobTitleId = jobTitleId;
    }

    public String getJobTitleName()
    {
        return jobTitleName;
    }

    public void setJobTitleName(String jobTitleName)
    {
        this.jobTitleName = jobTitleName;
    }

}
