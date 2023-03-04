/*
 * Created on Jun 7, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Nguy?n Xu�n Huy <huynx6@viettel.com.vn>
 * @version 1.0
 * @since Jul 29, 2016
 */
public class HibernateUtil
{
    private static final Logger logger = getLogger(HibernateUtil.class);

    private static Map<String, SessionFactory> sessionFactorys = new HashMap<String, SessionFactory>();

    private static SessionFactory buildSessionFactory(String resource)
    {
        try
        {
//            if (sessionFactorys.get(resource) == null) {
//                sessionFactorys.put(resource, new Configuration().configure(resource).buildSessionFactory());
//            }
//            return sessionFactorys.get(resource);
            if (sessionFactorys.get(resource) == null)
            {
                Configuration config = new Configuration().configure(resource);
                String _username = config.getProperty("hibernate.connection.username");
                String _password = config.getProperty("hibernate.connection.password");
                String _url = config.getProperty("hibernate.connection.url");
                try
                {
                    _username = PasswordEncoder.decrypt(_username);
                }
                catch (Exception e)
                {
                }
                try
                {
                    _password = PasswordEncoder.decrypt(_password);
                }
                catch (Exception e)
                {
                }
                try
                {
                    _url = PasswordEncoder.decrypt(_url);
                }
                catch (Exception e)
                {
                }
                config.setProperty("hibernate.connection.username", (_username));
                config.setProperty("hibernate.connection.password", (_password));
                config.setProperty("hibernate.connection.url", (_url));
//                return config.buildSessionFactory();
                sessionFactorys.put(resource, config.buildSessionFactory());
            }
            return sessionFactorys.get(resource);

        }
        catch (Throwable ex)
        {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return buildSessionFactory("/hibernate.cfg.xml");
    }

    /**
     * @param resource: "/hibernate.cfg.xml";
     * @return
     * @author huynx6
     */
    public static SessionFactory getSessionFactory(String resource)
    {
        if (resource == null)
        {
            return getSessionFactory();
        }
        return buildSessionFactory(resource);
    }

    public static void shutdown()
    {
        getSessionFactory().close();
    }

    public static Session openSession()
    {
        return getSessionFactory().openSession();
    }

    public static Session getCurrentSession()
    {
        return getSessionFactory().getCurrentSession();
    }

    public static Session getSessionAndBeginTransaction()
    {
        Session session = null;
        try
        {
            session = getSessionFactory().getCurrentSession();
            session.beginTransaction();
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        return session;
    }

    public static void close(Session session)
    {
        try
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
    }


    public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException
    {

        System.out.println(PasswordEncoder.decrypt("tsU/wPufDdr4q7DnpvnHElIVEVF2fCHhHkXwB39PXiCGEs/Dp+lUCGBYvIradkAi"));
        System.out.println(PasswordEncoder.decrypt("zM4TyqwEUo42WDs1gbXi+g=="));
        System.out.println(PasswordEncoder.decrypt("oNH+qlWBSGCL9wQLqcRkpw=="));

        System.out.println(PasswordEncoder.encrypt("GYM_SERVICE"));

        System.out.println(PasswordEncoder.encrypt("jdbc:oracle:thin:@14.225.3.186:1521:devdb"));
//        HibernateUtil.getSessionFactory().openSession().close();
    }
}
