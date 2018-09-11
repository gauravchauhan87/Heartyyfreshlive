package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class GlobalBean {

    private List<RefineBrandBean> refineBrandBeanList;
    private List<RefineSizeBean> refineSizeBeanList;
    private List<RefinePriceBean> refinePriceBeanList;

    public List<RefineBrandBean> getRefineBrandBeanList() {
        return refineBrandBeanList;
    }

    public void setRefineBrandBeanList(List<RefineBrandBean> refineBrandBeanList) {
        this.refineBrandBeanList = refineBrandBeanList;
    }

    public List<RefineSizeBean> getRefineSizeBeanList() {
        return refineSizeBeanList;
    }

    public void setRefineSizeBeanList(List<RefineSizeBean> refineSizeBeanList) {
        this.refineSizeBeanList = refineSizeBeanList;
    }

    public List<RefinePriceBean> getRefinePriceBeanList() {
        return refinePriceBeanList;
    }

    public void setRefinePriceBeanList(List<RefinePriceBean> refinePriceBeanList) {
        this.refinePriceBeanList = refinePriceBeanList;
    }
}
