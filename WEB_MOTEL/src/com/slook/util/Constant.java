package com.slook.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dungvv8 on 12/20/2016.
 */
//@ManagedBean
//@RequestScoped
public interface Constant {

    String _USER_TOKEN = "user";

    interface DIR {

        String REPORT_OUT = File.separator + ".." + File.separator + ".." + File.separator + "report_out" + File.separator;
        String TEMPLATES = "templates";
    }

    interface PAGE {

        String _ACCESS_DENIED = "/access-denied";
//        String _DEFAULT_URL = "/dashboard";
        String _DEFAULT_URL = "/member";
        String _LOGIN = "/login";
        String _ERROR_PAGE = "/error";
    }

    enum ACTION {
        ADD("ADD"),
        VIEW("VIEW"),
        EDIT("EDIT");
        String value;

        ACTION(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    enum GROUP_WORK_TYPE {
        WORK(1L),
        LEAVE(2L),
        UNPAID_LEAVE(3L),
        NO_REASON(4L),
        DAY_OFF(5L);
        Long value;

        GROUP_WORK_TYPE(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }

        private static Map<Long, GROUP_WORK_TYPE> map = new HashMap<>();

        static {
            for (GROUP_WORK_TYPE gs : GROUP_WORK_TYPE.values()) {
                map.put(gs.getValue(), gs);
            }
        }

        public static GROUP_WORK_TYPE valueOf(Long value) {
            return map.get(value);
        }
    }

    //    interface VSA_ROLE {
//        String AOM_ADMIN = "AOM_ADMIN";
//        String AOM_MONITOR = "AOM_MONITOR";
//    }
    enum VSA_ROLE {
        AOM_ADMIN("AOM_ADMIN"),
        AOM_MONITOR("AOM_MONITOR");
        String value;

        VSA_ROLE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //    interface MONITOR_TYPE {
//        String DATABASE = "DATABASE";
//        String APPLICATION = "APPLICATION";
//        String SERVER = "SERVER";
//    }
    enum MONITOR_TYPE {
        DATABASE_INSTANCE("DATABASE_INSTANCE"),
        DATABASE("DATABASE"),
        DATABASE_ORACLE_ALAM("DATABASE_ORACLE_ALAM"),
        DATABASE_MSSQL_ALAM("DATABASE_MSSQL_ALAM"),
        DATABASE_MYSQL_ALAM("DATABASE_MYSQL_ALAM"),
        SERVER("SERVER"),
        MOUNT_POINT("MOUNT_POINT"),
        APPLICATION("APPLICATION");
        String value;

        MONITOR_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    interface ORDER {

        String ASC = "ASC";
        String DESC = "DESC";
    }

    interface REGEX {

        String INTEGER = "\\d+";
        String DECIMAL = "-?\\d+(\\.\\d{1,2})?";
    }

    interface EXTRA_INFOR {

        String CRITICAL = "CRITICAL";
        String WARNING = "WARNING";
        String CLEARED = "CLEARED";
    }

    interface SERVICE {

        Long PRICE_ROOM_ID = 5L;
        String ELECTRIC = "ELECTRIC";//dv dien de tinh tien dien
        String WATER = "WATER";//dv nuoc de tinh tien nuoc

    }

//    interface STATUS {
//        Long ENABLE = 1L;
//        Long DISABLE = 0L;
//    }
//    interface STATUS_NAME {
//        String ENABLE = "ENABLE";
//        String DISABLE = "DISABLE";
//    }
    interface EXPORT {

        int TITLE_ROW = 0;
        int HEADER_ROW = 1;
        int DATA_ROW = 2;
    }

    interface FILE_NAME {

        String EMP_NO_REASON = "Emp_No_Reason";
        String EMP_OFF = "Emp_Off";
        String EMP_IN_LATE = "Emp_In_Late";
        String EMP_OUT_EARLY = "Emp_Out_Early";
        String ALARM_MONITORING = "alarmMonitoring";
        String SYS_ACTION_LOG = "sysActionLog";
        String RESULT_CHECK_IN = "Result_CheckIn";
        String RESULT_WORK_DAY = "Result_WorkDay";
        String IMPORT_CHECK_IN = "Template_Import_CheckIn";
        String IMPORT_WORK_DAY = "Template_Import_WorkDay";
    }

    interface EXTENSION {

        String XLSX = ".xlsx";
        String XLS = ".xls";
    }

    enum SERVICE_TYPE {
        DATABASE_INSTANCE("DATABASE_INSTANCE"),
        DATABASE("DATABASE"),
        SERVER("SERVER"),
        MOUNT_POINT("MOUNT_POINT");

        String value;

        SERVICE_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    enum ALARM_TYPE {
        DATABASE_INSTANCE("DATABASE_INSTANCE"),
        DATABASE("DATABASE"),
        SERVER("SERVER"),
        MOUNT_POINT("MOUNT_POINT");

        String value;

        ALARM_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    interface DB_REDIS {

        int AUDIT = 0;
        int CONFIG = 1;
        int SERVER = 2;
        int MODULE = 3;
        int MODULE_USER = 4;
        int DATABASE_CONFIG = 5;
        int ALARM_LEVEL = 6;
        int KPI_VALUE = 7;
        int ALARM_SERVER = 8;
        int ALARM_PROCESS = 9;
        int ALARM_DATABASE = 10;
        int MODULE_SERVICE_DATABASE = 11;
        int ALARM_CONFIG = 12;
        int KPI_DB_VALUE = 13;
        int DB_USER_SESSION = 14;
        int CLEAR_ALARM = 15;
    }

    interface PATTERN {

        String DATETIME_COMMON = "dd/MM/yyyy HH:mm:ss";
        String DATE_COMMON = "dd/MM/yyyy";
        String DATETIME_CHART = "yyyy-MM-dd HH:mm:ss";
        String DATE_CHART = "yyyy-MM-dd";

        String DATETIME_COMMON_YY = "dd/MM/yy HH:mm:ss";
        String DATE_COMMON_YY = "dd/MM/yy";
    }

    interface TEMPLATE {

        String IMPORT_CHECK_IN = "Template_Import_CheckIn.xlsx";
        String IMPORT_WORK_DAY = "Template_Import_WorkDay.xlsx";
    }

    //    interface GROUP_SERVICE {
//        Long DATABASE = 5L;
//        Long SERVER = 1L;
//    }
    enum GROUP_SERVICE {
        DATABASE(5L),
        DATABASE_ORACLE_ALAM(5L),
        DATABASE_MYSQL_ALAM(6L),
        DATABASE_MSSQL_ALAM(7L),
        SERVER(1L);
        Long value;

        GROUP_SERVICE(Long value) {
            this.value = value;
        }

        public Long getValue() {
            return value;
        }

        private static Map<Long, GROUP_SERVICE> map = new HashMap<>();

        static {
            for (GROUP_SERVICE gs : GROUP_SERVICE.values()) {
                map.put(gs.getValue(), gs);
            }
        }

        public static GROUP_SERVICE valueOf(Long value) {
            return map.get(value);
        }
    }

    //    interface PATTERN {
//        String DATETIME_COMMON = "dd/MM/yyyy HH:mm:ss";
//        String DATE_COMMON = "dd/MM/yyyy";
//    }
    interface CAT_CODE {

        String HEALTH_LEVEL = "HEALTH_LEVEL";
        String CONTRACT_TYPE = "CONTRACT_TYPE";
        String MARRIED_STATUS = "MARRIED_STATUS";
        String REASON_DEBT = "REASON_DEBT";
        String MACHINE_TYPE = "MACHINE_TYPE";
        String COMPARISON_OPERATOR = "COMPARISON_OPERATOR";

        String UNIT = "UNIT";

    }

    interface STATUS {

        Long DELETE = -1l;

        final Long INACTIVE = 0L;
        final Long ACTIVE = 1L;
        final Long PAUSE = 2L;

        Long DO_NOT_GET_DATA = 0L;
        Long PUSH_DATA_SUCCESS = 1L;
        Long IS_NOT_PUSH_DATA = 1L;

        Long ENABLE = 1L;
        Long DISABLE = 0L;

    }

    public interface LOG_ACTION {

        Long INSERT = 1L;
        Long UPDATE = 2L;
        Long DELETE = 3L;
        Long IMPORT = 4L;
        Long ROLE = 5L;
    }

    interface DATA_TYPE {

        Long EMPLOYEE = 1L;
        Long CHECK_IN = 0L;
        Long MACHINE = 2L;
    }

    interface METHOD {

        String INSERT = "INSERT";
        String UPDATE = "UPDATE";
        String DELETE = "DELETE";
    }
    public static final String DOCUMENT_FOLDER = "../../../resources/document/";
    public static final String FINGERPRINT_FOLDER = "../../../resources/fingerprintImg/";
    public static final String FINGERPRINT_FOLDER_TEMP = "../../../resources/fingerprintImg_temp/";
    public static final String REPORT_DESIGN = "../../../resources/report_design/";
//    public static final String DOCUMENT_FOLDER = "document/";
    public static final String OUT_FOLDER = "../../../report_out/";
    public static final String FONT_TIMES = "resources/olympos-layout/fonts/times.ttf";

    interface EMPLOYEE_STATUS {

        Long DELETE = -1l;
        Long NEW = 0l;
        Long ACTIVE = 1l;
        Long PAUSE = 2l;
    }

    interface MEMBER_STATUS {

        Long DELETE = -1l;
        Long NEW = 0L;
        Long ACTIVE = 1L;
        Long PAUSE = 2L;
        Long RESERVE = 3L;
        Long STOP = 4L;

    }

    interface PROMOTION_TYPE {

        Long THEM_LUOT = 1l;
        Long GIAM_TIEN_PERCENT = 2l;
        Long GIAM_TIEN_MAT = 3l;
        Long THEM_THOI_GIAN = 4l;

    }

    interface MEMBER_TYPE {

        Long MEMBER = 1L;
        Long GROUP_MEMBER = 2L;

        String GRADE_COPPER = "COPPER";
        String GRADE_SILVER = "SILVER";
        String GRADE_GOLD = "GOLD";
        String GRADE_DIAMOND = "DIAMOND";

    }

    interface MEMBERSHIP_ACTION_TYPE {

        Long NEW = 1L;
        Long EXTEND = 2L;
        Long TRANSFER = 3L;
    }

    interface CLIENT_STATUS {

        Long NEW = 0l;
        Long ACTIVE = 1l;
        Long STOP = 2L;
        Long RESERVE = 3L;
    }

    interface PAYMENT_TYPE {

        Long MEMBER = 1l;
        Long GROUP_MEMBER = 3l;
        Long CLIENT = 2L;
        Long DAT_COC_MUA_GOI = 1l;
        Long THANH_TOAN_TRA_NO = 2l;
        Long FEE_TRANSFER = 3l;
    }

    interface CUSTOMER_CHECKIN {

        Long CHECKIN = 1L;
        Long CHECKOUT = 2L;

        Long TYPE_MEMBER = 1l;
        Long TYPE_CLIENT = 2l;
        Long TYPE_GROUP_MEMBER = 3l;
    }

    interface MEMBERSHIP_STATUS {

        Long ACTIVE = 1l;
        Long RECEIVE = 4l;
        Long STATUS_SCHEDULE = 5l;
        Long STATUS_DISABLE = 6l;
    }

    interface MEMBER_USED_SERVICE {

        Long VALID = 1l;
        Long EXPIRE = 2l;
    }

    interface CLIENT_PAYMENT {

        Long TYPE_DEPOSIT = 1l;
        Long TYPE_PAYMENT = 2l;
        Long STATUS_NOT_USE = 0l;
        Long STATUS_USED = 1l;

        Long FIELD_PRICE = 1l;
        Long FIELD_DEPOSIT = 2l;

    }

    interface CLIENT_USE_PACK {

        Long STATUS_ACTIVE = 1l;
        Long STATUS_STOP = 2l;
        Long STATUS_SCHEDULE = 5l;
    }

    interface CAT_PROMOTION {

        Long ENABLE = 1L;
        Long DISABLE = 2L;

    }

    interface CAT_ITEM {

        interface REASON_DEBT {

            String USING_SERVICE = "USING_SERVICE";
        }

        interface UNIT {

            Long ROOM_PER_MONTH_ID = 2L;

        }

    }

    interface CONFIG {

        String IS_TEST = "1";
        String NO_TEST = "0";
        String IS_LOCAL = "1";
        String NO_LOCAL = "0";
    }

    interface WS_C_METHOD {

        int GET_LIST_ACCESS = 0;
        int ADD_ACCESS = 1;
        int REMOVE_ACCESS = 2;
        String RESULT_FAILURE = "FAILURE";
        String RESULT_SUCCESS = "SUCCESS";

        Long TYPE_CARD = 1L;
        Long TYPE_FINGERPRINT = 2L;

    }

    interface CUSTOMER_SCHEDULE {

        Long STATUS_SCHEDULE = 1L;
        Long STATUS_ACTIVE = 2L;
        Long STATUS_COMPLETED = 3L;
        Long STATUS_CANCEL = 4L;

    }

    interface DOOR_ACCESS {

        Long DOOR_TYPE_IN = 1l;
        Long DOOR_TYPE_OUT = 2l;
    }

    interface GROUP_PACK_TYPE {

        Long TYPE_LE = 1l;
        Long TYPE_HV_LUOT = 2l;
        Long TYPE_HV_TIME = 3l;
        Long TYPE_GROUP = 4l;

    }

    public interface COMPARISON_OPERATOR {

        public static final String EQ = "=";
        public static final String LT = "<";
        public static final String GT = ">";
        public static final String LE = "<=";
        public static final String GE = ">=";
        public static final String IN = "IN";
        public static final String NOT_IN = "NOT IN";
        public static final String CONTAINS = "CONTAINS";
        public static final String NOT_CONTAINS = "NOT CONTAINS";
        public static final String BETWEEN = "BETWEEN";
        public static final String NOT_BETWEEN = "NOT BETWEEN";
    }

    interface SERVICE_TICKET {

        Long STATUS_NEW = 1L;
        Long STATUS_USED = 2L;

    }

    interface REPORT_TYPE {

        Long REPORT_PAYMENT_GROUP_PACK = 1L;
        Long REPORT_EMPLOYEE_PAYMENT = 2L;
        Long REPORT_CUSTOMER_PAYMENT = 3L;
        Long REPORT_USE_GROUP_PACK = 4L;
        Long REPORT_SERVICE_TICKET = 5L;

    }

    interface VERIFY_MODE {

        Long FINGERPRINT = 1L;
        Long USER_PASS = 3L;
        Long CARD_CODE = 4L;

    }

    interface FUNCTION_PATH {

        Long TYPE_GROUP = 1L;
        Long TYPE_FUNCTION = 2L;
        Long TYPE_ACTION = 3L;

    }

    interface ROOM_STATUS {

        Long DELETE = -1l;
        Long FREE = 1L;
        Long USE = 2L;
        Long STOP = 3L;

    }
    interface HOME_STATUS {

        Long DELETE = -1l;
        Long ACTIVE = 1L;
        Long DISABLE = 2L;

    }

    interface CONTRACT_SERVICE {

        Long DEFAULT_STATUS_ON = 1L;
        Long DEFAULT_STATUS_OFF = 0L;
    }

    interface CONTRACT {

        Long STATUS_ACTIVE = 1L;
        Long STATUS_END = 2L;
    }

    interface CUSTOMER_ROOM {

        Long TYPE_PRIMARY = 1L;
        Long TYPE_EXTRA = 2L;

        Long STATUS_ACTIVE = 1L;
        Long STATUS_CHECKOUT = 2L;
    }

}
