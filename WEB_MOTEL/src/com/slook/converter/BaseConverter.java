/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.converter;

import com.slook.util.DataUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vietnv14
 * @see dung de lua chon gia trị ngay tại list cua combox; chi su dung cho combobox , KHONG THE su dung cho autocompleted
 */
public abstract class BaseConverter<T extends Serializable> implements Converter
{
    protected static final Logger logger = LoggerFactory.getLogger(BaseConverter.class);

    public abstract String getFieldId();

    private Class<T> domainClass;

    private Method getMethodId()
    {
        try
        {
            java.lang.reflect.Type genericSuperclass = getClass().getGenericSuperclass();
            this.domainClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            Method methods = domainClass.getMethod(DataUtil.getGetterOfColumn(getFieldId()));
            return methods;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
    {
        T ret = null;
        UIComponent src = arg1;

        Method methods = getMethodId();

        if (src != null)
        {
            List<UIComponent> childs = src.getChildren();
            UISelectItems itens = null;
            if (childs != null)
            {
                for (UIComponent ui : childs)
                {
                    if (ui instanceof UISelectItems)
                    {
                        itens = (UISelectItems) ui;
                        break;
                    }
                    else if (ui instanceof UISelectItem)
                    {
                        UISelectItem item = (UISelectItem) ui;
                        try
                        {
                            T val = (T) item.getItemValue();
                            Object value = methods.invoke(val);
                            if (value != null && arg2.equals("" + value))
                            {
                                ret = val;
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            logger.debug(e.getMessage(), e);
                        }
                    }
                }
            }

            if (itens != null)
            {
                List<T> values = (List<T>) itens.getValue();
                if (values != null)
                {
                    for (T val : values)
                    {
                        try
                        {
                            Object value = methods.invoke(val);
                            if (value != null && arg2.equals("" + value))
                            {
                                ret = val;
                                break;
                            }

                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
    {
        String ret = "";
        Method methods = getMethodId();
        if (arg2 != null)
        {
            try
            {
                T m = (T) arg2;
                ret += methods.invoke(m);
            }
            catch (Exception e)
            {
                java.util.logging.Logger.getLogger(BaseConverter.class.getName()).log(Level.INFO, null, e);
            }
        }
        return ret;
    }

}
