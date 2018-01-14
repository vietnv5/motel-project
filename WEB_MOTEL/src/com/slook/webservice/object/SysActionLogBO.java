/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
*/
package com.slook.webservice.object;


import java.io.Serializable;
import java.util.Date;

/**
 * @author xuanh
 */
public class SysActionLogBO implements Serializable {

    private String objectName;
    private Date receiveTime;
    private String className;
    private Date mdSendTime;
    private Date startTime;
    private Date proccessTime;
    private Integer status;
    private Long logId;
    private String step;
    private String content;
    private String userName;
    private String functionName;
    private String module;
    private String idAddress;
    private String mdService;
    private Date endTime;
    private int logType = 1;
    private Double duration;
    private String methodName;
    private Integer workerPort;
    private Long techpackExecutionId;
    private boolean isNew = true;
    private String serverHost;
    private String actionName;
    private String deptName;
    private boolean showLog = true;
    protected String vsaPath;
    private boolean fromInterceptor = false;

    public String getVsaPath() {
        return vsaPath;
    }

    public void setVsaPath(String vsaPath) {
        this.vsaPath = vsaPath;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Date getProccessTime() {
        return proccessTime;
    }

    public void setProccessTime(Date proccessTime) {
        this.proccessTime = proccessTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Long getTechpackExecutionId() {
        return techpackExecutionId;
    }

    public void setTechpackExecutionId(Long techpackExecutionId) {
        this.techpackExecutionId = techpackExecutionId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Date getMdSendTime() {
        return mdSendTime;
    }

    public void setMdSendTime(Date mdSendTime) {
        this.mdSendTime = mdSendTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getMdService() {
        return mdService;
    }

    public void setMdService(String mdService) {
        this.mdService = mdService;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getWorkerPort() {
        return workerPort;
    }

    public void setWorkerPort(Integer workerPort) {
        this.workerPort = workerPort;
    }

    public void setFromInterceptor(boolean fromInterceptor) {
        this.fromInterceptor = fromInterceptor;
    }

    public boolean isFromInterceptor() {
        return fromInterceptor;
    }

    public void onError(Exception ex) {
        if (ex != null && ex.getMessage() != null) {
            StackTraceElement[] stackTrace = ex.getStackTrace();
            String message = ex.getMessage() + "\n";
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement item : stackTrace) {
                    message += item.toString() + "\n";
                }
            }
            String content = "ERROR: " + message;
            this.setContent(content.substring(0, Math.min(3990,content.length())));

            this.setStatus(2);
        }
    }

    @Override
    public SysActionLogBO clone() {
        return new SysActionLogBO(objectName, receiveTime, className, mdSendTime, startTime,
                status, logId, step, content, userName, functionName,
                module, idAddress, mdService, endTime, logType, duration,
                methodName, workerPort, techpackExecutionId, isNew, proccessTime, serverHost, actionName,
                deptName, showLog, vsaPath);
    }

    public SysActionLogBO() {
    }

    public SysActionLogBO(String objectName, Date receiveTime, String className, Date mdSendTime, Date startTime,
                          Integer status, Long logId, String step, String content, String userName, String functionName,
                          String module, String idAddress, String mdService, Date endTime, int logType, Double duration,
                          String methodName, Integer workerPort, Long techpackExecutionId, boolean isNew,
                          Date proccessTime, String serverHost, String actionName, String deptName, boolean showLog,
                          String vsaPath) {
        this.objectName = objectName;
        this.receiveTime = receiveTime;
        this.className = className;
        this.mdSendTime = mdSendTime;
        this.startTime = startTime;
        this.status = status;
        this.logId = logId;
        this.step = step;
        this.content = content;
        this.userName = userName;
        this.functionName = functionName;
        this.module = module;
        this.idAddress = idAddress;
        this.mdService = mdService;
        this.endTime = endTime;
        this.logType = logType;
        this.duration = duration;
        this.methodName = methodName;
        this.workerPort = workerPort;
        this.techpackExecutionId = techpackExecutionId;
        this.isNew = isNew;
        this.proccessTime = proccessTime;
        this.serverHost = serverHost;
        this.actionName = actionName;
        this.showLog = showLog;
        this.deptName = deptName;
        this.vsaPath = vsaPath;
    }

    public SysActionLogBO(String className, String methodName, String idAddress, String userName, String functionName, String actionName, int logType) {
        this.startTime = new Date();
        this.className = className;
        this.idAddress = idAddress;
        this.logType = logType;
        this.methodName = methodName;
        this.status = 1;
        this.userName = userName;
        this.functionName = functionName;
        this.actionName = actionName;
        this.proccessTime = new Date();
    }

    public SysActionLogBO(String className, String methodName, String idAddress, String userName, String functionName, String actionName, int logType, Long requestId) {
        this.startTime = new Date();
        this.className = className;
        this.idAddress = idAddress;
        this.logType = logType;
        this.methodName = methodName;
        this.status = 1;
        this.userName = userName;
        this.functionName = functionName;
        this.actionName = actionName;
        this.proccessTime = new Date();
        this.techpackExecutionId = requestId;
    }

}