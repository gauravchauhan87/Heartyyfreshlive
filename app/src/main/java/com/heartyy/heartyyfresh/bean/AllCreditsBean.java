package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/12/2016.
 */
public class AllCreditsBean {
   private String userCreditId;
   private String creditDisplayText;
   private String creditReasonDisplay;
   private String creditAmount;
   private String appliedOrUsed;

    public String getUserCreditId() {
        return userCreditId;
    }

    public void setUserCreditId(String userCreditId) {
        this.userCreditId = userCreditId;
    }

    public String getCreditDisplayText() {
        return creditDisplayText;
    }

    public void setCreditDisplayText(String creditDisplayText) {
        this.creditDisplayText = creditDisplayText;
    }

    public String getCreditReasonDisplay() {
        return creditReasonDisplay;
    }

    public void setCreditReasonDisplay(String creditReasonDisplay) {
        this.creditReasonDisplay = creditReasonDisplay;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getAppliedOrUsed() {
        return appliedOrUsed;
    }

    public void setAppliedOrUsed(String appliedOrUsed) {
        this.appliedOrUsed = appliedOrUsed;
    }
}
