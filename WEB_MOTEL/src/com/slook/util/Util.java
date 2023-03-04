package com.slook.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class chua cac ham tien ich dung chung cua ca project
 *
 * @author xuanh
 */
@ManagedBean
public class Util
{

    static ExternalContext externalContext;
    static File TOMCAT_DIR;
    static File TEMP_DIR;
    //	static File RESOURCES_DIR;
    static File RESOURCES_OTHER_DIR;
    protected static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static File getTempDir()
    {
        return TEMP_DIR;
    }

    static
    {
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        TOMCAT_DIR = new File(((ServletContext) externalContext.getContext())
                .getRealPath("")).getParentFile().getParentFile();    //...../tomcat
        TEMP_DIR = new File(TOMCAT_DIR.getPath() + File.separator + "temp"); //...../tomcat/temp
        TEMP_DIR.mkdirs();
//		RESOURCES_DIR = new File(((ServletContext) externalContext.getContext())
//				.getRealPath("resources"));	//...../resources
//vietnv sua thanh su dung thu muc co dinh
        RESOURCES_OTHER_DIR = new File(new File(((ServletContext) externalContext.getContext())
                .getRealPath("")).getParentFile().getParentFile().getParentFile().getPath() + File.separator + "resources");    //...../resources
    }

    /**
     * Lay gia tri ip cua client.
     */
    public static String getClientIp()
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) context.getRequest();

        return req.getRemoteHost();
    }

    public static String getUploadFolder(String handleFolder)
    {
        String dir = RESOURCES_OTHER_DIR + File.separator + Config.ROOT_FOLDER_DATA + File.separator + handleFolder;
        new File(dir).mkdirs();
        return dir;
    }

    public static boolean storeFile(String handleFoder, UploadedFile fileUpload, String prefixImgPath)
    {
        File file = new File(getUploadFolder(handleFoder) + File.separator + prefixImgPath + fileUpload.getFileName());
        FileOutputStream out = null;

        try
        {
            out = new FileOutputStream(file);
            out.write(fileUpload.getContents());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return false;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return true;
    }

    public static String normalizeParamCode(String paramCode)
    {
        if (paramCode != null)
        {
            return paramCode.replace("(", "").replace(")", "").replace("[", "").replace("]", "");
        }
        return null;
    }

    public static LinkedList<String> splitLog2Line(Object log, String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        String newLog = "";
        if (log instanceof String)
        {
            newLog = (String) log;
        }
        else if (log instanceof byte[])
        {
            newLog = new String((byte[]) log);
        }
        LinkedList<String> lstData = new LinkedList<String>(Arrays.asList(pattern.split(newLog)));
        return lstData;
    }

    /**
     * Convert ky tu co dau sang khong dau
     *
     * @param in
     * @return
     */
    public static String unicode2Ascii(String in)
    {
        String s1 = Normalizer.normalize(in, Normalizer.Form.NFD);
        String regex = "[\\p{Block=CombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

        try
        {
            String s2 = s1.replaceAll("Đ", "D");
            s2 = s2.replaceAll("đ", "d");
            s2 = new String(s2.replaceAll(regex, "").getBytes("ascii"), "ascii");
            return s2;
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error(e.getMessage(), e);
            return in;
        }

    }

    public static String createCode(String s)
    {
        String result = "";
        if (s != null)
        {
            for (String s1 : s.split("\\s", -1))
            {
                if (!s1.isEmpty())
                {
                    result += s1.substring(0, 1);
                }
            }
        }
        return result.toUpperCase();
    }

    public static String getRealPath(String path)
    {
        String rs = "";
        String rsP1 = "";
        try
        {
            rsP1 = new File(((ServletContext) externalContext.getContext())
                    .getRealPath(path)).getPath();

        }
        catch (Exception e)
        {
        }
        if (StringUtil.isNullOrEmpty(rsP1))
        {
            File CUR_DIR = new File(((ServletContext) externalContext.getContext())
                    .getRealPath(""));
            if (StringUtil.isNotNull(path))
            {
                String[] arr = path.split("\\.\\./");
                int length = arr.length;
                for (int i = 1; i < length; i++)
                {
                    CUR_DIR = CUR_DIR.getParentFile();
                }
                rs = CUR_DIR.getPath() + File.separator + arr[length - 1];
            }
            else
            {
                rs = CUR_DIR.getPath();
            }
        }
        else
        {
            rs = rsP1;
        }
        return rs;
    }

}
