package com.heartyy.heartyyfresh.promotionbean;

/**
 * Created by Vijay on 4/22/2016.
 */
public class PercentOff {

    private String discountAmntPercent;
    private String maxDiscountAmnt;
    private String minimumOrderAmnt;

    public String getDiscountAmntPercent() {
        return discountAmntPercent;
    }

    public void setDiscountAmntPercent(String discountAmntPercent) {
        this.discountAmntPercent = discountAmntPercent;
    }

    public String getMaxDiscountAmnt() {
        return maxDiscountAmnt;
    }

    public void setMaxDiscountAmnt(String maxDiscountAmnt) {
        this.maxDiscountAmnt = maxDiscountAmnt;
    }

    public String getMinimumOrderAmnt() {
        return minimumOrderAmnt;
    }

    public void setMinimumOrderAmnt(String minimumOrderAmnt) {
        this.minimumOrderAmnt = minimumOrderAmnt;
    }
}
