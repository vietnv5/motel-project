package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/19/2017.
 */
@Entity
public class Employee {
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private Long jobTitleId;
    private CatJobTitle jobTitle;
    private String checkInCode;
    private String idNum;
    private Date birthDate;
    private String birthPlace;
    private Date joinDate;
    private String telephone;
    private String permanentAddress;
    private String presentAddress;
    private Long sex;
    private String personId;
    private Date issuedDate;
    private String personPlace;
    private String marriedStatus;
    private String nation;
    private String ethenic;
    private String religion;
    private String education;

    private Long contractType;
    private String contractNo;
    private Date beginContract;
    private Date endContract;
    private Long statusId;
    private Date leftDate;
    private Long branchId;

    Long departmentId;
    CatDepartment department;
//    EmployeeStatus status;
    CatBranch branch;

    @Id
    @Column(name = "EMPLOYEE_ID")
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "EMPLOYEE_SEQ", allocationSize = 1)

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "EMPLOYEE_CODE")
    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    @Basic
    @Column(name = "EMPLOYEE_NAME")
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Column(name = "Check_In_Code")
    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }

    @Column(name = "ID_NUM")
    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }


    @Column(name = "Birth_Date")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }


    @Column(name = "Birth_Place")
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }


    @Column(name = "Join_Date")
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Column(name = "Telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    @Column(name = "Permanent_Address")
    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    @Column(name = "Present_Address")
    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    @Column(name = "sex")
    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    @Column(name = "PERSON_ID")
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Column(name = "ISSUED_DATE")
    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    @Column(name = "PERSON_PLACE")
    public String getPersonPlace() {
        return personPlace;
    }

    public void setPersonPlace(String personPlace) {
        this.personPlace = personPlace;
    }

    @Column(name = "MARRIED_STATUS")
    public String getMarriedStatus() {
        return marriedStatus;
    }

    public void setMarriedStatus(String marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    @Column(name = "NATION")
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    @Column(name = "ETHENIC")
    public String getEthenic() {
        return ethenic;
    }

    public void setEthenic(String ethenic) {
        this.ethenic = ethenic;
    }

    @Column(name = "RELIGION")
    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    @Column(name = "EDUCATION")
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Column(name = "STATUS_ID")
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

//    @ManyToOne
//    @JoinColumn(name = "STATUS_ID", insertable = false, updatable = false)
//    public EmployeeStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(EmployeeStatus status) {
//        this.status = status;
//    }

    @Column(name = "LEFT_DATE")
    public Date getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(Date leftDate) {
        this.leftDate = leftDate;
    }

    @Column(name = "CONTRACT_TYPE")
    public Long getContractType() {
        return contractType;
    }

    public void setContractType(Long contractType) {
        this.contractType = contractType;
    }

    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Column(name = "BEGIN_CONTRACT")
    public Date getBeginContract() {
        return beginContract;
    }

    public void setBeginContract(Date beginContract) {
        this.beginContract = beginContract;
    }

    @Column(name = "END_CONTRACT")
    public Date getEndContract() {
        return endContract;
    }

    public void setEndContract(Date endContract) {
        this.endContract = endContract;
    }

    @Basic
    @Column(name = "JOB_TITLE_ID")
    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    @ManyToOne
    @JoinColumn(name = "JOB_TITLE_ID", insertable = false, updatable = false)
    public CatJobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(CatJobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Column(name = "DEPARTMENT_ID")
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID", insertable = false, updatable = false)
    public CatDepartment getDepartment() {
        return department;
    }

    public void setDepartment(CatDepartment department) {
        this.department = department;
    }

    @Basic
    @Column(name = "BRANCH_ID")
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID", insertable = false, updatable = false)
    public CatBranch getBranch() {
        return branch;
    }

    public void setBranch(CatBranch branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (employeeId != null ? !employeeId.equals(employee.employeeId) : employee.employeeId != null) return false;
        if (employeeCode != null ? !employeeCode.equals(employee.employeeCode) : employee.employeeCode != null)
            return false;
        if (employeeName != null ? !employeeName.equals(employee.employeeName) : employee.employeeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = employeeId != null ? employeeId.hashCode() : 0;
        result = 31 * result + (employeeCode != null ? employeeCode.hashCode() : 0);
        result = 31 * result + (employeeName != null ? employeeName.hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
