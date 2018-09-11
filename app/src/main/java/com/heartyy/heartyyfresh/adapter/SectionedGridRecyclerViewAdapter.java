package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.heartyy.heartyyfresh.StoreDetailActivity;
import com.heartyy.heartyyfresh.bean.CategoryAisleBean;
import com.heartyy.heartyyfresh.bean.SubAisleBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SectionedGridRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private int mSectionResourceId;
    private int mTextResourceId;
    private LayoutInflater mLayoutInflater;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<Section>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private StoreDetailActivity activity;
    private SharedPreferences pref;
    private String subCategoryId;


    public SectionedGridRecyclerViewAdapter(Context context, int sectionResourceId, int textResourceId, RecyclerView recyclerView,
                                            RecyclerView.Adapter baseAdapter, RecyclerView.LayoutManager mLayoutManager, StoreDetailActivity activity) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSectionResourceId = sectionResourceId;
        mTextResourceId = textResourceId;
        mBaseAdapter = baseAdapter;
        mContext = context;
        this.activity = activity;
        mRecyclerView = recyclerView;
        this.mLayoutManager = mLayoutManager;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);

        pref = context.getApplicationContext().getSharedPreferences("MyPref",
                context.MODE_PRIVATE);


        mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        final GridLayoutManager layoutManager = (GridLayoutManager) mLayoutManager;
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isSectionHeaderPosition(position)) ? layoutManager.getSpanCount() : 1;
            }
        });
    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title, more, subId, no;
        public ImageView popularImage;
        public RelativeLayout moreLayout;

        public SectionViewHolder(View view, int mTextResourceid) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceid);
            more = (TextView) view.findViewById(R.id.text_more);
            popularImage = (ImageView) view.findViewById(R.id.image_popular);
            moreLayout = (RelativeLayout) view.findViewById(R.id.layout_more);
            subId = (TextView) view.findViewById(R.id.text_subid);
            no = (TextView) view.findViewById(R.id.text_no);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false);
            return new SectionViewHolder(view, mTextResourceId);
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, typeView - 1);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder sectionViewHolder, final int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder) sectionViewHolder).title.setText(mSections.get(position).title);
            ((SectionViewHolder) sectionViewHolder).subId.setText(mSections.get(position).subCategoryId);
            ((SectionViewHolder) sectionViewHolder).no.setText(mSections.get(position).showMore);
            ((SectionViewHolder) sectionViewHolder).title.setTypeface(regular);
            ((SectionViewHolder) sectionViewHolder).more.setTypeface(regular);

            if (((SectionViewHolder) sectionViewHolder).title.getText().equals("Popular")) {
                ((SectionViewHolder) sectionViewHolder).popularImage.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).moreLayout.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).popularImage.setImageResource(R.drawable.popular_trans);
            } else if (((SectionViewHolder) sectionViewHolder).title.getText().equals("From your Previous Orders")) {
                ((SectionViewHolder) sectionViewHolder).popularImage.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).popularImage.setImageResource(R.drawable.previous_icon);
                ((SectionViewHolder) sectionViewHolder).moreLayout.setVisibility(View.VISIBLE);
            } else {
                ((SectionViewHolder) sectionViewHolder).popularImage.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).moreLayout.setVisibility(View.VISIBLE);
            }

            if (((SectionViewHolder) sectionViewHolder).no.getText().toString().equalsIgnoreCase("no")) {
                ((SectionViewHolder) sectionViewHolder).moreLayout.setVisibility(View.GONE);
            }
            ((SectionViewHolder) sectionViewHolder).moreLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("item name..", (String) ((SectionViewHolder) sectionViewHolder).subId.getText());
                    if(mSections.get(position).topCategoryId==null && mSections.get(position).subCategoryId=="-1"){

                    }else {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.CATEGORY, "sub");
                        editor.putString(Constants.SUB_CATEGORY_ID, (String) ((SectionViewHolder) sectionViewHolder).subId.getText());
                        if (mSections.get(position).topCategoryId != null) {
                            editor.putString(Constants.CATEGORY, "top");
                            editor.putString(Constants.TOP_CATEGORY_ID, mSections.get(position).topCategoryId);
                            editor.commit();
                            Global.sort = null;
                        }

                        editor.commit();
                    }
                    requestForAisleAndSupplierStore((String) ((SectionViewHolder) sectionViewHolder).subId.getText(), (String) ((SectionViewHolder) sectionViewHolder).no.getText(), mSections.get(position).topCategoryId);
                }
            });

        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
        }


    }

    private void requestForAisleAndSupplierStore(final String subCategoryId, String showmore, final String topCategoryId1) {

        Global.showProgress(activity);
        String url;
        final String supplierId = pref.getString(Constants.SUPPLIER_ID, null);
        final String topCategoryId = pref.getString(Constants.TOP_CATEGORY_ID, null);

        url = "product";


        RequestQueue rq = Volley.newRequestQueue(mContext);
        JSONObject params = new JSONObject();
        try {
            if(topCategoryId1==null&&subCategoryId=="-1"){
                params.put("popular", "");
            }else {
                if (topCategoryId1 != null) {
                    params.put("top_category_id", topCategoryId1);
                } else {
                    params.put("top_category_id", topCategoryId);
                    params.put("sub_category_id", subCategoryId);
                }
            }
            params.put("supplier_id", supplierId);


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
                                CategoryAisleBean aisleBean = ConversionHelper.convertCategoryAisleJsonToCategoryAisleBean(jsonObject);
                                if (Global.backCount == 0) {
                                    Global.backCount = 1;
                                    Global.backFrom = "";
                                    Global.topAisleBean = aisleBean;
                                } else {
                                    Global.subAisleBean = aisleBean;
                                    Global.backCount = 2;

                                    if (Global.backFrom == null) {
                                        Global.backCount = 1;
                                    } else if (Global.backFrom.isEmpty()) {
                                        Global.backFrom = "top";
                                    } else {
                                        Global.backFrom = "sub";
                                    }
                                }

                                if (!subCategoryId.equalsIgnoreCase("-1")) {

                                    List<SubAisleItemBean> subAisleItemBeanList = null;
                                    SubAisleBean subAisleBean1 = null;
                                    for (int i = 0; i < aisleBean.getTopAisleBean().getSubAisleBeanList().size(); i++) {
                                        if (aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId().equalsIgnoreCase(subCategoryId)) {
                                            subAisleItemBeanList = aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubAisleItemBeanList();
                                            subAisleBean1 = aisleBean.getTopAisleBean().getSubAisleBeanList().get(i);
                                        }
                                    }

                                    SimpleAdapter adapter = new SimpleAdapter(mContext, subAisleItemBeanList, activity);
                                    RecyclerView recyclerView;
                                    RecyclerView.LayoutManager mLayoutManager;
                                    recyclerView = (RecyclerView) activity.findViewById(R.id.cardList);
                                    recyclerView.setHasFixedSize(true);
                                    mLayoutManager = new GridLayoutManager(mContext, 3);

                                    List<Section> sections =
                                            new ArrayList<Section>();
                                    int j = 0;

                                    sections.add(new Section(j, "Showing all items in " + subAisleBean1.getSubCategoryName(), subAisleBean1.getSubCategoryId(), "no", null));


                                    //Add your adapter to the sectionAdapter
                                    Section[] dummy = new Section[sections.size()];
                                    SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                            SectionedGridRecyclerViewAdapter(mContext, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, activity);
                                    mSectionedAdapter.setSections(sections.toArray(dummy));

                                    //Apply this adapter to the RecyclerView
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(mSectionedAdapter);
                                    RelativeLayout categoryLayout = (RelativeLayout) activity.findViewById(R.id.layout_category);
                                    categoryLayout.setVisibility(View.VISIBLE);
                                    TextView topCategoryText = (TextView) activity.findViewById(R.id.text_topcategory);
                                    topCategoryText.setText(subAisleBean1.getSubCategoryName());
                                    requestForBrandandSizeForSubCategory(supplierId, topCategoryId, subCategoryId);
                                } else {
                                    Global.backCount = 1;
                                    Global.backFrom = "";
                                    List<Section> sections =
                                            new ArrayList<Section>();
                                    List<SubAisleItemBean> subAisleItemBeanList = new ArrayList<>();
                                    if (aisleBean.getPopularAisleItemBeanList() != null) {
                                        if (aisleBean.getPopularAisleItemBeanList().size() > 0) {
                                            for (int c = 0; c < aisleBean.getPopularAisleItemBeanList().size(); c++) {
                                                subAisleItemBeanList.add(aisleBean.getPopularAisleItemBeanList().get(c));
                                            }
                                            sections.add(new Section(0, "Popular", "1", "no", null));
                                        }
                                    }

                                    if(aisleBean.getTopAisleBean()!=null) {

                                        for (int j = 0; j < aisleBean.getTopAisleBean().getSubAisleBeanList().size(); j++) {
                                            List<SubAisleItemBean> topCategoryPopularBeanList = aisleBean.getTopAisleBean().getSubAisleBeanList().get(j).getSubAisleItemBeanList();
                                            if (topCategoryPopularBeanList != null) {
                                                for (int c = 0; c < topCategoryPopularBeanList.size(); c++) {
                                                    subAisleItemBeanList.add(topCategoryPopularBeanList.get(c));
                                                }
                                            }
                                        }
                                    }

                                    SimpleAdapter adapter = new SimpleAdapter(mContext, subAisleItemBeanList, activity);
                                    RecyclerView recyclerView;
                                    RecyclerView.LayoutManager mLayoutManager;
                                    recyclerView = (RecyclerView) activity.findViewById(R.id.cardList);
                                    recyclerView.setHasFixedSize(true);
                                    mLayoutManager = new GridLayoutManager(mContext, 3);
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

                                    if(aisleBean.getTopAisleBean()!=null) {

                                        for (int i = 0; i < aisleBean.getTopAisleBean().getSubAisleBeanList().size(); i++) {
                                            List<SubAisleItemBean> itemsList = aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubAisleItemBeanList();
                                            if (itemsList != null) {
                                                count += itemsList.size();

                                                sections.add(new Section(j, aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryName(), aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getSubCategoryId(), aisleBean.getTopAisleBean().getSubAisleBeanList().get(i).getIsMore(), null));
                                                j = count;
                                            }
                                        }
                                    }

                                    Section[] dummy = new Section[sections.size()];
                                    SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                            SectionedGridRecyclerViewAdapter(mContext, R.layout.section, R.id.section_text, recyclerView, adapter, mLayoutManager, activity);
                                    mSectionedAdapter.setSections(sections.toArray(dummy));

                                    //Apply this adapter to the RecyclerView
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(mSectionedAdapter);
                                    if(subCategoryId=="-1"&&topCategoryId1==null){
                                        Global.dialog.dismiss();
                                    }else {
                                        RelativeLayout categoryLayout = (RelativeLayout) activity.findViewById(R.id.layout_category);
                                        categoryLayout.setVisibility(View.VISIBLE);
                                        TextView topCategoryText = (TextView) activity.findViewById(R.id.text_topcategory);
                                        topCategoryText.setText(aisleBean.getTopAisleBean().getTopCategoryName());
                                        ImageView indicatoinImg = (ImageView) activity.findViewById(R.id.img_sort_indication);
                                        indicatoinImg.setImageResource(R.drawable.indication_dark_circle);
                                        ImageView refineImg = (ImageView) activity.findViewById(R.id.img_refine_indication);

                                        refineImg.setImageResource(R.drawable.indication_dark_circle);
                                        requestForBrandandSizeForSubCategory(supplierId, topCategoryId1, null);
                                    }
                                }


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();

                                //  showAlert(jsonObject.getString("message"));
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

    private void requestForBrandandSizeForSubCategory(String supplierId, String topCategoryId, String subCategoryId) {
        String url;
        if (subCategoryId == null) {
            url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&for=topcategory";
        } else {
            url = "product/filter?supplier_id=" + supplierId + "&top_category_id=" + topCategoryId + "&sub_category_id=" + subCategoryId + "&for=subcategory";
        }
        RequestQueue rq = Volley.newRequestQueue(mContext);

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
                                ImageView sortImage = (ImageView) activity.findViewById(R.id.image_sort_icon);
                                sortImage.setImageResource(R.drawable.sort_icon);
                                ImageView refineImage = (ImageView) activity.findViewById(R.id.image_refine_icon);
                                refineImage.setImageResource(R.drawable.refine_icon);

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

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }


    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;
        String subCategoryId, showMore, topCategoryId;

        public Section(int firstPosition, CharSequence title, String subCategoryId, String showMore, String topCategoryId) {
            this.firstPosition = firstPosition;
            this.title = title;
            this.subCategoryId = subCategoryId;
            this.showMore = showMore;
            this.topCategoryId = topCategoryId;

        }

        public CharSequence getTitle() {
            return title;
        }

        public String getSubCategoryId() {
            return subCategoryId;
        }

        public void setSubCategoryId(String subCategoryId) {
            this.subCategoryId = subCategoryId;
        }
    }


    public void setSections(Section[] sections) {
        mSections.clear();

        Arrays.sort(sections, new Comparator<Section>() {
            @Override
            public int compare(Section o, Section o1) {
                return (o.firstPosition == o1.firstPosition)
                        ? 0
                        : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            mSections.append(section.sectionedPosition, section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).firstPosition > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }


    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSections.indexOfKey(position)
                : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(activity);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
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
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}