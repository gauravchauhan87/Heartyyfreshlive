package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.heartyy.heartyyfresh.adapter.*;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DeliveryTimesActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    private HorizontalListView dateListView;
    private CustomDateListAdapter dateListAdapter;
    private ListView individualListView;
    private CustomIndividualStoreAdapter adapter;
    private List<IndividualDeliveryBean> individualDeliveryBeanList;
    private RelativeLayout carLayout, indiviualDeliveryTimeLayout;
    private TextView textcombineddeliveryprice, textcombineddeliverytext, textindividualdeliverytext, textseparatechardes;
    public TextView individualPriceText;
    private ImageView carImage;
    private RelativeLayout combinedDeliveryTimeLayout, dateViewLayout, deliverySlotsLayout, combineLayout;
    private StickyScrollView scroll;
    private int combineHeight, dateHeight, slotsHeight;
    private Button completeOrderBtn;
    private DeliveryWindowBean deliveryWindowBean;
    private HorizontalListView deliveryListView;
    private CustomDeliverySlotsListAdapter dladapter;
    private SharedPreferences pref;
    private ImageView combineImage, individualImage;
    private String estimatedfee;
    private double totalDeliveryFee = 0;
    public String deliveryType;
    String individualDeliveryPrice,combinedDeliveryFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_times);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_checkout));
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

        dateListView = (HorizontalListView) findViewById(R.id.list_dates);
        individualListView = (ListView) findViewById(R.id.individualcardlist);
        carLayout = (RelativeLayout) findViewById(R.id.layout_car);
        carImage = (ImageView) findViewById(R.id.image_car);
        combinedDeliveryTimeLayout = (RelativeLayout) findViewById(R.id.layout_combined_delivery_time);
        combineLayout = (RelativeLayout) findViewById(R.id.layout_combine);
        dateViewLayout = (RelativeLayout) findViewById(R.id.layout_date_view);
        deliverySlotsLayout = (RelativeLayout) findViewById(R.id.layout_delivery_slots);
        completeOrderBtn = (Button) findViewById(R.id.button_complete_order);
        scroll = (StickyScrollView) findViewById(R.id.scroll);
        deliveryListView = (HorizontalListView) findViewById(R.id.list_delivery_slots);
        combineHeight = combinedDeliveryTimeLayout.getMeasuredHeight();
        dateHeight = dateViewLayout.getMeasuredHeight();
        slotsHeight = deliverySlotsLayout.getMeasuredHeight();
        textcombineddeliveryprice = (TextView) findViewById(R.id.text_combined_delivery_price);
        textcombineddeliverytext = (TextView) findViewById(R.id.text_combined_delivery_text);
        textindividualdeliverytext = (TextView) findViewById(R.id.text_individual_delivery_text);
        textseparatechardes = (TextView) findViewById(R.id.text_separate_chardes);
        combineImage = (ImageView) findViewById(R.id.image_combine);
        individualImage = (ImageView) findViewById(R.id.image_individual);
        indiviualDeliveryTimeLayout = (RelativeLayout) findViewById(R.id.layout_individual_delivery_time);
        individualPriceText = (TextView) findViewById(R.id.text_individual_delivery_price);


        dateListView.setEnabled(false);
        deliveryListView.setEnabled(false);
        completeOrderBtn.setEnabled(false);
        individualListView.setEnabled(false);
        indiviualDeliveryTimeLayout.clearFocus();
        textcombineddeliveryprice.setTypeface(andBold);
        textcombineddeliverytext.setTypeface(light);
        textindividualdeliverytext.setTypeface(light);
        textseparatechardes.setTypeface(italic);
        completeOrderBtn.setTypeface(andBold);

        combineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.isIndividualDelivery = false;
                if (deliveryWindowBean != null) {
                    if (deliveryWindowBean.getCombineDeliveryBean() != null) {
                        deliveryType = "combine";
                        dateListView.setEnabled(true);
                        deliveryListView.setEnabled(true);
                        completeOrderBtn.setEnabled(true);
                        combineImage.setVisibility(View.GONE);
                        completeOrderBtn.setBackgroundResource(R.drawable.button_red);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.DELIVERY_TYPE, Constants.COMBINE);
                        editor.commit();
                      //estimatedfee = deliveryWindowBean.getCombineDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                        individualImage.setVisibility(View.VISIBLE);
                        individualListView.setEnabled(false);
                    }
                }
            }
        });

        individualImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.combinedDeliverDay = null;
                Global.isIndividualDelivery = true;
                if (deliveryWindowBean != null) {
                    if (deliveryWindowBean.getSingleDeliveryBean() != null) {
                        deliveryType = "individual";
                        individualListView.setEnabled(true);
                        completeOrderBtn.setEnabled(true);
                        individualImage.setVisibility(View.GONE);
                        completeOrderBtn.setBackgroundResource(R.drawable.button_red);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.DELIVERY_TYPE, Constants.INDIVIDUAL);
                        editor.commit();
                     //   estimatedfee = deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix();
                        dateListView.setEnabled(false);
                        deliveryListView.setEnabled(false);
                        combineImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        getDeliveryTimes();


        dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OperatingHourBean operatingHourBean = (OperatingHourBean) adapterView.getItemAtPosition(i);
                adapterView.setSelection(i);
                if (operatingHourBean.getIsClose().equalsIgnoreCase("no")) {
                    dateListAdapter.setSelected(i);
                    Global.datePos = i;
                    List<TimeIntervalBean> timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                    Global.combinedDeliverDay = operatingHourBean.getDayOfWeek();
                    Global.dayOfWeek = operatingHourBean.getDayOfWeek();

                    dladapter.changeSlot(timeIntervalBeanList);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.DELIVERY_DATE, operatingHourBean.getDate());
                    editor.putString(Constants.DELIVERY_FROM, timeIntervalBeanList.get(0).getStartTime());
                    editor.putString(Constants.DELIVERY_TO, timeIntervalBeanList.get(0).getEndTime());
                    editor.commit();

                    Global.timeIntervalBean = timeIntervalBeanList.get(0);
                    Global.operatingHourBean = operatingHourBean;

                    String dateTime[] = operatingHourBean.getTimeIntervalBeanList().get(0).getStartTime().split(" ");
                    String time[] = dateTime[1].split(":");
                    int timePost = Integer.parseInt(time[0]);
                    if (timePost >= 18) {
                        estimatedfee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                        Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostAfterSix();
                        combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                        textcombineddeliveryprice.setText("$"+combinedDeliveryFee);
                    } else {
                        estimatedfee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix();
                  //      Global.estimatedFee = estimatedfee;
                        Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();
                        combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix();
                        textcombineddeliveryprice.setText("$"+combinedDeliveryFee);
                    }
                }

            }
        });

        deliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeIntervalBean timeIntervalBean = (TimeIntervalBean) adapterView.getItemAtPosition(i);
                dladapter.setSelected(i);
                Global.slotPos = i;
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.DELIVERY_FROM, timeIntervalBean.getStartTime());
                editor.putString(Constants.DELIVERY_TO, timeIntervalBean.getEndTime());
                editor.commit();
                Global.timeIntervalBean = timeIntervalBean;
                //deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(0).getDayOfWeek();

                String dateTime[] = timeIntervalBean.getStartTime().split(" ");
                String time[] = dateTime[1].split(":");
                int timePost = Integer.parseInt(time[0]);
                if (timePost >= 18) {
                    estimatedfee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                    Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostAfterSix();
                    combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                    textcombineddeliveryprice.setText("$"+combinedDeliveryFee);
                } else if(timePost < 18){
                    estimatedfee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();
                    Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix();
                    combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix();
                    textcombineddeliveryprice.setText("$"+combinedDeliveryFee);
                }
            }
        });


        completeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.getString(Constants.DELIVERY_TYPE,null).equalsIgnoreCase(Constants.INDIVIDUAL)){
                    int flag=0;
                    for (Map.Entry<String, SupplierDeliveryScheduleBean> entry : Global.map.entrySet()){
                        SupplierDeliveryScheduleBean scheduleBean = entry.getValue();
                        String dateTime[] = scheduleBean.getDeliveryFrom().split(" ");
                        String time[] = dateTime[1].split(":");
                        int timePost = Integer.parseInt(time[0]);
                        if (timePost >= 18){
                            flag++;
                        }
                    }
                }

                if(deliveryType.equalsIgnoreCase("combine")){
                    Global.estimatedFee = combinedDeliveryFee;
                }else if(deliveryType.equalsIgnoreCase("individual")){
                    Global.estimatedFee = individualDeliveryPrice;
                }

                Intent intent = new Intent(DeliveryTimesActivity.this, CompleteOrderActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getDeliveryTimes() {
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
        Global.showProgress(DeliveryTimesActivity.this);
        DatabaseHandler db = new DatabaseHandler(this);
        String supplierId = "";
        String shippingWeight = "";
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
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
        supplierId = supplierId.substring(0, supplierId.length() - 1);
        shippingWeight = shippingWeight.substring(0, shippingWeight.length() - 1);
        String addressId = "";
        if(Global.locationBean!=null){
            addressId = Global.locationBean.getUserDeliveryLocationId();
        }
        String url = "store/operation/window?supplier_id=" + supplierId + "&timezone=" + tz.getID() + "&user_id=" + pref.getString(Constants.USER_ID, null) + "&zipcode=" + Global.zip + "&shipping_weight=" + shippingWeight+"&address_id="+addressId;
        Log.d("delivery url..",Constants.URL + url);

        RequestQueue rq = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                deliveryWindowBean = ConversionHelper.convertDeliveryJSonToDeliveryBean(jsonObject);
                                List<TimeIntervalBean> timeIntervalBeanList = null;
                                if (deliveryWindowBean.getCombineDeliveryBean() != null) {
                                    combineLayout.setVisibility(View.VISIBLE);
                                    combinedDeliveryTimeLayout.setVisibility(View.VISIBLE);

                                    int j=0;

                                    if(deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(j).getIsClose().equalsIgnoreCase("YES")){
                                        j++;
                                    }

                                    String[] dateTime = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(j).getTimeIntervalBeanList().get(0).getStartTime().split(" ");
                                    String[] startTimeFullString = dateTime[1].split(":");
                                    String hour = startTimeFullString[0];
                                    int hrValue = Integer.parseInt(hour);

                                    if(hrValue>=18){
                                        System.out.println("start time is after congestion time");
                                        textcombineddeliveryprice.setText("$" + deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix());
                                        combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeAfterSix();
                                        Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostAfterSix();
                                        Global.combinedDeliverDay = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(0).getDayOfWeek();
                                        Global.dayOfWeek = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(0).getDayOfWeek();
                                    }else{
                                        System.out.println("You selected regular delivery");
                                        textcombineddeliveryprice.setText("$" + deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix());
                                        combinedDeliveryFee = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryEstimatedChargeBeforeSix();
                                        Global.congestionCost = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getDeliveryCongestionCostBeforeSix();
                                        Global.combinedDeliverDay = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(0).getDayOfWeek();
                                        Global.dayOfWeek = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList().get(0).getDayOfWeek();
                                    }

                                    List<OperatingHourBean> operatingHourBeanList = deliveryWindowBean.getCombineDeliveryBean().getWorkingDaysBeanList().get(0).getOperatingHourBeanList();
                                    int pos = -1;
                                    for (int i = 0; i < operatingHourBeanList.size(); i++) {
                                        OperatingHourBean operatingHourBean = operatingHourBeanList.get(i);
                                        if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                                            pos = i;
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString(Constants.DELIVERY_DATE, operatingHourBean.getDate());
                                            editor.commit();
                                            Global.operatingHourBean = operatingHourBean;
                                            timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();

                                            break;
                                        }
                                    }



                                    dateListAdapter = new CustomDateListAdapter(DeliveryTimesActivity.this, operatingHourBeanList);
                                    dateListView.setAdapter(dateListAdapter);
                                    dateListAdapter.setSelected(pos);
                                    Global.datePos = pos;
                                    dladapter = new CustomDeliverySlotsListAdapter(DeliveryTimesActivity.this, timeIntervalBeanList);
                                    deliveryListView.setAdapter(dladapter);
                                    dladapter.setSelected(0);
                                    Global.slotPos = 0;
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(Constants.DELIVERY_FROM, timeIntervalBeanList.get(0).getStartTime());
                                    editor.putString(Constants.DELIVERY_TO, timeIntervalBeanList.get(0).getEndTime());
                                    editor.commit();
                                    Global.timeIntervalBean = timeIntervalBeanList.get(0);


                                } else {
                                    combineLayout.setVisibility(View.GONE);
                                    combinedDeliveryTimeLayout.setVisibility(View.GONE);
                                }

                                setIndividualPriceText(deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList());

//                              individualPriceText.setText("$" + deliveryWindowBean.getSingleDeliveryBean().getTotalDeliveryEstimatedFeeBeforeSix());
                                adapter = new CustomIndividualStoreAdapter(DeliveryTimesActivity.this, deliveryWindowBean.getSingleDeliveryBean().getWorkingDaysBeanList(), DeliveryTimesActivity.this);
                                individualListView.setAdapter(adapter);
                                Global.setListViewHeightBasedOnChildren(individualListView);
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) individualListView.getLayoutParams();
                                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) individualImage.getLayoutParams();
                                params1.height = params.height;
                                individualImage.setLayoutParams(params1);

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {

                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           Global.hideProgress();

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

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void setIndividualPriceText(List<WorkingDaysBean> workingDaysBeanList){
        totalDeliveryFee = 0;
        for(int i=0;i<workingDaysBeanList.size();i++){
            int j=0;
            do{
                if(workingDaysBeanList.get(i).getOperatingHourBeanList().get(j).getIsClose().equalsIgnoreCase("YES")){
                        j++;
                    }
              }while (j == workingDaysBeanList.get(i).getOperatingHourBeanList().size());

            String[] dateTime = workingDaysBeanList.get(i).getOperatingHourBeanList().get(j).getTimeIntervalBeanList().get(0).getStartTime().split(" ");
            String[] startTimeFullString = dateTime[1].split(":");
            String hour = startTimeFullString[0];
            int hrValue = Integer.parseInt(hour);

            double deliveryFee;
                    if(hrValue>=18){
                        System.out.println("start time is after congestion time");
                        deliveryFee = Double.parseDouble(workingDaysBeanList.get(i).getDeliveryEstimatedChargeAfterSix());
                    }else{
                        System.out.println("You selected regular delivery");
                        deliveryFee = Double.parseDouble(workingDaysBeanList.get(i).getDeliveryEstimatedChargeBeforeSix());
                    }
                    totalDeliveryFee += deliveryFee;
        }
        individualDeliveryPrice = String.format("%.2f", totalDeliveryFee);
        Global.estimatedFee = "0";
        individualPriceText.setText("$"+individualDeliveryPrice);
    }


    public void setIndividualDeliveryTotal(List<WorkingDaysBean> list){
        double displayPrice = 0;
        for(int k=0;k<list.size();k++){
            double individualPrice = 0;
            if(list.get(k).isCongestionDeliverSelected()){
                individualPrice = Double.parseDouble(list.get(k).getDeliveryEstimatedChargeAfterSix());
            }else if(!list.get(k).isCongestionDeliverSelected()){
                individualPrice = Double.parseDouble(list.get(k).getDeliveryEstimatedChargeBeforeSix());
            }
            displayPrice += individualPrice;
        }

        individualDeliveryPrice = String.format("%.2f", displayPrice);
        individualPriceText.setText("$"+individualDeliveryPrice);
    }


    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(DeliveryTimesActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                DeliveryTimesActivity.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            Log.d("string", "back");
            Global.map = new HashMap<>();
            Global.estimatedFee = "0";
            return false;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Log.d("string", "back");
        Global.map = new HashMap<>();


    }
}
