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

@Entity
@Table(name = "REPORT") 
/** 
* 
* @author: ThuanNHT 
* @version: 1.0 
* @since: 1.0 
*/
public class Report { 

	private Long role;
	private String code;
	private String reportPath;
	private Long status;
	private Long reportId;
	private String name;

	@Column(name = "ROLE", precision = 22, scale = 0) 
	public Long getRole() { 
		return role; 
	} 
	public void setRole(Long role) { 
		this.role = role; 
	} 
	@Column(name = "CODE", length = 255) 
	public String getCode() { 
		return code; 
	} 
	public void setCode(String code) { 
		this.code = code; 
	} 
	@Column(name = "REPORT_PATH", length = 255) 
	public String getReportPath() { 
		return reportPath; 
	} 
	public void setReportPath(String reportPath) { 
		this.reportPath = reportPath; 
	} 
	@Column(name = "STATUS", precision = 22, scale = 0) 
	public Long getStatus() { 
		return status; 
	} 
	public void setStatus(Long status) { 
		this.status = status; 
	} 
	@SequenceGenerator(name = "generator", sequenceName = "REPORT_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "REPORT_ID", unique = true, nullable = false, precision = 22, scale = 0) 
	public Long getReportId() { 
		return reportId; 
	} 
	public void setReportId(Long reportId) { 
		this.reportId = reportId; 
	} 
	@Column(name = "NAME", length = 255) 
	public String getName() { 
		return name; 
	} 
	public void setName(String name) { 
		this.name = name; 
	} 


	public Report() {
	}
	public Report(Long reportId) {
		this.reportId = reportId;
	}
}