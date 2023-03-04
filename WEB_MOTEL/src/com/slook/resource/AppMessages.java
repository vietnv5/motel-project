/*
 * Created on Jun 7, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.resource;

import com.slook.util.LanguageBean;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Messages Utils for i18n
 *
 * @author xuanh
 * @version 1.0.0
 * @since 2017
 */
public class AppMessages extends ResourceBundle
{
    protected static final String BUNDLE_NAME = "com.slook.resource.messages";
    protected static final String BUNDLE_EXTENSION = "properties";
    protected static final String CHARSET = "UTF-8";
    protected static final Control UTF8_CONTROL = new UTF8Control();

    public AppMessages()
    {
        clearCache();
        Locale locale = LanguageBean.getLocales().get(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
        setParent(getBundle(BUNDLE_NAME, locale, UTF8_CONTROL));
    }

    @Override
    protected Object handleGetObject(String key)
    {
        return parent.getObject(key);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Enumeration getKeys()
    {
        return parent.getKeys();
    }

    public static class UTF8Control extends Control
    {
        public ResourceBundle newBundle(String baseName, Locale locale,
                                        String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException,
                IOException
        {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);

            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload)
            {
                URL url = loader.getResource(resourceName);
                if (url != null)
                {
                    URLConnection connection = url.openConnection();
                    if (connection != null)
                    {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            }
            else
            {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null)
            {
                try
                {
                    bundle = new PropertyResourceBundle(new InputStreamReader(
                            stream, CHARSET));
                }
                finally
                {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}