package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 2/15/2016.
 */
public class OrderRecieptBean {

    private String displayOrderId;
    private String orderTotal;
    private String deliverTo;
    private String contactInfo;
    private String deliveryDate;
    private String bagRecycleFee;
    private PaymentCardBean paymentCardBean;
    private String promoCode;
    private String credits;
    private String tipAmount;
    private String tipPercent;
    private String tax;
    private String subTotal;
    private String deliveryCharge;

    private List<SuppliersBean> suppliersBeanList;
    private OrderDetailPromoBean orderDetailPromoBean;

    public String getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(String displayOrderId) {
        this.displayOrderId = displayOrderId;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<SuppliersBean> getSuppliersBeanList() {
        return suppliersBeanList;
    }

    public void setSuppliersBeanList(List<SuppliersBean> suppliersBeanList) {
        this.suppliersBeanList = suppliersBeanList;
    }

    public String getBagRecycleFee() {
        return bagRecycleFee;
    }

    public void setBagRecycleFee(String bagRecycleFee) {
        this.bagRecycleFee = bagRecycleFee;
    }

    public PaymentCardBean getPaymentCardBean() {
        return paymentCardBean;
    }

    public void setPaymentCardBean(PaymentCardBean paymentCardBean) {
        this.paymentCardBean = paymentCardBean;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
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

    public String getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(String tipPercent) {
        this.tipPercent = tipPercent;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public OrderDetailPromoBean getOrderDetailPromoBean() {
        return orderDetailPromoBean;
    }

    public void setOrderDetailPromoBean(OrderDetailPromoBean orderDetailPromoBean) {
        this.orderDetailPromoBean = orderDetailPromoBean;
    }
}
