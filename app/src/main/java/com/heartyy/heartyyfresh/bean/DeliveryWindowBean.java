package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 1/28/2016.
 */
public class DeliveryWindowBean {

    private SingleDeliveryBean singleDeliveryBean;
    private CombineDeliveryBean combineDeliveryBean;

    public SingleDeliveryBean getSingleDeliveryBean() {
        return singleDeliveryBean;
    }

    public void setSingleDeliveryBean(SingleDeliveryBean singleDeliveryBean) {
        this.singleDeliveryBean = singleDeliveryBean;
    }

    public CombineDeliveryBean getCombineDeliveryBean() {
        return combineDeliveryBean;
    }

    public void setCombineDeliveryBean(CombineDeliveryBean combineDeliveryBean) {
        this.combineDeliveryBean = combineDeliveryBean;
    }
}
