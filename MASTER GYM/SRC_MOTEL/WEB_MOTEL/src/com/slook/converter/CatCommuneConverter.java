package com.slook.converter;

import com.slook.model.CatCommune;
import com.slook.model.CatRoom;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by xuanh on 5/7/2017.
 */
@FacesConverter(value = "catCommuneConverter")
public class CatCommuneConverter implements Converter {
    GenericDaoImplNewV2<CatCommune, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<CatCommune, Long>() {
    };
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
        if (o !=null && o instanceof CatCommune)
            return ((CatCommune) o).getId().toString();
        return null;
    }
}
