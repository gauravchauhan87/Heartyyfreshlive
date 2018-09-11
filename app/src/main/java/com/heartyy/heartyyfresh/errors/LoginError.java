package com.heartyy.heartyyfresh.errors;

/**
 * Created by Dheeraj on 12/21/2015.
 */
public class LoginError {

    private String passwordRequired;
    private String emailRequired;
    private String emailInvalid;

    public String getPasswordRequired() {
        return passwordRequired;
    }

    public void setPasswordRequired(String passwordRequired) {
        this.passwordRequired = passwordRequired;
    }

    public String getEmailRequired() {
        return emailRequired;
    }

    public void setEmailRequired(String emailRequired) {
        this.emailRequired = emailRequired;
    }

    public String getEmailInvalid() {
        return emailInvalid;
    }

    public void setEmailInvalid(String emailInvalid) {
        this.emailInvalid = emailInvalid;
    }
}
