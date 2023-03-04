/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.motel.model.CustomerRoom;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV Jan 21, 2018
 */
@Scope("session")
@Service(value = "customerRoomService")
public class CustomerRoomServiceImpl extends GenericDaoImplNewV2<CustomerRoom, Long>
{

    private static CustomerRoomServiceImpl instance;

    public static CustomerRoomServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new CustomerRoomServiceImpl();
        }
        return instance;
    }
}
