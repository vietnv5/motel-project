/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
*/
package com.slook.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_CAT_ROOM_USE") 
/** 
* 
* @author: ThuanNHT 
* @version: 1.0 
* @since: 1.0 
*/
public class V_CatRoomUse { 

	private Long totalSeat;
	private Long manageType;
	private String roomCode;
	private Long status;
	private Long existMachineInOut;
	private Long communeId;
	private Long roomId;
	private String description;
	private Long useSeat;
	private String roomName;
	private String address;
	private Long machineId;
	private String manager;
	private Long type;
	private Long branchId;
	private Long machineOutId;

	@Column(name = "TOTAL_SEAT", precision = 22, scale = 0, updatable = false) 
	public Long getTotalSeat() { 
		return totalSeat; 
	} 
	public void setTotalSeat(Long totalSeat) { 
		this.totalSeat = totalSeat; 
	} 
	@Column(name = "MANAGE_TYPE", precision = 22, scale = 0, updatable = false) 
	public Long getManageType() { 
		return manageType; 
	} 
	public void setManageType(Long manageType) { 
		this.manageType = manageType; 
	} 
	@Column(name = "ROOM_CODE", length = 20, updatable = false) 
	public String getRoomCode() { 
		return roomCode; 
	} 
	public void setRoomCode(String roomCode) { 
		this.roomCode = roomCode; 
	} 
	@Column(name = "STATUS", precision = 22, scale = 0, updatable = false) 
	public Long getStatus() { 
		return status; 
	} 
	public void setStatus(Long status) { 
		this.status = status; 
	} 
	@Column(name = "EXIST_MACHINE_IN_OUT", precision = 22, scale = 0, updatable = false) 
	public Long getExistMachineInOut() { 
		return existMachineInOut; 
	} 
	public void setExistMachineInOut(Long existMachineInOut) { 
		this.existMachineInOut = existMachineInOut; 
	} 
	@Column(name = "COMMUNE_ID", precision = 22, scale = 0, updatable = false) 
	public Long getCommuneId() { 
		return communeId; 
	} 
	public void setCommuneId(Long communeId) { 
		this.communeId = communeId; 
	} 
	@Column(name = "ROOM_ID", precision = 22, scale = 0, updatable = false) 
        @Id
	public Long getRoomId() { 
		return roomId; 
	} 
	public void setRoomId(Long roomId) { 
		this.roomId = roomId; 
	} 
	@Column(name = "DESCRIPTION", length = 100, updatable = false) 
	public String getDescription() { 
		return description; 
	} 
	public void setDescription(String description) { 
		this.description = description; 
	} 
	@Column(name = "USE_SEAT", precision = 22, scale = 0, updatable = false) 
	public Long getUseSeat() { 
		return useSeat; 
	} 
	public void setUseSeat(Long useSeat) { 
		this.useSeat = useSeat; 
	} 
	@Column(name = "ROOM_NAME", length = 50, updatable = false) 
	public String getRoomName() { 
		return roomName; 
	} 
	public void setRoomName(String roomName) { 
		this.roomName = roomName; 
	} 
	@Column(name = "ADDRESS", length = 500, updatable = false) 
	public String getAddress() { 
		return address; 
	} 
	public void setAddress(String address) { 
		this.address = address; 
	} 
	@Column(name = "MACHINE_ID", precision = 22, scale = 0, updatable = false) 
	public Long getMachineId() { 
		return machineId; 
	} 
	public void setMachineId(Long machineId) { 
		this.machineId = machineId; 
	} 
	@Column(name = "MANAGER", length = 200, updatable = false) 
	public String getManager() { 
		return manager; 
	} 
	public void setManager(String manager) { 
		this.manager = manager; 
	} 
	@Column(name = "TYPE", precision = 22, scale = 0, updatable = false) 
	public Long getType() { 
		return type; 
	} 
	public void setType(Long type) { 
		this.type = type; 
	} 
	@Column(name = "BRANCH_ID", precision = 22, scale = 0, updatable = false) 
	public Long getBranchId() { 
		return branchId; 
	} 
	public void setBranchId(Long branchId) { 
		this.branchId = branchId; 
	} 
	@Column(name = "MACHINE_OUT_ID", precision = 22, scale = 0, updatable = false) 
	public Long getMachineOutId() { 
		return machineOutId; 
	} 
	public void setMachineOutId(Long machineOutId) { 
		this.machineOutId = machineOutId; 
	} 


	public V_CatRoomUse() {
	}
}