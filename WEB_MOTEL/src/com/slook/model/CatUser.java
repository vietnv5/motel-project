package com.slook.model;

import com.motel.model.GroupUser;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/12/2017.
 */
@Entity
@Table(name = "cat_user")
public class CatUser implements Serializable {

    private Long userId;
    private String userName;
    private String password;
    private Long roleId;
    private CatRole role;
    private Long empId;
    private Employee employee;
    private Long status;
    private String statusName;
    private String fullName;
    private String confirmPassword;

    //change password
    private String oldPassword;
    private String newPassword;
    private Long groupUserId;

    private GroupUser groupUser;

    // thong tin bo sung
    private Long sex;
    private Date birthDate;
    private String address;
    private String phoneNumber;
    private String description;
    private String email;
    private String job;

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID", insertable = false, updatable = false)
    public CatRole getRole() {
        return role;
    }

    public void setRole(CatRole role) {
        this.role = role;
    }
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "EMP_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }

    @Transient
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//    @SequenceGenerator(name = "generator", sequenceName = "CAT_USER_SEQ", allocationSize = 1)
    @Id
//    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, precision = 0)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "USER_NAME", nullable = true, length = 20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 200)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "ROLE_ID", nullable = true, precision = 0)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "EMP_ID", nullable = true, precision = 0)
    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    @Basic
    @Column(name = "status", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Transient
    public String getStatusName() {
        if (status != null) {
            if (status.equals(0l)) {
                statusName = "Hủy";
            } else if (status.equals(1l)) {
                statusName = "Hoạt động";
            } else if (status.equals(2l)) {
                statusName = "Tạm ngưng";
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Column(name = "full_name", nullable = true, length = 250)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Transient
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Transient
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Column(name = "GROUP_USER_ID")
    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
    }

    @JoinColumn(name = "GROUP_USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    public GroupUser getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(GroupUser groupUser) {
        this.groupUser = groupUser;
    }

    @Column(name = "SEX")
    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "JOB")
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

}
