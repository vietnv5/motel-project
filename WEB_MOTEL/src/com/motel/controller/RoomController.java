/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.motel.controller;

import com.motel.model.Contract;
import com.motel.model.Customer;
import com.motel.model.CustomerRoom;
import com.motel.model.Home;
import com.motel.model.Room;
import com.slook.controller.LogActionController;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatUser;
import com.slook.persistence.ContractServiceImpl;
import com.slook.persistence.CustomerServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.HomeServiceImpl;
import com.slook.persistence.RoomServiceImpl;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import static org.omnifaces.util.Faces.getRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author VietNV Jan 17, 2018
 */
@ManagedBean
@ViewScoped
public class RoomController
{

    protected static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    LazyDataModel<Room> lazyDataModel;
    Room currRoom = new Room();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    List<Home> lstHome;
    Long groupUserId = null;

    // customerRoom
    private GenericDaoImplNewV2<CustomerRoom, Long> customerRoomService;
    private CustomerRoom currCustomerRoom = new CustomerRoom();
    private boolean isEditCustomerRoom = false;
    private List<Customer> lstCustomer;
    LazyDataModel<CustomerRoom> lazyCustomerRoom;

    public void onToggler(ToggleEvent e)
    {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart()
    {

        try
        {
            CatUser catUser = null;
            if (getRequest().getSession().getAttribute("user") != null)
            {
                catUser = (CatUser) getRequest().getSession().getAttribute("user");
                groupUserId = catUser.getGroupUserId();
            }
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("home.homeName", Constant.ORDER.ASC);
            order.put("roomName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0)
            {//phan quyen
                filter.put("home.groupUserId", groupUserId);
            }
            filter.put("home.status-NEQ", Constant.STATUS.DELETE);

            lazyDataModel = new LazyDataModelBase<Room, Long>(RoomServiceImpl.getInstance(), filter, order);

            Map<String, Object> filtersHome = new HashMap<>();
            filtersHome.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0)
            {//phan quyen
                filtersHome.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderHome = new LinkedHashMap<>();
            orderHome.put("homeName", Constant.ORDER.ASC);
            lstHome = HomeServiceImpl.getInstance().findList(filtersHome, orderHome);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        init();
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, true, true, true
        );
    }

    public void init()
    {
        try
        {
            customerRoomService = new GenericDaoImplNewV2<CustomerRoom, Long>()
            {
            };
            Map<String, Object> filtersCustomer = new HashMap<>();
            filtersCustomer.put("status-NEQ", Constant.STATUS.DELETE);
            if (groupUserId != null && groupUserId > 0)
            {//phan quyen
                filtersCustomer.put("groupUserId", groupUserId);
            }
            LinkedHashMap<String, String> orderCustomer = new LinkedHashMap<>();
            orderCustomer.put("customerName", Constant.ORDER.ASC);
            lstCustomer = CustomerServiceImpl.getInstance().findList(filtersCustomer, orderCustomer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void preAdd()
    {
        this.currRoom = new Room();
        isEdit = false;
    }

    public void preEdit(Room role)
    {
        try
        {
            currRoom = RoomServiceImpl.getInstance().findById(role.getRoomId());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currRoom.toString();
    }

    public void onSaveOrUpdate()
    {
        try
        {
            Map<String, Object> filter = new HashMap<>();
            filter.put("status-NEQ", Constant.STATUS.DELETE);
            filter.put("roomName-EXAC_IGNORE_CASE", currRoom.getRoomName());
            filter.put("homeId", currRoom.getHomeId());
            if (currRoom.getRoomId() != null)
            {
                filter.put("roomId-NEQ", currRoom.getRoomId());
            }

            List lst = RoomServiceImpl.getInstance().findList(filter);
            if (lst != null && !lst.isEmpty())
            {
                MessageUtil.setInfoMessage("Tên phòng trọ đã tồn tại!");
                return;
            }

            RoomServiceImpl.getInstance().saveOrUpdate(currRoom);

            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('roomDlg').hide();");

        }
        catch (Exception e)
        {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(Room catRole)
    {
        try
        {
            RoomServiceImpl.getInstance().delete(catRole);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }
//khach tro theo phong

    public void preShowLstCustomerRoom(Room room)
    {
        try
        {
            currRoom = RoomServiceImpl.getInstance().findById(room.getRoomId());
            Contract contract = BillController.getContractOfRoom(room.getRoomId());
            currRoom.setCurrContract(contract);
            //lay ds cutomer room
            Map<String, Object> filtersCustomerRoom = new HashMap<>();
            filtersCustomerRoom.put("status", Constant.STATUS.ACTIVE);
            filtersCustomerRoom.put("roomId", room.getRoomId());

            LinkedHashMap<String, String> orderCustomerRoom = new LinkedHashMap<>();
            orderCustomerRoom.put("customer.customerName", Constant.ORDER.ASC);
            lazyCustomerRoom = new LazyDataModelBase<CustomerRoom, Long>(customerRoomService,
                    filtersCustomerRoom, orderCustomerRoom);
            preAddCustomerRoom();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currRoom.toString();
    }

    public void preAddCustomerRoom()
    {
        currCustomerRoom = new CustomerRoom();
        currCustomerRoom.setRoomId(currRoom.getRoomId());
        currCustomerRoom.setType(Constant.CUSTOMER_ROOM.TYPE_EXTRA);
        currCustomerRoom.setStatus(Constant.STATUS.ACTIVE);
        if (currRoom.getCurrContract() != null)
        {
            currCustomerRoom.setContractId(currRoom.getCurrContract().getContractId());
        }
        oldObjectStr = null;
    }

    public void onSaveCustomerRoom()
    {
        try
        {
            if (currCustomerRoom.getContractId() == null)
            {
                MessageUtil.setErrorMessage("Phòng chưa có hợp đồng!");
                return;
            }
            Map<String, Object> filtersCustomerRoom = new HashMap<>();
            filtersCustomerRoom.put("status", Constant.STATUS.ACTIVE);
            filtersCustomerRoom.put("roomId", currRoom.getRoomId());
            filtersCustomerRoom.put("customerId", currCustomerRoom.getCustomerId());
            if (currCustomerRoom.getCustomerRoomId() != null)
            {
                filtersCustomerRoom.put("customerRoomId-NEQ", currCustomerRoom.getCustomerRoomId());
            }
            List lstCR = customerRoomService.findList(filtersCustomerRoom);
            if (lstCR != null && !lstCR.isEmpty())
            {
                MessageUtil.setErrorMessage("Khách trọ đã tồn tại!");
                return;
            }
            if (currCustomerRoom.getCreateTime() == null)
            {
                currCustomerRoom.setCreateTime(new Date());
                currCustomerRoom.setStartTime(new Date());
            }
            customerRoomService.saveOrUpdate(currCustomerRoom);
            //ghi log
            if (oldObjectStr != null)
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currCustomerRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }
            else
            {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currCustomerRoom.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
//            RequestContext.getCurrentInstance().execute("PF('customerRoomDlg').hide();");
//clear
            preAddCustomerRoom();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MessageUtil.setErrorMessageFromRes("common.message.fail");

        }
    }

    public void onDeleteCustomerRoom(CustomerRoom customerRoom)
    {
        try
        {
            customerRoomService.delete(customerRoom);
            LogActionController.writeLogAction(Constant.LOG_ACTION.DELETE, null, customerRoom.toString(), null,
                    this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void onChangeTypeToPrimary(CustomerRoom customerRoom)
    {
        try
        {
            Map<String, Object> filtersCustomerRoom = new HashMap<>();
            filtersCustomerRoom.put("status", Constant.STATUS.ACTIVE);
            filtersCustomerRoom.put("roomId", customerRoom.getRoomId());
            List<CustomerRoom> lstCustomerRooms = customerRoomService.findList(filtersCustomerRoom);
            //doi lai vai tro 

            if (lstCustomerRooms != null && !lstCustomerRooms.isEmpty())
            {
                for (CustomerRoom bo : lstCustomerRooms)
                {
                    if (!customerRoom.getCustomerId().equals(bo.getCustomerId()))
                    {
                        bo.setType(Constant.CUSTOMER_ROOM.TYPE_EXTRA);
                    }
                    else
                    {
                        bo.setType(Constant.CUSTOMER_ROOM.TYPE_PRIMARY);
                    }
                }
                customerRoomService.saveOrUpdate(lstCustomerRooms);
            }
            //nguoi dai dien hop dong
            Contract currContract = currRoom.getCurrContract();
            if (currContract != null)
            {
                oldObjectStr = currContract.toString();
                currContract.setCustomerId(customerRoom.getCustomerId());
                ContractServiceImpl.getInstance().saveOrUpdate(currContract);
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currContract.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, null, currCustomerRoom.toString(),
                    this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    public void onCheckoutCustomer(CustomerRoom customerRoom)
    {
        try
        {
            //check neu la nguoi o chinh thi khong duoc checkout
            if (Constant.CUSTOMER_ROOM.TYPE_PRIMARY.equals(customerRoom.getType()))
            {
                MessageUtil.setErrorMessage("Phải chọn lại khách ở chính khác trước khi bỏ khách khỏi phòng!");
                return;
            }
            oldObjectStr = customerRoom.toString();
            customerRoom.setStatus(Constant.CUSTOMER_ROOM.STATUS_CHECKOUT);
            customerRoomService.saveOrUpdate(customerRoom);
            LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, customerRoom.toString(),
                    this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));

            MessageUtil.setInfoMessageFromRes("common.message.success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public LazyDataModel<Room> getLazyDataModel()
    {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<Room> lazyDataModel)
    {
        this.lazyDataModel = lazyDataModel;
    }

    public Room getCurrRoom()
    {
        return currRoom;
    }

    public void setCurrRoom(Room currRoom)
    {
        this.currRoom = currRoom;
    }

    public List<Boolean> getColumnVisibale()
    {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale)
    {
        this.columnVisibale = columnVisibale;
    }

    public boolean isIsEdit()
    {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
    }

    public List<Home> getLstHome()
    {
        return lstHome;
    }

    public void setLstHome(List<Home> lstHome)
    {
        this.lstHome = lstHome;
    }

    public String getOldObjectStr()
    {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr)
    {
        this.oldObjectStr = oldObjectStr;
    }

    public Long getGroupUserId()
    {
        return groupUserId;
    }

    public void setGroupUserId(Long groupUserId)
    {
        this.groupUserId = groupUserId;
    }

    public GenericDaoImplNewV2<CustomerRoom, Long> getCustomerRoomService()
    {
        return customerRoomService;
    }

    public void setCustomerRoomService(GenericDaoImplNewV2<CustomerRoom, Long> customerRoomService)
    {
        this.customerRoomService = customerRoomService;
    }

    public CustomerRoom getCurrCustomerRoom()
    {
        return currCustomerRoom;
    }

    public void setCurrCustomerRoom(CustomerRoom currCustomerRoom)
    {
        this.currCustomerRoom = currCustomerRoom;
    }

    public boolean isIsEditCustomerRoom()
    {
        return isEditCustomerRoom;
    }

    public void setIsEditCustomerRoom(boolean isEditCustomerRoom)
    {
        this.isEditCustomerRoom = isEditCustomerRoom;
    }

    public List<Customer> getLstCustomer()
    {
        return lstCustomer;
    }

    public void setLstCustomer(List<Customer> lstCustomer)
    {
        this.lstCustomer = lstCustomer;
    }

    public LazyDataModel<CustomerRoom> getLazyCustomerRoom()
    {
        return lazyCustomerRoom;
    }

    public void setLazyCustomerRoom(LazyDataModel<CustomerRoom> lazyCustomerRoom)
    {
        this.lazyCustomerRoom = lazyCustomerRoom;
    }
//</editor-fold>

}
