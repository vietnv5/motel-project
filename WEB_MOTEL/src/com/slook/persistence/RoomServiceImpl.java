/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.motel.model.Room;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV Jan 16, 2018
 */
@Scope("session")
@Service(value = "roomService")
public class RoomServiceImpl extends GenericDaoImplNewV2<Room, Long> {

    private static RoomServiceImpl instance;

    public static RoomServiceImpl getInstance() {
        if (instance == null) {
            instance = new RoomServiceImpl();
        }

        return instance;
    }
}
