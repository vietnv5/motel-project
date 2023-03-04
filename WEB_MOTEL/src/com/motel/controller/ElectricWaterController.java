/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.ElectricWater;
import com.motel.model.Home;
import com.motel.model.Room;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.RoomServiceImpl;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.DateTimeUtils;
import com.slook.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.time.DateUtils;

import static org.omnifaces.util.Faces.getRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author VietNV Jan 22, 2018
 */
@ManagedBean
@ViewScoped
public class ElectricWaterController
{

    protected static final Logger logger = LoggerFactory.getLogger(ElectricWaterController.class);

    private GenericDaoImplNewV2<ElectricWater, Long> electricWaterService;
    LazyDataModel<ElectricWater> lazyDataModel;
    ElectricWater currElectricWater = new ElectricWater();
    private String oldObjectStr = null;
    private Long groupUserId;
    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<Home> lstHome;
    List<Room> lstRoom;

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart()
    {
        electricWaterService = new GenericDaoImplNewV2<ElectricWater, Long>()
        {
        };

        try
        {
            CatUser catUser = null;
            groupUserId = null;
            if (getRequest().getSession().getAttribute("user") != null)
            {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("month", Constant.ORDER.DESC);
            order.put("room.home.homeName", Constant.ORDER.ASC);
            order.put("room.roomName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0)
            {//phan quyen
                filter.put("room.home.groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<ElectricWater, Long>(electricWaterService, filter, order);

            //init data
            Map<String, Object> filtersHome = new HashMap<>();
            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0)
            {//phan quyen
                filtersHome.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("homeName", Constant.ORDER.ASC);
            lstHome = HomeServiceImpl.getInstance().findList(filtersHome, orderHome);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, true, false, true, false
        );
    }

    /**
     * Lấy danh sách thông tin điện nước của phòng theo thời gian giảm dần
     *
     * @param roomId id phòng
     * @return
     */
    public List<ElectricWater> getListElectricWaterOfRoom(Long roomId)
    {
        LinkedHashMap<String, String> order = new LinkedHashMap<>();
        order.put("timeLine", Constant.ORDER.DESC);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status-NEQ", Constant.STATUS.DELETE);
        if (roomId != null)
        {
            filter.put("roomId", roomId);
        }
        List<ElectricWater> lst = new ArrayList<>();
        try
        {
            lst = electricWaterService.findList(filter, order);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lst;
    }

    public void onChangeHome()
    {
        if (currElectricWater != null && currElectricWater.getHomeId() != null && currElectricWater.getHomeId() > 0)
        {
            Map<String, Object> filtersRoom = new HashMap<>();
            filtersRoom.put("status-NEQ", Constant.ROOM_STATUS.DELETE);
            filtersRoom.put("homeId", currElectricWater.getHomeId());
            LinkedHashMap<String, String> orderRoom = new LinkedHashMap<>();
            orderRoom.put("roomName", Constant.ORDER.ASC);
            try
            {
                lstRoom = RoomServiceImpl.getInstance().findList(filtersRoom, orderRoom);
            }
            catch (Exception ex)
            {
                logger.error(ex.getMessage(), ex);
            }

        }
        else
        {
            lstRoom = new ArrayList<>();
        }
    }

    /**
     * tự động fill chỉ số điện nước đầu bằng chỉ số cuối của lần chốt trước
     */
    public void onSelectRoom()
    {
        if (currElectricWater != null && currElectricWater.getRoomId() != null)
        {
            List<ElectricWater> lstOldEW = getListElectricWaterOfRoom(currElectricWater.getRoomId());
            if (lstOldEW != null && !lstOldEW.isEmpty())
            {
                ElectricWater before = lstOldEW.get(0);
                currElectricWater.setElectricOld(before.getElectricNew());
                currElectricWater.setWaterOld(before.getWaterNew());
            }
        }
    }

    public void preAdd()
    {
        this.currElectricWater = new ElectricWater();
        isEdit = false;

        currElectricWater.setTimeLine(new Date());
        //xu ly round month
        int nextToMonth = 0;
        Date payment = new Date();
        try
        {
            if (DataConfig.getConfigByKey("NEXT_DAY_TO_MOTNH") != null)
            {
                nextToMonth = Long.valueOf(DataConfig.getConfigByKey("NEXT_DAY_TO_MOTNH")).intValue();
            }
            payment.setDate(payment.getDate() + nextToMonth);
        }
        catch (Exception e)
        {
        }
        Date month = DateUtils.truncate(payment, Calendar.MONTH);
        currElectricWater.setMonth(month);
        if (lstHome != null && !lstHome.isEmpty())
        {
            currElectricWater.setHomeId(lstHome.get(0).getHomeId());
        }
        this.selectedDate = month;
        onChangeHome();
    }

    public void preEdit(ElectricWater electricWater)
    {
        try
        {
            currElectricWater = electricWaterService.findById(electricWater.getElectricWaterId());
            List<Long> lstServiceId = new ArrayList<>();
            if (currElectricWater != null
                    && currElectricWater.getRoom() != null)
            {
                currElectricWater.setHomeId(currElectricWater.getRoom().getHomeId());
            }
            this.selectedDate = currElectricWater.getMonth();
            onChangeHome();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currElectricWater.toString();
    }

    public void onSaveOrUpdate()
    {
        try
        {
            if (currElectricWater.getElectricOld() != null && currElectricWater.getElectricNew() != null
                    && currElectricWater.getElectricOld() > currElectricWater.getElectricNew())
            {
                MessageUtil.setErrorMessage("Số điện mới không được nhỏ hơn số cũ!");
                return;
            }
            if (currElectricWater.getWaterOld() != null && currElectricWater.getWaterNew() != null
                    && currElectricWater.getWaterOld() > currElectricWater.getWaterNew())
            {
                MessageUtil.setErrorMessage("Số nước mới không được nhỏ hơn số cũ!");
                return;
            }
//xu ly round month
            currElectricWater.setMonth(selectedDate);
            Date month = DateUtils.round(currElectricWater.getMonth(), Calendar.MONTH);
            currElectricWater.setMonth(month);

            Map<String, Object> filter = new HashMap<>();
            filter.put("status-EQ", Constant.STATUS.ACTIVE);
            filter.put("roomId", currElectricWater.getRoomId());
            filter.put("month", currElectricWater.getMonth());
            if (currElectricWater.getElectricWaterId() != null)
            {
                filter.put("electricWaterId-NEQ", currElectricWater.getElectricWaterId());
            }
            List<ElectricWater> lstElectricWater = electricWaterService.findList(filter);
            if (lstElectricWater != null && !lstElectricWater.isEmpty())
            {
                MessageUtil.setErrorMessage("Thông tin chốt số điện nước cho phòng trong tháng "
                        + DateTimeUtils.format(lstElectricWater.get(0).getMonth(), "MM/yyyy") + " đã tồn tại!");

                return;
            }
            if (currElectricWater.getStatus() == null)
            {
                currElectricWater.setStatus(Constant.STATUS.ACTIVE);
            }
            if (currElectricWater.getCreateTime() == null)
            {
                currElectricWater.setCreateTime(new Date());
            }

            electricWaterService.saveOrUpdate(currElectricWater);

            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currElectricWater.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currElectricWater.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('electricWaterDlg').hide();");

        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(ElectricWater electricWater)
    {
        try
        {

            electricWaterService.delete(electricWater);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public LazyDataModel<ElectricWater> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<ElectricWater> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public ElectricWater getCurrElectricWater()
    {
        return currElectricWater;
    }

    public void setCurrElectricWater(ElectricWater currElectricWater)
    {
        this.currElectricWater = currElectricWater;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public boolean isIsEdit()
    {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
    }

    public GenericDaoImplNewV2<ElectricWater, Long> getElectricWaterService()
    {
        return electricWaterService;
    }

    public void setElectricWaterService(GenericDaoImplNewV2<ElectricWater, Long> electricWaterService)
    {
        this.electricWaterService = electricWaterService;
    }

    public Long getGroupUserId()
    {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId)
    {
        this.groupUserId = groupUserId;
    }

    public List<Home> getLstHome()
    {
        return lstHome;
    }

    public void setLstHome(List<Home> lstHome)
    {
        this.lstHome = lstHome;
    }

    public List<Room> getLstRoom()
    {
        return lstRoom;
    }

    public void setLstRoom(List<Room> lstRoom)
    {
        this.lstRoom = lstRoom;
    }

    //bo sung
    private Date selectedDate;

    public Date getSelectedDate()
    {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate)
    {
        this.selectedDate = selectedDate;
    }

    public void decrementMonth() throws Exception
    {
        Calendar c = Calendar.getInstance();
        c.setTime(this.selectedDate);
        c.add(Calendar.MONTH, -1);
        //... business logic to update date dependent data
        selectedDate = c.getTime();
    }

    public void incrementMonth() throws Exception
    {
        Calendar c = Calendar.getInstance();
        c.setTime(this.selectedDate);
        c.add(Calendar.MONTH, 1);

        //... business logic to update date dependent data
        selectedDate = c.getTime();
    }
}
