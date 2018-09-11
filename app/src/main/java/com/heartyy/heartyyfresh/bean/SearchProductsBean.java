package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 2/9/2016.
 */
public class SearchProductsBean {

    private String topCategoryId;
    private String subCategoryId;
    private String subCategoryName;
    private String isTaxApplicable;
    private List<SubAisleItemBean> subAisleItemBeans;

    public String getTopCategoryId() {
        return topCategoryId;
    }

    public void setTopCategoryId(String topCategoryId) {
        this.topCategoryId = topCategoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getIsTaxApplicable() {
        return isTaxApplicable;
    }

    public void setIsTaxApplicable(String isTaxApplicable) {
        this.isTaxApplicable = isTaxApplicable;
    }

    public List<SubAisleItemBean> getSubAisleItemBeans() {
        return subAisleItemBeans;
    }

    public void setSubAisleItemBeans(List<SubAisleItemBean> subAisleItemBeans) {
        this.subAisleItemBeans = subAisleItemBeans;
    }
}
