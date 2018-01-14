package com.slook.converter;

import com.slook.model.CatAddressCity;
import com.slook.model.CatAddressDistrict;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by Antz01 on 12/06/2017.
 */
@FacesConverter(value = "addressDistrictConverter")
public class AddressDistrictConverter implements Converter {
    GenericDaoImplNewV2<CatAddressDistrict, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<CatAddressDistrict, Long>() {};

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
        if (o !=null && o instanceof CatAddressDistrict)
            return ((CatAddressDistrict) o).getDistrictId().toString();
        return null;
    }
}
