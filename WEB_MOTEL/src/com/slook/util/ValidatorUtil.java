package com.slook.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dungvv8 on 12/27/2016.
 */
public class ValidatorUtil implements Constant
{
    public static boolean isNotNull(String value)
    {
        return !(value == null || value.trim().length() <= 0);
    }

    public static boolean isInteger(String str)
    {
        return !(str == null || !str.matches(REGEX.INTEGER));
    }

    public static boolean isDecimal(String str)
    {
        return !(str == null || !str.matches(REGEX.DECIMAL));
    }

    public static boolean isNotSelect(Object value)
    {
        return !(value == null || "-1".equals(value.toString()));
    }

    public static boolean isContainSpecChar(String value)
    {
        return !(value == null || !value.matches("[^A-Za-z0-9]"));
    }

    public static boolean isValidIPAddress(String value)
    {
        Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static boolean isMatchRegex(String value, String regex)
    {
        return value.matches(regex);
    }
}
