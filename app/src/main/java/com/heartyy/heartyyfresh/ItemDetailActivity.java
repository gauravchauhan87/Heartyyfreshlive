package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;
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
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
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
import io.card.payment.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemDetailActivity extends AppCompatActivity {
    ViewPagerAdapter adapter;
    private RelativeLayout addBagLayout;
    private TextView addToBtn;
    Typeface andBold;
    private ImageView bestImg;
    private RelativeLayout bestLayout;
    Typeface bold;
    private RelativeLayout bottomLayout;
    private TextView brandText;
    private RelativeLayout cartCountLayout;
    private TextView cartCountText;
    private int count = 0;
    RelativeLayout countLayout;
    DatabaseHandler db;
    private ImageView detailImg;
    private RelativeLayout detailLayout;
    private TextView detailsText;
    private ImageButton imageButtonBottomBag;
    private ImageView imageMoreArrow;
    private boolean isBest = false;
    private boolean isDetail = true;
    private boolean isMore = false;
    private boolean isNutrition = false;
    Typeface italic;
    private TextView itemDescriptionText;
    private String itemName;
    RelativeLayout layoutSimilar;
    private RelativeLayout layoutTextMore;
    Typeface light;
    private ImageButton likeBtn;
    PageIndicator mIndicator;
    Typeface medium;
    Typeface mediumItalic;
    private ImageButton minusBtn;
    private SimilarItemBean moreItemBean;
    HorizontalListView moreItemListView;
    MoreItemsListBaseAdapter moreItemsListBaseAdapter;
    private RelativeLayout moreLayout;
    private TextView moreText;
    private ImageView nutritionImg;
    private RelativeLayout nutritionLayout;
    private TextView offerText;
    private TextView onsaleText;
    private TextView orderText;
    private ImageButton plusBtn;
    PopupPagerAdapter popupPagerAdapter;
    private int pos;
    private SharedPreferences pref;
    RelativeLayout priceLayout;
    private TextView priceText;
    ProgressBar progressBar;
    private TextView quantityText;
    private TextView salePriceText;
    private ScrollView scroll;
    private SimilarItemBean similarItemBean;
    SimilarItemListBaseAdapter similarItemListAdapter;
    HorizontalListView similarItemListView;
    private TextView similarText;
    ViewPager sliding_pager;
    private String supplierItemId;
    private TextView textPriceDecimal;
    private TextView textSalePriceDecimal;
    private TextView textSalePriceDollor;
    private TextView text_price_dollor;
    private TextView totalCartCountText;
    private TextView txtOrdersPrice;
    private TextView txtProgress;
    ViewPager viewPager;
    ViewPagerItemAdapter viewPagerItemAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        mediumItalic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_MEDIUM);
        itemName = getIntent().getExtras().getString(ShareConstants.WEB_DIALOG_PARAM_NAME);
        supplierItemId = getIntent().getExtras().getString("supplierItemId");
        pos = getIntent().getExtras().getInt("position");
        SpannableString s = new SpannableString("Item Detail");
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(), 33);
        getSupportActionBar().setTitle(s);
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
        scroll = (ScrollView) findViewById(R.id.scroll);
        addBagLayout = (RelativeLayout) findViewById(R.id.relative_add_to_bag);
        cartCountLayout = (RelativeLayout) findViewById(R.id.layout_cart_count);
        cartCountText = (TextView) findViewById(R.id.text_cart_count);
        plusBtn = (ImageButton) findViewById(R.id.button_plus);
        minusBtn = (ImageButton) findViewById(R.id.button_minus);
        imageButtonBottomBag = (ImageButton) findViewById(R.id.image_bottom_bag);
        countLayout = (RelativeLayout) findViewById(R.id.layout_total_cart_count);
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
        textSalePriceDecimal = (TextView) findViewById(R.id.text_sale_price_decimal);
        textPriceDecimal = (TextView) findViewById(R.id.text_price_decimal);
        textSalePriceDollor = (TextView) findViewById(R.id.text_sale_price_dollor);
        text_price_dollor = (TextView) findViewById(R.id.text_price_dollor);
        onsaleText.setTypeface(mediumItalic);
        itemDescriptionText.setTypeface(andBold);
        quantityText.setTypeface(andBold);
        salePriceText.setTypeface(andBold);
        textSalePriceDollor.setTypeface(andBold);
        textSalePriceDecimal.setTypeface(andBold);
        priceText.setTypeface(medium);
        text_price_dollor.setTypeface(medium);
        textPriceDecimal.setTypeface(medium);
        offerText.setTypeface(light);
        detailsText.setTypeface(andBold);
        similarText.setTypeface(andBold);
        orderText.setTypeface(light);
        totalCartCountText.setTypeface(medium);
        addToBtn.setTypeface(andBold);
        brandText.setTypeface(light);
        db = new DatabaseHandler(this);
        setItemDetails();
        sliding_pager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 0) {
                    isDetail = true;
                    isNutrition = false;
                    isBest = false;
                    checkViewPager();
                    return;
                }
                isDetail = false;
                isNutrition = true;
                isBest = false;
                checkViewPager();
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        detailLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sliding_pager.setCurrentItem(0, true);
                isDetail = true;
                isNutrition = false;
                isBest = false;
                checkViewPager();
            }
        });
        nutritionLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sliding_pager.setCurrentItem(1, true);
                isDetail = false;
                isNutrition = true;
                isBest = false;
                checkViewPager();
            }
        });
        bestLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                sliding_pager.setCurrentItem(2, true);
                isDetail = false;
                isNutrition = false;
                isBest = true;
                checkViewPager();
            }
        });
        imageButtonBottomBag.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (pref.getString(Constants.USER_ID, null) == null) {
                    Intent intent = new Intent(ItemDetailActivity.this, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(ItemDetailActivity.this, CheckoutActivity.class));
            }
        });
        likeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String userId = pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    showAlert("Please login to save your items");
                    likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                    return;
                }
                final String supplierId = Global.subAisleItemBean.getSupplierItemId();
                String url = "user/saved";
                JSONObject params = new JSONObject();
                try {
                    params.put(Constants.USER_ID, userId);
                    JSONArray supplierArray = new JSONArray();
                    supplierArray.put(supplierId);
                    params.put("supplier_item_id", supplierArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("params---", params.toString());
                Volley.newRequestQueue(ItemDetailActivity.this).add(new JsonObjectRequest(1, Constants.URL + url, params, new Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                if (db.getLikeItem(supplierId) == null) {
                                    db.addLikeItem(supplierId);
                                    likeBtn.setBackgroundResource(R.drawable.like_icon);
                                    return;
                                }
                                db.deleteLikeItem(supplierId);
                                likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                            } else if (!status.equalsIgnoreCase(Constants.ERROR)) {
                            } else {
                                if (Global.subAisleItemBean.getIsSave().equalsIgnoreCase("YES")) {
                                    likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                    ((SubAisleItemBean) StoreDetailActivity.subAisleItemBeanList.get(pos)).setIsSave("NO");
                                    return;
                                }
                                likeBtn.setBackgroundResource(R.drawable.like_icon);
                                ((SubAisleItemBean) StoreDetailActivity.subAisleItemBeanList.get(pos)).setIsSave("YES");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.ERROR, "Error: " + error.toString());
                        Global.dialog.dismiss();
                        if (!(error instanceof NoConnectionError)) {
                        }
                    }
                }));
            }
        });
        addBagLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
                String supplierID = pref.getString(Constants.SUPPLIER_ID, null);
                if (Global.subAisleItemBean.getMaxQuantity() == null) {
                    Global.subAisleItemBean.setMaxQuantity(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                } else if (Global.subAisleItemBean.getMaxQuantity().equalsIgnoreCase("null")) {
                    Global.subAisleItemBean.setMaxQuantity(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                }
                if (Integer.parseInt(Global.subAisleItemBean.getMaxQuantity()) == 0 || Integer.parseInt(Global.subAisleItemBean.getMaxQuantity()) > 0) {
                    if (db.getSupplier(supplierID) == null) {
                        SuppliersBean newSuppliersBean = new SuppliersBean();
                        newSuppliersBean.setSupplierId(supplierID);
                        newSuppliersBean.setSupplierName(pref.getString(Constants.SUPPLIER_NAME, null));
                        db.addSupplier(newSuppliersBean);
                    }
                    if (db.getOrder(Global.subAisleItemBean.getSupplierItemId()) == null) {
                        OrderBean newOrder = new OrderBean();
                        newOrder.setSupplierItemId(Global.subAisleItemBean.getSupplierItemId());
                        newOrder.setItemName(Global.subAisleItemBean.getItemName());
                        newOrder.setSize(Global.subAisleItemBean.getSize());
                        newOrder.setShippingWeight(Global.subAisleItemBean.getShippingWeight());
                        newOrder.setUnitPrice(Global.subAisleItemBean.getFinalItemUnitPrice());
                        newOrder.setFinalPrice(Global.subAisleItemBean.getFinalItemUnitPrice());
                        newOrder.setSupplierId(supplierID);
                        if (Global.subAisleItemBean.getSubIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || Global.subAisleItemBean.getIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                            Log.d("taxable", "YES");
                            newOrder.setIsTaxable("TRUE");
                            newOrder.setTaxAmount(Global.subAisleItemBean.getTaxAmount());
                        } else {
                            Log.d("taxable", "NO");
                            newOrder.setIsTaxable("FALSE");
                            newOrder.setTaxAmount(0.0d);
                        }
                        newOrder.setRegularPrice(Global.subAisleItemBean.getPrice());
                        if (Global.subAisleItemBean.getBrand() != null) {
                            newOrder.setBrandName(Global.subAisleItemBean.getBrand().getBrandName());
                        } else {
                            newOrder.setBrandName(BuildConfig.VERSION_NAME);
                        }
                        newOrder.setOrderQuantity(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                        newOrder.setApplicableSaleDescription(BuildConfig.VERSION_NAME);
                        newOrder.setUom(Global.subAisleItemBean.getUom());
                        newOrder.setSubstitutionWith("Store's choice");
                        newOrder.setMaxQuantity(Global.subAisleItemBean.getMaxQuantity());
                        db.addOrderInCart(newOrder);
                        addBagLayout.setEnabled(false);
                        cartCountLayout.setVisibility(View.VISIBLE);
                        cartCountText.setText(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                        bottomLayout.setVisibility(View.VISIBLE);
                        countLayout.setVisibility(View.VISIBLE);
                        totalCartCountText.setText(String.valueOf(db.getAllOrders().size()));
                    }
                    refreshData();
                    checkAnyOrder();
                    return;
                }
                showAlert(Constants.MAX_QUANTITY);
            }
        });
        plusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(ItemDetailActivity.this);
                OrderBean updateOrderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity()) + 1;
                    int max = Integer.parseInt(Global.subAisleItemBean.getMaxQuantity());
                    if (max <= 0 || quantity <= max) {
                        double finalPrice = Double.parseDouble(updateOrderBean.getUnitPrice()) * ((double) quantity);
                        updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                        updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(updateOrderBean);
                        cartCountText.setText(String.valueOf(quantity));
                    } else {
                        showAlert(Constants.MAX_QUANTITY);
                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        refreshData();
                        checkAnyOrder();
                    }
                });
            }
        });
        minusBtn.setOnClickListener(new OnClickListener() {
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
                                countLayout.setVisibility(View.GONE);
                            } else {
                                totalCartCountText.setText(String.valueOf(cartUpdatedItem));
                            }
                        }
                        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                        for (int k = 0; k < suppliersBeanList.size(); k++) {
                            SuppliersBean suppliersBean = (SuppliersBean) suppliersBeanList.get(k);
                            if (db.getAllOrders(suppliersBean).size() == 0) {
                                db.deleteSupplier(suppliersBean);
                            }
                        }
                    } else {
                        itemQuantity--;
                        double finalPrice = Double.parseDouble(minusOrderBean.getUnitPrice()) * ((double) itemQuantity);
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
        layoutTextMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Animation animShow;
                if (isMore) {
                    animShow = AnimationUtils.loadAnimation(ItemDetailActivity.this, R.anim.aplha_hide);
                    imageMoreArrow.setImageResource(R.drawable.arrrow);
                    moreItemListView.startAnimation(animShow);
                    moreItemListView.setVisibility(View.GONE);
                    isMore = false;
                    scroll.fullScroll(33);
                } else if (moreItemBean.getSimilarItemsList() != null) {
                    moreItemListView.setVisibility(View.VISIBLE);
                    animShow = AnimationUtils.loadAnimation(ItemDetailActivity.this, R.anim.alpha_show);
                    imageMoreArrow.setImageResource(R.drawable.image_more_arrow);
                    moreItemListView.setAnimation(animShow);
                    isMore = true;
                    scroll.fullScroll(130);
                }
            }
        });
        similarItemListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubAisleItemBean item = (SubAisleItemBean) parent.getItemAtPosition(position);
                itemName = item.getItemName();
                supplierItemId = item.getSupplierItemId();
                Global.subAisleItemBean = item;
                setItemDetails();
                setDetailOnStart();
                checkAnyOrder();
            }
        });
        moreItemListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubAisleItemBean item = (SubAisleItemBean) parent.getItemAtPosition(position);
                itemName = item.getItemName();
                supplierItemId = item.getSupplierItemId();
                Global.subAisleItemBean = item;
                setItemDetails();
                setDetailOnStart();
                checkAnyOrder();
            }
        });
    }

    void setItemDetails() {
        if (db.getLikeItem(Global.subAisleItemBean.getSupplierItemId()) != null) {
            likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }
        if (Global.subAisleItemBean.getBrand() != null) {
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
        List<String> str = new ArrayList();
        str.add(Global.subAisleItemBean.getDescription());
        if (Global.subAisleItemBean.getNutritutionBean() != null) {
            nutritionLayout.setVisibility(View.VISIBLE);
            nutritionImg.setVisibility(View.VISIBLE);
            String temp = BuildConfig.VERSION_NAME;
            NutritutionBean nut = Global.subAisleItemBean.getNutritutionBean();
            str.add(((((((((((((temp + nut.getNutritionCalories() + ",") + nut.getNutritionCholestrol() + ",") + nut.getNutritionDietaryFiber() + ",") + nut.getNutritionMonoUnsatFat() + ",") + nut.getNutritionPolyUnsatFat() + ",") + nut.getNutritionProtein() + ",") + nut.getNutritionSaturatedFat() + ",") + nut.getNutritionSodium() + ",") + nut.getNutritionSugar() + ",") + nut.getNutritionTotalCarbs() + ",") + nut.getNutritionTotalFat() + ",") + nut.getNutritionTransFat()).replace("null", BuildConfig.VERSION_NAME));
        } else {
            nutritionLayout.setVisibility(View.GONE);
            nutritionImg.setVisibility(View.GONE);
        }
        viewPagerItemAdapter = new ViewPagerItemAdapter(this, str);
        sliding_pager.setAdapter(viewPagerItemAdapter);
        checkViewPager();
        String onSale = Global.subAisleItemBean.getSale();
        String[] temp2;
        if (onSale == null) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            temp2 = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(Global.subAisleItemBean.getPrice()))}).split("\\.");
            salePriceText.setText(temp2[0]);
            textSalePriceDecimal.setText(temp2[1]);
        } else if (onSale.equalsIgnoreCase("null")) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            temp2 = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(Global.subAisleItemBean.getPrice()))}).split("\\.");
            salePriceText.setText(temp2[0]);
            textSalePriceDecimal.setText(temp2[1]);
        } else if (onSale.equalsIgnoreCase("no")) {
            onsaleText.setVisibility(View.GONE);
            priceLayout.setVisibility(View.GONE);
            temp2 = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(Global.subAisleItemBean.getPrice()))}).split("\\.");
            salePriceText.setText(temp2[0]);
            textSalePriceDecimal.setText(temp2[1]);
        } else {
            onsaleText.setVisibility(View.VISIBLE);
            priceLayout.setVisibility(View.VISIBLE);
            temp2 = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(Global.subAisleItemBean.getPrice()))}).split("\\.");
            priceText.setText(temp2[0]);
            textPriceDecimal.setText(temp2[1]);
            double price = Double.parseDouble(Global.subAisleItemBean.getSalePrice());
            String[] temp1 = String.format("%.2f", new Object[]{Double.valueOf(price)}).split("\\.");
            if (temp2.length > 1) {
                salePriceText.setText(temp2[0]);
                textSalePriceDecimal.setText(temp2[1]);
            } else {
                salePriceText.setText(temp2[0]);
                textSalePriceDecimal.setText("00");
            }
        }
        itemDescriptionText.setText(Global.subAisleItemBean.getItemName());
        String quantity = Global.subAisleItemBean.getCount();
        if (quantity.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO) || quantity.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
            quantityText.setText(Global.subAisleItemBean.getSize() + " " + Global.subAisleItemBean.getUom());
        } else {
            quantityText.setText(Global.subAisleItemBean.getCount() + " X " + Global.subAisleItemBean.getSize() + " " + Global.subAisleItemBean.getUom());
        }
        if (Global.subAisleItemBean.getOffer() == null) {
            offerText.setText(BuildConfig.VERSION_NAME);
            offerText.setVisibility(View.GONE);
        } else if (Global.subAisleItemBean.getOffer().equalsIgnoreCase("null")) {
            offerText.setText(BuildConfig.VERSION_NAME);
            offerText.setVisibility(View.GONE);
        } else {
            offerText.setVisibility(View.VISIBLE);
            offerText.setText(Global.subAisleItemBean.getOffer());
        }
    }

    public void showLargeImage() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.popup_product_image, null);
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        ImageButton closeButton = (ImageButton) promptsView.findViewById(R.id.close_button);
        ViewPager popupPager = (ViewPager) promptsView.findViewById(R.id.popup_pager);
        CirclePageIndicator popupIndicator = (CirclePageIndicator) promptsView.findViewById(R.id.popup_indicator);
        popupPager.setAdapter(this.popupPagerAdapter);
        if (Global.subAisleItemBean.getImagesBeanList().size() != 1) {
            popupIndicator.setViewPager(popupPager);
        }
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void checkAnyOrder() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<OrderBean> orderBeanList = db.getAllOrders();
        if (orderBeanList == null) {
            countLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        } else if (orderBeanList.size() > 0) {
            TextView totalCartCountText = (TextView) findViewById(R.id.text_total_cart_count);
            TextView totalPriceText = (TextView) findViewById(R.id.text_order_price);
            countLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            totalCartCountText.setText(String.valueOf(db.getOrdersCount()));
            OrderBean orderBean = db.getOrder(Global.subAisleItemBean.getSupplierItemId());
            if (orderBean != null) {
                int orderQuantity = Integer.parseInt(orderBean.getOrderQuantity());
                if (orderQuantity > 0) {
                    cartCountLayout.setVisibility(View.VISIBLE);
                    cartCountText.setText(String.valueOf(orderQuantity));
                    addToBtn.setEnabled(false);
                } else {
                    cartCountLayout.setVisibility(View.GONE);
                    addBagLayout.setVisibility(View.VISIBLE);
                    addToBtn.setEnabled(true);
                    addBagLayout.setEnabled(false);
                }
            } else {
                cartCountLayout.setVisibility(View.GONE);
                addBagLayout.setVisibility(View.VISIBLE);
                addToBtn.setEnabled(true);
                addBagLayout.setEnabled(true);
            }
            double total = 0.0d;
            for (int i = 0; i < orderBeanList.size(); i++) {
                total += Double.valueOf(((OrderBean) orderBeanList.get(i)).getFinalPrice()).doubleValue();
            }
            totalPriceText.setText(" $" + String.format("%.2f", new Object[]{Double.valueOf(total)}));
            progressBar = (ProgressBar) findViewById(R.id.firstBar);
            txtProgress = (TextView) findViewById(R.id.text_progress);
            int temp = Integer.parseInt(String.valueOf(total).split("\\.")[0]);
            if (Global.promotionAvailableBean == null) {
                progressBar.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
            } else if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                double addMore;
                progressBar.setVisibility(View.VISIBLE);
                txtProgress.setVisibility(View.VISIBLE);
                progressBar.setMax(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());
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
                    return;
                }
                String text = String.format("%.2f", new Object[]{Double.valueOf(addMore)});
                progressBar.setProgress(temp);
                txtProgress.setText("Add $" + text + " for free delivery");
                Global.isFreeDelivery = false;
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
        String subCategoryId;
        Global.showProgress(this);
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        String category = pref.getString(Constants.CATEGORY, null);
        if (category == null) {
            subCategoryId = Global.subAisleItemBean.getSubCategoryId();
        } else if (category.equalsIgnoreCase("sub")) {
            subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
        } else {
            subCategoryId = Global.subAisleItemBean.getSubCategoryId();
        }
        String url = "product/similar?supplier_id=" + supplierId + "&sub_category_id=" + subCategoryId + "&supplier_item_id=" + supplierItemId;
        RequestQueue rq = Volley.newRequestQueue(this);
        Log.d("similarURL", Constants.URL + url);
        final String finalSubCategoryId = subCategoryId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(0, Constants.URL + url, null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
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
                        } else if (brandBean.getBrandName() == null) {
                            Global.dialog.dismiss();
                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    getMoreItems(finalSubCategoryId, supplierId, brandBean.getBrandId(), supplierItemId, brandBean.getBrandName());
                                }
                            });
                        }
                        new WrappingLinearLayoutManager(ItemDetailActivity.this).setOrientation(0);
                        if (similarItemBean.getSimilarItemsList() != null) {
                            layoutSimilar.setVisibility(View.VISIBLE);
                            similarItemListView.setVisibility(View.VISIBLE);
                            similarItemListAdapter = new SimilarItemListBaseAdapter(ItemDetailActivity.this, ItemDetailActivity.this, similarItemBean.getSimilarItemsList());
                            similarItemListView.setAdapter(similarItemListAdapter);
                            return;
                        }
                        layoutSimilar.setVisibility(View.GONE);
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                        Global.dialog.dismiss();
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Global.dialog.dismiss();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.ERROR, "Error: " + error.toString());
                Global.dialog.dismiss();
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

    private void getMoreItems(String finalSubCategoryId, String supplierId, String brandId, String supplierItemId, final String brandName) {
        String url = "product/more?supplier_id=" + supplierId + "&sub_category_id=" + finalSubCategoryId + "&brand_id=" + brandId + "&topn=2" + "&supplier_item_id=" + supplierItemId;
        RequestQueue rq = Volley.newRequestQueue(this);
        Log.d("moreURL", Constants.URL + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(0, Constants.URL + url, null, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
                Global.dialog.dismiss();
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        moreItemBean = ConversionHelper.convertSimilarItemJsonToSimilarItemBean(jsonObject);
                        new WrappingLinearLayoutManager(ItemDetailActivity.this).setOrientation(0);
                        if (moreItemBean.getSimilarItemsList() != null) {
                            moreLayout.setVisibility(View.VISIBLE);
                            moreItemsListBaseAdapter = new MoreItemsListBaseAdapter(ItemDetailActivity.this, ItemDetailActivity.this, moreItemBean.getSimilarItemsList());
                            moreItemListView.setAdapter(moreItemsListBaseAdapter);
                            moreText.setText("More from " + brandName);
                            return;
                        }
                        moreLayout.setVisibility(View.GONE);
                    } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                        Global.dialog.dismiss();
                        showAlert(jsonObject.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Global.dialog.dismiss();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.ERROR, "Error: " + error.toString());
                Global.dialog.dismiss();
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

    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void onResume() {
        super.onResume();
        checkAnyOrder();
    }

    public void onStart() {
        super.onStart();
        setDetailOnStart();
    }

    public void setDetailOnStart() {
        List<ImagesBean> mainImagesBeanList;
        List<ImagesBean> imagesBeanList = new ArrayList();
        ImagesBean imagesBean;
        if (Global.subAisleItemBean.getImagesBeanList() == null) {
            imagesBean = new ImagesBean();
            imagesBean.setImage("www.google.com");
            imagesBeanList.add(imagesBean);
            Global.subAisleItemBean.setImagesBeanList(imagesBeanList);
        } else if (Global.subAisleItemBean.getImagesBeanList().size() == 0) {
            imagesBean = new ImagesBean();
            imagesBean.setImage("www.google.com");
            imagesBeanList.add(imagesBean);
            Global.subAisleItemBean.setImagesBeanList(imagesBeanList);
        } else {
            imagesBeanList = Global.subAisleItemBean.getImagesBeanList();
        }
        if (Global.subAisleItemBean.getMainImagesBeanList() == null) {
            mainImagesBeanList = imagesBeanList;
        } else if (Global.subAisleItemBean.getMainImagesBeanList().size() > 0) {
            mainImagesBeanList = Global.subAisleItemBean.getMainImagesBeanList();
        } else {
            mainImagesBeanList = imagesBeanList;
        }
        adapter = new ViewPagerAdapter(this, imagesBeanList, this);
        popupPagerAdapter = new PopupPagerAdapter(this, mainImagesBeanList, this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(Global.subAisleItemBean.getImagesBeanList().size());
        if (Global.subAisleItemBean.getImagesBeanList().size() != 1) {
            mIndicator.setViewPager(viewPager);
        }
        runOnUiThread(new Runnable() {
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