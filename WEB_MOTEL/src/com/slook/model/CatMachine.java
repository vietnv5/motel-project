package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_MACHINE", catalog = "")
public class CatMachine
{

    private Long machineId;
    private String machineName;
    private String machineCode;
    private Long branchId;
    private String description;
    private String ip;
    private String port;
    private Long status;
    private String statusName;
    private Long machineType;
    private CatItemBO machineTypeBO;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "CAT_MACHINE_SEQ", allocationSize = 1)
    @Column(name = "MACHINE_ID", nullable = false, precision = 0)
    public Long getMachineId()
    {
        return machineId;
    }

    public void setMachineId(Long machineId)
    {
        this.machineId = machineId;
    }

    @Basic
    @Column(name = "MACHINE_NAME", nullable = true, length = 30)
    public String getMachineName()
    {
        return machineName;
    }

    public void setMachineName(String machineName)
    {
        this.machineName = machineName;
    }

    @Basic
    @Column(name = "MACHINE_CODE", nullable = true, length = 20)
    public String getMachineCode()
    {
        return machineCode;
    }

    public void setMachineCode(String machineCode)
    {
        this.machineCode = machineCode;
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

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 50)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

        CatMachine that = (CatMachine) o;

        if (machineId != null ? !machineId.equals(that.machineId) : that.machineId != null)
        {
            return false;
        }
        if (machineName != null ? !machineName.equals(that.machineName) : that.machineName != null)
        {
            return false;
        }
        if (machineCode != null ? !machineCode.equals(that.machineCode) : that.machineCode != null)
        {
            return false;
        }
        if (branchId != null ? !branchId.equals(that.branchId) : that.branchId != null)
        {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = machineId != null ? machineId.hashCode() : 0;
        result = 31 * result + (machineName != null ? machineName.hashCode() : 0);
        result = 31 * result + (machineCode != null ? machineCode.hashCode() : 0);
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Column
    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    @Column
    public String getPort()
    {
        return port;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column
    public Long getStatus()
    {
        return status;
    }

    @Transient
    public String getStatusName()
    {
        if (status != null)
        {
            if (status.equals(0l))
            {
                statusName = "Không sử dụng";
            }
            else if (status.equals(1l))
            {
                statusName = "Đang hoạt động";
            }
            else if (status.equals(2l))
            {
                statusName = "Tạm ngừng";
            }
        }
        return statusName;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "machine_type", nullable = true, precision = 0)
    public Long getMachineType()
    {
        return machineType;
    }

    public void setMachineType(Long machineType)
    {
        this.machineType = machineType;
    }

    @ManyToOne
    @JoinColumn(name = "machine_type", insertable = false, updatable = false)
    public CatItemBO getMachineTypeBO()
    {
        return machineTypeBO;
    }

    public void setMachineTypeBO(CatItemBO machineTypeBO)
    {
        this.machineTypeBO = machineTypeBO;
    }
}
