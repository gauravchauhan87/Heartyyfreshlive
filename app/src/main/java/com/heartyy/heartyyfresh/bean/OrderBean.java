package com.heartyy.heartyyfresh.bean;

/**
 * Created by Deepak Sharma on 1/22/2016.
 */
public class OrderBean {
    private String itemName;
    private String brandName;
    private String size;
    private String uom;
    private String shippingWeight;
    private String orderQuantity;
    private String unitPrice;
    private String finalPrice;
    private String supplierId;
    private String thumbnail;
    private String isTaxable;
    private double taxAmount;
    private String maxQuantity;
    private String taxRate;

    //For Order Detail
    private String supplierItemId;
    private String outOfStock;
    private String regularPrice;
    private String totalPrice;
    private String substitution;
    private String substitutionWith;
    private String isSave;
    private int headerPosition;
    private String isSubTaxApplicable;

    //For order reciept
    private String isSubstituted;
    private String suppliedQuantity;
    private String isOrderedSuppliedQuantitySame;
    private String freeItemQuantity;
    private String total;
    private String applicableSaleDescription;
    private OrderSubstitutionBean orderSubstitutionBean;




    public String getSupplierItemId() {
        return supplierItemId;
    }
    public void setSupplierItemId(String supplierItemId) {
        this.supplierItemId = supplierItemId;
    }

    public String getOutOfStock() {     return outOfStock;   }

    public void setOutOfStock(String outOfStock) {      this.outOfStock = outOfStock;    }

    public String getRegularPrice() {        return regularPrice;    }

    public void setRegularPrice(String regularPrice) {        this.regularPrice = regularPrice;    }

    public String getTotalPrice() {        return totalPrice;    }

    public void setTotalPrice(String totalPrice) {        this.totalPrice = totalPrice;    }

    public String getSubstitution() {        return substitution;    }

    public void setSubstitution(String substitution) {    this.substitution = substitution;    }

    public String getSubstitutionWith() {        return substitutionWith;    }

    public void setSubstitutionWith(String substitutionWith) { this.substitutionWith = substitutionWith;  }

    public String getIsSave() {        return isSave;    }

    public void setIsSave(String isSave) {        this.isSave = isSave;    }
    //End For Order Detail


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getApplicableSaleDescription() {
        return applicableSaleDescription;
    }

    public void setApplicableSaleDescription(String applicableSaleDescription) {
        this.applicableSaleDescription = applicableSaleDescription;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderPosition(int headerPosition) {
        this.headerPosition = headerPosition;
    }

    public String getIsSubstituted() {
        return isSubstituted;
    }

    public void setIsSubstituted(String isSubstituted) {
        this.isSubstituted = isSubstituted;
    }

    public String getSuppliedQuantity() {
        return suppliedQuantity;
    }

    public void setSuppliedQuantity(String suppliedQuantity) {
        this.suppliedQuantity = suppliedQuantity;
    }

    public String getIsOrderedSuppliedQuantitySame() {
        return isOrderedSuppliedQuantitySame;
    }

    public void setIsOrderedSuppliedQuantitySame(String isOrderedSuppliedQuantitySame) {
        this.isOrderedSuppliedQuantitySame = isOrderedSuppliedQuantitySame;
    }

    public String getFreeItemQuantity() {
        return freeItemQuantity;
    }

    public void setFreeItemQuantity(String freeItemQuantity) {
        this.freeItemQuantity = freeItemQuantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public OrderSubstitutionBean getOrderSubstitutionBean() {
        return orderSubstitutionBean;
    }

    public void setOrderSubstitutionBean(OrderSubstitutionBean orderSubstitutionBean) {
        this.orderSubstitutionBean = orderSubstitutionBean;
    }

    public String getIsTaxable() {
        return isTaxable;
    }

    public void setIsTaxable(String isTaxable) {
        this.isTaxable = isTaxable;
    }

    public String getIsSubTaxApplicable() {
        return isSubTaxApplicable;
    }

    public void setIsSubTaxApplicable(String isSubTaxApplicable) {
        this.isSubTaxApplicable = isSubTaxApplicable;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
