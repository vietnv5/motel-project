package com.slook.filter;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.util.PrettyURLBuilder;
import com.slook.model.CatUser;
import com.slook.model.FunctionPath;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
//import com.viettel.vsa.token.ObjectToken;
//import com.viettel.vsa.token.UserToken;
import org.apache.log4j.Logger;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.apache.log4j.Logger.getLogger;

/**
 * Filter chung cua ca he thong doi voi duong dan tuyet doi. Tat ca cac module
 * deu phai khai bao phan quyen o day.
 *
 * @author hanh45
 */
@WebFilter(dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE,
        DispatcherType.ERROR},
        urlPatterns = {
                "/faces/*"
//                "/faces"
        })
public class JSF2Filter implements Filter
{

    private static final Logger logger = getLogger(JSF2Filter.class);

    private String _REQUEST_PATH = "";
    private static final String _HOME_PATH = "/home";
    private static final String _ERROR_PATH = "/error";
    private static final String _XHTML = ".xhtml";
    private static final String _FACES = "/faces/";
    private static final String _USER_TOKEN = Constant._USER_TOKEN;

    public JSF2Filter()
    {
    }

    public void init(FilterConfig fConfig) throws ServletException
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        HttpServletResponse res = (HttpServletResponse) response;
        boolean checkAuth = false;

        // Skip JSF resources (CSS/JS/Images/etc)
        if (!req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER))
        {
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            res.setDateHeader("Expires", 0); // Proxies.
        }

        PrettyContext context = PrettyContext.getCurrentInstance(req);

        List<UrlMapping> lstMapping = context.getConfig().getMappings();
        Map<String, String> mapPath = (Map<String, String>) CommonUtil.convertListToMap((List) lstMapping, "viewId", "pattern");

        _REQUEST_PATH = req.getServletPath();
        int pos = _REQUEST_PATH.indexOf("/", _FACES.length());
        String SUB_REQUEST_PATH = _REQUEST_PATH.substring(0, pos);
        _REQUEST_PATH = _REQUEST_PATH.substring(0, _REQUEST_PATH.indexOf(_XHTML) + _XHTML.length());

        String pattern = mapPath.get(_REQUEST_PATH);//lay pattern
        // Kiem tra session timeout.
        CatUser userToken = (CatUser) session.getAttribute(_USER_TOKEN);

        if (AuthenticationFilter.isAlowUrl(SUB_REQUEST_PATH.substring(_FACES.length() - 1), AuthenticationFilter.allowedUrls)
                || (AuthenticationFilter.isAlowUrl(pattern, AuthenticationFilter.allowedUrls)))
        {

            logger.debug("URL cho phep ko can dang nhap");
            checkAuth = true;
        }
        else if (userToken != null)
        {
            // Current session on.
            switch (SUB_REQUEST_PATH)
            {
                case _FACES + "home":
                case _FACES + "login":
                    checkAuth = true;
                    break;
                default:
                    checkAuth = getPermission(session, pattern);
                    break;
            }

        }
        else
        {
            // Session timeout.
            // Xu ly cho ajax khi session timeout.
            session.setAttribute(_USER_TOKEN, null);
            handleSessionTimeout(req, res);
        }
        if (checkAuth)
        {
            chain.doFilter(request, response);
        }
        else
        {
//		 Dieu huong den trang bao loi.
            if (!res.isCommitted())
            {
//                res.sendRedirect(req.getContextPath() + _ERROR_PATH);
                res.sendRedirect(req.getContextPath() + Constant.PAGE._ACCESS_DENIED);
            }
        }
    }

    /**
     * Xu ly session timeout.
     *
     * @throws IOException
     */
    private void handleSessionTimeout(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        if ("partial/ajax".equals(req.getHeader("Faces-Request")))
        {
            res.setContentType("text/xml");
            res.getWriter().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").printf(
                    "<partial-response><redirect url=\"%s\"></redirect></partial-response>",
                    req.getContextPath() + _HOME_PATH);
        }
        else
        {
            if (!res.isCommitted())
            {
//                res.sendRedirect(req.getContextPath() + _HOME_PATH);
                res.sendRedirect(req.getContextPath() + Constant.PAGE._LOGIN);
            }
        }
    }

    /**
     * Ham kiem tra quyen cua user tren session
     *
     * @param urlCode
     * @return
     */
    private boolean getUrlPermission(HttpSession session, String urlCode)
    {
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

    private boolean getPermission(HttpSession session, String requestPath)
    {
        boolean checkAuth = false;
        if (DataConfig.getConfigByKey("IS_TEST") != null && DataConfig.getConfigByKey("IS_TEST").equals("1"))
        {
            return true;
        }
        try
        {
//            if (Pattern.compile("^" + _FACES + ".+?" + "/index.xhtml$").matcher(requestPath).find())
//                checkAuth = getUrlPermission(session, requestPath.substring(_FACES.length() - 1, requestPath.indexOf("/index.xhtml")));
            //khai duong dan toi file chuc nang
            checkAuth = getUrlPermission(session, requestPath);

        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        return checkAuth;
    }
}
