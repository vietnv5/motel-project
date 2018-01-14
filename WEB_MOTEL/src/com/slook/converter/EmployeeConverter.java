package com.slook.converter;

import com.slook.model.Employee;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by Antz01 on 07/06/2017.
 */
@FacesConverter(value = "employeeConverter")
public class EmployeeConverter implements Converter {
    GenericDaoImplNewV2<Employee, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<Employee, Long>() {};

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return genericDaoImplNewV2.findById(Long.valueOf(s));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o !=null && o instanceof Employee && ((Employee) o).getEmployeeId()!=null)
            return ((Employee) o).getEmployeeId().toString();
        return null;
    }
}
