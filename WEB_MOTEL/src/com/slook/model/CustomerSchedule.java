/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "CUSTOMER_SCHEDULE")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerSchedule
{

    private Date startTime;
    private Long status;
    private Date endTime;
    private Date insertTime;
    private Long empId;
    private Long customerScheduleId;
    private Long customerType;
    private Long customerId;
    private String description;

    private List<CustomerSchedulePack> lstCustomerSchedulePack;
    private Member member;
    private List<Membership> lstMembership;
    private Client client;
    private List<ClientUsePack> lstClientUsePack;
    private String statusName;
    private List<CatGroupPack> lstCatGroupPacks;

    private String phoneNumber;
    private String name;
    private String sex;
    private String code;
    private Employee employee;
    private String customerTypeName;
    private Long branchId;
    private String lstPackName;
    private CatBranch catBranch;
    private Long employeeId;
    private Employee employeeSchedule;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME", length = 11)
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "END_TIME", length = 11)
    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "INSERT_TIME", length = 11)
    public Date getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(Date insertTime)
    {
        this.insertTime = insertTime;
    }

    @Column(name = "EMP_ID", precision = 22, scale = 0)
    public Long getEmpId()
    {
        return empId;
    }

    public void setEmpId(Long empId)
    {
        this.empId = empId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "CUSTOMER_SCHEDULE_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CUSTOMER_SCHEDULE_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getCustomerScheduleId()
    {
        return customerScheduleId;
    }

    public void setCustomerScheduleId(Long customerScheduleId)
    {
        this.customerScheduleId = customerScheduleId;
    }

    @Column(name = "CUSTOMER_TYPE", precision = 22, scale = 0)
    public Long getCustomerType()
    {
        return customerType;
    }

    public void setCustomerType(Long customerType)
    {
        this.customerType = customerType;
    }

    @Column(name = "CUSTOMER_ID", precision = 22, scale = 0)
    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    @Column(name = "CODE", length = 50)
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    @Column(name = "DESCRIPTION", length = 750)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public CustomerSchedule()
    {
    }

    public CustomerSchedule(Long customerScheduleId)
    {
        this.customerScheduleId = customerScheduleId;
    }

    @Transient
    public List<CustomerSchedulePack> getLstCustomerSchedulePack()
    {
        if (customerScheduleId != null && lstCustomerSchedulePack == null)
        {
            try
            {
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("customerScheduleId", customerScheduleId);
                lstCustomerSchedulePack = new GenericDaoImplNewV2<CustomerSchedulePack, Long>()
                {
                }.findList(filter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return lstCustomerSchedulePack;
    }

    public void setLstCustomerSchedulePack(List<CustomerSchedulePack> lstCustomerSchedulePack)
    {
        this.lstCustomerSchedulePack = lstCustomerSchedulePack;
    }

    @Transient
    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

    @Transient
    public List<Membership> getLstMembership()
    {
        return lstMembership;
    }

    public void setLstMembership(List<Membership> lstMembership)
    {
        this.lstMembership = lstMembership;
    }

    @Transient
    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    @Transient
    public List<ClientUsePack> getLstClientUsePack()
    {
        return lstClientUsePack;
    }

    public void setLstClientUsePack(List<ClientUsePack> lstClientUsePack)
    {
        this.lstClientUsePack = lstClientUsePack;
    }

    @Transient
    public String getStatusName()
    {
        if (Constant.CUSTOMER_SCHEDULE.STATUS_SCHEDULE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerSchedule.statusSchedule");

        }
        else if (Constant.CUSTOMER_SCHEDULE.STATUS_ACTIVE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerSchedule.statusActive");

        }
        else if (Constant.CUSTOMER_SCHEDULE.STATUS_COMPLETED.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerSchedule.statusCompleted");

        }
        else if (Constant.CUSTOMER_SCHEDULE.STATUS_CANCEL.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("customerSchedule.statusCancel");
        }

        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    @Transient
    public List<CatGroupPack> getLstCatGroupPacks()
    {
        return lstCatGroupPacks;
    }

    public void setLstCatGroupPacks(List<CatGroupPack> lstCatGroupPacks)
    {
        this.lstCatGroupPacks = lstCatGroupPacks;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "PHONE_NUMBER", length = 50)
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "name", length = 50)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "SEX", length = 50)
    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    @Basic
    @Column(name = "BRANCH_ID", nullable = true, precision = 0)
    public Long getBranchId()
    {
        return branchId;
    }

    public void setBranchId(Long branchId)
    {
        this.branchId = branchId;
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

        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName)
    {
        this.customerTypeName = customerTypeName;
    }

    @Transient
    public String getLstPackName()
    {
        getLstCustomerSchedulePack();//init
        if (lstCustomerSchedulePack != null && lstCustomerSchedulePack.size() > 0)
        {
            String name = "";
            for (CustomerSchedulePack bo : lstCustomerSchedulePack)
            {
                if (bo.getCatGroupPack() != null && bo.getCatGroupPack().getGroupPackName() != null)
                {
                    name += bo.getCatGroupPack().getGroupPackName() + ",";
                }
            }
            if (name.endsWith(","))
            {
                name = name.substring(0, name.length() - 1);
            }
            lstPackName = name;
        }
        return lstPackName;
    }

    public void setLstPackName(String lstPackName)
    {
        this.lstPackName = lstPackName;
    }

    @Transient
    public CatBranch getCatBranch()
    {
        if (branchId != null && catBranch == null)
        {
            try
            {
                catBranch = new GenericDaoImplNewV2<CatBranch, Long>()
                {
                }.findById(branchId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return catBranch;
    }

    public void setCatBranch(CatBranch catBranch)
    {
        this.catBranch = catBranch;
    }

    @Column(name = "EMPLOYEE_ID", nullable = true, precision = 0)
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getEmployeeSchedule()
    {
        return employeeSchedule;
    }

    public void setEmployeeSchedule(Employee employeeSchedule)
    {
        this.employeeSchedule = employeeSchedule;
    }

}
