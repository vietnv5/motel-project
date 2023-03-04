package com.slook.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.primefaces.model.ScheduleModel;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by xuanh on 5/21/2017.
 */
@Entity
public class Schedule
{
    private Long scheduleId;
    private String scheduleName;
    private Long roomId;
    private CatRoom room;
    private Long employeeId;
    Employee employee;
    private Date startDate;
    private Date endDate;
    private String createBy;
    private Long packId;
    private CatPack pack;

    @Id
    @Column(name = "SCHEDULE_ID")
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "SCHEDULE_SEQ", allocationSize = 1)
    public Long getScheduleId()
    {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId)
    {
        this.scheduleId = scheduleId;
    }

    @Basic
    @Column(name = "SCHEDULE_NAME")
    public String getScheduleName()
    {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName)
    {
        this.scheduleName = scheduleName;
    }

    @Basic
    @Column(name = "ROOM_ID")
    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
    public CatRoom getRoom()
    {
        return room;
    }

    public void setRoom(CatRoom room)
    {
        this.room = room;
    }

    @Basic
    @Column(name = "EMPLOYEE_ID")
    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "EMPLOYEE_ID", insertable = false, updatable = false)
    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Schedule schedule = (Schedule) o;

        if (scheduleId != null ? !scheduleId.equals(schedule.scheduleId) : schedule.scheduleId != null)
        {
            return false;
        }
        if (scheduleName != null ? !scheduleName.equals(schedule.scheduleName) : schedule.scheduleName != null)
        {
            return false;
        }
        if (roomId != null ? !roomId.equals(schedule.roomId) : schedule.roomId != null)
        {
            return false;
        }
        if (employeeId != null ? !employeeId.equals(schedule.employeeId) : schedule.employeeId != null)
        {
            return false;
        }
        if (startDate != null ? !startDate.equals(schedule.startDate) : schedule.startDate != null)
        {
            return false;
        }
        if (endDate != null ? !endDate.equals(schedule.endDate) : schedule.endDate != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = scheduleId != null ? scheduleId.hashCode() : 0;
        result = 31 * result + (scheduleName != null ? scheduleName.hashCode() : 0);
        result = 31 * result + (roomId != null ? roomId.hashCode() : 0);
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    @Column(name = "PACK_ID")
    public Long getPackId()
    {
        return packId;
    }

    public void setPackId(Long packId)
    {
        this.packId = packId;
    }

    @ManyToOne()
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "PACK_ID", insertable = false, updatable = false)
    public CatPack getPack()
    {
        return pack;
    }

    public void setPack(CatPack pack)
    {
        this.pack = pack;
    }
}
