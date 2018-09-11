package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.OrderReceiptShopListAdapter;
import com.heartyy.heartyyfresh.adapter.OrderRecieptAdapter;
import com.heartyy.heartyyfresh.adapter.SectionedOrderRecieptAdapter;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.OrderDetailPromoBean;
import com.heartyy.heartyyfresh.bean.OrderRecieptBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
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

public class OrderRecieptActivity extends AppCompatActivity {

    Typeface andBold, bold, italic, light;
    private SharedPreferences pref;
    private ListView totalStoreListView;
    private RecyclerView orderStoreListView;
    private OrderRecieptAdapter adapter;
    private OrderReceiptShopListAdapter mAdapter;
    private List<SuppliersBean> suppliersBeansList;
    private TextView txtOrderId,txtOrderTotal,txtDeliveryDate,txtDeliveryAddress,txtContact;
    private TextView txtTax,txtDeliveryFee,txtTip,txtPromotion,txtTotal,txtCardNo,txtBagRecycleAmnt,textTipNote;
    ImageView imageCardType;
    private String orderid,orderSupplierId;
    private OrderRecieptBean orderRecieptBean;

    TextView textFinalOrder,txtOrderTotalWord,textDeliveredOn,textDeliverTo,textContactInfo,textItemsOrdered,textTotal,
            textYourTotal,textPaymentMethod,textPaymentType,textChargeInfo,creditText,creditAmountText;
    Button buttonNeedHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_reciept);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_order_reciept));
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

        orderid = getIntent().getExtras().getString("orderid");
        orderSupplierId = getIntent().getExtras().getString("order_supplier_id");

        txtOrderId = (TextView)findViewById(R.id.text_order_id);
        txtOrderTotal = (TextView)findViewById(R.id.text_order_total_amount);
        txtDeliveryDate = (TextView)findViewById(R.id.text_deliverd_date);
        txtDeliveryAddress = (TextView)findViewById(R.id.text_deliver_to_address);
        txtContact = (TextView)findViewById(R.id.text_contact_no);
        txtTax = (TextView)findViewById(R.id.text_tax_amt);
        txtDeliveryFee = (TextView)findViewById(R.id.text_delivery_amt);
        txtTip = (TextView)findViewById(R.id.text_tip_amt);
        txtPromotion = (TextView)findViewById(R.id.text_promotion_amt);
        txtTotal = (TextView)findViewById(R.id.text_total_amt);
        txtCardNo = (TextView)findViewById(R.id.text_card_number);
        imageCardType = (ImageView) findViewById(R.id.image_cardType);
        txtBagRecycleAmnt = (TextView) findViewById(R.id.text_bag_amt);
        orderStoreListView = (RecyclerView) findViewById(R.id.list_order_reciept);
        totalStoreListView = (ListView) findViewById(R.id.list_total_shops);
        textTipNote = (TextView) findViewById(R.id.text_tip_note);
        textFinalOrder = (TextView) findViewById(R.id.text_final_order);
        txtOrderTotalWord = (TextView) findViewById(R.id.text_order_total);
        textDeliveredOn = (TextView) findViewById(R.id.text_delivered_on);
        textDeliverTo = (TextView) findViewById(R.id.text_deliver_to);
        textContactInfo = (TextView) findViewById(R.id.text_contact_info);
        textItemsOrdered = (TextView) findViewById(R.id.text_items_ordered);
        textTotal = (TextView) findViewById(R.id.text_total);
        textYourTotal = (TextView) findViewById(R.id.text_your_total);
        textPaymentMethod = (TextView) findViewById(R.id.text_payment_method);
        textPaymentType = (TextView) findViewById(R.id.text_payment_type);
        textChargeInfo = (TextView) findViewById(R.id.text_charge_info);
        buttonNeedHelp = (Button) findViewById(R.id.button_need_help);
        creditText = (TextView) findViewById(R.id.text_credit);
        creditAmountText = (TextView) findViewById(R.id.text_credit_amt);

        textFinalOrder.setTypeface(light);
        txtOrderId.setTypeface(andBold);
        txtOrderTotalWord.setTypeface(light);
        txtOrderTotal.setTypeface(andBold);
        textDeliveredOn.setTypeface(light);
        txtDeliveryDate.setTypeface(andBold);
        textDeliverTo.setTypeface(light);
        txtDeliveryAddress.setTypeface(andBold);
        textContactInfo.setTypeface(light);
        txtContact.setTypeface(andBold);
        textItemsOrdered.setTypeface(light);
        ViewGroup vg = (ViewGroup)findViewById(R.id.order_detail_receipt);
        Global.setFont(vg, light);
        textYourTotal.setTypeface(andBold);
        txtTotal.setTypeface(andBold);
        textPaymentMethod.setTypeface(light);
        textPaymentType.setTypeface(light);
        txtCardNo.setTypeface(andBold);
        textChargeInfo.setTypeface(light);
        buttonNeedHelp.setTypeface(andBold);
        textTipNote.setTypeface(light);
        creditAmountText.setTypeface(light);
        creditText.setTypeface(light);


        getOrderReciept();
        txtPromotion.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));
        txtBagRecycleAmnt.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));

        buttonNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://heartyysupport.zendesk.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void mailOrderReceipt() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("order_id",orderid);
            params.put("user_id",pref.getString(Constants.USER_ID,null));
            params.put("order_supplier_id",orderSupplierId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL +"order/receipt/email", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
    }




    private void getOrderReciept() {
        Global.showProgress(OrderRecieptActivity.this);
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {

            params.put("order_id",orderid);
            params.put("user_id",pref.getString(Constants.USER_ID,null));
            params.put("order_supplier_id",orderSupplierId);
            params.put("timezone",tz.getID());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/receipt", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                            orderRecieptBean = ConversionHelper.convertOrderRecieptJsonToOrderRecieptBean(jsonObject);
                                if(orderRecieptBean!=null){
                                    txtOrderId.setText(" "+orderRecieptBean.getDisplayOrderId());
                                    txtOrderTotal.setText(" $"+orderRecieptBean.getOrderTotal());
                                    txtDeliveryDate.setText(" "+orderRecieptBean.getDeliveryDate());
                                    txtDeliveryAddress.setText(orderRecieptBean.getDeliverTo());
                                    txtContact.setText(orderRecieptBean.getContactInfo());
                                    textPaymentType.setText(orderRecieptBean.getPaymentCardBean().getCardIssuer());
                                    if(!orderRecieptBean.getPaymentCardBean().getCardLastFourDigit().equalsIgnoreCase("null")){

                                        txtCardNo.setVisibility(View.VISIBLE);
                                        txtCardNo.setText("XXXX-" + orderRecieptBean.getPaymentCardBean().getCardLastFourDigit());

                                        if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("Visa")) {
                                            imageCardType.setImageResource(R.drawable.visa_2x);
                                        } else if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("MasterCard")) {
                                            imageCardType.setImageResource(R.drawable.mastercard_2x);
                                        } else if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("Discover")) {
                                            imageCardType.setImageResource(R.drawable.discover_2x);
                                        } else if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("JCB")) {
                                            imageCardType.setImageResource(R.drawable.jcb_2x);
                                        } else if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("Maestro")) {
                                            imageCardType.setImageResource(R.drawable.maestro_2x);
                                        } else if (orderRecieptBean.getPaymentCardBean().getCardIssuer().equalsIgnoreCase("American Express")) {
                                            imageCardType.setImageResource(R.drawable.amex_2x);
                                        }
                                    }else{
                                        txtCardNo.setVisibility(View.VISIBLE);
                                        imageCardType.setImageResource(R.drawable.paypal_icon);
                                    }



                                    if(!orderRecieptBean.getTipPercent().equalsIgnoreCase("null")){
                                        String tip[] = orderRecieptBean.getTipPercent().split("\\.");
                                        textTipNote.setText("(" + tip[0] + "% selected)");
                                    }else{
                                        textTipNote.setVisibility(View.GONE);
                                    }



                                    String taxTemp[] = orderRecieptBean.getTax().split("\\.");
                                    if (taxTemp.length > 1) {
                                        txtTax.setText(Html.fromHtml("<sup>$</sup><big>" + taxTemp[0] + "</big><sup>" + taxTemp[1] + "</sup>"));
                                    } else {
                                        txtTax.setText(Html.fromHtml("<sup>$</sup><big>" + taxTemp[0] + "</big><sup>" + "0" + "</sup>"));
                                    }

                                    if(orderRecieptBean.getDeliveryCharge().equalsIgnoreCase("null")){
                                        txtDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + "0" + "</big><sup>" + "00" + "</sup>"));
                                    }else {
                                        String tempDelivery[] = orderRecieptBean.getDeliveryCharge().split("\\.");
                                        if (tempDelivery.length > 1) {
                                            txtDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempDelivery[0] + "</big><sup>" + tempDelivery[1] + "</sup>"));
                                        } else {
                                            txtDeliveryFee.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + tempDelivery[0] + "</big><sup>" + "0" + "</sup>"));
                                        }
                                    }

                                    String tipTemp[] = orderRecieptBean.getTipAmount().split("\\.");
                                    if (tipTemp.length > 1) {
                                        txtTip.setText(Html.fromHtml("<sup>$</sup><big>" + tipTemp[0] + "</big><sup>" + tipTemp[1] + "</sup>"));
                                    } else {
                                        txtTip.setText(Html.fromHtml("<sup>$</sup><big>" + tipTemp[0] + "</big><sup>" + "0" + "</sup>"));
                                    }


                                    String totalTemp[] = orderRecieptBean.getOrderTotal().split("\\.");
                                    if (totalTemp.length > 1) {
                                        txtTotal.setText(Html.fromHtml("<sup>$</sup><big>" + totalTemp[0] + "</big><sup>" + totalTemp[1] + "</sup>"));
                                    } else {
                                        txtTotal.setText(Html.fromHtml("<sup>$</sup><big>" + totalTemp[0] + "</big><sup>" + "0" + "</sup>"));
                                    }

                                    OrderDetailPromoBean orderDetailPromoBean = orderRecieptBean.getOrderDetailPromoBean();
                                    if(orderDetailPromoBean!=null){
                                        String promo[] = String.format("%.2f",Double.parseDouble(orderDetailPromoBean.getPromoAmount())).split("\\.");

                                        txtPromotion.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + promo[0] + "</big><sup>" + promo[1] + "</sup><big>)</big>"));
                                    }

                                    if(orderRecieptBean.getCredits().equalsIgnoreCase("null")){
                                        creditAmountText.setVisibility(View.GONE);
                                        creditText.setVisibility(View.GONE);
                                    }else{

                                        creditAmountText.setVisibility(View.VISIBLE);
                                        creditText.setVisibility(View.VISIBLE);

                                        String cred[] = String.format("%.2f", Double.parseDouble(orderRecieptBean.getCredits())).split("\\.");

                                        creditAmountText.setText(Html.fromHtml("<big>(</big><sup> $</sup><big>" + cred[0] + "</big><sup>" + cred[1] + "</sup><big>)</big>"));
                                    }

                                    List<OrderBean> detailOrderBeanList = new ArrayList<>();

                                    List<SectionedOrderRecieptAdapter.Section> sections =
                                            new ArrayList<SectionedOrderRecieptAdapter.Section>();

                                    int k = 0;
                                    int count = 0;
                                    for(int i=0;i<orderRecieptBean.getSuppliersBeanList().size();i++){
                                        sections.add(new SectionedOrderRecieptAdapter.Section(k,orderRecieptBean.getSuppliersBeanList().get(i).getSupplierName()));
                                        count += orderRecieptBean.getSuppliersBeanList().get(i).getDetailOrdersBeanList().size();
                                        k = count;
                                        for(int j=0;j<orderRecieptBean.getSuppliersBeanList().get(i).getDetailOrdersBeanList().size();j++){
                                            detailOrderBeanList.add(orderRecieptBean.getSuppliersBeanList().get(i).getDetailOrdersBeanList().get(j));
                                        }
                                    }


                                    adapter = new OrderRecieptAdapter(OrderRecieptActivity.this,detailOrderBeanList,OrderRecieptActivity.this);

                                    SectionedOrderRecieptAdapter.Section[] dummy = new SectionedOrderRecieptAdapter.Section[sections.size()];
                                     SectionedOrderRecieptAdapter mSectionedAdapter = new
                                             SectionedOrderRecieptAdapter(OrderRecieptActivity.this, R.layout.order_reciept_store_list_item, R.id.text_store_name, adapter, OrderRecieptActivity.this);
                                    mSectionedAdapter.setSections(sections.toArray(dummy));


                                    WrappingLinearLayoutManager layoutManager
                                            = new WrappingLinearLayoutManager(OrderRecieptActivity.this);
                                    orderStoreListView.setLayoutManager(layoutManager);
                                    orderStoreListView.setHasFixedSize(true);
                                    orderStoreListView.setAdapter(mSectionedAdapter);
                                    orderStoreListView.setNestedScrollingEnabled(false);


                                    mAdapter = new OrderReceiptShopListAdapter(OrderRecieptActivity.this,orderRecieptBean.getSuppliersBeanList());
                                    totalStoreListView.setAdapter(mAdapter);
                                    Global.setListViewHeightBasedOnChildren(totalStoreListView);
                                }


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

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(OrderRecieptActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                OrderRecieptActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_reciept, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_email_receipt) {
            mailOrderReceipt();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
