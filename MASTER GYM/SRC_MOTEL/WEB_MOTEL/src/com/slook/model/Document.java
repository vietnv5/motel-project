package com.slook.model;/*
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "DOCUMENT") 
/** 
* 
* @author: ThuanNHT 
* @version: 1.0 
* @since: 1.0 
*/
public class Document {

	private String documentName;
	private String attachFile;
	private String description;
	private Long documentId;
	private String fileName;

	@Column(name = "DOCUMENT_NAME", length = 255) 
	public String getDocumentName() { 
		return documentName; 
	} 
	public void setDocumentName(String documentName) { 
		this.documentName = documentName; 
	} 
	@Column(name = "ATTACH_FILE", length = 255) 
	public String getAttachFile() { 
		return attachFile; 
	} 
	public void setAttachFile(String attachFile) { 
		this.attachFile = attachFile; 
	} 
	@Column(name = "DESCRIPTION", length = 1000) 
	public String getDescription() { 
		return description; 
	} 
	public void setDescription(String description) { 
		this.description = description; 
	} 
	@SequenceGenerator(name = "generator", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "DOCUMENT_ID", unique = true, nullable = false, precision = 22, scale = 0) 
	public Long getDocumentId() { 
		return documentId; 
	} 
	public void setDocumentId(Long documentId) { 
		this.documentId = documentId; 
	} 
	@Column(name = "FILE_NAME", length = 255) 
	public String getFileName() { 
		return fileName; 
	} 
	public void setFileName(String fileName) { 
		this.fileName = fileName; 
	} 


	public Document() {
	}
	public Document(Long documentId) {
		this.documentId = documentId;
	}
}