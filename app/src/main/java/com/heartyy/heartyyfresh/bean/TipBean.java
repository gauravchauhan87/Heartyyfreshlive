package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/19/2016.
 */
public class TipBean {

    private String tipName;
    private String tipAmount;
    private String tipPercentApply;
    private String noTipApply;

    public TipBean(String tipName, String tipAmount, String tipPercentApply, String noTipApply) {
        this.tipName = tipName;
        this.tipAmount = tipAmount;
        this.tipPercentApply = tipPercentApply;
        this.noTipApply = noTipApply;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTipPercentApply() {
        return tipPercentApply;
    }

    public void setTipPercentApply(String tipPercentApply) {
        this.tipPercentApply = tipPercentApply;
    }

    public String getNoTipApply() {
        return noTipApply;
    }

    public void setNoTipApply(String noTipApply) {
        this.noTipApply = noTipApply;
    }
}
