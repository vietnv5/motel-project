/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.converter;

import com.slook.model.V_GroupMemberDebtPayment;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author SLOOK. JSC on Nov 2, 2017 10:37:43 AM
 */
@FacesConverter(value = "vGroupMemberDebtPaymentConverter")
public class VGroupMemberDebtPaymentConverter extends BaseConverter<V_GroupMemberDebtPayment>{

    @Override
    public String getFieldId() {
        return "groupMembershipId";
    }
}
