package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/28/2015.
 */
public class SimilarItems {

    private String supplierItemId;
    private String finalItemId;
    private String itemId;
    private String itemName;
    private String alternateItemName;
    private String size;
    private String uom;
    private String salePrice;
    private String price;
    private String isTaxAplicable;
    private String subIsTaxApplicable;
    private String inStock;
    private String buyGetFree;
    private String buy;
    private String get;
    private String onSale;
    private String percentOff;
    private BrandBean brand;
    private String nutrition;
    private String Images;
    private String thumbnail;
    private List<ImagesBean> imagesBeanList;
    private List<ImagesBean> mainImagesBeanList;
    private String description;
    private String offer;
    private String isSave;
    private String topCategoryId;
    private String shippingWeight;
    private String finalItemUnitPrice;
    private String subCategoryId;
    private String sale;
    private String count="0";
    private String maxQuantity;
    private double taxAmount;
    private NutritutionBean nutritutionBean;

    public void setCount(String count) {
        this.count = count;
    }

    public String getSupplierItemId() {
        return supplierItemId;
    }

    public void setSupplierItemId(String supplierItemId) {
        this.supplierItemId = supplierItemId;
    }

    public String getFinalItemId() {
        return finalItemId;
    }

    public void setFinalItemId(String finalItemId) {
        this.finalItemId = finalItemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAlternateItemName() {
        return alternateItemName;
    }

    public void setAlternateItemName(String alternateItemName) {
        this.alternateItemName = alternateItemName;
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

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsTaxAplicable() {
        return isTaxAplicable;
    }

    public void setIsTaxAplicable(String isTaxAplicable) {
        this.isTaxAplicable = isTaxAplicable;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public String getBuyGetFree() {
        return buyGetFree;
    }

    public void setBuyGetFree(String buyGetFree) {
        this.buyGetFree = buyGetFree;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public String getOnSale() {
        return onSale;
    }

    public void setOnSale(String onSale) {
        this.onSale = onSale;
    }

    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    public BrandBean getBrand() {
        return brand;
    }

    public void setBrand(BrandBean brand) {
        this.brand = brand;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<ImagesBean> getImagesBeanList() {
        return imagesBeanList;
    }

    public void setImagesBeanList(List<ImagesBean> imagesBeanList) {
        this.imagesBeanList = imagesBeanList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public String getTopCategoryId() {
        return topCategoryId;
    }

    public void setTopCategoryId(String topCategoryId) {
        this.topCategoryId = topCategoryId;
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getFinalItemUnitPrice() {
        return finalItemUnitPrice;
    }

    public void setFinalItemUnitPrice(String finalItemUnitPrice) {
        this.finalItemUnitPrice = finalItemUnitPrice;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getSubIsTaxApplicable() {
        return subIsTaxApplicable;
    }

    public void setSubIsTaxApplicable(String subIsTaxApplicable) {
        this.subIsTaxApplicable = subIsTaxApplicable;
    }

    public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public List<ImagesBean> getMainImagesBeanList() {
        return mainImagesBeanList;
    }

    public void setMainImagesBeanList(List<ImagesBean> mainImagesBeanList) {
        this.mainImagesBeanList = mainImagesBeanList;
    }

    public NutritutionBean getNutritutionBean() {
        return nutritutionBean;
    }

    public void setNutritutionBean(NutritutionBean nutritutionBean) {
        this.nutritutionBean = nutritutionBean;
    }

//    public String getCount() {
//        return count;
//    }
//
//    public String getCount(String count) {
//        return this.count=count;
//    }
}
