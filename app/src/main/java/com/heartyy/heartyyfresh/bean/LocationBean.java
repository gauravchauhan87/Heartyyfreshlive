package com.heartyy.heartyyfresh.bean;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class LocationBean {

    private String userDeliveryLocationId;
    private String userId;
    private String locationType;
    private String address1;
    private String address2;
    private String aptSuitUnit;
    private String locationName;
    private String deliveryInstructions;
    private String zipcode;
    private String isPrimaryLocation;
    private String city;
    private String state;
    private String country;
    private String taxRate;

    public String getUserDeliveryLocationId() {
        return userDeliveryLocationId;
    }

    public void setUserDeliveryLocationId(String userDeliveryLocationId) {
        this.userDeliveryLocationId = userDeliveryLocationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAptSuitUnit() {
        return aptSuitUnit;
    }

    public void setAptSuitUnit(String aptSuitUnit) {
        this.aptSuitUnit = aptSuitUnit;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIsPrimaryLocation() {
        return isPrimaryLocation;
    }

    public void setIsPrimaryLocation(String isPrimaryLocation) {
        this.isPrimaryLocation = isPrimaryLocation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
