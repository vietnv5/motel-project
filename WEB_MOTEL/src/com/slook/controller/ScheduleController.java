package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyScheduleGym;
import com.slook.model.CatBranch;
import com.slook.model.CatPack;
import com.slook.model.CatRoom;
import com.slook.model.CatUser;
import com.slook.model.Schedule;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.MessageUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import static org.omnifaces.util.Faces.getRequest;

/**
 * Created by xuanh on 5/21/2017.
 */
@ManagedBean
@ViewScoped
public class ScheduleController implements Serializable {
    ScheduleModel scheduleGym;
    private ScheduleEvent event = new DefaultScheduleEvent();
    GenericDaoServiceNewV2<Schedule, Long> scheduleService;
    CatBranch currBranch;
    CatRoom currRoom;
    CatPack currPack;

    @PostConstruct
    public void onStart() {
        scheduleService = new GenericDaoImplNewV2<Schedule, Long>() {
        };

        try {
            /*
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branchName", "ASC");
            List<CatBranch> list = new GenericDaoImplNewV2<CatBranch, Long>() {
            }.findList(null, order);
            if (!list.isEmpty())
                currBranch = list.get(0);
            else
                currBranch = new CatBranch();
            */
            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            Long branchId = null;
            if (user != null && user.getEmployee() != null) {
                branchId = user.getEmployee().getBranchId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("branchName", "ASC");
            currBranch = new GenericDaoImplNewV2<CatBranch, Long>() {
            }.findById(branchId);
        } catch (Exception e) {
            e.printStackTrace();
        }
          if (currBranch == null) {
            currBranch = new CatBranch();
        }
        currRoom = new CatRoom();
        currPack = new CatPack();
        loadSchedule();
    }

    public List<CatPack> getPackInRoom(Long roomId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("roomId", roomId);
//        scheduleGym.getEvents()
        try {
            scheduleService.findList(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadSchedule() {
        scheduleGym = new LazyScheduleGym(scheduleService, currBranch, currRoom, currPack);
    }

    public void onDateSelect(SelectEvent selectEvent) {
        Date dateSelected = (Date) selectEvent.getObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateSelected);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        event = new DefaultScheduleEvent("", dateSelected, calendar.getTime());
        Schedule schedule = new Schedule();
        CatRoom room = new CatRoom();
        room.setBranch(currBranch);
        room.setBranchId(currBranch.getBranchId());
        schedule.setRoom(room);
        ((DefaultScheduleEvent) event).setData(schedule);
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }


    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

    }

    public void onChangeView() {
//        System.out.println("kld");
    }

    public void addEvent(ActionEvent actionEvent) {

        try {
            String sqlCheckSchedule = "SELECT count(*)\n" +
                    "FROM SCHEDULE WHERE ROOM_ID = :roomId \n" +
                    "                    AND SCHEDULE_ID <> :scheduleId " +
                    "                    AND ( (:start <START_TIME and START_TIME < :end)  \n" +
                    "                         OR (:start < END_TIME and  END_TIME < :end )\n" +
                    "                         ) \n" +
                    "                    AND :start < :end";
            Map<String, Object> param = new HashMap<>();
            Schedule schedule = (Schedule) event.getData();
            param.put("roomId", schedule.getRoomId());
            param.put("start", event.getStartDate());
            param.put("end", event.getEndDate());
            if (schedule.getScheduleId() != null)
                param.put("scheduleId", schedule.getScheduleId());
            else
                sqlCheckSchedule = sqlCheckSchedule.replace("AND SCHEDULE_ID <> :scheduleId", "");

            List<?> count = scheduleService.findListSQLWithMapParameters(null, sqlCheckSchedule, -1, -1, param);
            if (!count.isEmpty() && ((BigDecimal) count.get(0)).longValue() == 0) {
//                schedule.setScheduleName(event.getTitle());
                schedule.setStartDate(event.getStartDate());
                schedule.setEndDate(event.getEndDate());
                schedule.setRoom(null);
                if (schedule.getEmployee() != null) schedule.setEmployeeId(schedule.getEmployee().getEmployeeId());
                if (event.getId() == null) {
                    try {
                        scheduleService.save(schedule);
                        scheduleGym.addEvent(event);
                        MessageUtil.setInfoMessage("Thêm lịch thành công");
                        RequestContext.getCurrentInstance().execute("PF('eventDialog').hide();");

                    } catch (AppException e) {
                        MessageUtil.setErrorMessage("Không thêm được lịch");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        scheduleService.saveOrUpdate(schedule);
                        scheduleGym.updateEvent(event);
                        MessageUtil.setInfoMessage("Cập nhật lịch thành công");
                        RequestContext.getCurrentInstance().execute("PF('eventDialog').hide();");
                    } catch (AppException e) {
                        MessageUtil.setErrorMessage("Không cập nhật được lịch");
                        e.printStackTrace();
                    }
                }
                event = new DefaultScheduleEvent();
            } else {
                MessageUtil.setErrorMessage("Lịch bị trùng");
            }
        } catch (SysException e) {
            e.printStackTrace();
            MessageUtil.setErrorMessage("Error");
        }
    }

    public ScheduleModel getScheduleGym() {
        return scheduleGym;
    }

    public void setScheduleGym(ScheduleModel scheduleGym) {
        this.scheduleGym = scheduleGym;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public GenericDaoServiceNewV2<Schedule, Long> getScheduleService() {
        return scheduleService;
    }

    public void setScheduleService(GenericDaoServiceNewV2<Schedule, Long> scheduleService) {
        this.scheduleService = scheduleService;
    }

    public CatBranch getCurrBranch() {
        return currBranch;
    }

    public void setCurrBranch(CatBranch currBranch) {
        this.currBranch = currBranch;
    }

    public CatRoom getCurrRoom() {
        return currRoom;
    }

    public void setCurrRoom(CatRoom currRoom) {
        this.currRoom = currRoom;
    }

    public CatPack getCurrPack() {
        return currPack;
    }

    public void setCurrPack(CatPack currPack) {
        this.currPack = currPack;
    }
}
