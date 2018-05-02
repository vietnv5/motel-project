package com.slook.util;

import com.slook.model.CatItemBO;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.log4j.Logger.getLogger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

//import com.viettel.persistence.SysActionLogServiceImpl;
/**
 * Created by dungvv8 on 3/8/2017.
 */
@Service(value = "commonUtil")
@Scope("session")
public class CommonUtil implements Serializable {

    private static final Logger logger = getLogger(CommonUtil.class);
    public Map<String, Boolean> colVisible;
//    @Autowired
//    private SysActionLogServiceImpl sysActionLogService;
    public static Mapper mapper = new DozerBeanMapper();

    public void onToggler(ToggleEvent e) {
        this.colVisible.put(new ArrayList<>(colVisible.keySet()).get((Integer) e.getData()),
                e.getVisibility() == Visibility.VISIBLE);
    }

//    public void createActionLog(Map<String, Object> map) {
//        try {
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            HttpServletRequest request = (HttpServletRequest) context.getRequest();
//            HttpSession session = request.getSession();
//            UserToken vsaUserToken = (UserToken) session.getAttribute("vsaUserToken");
//            Date startTime = (Date) map.get("startTime");
//            Date endTime = (Date) map.get("endTime");
//            Map<String, String> mapClassName = createMapClassName();
//            Map<String, String> mapFunctionName = createMapFunctionName();
//            String className = getClass().getName();
//            String functionName = (String) map.get("functionName");
//
//            SysActionLog sysActionLog = new SysActionLog();
//            sysActionLog.setIpClient(request.getRemoteHost());
//            sysActionLog.setUrl(request.getRequestURL().toString());
//            sysActionLog.setUserName(vsaUserToken.getUserName());
//            sysActionLog.setClassName(className);
//            sysActionLog.setFunctionName((String) map.get("functionName"));
//            sysActionLog.setFromObject((String) map.get("fromObject"));
//            sysActionLog.setToObject((String) map.get("toObject"));
//            sysActionLog.setStartTime(startTime);
//            sysActionLog.setEndTime(endTime);
//            sysActionLog.setDuration(endTime.getTime() - startTime.getTime());
//            sysActionLog.setClassFullName(mapClassName.get(className));
//            sysActionLog.setFunctionFullName(mapFunctionName.get(functionName));
//            sysActionLog.setIpServer(request.getLocalAddr());
//            sysActionLog.setPortServer((long) request.getLocalPort());
//
//            sysActionLogService = new SysActionLogServiceImpl();
//            sysActionLogService.save(sysActionLog);
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        }
//    }
    public Map<String, String> createMapClassName() {
        Map<String, String> map = new HashMap<>();
        map.put("com.viettel.controller.AlarmConfigController", MessageUtil.getKey("menu.title.config"));
        map.put("com.viettel.controller.AlarmMonitoringController", MessageUtil.getKey("menu.title.monitor"));
        map.put("com.viettel.controller.ObjectMonitorController", MessageUtil.getKey("menu.title.object"));
        map.put("com.viettel.controller.DashBoardController", MessageUtil.getKey("menu.title.dash_board"));
        return map;
    }

    public Map<String, String> createMapFunctionName() {
        Map<String, String> map = new HashMap<>();
        map.put("lockAlarmConfig", MessageUtil.getKey("funcName.lockAlarmConfig"));
        map.put("unlockAlarmConfig", MessageUtil.getKey("funcName.unlockAlarmConfig"));
        map.put("updateAlarmConfig", MessageUtil.getKey("funcName.updateAlarmConfig"));
        map.put("lockObject", MessageUtil.getKey("funcName.lockObject"));
        map.put("unlockObject", MessageUtil.getKey("funcName.unlockObject"));
        return map;
    }

//    public SysActionLogServiceImpl getSysActionLogService() {
//        return sysActionLogService;
//    }
//
//    public void setSysActionLogService(SysActionLogServiceImpl sysActionLogService) {
//        this.sysActionLogService = sysActionLogService;
//    }
    public Map<String, Boolean> getColVisible() {
        return colVisible;
    }

    public void setColVisible(Map<String, Boolean> colVisible) {
        this.colVisible = colVisible;
    }

//    vietnv start
    public static String getTableInConstraint(String constraintName) {
        Session session = null;
        String tableName = "";
        try {
            // lay ten constraint
            List<String> lstStr = StringUtil.findWithRegexMultiline(constraintName, " \\((\\w*\\d*\\.*)*\\)", 0);
            if (lstStr.size() > 0) {
                constraintName = lstStr.get(0).replaceAll("\\(", "")
                        .replaceAll("\\)", "");
                String[] arrs = constraintName.split("\\.");
                if (arrs.length > 0) {
                    constraintName = arrs[arrs.length - 1];
                }
            }

            session = HibernateUtil.openSession();

            Query query1 = session.createSQLQuery("   SELECT CONSTRAINT_NAME"
                    + "  FROM user_constraints"
                    + " WHERE"
                    + "   constraint_name = ?");
            query1.setParameter(0, constraintName);
            List lst = query1.list();

            if (lst.size() > 0) {
                tableName = (String) lst.get(0);
            }
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
        return tableName;
    }

    public static List getItemBOList(String catCode, String sortField) {
        Session session = null;
        List<CatItemBO> lst = new ArrayList<CatItemBO>();
        try {
            session = HibernateUtil.openSession();
            String choise = MessageUtil.getResourceBundleMessage("common.choose");
            Criteria cri = session.createCriteria(CatItemBO.class);
            cri.add(Restrictions.eq("catCode", catCode).ignoreCase());
            if (StringUtil.isNotNull(sortField)) {
                cri.addOrder(Order.asc(sortField));
            } else {
                cri.addOrder(Order.asc("code"));
            }
            List lstVendor = cri.list();
            CatItemBO bo = new CatItemBO();
            bo.setItemId(-999L);
            bo.setValue("");
            bo.setCode(choise);
            bo.setName(choise);
            lst.add(bo);
            if (lstVendor != null && lstVendor.size() > 0) {
                lst.addAll(lstVendor);
            }
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
//    vietnv end

    public static Map<String, Long> getMapCatItemByKeyName(Session session, String catCode) {
        Map<String, Long> map = new HashMap<String, Long>();
        Criteria cri = session.createCriteria(CatItemBO.class);
        cri.add(Restrictions.eq("catCode", catCode));
        List<CatItemBO> lst = cri.list();
        for (CatItemBO itemBO : lst) {
            map.put(itemBO.getName() != null ? itemBO.getName().trim().toUpperCase() : null, itemBO.getItemId());
        }
        return map;
    }

    public static Map<String, Long> getMapCatItemByKeyCode(Session session, String catCode) {
        Map<String, Long> map = new HashMap<String, Long>();
        Criteria cri = session.createCriteria(CatItemBO.class);
        cri.add(Restrictions.eq("catCode", catCode));
        List<CatItemBO> lst = cri.list();
        for (CatItemBO itemBO : lst) {
            map.put(itemBO.getCode() != null ? itemBO.getCode().trim().toUpperCase() : null, itemBO.getItemId());
        }
        return map;
    }

    public static Map<String, CatItemBO> getMapCatItemByKeyValue(Session session, String catCode) {
        Map<String, CatItemBO> map = new HashMap<String, CatItemBO>();
        Criteria cri = session.createCriteria(CatItemBO.class);
        cri.add(Restrictions.eq("catCode", catCode));
        List<CatItemBO> lst = cri.list();
        for (CatItemBO itemBO : lst) {
            map.put(itemBO.getValue() != null ? itemBO.getValue().trim().toUpperCase() : null, itemBO);
        }
        return map;
    }

    public static Map<String, CatItemBO> getMapCatItemByKeyValue(String catCode) {
        Map<String, CatItemBO> map = new HashMap<String, CatItemBO>();
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Criteria cri = session.createCriteria(CatItemBO.class);
            cri.add(Restrictions.eq("catCode", catCode));
            List<CatItemBO> lst = cri.list();
            for (CatItemBO itemBO : lst) {
                map.put(itemBO.getValue() != null ? itemBO.getValue().trim().toUpperCase() : null, itemBO);
            }
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
        return map;
    }

    public static Map<String, Long> getMapCatItemByKeyCode(String catCode) {
        Map<String, Long> map = new HashMap<String, Long>();
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            map = getMapCatItemByKeyCode(session, catCode);
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
        return map;
    }

    public static Map<Long, CatItemBO> getMapCatItemByKeyId(String catCode) {
        Map<Long, CatItemBO> map = new HashMap<Long, CatItemBO>();
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Criteria cri = session.createCriteria(CatItemBO.class);
            cri.add(Restrictions.eq("catCode", catCode));
            List<CatItemBO> lst = cri.list();
            for (CatItemBO itemBO : lst) {
                map.put(itemBO.getItemId(), itemBO);
            }
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
        return map;
    }

    public static Map convertListToMap(List<Object> lst, String field) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
            if (lst != null && lst.size() > 0) {
                Method methods = lst.get(0).getClass().getMethod(DataUtil.getGetterOfColumn(field));
                for (Object val : lst) {
                    Object value = methods.invoke(val);
                    map.put(value, val);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    public static Map convertListToMap(List<Object> lst, String field, boolean isUpperKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (lst != null && lst.size() > 0) {
                Method methods = lst.get(0).getClass().getMethod(DataUtil.getGetterOfColumn(field));
                for (Object val : lst) {
                    String value = (String) methods.invoke(val);
                    if (value != null) {
                        map.put(value.trim().toUpperCase(), val);
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    public static Map convertListToMap(List<Object> lst, String fieldKey, String fieldValue) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
            if (lst != null && lst.size() > 0) {
                Method methods = lst.get(0).getClass().getMethod(DataUtil.getGetterOfColumn(fieldKey));
                Method methodsVal = lst.get(0).getClass().getMethod(DataUtil.getGetterOfColumn(fieldValue));
                for (Object val : lst) {
                    Object valueKey = methods.invoke(val);
                    Object value = methodsVal.invoke(val);
                    map.put(valueKey, value);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return map;
    }

    public static boolean validateFileExtension(String fileName, List<String> lstFileType) {
        boolean rs = false;
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > -1) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            if (lstFileType != null && !lstFileType.isEmpty()) {
                for (String it : lstFileType) {
                    if (it != null && it.equalsIgnoreCase(extension)) {
                        return true;
                    }
                }
            }
        }
        return rs;
    }

    public static boolean makeDirectory(String urlFolder) {
        System.out.println(urlFolder);
        File directory = new File(urlFolder);

        if (!directory.exists()) {
            return directory.mkdir();
        }
        return true;
    }

    public static List getListAttributeInList(List<Object> lst, String field) {
        List lstRes = new ArrayList<>();
        try {
            if (lst != null && lst.size() > 0) {
                Method methods = lst.get(0).getClass().getMethod(DataUtil.getGetterOfColumn(field));
                for (Object val : lst) {
                    Object value = methods.invoke(val);
                    if (value != null) {
                        lstRes.add(value);
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lstRes;
    }

    public static boolean compareValue(String value, String comparisonOperator, String individualThreshold) {
        if (StringUtil.isNullOrEmpty(comparisonOperator)
                || (StringUtil.isNullOrEmpty(individualThreshold))) {
            return true;
        }
        if (StringUtil.isNullOrEmpty(value)) {
            return false;
        }
        String threshold = individualThreshold;
        switch (comparisonOperator) {

            case Constant.COMPARISON_OPERATOR.EQ:
                if (threshold.equalsIgnoreCase(value.trim())) {
                    return true;
                }
                try {
                    if (Double.valueOf(value).equals(Double.valueOf(threshold))) {
                        return true;
                    }
                } catch (Exception e) {
                }

                return false;
            case Constant.COMPARISON_OPERATOR.LT:
                try {
                    if (Double.valueOf(value) < (Double.valueOf(threshold))) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;
            case Constant.COMPARISON_OPERATOR.LE:
                try {
                    if (Double.valueOf(value) <= (Double.valueOf(threshold))) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;
            case Constant.COMPARISON_OPERATOR.GT:
                try {
                    if (Double.valueOf(value) > (Double.valueOf(threshold))) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;
            case Constant.COMPARISON_OPERATOR.GE:
                try {
                    if (Double.valueOf(value) > (Double.valueOf(threshold))) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;
            case Constant.COMPARISON_OPERATOR.IN:
                String[] lst = threshold.split(",");
                Set<String> set = new HashSet<String>();
                for (String s : lst) {
                    set.add(s.trim().toUpperCase());
                }
                if (set.contains(value.trim().toUpperCase())) {
                    return true;
                }
                return false;
            case Constant.COMPARISON_OPERATOR.NOT_IN:
                String[] lstNotIn = threshold.split(",");
                Set<String> setNotIn = new HashSet<String>();
                for (String s : lstNotIn) {
                    setNotIn.add(s.trim().toUpperCase());
                }
                if (setNotIn.contains(value.trim().toUpperCase())) {
                    return false;
                }
                return true;
            case Constant.COMPARISON_OPERATOR.CONTAINS:
                String[] lstValue = value.split(",");
                Set<String> setValue = new HashSet<String>();
                for (String s : lstValue) {
                    setValue.add(s.trim().toUpperCase());
                }
                if (setValue.contains(threshold.toUpperCase())) {
                    return true;
                }
                return false;
            case Constant.COMPARISON_OPERATOR.NOT_CONTAINS:
                String[] lstValueNot = value.split(",");
                Set<String> setValueNot = new HashSet<String>();
                for (String s : lstValueNot) {
                    setValueNot.add(s.trim().toUpperCase());
                }
                if (setValueNot.contains(threshold.toUpperCase())) {
                    return false;
                }
                return true;
            case Constant.COMPARISON_OPERATOR.BETWEEN:
                String[] lstNum = threshold.split(",");
                if (lstNum.length < 2) {
                    return false;
                }
                try {
                    Double val = Double.valueOf(value.trim());
                    Double val1 = Double.valueOf(lstNum[0]);
                    Double val2 = Double.valueOf(lstNum[1]);
                    if (val >= val1 && val <= val2) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;
            case Constant.COMPARISON_OPERATOR.NOT_BETWEEN:
                String[] lstNumNot = threshold.split(",");
                if (lstNumNot.length < 2) {
                    return false;
                }
                try {
                    Double val = Double.valueOf(value.trim());
                    Double val1 = Double.valueOf(lstNumNot[0]);
                    Double val2 = Double.valueOf(lstNumNot[1]);
                    if (val < val1 || val > val2) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return false;

            default:
                return false;
        }

    }

    public static String changeComparisonOperator(String operator) {
        String guide = "";
        if (operator != null) {

            switch (operator) {
                case Constant.COMPARISON_OPERATOR.GE:
                case Constant.COMPARISON_OPERATOR.GT:
                case Constant.COMPARISON_OPERATOR.LE:
                case Constant.COMPARISON_OPERATOR.LT:
                    guide = MessageUtil.getResourceBundleMessage("catChecklistDb.guide.inputNumber");
                    break;
                case Constant.COMPARISON_OPERATOR.EQ:
                    guide = MessageUtil.getResourceBundleMessage("catChecklistDb.guide.inputNumberOrString");
                    break;
                case Constant.COMPARISON_OPERATOR.BETWEEN:
                case Constant.COMPARISON_OPERATOR.NOT_BETWEEN:
                    guide = MessageUtil.getResourceBundleMessage("catChecklistDb.guide.inputBetween");
                    break;
                case Constant.COMPARISON_OPERATOR.CONTAINS:
                case Constant.COMPARISON_OPERATOR.NOT_CONTAINS:
                    guide = MessageUtil.getResourceBundleMessage("catChecklistDb.guide.inputContains");
                    break;
                case Constant.COMPARISON_OPERATOR.IN:
                case Constant.COMPARISON_OPERATOR.NOT_IN:
                    guide = MessageUtil.getResourceBundleMessage("catChecklistDb.guide.inputIn");
                    break;
            }
        } else {
            guide = "";
        }

        return guide;
    }
}
