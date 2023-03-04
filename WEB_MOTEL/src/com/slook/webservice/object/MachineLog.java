package com.slook.webservice.object;

/**
 * Created by T430 on 6/2/2017.
 */
public class MachineLog
{
    private Long machineId;
    private String ip;
    private String port;
    private Long status;

    public Long getMachineId()
    {
        return machineId;
    }

    public void setMachineId(Long machineId)
    {
        this.machineId = machineId;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }
}
