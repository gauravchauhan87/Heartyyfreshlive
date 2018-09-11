package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class StoreBean {

    private String hasAddress;
    private String avalilable;
    private String promotionCount;
    private String currentOrderCount;
    private String rating;
    private List<SuppliersBean> suppliersBeanList;
    private List<PromotionBean> promotionBeanList;
    private NewUserPromotionBean newUserPromotionBean;


    public String getHasAddress() {
        return hasAddress;
    }

    public void setHasAddress(String hasAddress) {
        this.hasAddress = hasAddress;
    }

    public String getAvalilable() {
        return avalilable;
    }

    public void setAvalilable(String avalilable) {
        this.avalilable = avalilable;
    }

    public List<SuppliersBean> getSuppliersBeanList() {
        return suppliersBeanList;
    }

    public void setSuppliersBeanList(List<SuppliersBean> suppliersBeanList) {
        this.suppliersBeanList = suppliersBeanList;
    }

    public List<PromotionBean> getPromotionBeanList() {
        return promotionBeanList;
    }

    public void setPromotionBeanList(List<PromotionBean> promotionBeanList) {
        this.promotionBeanList = promotionBeanList;
    }

    public String getPromotionCount() {
        return promotionCount;
    }

    public void setPromotionCount(String promotionCount) {
        this.promotionCount = promotionCount;
    }

    public String getCurrentOrderCount() {
        return currentOrderCount;
    }

    public void setCurrentOrderCount(String currentOrderCount) {
        this.currentOrderCount = currentOrderCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public NewUserPromotionBean getNewUserPromotionBean() {
        return newUserPromotionBean;
    }

    public void setNewUserPromotionBean(NewUserPromotionBean newUserPromotionBean) {
        this.newUserPromotionBean = newUserPromotionBean;
    }
}
