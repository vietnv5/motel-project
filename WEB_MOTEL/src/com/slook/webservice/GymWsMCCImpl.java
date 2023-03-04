/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.webservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slook.model.DoorAccessStatus;
import com.slook.model.Member;
import com.slook.persistence.MemberService;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.DataUtil;
import com.slook.util.StringUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author VietNV
 */
public class GymWsMCCImpl
{

    Client client;
    WebResource webResource;
    Gson gson;
    protected static final Logger logger = LoggerFactory.getLogger(GymWsMCCImpl.class);
    private static GymWsMCCImpl restClient;
    int timeOut = 1000;//1s
    String ipSourceFingerprint = "";

    public static GymWsMCCImpl getInstance()
    {
        if (restClient == null)
        {
            restClient = new GymWsMCCImpl();
        }
        return restClient;
    }

    public GymWsMCCImpl()
    {
        client = Client.create();
        try
        {
            String timeOutStr = DataConfig.getConfigByKey("TIME_OUT");
            timeOut = Integer.valueOf(timeOutStr);
        }
        catch (Exception e)
        {
        }

        client.setConnectTimeout(timeOut);
        webResource = client.resource(DataConfig.getConfigByKey("LINK_SERVER_SERVICES"));
//        webResource = client.resource("http://localhost:53984/TutorialService.svc");
        gson = new Gson();
        // LAY IP NGUON 
        try
        {
            ipSourceFingerprint = DataConfig.getConfigByKey("IP_SOURCE_FINGERPRINT");
//            if (!StringUtil.isNotNullAndNullStr(ipSourceFingerprint)) {
//                LogActionController.getClientIpAddr(getRequest());
//            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }

    }

    //type=1 using card
    public List<DoorAccessStatus> userAccess(List<String> lstIP, String cardCode, int typeMethod)
    {
        String lstIpStr = "";
        if (lstIP != null && lstIP.size() > 0)
        {
//            for (String ip : lstIP) {
//                lstIpStr += ip;
//            }
            lstIpStr = gson.toJson(lstIP);
        }
        lstIpStr = lstIpStr.replaceAll("\"", "\\\\\"");
        List<DoorAccessStatus> lst = new ArrayList<DoorAccessStatus>();
        String param = "{\"listIp\":\"" + lstIpStr + "\",\"cardCode\":\"" + cardCode + "\",\"action\":" + typeMethod + "}";
        String path = "/gym/modifyUserOfDevice";
        String key = "modifyUserOfDeviceResult";

        WebResource getReasonCountRs = webResource.path(path);

        ClientResponse response = getReasonCountRs.type("application/json;charset=utf-8 ").post(ClientResponse.class, param);
        String resData = response.getEntity(String.class);
        System.out.println("ket qua:" + resData);
        JSONObject root = new JSONObject(resData);

        String j1 = root.getString(key);
        System.out.println("data:" + j1);

        lst = gson.fromJson(j1, new TypeToken<List<DoorAccessStatus>>()
        {
        }.getType());

        return lst;
    }

    //type=2 using fingerprint
    public List<DoorAccessStatus> userAccessFingerprint(List<String> lstIP, String userIdStr, int typeMethod)
    {
        String lstIpStr = "";
        if (lstIP != null && lstIP.size() > 0)
        {
//            for (String ip : lstIP) {
//                lstIpStr += ip;
//            }
            lstIpStr = gson.toJson(lstIP);
        }
        lstIpStr = lstIpStr.replaceAll("\"", "\\\\\"");
        List<DoorAccessStatus> lst = new ArrayList<DoorAccessStatus>();
        String param = "{\"listIp\":\"" + lstIpStr + "\",\"cardCode\":\"" + userIdStr + "\",\"action\":" + typeMethod
                + ",\"type\":" + 2
                + ",\"ipSourceFingerprint\":\"" + ipSourceFingerprint + "\""
                + "}";
        String path = "/gym/modifyUserOfDevice";
        String key = "modifyUserOfDeviceResult";

        WebResource getReasonCountRs = webResource.path(path);

        ClientResponse response = getReasonCountRs.type("application/json;charset=utf-8 ").post(ClientResponse.class, param);
        String resData = response.getEntity(String.class);
        System.out.println("ket qua:" + resData);
        JSONObject root = new JSONObject(resData);

        String j1 = root.getString(key);
        System.out.println("data:" + j1);

        lst = gson.fromJson(j1, new TypeToken<List<DoorAccessStatus>>()
        {
        }.getType());

        //check neu xoa van tay tren may chu
        if (lstIP != null && lstIP.contains(ipSourceFingerprint) && Constant.WS_C_METHOD.REMOVE_ACCESS == typeMethod)
        {
            try
            {

                //goi thread lay van tay de cap nhat
                WorkThreadUpdateDataFingerprint wtFinger = new WorkThreadUpdateDataFingerprint();
                wtFinger.setUserId(userIdStr);
                wtFinger.start();
            }
            catch (Exception e)
            {
            }
        }
        return lst;
    }

    public DoorAccessStatus registerUserFingerprint(String userId, String customerName)
    {

        customerName = DataUtil.convertVnToNormalText(customerName);
        List<DoorAccessStatus> lst = new ArrayList<DoorAccessStatus>();
        DoorAccessStatus result = new DoorAccessStatus();
//        String param = "{\"listIp\":\"" + lstIpStr + "\",\"cardCode\":\"" + cardCode + "\",\"action\":" + typeMethod 
//                +  "\",\"type\":" + 2
//                  +  "\",\"ipSourceFingerprint\":\"" + ipSourceFingerprint + "\""
//                + "}";
        String param = "{\"ip\":\"" + ipSourceFingerprint + "\",\"name\":\"" + customerName + "\", \"cardNumber\":\"" + userId + "\", \"password\":\"\"}";
        String path = "/gym/registerUserFingerprint";
        String key = "registerUserFingerprintResult";

        WebResource getReasonCountRs = webResource.path(path);

        ClientResponse response = getReasonCountRs.type("application/json;charset=utf-8 ").post(ClientResponse.class, param);
        String resData = response.getEntity(String.class);
        System.out.println("ket qua:" + resData);
        JSONObject root = new JSONObject(resData);

        String j1 = root.getString(key);
        System.out.println("data:" + j1);

//        lst = gson.fromJson(j1, new TypeToken<List<DoorAccessStatus>>() {
//        }.getType());
        result = gson.fromJson(j1, new TypeToken<DoorAccessStatus>()
        {
        }.getType());
        result.setType(2l);

        try
        {

            //goi thread lay van tay de cap nhat
            WorkThreadUpdateDataFingerprint wtFinger = new WorkThreadUpdateDataFingerprint();
            wtFinger.setUserId(userId);
            wtFinger.start();
        }
        catch (Exception e)
        {
        }

        return result;
    }

    public List<DoorAccessStatus> deleteFullUserFinger(List<String> lstIPDoor, String userId)
    {
        List<String> lstIP = Arrays.asList(ipSourceFingerprint);
        List<DoorAccessStatus> lst1 = userAccessFingerprint(lstIPDoor, userId, Constant.WS_C_METHOD.REMOVE_ACCESS);
        lst1.addAll(userAccessFingerprint(lstIP, userId, Constant.WS_C_METHOD.REMOVE_ACCESS));
        return lst1;
    }

    public DoorAccessStatus getInforUserFingerprint(String userId)
    {
        DoorAccessStatus res = new DoorAccessStatus();
        try
        {

            List<String> lstip = Arrays.asList(ipSourceFingerprint);
            List<DoorAccessStatus> lst = userAccessFingerprint(lstip, userId, Constant.WS_C_METHOD.GET_LIST_ACCESS);
            if (lst != null && lst.size() > 0)
            {
                res = lst.get(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    private class WorkThreadUpdateDataFingerprint extends Thread
    {

        private String userId;

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        @Override
        public void run()
        {
            try
            {
                //wait thread for user put fingerprint  doi 60s lay van tay nguoi dung nhap vao
                for (int i = 0; i < 2; i++)
                {

                    Thread.sleep(60000);
                    DoorAccessStatus d = getInforUserFingerprint(userId);
                    Member m = MemberService.getInstance().findById(Long.valueOf(userId));
                    m.setFingerprintPath(d.getData());
                    MemberService.getInstance().saveOrUpdate(m);
                    if (StringUtil.isNotNullAndNullStr(d.getData()))
                    {
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {

        Gson gson = new Gson();
        List<String> lstip = Arrays.asList("192.168.1.201");
        System.out.println(gson.toJson(lstip));
        DoorAccessStatus bo = new DoorAccessStatus();
        bo.setIp("12.1.1.1");
        bo.setCardCode("abc123");
        List<DoorAccessStatus> lstd = new ArrayList<DoorAccessStatus>();
        lstd.add(bo);
        String weekReportFormStr = gson.toJson(lstd);
        System.out.println("weekReportFormStr:" + weekReportFormStr);
        List<DoorAccessStatus> d = gson.fromJson(weekReportFormStr, new TypeToken<List<DoorAccessStatus>>()
        {
        }.getType());
        GymWsMCCImpl ws = GymWsMCCImpl.getInstance();
        // ws.registerUserFingerprint("69","Nguyen Van Viet");
        ws.userAccessFingerprint(lstip, "10", Constant.WS_C_METHOD.GET_LIST_ACCESS);
//        ws.userAccess(lstip, "", Constant.WS_C_METHOD.GET_LIST_ACCESS);

    }
}
