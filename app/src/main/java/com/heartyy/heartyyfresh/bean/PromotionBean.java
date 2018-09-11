package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/27/2015.
 */
public class PromotionBean {

    private String promotionId;
    private String promotionTitle;
    private String promotionDisplayText;
    private String bannerBackground;
    private String promoType;
    private String creditAmount;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public String getPromotionDisplayText() {
        return promotionDisplayText;
    }

    public void setPromotionDisplayText(String promotionDisplayText) {
        this.promotionDisplayText = promotionDisplayText;
    }

    public String getBannerBackground() {
        return bannerBackground;
    }

    public void setBannerBackground(String bannerBackground) {
        this.bannerBackground = bannerBackground;
    }

    public String getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType = promoType;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }
}
