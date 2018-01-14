package com.slook.util;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;


/**
 * @author huynx6
 * @version 1.2
 * @since 09/2016
 * @lastupdate 22/03/2017
 */
@SuppressWarnings("serial")
@ManagedBean(name="language")
@SessionScoped
public class LanguageBean implements Serializable {
	
	 static Map<String, Locale> locales ;
	
	static {
		locales = new HashMap<>();
		Locale vn = new Locale("vi", "VN");
		locales.put(vn.getLanguage(), vn);
		Locale us = new Locale("en", "US");
		locales.put(us.getLanguage(), us);
	}

	private String localeCode = FacesContext.getCurrentInstance().getApplication().getDefaultLocale().getLanguage();

    public List<String> getCountries(){
		List<String> countries = new ArrayList<>();
		for (Iterator<String> iterator = locales.keySet().iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			countries.add(string);
		}
		return countries;
	}
	public List<Locale> getCountrie2s(){
		List<Locale> countries = new ArrayList<>();
		for (Iterator<String> iterator = locales.keySet().iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			countries.add((Locale) locales.get(string));
		}
		return countries;
	}
	
	public void countryLocaleCodeChanged (ValueChangeEvent e){
		
		String newLocaleCode = e.getNewValue().toString();
		for (Iterator<String> iterator = locales.keySet().iterator(); iterator.hasNext();) {
			String localeCode = (String) iterator.next();
			if(localeCode.equals(newLocaleCode)){
				FacesContext.getCurrentInstance().getViewRoot().setLocale(locales.get(localeCode));
			}
		}
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	public String getLocaleName(String localeCode){
		switch (localeCode) {
			case "vi":
				return "Tiếng Việt";
			case "en":
				return "English";
			default :
				break;
		}
		return "";
	}

    public static Map<String, Locale> getLocales() {
        return locales;
    }

    public static void setLocales(Map<String, Locale> locales) {
        LanguageBean.locales = locales;
    }
}
