package com.slook.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_MEMBER_DEBT_PAYMENT") 
public class V_MemberDebtPayment implements Serializable  { 

	private String groupPackName;
	private Long membershipId;
	private Long memberId;
	private Long sumDept;

	@Column(name = "GROUP_PACK_NAME", length = 500, updatable = false) 
	public String getGroupPackName() { 
		return groupPackName; 
	} 
	public void setGroupPackName(String groupPackName) { 
		this.groupPackName = groupPackName; 
	} 
        @Id
	@Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0, updatable = false) 
	public Long getMembershipId() { 
		return membershipId; 
	} 
	public void setMembershipId(Long membershipId) { 
		this.membershipId = membershipId; 
	} 
	@Column(name = "MEMBER_ID", precision = 22, scale = 0, updatable = false) 
	public Long getMemberId() { 
		return memberId; 
	} 
	public void setMemberId(Long memberId) { 
		this.memberId = memberId; 
	} 
	@Column(name = "SUM_DEPT", precision = 22, scale = 0, updatable = false) 
	public Long getSumDept() { 
		return sumDept; 
	} 
	public void setSumDept(Long sumDept) { 
		this.sumDept = sumDept; 
	} 


	public V_MemberDebtPayment() {
	}
}
