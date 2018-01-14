package com.slook.webservice.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xuanh
 */
public class RequestInputBO implements Serializable {
    private String code;
    private int compressData;
    private List<ParameterBO> params;
    private String query;

    public RequestInputBO() {
        params = new ArrayList<ParameterBO>();
    }

    public RequestInputBO(String code) {
        params = new ArrayList<ParameterBO>();
        this.code = code;
    }

    public RequestInputBO(String code, List<ParameterBO> params) {
        this.code = code;
        this.params = params;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ParameterBO> getParams() {
        return params;
    }

    public void setParams(List<ParameterBO> params) {
        this.params = params;
    }

    public int getCompressData() {
        return compressData;
    }

    public void setCompressData(int compressData) {
        this.compressData = compressData;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
