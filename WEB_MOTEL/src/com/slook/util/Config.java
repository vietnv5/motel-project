package com.slook.util;

import java.io.File;

public class Config
{
    // Trang error.
    public static final String _ERROR_PAGE = "/error";
    // Doi tuong UserToken trong du lieu VSA tra ve.
    public static final String _USER_TOKEN = Constant._USER_TOKEN;
    ;
    // Config default url cho ung dung.
    // Neu khong dung default thi gan gia tri ve ""
    public static final String _DEFAULT_URL = "/manager/example";
    public static final String PATH_OUT = File.separator + ".." + File.separator + ".." + File.separator + "report_out" + File.separator;
    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String ROOT_FOLDER_DATA = "datas";
    public static final String PROFILE_IMAGE_FOLDER = "profiles";

}