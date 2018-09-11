package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/26/2016.
 */
public class IndividualDeliveryBean {

    private List<DateBean> dateBeanList;

    public IndividualDeliveryBean() {

    }

    public IndividualDeliveryBean(List<DateBean> dateBeanList) {
        this.dateBeanList = dateBeanList;
    }

    public List<DateBean> getDateBeanList() {
        return dateBeanList;
    }

    public void setDateBeanList(List<DateBean> dateBeanList) {
        this.dateBeanList = dateBeanList;
    }
}
