/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.converter;

import com.slook.model.V_MemberDebtPayment;
import com.slook.persistence.VMemberDebtPaymentServiceImpl;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author SLOOK. JSC on Nov 2, 2017 8:53:35 AM
 */
@FacesConverter(value = "vMemberDebtPaymentConverter")
public class VMemberDebtPaymentConverter extends BaseConverter<V_MemberDebtPayment> implements Converter
{
    @Override
    public String getFieldId()
    {
        return "membershipId";
    }
}
