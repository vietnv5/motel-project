package com.slook.controller;

import com.slook.model.MemberCareStatus;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by Antz01 on 02/06/2017.
 */
@ViewScoped
@ManagedBean
public class MemberCareStatusController
{
    private static final Logger logger = getLogger(MemberCareStatusController.class);

    List<MemberCareStatus> statusList;
    private GenericDaoServiceNewV2<MemberCareStatus, Long> statusService;

    @PostConstruct
    public void init()
    {
        statusList = new ArrayList<>();
        statusService = new GenericDaoImplNewV2<MemberCareStatus, Long>()
        {
        };
    }

    public List<MemberCareStatus> findStatusList(String roleCode)
    {
        List<MemberCareStatus> l = new ArrayList<>();
        try
        {
            Map<String, Object> filter = new HashMap<>();
            filter.put("role.roleCode-EQ", roleCode);
            l = statusService.findList(filter);

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return l;
    }

}
