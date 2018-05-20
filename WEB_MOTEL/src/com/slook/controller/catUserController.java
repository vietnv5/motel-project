package com.slook.controller;

import com.motel.controller.HomeController;
import com.motel.model.GroupUser;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatRole;
import com.slook.model.CatUser;
import com.slook.model.Employee;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.persistence.GroupUserServiceImpl;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.SessionUtil;
import com.slook.util.SessionWrapper;
import com.slook.util.StringUtil;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.omnifaces.util.Faces.getRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@ViewScoped
public class catUserController implements Serializable {

    protected static final Logger logger = LoggerFactory.getLogger(catUserController.class);

    GenericDaoServiceNewV2 catUserService;
    GenericDaoServiceNewV2 catRoleService;
    GenericDaoServiceNewV2 employeeService;
    CatUser currCatUser;
    LazyDataModel<CatUser> lazyDataModel;
    List<CatRole> lstRole;
    List<Employee> lstEmployees;
    private boolean isEdit = false;
    String oldPassword;
    private String oldObjectStr = null;
// phan nhom nguoi dung
    Long groupUserId = null;
    List<GroupUser> lstGroupUser;

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
            CatUser catUser = null;
            if (getRequest().getSession().getAttribute("user") != null) {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupUserId();
            }

            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("userName", "ASC");

            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0
                    && !SessionUtil.getInstance().getUrlDisplay("/groupUser")) {//phan quyen
                filter.put("groupUserId", groupUserId);
            }
            lazyDataModel = new LazyDataModelBase<CatUser, Long>(catUserService, filter, order);
            lstRole = catRoleService.findList();
            Map<String, Object> filterEmp = new HashMap<>();
            filterEmp.put("statusId", Constant.EMPLOYEE_STATUS.ACTIVE);
            LinkedHashMap<String, String> orderEmp = new LinkedHashMap<>();
            orderEmp.put("employeeName", "ASC");
//            lstEmployees = employeeService.findList(filterEmp, orderEmp);

            Map<String, Object> filtersGroupUser = new HashMap<>();
            filtersGroupUser.put("status-NEQ", Constant.STATUS.DELETE);
//            if (groupUserId != null && groupUserId > 0) {//phan quyen
//                filtersGroupUser.put("groupUserId", groupUserId);
//            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("name", Constant.ORDER.ASC);
            lstGroupUser = GroupUserServiceImpl.getInstance().findList(filtersGroupUser, orderHome);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void preAdd() {
        isEdit = false;
        currCatUser = new CatUser();
        currCatUser.setStatus(1l);
        // set role mac dinh
        // cho truong hop user nguoi dung ko co quyen thay doi role
        if (getRequest().getSession().getAttribute("user") != null
                && !SessionUtil.getInstance().getUrlDisplay("/catUser.action_modify_role")) {
            currCatUser.setRoleId(((CatUser) getRequest().getSession().getAttribute("user")).getRoleId());
        }
        //mac dinh tao user cung nhom quan ly
        currCatUser.setGroupUserId(groupUserId);

        oldObjectStr = null;
    }

    public void saveOrUpdate() {
        try {
            Map<String, Object> filters = new HashMap<String, Object>();
            filters.put("userName-EXAC_IGNORE_CASE", currCatUser.getUserName());
            if (currCatUser.getUserId() != null) {
                filters.put("userId-NEQ", currCatUser.getUserId());
            }
            List<CatUser> lst = catUserService.findList(filters);
            if (!lst.isEmpty() && lst.size() > 0) {
                MessageUtil.setErrorMessage(MessageFormat.format(MessageUtil.getResourceBundleMessage("common.exist"),
                        "Username"
                ));
                return;
            }

            if (currCatUser.getEmployee() != null) {
                currCatUser.setEmpId(currCatUser.getEmployee().getEmployeeId());
            }
            if (currCatUser.getPassword() != null && !currCatUser.getPassword().equals(currCatUser.getConfirmPassword())) {
                MessageUtil.setErrorMessage("Mật khẩu nhập chưa khớp!");
                return;
            }
            if (StringUtil.isNullOrEmpty(currCatUser.getPassword())) {
                currCatUser.setPassword(oldPassword);
            }
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
//            RequestContext.getCurrentInstance().execute("panelAddCatUser:@child(0);PF('widTableCatUser').clearFilters();");

            preAdd();
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void preEdit(CatUser catUser) {
        isEdit = true;
        oldPassword = catUser.getPassword();
        currCatUser = catUser;
        oldObjectStr = catUser.toString();
    }

    public void onDelete(CatUser catUser) {
        try {
            catUser.setStatus(Constant.STATUS.DELETE);
            catUserService.saveOrUpdate(catUser);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public GenericDaoServiceNewV2 getCatUserService() {
        return catUserService;
    }

    public void setCatUserService(GenericDaoServiceNewV2 catUserService) {
        this.catUserService = catUserService;
    }

    public GenericDaoServiceNewV2 getCatRoleService() {
        return catRoleService;
    }

    public void setCatRoleService(GenericDaoServiceNewV2 catRoleService) {
        this.catRoleService = catRoleService;
    }

    public CatUser getCurrCatUser() {
        return currCatUser;
    }

    public void setCurrCatUser(CatUser currCatUser) {
        this.currCatUser = currCatUser;
    }

    public LazyDataModel<CatUser> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatUser> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public List<CatRole> getLstRole() {
        return lstRole;
    }

    public void setLstRole(List<CatRole> lstRole) {
        this.lstRole = lstRole;
    }

    public GenericDaoServiceNewV2 getEmployeeService() {
        return employeeService;
    }

    public void setEmployeeService(GenericDaoServiceNewV2 employeeService) {
        this.employeeService = employeeService;
    }

    public List<Employee> getLstEmployees() {
        return lstEmployees;
    }

    public void setLstEmployees(List<Employee> lstEmployees) {
        this.lstEmployees = lstEmployees;
    }

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean edit) {
        isEdit = edit;
    }

    public Long getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId) {
        this.groupUserId = groupUserId;
    }

    public List<GroupUser> getLstGroupUser() {
        return lstGroupUser;
    }

    public void setLstGroupUser(List<GroupUser> lstGroupUser) {
        this.lstGroupUser = lstGroupUser;
    }

    public void preChangePassword() {
        try {
            CatUser u = (CatUser) getRequest().getSession().getAttribute("user");
            currCatUser = (CatUser) catUserService.findById(u.getUserId());
            oldObjectStr = currCatUser.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangePassword() {
        try {

            if (currCatUser.getPassword() != null && !currCatUser.getPassword().equals(currCatUser.getOldPassword())) {
                MessageUtil.setErrorMessage("Mật khẩu cũ nhập chưa chính xác!");
                return;
            }
            if (currCatUser.getNewPassword() != null && !currCatUser.getNewPassword().equals(currCatUser.getConfirmPassword())) {
                MessageUtil.setErrorMessage("Mật khẩu mới nhập chưa khớp!");
                return;
            }
            if (!StringUtil.isNullOrEmpty(currCatUser.getNewPassword())) {
                currCatUser.setPassword(currCatUser.getNewPassword());
            }
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
            RequestContext.getCurrentInstance().execute("PF('changePassworDlg').hide();");

            preAdd();
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

}
