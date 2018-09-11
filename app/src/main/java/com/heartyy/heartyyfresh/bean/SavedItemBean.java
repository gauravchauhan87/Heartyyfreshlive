package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/6/2015.
 */
public class SavedItemBean  {

    private String zipcode;
    private List<SavedSuppliersBean> savedSuppliersBeanList;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<SavedSuppliersBean> getSavedSuppliersBeanList() {
        return savedSuppliersBeanList;
    }

    public void setSavedSuppliersBeanList(List<SavedSuppliersBean> savedSuppliersBeanList) {
        this.savedSuppliersBeanList = savedSuppliersBeanList;
    }


}
