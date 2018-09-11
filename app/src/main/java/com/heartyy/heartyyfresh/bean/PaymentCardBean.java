package com.heartyy.heartyyfresh.bean;

/**
 * Created by Gaurav on 11/22/2015.
 */
public class PaymentCardBean {

    private String cardType;
    private String paymentGatewayCustomerId;
    private String cardIssuer;
    private String cardCvvNumber;
    private String cardExpiratoinDate;
    private String cardLastFourDigit;
    private String cardGivenName;
    private String token;
    private String isPrimary;
    private String userPaymentMethodId;

    public String getPaymentGatewayCustomerId() {
        return paymentGatewayCustomerId;
    }

    public void setPaymentGatewayCustomerId(String paymentGatewayCustomerId) {
        this.paymentGatewayCustomerId = paymentGatewayCustomerId;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardCvvNumber() {
        return cardCvvNumber;
    }

    public void setCardCvvNumber(String cardCvvNumber) {
        this.cardCvvNumber = cardCvvNumber;
    }

    public String getCardExpiratoinDate() {
        return cardExpiratoinDate;
    }

    public void setCardExpiratoinDate(String cardExpiratoinDate) {
        this.cardExpiratoinDate = cardExpiratoinDate;
    }

    public String getCardLastFourDigit() {
        return cardLastFourDigit;
    }

    public void setCardLastFourDigit(String cardLastFourDigit) {
        this.cardLastFourDigit = cardLastFourDigit;
    }

    public String getCardGivenName() {
        return cardGivenName;
    }

    public void setCardGivenName(String cardGivenName) {
        this.cardGivenName = cardGivenName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getUserPaymentMethodId() {
        return userPaymentMethodId;
    }

    public void setUserPaymentMethodId(String userPaymentMethodId) {
        this.userPaymentMethodId = userPaymentMethodId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
