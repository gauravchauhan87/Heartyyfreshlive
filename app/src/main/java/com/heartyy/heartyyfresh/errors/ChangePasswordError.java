package com.heartyy.heartyyfresh.errors;

/**
 * Created by Dheeraj on 12/21/2015.
 */
public class ChangePasswordError {

    private String currentPasswordRequired;
    private String newPasswordrequired;
    private String passwordLength;
    private String same;

    public String getCurrentPasswordRequired() {
        return currentPasswordRequired;
    }

    public void setCurrentPasswordRequired(String currentPasswordRequired) {
        this.currentPasswordRequired = currentPasswordRequired;
    }

    public String getNewPasswordrequired() {
        return newPasswordrequired;
    }

    public void setNewPasswordrequired(String newPasswordrequired) {
        this.newPasswordrequired = newPasswordrequired;
    }

    public String getSame() {
        return same;
    }

    public void setSame(String same) {
        this.same = same;
    }

    public String getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(String passwordLength) {
        this.passwordLength = passwordLength;
    }
}
