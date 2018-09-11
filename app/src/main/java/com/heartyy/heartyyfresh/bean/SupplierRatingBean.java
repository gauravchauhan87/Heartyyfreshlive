package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 4/28/2016.
 */
public class SupplierRatingBean {

    private String supplierId;
    private String orderSupplierId;
    private String supplierName;
    private int rating = 0;


    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrderSupplierId() {
        return orderSupplierId;
    }

    public void setOrderSupplierId(String orderSupplierId) {
        this.orderSupplierId = orderSupplierId;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
