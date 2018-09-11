package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by amitvashist1 on 03/02/16.
 */
public class OrderTrackBean {
    private String orderStatusId;
    private String orderSupplierId;
    private String bagsUsed;
    private String title;
    private String message;
    private String status;
    private String time;
    private String deliveryBoy;
    private String supplierName;
    private String deliveryDate;
    private String deliveryTime;

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderSupplierId() {
        return orderSupplierId;
    }

    public void setOrderSupplierId(String orderSupplierId) {
        this.orderSupplierId = orderSupplierId;
    }

    public String getBagsUsed() {
        return bagsUsed;
    }

    public void setBagsUsed(String bagsUsed) {
        this.bagsUsed = bagsUsed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(String deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
