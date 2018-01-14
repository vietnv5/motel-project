/*
 * Created on Aug 15, 2013
 *
 * Copyright (C) 2013 by Viettel Network Company. All rights reserved
 */
package com.slook.persistence;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.util.HibernateUtil;
import org.apache.commons.lang3.ClassUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

/**
 * Class implement interface Generic Dao Service
 *
 * @author Nguyen Hai Ha (hanh45@viettel.com.vn)
 * @version 1.0.0
 * @since Aug 15, 2013
 */
public abstract class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {
    private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);
    protected String pkName;

    @SuppressWarnings("unchecked")
    protected Class<T> domainClass = (Class<T>) getTypeArguments(GenericDaoImpl.class, this.getClass()).get(0);

    /**
     * Method to return the class of the domain object
     */

	/*
     * (non-Javadoc)
	 *
	 * @see com.viettel.persistence.util.GenericDaoService#findById(java.io.
	 * Serializable)
	 */
    @Override
    public T findById(PK id) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        T object = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            switch (id.getClass().getName()) {
                case "java.lang.String":
                    String identifierName = session.getSessionFactory().getClassMetadata(domainClass)
                            .getIdentifierPropertyName();
                    Criteria criteria = session.createCriteria(domainClass);
                    criteria.add(Restrictions.ilike(identifierName, id.toString().toLowerCase(), MatchMode.EXACT));
                    object = (T) criteria.uniqueResult();
                    break;
                default:
                    object = (T) session.get(domainClass, id);
                    break;
            }

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return (T) object;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#delete(java.lang.Object)
     */
    @Override
    public void delete(T object) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(object);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#delete(java.util.List)
     */
    @Override
    public void delete(List<T> objects) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            for (T object : objects)
                session.delete(object);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#saveOrUpdate(java.lang.
     * Object)
     */
    @Override
    public void saveOrUpdate(T object) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(object);
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#save(java.lang.Object)
     */
    @Override
    public PK save(T object) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        PK o = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            o = (PK) session.save(object);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return o;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#saveOrUpdate(java.util.
     * List)
     */
    @Override
    public void saveOrUpdate(List<T> objects) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            for (T object : objects)
                session.saveOrUpdate(object);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.viettel.persistence.util.GenericDaoService#findList()
     */
    @Override
    public List<T> findList() throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        List<T> objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            objects = criteria.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return objects;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#findList(java.util.Map,
     * java.util.Map)
     */
    @Override
    public List<T> findList(Map<String, Object> filters, Map<String, String> orders) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        List<T> objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            // Xu ly filter.
            setCriteriaRestrictions(criteria, filters);

            // Xu ly order
            setCriteriaOrders(criteria, orders);

            objects = criteria.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return objects;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.viettel.persistence.util.GenericDaoService#findList(int, int,
     * java.util.Map)
     */
    @Override
    public List<T> findList(int first, int pageSize, Map<String, Object> filters) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        List<T> objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            // Xu ly filter.
            setCriteriaRestrictions(criteria, filters);

            // Xu ly paging
            criteria.setFirstResult(first);
            criteria.setMaxResults(pageSize);

            objects = criteria.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return objects;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.viettel.persistence.util.GenericDaoService#findList(int, int,
     * java.util.Map, java.util.Map)
     */
    @Override
    public List<T> findList(int first, int pageSize, Map<String, Object> filters, Map<String, String> orders)
            throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        List<T> objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            // Xu ly filter.
            setCriteriaRestrictions(criteria, filters);

            // Xu ly order
            setCriteriaOrders(criteria, orders);

            // Xu ly paging
            criteria.setFirstResult(first);
            criteria.setMaxResults(pageSize);

            objects = criteria.list();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return objects;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.viettel.persistence.util.GenericDaoService#count(java.util.Map)
     */
    @Override
    public int count(Map<String, Object> filters) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        int count = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);

            // Xu ly filter.
            setCriteriaRestrictions(criteria, filters);

            criteria.setProjection(Projections.rowCount());
            count = criteria.uniqueResult() == null ? 0 : ((Long) criteria.uniqueResult()).intValue();
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return count;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.viettel.persistence.util.GenericDaoService#count()
     */
    @Override
    public int count() throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        int count = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setProjection(Projections.rowCount());
            count = criteria.uniqueResult() == null ? 0 : ((Long) criteria.uniqueResult()).intValue();
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return count;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#persist(java.lang.Object)
     */
    @Override
    public void persist(T object) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.persist(object);
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.viettel.persistence.util.GenericDaoService#merge(java.lang.Object)
     */
    @Override
    public void merge(T object) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.clear();
            session.merge(object);
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public T get(PK id) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        T object = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            object = (T) session.get(domainClass, id);
            session.flush();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new AppException();
        } catch (Exception ex) {
            if (tx != null)
                tx.rollback();
            logger.error(ex.getMessage(), ex);

            throw new SysException();
        } finally {
            if (session != null)
                session.close();
        }
        return object;
    }

    /**
     * Th�m filter cho criteria.
     *
     * @param criteria
     * @return criteria
     */
    protected Criteria setCriteriaRestrictions(Criteria criteria, Map<String, Object> filters) {
        if (filters == null)
            return criteria;

        Map<String, String> properties = getFields();

        String type;
        String filedName;
        String fieldValue;
        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext(); ) {
            filedName = it.next();
            fieldValue = (String) filters.get(filedName);

            type = properties.get(filedName);
            switch (type) {
                case "java.lang.String":
                    criteria.add(Restrictions.ilike(filedName, fieldValue.toLowerCase(), MatchMode.ANYWHERE));
                    break;
                case "java.lang.Integer":
                    criteria.add(Restrictions.eq(filedName, Integer.valueOf(fieldValue)));
                    break;
                case "java.lang.Long":
                    criteria.add(Restrictions.eq(filedName, Long.valueOf(fieldValue)));
                    break;
                default:
                    break;
            }
        }

        return criteria;
    }

    /**
     * Th�m order cho criteria.
     *
     * @param criteria
     * @return criteria
     */
    protected Criteria setCriteriaOrders(Criteria criteria, Map<String, String> orders) {
        if (orders == null)
            return criteria;

        String propertyName;
        String orderType;

        for (Iterator<String> it = orders.keySet().iterator(); it.hasNext(); ) {
            propertyName = it.next();
            orderType = orders.get(propertyName);
            if (propertyName.contains(".")) {
                if (propertyName.startsWith(pkName + ".")) {

                } else {
                    String[] prs = propertyName.split("\\.", -1);
                    for (int i = 0; i < prs.length - 1; i++) {
                        String alias = "";
                        String aliasNew = "";
                        for (int j = 0; j <= i; j++) {
                            alias += prs[j] + ".";
                            aliasNew += prs[j] + (j == i - 2 ? "." : "");
                        }
                        alias = alias.replaceAll("\\.$", "");
                        Iterator<CriteriaImpl.Subcriteria> iter = ((CriteriaImpl) criteria).iterateSubcriteria();

                        if (iter.hasNext()) {
                            int c = 0;
                            while (iter.hasNext()) {
                                Criteria subcriteria = (Criteria) iter.next();
                                if (subcriteria.getAlias() != null && subcriteria.getAlias().contains(aliasNew)) {
                                    c++;
                                    break;
                                }
                            }
                            if (c == 0) {
                                criteria.createAlias(alias, aliasNew);

                            }
                        } else {
                            criteria.createAlias(alias, aliasNew);

                        }
                        if (i < prs.length - 2)
                            propertyName = propertyName.replaceFirst("\\.", "");
                    }

                    //fieldName = fieldName.replaceAll("^" + alias, alias + "-alias");
//					String alias = fieldName.split("\\.", -1)[0];
//					if (!criteria.getAlias().contains(alias))
//						criteria.createAlias(alias, alias + "-alias");
//					fieldName = fieldName.replaceAll("^" + alias, alias + "-alias");
                }
            }

            switch (orderType.toUpperCase()) {
                case "ASC":
                    criteria.addOrder(Order.asc(propertyName));
                    break;
                case "DESC":
                    criteria.addOrder(Order.desc(propertyName));
                    break;
                default:
                    criteria.addOrder(Order.asc(propertyName));
                    break;
            }
        }

        return criteria;
    }

    /**
     * Get all field and type of object.
     *
     * @return
     * @throws ClassNotFoundException
     */
    private Map<String, String> getFields() {
        PropertyDescriptor[] propertyDescriptors;
        Map<String, String> result = new HashMap<String, String>();
        try {
            propertyDescriptors = Introspector.getBeanInfo(domainClass).getPropertyDescriptors();
            String fieldName;
            String fieldType;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                fieldName = propertyDescriptor.getName();
                fieldType = propertyDescriptor.getPropertyType().getCanonicalName();

                if (ClassUtils.isPrimitiveOrWrapper(Class.forName(fieldType))) {
                    result.put(fieldName, fieldType);
                } else if ("java.lang.String".equalsIgnoreCase(fieldType)) {
                    result.put(fieldName, fieldType);
                } else if ("java.util.Date".equalsIgnoreCase(fieldType)) {
                    result.put(fieldName, fieldType);
                } else {
                    if (!"java.lang.Class".equalsIgnoreCase(fieldType)) {
                        result = getSubField(fieldName, fieldType, result);
                    }
                }
            }
        } catch (IntrospectionException | ClassNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    private Map<String, String> getSubField(String fieldName, String fieldType, Map<String, String> result) {
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(Class.forName(fieldType))
                    .getPropertyDescriptors();
            String subFieldName;
            String subFieldType;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                subFieldName = fieldName.concat("." + propertyDescriptor.getName());
                subFieldType = propertyDescriptor.getPropertyType().getCanonicalName();

                if (ClassUtils.isPrimitiveOrWrapper(Class.forName(subFieldType))) {
                    result.put(subFieldName, subFieldType);
                } else if ("java.lang.String".equalsIgnoreCase(subFieldType)) {
                    result.put(subFieldName, subFieldType);
                } else if ("java.util.Date".equalsIgnoreCase(subFieldType)) {
                    result.put(subFieldName, subFieldType);
                } else {
                    if (!"java.lang.Class".equalsIgnoreCase(subFieldType)) {
                        result = getSubField(subFieldName, subFieldType, result);
                    }
                }
            }
        } catch (IntrospectionException | ClassNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * Get the actual type arguments a child class has used to extend a generic
     * base class. (Taken from
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860. Thanks
     * mathieu.grenonville for finding this solution!)
     *
     * @param baseClass  the base class
     * @param childClass the child class
     * @return a list of the raw classes for the actual type arguments.
     */
    public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
        Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
        Type type = childClass;
        // start walking up the inheritance hierarchy until we hit baseClass
        while (!getClass(type).equals(baseClass)) {
            if (type instanceof Class) {
                // there is no useful information for us in raw types, so just
                // keep going.
                type = ((Class) type).getGenericSuperclass();
            } else {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class) parameterizedType.getRawType();

                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
                }

                if (!rawType.equals(baseClass)) {
                    type = rawType.getGenericSuperclass();
                }
            }
        }

        // finally, for each actual type argument provided to baseClass,
        // determine (if possible)
        // the raw class for that type argument.
        Type[] actualTypeArguments;
        if (type instanceof Class) {
            actualTypeArguments = ((Class) type).getTypeParameters();
        } else {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        }
        List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
        // resolve types by chasing down type variables.
        for (Type baseType : actualTypeArguments) {
            while (resolvedTypes.containsKey(baseType)) {
                baseType = resolvedTypes.get(baseType);
            }
            typeArgumentsAsClasses.add(getClass(baseType));
        }
        return typeArgumentsAsClasses;
    }

    /**
     * Get the underlying class for a type, or null if the type is a variable
     * type.
     *
     * @param type the type
     * @return the underlying class
     */
    private static Class<?> getClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
