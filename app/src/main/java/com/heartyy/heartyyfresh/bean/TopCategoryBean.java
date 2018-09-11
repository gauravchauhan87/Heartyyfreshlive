package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class TopCategoryBean {

    private String topCategoryId;
    private String topCategoryName;
    private SubAisleBean subAisleBean;

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

    public SubAisleBean getSubAisleBean() {
        return subAisleBean;
    }

    public void setSubAisleBean(SubAisleBean subAisleBean) {
        this.subAisleBean = subAisleBean;
    }
}
