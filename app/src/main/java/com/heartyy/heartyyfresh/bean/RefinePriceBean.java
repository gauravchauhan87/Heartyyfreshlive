package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class RefinePriceBean {

    private String from;
    private String to;
    private String label;
    private boolean isSelected = false;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
