/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.CatMachine;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV on Oct 21, 2017
 */
@Scope("session")
@Service(value = "catMachineService")
public class CatMachineServiceImpl extends GenericDaoImplNewV2<CatMachine, Long>
{

    private static CatMachineServiceImpl instance;

    public static CatMachineServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new CatMachineServiceImpl();
        }
        return instance;
    }

}
