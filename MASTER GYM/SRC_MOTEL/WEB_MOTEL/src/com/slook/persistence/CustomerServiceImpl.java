/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.motel.model.Customer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV Jan 16, 2018
 */
@Scope("session")
@Service(value = "customerService")
public class CustomerServiceImpl extends GenericDaoImplNewV2<Customer, Long>  {
     private static CustomerServiceImpl instance;

    public static CustomerServiceImpl getInstance() {
        if (instance == null) {
            instance = new CustomerServiceImpl();
        }

        return instance;
    }
}
