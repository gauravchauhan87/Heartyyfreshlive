package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/28/2015.
 */
public class AisleBean {

    private List<PromotionBean> promotionBeanList;
    private List<TopAisleBean> topAisleBeanList;
    private List<SubAisleItemBean> popularItemList;
    private PreviousOrderBean previousOrderBean;
    private String popularMore;

    public List<PromotionBean> getPromotionBeanList() {
        return promotionBeanList;
    }

    public void setPromotionBeanList(List<PromotionBean> promotionBeanList) {
        this.promotionBeanList = promotionBeanList;
    }

    public List<TopAisleBean> getTopAisleBeanList() {
        return topAisleBeanList;
    }

    public void setTopAisleBeanList(List<TopAisleBean> topAisleBeanList) {
        this.topAisleBeanList = topAisleBeanList;
    }

    public List<SubAisleItemBean> getPopularItemList() {
        return popularItemList;
    }

    public void setPopularItemList(List<SubAisleItemBean> popularItemList) {
        this.popularItemList = popularItemList;
    }

    public PreviousOrderBean getPreviousOrderBean() {
        return previousOrderBean;
    }

    public void setPreviousOrderBean(PreviousOrderBean previousOrderBean) {
        this.previousOrderBean = previousOrderBean;
    }

    public String getPopularMore() {
        return popularMore;
    }

    public void setPopularMore(String popularMore) {
        this.popularMore = popularMore;
    }
}
