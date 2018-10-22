package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.PastOrderSupplierBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/7/2015.
 */
public class CustomPastStoresListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<PastOrderSupplierBean> pastOrderSupplierBeansList;
    private boolean isClosed = false;

    public CustomPastStoresListAdapter(Context context, List<PastOrderSupplierBean> pastStoresBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.pastOrderSupplierBeansList = pastStoresBeanList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pastOrderSupplierBeansList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pastOrderSupplierBeansList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return pastOrderSupplierBeansList.indexOf(pastOrderSupplierBeansList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.past_stores_list_items, parent, false);
        Typeface andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

        PastOrderSupplierBean data = pastOrderSupplierBeansList.get(position);

        TextView textDate = (TextView) rowView.findViewById(R.id.text_date);
        textDate.setTypeface(robotoLight);
        TextView textTime = (TextView) rowView.findViewById(R.id.text_time);
        textTime.setTypeface(robotoLight);
        TextView supplierText = (TextView)rowView.findViewById(R.id.text_stores);
        TextView deliveryStatus = (TextView) rowView.findViewById(R.id.text_delivery_status);
        deliveryStatus.setTypeface(robotoLight);
        supplierText.setTypeface(andBold);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;



        textDate.setText(data.getDeliveryDate());
        textTime.setText(data.getDeliveryTime());
        supplierText.setText(data.getSupplierName());


        if(data.getDeliveryStatus().equalsIgnoreCase("cancelled")){
            ratingBar.setVisibility(View.GONE);
            deliveryStatus.setVisibility(View.VISIBLE);
            deliveryStatus.setText("Cancelled");
        }

        if(data.getSupplierRating().equalsIgnoreCase("null")){
            ratingBar.setRating(0.0f);
        }else{
            ratingBar.setRating((float) Integer.parseInt(data.getSupplierRating()));
        }
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context,R.color.hearty_star), mMode);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context,R.color.hearty_star), mMode);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context,R.color.edit_line_zip), mMode);

        if(data.getDeliveryStatus().equalsIgnoreCase("cancelled")){
            ratingBar.setVisibility(View.GONE);
        }else{
            ratingBar.setVisibility(View.VISIBLE);
        }

        return rowView;
    }
}
