package com.heartyy.heartyyfresh.bean;

/**
 * Created by Dheeraj on 12/6/2015.
 */
public class SavedSectionItem implements Item {

    public String title;

    public SavedSectionItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
