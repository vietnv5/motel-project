/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.webservice.object;

/**
 * @author xuanh
 */
public class GnocActionBO
{

    private String troubleCode;
    private Long alarmId;
    private String startTime;
    private String tblAlarmCurrent;
    private String tblAlarmClear;
    private String tblAlarmHis;
    private Long ntmsActionId;//0-Clear; 1-Close

    @Override
    public String toString()
    {
        return new StringBuilder().append("NtmsActionBO{")
                .append("alarmId=").append(alarmId)
                .append(", troubleCode=").append(troubleCode)
                .append(", tblAlarmCurrent=").append(tblAlarmCurrent)
                .append(", tblAlarmClear=").append(tblAlarmClear)
                .append("}").toString();
    }


    public String getTroubleCode()
    {
        return troubleCode;
    }

    public void setTroubleCode(String troubleCode)
    {
        this.troubleCode = troubleCode;
    }


    public Long getAlarmId()
    {
        return alarmId;
    }

    public void setAlarmId(Long alarmId)
    {
        this.alarmId = alarmId;
    }


    public String getTblAlarmCurrent()
    {
        return tblAlarmCurrent;
    }

    public void setTblAlarmCurrent(String tblAlarmCurrent)
    {
        this.tblAlarmCurrent = tblAlarmCurrent;
    }

    public String getTblAlarmClear()
    {
        return tblAlarmClear;
    }

    public void setTblAlarmClear(String tblAlarmClear)
    {
        this.tblAlarmClear = tblAlarmClear;
    }


    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getTblAlarmHis()
    {
        return tblAlarmHis;
    }

    public void setTblAlarmHis(String tblAlarmHis)
    {
        this.tblAlarmHis = tblAlarmHis;
    }

    public Long getNtmsActionId()
    {
        return ntmsActionId;
    }

    public void setNtmsActionId(Long ntmsActionId)
    {
        this.ntmsActionId = ntmsActionId;
    }
}
