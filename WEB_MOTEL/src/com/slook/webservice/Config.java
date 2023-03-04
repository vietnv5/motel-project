package com.slook.webservice;

/**
 * Created by xuanh on 6/10/2017.
 */
public class Config
{

    interface STATUS
    {
        Long ENABLE = 1L;
        Long DISABLE = 0L;
        Long GET_DATA = 1L;
        Long DO_NOT_GET_DATA = 0L;
        Long PUSH_DATA_SUCCESS = 1L;
        Long IS_NOT_PUSH_DATA = 0L;
        Long PROCESSING = 2L;
    }


    enum DATA_TYPE
    {
        CHECK_IN("0"),
        USER("1"),
        MACHINE("2");
        String value;

        DATA_TYPE(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public static DATA_TYPE fromString(String str)
        {
            for (DATA_TYPE b : DATA_TYPE.values())
            {
                if (b.value.equalsIgnoreCase(str))
                {
                    return b;
                }
            }
            return null;
        }
    }
}
