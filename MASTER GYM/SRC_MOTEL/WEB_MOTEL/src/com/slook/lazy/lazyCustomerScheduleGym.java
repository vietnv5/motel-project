package com.slook.lazy;

import com.slook.model.CatBranch;
import com.slook.model.CatPack;
import com.slook.model.CatRoom;
import com.slook.model.CustomerSchedule;
import com.slook.model.CustomerSchedule;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author VietNV
 */
public class lazyCustomerScheduleGym extends LazyScheduleModel {

    private GenericDaoServiceNewV2<CustomerSchedule, Long> scheduleService;
    CatBranch branch;
//    CatRoom room;
//    CatPack pack;
    Map<String, String> packStyles = new HashMap<>();
    Map<Long, String> mapStyle = new HashMap<>();

    public lazyCustomerScheduleGym(GenericDaoServiceNewV2 scheduleService, CatBranch branch) {
        this.scheduleService = scheduleService;
        this.branch = branch;
//        this.room = room;
//        this.pack = pack;
        mapStyle.clear();
        mapStyle.put(Constant.CUSTOMER_SCHEDULE.STATUS_SCHEDULE, "schedule");
        mapStyle.put(Constant.CUSTOMER_SCHEDULE.STATUS_ACTIVE, "activeSchedule");
        mapStyle.put(Constant.CUSTOMER_SCHEDULE.STATUS_COMPLETED, "completedSchedule");
        mapStyle.put(Constant.CUSTOMER_SCHEDULE.STATUS_CANCEL, "cancelSchedule");

    }

    private lazyCustomerScheduleGym() {
        super();
    }

    @Override
    public void loadEvents(Date start, Date end) {

        try {

            Map<String, Object> filter = new HashMap<>();
            filter.put("startTime", new Date[]{start, end});
            if (branch != null && branch.getBranchId() != null && branch.getBranchId() > 0) {
                filter.put("branchId", branch.getBranchId());
            }
//            if (room!=null && room.getRoomId()!=null){
//                filter.put("roomId",room.getRoomId());
//            }
//            if (pack!=null && pack.getPackId()!=null){
//                filter.put("packId",pack.getPackId());
//            }
            List<CustomerSchedule> schdules = scheduleService.findList(filter);
            Map<Long, Long> indexPack = new HashMap<>();
            Map<Long, Long> indexRoom = new HashMap<>();

            long i = 1;
            long j = 1;

            for (CustomerSchedule schedule : schdules) {
                if (!indexPack.containsKey(schedule.getCustomerId())) {
                    indexPack.put(schedule.getCustomerId(), (i++ % 10) + 1);
                }

                if (!indexRoom.containsKey(schedule.getCustomerScheduleId())) {
                    indexRoom.put(schedule.getCustomerScheduleId(), (j++ % 4) + 1);
                }
            }
            packStyles.clear();
            for (CustomerSchedule schedule : schdules) {
                DefaultScheduleEvent event = new DefaultScheduleEvent(schedule.getName(), schedule.getStartTime(), schedule.getEndTime(), schedule);
                event.setTitle(schedule.getName() + " - " + schedule.getLstPackName());
//                event.setStyleClass("Pack" + indexPack.get(schedule.getCustomerId()) + "Room" + indexRoom.get(schedule.getCustomerScheduleId()));
                event.setStyleClass(mapStyle.get(schedule.getStatus()));
                packStyles.put(schedule.getName() + " - " + schedule.getLstPackName(), event.getStyleClass());
                event.setEditable(false);
                event.setDescription("<b style>" + schedule.getCustomerId() + " - " + schedule.getCustomerScheduleId() + "</b>");
                event.setDescription("<table style=\"background: #f5f5f5\">\n"
                        + "<tbody>\n"
                        + "<tr>\n"
                        + "<td>Khách hàng:</td>\n"
                        + "<td><span style=\"font-weight: 600\">" + schedule.getName() + "</span></td>\n"
                        + "</tr>\n"
                        + "<tr>\n"
                        //                        + "<td>Tên câu lạc bộ</td>\n"
                        //                        + "<td><span style=\"font-weight: 600\">" + schedule.getRoom().getBranch().getBranchName() + "</span></td>\n"
                        //                        + "</tr>\n"
                        //                        + "<tr>\n"
                        //                        + "<td>Phòng tập</td>\n"
                        //                        + "<td><span style=\"font-weight: 600\">" + schedule.getRoom().getRoomName() + "</span></td>\n"
                        //                        + "</tr>\n"
                        //                        + "<tr>\n"
                        + "<td>Gói</td>\n"
                        + "<td><span style=\"font-weight: 600\">" + schedule.getLstPackName() + "</span></td>\n"
                        + "</tr>\n"
                        + "<tr>\n"
                        + "<td>Từ:</td>\n"
                        + "<td><span style=\"font-weight: 600\">" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(schedule.getStartTime()) + "</span></td>\n"
                        + "</tr>\n"
                        + "<tr>\n"
                        + "<td>Đến:</td>\n"
                        + "<td><span style=\"font-weight: 600\">" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(schedule.getEndTime()) + "</span></td>\n"
                        + "</tr>\n"
                        + "<tr>\n"
                        + "<td>HLV/Nhân viên</td>\n"
                        + "<td><span style=\"font-weight: 600\">" + (schedule.getEmployee() == null ? "" : schedule.getEmployee().getEmployeeName()) + "</span></td>\n"
                        + "</tr>\n"
                        + "</tbody>\n"
                        + "</table>");
                addEvent(event);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestContext.getCurrentInstance().update("customerScheduleForm:tip");

    }

    public Map<String, String> getPackStyles() {
        return packStyles;
    }

    public void setPackStyles(Map<String, String> packStyles) {
        this.packStyles = packStyles;
    }
}
