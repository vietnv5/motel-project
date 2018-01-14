package com.slook.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/15/2017.
 */
@Entity
@Table(name = "TASK_CARE")
public class TaskCare {
    private Long taskCareId;
    private Long employeeId;
    private CatUser user;
    private Long type;
    private String name;

    @Id
    @Column(name = "TASK_CARE_ID")
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "TASK_CARE_SEQ", allocationSize = 1)
    public Long getTaskCareId() {
        return taskCareId;
    }

    public void setTaskCareId(Long taskCareId) {
        this.taskCareId = taskCareId;
    }

    @Basic
    @Column(name = "EMPLOYEE_ID")
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long userId) {
        this.employeeId = userId;
    }

    @Basic
    @Column(name = "TYPE")
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskCare taskCare = (TaskCare) o;

        if (taskCareId != null ? !taskCareId.equals(taskCare.taskCareId) : taskCare.taskCareId != null) return false;
        if (employeeId != null ? !employeeId.equals(taskCare.employeeId) : taskCare.employeeId != null) return false;
        if (type != null ? !type.equals(taskCare.type) : taskCare.type != null) return false;
        if (name != null ? !name.equals(taskCare.name) : taskCare.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskCareId != null ? taskCareId.hashCode() : 0;
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
