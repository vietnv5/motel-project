/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.converter;

import com.slook.model.CatService;
import com.slook.persistence.GenericDaoImplNewV2;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author VietNV
 */
@FacesConverter(value = "catServiceConverter")
public class CatServiceConverter implements Converter {

    GenericDaoImplNewV2<CatService, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<CatService, Long>() {
    };

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            if (s != null && !"null".equalsIgnoreCase(s.trim())) {
                if ("".equals(s.trim())) {
                    return null;
                }
                return genericDaoImplNewV2.findById(Long.valueOf(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null && o instanceof CatService && ((CatService) o).getServiceId()!=null) {
            return ((CatService) o).getServiceId().toString();
        }
        return null;
    }
}
