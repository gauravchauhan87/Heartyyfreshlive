package com.heartyy.heartyyfresh.bean;

/**
 * Created by Vijay on 2/12/2016.
 */
public class DeliveryEstimateBean {
    private String estimatedDeliveryFee;
    private EstimatedCostDescriptionBean estimatedCostDescriptionBean;
    private EstimatedCostBreakDownBean estimatedCostBreakDownBean;


    public String getEstimatedDeliveryFee() {
        return estimatedDeliveryFee;
    }

    public void setEstimatedDeliveryFee(String estimatedDeliveryFee) {
        this.estimatedDeliveryFee = estimatedDeliveryFee;
    }

    public EstimatedCostDescriptionBean getEstimatedCostDescriptionBean() {
        return estimatedCostDescriptionBean;
    }

    public void setEstimatedCostDescriptionBean(EstimatedCostDescriptionBean estimatedCostDescriptionBeanl) {
        this.estimatedCostDescriptionBean = estimatedCostDescriptionBeanl;
    }

    public EstimatedCostBreakDownBean getEstimatedCostBreakDownBean() {
        return estimatedCostBreakDownBean;
    }

    public void setEstimatedCostBreakDownBean(EstimatedCostBreakDownBean estimatedCostBreakDownBean) {
        this.estimatedCostBreakDownBean = estimatedCostBreakDownBean;
    }
}
