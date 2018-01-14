//package com.slook.model;
//
//import javax.persistence.*;
//
///**
// * Created by T430 on 4/20/2017.
// */
//@Entity
//@Table(name = "CAT_SERVICE",  catalog = "")
//public class CatService {
//    private Long serviceId;
//    private String serviceName;
//    private String serviceType;
//
//    @Id
//    @Column(name = "SERVICE_ID", nullable = false, precision = 0)
//    public Long getServiceId() {
//        return serviceId;
//    }
//
//    public void setServiceId(Long serviceId) {
//        this.serviceId = serviceId;
//    }
//
//    @Basic
//    @Column(name = "SERVICE_NAME", nullable = true, length = 50)
//    public String getServiceName() {
//        return serviceName;
//    }
//
//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }
//
//    @Basic
//    @Column(name = "SERVICE_TYPE", nullable = true, length = 20)
//    public String getServiceType() {
//        return serviceType;
//    }
//
//    public void setServiceType(String serviceType) {
//        this.serviceType = serviceType;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        CatService that = (CatService) o;
//
//        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null) return false;
//        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) return false;
//        if (serviceType != null ? !serviceType.equals(that.serviceType) : that.serviceType != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = serviceId != null ? serviceId.hashCode() : 0;
//        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
//        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);
//        return result;
//    }
//}
