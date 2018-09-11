package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 1/25/2016.
 */
public class SubstituionBean {
    private String text;
    private boolean isChecked = false;

    public SubstituionBean(String text,boolean isChecked){
        this.text = text;
        this.isChecked = isChecked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
