package com.slook.util;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.apache.log4j.Logger.getLogger;


public class FileHelper
{

    private static final Logger logger = getLogger(FileHelper.class);

    /**
     * Su dung cho primefaces.
     */
    public static String uploadFile(String folderStore, UploadedFile fileUpload, String fileName)
    {
        if (fileUpload == null)
        {
            return "FALSE";
        }

        OutputStream outputStream;
        try
        {
            outputStream = getOutputStream(folderStore, fileName);
            outputStream.write(fileUpload.getContents());
            outputStream.close();
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            return "FALSE";
        }

        return "SUCCESS";
    }

    /**
     * Get output stream
     */
    public static OutputStream getOutputStream(String folderStore, String fileName)
    {
        OutputStream outputStream = null;
        try
        {
            File fileToCreate = new File(folderStore);
            if (!fileToCreate.exists())
            {

                fileToCreate.mkdirs();
            }

            fileToCreate = new File(folderStore, fileName);
            outputStream = new FileOutputStream(fileToCreate);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }

        return outputStream;
    }

    /**
     * Remove file
     */
    public static Boolean removeFile(String folderStore)
    {
        try
        {
            File fileToRemove = new File(folderStore);
            if (fileToRemove.exists())
            {

                if (fileToRemove.delete())
                {

                }
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }
}
