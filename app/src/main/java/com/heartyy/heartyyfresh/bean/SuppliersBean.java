package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 12/27/2015.
 */
public class SuppliersBean {

    private String supplierId;
    private String orderSupplierId;
    private String supplierName;
    private String city;
    private String supplierEthnicityId;
    private String priceMarkup;
    private String applicableTaxRate;
    private String estimateDelivery;
    private String estimateDeliveryTime;
    private String supplierTotal;
    private String deliveryStatusDisplay;
    private boolean isSelected = false;
    private String supplierAvailable;
    private String suppliersTotal;
    private String message;
    private String deliveryDate;
    private String deliveryTime;
    private String isOrderCancelable;
    private String deliveryStatus;
    private String comingSoon;
    private String comingSoonMessage;

    //More data for order detail module
    List<OrderBean> detailOrdersBeanList;
    private EstimatedCostDescriptionBean estimatedCostDescriptionBean;
    private EstimatedCostBreakDownBean estimatedCostBreakDownBean;



    public List<OrderBean> getDetailOrdersBeanList() {
        return detailOrdersBeanList;
    }



    public void setDetailOrdersBeanList(List<OrderBean> detailOrdersBeanList) {
        this.detailOrdersBeanList = detailOrdersBeanList;
    }
    //End:More data for order detail module



    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrderSupplierId() {
        return orderSupplierId;
    }

    public void setOrderSupplierId(String orderSupplierId) {
        this.orderSupplierId = orderSupplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSupplierEthnicityId() {
        return supplierEthnicityId;
    }

    public void setSupplierEthnicityId(String supplierEthnicityId) {
        this.supplierEthnicityId = supplierEthnicityId;
    }

    public String getPriceMarkup() {
        return priceMarkup;
    }

    public void setPriceMarkup(String priceMarkup) {
        this.priceMarkup = priceMarkup;
    }

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getEstimateDeliveryTime() {
        return estimateDeliveryTime;
    }

    public void setEstimateDeliveryTime(String estimateDeliveryTime) {
        this.estimateDeliveryTime = estimateDeliveryTime;
    }

    public String getSupplierTotal() {
        return supplierTotal;
    }

    public void setSupplierTotal(String supplierTotal) {
        this.supplierTotal = supplierTotal;
    }

    public String getSupplierAvailable() {
        return supplierAvailable;
    }

    public void setSupplierAvailable(String supplierAvailable) {
        this.supplierAvailable = supplierAvailable;
    }

    public String getApplicableTaxRate() {
        return applicableTaxRate;
    }

    public void setApplicableTaxRate(String applicableTaxRate) {
        this.applicableTaxRate = applicableTaxRate;
    }

    public String getSuppliersTotal() {
        return suppliersTotal;
    }

    public void setSuppliersTotal(String suppliersTotal) {
        this.suppliersTotal = suppliersTotal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public EstimatedCostDescriptionBean getEstimatedCostDescriptionBean() {
        return estimatedCostDescriptionBean;
    }

    public void setEstimatedCostDescriptionBean(EstimatedCostDescriptionBean estimatedCostDescriptionBean) {
        this.estimatedCostDescriptionBean = estimatedCostDescriptionBean;
    }

    public String getIsOrderCancelable() {
        return isOrderCancelable;
    }

    public void setIsOrderCancelable(String isOrderCancelable) {
        this.isOrderCancelable = isOrderCancelable;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public EstimatedCostBreakDownBean getEstimatedCostBreakDownBean() {
        return estimatedCostBreakDownBean;
    }

    public void setEstimatedCostBreakDownBean(EstimatedCostBreakDownBean estimatedCostBreakDownBean) {
        this.estimatedCostBreakDownBean = estimatedCostBreakDownBean;
    }

    public String getComingSoon() {
        return comingSoon;
    }

    public void setComingSoon(String comingSoon) {
        this.comingSoon = comingSoon;
    }

    public String getComingSoonMessage() {
        return comingSoonMessage;
    }

    public void setComingSoonMessage(String comingSoonMessage) {
        this.comingSoonMessage = comingSoonMessage;
    }

    public String getDeliveryStatusDisplay() {
        return deliveryStatusDisplay;
    }

    public void setDeliveryStatusDisplay(String deliveryStatusDisplay) {
        this.deliveryStatusDisplay = deliveryStatusDisplay;
    }
}
