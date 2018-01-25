package com.slook.persistence;

import com.slook.persistence.common.ConditionQuery;
import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.persistence.common.ObjectWithCondition;
import com.slook.persistence.common.OrderBy;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.HibernateUtil;
import org.apache.commons.lang3.ClassUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.ocpsoft.common.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static org.apache.log4j.Logger.getLogger;

/**
 * @param <T>
 * @param <PK>
 * @author Nguyễn Xuân Huy <huynx6@viettel.com.vn>
 * @version 1.0
 * @sin Jul 29, 2016
 */
public abstract class GenericDaoImplNewV2<T, PK extends Serializable>
        extends GenericDaoImpl<T, PK>
        implements GenericDaoServiceNewV2<T, PK> {

    /**
     *
     */
    private static final org.apache.log4j.Logger logger = getLogger(GenericDaoImplNewV2.class);
    private static final long serialVersionUID = 132L;
    public static final String COMPILE_OR = "\\|\\|";
    public static final String DELIMITER_OR = "||";
    public static final String EXAC = "EXAC";
    public static final String EXAC_IGNORE_CASE = "EXAC_IGNORE_CASE";
    public static final String GE = "GE";
    public static final String GT = "GT";
    public static final String LE = "LE";
    public static final String LT = "LT";
    public static final String NEQ = "NEQ";
    public static final String BW = "BW";
    public static final String PREFIX = "-";
    protected static final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImplNewV2.class);
    public LinkedHashMap<String, String> currOrders;
    public Map<String, Object> currFilters;
    public static final String LIKE = "LIKE";

    @Override
    public Map<String, Object> getCurrFilters() {
        return currFilters;
    }

    @Override
    public void setCurrFilters(Map<String, Object> currFilters) {
        this.currFilters = currFilters;
    }

    @Override
    public LinkedHashMap<String, String> getCurrOrders() {
        return currOrders;
    }

    @Override
    public void setCurrOrders(LinkedHashMap<String, String> currOrders) {
        this.currOrders = currOrders;
    }

    @Override
    public List<T> findListExport() {
        List<T> objects = new ArrayList<T>();
        try {
            objects = findList(-1, -1, getCurrFilters(), getCurrOrders());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return objects;
    }

    public GenericDaoImplNewV2(boolean... b) {
        if (b.length == 0 || b.length == 1 && b[0]) {
            init();
        }

    }

    @SuppressWarnings("unchecked")
    private void init() {
        // TODO Auto-generated method stub
        try {
            java.lang.reflect.Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass != null) {
                this.domainClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(domainClass);
                this.pkName = classMetadata.getIdentifierPropertyName();

                Field[] fields = this.domainClass.getDeclaredFields();
                for (Field f : fields) {
                    if (f.isAnnotationPresent(Id.class)) {
                        this.pkName = f.getName();
                    }
                }
                Assert.notNull(pkName, "[Assertion failed] - Argument pkName is required; it must not be null");
                Assert.notNull(domainClass, "[Assertion failed] - Argument domainClass is required; it must not be null");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
//	/**
//	 * @param isInit
//	 */
//	public GenericDaoImplNewV2(boolean isInit) {
//		if(isInit)
//			init();
//		
//	}

    public Session getCurrentSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    public void closeCurrentSession() {
        HibernateUtil.getSessionFactory().getCurrentSession().close();
    }

    @Override
    public int count() throws AppException, SysException {
        // TODO Auto-generated method stub
        Session localSession = null;
        Transaction localTransaction = null;
        int i = 0;
        try {
            localSession = HibernateUtil.getSessionFactory().openSession();
            localTransaction = localSession.beginTransaction();
            Criteria localCriteria = localSession.createCriteria(this.domainClass);
            localCriteria.setProjection(Projections.rowCount());
            Object result = localCriteria.uniqueResult();
            if (result instanceof Number) {
                i = ((Number) result).intValue();
            }
            localTransaction.commit();
        } catch (HibernateException ex) {
            if (localTransaction != null) {
                localTransaction.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (localTransaction != null) {
                localTransaction.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (localSession != null) {
                localSession.close();
            }
        }
        return i;
    }

    @Override
    public int countOld(Map<String, String> filters) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        int count = 0;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            setCriteriaRestrictionsOld(criteria, filters);
            criteria.setProjection(Projections.rowCount());
            Object res = criteria.uniqueResult();
            if (res instanceof Number) {
                return ((Number) res).intValue();
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return count;
    }

    @Override
    public int count2(Map<String, Object> filters) throws Exception {
        // TODO Auto-generated method stub
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            setSessionRestrictions(session, filters);
            setCriteriaRestrictions2(criteria, filters);

            int count = 0;
            if (isFilter) {
                count = criteria.list().size();
            } else {
                criteria.setProjection(Projections.rowCount());
                try {
                    count = ((Number) criteria.uniqueResult()).intValue();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            tx.commit();
            return count;
        } catch (Exception ex) {
//            if (tx != null) {
//                tx.rollback();
//            }
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(PK id) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        Object object = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            switch (id.getClass().getName()) {
                case "java.lang.String":
                    // String identifierName =
                    // session.getSessionFactory().getClassMetadata(this.domainClass).getIdentifierPropertyName();
                    Type identifierType = session.getSessionFactory().getClassMetadata(this.domainClass).getIdentifierType();
                    if (identifierType instanceof LongType) {
                        object = session.get(this.domainClass, Long.valueOf(id.toString()));
                    } else if (identifierType instanceof IntegerType) {
                        object = session.get(this.domainClass, Integer.valueOf(id.toString()));
                    } else if (identifierType instanceof StringType) {
                        object = session.get(this.domainClass, String.valueOf(id.toString()));
                    }
                    break;

                default:
                    object = session.get(this.domainClass, id);
                    break;
            }

            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findById(PK id, Map<String, Object> filter) throws AppException {
        // TODO Auto-generated method stub
        Session session = null;
        Transaction tx = null;
        Object object = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            setSessionRestrictions(session, filter);
            object = session.get(this.domainClass, id);
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findList(ConditionQuery query, OrderBy orderBy, final int pageNumber, final int pageSize) {
        Session session = null;
        List<T> objects = new ArrayList<T>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(this.domainClass);
            query.build(criteria);
            if (orderBy != null) {
                orderBy.build(criteria);
            }
            //vietnv bo su disnct cho customerScheduleController -> searchGrouppack
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            if (pageNumber > -1 && pageSize > -1) {
                int start = (pageNumber - 1) * pageSize;
                if (start != 0) {
                    criteria.setFirstResult(start);
                }
                criteria.setMaxResults(pageSize);
            }
            objects = criteria.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public long count(ConditionQuery query) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(this.domainClass);
            query.build(criteria);
            criteria.setProjection(Projections.rowCount());
            Object object = criteria.uniqueResult();
            if (object instanceof Long) {
                Long count = (Long) object;
                return count;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }

    @Override
    public List<T> findList2(int first, int pageSize, Map<String, ObjectWithCondition> filters, LinkedHashMap<String, String> orders) throws Exception {
        Map<String, Object> filter2s = new HashMap<String, Object>();
        if (filters != null) {
            for (Iterator<String> iterator = filters.keySet().iterator(); iterator.hasNext();) {
                String column = (String) iterator.next();
                ObjectWithCondition objectWithCondition = filters.get(column);
                if (objectWithCondition.getCondition() != null) {
                    filter2s.put(column + objectWithCondition.getCondition().normalizeValue(), objectWithCondition.getValue());
                }
            }
        }
        return findList(first, pageSize, filter2s, orders);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findList(int first, int pageSize, Map<String, Object> filters, LinkedHashMap<String, String> orders) throws Exception {
        // TODO Auto-generated method stub
        Session session = null;
        Transaction tx = null;

        List<T> objects = new ArrayList<T>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(domainClass);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            setSessionRestrictions(session, filters);
            setCriteriaRestrictions2(criteria, filters);
            setCriteriaOrders(criteria, orders);
            if (first != -1) {
                criteria.setFirstResult(first);
            }
            if (pageSize != -1) {
                criteria.setMaxResults(pageSize);
            }
            objects = (List<T>) criteria.list();
            //dungvv8
            //set orders,filters for export
            setCurrFilters(filters);
            setCurrOrders(orders);
            //dungvv8
            tx.commit();
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public List<T> findListOld(int first, int pageSize, Map<String, String> filters) throws AppException, SysException {
        // TODO Auto-generated method stub
        return findListOld(first, pageSize, filters, null);
    }

    @Override
    public List<T> findListOld(int first, int pageSize, Map<String, String> filters, Map<String, String> orders) throws AppException, SysException {
        try {
            if (orders == null) {
                return findList(first, pageSize, new HashMap<String, Object>(filters), null);
            }
            return findList(first, pageSize, new HashMap<String, Object>(filters), new LinkedHashMap<String, String>(orders));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return new ArrayList<T>();
    }

    @Override
    public List<T> findList(Map<String, Object> filters) throws Exception {
        return findList(-1, -1, filters, null);
    }

    @Override
    public List<T> findList(Map<String, Object> filters, LinkedHashMap<String, String> orders) throws Exception {
        return findList(-1, -1, filters, orders);
    }

    @Override
    public List<T> findListOld(Map<String, String> filters, Map<String, String> orders) throws AppException, SysException {
        // TODO Auto-generated method stub
        return findListOld(-1, -1, filters, orders);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findListExac(Map<String, Object> filter, Map<String, String> orders) throws Exception {

        Session session = null;
        Transaction tx = null;
        List<T> objects = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Criteria criteria = session.createCriteria(this.domainClass);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            Map<String, Object> _filter = new HashMap<String, Object>();
            for (Iterator<String> iterator = filter.keySet().iterator(); iterator.hasNext();) {
                String key = iterator.next();
                Object value = filter.get(key);
                if (!key.endsWith("-EQ") && !key.endsWith("-EXAC")) {
                    _filter.put(key + "-EXAC", value);
                }
            }
            setCriteriaRestrictions2(criteria, _filter);
            setCriteriaOrders(criteria, orders);

            objects = criteria.list();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objects;

    }

    @Override
    public int execteBulk(final String hql, final Object... paramlist) {
        Session session = getCurrentSession();
        boolean isNeedCloseSession = false;
        if (session == null) {
            session = openSession();
            isNeedCloseSession = true;
        }
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String _hql = fomatHQL(hql);
            Query query = session.createQuery(_hql);
            if (paramlist.length >= 1 && paramlist[0] instanceof Map<?, ?>) {
                setMapParameters(query, (Map<String, Object>) paramlist[0]);
            } else {
                setParameters(query, paramlist);
            }
            Object result = query.executeUpdate();
            tx.commit();
            return result == null ? 0 : ((Integer) result).intValue();
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (isNeedCloseSession && session != null) {
                session.close();
            }
        }
    }

    @Override
    public int execteNativeBulk(final String natvieSQL, final Object... paramlist) {
        Session session = getCurrentSession();
        boolean isNeedCloseSession = false;
        if (session == null) {
            session = openSession();
            isNeedCloseSession = true;
        }
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String _natvieSQL = fomatHQL(natvieSQL);
            Query query = session.createSQLQuery(_natvieSQL);
            setParameters(query, paramlist);
            Object result = query.executeUpdate();
            tx.commit();
            return result == null ? 0 : ((Integer) result).intValue();
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (isNeedCloseSession && session != null) {
                session.close();
            }
        }
    }

    @Override
    public int execteNativeBulk2(final String natvieSQL, Session _session, Transaction _tx, boolean isCommit, Map<String, Object> paramMap, final Object... paramlist) {

        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }

            String _natvieSQL = fomatHQL(natvieSQL);
            Query query = session.createSQLQuery(_natvieSQL);
            setMapParameters(query, paramMap);
            setParameters(query, paramlist);
            Object result = query.executeUpdate();
            if (isCommit) {
                tx.commit();
            }
            return result == null ? 0 : ((Integer) result).intValue();
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                if (isCommit || tx.getStatus() == TransactionStatus.ROLLED_BACK) {
                    session.close();
                }
            }
        }
    }

    @Override
    public int execteNativeBulk3(String natvieSQL, Session _session, Transaction _tx, boolean isCommit, Object... paramlist) {
        return execteNativeBulk2(natvieSQL, _session, _tx, isCommit, null, paramlist);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findList(final String hql, final int pageNumber, final int pageSize, final Object... paramlist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            String _hql = fomatHQL(hql);
            Query query = session.createQuery(_hql);
            setParameters(query, paramlist);
            if (pageNumber > -1 && pageSize > -1) {
                query.setMaxResults(pageSize);
                int start = (pageNumber - 1) * pageSize;
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }
            if (pageNumber < 0) {
                query.setFirstResult(0);
            }
            List<T> results = query.list();
            tx.commit();
            return results;
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findListAll(final String hql, final Object... paramlist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            String _hql = fomatHQL(hql);
            Query query = session.createQuery(_hql);
            setParameters(query, paramlist);
//			if (pageNumber > -1 && pageSize > -1) {
//				query.setMaxResults(pageSize);
//				int start = (pageNumber - 1) * pageSize;
//				if (start != 0) {
//					query.setFirstResult(start);
//				}
//			}
//			if (pageNumber < 0) {
//				query.setFirstResult(0);
//			}
            List<T> results = query.list();
            tx.commit();
            return results;
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<?> findListSQLWithMapParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, Map<String, Object> paramlist, Map<String, Type> addScale, Class<?> classBean) {
        if (paramlist == null) {
            paramlist = new LinkedHashMap<String, Object>();
        }
        if (addScale == null) {
            addScale = new LinkedHashMap<String, Type>();
        }
        return findListSQL(hibernateConfig, sql, pageNumber, pageSize, classBean, paramlist, addScale);
    }

    @Override
    public List<?> findListSQLWithMapParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, final Map<String, Object> paramlist) {
        return findListSQLWithMapParameters(hibernateConfig, sql, pageNumber, pageSize, paramlist, null, null);
    }

    @Override
    public List<?> findListSQLWithMapParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, final Map<String, Object> paramlist, final Map<String, Type> addScale) {
        return findListSQLWithMapParameters(hibernateConfig, sql, pageNumber, pageSize, paramlist, addScale, null);
    }

    @Override
    public List<?> findListSQLWithPosParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, final Map<String, Type> addScale, final Object... paramlist) {
        return findListSQLWithPosParameters(hibernateConfig, sql, pageNumber, pageSize, null, addScale, paramlist);
    }

    @Override
    public List<?> findListSQLWithPosParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, Class<?> classBean, final Map<String, Type> addScale, final Object... paramlist) {
        if (addScale != null) {
            return findListSQL(hibernateConfig, sql, pageNumber, pageSize, classBean, paramlist, addScale);
        } else {
            return findListSQL(hibernateConfig, sql, pageNumber, pageSize, classBean, paramlist);
        }
    }

    @Override
    public List<?> findListSQLWithPosParameters(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, final Object... paramlist) {
        return findListSQLWithPosParameters(hibernateConfig, sql, pageNumber, pageSize, null, null, paramlist);
    }

    @Override
    public List<?> findListSQL(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, final Object... paramlist) {
        return findListSQL(hibernateConfig, sql, pageNumber, pageSize, null, paramlist);
    }

    @Override
    public List<?> findListSQLAll(final String sql, final Object... paramlist) {
        return findListSQL(null, sql, -1, -1, null, paramlist);
    }

    @SuppressWarnings("unchecked")
    private List<?> findListSQL(String hibernateConfig, final String sql, final int pageNumber, final int pageSize, Class<?> classBean, final Object... paramlist) {
        Session session = null;
        boolean isNeedCloseSession = false;
        if (hibernateConfig != null) {
            session = HibernateUtil.getSessionFactory(hibernateConfig).openSession();
        } else {
            session = openSession();
        }
        isNeedCloseSession = true;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String _sql = fomatHQL(sql);
            SQLQuery query = session.createSQLQuery(_sql);
            if (paramlist.length > 1 && paramlist[0] instanceof Map<?, ?>) {
                setMapParameters(query, (Map<String, Object>) paramlist[0]);
                if (paramlist.length > 1 && paramlist[1] instanceof Map<?, ?>) {
                    setScalar(query, (Map<String, Type>) paramlist[1]);
                }
            } else {
                if (paramlist.length > 0 && paramlist[paramlist.length - 1] instanceof Map<?, ?>) {
                    setScalar(query, (Map<String, Type>) paramlist[paramlist.length - 1]);
                }
                setParameters(query, paramlist);
            }
            if (pageNumber > -1 && pageSize > -1) {
                query.setMaxResults(pageSize);
                int start = (pageNumber - 1) * pageSize;
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }
            if (pageNumber < 0) {
                query.setFirstResult(0);
            }
            if (classBean != null) {
                query.setResultTransformer(Transformers.aliasToBean(classBean));
            }
            List<Object> results = query.list();
            tx.commit();
            return results;
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public int count(String hibernateConfig, final String sql, Map<String, Object> paramlist) {
        Session session = null;
        int result = 0;
        boolean isNeedCloseSession = false;
        Transaction tx = null;
        try {
            if (hibernateConfig != null) {
                session = HibernateUtil.getSessionFactory(hibernateConfig).openSession();
            } else {
                session = openSession();
            }
            isNeedCloseSession = true;
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(sql);
            setMapParameters(query, paramlist);
            try {
                Object obj = query.uniqueResult();
                if (obj instanceof Number) {
                    result = ((BigDecimal) obj).intValueExact();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            tx.commit();
            return result;
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * for in
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findListWithIn(final String hql, final int start, final int length, final Map<String, Collection<?>> map, final Object... paramlist) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = session.beginTransaction();
            String _hql = fomatHQL(hql);
            Query query = session.createQuery(_hql);
            setParameters(query, paramlist);
            for (Entry<String, Collection<?>> e : map.entrySet()) {
                query.setParameterList(e.getKey(), e.getValue());
            }
            if (start > -1 && length > -1) {
                query.setMaxResults(length);
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }
            List<T> results = query.list();
            return results;
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private Map<String, String> getFields() {
        Map<String, String> result = new HashMap<String, String>();
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(this.domainClass).getPropertyDescriptors();

            String fieldName;
            String fieldType;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                fieldName = propertyDescriptor.getName();
                if (propertyDescriptor.getPropertyType() == null) {
                    continue;
                }

                fieldType = propertyDescriptor.getPropertyType().getCanonicalName();

                try {
                    if (ClassUtils.isPrimitiveOrWrapper(Class.forName(fieldType))) {
                        result.put(fieldName, fieldType);
                    } else if ("java.lang.String".equalsIgnoreCase(fieldType)) {
                        result.put(fieldName, fieldType);
                    } else if ("java.util.Date".equalsIgnoreCase(fieldType)) {
                        result.put(fieldName, fieldType);
                    } else if (!"java.lang.Class".equalsIgnoreCase(fieldType)) {
                        result = getSubField(fieldName, fieldType, result);
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    private Map<String, String> getSubField(String fieldName, String fieldType, Map<String, String> result) {
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(Class.forName(fieldType)).getPropertyDescriptors();

            String subFieldName;
            String subFieldType;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                subFieldName = fieldName.concat("." + propertyDescriptor.getName());
                if (subFieldName.split("\\.", -1).length == 6) {
                    continue;
                }
                if (propertyDescriptor.getPropertyType() == null) {
                    continue;
                }
                //dungvv8 add
                //java.beans.PropertyDescriptor[name=empty; propertyType=boolean; readMethod=public abstract boolean java.util.List.isEmpty()]
                if (propertyDescriptor.getPropertyType().equals(byte[].class)) {
                    continue;
                }
                if (propertyDescriptor.getPropertyType().equals(boolean.class)) {
                    continue;
                }
                //dungvv8 add
                subFieldType = propertyDescriptor.getPropertyType().getCanonicalName();

                if (ClassUtils.isPrimitiveOrWrapper((propertyDescriptor.getPropertyType()))) {
                    result.put(subFieldName, subFieldType);
                } else if ("java.lang.String".equalsIgnoreCase(subFieldType)) {
                    result.put(subFieldName, subFieldType);
                } else if ("java.util.Date".equalsIgnoreCase(subFieldType)) {
                    result.put(subFieldName, subFieldType);
                } else if (!"java.lang.Class".equalsIgnoreCase(subFieldType)) {
                    if (!subFieldType.equalsIgnoreCase(domainClass.getCanonicalName())) {
                        result = getSubField(subFieldName, subFieldType, result);
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public Criteria setCriteriaRestrictionsOld(Criteria criteria, Map<String, String> filters) {
        // TODO Auto-generated method stub
        try {
            setCriteriaRestrictions2(criteria, filters);
        } catch (ParseException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return criteria;
    }

    protected void setCriteriaRestrictions2(Criteria criteria, Map<String, ?> filters) throws ParseException {

        if (filters == null) {
            return;
        }

        Map<String, String> properties = getFields();

        String type;
        String colFieldName;
        Object fieldValue;
        for (Iterator<String> itFilter = filters.keySet().iterator(); itFilter.hasNext();) {
            colFieldName = (String) itFilter.next();
            if (Pattern.compile("_FILTER$").matcher(colFieldName).find()) {
                continue;
            }
            String[] colFieldNames = colFieldName.split("-", -1);
            String fieldName = colFieldNames[0];
            String expr = "";
            if (colFieldNames.length == 2) {
                expr = colFieldNames[1];
            } //dungvv8 bo sung truong hop load mac dinh lan dau
            else if (colFieldNames.length == 3) {
                expr = colFieldNames[1];
            }
            //dungvv8
            fieldValue = (Object) filters.get(colFieldName);

//                        vietnv14 check key khong hop le start
            if ((fieldValue != null && (fieldValue instanceof List && ((List) fieldValue).size() < 1))) {
                continue;
            }
            if (fieldValue != null && fieldValue instanceof Object[] && ((Object[]) fieldValue).length < 1) {
                continue;
            }
//                        vietnv14 check key khong hop le end
            if (fieldName.contains(".")) {
                if (fieldName.startsWith(pkName + ".")) {

                } else {
                    String[] prs = fieldName.split("\\.", -1);
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
                        if (i < prs.length - 2) {
                            fieldName = fieldName.replaceFirst("\\.", "");
                        }
                    }

                    //fieldName = fieldName.replaceAll("^" + alias, alias + "-alias");
//					String alias = fieldName.split("\\.", -1)[0];
//					if (!criteria.getAlias().contains(alias))
//						criteria.createAlias(alias, alias + "-alias");
//					fieldName = fieldName.replaceAll("^" + alias, alias + "-alias");
                }
            }

            type = (String) properties.get(colFieldNames[0]);
            if (type == null) {
                throw new IllegalArgumentException("Không tìm thấy kiểu dữ liệu của trường tìm kiếm: " + fieldName);
            }
            switch (type) {
                case "java.lang.String":
                    switch (expr) {
                        case EXAC:
                            if (fieldValue instanceof Object[]) {
                                criteria.add(Restrictions.in(fieldName, (Object[]) fieldValue));
                            } else if (fieldValue instanceof List<?>) {
                                criteria.add(Restrictions.in(fieldName, (List<?>) fieldValue));
                            } else if (fieldValue instanceof String) {
                                criteria.add(Restrictions.eq(fieldName, (fieldValue)));
                            }
                            break;
                        case EXAC_IGNORE_CASE:
                            if (fieldValue instanceof Object[]) {
                                fieldValue = Arrays.asList(fieldValue);
                            }
                            if (fieldValue instanceof List<?>) {
                                List<Criterion> cers = new ArrayList<Criterion>();
                                for (Object value : (List<?>) fieldValue) {
                                    if (value instanceof String) {
                                        Criterion cer = Restrictions.ilike(fieldName, value);
                                        cers.add(cer);
                                    }
                                }
                                criteria.add(Restrictions.or(cers.toArray(new Criterion[cers.size()])));
                            } else if (fieldValue instanceof String) {
                                criteria.add(Restrictions.ilike(fieldName, (fieldValue)));
                            }
                            break;
                        case NEQ:
                            if (fieldValue instanceof String) {
                                criteria.add(Restrictions.ne(fieldName, (fieldValue)));
                            } else if (fieldValue == null) {
                                criteria.add(Restrictions.isNotNull(fieldName));
                            }
                            break;
                        default:
                            if (fieldValue == null) {
                                criteria.add(Restrictions.isNull(fieldName));
                                //vietnv start
                            } else if (fieldValue instanceof Object[]) {
                                criteria.add(Restrictions.in(fieldName, (Object[]) fieldValue));
                            } else if (fieldValue instanceof List<?>) {
                                criteria.add(Restrictions.in(fieldName, (List<?>) fieldValue));
                            } else {
                                //vietnv end
                                criteria.add(Restrictions.ilike(fieldName, ((String) fieldValue).toLowerCase(), MatchMode.ANYWHERE));
                            }
                            break;
                    }
                    break;
                case "java.lang.Integer":
                case "java.lang.Long":
                case "java.lang.Double":
                case "java.lang.Boolean":
                    if (Arrays.asList(new String[]{LT, GT, LE, GE}).contains(expr)) {
                        if (fieldValue instanceof String) {
                            Number a = NumberFormat.getInstance().parse((String) fieldValue);
                            if ("java.lang.Integer".equals(type)) {
                                fieldValue = a.intValue();
                            } else {
                                fieldValue = a;
                            }
                        }
                    } else {
                        if (fieldValue instanceof String) {
                            Number a = NumberFormat.getInstance().parse((String) fieldValue);
                            if ("java.lang.Integer".equals(type)) {
                                fieldValue = a.intValue();
                            } else {
                                fieldValue = a;
                            }
                        } else if (fieldValue instanceof List<?>) {
                            List<Object> fieldValueConverted = new ArrayList<>();
                            for (Object value : (List<?>) fieldValue) {
                                if (value instanceof String) {
                                    Number value2 = NumberFormat.getInstance().parse((String) value);
                                    if ("java.lang.Integer".equals(type)) {
                                        fieldValueConverted.add(value2.intValue());
                                    } else {
                                        fieldValueConverted.add(value2);
                                    }
                                } else {
                                    fieldValueConverted.add(value);
                                }

                            }
                            fieldValue = fieldValueConverted;
                        } else if (fieldValue instanceof String[]) {
                            List<Object> fieldValueConverted = new ArrayList<>();
                            for (Object value : (String[]) fieldValue) {
                                if (value instanceof String) {
                                    Number value2 = NumberFormat.getInstance().parse((String) value);
                                    if ("java.lang.Integer".equals(type)) {
                                        fieldValueConverted.add(value2.intValue());
                                    } else {
                                        fieldValueConverted.add(value2);
                                    }
                                } else {
                                    fieldValueConverted.add(value);
                                }
                            }
                            fieldValue = fieldValueConverted;
                        }
                    }
                    switch (expr) {
                        case LT:
                            if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.lt(fieldName, (fieldValue)));
                            }
                            break;
                        case GT:
                            if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.gt(fieldName, (fieldValue)));
                            }
                            break;
                        case LE:
                            if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.le(fieldName, (fieldValue)));
                            }
                            break;
                        case GE:
                            if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.ge(fieldName, (fieldValue)));
                            }
                            break;
                        case NEQ:
                            if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.ne(fieldName, (fieldValue)));
                            } else if (fieldValue == null) {
                                criteria.add(Restrictions.isNotNull(fieldName));
                            }
                            break;
                        case LIKE:
                            if (fieldValue != null) {
//                                String sql = "(lower(" + DataUtil.getColumnNameSQL(fieldName) + ") like '%'|| ? ||'%')";
                                String sql = "(lower(" + DataUtil.getColumnNameSQL(fieldName) + ") like  CONCAT('%',CONCAT(?,'%')) )";
                                criteria.add(Restrictions.sqlRestriction(sql, (fieldValue.toString()).toLowerCase(), StandardBasicTypes.TEXT));
                            }

                            break;
                        case EXAC:
                        default:
                            if (fieldValue instanceof Object[]) {
                                criteria.add(Restrictions.in(fieldName, (Object[]) fieldValue));
                            } else if (fieldValue instanceof List<?>) {
                                criteria.add(Restrictions.in(fieldName, (List<?>) fieldValue));
                            } else if (fieldValue instanceof Number) {
                                criteria.add(Restrictions.eq(fieldName, (fieldValue)));
                            } else if (fieldValue instanceof Boolean) {
                                criteria.add(Restrictions.eq(fieldName, (fieldValue)));
                            } else if (fieldValue == null) {
                                criteria.add(Restrictions.isNull(fieldName));
                            }
                            break;
                    }
                    break;
                case "java.util.Date":
                    if (fieldValue instanceof String) {
                        String fieldValue2 = ((String) fieldValue).trim();
                        Pattern.compile("\\s{2}").matcher(fieldValue2).replaceAll("\\s");
                        Pattern.compile("\\s-\\s").matcher(fieldValue2).replaceAll("-");
                        String[] times = fieldValue2.split("-", -1);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH");
                        SimpleDateFormat formatter3 = new SimpleDateFormat("dd/MM/yyyy");
                        Date startTime = null;
                        Date endTime = null;
                        if (times[0].trim() != "") {
                            if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}").matcher(times[0].trim()).matches()) {
                                startTime = formatter.parse(times[0].trim());
                            } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}").matcher(times[0].trim()).matches()) {
                                startTime = formatter1.parse(times[0].trim());
                            } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}").matcher(times[0].trim()).matches()) {
                                startTime = formatter2.parse(times[0].trim());
                            } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}").matcher(times[0].trim()).matches()) {
                                startTime = formatter3.parse(times[0].trim());
                            }
                        }
                        if (times.length >= 2) {
                            if (times[1].trim() != "") {
                                if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}").matcher(times[1].trim()).matches()) {
                                    endTime = formatter.parse(times[1].trim());
                                } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}").matcher(times[1].trim()).matches()) {
                                    endTime = formatter1.parse(times[1].trim());
                                } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}").matcher(times[1].trim()).matches()) {
                                    endTime = formatter2.parse(times[1].trim());
                                } else if (Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4}").matcher(times[1].trim()).matches()) {
                                    endTime = formatter3.parse(times[1].trim());
                                }
                            }
                        }
                        fieldValue = new Date[]{startTime, endTime};
                    }

                    if (fieldValue instanceof List<?>) {
                        if (((List<?>) fieldValue).size() > 0 && ((List<?>) fieldValue).get(0) instanceof Date) {
                            fieldValue = ((List<?>) fieldValue).toArray(new Date[((List<?>) fieldValue).size()]);
                        }
                    }
                    if (fieldValue instanceof Date[]) {
                        Date startTime = null;
                        Date endTime = null;
                        if (((Date[]) fieldValue).length == 2) {
                            if (((Date[]) fieldValue)[0] != null && ((Date[]) fieldValue)[0] instanceof Date) {
                                startTime = (Date) ((Date[]) fieldValue)[0];
                            }
                            if (((Date[]) fieldValue)[1] != null && ((Date[]) fieldValue)[1] instanceof Date) {
                                endTime = (Date) ((Date[]) fieldValue)[1];
                            }

                        }
                        if (startTime != null && endTime != null) {
                            criteria.add(Restrictions.between(fieldName, startTime, endTime));

                        } else if (startTime != null && endTime == null) {
                            if (expr != null && expr.equals(GT)) {
                                criteria.add(Restrictions.gt(fieldName, startTime));
                            } else {
                                criteria.add(Restrictions.ge(fieldName, startTime));
                            }

                        } else if (startTime == null && endTime != null) {
                            if (expr != null && expr.equals(LT)) {
                                criteria.add(Restrictions.lt(fieldName, endTime));
                            } else {
                                criteria.add(Restrictions.le(fieldName, endTime));
                            }
                        }
                    } else if (fieldValue instanceof Date) {
                        switch (expr) {
                            case LT:
                                criteria.add(Restrictions.lt(fieldName, fieldValue));
                                break;
                            case GT:
                                criteria.add(Restrictions.gt(fieldName, fieldValue));
                                break;
                            case LE:
                                criteria.add(Restrictions.le(fieldName, fieldValue));
                                break;
                            case GE:
                                criteria.add(Restrictions.ge(fieldName, fieldValue));
                                break;
                            case BW:
                                Date fromDate = DateTimeUtils.trunc((Date) fieldValue);
                                Date toDate = DateTimeUtils.add(fromDate, 1);
                                criteria.add(Restrictions.ge(fieldName, fromDate));
                                criteria.add(Restrictions.lt(fieldName, toDate));
                                break;
                            case EXAC:
                            default:
                                criteria.add(Restrictions.eq(fieldName, new Timestamp(((Date) fieldValue).getTime())));
                                break;
                        }

                    }
                    break;
            }

        }

    }

    protected void setParameters(Query query, Object... paramlist) {
        if (paramlist != null) {
            for (int i = 0; i < paramlist.length; i++) {
                if (paramlist[i] instanceof Date) {
                    query.setTimestamp(i + "", (Date) paramlist[i]);
                }
                if (paramlist[i] instanceof Map<?, ?>) {
                    continue;
                } else {
                    query.setParameter(i + "", paramlist[i]);
                }
            }
        }
    }

    private void setScalar(SQLQuery query, Map<String, Type> map) {
        if (map != null) {
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
                String paramName = (String) iterator.next();
                query.addScalar(paramName, map.get(paramName));
            }
        }

    }

    protected void setMapParameters(Query query, Map<String, Object> paramlist) {
        if (paramlist != null) {
            for (Iterator<String> iterator = paramlist.keySet().iterator(); iterator.hasNext();) {
                String paramName = (String) iterator.next();
                Object value = paramlist.get(paramName);

                if (value instanceof Date) {
                    query.setTimestamp(paramName, (Date) value);
                }
                if (value instanceof List<?>) {
                    query.setParameterList(paramName, (List<?>) value);
                } else {
                    query.setParameter(paramName, value);

                }
            }

        }
    }

    boolean isFilter = false;

    @SuppressWarnings("unchecked")
    protected void setSessionRestrictions(Session session, Map<String, Object> filters) throws ParseException {
        isFilter = false;
        if (filters != null) {
            for (Iterator<String> iterator = filters.keySet().iterator(); iterator.hasNext();) {
                String key = ((String) iterator.next());
                if (Pattern.compile("_FILTER$").matcher(key).find()) {
                    Object fieldValue = (Object) filters.get(key);
                    isFilter = true;
                    if (fieldValue instanceof String) {
                        if ("DISABLE".equals(fieldValue)) {
                            session.disableFilter(key);
                        }
                    }
                    if (fieldValue instanceof Boolean) {
                        if ((boolean) fieldValue == false) {
                            session.disableFilter(key);
                        }
                    } else if (fieldValue instanceof Map<?, ?>) {
                        Filter filter = session.enableFilter(key);
                        for (Iterator<String> iterator2 = ((Map<String, Object>) fieldValue).keySet().iterator(); iterator2.hasNext();) {
                            String fieldName = (String) iterator2.next();
                            Object value = ((Map<String, Object>) fieldValue).get(fieldName);
                            filter.setParameter(fieldName, value);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Object[] openTransaction() {
        Object[] results = new Object[2];
        results[0] = HibernateUtil.getSessionFactory().openSession();
        results[1] = ((Session) results[0]).beginTransaction();
        return results;
    }

    @Override
    public void saveOrUpdate(T object, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }
            session.saveOrUpdate(object);
            session.flush();
            if (isCommit) {
                tx.commit();
            }
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            try {
                if (session != null) {
                    if (isCommit || (tx != null && tx.getStatus() == TransactionStatus.ROLLED_BACK)) {
                        session.close();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void saveOrUpdate(List<T> objects, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }
            for (T object : objects) {
                session.saveOrUpdate(object);
            }
            if (isCommit) {
                tx.commit();
            }
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            try {
                if (session != null) {
                    if (isCommit || (tx != null && tx.getStatus() == TransactionStatus.ROLLED_BACK)) {
                        session.close();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public int execteBulk2(final String hql, Session _session, Transaction _tx, boolean isCommit, final Object... paramlist) {
        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }
            String _hql = fomatHQL(hql);
            Query query = session.createQuery(_hql);
            if (paramlist.length >= 1 && paramlist[0] instanceof Map<?, ?>) {
                setMapParameters(query, (Map<String, Object>) paramlist[0]);
            } else {
                setParameters(query, paramlist);
            }
            Object result = query.executeUpdate();
            if (isCommit) {
                tx.commit();
            }
            return result == null ? 0 : ((Integer) result).intValue();
        } catch (Exception ex) {
//            if (tx != null)
//                tx.rollback();
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            if (session != null) {
                if (isCommit || tx.getStatus() == TransactionStatus.ROLLED_BACK) {
                    session.close();
                }
            }
        }
    }

    @Override
    public void delete(List<T> objects, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }
            for (T object : objects) {
                session.delete(object);
            }
            if (isCommit) {
                tx.commit();
            }
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            try {
                if (session != null) {
                    if (isCommit || (tx != null && tx.getStatus() == TransactionStatus.ROLLED_BACK)) {
                        session.close();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void delete(T object, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException {
        Session session = null;
        Transaction tx = null;
        try {
            if (_session != null) {
                session = _session;
            } else {
                session = HibernateUtil.getSessionFactory().openSession();
            }
            if (_tx != null) {
                tx = _tx;
            } else {
                tx = session.beginTransaction();
            }
            session.delete(object);
            if (isCommit) {
                tx.commit();
            }
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new AppException();
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
            throw new SysException();
        } finally {
            try {
                if (session != null) {
                    if (isCommit || (tx != null && tx.getStatus() == TransactionStatus.ROLLED_BACK)) {
                        session.close();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private String fomatHQL(CharSequence queryString) {
        StringBuffer buffer = new StringBuffer(queryString);
        int start = 0;
        int order = 0;
        while ((start = buffer.indexOf("?", start + 1)) > 0) {
            buffer.insert(start + 1, order);
            order++;
        }
        return buffer.toString();
    }

    //    vietnv
    @Override
    public List<T> findList(Map<String, Object> filters, Map<String, Object> sqlRes, LinkedHashMap<String, String> orders) throws Exception {
        return findList(-1, -1, filters, sqlRes, orders);
    }

    @Override
    public List<T> findList(int first, int pageSize, Map<String, Object> filters, Map<String, Object> sqlRes, LinkedHashMap<String, String> orders) throws Exception {
        // TODO Auto-generated method stub
        Session session = null;
        Transaction tx = null;

        List<T> objects = new ArrayList<T>();
        try {
            session = HibernateUtil.openSession();
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(this.domainClass);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            setCriteriaRestrictions2(criteria, filters);
            setCriteriaRestrictions3(criteria, sqlRes);
            setCriteriaOrders(criteria, orders);
            if (first != -1) {
                criteria.setFirstResult(first);
            }
            if (pageSize != -1) {
                criteria.setMaxResults(pageSize);
            }
            objects = (List<T>) criteria.list();
            tx.commit();
        } catch (Exception e) {
            if (null != tx) {
                tx.rollback();
            }
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return objects;
    }

    @Override
    public int count2(Map<String, Object> filters, Map<String, Object> sqlRes) throws Exception {
        // TODO Auto-generated method stub
        Session session;
        Transaction tx;
        session = HibernateUtil.openSession();
        tx = session.beginTransaction();

        try {
            Criteria criteria = session.createCriteria(this.domainClass);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            setCriteriaRestrictions2(criteria, filters);
            setCriteriaRestrictions3(criteria, sqlRes);

            criteria.setProjection(Projections.rowCount());
            int count = 0;
            try {
                count = ((Number) criteria.uniqueResult()).intValue();
            } catch (Exception e) {
                throw e;
            }
            tx.commit();
            return count;
        } catch (HibernateException e) {
//            if (tx != null) {
            tx.rollback();
//            }
            LOGGER.error(e.getMessage(), e);

            throw new AppException();
        } catch (Exception e) {
//            if (null != tx) {
            tx.rollback();
//            }
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
//            if (session != null) {
            session.close();
//            }
        }
    }

    protected void setCriteriaRestrictions3(Criteria criteria, Map<String, ?> sqlRes) throws ParseException {
        if (sqlRes == null) {
            return;
        }
        Object value;
        for (Map.Entry<String, ?> entry : sqlRes.entrySet()) {
            String sql = entry.getKey();
            value = (Object) entry.getValue();
            if (value instanceof Number) {
                criteria.add(Restrictions.sqlRestriction(sql, value, StandardBasicTypes.LONG));
            } else if (value instanceof String) {
                criteria.add(Restrictions.sqlRestriction(sql, value, StandardBasicTypes.TEXT));
            } else if (value instanceof Date) {
                criteria.add(Restrictions.sqlRestriction(sql, value, StandardBasicTypes.DATE));
                //vietnv14 add
            } else if (value instanceof String[]) {
                int size = ((String[]) value).length;
                Type[] arrType = new Type[size];
                for (int i = 0; i < size; i++) {
                    arrType[i] = StandardBasicTypes.TEXT;
                }
                criteria.add(Restrictions.sqlRestriction(sql, (String[]) value, arrType));
            } else if (value instanceof Number[]) {
                int size = ((Number[]) value).length;
                Type[] arrType = new Type[size];
                for (int i = 0; i < size; i++) {
                    arrType[i] = StandardBasicTypes.LONG;
                }
                criteria.add(Restrictions.sqlRestriction(sql, (Number[]) value, arrType));

            } else {
                criteria.add(Restrictions.sqlRestriction(sql));

            }

        }

    }

}
