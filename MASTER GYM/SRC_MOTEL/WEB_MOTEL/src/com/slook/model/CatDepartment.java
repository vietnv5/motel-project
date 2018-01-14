package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/24/2017.
 */
@Entity
@Table(name = "CAT_DEPARTMENT")
public class CatDepartment {
    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private Long status;
    private String statusName;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "DEPARTMENT_SQL", allocationSize = 1)
    @Column(name = "DEPARTMENT_ID" , nullable = false, precision = 0)
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "DEPARTMENT_CODE")
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Basic
    @Column(name = "DEPARTMENT_NAME")
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatDepartment that = (CatDepartment) o;

        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;
        if (departmentCode != null ? !departmentCode.equals(that.departmentCode) : that.departmentCode != null)
            return false;
        if (departmentName != null ? !departmentName.equals(that.departmentName) : that.departmentName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = departmentId != null ? departmentId.hashCode() : 0;
        result = 31 * result + (departmentCode != null ? departmentCode.hashCode() : 0);
        result = 31 * result + (departmentName != null ? departmentName.hashCode() : 0);
        return result;
    }


    @Column(name = "status", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Transient
    public String getStatusName() {
        if(status!=null){
            if(status.equals(Constant.STATUS.INACTIVE))statusName= MessageUtil.getResourceBundleMessage("common.INACTIVE");
            else if(status.equals(Constant.STATUS.ACTIVE))statusName= MessageUtil.getResourceBundleMessage("common.ACTIVE");
            else if(status.equals(Constant.STATUS.PAUSE))statusName= MessageUtil.getResourceBundleMessage("common.PAUSE");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
