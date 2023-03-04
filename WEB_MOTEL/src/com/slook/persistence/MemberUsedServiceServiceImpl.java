/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.slook.model.MemberUsedService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV on Nov 30, 2017
 */
@Service(value = "memberUsedServiceService")
@Scope("session")
public class MemberUsedServiceServiceImpl extends GenericDaoImplNewV2<MemberUsedService, Long>
{

    private static MemberUsedServiceServiceImpl instance;

    public static MemberUsedServiceServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new MemberUsedServiceServiceImpl();
        }

        return instance;
    }
}
