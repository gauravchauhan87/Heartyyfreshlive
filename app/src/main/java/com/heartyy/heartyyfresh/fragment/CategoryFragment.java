package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.SignUpActivity;
import com.heartyy.heartyyfresh.StoreDetailActivity;
import com.heartyy.heartyyfresh.adapter.ExpandableListAdapter;
import com.heartyy.heartyyfresh.adapter.SectionedGridRecyclerViewAdapter;
import com.heartyy.heartyyfresh.adapter.SimpleAdapter;
import com.heartyy.heartyyfresh.bean.CategoryAisleBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.RefineBrandBean;
import com.heartyy.heartyyfresh.bean.RefinePriceBean;
import com.heartyy.heartyyfresh.bean.SubAisleBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
import com.heartyy.heartyyfresh.bean.TopAisleBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dheeraj on 12/18/2015.
 */
@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment {

    private View rootView;
    private Context context;
    Typeface andBold, bold, italic, light;
    private ExpandableListView categoryListView;
    private ExpandableListAdapter adpt;
    List<TopAisleBean> listDataHeader;
    HashMap<String, List<SubAisleBean>> listDataChild;
    private List<TopAisleBean> topAisleBeanList;
    private SharedPreferences pref;
    private CategoryAisleBean aisleBean;
    private SimpleAdapter adapter;
    private List<SubAisleItemBean> popularItemList;
    StoreDetailActivity activity;
    RelativeLayout countLayout, bottomLayout;
    private String oldSubCategoryId, oldTopCategoryId;


    public CategoryFragment(Context context, List<TopAisleBean> topAisleBeanList, List<SubAisleItemBean> popularItemList, StoreDetailActivity activity) {
        this.context = context;
        this.topAisleBeanList = topAisleBeanList;
        this.popularItemList = popularItemList;
        this.activity = activity;
    }


    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category, container,
                false);
        context = getActivity().getApplicationContext();
        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

        pref = this.context.getApplicationContext().getSharedPreferences("MyPref",
                this.context.MODE_PRIVATE);

        //load model here
        categoryListView = (ExpandableListView) rootView.findViewById(R.id.list_category);

        if (topAisleBeanList.get(0).getTopCategoryName().equalsIgnoreCase("Saved Items")) {

        } else {
            TopAisleBean topAisleBean = new TopAisleBean();
            topAisleBean.setTopCategoryId("popular");
            topAisleBean.setTopCategoryName("Popular");
            topAisleBeanList.add(0, topAisleBean);

            TopAisleBean topAisleBean1 = new TopAisleBean();
            topAisleBean1.setTopCategoryId("saved items");
            topAisleBean1.setTopCategoryName("Saved Items");
            topAisleBeanList.add(0, topAisleBean1);
        }


        loadSampleData();
        adpt = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        categoryListView.setAdapter(adpt);

        categoryListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                activity.plusAisleBtn.setImageResource(R.drawable.plus_icon);
                String topCategoryId = listDataHeader.get(i).getTopCategoryId();
                List<SubAisleBean> subAisleBeanList = (List<SubAisleBean>) listDataChild.get(topCategoryId);
                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                RelativeLayout categoryLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_category);
                RelativeLayout fragmentLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_fragment);
                if (!StoreDetailActivity.isCategory) {
                    Animation animHide = AnimationUtils.loadAnimation(context, R.anim.profile_slide_down);
                    Animation alphaAnimshow = AnimationUtils.loadAnimation(context, R.anim.alpha_show);
                    recyclerView.startAnimation(animHide);
                    recyclerView.setVisibility(View.GONE);
                    fragmentLayout.setVisibility(View.VISIBLE);
                    fragmentLayout.startAnimation(alphaAnimshow);
                    StoreDetailActivity.isCategory = true;
                    StoreDetailActivity.cat = true;

                } else {
                    Animation animShow = AnimationUtils.loadAnimation(context, R.anim.profile_slide_up);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(context, R.anim.aplha_hide);
                    recyclerView.startAnimation(animShow);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    StoreDetailActivity.isCategory = false;
                    Global.sort = null;
                    Global.refineBrandBeanList = new ArrayList<RefineBrandBean>();
                    Global.refinePriceBeanList = new ArrayList<RefinePriceBean>();
                    if (i == 0) {
                        categoryLayout.setVisibility(View.GONE);
                        if (pref.getString(Constants.USER_ID, null) != null) {
                            requestForSaveAndPopular("saved");
                        } else {
                            Intent intent = new Intent(getActivity(), SignUpActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }

                    } else if (i == 1) {
                        categoryLayout.setVisibility(View.GONE);
                        requestForSaveAndPopular("popular");
                    } else {
                        categoryLayout.setVisibility(View.VISIBLE);
                        requestForAisleAndSupplierStoreForTopCategory(topCategoryId);
                    }


                    SharedPreferences.Editor editor = pref.edit();
                    oldSubCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                    oldTopCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    editor.putString(Constants.TOP_CATEGORY_ID, topCategoryId);
                    editor.putString(Constants.CATEGORY, "top");
                    editor.putString(Constants.SORT, null);
                    editor.apply();
                    ImageView sortImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                    ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);

                    sortImg.setImageResource(R.drawable.indication_dark_circle);
                    refineImg.setImageResource(R.drawable.indication_dark_circle);

                }
                return true;

            }
        });


        categoryListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                activity.plusAisleBtn.setImageResource(R.drawable.plus_icon);
                Log.d("group position", String.valueOf(i));
                Log.d("child position", String.valueOf(i1));
                Log.d("row id", String.valueOf(l));
                String topCategoryId = listDataHeader.get(i).getTopCategoryId();
                List<SubAisleBean> subAisleBeanList = (List<SubAisleBean>) listDataChild.get(topCategoryId);
                SubAisleBean subAisleBean = subAisleBeanList.get(i1);

                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                RelativeLayout categoryLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_category);
                RelativeLayout fragmentLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_fragment);
                if (!StoreDetailActivity.isCategory) {
                    Animation animHide = AnimationUtils.loadAnimation(context, R.anim.profile_slide_down);
                    Animation alphaAnimshow = AnimationUtils.loadAnimation(context, R.anim.alpha_show);
                    recyclerView.startAnimation(animHide);
                    recyclerView.setVisibility(View.GONE);
                    fragmentLayout.setVisibility(View.VISIBLE);
                    fragmentLayout.startAnimation(alphaAnimshow);
                    StoreDetailActivity.isCategory = true;
                } else {
                    Animation animShow = AnimationUtils.loadAnimation(context, R.anim.profile_slide_up);
                    Animation alphaAnimHide = AnimationUtils.loadAnimation(context, R.anim.aplha_hide);
                    recyclerView.startAnimation(animShow);
                    categoryLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    fragmentLayout.setVisibility(View.GONE);
                    fragmentLayout.startAnimation(alphaAnimHide);
                    StoreDetailActivity.isCategory = false;
                    StoreDetailActivity.cat = true;
                    Global.sort = null;
                    Global.refineBrandBeanList = new ArrayList<RefineBrandBean>();
                    Global.refinePriceBeanList = new ArrayList<RefinePriceBean>();
                    requestForAisleAndSupplierStore(subAisleBean.getSubCategoryId(), topCategoryId);
                    SharedPreferences.Editor editor = pref.edit();
                    oldSubCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                    oldTopCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    editor.putString(Constants.SUB_CATEGORY_ID, subAisleBean.getSubCategoryId());
                    editor.putString(Constants.TOP_CATEGORY_ID, topCategoryId);
                    editor.putString(Constants.CATEGORY, "sub");
                    editor.putString(Constants.SORT, null);
                    editor.apply();

                    ImageView sortImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                    ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);

                    sortImg.setImageResource(R.drawable.indication_dark_circle);
                    refineImg.setImageResource(R.drawable.indication_dark_circle);
                }
                return true;
            }
        });
        return rootView;
    }

    private void requestForSaveAndPopular(final String type) {
        Global.showProgress(getActivity());
        String url;
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        String userId = pref.getString(Constants.USER_ID, null);
        url = "product";
        RequestQueue rq = Volley.newRequestQueue(context);


        JSONObject params = new JSONObject();
        try {

            params.put("supplier_id", supplierId);
            if (type.equalsIgnoreCase("saved")) {
                params.put("saved", "");
            } else {
                params.put("popular", "");
            }

            params.put("user_id", userId);

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                               Global.hideProgress();
                                aisleBean = ConversionHelper.convertCategoryAisleJsonToCategoryAisleBean(jsonObject);
                                Global.topAisleBean = aisleBean;
                                Global.backCount = 1;
                                Global.backFrom = "";

                                if (aisleBean.getPopularAisleItemBeanList() != null) {
                                    if (aisleBean.getPopularAisleItemBeanList().size() > 0) {
                                        List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                                        List<SectionedGridRecyclerViewAdapter.Section> sections =
                                                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();


                                        for (int c = 0; c < aisleBean.getPopularAisleItemBeanList().size(); c++) {
                                            subAisleItemBeanList.add(aisleBean.getPopularAisleItemBeanList().get(c));
                                        }

                                        if (type.equalsIgnoreCase("popular")) {
                                            sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", aisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no", null));
                                        } else {
                                            sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Your saved items", aisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no", null));
                                        }


                                        DatabaseHandler db = new DatabaseHandler(context);
                                        for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                            SubAisleItemBean item = subAisleItemBeanList.get(c);
                                            if (item.getIsSave().equalsIgnoreCase("YES")) {
                                                String str = db.getLikeItem(item.getSupplierItemId());
                                                if (str == null) {
                                                    db.addLikeItem(item.getSupplierItemId());
                                                }
                                            }
                                        }

                                        adapter = new SimpleAdapter(context, subAisleItemBeanList, activity, CategoryFragment.this);
                                        RecyclerView recyclerView;
                                        RecyclerView.LayoutManager mLayoutManager;
                                        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                                        recyclerView.setHasFixedSize(true);
                                        mLayoutManager = new GridLayoutManager(context, 3);


                                        //Add your adapter to the sectionAdapter
                                        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                                        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                                SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, activity);
                                        mSectionedAdapter.setSections(sections.toArray(dummy));

                                        //Apply this adapter to the RecyclerView
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setAdapter(mSectionedAdapter);
                                    }
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();

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

    private void requestForAisleAndSupplierStoreForTopCategory(final String topCategoryId) {

        Global.showProgress(getActivity());
        String url;
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        String userId = pref.getString(Constants.USER_ID, null);
        url = "product";
        RequestQueue rq = Volley.newRequestQueue(context);

        JSONObject params = new JSONObject();
        try {

            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            params.put("user_id", userId);

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                aisleBean = ConversionHelper.convertCategoryAisleJsonToCategoryAisleBean(jsonObject);
                                Global.topAisleBean = aisleBean;
                                Global.backCount = 1;
                                Global.backFrom = "";
                                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                                List<SectionedGridRecyclerViewAdapter.Section> sections =
                                        new ArrayList<SectionedGridRecyclerViewAdapter.Section>();


                                if (aisleBean.getPopularAisleItemBeanList() != null) {
                                    if (aisleBean.getPopularAisleItemBeanList().size() > 0) {

                                        for (int c = 0; c < aisleBean.getPopularAisleItemBeanList().size(); c++) {
                                            subAisleItemBeanList.add(aisleBean.getPopularAisleItemBeanList().get(c));
                                        }

                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", aisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no", pref.getString(Constants.TOP_CATEGORY_ID, null)));
                                    }
                                }


                                SubAisleBean subAisleBean1 = null;


                                List<SubAisleBean> subAisleBeanList = aisleBean.getTopAisleBean().getSubAisleBeanList();
                                for (int j = 0; j < subAisleBeanList.size(); j++) {
                                    List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();

                                    for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                        subAisleItemBeanList1.get(k).setSubCategoryId(subAisleBeanList.get(j).getSubCategoryId());
                                        subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                    }
                                }

                                DatabaseHandler db = new DatabaseHandler(context);
                                for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                    SubAisleItemBean item = subAisleItemBeanList.get(c);
                                    if (item.getIsSave().equalsIgnoreCase("YES")) {
                                        String str = db.getLikeItem(item.getSupplierItemId());
                                        if (str == null) {
                                            db.addLikeItem(item.getSupplierItemId());
                                        }
                                    }
                                }


                                adapter = new SimpleAdapter(context, subAisleItemBeanList, activity, CategoryFragment.this);
                                RecyclerView recyclerView;
                                RecyclerView.LayoutManager mLayoutManager;
                                recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new GridLayoutManager(context, 3);
                                int j = 0;
                                int count = 0;

                                if (aisleBean.getPopularAisleItemBeanList() != null) {
                                    if (aisleBean.getPopularAisleItemBeanList().size() > 0) {
                                        j = 1;
                                        count += aisleBean.getPopularAisleItemBeanList().size();
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
                                        SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, activity);
                                mSectionedAdapter.setSections(sections.toArray(dummy));

                                //Apply this adapter to the RecyclerView
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(mSectionedAdapter);

                                TextView topCategoryText = (TextView) getActivity().findViewById(R.id.text_topcategory);
                                topCategoryText.setText(aisleBean.getTopAisleBean().getTopCategoryName());
                                requestForBrandandSizeForTopCategory(supplierId, topCategoryId);


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();

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

    private void requestForBrandandSizeForTopCategory(String supplierId, String topCategoryId) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&for=topcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

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
                                ImageView sortImage = (ImageView) getActivity().findViewById(R.id.image_sort_icon);
                                sortImage.setImageResource(R.drawable.sort_icon);
                                ImageView refineImage = (ImageView) getActivity().findViewById(R.id.image_refine_icon);
                                refineImage.setImageResource(R.drawable.refine_icon);
                                if (Global.globalBean.getRefineBrandBeanList() == null) {
                                    RelativeLayout refine = (RelativeLayout) activity.findViewById(R.id.layout_refine);
                                    refine.setVisibility(View.GONE);

                                } else {
                                    RelativeLayout refine = (RelativeLayout) activity.findViewById(R.id.layout_refine);
                                    refine.setVisibility(View.VISIBLE);
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
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

    public void requestForAisleAndSupplierStore(final String subCategoryid, final String topCategoryId) {
        Global.showProgress(getActivity());
        String url;
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        String userId = pref.getString(Constants.USER_ID, null);

        url = "product";


        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {

            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            params.put("sub_category_id", subCategoryid);
            params.put("user_id", userId);

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                aisleBean = ConversionHelper.convertCategoryAisleJsonToCategoryAisleBean(jsonObject);
                                Global.subAisleBean = aisleBean;
                                Global.backCount = 2;
                                Global.backFrom = "aisle";
                                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();

                                List<SectionedGridRecyclerViewAdapter.Section> sections =
                                        new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                                int j = 0;
                                if (aisleBean != null) {
                                    if (aisleBean.getPopularAisleItemBeanList() != null) {
                                        if (aisleBean.getPopularAisleItemBeanList().size() > 0) {
                                            for (int c = 0; c < aisleBean.getPopularAisleItemBeanList().size(); c++) {
                                                subAisleItemBeanList.add(aisleBean.getPopularAisleItemBeanList().get(c));
                                            }
                                            sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Popular", aisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), aisleBean.getPopularIsMore(), pref.getString(Constants.TOP_CATEGORY_ID, null)));
                                        }
                                    }
                                    SubAisleBean subAisleBean1 = null;
                                    if (aisleBean.getTopAisleBean() != null) {

                                        for (int i = 0; i < aisleBean.getTopAisleBean().getSubAisleBeanList().size(); i++) {
                                            if (aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId().equalsIgnoreCase(subCategoryid)) {
                                                List<SubAisleItemBean> subAisleItemBeanList1 = aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubAisleItemBeanList();
                                                for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                                    subAisleItemBeanList1.get(k).setSubCategoryId(aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId());
                                                    subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                                }
                                                subAisleBean1 = aisleBean.getTopAisleBean().getSubAisleBeanList().get(i);
                                            }
                                        }


                                        DatabaseHandler db = new DatabaseHandler(context);
                                        for (int c = 0; c < subAisleItemBeanList.size(); c++) {
                                            SubAisleItemBean item = subAisleItemBeanList.get(c);
                                            if (item.getIsSave().equalsIgnoreCase("YES")) {
                                                String str = db.getLikeItem(item.getSupplierItemId());
                                                if (str == null) {
                                                    db.addLikeItem(item.getSupplierItemId());
                                                }
                                            }
                                        }

                                        adapter = new SimpleAdapter(context, subAisleItemBeanList, activity, CategoryFragment.this);
                                        RecyclerView recyclerView;
                                        RecyclerView.LayoutManager mLayoutManager;
                                        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                                        recyclerView.setHasFixedSize(true);
                                        mLayoutManager = new GridLayoutManager(context, 3);


                                        int count = 0;
                                        if (aisleBean.getPopularAisleItemBeanList() != null) {
                                            if (aisleBean.getPopularAisleItemBeanList().size() > 0) {
                                                count += aisleBean.getPopularAisleItemBeanList().size();
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
                                                SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, activity);
                                        mSectionedAdapter.setSections(sections.toArray(dummy));

                                        //Apply this adapter to the RecyclerView
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setAdapter(mSectionedAdapter);

                                        TextView topCategoryText = (TextView) getActivity().findViewById(R.id.text_topcategory);
                                        topCategoryText.setText(subAisleBean1.getSubCategoryName());

                                        requestForBrandandSizeForSubCategory(supplierId, topCategoryId, subCategoryid);
                                    } else {
                                        RelativeLayout categoryLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_category);
                                        categoryLayout.setVisibility(View.GONE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(Constants.SUB_CATEGORY_ID, oldSubCategoryId);
                                        editor.putString(Constants.TOP_CATEGORY_ID, oldTopCategoryId);
                                        editor.putString(Constants.CATEGORY, null);
                                        editor.putString(Constants.SORT, null);
                                        editor.apply();
                                        showAlert("No Result found");
                                       Global.hideProgress();
                                    }
                                } else {
                                    RelativeLayout categoryLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_category);
                                    categoryLayout.setVisibility(View.GONE);

                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(Constants.SUB_CATEGORY_ID, oldSubCategoryId);
                                    editor.putString(Constants.TOP_CATEGORY_ID, oldTopCategoryId);
                                    editor.putString(Constants.CATEGORY, null);
                                    editor.putString(Constants.SORT, null);
                                    editor.apply();
                                    showAlert("No Result found");
                                   Global.hideProgress();
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();

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

    private void requestForBrandandSizeForSubCategory(String supplierId, String topCategoryId, String subCategoryid) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&sub_category_id=" + subCategoryid + "&for=subcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

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
                                ImageView sortImage = (ImageView) getActivity().findViewById(R.id.image_sort_icon);
                                sortImage.setImageResource(R.drawable.sort_icon);
                                ImageView refineImage = (ImageView) getActivity().findViewById(R.id.image_refine_icon);
                                refineImage.setImageResource(R.drawable.refine_icon);
                                if (Global.globalBean.getRefineBrandBeanList() == null) {
                                    RelativeLayout refine = (RelativeLayout) activity.findViewById(R.id.layout_refine);
                                    refine.setVisibility(View.GONE);

                                } else {
                                    RelativeLayout refine = (RelativeLayout) activity.findViewById(R.id.layout_refine);
                                    refine.setVisibility(View.VISIBLE);
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();

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


    public void loadSampleData() {


        listDataHeader = topAisleBeanList;
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<SubAisleBean> subAisleBeanList = listDataHeader.get(i).getSubAisleBeanList();
            listDataChild.put(listDataHeader.get(i).getTopCategoryId(), subAisleBeanList);
        }


    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(getActivity());
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
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
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            DatabaseHandler db = new DatabaseHandler(context);
            List<OrderBean> orderBeanList = db.getAllOrders();
            if (orderBeanList != null) {
                if (orderBeanList.size() > 0) {
                    countLayout = (RelativeLayout) activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout) activity.findViewById(R.id.layout_bottom);
                    TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                    TextView totalPriceText = (TextView) activity.findViewById(R.id.text_order_price);

                    countLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    int ordersCount = db.getOrdersCount();

                    totalCartCountText.setText(String.valueOf(ordersCount));

                    double total = 0;
                    for (int i = 0; i < orderBeanList.size(); i++) {
                        OrderBean order = orderBeanList.get(i);
                        total += Double.valueOf(order.getFinalPrice());
                    }
                    totalPriceText.setText(" $" + String.format("%.2f", total));

                } else {
                    countLayout = (RelativeLayout) activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout) activity.findViewById(R.id.layout_bottom);
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
            } else {
                countLayout = (RelativeLayout) activity.findViewById(R.id.layout_total_cart_count);
                bottomLayout = (RelativeLayout) activity.findViewById(R.id.layout_bottom);
                countLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
            }
        }
    }

    public void refreshData() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}

