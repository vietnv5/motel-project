package com.slook.converter;

import com.slook.controller.CatGroupPackController;
import com.slook.model.CatPack;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by xuanh on 5/7/2017.
 */
@FacesConverter(value = "catPackConverter")
public class CatPackConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        CatGroupPackController catGroupPackController = facesContext.getApplication().evaluateExpressionGet(facesContext,"#{catGroupPackController}",CatGroupPackController.class) ;
        try {
            return catGroupPackController.getCatPackService().findById(Long.valueOf(s));
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o !=null && o instanceof CatPack && ((CatPack) o).getPackId()!=null)
            return ((CatPack) o).getPackId().toString();
        return null;
    }
}
