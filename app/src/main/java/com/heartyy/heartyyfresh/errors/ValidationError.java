package com.heartyy.heartyyfresh.errors;

/**
 * Created by Dheeraj on 12/22/2015.
 */
public class ValidationError {

    private SignupError signupError;
    private LoginError loginError;
    private AddressError addressError;
    private CardError cardError;
    private SupportError supportError;
    private ChangePasswordError changePasswordError;

    public SignupError getSignupError() {
        return signupError;
    }

    public void setSignupError(SignupError signupError) {
        this.signupError = signupError;
    }

    public LoginError getLoginError() {
        return loginError;
    }

    public void setLoginError(LoginError loginError) {
        this.loginError = loginError;
    }

    public AddressError getAddressError() {
        return addressError;
    }

    public void setAddressError(AddressError addressError) {
        this.addressError = addressError;
    }

    public CardError getCardError() {
        return cardError;
    }

    public void setCardError(CardError cardError) {
        this.cardError = cardError;
    }

    public SupportError getSupportError() {
        return supportError;
    }

    public void setSupportError(SupportError supportError) {
        this.supportError = supportError;
    }

    public ChangePasswordError getChangePasswordError() {
        return changePasswordError;
    }

    public void setChangePasswordError(ChangePasswordError changePasswordError) {
        this.changePasswordError = changePasswordError;
    }
}
