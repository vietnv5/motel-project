package com.slook.controller;


import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;

/**
 * Created by Thanh Pham on 07/07/2017.
 */
@ManagedBean(name = "imageStreamerBill")
@ApplicationScoped
public class ImageStreamerBill {

    private static final Logger logger = getLogger(ImageStreamerBill.class);

    @PostConstruct
    public void init() {
    }

    public StreamedContent getImage() throws IOException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();

            if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                return new DefaultStreamedContent();
            } else {
                String imgPath = params.get("imgPath");
                Path path = Paths.get(imgPath);
                byte[] buf = Files.readAllBytes(path);
                return new DefaultStreamedContent(new ByteArrayInputStream(buf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent();
    }

    public static void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++) {
                if (m.stringWidth(currentLine + words[i]) < lineWidth) {
                    currentLine += " " + words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }

}
