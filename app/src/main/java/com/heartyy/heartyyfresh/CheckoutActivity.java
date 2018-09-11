package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.heartyy.heartyyfresh.adapter.*;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    Typeface andBold, bold, italic, light;
    RecyclerView recyclerView;
    private CustomCheckoutAdapter adapter;
    private List<OrderBean> finalOrderBeanList;
    private Button chooseDeliveryBtn;
    private SharedPreferences pref;
    public static SimpleSectionedRecyclerViewAdapter mSectionedAdapter;
    private List<CheckDeliveryBean> checkDeliveryBeanList;
    private List<StorePromotionBean> storePromotionBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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

        recyclerView = (RecyclerView) findViewById(R.id.list_checkout);
        chooseDeliveryBtn = (Button) findViewById(R.id.button_choose_delivery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checkDelivery();


        chooseDeliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);
                List<SuppliersBean> suppliersBeanList1 = db.getAllSuppliers();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.DELIVERY_DATE, null);
                editor.putString(Constants.DELIVERY_FROM, null);
                editor.putString(Constants.DELIVERY_TO, null);
                if (suppliersBeanList1.size() == 1) {
                    editor.putString(Constants.DELIVERY_TYPE, Constants.INDIVIDUAL);
                } else {
                    editor.putString(Constants.DELIVERY_TYPE, null);
                }
                editor.commit();
                Global.map = new HashMap<String, SupplierDeliveryScheduleBean>();
                Global.datePos = -1;
                Global.slotPos = -1;
                CompleteOrderActivity.creditAmount = 0;
                CompleteOrderActivity.promotionAvailableBean = null;
                Intent intent = new Intent(CheckoutActivity.this, CompleteOrderActivity.class);
                Global.estimatedFee = "0";
                startActivity(intent);

            }
        });
    }

    private void checkDelivery() {
        Global.showProgress(CheckoutActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);

        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();
            List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
            String[] array = new String[suppliersBeanList.size()];
            for (int i = 0; i < suppliersBeanList.size(); i++) {
                array[i] = suppliersBeanList.get(i).getSupplierId();
                supArray.put(suppliersBeanList.get(i).getSupplierId());
            }
            params.put("zipcode", Global.zip);
            params.put("supplier_id", supArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "check/delivery", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("delivery response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                checkDeliveryBeanList = ConversionHelper.convertCheckDeliveryJsonToCheckDeliveryBean(jsonObject);
                                DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);
                                List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                                for (int i = 0; i < suppliersBeanList.size(); i++) {
                                    if (checkDeliveryBeanList.size() > 0) {
                                        int flag = 0;
                                        for (int c = 0; c < checkDeliveryBeanList.size(); c++) {
                                            CheckDeliveryBean bean = checkDeliveryBeanList.get(c);
                                            if (bean.getSupplierId().equalsIgnoreCase(suppliersBeanList.get(i).getSupplierId())) {
                                                flag = 1;
                                                break;
                                            }

                                        }

                                        if (flag == 1) {
                                            SuppliersBean sup = suppliersBeanList.get(i);
                                            sup.setSupplierAvailable("no");
                                            db.updateSupplier(sup);
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
                                checkStorePromotion("main");
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

    public void getCheckoutData() {
        finalOrderBeanList = new ArrayList<>();
        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
        DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        if (suppliersBeanList != null) {
            if (suppliersBeanList.size() > 0) {
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBeanList.get(i));
                    for (int j = 0; j < orderBeanList.size(); j++){
                        orderBeanList.get(j).setHeaderPosition(i);
                    //    orderBeanList.get(j).setSubstitutionWith("Store's choice");
                        finalOrderBeanList.add(orderBeanList.get(j));
                    }
                }

                int k = 0;
                int count = 0;
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBeanList.get(i));
                    double totalPrice = 0;
                    int allSave = 0;
                    int flag = 0;
                    List<String> suppItemIdStringList = new ArrayList<>();
                    for (int c = 0; c < orderBeanList.size(); c++) {
                        double price = Double.parseDouble(orderBeanList.get(c).getFinalPrice());
                        totalPrice += price;
                        String isSave = db.getLikeItem(orderBeanList.get(c).getSupplierItemId());
                        if (isSave == null) {
                            allSave = 0;
                            flag = 1;
                        } else {
                            if (flag == 1) {
                                allSave = 0;
                            } else {
                                allSave = 1;
                            }
                        }
                        suppItemIdStringList.add(orderBeanList.get(c).getSupplierItemId());

                    }
                     SuppliersBean sup = suppliersBeanList.get(i);
                     if(sup.getSupplierAvailable().equalsIgnoreCase("yes")){

                         if (storePromotionBeanList != null) {
                             StorePromotionBean storePromotionBean = null;
                             int store = 0;
                             for(int c=0;c<storePromotionBeanList.size();c++){
                                 storePromotionBean = storePromotionBeanList.get(c);

                                 if(suppliersBeanList.get(i).getSupplierId().equalsIgnoreCase(storePromotionBean.getSupplierId())){
                                     store = 1;
                                     break;
                                 }
                             }
                             if(store==1){
                                 sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),storePromotionBean.getDiscountAmount()));
                                 sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, storePromotionBean.getMessage(), "minimum", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),storePromotionBean.getDiscountAmount()));
                             }else{
                                 sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));
                             }

                         } else {

                             sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));


                         }

                     }else{
                         sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));
                         if(checkDeliveryBeanList.size()>0){
                             for(int c=0;c<checkDeliveryBeanList.size();c++){
                                 if(checkDeliveryBeanList.get(c).getSupplierId().equalsIgnoreCase(sup.getSupplierId())){
                                     sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, checkDeliveryBeanList.get(c).getMessage(), "minimum", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                                     break;
                                 }
                             }
                         }

                     }


                    if (flag == 0) {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, "Section 1", "save", "yes", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                    } else {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, "Section 1", "save", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                    }


                    count += orderBeanList.size();
                    k = count;

                }
            }
        }

        adapter = new CustomCheckoutAdapter(this, finalOrderBeanList, this);

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this, R.layout.sction_checkout, R.id.section_text, adapter, this);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        recyclerView.setAdapter(mSectionedAdapter);

        Global.dialog.dismiss();


    }

    public void checkStorePromotion(final String type) {
        if(type.equalsIgnoreCase("main")){

        }else{
            Global.showProgress(CheckoutActivity.this);
        }

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        final DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);
        double price, salesPrice;

        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();

            List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
            for (int i = 0; i < suppliersBeanList.size(); i++) {
                SuppliersBean suppliersBean = suppliersBeanList.get(i);
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
            params.put("suppliers", supArray);
            params.put("zipcode", Global.zip);


        } catch (JSONException e) {
            Log.d("catch","Catch error");
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
                                if(type.equalsIgnoreCase("main")) {
                                    getCheckoutData();
                                }else{
                                    refreshAdapter();
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
                .from(CheckoutActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CheckoutActivity.this);
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

    public void refreshAdapter() {
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
        double checktotal=0;
        DatabaseHandler db = new DatabaseHandler(CheckoutActivity.this);
        List<OrderBean> finalOrderBeanList = new ArrayList<>();
        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
        if (suppliersBeanList != null) {
            if (suppliersBeanList.size() > 0) {
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBeanList.get(i));
                    for (int j = 0; j < orderBeanList.size(); j++) {
                        orderBeanList.get(j).setHeaderPosition(i);
                        //orderBeanList.get(j).setSubstitutionWith("Store's choice");
                        checktotal += Double.parseDouble(orderBeanList.get(j).getFinalPrice());
                        finalOrderBeanList.add(orderBeanList.get(j));
                    }
                }

                if (Global.promotionAvailableBean != null) {
                    if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                        if (checktotal >= Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {
                            Global.isFreeDelivery = true;
                        }else{
                            Global.isFreeDelivery = false;
                        }
                    }else{
                        Global.isFreeDelivery = false;
                    }
                }else{
                    Global.isFreeDelivery = false;
                }

                int k = 0;
                int count = 0;
                for (int i = 0; i < suppliersBeanList.size(); i++) {
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBeanList.get(i));
                    double totalPrice = 0;
                    int allSave = 0;
                    int flag = 0;
                    List<String> suppItemIdStringList = new ArrayList<>();
                    for (int c = 0; c < orderBeanList.size(); c++) {
                        double price = Double.parseDouble(orderBeanList.get(c).getFinalPrice());
                        totalPrice += price;
                        String isSave = db.getLikeItem(orderBeanList.get(c).getSupplierItemId());
                        if (isSave == null) {
                            allSave = 0;
                            flag = 1;
                        } else {
                            if (flag == 1) {
                                allSave = 0;
                            } else {
                                allSave = 1;
                            }
                        }
                        suppItemIdStringList.add(orderBeanList.get(c).getSupplierItemId());

                    }
                    SuppliersBean sup = suppliersBeanList.get(i);
                    if(sup.getSupplierAvailable().equalsIgnoreCase("yes")){

                        if (storePromotionBeanList != null) {
                            StorePromotionBean storePromotionBean = null;
                            int store = 0;
                            for(int c=0;c<storePromotionBeanList.size();c++){
                                storePromotionBean = storePromotionBeanList.get(c);

                                if(suppliersBeanList.get(i).getSupplierId().equalsIgnoreCase(storePromotionBean.getSupplierId())){
                                    store = 1;
                                    break;
                                }
                            }
                            if(store==1){
                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),storePromotionBean.getDiscountAmount()));
                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, storePromotionBean.getMessage(), "minimum", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),storePromotionBean.getDiscountAmount()));
                            }else{
                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));
                            }

                        } else {

                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));


                        }

                    }else{
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, suppliersBeanList.get(i).getSupplierName(), "category", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(), null));
                        if(checkDeliveryBeanList.size()>0){
                            for(int c=0;c<checkDeliveryBeanList.size();c++){
                                if(checkDeliveryBeanList.get(c).getSupplierId().equalsIgnoreCase(sup.getSupplierId())){
                                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, checkDeliveryBeanList.get(c).getMessage(), "minimum", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                                    break;
                                }
                            }
                        }

                    }
                    if (flag == 0) {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, "Section 1", "save", "yes", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                    } else {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(k, "Section 1", "save", "no", suppItemIdStringList, suppliersBeanList.get(i).getSupplierId(),null));
                    }


                    count += orderBeanList.size();
                    k = count;

                }

            }
        }
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        adapter.changeList(finalOrderBeanList);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        mSectionedAdapter.changeadapter(adapter);
        Global.dialog.dismiss();
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
        }

        return super.onOptionsItemSelected(item);
    }
}
