package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/8/2016.
 */
public class SavedSuppliersBean implements Item {

    private String supplierId;
    private String supplierName;
    private String city;
    private String zipcode;
    private String count;
    private String taxRate;
    private List<SavedSupplierItemBean> savedSupplierItemBeanList;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<SavedSupplierItemBean> getSavedSupplierItemBeanList() {
        return savedSupplierItemBeanList;
    }

    public void setSavedSupplierItemBeanList(List<SavedSupplierItemBean> savedSupplierItemBeanList) {
        this.savedSupplierItemBeanList = savedSupplierItemBeanList;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
