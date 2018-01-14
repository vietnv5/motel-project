//package com.slook.webservice;
//
//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
//import com.google.common.base.Function;
//import com.google.common.base.Joiner;
//import com.google.common.base.Splitter;
//import com.google.common.collect.Lists;
//import com.google.gson.GsonBuilder;
//import com.slook.model.CatMachine;
//import com.slook.model.MemberCheckin;
//import com.slook.persistence.GenericDaoImplNewV2;
//import com.slook.persistence.GenericDaoServiceNewV2;
//import com.slook.util.Constant;
//import com.slook.util.DateTimeUtils;
//import com.slook.util.HibernateUtil;
//import com.slook.webservice.object.AuthorityBO;
//import com.slook.webservice.object.CfgWsDataType;
//import com.slook.webservice.object.JsonResponseBO;
//import com.slook.webservice.object.MachineLog;
//import org.apache.log4j.Logger;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Restrictions;
//
//import javax.annotation.Resource;
//import javax.jws.WebService;
//import javax.xml.ws.WebServiceContext;
//import java.io.StringWriter;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created by xuanh on 6/10/2017.
// */
//@WebService(endpointInterface = "com.slook.webservice.GymWebservice")
//public class GymWebserviceImpl implements GymWebservice {
//
//    public static final String FORMAT_DATE = "yyyyMMdd HH:mm:ss";
//    private Logger logger = Logger.getLogger(GymWebserviceImpl.class);
//    GenericDaoServiceNewV2<CfgWsDataType,Long> cfgWsDataTypeService = new GenericDaoImplNewV2<CfgWsDataType,Long>() {};
//    @Resource
//    WebServiceContext wsContext;
//
//    @Override
//    public JsonResponseBO checkDataStatus(AuthorityBO authorityBO, String type, Long requestId) {
//        String code = Thread.currentThread().getStackTrace()[1].getMethodName();
//        logger.info("Begin function " + code + "[ReqId=" + authorityBO.getRequestId() + "]");
//        JsonResponseBO response = null;
//        try {
//            JsonResponseBO checkAuthen = isAuthenticated(authorityBO.getUserName(), authorityBO.getPassword());
//            if (checkAuthen != null)
//                return checkAuthen;
//            response = new JsonResponseBO();
//            response.setReceiverTime(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
//            List<CfgWsDataType> listCfgRun = getCfgWebServiceRun(Long.valueOf(type), Config.STATUS.GET_DATA);
//            List<CatMachine> listMachine = getListMachine(Config.STATUS.ENABLE);
//            String machines = Joiner.on(",").join(Lists.transform(listMachine, new Function<CatMachine, String>() {
//                @Override
//                public String apply(CatMachine catMachine) {
//                    return catMachine.getIp();
//                }
//            }));
//            Map<String, Object> mapDataJson = new HashMap<>();
//            if (listCfgRun != null && listCfgRun.size() == 1) {
//                CfgWsDataType cfgRun = listCfgRun.get(0);
//                cfgRun.setRequestId(authorityBO.getRequestId().longValue());
//                cfgRun.setStatus(Config.STATUS.PROCESSING);
//                new GenericDaoImplNewV2<CfgWsDataType, Long>() {
//                }.merge(cfgRun);
//                mapDataJson.put("request_Id", requestId);
//                mapDataJson.put("status", Config.STATUS.GET_DATA);
//                mapDataJson.put("list_Machine", machines);
//            } else {
//                mapDataJson.put("request_Id", requestId);
//                mapDataJson.put("status", Config.STATUS.DO_NOT_GET_DATA);
//                mapDataJson.put("list_Machine", machines);
//            }
//
//            List<Map<String, Object>> listDataJson = new ArrayList<>();
//            listDataJson.add(mapDataJson);
//            String content = createDataJson(listDataJson);
//            response.setDataJson(content);
//
//            //Thoi gian xong
//            response.setSendTime(new SimpleDateFormat(FORMAT_DATE).format(new Date()));
//        } catch (Exception ex) {
//            response.setStatus(1);
//            String detailError = ex.toString() +
//                    (ex.getCause() != null ? "\nCaused by: " + ex.getCause().toString() : "");
//            response.setDetailError(detailError);
//            logger.error(ex, ex);
//        } finally {
//            logger.info("[" + code + "] End function");
//        }
//        return response;
//    }
//
//
//    @Override
//    public JsonResponseBO saveDataToDatabase(AuthorityBO authorityBO, String type, Long requestId, String content) {
//        String code = Thread.currentThread().getStackTrace()[1].getMethodName();
//        logger.info("Begin function " + code + "[ReqId=" + authorityBO.getRequestId() + "]");
//        JsonResponseBO response = null;
//        try {
//            JsonResponseBO checkAuthen = isAuthenticated(authorityBO.getUserName(), authorityBO.getPassword());
//            if (checkAuthen != null)
//                return checkAuthen;
//            Map<String, Object> mapDataJson = new HashMap<>();
//            response = new JsonResponseBO();
//            response.setReceiverTime(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
//            updateDataStatus(requestId, Config.STATUS.DO_NOT_GET_DATA);
//            Config.DATA_TYPE dataType = Config.DATA_TYPE.fromString(type);
//            List<String> list = Splitter.on("$").splitToList(content);
//            switch (dataType) {
//                case CHECK_IN:
//                    List<MemberCheckin> listSuccess = getListFromContent(list);
//                    saveCheckIn(listSuccess);
//                    break;
//                case USER:
//                    break;
//                case MACHINE:
//                    List<MachineLog> machineLogs = new ArrayList<>();
//                    for (String str : list) {
//                        String[] arr = str.split("\\|");
//                        MachineLog machineLog = new MachineLog();
//                        machineLog.setMachineId(Long.valueOf(arr[0]));
//                        machineLog.setIp(arr[1]);
//                        machineLog.setPort(arr[2]);
//                        machineLog.setStatus(Long.valueOf(arr[3]));
//
//                        machineLogs.add(machineLog);
//                    }
//                    saveMachine(machineLogs);
//                    break;
//            }
//
//            mapDataJson.put("request_Id", requestId);
//            List<Map<String, Object>> listDataJson = new ArrayList<>();
//            listDataJson.add(mapDataJson);
//            String contentData = createDataJson(listDataJson);
//            response.setDataJson(contentData);
//
//            //Thoi gian xong
//            response.setSendTime(new SimpleDateFormat(FORMAT_DATE).format(new Date()));
//        }catch (Exception ex) {
//            response.setStatus(1);
//            String detailError = ex.toString() +
//                    (ex.getCause() != null ? "\nCaused by: " + ex.getCause().toString() : "");
//            response.setDetailError(detailError);
//            logger.error(ex, ex);
//        } finally {
//            logger.info("[" + code + "] End function");
//        }
//        return response;
//    }
//
//    private List<MemberCheckin> getListFromContent(List<String> list) {
//            List<MemberCheckin> empCheckIns = new ArrayList<>();
//            try {
//                Map<String, MemberCheckin> map = new HashMap<>();
//                for (String str : list) {
//                    String[] arr = str.split("\\|");
//                    Date date = DateTimeUtils.parse(arr[1], FORMAT_DATE);
//                    Date checkInDate = DateTimeUtils.trunc(date);
//                    String in1 = DateTimeUtils.format(date, "HH:mm");
//                    String checkInCode = arr[0];
//                    String key = checkInCode + "_" + DateTimeUtils.formatDateCommon(checkInDate);
//
//                    MemberCheckin empCheckIn;
//                    if (map.containsKey(key)) {
//                        empCheckIn = map.get(key);
//                        in1 = empCheckIn.getIn1() + "," + in1;
//                        empCheckIn.setIn1(in1);
//                    } else {
//                        empCheckIn = new MemberCheckin();
//                        empCheckIn.setCheckInCode(checkInCode);
//                        empCheckIn.setCheckInDate(checkInDate);
//                        empCheckIn.setIn1(in1);
////                    empCheckIns.add(empCheckIn);
//                    }
//                    map.put(key, empCheckIn);
//                }
//                for (Map.Entry<String, EmpCheckIn> entry : map.entrySet()) {
//                    empCheckIns.add(entry.getValue());
//                }
//                //xu ly in1
//                for (EmpCheckIn empCheckIn : empCheckIns) {
//                    String in1 = empCheckIn.getIn1();
//                    logger.info(in1);
//                    List<String> listTime = new ArrayList(Arrays.asList(in1.split(",")));
//                    Collections.sort(listTime, new Comparator<String>() {
//                        @Override
//                        public int compare(String o1, String o2) {
//                            Date _o1 = DateTimeUtils.parse(o1, "HH:mm");
//                            Date _o2 = DateTimeUtils.parse(o2, "HH:mm");
//                            return _o1.compareTo(_o2);
//                        }
//                    });
//                    Date max = null;
//                    for (Iterator<String> iterator = listTime.iterator(); iterator.hasNext(); ) {
//                        String time = iterator.next();
//                        Date _time = DateTimeUtils.parse(time, "HH:mm");
//                        if (max == null) {
//                            max = _time;
//                        } else {
//                            if (_time.getTime() - max.getTime() < 2 * 60 * 1000) {
//                                iterator.remove();
//                            } else {
//                                max = _time;
//                            }
//                        }
//                    }
//                    empCheckIn.setIn1(Joiner.on(",").join(listTime));
//                }
//            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
//                throw ex;
//            }
//            return empCheckIns;
//    }
//
//    private void saveMachine(List<MachineLog> machineLogs) {
//        Session session = null;
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            for (MachineLog machineLog : machineLogs) {
//                CatMachine catMachine = (CatMachine) session.get(CatMachine.class, machineLog.getMachineId());
//                if (catMachine == null) {
//                    catMachine = new CatMachine();
//                }
//                catMachine.setMachineId(machineLog.getMachineId());
//                catMachine.setIp(machineLog.getIp());
//                catMachine.setPort(machineLog.getPort());
//                catMachine.setStatus(machineLog.getStatus());
//
//                session.saveOrUpdate(catMachine);
//            }
//            session.getTransaction().commit();
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            throw ex;
//        } finally {
//            if(session!=null)
//                session.close();
//        }
//    }
//
//    @Override
//    public JsonResponseBO getSaveDataTimekeeper(AuthorityBO authorityBO, Long requestId) {
//        return null;
//    }
//
//    @Override
//    public JsonResponseBO checkSaveDataTimekeeper(AuthorityBO authorityBO, Long requestId, Long status, String description) {
//        return null;
//    }
//    public void updateDataStatus(Long requestId, Long status) throws Exception {
//
//        try {
//            Map<String, Object> filter = new HashMap<>();
//            filter.put("requestId", requestId);
//            GenericDaoImplNewV2<CfgWsDataType, Long> cfgWsDataTypeService = new GenericDaoImplNewV2<CfgWsDataType, Long>() {
//            };
//            List<CfgWsDataType> list = cfgWsDataTypeService.findList(filter);
//            if (list.size()==1){
//                CfgWsDataType cfgRun = list.get(0);
//                cfgRun.setStatus(status);
//                cfgRun.setUpdateTime(new Date());
//                cfgWsDataTypeService.merge(cfgRun);
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//            throw ex;
//        } finally {
//        }
//    }
//
//    protected JsonResponseBO isAuthenticated(String userName, String password) {
//        try{
//            String userConfig = ResourceBundle.getBundle("config").getString("usernameWS");
//            String passConfig = ResourceBundle.getBundle("config").getString("passwordWS");
//            if (userConfig!=null && userConfig.equals(userName)
//                    && passConfig!=null && passConfig.equals(password))
//                return null;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        JsonResponseBO response = new JsonResponseBO();
//        response.setReceiverTime(new SimpleDateFormat(FORMAT_DATE).format(new Date()));
//        response.setStatus(1);
//        response.setDetailError("User/Pass không đúng");
//        return response;
//    }
//    protected List<CfgWsDataType> getCfgWebServiceRun(Long type, Long status) {
//        Map<String, Object> filter = new HashMap<>();
//        try {
//            filter.put("type", type);
//            filter.put("status", status);
//            return cfgWsDataTypeService.findList(filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }
//    private List<CatMachine> getListMachine(Long status) {
//        try {
//            Map<String, Object> filter = new HashMap<>();
//            filter.put("status", status);
//            return new GenericDaoImplNewV2<CatMachine,Long>(){}.findList(filter);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }
//
//    private String createDataJson(List<Map<String, Object>> lst) throws Exception {
//        SimpleDateFormat formatDateJson = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        StringWriter sw = new StringWriter();
//        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(sw);
//        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
//        jsonGenerator.writeStartObject();
//        jsonGenerator.writeArrayFieldStart("data");
//        for (Map<String, Object> row : lst) {
//            jsonGenerator.writeStartObject();
//            for (String key : row.keySet()) {
//                String column = key.toLowerCase();
//                if (row.get(key) != null) {
//                    Object value = row.get(key);
//                    try {
//                        if (value instanceof String) {
//                            jsonGenerator.writeStringField(column, value.toString());
//                        } else if (value instanceof Timestamp) {
//                            Timestamp time = (Timestamp) value;
//                            java.util.Date date = new Date(time.getTime());
//                            jsonGenerator.writeStringField(column, formatDateJson.format(date));
//                        } else if (value instanceof java.sql.Date) {
//                            java.util.Date date = new Date(((java.sql.Date) value).getTime());
//                            jsonGenerator.writeStringField(column, formatDateJson.format(date));
//                        } else {
//                            if ((null != value) && value.toString().matches("-?\\d+\\.?\\d*")) {
//                                if (value.toString().contains(".")) {
//                                    jsonGenerator.writeNumberField(column, Double.parseDouble(value.toString()));
//                                } else {
//                                    jsonGenerator.writeNumberField(column, Long.parseLong(value.toString()));
//                                }
//                            }
//                        }
//                    } catch (Exception ex) {
//                        logger.error(ex.getMessage(), ex);
//                        throw new Exception("Error json write value: " + value + ". " + ex.toString());
//                    }
//                }
//            }
//            jsonGenerator.writeEndObject();
//        }
//        jsonGenerator.writeEndArray();
//        jsonGenerator.writeEndObject();
//        jsonGenerator.flush();
//        jsonGenerator.close();
//
//        String content = sw.getBuffer().toString();
//        sw.close();
//        logger.info("Convert json success: OK");
//        return content;
//    }
//
//}
