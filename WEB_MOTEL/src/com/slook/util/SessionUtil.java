/*
 * Created on Jun 7, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.util;

//import com.viettel.vsa.token.RoleToken;
//import com.viettel.vsa.token.UserToken;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

/**
 * Dinh nghia cac ham thao tac voi session cua ca he thong.
 *
 * @author Nguyen Hai Ha (hanh45@viettel.com.vn)
 * @version 1.0.0
 * @since Jun 7, 2013
 */
@ManagedBean
@RequestScoped
public class SessionUtil extends SessionWrapper implements Constant {

    private static final long serialVersionUID = -7313741895804416337L;

    /**
     * Lay gia tri menu default.
     */
    static SessionUtil sessionUtil;
    public static SessionUtil getInstance(){
        if(sessionUtil==null) sessionUtil=new SessionUtil();
        return sessionUtil;
    }
    public static String getMenuDefault() {
        /*
//		if (! Config._DEFAULT_URL.equals(""))
//			return Config._DEFAULT_URL;
//
//		if (getUrlByKey("/manager")){
//			return "/manager/example";
//		}
        HttpSession session = getCurrentSession();
        UserToken userToken = (UserToken) session.getAttribute("vsaUserToken");
        if (userToken != null && userToken.getRolesList() != null) {
            for (RoleToken roleToken : userToken.getRolesList()) {
                VSA_ROLE roleCode = VSA_ROLE.valueOf(roleToken.getRoleCode().toUpperCase());
//                switch (roleCode) {
//                    case AOM_ADMIN:
//                        return PAGE.CONFIG;
//                    case AOM_MONITOR:
//                        return PAGE.MONITOR;
//                    default:
//                        return PAGE.MONITOR;
//                }
            }
//            return PAGE._DEFAULT_URL;
            return PAGE._LOGIN;
        } else {
            return PAGE._LOGIN;
//            return PAGE._DEFAULT_URL;
//            return PAGE._ACCESS_DENIED;
        }
        // 	Nguoi dung khong co url nao trong he thong
        // Tra ve trang bao loi.
//        return _ERROR_PAGE;
         */
        return PAGE._LOGIN;

    }

    public boolean isCreateAlarmConfig() {
        return true;
    }

    public boolean getConfigByKeyEQ(String key, String val) {
        if (DataConfig.getConfigByKey(key) != null && DataConfig.getConfigByKey(key).equals(val)) {
            return true;
        }
        return false;
    }

}// End class
