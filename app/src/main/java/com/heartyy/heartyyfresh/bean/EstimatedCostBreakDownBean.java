package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 4/10/2016.
 */
public class EstimatedCostBreakDownBean {

    private String labourCost;
    private String fuelCost;
    private String convenience;
    private String deliveryEstimateCost;
    private String estimateDistance;
    private String estimateTime;
    private String from;
    private String to;
    private String addressFrom;
    private String addressTo;
    private LatLongBean latLongBean;

    public String getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(String labourCost) {
        this.labourCost = labourCost;
    }

    public String getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(String fuelCost) {
        this.fuelCost = fuelCost;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public String getDeliveryEstimateCost() {
        return deliveryEstimateCost;
    }

    public void setDeliveryEstimateCost(String deliveryEstimateCost) {
        this.deliveryEstimateCost = deliveryEstimateCost;
    }

    public String getEstimateDistance() {
        return estimateDistance;
    }

    public void setEstimateDistance(String estimateDistance) {
        this.estimateDistance = estimateDistance;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.estimateTime = estimateTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public LatLongBean getLatLongBean() {
        return latLongBean;
    }

    public void setLatLongBean(LatLongBean latLongBean) {
        this.latLongBean = latLongBean;
    }
}
