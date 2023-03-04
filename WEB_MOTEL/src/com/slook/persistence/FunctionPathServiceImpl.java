/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.FunctionPath;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV on Dec 19, 2017
 */
@Scope("session")
@Service(value = "functionPathService")
public class FunctionPathServiceImpl extends GenericDaoImplNewV2<FunctionPath, Long>
{

    private static FunctionPathServiceImpl instance;

    public static FunctionPathServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new FunctionPathServiceImpl();
        }
        return instance;
    }
}
