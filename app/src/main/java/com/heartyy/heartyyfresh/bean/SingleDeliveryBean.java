package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/28/2016.
 */
public class SingleDeliveryBean {

    private String totalDeliveryEstimatedFee;
    private String totalDeliveryEstimatedFeeBeforeSix;
    private String totalDeliveryEstimatedFeeAfterSix;
    private List<WorkingDaysBean> workingDaysBeanList;

    public String getTotalDeliveryEstimatedFee() {
        return totalDeliveryEstimatedFee;
    }

    public void setTotalDeliveryEstimatedFee(String totalDeliveryEstimatedFee) {
        this.totalDeliveryEstimatedFee = totalDeliveryEstimatedFee;
    }

    public String getTotalDeliveryEstimatedFeeBeforeSix() {
        return totalDeliveryEstimatedFeeBeforeSix;
    }

    public void setTotalDeliveryEstimatedFeeBeforeSix(String totalDeliveryEstimatedFeeBeforeSix) {
        this.totalDeliveryEstimatedFeeBeforeSix = totalDeliveryEstimatedFeeBeforeSix;
    }

    public String getTotalDeliveryEstimatedFeeAfterSix() {
        return totalDeliveryEstimatedFeeAfterSix;
    }

    public void setTotalDeliveryEstimatedFeeAfterSix(String totalDeliveryEstimatedFeeAfterSix) {
        this.totalDeliveryEstimatedFeeAfterSix = totalDeliveryEstimatedFeeAfterSix;
    }

    public List<WorkingDaysBean> getWorkingDaysBeanList() {
        return workingDaysBeanList;
    }

    public void setWorkingDaysBeanList(List<WorkingDaysBean> workingDaysBeanList) {
        this.workingDaysBeanList = workingDaysBeanList;
    }
}
