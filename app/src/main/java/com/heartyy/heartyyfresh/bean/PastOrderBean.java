package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/7/2015.
 */
public class PastOrderBean {

    private String orderId;
    private String displayOrderId;
    private String itemCount;
    private String amount;
    private String deliveryDate;
    private String deliveryTime;
    private String rating;

    List<PastOrderSupplierBean> pastOrderSupplierBeans;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(String displayOrderId) {
        this.displayOrderId = displayOrderId;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<PastOrderSupplierBean> getPastOrderSupplierBeans() {
        return pastOrderSupplierBeans;
    }

    public void setPastOrderSupplierBeans(List<PastOrderSupplierBean> pastOrderSupplierBeans) {
        this.pastOrderSupplierBeans = pastOrderSupplierBeans;
    }
}