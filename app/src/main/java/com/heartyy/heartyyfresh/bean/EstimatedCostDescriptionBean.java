package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/12/2016.
 */
public class EstimatedCostDescriptionBean {
       private String baseDeliveryCost;
       private String deliveryMinuteCost;
       private String deliveryDistance;
       private String deliveryTimeConsumption;
       private String estimatedDeliveryCost;
       private String from;
       private String to;
       private String addressFrom;
       private String addressTo;

    public String getBaseDeliveryCost() {
        return baseDeliveryCost;
    }

    public void setBaseDeliveryCost(String baseDeliveryCost) {
        this.baseDeliveryCost = baseDeliveryCost;
    }

    public String getDeliveryMinuteCost() {
        return deliveryMinuteCost;
    }

    public void setDeliveryMinuteCost(String deliveryMinuteCost) {
        this.deliveryMinuteCost = deliveryMinuteCost;
    }

    public String getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(String deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getDeliveryTimeConsumption() {
        return deliveryTimeConsumption;
    }

    public void setDeliveryTimeConsumption(String deliveryTimeConsumption) {
        this.deliveryTimeConsumption = deliveryTimeConsumption;
    }

    public String getEstimatedDeliveryCost() {
        return estimatedDeliveryCost;
    }

    public void setEstimatedDeliveryCost(String estimatedDeliveryCost) {
        this.estimatedDeliveryCost = estimatedDeliveryCost;
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
}
