package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "MEMBER_CHECKIN")
public class MemberCheckin
{

    private Long checkinId;
    private Long memberId;
    //    private String machineCode;
//    private String machineName;
    private Date checkInDate;
    private CatMachine machine;
    private Long machineId;
    //vong lap
//    private Member member;
    private Date insertTime;
    private Long verifyMode;
    private String cardCode;
    private String ip;
    private String verifyModeName;

    @SequenceGenerator(name = "generator", sequenceName = "member_checkin_seq", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "CHECKIN_ID", nullable = false, precision = 0)
    public Long getCheckinId()
    {
        return checkinId;
    }

    public void setCheckinId(Long checkinId)
    {
        this.checkinId = checkinId;
    }

    @Basic
    @Column(name = "MEMBER_ID", nullable = true, precision = 0)
    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    //    @ManyToOne
//    @JoinColumn(name = "member_id", insertable = false, updatable = false)
//    public Member getMember() {
//        return member;
//    }
//
//    public void setMember(Member member) {
//        this.member = member;
//    }
//    @Basic
//    @Column(name = "MACHINE_CODE", nullable = true, length = 20)
//    public String getMachineCode() {
//        return machineCode;
//    }
//
//    public void setMachineCode(String machineCode) {
//        this.machineCode = machineCode;
//    }
//
//    @Basic
//    @Column(name = "MACHINE_NAME", nullable = true, length = 20)
//    public String getMachineName() {
//        return machineName;
//    }
//    public void setMachineName(String machineName) {
//        this.machineName = machineName;
//    }
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECK_IN_DATE", nullable = true)
    public Date getCheckInDate()
    {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate)
    {
        this.checkInDate = checkInDate;
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

        MemberCheckin that = (MemberCheckin) o;

        if (checkinId != null ? !checkinId.equals(that.checkinId) : that.checkinId != null)
        {
            return false;
        }
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null)
        {
            return false;
        }
//        if (machineCode != null ? !machineCode.equals(that.machineCode) : that.machineCode != null) return false;
//        if (machineName != null ? !machineName.equals(that.machineName) : that.machineName != null) return false;
        if (checkInDate != null ? !checkInDate.equals(that.checkInDate) : that.checkInDate != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = checkinId != null ? checkinId.hashCode() : 0;
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
//        result = 31 * result + (machineCode != null ? machineCode.hashCode() : 0);
//        result = 31 * result + (machineName != null ? machineName.hashCode() : 0);
        result = 31 * result + (checkInDate != null ? checkInDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "MACHINE_ID", referencedColumnName = "MACHINE_ID", insertable = false, updatable = false)
    public CatMachine getMachine()
    {
        return machine;
    }

    public void setMachine(CatMachine machine)
    {
        this.machine = machine;
    }

    @Column(name = "MACHINE_ID")
    public Long getMachineId()
    {
        return machineId;
    }

    public void setMachineId(Long machineId)
    {
        this.machineId = machineId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "insert_time", nullable = true)
    public Date getInsertTime()
    {
        return insertTime;
    }

    public void setInsertTime(Date insertTime)
    {
        this.insertTime = insertTime;
    }

    @Column(name = "Verify_Mode", nullable = true, precision = 0)
    public Long getVerifyMode()
    {
        return verifyMode;
    }

    public void setVerifyMode(Long verifyMode)
    {
        this.verifyMode = verifyMode;
    }

    @Column(name = "card_Code")
    public String getCardCode()
    {
        return cardCode;
    }

    public void setCardCode(String cardCode)
    {
        this.cardCode = cardCode;
    }

    @Column(name = "ip")
    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    @Transient
    public String getVerifyModeName()
    {
        if (Constant.VERIFY_MODE.FINGERPRINT.equals(verifyMode))
        {
            verifyModeName = MessageUtil.getResourceBundleMessage("verifyMode.FINGERPRINT");
        }
        else if (Constant.VERIFY_MODE.USER_PASS.equals(verifyMode))
        {
            verifyModeName = MessageUtil.getResourceBundleMessage("verifyMode.USER_PASS");
        }
        else if (Constant.VERIFY_MODE.CARD_CODE.equals(verifyMode))
        {
            verifyModeName = MessageUtil.getResourceBundleMessage("verifyMode.CARD_CODE");
        }
        return verifyModeName;
    }

    public void setVerifyModeName(String verifyModeName)
    {
        this.verifyModeName = verifyModeName;
    }

}
