package com.slook.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;

import com.slook.util.Config;
import com.slook.util.Util;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.StreamedContent;


@ManagedBean
@RequestScoped
public class ImageStreamer
{


    public StreamedContent getImage() throws IOException
    {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
        {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else
        {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

            Object previewProfile = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("previewProfile");
            if (previewProfile != null)
            {
                if (previewProfile instanceof DefaultUploadedFile)
                {
                    return getPreviewImage(((DefaultUploadedFile) previewProfile).getInputstream());
                }
                else
                {
                    return new DefaultStreamedContent(new ByteArrayInputStream((byte[]) previewProfile));
                }
            }

            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String folderPath = request.getSession().getServletContext().getRealPath("resources");
            String defaultImage = folderPath + File.separator + "olympos-layout" + File.separator + "images" + File.separator + "avatar1.svg";
            String sex = context.getExternalContext().getRequestParameterMap().get("sex");
            if (sex != null && sex.equals("0"))//Avatar nữ mặc định
            {
                defaultImage = folderPath + File.separator + "olympos-layout" + File.separator + "images" + File.separator + "avatar2.svg";
            }
            String fileName = context.getExternalContext().getRequestParameterMap().get("fileName");
            String file = Util.getUploadFolder(Config.PROFILE_IMAGE_FOLDER) + File.separator + fileName;

            try
            {

                Path path = Paths.get(file);
                byte[] buf = Files.readAllBytes(path);
                return new DefaultStreamedContent(new ByteArrayInputStream(buf));
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                try
                {
//                    defaultImage = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\temp\\upload_2a8bf2a8_7df6_4e3d_8bb5_c8c8d1b3a266_00000083.tmp";
                    Path path = Paths.get(defaultImage);

                    byte[] buf = Files.readAllBytes(path);
                    return new DefaultStreamedContent(new ByteArrayInputStream(buf), "image/svg+xml");
                }
                catch (Exception e1)
                {
                    //e.printStackTrace();

                }
            }

        }
        return new DefaultStreamedContent();
    }

    public StreamedContent getPreviewImage(InputStream inputStream) throws IOException
    {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
        {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else
        {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            return new DefaultStreamedContent(inputStream);
        }
    }


}
