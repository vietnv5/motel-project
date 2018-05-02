/*
 * Created on Jun 7, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.controller;

import com.slook.exception.SysException;
import com.slook.util.SessionUtil;
import org.apache.log4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

import static org.apache.log4j.Logger.getLogger;

/**
 * Chức năng chính để forward các url tới phần phân quyền.
 *
 * @author Nguyen Hai Ha (hanh45@viettel.com.vn)
 * @version 1.0.0
 * @since Jun 7, 2013
 */
@RequestScoped
@ManagedBean(name = "forwardService")
public class ForwardController implements Serializable {
    private static final long serialVersionUID = 4870520554535423726L;
    // Trang home.
    private static final String _HOME_PAGE = "/home";
    private static final Logger logger = getLogger(ForwardController.class);

    /**
     * Dieu huong den trang home page.
     */
    private void homeForward() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        try {
            fc.getExternalContext().redirect(req.getContextPath() + _HOME_PAGE);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Redirect toi trang home.
     *
     * @throws IOException
     */
    public void doForward(final ComponentSystemEvent event) throws IOException {
        homeForward();
    }

    /**
     * Dieu huong den trang mac dinh cua user.
     *
     * @throws IOException
     */
    public void doRedirect(final ComponentSystemEvent event) throws IOException {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();

            // Lay gia tri menu default cua user dang nhap.
            String defaultUrl = SessionUtil.getMenuDefault();
            if (defaultUrl == "")
                homeForward();
            else
                fc.getExternalContext().redirect(req.getContextPath() + defaultUrl);
        } catch (SysException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}