/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV on Oct 21, 2017
 */
@Scope("session")
@Service(value = "customerCheckinService")
public class CustomerCheckinServiceImpl extends GenericDaoImplNewV2<CustomerCheckin, Long>
{

    private static CustomerCheckinServiceImpl instance;

    public static CustomerCheckinServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new CustomerCheckinServiceImpl();
        }
        return instance;
    }
}
