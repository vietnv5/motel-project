package com.slook.object;

import com.google.common.base.Splitter;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dungvv8 on 12/23/2016.
 */
public class ConfigObj
{
    private String day;
    private List<String> days;
    private List<String> hours;
    private List<String> selectedDays;
    private List<String> selectedHours;
    private Long fromHour;
    private Long toHour;
    private Long fromDay;
    private Long toDay;
    private boolean disabled;
    private String hour;
    private String warning;
    private String critical;
    private String unitType;
    private List<SelectItem> listDay;
    private List<SelectItem> listHour;

    public String getUnitType()
    {
        return unitType;
    }

    public void setUnitType(String unitType)
    {
        this.unitType = unitType;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public Long getFromDay()
    {
        return fromDay;
    }

    public void setFromDay(Long fromDay)
    {
        this.fromDay = fromDay;
    }

    public Long getToDay()
    {
        return toDay;
    }

    public void setToDay(Long toDay)
    {
        this.toDay = toDay;
    }

    public Long getFromHour()
    {
        return fromHour;
    }

    public void setFromHour(Long fromHour)
    {
        this.fromHour = fromHour;
    }

    public Long getToHour()
    {
        return toHour;
    }

    public void setToHour(Long toHour)
    {
        this.toHour = toHour;
    }

    public List<String> getDays()
    {
        return Arrays.asList(new String[]{"T2", "T3", "T4", "T5", "T6", "T7", "CN"});
    }

    public void setDays(List<String> days)
    {
        this.days = days;
    }

    public List<String> getHours()
    {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++)
        {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public void setHours(List<String> hours)
    {
        this.hours = hours;
    }

    public List<String> getSelectedHours()
    {
        return this.selectedHours;
    }

    public List<String> getSelectedHourValue()
    {
        return this.selectedHours;
    }

    public void setSelectedHours(List<String> selectedHours)
    {
        this.selectedHours = selectedHours;
    }

    public List<String> getSelectedDays()
    {
        return this.selectedDays;
    }

    public List<String> getSelectedDaysValue()
    {
        return this.selectedDays;
    }

    public void setSelectedDays(List<String> selectedDays)
    {
        this.selectedDays = selectedDays;
    }

    public List<SelectItem> getListDay()
    {
        List<SelectItem> list = new ArrayList<>();
        List<String> days = Splitter.on(",").splitToList(this.day);
        List<String> week = Arrays.asList(new String[]{"T2", "T3", "T4", "T5", "T6", "T7", "CN"});

        for (String dayOfWeek : week)
        {
            if (days.contains(dayOfWeek))
            {
                list.add(new SelectItem(true, dayOfWeek));
            }
            else
            {
                list.add(new SelectItem(false, dayOfWeek));
            }
        }
        return list;
    }

    public void setListDay(List<SelectItem> listDay)
    {
        this.listDay = listDay;
    }

    public List<SelectItem> getListHour()
    {
        return listHour;
    }

    public void setListHour(List<SelectItem> listHour)
    {
        this.listHour = listHour;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getHour()
    {
        return hour;
    }

    public void setHour(String hour)
    {
        this.hour = hour;
    }

    public String getWarning()
    {
        return warning;
    }

    public void setWarning(String warning)
    {
        this.warning = warning;
    }

    public String getCritical()
    {
        return critical;
    }

    public void setCritical(String critical)
    {
        this.critical = critical;
    }
}
