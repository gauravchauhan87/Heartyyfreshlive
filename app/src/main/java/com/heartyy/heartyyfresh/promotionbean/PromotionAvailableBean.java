package com.heartyy.heartyyfresh.promotionbean;

/**
 * Created by Vijay on 4/22/2016.
 */
public class PromotionAvailableBean {

    private String promoCode;
    private String excludeSalesItems;
    private String multipleDiscountApplies;
    private String autoApply;
    private String newUser;

    private DeliveryBean deliveryBean;
    private PercentOff percentOff;
    private BogoOffer bogoOffer;
    private AmountOff amountOff;
    private FreeDeliveryMinOrder freeDeliveryMinOrder;

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getExcludeSalesItems() {
        return excludeSalesItems;
    }

    public void setExcludeSalesItems(String excludeSalesItems) {
        this.excludeSalesItems = excludeSalesItems;
    }

    public String getMultipleDiscountApplies() {
        return multipleDiscountApplies;
    }

    public void setMultipleDiscountApplies(String multipleDiscountApplies) {
        this.multipleDiscountApplies = multipleDiscountApplies;
    }

    public String getAutoApply() {
        return autoApply;
    }

    public void setAutoApply(String autoApply) {
        this.autoApply = autoApply;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public DeliveryBean getDeliveryBean() {
        return deliveryBean;
    }

    public void setDeliveryBean(DeliveryBean deliveryBean) {
        this.deliveryBean = deliveryBean;
    }

    public PercentOff getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(PercentOff percentOff) {
        this.percentOff = percentOff;
    }

    public BogoOffer getBogoOffer() {
        return bogoOffer;
    }

    public void setBogoOffer(BogoOffer bogoOffer) {
        this.bogoOffer = bogoOffer;
    }

    public AmountOff getAmountOff() {
        return amountOff;
    }

    public void setAmountOff(AmountOff amountOff) {
        this.amountOff = amountOff;
    }

    public FreeDeliveryMinOrder getFreeDeliveryMinOrder() {
        return freeDeliveryMinOrder;
    }

    public void setFreeDeliveryMinOrder(FreeDeliveryMinOrder freeDeliveryMinOrder) {
        this.freeDeliveryMinOrder = freeDeliveryMinOrder;
    }
}
