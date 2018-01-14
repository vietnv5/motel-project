/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.DoorAccessStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV
 */
@Scope("session")
@Service(value = "doorAccessStatusService")
public class DoorAccessStatusServiceImpl extends GenericDaoImplNewV2<DoorAccessStatus, Long> {

    private static DoorAccessStatusServiceImpl instance;

    public static DoorAccessStatusServiceImpl getInstance() {
        if (instance == null) {
            instance = new DoorAccessStatusServiceImpl();
        }
        return instance;
    }
}
