/*
 * Created on Jun 7, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.util;

import com.slook.model.CatUser;
import com.slook.model.FunctionPath;
//import com.viettel.vsa.token.ObjectToken;
//import com.viettel.vsa.token.UserToken;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Map;

/**
 * Các hàm thao tác với session cơ bản của cả hệ thống.
 *
 * @author Nguyen Hai Ha (hanh45@viettel.com.vn)
 * @version 1.0.0
 * @since Jun 7, 2013
 */
public class SessionWrapper implements Serializable
{

    private static final long serialVersionUID = -8318262775763386620L;
    private static final String _USER_TOKEN = Constant._USER_TOKEN;
    ;
    private static final String _VSA_USER_ID = "netID";

    /**
     * Get current session cua he thong.
     *
     * @return current session
     */
    public static HttpSession getCurrentSession()
    {
        HttpServletRequest request = (HttpServletRequest) FacesContext
                .getCurrentInstance().getExternalContext().getRequest();
        return request.getSession();
    }

    /**
     * Lay gia tri session attribute.
     *
     * @param attributeName
     * @return
     */
    public String getSessionAttribute(String attributeName)
    {
        return (String) getCurrentSession().getAttribute(attributeName);
    }

    /**
     * Lay thong tin cua user hien tai dang login.
     */
    public static String getCurrentUsername()
    {
        try
        {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            Map sessionMap = context.getSessionMap();
            return ((CatUser) sessionMap.get("user")).getUserName();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiem tra xem URL nay co duoc truy cap khong.
     *
     * @return
     */
    public boolean getUrlDisplay(String urlCode)
    {
        if (DataConfig.getConfigByKey("IS_TEST") != null && DataConfig.getConfigByKey("IS_TEST").equals("1"))
        {
            return true;
        }
        HttpSession session = getCurrentSession();
        boolean result = false;

        String objToken;
        CatUser userToken = (CatUser) session.getAttribute(_USER_TOKEN);
        if (userToken != null && userToken.getRole() != null && userToken.getRole().getFunctionPaths() != null && userToken.getRole().getFunctionPaths().size() > 0)
        {
            for (FunctionPath ot : userToken.getRole().getFunctionPaths())
            {
                objToken = ot.getUrl();
                if (objToken.equalsIgnoreCase(urlCode))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

/*
    protected static Boolean getUrlByKey(String key) {
        HttpSession session = getCurrentSession();
        String url;
        UserToken userToken = (UserToken) session.getAttribute(_USER_TOKEN);
        if (userToken != null) {
            for (ObjectToken ot : userToken.getObjectTokens()) {
                url = ot.getObjectUrl();
                if (key.equalsIgnoreCase(url)) {
                    return true;
                }
            }
        } else {
            return false;
        }

        return false;
    }
    */
}// End class
