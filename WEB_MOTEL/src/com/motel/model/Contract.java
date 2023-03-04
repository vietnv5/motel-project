/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author VietNV
 */
@Entity
@Table(name = "contract")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c")
        , @NamedQuery(name = "Contract.findByContractId", query = "SELECT c FROM Contract c WHERE c.contractId = :contractId")
        , @NamedQuery(name = "Contract.findByContractCode", query = "SELECT c FROM Contract c WHERE c.contractCode = :contractCode")
        , @NamedQuery(name = "Contract.findByHomeId", query = "SELECT c FROM Contract c WHERE c.homeId = :homeId")
        , @NamedQuery(name = "Contract.findByRoomId", query = "SELECT c FROM Contract c WHERE c.roomId = :roomId")
        , @NamedQuery(name = "Contract.findByStartTime", query = "SELECT c FROM Contract c WHERE c.startTime = :startTime")
        , @NamedQuery(name = "Contract.findByEndTime", query = "SELECT c FROM Contract c WHERE c.endTime = :endTime")
        , @NamedQuery(name = "Contract.findByDeposit", query = "SELECT c FROM Contract c WHERE c.deposit = :deposit")
        , @NamedQuery(name = "Contract.findByDescription", query = "SELECT c FROM Contract c WHERE c.description = :description")
        , @NamedQuery(name = "Contract.findByStatus", query = "SELECT c FROM Contract c WHERE c.status = :status")
        , @NamedQuery(name = "Contract.findByCreateTime", query = "SELECT c FROM Contract c WHERE c.createTime = :createTime")})
public class Contract implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CONTRACT_ID")
    private Long contractId;
    @Column(name = "CONTRACT_CODE")
    private String contractCode;
    @Basic(optional = false)
    @Column(name = "HOME_ID")
    private Long homeId;
    @Basic(optional = false)
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.DATE)
    private Date startTime;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.DATE)
    private Date endTime;
    @Column(name = "DEPOSIT")
    private Long deposit;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Basic(optional = false)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractId")
    private List<ContractService> contractServiceList;
    @JoinColumn(name = "HOME_ID", referencedColumnName = "HOME_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Home home;
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ROOM_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Room room;
    @Transient
    private List<Long> lstServiceId;
    @Transient
    private String statusName;

    public Contract()
    {
    }

    public Contract(Long contractId)
    {
        this.contractId = contractId;
    }

    public Contract(Long contractId, Long homeId, Long roomId)
    {
        this.contractId = contractId;
        this.homeId = homeId;
        this.roomId = roomId;
    }

    public Long getContractId()
    {
        return contractId;
    }

    public void setContractId(Long contractId)
    {
        this.contractId = contractId;
    }

    public String getContractCode()
    {
        return contractCode;
    }

    public void setContractCode(String contractCode)
    {
        this.contractCode = contractCode;
    }

    public Long getHomeId()
    {
        return homeId;
    }

    public void setHomeId(Long homeId)
    {
        this.homeId = homeId;
    }

    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Long getDeposit()
    {
        return deposit;
    }

    public void setDeposit(Long deposit)
    {
        this.deposit = deposit;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    @XmlTransient
    public List<ContractService> getContractServiceList()
    {
        return contractServiceList;
    }

    public void setContractServiceList(List<ContractService> contractServiceList)
    {
        this.contractServiceList = contractServiceList;
    }

    public Home getHome()
    {
        return home;
    }

    public void setHome(Home home)
    {
        this.home = home;
    }

    public List<Long> getLstServiceId()
    {
        return lstServiceId;
    }

    public void setLstServiceId(List<Long> lstServiceId)
    {
        this.lstServiceId = lstServiceId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    @Override
    public String toString()
    {
        return "model.Contract[ contractId=" + contractId + " ]";
    }

    public Room getRoom()
    {
        return room;
    }

    public void setRoom(Room room)
    {
        this.room = room;
    }

    public String getStatusName()
    {
        if (Constant.CONTRACT.STATUS_ACTIVE.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("contract.status.STATUS_ACTIVE");
        }
        else if (Constant.CONTRACT.STATUS_END.equals(status))
        {
            statusName = MessageUtil.getResourceBundleMessage("contract.status.STATUS_END");
        }
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    public void createContractCode(Long groupUseId)
    {

//        String hqlCheckCode = "select count(*) from MemberPayment where paymentCode like ?||'%'";
//        String hqlCheckCode = "select max(CONVERT( substr(CONTRACT_CODE,-5),UNSIGNED INTEGER)) from contract where CONTRACT_CODE like CONCAT(?,'%')";
        String hqlCheckCode = "select max(CONVERT( substr(CONTRACT_CODE,INSTR(CONTRACT_CODE, '-')+1),UNSIGNED INTEGER)) from contract where CONTRACT_CODE like CONCAT(?,'%')";
        String code = "HD";
        String barch = "000";
        if (groupUseId != null)
        {
            if (groupUseId.toString().length() < 3)
            {
                barch = barch + groupUseId.toString();
                code += barch.substring(barch.length() - 3);
            }
            else
            {
                code += groupUseId.toString();
            }
        }
        else
        {
            code += barch;
        }

        List<?> counts = new GenericDaoImplNewV2<Contract, Long>()
        {
        }.findListSQLAll(hqlCheckCode, code);
        String numberStr = "00000";
        if (counts.size() > 0 && counts.get(0) != null)
        {
            Long number = (Long.valueOf(counts.get(0).toString()) + 1);
            numberStr += number;
            numberStr = numberStr.substring(numberStr.length() - 5);
        }
        else
        {
            numberStr = "00001";
        }
        contractCode = code + "-" + numberStr;
    }

}
