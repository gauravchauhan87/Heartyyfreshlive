package com.heartyy.heartyyfresh.bean;

/**
 * Created by amitvashist on 10/13/15.
 */
public class NotificationUserProfileBean {
    public String notification_id;
    public String is_on;
    public String notification_display_text;
    public String notification_type;

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getIs_on() {
        return is_on;
    }

    public void setIs_on(String is_on) {
        this.is_on = is_on;
    }

    public String getNotification_display_text() {
        return notification_display_text;
    }

    public void setNotification_display_text(String notification_display_text) {
        this.notification_display_text = notification_display_text;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }
}
