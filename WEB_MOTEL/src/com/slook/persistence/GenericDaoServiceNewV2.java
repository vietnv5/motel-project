package com.slook.persistence;

import com.slook.exception.SysException;
import com.slook.persistence.common.ConditionQuery;
import com.slook.exception.AppException;
import com.slook.persistence.common.ObjectWithCondition;
import com.slook.persistence.common.OrderBy;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T>:  Model
 * @param <PK>: Type Class Primary
 * @author Nguyễn Xuân Huy <huynx6@viettel.com.vn>
 * @version 2.0
 * @sin Mar 16, 2016
 */
public interface GenericDaoServiceNewV2<T, PK extends Serializable> extends GenericDao<T, PK>, Serializable
{
    /**
     * @param filter HashMap
     * @param orders LinkedHashMap
     * @return List Object by filter & order
     * @throws AppException
     * @throws SysException
     * @author huynx6
     */
    public List<T> findListExac(Map<String, Object> filter, Map<String, String> orders) throws Exception;

    public T findById(PK id, Map<String, Object> filter) throws Exception;

    public List<T> findList(int first, int pageSize, Map<String, Object> filters, LinkedHashMap<String, String> sorters) throws Exception;

    public int count2(Map<String, Object> filters) throws Exception;

    List<T> findList(Map<String, Object> filters, LinkedHashMap<String, String> orders) throws Exception;

    List<T> findList(Map<String, Object> filters) throws Exception;

    List<T> findListWithIn(String hql, int start, int length, Map<String, Collection<?>> map, Object... paramlist);

    List<T> findList(String hql, int pageNumber, int pageSize, Object... paramlist);

    int execteNativeBulk(String natvieSQL, Object... paramlist);

    int execteBulk(String hql, Object... paramlist);

    List<T> findList(ConditionQuery query, OrderBy orderBy, int pageNumber, int pageSize);

    List<T> findList2(int first, int pageSize, Map<String, ObjectWithCondition> filters, LinkedHashMap<String, String> orders) throws Exception;

    int count(String hibernateConfig, String sql, Map<String, Object> paramlist);

    List<?> findListSQLWithPosParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Class<?> classBean, Map<String, Type> addScale, Object... paramlist);

    List<?> findListSQLWithMapParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Map<String, Object> paramlist, Map<String, Type> addScale,
                                         Class<?> classBean);

    List<?> findListSQLWithPosParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Map<String, Type> addScale, Object... paramlist);

    List<?> findListSQLWithPosParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Object... paramlist);

    List<?> findListSQL(String hibernateConfig, String sql, int pageNumber, int pageSize, Object... paramlist);

    List<?> findListSQLWithMapParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Map<String, Object> paramlist, Map<String, Type> addScale);

    List<?> findListSQLWithMapParameters(String hibernateConfig, String sql, int pageNumber, int pageSize, Map<String, Object> paramlist);

    int countOld(Map<String, String> filters) throws AppException, SysException;

    List<T> findListOld(int first, int pageSize, Map<String, String> filters) throws AppException, SysException;

    List<T> findListOld(int first, int pageSize, Map<String, String> filters, Map<String, String> orders) throws AppException, SysException;

    List<T> findListOld(Map<String, String> filters, Map<String, String> orders) throws AppException, SysException;

    Criteria setCriteriaRestrictionsOld(Criteria criteria, Map<String, String> filters);


    long count(ConditionQuery query);

    void saveOrUpdate(T object, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException;

    void saveOrUpdate(List<T> objects, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException;

    int execteBulk2(String hql, Session _session, Transaction _tx, boolean isCommit, Object... paramlist);

    void delete(List<T> objects, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException;

    void delete(T object, Session _session, Transaction _tx, boolean isCommit) throws AppException, SysException;

    List<T> findListAll(String hql, Object... paramlist);

    List<?> findListSQLAll(String sql, Object... paramlist);

    int execteNativeBulk2(String natvieSQL, Session _session, Transaction _tx, boolean isCommit, Map<String, Object> paramMap, Object... paramlist);

    int execteNativeBulk3(String natvieSQL, Session _session, Transaction _tx, boolean isCommit, Object... paramlist);

    Object[] openTransaction();

    //dungvv8
    Map<String, Object> getCurrFilters();

    void setCurrFilters(Map<String, Object> filters);

    LinkedHashMap<String, String> getCurrOrders();

    void setCurrOrders(LinkedHashMap<String, String> orders);

    List<T> findListExport();
    //vietnv

    List<T> findList(Map<String, Object> filters, Map<String, Object> sqlRes, LinkedHashMap<String, String> orders) throws Exception;

    public List<T> findList(int first, int pageSize, Map<String, Object> filters, Map<String, Object> sqlRes, LinkedHashMap<String, String> sorters) throws Exception;

    public int count2(Map<String, Object> filters, Map<String, Object> sqlRes) throws Exception;

}
