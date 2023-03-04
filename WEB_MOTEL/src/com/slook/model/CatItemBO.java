package com.slook.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "cat_item")
public class CatItemBO implements Serializable
{

    private String code;
    private String catCode;
    private String description;
    private String note;
    private Date updateTime;
    private Long itemId;
    private String value;
    private String name;
    private String itemIdStr;
    private static final long serialVersionUID = 1L;

    @Column(name = "CODE", length = 30)
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @Column(name = "CAT_CODE", length = 30)
    public String getCatCode()
    {
        return catCode;
    }

    public void setCatCode(String catCode)
    {
        this.catCode = catCode;
    }

    @Column(name = "DESCRIPTION", length = 200)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "NOTE", length = 250)
    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", length = 7)
    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    //    @SequenceGenerator(name = "generator", sequenceName = "CAT_ITEM_SEQ", allocationSize = 1)
    @Id
//    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    @Column(name = "VALUE", length = 255)
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Column(name = "NAME", length = 250)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Transient
    public String getItemIdStr()
    {
        return String.valueOf(itemId);
    }

    public void setItemIdStr(String itemIdStr)
    {
        this.itemIdStr = itemIdStr;
    }

    public CatItemBO()
    {
    }

    public CatItemBO(Long itemId)
    {
        this.itemId = itemId;
    }

    public CatItemBO(String code, Long itemId, String value, String name)
    {
        this.code = code;
        this.itemId = itemId;
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
