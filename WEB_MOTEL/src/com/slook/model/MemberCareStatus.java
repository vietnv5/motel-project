package com.slook.model;

import javax.persistence.*;

/**
 * Created by Antz01 on 31/05/2017.
 */
@Entity
@Table(name = "MEMBER_CARE_STATUS", schema = "GYM_SERVICE", catalog = "")
public class MemberCareStatus
{
    private Long statusId;
    private String name;
    private Long roleId;

    private CatRole role;

    @Id
    @Column(name = "STATUS_ID")
    public Long getStatusId()
    {
        return statusId;
    }

    public void setStatusId(Long statusId)
    {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "NAME")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Basic
    @Column(name = "ROLE_ID")
    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    public CatRole getRole()
    {
        return role;
    }

    public void setRole(CatRole role)
    {
        this.role = role;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MemberCareStatus that = (MemberCareStatus) o;

        if (statusId != that.statusId)
        {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null)
        {
            return false;
        }
        if (roleId != that.roleId)
        {
            return false;
        }

        return true;
    }
}
