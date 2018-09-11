package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/14/2016.
 */
public class SupplierDeliveryScheduleBean {

    private String deliveryDate;
    private String deliveryDayOfWeek;
    private String deliveryFrom;
    private String deliveryTo;
    private String finalDeliveryPrice;
    private TimeIntervalBean timeIntervalBean;
    private OperatingHourBean operatingHourBean;


    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryFrom() {
        return deliveryFrom;
    }

    public void setDeliveryFrom(String deliveryFrom) {
        this.deliveryFrom = deliveryFrom;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public TimeIntervalBean getTimeIntervalBean() {
        return timeIntervalBean;
    }

    public void setTimeIntervalBean(TimeIntervalBean timeIntervalBean) {
        this.timeIntervalBean = timeIntervalBean;
    }

    public OperatingHourBean getOperatingHourBean() {
        return operatingHourBean;
    }

    public void setOperatingHourBean(OperatingHourBean operatingHourBean) {
        this.operatingHourBean = operatingHourBean;
    }

    public String getDeliveryDayOfWeek() {
        return deliveryDayOfWeek;
    }

    public void setDeliveryDayOfWeek(String deliveryDayOfWeek) {
        this.deliveryDayOfWeek = deliveryDayOfWeek;
    }

    public String getFinalDeliveryPrice() {
        return finalDeliveryPrice;
    }

    public void setFinalDeliveryPrice(String finalDeliveryPrice) {
        this.finalDeliveryPrice = finalDeliveryPrice;
    }
}
