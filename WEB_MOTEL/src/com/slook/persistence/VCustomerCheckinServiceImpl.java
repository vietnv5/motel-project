/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_CatItemBO;
import com.slook.model.V_CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service(value = "vCustomerCheckinService")
public class VCustomerCheckinServiceImpl extends GenericDaoImplNewV2<V_CustomerCheckin, Long>
{

    private static VCustomerCheckinServiceImpl instance;

    public static VCustomerCheckinServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new VCustomerCheckinServiceImpl();
        }

        return instance;
    }
}
