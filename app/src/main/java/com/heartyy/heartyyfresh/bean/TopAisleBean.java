package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/28/2015.
 */
public class TopAisleBean {

    private String topCategoryId;
    private String topCategoryName;
    private String isMore;
    private List<SubAisleBean> subAisleBeanList;
    private List<SubAisleItemBean> topCategoryPopularBeanList;

    public String getTopCategoryId() {
        return topCategoryId;
    }

    public void setTopCategoryId(String topCategoryId) {
        this.topCategoryId = topCategoryId;
    }

    public String getTopCategoryName() {
        return topCategoryName;
    }

    public void setTopCategoryName(String topCategoryName) {
        this.topCategoryName = topCategoryName;
    }

    public List<SubAisleBean> getSubAisleBeanList() {
        return subAisleBeanList;
    }

    public void setSubAisleBeanList(List<SubAisleBean> subAisleBeanList) {
        this.subAisleBeanList = subAisleBeanList;
    }

    public String getIsMore() {
        return isMore;
    }

    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }

    public List<SubAisleItemBean> getTopCategoryPopularBeanList() {
        return topCategoryPopularBeanList;
    }

    public void setTopCategoryPopularBeanList(List<SubAisleItemBean> topCategoryPopularBeanList) {
        this.topCategoryPopularBeanList = topCategoryPopularBeanList;
    }
}
