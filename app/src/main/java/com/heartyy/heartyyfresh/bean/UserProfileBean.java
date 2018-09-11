package com.heartyy.heartyyfresh.bean;


import java.util.List;

/**
 * Created by amitvashist on 10/13/15.
 */
public class UserProfileBean {
    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String picture;
    public List<NotificationUserProfileBean> notification;
    UserCreditsBean userCreditsBean;

    public UserCreditsBean getUserCreditsBean() {
        return userCreditsBean;
    }

    public void setUserCreditsBean(UserCreditsBean userCreditsBean) {
        this.userCreditsBean = userCreditsBean;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<NotificationUserProfileBean> getNotification() {
        return notification;
    }

    public void setNotification(List<NotificationUserProfileBean> notification) {
        this.notification = notification;
    }
}
