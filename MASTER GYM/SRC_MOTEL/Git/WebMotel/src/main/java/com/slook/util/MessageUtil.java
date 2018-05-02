package com.slook.util;

import com.slook.resource.AppMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Locale;
import java.util.ResourceBundle;

@SuppressWarnings("serial")
public class MessageUtil {

	protected static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);
    public static ResourceBundle getResourceBundle() {
        return getResourceBundle(null);
    }
	public static ResourceBundle getResourceBundle(Locale local) {
		FacesContext context = FacesContext.getCurrentInstance();
//	    Locale local = LanguageBean.getLocales().get(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
        ResourceBundle bundle;
        if (context==null){
            bundle = ResourceBundle.getBundle("com.viettel.resource.messages",local==null ? LanguageBean.getLocales().get("vi"):local, new AppMessages.UTF8Control());
        }else{
			bundle = context.getApplication()
					.getResourceBundle(context, "msg");
		}
		return bundle;
	}
        
//	public static synchronized void setResourceBundle() {
//		FacesContext context = FacesContext.getCurrentInstance();
//		bundle = context.getApplication()
//					.getResourceBundle(context, "msg");
//	}

	public static void setErrorMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Error", message);

		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}

	public static void setInfoMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
				message);

		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}

	public static void setWarnMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn",
				message);

		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}

	public static void setFatalMessage(String message) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,
				"Fatal", message);

		FacesContext.getCurrentInstance().addMessage("mainMessage", msg);
	}

	public static ResourceBundle getResourceBundleByVar(String name) {
		FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        if(context==null) {
            Locale local = LanguageBean.getLocales().get(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
            bundle = ResourceBundle.getBundle("com.viettel.resource.messages", local == null ? new Locale("vi", "VN") : local, new AppMessages.UTF8Control());
        }else
            bundle = context.getApplication().getResourceBundle(context, name);

		return bundle;
	}
	public static String getResourceBundleMessage(String key) {
        return getResourceBundleMessage(null,key);
    }
	public static String getResourceBundleMessage(Locale local, String key) {
		if(key==null)
			return key;
		if ("".equals(key)) {
			return "";
		}
		try {
            ResourceBundle bundle = getResourceBundle(local);
			return bundle.getString(key);
		} catch (Exception e) {
		    logger.debug(e.getMessage(),e);
			System.err.println(e.getMessage());
		}
		return key;
	}
	public static void setInfoMessageFromRes(String key) {
		setInfoMessage(getResourceBundleMessage(key));
	}
	public static void setErrorMessageFromRes(String key) {
		setErrorMessage(getResourceBundleMessage(key));
	}
	public static void setWarnMessageFromRes(String key) {
		setWarnMessage(getResourceBundleMessage(key));
	}
        
    public static String getResourceBundleConfig(String key) {
        if (key == null) {
            return key;
        }
        if ("".equals(key)) {
            return "";
        }
        try {
            ResourceBundle configBundle = ResourceBundle.getBundle("config");
            return configBundle.getString(key);
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
            System.err.println(e.getMessage());
        }
        return key;
    }

	public static void setInfoMessageFromRes(String string, Object... params) {
		String msg = getResourceBundleMessage(string);
		try {
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					msg = msg.replace("{"+i+"}", params[i].toString());
				}
			}
		} catch (Exception e) {
            logger.debug(e.getMessage(),e);
			 System.err.println(e.getMessage());
		}
		setInfoMessage(msg);
	}
	public static void setErrorMessageFromRes(String string, Object... params) {
		String msg = getResourceBundleMessage(string);
		try {
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					msg = msg.replace("{"+i+"}", params[i].toString());
				}
			}
		} catch (Exception e) {
            logger.debug(e.getMessage(),e);
			 System.err.println(e.getMessage());
		}
		setErrorMessage(msg);
	}
	public static void setWarnMessageFromRes(String string, Object... params) {
		String msg = getResourceBundleMessage(string);
		try {
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					msg = msg.replace("{"+i+"}", params[i].toString());
				}
			}
		} catch (Exception e) {
            logger.debug(e.getMessage(),e);
			 System.err.println(e.getMessage());
		}
		setWarnMessage(msg);
	}
	public static String getResourceBundleMessage(String key,Object... params) {
		String msg = getResourceBundleMessage(key);
		try {
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					msg = msg.replace("{"+i+"}", params[i].toString());
				}
			}
		} catch (Exception e) {
            logger.debug(e.getMessage(),e);
			 System.err.println(e.getMessage());
		}
		return msg;
	}
	public static String getKey(String key) {
		return getResourceBundleMessage(key);
	}
}
