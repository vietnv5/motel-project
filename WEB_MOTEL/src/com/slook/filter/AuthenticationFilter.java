package com.slook.filter;


import com.slook.util.Constant;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by T430 on 4/15/2017.
 */
public class AuthenticationFilter implements Filter, Constant {
    private static final Logger logger = getLogger(AuthenticationFilter.class);
    public static String[] allowedUrls;

    static {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("cas_en_US");
            if(rb==null) rb = ResourceBundle.getBundle("cas");
//            String serviceURL = rb.getString("service");
//            String domainCode = rb.getString("domainCode");
//            String passportValidateURL = rb.getString("validateUrl");
//            String errorUrl = rb.getString("errorUrl");
            allowedUrls = rb.getString("AllowUrl").split(",");
        } catch (MissingResourceException var1) {
            var1.printStackTrace();
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = null;
            HttpServletResponse res = null;
            if (request instanceof HttpServletRequest) {
                req = (HttpServletRequest) request;
            }

            if (response instanceof HttpServletResponse) {
                res = (HttpServletResponse) response;
            }

            req.getSession(false);
            String path = req.getServletPath();
//            logger.debug("REQUEST_PATH: " + path);
            if (isAlowUrl(path, allowedUrls)) {
                logger.debug("URL cho phep ko can dang nhap");
                filter.doFilter(request, response);
            }else//bo filter vong lap
            if (isValidPath(path)) {
                if (PAGE._LOGIN.equals(path)) {
                    if (isAuthenticate(req)) {
                        res.sendRedirect(req.getContextPath() + PAGE._DEFAULT_URL);
                    } else {
                        filter.doFilter(request, response);
                    }
                } else {
                    if (isAuthenticate(req)) {
                        filter.doFilter(request, response);
                    } else {
                        res.sendRedirect(req.getContextPath() + PAGE._LOGIN);
                    }
                }
            } else {
                res.sendRedirect(req.getContextPath() + PAGE._ERROR_PAGE);
            }


        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    private boolean isValidPath(String path) {
        return true;
    }

    @Override
    public void destroy() {

    }

    public boolean isAuthenticate(HttpServletRequest httpReq) {
        if (httpReq.getSession().getAttribute("user") != null) {
            return true;
        }
        return false;
    }

    public static  boolean isAlowUrl(String path, String[] allowedUrls) {
        int length = allowedUrls.length;

        for (int i = 0; i < length; ++i) {
            String allowedUrl = allowedUrls[i];
            if (path.equalsIgnoreCase(allowedUrl.trim())) {
                return true;
            }
        }

        return false;
    }
}
