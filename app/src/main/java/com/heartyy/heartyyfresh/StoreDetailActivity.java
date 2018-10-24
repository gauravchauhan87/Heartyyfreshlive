package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.heartyy.heartyyfresh.fragment.*;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public static SimpleAdapter adapter;
    private RelativeLayout fragmentLayout;
    public static boolean isCategory = false;
    private Typeface regular;
    RelativeLayout sortLayout, refineLayout, sortFragment, refineFragment, bannerFragment, showOfferLayout, mainLayout;
    public static boolean isSortFragment = false;
    public static boolean isRefineFragment = false;
    private Button closeBtn;
    public ImageButton plusAisleBtn;
    private boolean animationIsOn = false;
    private String supplierId;
    private SharedPreferences pref;
    private AisleBean aisleBean;
    private RelativeLayout showOfferImage, categoryLayout;
    private TextView txtProgress;
    private RelativeLayout countLayout, bottomLayout;
    private ProgressBar progressBar;
    public static boolean cat = false;
    public static List<SubAisleItemBean> subAisleItemBeanList;
    private CategoryFragment categoryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        Toolbar longToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back);

        String supplierName = getIntent().getExtras().getString("store","");
        String day = getIntent().getExtras().getString("day");
        SpannableString s = new SpannableString(supplierName);

        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        txtProgress = (TextView) mToolbar.findViewById(R.id.text_progress);
        EditText editSearch = (EditText) mToolbar.findViewById(R.id.edit_search);
        progressBar = (ProgressBar) findViewById(R.id.firstBar);
        toolbarTitle.setText(s);

    //    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        Typeface robotoLight = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        Typeface meduimItalic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_MEDIUM_ITALIC);
        Typeface medium = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_MEDIUM);
        Typeface bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        toolbarTitle.setTypeface(regular);
        TextView shopByText = (TextView) findViewById(R.id.text_shop_by);
        supplierId = getIntent().getExtras().getString("id");
        TextView shoppinginText = (TextView) longToolbar.findViewById(R.id.text_shopping_in);
        TextView zipText = (TextView) longToolbar.findViewById(R.id.text_zip);
        TextView earliestDeliveryText = (TextView) longToolbar.findViewById(R.id.text_delivery);
        plusAisleBtn = (ImageButton) findViewById(R.id.button_aisle);
        shoppinginText.setTypeface(robotoLight);
        zipText.setTypeface(regular);
        zipText.setText(Global.zip);
        earliestDeliveryText.setTypeface(regular);
        earliestDeliveryText.setText("Earliest delivery " + day);
        toolbarTitle.setTypeface(bold);

        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        // adapter = new CustomSavedCardAdapter(this);

        fragmentLayout = (RelativeLayout) findViewById(R.id.layout_fragment);
        RelativeLayout shopByLayout = (RelativeLayout) findViewById(R.id.layout_shop_by);
        sortLayout = (RelativeLayout) findViewById(R.id.layout_sort);
        sortFragment = (RelativeLayout) findViewById(R.id.layout_sort_fragment);
        refineLayout = (RelativeLayout) findViewById(R.id.layout_refine);
        refineFragment = (RelativeLayout) findViewById(R.id.layout_refine_fragment);
        bannerFragment = (RelativeLayout) findViewById(R.id.layout_banner_fragment);
        showOfferLayout = (RelativeLayout) findViewById(R.id.layout_show_offer);
        mainLayout = (RelativeLayout) findViewById(R.id.layout_store_main);
        categoryLayout = (RelativeLayout) findViewById(R.id.layout_category);
        closeBtn = (Button) findViewById(R.id.button_close);
        TextView sortText = (TextView) findViewById(R.id.text_sort);
        TextView refineText = (TextView) findViewById(R.id.text_refine);
        showOfferImage = (RelativeLayout) findViewById(R.id.image_show_offer);
        shopByText = (TextView) findViewById(R.id.text_shop_by);
        TextView topCategoryTextView = (TextView) findViewById(R.id.text_topcategory);
        topCategoryTextView.setTypeface(regular);
        countLayout = (RelativeLayout) findViewById(R.id.layout_total_cart_count);
        bottomLayout = (RelativeLayout) findViewById(R.id.layout_bottom);
        ImageButton bagButton = (ImageButton) findViewById(R.id.image_bag);

        shopByText.setTypeface(robotoLight);
        sortText.setTypeface(robotoLight);
        refineText.setTypeface(robotoLight);

        requestForAisleAndSupplierStore();

        setAmountTotal();

        zipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreDetailActivity.this, ChangeZipActivity.class);
                startActivity(intent);
                finish();
            }
        });


        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSortFragment) {
                    sortFragment.setVisibility(View.VISIBLE);
                    isSortFragment = true;
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    refineFragment.setVisibility(View.INVISIBLE);
                    isRefineFragment = false;
                    SortFragment categoryFragment = new SortFragment(StoreDetailActivity.this,StoreDetailActivity.this);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.sort_fragment, categoryFragment).commit();
                } else {
                    sortFragment.setVisibility(View.GONE);
                    isSortFragment = false;
                }
            }
        });
        refineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRefineFragment) {
                    refineFragment.setVisibility(View.VISIBLE);
                    isRefineFragment = true;
                    sortFragment.setVisibility(View.GONE);
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    isSortFragment = false;
                    RefineFragment categoryFragment = new RefineFragment(StoreDetailActivity.this,StoreDetailActivity.this);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.refine_fragment, categoryFragment).commit();

                } else {
                    refineFragment.setVisibility(View.INVISIBLE);
                    isRefineFragment = false;
                }
            }
        });


        /*shopByLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCategory) {
                    Animation animHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_down);
                    Animation alphaAnimshow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.alpha_show);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    recyclerView.startAnimation(animHide);
                    recyclerView.setVisibility(View.GONE);
                    if (cat) {
                        categoryLayout.startAnimation(alphaAnimHide);
                    }
                    categoryLayout.setVisibility(View.GONE);
                    fragmentLayout.setVisibility(View.VISIBLE);
                    fragmentLayout.startAnimation(alphaAnimshow);
                    isCategory = true;
                } else {
                    Animation animShow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_up);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    isCategory = false;
                }
            }
        });*/
        shopByLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Animation alphaAnimHide;
                if (isCategory) {
                    Animation animShow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_up);
                    alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    plusAisleBtn.setImageResource(R.drawable.plus_icon);
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    isCategory = false;
                    return;
                }
                plusAisleBtn.setImageResource(R.drawable.minus_icon);
                Animation animHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_down);
                Animation alphaAnimshow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.alpha_show);
                alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                recyclerView.startAnimation(animHide);
                recyclerView.setVisibility(View.GONE);
                if (cat) {
                    categoryLayout.startAnimation(alphaAnimHide);
                }
                categoryLayout.setVisibility(View.GONE);
                fragmentLayout.setVisibility(View.VISIBLE);
                fragmentLayout.startAnimation(alphaAnimshow);
                isCategory = true;
            }
        });


        plusAisleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCategory) {
                    plusAisleBtn.setImageResource(R.drawable.minus_icon);
                    Animation animHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_down);
                    Animation alphaAnimshow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.alpha_show);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    recyclerView.startAnimation(animHide);
                    recyclerView.setVisibility(View.GONE);
                    if (cat) {
                        categoryLayout.startAnimation(alphaAnimHide);
                    }
                    categoryLayout.setVisibility(View.GONE);
                    fragmentLayout.setVisibility(View.VISIBLE);
                    fragmentLayout.startAnimation(alphaAnimshow);
                    isCategory = true;
                } else {
                    Animation animShow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_up);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    plusAisleBtn.setImageResource(R.drawable.plus_icon);
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    isCategory = false;
                }
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!animationIsOn) {
                    int height = bannerFragment.getMeasuredHeight();
                    int h = showOfferLayout.getMeasuredHeight();
                    // height = height-h;
                    Animation animHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    bannerFragment.startAnimation(animHide);
                    bannerFragment.setVisibility(View.INVISIBLE);
                    showOfferLayout.setVisibility(View.VISIBLE);
                    Animation animshow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.alpha_show);
                    showOfferLayout.startAnimation(animshow);

                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -height);
                    anim.setDuration(2000);

                    final int finalHeight = height;
                    anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {

                            mainLayout.setDrawingCacheEnabled(true);
                            animationIsOn = true;
                            closeBtn.setEnabled(false);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            mainLayout.setDrawingCacheEnabled(false);
                            mainLayout.clearAnimation();
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
                            params.topMargin -= finalHeight;
                            mainLayout.setLayoutParams(params);
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            animation.setDuration(1);
                            mainLayout.startAnimation(animation);
                            bannerFragment.setVisibility(View.INVISIBLE);
                            animationIsOn = false;
                            showOfferImage.setEnabled(true);

                        }
                    });
                    mainLayout.startAnimation(anim);
                }
            }
        });

        showOfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!animationIsOn) {
                    int height = bannerFragment.getMeasuredHeight();
                    int h = showOfferLayout.getMeasuredHeight();
                    //height = height-h;
                    showOfferLayout.setVisibility(View.INVISIBLE);
                    Animation animhide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    showOfferLayout.startAnimation(animhide);
                    Animation animshow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.alpha_show);
                    bannerFragment.startAnimation(animshow);
                    bannerFragment.setVisibility(View.VISIBLE);


                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, height);
                    anim.setDuration(2000);

                    final int finalHeight = height;
                    anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            mainLayout.setDrawingCacheEnabled(true);
                            animationIsOn = true;
                            showOfferImage.setEnabled(false);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mainLayout.setDrawingCacheEnabled(false);
                            mainLayout.clearAnimation();
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
                            params.topMargin += finalHeight;
                            mainLayout.setLayoutParams(params);
                            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            animation.setDuration(1);
                            mainLayout.startAnimation(animation);
                            bannerFragment.setVisibility(View.VISIBLE);
                            animationIsOn = false;
                            closeBtn.setEnabled(true);


                        }
                    });
                    mainLayout.startAnimation(anim);
                }

            }
        });

        bagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = pref.getString(Constants.USER_ID, null);
                if (userid == null) {
                    //showAlert("Please login to place your order");
                    Intent intent = new Intent(StoreDetailActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(StoreDetailActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void requestForAisleAndSupplierStore() {
        Global.showProgress(this);
        String url = "store/aisle?supplier_id=" + supplierId + "&zipcode=" + Global.zip + "&user_id=" + pref.getString(Constants.USER_ID, null);
        Log.d("storeDetailURL",Constants.URL+url);
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
                                aisleBean = ConversionHelper.convertAisleJsonToTopAisleBeanList(jsonObject);
                                Global.popularAisleBean = aisleBean;
                                if (aisleBean.getTopAisleBeanList() != null) {
                                    categoryFragment = new CategoryFragment(StoreDetailActivity.this, aisleBean.getTopAisleBeanList(), aisleBean.getPopularItemList(),StoreDetailActivity.this);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.category_fragment, categoryFragment).commit();
                                }
                                List<SectionedGridRecyclerViewAdapter.Section> sections =
                                        new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                                subAisleItemBeanList = new ArrayList<>();
                                if (aisleBean.getPopularItemList() != null) {
                                    if (aisleBean.getPopularItemList().size() > 0) {
                                        for (int c = 0; c < aisleBean.getPopularItemList().size(); c++) {
                                            subAisleItemBeanList.add(aisleBean.getPopularItemList().get(c));
                                        }
                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", "-1", aisleBean.getPopularMore(), null));
                                    }
                                }

                                if (aisleBean.getPreviousOrderBean() != null) {
                                    if (aisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                                        if (aisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                            int count = 0;
                                            for (int c = 0; c < aisleBean.getPreviousOrderBean().getPreviousItemList().size(); c++) {
                                                subAisleItemBeanList.add(aisleBean.getPreviousOrderBean().getPreviousItemList().get(c));
                                            }
                                            if (aisleBean.getPopularItemList() != null) {
                                                if (aisleBean.getPopularItemList().size() > 0) {
                                                    count = aisleBean.getPopularItemList().size();
                                                }
                                            }
                                            sections.add(new SectionedGridRecyclerViewAdapter.Section(count, "From your Previous Orders", "-1", "no", null));
                                        }
                                    }
                                }

                                for (int j = 0; j < aisleBean.getTopAisleBeanList().size(); j++) {
                                    List<SubAisleItemBean> topCategoryPopularBeanList = aisleBean.getTopAisleBeanList().get(j).getTopCategoryPopularBeanList();
                                    if (topCategoryPopularBeanList != null) {
                                        for (int c = 0; c < topCategoryPopularBeanList.size(); c++) {
                                            subAisleItemBeanList.add(topCategoryPopularBeanList.get(c));
                                        }
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                                        for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                            SubAisleItemBean item = subAisleItemBeanList.get(c);
                                            if (item.getIsSave().equalsIgnoreCase("YES")) {
                                                String str = db.getLikeItem(item.getSupplierItemId());
                                                if (str == null) {
                                                    db.addLikeItem(item.getSupplierItemId());
                                                }
                                            }
                                        }
                                    }
                                });

                                adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);


                                int j = 0;
                                int count = 0;

                                if (aisleBean.getPopularItemList() != null) {
                                    if (aisleBean.getPopularItemList().size() > 0) {
                                        j = 1;
                                        count += aisleBean.getPopularItemList().size();
                                        j = count;
                                    } else {
                                        j = 0;
                                        count = 0;
                                    }
                                }

                                if (aisleBean.getPreviousOrderBean() != null) {
                                    if (aisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                                        if (aisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                            count += aisleBean.getPreviousOrderBean().getPreviousItemList().size();
                                            j = count;
                                        }
                                    }
                                }

                                for (int i = 0; i < aisleBean.getTopAisleBeanList().size(); i++) {
                                    List<SubAisleItemBean> itemsList = aisleBean.getTopAisleBeanList().get(i).getTopCategoryPopularBeanList();
                                    if (itemsList != null) {
                                        count += itemsList.size();

                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(j, aisleBean.getTopAisleBeanList().get(i).getTopCategoryName(), "-1", aisleBean.getTopAisleBeanList().get(i).getIsMore(), aisleBean.getTopAisleBeanList().get(i).getTopCategoryId()));
                                        j = count;
                                    }
                                }


                                //Add your adapter to the sectionAdapter
                                SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                                SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                        SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                                mSectionedAdapter.setSections(sections.toArray(dummy));

                                //Apply this adapter to the RecyclerView
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(mSectionedAdapter);

                                TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                                topCategoryText.setText(aisleBean.getTopAisleBeanList().get(0).getTopCategoryName());


                                if (aisleBean.getPromotionBeanList() != null) {
                                    bannerFragment.setVisibility(View.VISIBLE);
                                    BannerPagerFragment pagerFragment = new BannerPagerFragment(StoreDetailActivity.this, aisleBean.getPromotionBeanList());
                                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                                    fragmentManager1.beginTransaction().replace(R.id.banner_fragment, pagerFragment).commit();
                                } else {
                                    bannerFragment.setVisibility(View.GONE);
                                }

                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Constants.SUB_CATEGORY_ID, null);
                                editor.putString(Constants.TOP_CATEGORY_ID, null);
                                editor.putString(Constants.SORT, null);
                                editor.putString(Constants.REFINE, null);
                                editor.apply();


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();

                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
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


    private void setupActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.store_toolbar, null);
        ab.setCustomView(v);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refineFragment.setVisibility(View.INVISIBLE);
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(StoreDetailActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                StoreDetailActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(regular);
        okBtn.setTypeface(regular);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing())
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
           /* if(!Global.isSearchView) {
                longToolbar.setVisibility(View.GONE);
                toolbarTitle.setVisibility(View.GONE);
                editSearch.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                shopByLayout.setVisibility(View.GONE);
                categoryLayout.setVisibility(View.GONE);
               *//* bannerFragment.setVisibility(View.INVISIBLE);
                showOfferLayout.setVisibility(View.INVISIBLE);*//*
                RelativeLayout searchLayout = (RelativeLayout) findViewById(R.id.layout_search_fragment);
                searchLayout.setVisibility(View.VISIBLE);
                SearchFragment searchFragment = new SearchFragment(StoreDetailActivity.this);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.search_fragment, searchFragment).commit();
                item.setIcon(R.drawable.close_refine);
                Global.isSearchView = true;
                return true;
            }else{
                longToolbar.setVisibility(View.VISIBLE);
                toolbarTitle.setVisibility(View.VISIBLE);
                editSearch.setVisibility(View.GONE);
                RelativeLayout searchLayout = (RelativeLayout) findViewById(R.id.layout_search_fragment);
                searchLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                shopByLayout.setVisibility(View.VISIBLE);
                categoryLayout.setVisibility(View.GONE);
               *//* bannerFragment.setVisibility(View.VISIBLE);
                showOfferLayout.setVisibility(View.VISIBLE);*//*
                item.setIcon(R.drawable.search_icon);
                Global.isSearchView = false;
                return true;
            }*/
            Intent intent = new Intent(StoreDetailActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == android.R.id.home) {

            try {
                if (Global.backCount == 1) {
                    categoryLayout.setVisibility(View.GONE);
                    ImageView sortImg = (ImageView) findViewById(R.id.img_sort_indication);
                    ImageView refineImg = (ImageView) findViewById(R.id.img_refine_indication);
                    sortImg.setImageResource(R.drawable.indication_dark_circle);
                    refineImg.setImageResource(R.drawable.indication_dark_circle);

                    Global.refineBrandBeanList = new ArrayList<>();
                    Global.refinePriceBeanList = new ArrayList<>();
                    Global.sort = null;

                    if (Global.popularAisleBean.getTopAisleBeanList() != null) {
                        CategoryFragment categoryFragment = new CategoryFragment(StoreDetailActivity.this, Global.popularAisleBean.getTopAisleBeanList(), Global.popularAisleBean.getPopularItemList(),StoreDetailActivity.this);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.category_fragment, categoryFragment).commit();
                    }
                    List<SectionedGridRecyclerViewAdapter.Section> sections =
                            new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                    List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                    if (Global.popularAisleBean.getPopularItemList() != null) {
                        if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                            for (int c = 0; c < Global.popularAisleBean.getPopularItemList().size(); c++) {
                                subAisleItemBeanList.add(Global.popularAisleBean.getPopularItemList().get(c));
                            }
                            sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", "-1", Global.popularAisleBean.getPopularMore(), null));
                        }
                    }

                    if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                        if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                            if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                for (int c = 0; c < Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size(); c++) {
                                    subAisleItemBeanList.add(Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().get(c));
                                }

                            }
                        }
                    }

                    if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                        if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                            if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                int count = 0;
                                if (Global.popularAisleBean.getPopularItemList() != null) {
                                    if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                                        count = Global.popularAisleBean.getPopularItemList().size();
                                    }
                                }
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(count, "From your Previous Orders", "-1", "no", null));
                            }
                        }
                    }

                    for (int j = 0; j < Global.popularAisleBean.getTopAisleBeanList().size(); j++) {
                        List<SubAisleItemBean> topCategoryPopularBeanList = Global.popularAisleBean.getTopAisleBeanList().get(j).getTopCategoryPopularBeanList();
                        if (topCategoryPopularBeanList != null) {
                            for (int c = 0; c < topCategoryPopularBeanList.size(); c++) {
                                subAisleItemBeanList.add(topCategoryPopularBeanList.get(c));
                            }
                        }
                    }


                    DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                    for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                        SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                        if (item1.getIsSave().equalsIgnoreCase("YES")) {
                            String str = db.getLikeItem(item1.getSupplierItemId());
                            if (str == null) {
                                db.addLikeItem(item1.getSupplierItemId());
                            }
                        }
                    }

                    adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);


                    int j = 0;
                    int count = 0;

                    if (Global.popularAisleBean.getPopularItemList() != null) {
                        if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                            j = 1;
                            count += Global.popularAisleBean.getPopularItemList().size();
                            j = count;
                        } else {
                            j = 0;
                            count = 0;
                        }
                    }

                    if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                        if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                            if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                count += Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size();
                                j = count;
                            }
                        }
                    }


                    for (int i = 0; i < Global.popularAisleBean.getTopAisleBeanList().size(); i++) {
                        List<SubAisleItemBean> itemsList = Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryPopularBeanList();
                        if (itemsList != null) {
                            count += itemsList.size();

                            sections.add(new SectionedGridRecyclerViewAdapter.Section(j, Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryName(), "-1", Global.popularAisleBean.getTopAisleBeanList().get(i).getIsMore(), Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryId()));
                            j = count;
                        }
                    }


                    //Add your adapter to the sectionAdapter
                    SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                    SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                            SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                    mSectionedAdapter.setSections(sections.toArray(dummy));

                    //Apply this adapter to the RecyclerView
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mSectionedAdapter);

                    TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                    topCategoryText.setText(Global.popularAisleBean.getTopAisleBeanList().get(0).getTopCategoryName());
                    Global.backCount = 0;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.SUB_CATEGORY_ID, null);
                    editor.putString(Constants.TOP_CATEGORY_ID, null);
                    editor.putString(Constants.SORT, null);
                    editor.putString(Constants.REFINE, null);
                    editor.apply();
                    cat = false;
               /* Intent intent = new Intent(StoreDetailActivity.this,StoreDetailActivity.class);
                startActivity(intent);
                finish();*/
                    return true;
                } else if (Global.backCount == 2) {

                    if (Global.backFrom.equalsIgnoreCase("top")) {
                        String sort = pref.getString(Constants.SORT, null);
                        String refine = pref.getString(Constants.REFINE, null);

                        if (sort != null) {

                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                            SubAisleBean subAisleBean1 = null;

                            List<SubAisleBean> subAisleBeanList = Global.sortedTopAisleBean.getTopAisleBean().getSubAisleBeanList();
                            for (int j = 0; j < subAisleBeanList.size(); j++) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                            }

                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }

                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                            RecyclerView recyclerView;
                            RecyclerView.LayoutManager mLayoutManager;
                            recyclerView = (RecyclerView) findViewById(R.id.cardList);
                            recyclerView.setHasFixedSize(true);
                            mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);

                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                            int j = 0;
                            int count = 0;
                            for (int i = 0; i < subAisleBeanList.size(); i++) {
                                List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                count += itemsList.size();
                                subAisleBean1 = subAisleBeanList.get(i);
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(), null));
                                j = count;
                            }

                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(Global.sortedTopAisleBean.getTopAisleBean().getTopCategoryName());
                            Global.backCount = 1;

                        } else if (refine != null) {
                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();

                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

                            if (Global.refineTopAisleBean.getPopularAisleItemBeanList() != null) {
                                if (Global.refineTopAisleBean.getPopularAisleItemBeanList().size() > 0) {

                                    for (int c = 0; c < Global.refineTopAisleBean.getPopularAisleItemBeanList().size(); c++) {
                                        subAisleItemBeanList.add(Global.refineTopAisleBean.getPopularAisleItemBeanList().get(c));
                                    }

                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", Global.refineTopAisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no", null));
                                }
                            }
                            SubAisleBean subAisleBean1 = null;

                            List<SubAisleBean> subAisleBeanList = Global.refineTopAisleBean.getTopAisleBean().getSubAisleBeanList();
                            for (int j = 0; j < subAisleBeanList.size(); j++) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                            }

                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }


                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                            RecyclerView recyclerView;
                            RecyclerView.LayoutManager mLayoutManager;
                            recyclerView = (RecyclerView) findViewById(R.id.cardList);
                            recyclerView.setHasFixedSize(true);
                            mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);

                            int j = 0;
                            int count = 0;

                            if (Global.refineTopAisleBean.getPopularAisleItemBeanList() != null) {
                                if (Global.refineTopAisleBean.getPopularAisleItemBeanList().size() > 0) {
                                    j = 1;
                                    count += Global.refineTopAisleBean.getPopularAisleItemBeanList().size();
                                    j = count;
                                } else {
                                    j = 0;
                                    count = 0;
                                }
                            }


                            for (int i = 0; i < subAisleBeanList.size(); i++) {
                                List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                count += itemsList.size();
                                subAisleBean1 = subAisleBeanList.get(i);
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(), null));
                                j = count;
                            }


                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(Global.refineTopAisleBean.getTopAisleBean().getTopCategoryName());
                            Global.backCount = 1;
                            if (StoreDetailActivity.isRefineFragment) {
                                findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                StoreDetailActivity.isRefineFragment = false;

                            }
                            Global.refineBrandBeanList = new ArrayList<>();
                            Global.refinePriceBeanList = new ArrayList<>();
                        } else {

                            ImageView sortImg = (ImageView) findViewById(R.id.img_sort_indication);
                            ImageView refineImg = (ImageView) findViewById(R.id.img_refine_indication);
                            sortImg.setImageResource(R.drawable.indication_dark_circle);
                            refineImg.setImageResource(R.drawable.indication_dark_circle);

                            Global.refineBrandBeanList = new ArrayList<>();
                            Global.refinePriceBeanList = new ArrayList<>();
                            Global.sort = null;

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.REFINE,null);
                            editor.putString(Constants.SORT,null);
                            editor.apply();

                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();

                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();


                            if (Global.topAisleBean.getPopularAisleItemBeanList() != null) {
                                if (Global.topAisleBean.getPopularAisleItemBeanList().size() > 0) {

                                    for (int c = 0; c < Global.topAisleBean.getPopularAisleItemBeanList().size(); c++) {
                                        subAisleItemBeanList.add(Global.topAisleBean.getPopularAisleItemBeanList().get(c));
                                    }

                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", Global.topAisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no", pref.getString(Constants.TOP_CATEGORY_ID, null)));
                                }
                            }
                            SubAisleBean subAisleBean1 = null;

                            List<SubAisleBean> subAisleBeanList = Global.topAisleBean.getTopAisleBean().getSubAisleBeanList();
                            for (int j = 0; j < subAisleBeanList.size(); j++) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList1.get(k).setSubCategoryId(subAisleBeanList.get(j).getSubCategoryId());
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                            }

                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }

                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                            RecyclerView recyclerView;
                            RecyclerView.LayoutManager mLayoutManager;
                            recyclerView = (RecyclerView) findViewById(R.id.cardList);
                            recyclerView.setHasFixedSize(true);
                            mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);


                            int j = 0;
                            int count = 0;
                            if (Global.topAisleBean.getPopularAisleItemBeanList() != null) {
                                if (Global.topAisleBean.getPopularAisleItemBeanList().size() > 0) {
                                    j = 1;
                                    count += Global.topAisleBean.getPopularAisleItemBeanList().size();
                                    j = count;
                                } else {
                                    j = 0;
                                    count = 0;
                                }
                            }
                            for (int i = 0; i < subAisleBeanList.size(); i++) {
                                List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                count += itemsList.size();
                                subAisleBean1 = subAisleBeanList.get(i);
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(), null));
                                j = count;
                            }


                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(Global.topAisleBean.getTopAisleBean().getTopCategoryName());
                            SharedPreferences.Editor editor1 = pref.edit();
                            editor1.putString(Constants.CATEGORY, "top");
                            editor1.apply();
                            Global.backCount = 1;
                            Global.backFrom = "";
                            requestForBrandandSizeForTopCategory(supplierId, pref.getString(Constants.TOP_CATEGORY_ID, null));
                        }

                    } else if (Global.backFrom.equalsIgnoreCase("aisle")) {

                        String sort = pref.getString(Constants.SORT, null);
                        String refine = pref.getString(Constants.REFINE, null);
                        if (sort != null) {
                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                            SubAisleBean subAisleBean1 = null;

                            List<SubAisleBean> subAisleBeanList = Global.sortedSubAisleBean.getTopAisleBean().getSubAisleBeanList();
                            for (int j = 0; j < subAisleBeanList.size(); j++) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                            }

                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }


                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                            RecyclerView recyclerView;
                            RecyclerView.LayoutManager mLayoutManager;
                            recyclerView = (RecyclerView) findViewById(R.id.cardList);
                            recyclerView.setHasFixedSize(true);
                            mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);

                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                            int j = 0;
                            int count = 0;
                            for (int i = 0; i < subAisleBeanList.size(); i++) {
                                List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                count += itemsList.size();
                                subAisleBean1 = subAisleBeanList.get(i);
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), "no", null));
                                j = count;
                            }


                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(subAisleBean1.getSubCategoryName());
                            Global.backCount = 1;
                        } else if (refine != null) {
                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                            SubAisleBean subAisleBean1 = null;

                            List<SubAisleBean> subAisleBeanList = Global.refineSubAisleBean.getTopAisleBean().getSubAisleBeanList();
                            for (int j = 0; j < subAisleBeanList.size(); j++) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                            }


                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }

                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                            RecyclerView recyclerView;
                            RecyclerView.LayoutManager mLayoutManager;
                            recyclerView = (RecyclerView) findViewById(R.id.cardList);
                            recyclerView.setHasFixedSize(true);
                            mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);

                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                            int j = 0;
                            int count = 0;
                            for (int i = 0; i < subAisleBeanList.size(); i++) {
                                List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                count += itemsList.size();
                                subAisleBean1 = subAisleBeanList.get(i);
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), "no", null));
                                j = count;
                            }


                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(Global.refineSubAisleBean.getTopAisleBean().getTopCategoryName());
                            if (StoreDetailActivity.isRefineFragment) {
                                findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                StoreDetailActivity.isRefineFragment = false;

                            }

                            Global.refineBrandBeanList = new ArrayList<>();
                            Global.refinePriceBeanList = new ArrayList<>();
                            Global.backCount = 1;
                        } else {

                            ImageView sortImg = (ImageView) findViewById(R.id.img_sort_indication);
                            ImageView refineImg = (ImageView) findViewById(R.id.img_refine_indication);
                            sortImg.setImageResource(R.drawable.indication_dark_circle);
                            refineImg.setImageResource(R.drawable.indication_dark_circle);

                            Global.refineBrandBeanList = new ArrayList<>();
                            Global.refinePriceBeanList = new ArrayList<>();
                            Global.sort = null;

                            SharedPreferences.Editor editor0 = pref.edit();
                            editor0.putString(Constants.REFINE,null);
                            editor0.putString(Constants.SORT,null);
                            editor0.apply();

                            categoryLayout.setVisibility(View.GONE);
                            if (Global.popularAisleBean.getTopAisleBeanList() != null) {
                                CategoryFragment categoryFragment = new CategoryFragment(StoreDetailActivity.this, aisleBean.getTopAisleBeanList(), aisleBean.getPopularItemList(),StoreDetailActivity.this);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.category_fragment, categoryFragment).commit();
                            }
                            List<SectionedGridRecyclerViewAdapter.Section> sections =
                                    new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                            List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                            if (Global.popularAisleBean.getPopularItemList() != null) {
                                if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                                    for (int c = 0; c < Global.popularAisleBean.getPopularItemList().size(); c++) {
                                        subAisleItemBeanList.add(Global.popularAisleBean.getPopularItemList().get(c));
                                    }
                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", "-1", Global.popularAisleBean.getPopularMore(), null));
                                }
                            }

                            if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                                if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                                    if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                        for (int c = 0; c < Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size(); c++) {
                                            subAisleItemBeanList.add(Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().get(c));
                                        }
                                    }
                                }
                            }

                            if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                                if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                                    if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                        int count = 0;
                                        if (Global.popularAisleBean.getPopularItemList() != null) {
                                            if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                                                count = Global.popularAisleBean.getPopularItemList().size();
                                            }
                                        }
                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(count, "From your Previous Orders", "-1", "no", null));
                                    }
                                }
                            }

                            for (int j = 0; j < Global.popularAisleBean.getTopAisleBeanList().size(); j++) {
                                List<SubAisleItemBean> topCategoryPopularBeanList = Global.popularAisleBean.getTopAisleBeanList().get(j).getTopCategoryPopularBeanList();
                                if (topCategoryPopularBeanList != null) {
                                    for (int c = 0; c < topCategoryPopularBeanList.size(); c++) {
                                        subAisleItemBeanList.add(topCategoryPopularBeanList.get(c));
                                    }
                                }
                            }

                            DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                            for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                                if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                    String str = db.getLikeItem(item1.getSupplierItemId());
                                    if (str == null) {
                                        db.addLikeItem(item1.getSupplierItemId());
                                    }
                                }
                            }

                            adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);


                            int j = 0;
                            int count = 0;

                            if (Global.popularAisleBean.getPopularItemList() != null) {
                                if (Global.popularAisleBean.getPopularItemList().size() > 0) {
                                    j = 1;
                                    count += Global.popularAisleBean.getPopularItemList().size();
                                    j = count;
                                } else {
                                    j = 0;
                                    count = 0;
                                }
                            }

                            if (Global.popularAisleBean.getPreviousOrderBean() != null) {
                                if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList() != null) {
                                    if (Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size() > 0) {
                                        count += Global.popularAisleBean.getPreviousOrderBean().getPreviousItemList().size();
                                        j = count;
                                    }
                                }
                            }

                            for (int i = 0; i < Global.popularAisleBean.getTopAisleBeanList().size(); i++) {
                                List<SubAisleItemBean> itemsList = Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryPopularBeanList();
                                if (itemsList != null) {
                                    count += itemsList.size();

                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(j, Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryName(), "-1", Global.popularAisleBean.getTopAisleBeanList().get(i).getIsMore(), Global.popularAisleBean.getTopAisleBeanList().get(i).getTopCategoryId()));
                                    j = count;
                                }
                            }


                            //Add your adapter to the sectionAdapter
                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                    SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                            mSectionedAdapter.setSections(sections.toArray(dummy));

                            //Apply this adapter to the RecyclerView
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(mSectionedAdapter);

                            TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                            topCategoryText.setText(Global.popularAisleBean.getTopAisleBeanList().get(0).getTopCategoryName());
                            Global.backCount = 0;
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.CATEGORY, "sub");
                            editor.apply();

                            SharedPreferences.Editor editor1 = pref.edit();
                            editor1.putString(Constants.SUB_CATEGORY_ID, null);
                            editor1.putString(Constants.TOP_CATEGORY_ID, null);
                            editor.putString(Constants.SORT, null);
                            editor.putString(Constants.REFINE, null);
                            editor1.apply();
                        }

                    } else if (Global.backFrom.equalsIgnoreCase("sub")) {

                        ImageView sortImg = (ImageView) findViewById(R.id.img_sort_indication);
                        ImageView refineImg = (ImageView) findViewById(R.id.img_refine_indication);
                        sortImg.setImageResource(R.drawable.indication_dark_circle);
                        refineImg.setImageResource(R.drawable.indication_dark_circle);

                        Global.refineBrandBeanList = new ArrayList<>();
                        Global.refinePriceBeanList = new ArrayList<>();
                        Global.sort = null;

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.REFINE,null);
                        editor.putString(Constants.SORT,null);
                        editor.apply();



                        List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();

                        List<SectionedGridRecyclerViewAdapter.Section> sections =
                                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                        int j = 0;
                        if (Global.subAisleBean.getPopularAisleItemBeanList() != null) {
                            if (Global.subAisleBean.getPopularAisleItemBeanList().size() > 0) {
                                for (int c = 0; c < Global.subAisleBean.getPopularAisleItemBeanList().size(); c++) {
                                    subAisleItemBeanList.add(Global.subAisleBean.getPopularAisleItemBeanList().get(c));
                                }
                                sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Popular", Global.subAisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), Global.subAisleBean.getPopularIsMore(), pref.getString(Constants.TOP_CATEGORY_ID, null)));
                            }
                        }

                        SubAisleBean subAisleBean1 = null;
                        for (int i = 0; i < Global.subAisleBean.getTopAisleBean().getSubAisleBeanList().size(); i++) {
                            if (Global.subAisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId().equalsIgnoreCase(pref.getString(Constants.SUB_CATEGORY_ID, null))) {
                                List<SubAisleItemBean> subAisleItemBeanList1 = Global.subAisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubAisleItemBeanList();
                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                    subAisleItemBeanList1.get(k).setSubCategoryId(Global.subAisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId());
                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                }
                                subAisleBean1 = Global.subAisleBean.getTopAisleBean().getSubAisleBeanList().get(i);
                            }
                        }

                        DatabaseHandler db = new DatabaseHandler(StoreDetailActivity.this);
                        for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                            SubAisleItemBean item1 = subAisleItemBeanList.get(c);
                            if (item1.getIsSave().equalsIgnoreCase("YES")) {
                                String str = db.getLikeItem(item1.getSupplierItemId());
                                if (str == null) {
                                    db.addLikeItem(item1.getSupplierItemId());
                                }
                            }
                        }

                        adapter = new SimpleAdapter(StoreDetailActivity.this, subAisleItemBeanList, StoreDetailActivity.this);
                        RecyclerView recyclerView;
                        RecyclerView.LayoutManager mLayoutManager;
                        recyclerView = (RecyclerView) findViewById(R.id.cardList);
                        recyclerView.setHasFixedSize(true);
                        mLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 3);


                        int count = 0;
                        if (Global.subAisleBean.getPopularAisleItemBeanList() != null) {
                            if (Global.subAisleBean.getPopularAisleItemBeanList().size() > 0) {
                                count += Global.subAisleBean.getPopularAisleItemBeanList().size();
                                j = count;
                            } else {
                                j = 0;
                                count = 0;
                            }
                        }

                        sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(), null));


                        //Add your adapter to the sectionAdapter
                        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                SectionedGridRecyclerViewAdapter(StoreDetailActivity.this, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, StoreDetailActivity.this);
                        mSectionedAdapter.setSections(sections.toArray(dummy));

                        //Apply this adapter to the RecyclerView
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(mSectionedAdapter);

                        TextView topCategoryText = (TextView) findViewById(R.id.text_topcategory);
                        topCategoryText.setText(subAisleBean1.getSubCategoryName());

                        requestForBrandandSizeForSubCategory(supplierId, pref.getString(Constants.TOP_CATEGORY_ID, null), pref.getString(Constants.SUB_CATEGORY_ID, null));
                       // Global.backCount = 1;
                        if(Global.topAisleBean==null){
                            Global.backFrom="aisle";
                        }else{
                            Global.backFrom="top";
                        }
                        Global.backCount=2;
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.putString(Constants.CATEGORY, "sub");
                        editor2.apply();
                    }
                    return true;
                }else{
                    return super.onOptionsItemSelected(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void requestForBrandandSizeForTopCategory(String supplierId, String topCategoryId) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&for=topcategory";
        RequestQueue rq = Volley.newRequestQueue(StoreDetailActivity.this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                       Global.hideProgress();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                ImageView sortImage = (ImageView) findViewById(R.id.image_sort_icon);
                                sortImage.setImageResource(R.drawable.sort_icon);
                                ImageView refineImage = (ImageView) findViewById(R.id.image_refine_icon);
                                refineImage.setImageResource(R.drawable.refine_icon);

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

    private void requestForBrandandSizeForSubCategory(String supplierId, String topCategoryId, String subCategoryid) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&sub_category_id=" + subCategoryid + "&for=subcategory";
        RequestQueue rq = Volley.newRequestQueue(StoreDetailActivity.this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                       Global.hideProgress();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                ImageView sortImage = (ImageView) findViewById(R.id.image_sort_icon);
                                sortImage.setImageResource(R.drawable.sort_icon);
                                ImageView refineImage = (ImageView) findViewById(R.id.image_refine_icon);
                                refineImage.setImageResource(R.drawable.refine_icon);

                            }
                        } catch (JSONException e) {
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

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    public void setAmountTotal() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<OrderBean> orderBeanList = db.getAllOrders();
        if (orderBeanList != null) {
            if (orderBeanList.size() > 0) {
                countLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
                int ordersCount = db.getOrdersCount();
                TextView totalCartCountText = (TextView) findViewById(R.id.text_total_cart_count);
                totalCartCountText.setText(String.valueOf(ordersCount));
                TextView totalPriceText = (TextView) findViewById(R.id.text_order_price);
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

                if(Global.promotionAvailableBean!=null) {
                    if (Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")) {
                        progressBar.setVisibility(View.VISIBLE);
                        txtProgress.setVisibility(View.VISIBLE);
                        progressBar.setMax(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                        double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());

                        double addMore=0;
                        if(total>minamount){
                            addMore = total - minamount;
                        }else{
                            addMore = minamount - total;
                        }

                        if(total>minamount) {
                            Global.isFreeDelivery = true;
                            if(progressBar.getMax()==0){
                                progressBar.setMax(1);
                                progressBar.setProgress(1);
                            }else {
                                progressBar.setProgress(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                            }
                            txtProgress.setText("You got free delivery");
                        }else{
                            String text = String.format("%.2f", addMore);
                            progressBar.setProgress(temp);
                            txtProgress.setText("Add $" + text + " for free delivery");
                            Global.isFreeDelivery = false;
                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                        txtProgress.setVisibility(View.GONE);
                    }
                }else{
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("new call", "intent");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (Global.isSearchData) {
            recyclerView.setVisibility(View.GONE);
            categoryLayout.setVisibility(View.GONE);
            fragmentLayout.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    categoryFragment.requestForAisleAndSupplierStore(Global.subCategoryId, Global.topCategoryId);
                    Global.isSearchData = false;
                    Animation animShow = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.profile_slide_up);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(StoreDetailActivity.this, R.anim.aplha_hide);
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    isCategory = false;
                }
            }, 2000);

        }
        setAmountTotal();
    }

    public void refreshData() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}