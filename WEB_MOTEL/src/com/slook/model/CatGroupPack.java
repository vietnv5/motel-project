package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.util.Constant;
import com.slook.util.MessageUtil;
import com.slook.util.Util;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Created by T430 on 4/20/2017.
 */
@Entity
@Table(name = "CAT_GROUP_PACK")
public class CatGroupPack {

    List<CatPack> packs = new ArrayList<>(0);
    List<CatPack> pack2s = new ArrayList<>(0);
    Double price;
    Long among; //day
    private Long groupPackId;
    private String groupPackName;
    private String groupPackCode;
    private Long status;
    private Date dateStartProvide;
    private Date dateEndProvide;
    private String tag;
    private String createBy;
    List<CatRoom> rooms = new ArrayList<>(0);
    List<CatRoom> room2s = new ArrayList<>(0);
    List<PromotionGroupPack> promotionGroupPacks = new ArrayList<>(0);
    List<GroupHasPack> groupHasPacks = new ArrayList<>(0);
    List<GroupHasPack> groupHasPack2s = new ArrayList<>(0);

    Long bonusDay;
    Double sale;
    Long period;
    boolean editPrice = false;

    private Long type;
    private String typeName;
    private Long requireTrainer;


    @Id
    @Column(name = "GROUP_PACK_ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @SequenceGenerator(name = "generator", sequenceName = "GROUP_PACK_SEQ", allocationSize = 1)
    public Long getGroupPackId() {
        return groupPackId;
    }

    public void setGroupPackId(Long groupPackId) {
        this.groupPackId = groupPackId;
    }

    @Basic
    @Column(name = "GROUP_PACK_NAME", nullable = true, length = 50)
    public String getGroupPackName() {
        return groupPackName;
    }

    public void setGroupPackName(String groupPackName) {
        this.groupPackName = groupPackName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatGroupPack that = (CatGroupPack) o;

        if (groupPackId != null ? !groupPackId.equals(that.groupPackId) : that.groupPackId != null) {
            return false;
        }
        if (groupPackName != null ? !groupPackName.equals(that.groupPackName) : that.groupPackName != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupPackId != null ? groupPackId.hashCode() : 0;
        result = 31 * result + (groupPackName != null ? groupPackName.hashCode() : 0);
        return result;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinTable(name = "GROUP_HAS_PACK", joinColumns = {
        @JoinColumn(name = "GROUP_PACK_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "PACK_ID",
                        nullable = false, updatable = false)})
//    @Transient
    public List<CatPack> getPacks() {
        return packs;
    }

    public void setPacks(List<CatPack> packs) {
        this.packs = packs;
    }

    @Basic
    @Column(name = "PRICE")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "AMONG")
    public Long getAmong() {
        return among;
    }

    public void setAmong(Long among) {
        this.among = among;
    }

    @Transient
    public List<CatPack> getPack2s() {
        return pack2s;
    }

    public void setPack2s(List<CatPack> pack2s) {
        this.pack2s = pack2s;
    }

    @Basic
    @Column(name = "GROUP_PACK_CODE")
    public String getGroupPackCode() {
        return groupPackCode;
    }

    public void setGroupPackCode(String groupPackCode) {
        this.groupPackCode = groupPackCode;
    }

    @Basic
    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Basic
    @Column(name = "DATE_START_PROVIDE")
    public Date getDateStartProvide() {
        return dateStartProvide;
    }

    public void setDateStartProvide(Date createDate) {
        this.dateStartProvide = createDate;
    }

    @Column(name = "DATE_END_PROVIDE")
    public Date getDateEndProvide() {
        return dateEndProvide;
    }

    public void setDateEndProvide(Date dateEndProvide) {
        this.dateEndProvide = dateEndProvide;
    }

    @Basic
    @Column(name = "TAG")
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JoinTable(name = "GROUP_PACK_ROOM", joinColumns = {
        @JoinColumn(name = "GROUP_PACK_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "ROOM_ID",
                        nullable = false, updatable = false)})
    public List<CatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<CatRoom> rooms) {
        this.rooms = rooms;
    }

    @Transient
    public List<CatRoom> getRoom2s() {
        return room2s;
    }

    public void setRoom2s(List<CatRoom> room2s) {
        this.room2s = room2s;
    }

    public void computingPrice() {
        price = 0.0;
        List<GroupHasPack> groupHasPackTemps = new ArrayList<>(groupHasPack2s);
        groupHasPack2s.clear();
        for (CatPack pack2 : pack2s) {
            if (pack2.getPrice() != null) {
                price += pack2.getPrice();
            }
            GroupHasPack groupHasPack = new GroupHasPack();
            groupHasPack.setGroupPackId(groupPackId);
            groupHasPack.setPackId(pack2.getPackId());
            groupHasPack.setPack(pack2);

            for (GroupHasPack hasPack : groupHasPackTemps) {
                if (hasPack.getPackId().equals(pack2.getPackId())) {
                    groupHasPack.setSale(hasPack.getSale());
                    groupHasPack.setSaleType(hasPack.getSaleType());
                    groupHasPack.setPromotionPack2(hasPack.isPromotionPack2());
                    break;
                }
            }
            groupHasPack2s.add(groupHasPack);
        }
    }

    public void computingPrice2() {
        price = 0.0;
        for (GroupHasPack groupHasPack2 : groupHasPack2s) {
            if (groupHasPack2.isPromotionPack2()) {
                continue;
            }
            price += groupHasPack2.getPack().getPrice();
            if (groupHasPack2.getSaleType() != null && groupHasPack2.getSaleType().equals(1L)) {
                price -= groupHasPack2.getSale() * groupHasPack2.getPack().getPrice() / 100;
            } else if (groupHasPack2.getSaleType() != null && groupHasPack2.getSaleType().equals(1L)) {
                price -= groupHasPack2.getSale();
            }
        }
//        if (sale!=null){
//            price -=sale;
//        }

    }

    public void computingDay() {
        try {
            DateTime date1 = new DateTime(dateStartProvide.getTime());
            DateTime date2 = new DateTime(dateEndProvide.getTime());
            among = Long.valueOf(Days.daysBetween(date1, date2).getDays());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "groupPackId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<PromotionGroupPack> getPromotionGroupPacks() {
        return promotionGroupPacks;
    }

    public void setPromotionGroupPacks(List<PromotionGroupPack> promotionGroupPacks) {
        this.promotionGroupPacks = promotionGroupPacks;
    }

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "groupPackId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<GroupHasPack> getGroupHasPacks() {
        return groupHasPacks;
    }

    public void setGroupHasPacks(List<GroupHasPack> groupHasPacks) {
//        packs=new ArrayList<>(0);
//        for (GroupHasPack i : groupHasPacks) {
//            if(i.getPack()!=null)
//            packs.add(i.getPack());
//        }
        this.groupHasPacks = groupHasPacks;
    }

    @Transient
    public List<GroupHasPack> getGroupHasPack2s() {
        return groupHasPack2s;
    }

    public void setGroupHasPack2s(List<GroupHasPack> groupHasPack2s) {
        this.groupHasPack2s = groupHasPack2s;
    }

    public void createGroupPackCode() {
        String code = Util.createCode(groupPackName);
        String hqlCheckCode = "select count(*) from CatGroupPack where groupPackCode like ?||'-%'";
        if (code.isEmpty()) {
            return;
        }
        List<?> counts = new GenericDaoImplNewV2<CatGroupPack, Long>() {
        }.findListAll(hqlCheckCode, code);
        if (counts.size() > 0) {
            groupPackCode = code + "-" + (Long.valueOf(counts.get(0).toString()) + 1);
        }
    }

    @Column(name = "BONUS_DAY")
    public Long getBonusDay() {
        return bonusDay;
    }

    public void setBonusDay(Long bonusDay) {
        this.bonusDay = bonusDay;
    }

    @Column
    public Double getSale() {
        return sale;
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Transient
    public boolean getEditPrice() {
        return editPrice;
    }

    public void setEditPrice(boolean editPrice) {
        this.editPrice = editPrice;
    }

    private String statusName;

    @Transient
    public String getStatusName() {
        if (status != null) {
            if (status.equals(0l)) {
                statusName = MessageUtil.getResourceBundleMessage("datatable.header.pack.status0");
            } else if (status.equals(1l)) {
                statusName = MessageUtil.getResourceBundleMessage("datatable.header.pack.status1");
            } else if (status.equals(2l)) {
                statusName = MessageUtil.getResourceBundleMessage("datatable.header.pack.status2");
            } else if (status.equals(3l)) {
                statusName = MessageUtil.getResourceBundleMessage("datatable.header.pack.status3");
            }
        }

        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Column(name = "PERIOD")
    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    @Column(name = "TYPE")
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    @Transient
    public String getTypeName() {
        if (type != null) {
            if (type.equals(Constant.GROUP_PACK_TYPE.TYPE_LE)) {
                typeName = MessageUtil.getResourceBundleMessage("catGroupPack.type1");
            } else if (type.equals(Constant.GROUP_PACK_TYPE.TYPE_HV_LUOT)) {
                typeName = MessageUtil.getResourceBundleMessage("catGroupPack.type2");
            } else if (type.equals(Constant.GROUP_PACK_TYPE.TYPE_HV_TIME)) {
                typeName = MessageUtil.getResourceBundleMessage("catGroupPack.type3");
            } else if (type.equals(Constant.GROUP_PACK_TYPE.TYPE_GROUP)) {
                typeName = MessageUtil.getResourceBundleMessage("catGroupPack.type4");
            }
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name = "require_trainer")
    public Long getRequireTrainer() {
        return requireTrainer;
    }

    public void setRequireTrainer(Long requireTrainer) {
        this.requireTrainer = requireTrainer;
    }


}
