package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 4/20/2016.
 */
public class StorePromotionBean {

    private String supplierId;
    private String discountAmount;
    private String message;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
