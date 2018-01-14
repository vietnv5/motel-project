package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.Util;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_ROOM")
public class CatRoom {

    private Long roomId;
    private String roomName;
    private Long branchId;
    private CatBranch branch;
    private String description;
    Long type;
    String address;
    String roomCode;
    private CatCommune commune;
    private Long existMachineInOut;
    private boolean boolExistMachineInOut;
    private Long machineId;
    private CatMachine catMachine;
    private Long status;
    private Long machineOutId;
    private CatMachine catMachineOut;
    private String statusName;
    private Long totalSeat;

    @Id
    @Column(name = "ROOM_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "ROOM_SEQ", allocationSize = 1)
    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Basic
    @Column(name = "ROOM_NAME", nullable = true, length = 50)
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Basic
    @Column(name = "BRANCH_ID", nullable = true, precision = 0)
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

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatRoom catRoom = (CatRoom) o;

        if (roomId != null ? !roomId.equals(catRoom.roomId) : catRoom.roomId != null) {
            return false;
        }
        if (roomName != null ? !roomName.equals(catRoom.roomName) : catRoom.roomName != null) {
            return false;
        }
        if (branchId != null ? !branchId.equals(catRoom.branchId) : catRoom.branchId != null) {
            return false;
        }
        if (description != null ? !description.equals(catRoom.description) : catRoom.description != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = roomId != null ? roomId.hashCode() : 0;
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Column
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "ROOM_CODE")
    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void createRoomCode() {
        String code = null;
        switch (type.intValue()) {
            //Bổ sung danh mục loại phòng tập (gym, massage, sauna, tổ hợp..)
            case 1:
                code = "GYM";
                break;
            case 2:
                code = "MASSAGE";
                break;
            case 3:
                code = "SAUNA";
                break;
            case 4:
                code = "COMBO";
                break;
            default:
                return;
        }

        String hqlCheckCode = "select count(*) from CatRoom where roomCode like ?||'%'";
        List<?> counts = new GenericDaoImplNewV2<CatRoom, Long>() {
        }.findListAll(hqlCheckCode, code);
        if (counts.size() > 0) {
            roomCode = code + "" + (Long.valueOf(counts.get(0).toString()) + 1);
        }
    }

    @ManyToOne
    @JoinColumn(name = "COMMUNE_ID")
    public CatCommune getCommune() {
        return commune;
    }

    public void setCommune(CatCommune commune) {
        this.commune = commune;
    }

    @Column(name = "exist_machine_in_out", nullable = true, precision = 0)
    public Long getExistMachineInOut() {
        return existMachineInOut;
    }

    public void setExistMachineInOut(Long existMachineInOut) {
        this.existMachineInOut = existMachineInOut;
    }

    @Column(name = "MACHINE_ID", nullable = true, precision = 0)
    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    @ManyToOne
    @JoinColumn(name = "MACHINE_ID", insertable = false, updatable = false)
    public CatMachine getCatMachine() {
        return catMachine;
    }

    public void setCatMachine(CatMachine catMachine) {
        this.catMachine = catMachine;
    }

    @Transient
    public boolean isBoolExistMachineInOut() {
        if (existMachineInOut != null && existMachineInOut.equals(1l)) {
            boolExistMachineInOut = true;
        } else {
            boolExistMachineInOut = false;
        }
        return boolExistMachineInOut;
    }

    public void setBoolExistMachineInOut(boolean boolExistMachineInOut) {
        if (boolExistMachineInOut == true) {
            this.existMachineInOut = 1l;
        } else {
            this.existMachineInOut = 0l;
        }
    }

    @Column(name = "status", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Transient
    public String getStatusName() {
        if (status != null) {
            if (status.equals(Constant.STATUS.INACTIVE)) {
                statusName = MessageUtil.getResourceBundleMessage("common.INACTIVE");
            } else if (status.equals(Constant.STATUS.ACTIVE)) {
                statusName = MessageUtil.getResourceBundleMessage("common.ACTIVE");
            } else if (status.equals(Constant.STATUS.PAUSE)) {
                statusName = MessageUtil.getResourceBundleMessage("common.PAUSE");
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Column(name = "MACHINE_OUT_ID", nullable = true, precision = 0)
    public Long getMachineOutId() {
        return machineOutId;
    }

    public void setMachineOutId(Long machineOutId) {
        this.machineOutId = machineOutId;
    }

    @ManyToOne
    @JoinColumn(name = "MACHINE_OUT_ID", referencedColumnName = "MACHINE_ID", insertable = false, updatable = false)
    public CatMachine getCatMachineOut() {
        return catMachineOut;
    }

    public void setCatMachineOut(CatMachine catMachineOut) {
        this.catMachineOut = catMachineOut;
    }

    @Column(name = "total_seat", nullable = true, precision = 0)
    public Long getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Long totalSeat) {
        this.totalSeat = totalSeat;
    }

}
