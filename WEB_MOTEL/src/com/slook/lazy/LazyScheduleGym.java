package com.slook.lazy;


import com.slook.model.CatBranch;
import com.slook.model.CatPack;
import com.slook.model.CatRoom;
import com.slook.model.Schedule;
import com.slook.persistence.GenericDaoServiceNewV2;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanh on 5/29/2017.
 */
public class LazyScheduleGym extends LazyScheduleModel
{
    private GenericDaoServiceNewV2<Schedule, Long> scheduleService;
    CatBranch branch;
    CatRoom room;
    CatPack pack;
    Map<String, String> packStyles = new HashMap<>();

    public LazyScheduleGym(GenericDaoServiceNewV2 scheduleService, CatBranch branch, CatRoom room, CatPack pack)
    {
        this.scheduleService = scheduleService;
        this.branch = branch;
        this.room = room;
        this.pack = pack;
    }

    private LazyScheduleGym()
    {
        super();
    }

    @Override
    public void loadEvents(Date start, Date end)
    {
        try
        {
            Map<String, Object> filter = new HashMap<>();
            filter.put("startDate", new Date[]{start, end});
//            filter.put("endDate", new Date[]{null, end});
            if (branch != null && branch.getBranchId() != null && branch.getBranchId() > 0)
            {
                filter.put("room.branchId", branch.getBranchId());
            }
            if (room != null && room.getRoomId() != null)
            {
                filter.put("roomId", room.getRoomId());
            }
            if (pack != null && pack.getPackId() != null)
            {
                filter.put("packId", pack.getPackId());
            }
            List<Schedule> schdules = scheduleService.findList(filter);
            Map<Long, Long> indexPack = new HashMap<>();
            Map<Long, Long> indexRoom = new HashMap<>();

            long i = 1;
            long j = 1;
            for (Schedule schedule : schdules)
            {
                if (!indexPack.containsKey(schedule.getPackId()))
                {
                    indexPack.put(schedule.getPackId(), i++);
                }

                if (!indexRoom.containsKey(schedule.getRoomId()))
                {
                    indexRoom.put(schedule.getRoomId(), j++);
                }
            }
            packStyles.clear();
            for (Schedule schedule : schdules)
            {
                DefaultScheduleEvent event = new DefaultScheduleEvent(schedule.getScheduleName(), schedule.getStartDate(), schedule.getEndDate(), schedule);
                event.setTitle(schedule.getScheduleName() + " - " + schedule.getPack().getPackName() + " - " + schedule.getRoom().getRoomName());
                event.setStyleClass("Pack" + indexPack.get(schedule.getPackId()) + "Room" + indexRoom.get(schedule.getRoomId()));
//                System.out.println((schedule.getPackId() +"-"+ schedule.getRoomId() + ""));
                packStyles.put(schedule.getPack().getPackName() + " - " + schedule.getRoom().getRoomName(), event.getStyleClass());
//                System.out.println(event.getStyleClass());
                event.setEditable(false);
                event.setDescription("<b style>" + schedule.getScheduleName() + " - " + schedule.getRoom().getRoomName() + " - " + schedule.getRoom().getBranch().getBranchName() + "</b>");
                event.setDescription("<table style=\"background: #f5f5f5\">\n" +
                        "<tbody>\n" +
                        "<tr>\n" +
                        "<td>Lịch:</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + schedule.getScheduleName() + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>Tên câu lạc bộ</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + schedule.getRoom().getBranch().getBranchName() + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>Phòng tập</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + schedule.getRoom().getRoomName() + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>Tên gói lẻ</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + schedule.getPack().getPackName() + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>Từ:</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(schedule.getStartDate()) + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>Đến:</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(schedule.getEndDate()) + "</span></td>\n" +
                        "</tr>\n" +
                        "<tr>\n" +
                        "<td>HLV/Nhân viên</td>\n" +
                        "<td><span style=\"font-weight: 600\">" + (schedule.getEmployee() == null ? "" : schedule.getEmployee().getEmployeeName()) + "</span></td>\n" +
                        "</tr>\n" +
                        "</tbody>\n" +
                        "</table>");
                addEvent(event);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        RequestContext.getCurrentInstance().update("form:tip");

    }

    public Map<String, String> getPackStyles()
    {
        return packStyles;
    }

    public void setPackStyles(Map<String, String> packStyles)
    {
        this.packStyles = packStyles;
    }
}
