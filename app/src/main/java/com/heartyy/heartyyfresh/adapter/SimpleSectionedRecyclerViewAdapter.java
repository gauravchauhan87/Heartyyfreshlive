package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.CheckoutActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SimpleSectionedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private int mSectionResourceId;
    private int mTextResourceId;
    private LayoutInflater mLayoutInflater;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<Section>();
    private Typeface regular, meduimItalic, medium, robotoLight;
    private int pos = -1;
    private double value;
    private SharedPreferences pref;
    private CheckoutActivity activity;


    public SimpleSectionedRecyclerViewAdapter(Context context, int sectionResourceId, int textResourceId,
                                              RecyclerView.Adapter baseAdapter, CheckoutActivity activity) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSectionResourceId = sectionResourceId;
        mTextResourceId = textResourceId;
        mBaseAdapter = baseAdapter;
        mContext = context;

        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);
        pref = mContext.getApplicationContext().getSharedPreferences("MyPref",
                mContext.MODE_PRIVATE);
        this.activity = activity;

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
    }

    public void setNewValue(int pos, double value) {
        this.pos = pos;
        this.value = value;
        notifyDataSetChanged();
    }

    public void changeadapter(RecyclerView.Adapter baseAdapter) {
        mBaseAdapter = baseAdapter;
        notifyDataSetChanged();

    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title, more, countText, priceText, minimumText, saveAllItemsText;
        public RelativeLayout mainlayout, minimumLayout, saveAlllayout;
        public ImageView likeImage;

        public SectionViewHolder(View view, int mTextResourceid) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceid);
            more = (TextView) view.findViewById(R.id.text_more);
            mainlayout = (RelativeLayout) view.findViewById(R.id.layout_main);
            minimumLayout = (RelativeLayout) view.findViewById(R.id.layout_minimum);
            saveAlllayout = (RelativeLayout) view.findViewById(R.id.layout_save_all);
            countText = (TextView) view.findViewById(R.id.text_items_count);
            priceText = (TextView) view.findViewById(R.id.text_items_price);
            minimumText = (TextView) view.findViewById(R.id.text_minimum);
            likeImage = (ImageView) view.findViewById(R.id.image_like);
            saveAllItemsText = (TextView) view.findViewById(R.id.text_save_all);

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

            DatabaseHandler db = new DatabaseHandler(mContext);
            SuppliersBean suppliersBean = db.getSupplier(mSections.get(position).supplierId);
            List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
            double totalPrice = 0;
            for (int c = 0; c < orderBeanList.size(); c++) {
                double price = Double.parseDouble(orderBeanList.get(c).getFinalPrice());
                totalPrice += price;
            }



            if (mSections.get(position).type.equalsIgnoreCase("category")) {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.GONE);
                if (mSections.get(position).discountedPrice != null) {
                    totalPrice -= Double.parseDouble(mSections.get(position).discountedPrice);
                }

            } else if (mSections.get(position).type.equalsIgnoreCase("minimum")) {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.VISIBLE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumText.setText(mSections.get(position).title);


            } else {
                ((SectionViewHolder) sectionViewHolder).mainlayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).minimumLayout.setVisibility(View.GONE);
                ((SectionViewHolder) sectionViewHolder).saveAlllayout.setVisibility(View.VISIBLE);
                if (mSections.get(position).allSave.equalsIgnoreCase("yes")) {
                    ((SectionViewHolder) sectionViewHolder).likeImage.setImageResource(R.drawable.like_icon);
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Saved");

                } else {
                    ((SectionViewHolder) sectionViewHolder).likeImage.setImageResource(R.drawable.unlike_icon);
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Saved");
                    ((SectionViewHolder) sectionViewHolder).saveAllItemsText.setText("Save all items");
                }

            }

            ((SectionViewHolder) sectionViewHolder).countText.setText(String.valueOf(orderBeanList.size()) + " items");
            String[] temp;

            temp = String.format("%.2f", totalPrice).split("\\.");

            if (temp.length > 1) {
                ((SectionViewHolder) sectionViewHolder).priceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
            } else {
                ((SectionViewHolder) sectionViewHolder).priceText.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + "0" + "</sup>"));
            }
            ((SectionViewHolder) sectionViewHolder).title.setText(mSections.get(position).title);

            ((SectionViewHolder) sectionViewHolder).saveAlllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveAllItems(mSections.get(position).suppItemIdList, (SectionViewHolder) sectionViewHolder);
                }
            });
        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
        }


    }

    private void saveAllItems(final List<String> suppItemIdList, SectionViewHolder sectionViewHolder) {
        final DatabaseHandler db = new DatabaseHandler(mContext);

        String userId = pref.getString(Constants.USER_ID, null);
        if (userId == null) {
            ((SectionViewHolder) sectionViewHolder).likeImage.setBackgroundResource(R.drawable.unlike_icon);
        } else {
            String url;


            url = "user/saved";
            JSONObject params = new JSONObject();
            try {

                params.put("user_id", userId);
                JSONArray supplierArray = new JSONArray();
                for (int i = 0; i < suppItemIdList.size(); i++) {
                    String supplierId = suppItemIdList.get(i);
                    String str = db.getLikeItem(supplierId);

                    if (str == null) {
                        db.addLikeItem(supplierId);
                        supplierArray.put(supplierId);
                    }

                }

                params.put("supplier_item_id", supplierArray);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("params---", params.toString());


            RequestQueue rq = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("response", jsonObject.toString());

                            try {
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                 /*
                                    for(int i=0;i<suppItemIdList.size();i++){
                                        String suppId = suppItemIdList.get(i);
                                        String save = db.getLikeItem(suppId);
                                        if (save == null) {
                                            db.addLikeItem(suppId);

                                        } else {
                                            db.deleteLikeItem(suppId);
                                        }
                                    }*/
                                    activity.checkStorePromotion("adapter");


                                } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                    // holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
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
        String type;
        String itemCount;
        String itemPrice;
        String allSave, supplierId;
        List<String> suppItemIdList;
        String discountedPrice;


        public Section(int firstPosition, CharSequence title, String type, String allSave, List<String> suppItemIdList, String supplierId, String discountedPrice) {
            this.firstPosition = firstPosition;
            this.title = title;
            this.type = type;
            this.itemCount = itemCount;
            this.itemPrice = itemPrice;
            this.allSave = allSave;
            this.suppItemIdList = suppItemIdList;
            this.supplierId = supplierId;
            this.discountedPrice = discountedPrice;
        }

        public CharSequence getTitle() {
            return title;
        }

        public String getType() {
            return type;
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

}