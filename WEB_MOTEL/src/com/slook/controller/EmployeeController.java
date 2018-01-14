package com.slook.controller;

import com.slook.exception.AppException;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.*;
import com.slook.persistence.CfgWsTimekeeperServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import com.slook.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.transform.jexcel.JexcelTransformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by xuanh on 5/21/2017.
 */
@ManagedBean
@ViewScoped
public class EmployeeController {

    private static final Logger logger = getLogger(EmployeeController.class);

    GenericDaoImplNewV2<Employee, Long> employeeServerice;
    GenericDaoServiceNewV2<EmployeeStatus, Long> statusService;

    GenericDaoImplNewV2<CatDepartment, Long> departmentService;
    GenericDaoImplNewV2<CatJobTitle, Long> jobtitleService;
    GenericDaoServiceNewV2<CatAddressCity, Long> addressCityService;
    GenericDaoServiceNewV2<CatAddressDistrict, Long> addressDistrictService;
    GenericDaoServiceNewV2<CatAddressStreet, Long> addressStreetService;

    @ManagedProperty(value = "#{cfgWsTimekeeperService}")
    private CfgWsTimekeeperServiceImpl cfgWsTimekeeperService;

    LazyDataModel<Employee> lazyEmployee;
    List<CatDepartment> departments;

    Employee currentEmployee;
    CatJobTitle jobTitle;
    CatDepartment department;

    List<CatJobTitle> jobTitles;
    List<EmployeeStatus> statuses;

    Object currentField;

    List<CatAddressCity> addressCities;
    CatAddressCity city;
    List<CatAddressDistrict> addressDistricts;
    CatAddressDistrict district;
    List<CatAddressStreet> addressStreets;
    CatAddressStreet street;
    String addressNo;

    StreamedContent fileExported;

    boolean disableDistrict = true;
    boolean disableStreet = true;
    boolean disableEdit = true;

    List<CatItemBO> lstContractType;
    private Map<Long, CatItemBO> mapContractType;
    private List<CatItemBO> lstMarriedStatus;
    private Map<String, CatItemBO> mapMarriedStatus;
    List<Employee> allEmployees;
    private String oldObjectStr = null;
    private boolean isEdit = false;

    @PostConstruct
    public void onStart() {
        employeeServerice = new GenericDaoImplNewV2<Employee, Long>() {
        };
        departmentService = new GenericDaoImplNewV2<CatDepartment, Long>() {
        };
        jobtitleService = new GenericDaoImplNewV2<CatJobTitle, Long>() {
        };
        addressCityService = new GenericDaoImplNewV2<CatAddressCity, Long>() {
        };
        addressDistrictService = new GenericDaoImplNewV2<CatAddressDistrict, Long>() {
        };
        addressStreetService = new GenericDaoImplNewV2<CatAddressStreet, Long>() {
        };
        statusService = new GenericDaoImplNewV2<EmployeeStatus, Long>() {
        };

        Map<String, Object> filterDefault = new HashMap<String, Object>();
        filterDefault.put("statusId-NEQ", -1l);//xoa
        Map<String, Object> filterItem = new HashMap<String, Object>();
        filterItem.put("status", Constant.STATUS.ACTIVE);

        try {
            lazyEmployee = new LazyDataModelBase<Employee, Long>(employeeServerice, filterDefault);

            departments = departmentService.findList(filterItem);
            jobTitles = jobtitleService.findList(filterItem);
            currentEmployee = new Employee();
            statuses = statusService.findList();
//            addressCities = addressCityService.findList();

            lstContractType = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.CONTRACT_TYPE, "name");
            mapContractType = CommonUtil.getMapCatItemByKeyId(Constant.CAT_CODE.CONTRACT_TYPE);
            lstMarriedStatus = (List<CatItemBO>) CommonUtil.getItemBOList(Constant.CAT_CODE.MARRIED_STATUS, "name");
            mapMarriedStatus = CommonUtil.getMapCatItemByKeyValue(Constant.CAT_CODE.MARRIED_STATUS);
            //bo sung khoi tao
            Map<String, Object> filter = new HashMap<>();
            filter.put("statusId-NEQ", -1L);
            allEmployees = employeeServerice.findList(filter);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void addressField(String field) {
        currentField = field;
    }

    public void findAddress() {
        List<String> addr = new ArrayList<>();
        if (addressNo != null && addressNo != "") {
            addr.add(addressNo);
        }
        if (street != null && street.getStreetName() != "") {
            addr.add(street.getStreetName());
        }
        if (district != null && district.getDistrictName() != "") {
            addr.add(district.getDistrictName());
        }
        if (city != null && city.getCityName() != "") {
            addr.add(city.getCityName());
        }
        String sum = StringUtils.join(addr, ", ");
        if (currentField.equals("permanentAddress")) {
            currentEmployee.setPermanentAddress(sum);
        } else if (currentField.equals("PresentAddress")) {
            currentEmployee.setPresentAddress(sum);
        } else if (currentField.equals("birthPlace")) {
            currentEmployee.setBirthPlace(sum);
        }
    }

    public List<CatAddressCity> listAddressCities(String addCity) {
        if (addCity == null || addCity == "") {
            city = null;
            disableDistrict = true;
            disableStreet = true;
            return null;
        }
        try {
            Map<String, Object> filter = new HashMap<>();
            if (addCity != null && addCity != "") {
                filter.put("cityName", addCity);
            }
            return addressCityService.findList(filter);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public List<CatAddressDistrict> listAddressDistrict(String qDistrict) {
        if (qDistrict == null || qDistrict == "") {
            district = null;
            disableStreet = true;
            return null;
        }
        try {
            Map<String, Object> filter = new HashMap<>();
            if (qDistrict != null && qDistrict != "") {
                filter.put("districtName", qDistrict);
            }
            if (city != null) {
                filter.put("cityId", city.getCityId());
            }
            return addressDistrictService.findList(filter);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public List<CatAddressStreet> listAddressStreet(String street) {
        if (street == null || street == "") {
            this.street = null;
            return null;
        }
        try {
            Map<String, Object> filter = new HashMap<>();
            if (street != null && street != "") {
                filter.put("streetName", street);
            }
            if (district != null) {
                filter.put("districtId", district.getDistrictId());
            }
            return addressStreetService.findList(filter);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public void save() {
        try {
                        // validate
            Map<String, Object> filterExist = new HashMap<>();
            filterExist.put("idNum-EXAC_IGNORE_CASE", currentEmployee.getIdNum());
            if (currentEmployee.getEmployeeId()!= null) {
                filterExist.put("employeeId-NEQ", currentEmployee.getEmployeeId());
            }
            List<Employee> lstExist = employeeServerice.findList(filterExist);
            if (lstExist != null && lstExist.size() > 0) {
                MessageUtil.setErrorMessageFromRes("employee.existEmployeeNumId");
                return;
            }
            System.out.println(currentEmployee.getEmployeeCode());
            //validate
            if (currentEmployee.getContractType() != null && Long.valueOf(currentEmployee.getContractType()) < 0) {
                currentEmployee.setContractType(null);
            }
//           if (currentEmployee.getMarriedStatus() != null && Long.valueOf(currentEmployee.getMarriedStatus()) < 0) {
//                currentEmployee.setMarriedStatus("");
//            }
            employeeServerice.saveOrUpdate(currentEmployee);
            //Sinh ma tu dong
            Long employeeId = currentEmployee.getEmployeeId();
            Long branchId = currentEmployee.getBranchId();
//            if (StringUtil.isNullOrEmpty(currentEmployee.getEmployeeCode())) {
            String empCode = "";
            if (branchId != null) {
                empCode = branchId + "-" + employeeId;
            } else {
                empCode = 0 + "-" + employeeId;
            }
            currentEmployee.setEmployeeCode(empCode);
            currentEmployee.setCheckInCode(String.valueOf(employeeId));
//            }
            employeeServerice.saveOrUpdate(currentEmployee);

            List<CatMachine> list = getListMachine(Constant.STATUS.ENABLE);

            // Them ban ghi vao bang cfg_ws_timekeeper de ws c# biet la can them user
            if (!isEdit) {
                CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                cfg.setInsertTime(new Date());
                cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                cfg.setMethod(Constant.METHOD.INSERT);
                cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                for (CatMachine catMachine : list) {
                    // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                    cfg.setContent("EMP_" + currentEmployee.getCheckInCode() + "|" + currentEmployee.getEmployeeName() + "|" + currentEmployee.getIdNum() + "|1|" + catMachine.getIp());
                    cfgWsTimekeeperService.save(cfg);
                }
            } else {
                CfgWsTimekeeper cfg = new CfgWsTimekeeper();
                cfg.setInsertTime(new Date());
                cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
                cfg.setMethod(Constant.METHOD.UPDATE);
                cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
                for (CatMachine catMachine : list) {
                    // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                    cfg.setContent("EMP_" + currentEmployee.getCheckInCode() + "|" + currentEmployee.getEmployeeName() + "|" + currentEmployee.getIdNum() + "|1|" + catMachine.getIp());
                    cfgWsTimekeeperService.save(cfg);
                }
            }

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, currentEmployee.getEmployeeCode(), oldObjectStr, currentEmployee.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, currentEmployee.getEmployeeCode(), oldObjectStr, currentEmployee.toString(), this.getClass().getSimpleName(),
                        (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessage("Lưu thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Lưu thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    public static List<CatMachine> getListMachine(Long status) {
        List<CatMachine> list;
        Session session = null;
        try {
            session = HibernateUtil.getSessionAndBeginTransaction();
            Criteria cri = session.createCriteria(CatMachine.class);
            cri.add(Restrictions.eq("status", status));
            list = cri.list();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
            HibernateUtil.close(session);
        }
        return list;
    }

    public void onCitySelected() {
        disableDistrict = false;
        district = null;
        street = null;
    }

    public void onDistrictSelected() {
        disableStreet = false;
        street = null;
    }

    public CatDepartment getDepartment() {
        return department;
    }

    public void setDepartment(CatDepartment department) {
        this.department = department;
    }

    public CatJobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(CatJobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void test() {
        System.out.println(currentEmployee.getEmployeeName());
    }

    public void preInsert() {
        currentEmployee = new Employee();
        disableEdit = false;
        oldObjectStr = null;
        isEdit = false;
    }

    public void exportEmployee() {
        try {
            Map<String, String> order = new HashMap<>();
            order.put("employeeId", "ASC");
            List<Employee> empList = employeeServerice.findList(null, order);

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "employee_template.xls";

            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + "employee.xls";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + "employee.xls";

            if (!makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Export thất bại (Tạo folder)");
                fileExported = null;
                return;
            }

            InputStream is = ctx.getResourceAsStream(path);
            OutputStream os = new FileOutputStream(des);
            Context context = new Context();
            Map<Long, String> m = new HashMap<>();
            m.put(0L, "Nữ");
            m.put(1L, "Nam");
            context.putVar("employees", empList);
            context.putVar("timeNow", new Date());
            context.putVar("sex", m);

            JxlsHelper.getInstance().processTemplate(is, os, context);

            InputStream stream = ctx.getResourceAsStream(desPath);
            fileExported = new DefaultStreamedContent(stream, "application/vnd.ms-excel", "employee.xls");

            MessageUtil.setInfoMessage("Export thành công");
        } catch (Exception e) {
            MessageUtil.setErrorMessage("Export thất bại");
            logger.error(e.getMessage(), e);
        }
    }

    private boolean makeDirectory(String urlFolder) {
        System.out.println(urlFolder);
        File directory = new File(urlFolder);

        if (!directory.exists()) {
            return directory.mkdir();
        }
        return true;
    }

    public List<EmployeeStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<EmployeeStatus> statuses) {
        this.statuses = statuses;
    }

    public boolean isDisableDistrict() {
        return disableDistrict;
    }

    public void setDisableDistrict(boolean disableDistrict) {
        this.disableDistrict = disableDistrict;
    }

    public boolean isDisableStreet() {
        return disableStreet;
    }

    public void setDisableStreet(boolean disableStreet) {
        this.disableStreet = disableStreet;
    }

    public void onSelectedEmployee(Employee employee, boolean isEdit) {
        this.currentEmployee = employee;
        this.disableEdit = !isEdit;
        oldObjectStr = employee.toString();
        this.isEdit = isEdit;
    }

    public LazyDataModel<Employee> getLazyEmployee() {
        return lazyEmployee;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public void setLazyEmployee(LazyDataModel<Employee> lazyEmployee) {
        this.lazyEmployee = lazyEmployee;
    }

    public List<CatDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<CatDepartment> departments) {
        this.departments = departments;
    }

    public List<CatJobTitle> getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(List<CatJobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public List<CatAddressDistrict> getAddressDistricts() {
        return addressDistricts;
    }

    public void setAddressDistricts(List<CatAddressDistrict> addressDistricts) {
        this.addressDistricts = addressDistricts;
    }

    public CatAddressDistrict getDistrict() {
        return district;
    }

    public void setDistrict(CatAddressDistrict district) {
        this.district = district;
    }

    public List<CatAddressStreet> getAddressStreets() {
        return addressStreets;
    }

    public void setAddressStreets(List<CatAddressStreet> addressStreets) {
        this.addressStreets = addressStreets;
    }

    public CatAddressStreet getStreet() {
        return street;
    }

    public void setStreet(CatAddressStreet street) {
        this.street = street;
    }

    public CatAddressCity getCity() {
        return city;
    }

    public void setCity(CatAddressCity city) {
        this.city = city;
    }

    public List<CatAddressCity> getAddressCities() {
        return addressCities;
    }

    public void setAddressCities(List<CatAddressCity> addressCities) {
        this.addressCities = addressCities;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public boolean isDisableEdit() {
        return disableEdit;
    }

    public void setDisableEdit(boolean disableEdit) {
        this.disableEdit = disableEdit;
    }

    public StreamedContent getFileExported() {
        return fileExported;
    }

    public void setFileExported(StreamedContent fileExported) {
        this.fileExported = fileExported;
    }

    public List<CatItemBO> getLstContractType() {
        return lstContractType;
    }

    public void setLstContractType(List<CatItemBO> lstContractType) {
        this.lstContractType = lstContractType;
    }

    public Map<Long, CatItemBO> getMapContractType() {
        return mapContractType;
    }

    public void setMapContractType(Map<Long, CatItemBO> mapContractType) {
        this.mapContractType = mapContractType;
    }

    public List<CatItemBO> getLstMarriedStatus() {
        return lstMarriedStatus;
    }

    public void setLstMarriedStatus(List<CatItemBO> lstMarriedStatus) {
        this.lstMarriedStatus = lstMarriedStatus;
    }

    public Map<String, CatItemBO> getMapMarriedStatus() {
        return mapMarriedStatus;
    }

    public void setMapMarriedStatus(Map<String, CatItemBO> mapMarriedStatus) {
        this.mapMarriedStatus = mapMarriedStatus;
    }

    public String getNameContractType(String value) {
        if (mapContractType != null && mapContractType.get(value) != null) {
            return mapContractType.get(value).getName();
        }
        return "";
    }

    public String getNameMarrie(String value) {
        if (mapMarriedStatus != null && mapMarriedStatus.get(value) != null) {
            return mapMarriedStatus.get(value).getName();
        }
        return "";
    }

    public CfgWsTimekeeperServiceImpl getCfgWsTimekeeperService() {
        return cfgWsTimekeeperService;
    }

    public void setCfgWsTimekeeperService(CfgWsTimekeeperServiceImpl cfgWsTimekeeperService) {
        this.cfgWsTimekeeperService = cfgWsTimekeeperService;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    //vietnv add
    public List<Employee> completeEmployee(String query) {
        List<Employee> filteredEmployees = new ArrayList<Employee>();
        try {
//            Map<String, Object> filter = new HashMap<>();
//            filter.put("statusId-NEQ", 1l);
//            List<Employee> allEmployees = employeeServerice.findList(filter);

            for (int i = 0; i < allEmployees.size(); i++) {
                Employee skin = allEmployees.get(i);
                if (StringUtil.isNullOrEmpty(query)) {
                    query = query.toLowerCase();
                }
                if (skin != null && skin.getEmployeeName() != null && skin.getEmployeeName().toLowerCase().contains(query)) {
                    filteredEmployees.add(skin);
                    if (filteredEmployees.size() > 100) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return filteredEmployees;
    }

    public void onDelete(Employee employee) {
        try {
            String old = employee.toString();
            employee.setStatusId(Constant.EMPLOYEE_STATUS.DELETE);
            employeeServerice.saveOrUpdate(employee);
            //update may cham cong
            List<CatMachine> list = EmployeeController.getListMachine(Constant.STATUS.ENABLE);
            CfgWsTimekeeper cfg = new CfgWsTimekeeper();
            cfg.setInsertTime(new Date());
            cfg.setType(Constant.DATA_TYPE.EMPLOYEE);
            cfg.setMethod(Constant.METHOD.DELETE);
            cfg.setStatus(Constant.STATUS.IS_NOT_PUSH_DATA);
            for (CatMachine catMachine : list) {
                // Template data response sang ws c# - userid (check_in_code)|name|so the|do uu tien|ip thiet bi
                cfg.setContent("EMP_" + employee.getCheckInCode() + "|" + employee.getEmployeeName() + "|" + employee.getIdNum() + "|1|" + catMachine.getIp());
                cfgWsTimekeeperService.save(cfg);
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, employee.getEmployeeCode(), old, employee.toString(), this.getClass().getSimpleName(),
                    (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            MessageUtil.setInfoMessageFromRes("info.delete.suceess");
        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.delete.unsuceess");
            logger.error(e.getMessage(), e);
        }
    }

    public List<Employee> getAllEmployees() {
        return allEmployees;
    }

    public void setAllEmployees(List<Employee> allEmployees) {
        this.allEmployees = allEmployees;
    }

}
