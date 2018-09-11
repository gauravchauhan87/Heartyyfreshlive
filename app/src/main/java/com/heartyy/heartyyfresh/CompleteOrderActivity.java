package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.*;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.promotionbean.PromotionAvailableBean;
import com.heartyy.heartyyfresh.utils.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class CompleteOrderActivity extends AppCompatActivity {

    Typeface andBold, bold, italic, light;
    ListView storesListView;
    List<StoresTotalPriceBean> storesTotalPriceBeanList;
    CustomStoresTotalListAdapter adapter;
    Button placeOrderBtn;
    private SharedPreferences pref;
    TextView addressTypeText, addressText, addressText2, orderInsText, phoneText, paymentTypeText, cardNumberText, textdeliverTo, textorderInst, textoptional, textphone, textphoneOpt, textdeliverby, textpaymentmethod, textpaymenttype, textcardNumber, textchargeinfo, textcoupon, textcouponwarn, texttip, texttotal, texttax,
            textdeliveryfee, texttiptotal, textPromotionAmount, couponCodeText, creditText, creditAmountText;
    ImageView addressIcon, lineImage, cardIcon;
    private RelativeLayout addressLayout, orderInstructionsLayout, phoneLayout, dateViewLayout, deliverySlotsLayout, deliveryDateListLayout, tipListLayout, editCouponsLayout;
    private Button addAddressBtn, saveOrderInsBtn, savePhoneBtn, addCardBtn, buttonaddcoupon, applyPromoBtn, buttonaddtip;
    private EditText editOrderInst, editPhone, editcoupon;
    private boolean isAddress = false;
    private boolean isCard = false;
    private ListView deliveryStoreDateList;
    private CustomDeliveryStoresDateListAdapter dateListAdapter;
    private HorizontalListView dateListView, deliveryListView;
    private DeliveryWindowBean deliveryWindowBean;
    private CustomDateListAdapter dateAdapter;
    private CustomDeliverySlotsListAdapter dladapter;
    private TextView tipPriceText, deliveryPriceText, taxPriceText, returnPolicyText, termsOfText, totalAmountText, selectedTipAmount, tipNote, promotionNote, promoText;
    private TextView textYourTotal, storeWarningText;
    private String finalTotal, finalTaxPercent;
    private double finalTax = 0;
    private Button tipApplyBtn;
    private HorizontalListView tipList;
    private CustomTipListAdapter tipListAdapter;
    private List<TipBean> tipBeanList;
    private ImageView view10;
    private TipBean tipBean;
    private boolean isDelivery = true;
    private List<CheckDeliveryBean> checkDeliveryBeanList;
    private TextView storeNametext;
    private String estimatedFee;
    double totalAmount;
    List<SuppliersBean> availalesuppliersBeanList;
    private List<StorePromotionBean> storePromotionBeanList;
    private List<PromotionAvailableBean> promotionAvailableBeanList;
    public static PromotionAvailableBean promotionAvailableBean;
    double promotionDiscount = 0;
    public static double creditAmount = 0;
    public static boolean isCreditsApply = true;
    private boolean isShowCreditPopup = true;
    private boolean couponError = false;
    private boolean singleStoreDeliveryApplied = false;
    ProfileActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        final SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_checkout));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);

        ViewGroup rootView = (ViewGroup) findViewById(R.id.layout_return_policy);
        Global.setFont(rootView, light);
        estimatedFee = Global.estimatedFee;

        if (Global.phoneNo == null || Global.phoneNo.length() == 0) {
            Global.phoneNo = pref.getString(Constants.PHONE, null);
        }

        storesListView = (ListView) findViewById(R.id.list_stores_total);
        placeOrderBtn = (Button) findViewById(R.id.button_place_order);
        addressTypeText = (TextView) findViewById(R.id.text_address_type);
        addressText = (TextView) findViewById(R.id.text_address);
        addressText2 = (TextView) findViewById(R.id.text_address2);
        addressIcon = (ImageView) findViewById(R.id.image_address_icon);
        lineImage = (ImageView) findViewById(R.id.image_lineAddress);
        orderInstructionsLayout = (RelativeLayout) findViewById(R.id.layout_order_instructions);
        phoneLayout = (RelativeLayout) findViewById(R.id.layout_phone);
        addressLayout = (RelativeLayout) findViewById(R.id.layout_address);
        addAddressBtn = (Button) findViewById(R.id.button_addAddress);
        editOrderInst = (EditText) findViewById(R.id.edit_order_instructions);
        editPhone = (EditText) findViewById(R.id.edit_phone_no);
        saveOrderInsBtn = (Button) findViewById(R.id.button_save_order_instructions);
        savePhoneBtn = (Button) findViewById(R.id.button_save_phone_no);
        phoneText = (TextView) findViewById(R.id.text_phoneNo);
        paymentTypeText = (TextView) findViewById(R.id.text_payment_type);
        addCardBtn = (Button) findViewById(R.id.button_change_card);
        cardIcon = (ImageView) findViewById(R.id.image_cardType);
        dateViewLayout = (RelativeLayout) findViewById(R.id.layout_date_view);
        deliveryStoreDateList = (ListView) findViewById(R.id.list_stores_date);
        deliveryListView = (HorizontalListView) findViewById(R.id.list_delivery_slots);
        dateListView = (HorizontalListView) findViewById(R.id.list_dates);
        deliverySlotsLayout = (RelativeLayout) findViewById(R.id.layout_delivery_slots);
        deliveryDateListLayout = (RelativeLayout) findViewById(R.id.layout_delivery_date);
        textdeliverTo = (TextView) findViewById(R.id.text_deliverTo);
        textorderInst = (TextView) findViewById(R.id.text_orderInst);
        textoptional = (TextView) findViewById(R.id.text_optional);
        orderInsText = (TextView) findViewById(R.id.text_inst);
        textphone = (TextView) findViewById(R.id.text_phone);
        textdeliverby = (TextView) findViewById(R.id.text_deliver_by);
        textpaymentmethod = (TextView) findViewById(R.id.text_payment_method);
        cardNumberText = (TextView) findViewById(R.id.text_cardNumber);
        textchargeinfo = (TextView) findViewById(R.id.text_charge_info);
        textcoupon = (TextView) findViewById(R.id.text_coupon);
        buttonaddcoupon = (Button) findViewById(R.id.button_add_coupon);
        editcoupon = (EditText) findViewById(R.id.edit_coupon);
        applyPromoBtn = (Button) findViewById(R.id.button_apply);
        textcouponwarn = (TextView) findViewById(R.id.text_coupon_warn);
        texttip = (TextView) findViewById(R.id.text_tip);
        buttonaddtip = (Button) findViewById(R.id.button_add_tip);
        texttotal = (TextView) findViewById(R.id.text_total);
        texttax = (TextView) findViewById(R.id.text_tax);
        textdeliveryfee = (TextView) findViewById(R.id.text_delivery_fee);
        texttiptotal = (TextView) findViewById(R.id.text_tip_total);
        tipPriceText = (TextView) findViewById(R.id.text_tip_total_price);
        deliveryPriceText = (TextView) findViewById(R.id.text_delivery_fee_price);
        taxPriceText = (TextView) findViewById(R.id.text_tax_price);
        returnPolicyText = (TextView) findViewById(R.id.text_return_policy);
        termsOfText = (TextView) findViewById(R.id.text_terms_of);
        totalAmountText = (TextView) findViewById(R.id.text_total_amt);
        tipListLayout = (RelativeLayout) findViewById(R.id.layout_list_tip);
        tipApplyBtn = (Button) findViewById(R.id.button_tip_apply);
        tipList = (HorizontalListView) findViewById(R.id.list_tip);
        selectedTipAmount = (TextView) findViewById(R.id.text_selected_tip_amount);
        view10 = (ImageView) findViewById(R.id.view10);
        storeNametext = (TextView) findViewById(R.id.text_store_name);
        tipNote = (TextView) findViewById(R.id.text_tip_note);
        promotionNote = (TextView) findViewById(R.id.text_promotion_note);
        textYourTotal = (TextView) findViewById(R.id.text_your_total);
        promoText = (TextView) findViewById(R.id.text_promotion);
        storeWarningText = (TextView) findViewById(R.id.text_store_warning);
        editCouponsLayout = (RelativeLayout) findViewById(R.id.layout_edit_coupons);
        textPromotionAmount = (TextView) findViewById(R.id.text_promotion_amt);
        couponCodeText = (TextView) findViewById(R.id.text_coupon_code);
        creditText = (TextView) findViewById(R.id.text_credit);
        creditAmountText = (TextView) findViewById(R.id.text_credit_amt);

        textdeliverTo.setTypeface(andBold);
        addAddressBtn.setTypeface(andBold);
        addressText.setTypeface(andBold);
        textorderInst.setTypeface(light);
        textoptional.setTypeface(light);
        editOrderInst.setTypeface(andBold);
        saveOrderInsBtn.setTypeface(andBold);
        textphone.setTypeface(andBold);
        phoneText.setTypeface(light);
        editPhone.setTypeface(andBold);
        savePhoneBtn.setTypeface(andBold);
        textdeliverby.setTypeface(andBold);
        textpaymentmethod.setTypeface(andBold);
        addCardBtn.setTypeface(andBold);
        paymentTypeText.setTypeface(andBold);
        cardNumberText.setTypeface(light);
        textchargeinfo.setTypeface(light);
        textcoupon.setTypeface(andBold);
        buttonaddcoupon.setTypeface(andBold);
        editcoupon.setTypeface(andBold);
        applyPromoBtn.setTypeface(andBold);
        textcouponwarn.setTypeface(andBold);
        texttip.setTypeface(andBold);
        buttonaddtip.setTypeface(andBold);
        texttotal.setTypeface(andBold);
        texttax.setTypeface(light);
        taxPriceText.setTypeface(light);
        textdeliveryfee.setTypeface(light);
        deliveryPriceText.setTypeface(light);
        texttiptotal.setTypeface(light);
        tipPriceText.setTypeface(andBold);
        placeOrderBtn.setTypeface(andBold);
        tipApplyBtn.setTypeface(light);
        selectedTipAmount.setTypeface(light);
        storeNametext.setTypeface(andBold);
        textYourTotal.setTypeface(andBold);
        orderInsText.setTypeface(light);
        tipNote.setTypeface(light);
        promotionNote.setTypeface(light);
        promoText.setTypeface(light);
        storeWarningText.setTypeface(andBold);
        textPromotionAmount.setTypeface(light);
        couponCodeText.setTypeface(light);
        creditText.setTypeface(light);
        creditAmountText.setTypeface(light);

        final DatabaseHandler db = new DatabaseHandler(this);
        SpannableString content = new SpannableString(" return policy ");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        returnPolicyText.setText(content);

        SpannableString content1 = new SpannableString("Terms of Services");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        termsOfText.setText(content1);
        tipBeanList = new ArrayList<>();

        tipBeanList.add(new TipBean("No Tip", "0", "no", "yes"));
        tipBeanList.add(new TipBean("5%", "5", "yes", "no"));
        tipBeanList.add(new TipBean("10%", "10", "yes", "no"));
        tipBeanList.add(new TipBean("15%", "15", "yes", "no"));
        tipBeanList.add(new TipBean("$0.00", "0", "no", "no"));


        tipListAdapter = new CustomTipListAdapter(this, tipBeanList);
        tipList.setAdapter(tipListAdapter);

        tipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipBean = (TipBean) parent.getItemAtPosition(position);
                tipListAdapter.setSelected(position);
            }
        });

        buttonaddcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponCodeText.setVisibility(View.GONE);
                editCouponsLayout.setVisibility(View.VISIBLE);
            }
        });


        tipApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.tipBean = tipBean;
                if (Global.tipBean == null) {
                    showAlert("Please select a tip amount");
                } else {
                    //tipListLayout.setVisibility(View.GONE);
                    view10.setVisibility(View.GONE);
                    selectedTipAmount.setVisibility(View.VISIBLE);
                    //selectedTipAmount.setText(Global.tipBean.getTipName() + " selected");
                    buttonaddtip.setText("CHANGE");
                    isDelivery = false;

                    if (Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                        if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                            tipNote.setVisibility(View.VISIBLE);
                            tipNote.setText("(" + Global.tipBean.getTipAmount() + "% selected)");
                        } else {
                            tipNote.setVisibility(View.GONE);
                        }
                    } else {
                        tipNote.setVisibility(View.GONE);
                    }
                    finalTax = 0;
                    if (creditAmount > 0) {
                        String cr = pref.getString(Constants.CREDITS_AMOUNT, null);
                        if (cr != null) {
                            creditAmount = Double.parseDouble(cr);
                        }
                    }

                    promotionDiscount = 0;
                    getAvailableSuppliers();
                }
            }
        });

        buttonaddtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipListLayout.setVisibility(View.VISIBLE);
                view10.setVisibility(View.VISIBLE);
                selectedTipAmount.setVisibility(View.GONE);
                buttonaddtip.setText("CHANGE");
            }
        });

        if (Global.tipBean == null) {
            tipListLayout.setVisibility(View.GONE);
            view10.setVisibility(View.GONE);
            selectedTipAmount.setVisibility(View.GONE);
            tipNote.setVisibility(View.GONE);
            buttonaddtip.setText("ADD");
        } else {
            tipListLayout.setVisibility(View.GONE);
            view10.setVisibility(View.GONE);
            selectedTipAmount.setVisibility(View.VISIBLE);
            selectedTipAmount.setText(Global.tipBean.getTipName());
            tipNote.setVisibility(View.VISIBLE);
            if (Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                    tipNote.setText("(" + Global.tipBean.getTipAmount() + "% selected)");
                } else {
                    tipNote.setVisibility(View.GONE);
                }
            } else {
                tipNote.setVisibility(View.GONE);
            }
            buttonaddtip.setText("CHANGE");
        }

        if (Global.locationBean == null) {
            List<LocationBean> locationBeanList = db.getAllDeliveryaddress();
            for (int i = 0; i < locationBeanList.size(); i++) {
                LocationBean locationBean = locationBeanList.get(i);
                if (locationBean.getIsPrimaryLocation().equalsIgnoreCase("yes")) {
                    Global.locationBean = locationBean;
                    break;
                }
            }
        }


        final List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        if (suppliersBeanList.size() > 1) {
            deliveryDateListLayout.setVisibility(View.VISIBLE);
            dateViewLayout.setVisibility(View.GONE);
            deliverySlotsLayout.setVisibility(View.GONE);
            storeNametext.setVisibility(View.GONE);
            storeWarningText.setVisibility(View.GONE);
        } else {
            deliveryDateListLayout.setVisibility(View.GONE);
            dateViewLayout.setVisibility(View.VISIBLE);
            deliverySlotsLayout.setVisibility(View.VISIBLE);
            storeNametext.setVisibility(View.VISIBLE);
            storeWarningText.setVisibility(View.GONE);
        }


        if (Global.locationBean != null) {
            checkDelivery(Global.locationBean.getUserDeliveryLocationId(), "");
        } else {
            checkDelivery("", Global.zip);
        }

        applyPromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promo = editcoupon.getText().toString();
                if (promo.isEmpty()) {
                    showAlert("Please enter a valid Promo code");
                } else {
                    applyPromo(promo);
                }
            }
        });


        returnPolicyText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String suppId = "";
                DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
                List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    suppId += suppliersBeanList.get(i).getSupplierId() + ",";
                }
                suppId = suppId.substring(0, suppId.length() - 1);
                Intent intent = new Intent(CompleteOrderActivity.this, ReturnPolicyActivity.class);
                intent.putExtra("sup", suppId);
                startActivity(intent);
                return false;
            }
        });

        termsOfText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "tnc");
                startActivity(intent);
                Global.previousActivity = CompleteOrderActivity.this.getClass().getName();
                return false;
            }
        });

        phoneText.setText("");
        taxPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
        tipPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));


        if (estimatedFee.equalsIgnoreCase("0")) {
            deliveryPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
        } else {
            String delTemp[] = estimatedFee.split("\\.");
            deliveryPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + delTemp[0] + "</big><sup>" + delTemp[1] + "</sup>"));
        }

        totalAmountText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));

        if (Global.locationBean != null) {
            isAddress = true;
            if (Global.locationBean.getLocationName() != null) {
                if (Global.locationBean.getLocationName().length() > 0) {
                    addressTypeText.setText(Global.locationBean.getLocationName());
                } else {
                    if (Global.locationBean.getLocationType().equalsIgnoreCase("home")) {
                        addressTypeText.setText("Home");
                        addressIcon.setImageResource(R.drawable.home_icon);
                    } else {
                        addressTypeText.setText("work");
                        addressIcon.setImageResource(R.drawable.work_icon);
                    }
                }
            } else {
                if (Global.locationBean.getLocationType().equalsIgnoreCase("home")) {
                    addressTypeText.setText("Home");
                    addressIcon.setImageResource(R.drawable.home_icon);
                } else {
                    addressTypeText.setText("work");
                    addressIcon.setImageResource(R.drawable.work_icon);
                }
            }


            String[] address = new String[3];
            String[] address2 = new String[3];
            address[0] = Global.locationBean.getAddress1();
            address[1] = Global.locationBean.getAddress2();
            if (Global.locationBean.getAptSuitUnit().length() > 0) {
                if (Global.locationBean.getAptSuitUnit().contains("#")) {
                    Global.locationBean.setAptSuitUnit(Global.locationBean.getAptSuitUnit());
                } else {
                    Global.locationBean.setAptSuitUnit("#" + Global.locationBean.getAptSuitUnit());
                }
            }
            address[2] = Global.locationBean.getAptSuitUnit();
            address2[0] = Global.locationBean.getCity();
            address2[1] = Global.locationBean.getState();
            address2[2] = Global.locationBean.getZipcode();
            //address2[3] = Global.locationBean.getZipcode();
            addressText.setText(Global.FormatAddress(address));
            addressText2.setText(Global.FormatAddress(address2));

            addressLayout.setVisibility(View.VISIBLE);
            orderInstructionsLayout.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.VISIBLE);
            lineImage.setVisibility(View.VISIBLE);
            findViewById(R.id.view1).setVisibility(View.VISIBLE);
            findViewById(R.id.view2).setVisibility(View.VISIBLE);
            addAddressBtn.setText("CHANGE ADDRESS");


            if (!Global.locationBean.getDeliveryInstructions().equalsIgnoreCase("null") && Global.locationBean.getDeliveryInstructions().length() > 0) {
                Global.orderInstructions = Global.locationBean.getDeliveryInstructions();
            } else {
                Global.orderInstructions = null;
            }


        } else {
            addressLayout.setVisibility(View.GONE);
            orderInstructionsLayout.setVisibility(View.GONE);
            phoneLayout.setVisibility(View.GONE);
            lineImage.setVisibility(View.GONE);
            findViewById(R.id.view1).setVisibility(View.GONE);
            findViewById(R.id.view2).setVisibility(View.GONE);
            addAddressBtn.setText("ADD ADDRESS");
            orderInsText.setText("");

            double total = 0;

            List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
            for (int k = 0; k < suppliersBeanList1.size(); k++) {
                SuppliersBean suppliersBean = suppliersBeanList1.get(k);
                List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                for (int i = 0; i < orderBeanList.size(); i++) {
                    OrderBean order = orderBeanList.get(i);
                    total += Double.parseDouble(order.getFinalPrice());
                }
            }

            double totalAmount1 = 0 + total;
            String totalTemp[] = String.format("%.2f", totalAmount1).split("\\.");
            totalAmountText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + totalTemp[0] + "</big><sup>" + totalTemp[1] + "</sup>"));
        }

        if (Global.orderInstructions != null && Global.orderInstructions.length() > 0) {
            orderInsText.setVisibility(View.VISIBLE);
            orderInsText.setText(Global.orderInstructions);
            editOrderInst.setVisibility(View.GONE);
            saveOrderInsBtn.setVisibility(View.GONE);
        } else {
            orderInsText.setVisibility(View.GONE);
            editOrderInst.setVisibility(View.VISIBLE);
            saveOrderInsBtn.setVisibility(View.VISIBLE);
            orderInsText.setText("");
        }


        if (Global.phoneNo != null && Global.phoneNo.length() > 0) {
            phoneText.setVisibility(View.VISIBLE);
            phoneText.setText(Global.phoneNo);
            editPhone.setVisibility(View.GONE);
            savePhoneBtn.setVisibility(View.GONE);
        } else {
            phoneText.setVisibility(View.GONE);
            phoneText.setText("");
            editPhone.setVisibility(View.VISIBLE);
            savePhoneBtn.setVisibility(View.VISIBLE);
        }

        if (Global.paymentCardBean == null) {
            List<PaymentCardBean> paymentCardBeanList = db.getAllPaymentCards();
            for (int i = 0; i < paymentCardBeanList.size(); i++) {
                if (paymentCardBeanList.get(i).getIsPrimary().equalsIgnoreCase("yes")) {
                    Global.paymentCardBean = paymentCardBeanList.get(i);
                    break;
                }
            }
        }

        if (Global.paymentCardBean != null) {
            paymentTypeText.setVisibility(View.VISIBLE);
            cardNumberText.setVisibility(View.VISIBLE);
            cardIcon.setVisibility(View.VISIBLE);
            paymentTypeText.setText(Global.paymentCardBean.getCardIssuer());
            if (!Global.paymentCardBean.getCardLastFourDigit().equalsIgnoreCase("null")) {
                cardNumberText.setVisibility(View.VISIBLE);
                cardNumberText.setText("XXXX-" + Global.paymentCardBean.getCardLastFourDigit());
                if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("Visa")) {
                    cardIcon.setImageResource(R.drawable.visa_2x);
                } else if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("MasterCard")) {
                    cardIcon.setImageResource(R.drawable.mastercard_2x);
                } else if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("Discover")) {
                    cardIcon.setImageResource(R.drawable.discover_2x);
                } else if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("JCB")) {
                    cardIcon.setImageResource(R.drawable.jcb_2x);
                } else if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("Maestro")) {
                    cardIcon.setImageResource(R.drawable.maestro_2x);
                } else if (Global.paymentCardBean.getCardIssuer().equalsIgnoreCase("American Express")) {
                    cardIcon.setImageResource(R.drawable.amex_2x);
                }
            } else {
                cardIcon.setImageResource(R.drawable.paypal_icon);
                cardNumberText.setVisibility(View.GONE);
            }

            addCardBtn.setText("CHANGE CARD");
            isCard = true;
        } else {
            paymentTypeText.setVisibility(View.GONE);
            cardNumberText.setVisibility(View.GONE);
            cardIcon.setVisibility(View.GONE);
            addCardBtn.setText("ADD CARD");
        }


        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CompleteOrderActivity.this, PaymentActivity.class);
                startActivity(intent);
                Global.isCompleteBack = true;

            }
        });

        savePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString();
                if (phone.isEmpty()) {
                    showAlert("Please enter your phone no");
                    editPhone.requestFocus();
                } else if (phone.length() != 10) {
                    showAlert("Please enter a valid phone no");
                    editPhone.requestFocus();
                } else {
                    savePhoneBtn.setVisibility(View.GONE);
                    editPhone.setVisibility(View.GONE);
                    phoneText.setText(phone);
                    phoneText.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.PHONE, phone);
                    editor.commit();
                    Global.phoneNo = phone;
                }
            }
        });


        addAddressBtn.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View view) {
                                                 if (!isAddress) {
                                                     creditAmount = 0;
                                                     isCreditsApply = true;
                                                     isShowCreditPopup = true;

                                                     Intent intent = new Intent(CompleteOrderActivity.this, SearchAddressActivity.class);
                                                     startActivity(intent);
                                                     Global.isCompleteBack = true;
                                                     Global.previousActivity = DeliveryLocationActivity.class.getName();
                                                 } else {
                                                     creditAmount = 0;
                                                     isCreditsApply = true;
                                                     isShowCreditPopup = true;

                                                     Intent intent = new Intent(CompleteOrderActivity.this, DeliveryLocationActivity.class);
                                                     startActivity(intent);
                                                     Global.isCompleteBack = true;
                                                     Global.previousActivity = DeliveryLocationActivity.class.getName();
                                                 }
                                             }
                                         }

        );

        orderInstructionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderInsText.setVisibility(View.GONE);
                editOrderInst.setVisibility(View.VISIBLE);
                saveOrderInsBtn.setVisibility(View.VISIBLE);

            }
        });

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneText.setVisibility(View.GONE);
                editPhone.setVisibility(View.VISIBLE);
                savePhoneBtn.setVisibility(View.VISIBLE);
            }
        });


        saveOrderInsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inst = editOrderInst.getText().toString();
                if (inst.isEmpty()) {
                    showAlert("Please add order instructions");
                } else {
                    orderInsText.setVisibility(View.VISIBLE);
                    editOrderInst.setVisibility(View.GONE);
                    saveOrderInsBtn.setVisibility(View.GONE);
                    orderInsText.setText(inst);
                    Global.orderInstructions = inst;
                }
            }
        });

        dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OperatingHourBean operatingHourBean = (OperatingHourBean) adapterView.getItemAtPosition(i);
                adapterView.setSelection(i);
                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                if (operatingHourBean.getIsClose().equalsIgnoreCase("no")) {
                    dateAdapter.setSelected(i);
                    List<TimeIntervalBean> timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                    dladapter.changeSlot(timeIntervalBeanList);
                    SupplierDeliveryScheduleBean deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                    deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                    deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                    Global.dayOfWeek = operatingHourBean.getDayOfWeek();
                    deliveryScheduleBean.setDeliveryFrom(timeIntervalBeanList.get(0).getStartTime());
                    deliveryScheduleBean.setDeliveryTo(timeIntervalBeanList.get(0).getEndTime());
                    deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                    deliveryScheduleBean.setTimeIntervalBean(operatingHourBean.getTimeIntervalBeanList().get(0));
                    Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);
                    Global.map.get(suppliersBeanList1.get(0).getSupplierId());
                    Global.datePos = i;
                    Global.slotPos = 0;

                    Global.timeIntervalBean = timeIntervalBeanList.get(0);
                    Global.operatingHourBean = operatingHourBean;

                    String dateTime[] = operatingHourBean.getTimeIntervalBeanList().get(Global.slotPos).getStartTime().split(" ");
                    String time[] = dateTime[1].split(":");
                    int timePost = Integer.parseInt(time[0]);
                    if (timePost >= 18) {
                        estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeAfterSix();
                        Global.estimatedFee = estimatedFee;
                        Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                    } else {
                        estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                        Global.estimatedFee = estimatedFee;
                        Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                    }
                }
                promotionDiscount = 0;
                if (creditAmount > 0) {
                    String cr = pref.getString(Constants.CREDITS_AMOUNT, null);
                    if (cr != null) {
                        creditAmount = Double.parseDouble(cr);
                    }
                }
                checkIsPlaceOrderButtonEnabled();
                calculateTotal();
            }
        });

        deliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeIntervalBean timeIntervalBean = (TimeIntervalBean) adapterView.getItemAtPosition(i);
                dladapter.setSelected(i);
                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                SharedPreferences.Editor editor = pref.edit();
                Map<String, SupplierDeliveryScheduleBean> map = Global.map;
                SupplierDeliveryScheduleBean deliveryScheduleBean = map.get(suppliersBeanList1.get(0).getSupplierId());
                deliveryScheduleBean.setDeliveryFrom(timeIntervalBean.getStartTime());
                deliveryScheduleBean.setDeliveryTo(timeIntervalBean.getEndTime());
                Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);

                Global.timeIntervalBean = timeIntervalBean;
                Global.slotPos = i;

                String dateTime[] = timeIntervalBean.getStartTime().split(" ");
                String time[] = dateTime[1].split(":");
                int timePost = Integer.parseInt(time[0]);
                if (timePost >= 18) {
                    estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeAfterSix();
                    Global.estimatedFee = estimatedFee;
                    Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostAfterSix();

                } else {
                    estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                    Global.estimatedFee = estimatedFee;

                    Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();

                }
                promotionDiscount = 0;


                if (creditAmount > 0) {
                    String cr = pref.getString(Constants.CREDITS_AMOUNT, null);
                    if (cr != null) {
                        creditAmount = Double.parseDouble(cr);
                    }
                }
                checkIsPlaceOrderButtonEnabled();
                calculateTotal();

            }
        });

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 String taxPercent = null;
                                                 String addressId = null;
                                                 String paymentId = null;

                                                 LocationBean location = Global.locationBean;
                                                 PaymentCardBean card = Global.paymentCardBean;
                                                 if (location != null) {
                                                     taxPercent = location.getTaxRate();
                                                     addressId = location.getUserDeliveryLocationId();
                                                 }
                                                 if (card != null) {
                                                     paymentId = card.getUserPaymentMethodId();
                                                 }

                                                 if(Global.phoneNo == null){
                                                     showAlert("Please provide phone number");
                                                     editPhone.requestFocus();
                                                 }else if (location == null || card == null) {
                                                     if (location == null) {
                                                         showAlert("Please add your delivery location ");
                                                     } else if (card == null) {
                                                         showAlert("Please add your payment method");
                                                     }
                                                 } else {
                                                     if (Global.map.size() == 0) {
                                                         showAlert("Please select your delivery time");
                                                     } else {
                                                         int temp = 0;
                                                         String supplierName = null;
                                                         for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
                                                             if (Global.map.get(availalesuppliersBeanList.get(i).getSupplierId()) == null) {
                                                                 temp = 1;
                                                                 supplierName = availalesuppliersBeanList.get(i).getSupplierName();
                                                                 break;
                                                             }
                                                         }

                                                         if (temp == 0) {
                                                             singleStoreDeliveryApplied = true;
                                                             calculateTotal();
                                                             placeOrder(addressId, paymentId);
                                                         } else {
                                                             showAlert("Please select your delivery time for " + supplierName);
                                                         }
                                                     }
                                                 }
                                             }
                                         }

        );
        checkIsPlaceOrderButtonEnabled();
    }

    private void applyPromo(String promo) {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);

        String zip = null;
        if (Global.locationBean != null) {
            zip = Global.locationBean.getZipcode();
        } else {
            zip = Global.zip;
        }

        String url = "apply/code?code=" + promo + "&user_id=" + pref.getString(Constants.USER_ID, null) + "&zipcode=" + zip;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("coupon code  response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                promotionAvailableBeanList = ConversionHelper.convertPromotionAvailableJsonToPromotionAvailableBean(jsonObject);
                                if (promotionAvailableBeanList != null) {
                                    promotionAvailableBean = promotionAvailableBeanList.get(0);
                                    if (promotionAvailableBean.getMultipleDiscountApplies().equalsIgnoreCase("yes")) {
                                        promotionDiscount = 0;
                                        finalTax = 0;
                                        calculateTotal();
                                    } else {
                                        if (promotionDiscount != 0) {
                                            promotionAvailableBean = null;
                                            showAlert("You have already avail the best Promotion");

                                        } else {
                                            finalTax = 0;
                                            calculateTotal();
                                        }
                                    }
                                } else {
                                    promotionAvailableBean = null;
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {

                    showAlert(Constants.NO_INTERNET);

                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getAvailableSuppliers() {
        final DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        availalesuppliersBeanList = null;
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        availalesuppliersBeanList = new ArrayList<>();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            if (suppliersBeanList.get(i).getSupplierAvailable().equalsIgnoreCase("yes")) {
                availalesuppliersBeanList.add(suppliersBeanList.get(i));
            }
        }
        if (availalesuppliersBeanList.size() > 0) {
            checkStorePromotion();
        } else {
            ImageView view9 = (ImageView) findViewById(R.id.view9);
            view9.setVisibility(View.GONE);
            getDeliveryTimes();
        }
    }

    private void checkStorePromotion() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        double price, salesPrice;

        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();
            for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
                SuppliersBean suppliersBean = availalesuppliersBeanList.get(i);
                if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                    JSONObject supObj = new JSONObject();
                    supObj.put("supplier_id", suppliersBean.getSupplierId());
                    price = 0;
                    salesPrice = 0;
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                    for (int j = 0; j < orderBeanList.size(); j++) {
                        price += Double.parseDouble(orderBeanList.get(j).getFinalPrice());
                        if (orderBeanList.get(j).getRegularPrice().equalsIgnoreCase(orderBeanList.get(j).getUnitPrice())) {

                        } else {
                            salesPrice += Double.parseDouble(orderBeanList.get(j).getFinalPrice());
                        }

                    }
                    supObj.put("sub_total", String.format("%.2f", price));
                    supObj.put("sales_item_total", String.format("%.2f", salesPrice));
                    supArray.put(supObj);

                }
            }
            params.put("suppliers", supArray);
            if (Global.locationBean != null) {
                params.put("zipcode", Global.locationBean.getZipcode());
            } else {
                params.put("zipcode", Global.zip);
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "supplier/promotion/available", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("promotion response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                storePromotionBeanList = ConversionHelper.convertStorePromotionJsonToStorePromotionBean(jsonObject);

                                storesTotalPriceBeanList = new ArrayList<>();
                                if (availalesuppliersBeanList != null)

                                {
                                    for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
                                        StoresTotalPriceBean store = new StoresTotalPriceBean();
                                        SuppliersBean suppliersBean = availalesuppliersBeanList.get(i);
                                        if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                                            store.setStoreName(suppliersBean.getSupplierName());
                                            store.setIsAvailable(suppliersBean.getSupplierAvailable());
                                            List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                                            double total = 0;
                                            for (int j = 0; j < orderBeanList.size(); j++) {

                                                OrderBean order = orderBeanList.get(j);
                                                total += Double.parseDouble(order.getFinalPrice());
                                            }


                                            store.setStorePrice(String.valueOf(total));
                                            storesTotalPriceBeanList.add(store);
                                        }
                                    }


                                    if (storesTotalPriceBeanList.size() == 0) {
                                        ImageView view9 = (ImageView) findViewById(R.id.view9);
                                        view9.setVisibility(View.GONE);
                                    }

                                    adapter = new CustomStoresTotalListAdapter(CompleteOrderActivity.this, storesTotalPriceBeanList, CompleteOrderActivity.this);
                                    storesListView.setAdapter(adapter);
                                    Global.setListViewHeightBasedOnChildren(storesListView);
                                }

                                getDeliveryTimes();


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Global.dialog.dismiss();
                    showAlert(Constants.NO_INTERNET);

                } else {
                    Global.dialog.dismiss();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void getDeliveryTimes() {
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
        final DatabaseHandler db = new DatabaseHandler(this);
        String supplierId = "";
        String shippingWeight = "";
        final List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean suppliersBean = suppliersBeanList.get(i);
            if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                supplierId += suppliersBean.getSupplierId() + ",";
                double totalweight = 0;
                List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                for (int j = 0; j < orderBeanList.size(); j++) {
                    if (orderBeanList.get(j).getShippingWeight() != null) {
                        if (!orderBeanList.get(j).getShippingWeight().equalsIgnoreCase("null")) {
                            int quant = Integer.parseInt(orderBeanList.get(j).getOrderQuantity());
                            Double weight = Double.parseDouble(orderBeanList.get(j).getShippingWeight());
                            weight = weight * quant;
                            totalweight += weight;
                        }
                    }
                }
                shippingWeight += String.valueOf(totalweight) + ",";
            }
        }

        if (supplierId.length() > 0) {
            supplierId = supplierId.substring(0, supplierId.length() - 1);
        }

        String addressId = "";
        if (Global.locationBean != null) {
            addressId = Global.locationBean.getUserDeliveryLocationId();
        }

        String url = "store/operation/window?supplier_id=" + supplierId + "&timezone=" + tz.getID() + "&user_id=" + pref.getString(Constants.USER_ID, null) + "&zipcode=" + Global.zip + "&shipping_weight=" + shippingWeight + "&address_id=" + addressId;
        Log.d("delivery url..", Constants.URL + url);
        RequestQueue rq = Volley.newRequestQueue(this);

        final String finalSupplierId = supplierId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                deliveryWindowBean = ConversionHelper.convertDeliveryJSonToDeliveryBean(jsonObject);
                                int count = db.getSuppliersCount();
                                if (count == 1) {
                                    if (deliveryWindowBean.getSingleDeliveryBean() != null) {
                                        estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                                        Global.estimatedFee = estimatedFee;
                                        Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();

                                        List<TimeIntervalBean> timeIntervalBeanList = null;
                                        List<OperatingHourBean> operatingHourBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList();
                                        int pos = -1;
                                        for (int i = 0; i < operatingHourBeanList.size(); i++) {
                                            OperatingHourBean operatingHourBean = operatingHourBeanList.get(i);
                                            if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                                                pos = i;
                                                if (Global.datePos == -1) {
                                                    Global.datePos = i;
                                                    Global.slotPos = 0;
                                                    List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();

                                                    SupplierDeliveryScheduleBean deliveryScheduleBean = Global.map.get(suppliersBeanList1.get(0).getSupplierId());
                                                    if (deliveryScheduleBean == null) {
                                                        deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                                    }
                                                    deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                                                    Global.dayOfWeek = operatingHourBean.getDayOfWeek();
                                                    deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                                                    deliveryScheduleBean.setDeliveryFrom(operatingHourBean.getTimeIntervalBeanList().get(0).getStartTime());
                                                    deliveryScheduleBean.setDeliveryTo(operatingHourBean.getTimeIntervalBeanList().get(0).getEndTime());
                                                    deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                                                    deliveryScheduleBean.setTimeIntervalBean(operatingHourBean.getTimeIntervalBeanList().get(0));
                                                    String dateTime[] = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(Global.datePos).getTimeIntervalBeanList().get(Global.slotPos).getStartTime().split(" ");
                                                    String time[] = dateTime[1].split(":");
                                                    int timePost = Integer.parseInt(time[0]);
                                                    if (timePost >= 18) {
                                                        estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeAfterSix();
                                                        Global.estimatedFee = estimatedFee;
                                                        Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostAfterSix();
                                                        deliveryScheduleBean.setFinalDeliveryPrice(deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeAfterSix());
                                                    } else {
                                                        estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                                                        Global.estimatedFee = estimatedFee;
                                                        Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();
                                                        deliveryScheduleBean.setFinalDeliveryPrice(deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix());
                                                    }
                                                    Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);
                                                    Global.operatingHourBean = operatingHourBean;
                                                    timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                                                    checkIsPlaceOrderButtonEnabled();
                                                    break;
                                                }
                                            }
                                        }


                                        dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                        dateListView.setAdapter(dateAdapter);
                                        dateAdapter.setSelected(Global.datePos);

                                        dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, Global.operatingHourBean.getTimeIntervalBeanList());
                                        deliveryListView.setAdapter(dladapter);
                                        dladapter.setSelected(Global.slotPos);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(Constants.DELIVERY_DATE, Global.operatingHourBean.getDate());
                                        editor.putString(Constants.DELIVERY_FROM, Global.operatingHourBean.getTimeIntervalBeanList().get(0).getStartTime());
                                        editor.putString(Constants.DELIVERY_TO, Global.operatingHourBean.getTimeIntervalBeanList().get(0).getEndTime());
                                        editor.commit();
                                    } else {
                                        List<TimeIntervalBean> timeIntervalBeanList = null;
                                        List<OperatingHourBean> operatingHourBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList();
                                        int pos = -1;
                                        int i = 0;
                                        for (i = 0; i < operatingHourBeanList.size(); i++) {
                                            OperatingHourBean operatingHourBean = operatingHourBeanList.get(i);
                                            if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                                                pos = i;
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString(Constants.DELIVERY_DATE, operatingHourBean.getDate());
                                                editor.commit();
                                                if (db.getSuppliersCount() == 1) {
                                                    List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                                                    Map<String, SupplierDeliveryScheduleBean> map = Global.map;
                                                    SupplierDeliveryScheduleBean deliveryScheduleBean = map.get(pref.getString(Constants.SUPPLIER_ID, null));
                                                    if (deliveryScheduleBean == null) {
                                                        deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                                        deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                                                        deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                                                        Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);
                                                    }

                                                }
                                                Global.operatingHourBean = operatingHourBean;
                                                timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                                                break;
                                            }
                                        }

                                        dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                        dateListView.setAdapter(dateAdapter);
                                        dateAdapter.setSelected(pos);
                                        dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, timeIntervalBeanList);
                                        deliveryListView.setAdapter(dladapter);
                                        dladapter.setSelected(0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(Constants.DELIVERY_FROM, timeIntervalBeanList.get(0).getStartTime());
                                        editor.putString(Constants.DELIVERY_TO, timeIntervalBeanList.get(0).getEndTime());
                                        editor.commit();
                                        if (db.getSuppliersCount() == 1) {
                                            List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                                            Map<String, SupplierDeliveryScheduleBean> map = Global.map;
                                            SupplierDeliveryScheduleBean deliveryScheduleBean = map.get(pref.getString(Constants.SUPPLIER_ID, null));
                                            if (deliveryScheduleBean == null) {
                                                deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                            }
                                            deliveryScheduleBean.setDeliveryFrom(timeIntervalBeanList.get(0).getStartTime());
                                            deliveryScheduleBean.setDeliveryTo(timeIntervalBeanList.get(0).getEndTime());
                                            deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBeanList.get(i).getDayOfWeek());

                                            Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);
                                        }
                                        Global.timeIntervalBean = timeIntervalBeanList.get(0);
                                    }
                                }
                                calculateTotal();
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                if (finalSupplierId.length() > 0) {
                                    showAlert(jsonObject.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else if (error instanceof TimeoutError) {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                } else {
                    showAlert(Constants.SERVER_ERROR);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void calculateTotal() {
        double ef = Double.parseDouble(Global.estimatedFee);
        String delTemp[] = String.format("%.2f", ef).split("\\.");
        deliveryPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + delTemp[0] + "</big><sup>" + delTemp[1] + "</sup>"));

        DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        final List<SuppliersBean> finalAvailalesuppliersBeanList = availalesuppliersBeanList;

        double total = 0;
        int freeDelivery = 0;
        String promoNote = "";

        finalTax = 0;
        for (int k = 0; k < finalAvailalesuppliersBeanList.size(); k++) {
            SuppliersBean suppliersBean = finalAvailalesuppliersBeanList.get(k);
            List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
            for (int i = 0; i < orderBeanList.size(); i++) {
                OrderBean order = orderBeanList.get(i);
                total += Double.parseDouble(order.getFinalPrice());
                finalTax += (order.getTaxAmount() * Integer.parseInt(order.getOrderQuantity()));
            }
        }


        if (storePromotionBeanList != null) {
            for (int i = 0; i < storePromotionBeanList.size(); i++) {
                promotionDiscount = Double.parseDouble(storePromotionBeanList.get(i).getDiscountAmount());
            }
            couponError = false;
            promoNote = "(Auto Promotion Applied)";
        }

        double discountedFee = 0;
        double fee = 0;
        if (Global.isIndividualDelivery) {
            SupplierDeliveryScheduleBean sScheduledBean = null;
            for (int i = 0; i < finalAvailalesuppliersBeanList.size(); i++) {
                sScheduledBean = Global.map.get(finalAvailalesuppliersBeanList.get(i).getSupplierId());
                if (sScheduledBean != null) {
                    if (Global.promotionApplicableOnDays != null) {
                        if (Global.promotionApplicableOnDays.get(sScheduledBean.getDeliveryDayOfWeek()) != null) {
                            if (Global.promotionApplicableOnDays.get(sScheduledBean.getDeliveryDayOfWeek()).equalsIgnoreCase("YES")) {
                                fee += Double.parseDouble(sScheduledBean.getFinalDeliveryPrice());
                            }
                        }
                    }
                }
            }
            discountedFee = fee;
        } else {
            discountedFee = ef;
        }


        if (Global.isIndividualDelivery) {
            if (Global.promotionAvailableBean != null) {
                if (Global.isFreeDelivery) {
                    if (total >= Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {
                        if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                            //promotionDiscount = Double.parseDouble(Global.estimatedFee);
                            promotionDiscount = discountedFee;
                            freeDelivery = 1;
                            couponError = false;
                            promoNote = "(Free Delivery Applied)";
                        }
                    }
                }
            }
        } else if (Global.dayOfWeek != null) {
            if (Global.promotionApplicableOnDays.get(Global.dayOfWeek) != null) {
                if (Global.promotionApplicableOnDays.get(Global.dayOfWeek).equalsIgnoreCase("YES")) {
                    if (Global.promotionAvailableBean != null) {
                        if (Global.isFreeDelivery) {
                            if (total >= Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {
                                if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                                    //promotionDiscount = Double.parseDouble(Global.estimatedFee);
                                    promotionDiscount = discountedFee;
                                    freeDelivery = 1;
                                    couponError = false;
                                    promoNote = "(Free Delivery Applied)";
                                }
                            }
                        }
                    }
                }
            }
        }


        if (promotionAvailableBean != null) {
            if (promotionAvailableBean.getFreeDeliveryMinOrder() != null) {
                if (freeDelivery == 0) {
                    if (promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                        if (total >= Double.parseDouble(promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {
                            if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                                promotionDiscount = Double.parseDouble(Global.estimatedFee);
                                freeDelivery = 1;
                                couponError = false;
                                promoNote = "(Free Delivery Applied)";
                            }
                        }
                    }
                }
            } else if (promotionAvailableBean.getAmountOff() != null) {
                double amountOff = Double.parseDouble(promotionAvailableBean.getAmountOff().getDiscountAmntValue());
                if (!promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                    double min = Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt());
                    if (min > 0) {
                        if (total >= Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt())) {
                            if (promotionDiscount < amountOff) {
                                promotionDiscount = amountOff;
                                couponError = false;
                                promoNote = "(Amount off Applied)";
                                freeDelivery = 0;
                            }
                        } else {
                            couponCodeText.setVisibility(View.GONE);
                            buttonaddcoupon.setText("ADD");
                            couponError = true;
                            showAlert("Can not applied Coupon Code as your minimum order amount should be of $" + promotionAvailableBean.getAmountOff().getMinimumOrderAmnt());
                        }

                    } else {
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            couponError = false;
                            promoNote = "(Amount off Applied)";
                        }
                    }
                } else {
                    if (promotionDiscount < amountOff) {
                        promotionDiscount = amountOff;
                        freeDelivery = 0;
                        couponError = false;
                        promoNote = "(Amount off Applied)";
                    }
                }


            } else if (promotionAvailableBean.getDeliveryBean() != null) {
                if (promotionAvailableBean.getDeliveryBean().getFreeDelivery().equalsIgnoreCase("yes")) {
                    if (freeDelivery == 0) {
                        if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                            promotionDiscount = Double.parseDouble(Global.estimatedFee);
                            freeDelivery = 1;
                            couponError = false;
                            promoNote = "(Free Delivery Applied)";
                        }
                    }
                }
            } else if (promotionAvailableBean.getPercentOff() != null) {
                double discPercent = Double.parseDouble(promotionAvailableBean.getPercentOff().getDiscountAmntPercent());
                double supTotal = 0;
                double amountOff = 0;
                if (promotionAvailableBean.getExcludeSalesItems().equalsIgnoreCase("yes")) {
                    for (int s = 0; s < availalesuppliersBeanList.size(); s++) {
                        SuppliersBean sup = availalesuppliersBeanList.get(s);
                        List<OrderBean> orderBeanList = db.getAllOrders(sup);
                        for (int k = 0; k < orderBeanList.size(); k++) {
                            if (orderBeanList.get(k).getRegularPrice().equalsIgnoreCase(orderBeanList.get(k).getUnitPrice())) {
                                supTotal += Double.parseDouble(orderBeanList.get(k).getFinalPrice());
                            }

                        }
                    }

                    amountOff = (discPercent / 100) * supTotal;

                    if (!promotionAvailableBean.getPercentOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                        double min = Double.parseDouble(promotionAvailableBean.getPercentOff().getMinimumOrderAmnt());
                        if (min > 0) {
                            if (total >= Double.parseDouble(promotionAvailableBean.getPercentOff().getMinimumOrderAmnt())) {
                                if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                                    amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                                }

                                if (promotionDiscount < amountOff) {
                                    promotionDiscount = amountOff;
                                    freeDelivery = 0;
                                    promoNote = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                                }
                            } else {
                                couponCodeText.setVisibility(View.GONE);
                                buttonaddcoupon.setText("ADD");
                                couponError = true;
                                showAlert("Can not applied Coupon Code as your minimum order amount should be of $" + promotionAvailableBean.getPercentOff().getMinimumOrderAmnt());
                            }

                        } else {
                            if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                                amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                            }

                            if (promotionDiscount < amountOff) {
                                promotionDiscount = amountOff;
                                freeDelivery = 0;
                                couponError = false;
                                promoNote = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                            }
                        }
                    } else {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }

                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            couponError = false;
                            promoNote = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                        }
                    }
                }
            }
        }


        String[] taxstr = String.valueOf(finalTax).split("\\.");
        if (taxstr.length > 1) {
            String temp[] = String.format("%.2f", Double.parseDouble(String.valueOf(finalTax))).split("\\.");
            taxPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + taxstr[0] + "</big><sup>" + temp[1] + "</sup>"));
        } else {
            taxPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + taxstr[0] + "</big><sup>" + "00" + "</sup>"));
        }

        String prom[] = String.format("%.2f", promotionDiscount).split("\\.");
        textPromotionAmount.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + prom[0] + "</big><sup>" + prom[1] + "</sup><big>)</big>"));
        if (promotionDiscount != 0) {
            promoText.setVisibility(View.VISIBLE);
            textPromotionAmount.setVisibility(View.VISIBLE);
            promotionNote.setVisibility(View.VISIBLE);

            if (!couponError) {
                if (promotionAvailableBean != null) {
                    couponCodeText.setVisibility(View.VISIBLE);
                    couponCodeText.setText(promotionAvailableBean.getPromoCode());
                    buttonaddcoupon.setText("CHANGE");
                } else {
                    promotionAvailableBean = null;
                    couponCodeText.setVisibility(View.GONE);
                    buttonaddcoupon.setText("ADD");
                }
            } else {
                promotionAvailableBean = null;
                couponCodeText.setVisibility(View.GONE);
                buttonaddcoupon.setText("ADD");
            }

            promotionNote.setText(promoNote);
            editCouponsLayout.setVisibility(View.GONE);

        } else {
            promoText.setVisibility(View.GONE);
            textPromotionAmount.setVisibility(View.GONE);
            promotionNote.setVisibility(View.GONE);
        }

        if (freeDelivery == 0) {
            if (total > promotionDiscount) {
                total = total - promotionDiscount;
            } else {
                total = 0.00;
            }
        }

        if (availalesuppliersBeanList.size() == 0) {
            estimatedFee = "0";
            deliveryPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
            totalAmount = finalTax + total + Double.parseDouble(estimatedFee);
            totalAmount = finalTax + total;
            placeOrderBtn.setEnabled(false);
            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
        } else {
            if (freeDelivery == 1) {
                estimatedFee = Global.estimatedFee;
                double f = Double.parseDouble(estimatedFee) - discountedFee;
                totalAmount = finalTax + total + Double.parseDouble(estimatedFee);
                totalAmount = finalTax + total + f;
            } else {
                totalAmount = finalTax + total + Double.parseDouble(Global.estimatedFee);
            }
        }


        if (Global.tipBean != null) {
            tipListLayout.setVisibility(View.GONE);
            if (Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                    String tipPercent = Global.tipBean.getTipAmount();
                    double tipAmount = (totalAmount * Double.parseDouble(tipPercent)) / 100;
                    selectedTipAmount.setText("$" + String.format("%.2f", tipAmount) + " selected");
                    String tipTemp[] = String.format("%.2f", tipAmount).split("\\.");
                    tipPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tipTemp[0] + "</big><sup>" + tipTemp[1] + "</sup>"));
                    totalAmount += tipAmount;
                } else {
                    selectedTipAmount.setText(Global.tipBean.getTipName() + " selected");
                    String amount = Global.tipBean.getTipAmount();
                    totalAmount = totalAmount + Double.parseDouble(amount);
                    double tipAmount = Double.parseDouble(amount);
                    String tipTemp[] = String.format("%.2f", tipAmount).split("\\.");
                    tipPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tipTemp[0] + "</big><sup>" + tipTemp[1] + "</sup>"));
                }
            } else {
                tipPriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
            }
        }
        if (isCreditsApply) {
            if (creditAmount != 0) {
                creditAmountText.setVisibility(View.VISIBLE);
                creditText.setVisibility(View.VISIBLE);

                if (creditAmount >= totalAmount) {
                    creditAmount = totalAmount;
                }
                totalAmount = totalAmount - creditAmount;
                String credits1[] = String.format("%.2f", creditAmount).split("\\.");
                creditAmountText.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + credits1[0] + "</big><sup>" + credits1[1] + "</sup><big>)</big>"));
            } else {
                creditAmountText.setVisibility(View.GONE);
                creditText.setVisibility(View.GONE);
            }
        } else {
            creditAmountText.setVisibility(View.GONE);
            creditText.setVisibility(View.GONE);
        }

        String totalTemp[] = String.format("%.2f", totalAmount).split("\\.");
        totalAmountText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + totalTemp[0] + "</big><sup>" + totalTemp[1] + "</sup>"));
        // promotionDiscount = 0;
        String crdts = pref.getString(Constants.CREDITS_AMOUNT, null);
        if (crdts != null) {
            double crdtDouble = Double.parseDouble(crdts);
            if (creditAmount == 0 && isShowCreditPopup) {
                if (crdtDouble > 0.0) {
                    if (Global.map.size() != 0) {
                        showApplyCredits("Do you want to use your credits", crdts);
                    } else if (singleStoreDeliveryApplied) {
                        showApplyCredits("Do you want to use your credits", crdts);
                    }
                }
            }
        }
    }


    private void placeOrder(String addressId, String paymentId) {
        DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        Global.showProgress(CompleteOrderActivity.this);
        RequestQueue rq = Volley.newRequestQueue(CompleteOrderActivity.this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));

            JSONArray orderArray = new JSONArray();
            JSONObject orderObj = new JSONObject();

            JSONArray suppliersArray = new JSONArray();

            double total = 0;
            double tax = 0;
            double subtotal = 0;

            List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
            for (int k = 0; k < suppliersBeanList1.size(); k++) {
                SuppliersBean suppliersBean = suppliersBeanList1.get(k);
                if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                    JSONObject supObj = new JSONObject();
                    supObj.put("supplier_id", suppliersBean.getSupplierId());
                    String type = pref.getString(Constants.DELIVERY_TYPE, null);
                    if (type.equalsIgnoreCase(Constants.COMBINE)) {
                        supObj.put("delivery_date", pref.getString(Constants.DELIVERY_DATE, null));
                        supObj.put("delivery_from_time", pref.getString(Constants.DELIVERY_FROM, null));
                        supObj.put("delivery_to_time", pref.getString(Constants.DELIVERY_TO, null));
                        try {
                            List<WorkingDaysBean> workingDaysBeanList = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList();
                            WorkingDaysBean work = null;
                            for (int i = 0; i < workingDaysBeanList.size(); i++) {
                                work = workingDaysBeanList.get(i);
                                if (work.getSupplierId().equalsIgnoreCase(suppliersBean.getSupplierId())) {
                                    break;
                                }
                            }

                            supObj.put("est_delivery_miles", work.getDeliveryMiles());
                            supObj.put("est_delivery_minutes", work.getDeliveryMinutes());
                            supObj.put("est_delivery_congestion", Global.congestionCost);
                            supObj.put("delivery_weight_surcharge", work.getDeliveryWeightSurcharge());
                            supObj.put("est_delivery_total", Global.estimatedFee);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        SupplierDeliveryScheduleBean deliveryScheduleBean = Global.map.get(suppliersBean.getSupplierId());
                        if (deliveryScheduleBean == null) {
                            deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                        }
                        supObj.put("delivery_date", deliveryScheduleBean.getDeliveryDate());
                        supObj.put("delivery_from_time", deliveryScheduleBean.getDeliveryFrom());
                        supObj.put("delivery_to_time", deliveryScheduleBean.getDeliveryTo());

                        try {
                            List<WorkingDaysBean> workingDaysBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList();
                            WorkingDaysBean work = null;
                            for (int i = 0; i < workingDaysBeanList.size(); i++) {
                                work = workingDaysBeanList.get(i);
                                if (work.getSupplierId().equalsIgnoreCase(suppliersBean.getSupplierId())) {
                                    break;
                                }
                            }
                            supObj.put("est_delivery_miles", work.getDeliveryMiles());
                            supObj.put("est_delivery_minutes", work.getDeliveryMinutes());
                            supObj.put("est_delivery_congestion", Global.congestionCost);
                            supObj.put("delivery_weight_surcharge", work.getDeliveryWeightSurcharge());
                            supObj.put("est_delivery_total", Global.estimatedFee);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                    JSONArray itemsArray = new JSONArray();
                    for (int i = 0; i < orderBeanList.size(); i++) {
                        OrderBean order = orderBeanList.get(i);
                        total += Double.parseDouble(order.getFinalPrice());
                        JSONObject itemObj = new JSONObject();
                        itemObj.put("supplier_item_id", order.getSupplierItemId());
                        itemObj.put("item_name", order.getItemName());
                        if (order.getBrandName() != null) {
                            itemObj.put("brand_name", order.getBrandName());
                        } else {
                            itemObj.put("brand_name", "");
                        }
                        itemObj.put("size", order.getSize());
                        itemObj.put("uom", order.getUom());
                        if (order.getShippingWeight() != null) {
                            itemObj.put("shipping_weight", order.getShippingWeight());
                        } else {
                            itemObj.put("shipping_weight", "0");
                        }
                        itemObj.put("is_taxable", order.getIsTaxable());
                        itemObj.put("order_quantity", order.getOrderQuantity());
                        itemObj.put("regular_price", order.getRegularPrice());
                        if (order.getRegularPrice().equalsIgnoreCase(order.getUnitPrice())) {
                            itemObj.put("final_price", "0.00");
                        } else {
                            itemObj.put("final_price", order.getUnitPrice());
                        }

                        itemObj.put("applicable_sale_description", "");
                        String subs = order.getSubstitutionWith();
                        if (subs == null) {
                            itemObj.put("substitution", "1");
                        } else if (subs.equalsIgnoreCase("none")) {
                            itemObj.put("substitution", "1");
                        } else if (subs.equalsIgnoreCase("store's choice")) {
                            itemObj.put("substitution", "2");
                        } else {
                            itemObj.put("substitution", "3");
                        }

                        itemsArray.put(itemObj);
                    }

                    supObj.put("items", itemsArray);
                    suppliersArray.put(supObj);
                }
            }


            orderObj.put("suppliers", suppliersArray);

            subtotal = total;

            double promotionDiscount = 0;
            int freeDelivery = 0;


            if (storePromotionBeanList != null) {
                for (int i = 0; i < storePromotionBeanList.size(); i++) {
                    promotionDiscount += Double.parseDouble(storePromotionBeanList.get(i).getDiscountAmount());
                }
            }

            if (Global.isFreeDelivery) {

                if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                    freeDelivery = 1;
                }
            }

            if (promotionAvailableBean != null) {
                if (promotionAvailableBean.getFreeDeliveryMinOrder() != null) {
                    if (freeDelivery == 0) {
                        if (promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                            if (subtotal >= Double.parseDouble(promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {

                                if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                                    freeDelivery = 1;
                                }
                            }
                        }
                    }

                } else if (promotionAvailableBean.getAmountOff() != null) {
                    double amountOff = Double.parseDouble(promotionAvailableBean.getAmountOff().getDiscountAmntValue());
                    if (!promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                        double min = Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt());
                        if (min > 0) {
                            if (subtotal >= Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt())) {
                                if (promotionDiscount < amountOff) {
                                    promotionDiscount = amountOff;
                                }
                            }
                        } else {
                            if (promotionDiscount < amountOff) {
                                promotionDiscount = amountOff;

                            }
                        }
                    } else {
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;

                        }
                    }


                } else if (promotionAvailableBean.getDeliveryBean() != null) {
                    if (promotionAvailableBean.getDeliveryBean().getFreeDelivery().equalsIgnoreCase("yes")) {
                        if (freeDelivery == 0) {
                            if (promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                                promotionDiscount = Double.parseDouble(Global.estimatedFee);
                                freeDelivery = 1;

                            }
                        }
                    }
                } else if (promotionAvailableBean.getPercentOff() != null) {
                    double discPercent = Double.parseDouble(promotionAvailableBean.getPercentOff().getDiscountAmntPercent());
                    double supTotal = 0;
                    double amountOff = 0;
                    if (promotionAvailableBean.getExcludeSalesItems().equalsIgnoreCase("yes")) {
                        for (int s = 0; s < availalesuppliersBeanList.size(); s++) {
                            SuppliersBean sup = availalesuppliersBeanList.get(s);
                            List<OrderBean> orderBeanList = db.getAllOrders(sup);
                            for (int k = 0; k < orderBeanList.size(); k++) {
                                if (orderBeanList.get(k).getRegularPrice().equalsIgnoreCase(orderBeanList.get(k).getUnitPrice())) {
                                    supTotal += Double.parseDouble(orderBeanList.get(k).getFinalPrice());
                                }
                            }
                        }
                        amountOff = (discPercent / 100) * supTotal;
                        if (!promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                            double min = Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt());
                            if (min > 0) {
                                if (subtotal >= Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt())) {
                                    if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                                        amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                                    }

                                    if (promotionDiscount < amountOff) {
                                        promotionDiscount = amountOff;
                                    }
                                }
                            } else {
                                if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                                    amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                                }

                                if (promotionDiscount < amountOff) {
                                    promotionDiscount = amountOff;
                                }
                            }
                        } else {
                            if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                                amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                            }

                            if (promotionDiscount < amountOff) {
                                promotionDiscount = amountOff;
                            }
                        }


                    }
                }
            }


            if (freeDelivery == 0) {
                if (subtotal > promotionDiscount) {
                    subtotal -= promotionDiscount;
                } else {
                    subtotal = 0.00;
                }
            }


            tax = finalTax;
            total = 0;
            if (availalesuppliersBeanList.size() == 0) {
                estimatedFee = "0";
            }
            if (freeDelivery == 1) {
                promotionDiscount = Double.parseDouble(Global.estimatedFee);
                estimatedFee = "0.00";

                total += subtotal + tax + Double.parseDouble(estimatedFee);
            } else {
                total += subtotal + tax + Double.parseDouble(estimatedFee);
            }

            if (Global.tipBean != null) {
                if (Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                    if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                        String tipPercent = Global.tipBean.getTipAmount();
                        double tipAmount = (total * Double.parseDouble(tipPercent)) / 100;
                        total += tipAmount;
                        orderObj.put("tip_amount", String.format("%.2f", tipAmount));
                        orderObj.put("tip_percent", tipPercent);
                    } else {
                        String amount = Global.tipBean.getTipAmount();
                        total = total + Double.parseDouble(amount);
                        orderObj.put("tip_amount", String.format("%.2f", amount));
                        orderObj.put("tip_percent", "0");
                    }
                } else {
                    orderObj.put("tip_amount", "0");
                    orderObj.put("tip_percent", "0");
                }
            } else {
                orderObj.put("tip_amount", "0");
                orderObj.put("tip_percent", "0");
            }

            orderObj.put("payment_id", paymentId);
            orderObj.put("address_id", addressId);
            orderObj.put("delivery_instruction", orderInsText.getText().toString());
            orderObj.put("delivery_option", pref.getString(Constants.DELIVERY_TYPE, null));
            orderObj.put("delivery_phone_supplied", phoneText.getText().toString());
            if (creditAmount == 0) {
                orderObj.put("user_credit_applied", "NO");
                orderObj.put("credit_amount_applied", "");
            } else {
                orderObj.put("user_credit_applied", "YES");
                orderObj.put("credit_amount_applied", creditAmount);
            }

            orderObj.put("promo_code", "");
            if (promotionDiscount != 0) {
                orderObj.put("promo_amount", promotionDiscount);
            } else {
                orderObj.put("promo_amount", "");
            }

            orderObj.put("sub_total", String.format("%.2f", subtotal));
            orderObj.put("tax", String.format("%.2f", tax));
            if (isCreditsApply) {
                total = total - creditAmount;
            }

            orderObj.put("total_amount", String.format("%.2f", total));


            orderArray.put(orderObj);
            params.put("orders", orderArray);

            Log.d("order params", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/place", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject dataOj = jsonObject.getJSONObject("data");
                                JSONArray ordersArray = dataOj.getJSONArray("orders");
                                JSONObject notiCat = dataOj.optJSONObject("notification");
                                JSONObject notiObj = null;
                                if (notiCat != null) {
                                    notiObj = dataOj.getJSONObject("notification");
                                }
                                DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
                                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                                for (int i = 0; i < suppliersBeanList1.size(); i++) {
                                    SuppliersBean suppliersBean = suppliersBeanList1.get(i);
                                    if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                                        List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                                        for (int j = 0; j < orderBeanList.size(); j++) {
                                            db.deleteOrder(orderBeanList.get(j));
                                        }
                                        db.deleteSupplier(suppliersBean);
                                    }
                                }


                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Constants.DELIVERY_DATE, null);
                                editor.putString(Constants.DELIVERY_FROM, null);
                                editor.putString(Constants.DELIVERY_TO, null);
                                editor.putString(Constants.DELIVERY_TYPE, null);
                                editor.commit();
                                Intent intent = new Intent(CompleteOrderActivity.this, OrderPlacedActivity.class);

                                intent.putExtra("order_id", ordersArray.get(0).toString());
                                if (notiObj != null) {
                                    intent.putExtra("notification_id", notiObj.getString("notification_id"));
                                    intent.putExtra("is_on", notiObj.getString("is_on"));
                                } else {
                                    intent.putExtra("notification_id", "");
                                    intent.putExtra("is_on", "");
                                }

                                Global.locationBean = null;
                                Global.paymentCardBean = null;
                                Global.datePos = -1;
                                Global.slotPos = -1;
                                Global.map = new HashMap<>();
                                Global.tipBean = null;
                                Global.orderInstructions = null;
                                Global.phoneNo = null;
                                Global.isFreeDelivery = false;
                                Global.promotionAvailableBean = null;
                                creditAmount = 0;
                                Global.congestionCost = null;
                                isShowCreditPopup = true;

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                                Global.dialog.dismiss();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Global.dialog.dismiss();
                    showAlert(Constants.NO_INTERNET);

                } else {
                    Global.dialog.dismiss();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void checkDelivery(String addressId, String zipcode) {
        Global.showProgress(CompleteOrderActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();
            List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
            String[] array = new String[suppliersBeanList.size()];
            for (int i = 0; i < suppliersBeanList.size(); i++) {
                array[i] = suppliersBeanList.get(i).getSupplierId();
                supArray.put(suppliersBeanList.get(i).getSupplierId());
            }
            if (zipcode.isEmpty()) {
                params.put("address_id", addressId);
            } else {
                params.put("zipcode", zipcode);
            }
            params.put("suppliers", supArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/delivery/available", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("delivery response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.dialog.dismiss();
                                checkDeliveryBeanList = ConversionHelper.convertCheckDeliveryJsonToCheckDeliveryBean(jsonObject);
                                List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                                if (suppliersBeanList.size() > 1) {
                                    for (int i = 0; i < suppliersBeanList.size(); i++) {
                                        int flag = 0;
                                        CheckDeliveryBean bean = null;
                                        if (checkDeliveryBeanList.size() > 0) {
                                            for (int j = 0; j < checkDeliveryBeanList.size(); j++) {
                                                bean = checkDeliveryBeanList.get(j);
                                                if (bean.getSupplierId().equalsIgnoreCase(suppliersBeanList.get(i).getSupplierId())) {
                                                    flag = 1;
                                                    break;
                                                }
                                            }
                                            if (flag == 1) {
                                                SuppliersBean sup = suppliersBeanList.get(i);
                                                sup.setSupplierAvailable("no");
                                                db.updateSupplier(sup);
                                                suppliersBeanList.get(i).setMessage(bean.getMessage());
                                            } else {
                                                SuppliersBean sup = suppliersBeanList.get(i);
                                                sup.setSupplierAvailable("yes");
                                                db.updateSupplier(sup);
                                            }
                                        } else {
                                            SuppliersBean sup = suppliersBeanList.get(i);
                                            sup.setSupplierAvailable("yes");
                                            db.updateSupplier(sup);
                                        }
                                    }

                                    dateListAdapter = new CustomDeliveryStoresDateListAdapter(CompleteOrderActivity.this, suppliersBeanList);
                                    deliveryStoreDateList.setAdapter(dateListAdapter);
                                    Global.setListViewHeightBasedOnChildren(deliveryStoreDateList);

                                    List<SuppliersBean> suppliersBeanListNew = db.getAllSuppliers();
                                    int flag = 0;
                                    for (int i = 0; i < suppliersBeanListNew.size(); i++) {
                                        if (suppliersBeanListNew.get(i).getSupplierAvailable().equalsIgnoreCase("yes")) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 0) {
                                        placeOrderBtn.setEnabled(false);
                                        placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
                                    }

                                } else if (suppliersBeanList.size() == 1) {
                                    storeNametext.setText(suppliersBeanList.get(0).getSupplierName());
                                    if (checkDeliveryBeanList.size() > 0) {
                                        CheckDeliveryBean bean = checkDeliveryBeanList.get(0);
                                        if (bean.getSupplierId().equalsIgnoreCase(suppliersBeanList.get(0).getSupplierId())) {
                                            SuppliersBean sup = suppliersBeanList.get(0);
                                            sup.setSupplierAvailable("no");
                                            db.updateSupplier(sup);
                                            storeWarningText.setText(bean.getMessage());
                                            deliveryDateListLayout.setVisibility(View.GONE);
                                            dateViewLayout.setVisibility(View.GONE);
                                            deliverySlotsLayout.setVisibility(View.GONE);
                                            storeNametext.setVisibility(View.VISIBLE);
                                            storeWarningText.setVisibility(View.VISIBLE);
                                            showChangeAddressAlert("This store does not deliver in this zip code. Please change your delivery address or buy from another supplier.");
                                            placeOrderBtn.setEnabled(false);
                                            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
                                        }
                                    } else {
                                        SuppliersBean sup = suppliersBeanList.get(0);
                                        sup.setSupplierAvailable("yes");
                                        db.updateSupplier(sup);
                                    }
                                }
                                getAvailableSuppliers();
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("delivery error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Global.dialog.dismiss();
                    showAlert(Constants.NO_INTERNET);

                } else {
                    Global.dialog.dismiss();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    void checkIsPlaceOrderButtonEnabled() {
        final DatabaseHandler db = new DatabaseHandler(CompleteOrderActivity.this);
        availalesuppliersBeanList = null;
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        availalesuppliersBeanList = new ArrayList<>();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            if (suppliersBeanList.get(i).getSupplierAvailable().equalsIgnoreCase("yes")) {
                availalesuppliersBeanList.add(suppliersBeanList.get(i));
            }
        }

        int temp = 0;
        for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
            if (Global.map.get(availalesuppliersBeanList.get(i).getSupplierId()) == null) {
                temp = 1;
                break;
            }
        }


        if (Global.locationBean == null || Global.paymentCardBean == null || Global.map.size() == 0) {
            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
            placeOrderBtn.setEnabled(false);
        } else {
            if (temp == 0) {
                placeOrderBtn.setEnabled(true);
                placeOrderBtn.setBackgroundResource(R.drawable.button_red);
            } else {
                placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
                placeOrderBtn.setEnabled(false);
            }

        }
    }


    private void showAlert(final String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(CompleteOrderActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CompleteOrderActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showChangeAddressAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(CompleteOrderActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CompleteOrderActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button changeAddressBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        changeAddressBtn.setText("Choose Address");
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        changeAddressBtn.setTypeface(andBold);
        titleText.setText(msg);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddress) {
                    Intent intent = new Intent(CompleteOrderActivity.this, SearchAddressActivity.class);
                    startActivity(intent);
                    Global.isCompleteBack = true;
                    Global.previousActivity = DeliveryLocationActivity.class.getName();
                } else {
                    Intent intent = new Intent(CompleteOrderActivity.this, DeliveryLocationActivity.class);
                    startActivity(intent);
                    Global.isCompleteBack = true;
                    Global.previousActivity = DeliveryLocationActivity.class.getName();
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showApplyCredits(String msg, final String credits) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(CompleteOrderActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CompleteOrderActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button changeAddressBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        changeAddressBtn.setText("YES");
        closeBtn.setText("NO");
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        changeAddressBtn.setTypeface(andBold);
        titleText.setText(msg);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreditsApply = false;
                isShowCreditPopup = false;
                dialog.dismiss();
            }
        });
        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreditsApply = true;
                creditAmount = Double.parseDouble(credits);
                finalTax = 0;
                getAvailableSuppliers();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
