package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.HibernateUtil;
import com.slook.util.MessageUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.type.StandardBasicTypes;

@Entity
@Table(name = "GROUP_MEMBER")
public class GroupMember {

    private String groupMemberName;
    private Long groupMemberId;
    private Long status;
    private Date joinDate;
    private String groupMemberCode;
    private Date endDate;
    private Long branchId;
    private Long deputationMemberId;
    private CatBranch catBranch;
//    private Member member;
    private Member deputationMember;

    List<GroupMemberPayment> groupMemberPayments = new ArrayList<>(0);
    private List<GroupHasMember> lstGroupHasMembers;
    private List<GroupMembership> groupMemberships = new ArrayList<>();
    List<GroupMemberPromotion> groupMemberPromotions = new ArrayList<>(0);

    private GroupMembership newGroupMembership = new GroupMembership();
    private GroupMemberPayment groupMemberPayment = new GroupMemberPayment();
    private GroupMemberPromotion groupMemberPromotion = new GroupMemberPromotion();

    private Long totalPayment;
    private String statusName;

    private Long numMemberInGroup;
    private String lstCardCodeStr;
    private Long employeeId;
        private Employee employee;

    
    @Column(name = "GROUP_MEMBER_NAME", length = 255)
    public String getGroupMemberName() {
        return groupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        this.groupMemberName = groupMemberName;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_MEMBER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "GROUP_MEMBER_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "JOIN_DATE", length = 7)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Column(name = "GROUP_MEMBER_CODE", length = 255)
    public String getGroupMemberCode() {
        return groupMemberCode;
    }

    public void setGroupMemberCode(String groupMemberCode) {
        this.groupMemberCode = groupMemberCode;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "BRANCH_ID", precision = 22, scale = 0)
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Column(name = "DEPUTATION_MEMBER_ID", precision = 22, scale = 0)
    public Long getDeputationMemberId() {
        return deputationMemberId;
    }

    public void setDeputationMemberId(Long deputationMemberId) {
        this.deputationMemberId = deputationMemberId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BRANCH_ID", insertable = false, updatable = false)
    public CatBranch getCatBranch() {
        return catBranch;
    }

    public void setCatBranch(CatBranch catBranch) {
        this.catBranch = catBranch;
    }

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "DEPUTATION_MEMBER_ID", referencedColumnName = "member_id", insertable = false, updatable = false)
//    public Member getMember() {
//        return member;
//    }
//
//    public void setMember(Member member) {
//        this.member = member;
//    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "DEPUTATION_MEMBER_ID", referencedColumnName = "member_id", insertable = false, updatable = false)
    public Member getDeputationMember() {
        return deputationMember;
    }

    public void setDeputationMember(Member deputationMember) {
        this.deputationMember = deputationMember;
    }

    public GroupMember() {
    }

    public GroupMember(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    @Transient
    public List<GroupMemberPayment> getGroupMemberPayments() {
        return groupMemberPayments;
    }

    public void setGroupMemberPayments(List<GroupMemberPayment> groupMemberPayments) {
        this.groupMemberPayments = groupMemberPayments;
    }

    @Transient
    public List<GroupHasMember> getLstGroupHasMembers() {
        return lstGroupHasMembers;
    }

    public void setLstGroupHasMembers(List<GroupHasMember> lstGroupHasMembers) {
        this.lstGroupHasMembers = lstGroupHasMembers;
    }

    @Transient
    public List<GroupMembership> getGroupMemberships() {
        return groupMemberships;
    }

    public void setGroupMemberships(List<GroupMembership> groupMemberships) {
        this.groupMemberships = groupMemberships;
    }

    @Transient
    public GroupMembership getNewGroupMembership() {
        return newGroupMembership;
    }

    public void setNewGroupMembership(GroupMembership newGroupMembership) {
        this.newGroupMembership = newGroupMembership;
    }

    @Transient
    public GroupMemberPayment getGroupMemberPayment() {
        return groupMemberPayment;
    }

    public void setGroupMemberPayment(GroupMemberPayment groupMemberPayment) {
        this.groupMemberPayment = groupMemberPayment;
    }

    @Transient
    public GroupMemberPromotion getGroupMemberPromotion() {
        return groupMemberPromotion;
    }

    public void setGroupMemberPromotion(GroupMemberPromotion groupMemberPromotion) {
        this.groupMemberPromotion = groupMemberPromotion;
    }

    @Transient
    public Long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Long totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Transient
    public String getStatusName() {
        if (Constant.EMPLOYEE_STATUS.NEW.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status0");
        } else if (Constant.EMPLOYEE_STATUS.ACTIVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status1");
        } else if (Constant.EMPLOYEE_STATUS.PAUSE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("view.label.status2");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Transient
    public String getLstCardCodeStr() {
        if ( lstCardCodeStr == null) {
            getNumMemberInGroup();
        }
        return lstCardCodeStr;
    }

    public void setLstCardCodeStr(String lstCardCodeStr) {
        this.lstCardCodeStr = lstCardCodeStr;
    }

    @Transient
    public Long getNumMemberInGroup() {
            String sqlCheckCode = "select count(*) numMember,wm_concat(b.card_code) cardCode from group_has_member a \n"
                    + "left join member b on a.member_id=b.member_id  where group_Member_Id =? group by group_Member_Id ";
            Session session = HibernateUtil.openSession();
            try {
                SQLQuery query = session.createSQLQuery(sqlCheckCode).addScalar("numMember", StandardBasicTypes.LONG).addScalar("cardCode", StandardBasicTypes.MATERIALIZED_CLOB);
                query.setParameter(0, groupMemberId);
                List lst = query.list();
                if (lst.size() > 0) {
                    Object[] col = (Object[]) lst.get(0);
                    numMemberInGroup = Long.valueOf(col[0].toString());
                    lstCardCodeStr = col[1].toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                session.close();
            }
        
        return numMemberInGroup;
    }

    public void setNumMemberInGroup(Long numMemberInGroup) {
        this.numMemberInGroup = numMemberInGroup;
    }

    @Transient
    public List<GroupMemberPromotion> getGroupMemberPromotions() {
        return groupMemberPromotions;
    }

    public void setGroupMemberPromotions(List<GroupMemberPromotion> groupMemberPromotions) {
        this.groupMemberPromotions = groupMemberPromotions;
    }
        @Column(name = "EMPLOYEE_ID", nullable = true, precision = 0)
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
