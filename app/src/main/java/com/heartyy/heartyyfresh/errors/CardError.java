package com.heartyy.heartyyfresh.errors;

/**
 * Created by Dheeraj on 12/21/2015.
 */
public class CardError {

    private String numberRequired;
    private String numberLength;
    private String cvvRequired;
    private String cvvLength;
    private String monthRequired;
    private String monthLength;
    private String yearRequired;
    private String yearLength;
    private String zipcodeRequired;
    private String zipcodeLength;

    public String getNumberRequired() {
        return numberRequired;
    }

    public void setNumberRequired(String numberRequired) {
        this.numberRequired = numberRequired;
    }

    public String getNumberLength() {
        return numberLength;
    }

    public void setNumberLength(String numberLength) {
        this.numberLength = numberLength;
    }

    public String getCvvRequired() {
        return cvvRequired;
    }

    public void setCvvRequired(String cvvRequired) {
        this.cvvRequired = cvvRequired;
    }

    public String getCvvLength() {
        return cvvLength;
    }

    public void setCvvLength(String cvvLength) {
        this.cvvLength = cvvLength;
    }

    public String getMonthRequired() {
        return monthRequired;
    }

    public void setMonthRequired(String monthRequired) {
        this.monthRequired = monthRequired;
    }

    public String getMonthLength() {
        return monthLength;
    }

    public void setMonthLength(String monthLength) {
        this.monthLength = monthLength;
    }

    public String getYearRequired() {
        return yearRequired;
    }

    public void setYearRequired(String yearRequired) {
        this.yearRequired = yearRequired;
    }

    public String getYearLength() {
        return yearLength;
    }

    public void setYearLength(String yearLength) {
        this.yearLength = yearLength;
    }

    public String getZipcodeRequired() {
        return zipcodeRequired;
    }

    public void setZipcodeRequired(String zipcodeRequired) {
        this.zipcodeRequired = zipcodeRequired;
    }

    public String getZipcodeLength() {
        return zipcodeLength;
    }

    public void setZipcodeLength(String zipcodeLength) {
        this.zipcodeLength = zipcodeLength;
    }
}
