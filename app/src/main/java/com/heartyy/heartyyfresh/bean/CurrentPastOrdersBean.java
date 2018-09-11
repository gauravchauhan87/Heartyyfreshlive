package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by amitvashist1 on 29/01/16.
 */
public class CurrentPastOrdersBean {
    List<CurrentOrderBean> currentOrderBeansList;
    List<PastOrderBean> pastOrderBeansList;

    public List<CurrentOrderBean> getCurrentOrderBeansList() {
        return currentOrderBeansList;
    }

    public void setCurrentOrderBeansList(List<CurrentOrderBean> currentOrderBeansList) {
        this.currentOrderBeansList = currentOrderBeansList;
    }

    public List<PastOrderBean> getPastOrderBeansList() {
        return pastOrderBeansList;
    }

    public void setPastOrderBeansList(List<PastOrderBean> pastOrderBeansList) {
        this.pastOrderBeansList = pastOrderBeansList;
    }
}
