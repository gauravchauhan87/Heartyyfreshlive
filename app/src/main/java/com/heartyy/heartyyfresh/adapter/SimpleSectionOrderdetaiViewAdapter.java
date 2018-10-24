package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
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

/**
 * Created by Dheeraj on 2/2/2016.
 */
public class SimpleSectionOrderdetaiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private static final int SECTION_TYPE = 0;

    private boolean mValid = true;
    private int mSectionResourceId;
    private int mTextResourceId;
    private LayoutInflater mLayoutInflater;
    private RecyclerView.Adapter mBaseAdapter;
    private SparseArray<Section> mSections = new SparseArray<Section>();
    private Typeface regular, meduimItalic, medium,robotoLight;
    private SharedPreferences pref;
    private OrderDetailActivity activity;
    String supplierId;



    public SimpleSectionOrderdetaiViewAdapter(Context context, int sectionResourceId, int textResourceId,
                                              RecyclerView.Adapter baseAdapter, OrderDetailActivity activity) {

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

    public void changeadapter(OrderDetailAdapter adapter) {
        mBaseAdapter = adapter;
        notifyDataSetChanged();
    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        public TextView title,more,saveAlltext,dateText,timeText,deliverOn;
        public RelativeLayout saveAllayout,layoutReorder;
        public ImageView likeImage;
        public Button cancelOrderBtn;

        public SectionViewHolder(View view,int mTextResourceid) {
            super(view);
            title = (TextView) view.findViewById(mTextResourceid);
            layoutReorder = (RelativeLayout) view.findViewById(R.id.layout_reorder);
            saveAlltext = (TextView) view.findViewById(R.id.text_save_all);
            likeImage = (ImageView) view.findViewById(R.id.image_like);
            saveAllayout = (RelativeLayout) view.findViewById(R.id.layout_save_all);
            dateText = (TextView) view.findViewById(R.id.text_date);
            timeText = (TextView) view.findViewById(R.id.text_time);
            cancelOrderBtn = (Button) view.findViewById(R.id.button_cancel_order);
            deliverOn = (TextView) view.findViewById(R.id.text_deliver_on);


        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            final View view = LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false);
            return new SectionViewHolder(view,mTextResourceId);
        }else{
            return mBaseAdapter.onCreateViewHolder(parent, typeView -1);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder sectionViewHolder, final int position) {
        if (isSectionHeaderPosition(position)) {
            ((SectionViewHolder)sectionViewHolder).title.setText(mSections.get(position).title);
            ((SectionViewHolder)sectionViewHolder).title.setTypeface(regular);

            ((SectionViewHolder)sectionViewHolder).layoutReorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.reOrderAll(mSections.get(position).suppierId);
                }
            });

            if(mSections.get(position).allSave.equalsIgnoreCase("yes")){
                ((SectionViewHolder)sectionViewHolder).saveAlltext.setText("Saved");
                ((SectionViewHolder)sectionViewHolder).likeImage.setImageResource(R.drawable.like_icon);

            }else{
                ((SectionViewHolder)sectionViewHolder).saveAlltext.setText("Save all Items");
                ((SectionViewHolder)sectionViewHolder).likeImage.setImageResource(R.drawable.unlike_icon);
            }

            ((SectionViewHolder)sectionViewHolder).saveAllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveAllItems(mSections.get(position).suppItemIdStringList,(SectionViewHolder)sectionViewHolder);
                }
            });

            ((SectionViewHolder)sectionViewHolder).dateText.setTypeface(regular);
            ((SectionViewHolder)sectionViewHolder).timeText.setTypeface(regular);
            ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setTypeface(robotoLight);
            ((SectionViewHolder)sectionViewHolder).deliverOn.setTypeface(robotoLight);
            ((SectionViewHolder)sectionViewHolder).dateText.setText(mSections.get(position).date);
            ((SectionViewHolder)sectionViewHolder).timeText.setText(mSections.get(position).time);


            if(mSections.get(position).deliveryStatus.equalsIgnoreCase("TOBEDELIVER")){
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setVisibility(View.VISIBLE);
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setEnabled(true);
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setText("Cancel Order");
                if(mSections.get(position).isOrderCancellable.equalsIgnoreCase("false")){
                    ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setVisibility(View.INVISIBLE);
                }else{
                    ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setVisibility(View.VISIBLE);
                }
            }else{
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setText(mSections.get(position).displayDeliveryStatus);
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setEnabled(false);
                ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setVisibility(View.VISIBLE);
            }

              if(mSections.get(position).deliveryStatus.equalsIgnoreCase("DELIVERED")){
                ((SectionViewHolder)sectionViewHolder).deliverOn.setText("Delivered on");
            }else{
                  ((SectionViewHolder)sectionViewHolder).deliverOn.setText("To deliver on");
              }

            ((SectionViewHolder)sectionViewHolder).cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                activity.cancelOrderConfirmation(mSections.get(position).orderSupplierId);
                }
            });

        }else{
            mBaseAdapter.onBindViewHolder(sectionViewHolder,sectionedPositionToPosition(position));
        }
    }

    private void saveAllItems(final List<String> suppItemIdStringList, SectionViewHolder sectionViewHolder) {
        final DatabaseHandler db = new DatabaseHandler(mContext);

        String userId = pref.getString(Constants.USER_ID, null);
        if (userId == null) {
            ((SectionViewHolder)sectionViewHolder).likeImage.setBackgroundResource(R.drawable.unlike_icon);
        } else {
            String url;


            url = "user/saved";
            JSONObject params = new JSONObject();
            try {

                params.put("user_id", userId);
                JSONArray supplierArray = new JSONArray();
                for(int i=0;i<suppItemIdStringList.size();i++){
                    String supplierId = suppItemIdStringList.get(i);
                    String str = db.getLikeItem(supplierId);

                    if(str==null){
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
                                    for(int i=0;i<activity.finalOrderBeanList.size();i++){
                                        OrderBean orderBean = activity.finalOrderBeanList.get(i);
                                        for(int j=0;j<suppItemIdStringList.size();j++){
                                            String id = suppItemIdStringList.get(j);
                                            if(orderBean.getSupplierItemId().equalsIgnoreCase(id)){
                                                activity.finalOrderBeanList.get(i).setIsSave("YEs");
                                            }
                                        }

                                    }
                                    activity.refreshadapter();



                                } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                    // holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
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
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) +1 ;
    }

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;
        String type,suppierId;
        String itemCount;
        String itemPrice;
        String allSave;
        List<String> suppItemIdStringList;
        String date,time,orderSupplierId,deliveryStatus,isOrderCancellable,displayDeliveryStatus;


        public Section(int firstPosition, CharSequence title, String suppId,String allSave,List<String> suppItemIdStringList,String date,String time,
                       String orderSupplierId,String deliveryStatus,String deliveryStatusDisplay,String isOrderCancellable) {
            this.firstPosition = firstPosition;
            this.title = title;
            this.suppierId = suppId;
            this.allSave = allSave;
            this.suppItemIdStringList = suppItemIdStringList;
            this.date = date;
            this.time = time;
            this.orderSupplierId = orderSupplierId;
            this.deliveryStatus = deliveryStatus;
            this.isOrderCancellable = isOrderCancellable;
            this.displayDeliveryStatus = deliveryStatusDisplay;
        }

        public CharSequence getTitle() {
            return title;
        }
        public String getId(){return suppierId;}

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
