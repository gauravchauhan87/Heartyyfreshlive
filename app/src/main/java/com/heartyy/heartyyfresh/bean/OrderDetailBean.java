package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by amitvashist1 on 01/02/16.
 */
public class OrderDetailBean {
    private String orderId;
    private String displayOrderId;
    private String deliveryDate;
    private String deliveryTime;
    private String deliveryInstructions;
    private String deliveryPhoneSupplied;
    private String deliveryTo;
    private String deliveryCharge;
    private String credits;
    private String tipAmount;
    private String tipPercentage;
    private String subTotal;
    private String tax;
    private String totalAmount;
    private String isCancelable;

    private LocationBean locationBean;
    private PaymentCardBean paymentCardBean;
    private OrderDetailPromoBean orderDetailPromoBean;
    private List<SuppliersBean> suppliersBeanList;

    public String getIsCancelable() {
        return isCancelable;
    }

    public void setIsCancelable(String isCancelable) {
        this.isCancelable = isCancelable;
    }

    public List<SuppliersBean> getSuppliersBeanList() {
        return suppliersBeanList;
    }

    public void setSuppliersBeanList(List<SuppliersBean> suppliersBeanList) {
        this.suppliersBeanList = suppliersBeanList;
    }

    public void setLocationBean(LocationBean locationBean){
        this.locationBean = locationBean;
    }

    public LocationBean getLocationBean(){
        return locationBean;
    }

    public void setPaymentCardBean(PaymentCardBean paymentCardBean){
        this.paymentCardBean = paymentCardBean;
    }

    public PaymentCardBean getPaymentCardBean(){
        return paymentCardBean;
    }

    public void setOrderDetailPromoBean(OrderDetailPromoBean orderDetailPromoBean){
        this.orderDetailPromoBean = orderDetailPromoBean;
    }

    public OrderDetailPromoBean getOrderDetailPromoBean(){
        return orderDetailPromoBean;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(String displayOrderId) {
        this.displayOrderId = displayOrderId;
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

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getDeliveryPhoneSupplied() {
        return deliveryPhoneSupplied;
    }

    public void setDeliveryPhoneSupplied(String deliveryPhoneSupplied) {
        this.deliveryPhoneSupplied = deliveryPhoneSupplied;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(String tipPercentage) {
        this.tipPercentage = tipPercentage;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
