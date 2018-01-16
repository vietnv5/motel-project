package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatCommune;
import com.slook.model.CatMachine;
import com.slook.model.CatRoom;
import com.slook.model.V_CatRoomUse;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.common.ConditionQuery;
import com.slook.persistence.common.OrderBy;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;

/**
 * Created by xuanh on 4/23/2017.
 */
@ManagedBean
@ViewScoped
public class CatRoomController implements Serializable {

    List<CatRoom> rooms;
    GenericDaoServiceNewV2 roomService;
    CatRoom currRoom;
    LazyDataModel<CatRoom> lazyRoom;
    List<CatMachine> lstMachines;
    GenericDaoImplNewV2<CatMachine, Long> catMachineService;
    GenericDaoImplNewV2<V_CatRoomUse, Long> vCatRoomUseService;
    List<V_CatRoomUse> lstVCatRoomUse;
    Map<Long,Long> mapRoomUse =new HashMap<>();
    private String oldObjectStr = null;


    @PostConstruct
    public void onStart() {
        roomService = new GenericDaoImplNewV2<CatRoom, Long>() {
        };
        catMachineService = new GenericDaoImplNewV2<CatMachine, Long>() {
        };
        vCatRoomUseService = new GenericDaoImplNewV2<V_CatRoomUse, Long>() {
        };
        try {
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branch.branchName", "ASC");
            order.put("roomName", "ASC");
            Map<String, Object> filter = new HashMap<>();
            filter.put("branch.branchName-EXAC", "abc");
            rooms = roomService.findList(null, order);
            lazyRoom = new LazyDataModelBase<CatRoom, Long>(roomService);
            lstMachines = catMachineService.findList();
            lstVCatRoomUse=vCatRoomUseService.findList();
            mapRoomUse=(Map<Long,Long>) CommonUtil.convertListToMap((List)lstVCatRoomUse, "roomId","useSeat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        currRoom = new CatRoom();
    }

    public List<CatCommune> completeAddress(String key) {

        try {
            String hql = "from CatCommune where lower(communeName) like '%'||?||'%' or lower(district.districtName) like '%'||?||'%' or lower(district.province.provinceName) like '%'||?||'%'";
            return new GenericDaoImplNewV2<CatCommune, Long>() {
            }.findList(hql, 1, 20, key.toLowerCase(), key.toLowerCase(), key.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CatRoom> getRoomsInBranch(Long branchId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("branchId", branchId);
        try {
            return roomService.findList(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void preAdd() {
        currRoom = new CatRoom();
        currRoom.setStatus(1l);
        oldObjectStr=null;
    }

    public void saveOrUpdate() {
        try {
            if (currRoom.getCatMachine() != null) currRoom.setMachineId(currRoom.getCatMachine().getMachineId());
            if (currRoom.getCatMachineOut()!= null) currRoom.setMachineOutId(currRoom.getCatMachineOut().getMachineId());
            roomService.saveOrUpdate(currRoom);
            //ghi log
            if (oldObjectStr != null)
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currRoom.toString()
                        , this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            else
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currRoom.toString()
                        , this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
            preAdd();
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CatRoom catBranch) {
        currRoom = catBranch;
        oldObjectStr=catBranch.toString();
    }

    public void delete(CatRoom currBranch) {
        try {
            roomService.delete(currBranch);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, currBranch.toString(), null, this.getClass().getSimpleName()
                    , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (AppException e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            e.printStackTrace();
        }
    }

    public GenericDaoServiceNewV2 getRoomService() {
        return roomService;
    }

    public void setRoomService(GenericDaoServiceNewV2 roomService) {
        this.roomService = roomService;
    }

    public CatRoom getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(CatRoom currRoom) {
        this.currRoom = currRoom;
    }

    public List<CatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<CatRoom> rooms) {
        this.rooms = rooms;
    }

    public LazyDataModel<CatRoom> getLazyRoom() {
        return lazyRoom;
    }

    public void setLazyRoom(LazyDataModel<CatRoom> lazyRoom) {
        this.lazyRoom = lazyRoom;
    }

    public List<CatMachine> getLstMachines() {
        return lstMachines;
    }

    public void setLstMachines(List<CatMachine> lstMachines) {
        this.lstMachines = lstMachines;
    }

    public GenericDaoImplNewV2<CatMachine, Long> getCatMachineService() {
        return catMachineService;
    }

    public void setCatMachineService(GenericDaoImplNewV2<CatMachine, Long> catMachineService) {
        this.catMachineService = catMachineService;
    }

    public GenericDaoImplNewV2<V_CatRoomUse, Long> getvCatRoomUseService() {
        return vCatRoomUseService;
    }

    public void setvCatRoomUseService(GenericDaoImplNewV2<V_CatRoomUse, Long> vCatRoomUseService) {
        this.vCatRoomUseService = vCatRoomUseService;
    }

    public List<V_CatRoomUse> getLstVCatRoomUse() {
        return lstVCatRoomUse;
    }

    public void setLstVCatRoomUse(List<V_CatRoomUse> lstVCatRoomUse) {
        this.lstVCatRoomUse = lstVCatRoomUse;
    }

    public Map<Long, Long> getMapRoomUse() {
        return mapRoomUse;
    }

    public void setMapRoomUse(Map<Long, Long> mapRoomUse) {
        this.mapRoomUse = mapRoomUse;
    }

    public void onChangeBranch() {
        try {
            Map<String, Object> filter = new HashMap<>();
            if(currRoom.getBranchId()!=null && currRoom.getBranchId()>0)
            filter.put("branchId", currRoom.getBranchId());
            lstMachines = catMachineService.findList(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void updateStatus(CatRoom catRoom, Long newStatus) {
        try {
            String old=catRoom.toString();
            catRoom.setStatus(newStatus);
            roomService.saveOrUpdate(catRoom);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, catRoom.toString(), this.getClass().getSimpleName()
                    , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.save.success");
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }
    public Long getUseSeat(Long roomId){
       return mapRoomUse.get(roomId);
    }
}
