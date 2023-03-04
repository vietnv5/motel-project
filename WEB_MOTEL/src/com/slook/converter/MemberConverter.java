package com.slook.converter;

import com.slook.model.Member;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by xuanh on 5/7/2017.
 */
@FacesConverter(value = "memberConverter")
public class MemberConverter implements Converter
{

    GenericDaoImplNewV2<Member, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<Member, Long>()
    {
    };

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s)
    {
        try
        {
            return genericDaoImplNewV2.findById(Long.valueOf(s));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        try
        {

            if (o != null && o instanceof Member && ((Member) o).getMemberId() != null)
            {
                return ((Member) o).getMemberId().toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
