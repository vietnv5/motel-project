package com.slook.converter;

import com.slook.model.CatMachine;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "catMachineConverter")
public class CatMachineConverter implements Converter
{
    GenericDaoImplNewV2<CatMachine, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<CatMachine, Long>()
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
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o)
    {
        if (o != null && o instanceof CatMachine && ((CatMachine) o).getMachineId() != null)
        {
            return ((CatMachine) o).getMachineId().toString();
        }
        return null;
    }
}
