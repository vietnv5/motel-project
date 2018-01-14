package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/19/2017.
 */
@Entity
@Table(name = "CAT_JOB_TITLE")
public class CatJobTitle {
    private Long jobTitleId;
    private String jobTitleCode;
    private String jobTitleName;
    private Long status;
    private String statusName;
    @Id
    @Column(name = "JOB_TITLE_ID")
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "JOB_TITLE_SEQ", allocationSize = 1)
    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    @Basic
    @Column(name = "JOB_TITLE_CODE")
    public String getJobTitleCode() {
        return jobTitleCode;
    }

    public void setJobTitleCode(String jobTitleCode) {
        this.jobTitleCode = jobTitleCode;
    }

    @Basic
    @Column(name = "JOB_TITLE_NAME")
    public String getJobTitleName() {
        return jobTitleName;
    }

    public void setJobTitleName(String jobTitleName) {
        this.jobTitleName = jobTitleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatJobTitle that = (CatJobTitle) o;

        if (jobTitleId != null ? !jobTitleId.equals(that.jobTitleId) : that.jobTitleId != null) return false;
        if (jobTitleCode != null ? !jobTitleCode.equals(that.jobTitleCode) : that.jobTitleCode != null) return false;
        if (jobTitleName != null ? !jobTitleName.equals(that.jobTitleName) : that.jobTitleName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jobTitleId != null ? jobTitleId.hashCode() : 0;
        result = 31 * result + (jobTitleCode != null ? jobTitleCode.hashCode() : 0);
        result = 31 * result + (jobTitleName != null ? jobTitleName.hashCode() : 0);
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
