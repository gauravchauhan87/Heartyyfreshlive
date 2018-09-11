package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Vijay on 2/12/2016.
 */
public class UserCreditsBean {
    private String totalCreditAmount;
    List<AllCreditsBean> allCreditsBeanList;

    public String getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public void setTotalCreditAmount(String totalCreditAmount) {
        this.totalCreditAmount = totalCreditAmount;
    }

    public List<AllCreditsBean> getAllCreditsBeanList() {
        return allCreditsBeanList;
    }

    public void setAllCreditsBeanList(List<AllCreditsBean> allCreditsBeanList) {
        this.allCreditsBeanList = allCreditsBeanList;
    }
}
