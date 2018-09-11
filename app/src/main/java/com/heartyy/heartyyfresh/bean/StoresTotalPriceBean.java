package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 1/27/2016.
 */
public class StoresTotalPriceBean {

    private String storeName;
    private String storePrice;
    private String isAvailable;
    private String storeMessage;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(String storePrice) {
        this.storePrice = storePrice;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getStoreMessage() {
        return storeMessage;
    }

    public void setStoreMessage(String storeMessage) {
        this.storeMessage = storeMessage;
    }
}
