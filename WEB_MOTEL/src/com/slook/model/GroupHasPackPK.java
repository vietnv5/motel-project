package com.slook.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by xuanh on 5/27/2017.
 */
public class GroupHasPackPK implements Serializable {
    private Long groupPackId;
    private Long packId;

    @Column(name = "GROUP_PACK_ID")
    @Id
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Column(name = "PACK_ID")
    @Id
    public Long getPackId() {
        return packId;
    }

    public void setPackId(Long packId) {
        this.packId = packId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupHasPackPK that = (GroupHasPackPK) o;

        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null) return false;
        if (packId != null ? !packId.equals(that.packId) : that.packId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupPackId != null ? groupPackId.hashCode() : 0;
        result = 31 * result + (packId != null ? packId.hashCode() : 0);
        return result;
    }
}
