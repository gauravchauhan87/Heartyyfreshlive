package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.SignUpActivity;
import com.heartyy.heartyyfresh.StoreDetailActivity;
import com.heartyy.heartyyfresh.adapter.CustomSortListAdapter;
import com.heartyy.heartyyfresh.adapter.SectionedGridRecyclerViewAdapter;
import com.heartyy.heartyyfresh.adapter.SimpleAdapter;
import com.heartyy.heartyyfresh.bean.CategoryAisleBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SortBean;
import com.heartyy.heartyyfresh.bean.SortItemsBean;
import com.heartyy.heartyyfresh.bean.SubAisleBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 12/20/2015.
 */
@SuppressLint("ValidFragment")
public class SortFragment extends Fragment {

    private View rootView;
    private Context context;
    Typeface andBold, bold, italic, light;
    private ListView sortListView;
    private CustomSortListAdapter sadapter;
    private List<SortBean> sortTypeList;
    TextView sortByText;
    ImageButton closeBtn;
    private SharedPreferences pref;
    private SortItemsBean aisleBean;
    private CategoryAisleBean categoryAisleBean;
   // private SimpleAdapter adapter;
    RelativeLayout countLayout, bottomLayout;
    StoreDetailActivity activity;


    @SuppressLint("ValidFragment")
    public SortFragment(Context context, StoreDetailActivity activity) {
        this.context = context;
        this.activity = activity;
    }


    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_sort, container,
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
        sortListView = (ListView) rootView.findViewById(R.id.list_sort_by);
        sortByText = (TextView) rootView.findViewById(R.id.text_sort_by);
        closeBtn = (ImageButton) rootView.findViewById(R.id.button_close);
        sortByText.setTypeface(light);
        sortTypeList = new ArrayList<>();
        SortBean s0 = new SortBean();
        s0.setSortType("Relevance");
        s0.setSortIcon(R.drawable.relevance_icon);
        sortTypeList.add(s0);

        SortBean s1 = new SortBean();
        s1.setSortType("Price: Low to High");
        s1.setSortIcon(R.drawable.low_to_high);
        sortTypeList.add(s1);

        SortBean s2 = new SortBean();
        s2.setSortType("Price: High to Low");
        s2.setSortIcon(R.drawable.high_to_low);
        sortTypeList.add(s2);

        SortBean s3 = new SortBean();
        s3.setSortType("On Sale");
        s3.setSortIcon(R.drawable.on_sale);
        sortTypeList.add(s3);

        SortBean s4 = new SortBean();
        s4.setSortType("Saved Items");
        s4.setSortIcon(R.drawable.saved_items);
        sortTypeList.add(s4);

        SortBean s5 = new SortBean();
        s5.setSortType("Popularity");
        s5.setSortIcon(R.drawable.popularity);
        sortTypeList.add(s5);

        sadapter = new CustomSortListAdapter(context, sortTypeList);
        sortListView.setAdapter(sadapter);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StoreDetailActivity.isSortFragment) {
                    getActivity().findViewById(R.id.layout_sort_fragment).setVisibility(View.GONE);
                    StoreDetailActivity.isSortFragment = false;
                }

                ////////////
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
                ////////////
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (StoreDetailActivity.isSortFragment) {
                    getActivity().findViewById(R.id.layout_sort_fragment).setVisibility(View.GONE);
                    StoreDetailActivity.isSortFragment = false;

                }
                String category = pref.getString(Constants.CATEGORY, null);
                String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
                String subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                if (category.equalsIgnoreCase("sub")) {
                    if (i == 0) {
                        Global.sort = null;
                        sadapter.notifyDataSetChanged();
                        sortItems(supplierId, subCategoryId, topCategoryId, null);
                    } else if (i == 1) {
                        Global.sort = "low";
                        sadapter.notifyDataSetChanged();
                        sortItems(supplierId, subCategoryId, topCategoryId, "low");
                    } else if (i == 2) {
                        Global.sort = "high";
                        sadapter.notifyDataSetChanged();
                        sortItems(supplierId, subCategoryId, topCategoryId, "high");
                    } else if (i == 3) {
                        Global.sort = "onsale";
                        sadapter.notifyDataSetChanged();
                        sortItems(supplierId, subCategoryId, topCategoryId, "onsale");
                    } else if (i == 4) {
                        if (pref.getString(Constants.USER_ID, null) == null) {
                            Intent intent = new Intent(getActivity(), SignUpActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        } else {
                            Global.sort = "saved";
                            sadapter.notifyDataSetChanged();
                            sortItems(supplierId, subCategoryId, topCategoryId, "saved");
                        }
                    } else if (i == 5) {
                        Global.sort = "popular";
                        sadapter.notifyDataSetChanged();
                        sortItems(supplierId, subCategoryId, topCategoryId, "popular");
                    }
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.SORT, "yes");
                    editor.putString(Constants.REFINE, null);
                    editor.commit();
                    ImageView sortImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                    ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);

                    sortImg.setImageResource(R.drawable.indication_circle);

                    if (Global.refineBrandBeanList.size() > 0 || Global.refinePriceBeanList.size() > 0) {
                        refineImg.setImageResource(R.drawable.indication_circle);
                    } else {
                        refineImg.setImageResource(R.drawable.indication_dark_circle);
                    }

                    if (Global.backFrom.equalsIgnoreCase("top")) {
                        SharedPreferences.Editor editor1 = pref.edit();
                        editor1.putString(Constants.SORT, null);
                        editor1.putString(Constants.REFINE, null);
                        editor1.commit();
                    }

                } else {
                    if (i == 0) {
                        Global.sort = null;
                        sadapter.notifyDataSetChanged();
                        sortItemsForTopcategory(supplierId, topCategoryId, null);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.SORT, null);
                        editor.putString(Constants.REFINE, null);
                        editor.commit();
                    } else if (i == 1) {
                        Global.sort = "low";
                        sadapter.notifyDataSetChanged();
                        sortItemsForTopcategory(supplierId, topCategoryId, "low");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.SORT, "yes");
                        editor.putString(Constants.REFINE, null);
                        editor.commit();
                    } else if (i == 2) {
                        Global.sort = "high";
                        sadapter.notifyDataSetChanged();
                        sortItemsForTopcategory(supplierId, topCategoryId, "high");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.SORT, "yes");
                        editor.putString(Constants.REFINE, null);
                        editor.commit();
                    } else if (i == 3) {
                        Global.sort = "onsale";
                        sadapter.notifyDataSetChanged();
                        sortItemsForTopcategory(supplierId, topCategoryId, "onsale");
                    } else if (i == 4) {
                        if (pref.getString(Constants.USER_ID, null) == null) {
                            Intent intent = new Intent(getActivity(), SignUpActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        } else {
                            Global.sort = "saved";
                            sadapter.notifyDataSetChanged();
                            sortItemsForTopcategory(supplierId, topCategoryId, "saved");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.SORT, "yes");
                            editor.putString(Constants.REFINE, null);
                            editor.commit();
                        }
                    } else if (i == 5) {
                        Global.sort = "popular";
                        sadapter.notifyDataSetChanged();
                        sortItemsForTopcategory(supplierId, topCategoryId, "popular");
                    }
                    if (pref.getString(Constants.USER_ID, null) != null) {
                        ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                        refineImg.setImageResource(R.drawable.indication_dark_circle);
                        ImageView indicatoinImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                        indicatoinImg.setImageResource(R.drawable.indication_circle);
                    }
                }

                ImageView indicatoinImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                indicatoinImg.setImageResource(R.drawable.indication_circle);
                ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                if (Global.refineBrandBeanList.size() > 0 || Global.refinePriceBeanList.size() > 0) {
                    refineImg.setImageResource(R.drawable.indication_circle);
                } else {
                    refineImg.setImageResource(R.drawable.indication_dark_circle);
                }

                //////
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
                        for (int j = 0; j < orderBeanList.size(); j++) {
                            OrderBean order = orderBeanList.get(j);
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
                /////


            }
        });

        return rootView;
    }

    private void sortItemsForTopcategory(String supplierId, String topCategoryId, String sortType) {

        Global.showProgress(getActivity());
        String url;
        url = "product";
        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        String userId = pref.getString(Constants.USER_ID, null);
        try {

            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            if(sortType!=null) {
                params.put("sort", sortType);
            }
            params.put("user_id", userId);
            if (Global.refineBrandBeanList != null) {
                if (Global.refineBrandBeanList.size() > 0) {
                    JSONArray brandsArray = new JSONArray();
                    for (int i = 0; i < Global.refineBrandBeanList.size(); i++) {
                        brandsArray.put(Global.refineBrandBeanList.get(i).getBrandid());
                    }
                    params.put("brands", brandsArray);
                }

            }

            if (Global.refineSizeBeanList != null) {
                if (Global.refineSizeBeanList.size() > 0) {
                    JSONArray sizeArray = new JSONArray();
                    for (int i = 0; i < Global.refineSizeBeanList.size(); i++) {

                        sizeArray.put(Global.refineSizeBeanList.get(i).getSize() + " " + Global.refineSizeBeanList.get(i).getUom());
                    }
                    params.put("sizes", sizeArray);
                    params.put("user_id", pref.getString(Constants.USER_ID, null));
                }
            }

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                categoryAisleBean = ConversionHelper.convertTopCategoryJsonToTopCategoryBeanOnTopCategoryBasis(jsonObject);
                                Global.sortedTopAisleBean = categoryAisleBean;
                                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                                SubAisleBean subAisleBean1 = null;

                                List<SubAisleBean> subAisleBeanList = categoryAisleBean.getTopAisleBean().getSubAisleBeanList();
                                for (int j = 0; j < subAisleBeanList.size(); j++) {
                                    List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                    for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
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

                                activity.adapter = new SimpleAdapter(context, subAisleItemBeanList, activity);
                                RecyclerView recyclerView;
                                RecyclerView.LayoutManager mLayoutManager;
                                recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new GridLayoutManager(context, 3);

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
                                        SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, recyclerView, activity.adapter, mLayoutManager, activity);
                                mSectionedAdapter.setSections(sections.toArray(dummy));

                                //Apply this adapter to the RecyclerView
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(mSectionedAdapter);

                                TextView topCategoryText = (TextView) getActivity().findViewById(R.id.text_topcategory);
                                topCategoryText.setText(categoryAisleBean.getTopAisleBean().getTopCategoryName());

                                ImageView indicatoinImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                                indicatoinImg.setImageResource(R.drawable.indication_circle);
                                ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                                //refineImg.setImageResource(R.drawable.indication_circle);
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();
                                //showAlert(jsonObject.getString("message"));
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

    private void sortItems(String supplierId, String subCategoryId, String topCategoryId, String sortType) {
        Global.showProgress(getActivity());
        String url;
        url = "product";
        RequestQueue rq = Volley.newRequestQueue(context);
        String userId = pref.getString(Constants.USER_ID, null);
        JSONObject params = new JSONObject();
        try {

            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            params.put("sub_category_id", subCategoryId);
            if(sortType!=null) {
                params.put("sort", sortType);
            }
            params.put("user_id", userId);
            if (Global.refineBrandBeanList != null) {
                if (Global.refineBrandBeanList.size() > 0) {
                    JSONArray brandsArray = new JSONArray();
                    for (int i = 0; i < Global.refineBrandBeanList.size(); i++) {
                        brandsArray.put(Global.refineBrandBeanList.get(i).getBrandid());
                    }
                    params.put("brands", brandsArray);
                }

            }

            if (Global.refinePriceBeanList != null) {
                if (Global.refinePriceBeanList.size() > 0) {
                    JSONArray priceArray = new JSONArray();
                    for (int i = 0; i < Global.refinePriceBeanList.size(); i++) {
                        JSONObject priceObj = new JSONObject();
                        priceObj.put("from", Global.refinePriceBeanList.get(i).getFrom());
                        priceObj.put("to", Global.refinePriceBeanList.get(i).getTo());
                        priceArray.put(priceObj);
                    }
                    params.put("prices", priceArray);
                }
            }

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                categoryAisleBean = ConversionHelper.convertTopCategoryJsonToTopCategoryBeanOnTopCategoryBasis(jsonObject);
                                Global.sortedSubAisleBean = categoryAisleBean;
                                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                                SubAisleBean subAisleBean1 = null;

                                List<SubAisleBean> subAisleBeanList = categoryAisleBean.getTopAisleBean().getSubAisleBeanList();
                                for (int j = 0; j < subAisleBeanList.size(); j++) {
                                    List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                    for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
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


                                activity.adapter = new SimpleAdapter(context, subAisleItemBeanList, activity);
                                RecyclerView recyclerView;
                                RecyclerView.LayoutManager mLayoutManager;
                                recyclerView = (RecyclerView) getActivity().findViewById(R.id.cardList);
                                recyclerView.setHasFixedSize(true);
                                mLayoutManager = new GridLayoutManager(context, 3);

                                List<SectionedGridRecyclerViewAdapter.Section> sections =
                                        new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                                int j = 0;
                                int count = 0;
                                for (int i = 0; i < subAisleBeanList.size(); i++) {
                                    List<SubAisleItemBean> itemsList = subAisleBeanList.get(i).getSubAisleItemBeanList();
                                    count = itemsList.size();
                                    subAisleBean1 = subAisleBeanList.get(i);
                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(), null));
                                    j = count;
                                }


                                //Add your adapter to the sectionAdapter
                                SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                                SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                        SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, recyclerView, activity.adapter, mLayoutManager, activity);
                                mSectionedAdapter.setSections(sections.toArray(dummy));

                                //Apply this adapter to the RecyclerView
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(mSectionedAdapter);

                                TextView topCategoryText = (TextView) getActivity().findViewById(R.id.text_topcategory);
                                topCategoryText.setText(subAisleBean1.getSubCategoryName());


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
        if (activity.adapter != null) {
            activity.adapter.notifyDataSetChanged();
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
}
