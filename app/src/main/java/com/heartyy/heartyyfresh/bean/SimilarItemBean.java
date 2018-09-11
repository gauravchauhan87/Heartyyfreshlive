package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/28/2015.
 */
public class SimilarItemBean {

    private String count;
    private List<SimilarItems> similarItemsList;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<SimilarItems> getSimilarItemsList() {
        return similarItemsList;
    }

    public void setSimilarItemsList(List<SimilarItems> similarItemsList) {
        this.similarItemsList = similarItemsList;
    }
}
