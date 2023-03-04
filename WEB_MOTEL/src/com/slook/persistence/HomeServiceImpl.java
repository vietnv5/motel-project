/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.motel.model.Home;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV Jan 17, 2018
 */
@Scope("session")
@Service(value = "homeService")
public class HomeServiceImpl extends GenericDaoImplNewV2<Home, Long>
{

    private static HomeServiceImpl instance;

    public static HomeServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new HomeServiceImpl();
        }
        return instance;
    }
}
