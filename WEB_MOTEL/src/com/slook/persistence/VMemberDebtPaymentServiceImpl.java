/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_MemberDebtPayment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author SLOOK. JSC
 */
@Scope("session")
@Service(value = "vMemberDebtPaymentService")
public class VMemberDebtPaymentServiceImpl extends GenericDaoImplNewV2<V_MemberDebtPayment, Long>
{

    private static VMemberDebtPaymentServiceImpl instance;

    public static VMemberDebtPaymentServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new VMemberDebtPaymentServiceImpl();
        }
        return instance;
    }
}
