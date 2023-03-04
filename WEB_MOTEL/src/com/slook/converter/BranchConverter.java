package com.slook.converter;

import com.slook.controller.BranchController;
import com.slook.controller.MemberController;
import com.slook.model.CatBranch;
import com.slook.model.Member;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by xuanh on 4/24/2017.
 */
@FacesConverter(value = "branchConverter")
public class BranchConverter implements Converter
{
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        BranchController branchController = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{branchController}", BranchController.class);
        try
        {
            for (CatBranch catBranch : branchController.getBranchs())
            {
                if (catBranch.getBranchId().equals(Long.valueOf(s)))
                {
                    return catBranch;
                }
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        if (o != null && o instanceof CatBranch)
        {
            return ((CatBranch) o).getBranchId().toString();
        }
        return null;
    }
}
