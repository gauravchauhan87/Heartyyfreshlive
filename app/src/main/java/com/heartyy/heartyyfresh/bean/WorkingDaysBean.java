package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/28/2016.
 */
public class WorkingDaysBean {

    private String supplierId;
    private String deliveryEstimatedFee;
    private String deliveryEstimatedChargeBeforeSix;
    private String deliveryEstimatedChargeAfterSix;
    private String deliveryMiles;
    private String deliveryMinutes;
    private String deliveryMilesCharged;
    private String deliveryMinuteCharged;
    private String deliveryCongestionFee;
    private String deliveryCharge;
    private String deliveryWeightSurcharge;
    private String intEstCost;
    private String deliveryCongestionCost;
    private String deliveryCongestionCostBeforeSix;
    private String deliveryCongestionCostAfterSix;
    private boolean isCongestionDeliverSelected = false;
    private List<OperatingHourBean> operatingHourBeanList;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getDeliveryEstimatedFee() {
        return deliveryEstimatedFee;
    }

    public void setDeliveryEstimatedFee(String deliveryEstimatedFee) {
        this.deliveryEstimatedFee = deliveryEstimatedFee;
    }

    public List<OperatingHourBean> getOperatingHourBeanList() {
        return operatingHourBeanList;
    }

    public void setOperatingHourBeanList(List<OperatingHourBean> operatingHourBeanList) {
        this.operatingHourBeanList = operatingHourBeanList;
    }

    public String getDeliveryMiles() {
        return deliveryMiles;
    }

    public void setDeliveryMiles(String deliveryMiles) {
        this.deliveryMiles = deliveryMiles;
    }

    public String getDeliveryMinutes() {
        return deliveryMinutes;
    }

    public void setDeliveryMinutes(String deliveryMinutes) {
        this.deliveryMinutes = deliveryMinutes;
    }

    public String getDeliveryMilesCharged() {
        return deliveryMilesCharged;
    }

    public void setDeliveryMilesCharged(String deliveryMilesCharged) {
        this.deliveryMilesCharged = deliveryMilesCharged;
    }

    public String getDeliveryMinuteCharged() {
        return deliveryMinuteCharged;
    }

    public void setDeliveryMinuteCharged(String deliveryMinuteCharged) {
        this.deliveryMinuteCharged = deliveryMinuteCharged;
    }

    public String getDeliveryCongestionFee() {
        return deliveryCongestionFee;
    }

    public void setDeliveryCongestionFee(String deliveryCongestionFee) {
        this.deliveryCongestionFee = deliveryCongestionFee;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDeliveryWeightSurcharge() {
        return deliveryWeightSurcharge;
    }

    public void setDeliveryWeightSurcharge(String deliveryWeightSurcharge) {
        this.deliveryWeightSurcharge = deliveryWeightSurcharge;
    }

    public String getIntEstCost() {
        return intEstCost;
    }

    public void setIntEstCost(String intEstCost) {
        this.intEstCost = intEstCost;
    }

    public String getDeliveryCongestionCost() {
        return deliveryCongestionCost;
    }

    public void setDeliveryCongestionCost(String deliveryCongestionCost) {
        this.deliveryCongestionCost = deliveryCongestionCost;
    }

    public String getDeliveryCongestionCostBeforeSix() {
        return deliveryCongestionCostBeforeSix;
    }

    public void setDeliveryCongestionCostBeforeSix(String deliveryCongestionCostBeforeSix) {
        this.deliveryCongestionCostBeforeSix = deliveryCongestionCostBeforeSix;
    }

    public String getDeliveryCongestionCostAfterSix() {
        return deliveryCongestionCostAfterSix;
    }

    public void setDeliveryCongestionCostAfterSix(String deliveryCongestionCostAfterSix) {
        this.deliveryCongestionCostAfterSix = deliveryCongestionCostAfterSix;
    }

    public String getDeliveryEstimatedChargeBeforeSix() {
        return deliveryEstimatedChargeBeforeSix;
    }

    public void setDeliveryEstimatedChargeBeforeSix(String deliveryEstimatedChargeBeforeSix) {
        this.deliveryEstimatedChargeBeforeSix = deliveryEstimatedChargeBeforeSix;
    }

    public String getDeliveryEstimatedChargeAfterSix() {
        return deliveryEstimatedChargeAfterSix;
    }

    public void setDeliveryEstimatedChargeAfterSix(String deliveryEstimatedChargeAfterSix) {
        this.deliveryEstimatedChargeAfterSix = deliveryEstimatedChargeAfterSix;
    }

    public boolean isCongestionDeliverSelected() {
        return isCongestionDeliverSelected;
    }

    public void setIsCongestionDeliverSelected(boolean isCongestionDeliverSelected) {
        this.isCongestionDeliverSelected = isCongestionDeliverSelected;
    }
}
