/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.GroupUser;
import com.motel.model.Home;
import com.slook.controller.LogActionController;
import com.slook.controller.catUserController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatRole;
import com.slook.model.CatUser;
import com.slook.model.Employee;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.GroupUserServiceImpl;
import com.slook.persistence.HomeServiceImpl;
import com.slook.util.Constant;
import com.slook.util.DataConfig;
import com.slook.util.DateTimeUtils;
import com.slook.util.MessageUtil;
import com.slook.util.SessionUtil;
import com.slook.util.StringUtil;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import static org.omnifaces.util.Faces.getRequest;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author VietNV Jun 7, 2018
 */
@ManagedBean
@ViewScoped
public class AccountRegistrationController {

    protected static final Logger logger = LoggerFactory.getLogger(catUserController.class);

    GenericDaoServiceNewV2 catUserService;
    GenericDaoServiceNewV2 catRoleService;
    CatUser currCatUser;
    List<CatRole> lstRole;
    String oldPassword;
    private String oldObjectStr = null;
// phan nhom nguoi dung
    private boolean isEdit = false;

    @PostConstruct
    public void onStart() {
        catUserService = new GenericDaoImplNewV2<CatUser, Long>() {
        };
        currCatUser = new CatUser();
        catRoleService = new GenericDaoImplNewV2<CatRole, Long>() {
        };
//        employeeService = new GenericDaoImplNewV2<Employee, Long>() {
//        };
        try {

            lstRole = catRoleService.findList();

            // su dung cho dk tai khoan
            preRegistrationAccount();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean checkValidate() {
        try {
            if (StringUtil.isNotNullAndNullStr(currCatUser.getPhoneNumber())) {

                Map<String, Object> filters = new HashMap<String, Object>();
                filters.put("phoneNumber-EXAC_IGNORE_CASE", currCatUser.getPhoneNumber());
                filters.put("status-NEQ", Constant.STATUS.DELETE);
                if (currCatUser.getUserId() != null) {
                    filters.put("userId-NEQ", currCatUser.getUserId());
                }
                List<CatUser> lst = catUserService.findList(filters);
                if (!lst.isEmpty() && lst.size() > 0) {
                    MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                            MessageUtil.getResourceBundleMessage("customer.phone")
                    ));
                    return false;
                }
            }
            if (StringUtil.isNotNullAndNullStr(currCatUser.getEmail())) {

                Map<String, Object> filters = new HashMap<String, Object>();
                filters.put("email-EXAC_IGNORE_CASE", currCatUser.getEmail());
                filters.put("status-NEQ", Constant.STATUS.DELETE);

                if (currCatUser.getUserId() != null) {
                    filters.put("userId-NEQ", currCatUser.getUserId());
                }
                List<CatUser> lst = catUserService.findList(filters);
                if (!lst.isEmpty() && lst.size() > 0) {
                    MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                            MessageUtil.getResourceBundleMessage("datatable.header.email")
                    ));
                    return false;
                }
            }

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
        return true;
    }

    public void preRegistrationAccount() {
        try {
            isEdit = false;
            currCatUser = new CatUser();
            currCatUser.setStatus(1l);
            currCatUser.setGroupUser(new GroupUser());
            oldObjectStr = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRegistrationAccount(CatUser currCatUser) {
        try {
            if (currCatUser.getUserId() == null) {

                if (currCatUser.getPassword() != null && !currCatUser.getPassword().equals(currCatUser.getConfirmPassword())) {
                    MessageUtil.setErrorMessage("Mật khẩu nhập chưa khớp!");
                    return;
                }
                // set role
                String roleCfg = Constant.ROLE.CUSTOMER;
                try {
                    roleCfg = DataConfig.getConfigByKey("ROLE_REGISTRATION");
                } catch (Exception e) {
                }
                String ROLE_REGISTRATION =roleCfg!=null?roleCfg: Constant.ROLE.CUSTOMER;
                List<CatRole> lsRole = lstRole.stream().filter(o -> ROLE_REGISTRATION.equalsIgnoreCase(o.getRoleCode())).collect(Collectors.toList());
                if (lsRole != null && !lsRole.isEmpty()) {
                    currCatUser.setRoleId(lsRole.get(0).getRoleId());
                    currCatUser.setRole(lsRole.get(0));
                }

                GroupUser currGroupUser = new GroupUser();
                if (currCatUser.getGroupUser() != null) {
                    currGroupUser = currCatUser.getGroupUser();
                }
                //set thong tin mac dinh
                currGroupUser.setCreateTime(new Date());
                currGroupUser.setCode(DateTimeUtils.format(new Date(), "yyyyMMddHHmmss"));
                long numRoomDefault = 10;
                try {
                    String numRoomStr = DataConfig.getConfigByKey("MAX_NUMBER_ROOM_DEFAULT");
                    numRoomDefault = Long.valueOf(numRoomStr);
                } catch (Exception e) {
                }
                currGroupUser.setMaxNumberRoom(numRoomDefault);
                currGroupUser.setStartTime(new Date());
                currGroupUser.setDescription("Registration Account");
                currGroupUser.setStatus(Constant.STATUS.ACTIVE);
                GroupUserServiceImpl.getInstance().saveOrUpdate(currGroupUser);
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, currGroupUser.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

                currCatUser.setGroupUserId(currGroupUser.getId());

                // tao thong tin nha tro
                Home currHome = new Home();
                currHome.setGroupUserId(currGroupUser.getId());
                currHome.setStatus(Constant.STATUS.ACTIVE);
                currHome.setHomeName(currGroupUser.getName());
                HomeServiceImpl.getInstance().saveOrUpdate(currHome);
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, null, currHome.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void onCreateAccount() {
        try {
            if (StringUtil.isNullOrEmpty(currCatUser.getPhoneNumber()) && StringUtil.isNullOrEmpty(currCatUser.getEmail())) {
                MessageUtil.setErrorMessage("Cần phải nhập email hoặc số điện thoại!");
                return;
            }

            if (!checkValidate()) {
                return;
            }

//trường hợp đăng ký tài khoản mới
            processRegistrationAccount(currCatUser);

            catUserService.saveOrUpdate(currCatUser);
            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, currCatUser.getUserName(), oldObjectStr, currCatUser.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, currCatUser.getUserName(), oldObjectStr, currCatUser.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
//            RequestContext.getCurrentInstance().execute("PF('infoCurrUserDlg').hide();");

// chuyen toi trang chu
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            Map sessionMap = context.getSessionMap();
            sessionMap.put("user", currCatUser);
            sessionMap.put("authenticated", true);
            String defaultUrl = DataConfig.getConfigByKey("DEFAULT_URL") != null
                    ? DataConfig.getConfigByKey("DEFAULT_URL") : "";

            HttpServletRequest req = (HttpServletRequest) context.getRequest();
            preRegistrationAccount();
            context.redirect(req.getContextPath() + defaultUrl);
//            preAdd();
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public CatUser getCurrCatUser() {
        return currCatUser;
    }

    public void setCurrCatUser(CatUser currCatUser) {
        this.currCatUser = currCatUser;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

}
