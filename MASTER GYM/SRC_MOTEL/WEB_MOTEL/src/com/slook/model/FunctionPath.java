/* 
* Copyright 2011 Viettel Telecom. All rights reserved. 
* VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */
package com.slook.model;

import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "function_path")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class FunctionPath {

    private Long functionPathId;
    private Long status;
    private String url;
    private String name;
    private String keyUrlName;
    private Long type;
    private Long parentId;
    private String typeName;

//    private List<FunctionPath> lstChildren;
//    @SequenceGenerator(name = "generator", sequenceName = "FUNCTION_PATH_SEQ", allocationSize = 1)
    @Id
//    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUNCTION_PATH_ID", unique = true, nullable = false, precision = 22, scale = 0)
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

    @Column(name = "URL", length = 250)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "NAME", length = 250)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FunctionPath() {
    }

    public FunctionPath(Long functionPathId) {
        this.functionPathId = functionPathId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.functionPathId);
        hash = 13 * hash + Objects.hashCode(this.status);
        hash = 13 * hash + Objects.hashCode(this.url);
        hash = 13 * hash + Objects.hashCode(this.name);
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
        final FunctionPath other = (FunctionPath) obj;
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.functionPathId, other.functionPathId)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

    @Transient
    public String getKeyUrlName() {
        keyUrlName = "";
        if (url != null) {
            keyUrlName += url;
        }
        keyUrlName += "_";
        if (name != null) {
            keyUrlName += name;
        }
        return keyUrlName;
    }

    public void setKeyUrlName(String keyUrlName) {
        this.keyUrlName = keyUrlName;
    }

    @Column(name = "type", precision = 22, scale = 0)
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Column(name = "parent_id", precision = 22, scale = 0)
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Transient
    public String getTypeName() {
        if (Constant.FUNCTION_PATH.TYPE_GROUP.equals(type)) {
            typeName = MessageUtil.getResourceBundleMessage("functionPath.type.TYPE_GROUP");
        } else if (Constant.FUNCTION_PATH.TYPE_FUNCTION.equals(type)) {
            typeName = MessageUtil.getResourceBundleMessage("functionPath.type.TYPE_FUNCTION");
        } else if (Constant.FUNCTION_PATH.TYPE_ACTION.equals(type)) {
            typeName = MessageUtil.getResourceBundleMessage("functionPath.type.TYPE_ACTION");
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public FunctionPath(String url, String name, Long type) {
        this.url = url;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
