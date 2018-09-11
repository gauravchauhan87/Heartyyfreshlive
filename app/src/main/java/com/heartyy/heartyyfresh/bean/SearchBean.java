package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/21/2016.
 */
public class SearchBean {

    private List<SearchTagBean> searchTagBeanList;
    private List<SearchSubAisleBean> searchSubAisleBeanList;

    public List<SearchTagBean> getSearchTagBeanList() {
        return searchTagBeanList;
    }

    public void setSearchTagBeanList(List<SearchTagBean> searchTagBeanList) {
        this.searchTagBeanList = searchTagBeanList;
    }

    public List<SearchSubAisleBean> getSearchSubAisleBeanList() {
        return searchSubAisleBeanList;
    }

    public void setSearchSubAisleBeanList(List<SearchSubAisleBean> searchSubAisleBeanList) {
        this.searchSubAisleBeanList = searchSubAisleBeanList;
    }
}
