/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_CustomerAccessStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV
 */
@Scope("session")
@Service(value = "vCustomerAccessStatusService")
public class VCustomerAccessStatusServiceImpl extends GenericDaoImplNewV2<V_CustomerAccessStatus, Long> {

    private static VCustomerAccessStatusServiceImpl instance;

    public static VCustomerAccessStatusServiceImpl getInstance() {
        if (instance == null) {
            instance = new VCustomerAccessStatusServiceImpl();
        }
        return instance;
    }

}
