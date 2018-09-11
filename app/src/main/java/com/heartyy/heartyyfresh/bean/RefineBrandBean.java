package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class RefineBrandBean {

    private String brandid;
    private String brandname;
    private String bandLogo;
    private String bandLogoThumbnail;
    private String isRecognizedBrand;
    private boolean isSelected = false;


    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getBandLogo() {
        return bandLogo;
    }

    public void setBandLogo(String bandLogo) {
        this.bandLogo = bandLogo;
    }

    public String getBandLogoThumbnail() {
        return bandLogoThumbnail;
    }

    public void setBandLogoThumbnail(String bandLogoThumbnail) {
        this.bandLogoThumbnail = bandLogoThumbnail;
    }

    public String getIsRecognizedBrand() {
        return isRecognizedBrand;
    }

    public void setIsRecognizedBrand(String isRecognizedBrand) {
        this.isRecognizedBrand = isRecognizedBrand;
    }
}
