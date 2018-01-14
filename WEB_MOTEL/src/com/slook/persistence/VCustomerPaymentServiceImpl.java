/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.slook.model.V_CustomerPayment;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV on Dec 2, 2017
 */
@Scope("session")
@Service(value = "vCustomerPaymentService")
public class VCustomerPaymentServiceImpl  extends GenericDaoImplNewV2<V_CustomerPayment, Long> {

    private static final Logger logger = getLogger(VCustomerPaymentServiceImpl.class);

    private static VCustomerPaymentServiceImpl instance;

    public static VCustomerPaymentServiceImpl getInstance() {
        if (instance == null) {
            instance = new VCustomerPaymentServiceImpl();
        }
        return instance;
    }
    
    

}
