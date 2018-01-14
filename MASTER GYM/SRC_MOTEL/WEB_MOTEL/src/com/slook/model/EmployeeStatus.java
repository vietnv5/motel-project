package com.slook.model;

import javax.persistence.*;

/**
 * Created by Antz01 on 14/06/2017.
 */
@Entity
@Table(name = "EMPLOYEE_STATUS", schema = "GYM_SERVICE", catalog = "")
public class EmployeeStatus {
    private Long statusId;
    private String statusName;

    @Id
    @Column(name = "STATUS_ID")
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "STATUS_NAME")
    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeStatus that = (EmployeeStatus) o;

        if (statusId != null ? !statusId.equals(that.statusId) : that.statusId != null) return false;
        if (statusName != null ? !statusName.equals(that.statusName) : that.statusName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusId != null ? statusId.hashCode() : 0;
        result = 31 * result + (statusName != null ? statusName.hashCode() : 0);
        return result;
    }
}
