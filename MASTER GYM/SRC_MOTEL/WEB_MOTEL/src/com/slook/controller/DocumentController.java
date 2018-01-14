package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.exception.SysException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.Document;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;
import com.slook.util.Util;
import com.viettel.vsa.util.ResourceBundleUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import static org.apache.log4j.Logger.getLogger;
import static org.apache.logging.log4j.MarkerManager.clear;
import static org.omnifaces.util.Faces.getResponse;

/**
 * @author VietNV
 */
@ManagedBean
@ViewScoped
public class DocumentController {

    private static final Logger logger = getLogger(DocumentController.class);

    GenericDaoImplNewV2<Document, Long> documentServiceImpl;
    private LazyDataModel<Document> lazyModelDocument;
    private Document newObjDocument;
    private Document deleteDocument;
    private boolean isAttach = false;
    private boolean isEdit = false;
    private String fileName;

    @PostConstruct
    public void onStart() {
        documentServiceImpl = new GenericDaoImplNewV2<Document, Long>() {
        };
        LinkedHashMap<String, String> ordersDocument = new LinkedHashMap<>();
        ordersDocument.put("documentName", "ASC");
        lazyModelDocument = new LazyDataModelBase<>(documentServiceImpl, null, ordersDocument);
    }

    public boolean validateDocument() {
        if (StringUtil.isNullOrEmpty(newObjDocument.getDocumentName()) || "".equals(newObjDocument.getDocumentName())) {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    MessageUtil.getResourceBundleMessage("document.documentName")));
            return false;
        }
        if (!isAttach) {
            MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.required"),
                    MessageUtil.getResourceBundleMessage("document.attachFile")));
            return false;
        }
        return true;
    }

    public void insertDocument() {
        if (!validateDocument()) {
            return;
        }
        try {
            Document tempObj;
            if (uploadedFile != null) {

                String fileName = uploadedFile.getFileName();
                Map<String, Object> filter = new HashMap<String, Object>();
                filter.put("fileName-EXAC", fileName);
                if (newObjDocument != null && newObjDocument.getDocumentId() != null) {
                    filter.put("documentId-NEQ", newObjDocument.getDocumentId());
                }
                List lst = documentServiceImpl.findList(filter);
                if (lst != null && lst.size() > 0) {
                    MessageUtil.setErrorMessage("File đính kèm đã tồn tại!");
                    return;
                }
            }


//            InputStream inputStream = null;
            if (isAttach) {
                try {


                    String resultPath = Constant.DOCUMENT_FOLDER;
                    ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                            .getExternalContext().getContext();
                    System.out.printf("********** " + System.getProperty("user.dir"));
//                    File fileSaveDir = new File(ctx.getRealPath(resultPath));
                    File fileSaveDir = new File(Util.getRealPath(resultPath));
                    if (!fileSaveDir.exists()) {
                        fileSaveDir.mkdir();
                    }
                    File tmpTemplateFile = new File(fileSaveDir.getPath(), fileName);
                    OutputStream outputStream = new FileOutputStream(tmpTemplateFile);
                    outputStream.write(uploadedFile.getContents());
                    outputStream.flush();
                    outputStream.close();
                    /*
//                    inputStream = uploadedFile.getInputstream();

                    String fileName = uploadedFile.getFileName();
                    //Luu file name nay vao db
                    //String uploadPath = "/share/upload/app_file/";
                    SFTPNET client = new SFTPNET();
//            String uploadPathParent = ResourceBundleUtils.getValueByKey("UPLOAD_FILE_PATH");
                    String uploadPathParentFtp = ResourceBundleUtils.getValueByKey("UPLOAD_FILE_PATH_FTP");
                    if (getConnectFTP(client)) {
                        String uploadPath = uploadPathParentFtp + "/";
                        client.uploadFile2(uploadedFile.getInputstream(), uploadPath, fileName);
                        client.disconnect();
                    }
                    */
                    newObjDocument.setFileName(fileName);
                    isAttach = false;
                    //session.saveOrUpdate(fub);
                } catch (Exception e1) {
                    MessageUtil.setErrorMessage("Cannot read input file!");
                    e1.printStackTrace();
                    logger.error(e1.getMessage(), e1);
                    return;
                }

            }

            if (!isEdit) {
                tempObj = new Document();
            } else {
                tempObj = documentServiceImpl.findById(newObjDocument.getDocumentId());
            }
            tempObj.setDocumentName(newObjDocument.getDocumentName());
            tempObj.setAttachFile(newObjDocument.getAttachFile());
            tempObj.setFileName(newObjDocument.getFileName());
            tempObj.setDescription(newObjDocument.getDescription());

            documentServiceImpl.saveOrUpdate(tempObj);

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('addDocumentDlg').hide();");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void preInsertDocument() {
        newObjDocument = new Document();
        isEdit = false;
        isAttach = false;
        fileName = null;

    }

    //Get dữ liệu update
    public void prepareUpdateDocument(Document node) {
        isEdit = true;
        newObjDocument = node;
        isAttach = true;
        fileName = node.getFileName();
    }

    public void preDeleteDocument(Document servicesDocument) {
        deleteDocument = servicesDocument;
    }

    public void deleteDocument() {
        if (deleteDocument != null) {
            try {
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                        .getExternalContext().getContext();
//                File fileDel = new File(ctx.getRealPath(Constant.DOCUMENT_FOLDER + "/" + deleteDocument.getFileName()));
                File fileDel = new File(Util.getRealPath(Constant.DOCUMENT_FOLDER + "/" + deleteDocument.getFileName()));
                if (fileDel.exists()) fileDel.delete();
                documentServiceImpl.delete(deleteDocument);
                deleteDocument = null;
                MessageUtil.setInfoMessageFromRes("info.delete.suceess");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            }
        }
    }

    private UploadedFile uploadedFile;

    public void handleFileUpload(FileUploadEvent event) {
        uploadedFile = event.getFile();
        isAttach = true;
        fileName = uploadedFile.getFileName();
    }


    public StreamedContent getFileTemplate(Document newObjDocument) {
        StreamedContent fileDispatch = null;
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance()
                    .getExternalContext();
//            HttpServletRequest request = (HttpServletRequest) externalContext
//                    .getRequest();

//local
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                    .getExternalContext().getContext();
            InputStream input = null;
//            input = new FileInputStream(ctx.getRealPath(Constant.DOCUMENT_FOLDER + "/" + newObjDocument.getFileName()));
            input = new FileInputStream(Util.getRealPath(Constant.DOCUMENT_FOLDER + "/" + newObjDocument.getFileName()));
            /*
            //vietnv14 20160923 chuyen sang ftp start
            SFTPNET client = new SFTPNET();
            if (getConnectFTP(client)) {
                String uploadPathFtp = ResourceBundleUtils.getValueByKey("UPLOAD_FILE_PATH_FTP");
                uploadPathFtp = uploadPathFtp.replace("\\", "/");
                input = client.getFileStream(uploadPathFtp, newObjDocument.getFileName());

            }
*/
            HttpServletResponse response = getResponse();
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + newObjDocument.getFileName() + "\"");
            response.getOutputStream().flush();

            fileDispatch = new DefaultStreamedContent(input,
                    externalContext.getMimeType(newObjDocument.getFileName()),
                    newObjDocument.getFileName());
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }
        return fileDispatch;
    }


    public GenericDaoImplNewV2<Document, Long> getDocumentServiceImpl() {
        return documentServiceImpl;
    }

    public void setDocumentServiceImpl(GenericDaoImplNewV2<Document, Long> documentServiceImpl) {
        this.documentServiceImpl = documentServiceImpl;
    }

    public LazyDataModel<Document> getLazyModelDocument() {
        return lazyModelDocument;
    }

    public void setLazyModelDocument(LazyDataModel<Document> lazyModelDocument) {
        this.lazyModelDocument = lazyModelDocument;
    }

    public Document getNewObjDocument() {
        return newObjDocument;
    }

    public void setNewObjDocument(Document newObjDocument) {
        this.newObjDocument = newObjDocument;
    }

    public Document getDeleteDocument() {
        return deleteDocument;
    }

    public void setDeleteDocument(Document deleteDocument) {
        this.deleteDocument = deleteDocument;
    }

    public boolean isAttach() {
        return isAttach;
    }

    public void setAttach(boolean attach) {
        isAttach = attach;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean edit) {
        isEdit = edit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
