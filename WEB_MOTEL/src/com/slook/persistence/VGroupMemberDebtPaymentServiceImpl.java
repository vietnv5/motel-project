/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_GroupMemberDebtPayment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author SLOOK. JSC
 */
@Scope("session")
@Service(value = "vGroupMemberDebtPaymentService")
public class VGroupMemberDebtPaymentServiceImpl extends GenericDaoImplNewV2<V_GroupMemberDebtPayment, Long>  {
    
    private static VGroupMemberDebtPaymentServiceImpl instance;

    public static VGroupMemberDebtPaymentServiceImpl getInstance() {
        if (instance == null) {
            instance = new VGroupMemberDebtPaymentServiceImpl();
        }
        return instance;
    }
}
