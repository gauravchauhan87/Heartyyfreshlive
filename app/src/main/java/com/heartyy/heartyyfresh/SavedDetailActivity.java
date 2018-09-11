package com.heartyy.heartyyfresh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.heartyy.heartyyfresh.adapter.CustomSavedCardAdapter;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SavedSupplierItemBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import java.util.List;

public class SavedDetailActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    CustomSavedCardAdapter adapter;
    RelativeLayout layoutReorder;
    Button txtReorder;
    OrderBean orderBean;
    private SharedPreferences pref;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_detail);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_saved_detail));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.saved_item_main);
        Global.setFont(root, andBold);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);

        db = new DatabaseHandler(this);

        layoutReorder = (RelativeLayout) findViewById(R.id.layout_reorder);
        txtReorder = (Button) findViewById(R.id.button_reorder_all);
        txtReorder.setTypeface(andBold);


        txtReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reOrderAllItems();
            }
        });
    }

    public void reOrderAllItems() {

        DatabaseHandler db = new DatabaseHandler(SavedDetailActivity.this);
        SuppliersBean supplierBean = db.getSupplier(Global.savedSupplierId);
        if (supplierBean == null) {
            SuppliersBean newSuppliersBean = new SuppliersBean();
            newSuppliersBean.setSupplierId(Global.savedSupplierId);
            newSuppliersBean.setSupplierName(Global.savedSupplierName);
            db.addSupplier(newSuppliersBean);
        }

        for (int i = 0; i < Global.savedSupplierItemBeanList.size(); i++) {
            orderBean = db.getOrder(Global.savedSupplierItemBeanList.get(i).getSupplierItemId());
            if (orderBean == null) {
                SavedSupplierItemBean s = Global.savedSupplierItemBeanList.get(i);
                String onSale = s.getSale();
                if (onSale == null) {
                    String priceTemp = s.getPrice();
                    if (Global.savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        Global.savedSupplierItemBeanList.get(i).setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    Global.savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("null")) {
                    String priceTemp = s.getPrice();
                    if (Global.savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        Global.savedSupplierItemBeanList.get(i).setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    Global.savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("no")) {
                    String priceTemp = s.getPrice();
                    if (Global.savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        Global.savedSupplierItemBeanList.get(i).setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    Global.savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                } else if (onSale.equalsIgnoreCase("yes")) {
                    String priceTemp = s.getSalePrice();
                    if (Global.savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                        String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                        double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                        Global.savedSupplierItemBeanList.get(i).setTaxAmount(temp2);
                        // priceTemp = String.valueOf(String.format("%.2f",temp2));
                    }
                    Global.savedSupplierItemBeanList.get(i).setFinalItemUnitPrice(priceTemp);
                }
                OrderBean newOrder = new OrderBean();
                newOrder.setSupplierItemId(Global.savedSupplierItemBeanList.get(i).getSupplierItemId());
                newOrder.setItemName(Global.savedSupplierItemBeanList.get(i).getItemName());
                newOrder.setSize(Global.savedSupplierItemBeanList.get(i).getSize());
                newOrder.setShippingWeight(Global.savedSupplierItemBeanList.get(i).getShippingWeight());
                newOrder.setUnitPrice(Global.savedSupplierItemBeanList.get(i).getFinalItemUnitPrice());
                newOrder.setFinalPrice(Global.savedSupplierItemBeanList.get(i).getPrice());
                newOrder.setOrderQuantity("1");
                newOrder.setSubstitutionWith("none");
                newOrder.setSupplierId(Global.savedSupplierId);
                if (Global.savedSupplierItemBeanList.get(i).getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.savedSupplierItemBeanList.get(i).getIsTaxApplicable().equalsIgnoreCase("true")) {
                    newOrder.setIsTaxable("TRUE");
                    newOrder.setTaxAmount(Global.savedSupplierItemBeanList.get(i).getTaxAmount());
                } else {
                    newOrder.setIsTaxable("FALSE");
                    newOrder.setTaxAmount(0);
                }
                newOrder.setRegularPrice(Global.savedSupplierItemBeanList.get(i).getPrice());
                newOrder.setThumbnail(Global.savedSupplierItemBeanList.get(i).getThumbnail());
                newOrder.setSubstitutionWith("Store's choice");
                if (Global.savedSupplierItemBeanList.get(i).getBrand().getBrandName() != null) {
                    newOrder.setBrandName(Global.savedSupplierItemBeanList.get(i).getBrand().getBrandName());
                } else {
                    newOrder.setBrandName("");
                }
                newOrder.setOrderQuantity("1");
                newOrder.setUom(Global.savedSupplierItemBeanList.get(i).getUom());

                if (Global.savedSupplierItemBeanList.get(i).getMaxQuantity() == null) {
                    newOrder.setMaxQuantity("0");
                } else if (Global.savedSupplierItemBeanList.get(i).getMaxQuantity().equalsIgnoreCase("null")) {
                    newOrder.setMaxQuantity("0");
                } else {
                    newOrder.setMaxQuantity(Global.savedSupplierItemBeanList.get(i).getMaxQuantity());
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

        Intent intent = new Intent(SavedDetailActivity.this, CheckoutActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);

        for (int i = 0; i < Global.savedSupplierItemBeanList.size(); i++) {
            SavedSupplierItemBean supp = Global.savedSupplierItemBeanList.get(i);
            String str = db.getLikeItem(Global.savedSupplierItemBeanList.get(i).getSupplierItemId());
            if (str == null) {
                db.addLikeItem(supp.getSupplierItemId());
            }
        }

        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CustomSavedCardAdapter(SavedDetailActivity.this, Global.savedSupplierItemBeanList, SavedDetailActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
