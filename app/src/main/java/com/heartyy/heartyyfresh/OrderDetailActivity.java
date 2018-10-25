package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.OrderDetailAdapter;
import com.heartyy.heartyyfresh.adapter.OrderDetailShopsAdapter;
import com.heartyy.heartyyfresh.adapter.SimpleSectionOrderdetaiViewAdapter;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.OrderDetailBean;
import com.heartyy.heartyyfresh.bean.OrderDetailPromoBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;
import com.heartyy.heartyyfresh.utils.WrappingLinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class OrderDetailActivity extends AppCompatActivity {

    Typeface andBold, bold, italic, light;
    TextView txtOrderNo, txtDeliveryAddress, txtOrderInstruction,
            txtContactInfo, txtCardNo, txtCardType,textorder, textdeliverto, textaddress, textorderInst,textphone,
             textpaymentmethod, textcardnumber, creditText, creditAmountText;
    TextView txtTaxWord, textDeliveryFeeWord, textTax, textDeliveryFee, txtTipWord, textTip, textTipNote, textPromotion, textPromotionAmt,
            textPromotionNote, textYourTotal, txtTotal, txtTotalAmount, textAddress2;
    private Button rateThis;

    ListView listOrderDetailShops;
    private SharedPreferences pref;
    private ImageView imageCardType;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public List<OrderBean> finalOrderBeanList;
    private List<SuppliersBean> finalSupplierBeanList;
    private OrderDetailAdapter adapter;
    private OrderDetailShopsAdapter shopsAdapter;
    private OrderBean orderBean;
    private String orderid, orderSupplierId;
    Button btnTrackDelivery;
    OrderDetailBean data;
    TimeZone tz;
    private Menu menu;
    private String orderType;
    private RelativeLayout bottom;
    SimpleSectionOrderdetaiViewAdapter mSectionedAdapter;
    public static boolean isRate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_order_detail));
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

        ViewGroup root = (ViewGroup) findViewById(R.id.order_detail_receipt);
        Global.setFont(root, andBold);
        tz = TimeZone.getDefault();

        orderid = getIntent().getExtras().getString("orderid");
        orderType = getIntent().getExtras().getString("order_type");
        orderSupplierId = getIntent().getExtras().getString("order_supplier_id");

        bottom = (RelativeLayout) findViewById(R.id.layout_bottom);
        imageCardType = (ImageView) findViewById(R.id.image_cardType);

        if (orderType.equalsIgnoreCase("past")) {
            if (isRate) {
                bottom.setVisibility(View.VISIBLE);
            } else {
                bottom.setVisibility(View.GONE);
            }
        }

        txtOrderNo = (TextView) findViewById(R.id.text_order_no_detail);
        txtDeliveryAddress = (TextView) findViewById(R.id.text_address);
        txtOrderInstruction = (TextView) findViewById(R.id.text_inst);
        txtContactInfo = (TextView) findViewById(R.id.text_phone_no);
        txtCardNo = (TextView) findViewById(R.id.text_card_number);
        txtCardType = (TextView) findViewById(R.id.text_payment_type);
        textTax = (TextView) findViewById(R.id.text_tax_amt);
        txtTaxWord = (TextView) findViewById(R.id.text_tax);
        textDeliveryFee = (TextView) findViewById(R.id.text_delivery_amt);
        txtTipWord = (TextView) findViewById(R.id.text_tip);
        textTip = (TextView) findViewById(R.id.text_tip_amt);
        textTipNote = (TextView) findViewById(R.id.text_tip_note);
        textPromotion = (TextView) findViewById(R.id.text_promotion);
        textPromotionAmt = (TextView) findViewById(R.id.text_promotion_amt);
        textPromotionNote = (TextView) findViewById(R.id.text_promotion_note);
        txtTotalAmount = (TextView) findViewById(R.id.text_total_amt);
        txtTotal = (TextView) findViewById(R.id.text_total);
        textYourTotal = (TextView) findViewById(R.id.text_your_total);
        textDeliveryFeeWord = (TextView) findViewById(R.id.text_delivery_fee);
        btnTrackDelivery = (Button) findViewById(R.id.button_track_delivery);
        listOrderDetailShops = (ListView) findViewById(R.id.list_order_detail_shops);
        recyclerView = (RecyclerView) findViewById(R.id.list_checkout);
        textdeliverto = (TextView) findViewById(R.id.text_deliver_to);
        textaddress = (TextView) findViewById(R.id.text_address);
        textorderInst = (TextView) findViewById(R.id.text_orderInst);
        textphone = (TextView) findViewById(R.id.text_phone);
        textpaymentmethod = (TextView) findViewById(R.id.text_payment_method);
        textcardnumber = (TextView) findViewById(R.id.text_card_number);
        rateThis = (Button) findViewById(R.id.rate_this);
        textorder = (TextView) findViewById(R.id.text_order_no);
        creditText = (TextView) findViewById(R.id.text_credit);
        creditAmountText = (TextView) findViewById(R.id.text_credit_amt);
        textAddress2 = (TextView) findViewById(R.id.text_address2);


        txtOrderNo.setTypeface(andBold);
        textdeliverto.setTypeface(light);
        textaddress.setTypeface(andBold);
        textorderInst.setTypeface(light);
        txtContactInfo.setTypeface(bold);
        textphone.setTypeface(andBold);
        textpaymentmethod.setTypeface(light);
        txtCardType.setTypeface(light);
        txtCardNo.setTypeface(andBold);
        txtTotal.setTypeface(light);
        txtTaxWord.setTypeface(light);
        textTax.setTypeface(light);
        textDeliveryFeeWord.setTypeface(light);
        textDeliveryFee.setTypeface(light);
        txtTipWord.setTypeface(light);
        textTip.setTypeface(light);
        textTipNote.setTypeface(light);
        textPromotion.setTypeface(light);
        textPromotionAmt.setTypeface(light);
        textPromotionNote.setTypeface(light);
        textYourTotal.setTypeface(andBold);
        txtTotalAmount.setTypeface(andBold);
        rateThis.setTypeface(andBold);
        txtDeliveryAddress.setTypeface(bold);
        textAddress2.setTypeface(bold);
        txtOrderInstruction.setTypeface(bold);
        textorder.setTypeface(light);
        creditAmountText.setTypeface(light);
        creditText.setTypeface(light);

        textPromotionAmt.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));

        if (orderType.equalsIgnoreCase("current")) {
            btnTrackDelivery.setVisibility(View.VISIBLE);
        } else {
            btnTrackDelivery.setVisibility(View.GONE);
        }

        rateThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, RateUsActivity.class);
                intent.putExtra("order_id", data.getOrderId());
                startActivity(intent);
            }
        });

        btnTrackDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data!=null) {
                    List<SuppliersBean> currentOrderSupplierBeans = data.getSuppliersBeanList();
                    final String[] suppIds = new String[currentOrderSupplierBeans.size()];
                    final String[] suppNames = new String[currentOrderSupplierBeans.size()];
                    for (int i = 0; i < currentOrderSupplierBeans.size(); i++) {
                        suppIds[i] = currentOrderSupplierBeans.get(i).getOrderSupplierId();
                    }
                    Intent intent = new Intent(OrderDetailActivity.this, TrackOrderActivity.class);
                    intent.putExtra("date", data.getDeliveryDate());
                    intent.putExtra("time", data.getDeliveryTime());
                    intent.putExtra("orderId", data.getOrderId());
                    intent.putExtra("displayorderId", data.getDisplayOrderId());
                    intent.putExtra("supplierId", suppIds);
                    startActivity(intent);
                }
            }
        });


        getOrderDetail();
        finalOrderBeanList = new ArrayList<>();
        finalSupplierBeanList = new ArrayList<>();
    }

    public void getOrderDetail() {
        Global.showProgress(OrderDetailActivity.this);
        RequestQueue rq = Volley.newRequestQueue(OrderDetailActivity.this);
        String url = "order/detail?user_id=" + pref.getString(Constants.USER_ID, null) + "&order_id=" + orderid + "&order_supplier_id=" + orderSupplierId;
        //String url = "order/detail?user_id=11&order_id=1";
        Log.d("order url..",Constants.URL + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                data = ConversionHelper.convertOrderDetailJsonToOrderDetailBean(jsonObject);
                                if ((orderType.equalsIgnoreCase("current"))) {
                                    menu.findItem(R.id.action_order_reciept).setVisible(false);
                                    bottom.setVisibility(View.GONE);
                                }
                                txtOrderNo.setText(data.getDisplayOrderId());
                                //Supplier Receipt Data and List Adapter
                                String tempTax[] = data.getTax().split("\\.");

                                if (tempTax.length > 1) {
                                    textTax.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTax[0] + "</big><sup>" + tempTax[1] + "</sup>"));
                                } else {
                                    textTax.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTax[0] + "</big><sup>" + "0" + "</sup>"));
                                }

                                if(data.getTax() == null){
                                    textTax.setText("");
                                }

                                if (data.getDeliveryCharge().equalsIgnoreCase("null")) {
                                    //textDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
                                    textDeliveryFee.setText("");
                                } else {
                                    String tempDelivery[] = data.getDeliveryCharge().split("\\.");

                                    if (tempDelivery.length > 1) {
                                        textDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempDelivery[0] + "</big><sup>" + tempDelivery[1] + "</sup>"));
                                    } else {
                                        textDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempDelivery[0] + "</big><sup>" + "0" + "</sup>"));
                                    }
                                }

                                String tempTip[] = data.getTipAmount().split("\\.");
                                if (tempTip.length > 1) {
                                    textTip.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTip[0] + "</big><sup>" + tempTip[1] + "</sup>"));
                                } else {
                                    textTip.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTip[0] + "</big><sup>" + "0" + "</sup>"));
                                }

                                if(data.getTipAmount() == null){
                                    textTip.setText("");
                                }

                                String tempTotal[] = data.getTotalAmount().split("\\.");
                                if (tempTotal.length > 1) {
                                    txtTotalAmount.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTotal[0] + "</big><sup>" + tempTotal[1] + "</sup>"));
                                } else {
                                    txtTotalAmount.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempTotal[0] + "</big><sup>" + "0" + "</sup>"));
                                }

                                OrderDetailPromoBean orderDetailPromoBean = data.getOrderDetailPromoBean();
                                if (orderDetailPromoBean == null) {
                                    textPromotionAmt.setText(" ");
                                }else{
                                    String promo[] = String.format("%.2f", Double.parseDouble(orderDetailPromoBean.getPromoAmount())).split("\\.");
                                    textPromotionAmt.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + promo[0] + "</big><sup>" + promo[1] + "</sup><big>)</big>"));
                                }

                                if (data.getCredits().equalsIgnoreCase("null")) {
                                    creditAmountText.setVisibility(View.GONE);
                                    creditText.setVisibility(View.GONE);
                                } else {

                                    creditAmountText.setVisibility(View.VISIBLE);
                                    creditText.setVisibility(View.VISIBLE);

                                    String cred[] = String.format("%.2f", Double.parseDouble(data.getCredits())).split("\\.");

                                    creditAmountText.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + cred[0] + "</big><sup>" + cred[1] + "</sup><big>)</big>"));
                                }

                                finalSupplierBeanList = data.getSuppliersBeanList();
                                shopsAdapter = new OrderDetailShopsAdapter(OrderDetailActivity.this, finalSupplierBeanList, OrderDetailActivity.this);
                                listOrderDetailShops.setAdapter(shopsAdapter);
                                Global.setListViewHeightBasedOnChildren(listOrderDetailShops);
                                //End Supplier Receipt List Adapter

                                LocationBean locationBean = data.getLocationBean();

                                String[] address = new String[3];
                                String[] address2 = new String[3];
                                address[0] = locationBean.getAddress1();
                                address[1] = locationBean.getAddress2();
                                if (locationBean.getAptSuitUnit().length() > 0) {
                                    if (locationBean.getAptSuitUnit().contains("#")) {
                                        locationBean.setAptSuitUnit(locationBean.getAptSuitUnit());
                                    } else {
                                        locationBean.setAptSuitUnit("#" + locationBean.getAptSuitUnit());
                                    }
                                }
                                address[2] = locationBean.getAptSuitUnit();
                                address2[0] = locationBean.getCity();
                                address2[1] = locationBean.getState();
                                address2[2] = locationBean.getZipcode();
                                //address2[3] = data.getZipcode();
                                txtDeliveryAddress.setText(Global.FormatAddress(address) + ",");
                                textAddress2.setText(Global.FormatAddress(address2));
/*
                                txtDeliveryAddress.setText(locationBean.getAddress1() + ", " + locationBean.getAddress2()
                                        + locationBean.getAptSuitUnit() + ", " + locationBean.getCity() + ", " +
                                        locationBean.getZipcode() + ", " +
                                        locationBean.getState());
*/

                                if(data.getDeliveryInstructions().isEmpty()){
                                    txtOrderInstruction.setText("No delivery instructions given");
                                }else txtOrderInstruction.setText(data.getDeliveryInstructions());

                                txtContactInfo.setText(data.getDeliveryPhoneSupplied());
                                PaymentCardBean paymentCardBean = data.getPaymentCardBean();
                                txtCardType.setText(paymentCardBean.getCardIssuer());
                                if(!paymentCardBean.getCardLastFourDigit().equalsIgnoreCase("null")){
                                    txtCardNo.setVisibility(View.VISIBLE);
                                    txtCardNo.setText("XXXX-" + paymentCardBean.getCardLastFourDigit());

                                    if (paymentCardBean.getCardIssuer().equalsIgnoreCase("Visa")) {
                                        imageCardType.setImageResource(R.drawable.visa_2x);
                                    } else if (paymentCardBean.getCardIssuer().equalsIgnoreCase("MasterCard")) {
                                        imageCardType.setImageResource(R.drawable.mastercard_2x);
                                    } else if (paymentCardBean.getCardIssuer().equalsIgnoreCase("Discover")) {
                                        imageCardType.setImageResource(R.drawable.discover_2x);
                                    } else if (paymentCardBean.getCardIssuer().equalsIgnoreCase("JCB")) {
                                        imageCardType.setImageResource(R.drawable.jcb_2x);
                                    } else if (paymentCardBean.getCardIssuer().equalsIgnoreCase("Maestro")) {
                                        imageCardType.setImageResource(R.drawable.maestro_2x);
                                    } else if (paymentCardBean.getCardIssuer().equalsIgnoreCase("American Express")) {
                                        imageCardType.setImageResource(R.drawable.amex_2x);
                                    }

                                }else{
                                    txtCardNo.setVisibility(View.GONE);
                                    imageCardType.setImageResource(R.drawable.paypal_icon);
                                }

                                if (!data.getTipPercentage().equalsIgnoreCase("null")) {
                                    String tip[] = data.getTipPercentage().split("\\.");
                                    textTipNote.setText("(" + tip[0] + "% selected)");
                                } else {
                                    textTipNote.setVisibility(View.GONE);
                                }

                                //List of ordered Items
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getOrderItemsList();
                                    }
                                });


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            showAlert(Constants.SERVER_ERROR);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void getOrderItemsList() {
        DatabaseHandler db = new DatabaseHandler(OrderDetailActivity.this);
        List<SuppliersBean> suppliersBeanList = data.getSuppliersBeanList();
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean suppliersBean = suppliersBeanList.get(i);
            List<OrderBean> orderBeanList = suppliersBean.getDetailOrdersBeanList();
            for (int j = 0; j < orderBeanList.size(); j++) {
                orderBean = orderBeanList.get(j);
                finalOrderBeanList.add(orderBean);
            }
        }

        List<SimpleSectionOrderdetaiViewAdapter.Section> sections =
                new ArrayList<SimpleSectionOrderdetaiViewAdapter.Section>();

        int k = 0;
        int count = 0;
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean suppliersBean = suppliersBeanList.get(i);
            List<OrderBean> orderBeanList = suppliersBeanList.get(i).getDetailOrdersBeanList();
            int allSave = 0;
            int flag = 0;
            List<String> suppItemIdStringList = new ArrayList<>();
            for (int c = 0; c < orderBeanList.size(); c++) {
                String isSave = orderBeanList.get(c).getIsSave();
                if (isSave.equalsIgnoreCase("NO")) {
                    allSave = 0;
                    flag = 1;
                } else {
                    String str = db.getLikeItem(orderBeanList.get(c).getSupplierItemId());
                    if (str == null) {
                        db.addLikeItem(orderBeanList.get(c).getSupplierItemId());
                    }
                    if (flag == 1) {
                        allSave = 0;
                    } else {
                        allSave = 1;
                    }
                }
                suppItemIdStringList.add(orderBeanList.get(c).getSupplierItemId());

            }
            String storeId = suppliersBean.getSupplierId();
            if (flag == 0) {
                sections.add(new SimpleSectionOrderdetaiViewAdapter.Section(k, suppliersBean.getSupplierName(), suppliersBean.getSupplierId(), "yes",
                        suppItemIdStringList, suppliersBean.getDeliveryDate(), suppliersBean.getDeliveryTime(), suppliersBean.getOrderSupplierId(),
                        suppliersBean.getDeliveryStatus(), suppliersBean.getDeliveryStatusDisplay(), suppliersBean.getIsOrderCancelable()));
            } else {
                sections.add(new SimpleSectionOrderdetaiViewAdapter.Section(k, suppliersBean.getSupplierName(), suppliersBean.getSupplierId(), "no",
                        suppItemIdStringList, suppliersBean.getDeliveryDate(), suppliersBean.getDeliveryTime(), suppliersBean.getOrderSupplierId(),
                        suppliersBean.getDeliveryStatus(),suppliersBean.getDeliveryStatusDisplay(), suppliersBean.getIsOrderCancelable()));
            }

            count += orderBeanList.size();
            k = count;

        }

        for (int c = 0; c < finalOrderBeanList.size(); c++) {
            String id = finalOrderBeanList.get(c).getSupplierItemId();
            if (finalOrderBeanList.get(c).getIsSave().equalsIgnoreCase("yes")) {
                String str = db.getLikeItem(id);
                if (str == null) {
                    db.addLikeItem(id);
                }
            }
        }

        adapter = new OrderDetailAdapter(OrderDetailActivity.this, finalOrderBeanList, OrderDetailActivity.this, orderType);

        SimpleSectionOrderdetaiViewAdapter.Section[] dummy = new SimpleSectionOrderdetaiViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SimpleSectionOrderdetaiViewAdapter(OrderDetailActivity.this, R.layout.order_detail_section_header, R.id.text_store, adapter, OrderDetailActivity.this);
        mSectionedAdapter.setSections(sections.toArray(dummy));


        WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(OrderDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mSectionedAdapter);
        recyclerView.setNestedScrollingEnabled(false);

       Global.hideProgress();

    }


    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(OrderDetailActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OrderDetailActivity.this);
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

    public void reOrderAll(String suppId) {
        DatabaseHandler db = new DatabaseHandler(OrderDetailActivity.this);
        List<SuppliersBean> suppliersBeanList = data.getSuppliersBeanList();
        List<OrderBean> orderedBeanList = null;
        String supplierName = null;

        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean supplierBean = suppliersBeanList.get(i);
            if (supplierBean.getSupplierId().equalsIgnoreCase(suppId)) {
                supplierName = supplierBean.getSupplierName();
                orderedBeanList = supplierBean.getDetailOrdersBeanList();
                break;
            }
        }

        SuppliersBean suppliersBean = db.getSupplier(suppId);
        if (suppliersBean == null) {
            SuppliersBean newSuppliersBean = new SuppliersBean();
            newSuppliersBean.setSupplierId(suppId);
            newSuppliersBean.setSupplierName(supplierName);
            db.addSupplier(newSuppliersBean);
        }
        for (int k = 0; k < orderedBeanList.size(); k++) {
            orderBean = db.getOrder(orderedBeanList.get(k).getSupplierItemId());
            if (orderBean == null) {
                OrderBean newOrder = new OrderBean();
                newOrder.setSupplierItemId(orderedBeanList.get(k).getSupplierItemId());
                newOrder.setItemName(orderedBeanList.get(k).getItemName());
                newOrder.setSize(orderedBeanList.get(k).getSize());
                newOrder.setShippingWeight(orderedBeanList.get(k).getShippingWeight());
                newOrder.setUnitPrice(orderedBeanList.get(k).getRegularPrice());
                newOrder.setFinalPrice(orderedBeanList.get(k).getTotalPrice());
                newOrder.setOrderQuantity(orderedBeanList.get(k).getOrderQuantity());
                newOrder.setSubstitutionWith("Stores's choice");
                newOrder.setSupplierId(suppId);
                if (orderedBeanList.get(k).getIsSubTaxApplicable().equalsIgnoreCase("true") || orderedBeanList.get(k).getIsTaxable().equalsIgnoreCase("true")) {
                    newOrder.setIsTaxable("TRUE");

                    double taxAmount = Double.parseDouble(orderedBeanList.get(k).getTaxRate()) * Double.parseDouble(orderedBeanList.get(k).getRegularPrice());

                    newOrder.setTaxAmount(taxAmount);
                } else {
                    newOrder.setIsTaxable("FALSE");
                    newOrder.setTaxAmount(0);
                }
                newOrder.setRegularPrice(orderedBeanList.get(k).getRegularPrice());
                newOrder.setThumbnail(orderedBeanList.get(k).getThumbnail());
                if (orderedBeanList.get(k).getBrandName() != null) {
                    newOrder.setBrandName(orderedBeanList.get(k).getBrandName());
                } else {
                    newOrder.setBrandName("");
                }
                newOrder.setApplicableSaleDescription("");
                newOrder.setUom(orderedBeanList.get(k).getUom());

                if (orderedBeanList.get(k).getMaxQuantity() == null) {
                    newOrder.setMaxQuantity("0");
                } else if (orderedBeanList.get(k).getMaxQuantity().equalsIgnoreCase("null")) {
                    newOrder.setMaxQuantity("0");
                } else {
                    newOrder.setMaxQuantity(orderedBeanList.get(k).getMaxQuantity());
                }
                db.addOrderInCart(newOrder);
            }
        }

        double orderTotalAmount = 0;
        List<OrderBean> ordersList = db.getAllOrders();
        for (int j = 0; j < ordersList.size(); j++) {
            orderTotalAmount += Double.parseDouble(ordersList.get(j).getFinalPrice());
        }

        if (Global.promotionAvailableBean != null) {
            if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());
                if (orderTotalAmount > minamount) {
                    Global.isFreeDelivery = true;
                } else {
                    Global.isFreeDelivery = false;
                }

            }
        }


        Intent intent = new Intent(OrderDetailActivity.this, CheckoutActivity.class);
        startActivity(intent);
    }

    public void cancelOrderConfirmation(final String orderSuppilerId) {
        Global.showProgress(OrderDetailActivity.this);
        RequestQueue rq = Volley.newRequestQueue(OrderDetailActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("order_id", data.getOrderId());
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("order_supplier_id", orderSuppilerId);
            params.put("timezone", tz.getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/cancel/confirmation", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                String msg = dataObj.getString("message");
                                showConfirmCancel(msg, orderSuppilerId);
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("status"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlert("Can not cancel order right now!!");
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               Global.hideProgress();

                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);
                } else {
                   Global.hideProgress();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
    }

    public void cancelFromServer(String orderSuppilerId) {
        RequestQueue rq = Volley.newRequestQueue(OrderDetailActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("order_id", data.getOrderId());
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("order_supplier_id", orderSuppilerId);
            params.put("timezone", tz.getID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/cancel", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                showSuccessAlert("You order has been Cancelled");
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlert("Can not cancel order right now!!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               Global.hideProgress();

                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);

                } else {
                   Global.hideProgress();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(70 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void showSuccessAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(OrderDetailActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OrderDetailActivity.this);
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
                Intent intent = new Intent(OrderDetailActivity.this, OrdersActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showConfirmCancel(String msg, final String orderSuppilerId) {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderDetailActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.confirm_order_cancel, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDetailActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();

        TextView textTitle = (TextView) promptsView.findViewById(R.id.text_title_msg);
        TextView textMessage = (TextView) promptsView.findViewById(R.id.text_cancel_message);
        Button btnClose = (Button) promptsView.findViewById(R.id.button_close);
        Button btnCancelOrder = (Button) promptsView.findViewById(R.id.button_cancel_order);

        textTitle.setTypeface(andBold);
        textMessage.setTypeface(andBold);
        btnClose.setTypeface(andBold);
        btnCancelOrder.setTypeface(andBold);

        textMessage.setText(msg);

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelFromServer(orderSuppilerId);
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        this.menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_order_reciept) {
            Intent intent = new Intent(OrderDetailActivity.this, OrderRecieptActivity.class);
            intent.putExtra("order_supplier_id", orderSupplierId);
            intent.putExtra("orderid", orderid);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshadapter() {
        List<SimpleSectionOrderdetaiViewAdapter.Section> sections =
                new ArrayList<SimpleSectionOrderdetaiViewAdapter.Section>();

        DatabaseHandler db = new DatabaseHandler(OrderDetailActivity.this);
        List<SuppliersBean> suppliersBeanList = data.getSuppliersBeanList();

        int k = 0;
        int count = 0;
        for (int i = 0; i < suppliersBeanList.size(); i++) {
            SuppliersBean suppliersBean = suppliersBeanList.get(i);
            List<OrderBean> orderBeanList = suppliersBeanList.get(i).getDetailOrdersBeanList();
            int allSave = 0;
            int flag = 0;
            List<String> suppItemIdStringList = new ArrayList<>();
            for (int c = 0; c < finalOrderBeanList.size(); c++) {
                String isSave = finalOrderBeanList.get(c).getIsSave();
                if (isSave.equalsIgnoreCase("NO")) {
                    allSave = 0;
                    flag = 1;
                } else {
                    if (flag == 1) {
                        allSave = 0;
                    } else {
                        allSave = 1;
                    }
                }
                suppItemIdStringList.add(finalOrderBeanList.get(c).getSupplierItemId());

            }
            String storeId = suppliersBean.getSupplierId();
            if (flag == 0) {
                sections.add(new SimpleSectionOrderdetaiViewAdapter.Section(k, suppliersBean.getSupplierName(), suppliersBean.getSupplierId(), "yes", suppItemIdStringList, suppliersBean.getDeliveryDate(),
                        suppliersBean.getDeliveryTime(), suppliersBean.getOrderSupplierId(),suppliersBean.getDeliveryStatus(), suppliersBean.getDeliveryStatusDisplay(), suppliersBean.getIsOrderCancelable()));
            } else {
                sections.add(new SimpleSectionOrderdetaiViewAdapter.Section(k, suppliersBean.getSupplierName(), suppliersBean.getSupplierId(), "no", suppItemIdStringList,
                        suppliersBean.getDeliveryDate(), suppliersBean.getDeliveryTime(), suppliersBean.getOrderSupplierId(),suppliersBean.getDeliveryStatus(), suppliersBean.getDeliveryStatusDisplay(), suppliersBean.getIsOrderCancelable()));
            }

            count += orderBeanList.size();
            k = count;

        }

        for (int c = 0; c < finalOrderBeanList.size(); c++) {
            String id = finalOrderBeanList.get(c).getSupplierItemId();
            if (finalOrderBeanList.get(c).getIsSave().equalsIgnoreCase("yes")) {
                String str = db.getLikeItem(id);
                if (str == null) {
                    db.addLikeItem(id);
                }
            }
        }

        SimpleSectionOrderdetaiViewAdapter.Section[] dummy = new SimpleSectionOrderdetaiViewAdapter.Section[sections.size()];
        adapter.changeList(finalOrderBeanList);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        mSectionedAdapter.changeadapter(adapter);
    }
}
