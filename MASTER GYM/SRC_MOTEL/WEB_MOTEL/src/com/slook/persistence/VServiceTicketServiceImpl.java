/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.V_ServiceTicket;
import com.slook.object.PaymentGroupPackForm;
import com.slook.object.PaymentSearchForm;
import com.slook.object.PaymentGroupPackForm;
import com.slook.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Calendar;
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
 *
 * @author VietNV on Nov 30, 2017
 */
@Service(value = "vServiceTicketService")
@Scope("session")
public class VServiceTicketServiceImpl extends GenericDaoImplNewV2<V_ServiceTicket, Long> {

    private static final Logger logger = getLogger(VServiceTicketServiceImpl.class);

    private static VServiceTicketServiceImpl instance;

    public static VServiceTicketServiceImpl getInstance() {
        if (instance == null) {
            instance = new VServiceTicketServiceImpl();
        }

        return instance;
    }

    public static List<PaymentGroupPackForm> searchListEmployeePaymentGroupPack(PaymentSearchForm paymentSearchForm) {
        Session session = null;
        List<PaymentGroupPackForm> lst = new ArrayList<>();
        Map<String, Object> filter = new HashMap<String, Object>();
        Long type = paymentSearchForm.getType();
        try {
            session = HibernateUtil.openSession();

            String sql = " select employee_id employeeId,employee_code employeeCode,employee_name employeeName"
                    + ",group_pack_id groupPackId,group_pack_name groupPackName,count(*) soLuong\n"
                    + ",employee_id || '_' || group_pack_id as keyServiceTicket, emp_telephone empTelephone ,job_title_id jobTitleId,job_title_name jobTitleName"
                    + " from v_service_ticket\n"
                    + "where status=2  ";
            if (paymentSearchForm.getFromDate() != null) {
                sql += " and USED_TIME  >= :fromDate ";
                filter.put("fromDate", paymentSearchForm.getFromDate());
            }
            if (paymentSearchForm.getToDate() != null) {
                sql += " and  USED_TIME  < :toDate \n";
                Calendar cd = Calendar.getInstance();
                cd.setTime(paymentSearchForm.getToDate());
//                cd.add(Calendar.DATE, 1);
                filter.put("toDate", cd.getTime());
            }

            sql += " group by  employee_id,employee_code,employee_name,group_pack_id,group_pack_name, EMP_TELEPHONE ,job_title_id,job_title_name \n"
                    + " order by EMPLOYEE_NAME, GROUP_PACK_NAME  ";

            SQLQuery query1 = session.createSQLQuery(sql);

            query1.addScalar("employeeId", LongType.INSTANCE);
            query1.addScalar("employeeCode", StringType.INSTANCE);
            query1.addScalar("employeeName", StringType.INSTANCE);
            query1.addScalar("groupPackId", LongType.INSTANCE);
            query1.addScalar("groupPackName", StringType.INSTANCE);
            query1.addScalar("soLuong", LongType.INSTANCE);
            query1.addScalar("keyServiceTicket", StringType.INSTANCE);
            query1.addScalar("empTelephone", StringType.INSTANCE);
            query1.addScalar("jobTitleId", LongType.INSTANCE);
            query1.addScalar("jobTitleName", StringType.INSTANCE);
            query1.setResultTransformer(Transformers.aliasToBean(PaymentGroupPackForm.class));
            for (String key : filter.keySet()) {
                query1.setParameter(key, filter.get(key));
            }
            lst = query1.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

}
