package com.heartyy.heartyyfresh.helper;

import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.errors.*;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.promotionbean.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dheeraj on 12/1/2015.
 */
public class ConversionHelper {
    public static UserBean convertLoginUserJsonToLoginUserBean(JSONObject jsonObject) throws JSONException {
        UserBean user = null;
        if (jsonObject != null) {
            user = new UserBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            user.setFirstName(dataObj.getString("firstname"));
            user.setLastName(dataObj.getString("lastname"));
            user.setEmail(dataObj.getString("email"));
            user.setUserId(dataObj.getString("userId"));
            user.setPicture(dataObj.getString("picture"));
            user.setPhone(dataObj.getString("user_phone"));
            user.setZip(dataObj.getString("zipcode"));
        }

        return user;
    }

    public static UserProfileBean convertUserProfileJsonToUserProfileBean(JSONObject jsonObject) throws JSONException {
        UserProfileBean userProfileBean = null;
        if (jsonObject != null) {
            userProfileBean = new UserProfileBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            userProfileBean.setFirstName(dataObj.getString("firstname"));
            userProfileBean.setLastName(dataObj.getString("lastname"));
            userProfileBean.setEmail(dataObj.getString("email"));
            userProfileBean.setPhone(dataObj.getString("phone"));
            userProfileBean.setPicture(dataObj.getString("picture"));

            JSONArray category = dataObj.optJSONArray("notifications");
            if (category != null) {
                JSONArray notification = dataObj.getJSONArray("notifications");
                List<NotificationUserProfileBean> notifications = new ArrayList<>();
                for (int i = 0; i < notification.length(); i++) {

                    NotificationUserProfileBean notificationUserProfileBean = new NotificationUserProfileBean();
                    JSONObject jsonObj = notification.getJSONObject(i);
                    if (jsonObj.has("notification_id")) {
                        notificationUserProfileBean.setNotification_id(jsonObj.getString("notification_id"));
                    } else {
                        notificationUserProfileBean.setNotification_id(jsonObj.getString("id"));
                    }
                    notificationUserProfileBean.setNotification_display_text(jsonObj.getString("notification_display_text"));
                    if(jsonObj.has("is_on")){
                        notificationUserProfileBean.setIs_on(jsonObj.getString("is_on"));
                    }
                    notifications.add(notificationUserProfileBean);

                }
                userProfileBean.setNotification(notifications);
            }

            JSONObject optObj = dataObj.optJSONObject("credits");
            if (optObj != null) {
                JSONObject creditsObj = dataObj.getJSONObject("credits");
                UserCreditsBean userCreditsBean = null;
                AllCreditsBean allCreditsBean = null;
                if (creditsObj != null) {
                    userCreditsBean = new UserCreditsBean();
                    userCreditsBean.setTotalCreditAmount(creditsObj.getString("total_credit_amount"));
                    JSONArray cat = creditsObj.optJSONArray("credit_list");
                    if (cat != null) {
                        JSONArray creditsArray = creditsObj.getJSONArray("credit_list");
                        List<AllCreditsBean> allCreditsBeanList = new ArrayList<>();
                        for (int i = 0; i < creditsArray.length(); i++) {
                            allCreditsBean = new AllCreditsBean();
                            JSONObject crObj = creditsArray.getJSONObject(i);
                            allCreditsBean.setUserCreditId(crObj.getString("user_credit_id"));
                            allCreditsBean.setCreditDisplayText(crObj.getString("credit_display_text"));
                            allCreditsBean.setCreditReasonDisplay(crObj.getString("credit_reason_display"));
                            allCreditsBean.setCreditAmount(crObj.getString("credit_amount"));
                            allCreditsBean.setAppliedOrUsed(crObj.getString("applied_or_used"));
                            allCreditsBeanList.add(allCreditsBean);
                        }
                        userCreditsBean.setAllCreditsBeanList(allCreditsBeanList);
                    }
                    userProfileBean.setUserCreditsBean(userCreditsBean);
                }
            }
        }
        return userProfileBean;

    }

    public static List<LocationBean> convertDeliveryAddressJsonToDeliveryAddressBean(JSONObject jsonObject) throws JSONException {
        List<LocationBean> locationBeanList = null;
        if (jsonObject != null) {
            locationBeanList = new ArrayList<>();
            JSONArray category = jsonObject.optJSONArray("data");
            if (category != null) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    LocationBean location = new LocationBean();
                    location.setUserDeliveryLocationId(obj.getString("user_delivery_location_id"));
                    location.setUserId(obj.getString("user_id"));
                    location.setLocationType(obj.getString("location_type"));
                    location.setAddress1(obj.getString("address1"));
                    location.setAddress2(obj.getString("address2"));
                    location.setAptSuitUnit(obj.getString("apt_suite_unit"));
                    location.setLocationName(obj.getString("location_name"));
                    location.setDeliveryInstructions(obj.getString("delivery_instructions"));
                    location.setZipcode(obj.getString("zipcode"));
                    location.setIsPrimaryLocation(obj.getString("is_primary_location"));
                    location.setCity(obj.getString("city"));
                    location.setState(obj.getString("state"));
                    location.setCountry(obj.getString("country"));
                    location.setTaxRate(obj.getString("tax_rate"));

                    locationBeanList.add(location);


                }
            }
        }
        return locationBeanList;
    }

    public static List<PaymentCardBean> covertPaymentCardJsonToPaymentCardApiBean(JSONObject jsonObject) throws JSONException {
        List<PaymentCardBean> paymentCardBeanList = null;
        if (jsonObject != null) {
            paymentCardBeanList = new ArrayList<>();
            JSONArray category = jsonObject.optJSONArray("data");
            if (category != null) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    PaymentCardBean card = new PaymentCardBean();
                    card.setPaymentGatewayCustomerId(obj.getString("payment_gateway_customer_id"));
                    card.setCardCvvNumber(obj.getString("cc_number"));
                    card.setCardIssuer(obj.getString("card_issuer"));
                    card.setCardExpiratoinDate(obj.getString("expiration_date"));
                    card.setCardLastFourDigit(obj.getString("last_4_digits"));
                    card.setCardGivenName(obj.getString("given_card_name"));
                    card.setIsPrimary(obj.getString("is_primary"));
                    card.setToken(obj.getString("token"));
                    card.setUserPaymentMethodId(obj.getString("user_payment_method_id"));

                    paymentCardBeanList.add(card);
                }
            }

        }
        return paymentCardBeanList;
    }

    public static ValidationError convertErrorMsgsJsonToErrorMsgsBean(JSONObject jsonObject) throws JSONException {
        ValidationError validationError = null;
        if (jsonObject != null) {
            validationError = new ValidationError();
            JSONObject dataObj = jsonObject.getJSONObject("data");

            JSONObject profObj = dataObj.getJSONObject("profile");
            JSONObject profNameObj = profObj.getJSONObject("firstname");
            JSONObject profLastNameObj = profObj.getJSONObject("lastname");
            JSONObject profEmail = profObj.getJSONObject("email");
            JSONObject profPassword = profObj.getJSONObject("password");
            JSONObject profZip = profObj.getJSONObject("zipcode");
            JSONObject profPone = profObj.getJSONObject("phone");
            SignupError signupError = new SignupError();
            signupError.setNameRequired(profNameObj.getString("required"));
            signupError.setNameLength(profNameObj.getString("length"));
            signupError.setEmailRequired(profEmail.getString("required"));
            signupError.setEmailInvalid(profEmail.getString("invalid"));
            signupError.setPasswordRequired(profPassword.getString("required"));
            signupError.setPasswordInvalid(profPassword.getString("invalid"));
            signupError.setPasswordLength(profPassword.getString("length"));
            signupError.setZipcodeRequired(profZip.getString("required"));
            signupError.setZipcodeInvalid(profZip.getString("invalid"));
            signupError.setZipcodeLength(profZip.getString("length"));
            signupError.setPhoneRequired(profPone.getString("required"));
            signupError.setPhoneInvalid(profPone.getString("invalid"));
            signupError.setLastNameRequired(profLastNameObj.getString("required"));
            signupError.setLastNameLength(profLastNameObj.getString("length"));

            validationError.setSignupError(signupError);

            JSONObject lgObj = dataObj.getJSONObject("login");
            JSONObject passObj = lgObj.getJSONObject("password");
            JSONObject emailObj = lgObj.getJSONObject("email");
            LoginError loginError = new LoginError();
            loginError.setPasswordRequired(passObj.getString("required"));
            loginError.setEmailRequired(emailObj.getString("required"));
            loginError.setEmailInvalid(emailObj.getString("invalid"));

            validationError.setLoginError(loginError);

            JSONObject adrsObj = dataObj.getJSONObject("address");
            JSONObject searchObj = adrsObj.getJSONObject("search");
            JSONObject streetObj = adrsObj.getJSONObject("street");
            JSONObject aptObj = adrsObj.getJSONObject("apt");
            JSONObject cityObj = adrsObj.getJSONObject("city");
            JSONObject stateObj = adrsObj.getJSONObject("state");
            JSONObject zipObj = adrsObj.getJSONObject("zipcode");
            AddressError addressError = new AddressError();
            addressError.setNoResult(searchObj.getString("no-reuslt"));
            addressError.setStreetRequired(streetObj.getString("required"));
            addressError.setStreetLength(streetObj.getString("length"));
            addressError.setAptRequired(aptObj.getString("required"));
            addressError.setAptLength(aptObj.getString("length"));
            addressError.setCityRequired(cityObj.getString("required"));
            addressError.setCityLength(cityObj.getString("length"));
            addressError.setStateRequired(stateObj.getString("required"));
            addressError.setStateLength(stateObj.getString("length"));
            addressError.setZipcodeRequired(zipObj.getString("required"));
            addressError.setZipcodeInvalid(zipObj.getString("invalid"));
            addressError.setZipcodeLength(zipObj.getString("length"));

            validationError.setAddressError(addressError);

            JSONObject cardObj = dataObj.getJSONObject("card");
            JSONObject cardNumObj = cardObj.getJSONObject("number");
            JSONObject cvvObj = cardObj.getJSONObject("cvv");
            JSONObject monthObj = cardObj.getJSONObject("expiry-month");
            JSONObject yearObj = cardObj.getJSONObject("expiry-year");
            JSONObject cardZipObj = cardObj.getJSONObject("zipcode");
            CardError cardError = new CardError();
            cardError.setNumberRequired(cardNumObj.getString("required"));
            cardError.setNumberLength(cardNumObj.getString("length"));
            cardError.setCvvRequired(cvvObj.getString("required"));
            cardError.setCvvLength(cvvObj.getString("length"));
            cardError.setMonthRequired(monthObj.getString("required"));
            cardError.setMonthLength(monthObj.getString("length"));
            cardError.setYearRequired(yearObj.getString("required"));
            cardError.setYearLength(yearObj.getString("length"));
            cardError.setZipcodeRequired(cardZipObj.getString("required"));
            cardError.setZipcodeLength(cardZipObj.getString("length"));

            validationError.setCardError(cardError);

            JSONObject commentObj = dataObj.getJSONObject("support").getJSONObject("comment");
            SupportError supportError = new SupportError();
            supportError.setCommentRequired(commentObj.getString("required"));

            validationError.setSupportError(supportError);

            JSONObject crntPassObj = dataObj.getJSONObject("changePassword").getJSONObject("current");
            JSONObject newPassObj = dataObj.getJSONObject("changePassword").getJSONObject("new");

            ChangePasswordError changePasswordError = new ChangePasswordError();
            changePasswordError.setCurrentPasswordRequired(crntPassObj.getString("required"));
            changePasswordError.setNewPasswordrequired(newPassObj.getString("required"));
            changePasswordError.setSame(dataObj.getJSONObject("changePassword").getString("same"));
            changePasswordError.setPasswordLength(newPassObj.getString("length"));

            validationError.setChangePasswordError(changePasswordError);


        }


        return validationError;
    }

    public static StoreBean convertStoreJsonToStoreBeanList(JSONObject jsonObject) throws JSONException {
        StoreBean storeBeanList = null;
        if (jsonObject != null) {
            storeBeanList = new StoreBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            if (dataObj.has("has_address")) {
                storeBeanList.setHasAddress(dataObj.getString("has_address"));
            }
            if (dataObj.has("promotion_count")) {
                storeBeanList.setPromotionCount(dataObj.getString("promotion_count"));
            }
            if (dataObj.has("current_order_count")) {
                storeBeanList.setCurrentOrderCount(dataObj.getString("current_order_count"));
            }
            if (dataObj.has("rating")) {
                storeBeanList.setRating(dataObj.getString("rating"));
            }


            storeBeanList.setAvalilable(dataObj.getString("available"));
            JSONArray supCategory = dataObj.optJSONArray("suppliers");
            if (supCategory != null) {
                List<SuppliersBean> suppliersBeanList = new ArrayList<>();
                JSONArray suppArray = dataObj.getJSONArray("suppliers");
                for (int i = 0; i < suppArray.length(); i++) {
                    SuppliersBean suppliersBean = new SuppliersBean();
                    JSONObject supObj = suppArray.getJSONObject(i);
                    suppliersBean.setSupplierId(supObj.getString("supplier_id"));
                    suppliersBean.setSupplierName(supObj.getString("supplier_name"));
                    suppliersBean.setCity(supObj.getString("city"));
                    suppliersBean.setApplicableTaxRate(supObj.getString("applicable_tax_rate"));
                    suppliersBean.setSupplierEthnicityId(supObj.getString("supplier_ethnicity_id"));
                    suppliersBean.setPriceMarkup(supObj.getString("price_markup"));
                    suppliersBean.setEstimateDelivery(supObj.getString("initial_estimated_delivery_cost"));
                    suppliersBean.setEstimateDeliveryTime(supObj.getString("estimated_delivery_time"));

                    JSONObject cat = supObj.optJSONObject("inital_estimate_cost_break_down");
                    if (cat != null) {
                        JSONObject obj = supObj.getJSONObject("inital_estimate_cost_break_down");
                        EstimatedCostBreakDownBean estimatedCostBreakDownBean = new EstimatedCostBreakDownBean();
                        estimatedCostBreakDownBean.setLabourCost(obj.getString("labour_cost"));
                        estimatedCostBreakDownBean.setFuelCost(obj.getString("fuel_cost"));
                        estimatedCostBreakDownBean.setConvenience(obj.getString("convenience"));
                        estimatedCostBreakDownBean.setDeliveryEstimateCost(obj.getString("delivery_est_cost"));
                        estimatedCostBreakDownBean.setEstimateDistance(obj.getString("estimate_distance"));
                        estimatedCostBreakDownBean.setEstimateTime(obj.getString("estimate_time"));
                        estimatedCostBreakDownBean.setFrom(obj.getString("from"));
                        estimatedCostBreakDownBean.setTo(obj.getString("to"));
                        estimatedCostBreakDownBean.setAddressFrom(obj.getString("address_from"));
                        estimatedCostBreakDownBean.setAddressTo(obj.getString("address_to"));
                        LatLongBean latLongBean = null;

                        JSONArray pathCat = obj.optJSONArray("display_path_locations");
                        if (pathCat != null) {
                            latLongBean = new LatLongBean();
                            JSONArray pathArray = obj.getJSONArray("display_path_locations");
                            JSONObject sObj = pathArray.getJSONObject(0);
                            JSONObject dObj = pathArray.getJSONObject(pathArray.length() - 1);

                            latLongBean.setSourceLatitude(sObj.getJSONObject("latLng").getString("lat"));
                            latLongBean.setSourceLongitude(sObj.getJSONObject("latLng").getString("lng"));
                            latLongBean.setDestinationLatitude(dObj.getJSONObject("latLng").getString("lat"));
                            latLongBean.setDestinationLongitude(dObj.getJSONObject("latLng").getString("lng"));
                            estimatedCostBreakDownBean.setLatLongBean(latLongBean);

                        }
                        suppliersBean.setEstimatedCostBreakDownBean(estimatedCostBreakDownBean);
                    }
                    suppliersBean.setComingSoon(supObj.getString("coming_soon"));
                    suppliersBean.setComingSoonMessage(supObj.getString("coming_soon_message"));
                    suppliersBeanList.add(suppliersBean);
                }
                storeBeanList.setSuppliersBeanList(suppliersBeanList);
            }
            JSONArray category = dataObj.optJSONArray("promotions");
            if (category != null) {
                JSONArray promotionArray = dataObj.getJSONArray("promotions");
                List<PromotionBean> promotionBeanList = new ArrayList<>();
                for (int i = 0; i < promotionArray.length(); i++) {
                    PromotionBean prom = new PromotionBean();
                    JSONObject promObj = promotionArray.getJSONObject(i);
                    prom.setPromotionId(promObj.getString("promotion_id"));
                    prom.setPromotionTitle(promObj.getString("promotion_title"));
                    prom.setPromotionDisplayText(promObj.getString("promotion_display_text"));
                    prom.setBannerBackground(promObj.getString("banner_background"));
                    promotionBeanList.add(prom);
                }
                storeBeanList.setPromotionBeanList(promotionBeanList);
            }
            JSONObject apliCat = dataObj.optJSONObject("applicable_promotion");
            if (apliCat != null) {
                JSONObject apliPromObj = dataObj.getJSONObject("applicable_promotion");
                if (apliPromObj.optJSONObject("newuser") != null) {
                    JSONObject newObj = apliPromObj.getJSONObject("newuser");
                    NewUserPromotionBean newUserPromotionBean = new NewUserPromotionBean();
                    newUserPromotionBean.setFreeDelivery(newObj.getString("free_delivery"));
                    newUserPromotionBean.setMinOrderAmount(newObj.getString("min_order_amount"));
                    storeBeanList.setNewUserPromotionBean(newUserPromotionBean);
                } else if (apliPromObj.optJSONObject("delivery") != null) {
                    JSONObject newObj = apliPromObj.getJSONObject("delivery");
                    NewUserPromotionBean newUserPromotionBean = new NewUserPromotionBean();
                    newUserPromotionBean.setFreeDelivery(newObj.getString("free_delivery"));
                    newUserPromotionBean.setMinOrderAmount(newObj.getString("min_order_amount"));
                    storeBeanList.setNewUserPromotionBean(newUserPromotionBean);
                }


                JSONObject aplicableDaysCat = apliPromObj.optJSONObject("applicable_days");
                if(aplicableDaysCat != null){
                    JSONObject applicableDaysObj = apliPromObj.getJSONObject("applicable_days");
                    Map<String,String> promotionApplicableOnDays = new HashMap<String,String>();
                    promotionApplicableOnDays.put("MON",applicableDaysObj.getString("is_applicable_monday"));
                    promotionApplicableOnDays.put("TUE",applicableDaysObj.getString("is_applicable_tuesday"));
                    promotionApplicableOnDays.put("WED",applicableDaysObj.getString("is_applicable_wednesday"));
                    promotionApplicableOnDays.put("THU",applicableDaysObj.getString("is_applicable_thrusday"));
                    promotionApplicableOnDays.put("FRI",applicableDaysObj.getString("is_applicable_friday"));
                    promotionApplicableOnDays.put("SAT",applicableDaysObj.getString("is_applicable_saturday"));
                    promotionApplicableOnDays.put("SUN",applicableDaysObj.getString("is_applicable_sunday"));
                    Global.promotionApplicableOnDays = promotionApplicableOnDays;
                }
            }
        }

        return storeBeanList;
    }

    public static AisleBean convertAisleJsonToTopAisleBeanList(JSONObject jsonObject) throws JSONException {
        AisleBean aisleBean = null;
        if (jsonObject != null) {
            aisleBean = new AisleBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray promcategory = dataObj.optJSONArray("promotions");
            JSONObject popularcategory = dataObj.optJSONObject("populars");
            if (popularcategory != null) {
                JSONObject popularObj = dataObj.getJSONObject("populars");
                aisleBean.setPopularMore(popularObj.getString("is_more"));
                JSONArray popularArray = popularObj.getJSONArray("items");
                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                for (int i = 0; i < popularArray.length(); i++) {
                    JSONObject subItemObj = popularArray.getJSONObject(i);
                    SubAisleItemBean item = new SubAisleItemBean();
                    item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                    item.setTopCategoryId(subItemObj.getString("top_category_id"));
                    item.setItemId(subItemObj.getString("item_id"));
                    item.setPrice(subItemObj.getString("price"));
                    item.setDescription(subItemObj.getString("description"));
                    item.setItemName(subItemObj.getString("item_name"));
                    item.setSize(subItemObj.getString("size"));
                    item.setUom(subItemObj.getString("uom"));
                    item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                    item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                    item.setInStock(subItemObj.getString("in_stock"));
                    item.setSalePrice(subItemObj.getString("sale_price"));
                    item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                    item.setCount(subItemObj.getString("count"));
                    item.setFinalItemId(subItemObj.getString("final_item_id"));
                    item.setOnSale(subItemObj.getString("on_sale"));
                    item.setSale(subItemObj.getString("sale"));
                    item.setOffer(subItemObj.getString("offer"));
                    item.setShippingWeight(subItemObj.getString("shipping_weight"));
                    item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                    if (subItemObj.has("is_save")) {
                        item.setIsSave(subItemObj.getString("is_save"));
                    }

                    JSONObject brandCategory = subItemObj.optJSONObject("brand");
                    if (brandCategory != null) {
                        JSONObject brandObj = subItemObj.getJSONObject("brand");
                        BrandBean brandBean = new BrandBean();
                        brandBean.setBrandId(brandObj.getString("brand_id"));
                        brandBean.setBrandName(brandObj.getString("brand_name"));
                        if (brandObj.has("brand_logo")) {
                            brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                        }
                        if (brandObj.has("brand_logo_thumbnail")) {
                            brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                        }

                        item.setBrand(brandBean);
                    }
                    item.setNutrition(subItemObj.getString("nutrition"));
                    JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                    if (nutCat != null) {
                        JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                        NutritutionBean nutritutionBean = new NutritutionBean();
                        nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                        nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                        nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                        nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                        nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                        nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                        nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                        nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                        nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                        nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                        nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                        nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                        nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                        item.setNutritutionBean(nutritutionBean);
                    }
                    JSONObject imgObj = subItemObj.optJSONObject("images");
                    if (imgObj != null) {
                        JSONObject img = subItemObj.getJSONObject("images");
                        if (img.has("thumbnail")) {
                            item.setThumbnail(img.getString("thumbnail"));
                        }
                        JSONArray mainCat = img.optJSONArray("app");
                        if (mainCat != null) {
                            JSONArray mainArray = img.getJSONArray("app");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            item.setImagesBeanList(imagesBeanList);
                        }
                        JSONArray mainCat1 = img.optJSONArray("main");
                        if (mainCat1 != null) {
                            JSONArray mainArray = img.getJSONArray("main");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            item.setMainImagesBeanList(imagesBeanList);
                        }
                    } else {
                        item.setImages(subItemObj.getString("images"));
                    }
                    subAisleItemBeanList.add(item);
                }
                aisleBean.setPopularItemList(subAisleItemBeanList);
            }
            if (promcategory != null) {
                JSONArray promotionArray = dataObj.getJSONArray("promotions");
                List<PromotionBean> promotionBeanList = new ArrayList<>();
                for (int i = 0; i < promotionArray.length(); i++) {
                    PromotionBean prom = new PromotionBean();
                    JSONObject promObj = promotionArray.getJSONObject(i);
                    prom.setPromotionId(promObj.getString("promotion_id"));
                    prom.setPromotionTitle(promObj.getString("promotion_title"));
                    prom.setPromotionDisplayText(promObj.getString("promotion_display_text"));
                    prom.setBannerBackground(promObj.getString("banner_background"));
                    promotionBeanList.add(prom);
                }
                aisleBean.setPromotionBeanList(promotionBeanList);
            }
            JSONObject previousCat = dataObj.optJSONObject("previous_orders");
            if (previousCat != null) {
                JSONObject prevObj = dataObj.getJSONObject("previous_orders");
                PreviousOrderBean previousOrderBean = new PreviousOrderBean();
                previousOrderBean.setIsMore(prevObj.getString("is_more"));
                JSONArray prevCatArray = prevObj.optJSONArray("items");
                if (prevCatArray != null) {
                    List<SubAisleItemBean> previousOrderBeanList = new ArrayList<>();
                    JSONArray prevArray = prevObj.getJSONArray("items");
                    for (int s = 0; s < prevArray.length(); s++) {
                        JSONObject pObj = prevArray.getJSONObject(s);
                        SubAisleItemBean item = new SubAisleItemBean();
                        item.setItemId(pObj.getString("item_id"));
                        item.setPrice(pObj.getString("price"));
                        item.setItemName(pObj.getString("item_name"));
                        item.setSize(pObj.getString("size"));
                        item.setUom(pObj.getString("uom"));
                        item.setIsTaxApplicable(pObj.getString("is_tax_applicable"));
                        item.setSubIsTaxApplicable(pObj.getString("sub_is_tax_applicable"));
                        item.setInStock(pObj.getString("in_stock"));
                        item.setSalePrice(pObj.getString("sale_price"));
                        item.setMaxQuantity(pObj.getString("max_quantity_per_order"));
                        item.setCount(pObj.getString("count"));
                        item.setFinalItemId(pObj.getString("final_item_id"));
                        item.setOnSale(pObj.getString("on_sale"));
                        item.setSale(pObj.getString("sale"));
                        item.setOffer(pObj.getString("offer"));
                        item.setShippingWeight(pObj.getString("shipping_weight"));
                        item.setSupplierItemId(pObj.getString("supplier_item_id"));
                        if (pObj.has("is_save")) {
                            item.setIsSave(pObj.getString("is_save"));
                        }

                        JSONObject brandCategory = pObj.optJSONObject("brand");
                        if (brandCategory != null) {
                            JSONObject brandObj = pObj.getJSONObject("brand");
                            BrandBean brandBean = new BrandBean();
                            brandBean.setBrandId(brandObj.getString("brand_id"));
                            brandBean.setBrandName(brandObj.getString("brand_name"));
                            if (brandObj.has("brand_logo")) {
                                brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                            }
                            if (brandObj.has("brand_logo_thumbnail")) {
                                brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                            }
                            item.setBrand(brandBean);
                        }
                        item.setNutrition(pObj.getString("nutrition"));
                        JSONArray nutCat = pObj.optJSONArray("nutrition");
                        if (nutCat != null) {
                            JSONObject nutObj = pObj.getJSONArray("nutrition").getJSONObject(0);
                            NutritutionBean nutritutionBean = new NutritutionBean();
                            nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                            nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                            nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                            nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                            nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                            nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                            nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                            nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                            nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                            nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                            nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                            nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                            nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                            item.setNutritutionBean(nutritutionBean);
                        }
                        JSONObject imgObj = pObj.optJSONObject("images");
                        if (imgObj != null) {
                            JSONObject img = pObj.getJSONObject("images");
                            if (img.has("thumbnail")) {
                                item.setThumbnail(img.getString("thumbnail"));
                            }
                            JSONArray mainCat = img.optJSONArray("app");
                            if (mainCat != null) {
                                JSONArray mainArray = img.getJSONArray("app");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setImagesBeanList(imagesBeanList);
                            }
                            JSONArray mainCat1 = img.optJSONArray("main");
                            if (mainCat1 != null) {
                                JSONArray mainArray = img.getJSONArray("main");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setMainImagesBeanList(imagesBeanList);
                            }
                        } else {
                            item.setImages(pObj.getString("images"));
                        }

                        previousOrderBeanList.add(item);
                    }
                    previousOrderBean.setPreviousItemList(previousOrderBeanList);
                    aisleBean.setPreviousOrderBean(previousOrderBean);
                }
            }
            JSONArray topArray = dataObj.getJSONArray("top_aisle");
            List<TopAisleBean> topAisleBeanList = new ArrayList<>();
            for (int i = 0; i < topArray.length(); i++) {
                JSONObject topObj = topArray.getJSONObject(i);
                TopAisleBean topAisleBean = new TopAisleBean();
                topAisleBean.setTopCategoryId(topObj.getString("top_category_id"));
                topAisleBean.setTopCategoryName(topObj.getString("top_category_name"));
                JSONObject topCat = topObj.optJSONObject("top_category_popular");
                if (topCat != null) {
                    JSONObject tcpObj = topObj.getJSONObject("top_category_popular");
                    topAisleBean.setIsMore(tcpObj.getString("is_more"));
                    JSONArray itemsCat = tcpObj.optJSONArray("items");
                    List<SubAisleItemBean> topCategoryPopularBeanList = null;
                    if (itemsCat != null) {
                        topCategoryPopularBeanList = new ArrayList<>();
                        JSONArray itemsArray = tcpObj.getJSONArray("items");
                        for (int j = 0; j < itemsArray.length(); j++) {
                            JSONObject subItemObj = itemsArray.getJSONObject(j);
                            SubAisleItemBean item = new SubAisleItemBean();
                            item.setSale(subItemObj.getString("sale"));
                            item.setItemId(subItemObj.getString("item_id"));
                            item.setPrice(subItemObj.getString("price"));
                            item.setItemName(subItemObj.getString("item_name"));
                            item.setSize(subItemObj.getString("size"));
                            item.setUom(subItemObj.getString("uom"));
                            item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                            item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                            item.setInStock(subItemObj.getString("in_stock"));
                            item.setSalePrice(subItemObj.getString("sale_price"));
                            item.setShippingWeight(subItemObj.getString("shipping_weight"));
                            item.setTopCategoryId(subItemObj.getString("top_category_id"));
                            item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                            item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                            // item.setBuyGetFree(subItemObj.getString("buygetfree"));
//                        item.setBuy(subItemObj.getString("buy"));
                            //  item.setGet(subItemObj.getString("get"));
                            item.setCount(subItemObj.getString("count"));
                            item.setFinalItemId(subItemObj.getString("final_item_id"));
                            item.setOnSale(subItemObj.getString("on_sale"));
                            //  item.setPercentOff(subItemObj.getString("percent_off"));
                            if (subItemObj.has("is_save")) {
                                item.setIsSave(subItemObj.getString("is_save"));
                            }
                            item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                            item.setDescription(subItemObj.getString("description"));
                            if (subItemObj.has("offer")) {
                                item.setOffer(subItemObj.getString("offer"));
                            }
                            JSONObject brandCategory = subItemObj.optJSONObject("brand");
                            if (brandCategory != null) {
                                JSONObject brandObj = subItemObj.getJSONObject("brand");
                                BrandBean brandBean = new BrandBean();
                                brandBean.setBrandId(brandObj.getString("brand_id"));
                                brandBean.setBrandName(brandObj.getString("brand_name"));
                                if (brandObj.has("brand_logo")) {
                                    brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                                }
                                if (brandObj.has("brand_logo_thumbnail")) {
                                    brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                                }
                                item.setBrand(brandBean);
                            }
                            item.setNutrition(subItemObj.getString("nutrition"));
                            JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                            if (nutCat != null) {
                                JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                                NutritutionBean nutritutionBean = new NutritutionBean();
                                nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                                nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                                nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                                nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                                nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                                nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                                nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                                nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                                nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                                nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                                nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                                nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                                nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                                item.setNutritutionBean(nutritutionBean);
                            }
                            JSONObject imgObj = subItemObj.optJSONObject("images");
                            if (imgObj != null) {
                                JSONObject img = subItemObj.getJSONObject("images");
                                if (img.has("thumbnail")) {
                                    item.setThumbnail(img.getString("thumbnail"));
                                }
                                JSONArray mainCat = img.optJSONArray("app");
                                if (mainCat != null) {
                                    JSONArray mainArray = img.getJSONArray("app");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setImagesBeanList(imagesBeanList);
                                }
                                JSONArray mainCat1 = img.optJSONArray("main");
                                if (mainCat1 != null) {
                                    JSONArray mainArray = img.getJSONArray("main");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setMainImagesBeanList(imagesBeanList);
                                }
                            } else {
                                item.setImages(subItemObj.getString("images"));
                            }

                            topCategoryPopularBeanList.add(item);

                        }


                        topAisleBean.setTopCategoryPopularBeanList(topCategoryPopularBeanList);
                    }
                }
                JSONArray subcat = topObj.optJSONArray("sub_aisle");
                if (subcat != null) {
                    JSONArray subArray = topObj.getJSONArray("sub_aisle");
                    List<SubAisleBean> subAisleBeanList = new ArrayList<>();
                    for (int j = 0; j < subArray.length(); j++) {
                        SubAisleBean subAisleBean = new SubAisleBean();
                        JSONObject subObj = subArray.getJSONObject(j);
                        subAisleBean.setSubCategoryId(subObj.getString("sub_category_id"));
                        subAisleBean.setSubCategoryName(subObj.getString("sub_category_name"));
                        subAisleBeanList.add(subAisleBean);
                    }

                    topAisleBean.setSubAisleBeanList(subAisleBeanList);
                    topAisleBeanList.add(topAisleBean);
                }


            }

            aisleBean.setTopAisleBeanList(topAisleBeanList);


        }
        return aisleBean;
    }

    public static CategoryAisleBean convertCategoryAisleJsonToCategoryAisleBean(JSONObject jsonObject) throws JSONException {
        CategoryAisleBean categoryAisleBean = null;
        if (jsonObject != null) {
            categoryAisleBean = new CategoryAisleBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONObject pop1Cat = dataObj.optJSONObject("popular");
            if (pop1Cat != null) {
                JSONObject pop1 = dataObj.getJSONObject("popular");
                JSONArray popCat = pop1.optJSONArray("items");
                    List<SubAisleItemBean> popList = null;
                if (popCat != null) {
                    JSONArray popArray = pop1.getJSONArray("items");
                    categoryAisleBean.setPopularIsMore(pop1.getString("is_more"));
                    popList = new ArrayList<>();
                    for (int i = 0; i < popArray.length(); i++) {
                        JSONObject popObj = popArray.getJSONObject(i);
                        SubAisleItemBean pop = new SubAisleItemBean();
                        pop.setTopCategoryId(popObj.getString("top_category_id"));
                        pop.setSubCategoryId(popObj.getString("sub_category_id"));
                        pop.setSale(popObj.getString("sale"));
                        pop.setItemId(popObj.getString("item_id"));
                        pop.setPrice(popObj.getString("price"));
                        pop.setItemName(popObj.getString("item_name"));
                        pop.setSize(popObj.getString("size"));
                        pop.setUom(popObj.getString("uom"));
                        pop.setIsTaxApplicable(popObj.getString("is_tax_applicable"));
                        pop.setSubIsTaxApplicable(popObj.getString("sub_is_tax_applicable"));
                        pop.setInStock(popObj.getString("in_stock"));
                        pop.setSalePrice(popObj.getString("sale_price"));
                        pop.setMaxQuantity(popObj.getString("max_quantity_per_order"));
                        pop.setShippingWeight(popObj.getString("shipping_weight"));
                        pop.setCount(popObj.getString("count"));
                        pop.setFinalItemId(popObj.getString("final_item_id"));
                        pop.setOnSale(popObj.getString("on_sale"));
                        if (popObj.has("is_save")) {
                            pop.setIsSave(popObj.getString("is_save"));
                        }
                        pop.setSupplierItemId(popObj.getString("supplier_item_id"));
                        pop.setDescription(popObj.getString("description"));
                        if (popObj.has("offer")) {
                            pop.setOffer(popObj.getString("offer"));
                        }
                        JSONObject brandCategory = popObj.optJSONObject("brand");
                        if (brandCategory != null) {
                            JSONObject brandObj = popObj.getJSONObject("brand");
                            BrandBean brandBean = new BrandBean();
                            brandBean.setBrandId(brandObj.getString("brand_id"));
                            brandBean.setBrandName(brandObj.getString("brand_name"));
                            brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                            brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                            pop.setBrand(brandBean);
                        }
                        pop.setNutrition(popObj.getString("nutrition"));
                        JSONArray nutCat = popObj.optJSONArray("nutrition");
                        if (nutCat != null) {
                            JSONObject nutObj = popObj.getJSONArray("nutrition").getJSONObject(0);
                            NutritutionBean nutritutionBean = new NutritutionBean();
                            nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                            nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                            nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                            nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                            nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                            nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                            nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                            nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                            nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                            nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                            nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                            nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                            nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                            pop.setNutritutionBean(nutritutionBean);
                        }
                        JSONObject imgObj = popObj.optJSONObject("images");
                        if (imgObj != null) {
                            JSONObject img = popObj.getJSONObject("images");
                            if (img.has("thumbnail")) {
                                pop.setThumbnail(img.getString("thumbnail"));
                            }
                            JSONArray mainCat = img.optJSONArray("app");
                            if (mainCat != null) {
                                JSONArray mainArray = img.getJSONArray("app");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                pop.setImagesBeanList(imagesBeanList);
                            }
                            JSONArray mainCat1 = img.optJSONArray("main");
                            if (mainCat1 != null) {
                                JSONArray mainArray = img.getJSONArray("main");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                pop.setMainImagesBeanList(imagesBeanList);
                            }
                        } else {
                            pop.setImages(popObj.getString("images"));
                        }
                        popList.add(pop);

                    }
                }
                categoryAisleBean.setPopularAisleItemBeanList(popList);
            }
            JSONArray saveCat = dataObj.optJSONArray("saved");
            List<SubAisleItemBean> saveList = null;
            if (saveCat != null) {
                JSONArray saveArray = dataObj.getJSONArray("saved");
                saveList = new ArrayList<>();
                for (int i = 0; i < saveArray.length(); i++) {
                    JSONObject popObj = saveArray.getJSONObject(i);
                    SubAisleItemBean pop = new SubAisleItemBean();
                    pop.setTopCategoryId(popObj.getString("top_category_id"));
                    pop.setSubCategoryId(popObj.getString("sub_category_id"));
                    pop.setSale(popObj.getString("sale"));
                    pop.setItemId(popObj.getString("item_id"));
                    pop.setPrice(popObj.getString("price"));
                    pop.setItemName(popObj.getString("item_name"));
                    pop.setSize(popObj.getString("size"));
                    pop.setUom(popObj.getString("uom"));
                    pop.setIsTaxApplicable(popObj.getString("is_tax_applicable"));
                    pop.setSubIsTaxApplicable(popObj.getString("sub_is_tax_applicable"));
                    pop.setInStock(popObj.getString("in_stock"));
                    pop.setSalePrice(popObj.getString("sale_price"));
                    pop.setMaxQuantity(popObj.getString("max_quantity_per_order"));
                    pop.setShippingWeight(popObj.getString("shipping_weight"));
                    pop.setCount(popObj.getString("count"));
                    pop.setFinalItemId(popObj.getString("final_item_id"));
                    pop.setOnSale(popObj.getString("on_sale"));
                    if (popObj.has("is_save")) {
                        pop.setIsSave(popObj.getString("is_save"));
                    }
                    pop.setSupplierItemId(popObj.getString("supplier_item_id"));
                    pop.setDescription(popObj.getString("description"));
                    if (popObj.has("offer")) {
                        pop.setOffer(popObj.getString("offer"));
                    }
                    JSONObject brandCategory = popObj.optJSONObject("brand");
                    if (brandCategory != null) {
                        JSONObject brandObj = popObj.getJSONObject("brand");
                        BrandBean brandBean = new BrandBean();
                        brandBean.setBrandId(brandObj.getString("brand_id"));
                        brandBean.setBrandName(brandObj.getString("brand_name"));
                        brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                        brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                        pop.setBrand(brandBean);
                    }
                    pop.setNutrition(popObj.getString("nutrition"));
                    JSONArray nutCat = popObj.optJSONArray("nutrition");
                    if (nutCat != null) {
                        JSONObject nutObj = popObj.getJSONArray("nutrition").getJSONObject(0);
                        NutritutionBean nutritutionBean = new NutritutionBean();
                        nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                        nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                        nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                        nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                        nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                        nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                        nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                        nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                        nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                        nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                        nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                        nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                        nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                        pop.setNutritutionBean(nutritutionBean);
                    }
                    JSONObject imgObj = popObj.optJSONObject("images");
                    if (imgObj != null) {
                        JSONObject img = popObj.getJSONObject("images");
                        if (img.has("thumbnail")) {
                            pop.setThumbnail(img.getString("thumbnail"));
                        }
                        JSONArray mainCat = img.optJSONArray("app");
                        if (mainCat != null) {
                            JSONArray mainArray = img.getJSONArray("app");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            pop.setImagesBeanList(imagesBeanList);
                        }
                        JSONArray mainCat1 = img.optJSONArray("main");
                        if (mainCat1 != null) {
                            JSONArray mainArray = img.getJSONArray("main");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            pop.setMainImagesBeanList(imagesBeanList);
                        }
                    } else {
                        pop.setImages(popObj.getString("images"));
                    }
                    saveList.add(pop);

                }
                categoryAisleBean.setPopularAisleItemBeanList(saveList);
            }

            JSONObject topCat = dataObj.optJSONObject("top_aisle");
            if (topCat != null) {
                JSONObject topObj = dataObj.getJSONObject("top_aisle");
                TopAisleBean topAisleBean = new TopAisleBean();
                topAisleBean.setTopCategoryId(topObj.getString("top_category_id"));
                topAisleBean.setTopCategoryName(topObj.getString("top_category_name"));
                JSONArray subArray = topObj.getJSONArray("sub_aisle");
                List<SubAisleBean> subAisleBeanList = new ArrayList<>();
                for (int j = 0; j < subArray.length(); j++) {
                    SubAisleBean subAisleBean = new SubAisleBean();
                    JSONObject subObj = subArray.getJSONObject(j);
                    subAisleBean.setSubCategoryId(subObj.getString("sub_category_id"));
                    subAisleBean.setSubCategoryName(subObj.getString("sub_category_name"));
                    subAisleBean.setIsMore(subObj.getString("is_more"));
                    if (subObj.has("is_tax_applicable")) {
                        subAisleBean.setIsTaxApplicable(subObj.getString("is_tax_applicable"));
                    }
                    JSONArray subItemsCat = subObj.optJSONArray("sub_aisle_items");
                    if (subItemsCat != null) {
                        JSONArray subIemsArray = subObj.getJSONArray("sub_aisle_items");
                        List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                        for (int k = 0; k < subIemsArray.length(); k++) {
                            JSONObject subItemObj = subIemsArray.getJSONObject(k);
                            SubAisleItemBean item = new SubAisleItemBean();
                            item.setSale(subItemObj.getString("sale"));
                            item.setTopCategoryId(subItemObj.getString("top_category_id"));
                            item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                            item.setItemId(subItemObj.getString("item_id"));
                            item.setPrice(subItemObj.getString("price"));
                            item.setItemName(subItemObj.getString("item_name"));
                            item.setSize(subItemObj.getString("size"));
                            item.setUom(subItemObj.getString("uom"));
                            item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                            item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                            item.setInStock(subItemObj.getString("in_stock"));
                            item.setSalePrice(subItemObj.getString("sale_price"));
                            item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                            item.setShippingWeight(subItemObj.getString("shipping_weight"));
                            item.setCount(subItemObj.getString("count"));
                            item.setFinalItemId(subItemObj.getString("final_item_id"));
                            item.setOnSale(subItemObj.getString("on_sale"));
                            if (subItemObj.has("is_save")) {
                                item.setIsSave(subItemObj.getString("is_save"));
                            }
                            item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                            item.setDescription(subItemObj.getString("description"));
                            if (subItemObj.has("offer")) {
                                item.setOffer(subItemObj.getString("offer"));
                            }
                            JSONObject brandCategory = subItemObj.optJSONObject("brand");
                            if (brandCategory != null) {
                                JSONObject brandObj = subItemObj.getJSONObject("brand");
                                BrandBean brandBean = new BrandBean();
                                brandBean.setBrandId(brandObj.getString("brand_id"));
                                brandBean.setBrandName(brandObj.getString("brand_name"));
                                brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                                brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                                item.setBrand(brandBean);
                            }
                            item.setNutrition(subItemObj.getString("nutrition"));
                            JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                            if (nutCat != null) {
                                JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                                NutritutionBean nutritutionBean = new NutritutionBean();
                                nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                                nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                                nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                                nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                                nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                                nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                                nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                                nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                                nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                                nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                                nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                                nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                                nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                                item.setNutritutionBean(nutritutionBean);
                            }

                            JSONObject imgObj = subItemObj.optJSONObject("images");
                            if (imgObj != null) {
                                JSONObject img = subItemObj.getJSONObject("images");
                                if (img.has("thumbnail")) {
                                    item.setThumbnail(img.getString("thumbnail"));
                                }
                                JSONArray mainCat = img.optJSONArray("app");
                                if (mainCat != null) {
                                    JSONArray mainArray = img.getJSONArray("app");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setImagesBeanList(imagesBeanList);
                                }
                                JSONArray mainCat1 = img.optJSONArray("main");
                                if (mainCat1 != null) {
                                    JSONArray mainArray = img.getJSONArray("main");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setMainImagesBeanList(imagesBeanList);
                                }
                            } else {
                                item.setImages(subItemObj.getString("images"));
                            }

                            subAisleItemBeanList.add(item);


                        }
                        subAisleBean.setSubAisleItemBeanList(subAisleItemBeanList);
                        subAisleBeanList.add(subAisleBean);
                    }
                }

                topAisleBean.setSubAisleBeanList(subAisleBeanList);
                categoryAisleBean.setTopAisleBean(topAisleBean);
            }
        }

        return categoryAisleBean;
    }

    public static CategoryAisleBean convertTopCategoryJsonToTopCategoryBeanOnTopCategoryBasis(JSONObject jsonObject) throws JSONException {
        CategoryAisleBean categoryAisleBean = null;
        if (jsonObject != null) {
            categoryAisleBean = new CategoryAisleBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONObject topObj = dataObj.getJSONObject("top_aisle");
            TopAisleBean topAisleBean = new TopAisleBean();
            topAisleBean.setTopCategoryId(topObj.getString("top_category_id"));
            topAisleBean.setTopCategoryName(topObj.getString("top_category_name"));
            JSONArray subArray = topObj.getJSONArray("sub_aisle");
            List<SubAisleBean> subAisleBeanList = new ArrayList<>();
            for (int j = 0; j < subArray.length(); j++) {
                SubAisleBean subAisleBean = new SubAisleBean();
                JSONObject subObj = subArray.getJSONObject(j);
                subAisleBean.setSubCategoryId(subObj.getString("sub_category_id"));
                subAisleBean.setSubCategoryName(subObj.getString("sub_category_name"));
                if (subObj.has("is_more")) {
                    subAisleBean.setIsMore(subObj.getString("is_more"));
                }
                if (subObj.has("is_tax_applicable")) {
                    subAisleBean.setIsTaxApplicable(subObj.getString("is_tax_applicable"));
                }
                JSONArray subItemsCat = subObj.optJSONArray("sub_aisle_items");
                if (subItemsCat != null) {
                    JSONArray subIemsArray = subObj.getJSONArray("sub_aisle_items");
                    List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                    for (int k = 0; k < subIemsArray.length(); k++) {
                        JSONObject subItemObj = subIemsArray.getJSONObject(k);
                        SubAisleItemBean item = new SubAisleItemBean();
                        item.setSale(subItemObj.getString("sale"));
                        item.setItemId(subItemObj.getString("item_id"));
                        item.setPrice(subItemObj.getString("price"));
                        item.setItemName(subItemObj.getString("item_name"));
                        item.setSize(subItemObj.getString("size"));
                        item.setUom(subItemObj.getString("uom"));
                        item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                        item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                        item.setInStock(subItemObj.getString("in_stock"));
                        item.setSalePrice(subItemObj.getString("sale_price"));
                        item.setShippingWeight(subItemObj.getString("shipping_weight"));
                        item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                        item.setCount(subItemObj.getString("count"));
                        item.setFinalItemId(subItemObj.getString("final_item_id"));
                        item.setOnSale(subItemObj.getString("on_sale"));
                        item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                        if (subItemObj.has("is_save")) {
                            item.setIsSave(subItemObj.getString("is_save"));
                        }
                        item.setDescription(subItemObj.getString("description"));
                        if (subItemObj.has("offer")) {
                            item.setOffer(subItemObj.getString("offer"));
                        }
                        JSONObject brandCategory = subItemObj.optJSONObject("brand");
                        if (brandCategory != null) {
                            JSONObject brandObj = subItemObj.getJSONObject("brand");
                            BrandBean brandBean = new BrandBean();
                            brandBean.setBrandId(brandObj.getString("brand_id"));
                            brandBean.setBrandName(brandObj.getString("brand_name"));
                            brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                            brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                            item.setBrand(brandBean);
                        }
                        item.setNutrition(subItemObj.getString("nutrition"));
                        JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                        if (nutCat != null) {
                            JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                            NutritutionBean nutritutionBean = new NutritutionBean();
                            nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                            nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                            nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                            nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                            nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                            nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                            nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                            nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                            nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                            nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                            nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                            nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                            nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                            item.setNutritutionBean(nutritutionBean);
                        }
                        JSONObject imgObj = subItemObj.optJSONObject("images");
                        if (imgObj != null) {
                            JSONObject img = subItemObj.getJSONObject("images");
                            if (img.has("thumbnail")) {
                                item.setThumbnail(img.getString("thumbnail"));
                            }
                            JSONArray mainCat = img.optJSONArray("app");
                            if (mainCat != null) {
                                JSONArray mainArray = img.getJSONArray("app");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setImagesBeanList(imagesBeanList);
                            }
                            JSONArray mainCat1 = img.optJSONArray("main");
                            if (mainCat1 != null) {
                                JSONArray mainArray = img.getJSONArray("main");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setMainImagesBeanList(imagesBeanList);
                            }
                        } else {
                            item.setImages(subItemObj.getString("images"));
                        }

                        subAisleItemBeanList.add(item);


                    }
                    subAisleBean.setSubAisleItemBeanList(subAisleItemBeanList);
                    subAisleBeanList.add(subAisleBean);
                }
            }

            topAisleBean.setSubAisleBeanList(subAisleBeanList);
            categoryAisleBean.setTopAisleBean(topAisleBean);
        }
        return categoryAisleBean;
    }

    public static SortItemsBean convertTopCategoryJsonToTopCategoryBean(JSONObject jsonObject) throws JSONException {
        SortItemsBean sortItemsBean = null;
        if (jsonObject != null) {
            sortItemsBean = new SortItemsBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONObject topObj = dataObj.getJSONObject("top_aisle");
            TopCategoryBean topCategoryBean = new TopCategoryBean();
            topCategoryBean.setTopCategoryId(topObj.getString("top_category_id"));
            topCategoryBean.setTopCategoryName(topObj.getString("top_category_name"));

            JSONObject subObj = topObj.getJSONObject("sub_aisle");
            SubAisleBean subAisleBean = new SubAisleBean();
            subAisleBean.setSubCategoryId(subObj.getString("sub_category_id"));
            subAisleBean.setSubCategoryName(subObj.getString("sub_category_name"));
            JSONArray subItemsCat = subObj.optJSONArray("sub_aisle_items");
            if (subItemsCat != null) {
                JSONArray subIemsArray = subObj.getJSONArray("sub_aisle_items");
                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                for (int k = 0; k < subIemsArray.length(); k++) {
                    JSONObject subItemObj = subIemsArray.getJSONObject(k);
                    SubAisleItemBean item = new SubAisleItemBean();
                    item.setItemId(subItemObj.getString("item_id"));
                    item.setPrice(subItemObj.getString("price"));
                    item.setItemName(subItemObj.getString("item_name"));
                    item.setSize(subItemObj.getString("size"));
                    item.setUom(subItemObj.getString("uom"));
                    item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                    item.setInStock(subItemObj.getString("in_stock"));
                    item.setSalePrice(subItemObj.getString("sale_price"));
                    item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                    item.setBuy(subItemObj.getString("buy"));
                    item.setGet(subItemObj.getString("get"));
                    item.setCount(subItemObj.getString("count"));
                    item.setFinalItemId(subItemObj.getString("final_item_id"));
                    item.setOnSale(subItemObj.getString("on_sale"));
                    item.setShippingWeight(subItemObj.getString("shipping_weight"));

                    item.setPercentOff(subItemObj.getString("percent_off"));
                    if (subItemObj.has("is_save")) {
                        item.setIsSave(subItemObj.getString("is_save"));
                    }
                    item.setDescription(subItemObj.getString("description"));
                    if (subItemObj.has("offer")) {
                        item.setOffer(subItemObj.getString("offer"));
                    }
                    JSONObject brandCategory = subItemObj.optJSONObject("brand");
                    if (brandCategory != null) {
                        JSONObject brandObj = subItemObj.getJSONObject("brand");
                        BrandBean brandBean = new BrandBean();
                        brandBean.setBrandId(brandObj.getString("brand_id"));
                        brandBean.setBrandName(brandObj.getString("brand_name"));
                        brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                        brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                        item.setBrand(brandBean);
                    }
                    item.setNutrition(subItemObj.getString("nutrition"));
                    JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                    if (nutCat != null) {
                        JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                        NutritutionBean nutritutionBean = new NutritutionBean();
                        nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                        nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                        nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                        nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                        nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                        nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                        nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                        nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                        nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                        nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                        nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                        nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                        nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                        item.setNutritutionBean(nutritutionBean);
                    }
                    JSONObject imgObj = subItemObj.optJSONObject("images");
                    if (imgObj != null) {
                        JSONObject img = subItemObj.getJSONObject("images");
                        if (img.has("thumbnail")) {
                            item.setThumbnail(img.getString("thumbnail"));
                        }
                        JSONArray mainCat = img.optJSONArray("app");
                        if (mainCat != null) {
                            JSONArray mainArray = img.getJSONArray("app");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            item.setImagesBeanList(imagesBeanList);
                        }
                        JSONArray mainCat1 = img.optJSONArray("main");
                        if (mainCat1 != null) {
                            JSONArray mainArray = img.getJSONArray("main");
                            List<ImagesBean> imagesBeanList = new ArrayList<>();
                            for (int c = 0; c < mainArray.length(); c++) {
                                String img1 = mainArray.getString(c);
                                ImagesBean imagesBean = new ImagesBean();
                                imagesBean.setImage(img1);
                                imagesBeanList.add(imagesBean);

                            }
                            item.setMainImagesBeanList(imagesBeanList);
                        }
                    } else {
                        item.setImages(subItemObj.getString("images"));
                    }

                    subAisleItemBeanList.add(item);


                }
                subAisleBean.setSubAisleItemBeanList(subAisleItemBeanList);

            }
            topCategoryBean.setSubAisleBean(subAisleBean);
            sortItemsBean.setTopCategoryBean(topCategoryBean);

        }
        return sortItemsBean;
    }

    public static SimilarItemBean convertSimilarItemJsonToSimilarItemBean(JSONObject jsonObject) throws JSONException {
        SimilarItemBean similarItemBean = null;
        if (jsonObject != null) {
            similarItemBean = new SimilarItemBean();
            JSONObject cat = jsonObject.optJSONObject("data");
            if (cat != null) {
                JSONObject dataObj = jsonObject.getJSONObject("data");
                similarItemBean.setCount(dataObj.getString("count"));
                JSONArray catArray = dataObj.optJSONArray("items");
                if (catArray != null) {
                    JSONArray itemsArray = dataObj.getJSONArray("items");
                    List<SimilarItems> similarItemsList = new ArrayList<>();
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject subItemObj = itemsArray.getJSONObject(i);
                        SimilarItems item = new SimilarItems();
                        item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                        item.setItemId(subItemObj.getString("item_id"));
                        item.setPrice(subItemObj.getString("price"));
                        item.setItemName(subItemObj.getString("item_name"));
                        item.setSize(subItemObj.getString("size"));
                        item.setUom(subItemObj.getString("uom"));
                        item.setIsTaxAplicable(subItemObj.getString("is_tax_applicable"));
                        item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                        item.setInStock(subItemObj.getString("in_stock"));
                        item.setSalePrice(subItemObj.getString("sale_price"));
                        item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                        item.setShippingWeight(subItemObj.getString("shipping_weight"));
                        item.setTopCategoryId(subItemObj.getString("top_category_id"));
                        item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                        item.setFinalItemId(subItemObj.getString("final_item_id"));
                        item.setOnSale(subItemObj.getString("on_sale"));
                        item.setSale(subItemObj.getString("sale"));

                        if (subItemObj.has("is_save")) {
                            item.setIsSave(subItemObj.getString("is_save"));
                        }
                        item.setDescription(subItemObj.getString("description"));
                        if (subItemObj.has("offer")) {
                            item.setOffer(subItemObj.getString("offer"));
                        }
                        JSONObject brandCategory = subItemObj.optJSONObject("brand");
                        if (brandCategory != null) {
                            JSONObject brandObj = subItemObj.getJSONObject("brand");
                            BrandBean brandBean = new BrandBean();
                            brandBean.setBrandId(brandObj.getString("brand_id"));
                            brandBean.setBrandName(brandObj.getString("brand_name"));
                            brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                            brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                            item.setBrand(brandBean);
                        }
                        item.setNutrition(subItemObj.getString("nutrition"));
                        JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                        if (nutCat != null) {
                            JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                            NutritutionBean nutritutionBean = new NutritutionBean();
                            nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                            nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                            nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                            nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                            nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                            nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                            nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                            nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                            nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                            nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                            nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                            nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                            nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                            item.setNutritutionBean(nutritutionBean);
                        }
                        JSONObject imgObj = subItemObj.optJSONObject("images");
                        if (imgObj != null) {
                            JSONObject img = subItemObj.getJSONObject("images");
                            if (img.has("thumbnail")) {
                                item.setThumbnail(img.getString("thumbnail"));
                            }
                            JSONArray mainCat = img.optJSONArray("app");
                            if (mainCat != null) {
                                JSONArray mainArray = img.getJSONArray("app");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setImagesBeanList(imagesBeanList);
                            }
                            JSONArray mainCat1 = img.optJSONArray("main");
                            if (mainCat1 != null) {
                                JSONArray mainArray = img.getJSONArray("main");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setMainImagesBeanList(imagesBeanList);
                            }
                        } else {
                            item.setImages(subItemObj.getString("images"));
                        }

                        similarItemsList.add(item);

                    }
                    similarItemBean.setSimilarItemsList(similarItemsList);
                }
            }
        }
        return similarItemBean;
    }


    public static GlobalBean convertGlobalJsonToGlobalBean(JSONObject jsonObject) throws JSONException {
        GlobalBean globalBean = null;
        if (jsonObject != null) {
            globalBean = new GlobalBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            List<RefineBrandBean> refineBrandBeanList = null;
            JSONArray brandCat = dataObj.optJSONArray("brands");
            if (brandCat != null) {
                JSONArray brandsArray = dataObj.getJSONArray("brands");
                refineBrandBeanList = new ArrayList<>();
                for (int i = 0; i < brandsArray.length(); i++) {
                    JSONObject brandObj = brandsArray.getJSONObject(i);
                    RefineBrandBean brandBean = new RefineBrandBean();
                    brandBean.setBrandid(brandObj.getString("brand_id"));
                    brandBean.setBrandname(brandObj.getString("brand_name"));
                    brandBean.setBandLogo(brandObj.getString("brand_logo"));
                    brandBean.setBandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));

                    refineBrandBeanList.add(brandBean);
                }

                globalBean.setRefineBrandBeanList(refineBrandBeanList);
            }
            JSONArray pCat = dataObj.optJSONArray("prices");
            if (pCat != null) {
                JSONArray priceArray = dataObj.getJSONArray("prices");
                List<RefinePriceBean> refinePriceBeanList = new ArrayList<>();
                for (int i = 0; i < priceArray.length(); i++) {
                    JSONObject priceObj = priceArray.getJSONObject(i);
                    RefinePriceBean priceBean = new RefinePriceBean();
                    priceBean.setFrom(priceObj.getString("from"));
                    priceBean.setTo(priceObj.getString("to"));
                    priceBean.setLabel(priceObj.getString("label"));
                    refinePriceBeanList.add(priceBean);
                }
                globalBean.setRefinePriceBeanList(refinePriceBeanList);
            }

        }

        return globalBean;
    }

    public static List<SavedItemBean> convertSavedItemsJsonToSavedItemsBean(JSONObject jsonObject) throws JSONException {
        List<SavedItemBean> savedItemBeanList = null;
        if (jsonObject != null) {
            savedItemBeanList = new ArrayList<>();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray saveArray = dataObj.getJSONArray("saved_items");
            for (int i = 0; i < saveArray.length(); i++) {
                JSONObject saveObj = saveArray.getJSONObject(i);
                SavedItemBean savedItemBean = new SavedItemBean();
                savedItemBean.setZipcode(saveObj.getString("zipcode"));
                List<SavedSuppliersBean> savedSuppliersBeanList = new ArrayList<>();
                JSONArray supArray = saveObj.getJSONArray("suppliers");
                for (int j = 0; j < supArray.length(); j++) {
                    JSONObject supObj = supArray.getJSONObject(j);

                    SavedSuppliersBean savedSuppliersBean = new SavedSuppliersBean();
                    savedSuppliersBean.setZipcode(supObj.getString("zipcode"));
                    savedSuppliersBean.setSupplierId(supObj.getString("supplier_id"));
                    savedSuppliersBean.setSupplierName(supObj.getString("supplier_name"));
                    savedSuppliersBean.setCity(supObj.getString("city"));
                    savedSuppliersBean.setCount(supObj.getString("count"));
                    savedSuppliersBean.setTaxRate(supObj.getString("tax_rate"));

                    JSONArray itemsArray = supObj.getJSONArray("items");
                    List<SavedSupplierItemBean> savedSupplierItemBeans = new ArrayList<>();
                    for (int k = 0; k < itemsArray.length(); k++) {
                        JSONObject subItemObj = itemsArray.getJSONObject(k);
                        SavedSupplierItemBean item = new SavedSupplierItemBean();
                        item.setItemId(subItemObj.getString("item_id"));
                        item.setPrice(subItemObj.getString("price"));
                        item.setItemName(subItemObj.getString("item_name"));
                        item.setSize(subItemObj.getString("size"));
                        item.setUom(subItemObj.getString("uom"));
                        item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                        item.setShippingWeight(subItemObj.getString("shipping_weight"));
                        item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                        item.setShippingWeight(subItemObj.getString("shipping_weight"));
                        item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                        item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                        item.setTopCategoryId(subItemObj.getString("top_category_id"));
                        item.setSale(subItemObj.getString("sale"));
                        item.setInStock(subItemObj.getString("in_stock"));
                        item.setSalePrice(subItemObj.getString("sale_price"));
                        item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                        item.setOffer(subItemObj.getString("offer"));

                        item.setCount(subItemObj.getString("count"));
                        item.setFinalItemId(subItemObj.getString("final_item_id"));
                        item.setOnSale(subItemObj.getString("on_sale"));

                        JSONObject brandCategory = subItemObj.optJSONObject("brand");
                        if (brandCategory != null) {
                            JSONObject brandObj = subItemObj.getJSONObject("brand");
                            BrandBean brandBean = new BrandBean();
                            brandBean.setBrandId(brandObj.getString("brand_id"));
                            brandBean.setBrandName(brandObj.getString("brand_name"));
                            brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                            brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                            item.setBrand(brandBean);
                        }
                        item.setNutrition(subItemObj.getString("nutrition"));
                        JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                        if (nutCat != null) {
                            JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                            NutritutionBean nutritutionBean = new NutritutionBean();
                            nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                            nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                            nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                            nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                            nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                            nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                            nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                            nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                            nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                            nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                            nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                            nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                            nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                            item.setNutritutionBean(nutritutionBean);
                        }
                        JSONObject imgObj = subItemObj.optJSONObject("images");
                        if (imgObj != null) {
                            JSONObject img = subItemObj.getJSONObject("images");
                            if (img.has("thumbnail")) {
                                item.setThumbnail(img.getString("thumbnail"));
                            }
                            JSONArray mainCat = img.optJSONArray("app");
                            if (mainCat != null) {
                                JSONArray mainArray = img.getJSONArray("app");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setImagesBeanList(imagesBeanList);
                            }
                            JSONArray mainCat1 = img.optJSONArray("main");
                            if (mainCat1 != null) {
                                JSONArray mainArray = img.getJSONArray("main");
                                List<ImagesBean> imagesBeanList = new ArrayList<>();
                                for (int c = 0; c < mainArray.length(); c++) {
                                    String img1 = mainArray.getString(c);
                                    ImagesBean imagesBean = new ImagesBean();
                                    imagesBean.setImage(img1);
                                    imagesBeanList.add(imagesBean);

                                }
                                item.setMainImagesBeanList(imagesBeanList);
                            }
                        } else {
                            item.setImages(subItemObj.getString("images"));
                        }
                        savedSupplierItemBeans.add(item);
                    }
                    savedSuppliersBean.setSavedSupplierItemBeanList(savedSupplierItemBeans);
                    savedSuppliersBeanList.add(savedSuppliersBean);
                }


                savedItemBean.setSavedSuppliersBeanList(savedSuppliersBeanList);
                savedItemBeanList.add(savedItemBean);
            }

        }
        return savedItemBeanList;
    }

    public static HeartyPromotionBean convertPromotionJSonToPromotionBean(JSONObject jsonObject) throws JSONException {
        HeartyPromotionBean heartyPromotionBean = null;
        if (jsonObject != null) {
            heartyPromotionBean = new HeartyPromotionBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray promCat = dataObj.optJSONArray("promotions");
            if (promCat != null) {
                List<PromotionBean> promotionBeanList = new ArrayList<>();
                promotionBeanList = new ArrayList<>();
                JSONArray promArray = dataObj.getJSONArray("promotions");
                for (int i = 0; i < promArray.length(); i++) {
                    PromotionBean prom = new PromotionBean();
                    JSONObject promObj = promArray.getJSONObject(i);
                    prom.setPromotionId(promObj.getString("promotion_id"));
                    prom.setPromotionTitle(promObj.getString("promotion_title"));
                    prom.setPromotionDisplayText(promObj.getString("promotion_display_text"));
                    prom.setBannerBackground(promObj.getString("banner_background"));
                    prom.setPromoType(promObj.getString("promo_type"));
                    prom.setCreditAmount(promObj.getString("credit_amount"));
                    promotionBeanList.add(prom);
                }
                heartyPromotionBean.setPromotionBeanList(promotionBeanList);
            }

            JSONObject notiCat = dataObj.optJSONObject("notification");
            if (notiCat != null) {
                JSONObject notiObj = dataObj.getJSONObject("notification");
                NotificationUserProfileBean notificationUserProfileBean = new NotificationUserProfileBean();
                notificationUserProfileBean.setNotification_id(notiObj.getString("notification_id"));
                notificationUserProfileBean.setNotification_display_text(notiObj.getString("notification_display_text"));
                notificationUserProfileBean.setIs_on(notiObj.getString("is_on"));
                notificationUserProfileBean.setNotification_type(notiObj.getString("notification_type"));
                heartyPromotionBean.setNotificationUserProfileBean(notificationUserProfileBean);
            }

            JSONObject shareCat = dataObj.optJSONObject("share_promotions");
            if (shareCat != null) {
                JSONObject shareObj = dataObj.getJSONObject("share_promotions");
                SharePromotionBean sharePromotionBean = new SharePromotionBean();
                sharePromotionBean.setTitle(shareObj.getString("title"));
                sharePromotionBean.setDisplayText(shareObj.getString("display_text"));
                heartyPromotionBean.setSharePromotionBean(sharePromotionBean);

            }


        }
        return heartyPromotionBean;
    }


    public static SearchBean convertSearchJsonToSearchBean(JSONObject jsonObject) throws JSONException {
        SearchBean searchBean = null;
        if (jsonObject != null) {
            searchBean = new SearchBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");

            JSONArray optTagsArray = dataObj.optJSONArray("tags");
            if(optTagsArray != null){
                JSONArray tagsArray = dataObj.getJSONArray("tags");
                List<SearchTagBean> searchTagBeanList = new ArrayList<>();
                for (int i = 0; i < tagsArray.length(); i++) {
                    JSONObject tagObj = tagsArray.getJSONObject(i);
                    SearchTagBean tagBean = new SearchTagBean();
                    tagBean.setTag(tagObj.getString("tag"));
                    searchTagBeanList.add(tagBean);
                }
                searchBean.setSearchTagBeanList(searchTagBeanList);
            }

            JSONArray cat = dataObj.optJSONArray("aisles");
            if (cat != null) {
                List<SearchSubAisleBean> searchSubAisleBeanList = new ArrayList<>();
                JSONArray aisleArray = dataObj.getJSONArray("aisles");
                for (int i = 0; i < aisleArray.length(); i++) {
                    SearchSubAisleBean subAisleBean = new SearchSubAisleBean();
                    JSONObject obj = aisleArray.getJSONObject(i);
                    subAisleBean.setTopCategoryId(obj.getString("top_category_id"));
                    subAisleBean.setSubCategoryId(obj.getString("sub_category_id"));
                    subAisleBean.setSubCategoryName(obj.getString("sub_category_name"));
                    searchSubAisleBeanList.add(subAisleBean);
                }
                searchBean.setSearchSubAisleBeanList(searchSubAisleBeanList);
            }
        }
        return searchBean;
    }


    public static DeliveryWindowBean    convertDeliveryJSonToDeliveryBean(JSONObject jsonObject) throws JSONException {
        DeliveryWindowBean deliveryWindowBean = null;
        if (jsonObject != null) {
            deliveryWindowBean = new DeliveryWindowBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONObject singObj = dataObj.getJSONObject("single");
            SingleDeliveryBean singleDeliveryBean = new SingleDeliveryBean();
            singleDeliveryBean.setTotalDeliveryEstimatedFee(singObj.getString("total_delivery_estimated_fee"));
            singleDeliveryBean.setTotalDeliveryEstimatedFeeBeforeSix(singObj.getString("total_delivery_estimated_fee_before_six"));
            singleDeliveryBean.setTotalDeliveryEstimatedFeeAfterSix(singObj.getString("total_delivery_estimated_fee_after_six"));
            JSONArray workArray = singObj.getJSONArray("working_days");
            List<WorkingDaysBean> workingDaysBeanList = new ArrayList<>();
            for (int i = 0; i < workArray.length(); i++) {
                JSONObject workObj = workArray.getJSONObject(i);
                WorkingDaysBean workingDaysBean = new WorkingDaysBean();
                workingDaysBean.setSupplierId(workObj.getString("supplier_id"));
                workingDaysBean.setDeliveryEstimatedFee(workObj.getString("delivery_estimated_fee"));
                workingDaysBean.setDeliveryEstimatedChargeBeforeSix(workObj.getString("delivery_estimated_charge_before_six"));
                workingDaysBean.setDeliveryEstimatedChargeAfterSix(workObj.getString("delivery_estimated_charge_after_six"));
                JSONObject estObj = workObj.getJSONObject("estimated_delivery_fee_description");
                workingDaysBean.setDeliveryMiles(estObj.getString("delivery_miles"));
                workingDaysBean.setDeliveryMinutes(estObj.getString("delivery_minutes"));
                workingDaysBean.setDeliveryMilesCharged(estObj.getString("delivery_miles_charged"));
                workingDaysBean.setDeliveryMinuteCharged(estObj.getString("delivery_minute_charged"));
                workingDaysBean.setDeliveryCongestionFee(estObj.getString("delivery_congestion_fee"));
                workingDaysBean.setDeliveryCharge(estObj.getString("delivery_charge"));
                workingDaysBean.setDeliveryWeightSurcharge(estObj.getString("delivery_weight_surcharge"));
                workingDaysBean.setIntEstCost(estObj.getString("int_est_cost"));
                workingDaysBean.setDeliveryCongestionCost(estObj.getString("delivery_congestion_cost"));
                workingDaysBean.setDeliveryCongestionCostBeforeSix(estObj.getString("delivery_congestion_cost_before"));
                workingDaysBean.setDeliveryCongestionCostAfterSix(estObj.getString("delivery_congestion_cost_after"));
                JSONArray opertArray = workObj.getJSONArray("operating_hours");
                List<OperatingHourBean> operatingHourBeanList = new ArrayList<>();
                for (int j = 0; j < opertArray.length(); j++) {
                    JSONObject optObj = opertArray.getJSONObject(j);
                    OperatingHourBean operatingHourBean = new OperatingHourBean();
                    operatingHourBean.setDate(optObj.getString("date"));
                    operatingHourBean.setIsClose(optObj.getString("is_close"));
                    operatingHourBean.setDayOfWeek(optObj.getString("dayofweek"));
                    JSONArray cat = optObj.optJSONArray("interval");
                    if (cat != null) {
                        JSONArray intervalArray = optObj.getJSONArray("interval");
                        List<TimeIntervalBean> timeIntervalBeanList = new ArrayList<>();
                        for (int k = 0; k < intervalArray.length(); k++) {
                            JSONObject obj = intervalArray.getJSONObject(k);
                            TimeIntervalBean timeIntervalBean = new TimeIntervalBean();
                            timeIntervalBean.setStartTime(obj.getString("start_time"));
                            timeIntervalBean.setEndTime(obj.getString("end_time"));
                            timeIntervalBean.setStartTimeLabel(obj.getString("start_time_label"));
                            timeIntervalBean.setEndTimeLabel(obj.getString("end_time_label"));
                            timeIntervalBeanList.add(timeIntervalBean);
                        }
                        operatingHourBean.setTimeIntervalBeanList(timeIntervalBeanList);
                    }
                    operatingHourBeanList.add(operatingHourBean);

                }
                workingDaysBean.setOperatingHourBeanList(operatingHourBeanList);
                workingDaysBeanList.add(workingDaysBean);
            }
            singleDeliveryBean.setWorkingDaysBeanList(workingDaysBeanList);
            deliveryWindowBean.setSingleDeliveryBean(singleDeliveryBean);


            JSONObject combineCat = dataObj.optJSONObject("combine");
            if (combineCat != null) {
                JSONObject combineObj = dataObj.getJSONObject("combine");
                CombineDeliveryBean combineDeliveryBean = new CombineDeliveryBean();
                combineDeliveryBean.setTotalDeliveryEstimatedFee(combineObj.getString("total_delivery_estimated_fee"));
                combineDeliveryBean.setTotalDeliveryEstimatedFeeBeforeSix(combineObj.getString("total_delivery_estimated_fee_before_six"));
                combineDeliveryBean.setTotalDeliveryEstimatedFeeAfterSix(combineObj.getString("total_delivery_estimated_fee_after_six"));
                JSONArray workArray1 = combineObj.getJSONArray("working_days");
                List<WorkingDaysBean> workingDaysBeanList1 = new ArrayList<>();
                for (int i = 0; i < workArray1.length(); i++) {
                    JSONObject workObj = workArray1.getJSONObject(i);
                    WorkingDaysBean workingDaysBean = new WorkingDaysBean();
                    workingDaysBean.setSupplierId(workObj.getString("supplier_id"));
                    workingDaysBean.setDeliveryEstimatedFee(workObj.getString("delivery_estimated_fee"));
                    workingDaysBean.setDeliveryEstimatedChargeBeforeSix(workObj.getString("delivery_estimated_charge_before_six"));
                    workingDaysBean.setDeliveryEstimatedChargeAfterSix(workObj.getString("delivery_estimated_charge_after_six"));
                    JSONObject estObj = workObj.getJSONObject("estimated_delivery_fee_description");
                    workingDaysBean.setDeliveryMiles(estObj.getString("delivery_miles"));
                    workingDaysBean.setDeliveryMinutes(estObj.getString("delivery_minutes"));
                    workingDaysBean.setDeliveryMilesCharged(estObj.getString("delivery_miles_charged"));
                    workingDaysBean.setDeliveryMinuteCharged(estObj.getString("delivery_minute_charged"));
                    workingDaysBean.setDeliveryCongestionFee(estObj.getString("delivery_congestion_fee"));
                    workingDaysBean.setDeliveryCharge(estObj.getString("delivery_charge"));
                    workingDaysBean.setDeliveryWeightSurcharge(estObj.getString("delivery_weight_surcharge"));
                    workingDaysBean.setIntEstCost(estObj.getString("int_est_cost"));
                    workingDaysBean.setDeliveryCongestionCost(estObj.getString("delivery_congestion_cost"));
                    workingDaysBean.setDeliveryCongestionCostBeforeSix(estObj.getString("delivery_congestion_cost_before"));
                    workingDaysBean.setDeliveryCongestionCostAfterSix(estObj.getString("delivery_congestion_cost_after"));
                    JSONArray opertArray = workObj.getJSONArray("operating_hours");
                    List<OperatingHourBean> operatingHourBeanList = new ArrayList<>();
                    for (int j = 0; j < opertArray.length(); j++) {
                        JSONObject optObj = opertArray.getJSONObject(j);
                        OperatingHourBean operatingHourBean = new OperatingHourBean();
                        operatingHourBean.setDate(optObj.getString("date"));
                        operatingHourBean.setIsClose(optObj.getString("is_close"));
                        operatingHourBean.setDayOfWeek(optObj.getString("dayofweek"));
                        JSONArray cat = optObj.optJSONArray("interval");
                        if (cat != null) {
                            JSONArray intervalArray = optObj.getJSONArray("interval");
                            List<TimeIntervalBean> timeIntervalBeanList = new ArrayList<>();
                            for (int k = 0; k < intervalArray.length(); k++) {
                                JSONObject obj = intervalArray.getJSONObject(k);
                                TimeIntervalBean timeIntervalBean = new TimeIntervalBean();
                                timeIntervalBean.setStartTime(obj.getString("start_time"));
                                timeIntervalBean.setEndTime(obj.getString("end_time"));
                                timeIntervalBean.setStartTimeLabel(obj.getString("start_time_label"));
                                timeIntervalBean.setEndTimeLabel(obj.getString("end_time_label"));
                                timeIntervalBeanList.add(timeIntervalBean);
                            }
                            operatingHourBean.setTimeIntervalBeanList(timeIntervalBeanList);
                        }
                        operatingHourBeanList.add(operatingHourBean);

                    }
                    workingDaysBean.setOperatingHourBeanList(operatingHourBeanList);
                    workingDaysBeanList1.add(workingDaysBean);
                }
                combineDeliveryBean.setWorkingDaysBeanList(workingDaysBeanList1);
                deliveryWindowBean.setCombineDeliveryBean(combineDeliveryBean);
            }
        }

        return deliveryWindowBean;
    }

    public static CurrentPastOrdersBean convertOrderJsonToCurrentPastOrdersBean(JSONObject jsonObject) throws JSONException {
        CurrentPastOrdersBean currentPastOrdersBean = null;
        if (jsonObject != null) {
            currentPastOrdersBean = new CurrentPastOrdersBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
//Current Orders
            JSONArray catCurrent = dataObj.optJSONArray("current_orders");
            if (catCurrent != null) {
                JSONArray currentOrdersArray = dataObj.getJSONArray("current_orders");
                List<CurrentOrderBean> currentOrderBeansList = new ArrayList<>();
                for (int i = 0; i < currentOrdersArray.length(); i++) {
                    JSONObject currentOrderObj = currentOrdersArray.getJSONObject(i);
                    CurrentOrderBean currentOrderBean = new CurrentOrderBean();
                    currentOrderBean.setOrderId(currentOrderObj.getString("order_id"));
                    currentOrderBean.setDisplayOrderId(currentOrderObj.getString("display_order_id"));
                    currentOrderBean.setItemCount(currentOrderObj.getString("item_count"));
                    currentOrderBean.setAmount(currentOrderObj.getString("amount"));
                    //Current Order Suppliers
                    JSONArray currentSupplierArray = currentOrderObj.getJSONArray("suppliers");
                    List<CurrentOrderSupplierBean> currentOrderSupplierBeansList = new ArrayList<>();
                    for (int j = 0; j < currentSupplierArray.length(); j++) {
                        JSONObject currentOrderSupplierObj = currentSupplierArray.getJSONObject(j);
                        CurrentOrderSupplierBean currentOrderSupplierBean = new CurrentOrderSupplierBean();
                        currentOrderSupplierBean.setSupplierId(currentOrderSupplierObj.getString("supplier_id"));
                        currentOrderSupplierBean.setSupplierName(currentOrderSupplierObj.getString("supplier_name"));
                        currentOrderSupplierBean.setSupplierCity(currentOrderSupplierObj.getString("supplier_id"));
                        currentOrderSupplierBean.setOrderSupplierId(currentOrderSupplierObj.getString("order_supplier_id"));
                        currentOrderSupplierBean.setSupplierRating(currentOrderSupplierObj.getString("rating"));
                        currentOrderSupplierBean.setDeliveryDate(currentOrderSupplierObj.getString("delivery_date"));
                        currentOrderSupplierBean.setDeliveryTime(currentOrderSupplierObj.getString("delivery_time"));
                        currentOrderSupplierBean.setDeliveryStatus(currentOrderSupplierObj.getString("delivery_status"));
                        currentOrderSupplierBean.setDeliveryStatusDisplay(currentOrderSupplierObj.getString("delivery_status_display"));
                        currentOrderSupplierBeansList.add(currentOrderSupplierBean);
                    }
                    currentOrderBean.setRating(currentOrderObj.getString("rating"));
                    currentOrderBean.setCurrentOrderSupplierBeans(currentOrderSupplierBeansList);
                    currentOrderBeansList.add(currentOrderBean);
                    currentPastOrdersBean.setCurrentOrderBeansList(currentOrderBeansList);
                }
            }
//Past Orders
            JSONArray pastCat = dataObj.optJSONArray("past_orders");
            if (pastCat != null) {
                JSONArray pastOrdersArray = dataObj.getJSONArray("past_orders");
                List<PastOrderBean> pastOrderBeansList = new ArrayList<>();
                for (int k = 0; k < pastOrdersArray.length(); k++) {
                    JSONObject pastOrderObj = pastOrdersArray.getJSONObject(k);
                    PastOrderBean pastOrderBean = new PastOrderBean();
                    pastOrderBean.setOrderId(pastOrderObj.getString("order_id"));
                    pastOrderBean.setDisplayOrderId(pastOrderObj.getString("display_order_id"));
                    pastOrderBean.setItemCount(pastOrderObj.getString("item_count"));
                    pastOrderBean.setAmount(pastOrderObj.getString("amount"));
                    pastOrderBean.setRating(pastOrderObj.getString("rating"));
                    //Past Order Suppliers
                    JSONArray pastSupplierArray = pastOrderObj.getJSONArray("suppliers");
                    List<PastOrderSupplierBean> pastOrderSupplierBeansList = new ArrayList<>();
                    for (int j = 0; j < pastSupplierArray.length(); j++) {
                        JSONObject pastSupplierObj = pastSupplierArray.getJSONObject(j);
                        PastOrderSupplierBean pastOrderSupplierBean = new PastOrderSupplierBean();
                        pastOrderSupplierBean.setSupplierId(pastSupplierObj.getString("supplier_id"));
                        pastOrderSupplierBean.setSupplierName(pastSupplierObj.getString("supplier_name"));
                        pastOrderSupplierBean.setSupplierCity(pastSupplierObj.getString("city"));
                        pastOrderSupplierBean.setOrderSupplierId(pastSupplierObj.getString("order_supplier_id"));
                        pastOrderSupplierBean.setSupplierRating(pastSupplierObj.getString("rating"));
                        pastOrderSupplierBean.setDeliveryDate(pastSupplierObj.getString("delivery_date"));
                        pastOrderSupplierBean.setDeliveryTime(pastSupplierObj.getString("delivery_time"));
                        pastOrderSupplierBean.setDeliveryStatus(pastSupplierObj.getString("delivery_status"));
                        pastOrderSupplierBeansList.add(pastOrderSupplierBean);
                    }
                    pastOrderBean.setRating(pastOrderObj.getString("rating"));
                    pastOrderBean.setPastOrderSupplierBeans(pastOrderSupplierBeansList);
                    pastOrderBeansList.add(pastOrderBean);
                    currentPastOrdersBean.setPastOrderBeansList(pastOrderBeansList);
                }
            }
        }
        return currentPastOrdersBean;
    }

    public static OrderDetailBean convertOrderDetailJsonToOrderDetailBean(JSONObject jsonObject) throws JSONException {
        OrderDetailBean orderDetailBean = null;
        if (jsonObject != null) {
            orderDetailBean = new OrderDetailBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            orderDetailBean.setOrderId(dataObj.getString("order_id"));
            orderDetailBean.setDisplayOrderId(dataObj.getString("display_order_id"));
            orderDetailBean.setDeliveryInstructions(dataObj.getString("delivery_instructions"));
            orderDetailBean.setDeliveryPhoneSupplied(dataObj.getString("delivery_phone_supplied"));
            orderDetailBean.setDeliveryTo(dataObj.getString("deliver_to"));
            orderDetailBean.setDeliveryCharge(dataObj.getString("delivery_charge"));
            orderDetailBean.setCredits(dataObj.getString("credits"));
            orderDetailBean.setTipAmount(dataObj.getString("tip_amount"));
            orderDetailBean.setTipPercentage(dataObj.getString("tip_percentage"));
            orderDetailBean.setSubTotal(dataObj.getString("subtotal"));
            orderDetailBean.setTax(dataObj.getString("tax"));
            orderDetailBean.setTotalAmount(dataObj.getString("total_amount"));

            LocationBean locationBean = null;
            JSONObject addObj = dataObj.getJSONObject("address");
            if (addObj != null) {
                locationBean = new LocationBean();
                locationBean.setAddress1(addObj.getString("address1"));
                locationBean.setAddress2(addObj.getString("address2"));
                locationBean.setAptSuitUnit(addObj.getString("apt_suite_unit"));
                locationBean.setCity(addObj.getString("city"));
                locationBean.setState(addObj.getString("state"));
                locationBean.setZipcode(addObj.getString("zipcode"));

                orderDetailBean.setLocationBean(locationBean);
            }

            PaymentCardBean paymentCardBean = null;
            JSONObject paymentObj = dataObj.getJSONObject("payment");
            if (paymentObj != null) {
                paymentCardBean = new PaymentCardBean();
                paymentCardBean.setCardType(paymentObj.getString("card_type"));
                paymentCardBean.setCardIssuer(paymentObj.getString("card_issuer"));
                paymentCardBean.setCardCvvNumber(paymentObj.getString("cc_number"));
                paymentCardBean.setCardExpiratoinDate(paymentObj.getString("expiration_date"));
                paymentCardBean.setCardLastFourDigit(paymentObj.getString("last_4_digits"));
                paymentCardBean.setCardGivenName(paymentObj.getString("given_card_name"));
                orderDetailBean.setPaymentCardBean(paymentCardBean);
            }

            JSONObject credCat = dataObj.optJSONObject("credits");
            if (credCat != null) {
                JSONObject credObj = dataObj.getJSONObject("credits");
                orderDetailBean.setCredits(credObj.getString("credit_amount"));
            }

            OrderDetailPromoBean orderDetailPromoBean = null;
            JSONObject cat = dataObj.optJSONObject("promo_code");
            if (cat != null) {
                JSONObject promoObj = dataObj.getJSONObject("promo_code");
                if (promoObj != null) {
                    orderDetailPromoBean = new OrderDetailPromoBean();
                    orderDetailPromoBean.setPromoCode(promoObj.getString("promo_code"));
                    orderDetailPromoBean.setPromoAmount(promoObj.getString("promo_amount"));
                    orderDetailBean.setOrderDetailPromoBean(orderDetailPromoBean);
                }
            }

            JSONArray supplierArray = dataObj.getJSONArray("suppliers");
            List<SuppliersBean> orderDetailSuppliersBeanList = new ArrayList<>();
            for (int i = 0; i < supplierArray.length(); i++) {
                JSONObject supplierObj = supplierArray.getJSONObject(i);
                SuppliersBean suppliersBean = new SuppliersBean();
                suppliersBean.setSupplierId(supplierObj.getString("supplier_id"));
                suppliersBean.setSupplierName(supplierObj.getString("supplier_name"));
                suppliersBean.setOrderSupplierId(supplierObj.getString("order_supplier_id"));
                suppliersBean.setSupplierTotal(supplierObj.getString("supplier_total"));
                suppliersBean.setDeliveryDate(supplierObj.getString("delivery_date"));
                suppliersBean.setDeliveryTime(supplierObj.getString("delivery_time"));
                suppliersBean.setDeliveryStatus(supplierObj.getString("delivery_status"));
                suppliersBean.setDeliveryStatusDisplay(supplierObj.getString("delivery_status_display"));
                suppliersBean.setIsOrderCancelable(supplierObj.getString("is_order_cancellable"));

                JSONArray orderItemsArray = supplierObj.getJSONArray("order_items");
                List<OrderBean> orderDetailOrdersBeanList = new ArrayList<>();
                for (int j = 0; j < orderItemsArray.length(); j++) {
                    JSONObject ordersObj = orderItemsArray.getJSONObject(j);
                    OrderBean orderBean = new OrderBean();
                    orderBean.setIsTaxable(ordersObj.getString("is_tax_applicable"));
                    orderBean.setIsSubTaxApplicable(ordersObj.getString("sub_is_tax_applicable"));
                    orderBean.setShippingWeight(ordersObj.getString("shipping_weight"));
                    orderBean.setSupplierItemId(ordersObj.getString("supplier_item_id"));
                    orderBean.setItemName(ordersObj.getString("item_name"));
                    orderBean.setSize(ordersObj.getString("size"));
                    orderBean.setUom(ordersObj.getString("uom"));
                    orderBean.setMaxQuantity(ordersObj.getString("max_quantity_per_order"));
                    orderBean.setTaxRate(ordersObj.getString("tax_rate"));
                    orderBean.setBrandName(ordersObj.getString("brand_name"));
                    orderBean.setOrderQuantity(ordersObj.getString("order_quantity"));
                    orderBean.setOutOfStock(ordersObj.getString("out_of_stock"));
                    orderBean.setRegularPrice(ordersObj.getString("regular_price"));
                    orderBean.setFinalPrice(ordersObj.getString("final_price"));
                    orderBean.setTotalPrice(ordersObj.getString("total"));
                    orderBean.setSubstitution(ordersObj.getString("substitution"));
                    orderBean.setSubstitutionWith(ordersObj.getString("substitution_with"));
                    if (ordersObj.has("supplied_qty")) {
                        orderBean.setSuppliedQuantity(ordersObj.getString("supplied_qty"));
                    }
                    if (ordersObj.has("is_ordered_supplied_quantity_same")) {
                        orderBean.setIsOrderedSuppliedQuantitySame(ordersObj.getString("is_ordered_supplied_quantity_same"));
                    }
                    if (ordersObj.has("free_item_qty")) {
                        orderBean.setFreeItemQuantity(ordersObj.getString("free_item_qty"));
                    }
                    if (ordersObj.has("is_substituted")) {
                        orderBean.setIsSubstituted(ordersObj.getString("is_substituted"));
                    }
                    orderBean.setIsSave(ordersObj.getString("is_save"));
                    orderBean.setApplicableSaleDescription(ordersObj.getString("applicable_sale_description"));
                    JSONObject imgObj = ordersObj.optJSONObject("images");
                    if (imgObj != null) {
                        JSONObject img = ordersObj.getJSONObject("images");
                        if (img.has("thumbnail")) {
                            orderBean.setThumbnail(img.getString("thumbnail"));
                        }
                    }
                    JSONObject cat1 = ordersObj.optJSONObject("substitution_with");
                    if (cat1 != null) {
                        JSONObject subObj = ordersObj.getJSONObject("substitution_with");
                        OrderSubstitutionBean substitutionBean = new OrderSubstitutionBean();
                        substitutionBean.setSupplierItemId(subObj.getString("supplier_item_id"));
                        substitutionBean.setItemName(subObj.getString("item_name"));
                        substitutionBean.setSize(subObj.getString("size"));
                        substitutionBean.setUom(subObj.getString("uom"));
                        substitutionBean.setBrandName(subObj.getString("brand_name"));
                        substitutionBean.setOrderQuantity(subObj.getString("order_quantity"));
                        substitutionBean.setSuppliedQuantity(subObj.getString("supplied_qty"));
                        substitutionBean.setRegularPrice(subObj.getString("regular_price"));
                        substitutionBean.setFinalPrice(subObj.getString("final_price"));
                        substitutionBean.setTotal(subObj.getString("total"));
                        substitutionBean.setImages(subObj.getString("images"));
                        orderBean.setOrderSubstitutionBean(substitutionBean);
                    }
                    orderDetailOrdersBeanList.add(orderBean);
                }
                suppliersBean.setDetailOrdersBeanList(orderDetailOrdersBeanList);
                orderDetailSuppliersBeanList.add(suppliersBean);
            }
            orderDetailBean.setSuppliersBeanList(orderDetailSuppliersBeanList);
        }
        return orderDetailBean;
    }

    public static List<ReturnPolicyBean> convertReturnPolicyJsonToReturnPoliyBean(JSONObject jsonObject) throws JSONException {
        List<ReturnPolicyBean> returnPolicyBeanList = null;
        if (jsonObject != null) {
            returnPolicyBeanList = new ArrayList<>();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray returnPolicyArray = dataObj.getJSONArray("return_policies");
            for (int i = 0; i < returnPolicyArray.length(); i++) {
                JSONObject obj = returnPolicyArray.getJSONObject(i);
                ReturnPolicyBean policy = new ReturnPolicyBean();
                policy.setSupplierId(obj.getString("supplier_id"));
                policy.setSupplierName(obj.getString("supplier_name"));
                policy.setCity(obj.getString("city"));
                policy.setReturnPolicy(obj.getString("return_policy"));
                returnPolicyBeanList.add(policy);
            }

        }

        return returnPolicyBeanList;
    }

    public static TrackOrderBean convertTrackOrderJsonToTrackOrderBean(JSONObject jsonObject) throws JSONException {
        TrackOrderBean trackOrderBean = null;
        if (jsonObject != null) {
            trackOrderBean = new TrackOrderBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray cat = dataObj.optJSONArray("orders");
            if (cat != null) {
                JSONArray ordersArray = dataObj.getJSONArray("orders");
                List<OrderTrackBean> orderTrackBeanList = new ArrayList<>();
                for (int i = 0; i < ordersArray.length(); i++) {
                    JSONObject ordersObj = ordersArray.getJSONObject(i);
                    OrderTrackBean orderTrackBean = new OrderTrackBean();
                    orderTrackBean.setOrderStatusId(ordersObj.getString("order_status_id"));
                    orderTrackBean.setSupplierName(ordersObj.getString("supplier_name"));
                    orderTrackBean.setOrderSupplierId(ordersObj.getString("order_supplier_id"));
                    orderTrackBean.setBagsUsed(ordersObj.getString("num_bags_used"));

                    JSONObject orderStatusObj = ordersObj.getJSONObject("status");
                    orderTrackBean.setTitle(orderStatusObj.getString("title"));
                    orderTrackBean.setMessage(orderStatusObj.getString("message"));
                    orderTrackBean.setStatus(orderStatusObj.getString("status"));
                    orderTrackBean.setTime(orderStatusObj.getString("time"));
                    orderTrackBean.setDeliveryBoy(orderStatusObj.getString("delivery_boy"));
                    orderTrackBean.setDeliveryDate(ordersObj.getString("delivery_date"));
                    orderTrackBean.setDeliveryTime(ordersObj.getString("delivery_time"));
                    orderTrackBeanList.add(orderTrackBean);
                }
                trackOrderBean.setOrderTrackBeansList(orderTrackBeanList);
            }
            JSONObject notiCat = dataObj.optJSONObject("notification");
            if (notiCat != null) {
                JSONObject notificationObj = dataObj.getJSONObject("notification");
                trackOrderBean.setNotificationId(notificationObj.getString("notification_id"));
                trackOrderBean.setIsOn(notificationObj.getString("is_on"));
            }
        }
        return trackOrderBean;
    }

    public static SearchDetailBean convertSearchDetailJsonToSearchDetailBean(JSONObject jsonObject) throws JSONException {
        SearchDetailBean searchDetailBean = null;
        if (jsonObject != null) {
            searchDetailBean = new SearchDetailBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray proCat = dataObj.optJSONArray("products");
            List<SearchProductsBean> searchProductsBeanList = null;
            if (proCat != null) {
                JSONArray prodArray = dataObj.getJSONArray("products");
                searchProductsBeanList = new ArrayList<>();
                for (int i = 0; i < prodArray.length(); i++) {
                    JSONObject prodObj = prodArray.getJSONObject(i);
                    SearchProductsBean searchProductsBean = new SearchProductsBean();
                    searchProductsBean.setTopCategoryId(prodObj.getString("top_category_id"));
                    searchProductsBean.setSubCategoryId(prodObj.getString("sub_category_id"));
                    searchProductsBean.setSubCategoryName(prodObj.getString("sub_category_name"));
                    searchProductsBean.setIsTaxApplicable(prodObj.getString("is_tax_applicable"));

                    JSONArray subCat = prodObj.optJSONArray("sub_category_items");
                    List<SubAisleItemBean> subAisleItemBeanList = null;
                    if (subCat != null) {
                        subAisleItemBeanList = new ArrayList<>();
                        JSONArray subIemsArray = prodObj.getJSONArray("sub_category_items");
                        for (int k = 0; k < subIemsArray.length(); k++) {
                            JSONObject subItemObj = subIemsArray.getJSONObject(k);
                            SubAisleItemBean item = new SubAisleItemBean();
                            item.setSale(subItemObj.getString("sale"));
                            item.setItemId(subItemObj.getString("item_id"));
                            item.setPrice(subItemObj.getString("price"));
                            item.setItemName(subItemObj.getString("item_name"));
                            item.setSize(subItemObj.getString("size"));
                            item.setUom(subItemObj.getString("uom"));
                            item.setIsTaxApplicable(subItemObj.getString("is_tax_applicable"));
                            item.setSubIsTaxApplicable(subItemObj.getString("sub_is_tax_applicable"));
                            item.setInStock(subItemObj.getString("in_stock"));
                            item.setSalePrice(subItemObj.getString("sale_price"));
                            item.setSubCategoryId(subItemObj.getString("sub_category_id"));
                            item.setTopCategoryId(subItemObj.getString("top_category_id"));
                            item.setShippingWeight(subItemObj.getString("shipping_weight"));
                            item.setCount(subItemObj.getString("count"));
                            item.setFinalItemId(subItemObj.getString("final_item_id"));
                            item.setMaxQuantity(subItemObj.getString("max_quantity_per_order"));
                            item.setOnSale(subItemObj.getString("on_sale"));
                            if (subItemObj.has("is_save")) {
                                item.setIsSave(subItemObj.getString("is_save"));
                            }
                            item.setSupplierItemId(subItemObj.getString("supplier_item_id"));
                            item.setDescription(subItemObj.getString("description"));
                            if (subItemObj.has("offer")) {
                                item.setOffer(subItemObj.getString("offer"));
                            }
                            JSONObject brandCategory = subItemObj.optJSONObject("brand");
                            if (brandCategory != null) {
                                JSONObject brandObj = subItemObj.getJSONObject("brand");
                                BrandBean brandBean = new BrandBean();
                                brandBean.setBrandId(brandObj.getString("brand_id"));
                                brandBean.setBrandName(brandObj.getString("brand_name"));
                                brandBean.setBrandLogo(brandObj.getString("brand_logo"));
                                brandBean.setBrandLogoThumbnail(brandObj.getString("brand_logo_thumbnail"));
                                item.setBrand(brandBean);
                            }
                            item.setNutrition(subItemObj.getString("nutrition"));
                            JSONArray nutCat = subItemObj.optJSONArray("nutrition");
                            if (nutCat != null) {
                                JSONObject nutObj = subItemObj.getJSONArray("nutrition").getJSONObject(0);
                                NutritutionBean nutritutionBean = new NutritutionBean();
                                nutritutionBean.setNutritionId(nutObj.getString("item_nutrition_id"));
                                nutritutionBean.setNutritionCalories(nutObj.getString("nutrition_calories"));
                                nutritutionBean.setNutritionCholestrol(nutObj.getString("nutrition_cholestrol"));
                                nutritutionBean.setNutritionDietaryFiber(nutObj.getString("nutrition_dietary_fiber"));
                                nutritutionBean.setNutritionMonoUnsatFat(nutObj.getString("nutrition_mono_unsat_fat"));
                                nutritutionBean.setNutritionPolyUnsatFat(nutObj.getString("nutrition_poly_unsat_fat"));
                                nutritutionBean.setNutritionProtein(nutObj.getString("nutrition_protein"));
                                nutritutionBean.setNutritionSaturatedFat(nutObj.getString("nutrition_saturated_fat"));
                                nutritutionBean.setNutritionSodium(nutObj.getString("nutrition_sodium"));
                                nutritutionBean.setNutritionSugar(nutObj.getString("nutrition_sugar"));
                                nutritutionBean.setNutritionTotalCarbs(nutObj.getString("nutrition_total_carbs"));
                                nutritutionBean.setNutritionTotalFat(nutObj.getString("nutrition_total_fat"));
                                nutritutionBean.setNutritionTransFat(nutObj.getString("nutrition_trans_fat"));
                                item.setNutritutionBean(nutritutionBean);
                            }
                            JSONObject imgObj = subItemObj.optJSONObject("images");
                            if (imgObj != null) {
                                JSONObject img = subItemObj.getJSONObject("images");
                                if (img.has("thumbnail")) {
                                    item.setThumbnail(img.getString("thumbnail"));
                                }
                                JSONArray mainCat = img.optJSONArray("app");
                                if (mainCat != null) {
                                    JSONArray mainArray = img.getJSONArray("app");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setImagesBeanList(imagesBeanList);
                                }
                                JSONArray mainCat1 = img.optJSONArray("main");
                                if (mainCat1 != null) {
                                    JSONArray mainArray = img.getJSONArray("main");
                                    List<ImagesBean> imagesBeanList = new ArrayList<>();
                                    for (int c = 0; c < mainArray.length(); c++) {
                                        String img1 = mainArray.getString(c);
                                        ImagesBean imagesBean = new ImagesBean();
                                        imagesBean.setImage(img1);
                                        imagesBeanList.add(imagesBean);

                                    }
                                    item.setMainImagesBeanList(imagesBeanList);
                                }
                            } else {
                                item.setImages(subItemObj.getString("images"));
                            }

                            subAisleItemBeanList.add(item);


                        }
                    }

                    searchProductsBean.setSubAisleItemBeans(subAisleItemBeanList);
                    searchProductsBeanList.add(searchProductsBean);


                }
                searchDetailBean.setSearchProductsBeanList(searchProductsBeanList);
            }

        }
        return searchDetailBean;
    }

    public static List<CheckDeliveryBean> convertCheckDeliveryJsonToCheckDeliveryBean(JSONObject jsonObject) throws JSONException {
        List<CheckDeliveryBean> checkDeliveryBeanList = null;
        if (jsonObject != null) {
            checkDeliveryBeanList = new ArrayList<>();
            JSONArray cat = jsonObject.optJSONArray("data");
            if (cat != null) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    CheckDeliveryBean bean = new CheckDeliveryBean();
                    bean.setSupplierId(obj.getString("supplier_id"));
                    bean.setMessage(obj.getString("message"));
                    checkDeliveryBeanList.add(bean);
                }

            }
        }
        return checkDeliveryBeanList;
    }


    public static DeliveryEstimateBean convertDeliveryEstimateJsonToDeliveryEstimateBean(JSONObject jsonObject) throws JSONException {
        DeliveryEstimateBean deliveryEstimateBean = null;
        EstimatedCostDescriptionBean estimatedCostDescriptionBean = null;
        if (jsonObject != null) {
            JSONObject dataObj = jsonObject.getJSONObject("data");
            deliveryEstimateBean = new DeliveryEstimateBean();
            deliveryEstimateBean.setEstimatedDeliveryFee(dataObj.getString("initial_estimated_delivery_cost"));

            JSONObject cat = dataObj.optJSONObject("estimated_cost_break_down");
            if (cat != null) {
                JSONObject obj = dataObj.getJSONObject("estimated_cost_break_down");
                EstimatedCostBreakDownBean estimatedCostBreakDownBean = new EstimatedCostBreakDownBean();
                estimatedCostBreakDownBean.setLabourCost(obj.getString("labour_cost"));
                estimatedCostBreakDownBean.setFuelCost(obj.getString("fuel_cost"));
                estimatedCostBreakDownBean.setConvenience(obj.getString("convenience"));
                estimatedCostBreakDownBean.setDeliveryEstimateCost(obj.getString("delivery_est_cost"));
                estimatedCostBreakDownBean.setEstimateDistance(obj.getString("estimate_distance"));
                estimatedCostBreakDownBean.setEstimateTime(obj.getString("estimate_time"));
                estimatedCostBreakDownBean.setFrom(obj.getString("from"));
                estimatedCostBreakDownBean.setTo(obj.getString("to"));
                estimatedCostBreakDownBean.setAddressFrom(obj.getString("address_from"));
                estimatedCostBreakDownBean.setAddressTo(obj.getString("address_to"));
                LatLongBean latLongBean = null;

                JSONArray pathCat = obj.optJSONArray("display_path_locations");
                if (pathCat != null) {
                    latLongBean = new LatLongBean();
                    JSONArray pathArray = obj.getJSONArray("display_path_locations");
                    JSONObject sObj = pathArray.getJSONObject(0);
                    JSONObject dObj = pathArray.getJSONObject(pathArray.length() - 1);

                    latLongBean.setSourceLatitude(sObj.getJSONObject("latLng").getString("lat"));
                    latLongBean.setSourceLongitude(sObj.getJSONObject("latLng").getString("lng"));
                    latLongBean.setDestinationLatitude(dObj.getJSONObject("latLng").getString("lat"));
                    latLongBean.setDestinationLongitude(dObj.getJSONObject("latLng").getString("lng"));
                    estimatedCostBreakDownBean.setLatLongBean(latLongBean);

                }
                deliveryEstimateBean.setEstimatedCostBreakDownBean(estimatedCostBreakDownBean);
            }

        }
        return deliveryEstimateBean;
    }


    public static OrderRecieptBean convertOrderRecieptJsonToOrderRecieptBean(JSONObject jsonObject) throws JSONException {
        OrderRecieptBean orderRecieptBean = null;
        if (jsonObject != null) {
            orderRecieptBean = new OrderRecieptBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            orderRecieptBean.setDisplayOrderId(dataObj.getString("display_order_id"));
            orderRecieptBean.setOrderTotal(dataObj.getString("order_total"));
            orderRecieptBean.setDeliverTo(dataObj.getString("deliver_to"));
            orderRecieptBean.setContactInfo(dataObj.getString("contact_info"));
            orderRecieptBean.setDeliveryDate(dataObj.getString("delivery_date"));
            orderRecieptBean.setBagRecycleFee(dataObj.getString("bag_recycle_fee"));
            orderRecieptBean.setDeliveryCharge(dataObj.getString("delivery_charge"));
            orderRecieptBean.setPromoCode(dataObj.getString("promo_code"));
            orderRecieptBean.setCredits(dataObj.getString("credits"));
            orderRecieptBean.setTipAmount(dataObj.getString("tip_amount"));
            orderRecieptBean.setTipPercent(dataObj.getString("tip_percentage"));
            orderRecieptBean.setSubTotal(dataObj.getString("subtotal"));
            orderRecieptBean.setTax(dataObj.getString("tax"));
            JSONObject payObj = dataObj.getJSONObject("payments");
            PaymentCardBean cardBean = new PaymentCardBean();
            cardBean.setCardType(payObj.getString("card_type"));
            cardBean.setCardIssuer(payObj.getString("card_issuer"));
            cardBean.setCardCvvNumber(payObj.getString("cc_number"));
            cardBean.setCardExpiratoinDate(payObj.getString("expiration_date"));
            cardBean.setCardLastFourDigit(payObj.getString("last_4_digits"));
            cardBean.setCardGivenName(payObj.getString("given_card_name"));
            orderRecieptBean.setPaymentCardBean(cardBean);

            OrderDetailPromoBean orderDetailPromoBean = null;
            JSONObject cat = dataObj.optJSONObject("promo_code");
            if (cat != null) {
                JSONObject promoObj = dataObj.getJSONObject("promo_code");
                if (promoObj != null) {
                    orderDetailPromoBean = new OrderDetailPromoBean();
                    orderDetailPromoBean.setPromoCode(promoObj.getString("promo_code"));
                    orderDetailPromoBean.setPromoAmount(promoObj.getString("promo_amount"));
                    orderRecieptBean.setOrderDetailPromoBean(orderDetailPromoBean);
                }
            }
            JSONObject credCat = dataObj.optJSONObject("credits");
            if (credCat != null) {
                JSONObject credObj = dataObj.getJSONObject("credits");
                orderRecieptBean.setCredits(credObj.getString("credit_amount"));
            }


            JSONArray suppArray = dataObj.getJSONArray("suppliers");
            List<SuppliersBean> suppliersBeanList = new ArrayList<>();
            for (int i = 0; i < suppArray.length(); i++) {
                JSONObject suppObj = suppArray.getJSONObject(i);
                SuppliersBean suppliersBean = new SuppliersBean();
                suppliersBean.setSupplierId(suppObj.getString("supplier_id"));
                suppliersBean.setSupplierName(suppObj.getString("supplier_name"));
                suppliersBean.setCity(suppObj.getString("city"));
                suppliersBean.setOrderSupplierId(suppObj.getString("order_supplier_id"));
                suppliersBean.setSuppliersTotal(suppObj.getString("supplier_total"));
                JSONArray orderArray = suppObj.getJSONArray("order_items");
                List<OrderBean> orderBeanList = new ArrayList<>();
                for (int j = 0; j < orderArray.length(); j++) {
                    JSONObject obj = orderArray.getJSONObject(j);
                    OrderBean orderBean = new OrderBean();
                    orderBean.setItemName(obj.getString("item_name"));
                    orderBean.setSize(obj.getString("size"));
                    orderBean.setUom(obj.getString("uom"));
                    orderBean.setBrandName(obj.getString("brand_name"));
                    orderBean.setOrderQuantity(obj.getString("order_quantity"));
                    orderBean.setRegularPrice(obj.getString("regular_price"));
                    orderBean.setFinalPrice(obj.getString("final_price"));
                    orderBean.setIsSubstituted(obj.getString("is_substituted"));
                    orderBean.setSuppliedQuantity(obj.getString("supplied_qty"));
                    orderBean.setOutOfStock(obj.getString("out_of_stock"));
                    orderBean.setIsOrderedSuppliedQuantitySame(obj.getString("is_ordered_supplied_quantity_same"));
                    orderBean.setFreeItemQuantity(obj.getString("free_item_qty"));
                    orderBean.setTotal(obj.getString("total"));
                    orderBean.setSubstitution(obj.getString("substitution"));
                    orderBean.setApplicableSaleDescription(obj.getString("applicable_sale_description"));
                    JSONObject cat1 = obj.optJSONObject("substitution_with");
                    if (cat1 != null) {
                        JSONObject subObj = obj.getJSONObject("substitution_with");
                        OrderSubstitutionBean substitutionBean = new OrderSubstitutionBean();
                        substitutionBean.setSupplierItemId(subObj.getString("supplier_item_id"));
                        substitutionBean.setItemName(subObj.getString("item_name"));
                        substitutionBean.setSize(subObj.getString("size"));
                        substitutionBean.setUom(subObj.getString("uom"));
                        substitutionBean.setBrandName(subObj.getString("brand_name"));
                        substitutionBean.setOrderQuantity(subObj.getString("order_quantity"));
                        substitutionBean.setSuppliedQuantity(subObj.getString("supplied_qty"));
                        substitutionBean.setRegularPrice(subObj.getString("regular_price"));
                        substitutionBean.setFinalPrice(subObj.getString("final_price"));
                        substitutionBean.setTotal(subObj.getString("total"));
                        substitutionBean.setImages(subObj.getString("images"));
                        orderBean.setOrderSubstitutionBean(substitutionBean);
                    }

                    orderBeanList.add(orderBean);
                }

                suppliersBean.setDetailOrdersBeanList(orderBeanList);
                suppliersBeanList.add(suppliersBean);

            }
            orderRecieptBean.setSuppliersBeanList(suppliersBeanList);

        }

        return orderRecieptBean;

    }

    public static SearchTrendingBean convertRecentSearchJsonToRecentSearchbean(JSONObject jsonObject) throws JSONException {
        SearchTrendingBean searchTrendingBean = null;
        if (jsonObject != null) {
            searchTrendingBean = new SearchTrendingBean();
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONArray cat = dataObj.optJSONArray("tags");
            if (cat != null) {
                List<RecentSearchBean> recentSearchBeanList = new ArrayList<>();
                JSONArray tagArray = dataObj.getJSONArray("tags");
                for (int i = 0; i < tagArray.length(); i++) {
                    RecentSearchBean recentSearchBean = new RecentSearchBean();
                    JSONObject obj = tagArray.getJSONObject(i);
                    recentSearchBean.setSearchTag(obj.getString("tag"));
                    recentSearchBeanList.add(recentSearchBean);
                }
                searchTrendingBean.setRecentSearchBeanList(recentSearchBeanList);
            }

            JSONArray trendCat = dataObj.optJSONArray("trending_tags");
            if (trendCat != null) {
                List<TrendingBean> trendingBeanList = new ArrayList<>();
                JSONArray trendArray = dataObj.getJSONArray("trending_tags");
                for (int i = 0; i < trendArray.length(); i++) {
                    TrendingBean trendingBean = new TrendingBean();
                    JSONObject trendObj = trendArray.getJSONObject(i);
                    trendingBean.setTag(trendObj.getString("tag"));
                    trendingBeanList.add(trendingBean);
                }
                searchTrendingBean.setTrendingBeanList(trendingBeanList);
            }
        }
        return searchTrendingBean;
    }

    public static PaymentCardBean convertPaymentCardJsonToPaymentCardBean(JSONObject jsonObject) throws JSONException {
        PaymentCardBean card = null;
        if (jsonObject != null) {
            card = new PaymentCardBean();
            JSONObject obj = jsonObject.getJSONObject("data");
            card.setPaymentGatewayCustomerId(obj.getString("payment_gateway_customer_id"));
            card.setCardCvvNumber(obj.getString("cc_number"));
            card.setCardIssuer(obj.getString("card_issuer"));
            card.setCardExpiratoinDate(obj.getString("expiration_date"));
            card.setCardLastFourDigit(obj.getString("last_4_digits"));
            card.setCardGivenName(obj.getString("given_card_name"));
            card.setIsPrimary(obj.getString("is_primary"));
            card.setToken(obj.getString("token"));
            card.setUserPaymentMethodId(obj.getString("user_payment_method_id"));

        }

        return card;
    }

    public static LocationBean convertLocationJsonToLocationBean(JSONObject jsonObject) throws JSONException {
        LocationBean location = null;
        if (jsonObject != null) {
            location = new LocationBean();
            JSONObject obj = jsonObject.getJSONObject("data");
            location.setUserDeliveryLocationId(obj.getString("user_delivery_location_id"));
            location.setUserId(obj.getString("user_id"));
            location.setLocationType(obj.getString("location_type"));
            location.setAddress1(obj.getString("address1"));
            location.setAddress2(obj.getString("address2"));
            location.setAptSuitUnit(obj.getString("apt_suite_unit"));
            location.setLocationName(obj.getString("location_name"));
            location.setDeliveryInstructions(obj.getString("delivery_instructions"));
            location.setZipcode(obj.getString("zipcode"));
            location.setIsPrimaryLocation(obj.getString("is_primary_location"));
            location.setCity(obj.getString("city"));
            location.setState(obj.getString("state"));
            location.setCountry(obj.getString("country"));
        }

        return location;
    }

    public static List<StorePromotionBean> convertStorePromotionJsonToStorePromotionBean(JSONObject jsonObject) throws JSONException {
        List<StorePromotionBean> storePromotionBeanList = null;
        if (jsonObject != null) {
            JSONArray dataCat = jsonObject.optJSONArray("data");
            if (dataCat != null) {
                storePromotionBeanList = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    StorePromotionBean storePromotionBean = new StorePromotionBean();
                    JSONObject obj = dataArray.getJSONObject(i);
                    storePromotionBean.setSupplierId(obj.getString("supplier_id"));
                    storePromotionBean.setDiscountAmount(obj.getString("discount_amount"));
                    storePromotionBean.setMessage(obj.getString("message"));
                    storePromotionBeanList.add(storePromotionBean);
                }
            }
        }

        return storePromotionBeanList;
    }

    public static List<PromotionAvailableBean> convertPromotionAvailableJsonToPromotionAvailableBean(JSONObject jsonObject) throws JSONException {
        List<PromotionAvailableBean> promotionAvailableBeanList = null;
        if (jsonObject != null) {

            JSONArray dataCat = jsonObject.optJSONArray("data");
            if (dataCat != null) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                promotionAvailableBeanList = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    PromotionAvailableBean promotionAvailableBean = new PromotionAvailableBean();
                    promotionAvailableBean.setPromoCode(dataObj.getString("promo_code"));
                    promotionAvailableBean.setExcludeSalesItems(dataObj.getString("exclude_sales_items"));
                    promotionAvailableBean.setMultipleDiscountApplies(dataObj.getString("multiple_discount_applies"));
                    promotionAvailableBean.setAutoApply(dataObj.getString("auto_apply"));
                    promotionAvailableBean.setNewUser(dataObj.getString("new_user"));

                    JSONObject delCat = dataObj.optJSONObject("delivery");
                    if (delCat != null) {
                        DeliveryBean deliveryBean = new DeliveryBean();
                        JSONObject delObj = dataObj.getJSONObject("delivery");
                        deliveryBean.setFreeDelivery(delObj.getString("free_delivery"));
                        promotionAvailableBean.setDeliveryBean(deliveryBean);
                    }

                    JSONObject perCat = dataObj.optJSONObject("percent_off");
                    if (perCat != null) {
                        JSONObject perObj = dataObj.getJSONObject("percent_off");
                        PercentOff percentOff = new PercentOff();
                        percentOff.setDiscountAmntPercent(perObj.getString("discount_amt_percent"));
                        percentOff.setMaxDiscountAmnt(perObj.getString("max_discount_amount"));
                        percentOff.setMinimumOrderAmnt(perObj.getString("min_order_amount"));
                        promotionAvailableBean.setPercentOff(percentOff);
                    }

                    JSONObject bogoCat = dataObj.optJSONObject("bogo_offer");
                    if (bogoCat != null) {
                        JSONObject bogObj = dataObj.getJSONObject("bogo_offer");
                        BogoOffer bogoOffer = new BogoOffer();
                        bogoOffer.setBogoOffer(bogObj.getString("bogo_offer"));
                        bogoOffer.setFreeItemId(bogObj.getString("free_item_id"));
                        bogoOffer.setMinimumOrderAmnt(bogObj.getString("min_order_amount"));
                        promotionAvailableBean.setBogoOffer(bogoOffer);

                    }

                    JSONObject amtCat = dataObj.optJSONObject("amount_off");
                    if (amtCat != null) {
                        JSONObject amntObj = dataObj.getJSONObject("amount_off");
                        AmountOff amountOff = new AmountOff();
                        amountOff.setDiscountAmntValue(amntObj.getString("discount_amt_value"));
                        amountOff.setMinimumOrderAmnt(amntObj.getString("min_order_amount"));
                        promotionAvailableBean.setAmountOff(amountOff);

                    }

                    JSONObject freeCat = dataObj.optJSONObject("free_delivery_min_order");
                    if (freeCat != null) {
                        JSONObject freeObj = dataObj.getJSONObject("free_delivery_min_order");
                        FreeDeliveryMinOrder freeDeliveryMinOrder = new FreeDeliveryMinOrder();
                        freeDeliveryMinOrder.setFreeDelivery(freeObj.getString("free_delivery"));
                        freeDeliveryMinOrder.setMinOrderAmnt(freeObj.getString("min_order_amount"));
                        promotionAvailableBean.setFreeDeliveryMinOrder(freeDeliveryMinOrder);

                    }
                    promotionAvailableBeanList.add(promotionAvailableBean);

                }
            }
        }

        return promotionAvailableBeanList;
    }

    public static PopupRatingBean convertSupplierRatingJsonToSupplierRatingBean(JSONObject jsonObject) throws JSONException {
        PopupRatingBean ratingBean = null;
        if (jsonObject != null) {
            JSONObject cat = jsonObject.optJSONObject("data");
            if (cat != null) {
                ratingBean = new PopupRatingBean();
                JSONObject dataObj = jsonObject.getJSONObject("data");
                ratingBean.setOrderId(dataObj.getString("order_id"));
                ratingBean.setUserId(dataObj.getString("user_id"));
                ratingBean.setDriverId(dataObj.getString("driver_id"));
                ratingBean.setDriverName(dataObj.getString("driver_name"));
                ratingBean.setDriverPicture(dataObj.getString("driver_picture"));
                ratingBean.setDisplayOrderId(dataObj.getString("display_order_id"));
                ratingBean.setActualDeliveryDate(dataObj.getString("actual_delivery_date"));
                ratingBean.setActualDeliveryTime(dataObj.getString("actual_delivery_time"));
                JSONArray supCat = dataObj.optJSONArray("suppliers");

                List<SupplierRatingBean> supplierRatingBeanList = null;
                if (supCat != null) {
                    supplierRatingBeanList = new ArrayList<>();
                    JSONArray supArray = dataObj.getJSONArray("suppliers");
                    for (int i = 0; i < supArray.length(); i++) {
                        JSONObject obj = supArray.getJSONObject(i);
                        SupplierRatingBean supplierRatingBean = new SupplierRatingBean();
                        supplierRatingBean.setSupplierId(obj.getString("supplier_id"));
                        supplierRatingBean.setOrderSupplierId(obj.getString("order_supplier_id"));
                        supplierRatingBean.setSupplierName(obj.getString("supplier_name"));
                        supplierRatingBeanList.add(supplierRatingBean);

                    }

                    ratingBean.setSupplierRatingBeanList(supplierRatingBeanList);
                }
            }
        }
        return ratingBean;
    }
}
