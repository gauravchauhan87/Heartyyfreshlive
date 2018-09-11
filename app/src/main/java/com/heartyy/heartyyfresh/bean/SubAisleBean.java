package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/28/2015.
 */
public class SubAisleBean {

    private String subCategoryId;
    private String subCategoryName;
    private String isTaxApplicable;
    private String isMore;
    private List<SubAisleItemBean> subAisleItemBeanList;

    public List<SubAisleItemBean> getSubAisleItemBeanList() {
        return subAisleItemBeanList;
    }

    public void setSubAisleItemBeanList(List<SubAisleItemBean> subAisleItemBeanList) {
        this.subAisleItemBeanList = subAisleItemBeanList;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getIsTaxApplicable() {
        return isTaxApplicable;
    }

    public void setIsTaxApplicable(String isTaxApplicable) {
        this.isTaxApplicable = isTaxApplicable;
    }

    public String getIsMore() {
        return isMore;
    }

    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }
}
