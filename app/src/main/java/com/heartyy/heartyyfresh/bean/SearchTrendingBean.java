package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 2/28/2016.
 */
public class SearchTrendingBean {

    private List<RecentSearchBean> recentSearchBeanList;
    private List<TrendingBean> trendingBeanList;

    public List<RecentSearchBean> getRecentSearchBeanList() {
        return recentSearchBeanList;
    }

    public void setRecentSearchBeanList(List<RecentSearchBean> recentSearchBeanList) {
        this.recentSearchBeanList = recentSearchBeanList;
    }

    public List<TrendingBean> getTrendingBeanList() {
        return trendingBeanList;
    }

    public void setTrendingBeanList(List<TrendingBean> trendingBeanList) {
        this.trendingBeanList = trendingBeanList;
    }
}
