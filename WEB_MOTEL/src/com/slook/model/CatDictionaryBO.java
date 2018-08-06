package com.slook.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "cat_dictionary")
public class CatDictionaryBO implements Serializable {

    private String catCode;
    private Long editable;
    private String catName;
    private String regexValue;
    private String messageValid;
    private Long requireValue;

    @Id
    @Column(name = "CAT_CODE", unique = true, nullable = false, precision = 30, scale = 0)
    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    @Column(name = "EDITABLE", precision = 22, scale = 0)
    public Long getEditable() {
        return editable;
    }

    public void setEditable(Long editable) {
        this.editable = editable;
    }

    @Column(name = "CAT_NAME", length = 50)
    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public CatDictionaryBO() {
    }

    public CatDictionaryBO(String code) {
        this.catCode = code;
    }

    @Column(name = "regex_value", length = 250)
    public String getRegexValue() {
        return regexValue;
    }

    public void setRegexValue(String regexValue) {
        this.regexValue = regexValue;
    }

    @Column(name = "message_valid", length = 250)
    public String getMessageValid() {
        return messageValid;
    }

    public void setMessageValid(String messageValid) {
        this.messageValid = messageValid;
    }

    @Column(name = "require_value", precision = 22, scale = 0)
    public Long getRequireValue() {
        return requireValue;
    }

    public void setRequireValue(Long requireValue) {
        this.requireValue = requireValue;
    }

}
