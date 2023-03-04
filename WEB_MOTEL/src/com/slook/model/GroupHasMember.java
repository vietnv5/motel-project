/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.model;

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
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "GROUP_HAS_MEMBER")
/**
 *
 * @author: ThuanNHT
 * @version: 1.0
 * @since: 1.0
 */
public class GroupHasMember
{

    private Long groupMemberId;
    private Long status;
    private Long memberId;
    private Long id;
    private Member member;

    @Column(name = "GROUP_MEMBER_ID", precision = 22, scale = 0)
    public Long getGroupMemberId()
    {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId)
    {
        this.groupMemberId = groupMemberId;
    }

    @Column(name = "STATUS", precision = 22, scale = 0)
    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    @Column(name = "MEMBER_ID", precision = 22, scale = 0)
    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    @SequenceGenerator(name = "generator", sequenceName = "GROUP_HAS_MEMBER_SEQ", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public GroupHasMember()
    {
    }

    public GroupHasMember(Long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.groupMemberId);
        hash = 47 * hash + Objects.hashCode(this.status);
        hash = 47 * hash + Objects.hashCode(this.memberId);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final GroupHasMember other = (GroupHasMember) obj;
        if (!Objects.equals(this.groupMemberId, other.groupMemberId))
        {
            return false;
        }
        if (!Objects.equals(this.status, other.status))
        {
            return false;
        }
        if (!Objects.equals(this.memberId, other.memberId))
        {
            return false;
        }
        return true;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

}
