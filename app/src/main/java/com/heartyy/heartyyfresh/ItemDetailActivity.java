package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.MoreItemsListBaseAdapter;
import com.heartyy.heartyyfresh.adapter.PopupPagerAdapter;
import com.heartyy.heartyyfresh.adapter.SimilarItemListBaseAdapter;
import com.heartyy.heartyyfresh.adapter.ViewPagerAdapter;
import com.heartyy.heartyyfresh.adapter.ViewPagerItemAdapter;
import com.heartyy.heartyyfresh.bean.BrandBean;
import com.heartyy.heartyyfresh.bean.ImagesBean;
import com.heartyy.heartyyfresh.bean.NutritutionBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SimilarItemBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.HorizontalListView;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;
import com.heartyy.heartyyfresh.utils.WrappingLinearLayoutManager;
import com.heartyy.heartyyfresh.viewpagerindicator.CirclePageIndicator;
import com.heartyy.heartyyfresh.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    Typeface andBold, bold, italic, light, mediumItalic, medium;
    private SharedPreferences pref;
    ViewPager viewPager, sliding_pager;
    ViewPagerAdapter adapter;
    PopupPagerAdapter popupPagerAdapter;
    ViewPagerItemAdapter viewPagerItemAdapter;
    PageIndicator mIndicator;
    private TextView txtProgress, txtOrdersPrice, onsaleText, itemDescriptionText, quantityText, priceText, salePriceText, offerText, moreText, cartCountText, totalCartCountText, detailsText, similarText, orderText, brandText;
    RelativeLayout priceLayout, countLayout;
    private String itemName, supplierItemId;
    private RelativeLayout detailLayout, nutritionLayout, bestLayout, moreLayout, layoutTextMore, addBagLayout, cartCountLayout, totalCartCountLayout, bottomLayout;
    private boolean isDetail = true;
    private boolean isNutrition = false;
    private boolean isBest = false;
    private ImageView detailImg, nutritionImg, bestImg, imageMoreArrow;
    private SimilarItemBean similarItemBean, moreItemBean;
    private ScrollView scroll;
    private ImageButton plusBtn, minusBtn, imageButtonBottomBag;
    private int count = 0;
    private TextView addToBtn;
    private ImageButton likeBtn;
    private boolean isMore = false;
    ProgressBar progressBar;
    //MoreItemsAdapter madapter;
    RelativeLayout layoutSimilar;
    HorizontalListView similarItemListView, moreItemListView;
    SimilarItemListBaseAdapter similarItemListAdapter;
    MoreItemsListBaseAdapter moreItemsListBaseAdapter;

    DatabaseHandler db;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
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
        mediumItalic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_MEDIUM);

        itemName = getIntent().getExtras().getString("name");
        supplierItemId = getIntent().getExtras().getString("supplierItemId");


        SpannableString s = new SpannableString("Item Detail");
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        pos = getIntent().getExtras().getInt("position");

        viewPager = (ViewPager) findViewById(R.id.pager);
        sliding_pager = (ViewPager) findViewById(R.id.pager1);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        onsaleText = (TextView) findViewById(R.id.text_on_sale);
        txtProgress = (TextView) findViewById(R.id.text_progress);
        itemDescriptionText = (TextView) findViewById(R.id.text_description);
        quantityText = (TextView) findViewById(R.id.text_quantity);
        txtOrdersPrice = (TextView) findViewById(R.id.text_order_price);
        priceText = (TextView) findViewById(R.id.text_price);
        salePriceText = (TextView) findViewById(R.id.text_sale_price);
        priceLayout = (RelativeLayout) findViewById(R.id.layout_price);
        offerText = (TextView) findViewById(R.id.text_offer);
        detailImg = (ImageView) findViewById(R.id.img_detail);
        imageMoreArrow = (ImageView) findViewById(R.id.image_more_arrow);
        nutritionImg = (ImageView) findViewById(R.id.img_nutrition);
        bestImg = (ImageView) findViewById(R.id.img_best);
        detailLayout = (RelativeLayout) findViewById(R.id.layout_details);
        nutritionLayout = (RelativeLayout) findViewById(R.id.layout_nutrition);
        bestLayout = (RelativeLayout) findViewById(R.id.layout_best);
        moreText = (TextView) findViewById(R.id.text_more);
        moreLayout = (RelativeLayout) findViewById(R.id.layout_more);
        layoutSimilar = (RelativeLayout) findViewById(R.id.layout_similar);
        layoutTextMore = (RelativeLayout) findViewById(R.id.layout_text_more);
        //moreRecyclerView = (RecyclerView) findViewById(R.id.morecardList);
        scroll = (ScrollView) findViewById(R.id.scroll);
        addBagLayout = (RelativeLayout) findViewById(R.id.relative_add_to_bag);
        cartCountLayout = (RelativeLayout) findViewById(R.id.layout_cart_count);
        cartCountText = (TextView) findViewById(R.id.text_cart_count);
        plusBtn = (ImageButton) findViewById(R.id.button_plus);
        minusBtn = (ImageButton) findViewById(R.id.button_minus);
        imageButtonBottomBag = (ImageButton) findViewById(R.id.image_bottom_bag);
        totalCartCountLayout = (RelativeLayout) findViewById(R.id.layout_total_cart_count);
        totalCartCountText = (TextView) findViewById(R.id.text_total_cart_count);
        bottomLayout = (RelativeLayout) findViewById(R.id.layout_bottom);
        detailsText = (TextView) findViewById(R.id.text_details);
        similarText = (TextView) findViewById(R.id.text_similar);
        orderText = (TextView) findViewById(R.id.text_order_total);
        addToBtn = (TextView) findViewById(R.id.button_add_to_bag);
        likeBtn = (ImageButton) findViewById(R.id.image_like);
        brandText = (TextView) findViewById(R.id.text_item_brand);
        progressBar = (ProgressBar) findViewById(R.id.firstBar);
        similarItemListView = (HorizontalListView) findViewById(R.id.similar_item_list_view);
        moreItemListView = (HorizontalListView) findViewById(R.id.more_item_list_view);


        countLayout = (RelativeLayout) findViewById(R.id.layout_total_cart_count);
        onsaleText.setTypeface(mediumItalic);
        itemDescriptionText.setTypeface(andBold);
        quantityText.setTypeface(andBold);
        salePriceText.setTypeface(andBold);
        priceText.setTypeface(medium);
        offerText.setTypeface(light);
        detailsText.setTypeface(andBold);
        similarText.setTypeface(andBold);
        orderText.setTypeface(light);
        totalCartCountText.setTypeface(medium);
        addToBtn.setTypeface(andBold);
        brandText.setTypeface(light);

        final DatabaseHandler db = new DatabaseHandler(this);
        String isSave = db.getLikeItem(Global.subAisleItemBean.getSupplierItemId());
        if (isSave != null) {
            likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }

       /* BrandBean brandBean = Global.subAisleItemBean.getBrand();
        if (brandBean == null) {
            moreLayout.setVisibility(View.GONE);
        } else {
            if (brandBean.getBrandName() == null) {
                moreLayout.setVisibility(View.GONE);
            } else {
                moreText.setText("More from " + brandBean.getBrandName());
                moreLayout.setVisibility(View.VISIBLE);
            }
        }*/


        BrandBean brandBean1 = Global.subAisleItemBean.getBrand();
        if (brandBean1 != null) {
            String brand = Global.subAisleItemBean.getBrand().getBrandName();
            if (brand == null) {
                brandText.setVisibility(View.GONE);
            } else {
                brandText.setVisibility(View.VISIBLE);
                brandText.setText("(" + brand + ")");
            }
        } else {
            brandText.setVisibility(View.GONE);
        }


        bestLayout.setVisibility(View.GONE);
        bestImg.setVisibility(View.GONE);


        List<String> str = new ArrayList<>();
        str.add(Global.subAisleItemBean.getDescription());
        if (Global.subAisleItemBean.getNutritutionBean() != null) {
            nutritionLayout.setVisibility(View.VISIBLE);
            nutritionImg.setVisibility(View.VISIBLE);
            String temp = "";
            NutritutionBean nut = Global.subAisleItemBean.getNutritutionBean();
            temp += nut.getNutritionCalories() + ",";
            temp += nut.getNutritionCholestrol() + ",";
            temp += nut.getNutritionDietaryFiber() + ",";
            temp += nut.getNutritionMonoUnsatFat() + ",";
            temp += nut.getNutritionPolyUnsatFat() + ",";
            temp += nut.getNutritionProtein() + ",";
            temp += nut.getNutritionSaturatedFat() + ",";
            temp += nut.getNutritionSodium() + ",";
            temp += nut.getNutritionSugar() + ",";
            temp += nut.getNutritionTotalCarbs() + ",";
            temp += nut.getNutritionTotalFat() + ",";
            temp += nut.getNutritionTransFat();
            temp = temp.replace("null", "");
            str.add(temp);
        } else {
            nutritionLayout.setVisibility(View.GONE);
            nutritionImg.setVisibility(View.GONE);
        }

        viewPagerItemAdapter = new ViewPagerItemAdapter(this, str);
        sliding_pager.setAdapter(viewPagerItemAdapter);
        checkViewPager();

        sliding_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isDetail = true;
                    isNutrition = false;
                    isBest = false;
                    checkViewPager();
                } else {
                    isDetail = false;
                    isNutrition = true;
                    isBest = false;
                    checkViewPager();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliding_pager.setCurrentItem(0, true);
                isDetail = true;
                isNutrition = false;
                isBest = false;
                checkViewPager();
            }
        });

        nutritionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliding_pager.setCurrentItem(1, true);
                isDetail = false;
                isNutrition = true;
                isBest = false;
                checkViewPager();
            }
        });

        bestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliding_pager.setCurrentItem(2, true);
                isDetail = false;
                isNutrition = false;
                isBest = true;
                checkViewPager();
            }
        });

        imageButtonBottomBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = pref.getString(Constants.USER_ID, null);
                if (userid == null) {
                    // showAlert("Please login to place your order");
                    Intent intent = new Intent(ItemDetailActivity.this, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ItemDetailActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }
            }
        });


        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    showAlert("Please login to save your items");
                    likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                } else {
                    String url;
                    final String supplierId = Global.subAisleItemBean.getSupplierItemId();


                    url = "user/saved";
                    JSONObject params = new JSONObject();
                    try {

                        params.put("user_id", userId);
                        JSONArray supplierArray = new JSONArray();
                        supplierArray.put(supplierId);
                        params.put("supplier_item_id", supplierArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("params---", params.toString());


                    RequestQueue rq = Volley.newRequestQueue(ItemDetailActivity.this);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("response", jsonObject.toString());

                                    try {
                                        String status = jsonObject.getString("status");
                                        if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                            String isSave = db.getLikeItem(supplierId);
                                            if (isSave == null) {
                                                db.addLikeItem(supplierId);
                                                likeBtn.setBackgroundResource(R.drawable.like_icon);
                                            } else {
                                                db.deleteLikeItem(supplierId);
                                                likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                            }


                                        } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                            //likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                            String isSave = Global.subAisleItemBean.getIsSave();
                                            if (isSave.equalsIgnoreCase("YES")) {
                                                likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                                StoreDetailActivity.subAisleItemBeanList.get(pos).setIsSave("NO");
                                            } else {
                                                likeBtn.setBackgroundResource(R.drawable.like_icon);
                                                StoreDetailActivity.subAisleItemBeanList.get(pos).setIsSave("YES");
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
                                //  showAlert(Constants.NO_INTERNET);
                            } else {
                                //showAlert(Constants.REQUEST_TIMED_OUT);
                            }
                        }
                    });

// Adding request to request queue
                    rq.add(jsonObjReq);
                }

            }
        });

        String onSale = Global.subAisleItemBean.getSale();
        if (onSale == null) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            String temp[] = String.format("%.2f", Double.parseDouble(Global.subAisleItemBean.getPrice())).split("\\.");
            salePriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        } else if (onSale.equalsIgnoreCase("null")) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            String temp[] = String.format("%.2f", Double.parseDouble(Global.subAisleItemBean.getPrice())).split("\\.");
            salePriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        } else if (onSale.equalsIgnoreCase("no")) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            String temp[] = String.format("%.2f", Double.parseDouble(Global.subAisleItemBean.getPrice())).split("\\.");
            salePriceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        } else {
            onsaleText.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            String temp[] = String.format("%.2f", Double.parseDouble(Global.subAisleItemBean.getPrice())).split("\\.");
            priceText.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
            double price = Double.parseDouble(Global.subAisleItemBean.getSalePrice());
            String temp1[] = String.format("%.2f", price).split("\\.");
            if (temp.length > 1) {
                salePriceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1].substring(0, 1) + "</sup>"));
            } else {
                salePriceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        }

        itemDescriptionText.setText(Global.subAisleItemBean.getItemName());
        final String quantity = Global.subAisleItemBean.getCount();
        if (quantity.equalsIgnoreCase("0") || quantity.equalsIgnoreCase("1")) {
            quantityText.setText(Global.subAisleItemBean.getSize() + " " + Global.subAisleItemBean.getUom());
        } else {
            quantityText.setText(Global.subAisleItemBean.getCount() + " X " + Global.subAisleItemBean.getSize() + " " + Global.subAisleItemBean.getUom());
        }

        if (Global.subAisleItemBean.getOffer() == null) {
            offerText.setText("");
            offerText.setVisibility(View.GONE);
        } else if (Global.subAisleItemBean.getOffer().equalsIgnoreCase("null")) {
            offerText.setText("");
            offerText.setVisibility(View.GONE);
        } else {

            offerText.setVisibility(View.VISIBLE);
            offerText.setText(Global.subAisleItemBean.getOffer());
        }

        addBagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
                String supplierID = pref.getString(Constants.SUPPLIER_ID, null);
                if (Global.subAisleItemBean.getMaxQuantity() == null) {
                    Global.subAisleItemBean.setMaxQuantity("0");
                } else if (Global.subAisleItemBean.getMaxQuantity().equalsIgnoreCase("null")) {
                    Global.subAisleItemBean.setMaxQuantity("0");
                }
                if (Integer.parseInt(Global.subAisleItemBean.getMaxQuantity()) == 0 || Integer.parseInt(Global.subAisleItemBean.getMaxQuantity()) > 0) {
                    SuppliersBean suppliersBean = db.getSupplier(supplierID);
                    if (suppliersBean == null) {
                        SuppliersBean newSuppliersBean = new SuppliersBean();
                        newSuppliersBean.setSupplierId(supplierID);
                        newSuppliersBean.setSupplierName(pref.getString(Constants.SUPPLIER_NAME, null));
                        db.addSupplier(newSuppliersBean);
                    }

                    OrderBean orderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
                    if (orderBean == null) {
                        OrderBean newOrder = new OrderBean();
                        newOrder.setSupplierItemId(Global.subAisleItemBean.getSupplierItemId());
                        newOrder.setItemName(Global.subAisleItemBean.getItemName());
                        newOrder.setSize(Global.subAisleItemBean.getSize());
                        newOrder.setShippingWeight(Global.subAisleItemBean.getShippingWeight());
                        newOrder.setUnitPrice(Global.subAisleItemBean.getFinalItemUnitPrice());
                        newOrder.setFinalPrice(Global.subAisleItemBean.getFinalItemUnitPrice());
                        newOrder.setSupplierId(supplierID);
                        if (Global.subAisleItemBean.getSubIsTaxApplicable().equalsIgnoreCase("true") || Global.subAisleItemBean.getIsTaxApplicable().equalsIgnoreCase("true")) {
                            Log.d("taxable", "YES");
                            newOrder.setIsTaxable("TRUE");
                            newOrder.setTaxAmount(Global.subAisleItemBean.getTaxAmount());
                        } else {
                            Log.d("taxable", "NO");
                            newOrder.setIsTaxable("FALSE");
                            newOrder.setTaxAmount(0);
                        }
                        newOrder.setRegularPrice(Global.subAisleItemBean.getPrice());
                        if (Global.subAisleItemBean.getBrand() != null) {
                            newOrder.setBrandName(Global.subAisleItemBean.getBrand().getBrandName());
                        } else {
                            newOrder.setBrandName("");
                        }
                        newOrder.setOrderQuantity("1");
                        newOrder.setApplicableSaleDescription("");
                        newOrder.setUom(Global.subAisleItemBean.getUom());
                        newOrder.setSubstitutionWith("Store's choice");
                        newOrder.setMaxQuantity(Global.subAisleItemBean.getMaxQuantity());
                        db.addOrderInCart(newOrder);

                        addBagLayout.setEnabled(false);
                        cartCountLayout.setVisibility(View.VISIBLE);
                        cartCountText.setText("1");

                        bottomLayout.setVisibility(View.VISIBLE);
                        totalCartCountLayout.setVisibility(View.VISIBLE);

                        totalCartCountText.setText(String.valueOf(db.getAllOrders().size()));
                    }
                    refreshData();
                    checkAnyOrder();
                } else {
                    showAlert(Constants.MAX_QUANTITY);
                }

            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
                OrderBean updateOrderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity());
                    quantity++;
                    int max = Integer.parseInt(Global.subAisleItemBean.getMaxQuantity());
                    if (max > 0 && quantity > max) {
                        showAlert(Constants.MAX_QUANTITY);
                    } else {
                        double unitPrice = Double.parseDouble(updateOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * quantity;
                        updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                        updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(updateOrderBean);
                        cartCountText.setText(String.valueOf(quantity));
                    }

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        refreshData();
                        checkAnyOrder();

                    }
                });


            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
                OrderBean minusOrderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
                if (minusOrderBean != null) {
                    int itemQuantity = Integer.parseInt(minusOrderBean.getOrderQuantity());
                    if (itemQuantity == 1) {
                        cartCountLayout.setVisibility(View.GONE);
                        db.deleteOrder(minusOrderBean);
                        addBagLayout.setVisibility(View.VISIBLE);
                        addToBtn.setEnabled(true);
                        addBagLayout.setEnabled(true);

                        List<OrderBean> orderList = db.getAllOrders();
                        if (orderList != null) {
                            int cartUpdatedItem = orderList.size();
                            if (cartUpdatedItem == 0) {
                                bottomLayout.setVisibility(View.GONE);
                                totalCartCountLayout.setVisibility(View.GONE);
                            } else
                                totalCartCountText.setText(String.valueOf(cartUpdatedItem));
                        }

                        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                        for (int k = 0; k < suppliersBeanList.size(); k++) {
                            SuppliersBean suppliersBean = suppliersBeanList.get(k);
                            List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                            if (orderBeanList.size() == 0) {
                                db.deleteSupplier(suppliersBean);
                            }
                        }

                    } else {
                        itemQuantity--;
                        double unitPrice = Double.parseDouble(minusOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * itemQuantity;
                        minusOrderBean.setOrderQuantity(String.valueOf(itemQuantity));
                        minusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(minusOrderBean);
                        cartCountText.setText(String.valueOf(itemQuantity));
                    }
                }
                refreshData();
                checkAnyOrder();
            }
        });

        layoutTextMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMore) {
                    if (moreItemBean.getSimilarItemsList() != null) {
                        moreItemListView.setVisibility(View.VISIBLE);
                        Animation animShow = AnimationUtils.loadAnimation(ItemDetailActivity.this, R.anim.alpha_show);
                        imageMoreArrow.setImageResource(R.drawable.image_more_arrow);
                        moreItemListView.setAnimation(animShow);
                        isMore = true;
                    }
                } else {
                    Animation animShow = AnimationUtils.loadAnimation(ItemDetailActivity.this, R.anim.aplha_hide);
                    imageMoreArrow.setImageResource(R.drawable.arrrow);
                    moreItemListView.startAnimation(animShow);
                    moreItemListView.setVisibility(View.GONE);
                    isMore = false;
                }
            }
        });

    }


    public void showLargeImage() {
        LayoutInflater layoutInflater = LayoutInflater.from(ItemDetailActivity.this);
        View promptsView = layoutInflater.inflate(R.layout.popup_product_image, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ItemDetailActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();

        ImageButton closeButton = (ImageButton) promptsView.findViewById(R.id.close_button);
        ViewPager popupPager = (ViewPager) promptsView.findViewById(R.id.popup_pager);
        PageIndicator popupIndicator = (CirclePageIndicator) promptsView.findViewById(R.id.popup_indicator);
        popupPager.setAdapter(popupPagerAdapter);

        if (Global.subAisleItemBean.getImagesBeanList().size() == 1) {

        } else {
            popupIndicator.setViewPager(popupPager);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void checkAnyOrder() {
        DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
        List<OrderBean> orderBeanList = db.getAllOrders();
        if (orderBeanList != null) {
            if (orderBeanList.size() > 0) {
                TextView totalCartCountText = (TextView) findViewById(R.id.text_total_cart_count);
                TextView totalPriceText = (TextView) findViewById(R.id.text_order_price);

                countLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);

                int ordersCount = db.getOrdersCount();
                totalCartCountText.setText(String.valueOf(ordersCount));

                OrderBean orderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
                if (orderBean != null) {
                    int orderQuantity = Integer.parseInt(orderBean.getOrderQuantity());
                    if (orderQuantity > 0) {
                        cartCountLayout.setVisibility(View.VISIBLE);
                        cartCountText.setText(String.valueOf(orderQuantity));
                        addToBtn.setEnabled(false);
                    }
                }

                double total = 0;
                for (int i = 0; i < orderBeanList.size(); i++) {
                    OrderBean order = orderBeanList.get(i);
                    total += Double.valueOf(order.getFinalPrice());
                }
                totalPriceText.setText(" $" + String.format("%.2f", total));

                progressBar = (ProgressBar) findViewById(R.id.firstBar);
                txtProgress = (TextView) findViewById(R.id.text_progress);
                String p[] = String.valueOf(total).split("\\.");
                int temp = Integer.parseInt(p[0]);

                if (Global.promotionAvailableBean != null) {
                    if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                        progressBar.setVisibility(View.VISIBLE);
                        txtProgress.setVisibility(View.VISIBLE);
                        progressBar.setMax(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                        double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());

                        double addMore = 0;
                        if (total > minamount) {
                            addMore = total - minamount;
                        } else {
                            addMore = minamount - total;
                        }

                        if (total > minamount) {
                            Global.isFreeDelivery = true;
                            if (progressBar.getMax() == 0) {
                                progressBar.setMax(1);
                                progressBar.setProgress(1);
                            } else {
                                progressBar.setProgress(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                            }
                            txtProgress.setText("You got free delivery");
                        } else {
                            String text = String.format("%.2f", addMore);
                            progressBar.setProgress(temp);
                            txtProgress.setText("Add $" + text + " for free delivery");
                            Global.isFreeDelivery = false;
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        txtProgress.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    txtProgress.setVisibility(View.GONE);
                }


            } else {
                cartCountLayout.setVisibility(View.GONE);
                addBagLayout.setEnabled(false);
                addBagLayout.setVisibility(View.VISIBLE);
                addToBtn.setEnabled(true);
                addBagLayout.setEnabled(true);
                countLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
            }
        } else {
            countLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }


    private void checkViewPager() {
        if (isDetail) {
            detailImg.setVisibility(View.VISIBLE);
            nutritionImg.setVisibility(View.INVISIBLE);
            bestImg.setVisibility(View.INVISIBLE);
        } else if (isNutrition) {
            detailImg.setVisibility(View.INVISIBLE);
            nutritionImg.setVisibility(View.VISIBLE);
            bestImg.setVisibility(View.INVISIBLE);
        } else if (isBest) {
            detailImg.setVisibility(View.INVISIBLE);
            nutritionImg.setVisibility(View.INVISIBLE);
            bestImg.setVisibility(View.VISIBLE);
        }
    }

    private void getSimilarItems() {
        Global.showProgress(ItemDetailActivity.this);
        String url;
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        String category = pref.getString(Constants.CATEGORY, null);
        String subCategoryId = null;
        if (category != null) {
            if (category.equalsIgnoreCase("sub")) {
                subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
            } else {
                subCategoryId = Global.subAisleItemBean.getSubCategoryId();
            }
        } else {
            subCategoryId = Global.subAisleItemBean.getSubCategoryId();
        }
        url = "product/similar?supplier_id=" + supplierId + "&sub_category_id=" + subCategoryId + "&supplier_item_id=" + supplierItemId;
        RequestQueue rq = Volley.newRequestQueue(ItemDetailActivity.this);
        Log.d("similarURL", Constants.URL + url);
        final String finalSubCategoryId = subCategoryId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                similarItemBean = ConversionHelper.convertSimilarItemJsonToSimilarItemBean(jsonObject);
                                if (similarItemBean.getSimilarItemsList() == null) {
                                    similarText.setVisibility(View.GONE);
                                } else {
                                    similarText.setVisibility(View.VISIBLE);
                                }

                                final BrandBean brandBean = Global.subAisleItemBean.getBrand();
                                if (brandBean == null) {
                                    Global.dialog.dismiss();
                                } else {
                                    if (brandBean.getBrandName() == null) {
                                        Global.dialog.dismiss();
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getMoreItems(finalSubCategoryId, supplierId, brandBean.getBrandId(), supplierItemId);
                                            }
                                        });
                                    }
                                }

                                WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(ItemDetailActivity.this);
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                                if (similarItemBean.getSimilarItemsList() != null) {
                                    layoutSimilar.setVisibility(View.VISIBLE);
                                    similarItemListView.setVisibility(View.VISIBLE);
                                    similarItemListAdapter = new SimilarItemListBaseAdapter(ItemDetailActivity.this, ItemDetailActivity.this, similarItemBean.getSimilarItemsList());
                                    similarItemListView.setAdapter(similarItemListAdapter);
                                } else layoutSimilar.setVisibility(View.GONE);

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
                Log.d("error", "Error: " + error.toString());
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


    private void getMoreItems(String finalSubCategoryId, String supplierId, String brandId, String supplierItemId) {
        String url = "product/more?supplier_id=" + supplierId + "&sub_category_id=" + finalSubCategoryId + "&brand_id=" + brandId + "&topn=2" + "&supplier_item_id=" + supplierItemId;
        RequestQueue rq = Volley.newRequestQueue(ItemDetailActivity.this);
        Log.d("moreURL", Constants.URL + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                moreItemBean = ConversionHelper.convertSimilarItemJsonToSimilarItemBean(jsonObject);
                                WrappingLinearLayoutManager layoutManager = new WrappingLinearLayoutManager(ItemDetailActivity.this);
                                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                //moreRecyclerView.setHasFixedSize(false);
                                //moreRecyclerView.setLayoutManager(layoutManager);

/*
                                if (moreItemBean.getSimilarItemsList() != null) {
                                    madapter = new MoreItemsAdapter(ItemDetailActivity.this, ItemDetailActivity.this, moreItemBean.getSimilarItemsList());
                                    moreRecyclerView.setAdapter(madapter);
                                    moreRecyclerView.setNestedScrollingEnabled(false);
                                }else moreLayout.setVisibility(View.GONE);
*/


                                if (moreItemBean.getSimilarItemsList() != null) {
                                    moreLayout.setVisibility(View.VISIBLE);
                                    moreItemsListBaseAdapter = new MoreItemsListBaseAdapter(ItemDetailActivity.this, ItemDetailActivity.this, moreItemBean.getSimilarItemsList());
                                    moreItemListView.setAdapter(moreItemsListBaseAdapter);
                                    //moreRecyclerView.setNestedScrollingEnabled(false);
                                } else moreLayout.setVisibility(View.GONE);

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
                Log.d("error", "Error: " + error.toString());
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

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ItemDetailActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ItemDetailActivity.this);
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
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAnyOrder();
    }

    @Override
    public void onStart() {
        super.onStart();

        List<ImagesBean> imagesBeanList = new ArrayList<>();
        List<ImagesBean> mainImagesBeanList = null;


        if (Global.subAisleItemBean.getImagesBeanList() == null) {
            ImagesBean imagesBean = new ImagesBean();
            imagesBean.setImage("www.google.com");
            imagesBeanList.add(imagesBean);
            Global.subAisleItemBean.setImagesBeanList(imagesBeanList);
        } else {
            if (Global.subAisleItemBean.getImagesBeanList().size() == 0) {
                ImagesBean imagesBean = new ImagesBean();
                imagesBean.setImage("www.google.com");
                imagesBeanList.add(imagesBean);
                Global.subAisleItemBean.setImagesBeanList(imagesBeanList);
            } else {
                imagesBeanList = Global.subAisleItemBean.getImagesBeanList();
            }
        }

        if (Global.subAisleItemBean.getMainImagesBeanList() != null) {
            if (Global.subAisleItemBean.getMainImagesBeanList().size() > 0) {
                mainImagesBeanList = Global.subAisleItemBean.getMainImagesBeanList();
            } else {
                mainImagesBeanList = imagesBeanList;
            }

        } else {
            mainImagesBeanList = imagesBeanList;
        }

        adapter = new ViewPagerAdapter(ItemDetailActivity.this, imagesBeanList, this);
        popupPagerAdapter = new PopupPagerAdapter(ItemDetailActivity.this, mainImagesBeanList, this);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(Global.subAisleItemBean.getImagesBeanList().size());
        if (Global.subAisleItemBean.getImagesBeanList().size() == 1) {

        } else {
            mIndicator.setViewPager(viewPager);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSimilarItems();
            }
        });
    }


    public void refreshData() {
        if (similarItemListAdapter != null) {
            similarItemListAdapter.notifyDataSetChanged();
        }
        if (moreItemsListBaseAdapter != null) {
            moreItemsListBaseAdapter.notifyDataSetChanged();
        }
    }


}
