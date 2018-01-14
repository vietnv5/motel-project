/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
*/
package com.slook.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CLIENT_USED_SERVICE") 
/** 
* 
* @author: ThuanNHT 
* @version: 1.0 
* @since: 1.0 
*/
public class ClientUsedService { 

	private Long available;
	private Long status;
	private Long clientId;
	private Long membershipId;
	private Long usedNumber;
	private Long serviceId;
	private Date endDate;
	private Date startDate;
	private Long id;
	private Date createTime;
	private Long totalNumber;

	@Column(name = "AVAILABLE", precision = 22, scale = 0) 
	public Long getAvailable() { 
		return available; 
	} 
	public void setAvailable(Long available) { 
		this.available = available; 
	} 
	@Column(name = "STATUS", precision = 22, scale = 0) 
	public Long getStatus() { 
		return status; 
	} 
	public void setStatus(Long status) { 
		this.status = status; 
	} 
	@Column(name = "CLIENT_ID", precision = 22, scale = 0) 
	public Long getClientId() { 
		return clientId; 
	} 
	public void setClientId(Long clientId) { 
		this.clientId = clientId; 
	} 
	@Column(name = "MEMBERSHIP_ID", precision = 22, scale = 0) 
	public Long getMembershipId() { 
		return membershipId; 
	} 
	public void setMembershipId(Long membershipId) { 
		this.membershipId = membershipId; 
	} 
	@Column(name = "USED_NUMBER", precision = 22, scale = 0) 
	public Long getUsedNumber() { 
		return usedNumber; 
	} 
	public void setUsedNumber(Long usedNumber) { 
		this.usedNumber = usedNumber; 
	} 
	@Column(name = "SERVICE_ID", precision = 22, scale = 0) 
	public Long getServiceId() { 
		return serviceId; 
	} 
	public void setServiceId(Long serviceId) { 
		this.serviceId = serviceId; 
	} 
	@Temporal(TemporalType.DATE) 
	@Column(name = "END_DATE", length = 7) 
	public Date getEndDate() { 
		return endDate; 
	} 
	public void setEndDate(Date endDate) { 
		this.endDate = endDate; 
	} 
	@Temporal(TemporalType.DATE) 
	@Column(name = "START_DATE", length = 7) 
	public Date getStartDate() { 
		return startDate; 
	} 
	public void setStartDate(Date startDate) { 
		this.startDate = startDate; 
	} 
	@SequenceGenerator(name = "generator", sequenceName = "CLIENT_USED_SERVICE_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0) 
	public Long getId() { 
		return id; 
	} 
	public void setId(Long id) { 
		this.id = id; 
	} 
	@Temporal(TemporalType.DATE) 
	@Column(name = "CREATE_TIME", length = 11) 
	public Date getCreateTime() { 
		return createTime; 
	} 
	public void setCreateTime(Date createTime) { 
		this.createTime = createTime; 
	} 
	@Column(name = "TOTAL_NUMBER", precision = 22, scale = 0) 
	public Long getTotalNumber() { 
		return totalNumber; 
	} 
	public void setTotalNumber(Long totalNumber) { 
		this.totalNumber = totalNumber; 
	} 


	public ClientUsedService() {
	}
	public ClientUsedService(Long id) {
		this.id = id;
	}
}