package com.slook.webservice.object;

import java.io.Serializable;


/**
 * @author xuanh
 */
public class AuthorityBO implements Serializable {
    private Integer requestId;
    private String userName;
    private String password;

    public AuthorityBO() {
    }

    public AuthorityBO(Integer requestId, String userName, String password) {
        this.requestId = requestId;
        this.userName = userName;
        this.password = password;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
