package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/16/2016.
 */
public class OrderSubstitutionBean {

    private String supplierItemId;
    private String itemName;
    private String size;
    private String uom;
    private String brandName;
    private String orderQuantity;
    private String suppliedQuantity;
    private String regularPrice;
    private String finalPrice;
    private String total;
    private String images;

    public String getSupplierItemId() {
        return supplierItemId;
    }

    public void setSupplierItemId(String supplierItemId) {
        this.supplierItemId = supplierItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getSuppliedQuantity() {
        return suppliedQuantity;
    }

    public void setSuppliedQuantity(String suppliedQuantity) {
        this.suppliedQuantity = suppliedQuantity;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
