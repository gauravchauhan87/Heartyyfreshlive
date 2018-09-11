package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 2/28/2016.
 */
public class PreviousOrderBean {

    private String isMore;
    private List<SubAisleItemBean> previousItemList;

    public String getIsMore() {
        return isMore;
    }

    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }

    public List<SubAisleItemBean> getPreviousItemList() {
        return previousItemList;
    }

    public void setPreviousItemList(List<SubAisleItemBean> previousItemList) {
        this.previousItemList = previousItemList;
    }
}
