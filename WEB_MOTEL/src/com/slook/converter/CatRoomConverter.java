package com.slook.converter;

import com.slook.controller.CatGroupPackController;
import com.slook.controller.RoomController;
import com.slook.model.CatPack;
import com.slook.model.CatRoom;
import com.slook.persistence.GenericDaoImplNewV2;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created by xuanh on 5/7/2017.
 */
@FacesConverter(value = "catRoomConverter")
public class CatRoomConverter implements Converter {
    GenericDaoImplNewV2<CatRoom, Long> genericDaoImplNewV2 = new GenericDaoImplNewV2<CatRoom, Long>() {
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
        if (o !=null && o instanceof CatRoom)
            return ((CatRoom) o).getRoomId().toString();
        return null;
    }
}
