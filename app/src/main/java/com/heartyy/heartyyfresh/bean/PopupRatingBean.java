package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 4/28/2016.
 */
public class PopupRatingBean {

    private String userId;
    private String orderId;
    private String displayOrderId;
    private String driverName;
    private String driverPicture;
    private String driverId;
    private String actualDeliveryDate;
    private String actualDeliveryTime;
    private List<SupplierRatingBean> supplierRatingBeanList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<SupplierRatingBean> getSupplierRatingBeanList() {
        return supplierRatingBeanList;
    }

    public void setSupplierRatingBeanList(List<SupplierRatingBean> supplierRatingBeanList) {
        this.supplierRatingBeanList = supplierRatingBeanList;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPicture() {
        return driverPicture;
    }

    public void setDriverPicture(String driverPicture) {
        this.driverPicture = driverPicture;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(String displayOrderId) {
        this.displayOrderId = displayOrderId;
    }

    public String getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(String actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(String actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }
}
