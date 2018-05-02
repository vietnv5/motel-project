package com.slook.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_CAT_ITEM") 
public class V_CatItemBO implements Serializable { 

	private String catCode;
	private String description;
	private Long itemId;
	private String itemCode;
	private String value;
	private Long editable;
	private String catName;
	private String itemName;

	@Column(name = "CAT_CODE", length = 30, updatable = false) 
	public String getCatCode() { 
		return catCode; 
	} 
	public void setCatCode(String catCode) { 
		this.catCode = catCode; 
	} 
	@Column(name = "DESCRIPTION", length = 200, updatable = false) 
	public String getDescription() { 
		return description; 
	} 
	public void setDescription(String description) { 
		this.description = description; 
	} 
        @Id
	@Column(name = "ITEM_ID", precision = 22, scale = 0, updatable = false) 
	public Long getItemId() { 
		return itemId; 
	} 
	public void setItemId(Long itemId) { 
		this.itemId = itemId; 
	} 
	@Column(name = "ITEM_CODE", length = 250, updatable = false) 
	public String getItemCode() { 
		return itemCode; 
	} 
	public void setItemCode(String itemCode) { 
		this.itemCode = itemCode; 
	} 
	@Column(name = "VALUE", length = 255, updatable = false) 
	public String getValue() { 
		return value; 
	} 
	public void setValue(String value) { 
		this.value = value; 
	} 
	@Column(name = "EDITABLE", precision = 22, scale = 0, updatable = false) 
	public Long getEditable() { 
		return editable; 
	} 
	public void setEditable(Long editable) { 
		this.editable = editable; 
	} 
	@Column(name = "CAT_NAME", length = 50, updatable = false) 
	public String getCatName() { 
		return catName; 
	} 
	public void setCatName(String catName) { 
		this.catName = catName; 
	} 
	@Column(name = "ITEM_NAME", length = 250, updatable = false) 
	public String getItemName() { 
		return itemName; 
	} 
	public void setItemName(String itemName) { 
		this.itemName = itemName; 
	} 


	public V_CatItemBO() {
	}
}