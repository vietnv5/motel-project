package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_BRANCH", catalog = "")
public class CatBranch
{
    private Long branchId;
    private String branchCode;
    private String branchName;
    private String description;

    private Long status;
    private String statusName;

    @Id
    @Column(name = "BRANCH_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "BRANCH_SEQ", allocationSize = 1)
    public Long getBranchId()
    {
        return branchId;
    }

    public void setBranchId(Long branchId)
    {
        this.branchId = branchId;
    }

    @Basic
    @Column(name = "BRANCH_CODE", nullable = true, length = 20)
    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    @Basic
    @Column(name = "BRANCH_NAME", nullable = true, length = 100)
    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 300)
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

        CatBranch catBranch = (CatBranch) o;

        if (branchId != null ? !branchId.equals(catBranch.branchId) : catBranch.branchId != null)
        {
            return false;
        }
        if (branchCode != null ? !branchCode.equals(catBranch.branchCode) : catBranch.branchCode != null)
        {
            return false;
        }
        if (branchName != null ? !branchName.equals(catBranch.branchName) : catBranch.branchName != null)
        {
            return false;
        }
        if (description != null ? !description.equals(catBranch.description) : catBranch.description != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = branchId != null ? branchId.hashCode() : 0;
        result = 31 * result + (branchCode != null ? branchCode.hashCode() : 0);
        result = 31 * result + (branchName != null ? branchName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Column(name = "status", nullable = true, precision = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Transient
    public String getStatusName()
    {
        if (status != null)
        {
            if (status.equals(Constant.STATUS.INACTIVE))
            {
                statusName = MessageUtil.getResourceBundleMessage("common.INACTIVE");
            }
            else if (status.equals(Constant.STATUS.ACTIVE))
            {
                statusName = MessageUtil.getResourceBundleMessage("common.ACTIVE");
            }
            else if (status.equals(Constant.STATUS.PAUSE))
            {
                statusName = MessageUtil.getResourceBundleMessage("common.PAUSE");
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName)
    {
        this.statusName = statusName;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
