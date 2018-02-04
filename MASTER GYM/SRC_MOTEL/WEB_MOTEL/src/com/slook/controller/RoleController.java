/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.controller;

import com.google.gson.Gson;
import com.slook.lazy.LazyDataModelBase;
import com.slook.model.CatRole;
import com.slook.model.FunctionPath;
import com.slook.model.RoleHasFunctionPath;
import com.slook.persistence.CatRoleServiceImpl;
import com.slook.persistence.FunctionPathServiceImpl;
import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author VietNV on Dec 18, 2017
 */
@ManagedBean
@ViewScoped
public class RoleController {

    protected static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    LazyDataModel<CatRole> lazyDataModel;
    CatRole currRole = new CatRole();
    private String oldObjectStr = null;

    private List<Boolean> columnVisibale = new ArrayList<>();
    private boolean isEdit = false;
    // grant function for role
    private List<FunctionPath> lstFunctionPaths = new ArrayList<>();
    private DualListModel<FunctionPath> dualLstFunctionPath = new DualListModel<>();
    private GenericDaoImplNewV2 roleHasFunctionPathService;
    // grant role by tree
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private TreeNode rootAction = new CheckboxTreeNode(new FunctionPath("", "Group Function", Constant.FUNCTION_PATH.TYPE_GROUP), null);
    ;;
    private TreeNode[] selectedNodeActions;

    public void onToggler(ToggleEvent e) {
        this.columnVisibale.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    @PostConstruct
    public void onStart() {

        try {
            roleHasFunctionPathService = new GenericDaoImplNewV2<RoleHasFunctionPath, Long>() {
            };
            LinkedHashMap<String, String> order = new LinkedHashMap<>();
            order.put("roleName", Constant.ORDER.ASC);
            Map<String, Object> filter = new HashMap<>();

            lazyDataModel = new LazyDataModelBase<CatRole, Long>(CatRoleServiceImpl.getInstance(), filter, order);

            Map<String, Object> filtersFunction = new HashMap<>();
            filtersFunction.put("status", Constant.CAT_PROMOTION.ENABLE);
            LinkedHashMap<String, String> orderFunction = new LinkedHashMap<>();
            orderFunction.put("name", Constant.ORDER.ASC);
            lstFunctionPaths = FunctionPathServiceImpl.getInstance().findList(filtersFunction, orderFunction);
            root = initCheckboxFunctionPath(lstFunctionPaths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        columnVisibale = Arrays.asList(true, true, true, true, true,
                true, true, true, true, true
        );
    }

    public void preAdd() {
        this.currRole = new CatRole();
        isEdit = false;
    }

    public void preEdit(CatRole role) {
        try {
            currRole = CatRoleServiceImpl.getInstance().findById(role.getRoleId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
        oldObjectStr = currRole.toString();
    }

    public void onSaveOrUpdate() {
        try {
            CatRoleServiceImpl.getInstance().saveOrUpdate(currRole);

            //ghi log
            if (oldObjectStr != null) {
                LogActionController.writeLogAction(Constant.LOG_ACTION.UPDATE, null, oldObjectStr, currRole.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            } else {
                LogActionController.writeLogAction(Constant.LOG_ACTION.INSERT, null, oldObjectStr, currRole.toString(),
                        this.getClass().getSimpleName(), (new Exception("get Name method").getStackTrace()[0].getMethodName()));
            }

            MessageUtil.setInfoMessageFromRes("info.save.success");
            RequestContext.getCurrentInstance().execute("PF('catRoleDlg').hide();");

        } catch (Exception e) {
            MessageUtil.setErrorMessageFromRes("error.save.unsuccess");
            e.printStackTrace();
        }
    }

    public void onDelete(CatRole catRole) {
        try {
            CatRoleServiceImpl.getInstance().delete(catRole);
            MessageUtil.setInfoMessageFromRes("common.message.success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="get/set">
    public LazyDataModel<CatRole> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<CatRole> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }

    public CatRole getCurrRole() {
        return currRole;
    }

    public void setCurrRole(CatRole currRole) {
        this.currRole = currRole;
    }

    public String getOldObjectStr() {
        return oldObjectStr;
    }

    public void setOldObjectStr(String oldObjectStr) {
        this.oldObjectStr = oldObjectStr;
    }

    public List<Boolean> getColumnVisibale() {
        return columnVisibale;
    }

    public void setColumnVisibale(List<Boolean> columnVisibale) {
        this.columnVisibale = columnVisibale;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public List<FunctionPath> getLstFunctionPaths() {
        return lstFunctionPaths;
    }

    public void setLstFunctionPaths(List<FunctionPath> lstFunctionPaths) {
        this.lstFunctionPaths = lstFunctionPaths;
    }

    public DualListModel<FunctionPath> getDualLstFunctionPath() {
        return dualLstFunctionPath;
    }

    public void setDualLstFunctionPath(DualListModel<FunctionPath> dualLstFunctionPath) {
        this.dualLstFunctionPath = dualLstFunctionPath;
    }

    public GenericDaoImplNewV2 getRoleHasFunctionPathService() {
        return roleHasFunctionPathService;
    }

    public void setRoleHasFunctionPathService(GenericDaoImplNewV2 roleHasFunctionPathService) {
        this.roleHasFunctionPathService = roleHasFunctionPathService;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public TreeNode getRootAction() {
        return rootAction;
    }

    public void setRootAction(TreeNode rootAction) {
        this.rootAction = rootAction;
    }

    public TreeNode[] getSelectedNodeActions() {
        return selectedNodeActions;
    }

    public void setSelectedNodeActions(TreeNode[] selectedNodeActions) {
        this.selectedNodeActions = selectedNodeActions;
    }

    //</editor-fold>
    //functionPath
    public void preEditHasFunctionPath(CatRole role) {
        List<FunctionPath> functionSource = new ArrayList<>();
        List<FunctionPath> functionTarget = new ArrayList<>();
        try {

            currRole = CatRoleServiceImpl.getInstance().findById(role.getRoleId());
            functionTarget.addAll(currRole.getFunctionPaths());
            for (FunctionPath bo : lstFunctionPaths) {
                if (!functionTarget.contains(bo)) {
                    functionSource.add(bo);
                }
            }
            dualLstFunctionPath = new DualListModel<>(functionSource, functionTarget);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;

        oldObjectStr = (new Gson()).toJson(functionTarget);;
    }

    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for (Object item : event.getItems()) {
            String s = ((FunctionPath) item).getUrl() + " " + (((FunctionPath) item).getName() != null ? ((FunctionPath) item).getName() : "");
            builder.append(s).append("<br />");
        }

        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }

    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }

    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }

    public void onSaveOrUpdateRoleHasFunctionPath() {
        try {
            List<FunctionPath> functionTarget = dualLstFunctionPath.getTarget();

            List<RoleHasFunctionPath> lstSelect = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspDel = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspResult = new ArrayList<>();

            if (functionTarget != null) {
                for (FunctionPath bo : functionTarget) {
                    lstSelect.add(new RoleHasFunctionPath(bo.getFunctionPathId(), currRole.getRoleId(), null, bo));
                }
            }
            if (currRole.getRoleHasFunctionPaths() != null
                    && currRole.getRoleHasFunctionPaths().size() > 0) {
                for (RoleHasFunctionPath bo : currRole.getRoleHasFunctionPaths()) {
                    if (lstSelect.contains(bo)) {
                        lstCspResult.add(bo);
                    } else {
                        lstCspDel.add(bo);// bo chuc nang
                    }
                }
            }
            for (RoleHasFunctionPath bo : lstSelect) {
                if (!lstCspResult.contains(bo)) {
                    lstCspResult.add(bo);//them chuc nang
                }

            }
            roleHasFunctionPathService.delete(lstCspDel);//bo chuc nang
            roleHasFunctionPathService.saveOrUpdate(lstCspResult);// cap nhat, bo sung chuc nang

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('roleHasFunctionPathDlg').hide();");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }

    }

    // tree grant role
    public TreeNode initCheckboxFunctionPath(List<FunctionPath> lstFunctionPaths) {
        TreeNode root = new CheckboxTreeNode(new FunctionPath("", "Group Function", Constant.FUNCTION_PATH.TYPE_GROUP), null);;
        List<FunctionPath> lstTotal = new ArrayList<>();
        lstTotal.addAll(lstFunctionPaths);
        createTreeFunctionPath(root, lstTotal);//tao node con
        // xu ly voi node co parentId khac null nhung khong thuoc node cha nao
        if (lstTotal.size() > 0) {
            lstTotal.forEach((bo) -> {
                if (!Constant.FUNCTION_PATH.TYPE_ACTION.equals(bo.getType())) {// bo qua cac node action
                    new CheckboxTreeNode(bo, root);
                }
            });
        }
        return root;
    }

    public void createTreeFunctionPath(TreeNode parentNode, List<FunctionPath> lstTotal) {
        if (lstTotal == null || lstTotal.size() == 0) {
            return;
        }
        int size = lstTotal.size();
        List<TreeNode> lstNode = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (lstTotal.get(i).getParentId() == null && parentNode.getParent() == null
                    || (lstTotal.get(i).getParentId() != null && parentNode.getData() != null && parentNode.getData() instanceof FunctionPath
                    && lstTotal.get(i).getParentId().equals(((FunctionPath) parentNode.getData()).getFunctionPathId()))) {
                if (!Constant.FUNCTION_PATH.TYPE_ACTION.equals(lstTotal.get(i).getType())) {// bo qua cac node action

                    TreeNode node = new CheckboxTreeNode(lstTotal.get(i), parentNode);
                    lstNode.add(node);
                }
                lstTotal.remove(i);
                size--;
                i--;
            }
        }
        lstNode.forEach((node) -> {
            createTreeFunctionPath(node, lstTotal);
        });

    }

    public TreeNode initCheckboxAction(List<FunctionPath> lstFunctionPaths, List<Long> lstFunctionPathIdSelect) {
        TreeNode root = new CheckboxTreeNode(new FunctionPath("", "Group Function", Constant.FUNCTION_PATH.TYPE_GROUP), null);
        List<FunctionPath> lstTotal = new ArrayList<>();
        lstTotal.addAll(lstFunctionPaths);
        createTreeAction(root, lstTotal, lstFunctionPathIdSelect);//tao node con
        // xu ly voi node co parentId khac null nhung khong thuoc node cha nao
        /*if (lstTotal.size() > 0) {
            lstTotal.forEach((bo) -> {
                if (!Constant.FUNCTION_PATH.TYPE_ACTION.equals(bo.getType())) {// bo qua cac node action
                    new CheckboxTreeNode(bo, root);
                }
            });
        }*/

        return root;
    }

    public Long createTreeAction(TreeNode parentNode, List<FunctionPath> lstTotal, List<Long> lstFunctionPathIdSelect) {
        Long typeHasAction = ((FunctionPath) parentNode.getData()).getType();
        if (lstTotal == null || lstTotal.size() == 0) {
            return typeHasAction;
        }
        int size = lstTotal.size();
        List<TreeNode> lstNode = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (lstTotal.get(i).getParentId() == null && parentNode.getParent() == null
                    || (lstTotal.get(i).getParentId() != null && parentNode.getData() != null && parentNode.getData() instanceof FunctionPath
                    && lstTotal.get(i).getParentId().equals(((FunctionPath) parentNode.getData()).getFunctionPathId()))) {

                TreeNode node = new CheckboxTreeNode(lstTotal.get(i), parentNode);
                if (Constant.FUNCTION_PATH.TYPE_FUNCTION.equals(lstTotal.get(i).getType())
                        && !lstFunctionPathIdSelect.contains(lstTotal.get(i).getFunctionPathId())) {// chuc nang khong duoc gan quyen thi bo di
                    continue;
                }
                lstNode.add(node);
                lstTotal.remove(i);
                size--;
                i--;
            }
        }
        for (TreeNode node : lstNode) {
            if (!Constant.FUNCTION_PATH.TYPE_ACTION.equals(((FunctionPath) parentNode.getData()).getType())) {// chi them voi node hien tai la chuc nang
                Long type = createTreeAction(node, lstTotal, lstFunctionPathIdSelect);
                if (!Constant.FUNCTION_PATH.TYPE_ACTION.equals(type)) {// xoa node  neu khong co action
                    parentNode.getChildren().remove(node);
                } else {
                    typeHasAction = type;
                }
            }
        }
        return typeHasAction;
    }

    //pre edit for tree
    public List<TreeNode> getListTreeNodeSelect(TreeNode node, List<Long> lstFunctionPathIdSelect) {
        List<TreeNode> lst = new ArrayList<>();
        if (node == null) {
            return lst;
        } else if (lstFunctionPathIdSelect == null || lstFunctionPathIdSelect.size() == 0) {// unable selected
            node.setSelected(false);
            node.setExpanded(false);
            if (node.getChildCount() > 0) {
                for (TreeNode nodeChildren : node.getChildren()) {
                    lst.addAll(getListTreeNodeSelect(nodeChildren, lstFunctionPathIdSelect));
                }
            }
        }
        if (lstFunctionPathIdSelect.contains(((FunctionPath) node.getData()).getFunctionPathId())) {
            lst.add(node);
            node.setSelected(true);
        } else {
            node.setSelected(false);

        }
        node.setExpanded(false);
        if (node.getChildCount() > 0) {
            for (TreeNode nodeChildren : node.getChildren()) {
                lst.addAll(getListTreeNodeSelect(nodeChildren, lstFunctionPathIdSelect));
            }
        }
        return lst;
    }

    public void preEditTreeHasFunctionPath(CatRole role) {
        List<FunctionPath> functionTarget = new ArrayList<>();
        try {

            currRole = CatRoleServiceImpl.getInstance().findById(role.getRoleId());
            functionTarget.addAll(currRole.getFunctionPaths());
            List<Long> lstFunctionPathIdSelect = CommonUtil.getListAttributeInList((List) functionTarget, "functionPathId");
            List<TreeNode> lstNodeSelect = getListTreeNodeSelect(root, lstFunctionPathIdSelect);
            selectedNodes = lstNodeSelect.toArray(new TreeNode[lstNodeSelect.size()]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;

        oldObjectStr = (new Gson()).toJson(functionTarget);

    }

    public void onSaveOrUpdateRoleSelectedMultiple(TreeNode[] nodes) {
        List<FunctionPath> functionTarget = new ArrayList<>();

        if (nodes != null && nodes.length > 0) {
//            StringBuilder builder = new StringBuilder();
            for (TreeNode node : nodes) {
                functionTarget.add(((FunctionPath) node.getData()));
//                builder.append(((FunctionPath) node.getData()).getName().toString());
//                builder.append("<br />");
            }
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", builder.toString());
//            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        try {

            List<RoleHasFunctionPath> lstSelect = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspDel = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspResult = new ArrayList<>();

            if (functionTarget != null) {
                for (FunctionPath bo : functionTarget) {
                    lstSelect.add(new RoleHasFunctionPath(bo.getFunctionPathId(), currRole.getRoleId(), null, bo));
                }
            }
            if (currRole.getRoleHasFunctionPaths() != null
                    && currRole.getRoleHasFunctionPaths().size() > 0) {
                for (RoleHasFunctionPath bo : currRole.getRoleHasFunctionPaths()) {
                    if (lstSelect.contains(bo)) {
                        lstCspResult.add(bo);
                    } else {
                        lstCspDel.add(bo);// bo chuc nang
                    }
                }
            }
            for (RoleHasFunctionPath bo : lstSelect) {
                if (!lstCspResult.contains(bo)) {
                    lstCspResult.add(bo);//them chuc nang
                }

            }
            roleHasFunctionPathService.delete(lstCspDel);//bo chuc nang
            roleHasFunctionPathService.saveOrUpdate(lstCspResult);// cap nhat, bo sung chuc nang

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('grantRoleDlg').hide();");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }
// edit action

    public void preEditTreeHasAction(CatRole role) {
        List<FunctionPath> functionTarget = new ArrayList<>();
        List<FunctionPath> functionTargetAction = new ArrayList<>();
        try {

            currRole = CatRoleServiceImpl.getInstance().findById(role.getRoleId());
            functionTarget.addAll(currRole.getFunctionPaths());
            List<Long> lstFunctionPathIdSelect = CommonUtil.getListAttributeInList((List) functionTarget, "functionPathId");

            rootAction = initCheckboxAction(lstFunctionPaths, lstFunctionPathIdSelect);
            //lay danh sach cac action
            functionTarget.forEach((t) -> {
                if (Constant.FUNCTION_PATH.TYPE_ACTION.equals(t.getType())) {
                    functionTargetAction.add(t);
                }
            });
            List<Long> lstFunctionPathIdActionSelect = CommonUtil.getListAttributeInList((List) functionTargetAction, "functionPathId");
            List<TreeNode> lstNodeSelect = getListTreeNodeSelect(rootAction, lstFunctionPathIdActionSelect);
            selectedNodeActions = lstNodeSelect.toArray(new TreeNode[lstNodeSelect.size()]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;

        oldObjectStr = (new Gson()).toJson(functionTarget);
    }

    public void onSaveOrUpdateRoleActionSelectedMultiple(TreeNode[] nodes) {
        List<FunctionPath> functionTarget = new ArrayList<>();

        if (nodes != null && nodes.length > 0) {
            for (TreeNode node : nodes) {
                if (Constant.FUNCTION_PATH.TYPE_ACTION.equals(((FunctionPath) node.getData()).getType())) {
                    functionTarget.add(((FunctionPath) node.getData()));
                }
            }
        }
        try {

            List<RoleHasFunctionPath> lstSelect = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspDel = new ArrayList<>();
            List<RoleHasFunctionPath> lstCspResult = new ArrayList<>();

            List<RoleHasFunctionPath> roleHasFunctionPathCurr = currRole.getRoleHasFunctionPaths();
            List<RoleHasFunctionPath> roleHasActionCurr = new ArrayList<>();
            if (roleHasFunctionPathCurr != null) {
                for (RoleHasFunctionPath bo : roleHasFunctionPathCurr) {
                    if (bo.getFunctionPath() != null && Constant.FUNCTION_PATH.TYPE_ACTION.equals(bo.getFunctionPath().getType())) {
                        roleHasActionCurr.add(bo);
                    }
                }
            }

            if (functionTarget != null) {
                for (FunctionPath bo : functionTarget) {
                    lstSelect.add(new RoleHasFunctionPath(bo.getFunctionPathId(), currRole.getRoleId(), null, bo));
                }
            }
            if (roleHasActionCurr != null
                    && roleHasActionCurr.size() > 0) {
                for (RoleHasFunctionPath bo : roleHasActionCurr) {
                    if (lstSelect.contains(bo)) {
                        lstCspResult.add(bo);
                    } else {
                        if (bo.getFunctionPath() != null && Constant.FUNCTION_PATH.TYPE_ACTION.equals(bo.getFunctionPath().getType())) {
                            lstCspDel.add(bo);// bo action
                        }
                    }
                }
            }
            for (RoleHasFunctionPath bo : lstSelect) {
                if (!lstCspResult.contains(bo)) {
                    lstCspResult.add(bo);//them chuc nang
                }

            }
            roleHasFunctionPathService.delete(lstCspDel);//bo chuc nang
            roleHasFunctionPathService.saveOrUpdate(lstCspResult);// cap nhat, bo sung chuc nang

            MessageUtil.setInfoMessageFromRes("common.message.success");
            RequestContext.getCurrentInstance().execute("PF('grantRoleActionDlg').hide();");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            MessageUtil.setErrorMessageFromRes("common.message.fail");
        }
    }

}
