/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "ROLE_HAS_FUNCTION_PATH")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class RoleHasFunctionPath {

    private Long functionPathId;
    private Long status;
    private Long roleId;
    private Long id;
    private FunctionPath functionPath;

    @Column(name = "FUNCTION_PATH_ID", precision = 22, scale = 0)
    public Long getFunctionPathId() {
        return functionPathId;
    }

    public void setFunctionPathId(Long functionPathId) {
        this.functionPathId = functionPathId;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "ROLE_ID", precision = 22, scale = 0)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "ROLE_HAS_FUNCTION_PATH_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleHasFunctionPath() {
    }

    public RoleHasFunctionPath(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "FUNCTION_PATH_ID", referencedColumnName = "FUNCTION_PATH_ID", insertable = false, updatable = false)
    public FunctionPath getFunctionPath() {
        return functionPath;
    }

    public void setFunctionPath(FunctionPath functionPath) {
        this.functionPath = functionPath;
    }

    public RoleHasFunctionPath(Long functionPathId, Long roleId, Long id,FunctionPath functionPath) {
        this.functionPathId = functionPathId;
        this.status = Constant.STATUS.ACTIVE;
        this.roleId = roleId;
        this.id = id;
        this.functionPath=functionPath;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.functionPathId);
        hash = 89 * hash + Objects.hashCode(this.roleId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RoleHasFunctionPath other = (RoleHasFunctionPath) obj;
        if (!Objects.equals(this.functionPathId, other.functionPathId)) {
            return false;
        }
        if (!Objects.equals(this.roleId, other.roleId)) {
            return false;
        }
        return true;
    }

}
