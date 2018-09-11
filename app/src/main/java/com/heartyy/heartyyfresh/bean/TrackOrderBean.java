package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by amitvashist1 on 02/02/16.
 */
public class TrackOrderBean {
    List<OrderTrackBean> orderTrackBeansList;
    private String notificationId;
    private String isOn;

    public List<OrderTrackBean> getOrderTrackBeansList() {
        return orderTrackBeansList;
    }

    public void setOrderTrackBeansList(List<OrderTrackBean> orderTrackBeansList) {
        this.orderTrackBeansList = orderTrackBeansList;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getIsOn() {
        return isOn;
    }

    public void setIsOn(String isOn) {
        this.isOn = isOn;
    }
}
