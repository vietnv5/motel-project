package com.slook.persistence.common;


/**
 * @author Nguyễn Xuân Huy <huynx6@viettel.com.vn>
 * @version 1.0
 * @sin Apr 14, 2016
 */
public enum Condition
{
    LT("LT"), GT("GT"), EXAC("EXAC"), LE("LE"), GE("GE"), EXAC_IGNORE_CASE("EXAC_IGNORE_CASE"), LIKE_STRING("");
    public String value;

    private Condition(String value)
    {
        this.value = value;
    }

    public String normalizeValue()
    {
        if (value.isEmpty())
        {
            return "";
        }
        else
        {
            return "-" + value;
        }
    }

}
