package com.slook.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dungvv8 on 1/13/2017.
 */
public class StringUtil
{
    public static String toHtmlDisplay(String str)
    {
        if (str != null)
        {
            return str.replaceAll("\n", "</br>");
        }
        else
        {
            return null;
        }
    }

    //    vietnv14 start
    public static List<String> findWithRegexMultiline(String response, String regex, int indexGroup) throws Exception
    {
        List<String> listResult = new ArrayList<String>();
        try
        {
            if (response != null && !response.trim().isEmpty())
            {
                Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.COMMENTS);
                Matcher m = p.matcher(response.trim());
                while (m.find())
                {
                    listResult.add(m.group(indexGroup));
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return listResult;
    }

    public static boolean isNotNull(String value)
    {
        return value != null && value.trim().length() > 0;
    }

    public static boolean isNotNullAndNullStr(String value)
    {
        return value != null && value.trim().length() > 0 && !"null".equals(value);
    }

    public static String upperFirstChar(String input)
    {
        if (isNullOrEmpty(input))
        {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static boolean isNullOrEmpty(String obj1)
    {
        return (obj1 == null || "".equals(obj1.trim()));
    }

    public static String upperString(String input)
    {
        if (isNotNull(input))
        {
            input = input.toUpperCase();
        }
        return input;
    }

    public static String lowerString(String input)
    {
        if (isNotNull(input))
        {
            input = input.toLowerCase();
        }
        return input;
    }
//    vietnv end

}
