/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.converter;

import com.slook.model.FunctionPath;
import com.slook.persistence.FunctionPathServiceImpl;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author VietNV on Dec 19, 2017
 */
@FacesConverter(value = "functionPathConverter")
public class FunctionPathConverter  implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        try {
            return FunctionPathServiceImpl.getInstance().findById(Long.valueOf(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        try {

            if (o != null && o instanceof FunctionPath && ((FunctionPath) o).getFunctionPathId()!=null) {
                return ((FunctionPath) o).getFunctionPathId().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
