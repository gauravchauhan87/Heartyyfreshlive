package com.heartyy.heartyyfresh.bean;

import java.util.List;

/**
 * Created by Dheeraj on 1/4/2016.
 */
public class HeartyPromotionBean {

    private NotificationUserProfileBean notificationUserProfileBean;
    private List<PromotionBean> promotionBeanList;
    private SharePromotionBean sharePromotionBean;

    public NotificationUserProfileBean getNotificationUserProfileBean() {
        return notificationUserProfileBean;
    }

    public void setNotificationUserProfileBean(NotificationUserProfileBean notificationUserProfileBean) {
        this.notificationUserProfileBean = notificationUserProfileBean;
    }

    public List<PromotionBean> getPromotionBeanList() {
        return promotionBeanList;
    }

    public void setPromotionBeanList(List<PromotionBean> promotionBeanList) {
        this.promotionBeanList = promotionBeanList;
    }

    public SharePromotionBean getSharePromotionBean() {
        return sharePromotionBean;
    }

    public void setSharePromotionBean(SharePromotionBean sharePromotionBean) {
        this.sharePromotionBean = sharePromotionBean;
    }
}
