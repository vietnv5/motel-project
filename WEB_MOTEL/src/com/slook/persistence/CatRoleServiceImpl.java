/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.slook.model.CatRole;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author VietNV on Dec 18, 2017
 */
@Scope("session")
@Service(value = "catRoleService")
public class CatRoleServiceImpl extends GenericDaoImplNewV2<CatRole, Long>
{

    private static CatRoleServiceImpl instance;

    public static CatRoleServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new CatRoleServiceImpl();
        }
        return instance;
    }
}
