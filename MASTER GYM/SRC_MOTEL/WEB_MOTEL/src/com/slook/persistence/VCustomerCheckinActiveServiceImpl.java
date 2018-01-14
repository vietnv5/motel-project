/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.slook.model.V_CustomerCheckinActive;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author SLOOK. JSC on Nov 3, 2017 2:55:49 PM
 */
@Scope("session")
@Service(value = "vCustomerCheckinActiveService")
public class VCustomerCheckinActiveServiceImpl extends GenericDaoImplNewV2<V_CustomerCheckinActive, Long> {
 
    private static VCustomerCheckinActiveServiceImpl instance;

    public static VCustomerCheckinActiveServiceImpl getInstance() {
        if (instance == null) {
            instance = new VCustomerCheckinActiveServiceImpl();
        }
        return instance;
    }
}
