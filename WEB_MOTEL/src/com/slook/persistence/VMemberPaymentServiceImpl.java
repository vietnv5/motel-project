/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_MemberPayment;
import com.slook.object.PaymentGroupPackForm;
import com.slook.object.PaymentSearchForm;
import com.slook.util.Constant;
import com.slook.util.HibernateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import static org.apache.log4j.Logger.getLogger;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author SLOOK. JSC on Nov 7, 2017 3:18:54 PM
 */
@Scope("session")
@Service(value = "vMemberPaymentService")
public class VMemberPaymentServiceImpl extends GenericDaoImplNewV2<V_MemberPayment, Long>
{

    private static final Logger logger = getLogger(VMemberPaymentServiceImpl.class);

    private static VMemberPaymentServiceImpl instance;

    public static VMemberPaymentServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new VMemberPaymentServiceImpl();
        }

        return instance;
    }

    public static String getFromDataPaymentGroupPack(PaymentSearchForm paymentSearchForm)
    {
        Long type = paymentSearchForm.getType();
        String fromSql = "(";

        if (type == null || Constant.CUSTOMER_CHECKIN.TYPE_GROUP_MEMBER.equals(type))
        {
            fromSql += "    select a.price,a.debt,b.group_pack_id,a.create_time,c.EMPLOYEE_ID "
                    + ",c.group_MEMBER_ID customer_id,c.group_MEMBER_NAME customer_name,d.PHONE_NUMBER ,3 as customer_Type"
                    + ",(case when a.type=2 or a.type=3 then 0 else 1  end) number_pack from\n"
                    + "group_member_PAYMENT a\n"
                    + "inner join group_membership b on a.group_membership_ID=b.group_membership_ID and  (b.status is null or b.status!=-1)\n"
                    + "inner join group_member c on c.group_member_id=a.group_member_id and c.status!=-1 "
                    + " left join member d on c.DEPUTATION_MEMBER_ID=d.member_id\n";
        }
        if (type == null)
        {
            fromSql += " union \n";
        }
        if (type == null || Constant.CUSTOMER_CHECKIN.TYPE_CLIENT.equals(type))
        {
            // client chi lay tong gia bang cac goi su dung va loai la dat coc
            // cong no tinh bang tong cac ban ghi trang thai la hoat dong
            // khach le dang ngung hoat dong cho cong no bang 0
            fromSql += "    select ( case when a.status=1 and a.type=1 "
                    + " and (  a.CLIENT_USE_PACK_ID=b.CLIENT_USE_PACK_ID  )  then a.price\n"
                    + "else 0 end) price ,( case when a.status=1 and c.status in (0,1)"
                    + " and  ( a.CLIENT_USE_PACK_ID=b.CLIENT_USE_PACK_ID ) then a.debt\n"
                    + "else 0 end) debt ,b.group_pack_id,a.create_time,c.EMPLOYEE_ID "
                    + ",c.client_ID customer_id,c.NAME customer_name,c.PHONE_NUMBER ,2 as customer_Type"
                    + ",(case when a.type=2 or a.type=3  then 0 else b.number_pack  end) number_pack from\n"
                    + "CLIENT_PAYMENT a\n"
                    + "left join CLIENT_USE_PACK b on a.CLIENT_USE_PACK_ID=b.CLIENT_USE_PACK_ID and (b.status is null or b.status!=-1)  \n"
                    + "inner join client c on c.client_id=a.client_id and c.status!=-1  and a.type=1\n";
        }
        if (type == null)
        {
            fromSql += " union ";
        }
        if (type == null || Constant.CUSTOMER_CHECKIN.TYPE_MEMBER.equals(type))
        {
            fromSql += "    select a.price,a.debt,b.group_pack_id,a.create_time,c.EMPLOYEE_ID "
                    + " ,c.MEMBER_ID customer_id,c.MEMBER_NAME customer_name,c.PHONE_NUMBER ,1 as customer_Type"
                    + ",(case when a.type=2 or a.type=3  then 0 else b.number_pack  end) number_pack from\n"
                    + "member_payment a\n"
                    + "inner join membership b on a.membership_id=b.membership_id and  (b.status is null or b.status!=-1)\n"
                    + "inner join member c on c.member_id=a.member_id and c.status!=-1\n";
        }

        fromSql += ")";

        return fromSql;
    }

    public static List<PaymentGroupPackForm> searchListPaymentGroupPack(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = "select sum(a.price) tongTien,sum(a.debt) tongNo ,( sum(a.price+a.debt)) daNop\n"
                    + ",d.GROUP_PACK_ID groupPackId,d.GROUP_PACK_NAME groupPackName,sum(a.number_pack) soLuong, " + type + " as customerType from(";
            sql += getFromDataPaymentGroupPack(paymentSearchForm);
            sql += ") a\n"
                    + "left join cat_group_pack d on a.group_pack_id= d.group_pack_id\n"
                    + "where 1=1  ";
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.create_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.create_time  <= :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }
            sql += " group by d.GROUP_PACK_NAME,d.GROUP_PACK_ID\n"
                    + "order by d.GROUP_PACK_NAME ";

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("tongTien", LongType.INSTANCE)
                    .addScalar("tongNo", LongType.INSTANCE);
            query1.addScalar("daNop", LongType.INSTANCE);
            query1.addScalar("groupPackId", LongType.INSTANCE);
            query1.addScalar("groupPackName", StringType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

    public static PaymentGroupPackForm totalPaymentGroupPack(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = "select sum(a.price) tongTien,sum(a.debt) tongNo ,( sum(a.price+a.debt)) daNop\n"
                    + ", sum(a.number_pack) soLuong,  " + type + " as customerType from(";
            sql += getFromDataPaymentGroupPack(paymentSearchForm);
            sql += ") a\n"
                    + "where 1=1  ";
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.create_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.create_time  <= :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("tongTien", LongType.INSTANCE)
                    .addScalar("tongNo", LongType.INSTANCE);
            query1.addScalar("daNop", LongType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (lst != null && lst.size() > 0)
        {
            return lst.get(0);
        }
        else
        {
            return new PaymentGroupPackForm();
        }
    }

    public static List<PaymentGroupPackForm> searchListEmployeePayment(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = "select sum(a.price) tongTien,sum(a.debt) tongNo ,( sum(a.price+a.debt)) daNop\n"
                    + ",a.EMPLOYEE_ID employeeId,e.EMPLOYEE_CODE employeeCode,e.EMPLOYEE_NAME employeeName,e.TELEPHONE telephone,sum(a.number_pack) soLuong, " + type + " as customerType from(";
            sql += getFromDataPaymentGroupPack(paymentSearchForm);
            sql += ") a\n";
//            sql += "left join cat_group_pack d on a.group_pack_id= d.group_pack_id\n";
            sql += " left join EMPLOYEE e on a.EMPLOYEE_ID= e.EMPLOYEE_ID";
            sql += " where 1=1  ";
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.create_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.create_time  <= :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }

            sql += " group by a.EMPLOYEE_ID ,e.EMPLOYEE_CODE,e.EMPLOYEE_NAME,e.TELEPHONE \n"
                    + "order by e.EMPLOYEE_NAME ";

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("tongTien", LongType.INSTANCE)
                    .addScalar("tongNo", LongType.INSTANCE);
            query1.addScalar("daNop", LongType.INSTANCE);
            query1.addScalar("employeeId", LongType.INSTANCE);
            query1.addScalar("employeeCode", StringType.INSTANCE);
            query1.addScalar("employeeName", StringType.INSTANCE);
            query1.addScalar("telephone", StringType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

    public static List<PaymentGroupPackForm> searchListEmployeePaymentGroupPack(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = " select sum(a.price) tongTien,sum(a.debt) tongNo ,( sum(a.price+a.debt)) daNop\n"
                    + ",d.GROUP_PACK_ID groupPackId,d.GROUP_PACK_NAME groupPackName"
                    + ",a.EMPLOYEE_ID employeeId,e.EMPLOYEE_CODE employeeCode,e.EMPLOYEE_NAME employeeName,e.TELEPHONE telephone,sum(a.number_pack) soLuong, " + type + " as customerType from(";
            sql += getFromDataPaymentGroupPack(paymentSearchForm);
            sql += ") a\n";
            sql += " left join cat_group_pack d on a.group_pack_id= d.group_pack_id\n";
            sql += " left join EMPLOYEE e on a.EMPLOYEE_ID= e.EMPLOYEE_ID";
            sql += " where 1=1  ";
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.create_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.create_time  <= :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }

            sql += " group by d.GROUP_PACK_NAME,d.GROUP_PACK_ID,a.EMPLOYEE_ID ,e.EMPLOYEE_CODE,e.EMPLOYEE_NAME,e.TELEPHONE \n"
                    + " order by e.EMPLOYEE_NAME, d.GROUP_PACK_NAME  ";

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("tongTien", LongType.INSTANCE);
            query1.addScalar("tongNo", LongType.INSTANCE);
            query1.addScalar("daNop", LongType.INSTANCE);
            query1.addScalar("employeeId", LongType.INSTANCE);
            query1.addScalar("employeeCode", StringType.INSTANCE);
            query1.addScalar("employeeName", StringType.INSTANCE);
            query1.addScalar("telephone", StringType.INSTANCE);
            query1.addScalar("groupPackId", LongType.INSTANCE);
            query1.addScalar("groupPackName", StringType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }
    // thong ke goi dich vu su dung - Thống kê được gói dịch vụ sử dụng nhiều nhất

    public static List<PaymentGroupPackForm> searchListUseGroupPack(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = "select count(*) soLuong,b.group_pack_id groupPackId,c.GROUP_PACK_NAME groupPackName, " + type + " as customerType"
                    + " from  CUSTOMER_CHECKIN a "
                    + " inner join (\n"
                    + " select a.membership_id, a.group_pack_id, 1 as type, 1 as type_cusomer from membership a"
                    + " inner join member c on c.member_id=a.member_id and c.status!=-1\n"
                    + " union\n"
                    + " select a.group_membership_id membership_id, a.group_pack_id, 1 as type,3 as type_cusomer from group_membership a"
                    + " inner join group_member c on c.group_member_id=a.group_member_id and c.status!=-1\n"
                    + " union\n"
                    + " select a.client_use_pack_id membership_id, a.group_pack_id, 2 as type, 1 as type_cusomer from client_use_pack a"
                    + " inner join client c on c.client_id=a.client_id and c.status!=-1\n"
                    + " ) b on a.membership_id=b.membership_id and a.type=b.type and ((a.group_member_id is null and b.type_cusomer=1 ) or (a.group_member_id is not null and b.type_cusomer=3) )\n"
                    + " left join cat_group_pack c on c.GROUP_PACK_ID=b.group_pack_id\n"
                    + " where 1=1 ";
            if (type != null)
            {
                sql += " and b.type  = :typeCustomer";
                filter.put("typeCustomer", type);
            }
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.check_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.check_time  <= :toDate \n";
                filter.put("toDate", paymentSearchForm.getToDate());
            }
            sql += " group by  b.group_pack_id,c.GROUP_PACK_NAME\n"
                    + "order by soLuong desc ";

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("groupPackId", LongType.INSTANCE);
            query1.addScalar("groupPackName", StringType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

    // thong ke khach hang
    public static List<PaymentGroupPackForm> searchListCustomerPayment(PaymentSearchForm paymentSearchForm)
    {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try
        {
            session = HibernateUtil.openSession();

            String sql = "select sum(a.price) tongTien,sum(a.debt) tongNo ,( sum(a.price+a.debt)) daNop\n"
                    + ",sum(a.number_pack) soLuong,a.EMPLOYEE_ID employeeId,e.EMPLOYEE_CODE employeeCode,e.EMPLOYEE_NAME employeeName,e.TELEPHONE telephone"
                    + ",a.customer_id customerId,a.customer_name customerName,a.PHONE_NUMBER phoneNumber, a.customer_Type as customerType from(";
            sql += getFromDataPaymentGroupPack(paymentSearchForm);
            sql += ") a\n";
//            sql += "left join cat_group_pack d on a.group_pack_id= d.group_pack_id\n";
            sql += " left join EMPLOYEE e on a.EMPLOYEE_ID= e.EMPLOYEE_ID";
            sql += " where 1=1  ";
            if (paymentSearchForm.getFromDate() != null)
            {
                sql += " and a.create_time  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null)
            {
                sql += " and  a.create_time  <= :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }
            if (paymentSearchForm.getLoaiNo() != null && paymentSearchForm.getLoaiNo().equals(1l))
            {
                sql += " having sum(a.debt)<0 \n";
            }
            else if (paymentSearchForm.getLoaiNo() != null && paymentSearchForm.getLoaiNo().equals(2l))
            {
                sql += " having sum(a.debt)>=0 \n";
            }

            sql += " group by a.EMPLOYEE_ID ,e.EMPLOYEE_CODE,e.EMPLOYEE_NAME,e.TELEPHONE , a.customer_Type ,a.customer_id,a.customer_name,a.PHONE_NUMBER \n"
                    + " order by tongNo ";

            SQLQuery query1 = session.createSQLQuery(sql);
            query1.addScalar("tongTien", LongType.INSTANCE)
                    .addScalar("tongNo", LongType.INSTANCE);
            query1.addScalar("daNop", LongType.INSTANCE);
            query1.addScalar("employeeId", LongType.INSTANCE);
            query1.addScalar("employeeCode", StringType.INSTANCE);
            query1.addScalar("employeeName", StringType.INSTANCE);
            query1.addScalar("telephone", StringType.INSTANCE);
            query1.addScalar("telephone", StringType.INSTANCE);
            query1.addScalar("customerId", LongType.INSTANCE);
            query1.addScalar("customerName", StringType.INSTANCE);
            query1.addScalar("phoneNumber", StringType.INSTANCE);
            query1.addScalar("customerType", LongType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet())
            {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            if (session != null)
            {
                try
                {
                    session.close();
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

}
