package com.slook.model;

import com.slook.controller.MemberController;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.DateTimeUtils;
import com.slook.util.HibernateUtil;
import com.slook.util.MessageUtil;
import java.io.BufferedReader;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.primefaces.model.UploadedFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
public class Member {

    private Long memberId;
    private String memberCode;
    private String memberName;
    private String cardCode;
    private String phoneNumber;
    private String email;
    private String address;
    private String sex;
    private Date birthDay;
    private Date joinDate;
    private Date endDate;
    private Long status;
    private Long point;
    private String memberType;
    private String imgPath;
    private Long branchId;
    private UploadedFile imageFile;
    private Boolean isPreviewProfile;

    private CatBranch branch;

    private Long type;
    private Long deputationMemberId;
//    private Member deputationMember;

    private List<Membership> memberships = new ArrayList<>();
    private Membership newMembership = new Membership();

    List<MemberCheckin> memberCheckins = new ArrayList<>(0);
    List<MemberPayment> memberPayments = new ArrayList<>(0);
    List<MemberPromotion> memberPromotions = new ArrayList<>(0);
    List<MemberHealth> memberHealths = new ArrayList<>(0);
    List<MemberCare> memberCares = new ArrayList<>(0);

    private MemberHealth newMemberHealth = new MemberHealth();

    private MemberPayment memberPayment = new MemberPayment();
    private MemberPromotion memberPromotion = new MemberPromotion();

    private Long totalPayment;

    private List<GroupHasMember> lstGroupHasMembers;
    private String typeName;
    private String statusName;

    private Long numMemberInGroup;
    private String lstCardCodeStr;
    private Long employeeId;
    private Employee employee;

    //van tay
    private String fingerprintPath;
    //goi con hieu luc
    private List<Membership> membershipAvailbles = new ArrayList<>();
    private String membershipAvailblesStr;

    @Id
    @Column(name = "MEMBER_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "MEMBER_SEQ", allocationSize = 1)
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "MEMBER_CODE", nullable = true, length = 50)
    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    @Basic
    @Column(name = "MEMBER_NAME", nullable = true, length = 200)
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Basic
    @Column(name = "CARD_CODE", nullable = true, length = 20)
    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    @Basic
    @Column(name = "PHONE_NUMBER", nullable = true, length = 30)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 20)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "SEX", nullable = true, length = 20)
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_DAY", nullable = true)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "JOIN_DATE", nullable = true)
    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "STATUS", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Basic
    @Column(name = "POINT", nullable = true, precision = 0)
    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    @Basic
    @Column(name = "MEMBER_TYPE", nullable = true, length = 20)
    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    @Basic
    @Column(name = "IMG_PATH", nullable = true, length = 20)
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Basic
    @Column(name = "BRANCH_ID", nullable = true, precision = 0)
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "BRANCH_ID", insertable = false, updatable = false)
    public CatBranch getBranch() {
        return branch;
    }

    public void setBranch(CatBranch branch) {
        this.branch = branch;
    }

    @Column(name = "TYPE", nullable = true, precision = 0)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "deputation_member_id", nullable = true, precision = 0)
    public Long getDeputationMemberId() {
        return deputationMemberId;
    }

    public void setDeputationMemberId(Long deputationMemberId) {
        this.deputationMemberId = deputationMemberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Member member = (Member) o;

        if (memberId != null ? !memberId.equals(member.memberId) : member.memberId != null) {
            return false;
        }
        if (memberCode != null ? !memberCode.equals(member.memberCode) : member.memberCode != null) {
            return false;
        }
        if (memberName != null ? !memberName.equals(member.memberName) : member.memberName != null) {
            return false;
        }
        if (cardCode != null ? !cardCode.equals(member.cardCode) : member.cardCode != null) {
            return false;
        }
        if (phoneNumber != null ? !phoneNumber.equals(member.phoneNumber) : member.phoneNumber != null) {
            return false;
        }
        if (email != null ? !email.equals(member.email) : member.email != null) {
            return false;
        }
        if (address != null ? !address.equals(member.address) : member.address != null) {
            return false;
        }
        if (sex != null ? !sex.equals(member.sex) : member.sex != null) {
            return false;
        }
        if (birthDay != null ? !birthDay.equals(member.birthDay) : member.birthDay != null) {
            return false;
        }
        if (joinDate != null ? !joinDate.equals(member.joinDate) : member.joinDate != null) {
            return false;
        }
        if (endDate != null ? !endDate.equals(member.endDate) : member.endDate != null) {
            return false;
        }
        if (status != null ? !status.equals(member.status) : member.status != null) {
            return false;
        }
        if (point != null ? !point.equals(member.point) : member.point != null) {
            return false;
        }
        if (memberType != null ? !memberType.equals(member.memberType) : member.memberType != null) {
            return false;
        }
        if (imgPath != null ? !imgPath.equals(member.imgPath) : member.imgPath != null) {
            return false;
        }
        if (branchId != null ? !branchId.equals(member.branchId) : member.branchId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = memberId != null ? memberId.hashCode() : 0;
        result = 31 * result + (memberCode != null ? memberCode.hashCode() : 0);
        result = 31 * result + (memberName != null ? memberName.hashCode() : 0);
        result = 31 * result + (cardCode != null ? cardCode.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthDay != null ? birthDay.hashCode() : 0);
        result = 31 * result + (joinDate != null ? joinDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (point != null ? point.hashCode() : 0);
        result = 31 * result + (memberType != null ? memberType.hashCode() : 0);
        result = 31 * result + (imgPath != null ? imgPath.hashCode() : 0);
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        return result;
    }

    @Transient
    public UploadedFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(UploadedFile imageFile) {
        this.imageFile = imageFile;
    }

    @Transient
    public Boolean getPreviewProfile() {
        return isPreviewProfile;
    }

    public void setPreviewProfile(Boolean isPreviewProfile) {
        this.isPreviewProfile = isPreviewProfile;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderBy("joinDate DESC")
    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    @Transient
    public Membership getNewMembership() {
        return newMembership;
    }

    public void setNewMembership(Membership newMembership) {
        this.newMembership = newMembership;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<MemberCheckin> getMemberCheckins() {
        return memberCheckins;
    }

    public void setMemberCheckins(List<MemberCheckin> memberCheckins) {
        this.memberCheckins = memberCheckins;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<MemberPayment> getMemberPayments() {
        return memberPayments;
    }

    public void setMemberPayments(List<MemberPayment> memberPayments) {
        this.memberPayments = memberPayments;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<MemberPromotion> getMemberPromotions() {
        return memberPromotions;
    }

    public void setMemberPromotions(List<MemberPromotion> memberPromotions) {
        this.memberPromotions = memberPromotions;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<MemberHealth> getMemberHealths() {
        return memberHealths;
    }

    public void setMemberHealths(List<MemberHealth> memberHealths) {
        this.memberHealths = memberHealths;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "memberId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<MemberCare> getMemberCares() {
        return memberCares;
    }

    public void setMemberCares(List<MemberCare> memberCares) {
        this.memberCares = memberCares;
    }

//    @OneToOne(fetch = FetchType.EAGER)
//    @LazyCollection(LazyCollectionOption.EXTRA)
//    @JoinColumn(name = "deputation_member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
//    public Member getDeputationMember() {
//        return deputationMember;
//    }
//
//    public void setDeputationMember(Member deputationMember) {
//        this.deputationMember = deputationMember;
//    }
    @Transient
    public MemberHealth getNewMemberHealth() {
        return newMemberHealth;
    }

    public void setNewMemberHealth(MemberHealth newMemberHealth) {
        this.newMemberHealth = newMemberHealth;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Transient
    public MemberPayment getMemberPayment() {
        return memberPayment;
    }

    public void setMemberPayment(MemberPayment memberPayment) {
        this.memberPayment = memberPayment;
    }

    @Transient
    public MemberPromotion getMemberPromotion() {
        return memberPromotion;
    }

    public void setMemberPromotion(MemberPromotion memberPromotion) {
        this.memberPromotion = memberPromotion;
    }

    @Transient
    public Long getTotalPayment() {
        if (totalPayment == null) {
            totalPayment = MemberController.computinDebt(memberId);
        }
        return totalPayment;
    }

    public void setTotalPayment(Long totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Transient
    public List<GroupHasMember> getLstGroupHasMembers() {
        return lstGroupHasMembers;
    }

    public void setLstGroupHasMembers(List<GroupHasMember> lstGroupHasMembers) {
        this.lstGroupHasMembers = lstGroupHasMembers;
    }

    @Transient
    public String getTypeName() {
        if (Constant.MEMBER_TYPE.GROUP_MEMBER.equals(type)) {
            typeName = "Nhóm";
        } else {
            typeName = "Hội viên";
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Transient
    public String getStatusName() {
        if (Constant.MEMBER_STATUS.NEW.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("member.status.NEW");
        } else if (Constant.MEMBER_STATUS.ACTIVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("member.status.ACTIVE");
        } else if (Constant.MEMBER_STATUS.PAUSE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("member.status.PAUSE");
        } else if (Constant.MEMBER_STATUS.RESERVE.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("member.status.RESERVE");
        } else if (Constant.MEMBER_STATUS.STOP.equals(status)) {
            statusName = MessageUtil.getResourceBundleMessage("member.status.STOP");
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Transient
    public String getLstCardCodeStr() {
        if (Constant.MEMBER_TYPE.GROUP_MEMBER.equals(type) && lstCardCodeStr == null) {
            getNumMemberInGroup();
        }
        return lstCardCodeStr;
    }

    public void setLstCardCodeStr(String lstCardCodeStr) {
        this.lstCardCodeStr = lstCardCodeStr;
    }

    @Transient
    public Long getNumMemberInGroup() {
        if (Constant.MEMBER_TYPE.GROUP_MEMBER.equals(type)) {
            String sqlCheckCode = "select count(*) numMember,wm_concat(b.card_code) cardCode from group_has_member a \n"
                    + "left join member b on a.member_id=b.member_id  where group_Member_Id =? group by group_Member_Id ";
            Session session = HibernateUtil.openSession();
            try {
                SQLQuery query = session.createSQLQuery(sqlCheckCode).addScalar("numMember", StandardBasicTypes.LONG).addScalar("cardCode", StandardBasicTypes.MATERIALIZED_CLOB);
                query.setParameter(0, memberId);
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
        }
        return numMemberInGroup;
    }

    public void setNumMemberInGroup(Long numMemberInGroup) {
        this.numMemberInGroup = numMemberInGroup;
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

    @Column(name = "fingerprint_path", nullable = true, length = 4000)
    public String getFingerprintPath() {
        return fingerprintPath;
    }

    public void setFingerprintPath(String fingerprintPath) {
        this.fingerprintPath = fingerprintPath;
    }

    @Transient
    public List<Membership> getMembershipAvailbles() {
        if (memberships != null && !memberships.isEmpty() && membershipAvailbles == null || membershipAvailbles.isEmpty()) {
            membershipAvailbles = new ArrayList<>();
            for (Membership membership : memberships) {
                if ((Constant.MEMBERSHIP_STATUS.ACTIVE.equals(membership.getStatus())
                        || Constant.MEMBERSHIP_STATUS.RECEIVE.equals(membership.getStatus()))
                        && (membership.getJoinDate() == null || membership.getJoinDate().getTime() <= (new Date()).getTime())
                        && (membership.getEndDate() != null && membership.getEndDate().getTime() >= (new Date()).getTime())) {
                    if (membership.getAvailable() == null || membership.getAvailable() > 0) {
                        membershipAvailbles.add(membership);
                    }
                }
            }
        }
        return membershipAvailbles;
    }

    public void setMembershipAvailbles(List<Membership> membershipAvailbles) {
        this.membershipAvailbles = membershipAvailbles;
    }

    @Transient
    public String getMembershipAvailblesStr() {
        membershipAvailblesStr = "";
        try {

            getMembershipAvailbles();//khoi tao gt
            if (membershipAvailbles != null && membershipAvailbles.size() > 0) {
                for (Membership bo : membershipAvailbles) {
                    String s = "- ";
                    if (bo.getGroupPack() != null && bo.getGroupPack().getGroupPackName() != null) {
                        s += bo.getGroupPack().getGroupPackName() + " - ";
                        if (bo.getAvailable() != null) {
                            s += bo.getAvailable() + " lượt";
                        } else {
                            s += DateTimeUtils.format(bo.getEndDate(), "dd/MM/yyyy");
                        }
                        s += "\n";
                    }
                    membershipAvailblesStr += s;
                }
            }
        } catch (Exception e) {
        }
        return membershipAvailblesStr;
    }

    public String toExportMembershipAvailbles() {
        return getMembershipAvailblesStr();
    }

    public void setMembershipAvailblesStr(String membershipAvailblesStr) {
        this.membershipAvailblesStr = membershipAvailblesStr;
    }

}
