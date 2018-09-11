package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class RefineSizeBean {

    private String size;
    private String uom;
    private boolean isSelected = false;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
