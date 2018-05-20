package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Created by T430 on 4/12/2017.
 */
@Entity
@Table(name = "CAT_ROLE", catalog = "")
public class CatRole implements Serializable{

    private Long roleId;
    private String roleName;
    private String roleCode;
    List<FunctionPath> functionPaths = new ArrayList<>(0);
    List<RoleHasFunctionPath> roleHasFunctionPaths;
    private Long status;
    private String statusName;

    @Id
    @Column(name = "ROLE_ID", nullable = false, precision = 0)
//    @SequenceGenerator(name = "generator", sequenceName = "CAT_ROLE_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "ROLE_NAME", nullable = true, length = 250)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "ROLE_CODE", nullable = true, length = 200)
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatRole catRole = (CatRole) o;

        if (roleId != null ? !roleId.equals(catRole.roleId) : catRole.roleId != null) {
            return false;
        }
        if (roleName != null ? !roleName.equals(catRole.roleName) : catRole.roleName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        return result;
    }

    @Transient
    public List<FunctionPath> getFunctionPaths() {
        if (functionPaths != null && !functionPaths.isEmpty()) {
            return functionPaths;
        }
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("roleId", roleId);
            List<RoleHasFunctionPath> lst = new GenericDaoImplNewV2<RoleHasFunctionPath, Long>() {
            }.findList(filter);
            if (lst != null && lst.size() > 0) {
                functionPaths = CommonUtil.getListAttributeInList((List) lst, "functionPath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return functionPaths;
    }

    public void setFunctionPaths(List<FunctionPath> functionPaths) {
        this.functionPaths = functionPaths;
    }

    @Column(name = "status", nullable = true, precision = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Transient
    public String getStatusName() {
        if (status != null) {
            if (status.equals(Constant.CAT_PROMOTION.ENABLE)) {
                statusName = MessageUtil.getResourceBundleMessage("common.ACTIVE");
            }
            if (status.equals(Constant.CAT_PROMOTION.DISABLE)) {
                statusName = MessageUtil.getResourceBundleMessage("common.INACTIVE");
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "roleId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<RoleHasFunctionPath> getRoleHasFunctionPaths() {
        return roleHasFunctionPaths;
    }

    public void setRoleHasFunctionPaths(List<RoleHasFunctionPath> roleHasFunctionPaths) {
        this.roleHasFunctionPaths = roleHasFunctionPaths;
    }

}
