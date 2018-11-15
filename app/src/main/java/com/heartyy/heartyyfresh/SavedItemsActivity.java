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
import com.heartyy.heartyyfresh.adapter.CustomSavedItemListAdapter;
import com.heartyy.heartyyfresh.bean.Item;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SavedItemBean;
import com.heartyy.heartyyfresh.bean.SavedSectionItem;
import com.heartyy.heartyyfresh.bean.SavedSupplierItemBean;
import com.heartyy.heartyyfresh.bean.SavedSuppliersBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedItemsActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    ListView saveItemListView;
    TextView noSave;
    private SharedPreferences pref;
    private List<SavedItemBean> savedItemBeanList;
    ArrayList<Item> items;
    OrderBean orderBean;
    Button buttonStartShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_saved_items));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        saveItemListView = (ListView) findViewById(R.id.list_saved_items);
        noSave = (TextView) findViewById(R.id.text_no_save);
        buttonStartShopping = (Button) findViewById(R.id.button_start_shopping);

        noSave.setTypeface(light);


        saveItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item items = (Item) adapterView.getItemAtPosition(i);
                if (!items.isSection()) {
                    SavedSuppliersBean ei = (SavedSuppliersBean) items;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.APPLICABLE_TAX_RATE, ei.getTaxRate());
                    editor.apply();
                    Global.savedSupplierItemBeanList = ei.getSavedSupplierItemBeanList();
                    Global.savedSupplierId = ei.getSupplierId();
                    Global.savedSupplierName = ei.getSupplierName();
                    ei.getSupplierName();
                    Intent intent = new Intent(SavedItemsActivity.this, SavedDetailActivity.class);
                    startActivity(intent);
                }
            }
        });


        buttonStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedItemsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getSavedItems() {

        items = new ArrayList<Item>();

        Global.showProgress(SavedItemsActivity.this);
        String userId = pref.getString(Constants.USER_ID, null);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        String url = "user/saved?user_id=" + userId + "&zipcode=" + Global.zip;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            savedItemBeanList = ConversionHelper.convertSavedItemsJsonToSavedItemsBean(jsonObject);
                            if (savedItemBeanList.size() > 0) {
                                noSave.setVisibility(View.GONE);
                                buttonStartShopping.setVisibility(View.GONE);
                                saveItemListView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < savedItemBeanList.size(); i++) {
                                    SavedItemBean savedItemBean = savedItemBeanList.get(i);
                                    items.add((new SavedSectionItem("You saved items from " + savedItemBean.getZipcode())));
                                    for (int j = 0; j < savedItemBean.getSavedSuppliersBeanList().size(); j++) {
                                        SavedSuppliersBean suppliersBean = savedItemBean.getSavedSuppliersBeanList().get(j);
                                        items.add(suppliersBean);
                                    }
                                }

                                final CustomSavedItemListAdapter adapter = new CustomSavedItemListAdapter(SavedItemsActivity.this, items, SavedItemsActivity.this);
                                saveItemListView.setAdapter(adapter);
                            } else {
                                noSave.setVisibility(View.VISIBLE);
                                buttonStartShopping.setVisibility(View.VISIBLE);
                                saveItemListView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                           Global.hideProgress();
                            noSave.setVisibility(View.VISIBLE);
                            buttonStartShopping.setVisibility(View.VISIBLE);
                            saveItemListView.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
               Global.hideProgress();
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);

                } else {
                   Global.hideProgress();
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
                .from(SavedItemsActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SavedItemsActivity.this);
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

    public void reOrderSaved(String suppId, List<SavedSupplierItemBean> savedSupplierItemBeanList, String supplierName) {
        DatabaseHandler db = new DatabaseHandler(SavedItemsActivity.this);
        SuppliersBean suppliersBean = db.getSupplier(suppId);
        if (suppliersBean == null) {
            SuppliersBean newSuppliersBean = new SuppliersBean();
            newSuppliersBean.setSupplierId(suppId);
            newSuppliersBean.setSupplierName(supplierName);
            db.addSupplier(newSuppliersBean);
        }

        for (int i = 0; i < savedSupplierItemBeanList.size(); i++) {
            orderBean = db.getOrder(savedSupplierItemBeanList.get(i).getSupplierItemId());
            if (orderBean == null) {
                SavedSupplierItemBean s = savedSupplierItemBeanList.get(i);
                OrderBean newOrder = new OrderBean();
                String onSale = s.getSale();
                if (onSale == null) {
                    String priceTemp = s.getPrice();
                    if (savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        s.setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("null")) {
                    String priceTemp = s.getPrice();
                    if (savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        s.setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("no")) {
                    String priceTemp = s.getPrice();
                    if (savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        s.setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("yes")) {
                    String priceTemp = s.getSalePrice();
                    if (savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        s.setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                }


                newOrder.setSupplierItemId(savedSupplierItemBeanList.get(i).getSupplierItemId());
                newOrder.setItemName(savedSupplierItemBeanList.get(i).getItemName());
                newOrder.setSize(savedSupplierItemBeanList.get(i).getSize());
                newOrder.setShippingWeight(savedSupplierItemBeanList.get(i).getShippingWeight());
                newOrder.setUnitPrice(savedSupplierItemBeanList.get(i).getFinalItemUnitPrice());
                newOrder.setFinalPrice(savedSupplierItemBeanList.get(i).getFinalItemUnitPrice());
                newOrder.setOrderQuantity("1");
                newOrder.setSubstitutionWith("Store's choice");
                newOrder.setSupplierId(suppId);
                if (savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                    newOrder.setIsTaxable("TRUE");
                    newOrder.setTaxAmount(savedSupplierItemBeanList.get(i).getTaxAmount());
                } else {
                    newOrder.setIsTaxable("FALSE");
                    newOrder.setTaxAmount(0);
                }
                newOrder.setRegularPrice(savedSupplierItemBeanList.get(i).getPrice());
                newOrder.setThumbnail(savedSupplierItemBeanList.get(i).getThumbnail());
                if (savedSupplierItemBeanList.get(i).getBrand().getBrandName() != null) {
                    newOrder.setBrandName(savedSupplierItemBeanList.get(i).getBrand().getBrandName());
                } else {
                    newOrder.setBrandName("");
                }
                newOrder.setOrderQuantity("1");
                newOrder.setUom(savedSupplierItemBeanList.get(i).getUom());
                if (savedSupplierItemBeanList.get(i).getMaxQuantity() == null) {
                    newOrder.setMaxQuantity("0");
                } else if (savedSupplierItemBeanList.get(i).getMaxQuantity().equalsIgnoreCase("null")) {
                    newOrder.setMaxQuantity("0");
                } else {
                    newOrder.setMaxQuantity(savedSupplierItemBeanList.get(i).getMaxQuantity());
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

        Intent intent = new Intent(SavedItemsActivity.this, CheckoutActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        getSavedItems();
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
