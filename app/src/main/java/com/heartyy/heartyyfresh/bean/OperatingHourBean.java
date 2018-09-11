package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/28/2016.
 */
public class OperatingHourBean {

    private String date;
    private String isClose;
    private String dayOfWeek;
    private boolean isSelected = false;
    private List<TimeIntervalBean> timeIntervalBeanList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<TimeIntervalBean> getTimeIntervalBeanList() {
        return timeIntervalBeanList;
    }

    public void setTimeIntervalBeanList(List<TimeIntervalBean> timeIntervalBeanList) {
        this.timeIntervalBeanList = timeIntervalBeanList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
