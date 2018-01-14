/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.util;

//import com.viettel.language.util.EnumWordType;
//import com.viettel.language.util.MultiLanguageNumberToWords;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.Range;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DataUtil {

    protected static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static String getStringNullOrZero(String strNullOrZero) {
        return isStringNullOrEmpty(strNullOrZero) ? "" : strNullOrZero;
    }


    /*
     * Kiem tra Long bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    /**
     * Kiem tra Bigdecimal bi null hoac zero
     *
     * @param value
     * @return
     */
    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.equals(BigDecimal.ZERO));
    }

    /**
     * Lay ten phuong thuc getter
     *
     * @param columnName
     * @return
     */
    public static String getHibernateName(String columnName) {
        columnName = columnName.toLowerCase();
        String[] arrs = columnName.split("_");
        String method = "";
        for (String arr : arrs) {
            method += DataUtil.upperFirstChar(arr);
        }
        return method;
    }

    /**
     * Lay getter
     *
     * @param columnName
     * @return
     */
    public static String getGetterByColumnName(String columnName) {
        return "get" + getHibernateName(columnName);
    }

    //truong bx3 modify 20/04/2015 for tree
    public static String getGetterOfColumn(String column) {
        return "get" + upperFirstChar(column);

    }

    public static String getSetterOfColumn(String column) {
        return "set" + upperFirstChar(column);

    }
//       truongbx3 finish modify

    /**
     * Lay ten phuong thuc setter
     *
     * @param columnName
     * @return
     */
    public static String getSetterByColumnName(String columnName) {
        return "set" + getHibernateName(columnName);
    }

    /**
     * Upper first character
     *
     * @param input
     * @return
     */
    public static String upperFirstChar(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Lower first characater
     *
     * @param input
     * @return
     */
    public static String lowerFirstChar(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return input;
        }
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    /**
     * @param obj1 Object
     * @return Long
     */
    public static Long safeToLong(Object obj1) {
        Long result = 0L;
        if (obj1 != null) {
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ex) {
                logger.debug(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Double safeToDouble(Object obj1) {
        Double result = 0.0;
        if (obj1 != null) {
            try {
                result = Double.parseDouble(obj1.toString());
            } catch (Exception ex) {
                logger.debug(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Short safeToShort(Object obj1) {
        Short result = 0;
        if (obj1 != null) {
            try {
                result = Short.parseShort(obj1.toString());
            } catch (Exception ex) {
                logger.debug(ex.getMessage(), ex);
            }
        }

        return result;
    }

    /**
     * @param obj1 Object
     * @return int
     */
    public static int safeToInt(Object obj1) {
        int result = 0;
        if (obj1 == null) {
            return 0;
        }
        try {
            result = Integer.parseInt(obj1.toString());
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1) {
        if (obj1 == null) {
            return "";
        }

        return obj1.toString();
    }

    /**
     * safe equal
     *
     * @param obj1 Long
     * @param obj2 Long
     * @return boolean
     */
    public static boolean safeEqual(Long obj1, Long obj2) {
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    /**
     * safe equal
     *
     * @param obj1 String
     * @param obj2 String
     * @return boolean
     */
    public static boolean safeEqual(String obj1, String obj2) {
        return ((obj1 != null) && (obj2 != null) && obj1.equals(obj2));
    }

    /**
     * increase cur no
     *
     * @param obj1 String
     * @param obj2 String
     * @return String
     */
    public static String increaseCurNo(String obj1, int obj2) {
        return String.format("%05d", Integer.parseInt(obj1) + obj2);
    }

    /**
     * create log
     *
     * @param info String
     * @return String
     */
    public static String createLog(String info) {
        //return (DateUtil.dateTime2String(DateUtil.sysDate()) + ": " + info);
        return info;
    }

    /**
     * check null or empty
     *
     * @param obj1 String
     * @return boolean
     */
    public static boolean isNullOrEmpty(String obj1) {
        return (obj1 == null || "".equals(obj1.trim()));
    }

    public static boolean isStringNullOrEmpty(Object obj1) {
        return obj1 == null || "".equals(obj1.toString().trim());
    }

    public static boolean isStringNotNullAndNotEmpty(Object obj1) {
        return !isStringNullOrEmpty(obj1);
    }

    /**
     * @param obj1 Object
     * @return BigDecimal
     */
    public static BigDecimal safeToBigDecimal(Object obj1) {
        BigDecimal result = new BigDecimal(0);
        if (obj1 == null) {
            return result;
        }
        try {
            result = new BigDecimal(obj1.toString());
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
        }

        return result;
    }

    public static BigInteger safeToBigInterger(Object obj1) {
        BigInteger result = null;
        if (obj1 == null) {
            return null;
        }
        try {
            result = new BigInteger(obj1.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    /**
     * add
     *
     * @param obj1 BigDecimal
     * @param obj2 BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal add(BigDecimal obj1, BigDecimal obj2) {
        if (obj1 == null) {
            return obj2;
        } else if (obj2 == null) {
            return obj1;
        }

        return obj1.add(obj2);
    }

    /**
     * Convert an IP address to a number
     *
     * @param ipAddress Input IP address
     * @return The IP address as a number
     */
    public final static String MAX_NUMBER_RANGE = "1000000";

    public static BigInteger ipv4ToNumber(String ipAddress) {
        BigInteger result = BigInteger.valueOf(0);
        String[] atoms = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--) {
            BigInteger bi = new BigInteger(atoms[3 - i]);
            result = result.shiftLeft(8).add(bi);
        }

        return result;
    }

    public static String numberToIpv4(BigInteger ipNumber) {

        String ipString = "";
        BigInteger a = new BigInteger("FF", 16);

        for (int i = 0; i < 4; i++) {
            ipString = ipNumber.and(a).toString() + "." + ipString;

            ipNumber = ipNumber.shiftRight(8);
        }

        return ipString.substring(0, ipString.length() - 1);
    }

//    public static BigInteger ipv6ToNumber(String addr) {
//        int startIndex = addr.indexOf("::");
//
//        if (startIndex != -1) {
//
//            String firstStr = addr.substring(0, startIndex);
//            String secondStr = addr.substring(startIndex + 2, addr.length());
//
//            BigInteger first = ipv6ToNumber(firstStr);
//
//            int x = countChar(addr, ':');
//            int y = countChar(secondStr, ':');
//            //first = first.shiftLeft(16 * (7 - x)).add(ipv6ToNumber(secondStr));
//            first = first.shiftLeft(16 * (7 - x + y));
//            first = first.add(ipv6ToNumber(secondStr));
//
//            return first;
//        }
//
//        String[] strArr = addr.split(":");
//
//        BigInteger retValue = BigInteger.valueOf(0);
//        for (int i = 0; i < strArr.length; i++) {
//            BigInteger bi = new BigInteger(strArr[i], 16);
//            retValue = retValue.shiftLeft(16).add(bi);
//        }
//        return retValue;
//    }
    public static String numberToIPv6(BigInteger ipNumber) {
        String ipString = "";
        BigInteger a = new BigInteger("FFFF", 16);

        for (int i = 0; i < 8; i++) {
            ipString = ipNumber.and(a).toString(16) + ":" + ipString;

            ipNumber = ipNumber.shiftRight(16);
        }

        return ipString.substring(0, ipString.length() - 1);

    }

//    public static int countChar(String str, char reg) {
//        char[] ch = str.toCharArray();
//        int count = 0;
//        for (int i = 0; i < ch.length; ++i) {
//            if (ch[i] == reg) {
//                if (ch[i + 1] == reg) {
//                    ++i;
//                    continue;
//                }
//                ++count;
//            }
//        }
//        return count;
//    }
    public static boolean checkValidateIPv4(String fromIPAddress, String toIPAddress, int mask) {

        BigInteger fromIP = ipv4ToNumber(fromIPAddress);
        BigInteger toIP = ipv4ToNumber(toIPAddress);
        BigInteger subnet = new BigInteger("FFFFFFFF", 16);

        fromIP = fromIP.shiftRight(32 - mask).shiftLeft(32 - mask);
        subnet = subnet.shiftRight(mask);

        BigInteger broadcastIP = fromIP.xor(subnet);

        if (toIP.compareTo(broadcastIP) == 1) {
            return false;
        }

        return true;
    }

    public static boolean checkLengthIPV4numberRange(String fromIPAddress, String toIPAddress) {
        BigInteger fromIP = ipv4ToNumber(fromIPAddress);
        BigInteger toIP = ipv4ToNumber(toIPAddress);
        BigInteger limit = toIP.subtract(fromIP);
        if (limit.compareTo(new BigInteger(MAX_NUMBER_RANGE)) == 1) {
            return false;
        }
        return true;
    }

//    public static boolean checkValidateIPv6(String fromIPAddress, String toIPAddress, int mask) {
//
//        BigInteger fromIP = ipv6ToNumber(fromIPAddress);
//        BigInteger toIP = ipv6ToNumber(toIPAddress);
//        BigInteger limit = toIP.subtract(fromIP);
//        if (limit.compareTo(new BigInteger(MAX_NUMBER_RANGE)) == 1) {
//            return false;
//        }
//        BigInteger subnet = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
//
//        fromIP = fromIP.shiftRight(128 - mask).shiftLeft(128 - mask);
//        subnet = subnet.shiftRight(mask);
//
//        BigInteger broadcastIP = fromIP.xor(subnet);
//
//        if (toIP.compareTo(broadcastIP) == 1) {
//            return false;
//        }
//
//        return true;
//    }
    public static String safeStringToSearch(String input) {
        return input.replace("_", "\\_").replace("-", "\\-").replace("%", "\\%");
    }

    public static boolean isLongNumber(BigDecimal minCar) {
        try {
            Long.parseLong(minCar.toString());
            return true;
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * @param min
     * @param max
     * @return
     * @author minhvh1
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * @param number
     * @param pattern
     * @return
     * @author KhuongDV Ham format so thuc ve dang co max la 4 chu so thap phan.
     * Trim() so 0 vo nghia
     */
    public static String getFormattedString4Digits(String number, String pattern) {
        double amount = 0;
        try {
            amount = Double.parseDouble(number);
            DecimalFormat formatter = new DecimalFormat(pattern);
            return formatter.format(amount);
        } catch (Exception ex) {
            logger.debug(ex.getMessage(), ex);
            return number;
        }
    }

    public static Character safeToCharacter(Object value) {
        return safeToCharacter(value, '0');
    }

    public static Character safeToCharacter(Object value, Character defaulValue) {
        if (value == null) {
            return defaulValue;
        }
        return String.valueOf(value).charAt(0);
    }

    public static Collection<Long> strToCollectionLong(List<String> list) {
        Collection<Long> result = new ArrayList<>();
        if (list.isEmpty()) {
            return result;
        }
        for (String s : list) {
            result.add(DataUtil.safeToLong(s));
        }
        return result;
    }

    public static Collection<Long> objLstToLongLst(List<Object> list) {
        Collection<Long> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Object item : list) {
                result.add(safeToLong(item));
            }
        }
        return result;
    }

    public static Collection<Short> objLstToShortLst(List<Object> list) {
        Collection<Short> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Object item : list) {
                result.add(safeToShort(item));
            }
        }
        return result;
    }

    public static Collection<BigDecimal> objLstToBigDecimalLst(List<Object> list) {
        Collection<BigDecimal> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Object item : list) {
                result.add(safeToBigDecimal(item));
            }
        }
        return result;
    }

    public static Collection<Character> objLstToCharLst(List<Object> list) {
        Collection<Character> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (Object item : list) {
                result.add(item.toString().charAt(0));
            }
        }

        return result;
    }

    public static boolean isDelete(Character isDelete) {
        return isDelete != null && !DataUtil.isNullOrEmpty(String.valueOf(isDelete)) && Objects.equals(isDelete, 0);
    }

    /**
     * Check an object is active
     *
     * @param status status of object
     * @param isDelete isdetete status of object
     * @return
     */
    public static boolean isActive(Character status, Character isDelete) {
        return Objects.equals(status, '1') && (isDelete == null || isDelete.equals('0'));
    }

    public static <T> T getMapValue(Map<String, Object> params, String key, Class<T> type) {
        Object obj = params.get(key);
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isAssignableFrom(obj.getClass())) {
            return type.cast(obj);
        }

        return null;
    }

    public static <T> T nvl(T... objs) {
        for (T obj : objs) {
            if (obj != null) {
                return obj;
            }
        }

        return null;
    }

    public static String strNvl(String... objs) {
        for (String obj : objs) {
            if (!DataUtil.isNullOrEmpty(obj)) {
                return obj;
            }
        }

        return null;
    }

    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }

    public static String convertToDisplayName(String parType, String index) {
        return parType.replace('_', '.').toLowerCase() + "." + index;
    }

    //ChuDV: 10/05/2015
    public static Long convertDoubleToLong(Double value) {
        return value == null ? 0L : Long.parseLong(value.toString().replace(".0", ""));
    }

    public static List<String> parseInputList(String input) {
        return Splitter.on(",").trimResults().omitEmptyStrings().splitToList(input);
    }

    public static Long[] parseInputListLong(String input) {

        List<String> lstString = parseInputList(input);
        Long[] lstLong = new Long[lstString.size()];
        for (int i = 0; i < lstString.size(); i++) {
            lstLong[i] = (Long.parseLong(lstString.get(i)));
        }
        return lstLong;
    }

    public static Double[] parseInputListDouble(String input) {
        List<String> lstString = parseInputList(input);
        Double[] lstDouble = new Double[lstString.size()];
        for (int i = 0; i < lstString.size(); i++) {
            lstDouble[i] = (Double.parseDouble(lstString.get(i)));
        }
        return lstDouble;
    }

    public static String[] parseInputListString(String input) {
        List<String> lstString = parseInputList(input);
        String[] lstStringValue = new String[lstString.size()];
        for (int i = 0; i < lstString.size(); i++) {
            lstStringValue[i] = lstString.get(i);
        }
        return lstStringValue;
    }

    //Add by TruongBX3: 14/05/2015 - 
    public static Object cloneObject(Object obj) {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            return null;
        }
    }

    //
//    public static Map<String, GoodsDTO> putGoodsToMap(List<GoodsDTO> lstGoods) {
//        Map<String, GoodsDTO> mapGoodsDTO = new HashMap<>();
//        if (lstGoods != null) {
//            for (GoodsDTO goodsDTO : lstGoods) {
//                mapGoodsDTO.put(goodsDTO.getGoodsId(), goodsDTO);
//            }
//        }        
//        return mapGoodsDTO;               
//    }
    public static List<String> splitListFile(String strFiles) {
        List<String> lstFile = Lists.newArrayList();
        if (!isStringNullOrEmpty(strFiles)) {
            String lst[] = strFiles.split(";");
            lstFile = Arrays.asList(lst);
        }
        return lstFile;
    }

    public static List<String> splitListFile(String strFiles, String comma) {
        List<String> lstFile = Lists.newArrayList();
        if (StringUtil.isNullOrEmpty(comma)) {
            comma = ",";
        }
        if (!isStringNullOrEmpty(strFiles)) {
            String lst[] = strFiles.split(comma);
            lstFile = Arrays.asList(lst);
        }
        return lstFile;
    }

    public static List<String> splitListFileByComma(String strFiles) {
        List<String> lstFile = Lists.newArrayList();
        if (!isStringNullOrEmpty(strFiles)) {
            String lst[] = strFiles.split(",");
            lstFile = Arrays.asList(lst);
        }
        return lstFile;
    }

    public static String getColumnNameSQL(String columnName) {
        if (StringUtil.isNullOrEmpty(columnName)) {
            return columnName;
        }
        columnName = columnName.replaceAll("([a-z])([A-Z])", "$1_$2");
        return columnName;
    }

    public static boolean isFormatIpv4(String ip) {
        if (isNullOrEmpty(ip)) {
            return false;
        }
        return ip.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    }

    public static List<String> findWithRegexMultiline(String response, String regex, int indexGroup) throws Exception {
        List<String> listResult = new ArrayList<String>();
        try {
            if (response != null && !response.trim().isEmpty()) {
                Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.COMMENTS);
                Matcher m = p.matcher(response.trim());
                while (m.find()) {
                    listResult.add(m.group(indexGroup));
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return listResult;
    }

    public static String converListStringToString(List<String> lst, String f) {
        String res = "";
        if (lst != null && !lst.isEmpty()) {
            res = Arrays.toString(lst.toArray());
            res = res.substring(1, res.length() - 1);
            if (f != null) {
                res = res.replace(",", f);
            }
        }
        return res;

    }

    //ip subnet mac
    public static boolean checkIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        String ipNotSpace = ip.trim();
        String[] str = ipNotSpace.split("\\.");
        int num;
        if (".".equals(ipNotSpace.substring(ipNotSpace.length() - 1))) {
            return false;
        }
        if (str.length != 4) {
            return false;
        } else {
            for (int i = 0; i < str.length; i++) {
                try {
                    if (str[i].length() > 3) {
                        return false;
                    }
                    num = Integer.parseInt(str[i].toString().trim());
                    if (num < 0 || num > 255) {
                        return false;
                    }
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkSubnetMask(String subnetMask) {
        if (subnetMask == null || subnetMask.trim().isEmpty()) {
            return false;
        }
        try {
            if (subnetMask.trim().length() > 2) {
                return false;
            }
            int num = Integer.parseInt(subnetMask.trim());
            if (num < 0 || num > 32) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    private static long convertIpToInteger(String ip) throws Exception {
        long intIp = 0;
        if (ip == null) {
            throw new Exception("Dia chi IP bi null !");
        } else if (!isFormatIpv4(ip)) {
            if (ip.contains("/")) {
                SubnetUtils utils = new SubnetUtils(ip);
                ip = utils.getInfo().getLowAddress();
                String[] parts = ip.split("\\.");
                for (int i = 3; i >= 0; i--) {
                    intIp += Long.valueOf(parts[i].trim()) * (long) Math.pow(256, 3 - i);
                }
            } else {
                throw new Exception("IP " + ip + " khong dung dinh dang !");
            }
        } else {
            String[] parts = ip.split("\\.");
            for (int i = 3; i >= 0; i--) {
                intIp += Long.valueOf(parts[i].trim()) * (long) Math.pow(256, 3 - i);
            }
        }
        return intIp;
    }

    public static Range getRangeIp(String ipAndMask) throws Exception {
        String ipLower;
        String ipUpper;
        if (ipAndMask.endsWith("/32")) {
            ipAndMask = ipAndMask.replace("/32", "");
        }
        if (ipAndMask.contains("/")) {
            try {
                SubnetUtils utils = new SubnetUtils(ipAndMask);
                ipLower = utils.getInfo().getLowAddress();
            } catch (Exception e) {
                throw e;
            }
        } else {
            ipLower = ipAndMask;
        }

        if (ipAndMask.contains("/")) {
            try {
                SubnetUtils utils = new SubnetUtils(ipAndMask);
                ipUpper = utils.getInfo().getHighAddress();
            } catch (Exception e) {
                throw e;
            }
        } else {
            ipUpper = ipAndMask;
        }
        Long lgIpLower = convertIpToInteger(ipLower);
        Long lgIpUpper = convertIpToInteger(ipUpper);
        Range<Long> rs = Range.between(lgIpLower, lgIpUpper);
        return rs;

    }

    public static boolean exitContainsRangeInList(Range rage, List<Range> lst) {
        if (rage == null) {
            return false;
        }
        if (lst != null && !lst.isEmpty()) {
            for (Range bo : lst) {
                if (rage.containsRange(bo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean exitListContainsRange(List<Range> lst, Range rage) {
        if (rage == null) {
            return false;
        }
        if (lst != null && !lst.isEmpty()) {
            for (Range bo : lst) {
                if (bo.containsRange(rage)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getStringNumber(Long n) {
        if (n == null) {
            return "";
        }
        return getStringNumber(n.longValue());
    }

    public static String getStringNumber(Double n) {
        if (n == null) {
            return "";
        }
        return getStringNumber(n.doubleValue());
    }

    public static String getStringNumber(long n) {
//        DecimalFormat formatter = new DecimalFormat("#,###.00");
        DecimalFormat formatter = new DecimalFormat("#,###");
//        System.out.println(formatter.format(n));
        return formatter.format(n);
    }

    public static String getStringNumber(double n) {

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(n);
    }

    public static String convertVnToNormalText(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    private static Random random = new Random((new Date()).getTime());

    public static String generateRandomString(int length) {

        char[] values = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9'};

        String out = "";

        for (int i = 0;
                i < length;
                i++) {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    public static String generateRandomStringUpper(int length) {
        String out = generateRandomString(length).toUpperCase();
        return out;
    }

    public static void main(String args[]) throws Exception {

        StringBuffer x1=new StringBuffer("Hello");
        StringBuffer x2=x1.reverse();
        System.out.println(x2);
        String s = "abc";
        byte b[] = s.getBytes();
        ByteArrayInputStream obj1 = new ByteArrayInputStream(b);
        for (int i = 0; i < 2; i++) {
            int c;
            ByteArrayOutputStream obj2 = new ByteArrayOutputStream();
            while ((c = obj1.read()) != -1) {
                if (i == 0) {
                    System.out.print(Character.toUpperCase((char) c));
                    obj2.write(1);

                }
            }

            System.out.print(obj2);
        }
        
        
        
        ByteArrayOutputStream b2 = new ByteArrayOutputStream();

        b2.write(1);
        System.out.println(b2);
        b2.write(1);
        System.out.println(b2);
        b2.write(1);
        System.out.println(b2);
        StringBuffer s1 = new StringBuffer("Hello world");
        s1.insert(6, "good ");
        System.out.println(s1);
        /*
        
        System.out.println(convertVnToNormalText("Xin chào Việt Nam"));
        getStringNumber(1000000);
        List<String> lstStr = findWithRegexMultiline("ORA-02292: integrity constraint (QLTN.REFCAT_ITEM94) violated - child record found", " \\((\\w*\\d*\\.*)*\\)", 0);
        if (lstStr.size() > 0) {
            String[] arrs = lstStr.get(0).replaceAll("\\(", "")
                    .replaceAll("\\)", "").split("\\.");
            System.out.println(arrs[arrs.length - 1]);
        }

        String columnName = "pathSetup\n"
                + "servicesName\n"
                + "moduleStatus\n"
                + "reasonDisable\n"
                + "webServer\n"
                + "webServerName\n"
                + "note\n"
                + "purpose\n"
                + "username\n"
                + "verifyStatus\n"
                + "language\n"
                + "moduleName\n"
                + "realStatus\n"
                + "verifyTime\n"
                + "upTime\n"
                + "servicesCode\n"
                + "detail\n"
                + "function\n"
                + "createDate\n"
                + "moduleType\n"
                + "moduleTypeName\n"
                + "userTypeName\n"
                + "password\n"
                + "pathSetupSpecial\n"
                + "stopDate\n"
                + "userType\n"
                + "moduleCode\n"
                + "languageName\n"
                + "ipServer";
        System.out.println(columnName);
        columnName = columnName.replaceAll("([a-z])([A-Z])", "$1 $2");
        System.out.println("\n");
        System.out.println(columnName);
         */
    }

}
