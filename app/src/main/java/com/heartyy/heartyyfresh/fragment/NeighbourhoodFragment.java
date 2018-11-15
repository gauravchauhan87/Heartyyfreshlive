package com.heartyy.heartyyfresh.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.ChangeZipActivity;
import com.heartyy.heartyyfresh.CheckoutActivity;
import com.heartyy.heartyyfresh.DeliveryEstimateActivity;
import com.heartyy.heartyyfresh.HomeActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.SignUpActivity;
import com.heartyy.heartyyfresh.StoreDetailActivity;
import com.heartyy.heartyyfresh.adapter.CustomPageAdapter;
import com.heartyy.heartyyfresh.adapter.CustomStoreListAdapter;
import com.heartyy.heartyyfresh.adapter.PopupStoreRatingListAdapter;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.PopupRatingBean;
import com.heartyy.heartyyfresh.bean.PromotionBean;
import com.heartyy.heartyyfresh.bean.StoreBean;
import com.heartyy.heartyyfresh.bean.SupplierRatingBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.bean.UserCreditsBean;
import com.heartyy.heartyyfresh.bean.UserProfileBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.promotionbean.FreeDeliveryMinOrder;
import com.heartyy.heartyyfresh.promotionbean.PromotionAvailableBean;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.GPSTracker;
import com.heartyy.heartyyfresh.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.heartyy.heartyyfresh.R.id.text_driver_name;

/**
 * Created by Gaurav on 11/23/2015.
 */
@SuppressLint("ValidFragment")
public class NeighbourhoodFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private View rootView;
    private Context context;
    private ListView storeListView;
    private CustomStoreListAdapter adapter;
    private StoreBean storeBeanList;
    Typeface andBold, bold, italic, light;
    private Button closeBtn;
    private View bannerFragment;
    private RelativeLayout storeLayout, showOfferLayout, changeZipLayout;
    private boolean animationIsOn = false;
    private TextView addDeliveryFeeText, userZipcodeText, shoppingIn, chooseStore, changeZipBtn, txtProgress;
    private SharedPreferences pref;
    private String zip, currentZip;
    private RelativeLayout showOfferImage;
    Bundle bundle;
    private RelativeLayout countLayout, bottomLayout;
    private ImageButton bagButton;
    ProgressBar progressBar;
    private List<PromotionAvailableBean> promotionAvailableBeanList;
    private ListView storeRatingListView;
    private PopupRatingBean ratingBean;
    private PopupStoreRatingListAdapter storeRatingListAdapter;
    private int driverRating = 0;
    private UserProfileBean userProfileBean;
    private UserCreditsBean userCreditsBean;
    HomeActivity activity;
    ViewPager pager;
    PageIndicator mIndicator;

    @SuppressLint("ValidFragment")
    public NeighbourhoodFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_neighbourhood, container, false);

        if (getActivity().getApplicationContext() != null) {
            context = getActivity().getApplicationContext();
        }
        activity = (HomeActivity) getActivity();

        andBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_LIGHT);
        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.neighborhood_main);
        Global.setFont(root, andBold);
        pref = context.getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        storeListView = (ListView) rootView.findViewById(R.id.list_stores);
        changeZipLayout = (RelativeLayout) rootView.findViewById(R.id.layout_change_zip);
        countLayout = (RelativeLayout) rootView.findViewById(R.id.layout_total_cart_count);
        bottomLayout = (RelativeLayout) rootView.findViewById(R.id.layout_bottom);

        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            zip = getActivity().getIntent().getExtras().getString("zip");
        }

        closeBtn = (Button) rootView.findViewById(R.id.button_close);
        bannerFragment = rootView.findViewById(R.id.layout_fragment);
        storeLayout = (RelativeLayout) rootView.findViewById(R.id.layout_store);
        showOfferLayout = (RelativeLayout) rootView.findViewById(R.id.layout_show_offer);
        showOfferImage = (RelativeLayout) rootView.findViewById(R.id.image_show_offer);
        userZipcodeText = (TextView) rootView.findViewById(R.id.text_user_zipcode);
        shoppingIn = (TextView) rootView.findViewById(R.id.text_shoppingin);
        changeZipBtn = (TextView) rootView.findViewById(R.id.button_change_zip);
        chooseStore = (TextView) rootView.findViewById(R.id.text_choose_store);
        txtProgress = (TextView) rootView.findViewById(R.id.text_progress);
        bagButton = (ImageButton) rootView.findViewById(R.id.image_bag);
        progressBar = (ProgressBar) rootView.findViewById(R.id.firstBar);

        pager = rootView.findViewById(R.id.pager);
        mIndicator = rootView.findViewById(R.id.indicator);



        chooseStore.setTypeface(light);
        changeZipBtn.setTypeface(light);
        shoppingIn.setTypeface(light);
        userZipcodeText.setTypeface(andBold);
        if (zip != null) {
            userZipcodeText.setText(zip);
        }
        if (Global.zip != null) {
            userZipcodeText.setText(Global.zip);
        }

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SuppliersBean suppliersBean = (SuppliersBean) adapterView.getItemAtPosition(i);
                if (Global.isDeliveryBack) {
                    if (suppliersBean.getEstimatedCostBreakDownBean() != null) {
                        Intent intent = new Intent(context, DeliveryEstimateActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Global.supplierId = suppliersBean.getSupplierId();
                        String[] time = suppliersBean.getEstimateDeliveryTime().split(" ");
                        String day = "";
                        if (time.length > 2) {
                            day = time[0] + " " + "by" + " " + time[1] + " " + time[2];
                        } else {
                            day = time[0] + " " + "by" + " " + time[1];
                        }
                        intent.putExtra("zipcode", Global.zip);
                        intent.putExtra("address_id", "");
                        intent.putExtra("id", suppliersBean.getSupplierId());
                        intent.putExtra("store", suppliersBean.getSupplierName());
                        intent.putExtra("day", day);
                        DeliveryEstimateActivity.selectedDeliveryLocationPosition = 0;
                        Global.estimatedCostBreakDownBean = suppliersBean.getEstimatedCostBreakDownBean();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.SUPPLIER_ID, suppliersBean.getSupplierId());
                        editor.putString(Constants.SUPPLIER_NAME, suppliersBean.getSupplierName());
                        editor.putString(Constants.APPLICABLE_TAX_RATE, suppliersBean.getApplicableTaxRate());
                        editor.apply();
                        Global.isDeliveryBack = false;
                        context.startActivity(intent);
                    } else {
                        showAlert(Constants.SERVER_ERROR);
                    }
                } else {
                    if (!suppliersBean.getComingSoon().equalsIgnoreCase("YES")) {
                        adapter.setSelected(i);
                        String[] time = suppliersBean.getEstimateDeliveryTime().split(" ");
                        String day = "";
                        if (time.length > 2) {
                            day = time[0] + " " + "by" + " " + time[1] + " " + time[2];
                        } else {
                            day = time[0] + " " + "by" + " " + time[1];
                        }
                        adapter.notifyDataSetChanged();
                        Global.pos = i;
                        Intent intent = new Intent(context, StoreDetailActivity.class);
                        intent.putExtra("id", suppliersBean.getSupplierId());
                        intent.putExtra("store", suppliersBean.getSupplierName());
                        intent.putExtra("day", day);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.SUPPLIER_ID, suppliersBean.getSupplierId());
                        editor.putString(Constants.SUPPLIER_NAME, suppliersBean.getSupplierName());
                        editor.putString(Constants.APPLICABLE_TAX_RATE, suppliersBean.getApplicableTaxRate());
                        editor.apply();
                    }
                }
            }
        });


        changeZipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChangeZipActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        bagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = pref.getString(Constants.USER_ID, null);
                if (userid == null) {
                    // showAlert("Please login to place your order");
                    Intent intent = new Intent(context, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } else {
                    if (Global.zip != null) {
                        Intent intent = new Intent(context, CheckoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!animationIsOn) {
                    final int height = bannerFragment.getMeasuredHeight();
                    Animation animHide = AnimationUtils.loadAnimation(context, R.anim.aplha_hide);

                    bannerFragment.startAnimation(animHide);
                    bannerFragment.setVisibility(View.INVISIBLE);
                    showOfferLayout.setVisibility(View.VISIBLE);
                    Animation animshow = AnimationUtils.loadAnimation(context, R.anim.alpha_show);
                    showOfferLayout.startAnimation(animshow);

                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -height);
                    anim.setDuration(1000);
                    anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                            storeLayout.setDrawingCacheEnabled(true);
                            animationIsOn = true;
                            closeBtn.setEnabled(false);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            storeLayout.setDrawingCacheEnabled(false);
                            storeLayout.clearAnimation();
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) storeLayout.getLayoutParams();
                            params.topMargin -= height;
                            storeLayout.setLayoutParams(params);
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            animation.setDuration(1);
                            storeLayout.startAnimation(animation);
                            bannerFragment.setVisibility(View.INVISIBLE);
                            animationIsOn = false;
                            showOfferImage.setEnabled(true);
                        }
                    });
                    storeLayout.startAnimation(anim);
                }

            }
        });

        showOfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!animationIsOn) {
                    final int height = bannerFragment.getMeasuredHeight();
                    Animation animshow = AnimationUtils.loadAnimation(context, R.anim.alpha_show);
                    bannerFragment.startAnimation(animshow);
                    bannerFragment.setVisibility(View.VISIBLE);
                    showOfferLayout.setVisibility(View.INVISIBLE);
                    Animation animhide = AnimationUtils.loadAnimation(context, R.anim.aplha_hide);
                    showOfferLayout.startAnimation(animhide);

                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, height);
                    anim.setDuration(1000);

                    anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            storeLayout.setDrawingCacheEnabled(true);
                            animationIsOn = true;
                            showOfferImage.setEnabled(false);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            storeLayout.setDrawingCacheEnabled(false);
                            storeLayout.clearAnimation();
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) storeLayout.getLayoutParams();
                            params.topMargin += height;
                            storeLayout.setLayoutParams(params);
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            animation.setDuration(1);
                            storeLayout.startAnimation(animation);
                            bannerFragment.setVisibility(View.VISIBLE);
                            animationIsOn = false;
                            closeBtn.setEnabled(true);
                        }
                    });
                    storeLayout.startAnimation(anim);
                }

            }
        });
        return rootView;

    }


    private void getNeighbourhoodStore() {
        if (!Global.changeZip) {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            List<LocationBean> locationBeanList = db.getAllDeliveryaddress();
            int flag = 0;
            if (locationBeanList != null) {
                if (locationBeanList.size() > 0) {
                    for (int i = 0; i < locationBeanList.size(); i++) {
                        LocationBean locationBean = locationBeanList.get(i);
                        if (locationBean.getIsPrimaryLocation().equalsIgnoreCase("yes")) {
                            flag = 1;
                            Global.zip = locationBean.getZipcode();
                            break;
                        }
                    }
                }
            }
        }

        userZipcodeText.setText(Global.zip);
        TimeZone tz = TimeZone.getDefault();
        Log.d("TimeZone   ", tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());
        if (getActivity() != null) {
            Global.showProgress(getActivity());
        }
        String url;

        if (Global.zip == null) {
            if (currentZip != null) {
                Global.zip = currentZip;
            } else {
                Global.zip = pref.getString(Constants.ZIP, null);
            }
        }
        userZipcodeText.setText(Global.zip);

        if (pref.getString(Constants.USER_ID, null) != null) {
            url = "service/available?zipcode=" + Global.zip + "&user_id=" + pref.getString(Constants.USER_ID, null) + "&timezone=" + tz.getID();
        } else {
            url = "service/available?zipcode=" + Global.zip + "&timezone=" + tz.getID();
        }

        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                chooseStore.setText(R.string.neighbourhood_store);
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                String available = dataObj.getString("available");
                                if (available.equalsIgnoreCase("YES")) {
                                    storeBeanList = ConversionHelper.convertStoreJsonToStoreBeanList(jsonObject);
                                    activity.setCount(storeBeanList.getPromotionCount(), storeBeanList.getCurrentOrderCount());
                                    String hasAddress = storeBeanList.getHasAddress();
                                    if (storeBeanList.getSuppliersBeanList() != null) {
                                        adapter = new CustomStoreListAdapter(context, storeBeanList.getSuppliersBeanList(), hasAddress, getActivity());
                                        storeListView.setAdapter(adapter);
                                        adapter.setSelected(Global.pos);
                                        adapter.notifyDataSetChanged();
                                        checkAvailablePromotion();
                                    } else {
                                        Global.hideProgress();
                                    }
                                    if (storeBeanList.getPromotionBeanList() != null) {
                                        bannerFragment.setVisibility(View.VISIBLE);
                                        configSlider(storeBeanList.getPromotionBeanList());
//                                        try {
//                                            //todo : modify the code here
//                                            BannerPagerFragment pagerFragment = new BannerPagerFragment(context, storeBeanList.getPromotionBeanList());
//                                            FragmentManager fragmentManager = getFragmentManager();
//                                            if (fragmentManager != null) {
//                                                fragmentManager.beginTransaction()
//                                                        .replace(R.id.banner_fragment, pagerFragment)
//                                                        .commit();
//                                            }
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            Intent intent = new Intent(context, HomeActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            context.startActivity(intent);
//                                            getActivity().finish();
//                                        }
                                    } else {
                                        bannerFragment.setVisibility(View.GONE);
                                    }
                                } else {
                                    showNoEmailAlert(Global.zip);
                                }


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.hideProgress();
                                chooseStore.setText(R.string.no_store_avl);

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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void configSlider(List<PromotionBean> promotionBeanList) {
        CustomPageAdapter pagerAdapter = new CustomPageAdapter(getContext(), promotionBeanList);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        mIndicator.setViewPager(pager);
    }



    public void checkAvailablePromotion() {
        RequestQueue rq = null;
        if (getActivity() != null) {
            rq = Volley.newRequestQueue(getActivity());
        }
        final DatabaseHandler db = new DatabaseHandler(getActivity());
        double price, salesPrice;

        JSONObject params = new JSONObject();
        try {
            JSONArray supArray = new JSONArray();


            for (int i = 0; i < storeBeanList.getSuppliersBeanList().size(); i++) {
                SuppliersBean suppliersBean = storeBeanList.getSuppliersBeanList().get(i);

                supArray.put(suppliersBean.getSupplierId());
            }
            params.put("supplier_id", supArray);
            params.put("zipcode", Global.zip);
            params.put("user_id", pref.getString(Constants.USER_ID, null));


        } catch (JSONException e) {

            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "promotion/available", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("promotion response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                promotionAvailableBeanList = ConversionHelper.convertPromotionAvailableJsonToPromotionAvailableBean(jsonObject);
                               /* if (promotionAvailableBeanList != null) {
                                    Global.promotionAvailableBean = promotionAvailableBeanList.get(0);
                                } else {
                                    Global.promotionAvailableBean = null;
                                }*/

                                if (storeBeanList.getNewUserPromotionBean() != null) {
                                    if (storeBeanList.getNewUserPromotionBean().getFreeDelivery().equalsIgnoreCase("yes")) {
                                        if (storeBeanList.getNewUserPromotionBean().getMinOrderAmount().equalsIgnoreCase("0")) {
                                            FreeDeliveryMinOrder freeDeliveryMinOrder = new FreeDeliveryMinOrder();
                                            freeDeliveryMinOrder.setFreeDelivery("yes");
                                            freeDeliveryMinOrder.setMinOrderAmnt("0");
                                            if (Global.promotionAvailableBean == null) {
                                                Global.promotionAvailableBean = new PromotionAvailableBean();
                                            }
                                            Global.promotionAvailableBean.setFreeDeliveryMinOrder(freeDeliveryMinOrder);
                                        } else {
                                           /* if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                                                if (Double.parseDouble(storeBeanList.getNewUserPromotionBean().getMinOrderAmount()) < Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt())) {
                                                    FreeDeliveryMinOrder freeDeliveryMinOrder = new FreeDeliveryMinOrder();
                                                    freeDeliveryMinOrder.setFreeDelivery("yes");
                                                    freeDeliveryMinOrder.setMinOrderAmnt(storeBeanList.getNewUserPromotionBean().getMinOrderAmount());
                                                    if (Global.promotionAvailableBean == null) {
                                                        Global.promotionAvailableBean = new PromotionAvailableBean();
                                                    }
                                                    Global.promotionAvailableBean.setFreeDeliveryMinOrder(freeDeliveryMinOrder);
                                                }
                                            }else{
                                                FreeDeliveryMinOrder freeDeliveryMinOrder = new FreeDeliveryMinOrder();
                                                freeDeliveryMinOrder.setFreeDelivery("yes");
                                                freeDeliveryMinOrder.setMinOrderAmnt(storeBeanList.getNewUserPromotionBean().getMinOrderAmount());
                                                if (Global.promotionAvailableBean == null) {
                                                    Global.promotionAvailableBean = new PromotionAvailableBean();
                                                }
                                                Global.promotionAvailableBean.setFreeDeliveryMinOrder(freeDeliveryMinOrder);
                                            }*/

                                            FreeDeliveryMinOrder freeDeliveryMinOrder = new FreeDeliveryMinOrder();
                                            freeDeliveryMinOrder.setFreeDelivery("yes");
                                            freeDeliveryMinOrder.setMinOrderAmnt(storeBeanList.getNewUserPromotionBean().getMinOrderAmount());
                                            if (Global.promotionAvailableBean == null) {
                                                Global.promotionAvailableBean = new PromotionAvailableBean();
                                            }
                                            Global.promotionAvailableBean.setFreeDeliveryMinOrder(freeDeliveryMinOrder);
                                        }
                                    }
                                }

                                setTotalAmount();

                                if (pref.getString(Constants.USER_ID, null) != null) {
                                    getUserProfile();
                                } else {
                                    Global.hideProgress();
                                }


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.hideProgress();

                                setTotalAmount();
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
                VolleyLog.d("error", "Error: " + error.toString());
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

// Adding request to request queue
        if (rq != null) {
            rq.add(jsonObjReq);
        }
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void showRating() {
        RequestQueue rq = null;
        if (getActivity() != null) {
            rq = Volley.newRequestQueue(getActivity());
        }
        JSONObject params = new JSONObject();
        try {

            params.put("user_id", pref.getString(Constants.USER_ID, null));


        } catch (JSONException e) {

            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "getreviews", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("rating response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.hideProgress();
                                ratingBean = ConversionHelper.convertSupplierRatingJsonToSupplierRatingBean(jsonObject);
                                if (ratingBean != null) {
                                    Global.ratingBean = ratingBean;
                                    showRatingPopup(ratingBean);
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.hideProgress();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.hideProgress();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
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

// Adding request to request queue
        if (rq != null) {
            rq.add(jsonObjReq);
        }
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void getUserProfile() {
        String url = "profile?user_id=" + pref.getString(Constants.USER_ID, null);
        RequestQueue rq = Volley.newRequestQueue(context);
        Log.d("USER_ID", "" + pref.getString(Constants.USER_ID, null));
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Global.hideProgress();
                        Log.d("response", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.hideProgress();
                                userProfileBean = ConversionHelper.convertUserProfileJsonToUserProfileBean(response);
                                if (userProfileBean != null) {
                                    userCreditsBean = userProfileBean.getUserCreditsBean();
                                    if (userCreditsBean != null) {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(Constants.CREDITS_AMOUNT, userCreditsBean.getTotalCreditAmount());
                                        editor.apply();
                                    }

                                }
                                if (storeBeanList != null) {
                                    if (!storeBeanList.getRating().equalsIgnoreCase("YES")) {
                                        showRating();
                                    }
                                }
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                //Global.hideProgress();
                            }

                        } catch (JSONException e) {
//                           Global.hideProgress();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Global.hideProgress();
                Log.d("response", error.toString());
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

    private void showNoEmailAlert(final String zipCode) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptsView = layoutInflater.inflate(R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button keepInformedBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        keepInformedBtn.setTypeface(andBold);
        titleText.setText("We are not in " + zipCode + " just yet\nWe will inform you once we start serving this area");
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
        keepInformedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInterest(zipCode, pref.getString(Constants.EMAIL, null));
                if (dialog != null && dialog.isShowing())
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing())
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
    }

    private void userInterest(String zipCode, String email) {
        if (getActivity() != null) {
            Global.showProgress(getActivity());
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            String device = "Android";
            String androidOsVersion = getDeviceOs();
            params.put("email", email);
            params.put("zipcode", zipCode);
            params.put("phone_type", device);
            params.put("os_version", androidOsVersion);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "interest", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                showBackAlert(message);

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Intent intent = new Intent(context, ChangeZipActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Global.hideProgress();
                            showAlert("Something went wrong!");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
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
    }

    public static String getDeviceOs() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }
        return "OS: " + builder.toString();

    }

    private void showRatingPopup(final PopupRatingBean ratingBean) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptsView = layoutInflater.inflate(R.layout.popup_store_rating, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView thanksText = (TextView) promptsView.findViewById(R.id.text_thanks);
        TextView rateText = (TextView) promptsView.findViewById(R.id.text_rate);
        TextView rateStaffText = (TextView) promptsView.findViewById(R.id.text_rate_staff);
        RatingBar driverRatingBar = (RatingBar) promptsView.findViewById(R.id.ratingBar);
        Button notNowBtn = (Button) promptsView.findViewById(R.id.button_not_now);
        Button rateUsBtn = (Button) promptsView.findViewById(R.id.button_rate_us);
        TextView driverName = (TextView) promptsView.findViewById(text_driver_name);
        storeRatingListView = (ListView) promptsView.findViewById(R.id.list_stores_rating);
        ImageView userImage = (ImageView) promptsView.findViewById(R.id.image_user);

        driverRatingBar.setRating(0);
        driverRatingBar.setMax(5);
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

        LayerDrawable stars = (LayerDrawable) driverRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.hearty_star), mMode);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context, R.color.hearty_star), mMode);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context, R.color.edit_line_zip), mMode);

        driverName.setTypeface(andBold);
        thanksText.setTypeface(andBold);
        rateStaffText.setTypeface(light);
        rateText.setTypeface(light);
        rateUsBtn.setTypeface(andBold);
        notNowBtn.setTypeface(andBold);

        driverName.setText(ratingBean.getDriverName());

        storeRatingListAdapter = new PopupStoreRatingListAdapter(context, ratingBean.getSupplierRatingBeanList());
        storeRatingListView.setAdapter(storeRatingListAdapter);
        Global.setListViewHeightBasedOnChildren(storeRatingListView);

        driverRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                driverRating = (int) ratingBar.getRating();
            }
        });

        setDriverImage(ratingBean, userImage);

        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRating(ratingBean, "no");
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });

        notNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(ratingBean, "yes");
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void setDriverImage(final PopupRatingBean ratingBean, final ImageView userImage) {
        android.os.Handler handler = new android.os.Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + ratingBean.getDriverPicture(), ImageLoader.getImageListener(
                        userImage, R.drawable.user_icon, R.drawable.user_icon));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + ratingBean.getDriverPicture());
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        // handle data, like converting it to xml, json, bitmap etc.,
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    // cached response doesn't exists. Make a network call here
                }

            }
        });
    }

    private void setRating(PopupRatingBean ratingBean, final String avoide) {

        if (getActivity() != null) {
            Global.showProgress(getActivity());
        }
        RequestQueue rq = Volley.newRequestQueue(context);

        JSONObject params = new JSONObject();
        try {

            params.put("order_id", ratingBean.getOrderId());
            params.put("user_id", ratingBean.getUserId());
            params.put("order_rating", String.valueOf(driverRating));
            params.put("user_avoid", avoide);
            params.put("display_order_id", ratingBean.getDisplayOrderId());
            params.put("driver_id", ratingBean.getDriverId());
            params.put("actual_delivery_date", ratingBean.getActualDeliveryDate());
            params.put("actual_delivery_time", ratingBean.getActualDeliveryTime());

            JSONArray supparray = new JSONArray();
            for (int i = 0; i < Global.ratingBean.getSupplierRatingBeanList().size(); i++) {
                SupplierRatingBean supplierRatingBean = Global.ratingBean.getSupplierRatingBeanList().get(i);
                JSONObject supObj = new JSONObject();
                supObj.put("supplier_id", supplierRatingBean.getSupplierId());
                supObj.put("supplier_rating", supplierRatingBean.getRating());
                supObj.put("order_supplier_id", supplierRatingBean.getOrderSupplierId());
                supparray.put(supObj);
            }

            params.put("suppliers", supparray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "postreviews", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                if (avoide.equalsIgnoreCase("no")) {
                                    showAlert(jsonObject.getString("message"));
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
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
                VolleyLog.d("error", "Error: " + error.getMessage());
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptsView = layoutInflater.inflate(R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.show();

    }

    AlertDialog dialog;

    private void showBackAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptsView = layoutInflater.inflate(R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog =alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeZipActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void setTotalAmount() {
        DatabaseHandler db = new DatabaseHandler(context);
        List<OrderBean> orderBeanList = db.getAllOrders();
        if (orderBeanList != null) {
            if (orderBeanList.size() > 0) {
                countLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
                int ordersCount = db.getOrdersCount();
                TextView totalCartCountText = (TextView) rootView.findViewById(R.id.text_total_cart_count);
                totalCartCountText.setText(String.valueOf(ordersCount));
                TextView totalPriceText = (TextView) rootView.findViewById(R.id.text_order_price);
                double total = 0;
                for (int i = 0; i < orderBeanList.size(); i++) {
                    OrderBean order = orderBeanList.get(i);
                    total += Double.valueOf(order.getFinalPrice());
                }

                totalPriceText.setText(" $" + String.format("%.2f", total));
                progressBar = (ProgressBar) rootView.findViewById(R.id.firstBar);
                txtProgress = (TextView) rootView.findViewById(R.id.text_progress);
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
                            if (progressBar.getMax() == 0) {
                                progressBar.setMax(1);
                                progressBar.setProgress(1);
                            } else {
                                progressBar.setProgress(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                            }
                            Global.isFreeDelivery = true;
                            txtProgress.setText(R.string.free_delivery);
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
                countLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
            }
        } else {
            countLayout.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    public void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
// lat,lng, your current location
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(Constants.LATITUDE), Double.parseDouble(Constants.LONGITUDE), 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String postalCode1 = addresses.get(0).getPostalCode();
                    Log.d("latitude...", String.valueOf(latitude));
                    Log.d("longitude...", String.valueOf(longitude));
                    Log.d("zipcode...", postalCode1);

                    if (postalCode1 != null) {
                        if (postalCode1.length() == 5) {
                            currentZip = postalCode1;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setTotalAmount();
        int locationCheck = 0;
        if (Global.zip != null) {
            userZipcodeText = (TextView) rootView.findViewById(R.id.text_user_zipcode);
            userZipcodeText.setText(Global.zip);
            userZipcodeText.setTypeface(andBold);
        }
        if (getActivity() != null) {
            locationCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationCheck == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        getNeighbourhoodStore();

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            AppController.getRefWatcher(getActivity()).watch(this);
        }
    }*/
}
