package com.slook.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.motel.model.CatService;
import com.motel.model.Room;
import com.slook.persistence.CatServiceServiceImpl;
import com.slook.persistence.RoomServiceImpl;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author VietNV Jun 10, 2018
 */
public class XMLEncoderDecoderExample {

    static String pathFile = "resources/datas/";

    public static void main(String[] args) throws IOException {
        pathFile = "WebContent/resources/datas1/";
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            filter.put("groupUserId", 1L);

            List lstService = CatServiceServiceImpl.getInstance().findList(filter);
            serializeToXMLObject(lstService, pathFile);
            List lstRes = (List) deserializeFromXMLObject(CatService.class, pathFile);
            System.out.println("lstRes:" + lstRes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            filter.put("home.groupUserId", 1L);

            List lstService = RoomServiceImpl.getInstance().findList(filter);
            serializeToXMLObject(lstService, pathFile);
            List lstRes = (List) deserializeFromXMLObject(Room.class, pathFile);
            System.out.println("lstRes room:" + lstRes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializeToXMLObject(Object settings, String pathFile) throws IOException {
        String fileName = "settings.xml";
        if (settings instanceof List) {
            if (settings != null && !((List) settings).isEmpty()) {
                fileName = ((List) settings).get(0).getClass().getSimpleName() + ".xml";
            } else {
                fileName = settings.getClass().getName() + ".xml";
            }
        }
        FileOutputStream fos = new FileOutputStream(pathFile + fileName);
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            public void exceptionThrown(Exception e) {
                System.out.println("Exception! :" + e.toString());
            }
        });
        encoder.writeObject(settings);
        encoder.close();
        fos.close();
    }

    public static Object deserializeFromXMLObject(Class clazz, String pathFile) throws IOException {
        String fileName = clazz.getSimpleName() + ".xml";
        FileInputStream fis = new FileInputStream(pathFile+File.separator + fileName);
        XMLDecoder decoder = new XMLDecoder(fis);
        Object decodedSettings = decoder.readObject();
        decoder.close();
        fis.close();
        return decodedSettings;
    }

    private static void serializeToXMLObject(Object settings) throws IOException {
        serializeToXMLObject(settings, Util.getRealPath(pathFile));
    }

    public static Object deserializeFromXMLObject(Class clazz) throws IOException {
        return deserializeFromXMLObject(clazz, Util.getRealPath(pathFile));
    }
}
