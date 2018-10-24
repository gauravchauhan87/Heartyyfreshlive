package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AnalyticsEvents;
import com.heartyy.heartyyfresh.CheckoutActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SimpleSectionedRecyclerViewAdapter extends Adapter<ViewHolder> {
    private static final int SECTION_TYPE = 0;
    private final Context mContext;
    private CheckoutActivity activity;
    private Adapter mBaseAdapter;
    private LayoutInflater mLayoutInflater;
    private int mSectionResourceId;
    private SparseArray<Section> mSections = new SparseArray();
    private int mTextResourceId;
    private boolean mValid = true;
    private Typeface medium;
    private Typeface meduimItalic;
    private int pos = -1;
    private SharedPreferences pref;
    private Typeface regular;
    private Typeface robotoLight;
    private double value;

    public SimpleSectionedRecyclerViewAdapter(Context context, int sectionResourceId, int textResourceId, Adapter baseAdapter, CheckoutActivity activity) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSectionResourceId = sectionResourceId;
        this.mTextResourceId = textResourceId;
        this.mBaseAdapter = baseAdapter;
        this.mContext = context;
        this.robotoLight = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_LIGHT);
        this.regular = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_REGULAR);
        this.meduimItalic = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_MEDIUM_ITALIC);
        this.medium = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_MEDIUM);
        Context context2 = this.mContext;
        this.pref = this.mContext.getApplicationContext().getSharedPreferences("MyPref", 0);
        this.activity = activity;
        this.mBaseAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                SimpleSectionedRecyclerViewAdapter.this.mValid = SimpleSectionedRecyclerViewAdapter.this.mBaseAdapter.getItemCount() > 0;
                SimpleSectionedRecyclerViewAdapter.this.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                SimpleSectionedRecyclerViewAdapter.this.mValid = SimpleSectionedRecyclerViewAdapter.this.mBaseAdapter.getItemCount() > 0;
                SimpleSectionedRecyclerViewAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                SimpleSectionedRecyclerViewAdapter.this.mValid = SimpleSectionedRecyclerViewAdapter.this.mBaseAdapter.getItemCount() > 0;
                SimpleSectionedRecyclerViewAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                SimpleSectionedRecyclerViewAdapter.this.mValid = SimpleSectionedRecyclerViewAdapter.this.mBaseAdapter.getItemCount() > 0;
                SimpleSectionedRecyclerViewAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    public void setNewValue(int pos, double value) {
        this.pos = pos;
        this.value = value;
        notifyDataSetChanged();
    }

    public void changeadapter(Adapter baseAdapter) {
        this.mBaseAdapter = baseAdapter;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == 0) {
            return new SectionViewHolder(LayoutInflater.from(this.mContext).inflate(this.mSectionResourceId, parent, false), this.mTextResourceId);
        }
        return this.mBaseAdapter.onCreateViewHolder(parent, typeView - 1);
    }

    public void onBindViewHolder(final ViewHolder sectionViewHolder, final int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder) sectionViewHolder).title.setText(((Section) this.mSections.get(position)).title);
            DatabaseHandler db = new DatabaseHandler(this.mContext);
            List<OrderBean> orderBeanList = db.getAllOrders(db.getSupplier(((Section) this.mSections.get(position)).supplierId));
            double totalPrice = 0.0d;
            for (int c = 0; c < orderBeanList.size(); c++) {
                totalPrice += Double.parseDouble(((OrderBean) orderBeanList.get(c)).getFinalPrice());
            }
            if (((Section) this.mSections.get(position)).type.equalsIgnoreCase(Constants.CATEGORY)) {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.GONE);
                if (((Section) this.mSections.get(position)).discountedPrice != null) {
                    totalPrice -= Double.parseDouble(((Section) this.mSections.get(position)).discountedPrice);
                }
            } else if (((Section) this.mSections.get(position)).type.equalsIgnoreCase("minimum")) {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumText.setText(((Section) this.mSections.get(position)).title);
            } else {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.VISIBLE);
                if (((Section) this.mSections.get(position)).allSave.equalsIgnoreCase("yes")) {
                    ((SectionViewHolder) sectionViewHolder).likeImage.setImageResource(R.drawable.like_icon);
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Saved");
                } else {
                    ((SectionViewHolder) sectionViewHolder).likeImage.setImageResource(R.drawable.unlike_icon);
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Saved");
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Save all items");
                }
            }
            ((SectionViewHolder) sectionViewHolder).countText.setText(String.valueOf(orderBeanList.size()) + " items");
            String[] temp = String.format("%.2f", new Object[]{Double.valueOf(totalPrice)}).split("\\.");
            if (temp.length > 1) {
                ((SectionViewHolder) sectionViewHolder).priceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
            } else {
                ((SectionViewHolder) sectionViewHolder).priceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + AppEventsConstants.EVENT_PARAM_VALUE_NO + "</sup>"));
            }
            ((SectionViewHolder) sectionViewHolder).title.setText(((Section) this.mSections.get(position)).title);
            ((SectionViewHolder) sectionViewHolder).saveAlllayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SimpleSectionedRecyclerViewAdapter.this.saveAllItems(((Section) SimpleSectionedRecyclerViewAdapter.this.mSections.get(position)).suppItemIdList, (SectionViewHolder) sectionViewHolder);
                }
            });
            return;
        }
        this.mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
    }

    private void saveAllItems(List<String> suppItemIdList, SectionViewHolder sectionViewHolder) {
        DatabaseHandler db = new DatabaseHandler(this.mContext);
        String userId = this.pref.getString(Constants.USER_ID, null);
        if (userId == null) {
            sectionViewHolder.likeImage.setBackgroundResource(R.drawable.unlike_icon);
            return;
        }
        String url = "user/saved";
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.USER_ID, userId);
            JSONArray supplierArray = new JSONArray();
            for (int i = 0; i < suppItemIdList.size(); i++) {
                String supplierId = (String) suppItemIdList.get(i);
                if (db.getLikeItem(supplierId) == null) {
                    db.addLikeItem(supplierId);
                    supplierArray.put(supplierId);
                }
            }
            params.put("supplier_item_id", supplierArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());
        Volley.newRequestQueue(this.mContext).add(new JsonObjectRequest(1, Constants.URL + url, params, new Listener<JSONObject>() {
            public void onResponse(JSONObject jsonObject) {
                Log.d("response", jsonObject.toString());
                try {
                    String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                    if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                        SimpleSectionedRecyclerViewAdapter.this.activity.checkStorePromotion("adapter");
                    } else if (!status.equalsIgnoreCase(Constants.ERROR)) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                   Global.hideProgress();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d(Constants.ERROR, "Error: " + error.toString());
               Global.hideProgress();
                if (!(error instanceof NoConnectionError)) {
                }
            }
        }));
    }

    public int getItemViewType(int position) {
        if (isSectionHeaderPosition(position)) {
            return 0;
        }
        return this.mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    public void setSections(Section[] sections) {
        this.mSections.clear();
        Arrays.sort(sections, new Comparator<Section>() {
            public int compare(Section o, Section o1) {
                if (o.firstPosition == o1.firstPosition) {
                    return 0;
                }
                return o.firstPosition < o1.firstPosition ? -1 : 1;
            }
        });
        int offset = 0;
        for (Section section : sections) {
            section.sectionedPosition = section.firstPosition + offset;
            this.mSections.append(section.sectionedPosition, section);
            offset++;
        }
        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        int i = 0;
        while (i < this.mSections.size() && ((Section) this.mSections.valueAt(i)).firstPosition <= position) {
            offset++;
            i++;
        }
        return position + offset;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return -1;
        }
        int offset = 0;
        int i = 0;
        while (i < this.mSections.size() && ((Section) this.mSections.valueAt(i)).sectionedPosition <= sectionedPosition) {
            offset--;
            i++;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return this.mSections.get(position) != null;
    }

    public long getItemId(int position) {
        if (isSectionHeaderPosition(position)) {
            return (long) (Integer.MAX_VALUE - this.mSections.indexOfKey(position));
        }
        return this.mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    public int getItemCount() {
        return this.mValid ? this.mBaseAdapter.getItemCount() + this.mSections.size() : 0;
    }

    public static class Section {
        String allSave;
        String discountedPrice;
        int firstPosition;
        String itemCount = this.itemCount;
        String itemPrice = this.itemPrice;
        int sectionedPosition;
        List<String> suppItemIdList;
        String supplierId;
        CharSequence title;
        String type;

        public Section(int firstPosition, CharSequence title, String type, String allSave, List<String> suppItemIdList, String supplierId, String discountedPrice) {
            this.firstPosition = firstPosition;
            this.title = title;
            this.type = type;
            this.allSave = allSave;
            this.suppItemIdList = suppItemIdList;
            this.supplierId = supplierId;
            this.discountedPrice = discountedPrice;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public String getType() {
            return this.type;
        }
    }

    public static class SectionViewHolder extends ViewHolder {
        public TextView countText;
        public ImageView likeImage;
        public RelativeLayout mainlayout;
        public RelativeLayout minimumLayout;
        public TextView minimumText;
        public TextView more;
        public TextView priceText;
        public TextView saveAllItemsText;
        public RelativeLayout saveAlllayout;
        public TextView title;

        public SectionViewHolder(View view, int mTextResourceid) {
            super(view);
            this.title = (TextView) view.findViewById(mTextResourceid);
            this.more = (TextView) view.findViewById(R.id.text_more);
            this.mainlayout = (RelativeLayout) view.findViewById(R.id.layout_main);
            this.minimumLayout = (RelativeLayout) view.findViewById(R.id.layout_minimum);
            this.saveAlllayout = (RelativeLayout) view.findViewById(R.id.layout_save_all);
            this.countText = (TextView) view.findViewById(R.id.text_items_count);
            this.priceText = (TextView) view.findViewById(R.id.text_items_price);
            this.minimumText = (TextView) view.findViewById(R.id.text_minimum);
            this.likeImage = (ImageView) view.findViewById(R.id.image_like);
            this.saveAllItemsText = (TextView) view.findViewById(R.id.text_save_all);
        }
    }
}