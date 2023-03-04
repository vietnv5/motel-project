package com.slook.controller;

import com.slook.model.*;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.FilterEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by Anonymous on 5/30/2017.
 */
@ViewScoped
@ManagedBean(name = "memberCareController")

public class MemberCareController
{
    private Date startDate;
    private Date endDate;

    private static final Logger logger = getLogger(MemberCareController.class);
    private MemberCareStatus status;
    private MemberCareStatus selectedSubmitStatus;

    private List<Member> members;
    private List<Employee> employees;

    private GenericDaoServiceNewV2<Member, Long> memberService;
    private GenericDaoServiceNewV2<MemberCareStatus, Long> statusService;
    private GenericDaoServiceNewV2<Employee, Long> employeService;
    private GenericDaoServiceNewV2<MemberCare, Long> memberCareService;

    private Long type = 1L;
    private CatUser userLogged;
    private Long selectStatus;
    private String result;

    private static final int CARE_BIRTH_DAY = 1;
    private static final int CARE_EXPIRE = 2;
    private static final int CARE_GIVE_UP = 3;

    private List<Member> selectedMembers;
    private List<MemberCare> selectedMemberCare;

    private Employee employee;
    private MemberModel memberModel;

    private String oldObjectStr = null;

    @PostConstruct
    public void init()
    {
        try
        {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            Map sessionMap = context.getSessionMap();
            memberService = new GenericDaoImplNewV2<Member, Long>()
            {
            };
            statusService = new GenericDaoImplNewV2<MemberCareStatus, Long>()
            {
            };
            employeService = new GenericDaoImplNewV2<Employee, Long>()
            {
            };
            memberCareService = new GenericDaoImplNewV2<MemberCare, Long>()
            {
            };
            selectedSubmitStatus = new MemberCareStatus();

            boolean isLogged = (boolean) sessionMap.get("authenticated");
            userLogged = new CatUser();
            if (isLogged)
            {
                userLogged = (CatUser) sessionMap.get("user");

                status = new MemberCareStatus();
                members = new ArrayList<>();

//                employees = employeService.findList();
                memberModel = getList(type);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public void adminCheckStatus()
    {
        try
        {
            if (selectedMembers == null || selectedMembers.size() == 0)
            {
                MessageUtil.setErrorMessage("Hãy chọn member");
                clearFilter("tableMemberCare");
                memberModel = getList(type);
                return;
            }

            for (int i = 0; i < selectedMembers.size(); i++)
            {
                Member m = selectedMembers.get(i);

                MemberCare memberCare = new MemberCare();
                memberCare.setStatusId(5L);
                List<MemberCare> l = findMemberCare(m.getMemberId(), type);
                if (l.size() > 0)
                {
                    memberCare.setMemberCareId(l.get(0).getMemberCareId());
                    memberCare.setMemberId(m.getMemberId());
                    memberCare.setEmployeeId(l.get(0).getEmployee().getEmployeeId());
                    memberCare.setType(type);
                    memberCare.setStartDate(l.get(0).getStartDate());
                    memberCare.setEndDate(l.get(0).getEndDate());

                    if (l.get(0).getStatusId() != 4)
                    {
                        MessageUtil.setErrorMessage("Chỉ có thể phê duyệt các tác vụ của nhân viên đã hoàn thành");
                    }
                    else
                    {
                        String oldmemberCare = l.get(0).toString();
                        memberCareService.saveOrUpdate(memberCare);
                        LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldmemberCare, memberCare.toString(), this.getClass().getSimpleName()
                                , (new Exception("get Name method").getStackTrace()[0].getMethodName()));

                        MessageUtil.setInfoMessageFromRes("info.save.success");
                    }
                }
            }

            selectedMembers.clear();
            clearFilter("tableMemberCare");
            memberModel = getList(type);
        }
        catch (Exception e)
        {
            MessageUtil.setInfoMessageFromRes("error.save.unsuccess");
            logger.error(e.getMessage(), e);
        }
    }

    public void updateMemberCare()
    {
        Employee employee = this.employee;
        try
        {
            if (employee == null)
            {
                MessageUtil.setErrorMessage("Chọn nhân viên");
                clearFilter("tableMemberCare");
                memberModel = getList(type);
                return;
            }
            if (selectedMembers == null || selectedMembers.size() == 0)
            {
                MessageUtil.setErrorMessage("Hãy chọn member");
                clearFilter("tableMemberCare");
                memberModel = getList(type);
                return;
            }
            if (getZeroTimeDate(startDate).compareTo(getZeroTimeDate(new Date())) < 0)
            {
                MessageUtil.setErrorMessage("Ngày bắt đầu phải từ hôm nay trở đi");
                clearFilter("tableMemberCare");
                memberModel = getList(type);
                return;
            }
            if (startDate.compareTo(endDate) >= 0)
            {
                MessageUtil.setErrorMessage("Ngày bắt đầu phải lớn hơn ngày kết thúc");
                clearFilter("tableMemberCare");
                memberModel = getList(type);
                return;
            }
            for (int i = 0; i < selectedMembers.size(); i++)
            {
                Member m = selectedMembers.get(i);
                System.out.println(m.getMemberName() + ":" + employee.getEmployeeName() + ":" + +employee.getEmployeeId() + ":" + type);

                MemberCare memberCare = new MemberCare();
                memberCare.setMemberId(m.getMemberId());
                memberCare.setEmployeeId(employee.getEmployeeId());
                memberCare.setStatusId(2L);
                memberCare.setType(type);
                memberCare.setStartDate(startDate);
                memberCare.setEndDate(endDate);

                List<MemberCare> l = findMemberCare(m.getMemberId(), type);
                String oldObj = null;
                if (l.size() > 0)
                {
                    memberCare.setMemberCareId(l.get(0).getMemberCareId());
                    if (l.get(0).getStatusId() == 4)
                    {
                        MessageUtil.setErrorMessage("Chăm sóc thành viên " + m.getMemberName() + " đã được thực hiện. Bạn chỉ có thể phê duyệt");
                        break;
                    }
                    oldObj = l.get(0).toString();
                }
                memberCareService.saveOrUpdate(memberCare);
                //ghi log
                if (oldObj != null)
                {
                    LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObj, memberCare.toString()
                            , this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
                }
                else
                {
                    LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, memberCare.toString(), this.getClass().getSimpleName()
                            , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
                }

                MessageUtil.setInfoMessageFromRes("info.save.success");
            }
            selectedMembers.clear();
            memberModel = getList(type);
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            logger.error(e.getMessage(), e);
        }

        clearFilter("tableMemberCare");
    }

    private List<MemberCare> findMemberCare(Long memberId, Long memberCareType) throws Exception
    {
        Map<String, Object> memberCareExist = new HashMap<>();
        memberCareExist.put("memberId", memberId);
        memberCareExist.put("type", memberCareType);
        return memberCareService.findList(memberCareExist);
    }

    public List<Member> getSelectedMembers()
    {
        return selectedMembers;
    }

    public void setSelectedMembers(List<Member> selectedMembers)
    {
        this.selectedMembers = selectedMembers;
    }

    public List<Employee> selectImployeList(String query)
    {
        if (query == "")
        {
            return null;
        }
        try
        {
            Map<String, Object> filter = new HashMap<>();
            filter.put("employeeCode", query);
            return employeService.findList(filter);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public MemberModel getList(Long type)
    {
        try
        {
            switch (type.intValue())
            {
                case CARE_BIRTH_DAY:
                {   // sinh nhat
                    DateTime now = new DateTime();
                    DateTime newTime = now.plusDays(14);
                    List<Member> l = memberService.findListAll(genHQL(now.getDayOfMonth(), now.getMonthOfYear(), newTime.getDayOfMonth(), newTime.getMonthOfYear()));
                    MemberModel m = new MemberModel(l);
                    return m;
                }
                case CARE_EXPIRE:
                {  // sap het han
                    return new MemberModel(memberService.findListAll(genHQLExpire(14)));
                }
                case CARE_GIVE_UP:
                {  // lau k den
                    String hql = "select m.member from MemberCheckin m where (m.memberId, m.checkInDate) in (select memberId, max(checkInDate) FROM MemberCheckin GROUP BY  memberId) and m.checkInDate < current_date -14 and m.member.status <> 2";
                    return new MemberModel(memberService.findListAll(hql));
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String findEmployeeName(Member mem)
    {
        for (MemberCare memberCare : mem.getMemberCares())
        {
            if (memberCare.getType() != null && memberCare.getType() == type)
            {
                return memberCare.getEmployee().getEmployeeName();
            }
        }
        return "";
    }

    public MemberCareModel getListCare(Long type)
    {
        try
        {

            Map<String, Object> filter = new HashMap<>();
            filter.clear();
            filter.put("type-EQ", type);
            filter.put("employeeId-EQ", userLogged.getEmpId());
            List<MemberCare> l = memberCareService.findList(filter);
            MemberCareModel m = new MemberCareModel(l);
            return m;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
//    public List<MemberCare> getListCare(Long type) {
//        try {
//            Map<String, Object> filter = new HashMap<>();
//            filter.clear();
//            filter.put("type-EQ", type);
//            filter.put("employeeId-EQ", userLogged.getEmpId());
//            List<MemberCare> l = memberCareService.findList(filter);
//            for(int i = 0; i < l.size(); i++) {
//                System.out.println(l.get(i).getMemberCareId());
//            }
//            System.out.println("===========================");
//            return l;
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//        }
//        return new ArrayList<>();
//    }

    public void onFilterChange(String widgetVar)
    {
        clearFilter(widgetVar);
        memberModel = getList(type);
    }

    private void clearFilter(String widgetVar)
    {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('" + widgetVar + "').clearFilters()");
    }

    public MemberCare findStatus(Long memberId, Long type)
    {
        MemberCare m = new MemberCare();
        try
        {
            Map<String, Object> filter = new HashMap<>();
            filter.put("type-EQ", type);
            filter.put("memberId-EQ", memberId);

            List<MemberCare> l = memberCareService.findList(filter);
            if (l.size() > 0)
            {
                return l.get(0);
            }
            return m;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        m.setStatusId(1L);
        return m;
    }

    public void updateStatus()
    {
        try
        {
            if (selectedMemberCare == null || selectedMemberCare.size() == 0)
            {
                clearFilter("tableMemberCareEmployee");
                MessageUtil.setErrorMessage("Hãy chọn member");
                return;
            }
            for (int i = 0; i < selectedMemberCare.size(); i++)
            {
                MemberCare mc = selectedMemberCare.get(i);
                if (mc.getStatusId() == 5)
                {
                    MessageUtil.setErrorMessage("Công việc đã đóng, bạn không thể thay đổi trạng thái");
                }
                else
                {
                    String oldMc = mc.toString();
                    mc.setStatusId(selectStatus);
                    mc.setResult(result);
                    memberCareService.saveOrUpdate(mc);
                    //log
                    LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, mc.toString(), this.getClass().getSimpleName()
                            , (new Exception("get Name method").getStackTrace()[0].getMethodName()));
                    MessageUtil.setInfoMessageFromRes("info.save.success");
                }
            }
            selectedMemberCare.clear();
            clearFilter("tableMemberCareEmployee");
        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            logger.error(e.getMessage(), e);
        }
    }

    private String genHQL(int startDay, int startMonth, int endDay, int endMonth)
    {
        if (endDay > startDay)
        {
            return String.format("FROM Member WHERE month(birthDay) = %d AND day(birthDay) >= %d AND day(birthDay) <= %d", startMonth, startDay, endDay);
        }
        return String.format("FROM Member WHERE (month(birthDay) = %d OR month(birthDay) = %d) AND day(birthDay) >= %d AND day(birthDay) <= %d", startMonth, endMonth, startDay, endDay);
    }

    private String genHQLExpire(int days)
    {
        DateTime now = new DateTime();
        DateTime newTime = now.plusDays(days);

        return String.format("FROM Member\n" +
                "WHERE endDate BETWEEN trunc(sysdate) AND TO_DATE('%02d/%02d/%04d', 'DD/MM/YYYY')", newTime.getDayOfMonth(), newTime.getMonthOfYear(), newTime.getYear());
    }

    public String typeToString(int type)
    {
        switch (type)
        {
            case CARE_BIRTH_DAY:
            {
                return "Sắp tới sinh nhật";
            }
            case CARE_EXPIRE:
            {
                return "Sắp hết hạn";
            }
            case CARE_GIVE_UP:
            {
                return "Lâu không tập";
            }
            default:
            {
                return "Không xác định";
            }
        }
    }

    public static Date getZeroTimeDate(Date fecha)
    {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }

    public MemberModel getMemberModel()
    {
        return memberModel;
    }

    public void setMemberModel(MemberModel memberModel)
    {
        this.memberModel = memberModel;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public List<MemberCare> getSelectedMemberCare()
    {
        return selectedMemberCare;
    }

    public void setSelectedMemberCare(List<MemberCare> selectedMemberCare)
    {
        this.selectedMemberCare = selectedMemberCare;
    }

    public Long getSelectStatus()
    {
        return selectStatus;
    }

    public void setSelectStatus(Long selectStatus)
    {
        this.selectStatus = selectStatus;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public CatUser getUserLogged()
    {
        return userLogged;
    }

    public void setUserLogged(CatUser userLogged)
    {
        this.userLogged = userLogged;
    }

    public List<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(List<Employee> employees)
    {
        this.employees = employees;
    }

    public GenericDaoServiceNewV2<Member, Long> getMemberService()
    {
        return memberService;
    }

    public void setMemberService(GenericDaoServiceNewV2<Member, Long> memberService)
    {
        this.memberService = memberService;
    }


    public MemberCareStatus getStatus()
    {
        return status;
    }

    public void setStatus(MemberCareStatus status)
    {
        this.status = status;
    }

    public List<Member> getMembers()
    {
        return members;
    }

    public void setMembers(List<Member> members)
    {
        this.members = members;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    private void addMessage(FacesMessage message)
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Long getType()
    {
        return type;
    }

    public void setType(Long type)
    {
        this.type = type;
    }

    public int getCARE_BIRTH_DAY()
    {
        return CARE_BIRTH_DAY;
    }

    public int getCARE_EXPIRE()
    {
        return CARE_EXPIRE;
    }


    public int getCARE_GIVE_UP()
    {
        return CARE_GIVE_UP;
    }

    public MemberCareStatus getSelectedSubmitStatus()
    {
        return selectedSubmitStatus;
    }

    public void setSelectedSubmitStatus(MemberCareStatus selectedSubmitStatus)
    {
        this.selectedSubmitStatus = selectedSubmitStatus;
    }
}
