/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.webservice.object;

/**
 * @author xuanh
 */
public class UserBO
{
    private String userName;
    private String fullDeptName;
    private String deptName;

    @Override
    public String toString()
    {
        return new StringBuilder().append("UserBO{")
                .append("userName=").append(userName)
                .append("fullDeptName=").append(fullDeptName)
                .append("deptName=").append(deptName)
                .append("}").toString();
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFullDeptName()
    {
        return fullDeptName;
    }

    public void setFullDeptName(String fullDeptName)
    {
        this.fullDeptName = fullDeptName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }
}
