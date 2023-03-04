package com.slook.controller;

import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.model.LogAction;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.Constant;
import com.slook.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.omnifaces.util.Faces.getRequest;

/**
 * @author VietNV
 */
@ViewScoped
@ManagedBean
public class LogActionController
{


    private static final Logger logger = Logger.getLogger(LogActionController.class);

    GenericDaoServiceNewV2 logActionSevice;
    LazyDataModel<LogAction> lazyDataModel;

    private Date fromDate;
    private Date toDate;
    private List<Boolean> columnVisibale = new ArrayList<>();

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart()
    {
        logActionSevice = new GenericDaoImplNewV2<LogAction, Long>()
        {
        };

        Date sysdate = new Date();
        int y = sysdate.getYear();
        int m = sysdate.getMonth();
        int d = sysdate.getDate();

        fromDate = new Date(y, m, d - 10);
        toDate = new Date();
        search();
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, false, false, true, true);
    }

    public void search()
    {
        try
        {
            Map<String, Object> filters = new HashMap<String, Object>();
            Map<String, String> orders = new LinkedHashMap<>();

            if (fromDate != null)
            {
                filters.put("eventTime-GE", fromDate);
            }
            if (toDate != null)
//                filters.put("workDate-LT", new Date(toDate.getTime() + 24 * 60 * 60 * 1000));
            {
                filters.put("eventTime-LT", toDate);
            }

            orders.put("eventTime", Constant.ORDER.DESC);
            lazyDataModel = new LazyDataModelBase<>(logActionSevice, filters, orders);

            RequestContext.getCurrentInstance().execute("PF('tblWidgetId').clearFilters();");
            RequestContext.getCurrentInstance().update("form");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public GenericDaoServiceNewV2 getLogActionSevice()
    {
        return logActionSevice;
    }

    public void setLogActionSevice(GenericDaoServiceNewV2 logActionSevice)
    {
        this.logActionSevice = logActionSevice;
    }

    public LazyDataModel<LogAction> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<LogAction> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public static String getClientIpAddr(HttpServletRequest request)
    {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }

        return ip;

    }


    public static void writeLogAction(Session session, Long action, String objectCode,
                                      String oldValue, String newValue
            , String className, String function)
    {
        LogAction sysEventLogBO = new LogAction();
        String userName = "";
        try
        {
            CatUser user = (CatUser) getRequest().getSession().getAttribute("user");
            if (user != null)
            {
                userName = user.getUserName();
            }
            sysEventLogBO.setEventTime(new Date());
            sysEventLogBO.setUserName(userName);
            sysEventLogBO.setClientIp(getClientIpAddr(getRequest()));
            sysEventLogBO.setActionType(action);
            sysEventLogBO.setObjectCode(objectCode);
            sysEventLogBO.setOldValue(oldValue);
            sysEventLogBO.setNewValue(newValue);
            sysEventLogBO.setClassName(className);
            sysEventLogBO.setFunction(function);

            session.save(sysEventLogBO);
            session.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public static void writeLogAction(Long action, String objectCode,
                                      String oldValue, String newValue, String className, String function)
    {
        Session session = null;
        Transaction tx = null;
        try
        {
            session = HibernateUtil.openSession();
            tx = session.beginTransaction();
            writeLogAction(session, action, objectCode, oldValue, newValue, className, function);
            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null)
            {
                tx.rollback();
            }
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
    }
}
