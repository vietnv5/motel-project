package com.slook.controller;

import com.slook.model.CatUser;
import com.slook.persistence.CatUserServiceImpl;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.log4j.Logger.getLogger;
import static org.omnifaces.util.Faces.getRequest;

/**
 * Created by T430 on 4/5/2017.
 */
@ViewScoped
@ManagedBean
public class AuthenticationController implements Constant, Serializable
{

    private static final Logger logger = getLogger(AuthenticationController.class);

    private String userName;
    private String password;
    private CatUser user;
    //    @ManagedProperty(value = "#{catUserService}")
    private CatUserServiceImpl catUserService;
    int TYPE_USERNAME = 0;
    int TYPE_EMAIL = 1;
    int TYPE_PHONE = 2;

    public boolean authenticated()
    {
        try
        {
            catUserService = new CatUserServiceImpl();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            Map sessionMap = context.getSessionMap();
            int typeAcount = checkTypeAcount(userName);
            Map<String, Object> filters = new HashMap<>();
            if (typeAcount == TYPE_USERNAME)
            {
                filters.put("userName-EXAC_IGNORE_CASE", userName);
            }
            else if (typeAcount == TYPE_EMAIL)
            {
                filters.put("email-EXAC_IGNORE_CASE", userName);
            }
            else if (typeAcount == TYPE_PHONE)
            {
                filters.put("phoneNumber-EXAC_IGNORE_CASE", userName);
            }
            filters.put("password-EXAC", password);
            filters.put("status", Constant.STATUS.ACTIVE);
            List<CatUser> list = catUserService.findList(filters);
            if (list != null && !list.isEmpty() && list.size() == 1)
            {
                user = list.get(0);
                sessionMap.put("user", user);
                sessionMap.put("authenticated", true);
                return true;
            }
//            if ("admin".equals(userName) && "admin@!@#$".equals(password)) {
//                user.setUserName(userName);
//                user.setPassword(password);
//                sessionMap.put("user", user);
//                sessionMap.put("authenticated", true);
//                return true;
//            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public int checkTypeAcount(String userName)
    {
        int type = TYPE_USERNAME;
        if (userName == null)
        {
            return TYPE_USERNAME;
        }
        if (userName.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
        {
            return TYPE_EMAIL;
        }

        if (userName.matches("^[0-9\\-\\+]{4,25}$"))
        {
            return TYPE_PHONE;
        }

        return type;
    }

    public void login()
    {
        try
        {
            if (authenticated())
            {
                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//                fc.getExternalContext().redirect(req.getContextPath() + PAGE._DEFAULT_URL);
                String defaultUrl = DataConfig.getConfigByKey("DEFAULT_URL") != null
                        ? DataConfig.getConfigByKey("DEFAULT_URL") : PAGE._DEFAULT_URL;
                fc.getExternalContext().redirect(req.getContextPath() + defaultUrl);
            }
            else
            {
                MessageUtil.setErrorMessage("Username or Password is not correct");
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void logout()
    {
        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            fc.getExternalContext().redirect(req.getContextPath() + PAGE._LOGIN);

            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            httpSession.invalidate();

        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public CatUser getUser()
    {

        return (CatUser) getRequest().getSession().getAttribute("user");
    }

    public void setUser(CatUser user)
    {
        this.user = user;
    }

    public CatUserServiceImpl getCatUserService()
    {
        return catUserService;
    }

    public void setCatUserService(CatUserServiceImpl catUserService)
    {
        this.catUserService = catUserService;
    }

}
