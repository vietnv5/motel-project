/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.motel.model.Contract;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV Feb 6, 2018
 */
@Scope("session")
@Service(value = "contractServiceImpl")
public class ContractServiceImpl extends GenericDaoImplNewV2<Contract, Long>
{

    private static ContractServiceImpl instance;

    public static ContractServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new ContractServiceImpl();
        }
        return instance;
    }
}
