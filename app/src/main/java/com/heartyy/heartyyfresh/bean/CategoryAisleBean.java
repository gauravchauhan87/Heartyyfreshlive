package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/29/2015.
 */
public class CategoryAisleBean {

    private TopAisleBean topAisleBean;
    private String popularIsMore;
    private List<SubAisleItemBean> popularAisleItemBeanList;

    public TopAisleBean getTopAisleBean() {
        return topAisleBean;
    }

    public void setTopAisleBean(TopAisleBean topAisleBean) {
        this.topAisleBean = topAisleBean;
    }

    public List<SubAisleItemBean> getPopularAisleItemBeanList() {
        return popularAisleItemBeanList;
    }

    public void setPopularAisleItemBeanList(List<SubAisleItemBean> popularAisleItemBeanList) {
        this.popularAisleItemBeanList = popularAisleItemBeanList;
    }

    public String getPopularIsMore() {
        return popularIsMore;
    }

    public void setPopularIsMore(String popularIsMore) {
        this.popularIsMore = popularIsMore;
    }
}
