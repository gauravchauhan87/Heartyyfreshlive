package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.share.internal.ShareConstants;
import com.heartyy.heartyyfresh.adapter.CustomDateListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomDeliverySlotsListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomDeliveryStoresDateListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomStoresTotalListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomTipListAdapter;
import com.heartyy.heartyyfresh.bean.CheckDeliveryBean;
import com.heartyy.heartyyfresh.bean.DeliveryWindowBean;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.StorePromotionBean;
import com.heartyy.heartyyfresh.bean.StoresTotalPriceBean;
import com.heartyy.heartyyfresh.bean.SupplierDeliveryScheduleBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.bean.TimeIntervalBean;
import com.heartyy.heartyyfresh.bean.TipBean;
import com.heartyy.heartyyfresh.bean.WorkingDaysBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.promotionbean.PromotionAvailableBean;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.HorizontalListView;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import io.card.payment.BuildConfig;

public class CompleteOrderActivity extends AppCompatActivity {
    public static double creditAmount = 0;
    public static boolean isCreditsApply = true;
    public static PromotionAvailableBean promotionAvailableBean;
    ProfileActivity activity;
    CustomStoresTotalListAdapter adapter;
    private Button addAddressBtn,addCardBtn;
    private ImageView addressIcon;
    private RelativeLayout addressLayout;
    TextView addressText,addressText2,addressTypeText;
    Typeface andBold;
    private Button applyPromoBtn;
    List<SuppliersBean> availalesuppliersBeanList;
    Typeface bold;
    private Button buttonaddcoupon;
    private Button buttonaddtip;
    ImageView cardIcon;
    TextView cardNumberText;
    private List<CheckDeliveryBean> checkDeliveryBeanList;
    TextView couponCodeText;
    private boolean couponError = false;
    TextView creditAmountText;
    TextView creditText;
    private CustomDateListAdapter dateAdapter;
    private CustomDeliveryStoresDateListAdapter dateListAdapter;
    private HorizontalListView dateListView;
    private RelativeLayout dateViewLayout;
    private RelativeLayout deliveryDateListLayout;
    private HorizontalListView deliveryListView;
    private TextView deliveryPriceText;
    private RelativeLayout deliverySlotsLayout;
    private ListView deliveryStoreDateList;
    private DeliveryWindowBean deliveryWindowBean;
    private CustomDeliverySlotsListAdapter dladapter;
    private RelativeLayout editCouponsLayout;
    private EditText editOrderInst;
    private EditText editPhone;
    private EditText editcoupon;
    private String estimatedFee;
    private double finalTax = 0.0d;
    private String finalTaxPercent;
    private String finalTotal;
    private boolean isAddress = false;
    private boolean isCard = false;
    public boolean isCouponApplied = false;
    private boolean isDelivery = true;
    private boolean isShowCreditPopup = true;
    Typeface italic;
    Typeface light;
    ImageView lineImage;
    ScrollView mainScrollView;
    TextView orderInsText;
    private RelativeLayout orderInstructionsLayout;
    TextView paymentTypeText;
    private RelativeLayout phoneLayout;
    TextView phoneText;
    Button placeOrderBtn;
    private SharedPreferences pref;
    private TextView promoText;
    private List<PromotionAvailableBean> promotionAvailableBeanList;
    double promotionDiscount = 0.0d;
    private TextView promotionNote;
    private TextView returnPolicyText;
    private Button saveOrderInsBtn;
    private Button savePhoneBtn;
    private TextView selectedTipAmount;
    private boolean singleStoreDeliveryApplied = false;
    private TextView storeNametext;
    private List<StorePromotionBean> storePromotionBeanList;
    private TextView storeWarningText;
    ListView storesListView;
    List<StoresTotalPriceBean> storesTotalPriceBeanList;
    private TextView taxPriceText,termsOfText,textCreditAmtDecimal,textCreditAmtDollor,textDeliveryFeePriceDecimal,textDeliveryFeePriceDollor;
    private TextView textPromotionAmount,textPromotionAmtDecimal,textPromotionAmtDollor,textTaxPriceDecimal,textTaxPriceDollor;
    private TextView textTipTotalPriceDecimal,textTipTotalPriceDollor,textTotalAmtDecimal,textTotalAmtDollor,textYourTotal;
    private TextView textchargeinfo,textcoupon,textcouponwarn,textdeliverTo,textdeliverby,textdeliveryfee,textoptional,textorderInst,textpaymentmethod;
    private TextView textphone,texttax,texttip,texttiptotal,texttotal;
    private Button tipApplyBtn;
    private TipBean tipBean;
    private List<TipBean> tipBeanList;
    private HorizontalListView tipList;
    private CustomTipListAdapter tipListAdapter;
    private RelativeLayout tipListLayout;
    private TextView tipNote;
    private TextView tipPriceText;
    double totalAmount;
    private TextView totalAmountText;
    private ImageView view10;

    protected void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        final SpannableString spannableString = new SpannableString(getResources().getString(R.string.title_activity_checkout));
        spannableString.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Global.setFont((ViewGroup) findViewById(R.id.layout_return_policy), light);
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
        textCreditAmtDecimal = (TextView) findViewById(R.id.text_credit_amt_decimal);
        textTaxPriceDecimal = (TextView) findViewById(R.id.text_tax_price_decimal);
        mainScrollView = (ScrollView) findViewById(R.id.main_scroll_view);
        textDeliveryFeePriceDecimal = (TextView) findViewById(R.id.text_delivery_fee_price_decimal);
        textTipTotalPriceDecimal = (TextView) findViewById(R.id.text_tip_total_price_decimal);
        textTotalAmtDecimal = (TextView) findViewById(R.id.text_total_amt_decimal);
        textTotalAmtDollor = (TextView) findViewById(R.id.text_total_amt_dollor);
        textCreditAmtDollor = (TextView) findViewById(R.id.text_credit_amt_dollor);
        textTipTotalPriceDollor = (TextView) findViewById(R.id.text_tip_total_price_dollor);
        textDeliveryFeePriceDollor = (TextView) findViewById(R.id.text_delivery_fee_price_dollor);
        textTaxPriceDollor = (TextView) findViewById(R.id.text_tax_price_dollor);
        textPromotionAmtDollor = (TextView) findViewById(R.id.text_promotion_amt_dollor);
        textPromotionAmtDecimal = (TextView) findViewById(R.id.text_promotion_amt_decimal);

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
        textTaxPriceDecimal.setTypeface(light);
        textDeliveryFeePriceDecimal.setTypeface(light);
        textTotalAmtDecimal.setTypeface(light);
        texttiptotal.setTypeface(light);
        tipPriceText.setTypeface(light);
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
        textTaxPriceDollor.setTypeface(light);
        textCreditAmtDollor.setTypeface(light);
        textTotalAmtDollor.setTypeface(light);
        textPromotionAmtDollor.setTypeface(light);
        textPromotionAmtDecimal.setTypeface(light);
        textDeliveryFeePriceDollor.setTypeface(light);
        textTipTotalPriceDecimal.setTypeface(light);
        textTipTotalPriceDollor.setTypeface(light);
        totalAmountText.setTypeface(andBold);
        textTotalAmtDecimal.setTypeface(andBold);
        textCreditAmtDecimal.setTypeface(andBold);

        final DatabaseHandler db = new DatabaseHandler(this);
        SpannableString content = new SpannableString(" return policy ");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        this.returnPolicyText.setText(content);
        SpannableString content1 = new SpannableString("Terms of Services");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        termsOfText.setText(content1);

        tipBeanList = new ArrayList();
        tipBeanList.add(new TipBean("No Tip", AppEventsConstants.EVENT_PARAM_VALUE_NO, "no", "yes"));
        tipBeanList.add(new TipBean("5%", "5", "yes", "no"));
        tipBeanList.add(new TipBean("10%", "10", "yes", "no"));
        tipBeanList.add(new TipBean("15%", "15", "yes", "no"));
        tipBeanList.add(new TipBean("$0.00", AppEventsConstants.EVENT_PARAM_VALUE_NO, "no", "no"));

        tipListAdapter = new CustomTipListAdapter(this, tipBeanList);
        tipList.setAdapter(tipListAdapter);
        tipList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipBean = (TipBean) parent.getItemAtPosition(position);
                tipListAdapter.setSelected(position);
            }
        });
        buttonaddcoupon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                couponCodeText.setVisibility(View.GONE);
                editCouponsLayout.setVisibility(View.VISIBLE);
            }
        });
        tipApplyBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Global.tipBean = null;
                Global.tipBean = tipBean;
                if (Global.tipBean == null) {
                    showAlert("Please select a tip amount");
                    return;
                }
                view10.setVisibility(View.GONE);
                selectedTipAmount.setVisibility(View.VISIBLE);
                buttonaddtip.setText("CHANGE");
                isDelivery = false;
                if (!Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                    tipNote.setText("(" + Global.tipBean.getTipAmount() + "% selected)");
                    tipNote.setVisibility(View.GONE);
                    selectedTipAmount.setVisibility(View.GONE);
                } else if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                    tipNote.setVisibility(View.VISIBLE);
                    tipNote.setText("(" + Global.tipBean.getTipAmount() + "% selected)");
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
        });
        buttonaddtip.setOnClickListener(new OnClickListener() {
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
            if (!Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                tipNote.setVisibility(View.GONE);
            } else if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                tipNote.setText("(" + Global.tipBean.getTipAmount() + "% selected)");
            } else {
                tipNote.setVisibility(View.GONE);
            }
            this.buttonaddtip.setText("CHANGE");
        }
        if (Global.locationBean == null) {
            List<LocationBean> locationBeanList = db.getAllDeliveryaddress();
            for (i = 0; i < locationBeanList.size(); i++) {
                LocationBean locationBean = (LocationBean) locationBeanList.get(i);
                if (locationBean.getIsPrimaryLocation().equalsIgnoreCase("yes")) {
                    Global.locationBean = locationBean;
                    break;
                }
            }
        }
        if (db.getAllSuppliers().size() > 1) {
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
            checkDelivery(Global.locationBean.getUserDeliveryLocationId(), BuildConfig.VERSION_NAME);
        } else {
            checkDelivery(BuildConfig.VERSION_NAME, Global.zip);
        }
        applyPromoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String promo = editcoupon.getText().toString();
                if (promo.isEmpty()) {
                    showAlert("Please enter a valid Promo code");
                } else {
                    applyPromo(promo);
                }
            }
        });
        returnPolicyText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String suppId = BuildConfig.VERSION_NAME;
                List<SuppliersBean> suppliersBeanList = new DatabaseHandler(CompleteOrderActivity.this).getAllSuppliers();
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    suppId += suppliersBeanList.get(i).getSupplierId() + ",";
                }
                suppId = suppId.substring(0, suppId.length() - 1);
                Intent intent = new Intent(CompleteOrderActivity.this, ReturnPolicyActivity.class);
                intent.putExtra("sup", suppId);
                CompleteOrderActivity.this.startActivity(intent);
                Global.previousActivity = getClass().getName();
                return false;
            }
        });
        termsOfText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "tnc");
                startActivity(intent);
                Global.previousActivity = getClass().getName();
                return false;
            }
        });
        phoneText.setText(BuildConfig.VERSION_NAME);
        taxPriceText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
        textTaxPriceDecimal.setText("00");
        tipPriceText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
        textTipTotalPriceDecimal.setText("00");
        if (estimatedFee.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
            deliveryPriceText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            textDeliveryFeePriceDecimal.setText("00");
        } else {
            String[] delTemp = estimatedFee.split("\\.");
            if (delTemp.length > 1) {
                deliveryPriceText.setText(delTemp[0]);
                textDeliveryFeePriceDecimal.setText(delTemp[1]);
            } else {
                deliveryPriceText.setText(delTemp[0]);
                textDeliveryFeePriceDecimal.setText("00");
            }
        }
        totalAmountText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
        textTotalAmtDecimal.setText("00");
        if (Global.locationBean != null) {
            isAddress = true;
            if (Global.locationBean.getLocationName() != null) {
                if (Global.locationBean.getLocationName().length() > 0) {
                    addressTypeText.setText(Global.locationBean.getLocationName());
                } else if (Global.locationBean.getLocationType().equalsIgnoreCase("home")) {
                    addressTypeText.setText("Home");
                    addressIcon.setImageResource(R.drawable.home_icon);
                } else {
                    addressTypeText.setText("work");
                    addressIcon.setImageResource(R.drawable.work_icon);
                }
            } else if (Global.locationBean.getLocationType().equalsIgnoreCase("home")) {
                addressTypeText.setText("Home");
                addressIcon.setImageResource(R.drawable.home_icon);
            } else {
                addressTypeText.setText("work");
                addressIcon.setImageResource(R.drawable.work_icon);
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
            addressText.setText(Global.FormatAddress(address));
            addressText2.setText(Global.FormatAddress(address2));
            addressLayout.setVisibility(View.VISIBLE);
            orderInstructionsLayout.setVisibility(View.VISIBLE);
            phoneLayout.setVisibility(View.VISIBLE);
            lineImage.setVisibility(View.VISIBLE);
            findViewById(R.id.view1).setVisibility(View.VISIBLE);
            findViewById(R.id.view2).setVisibility(View.VISIBLE);
            addAddressBtn.setText("CHANGE ADDRESS");
            if (!Global.locationBean.getDeliveryInstructions().equalsIgnoreCase("null") && Global.locationBean.getDeliveryInstructions().length() > 0 && Global.orderInstructions == null) {
                Global.orderInstructions = Global.locationBean.getDeliveryInstructions();
            }
        } else {
            addressLayout.setVisibility(View.GONE);
            orderInstructionsLayout.setVisibility(View.GONE);
            phoneLayout.setVisibility(View.GONE);
            lineImage.setVisibility(View.GONE);
            findViewById(R.id.view1).setVisibility(View.GONE);
            findViewById(R.id.view2).setVisibility(View.GONE);
            addAddressBtn.setText("ADD ADDRESS");
            orderInsText.setText(BuildConfig.VERSION_NAME);
            double total = 0.0d;
            List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
            for (int k = 0; k < suppliersBeanList1.size(); k++) {
                List<OrderBean> orderBeanList = db.getAllOrders((SuppliersBean) suppliersBeanList1.get(k));
                for (i = 0; i < orderBeanList.size(); i++) {
                    total += Double.parseDouble(((OrderBean) orderBeanList.get(i)).getFinalPrice());
                }
            }
            String[] totalTemp = String.format("%.2f", new Object[]{Double.valueOf(0.0d + total)}).split("\\.");
            if (totalTemp.length > 1) {
                totalAmountText.setText(totalTemp[0]);
                textTotalAmtDecimal.setText(totalTemp[1]);
            } else {
                totalAmountText.setText(totalTemp[0]);
                textTotalAmtDecimal.setText("00");
            }
        }
        if (Global.orderInstructions == null || Global.orderInstructions.length() <= 0) {
            orderInsText.setVisibility(View.GONE);
            editOrderInst.setVisibility(View.VISIBLE);
            saveOrderInsBtn.setVisibility(View.VISIBLE);
            orderInsText.setText(BuildConfig.VERSION_NAME);
        } else {
            orderInsText.setVisibility(View.VISIBLE);
            orderInsText.setText(Global.orderInstructions);
            editOrderInst.setVisibility(View.GONE);
            saveOrderInsBtn.setVisibility(View.GONE);
        }
        if (Global.phoneNo == null || Global.phoneNo.length() <= 0) {
            phoneText.setVisibility(View.GONE);
            phoneText.setText(BuildConfig.VERSION_NAME);
            editPhone.setVisibility(View.VISIBLE);
            savePhoneBtn.setVisibility(View.VISIBLE);
        } else {
            phoneText.setVisibility(View.VISIBLE);
            phoneText.setText(Global.phoneNo);
            editPhone.setVisibility(View.GONE);
            savePhoneBtn.setVisibility(View.GONE);
        }
        if (Global.paymentCardBean == null) {
            List<PaymentCardBean> paymentCardBeanList = db.getAllPaymentCards();
            for (i = 0; i < paymentCardBeanList.size(); i++) {
                if ((paymentCardBeanList.get(i)).getIsPrimary().equalsIgnoreCase("yes")) {
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
            if (Global.paymentCardBean.getCardLastFourDigit().equalsIgnoreCase("null")) {
                cardIcon.setImageResource(R.drawable.paypal_icon);
                cardNumberText.setVisibility(View.GONE);
            } else {
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
            }
            addCardBtn.setText("CHANGE CARD");
            isCard = true;
        } else {
            paymentTypeText.setVisibility(View.GONE);
            cardNumberText.setVisibility(View.GONE);
            cardIcon.setVisibility(View.GONE);
            addCardBtn.setText("ADD CARD");
        }
        addCardBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(CompleteOrderActivity.this, PaymentActivity.class));
                Global.isCompleteBack = true;
            }
        });
        savePhoneBtn.setOnClickListener(new OnClickListener() {
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
                    Editor editor = pref.edit();
                    editor.putString(Constants.PHONE, phone);
                    editor.apply();
                    Global.phoneNo = phone;
                }
            }
        });
        addAddressBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (isAddress) {
                    creditAmount = 0;
                    isCreditsApply = true;
                    isShowCreditPopup = true;
                    startActivity(new Intent(CompleteOrderActivity.this, DeliveryLocationActivity.class));
                    Global.isCompleteBack = true;
                    Global.previousActivity = DeliveryLocationActivity.class.getName();
                    return;
                }
                creditAmount = 0;
                isCreditsApply = true;
                isShowCreditPopup = true;
                startActivity(new Intent(CompleteOrderActivity.this, SearchAddressActivity.class));
                Global.isCompleteBack = true;
                Global.previousActivity = DeliveryLocationActivity.class.getName();
            }
        });
        orderInstructionsLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                orderInsText.setVisibility(View.GONE);
                editOrderInst.setVisibility(View.VISIBLE);
                saveOrderInsBtn.setVisibility(View.VISIBLE);
            }
        });
        phoneLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                phoneText.setVisibility(View.GONE);
                editPhone.setVisibility(View.VISIBLE);
                savePhoneBtn.setVisibility(View.VISIBLE);
            }
        });
        saveOrderInsBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String inst = editOrderInst.getText().toString();
                if (inst.isEmpty()) {
                    showAlert("Please add order instructions");
                    return;
                }
                orderInsText.setVisibility(View.VISIBLE);
                editOrderInst.setVisibility(View.GONE);
                saveOrderInsBtn.setVisibility(View.GONE);
                orderInsText.setText(inst);
                Global.orderInstructions = inst;
            }
        });
        dateListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OperatingHourBean operatingHourBean = (OperatingHourBean) adapterView.getItemAtPosition(i);
                adapterView.setSelection(i);
                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                if (operatingHourBean.getIsClose().equalsIgnoreCase("no")) {
                    isCouponApplied = false;
                    dateAdapter.setSelected(i);
                    List<TimeIntervalBean> timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                    dladapter.changeSlot(timeIntervalBeanList);
                    SupplierDeliveryScheduleBean deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                    deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                    deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                    Global.dayOfWeek = operatingHourBean.getDayOfWeek();
                    Global.promotionDeliveryDates = new ArrayList();
                    Global.promotionDeliveryDates.add(operatingHourBean.getDate());
                    deliveryScheduleBean.setDeliveryFrom((timeIntervalBeanList.get(0)).getStartTime());
                    deliveryScheduleBean.setDeliveryTo((timeIntervalBeanList.get(0)).getEndTime());
                    deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                    deliveryScheduleBean.setTimeIntervalBean(operatingHourBean.getTimeIntervalBeanList().get(0));
                    Global.map.put((suppliersBeanList1.get(0)).getSupplierId(), deliveryScheduleBean);
                    Global.map.get((suppliersBeanList1.get(0)).getSupplierId());
                    Global.datePos = i;
                    Global.slotPos = 0;
                    Global.timeIntervalBean = timeIntervalBeanList.get(0);
                    Global.operatingHourBean = operatingHourBean;
                    if (Integer.parseInt(operatingHourBean.getTimeIntervalBeanList().get(Global.slotPos).getStartTime().split(" ")[1].split(":")[0]) >= 18) {
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
        deliveryListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeIntervalBean timeIntervalBean = (TimeIntervalBean) adapterView.getItemAtPosition(i);
                dladapter.setSelected(i);
                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                Editor editor = pref.edit();
                SupplierDeliveryScheduleBean deliveryScheduleBean = Global.map.get(suppliersBeanList1.get(0).getSupplierId());
                deliveryScheduleBean.setDeliveryFrom(timeIntervalBean.getStartTime());
                deliveryScheduleBean.setDeliveryTo(timeIntervalBean.getEndTime());
                Global.map.put(suppliersBeanList1.get(0).getSupplierId(), deliveryScheduleBean);
                Global.timeIntervalBean = timeIntervalBean;
                Global.slotPos = i;
                if (Integer.parseInt(timeIntervalBean.getStartTime().split(" ")[1].split(":")[0]) >= 18) {
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
        placeOrderBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String addressId = null;
                String paymentId = null;
                LocationBean location = Global.locationBean;
                PaymentCardBean card = Global.paymentCardBean;
                if (location != null) {
                    String taxPercent = location.getTaxRate();
                    addressId = location.getUserDeliveryLocationId();
                }
                if (card != null) {
                    paymentId = card.getUserPaymentMethodId();
                }
                if (Global.phoneNo == null) {
                    showAlert("Please provide phone number");
                    mainScrollView.fullScroll(33);
                    editPhone.requestFocus();
                    return;
                }
                int temp = 0;
                String supplierName = null;
                for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
                    if (Global.map.get((availalesuppliersBeanList.get(i)).getSupplierId()) == null) {
                        temp = 1;
                        supplierName = availalesuppliersBeanList.get(i).getSupplierName();
                        break;
                    }
                }
                if (temp == 0) {
                    singleStoreDeliveryApplied = true;
                    calculateTotal();
                    placeOrder(addressId, paymentId);
                    return;
                }
                showAlert("Please select your delivery time for " + supplierName);
            }
        });
        checkIsPlaceOrderButtonEnabled();
    }

    private void applyPromo(String promo) {
        String zip;
        int i;
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        DatabaseHandler db = new DatabaseHandler(this);
        if (Global.locationBean != null) {
            zip = Global.locationBean.getZipcode();
        } else {
            zip = Global.zip;
        }
        String deliveryDates = BuildConfig.VERSION_NAME;
        if (db.getAllSuppliers().size() > 1) {
            if (Global.isIndividualDelivery) {
                Global.promotionDeliveryDates = new ArrayList();
                for (i = 0; i < this.availalesuppliersBeanList.size(); i++) {
                    if (Global.map.get((availalesuppliersBeanList.get(i)).getSupplierId()) != null) {
                        Global.promotionDeliveryDates.add((Global.map.get((availalesuppliersBeanList.get(i)).getSupplierId())).getDeliveryDate());
                    }
                }
            }
            if (Global.promotionDeliveryDates.size() < 1) {
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                Date dateObj = new Date();
                Global.promotionDeliveryDates = new ArrayList();
                Global.promotionDeliveryDates.add(date.format(dateObj));
            }
        }
        for (i = 0; i < Global.promotionDeliveryDates.size(); i++) {
            deliveryDates = deliveryDates + "," + ((String) Global.promotionDeliveryDates.get(i));
        }
        String url = "apply/code?code=" + promo + "&user_id=" + this.pref.getString(Constants.USER_ID, null) + "&zipcode=" + zip + "&deliverydate=" + deliveryDates.substring(1, deliveryDates.length());
        Log.d("promoURL", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(0, Constants.URL + url, null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("coupon code response", jsonObject.toString());
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        promotionAvailableBeanList = ConversionHelper.convertPromotionAvailableJsonToPromotionAvailableBean(jsonObject);
                        if (promotionAvailableBeanList != null) {
                            promotionAvailableBean = promotionAvailableBeanList.get(0);
                            if (promotionAvailableBean.getMultipleDiscountApplies().equalsIgnoreCase("yes")) {
                                promotionDiscount = 0;
                                finalTax = 0;
                                isCouponApplied = true;
                                calculateTotal();
                                return;
                            }
                            promotionDiscount = 0;
                            finalTax = 0;
                            isCouponApplied = true;
                            calculateTotal();
                            return;
                        }
                        promotionAvailableBean = null;
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.ERROR, new Object[]{"Error: " + error.toString()});
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
    }

    private void getAvailableSuppliers() {
        DatabaseHandler db = new DatabaseHandler(this);
        availalesuppliersBeanList = null;
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        this.availalesuppliersBeanList = new ArrayList();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            if ((suppliersBeanList.get(i)).getSupplierAvailable().equalsIgnoreCase("yes")) {
                this.availalesuppliersBeanList.add(suppliersBeanList.get(i));
            }
        }
        if (this.availalesuppliersBeanList.size() > 0) {
            checkStorePromotion();
            return;
        }
        ((ImageView) findViewById(R.id.view9)).setVisibility(View.GONE);
        getDeliveryTimes();
    }

    private void checkStorePromotion() {
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(this);
        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();
            for (int i = 0; i < this.availalesuppliersBeanList.size(); i++) {
                SuppliersBean suppliersBean = availalesuppliersBeanList.get(i);
                if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                    JSONObject supObj = new JSONObject();
                    supObj.put(Constants.SUPPLIER_ID, suppliersBean.getSupplierId());
                    double price = 0;
                    double salesPrice = 0;
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                    for (int j = 0; j < orderBeanList.size(); j++) {
                        price += Double.parseDouble(orderBeanList.get(j).getFinalPrice());
                        if (!orderBeanList.get(j).getRegularPrice().equalsIgnoreCase(orderBeanList.get(j).getUnitPrice())) {
                            salesPrice += Double.parseDouble(orderBeanList.get(j).getFinalPrice());
                        }
                    }
                    supObj.put("sub_total", String.format("%.2f", new Object[]{Double.valueOf(price)}));
                    supObj.put("sales_item_total", String.format("%.2f", new Object[]{Double.valueOf(salesPrice)}));
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
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, "https://api.heartyyfresh.com/api/v2/supplier/promotion/available", params, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("promotion response", jsonObject.toString());
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        storePromotionBeanList = ConversionHelper.convertStorePromotionJsonToStorePromotionBean(jsonObject);
                        storesTotalPriceBeanList = new ArrayList();
                        if (availalesuppliersBeanList != null) {
                            for (int i = 0; i < availalesuppliersBeanList.size(); i++) {
                                StoresTotalPriceBean store = new StoresTotalPriceBean();
                                SuppliersBean suppliersBean = availalesuppliersBeanList.get(i);
                                if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                                    store.setStoreName(suppliersBean.getSupplierName());
                                    store.setIsAvailable(suppliersBean.getSupplierAvailable());
                                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                                    double total = 0;
                                    for (int j = 0; j < orderBeanList.size(); j++) {
                                        total += Double.parseDouble(((OrderBean) orderBeanList.get(j)).getFinalPrice());
                                    }
                                    store.setStorePrice(String.valueOf(total));
                                    storesTotalPriceBeanList.add(store);
                                }
                            }
                            if (storesTotalPriceBeanList.size() == 0) {
                                ((ImageView) CompleteOrderActivity.this.findViewById(R.id.view9)).setVisibility(View.GONE);
                            }
                            adapter = new CustomStoresTotalListAdapter(CompleteOrderActivity.this, storesTotalPriceBeanList, CompleteOrderActivity.this);
                            storesListView.setAdapter(adapter);
                            Global.setListViewHeightBasedOnChildren(storesListView);
                        }
                        getDeliveryTimes();
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                       Global.hideProgress();
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.ERROR, new Object[]{"Error: " + error.toString()});
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);
                    return;
                }
               Global.hideProgress();
                showAlert(Constants.REQUEST_TIMED_OUT);
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
    }

    private void getDeliveryTimes() {
        TimeZone tz = TimeZone.getDefault();
        Log.d("TimeZone   " , tz.getDisplayName(false, 0) + " Timezon id :: " + tz.getID());
        final DatabaseHandler db = new DatabaseHandler(this);
        String supplierId = BuildConfig.VERSION_NAME;
        String shippingWeight = BuildConfig.VERSION_NAME;
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean suppliersBean = (SuppliersBean) suppliersBeanList.get(i);
            if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                supplierId = supplierId + suppliersBean.getSupplierId() + ",";
                double totalweight = 0.0d;
                List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                int j = 0;
                while (j < orderBeanList.size()) {
                    if (!((orderBeanList.get(j).getShippingWeight() == null || orderBeanList.get(j).getShippingWeight().equalsIgnoreCase("null")))) {
                        totalweight += Double.parseDouble(orderBeanList.get(j).getShippingWeight()) * Double.parseDouble(orderBeanList.get(j).getOrderQuantity());
                    }
                    j++;
                }
                shippingWeight = shippingWeight + String.valueOf(totalweight) + ",";
            }
        }
        if (supplierId.length() > 0) {
            supplierId = supplierId.substring(0, supplierId.length() - 1);
        }
        String addressId = BuildConfig.VERSION_NAME;
        if (Global.locationBean != null) {
            addressId = Global.locationBean.getUserDeliveryLocationId();
        }
        String url = "store/operation/window?supplier_id=" + supplierId + "&timezone=" + tz.getID() + "&user_id=" + this.pref.getString(Constants.USER_ID, null) + "&zipcode=" + Global.zip + "&shipping_weight=" + shippingWeight + "&address_id=" + addressId;
        Log.d("delivery url..", Constants.URL + url);
        RequestQueue rq = Volley.newRequestQueue(this);
        final String finalSupplierId = supplierId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(0, Constants.URL + url, null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
               Global.hideProgress();
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        deliveryWindowBean = ConversionHelper.convertDeliveryJSonToDeliveryBean(jsonObject);
                        if (db.getSuppliersCount() == 1) {
                            List<OperatingHourBean> operatingHourBeanList;
                            OperatingHourBean operatingHourBean;
                            int i;
                            int pos;
                            List<SuppliersBean> suppliersBeanList1;
                            SupplierDeliveryScheduleBean deliveryScheduleBean;
                            List<TimeIntervalBean> timeIntervalBeanList;
                            Editor editor;
                            if (deliveryWindowBean.getSingleDeliveryBean() != null) {
                                estimatedFee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                                Global.estimatedFee = estimatedFee;
                                Global.congestionCost = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();
                                operatingHourBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList();
                                for (i = 0; i < operatingHourBeanList.size(); i++) {
                                    operatingHourBean = (OperatingHourBean) operatingHourBeanList.get(i);
                                    if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                                        pos = i;
                                        if (Global.datePos == -1) {
                                            Global.datePos = i;
                                            Global.slotPos = 0;
                                            suppliersBeanList1 = db.getAllSuppliers();
                                            deliveryScheduleBean = Global.map.get(suppliersBeanList1.get(0).getSupplierId());
                                            if (deliveryScheduleBean == null) {
                                                deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                            }
                                            deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                                            Global.dayOfWeek = operatingHourBean.getDayOfWeek();
                                            Global.promotionDeliveryDates = new ArrayList();
                                            Global.promotionDeliveryDates.add(operatingHourBean.getDate());
                                            deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                                            deliveryScheduleBean.setDeliveryFrom(operatingHourBean.getTimeIntervalBeanList().get(0).getStartTime());
                                            deliveryScheduleBean.setDeliveryTo(operatingHourBean.getTimeIntervalBeanList().get(0).getEndTime());
                                            deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                                            deliveryScheduleBean.setTimeIntervalBean(operatingHourBean.getTimeIntervalBeanList().get(0));
                                            if (Integer.parseInt(((TimeIntervalBean) ((OperatingHourBean) ((WorkingDaysBean) CompleteOrderActivity.this.deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0)).getOperatingHourBeanList().get(Global.datePos)).getTimeIntervalBeanList().get(Global.slotPos)).getStartTime().split(" ")[1].split(":")[0]) >= 18) {
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
                                            Global.map.put(((SuppliersBean) suppliersBeanList1.get(0)).getSupplierId(), deliveryScheduleBean);
                                            Global.operatingHourBean = operatingHourBean;
                                            timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                                            checkIsPlaceOrderButtonEnabled();
                                            dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                            dateListView.setAdapter(dateAdapter);
                                            dateAdapter.setSelected(Global.datePos);
                                            dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, Global.operatingHourBean.getTimeIntervalBeanList());
                                            deliveryListView.setAdapter(dladapter);
                                            dladapter.setSelected(Global.slotPos);
                                            editor = pref.edit();
                                            editor.putString(Constants.DELIVERY_DATE, Global.operatingHourBean.getDate());
                                            editor.putString(Constants.DELIVERY_FROM, ((TimeIntervalBean) Global.operatingHourBean.getTimeIntervalBeanList().get(0)).getStartTime());
                                            editor.putString(Constants.DELIVERY_TO, ((TimeIntervalBean) Global.operatingHourBean.getTimeIntervalBeanList().get(0)).getEndTime());
                                            editor.apply();
                                        }
                                    }
                                }
                                CompleteOrderActivity.this.dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                CompleteOrderActivity.this.dateListView.setAdapter(CompleteOrderActivity.this.dateAdapter);
                                CompleteOrderActivity.this.dateAdapter.setSelected(Global.datePos);
                                CompleteOrderActivity.this.dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, Global.operatingHourBean.getTimeIntervalBeanList());
                                CompleteOrderActivity.this.deliveryListView.setAdapter(CompleteOrderActivity.this.dladapter);
                                CompleteOrderActivity.this.dladapter.setSelected(Global.slotPos);
                                editor = CompleteOrderActivity.this.pref.edit();
                                editor.putString(Constants.DELIVERY_DATE, Global.operatingHourBean.getDate());
                                editor.putString(Constants.DELIVERY_FROM, ((TimeIntervalBean) Global.operatingHourBean.getTimeIntervalBeanList().get(0)).getStartTime());
                                editor.putString(Constants.DELIVERY_TO, ((TimeIntervalBean) Global.operatingHourBean.getTimeIntervalBeanList().get(0)).getEndTime());
                                editor.commit();
                            } else {
                                timeIntervalBeanList = null;
                                operatingHourBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList();
                                pos = -1;
                                i = 0;
                                while (i < operatingHourBeanList.size()) {
                                    operatingHourBean = (OperatingHourBean) operatingHourBeanList.get(i);
                                    if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                                        pos = i;
                                        editor = CompleteOrderActivity.this.pref.edit();
                                        editor.putString(Constants.DELIVERY_DATE, operatingHourBean.getDate());
                                        editor.commit();
                                        if (db.getSuppliersCount() == 1) {
                                            suppliersBeanList1 = db.getAllSuppliers();
                                            if (Global.map.get(pref.getString(Constants.SUPPLIER_ID, null)) == null) {
                                                deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                                deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                                                deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                                                Global.map.put(((SuppliersBean) suppliersBeanList1.get(0)).getSupplierId(), deliveryScheduleBean);
                                            }
                                        }
                                        Global.operatingHourBean = operatingHourBean;
                                        timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                                        dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                        dateListView.setAdapter(dateAdapter);
                                        dateAdapter.setSelected(pos);
                                        dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, timeIntervalBeanList);
                                        deliveryListView.setAdapter(dladapter);
                                        dladapter.setSelected(0);
                                        editor = pref.edit();
                                        editor.putString(Constants.DELIVERY_FROM, timeIntervalBeanList.get(0).getStartTime());
                                        editor.putString(Constants.DELIVERY_TO, timeIntervalBeanList.get(0).getEndTime());
                                        editor.commit();
                                        if (db.getSuppliersCount() == 1) {
                                            suppliersBeanList1 = db.getAllSuppliers();
                                            deliveryScheduleBean = (SupplierDeliveryScheduleBean) Global.map.get(pref.getString(Constants.SUPPLIER_ID, null));
                                            if (deliveryScheduleBean == null) {
                                                deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                            }
                                            deliveryScheduleBean.setDeliveryFrom(((TimeIntervalBean) timeIntervalBeanList.get(0)).getStartTime());
                                            deliveryScheduleBean.setDeliveryTo(((TimeIntervalBean) timeIntervalBeanList.get(0)).getEndTime());
                                            deliveryScheduleBean.setDeliveryDayOfWeek(((OperatingHourBean) operatingHourBeanList.get(i)).getDayOfWeek());
                                            Global.map.put(((SuppliersBean) suppliersBeanList1.get(0)).getSupplierId(), deliveryScheduleBean);
                                        }
                                        Global.timeIntervalBean = (TimeIntervalBean) timeIntervalBeanList.get(0);
                                    } else {
                                        i++;
                                    }
                                }
                                dateAdapter = new CustomDateListAdapter(CompleteOrderActivity.this, operatingHourBeanList);
                                dateListView.setAdapter(dateAdapter);
                                dateAdapter.setSelected(pos);
                                dladapter = new CustomDeliverySlotsListAdapter(CompleteOrderActivity.this, timeIntervalBeanList);
                                deliveryListView.setAdapter(dladapter);
                                dladapter.setSelected(0);
                                editor = pref.edit();
                                editor.putString(Constants.DELIVERY_FROM, timeIntervalBeanList.get(0).getStartTime());
                                editor.putString(Constants.DELIVERY_TO, timeIntervalBeanList.get(0).getEndTime());
                                editor.commit();
                                if (db.getSuppliersCount() == 1) {
                                    suppliersBeanList1 = db.getAllSuppliers();
                                    deliveryScheduleBean = Global.map.get(pref.getString(Constants.SUPPLIER_ID, null));
                                    if (deliveryScheduleBean == null) {
                                        deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                                    }
                                    deliveryScheduleBean.setDeliveryFrom(timeIntervalBeanList.get(0).getStartTime());
                                    deliveryScheduleBean.setDeliveryTo(((TimeIntervalBean) timeIntervalBeanList.get(0)).getEndTime());
                                    deliveryScheduleBean.setDeliveryDayOfWeek(((OperatingHourBean) operatingHourBeanList.get(i)).getDayOfWeek());
                                    Global.map.put(((SuppliersBean) suppliersBeanList1.get(0)).getSupplierId(), deliveryScheduleBean);
                                }
                                Global.timeIntervalBean = (TimeIntervalBean) timeIntervalBeanList.get(0);
                            }
                        }
                        calculateTotal();
                    } else if (status.equalsIgnoreCase(Constants.ERROR) && finalSupplierId.length() > 0) {
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.ERROR, "Error: " + error.toString());
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else if (error instanceof TimeoutError) {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                } else {
                    showAlert(Constants.SERVER_ERROR);
                }
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
    }

    private void calculateTotal() {
        double ef = Double.parseDouble(Global.estimatedFee);
        double discountedFee;
        String[] delTemp = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(Global.estimatedFee))}).split("\\.");
        if (delTemp.length > 1) {
            deliveryPriceText.setText(delTemp[0]);
            textDeliveryFeePriceDecimal.setText(delTemp[1]);
        } else {
            deliveryPriceText.setText(delTemp[0]);
            textDeliveryFeePriceDecimal.setText("00");
        }
        DatabaseHandler db = new DatabaseHandler(this);
        List<SuppliersBean> finalAvailalesuppliersBeanList = availalesuppliersBeanList;
        double total = 0;
        int freeDelivery = 0;
        String str = BuildConfig.VERSION_NAME;
        finalTax = 0;
        for (int k = 0; k < finalAvailalesuppliersBeanList.size(); k++) {
            List<OrderBean> orderBeanList = db.getAllOrders(finalAvailalesuppliersBeanList.get(k));
            for (int i = 0; i < orderBeanList.size(); i++) {
                OrderBean order = orderBeanList.get(i);
                total += Double.parseDouble(order.getFinalPrice());
                finalTax += order.getTaxAmount() * ((double) Integer.parseInt(order.getOrderQuantity()));
            }
        }
        if (storePromotionBeanList != null) {
            for (int i = 0; i < storePromotionBeanList.size(); i++) {
                promotionDiscount = Double.parseDouble(storePromotionBeanList.get(i).getDiscountAmount());
            }
            couponError = false;
            str = "(Auto Promotion Applied)";
        }
        double fee = 0.0d;
        if (Global.isIndividualDelivery) {
            for (int i = 0; i < finalAvailalesuppliersBeanList.size(); i++) {
                SupplierDeliveryScheduleBean sScheduledBean = Global.map.get(finalAvailalesuppliersBeanList.get(i).getSupplierId());
                if (!(sScheduledBean == null || Global.promotionApplicableOnDays == null || Global.promotionApplicableOnDays.get(sScheduledBean.getDeliveryDayOfWeek()) == null || !(Global.promotionApplicableOnDays.get(sScheduledBean.getDeliveryDayOfWeek())).equalsIgnoreCase("YES"))) {
                    fee += Double.parseDouble(sScheduledBean.getFinalDeliveryPrice());
                }
            }
            discountedFee = fee;
        } else {
            discountedFee = ef;
        }
        if (Global.isIndividualDelivery) {
            if (Global.promotionAvailableBean != null && Global.isFreeDelivery && total >= Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()) && this.promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                promotionDiscount = discountedFee;
                freeDelivery = 1;
                couponError = false;
                str = "(Free Delivery Applied)";
            }
        } else if (Global.dayOfWeek != null && Global.promotionApplicableOnDays.get(Global.dayOfWeek) != null && (Global.promotionApplicableOnDays.get(Global.dayOfWeek)).equalsIgnoreCase("YES") && Global.promotionAvailableBean != null && Global.isFreeDelivery && total >= Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()) && promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
            promotionDiscount = discountedFee;
            freeDelivery = 1;
            couponError = false;
            str = "(Free Delivery Applied)";
        }
        if (promotionAvailableBean != null) {
            if (promotionAvailableBean.getFreeDeliveryMinOrder() != null) {
                if (freeDelivery == 0 && promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes") && total >= Double.parseDouble(promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()) && this.promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                    freeDelivery = 1;
                    couponError = false;
                    str = "(Free Delivery Applied)";
                }
            } else if (promotionAvailableBean.getAmountOff() != null) {
                if (isCouponApplied) {
                    Double amountOff = Double.parseDouble(promotionAvailableBean.getAmountOff().getDiscountAmntValue());
                    if (promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                        if (this.promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            couponError = false;
                            str = "(Amount off Applied)";
                        }
                    } else if (Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt()) > 0) {
                        if (total < Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt())) {
                            couponCodeText.setVisibility(View.GONE);
                            buttonaddcoupon.setText("ADD");
                            couponError = true;
                            showAlert("Coupon applicable only for orders above $" + promotionAvailableBean.getAmountOff().getMinimumOrderAmnt());
                            promotionAvailableBean = null;
                        } else if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            couponError = false;
                            str = "(Amount off Applied)";
                            freeDelivery = 0;
                        }
                    } else if (promotionDiscount < amountOff) {
                        promotionDiscount = amountOff;
                        freeDelivery = 0;
                        couponError = false;
                        str = "(Amount off Applied)";
                    }
                }
            } else if (promotionAvailableBean.getDeliveryBean() != null) {
                if (promotionAvailableBean.getDeliveryBean().getFreeDelivery().equalsIgnoreCase("yes") && freeDelivery == 0 && this.promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                    freeDelivery = 1;
                    couponError = false;
                    str = "(Free Delivery Applied)";
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
                            if ((orderBeanList.get(k)).getRegularPrice().equalsIgnoreCase((orderBeanList.get(k)).getUnitPrice())) {
                                supTotal += Double.parseDouble((orderBeanList.get(k)).getFinalPrice());
                            }
                        }
                    }
                    amountOff = (discPercent / 100) * supTotal;
                    if (promotionAvailableBean.getPercentOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            couponError = false;
                            str = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                        }
                    } else if (Double.parseDouble(promotionAvailableBean.getPercentOff().getMinimumOrderAmnt()) <= 0.0d) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            couponError = false;
                            str = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                        }
                    } else if (total >= Double.parseDouble(promotionAvailableBean.getPercentOff().getMinimumOrderAmnt())) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                            freeDelivery = 0;
                            str = "(" + promotionAvailableBean.getPercentOff().getDiscountAmntPercent() + "% Promotion Applied )";
                        }
                    } else {
                        couponCodeText.setVisibility(View.GONE);
                        buttonaddcoupon.setText("ADD");
                        couponError = true;
                        showAlert("Coupon applicable only for orders above $" + promotionAvailableBean.getPercentOff().getMinimumOrderAmnt());
                        promotionAvailableBean = null;
                    }
                }
            }
        }
        String[] taxstr = String.valueOf(finalTax).split("\\.");
        if (taxstr.length > 1) {
            String[] temp = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(String.valueOf(this.finalTax)))}).split("\\.");
            taxPriceText.setText(taxstr[0]);
            textTaxPriceDecimal.setText(temp[1]);
        } else {
            taxPriceText.setText(taxstr[0]);
            textTaxPriceDecimal.setText("00");
        }
        String[] prom = String.format("%.2f", new Object[]{Double.valueOf(this.promotionDiscount)}).split("\\.");
        if (prom.length > 1) {
            textPromotionAmount.setText(prom[0]);
            textPromotionAmtDecimal.setText(prom[1]);
        } else {
            textPromotionAmount.setText(prom[0]);
            textPromotionAmtDecimal.setText("00");
        }
        if (promotionDiscount != 0) {
            promoText.setVisibility(View.VISIBLE);
            textPromotionAmtDollor.setVisibility(View.VISIBLE);
            textPromotionAmount.setVisibility(View.VISIBLE);
            textPromotionAmtDecimal.setVisibility(View.VISIBLE);
            promotionNote.setVisibility(View.VISIBLE);
            if (isCouponApplied) {
                if (couponError) {
                    promotionAvailableBean = null;
                    couponCodeText.setVisibility(View.GONE);
                    buttonaddcoupon.setText("ADD");
                    editCouponsLayout.setVisibility(View.VISIBLE);
                } else if (promotionAvailableBean != null) {
                    couponCodeText.setVisibility(View.VISIBLE);
                    couponCodeText.setText(promotionAvailableBean.getPromoCode());
                    buttonaddcoupon.setText("CHANGE");
                } else {
                    promotionAvailableBean = null;
                    couponCodeText.setVisibility(View.GONE);
                    buttonaddcoupon.setText("ADD");
                }
                editCouponsLayout.setVisibility(View.GONE);
            } else {
                couponCodeText.setVisibility(View.GONE);
                buttonaddcoupon.setText("ADD");
                editCouponsLayout.setVisibility(View.VISIBLE);
            }
            promotionNote.setText(str);
        } else {
            promoText.setVisibility(View.GONE);
            textPromotionAmtDollor.setVisibility(View.GONE);
            textPromotionAmount.setVisibility(View.GONE);
            textPromotionAmtDecimal.setVisibility(View.GONE);
            promotionNote.setVisibility(View.GONE);
            couponCodeText.setVisibility(View.GONE);
            buttonaddcoupon.setText("ADD");
            editcoupon.setText(BuildConfig.VERSION_NAME);
            editCouponsLayout.setVisibility(View.GONE);
        }
        if (availalesuppliersBeanList.size() == 0) {
            estimatedFee = AppEventsConstants.EVENT_PARAM_VALUE_NO;
            deliveryPriceText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            textDeliveryFeePriceDecimal.setText("00");
            totalAmount = finalTax + total + Double.parseDouble(estimatedFee);
            totalAmount = finalTax + total;
            placeOrderBtn.setEnabled(false);
            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
        } else if (freeDelivery == 1) {
            estimatedFee = Global.estimatedFee;
            double f = Double.parseDouble(estimatedFee) - discountedFee;
            totalAmount = finalTax + total + Double.parseDouble(estimatedFee);
            totalAmount = finalTax + total + f;
        } else {
            totalAmount = finalTax + total + Double.parseDouble(Global.estimatedFee);
        }
        if (Global.tipBean != null) {
            tipListLayout.setVisibility(View.GONE);
            if (!Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                tipPriceText.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                textTipTotalPriceDecimal.setText("00");
            } else if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                String tipPercent = Global.tipBean.getTipAmount();
                double tipAmount = (Double.parseDouble(tipPercent) * total) / 100;
                selectedTipAmount.setText("Tip selected is " + tipPercent + "%" + " = $" + String.format("%.2f", new Object[]{Double.valueOf(tipAmount)}));
                String[] tipTemp = String.format("%.2f", new Object[]{Double.valueOf(tipAmount)}).split("\\.");
                if (tipTemp.length > 1) {
                    tipPriceText.setText(tipTemp[0]);
                    textTipTotalPriceDecimal.setText(tipTemp[1]);
                } else {
                    tipPriceText.setText(tipTemp[0]);
                    textTipTotalPriceDecimal.setText("00");
                }
                totalAmount += tipAmount;
            } else {
                selectedTipAmount.setText(Global.tipBean.getTipName() + " selected");
                String amount = Global.tipBean.getTipAmount();
                totalAmount += Double.parseDouble(Global.tipBean.getTipAmount());
                String[] tipTemp = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(amount))}).split("\\.");
                if (tipTemp.length > 1) {
                    tipPriceText.setText(tipTemp[0]);
                    textTipTotalPriceDecimal.setText(tipTemp[1]);
                } else {
                    tipPriceText.setText(tipTemp[0]);
                    textTipTotalPriceDecimal.setText("00");
                }
            }
        }
        if (freeDelivery == 0) {
            if (totalAmount > promotionDiscount) {
                totalAmount -= this.promotionDiscount;
            } else {
                totalAmount = 0;
            }
        }
        if (!isCreditsApply) {
            findViewById(R.id.text_credit_amt_dollor).setVisibility(View.GONE);
            creditAmountText.setVisibility(View.GONE);
            textCreditAmtDecimal.setVisibility(View.GONE);
            creditText.setVisibility(View.GONE);
        } else if (creditAmount != 0.0d) {
            creditAmountText.setVisibility(View.VISIBLE);
            findViewById(R.id.text_credit_amt_dollor).setVisibility(View.VISIBLE);
            textCreditAmtDecimal.setVisibility(View.VISIBLE);
            creditText.setVisibility(View.VISIBLE);
            if (totalAmount <= creditAmount) {
                creditAmount = totalAmount;
            }
            totalAmount -= creditAmount;
            String[] credits1 = String.format("%.2f", new Object[]{Double.valueOf(creditAmount)}).split("\\.");
            if (credits1.length > 1) {
                creditAmountText.setText(credits1[0]);
                textCreditAmtDecimal.setText(credits1[1]);
            } else {
                this.creditAmountText.setText(credits1[0]);
                this.textCreditAmtDecimal.setText("00");
            }
        } else {
            findViewById(R.id.text_credit_amt_dollor).setVisibility(View.GONE);
            creditAmountText.setVisibility(View.GONE);
            textCreditAmtDecimal.setVisibility(View.GONE);
            creditText.setVisibility(View.GONE);
        }
        String[] totalTemp = String.format("%.2f", new Object[]{Double.valueOf(this.totalAmount)}).split("\\.");
        totalAmountText.setText(totalTemp[0]);
        textTotalAmtDecimal.setText(totalTemp[1]);
        String crdts = pref.getString(Constants.CREDITS_AMOUNT, null);
        if (crdts != null) {
            double crdtDouble = Double.parseDouble(crdts);
            if (creditAmount != 0.0d || !isShowCreditPopup || crdtDouble <= 0.0d) {
                return;
            }
            if (Global.map.size() != 0) {
                showApplyCredits("Do you want to use your credits", crdts);
            } else if (singleStoreDeliveryApplied) {
                showApplyCredits("Do you want to use your credits", crdts);
            }
        }
    }

    private void placeOrder(String addressId, String paymentId) {
        int k;
        JSONObject jSONObject;
        DatabaseHandler db = new DatabaseHandler(this);
        Global.showProgress(this);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JSONObject params = new JSONObject();
        JSONArray orderArray = new JSONArray();
        JSONObject orderObj = new JSONObject();
        JSONArray suppliersArray = new JSONArray();
        double total = 0.0d;
        List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
        try {
            params.put(Constants.USER_ID, pref.getString(Constants.USER_ID, null));

            for (k = 0; k < suppliersBeanList1.size(); k++) {
                int i;
                List<OrderBean> orderBeanList;
                SuppliersBean suppliersBean = (SuppliersBean) suppliersBeanList1.get(k);
                if (suppliersBean.getSupplierAvailable().equalsIgnoreCase("yes")) {
                    JSONObject supObj = new JSONObject();
                    supObj.put(Constants.SUPPLIER_ID, suppliersBean.getSupplierId());
                    List<WorkingDaysBean> workingDaysBeanList;
                    WorkingDaysBean work;
                    if (this.pref.getString(Constants.DELIVERY_TYPE, null).equalsIgnoreCase(Constants.COMBINE)) {
                        supObj.put(Constants.DELIVERY_DATE, pref.getString(Constants.DELIVERY_DATE, null));
                        showAlert(this.pref.getString(Constants.DELIVERY_DATE, null));
                        supObj.put("delivery_from_time", pref.getString(Constants.DELIVERY_FROM, null));
                        supObj.put("delivery_to_time", pref.getString(Constants.DELIVERY_TO, null));
                        try {
                            workingDaysBeanList = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList();
                            work = null;
                            for (i = 0; i < workingDaysBeanList.size(); i++) {
                                work = (WorkingDaysBean) workingDaysBeanList.get(i);
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
                        supObj.put(Constants.DELIVERY_DATE, deliveryScheduleBean.getDeliveryDate());
                        supObj.put("delivery_from_time", deliveryScheduleBean.getDeliveryFrom());
                        supObj.put("delivery_to_time", deliveryScheduleBean.getDeliveryTo());
                        try {
                            workingDaysBeanList = deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList();
                            work = null;
                            for (i = 0; i < workingDaysBeanList.size(); i++) {
                                work = (WorkingDaysBean) workingDaysBeanList.get(i);
                                if (work.getSupplierId().equalsIgnoreCase(suppliersBean.getSupplierId())) {
                                    break;
                                }
                            }
                            supObj.put("est_delivery_miles", work.getDeliveryMiles());
                            supObj.put("est_delivery_minutes", work.getDeliveryMinutes());
                            supObj.put("est_delivery_congestion", Global.congestionCost);
                            supObj.put("delivery_weight_surcharge", work.getDeliveryWeightSurcharge());
                            supObj.put("est_delivery_total", Global.estimatedFee);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    orderBeanList = db.getAllOrders(suppliersBean);
                    JSONArray itemsArray = new JSONArray();
                    for (i = 0; i < orderBeanList.size(); i++) {
                        OrderBean order = (OrderBean) orderBeanList.get(i);
                        total += Double.parseDouble(order.getFinalPrice());
                        JSONObject itemObj = new JSONObject();
                        itemObj.put("supplier_item_id", order.getSupplierItemId());
                        itemObj.put("item_name", order.getItemName());
                        if (order.getBrandName() != null) {
                            itemObj.put("brand_name", order.getBrandName());
                        } else {
                            itemObj.put("brand_name", BuildConfig.VERSION_NAME);
                        }
                        itemObj.put("size", order.getSize());
                        itemObj.put("uom", order.getUom());
                        if (order.getShippingWeight() != null) {
                            itemObj.put("shipping_weight", order.getShippingWeight());
                        } else {
                            itemObj.put("shipping_weight", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                        }
                        itemObj.put("is_taxable", order.getIsTaxable());
                        itemObj.put("order_quantity", order.getOrderQuantity());
                        itemObj.put("regular_price", order.getRegularPrice());
                        if (order.getRegularPrice().equalsIgnoreCase(order.getUnitPrice())) {
                            itemObj.put("final_price", "0.00");
                        } else {
                            itemObj.put("final_price", order.getUnitPrice());
                        }
                        itemObj.put("applicable_sale_description", BuildConfig.VERSION_NAME);
                        String subs = order.getSubstitutionWith();
                        if (subs == null) {
                            itemObj.put("substitution", AppEventsConstants.EVENT_PARAM_VALUE_YES);
                        } else {
                            if (subs.equalsIgnoreCase("none")) {
                                itemObj.put("substitution", AppEventsConstants.EVENT_PARAM_VALUE_YES);
                            } else {
                                if (subs.equalsIgnoreCase("store's choice")) {
                                    itemObj.put("substitution", "2");
                                } else {
                                    itemObj.put("substitution", "3");
                                }
                            }
                        }
                        itemsArray.put(itemObj);
                    }
                    supObj.put("items", itemsArray);
                    suppliersArray.put(supObj);
                }
            }
            orderObj.put("suppliers", suppliersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        double subtotal = total;
        double promotionDiscount = 0;
        int freeDelivery = 0;
        double amountOff = 0;
        if (storePromotionBeanList != null) {
            for (int i = 0; i < storePromotionBeanList.size(); i++) {
                promotionDiscount += Double.parseDouble(storePromotionBeanList.get(i).getDiscountAmount());
            }
        }
        if (Global.isFreeDelivery && promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
            promotionDiscount = Double.parseDouble(Global.estimatedFee);
            freeDelivery = 1;
        }
        if (promotionAvailableBean != null) {
            if (promotionAvailableBean.getFreeDeliveryMinOrder() != null) {
                if (freeDelivery == 0 && promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes") && subtotal >= Double.parseDouble(promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()) && promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                    freeDelivery = 1;
                }
            } else if (promotionAvailableBean.getAmountOff() != null) {
                amountOff = Double.parseDouble(promotionAvailableBean.getAmountOff().getDiscountAmntValue());
                if (promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                    if (promotionDiscount < amountOff) {
                        promotionDiscount = amountOff;
                    }
                } else if (Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt()) > 0.0d) {
                    if (subtotal >= Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt()) && promotionDiscount < amountOff) {
                        promotionDiscount = amountOff;
                    }
                } else if (promotionDiscount < amountOff) {
                    promotionDiscount = amountOff;
                }
                freeDelivery = 0;
            } else if (promotionAvailableBean.getDeliveryBean() != null) {
                if (promotionAvailableBean.getDeliveryBean().getFreeDelivery().equalsIgnoreCase("yes") && freeDelivery == 0 && promotionDiscount < Double.parseDouble(Global.estimatedFee)) {
                    promotionDiscount = Double.parseDouble(Global.estimatedFee);
                    freeDelivery = 1;
                }
            } else if (promotionAvailableBean.getPercentOff() != null) {
                double discPercent = Double.parseDouble(promotionAvailableBean.getPercentOff().getDiscountAmntPercent());
                double supTotal = 0.0d;
                if (promotionAvailableBean.getExcludeSalesItems().equalsIgnoreCase("yes")) {
                    for (int s = 0; s < availalesuppliersBeanList.size(); s++) {
                        List<OrderBean> orderBeanList = db.getAllOrders(availalesuppliersBeanList.get(s));
                        for (k = 0; k < orderBeanList.size(); k++) {
                            if ((orderBeanList.get(k)).getRegularPrice().equalsIgnoreCase(orderBeanList.get(k).getUnitPrice())) {
                                supTotal += Double.parseDouble(orderBeanList.get(k).getFinalPrice());
                            }
                        }
                    }
                    amountOff = (discPercent / 100) * supTotal;
                    if (promotionAvailableBean.getAmountOff().getMinimumOrderAmnt().equalsIgnoreCase("null")) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                        }
                    } else if (Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt()) <= 0.0d) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                        }
                    } else if (subtotal >= Double.parseDouble(promotionAvailableBean.getAmountOff().getMinimumOrderAmnt())) {
                        if (amountOff > Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt())) {
                            amountOff = Double.parseDouble(promotionAvailableBean.getPercentOff().getMaxDiscountAmnt());
                        }
                        if (promotionDiscount < amountOff) {
                            promotionDiscount = amountOff;
                        }
                    }
                }
                freeDelivery = 0;
            }
        }
        double tax = finalTax;
        if (availalesuppliersBeanList.size() == 0) {
            estimatedFee = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        if (freeDelivery == 1) {
            estimatedFee = "0.00";
            total = 0.0d + subtotal + tax + Double.parseDouble(this.estimatedFee);
        } else {
            total = 0.0d + subtotal + tax + Double.parseDouble(this.estimatedFee);
        }
        if (freeDelivery == 0) {
            if (total > promotionDiscount) {
                total -= promotionDiscount;
            } else {
                total = 0.0d;
            }
        }
        try {

            if (Global.tipBean == null) {
                orderObj.put("tip_amount", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                orderObj.put("tip_percent", AppEventsConstants.EVENT_PARAM_VALUE_NO);
            } else if (!Global.tipBean.getNoTipApply().equalsIgnoreCase("no")) {
                orderObj.put("tip_amount", AppEventsConstants.EVENT_PARAM_VALUE_NO);
                orderObj.put("tip_percent", AppEventsConstants.EVENT_PARAM_VALUE_NO);
            } else if (Global.tipBean.getTipPercentApply().equalsIgnoreCase("yes")) {
                String tipPercent = Global.tipBean.getTipAmount();
                double tipPercent1 = Double.parseDouble(tipPercent) / 100.0d;
                total += (Double.parseDouble(tipPercent) * subtotal) / 100.0d;
                jSONObject = orderObj;
                jSONObject.put("tip_amount", String.format("%.2f", new Object[]{Double.valueOf((Double.parseDouble(tipPercent) * subtotal) / 100.0d)}));
                orderObj.put("tip_percent", tipPercent1);
            } else {
                String amount = Global.tipBean.getTipAmount();
                total += Double.parseDouble(amount);
                orderObj.put("tip_amount", amount);
                orderObj.put("tip_percent", AppEventsConstants.EVENT_PARAM_VALUE_NO);
            }
            orderObj.put("payment_id", paymentId);
            orderObj.put("address_id", addressId);
            orderObj.put("delivery_instruction", orderInsText.getText().toString());
            orderObj.put("delivery_option", pref.getString(Constants.DELIVERY_TYPE, null));
            orderObj.put("delivery_phone_supplied", phoneText.getText().toString());
            if (creditAmount == 0.0d) {
                orderObj.put("user_credit_applied", "NO");
                orderObj.put("credit_amount_applied", BuildConfig.VERSION_NAME);
            } else {
                orderObj.put("user_credit_applied", "YES");
                orderObj.put("credit_amount_applied", creditAmount);
            }
            if (!isCouponApplied) {
                orderObj.put("promo_code", BuildConfig.VERSION_NAME);
            } else if (couponError) {
                orderObj.put("promo_code", BuildConfig.VERSION_NAME);
            } else {
                orderObj.put("promo_code", this.couponCodeText.getText());
            }
            if (promotionDiscount != 0.0d) {
                orderObj.put("promo_amount", promotionDiscount);
            } else {
                orderObj.put("promo_amount", BuildConfig.VERSION_NAME);
            }
            jSONObject = orderObj;
            jSONObject.put("sub_total", String.format("%.2f", new Object[]{Double.valueOf(subtotal)}));
            jSONObject = orderObj;
            jSONObject.put("tax", String.format("%.2f", new Object[]{Double.valueOf(tax)}));
            if (isCreditsApply) {
                total -= creditAmount;
            }
            jSONObject = orderObj;
            jSONObject.put("total_amount", String.format("%.2f", new Object[]{Double.valueOf(total)}));
            orderArray.put(orderObj);
            params.put("orders", orderArray);
            Log.d("order params", params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, "https://api.heartyyfresh.com/api/v2/order/place", params, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
               Global.hideProgress();
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        JSONObject dataOj = jsonObject.getJSONObject(ShareConstants.WEB_DIALOG_PARAM_DATA);
                        JSONArray ordersArray = dataOj.getJSONArray("orders");
                        JSONObject notiObj = null;
                        if (dataOj.optJSONObject("notification") != null) {
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
                        Editor editor = CompleteOrderActivity.this.pref.edit();
                        editor.putString(Constants.DELIVERY_DATE, null);
                        editor.putString(Constants.DELIVERY_FROM, null);
                        editor.putString(Constants.DELIVERY_TO, null);
                        editor.putString(Constants.DELIVERY_TYPE, null);
                        editor.apply();
                        Intent intent = new Intent(CompleteOrderActivity.this, OrderPlacedActivity.class);
                        intent.putExtra("order_id", ordersArray.get(0).toString());
                        if (notiObj != null) {
                            intent.putExtra("notification_id", notiObj.getString("notification_id"));
                            intent.putExtra("is_on", notiObj.getString("is_on"));
                        } else {
                            intent.putExtra("notification_id", BuildConfig.VERSION_NAME);
                            intent.putExtra("is_on", BuildConfig.VERSION_NAME);
                        }
                        Global.locationBean = null;
                        Global.paymentCardBean = null;
                        Global.datePos = -1;
                        Global.slotPos = -1;
                        Global.map = new HashMap();
                        Global.tipBean = null;
                        Global.orderInstructions = null;
                        Global.phoneNo = null;
                        Global.isFreeDelivery = false;
                        Global.promotionAvailableBean = null;
                        creditAmount = 0.0d;
                        Global.congestionCost = null;
                        isShowCreditPopup = true;
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                       Global.hideProgress();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.ERROR, new Object[]{"Error: " + error.toString()});
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);
                    return;
                }
               Global.hideProgress();
                showAlert(Constants.REQUEST_TIMED_OUT);
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, 0, 1.0f));
    }

    private void checkDelivery(String addressId, String zipcode) {
        Global.showProgress(this);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(this);
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
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(1, "https://api.heartyyfresh.com/api/v2/order/delivery/available", params, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("delivery response", jsonObject.toString());
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                       Global.hideProgress();
                        checkDeliveryBeanList = ConversionHelper.convertCheckDeliveryJsonToCheckDeliveryBean(jsonObject);
                        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                        CheckDeliveryBean bean;
                        SuppliersBean sup;
                        if (suppliersBeanList.size() > 1) {
                            int i;
                            int flag;
                            for (i = 0; i < suppliersBeanList.size(); i++) {
                                flag = 0;
                                bean = null;
                                if (checkDeliveryBeanList.size() > 0) {
                                    for (int j = 0; j < checkDeliveryBeanList.size(); j++) {
                                        bean = checkDeliveryBeanList.get(j);
                                        if (bean.getSupplierId().equalsIgnoreCase(suppliersBeanList.get(i).getSupplierId())) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag == 1) {
                                        sup = suppliersBeanList.get(i);
                                        sup.setSupplierAvailable("no");
                                        db.updateSupplier(sup);
                                        sup.setMessage(bean.getMessage());
                                    } else {
                                        sup = suppliersBeanList.get(i);
                                        sup.setSupplierAvailable("yes");
                                        db.updateSupplier(sup);
                                    }
                                } else {
                                    sup = suppliersBeanList.get(i);
                                    sup.setSupplierAvailable("yes");
                                    db.updateSupplier(sup);
                                }
                            }
                            dateListAdapter = new CustomDeliveryStoresDateListAdapter(CompleteOrderActivity.this, suppliersBeanList);
                            deliveryStoreDateList.setAdapter(dateListAdapter);
                            Global.setListViewHeightBasedOnChildren(deliveryStoreDateList);
                            List<SuppliersBean> suppliersBeanListNew = db.getAllSuppliers();
                            flag = 0;
                            for (i = 0; i < suppliersBeanListNew.size(); i++) {
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
                                bean = checkDeliveryBeanList.get(0);
                                if (bean.getSupplierId().equalsIgnoreCase(suppliersBeanList.get(0).getSupplierId())) {
                                    sup = suppliersBeanList.get(0);
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
                                sup = (SuppliersBean) suppliersBeanList.get(0);
                                sup.setSupplierAvailable("yes");
                                db.updateSupplier(sup);
                            }
                        }
                        getAvailableSuppliers();
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                       Global.hideProgress();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("delivery error", new Object[]{"Error: " + error.toString()});
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);
                    return;
                }
               Global.hideProgress();
                showAlert(Constants.REQUEST_TIMED_OUT);
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1.0f));
    }

    void checkIsPlaceOrderButtonEnabled() {
        int i;
        DatabaseHandler db = new DatabaseHandler(this);
        this.availalesuppliersBeanList = null;
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        this.availalesuppliersBeanList = new ArrayList();
        for (i = 0; i < suppliersBeanList.size(); i++) {
            if (suppliersBeanList.get(i).getSupplierAvailable().equalsIgnoreCase("yes")) {
                availalesuppliersBeanList.add(suppliersBeanList.get(i));
            }
        }
        int temp = 0;
        for (i = 0; i < availalesuppliersBeanList.size(); i++) {
            if (Global.map.get(availalesuppliersBeanList.get(i).getSupplierId()) == null) {
                temp = 1;
                break;
            }
        }
        if (Global.locationBean == null || Global.paymentCardBean == null || Global.map.size() == 0) {
            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
            placeOrderBtn.setEnabled(false);
        } else if (temp == 0) {
            placeOrderBtn.setEnabled(true);
            placeOrderBtn.setBackgroundResource(R.drawable.button_red);
        } else {
            placeOrderBtn.setBackgroundResource(R.drawable.button_disable);
            placeOrderBtn.setEnabled(false);
        }
    }

    private void showAlert(String msg) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.error_dialog, null);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(this.andBold);
        okBtn.setTypeface(this.andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showChangeAddressAlert(String msg) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_no_email, null);
        Builder alertDialogBuilder = new Builder(this);
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
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        changeAddressBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (isAddress) {
                    startActivity(new Intent(CompleteOrderActivity.this, DeliveryLocationActivity.class));
                    Global.isCompleteBack = true;
                    Global.previousActivity = DeliveryLocationActivity.class.getName();
                } else {
                    startActivity(new Intent(CompleteOrderActivity.this, SearchAddressActivity.class));
                    Global.isCompleteBack = true;
                    Global.previousActivity = DeliveryLocationActivity.class.getName();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showApplyCredits(String msg, final String credits) {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_no_email, null);
        Builder alertDialogBuilder = new Builder(this);
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
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                isCreditsApply = false;
                isShowCreditPopup = false;
                dialog.dismiss();
            }
        });
        changeAddressBtn.setOnClickListener(new OnClickListener() {
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