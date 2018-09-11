package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
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
import com.heartyy.heartyyfresh.StoreDetailActivity;
import com.heartyy.heartyyfresh.adapter.CustomRefineBrandListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomRefineCategoryListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomRefinePriceAdapter;
import com.heartyy.heartyyfresh.adapter.CustomRefineSizeAdapter;
import com.heartyy.heartyyfresh.adapter.SectionedGridRecyclerViewAdapter;
import com.heartyy.heartyyfresh.adapter.SimpleAdapter;
import com.heartyy.heartyyfresh.bean.CategoryAisleBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.RefineBrandBean;
import com.heartyy.heartyyfresh.bean.RefinePriceBean;
import com.heartyy.heartyyfresh.bean.RefineSizeBean;
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
public class RefineFragment extends Fragment {

    private View rootView;
    private Context context;
    Typeface andBold, bold, italic, light;
    private ListView refineMainListView, refineBrandListView, refineSizeListView, refinePriceListView;
    private CustomRefineCategoryListAdapter catAdapter;
    private CustomRefineBrandListAdapter radapter;
    private CustomRefinePriceAdapter priceAdapter;
    private CustomRefineSizeAdapter sizeAdapter;
    List<String> categoryList;
    List<RefineBrandBean> refineBrandBeanList;
    List<RefineSizeBean> refineSizeBeanList;
    List<RefinePriceBean> refinePriceBeanList;
    ImageButton closeBtn;
    private SharedPreferences pref;
    private Button applyBtn, clearBtn;
    private CategoryAisleBean categoryAisleBean;
    TextView refineByText;
    StoreDetailActivity activity;
    RelativeLayout countLayout,bottomLayout;


    public RefineFragment(Context context, StoreDetailActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_refine, container,
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
        refineMainListView = (ListView) rootView.findViewById(R.id.list_main_category);
        refineBrandListView = (ListView) rootView.findViewById(R.id.list_refine_brand);
        refineSizeListView = (ListView) rootView.findViewById(R.id.list_refine_size);
        refinePriceListView = (ListView) rootView.findViewById(R.id.list_refine_price);
        closeBtn = (ImageButton) rootView.findViewById(R.id.button_close);
        applyBtn = (Button) rootView.findViewById(R.id.button_apply);
        clearBtn = (Button) rootView.findViewById(R.id.button_clear_all);
        refineByText = (TextView) rootView.findViewById(R.id.text_refine_by);
        categoryList = new ArrayList<>();
        categoryList.add("Brand");
        categoryList.add("Price");
        clearBtn.setTypeface(andBold);
        applyBtn.setTypeface(andBold);
        refineByText.setTypeface(light);


        catAdapter = new CustomRefineCategoryListAdapter(context, categoryList);
        radapter = new CustomRefineBrandListAdapter(context, Global.globalBean.getRefineBrandBeanList(), "Brand");
        if(Global.globalBean.getRefinePriceBeanList()!=null) {
            priceAdapter = new CustomRefinePriceAdapter(context, Global.globalBean.getRefinePriceBeanList(), "1");
            refinePriceListView.setAdapter(priceAdapter);
        }
        refineMainListView.setAdapter(catAdapter);
        refineBrandListView.setAdapter(radapter);



        refineMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomRefineCategoryListAdapter adapter = (CustomRefineCategoryListAdapter) refineMainListView.getAdapter();
                String data = (String) adapterView.getItemAtPosition(i);
                adapter.setSelection(i);
                adapter.notifyDataSetChanged();
                if (i == 0) {
                    String category = pref.getString(Constants.CATEGORY, null);
                    String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
                   /* if (category.equalsIgnoreCase("top")) {
                        String sizeId = "";
                        List<RefineSizeBean> sizeBeanList = Global.globalBean.getRefineSizeBeanList();
                        for (int j = 0; j < sizeBeanList.size(); j++) {
                            RefineSizeBean sizeBean = sizeBeanList.get(j);
                            if (sizeBean.isSelected()) {
                                sizeId += sizeBean.getSize() + ",";

                            }
                        }

                        if (sizeId.length() > 0) {
                            sizeId = sizeId.substring(0, sizeId.length() - 1);
                            String topCategory = pref.getString(Constants.TOP_CATEGORY_ID, null);
                            getBrandTopCategoryListOnSize(supplierId, topCategory, sizeId);
                        }
                    } else {
                        String sizeId = "";
                        List<RefineSizeBean> sizeBeanList = Global.globalBean.getRefineSizeBeanList();
                            RefineSizeBean sizeBean = sizeBeanList.get(j);
                            if (sizeBean.isSelected()) {
                                sizeId += sizeBean.getSize() + ",";

                            }
                        }

                        if (sizeId.length() > 0) {
                            sizeId = sizeId.substring(0, sizeId.length() - 1);
                            String topCategory = pref.getString(Constants.TOP_CATEGORY_ID, null);
                            String subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                            getBrandSubCategoryListOnSize(supplierId, topCategory, subCategoryId, sizeId);
                        }
                    }*/
                    refineBrandListView.setVisibility(View.VISIBLE);
                    refineSizeListView.setVisibility(View.GONE);
                    refinePriceListView.setVisibility(View.GONE);

                } else if (i == 1) {
                    String category = pref.getString(Constants.CATEGORY, null);
                    String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
                    /*if (category.equalsIgnoreCase("top")) {
                        String brandId = "";
                        List<RefineBrandBean> brandBeanList = Global.globalBean.getRefineBrandBeanList();
                        for (int j = 0; j < brandBeanList.size(); j++) {
                            RefineBrandBean brandBean = brandBeanList.get(j);
                            if (brandBean.isSelected()) {
                                brandId += brandBean.getBrandid() + ",";
                            }
                        }

                        if (brandId.length() > 0) {

                            brandId = brandId.substring(0, brandId.length() - 1);
                            String topCategory = pref.getString(Constants.TOP_CATEGORY_ID, null);
                            getSizeTopCategoryListOnBrand(supplierId, topCategory, brandId);
                        }
                    } else {
                        String brandId = "";
                        List<RefineBrandBean> brandBeanList = Global.globalBean.getRefineBrandBeanList();
                        for (int j = 0; j < brandBeanList.size(); j++) {
                            RefineBrandBean brandBean = brandBeanList.get(j);
                            if (brandBean.isSelected()) {
                                brandId += brandBean.getBrandid() + ",";
                            }
                        }

                        if (brandId.length() > 0) {
                            brandId = brandId.substring(0, brandId.length() - 1);
                            String topCategory = pref.getString(Constants.TOP_CATEGORY_ID, null);
                            String subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                            getSizeSubCategoryListOnBrand(supplierId, topCategory, subCategoryId, brandId);
                        }
                    }*/

                    refineBrandListView.setVisibility(View.GONE);
                    refineSizeListView.setVisibility(View.GONE);
                    refinePriceListView.setVisibility(View.VISIBLE);
                }


            }
        });

        refineBrandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RefineBrandBean brandBean = (RefineBrandBean) adapterView.getItemAtPosition(i);
                for (int j = 0; j < Global.globalBean.getRefineBrandBeanList().size(); j++) {
                    if (Global.globalBean.getRefineBrandBeanList().get(j).equals(brandBean)) {
                        if (!brandBean.isSelected()) {
                            Global.globalBean.getRefineBrandBeanList().get(j).setIsSelected(true);

                            Global.refineBrandBeanList.add(brandBean);

                        } else {
                            Global.globalBean.getRefineBrandBeanList().get(j).setIsSelected(false);
                            Global.refineBrandBeanList.remove(brandBean);
                        }
                        break;
                    }
                }
                CustomRefineBrandListAdapter madapter = (CustomRefineBrandListAdapter) refineBrandListView.getAdapter();
                madapter.setSelected(i);
                madapter.notifyDataSetChanged();


            }
        });


        refinePriceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RefinePriceBean priceBean = (RefinePriceBean) adapterView.getItemAtPosition(i);
                for (int j = 0; j < Global.globalBean.getRefinePriceBeanList().size(); j++) {
                    if (Global.globalBean.getRefinePriceBeanList().get(j).equals(priceBean)) {
                        if (!priceBean.isSelected()) {
                            Global.globalBean.getRefinePriceBeanList().get(j).setIsSelected(true);
                            Global.refinePriceBeanList.add(priceBean);

                        } else {
                            Global.globalBean.getRefinePriceBeanList().get(j).setIsSelected(false);
                            Global.refinePriceBeanList.remove(priceBean);

                        }
                        break;
                    }
                }
                CustomRefinePriceAdapter padapter = (CustomRefinePriceAdapter) refinePriceListView.getAdapter();
                padapter.setSelected(i);
                padapter.notifyDataSetChanged();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = pref.getString(Constants.CATEGORY, null);
                String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
                if (category.equalsIgnoreCase("top")) {
                    String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    applyRefineOnTopCategroy(supplierId, topCategoryId);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.REFINE, "yes");
                    editor.putString(Constants.SORT, null);
                    editor.commit();

                } else {
                    String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    String subCategoryId = pref.getString(Constants.SUB_CATEGORY_ID, null);
                    applyRefineOnSubCategroy(supplierId, topCategoryId, subCategoryId);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.REFINE, "yes");
                    editor.putString(Constants.SORT, null);
                    editor.commit();
                }

                ImageView indicatoinImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                indicatoinImg.setBackgroundResource(R.drawable.indication_circle);


                ImageView sortImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                ImageView refineImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                if(Global.sort!=null){
                    sortImg.setImageResource(R.drawable.indication_circle);
                }else{
                    sortImg.setImageResource(R.drawable.indication_dark_circle);
                }

                refineImg.setImageResource(R.drawable.indication_circle);

//////
                DatabaseHandler db = new DatabaseHandler(context);
                List<OrderBean> orderBeanList = db.getAllOrders();
                if (orderBeanList != null) {
                    if(orderBeanList.size() > 0) {
                        countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                        bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                        TextView totalCartCountText = (TextView)activity.findViewById(R.id.text_total_cart_count);
                        TextView totalPriceText = (TextView)activity.findViewById(R.id.text_order_price);

                        countLayout.setVisibility(View.VISIBLE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        int ordersCount = db.getOrdersCount();

                        totalCartCountText.setText(String.valueOf(ordersCount));

                        double total = 0;
                        for(int i=0;i<orderBeanList.size();i++){
                            OrderBean order = orderBeanList.get(i);
                            total += Double.valueOf(order.getFinalPrice());
                        }
                        totalPriceText.setText(" $"+String.format("%.2f",total));

                    }else{
                        countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                        bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                        countLayout.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                    }
                }else{
                    countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
////////
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.showProgress(getActivity());
                String category = pref.getString(Constants.CATEGORY, null);
                String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
                //Global.sort=null;
                if (category.equalsIgnoreCase("top")) {
                    String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    requestForBrandandSizeForTopCategory(supplierId, topCategoryId);
                } else {
                    String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);
                    String subCategoryid = pref.getString(Constants.SUB_CATEGORY_ID, null);
                    requestForBrandandSizeForSubCategory(supplierId, topCategoryId, subCategoryid);
                }

                ImageView indicatoinImg = (ImageView) getActivity().findViewById(R.id.img_refine_indication);
                indicatoinImg.setImageResource(R.drawable.indication_dark_circle);
                ImageView sortImg = (ImageView) getActivity().findViewById(R.id.img_sort_indication);
                //sortImg.setImageResource(R.drawable.indication_dark_circle);

                ///
                DatabaseHandler db = new DatabaseHandler(context);
                List<OrderBean> orderBeanList = db.getAllOrders();
                if (orderBeanList != null) {
                    if(orderBeanList.size() > 0) {
                        countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                        bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                        TextView totalCartCountText = (TextView)activity.findViewById(R.id.text_total_cart_count);
                        TextView totalPriceText = (TextView)activity.findViewById(R.id.text_order_price);

                        countLayout.setVisibility(View.VISIBLE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        int ordersCount = db.getOrdersCount();

                        totalCartCountText.setText(String.valueOf(ordersCount));

                        double total = 0;
                        for(int i=0;i<orderBeanList.size();i++){
                            OrderBean order = orderBeanList.get(i);
                            total += Double.valueOf(order.getFinalPrice());
                        }
                        totalPriceText.setText(" $"+String.format("%.2f",total));

                    }else{
                        countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                        bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                        countLayout.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                    }
                }else{
                    countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
                ///


            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StoreDetailActivity.isRefineFragment) {
                    getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                    StoreDetailActivity.isRefineFragment = false;

                }
            }
        });

        return rootView;
    }

    private void applyRefineOnSubCategroy(String supplierId, String topCategoryId, String subCategoryId) {
        Global.showProgress(getActivity());

        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            params.put("sub_category_id", subCategoryId);
            params.put("user_id", pref.getString(Constants.USER_ID, null));
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
            if (Global.sort != null) {
                params.put("sort", Global.sort);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + "product", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                categoryAisleBean = ConversionHelper.convertTopCategoryJsonToTopCategoryBeanOnTopCategoryBasis(jsonObject);
                                Global.refineSubAisleBean = categoryAisleBean;
                                Global.backFrom = "sub";
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
                                for(int c=0;c<subAisleItemBeanList.size();c++){
                                    SubAisleItemBean item = subAisleItemBeanList.get(c);
                                    if(item.getIsSave().equalsIgnoreCase("YES")){
                                        String str = db.getLikeItem(item.getSupplierItemId());
                                        if(str==null) {
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
                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), "no",null));
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
                                if (StoreDetailActivity.isRefineFragment) {
                                    getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                    StoreDetailActivity.isRefineFragment = false;

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

    private void applyRefineOnTopCategroy(String supplierId, String topCategoryId) {
        Global.showProgress(getActivity());

        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("supplier_id", supplierId);
            params.put("top_category_id", topCategoryId);
            params.put("user_id", pref.getString(Constants.USER_ID, null));
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
            if (Global.sort != null) {
                params.put("sort", Global.sort);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + "product", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                categoryAisleBean = ConversionHelper.convertTopCategoryJsonToTopCategoryBeanOnTopCategoryBasis(jsonObject);
                                Global.refineTopAisleBean = categoryAisleBean;
                                List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();

                                List<SectionedGridRecyclerViewAdapter.Section> sections =
                                        new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

                                if (categoryAisleBean.getPopularAisleItemBeanList() != null) {
                                    if (categoryAisleBean.getPopularAisleItemBeanList().size() > 0) {

                                        for (int c = 0; c < categoryAisleBean.getPopularAisleItemBeanList().size(); c++) {
                                            subAisleItemBeanList.add(categoryAisleBean.getPopularAisleItemBeanList().get(c));
                                        }

                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "Popular", categoryAisleBean.getPopularAisleItemBeanList().get(0).getSubCategoryId(), "no",null));
                                    }
                                }

                                SubAisleBean subAisleBean1 = null;

                                List<SubAisleBean> subAisleBeanList = categoryAisleBean.getTopAisleBean().getSubAisleBeanList();
                                for (int j = 0; j < subAisleBeanList.size(); j++) {
                                    List<SubAisleItemBean> subAisleItemBeanList1 = subAisleBeanList.get(j).getSubAisleItemBeanList();
                                    for (int k = 0; k < subAisleItemBeanList1.size(); k++) {
                                        subAisleItemBeanList.add(subAisleItemBeanList1.get(k));
                                    }
                                }

                                DatabaseHandler db = new DatabaseHandler(context);
                                for(int c=0;c<subAisleItemBeanList.size();c++){
                                    SubAisleItemBean item = subAisleItemBeanList.get(c);
                                    if(item.getIsSave().equalsIgnoreCase("YES")){
                                        String str = db.getLikeItem(item.getSupplierItemId());
                                        if(str==null) {
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


                                int j = 0;
                                int count = 0;

                                if (categoryAisleBean.getPopularAisleItemBeanList() != null) {
                                    if (categoryAisleBean.getPopularAisleItemBeanList().size() > 0) {
                                        j = 1;
                                        count += categoryAisleBean.getPopularAisleItemBeanList().size();
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
                                    sections.add(new SectionedGridRecyclerViewAdapter.Section(j, subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), subAisleBean1.getIsMore(),null));
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
                                if (StoreDetailActivity.isRefineFragment) {
                                    getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                    StoreDetailActivity.isRefineFragment = false;

                                }


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();

                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.REFINE, null);
                            editor.putString(Constants.SORT, null);
                            editor.commit();
                            if (StoreDetailActivity.isRefineFragment) {
                                getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                StoreDetailActivity.isRefineFragment = false;
                                ImageView indicatoinImg = (ImageView) activity.findViewById(R.id.img_sort_indication);
                                indicatoinImg.setImageResource(R.drawable.indication_dark_circle);

                                ImageView refineImg = (ImageView) activity.findViewById(R.id.img_refine_indication);
                                refineImg.setImageResource(R.drawable.indication_dark_circle);
                                Global.refineBrandBeanList = new ArrayList<>();
                                Global.refinePriceBeanList = new ArrayList<>();

                            }
                            showAlert("No such refined data found");
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

    private void getBrandSubCategoryListOnSize(String supplierId, String topCategory, String subCategoryId, String sizeId) {
        Global.showProgress(getActivity());
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategory + "&sub_category_id=" + subCategoryId + "&sizes=" + sizeId + "&for=subcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                for (int i = 0; i < Global.globalBean.getRefineBrandBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineBrandBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineBrandBeanList().get(i).getBrandid().equalsIgnoreCase(Global.refineBrandBeanList.get(j).getBrandid())) {
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if (flag == 1) {
                                        Global.globalBean.getRefineBrandBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                for (int i = 0; i < Global.globalBean.getRefineSizeBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineSizeBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineSizeBeanList().get(i).getSize().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getSize()) && Global.globalBean.getRefineSizeBeanList().get(i).getUom().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getUom())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineSizeBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                radapter = new CustomRefineBrandListAdapter(context, Global.globalBean.getRefineBrandBeanList(), "Brand");
                                priceAdapter = new CustomRefinePriceAdapter(context, refinePriceBeanList, "1");
                                sizeAdapter = new CustomRefineSizeAdapter(context, Global.globalBean.getRefineSizeBeanList(), "1");
                                refineBrandListView.setAdapter(radapter);
                                refineSizeListView.setAdapter(sizeAdapter);
                                refinePriceListView.setAdapter(priceAdapter);

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

    private void getBrandTopCategoryListOnSize(String supplierId, String topCategory, String sizeId) {
        Global.showProgress(getActivity());
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategory + "&sizes=" + sizeId + "&for=topcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                for (int i = 0; i < Global.globalBean.getRefineBrandBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineBrandBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineBrandBeanList().get(i).getBrandid().equalsIgnoreCase(Global.refineBrandBeanList.get(j).getBrandid())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineBrandBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                for (int i = 0; i < Global.globalBean.getRefineSizeBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineSizeBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineSizeBeanList().get(i).getSize().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getSize()) && Global.globalBean.getRefineSizeBeanList().get(i).getUom().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getUom())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineSizeBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                radapter = new CustomRefineBrandListAdapter(context, Global.globalBean.getRefineBrandBeanList(), "Brand");
                                priceAdapter = new CustomRefinePriceAdapter(context, refinePriceBeanList, "1");
                                sizeAdapter = new CustomRefineSizeAdapter(context, Global.globalBean.getRefineSizeBeanList(), "1");
                                refineBrandListView.setAdapter(radapter);
                                refineSizeListView.setAdapter(sizeAdapter);
                                refinePriceListView.setAdapter(priceAdapter);

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

    private void getSizeSubCategoryListOnBrand(String supplierId, String topCategory, String subCategoryId, String brandId) {
        Global.showProgress(getActivity());
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategory + "&sub_category_id=" + subCategoryId + "&brands=" + brandId + ",4003&for=subcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                for (int i = 0; i < Global.globalBean.getRefineSizeBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineSizeBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineSizeBeanList().get(i).getSize().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getSize()) && Global.globalBean.getRefineSizeBeanList().get(i).getUom().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getUom())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineSizeBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                for (int i = 0; i < Global.globalBean.getRefineBrandBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineBrandBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineBrandBeanList().get(i).getBrandid().equalsIgnoreCase(Global.refineBrandBeanList.get(j).getBrandid())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineBrandBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                radapter = new CustomRefineBrandListAdapter(context, Global.globalBean.getRefineBrandBeanList(), "Brand");
                                priceAdapter = new CustomRefinePriceAdapter(context, refinePriceBeanList, "1");
                                sizeAdapter = new CustomRefineSizeAdapter(context, Global.globalBean.getRefineSizeBeanList(), "1");
                                refineBrandListView.setAdapter(radapter);
                                refineSizeListView.setAdapter(sizeAdapter);
                                refinePriceListView.setAdapter(priceAdapter);

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

    private void getSizeTopCategoryListOnBrand(String supplierId, String topCategory, String brandId) {
        Global.showProgress(getActivity());
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategory + "&brands=" + brandId + "&for=topcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                for (int i = 0; i < Global.globalBean.getRefineSizeBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineSizeBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineSizeBeanList().get(i).getSize().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getSize()) && Global.globalBean.getRefineSizeBeanList().get(i).getUom().equalsIgnoreCase(Global.refineSizeBeanList.get(j).getUom())) {
                                            flag = 1;
                                            break;
                                        }

                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineSizeBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                for (int i = 0; i < Global.globalBean.getRefineBrandBeanList().size(); i++) {
                                    int flag = 0;
                                    for (int j = 0; j < Global.refineBrandBeanList.size(); j++) {
                                        if (Global.globalBean.getRefineBrandBeanList().get(i).getBrandid().equalsIgnoreCase(Global.refineBrandBeanList.get(j).getBrandid())) {
                                            flag = 1;
                                            break;
                                        }
                                    }

                                    if (flag == 1) {
                                        Global.globalBean.getRefineBrandBeanList().get(i).setIsSelected(true);
                                    }
                                }
                                radapter = new CustomRefineBrandListAdapter(context, Global.globalBean.getRefineBrandBeanList(), "Brand");
                                priceAdapter = new CustomRefinePriceAdapter(context, refinePriceBeanList, "1");
                                sizeAdapter = new CustomRefineSizeAdapter(context, Global.globalBean.getRefineSizeBeanList(), "1");
                                refineBrandListView.setAdapter(radapter);
                                refineSizeListView.setAdapter(sizeAdapter);
                                refinePriceListView.setAdapter(priceAdapter);

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

    private void requestForBrandandSizeForTopCategory(final String supplierId, final String topCategoryId) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&for=topcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and pri response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                Global.refineBrandBeanList = new ArrayList<>();
                                Global.refineSizeBeanList = new ArrayList<>();
                                Global.refinePriceBeanList = new ArrayList<>();
                                applyRefineOnTopCategroy(supplierId, topCategoryId);


                                if (StoreDetailActivity.isRefineFragment) {
                                    getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                    StoreDetailActivity.isRefineFragment = false;

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

    private void requestForBrandandSizeForSubCategory(final String supplierId, final String topCategoryId, final String subCategoryid) {
        String url;
        url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&sub_category_id=" + subCategoryid + "&for=subcategory";
        RequestQueue rq = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("brand and size response", jsonObject.toString());
                        Global.dialog.dismiss();

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.globalBean = ConversionHelper.convertGlobalJsonToGlobalBean(jsonObject);
                                Global.refineBrandBeanList = new ArrayList<>();
                                Global.refineSizeBeanList = new ArrayList<>();
                                applyRefineOnSubCategroy(supplierId, topCategoryId, subCategoryid);

                                if (StoreDetailActivity.isRefineFragment) {
                                    getActivity().findViewById(R.id.layout_refine_fragment).setVisibility(View.GONE);
                                    StoreDetailActivity.isRefineFragment = false;

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
    public void onResume(){
        super.onResume();
        if(activity.adapter!=null) {
            activity.adapter.notifyDataSetChanged();
            DatabaseHandler db = new DatabaseHandler(context);
            List<OrderBean> orderBeanList = db.getAllOrders();
            if (orderBeanList != null) {
                if(orderBeanList.size() > 0) {
                    countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                    TextView totalCartCountText = (TextView)activity.findViewById(R.id.text_total_cart_count);
                    TextView totalPriceText = (TextView)activity.findViewById(R.id.text_order_price);

                    countLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    int ordersCount = db.getOrdersCount();

                    totalCartCountText.setText(String.valueOf(ordersCount));

                    double total = 0;
                    for(int i=0;i<orderBeanList.size();i++){
                        OrderBean order = orderBeanList.get(i);
                        total += Double.valueOf(order.getFinalPrice());
                    }
                    totalPriceText.setText(" $"+String.format("%.2f",total));

                }else{
                    countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                    bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                    countLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
            }else{
                countLayout = (RelativeLayout)activity.findViewById(R.id.layout_total_cart_count);
                bottomLayout = (RelativeLayout)activity.findViewById(R.id.layout_bottom);
                countLayout.setVisibility(View.GONE);
                bottomLayout.setVisibility(View.GONE);
            }
        }
    }


}
