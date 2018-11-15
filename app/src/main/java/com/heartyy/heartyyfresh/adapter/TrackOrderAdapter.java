package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderTrackBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by amitvashist1 on 02/02/16.
 */
public class TrackOrderAdapter extends BaseAdapter {
    private Context context;
    private Typeface regular, meduimItalic, medium, robotoLight;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<OrderTrackBean> orderTrackBeansList;


    public TrackOrderAdapter(Context context, List<OrderTrackBean> orderTrackBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.orderTrackBeansList = orderTrackBeanList;

        robotoLight = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_MEDIUM);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderTrackBeansList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return orderTrackBeansList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return orderTrackBeansList.indexOf(orderTrackBeansList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.track_order_item_list, viewGroup, false);
        OrderTrackBean data = orderTrackBeansList.get(position);
        TextView txtTrackTitle = (TextView) rowView.findViewById(R.id.text_track_title);
        TextView txtTrackMessage = (TextView) rowView.findViewById(R.id.text_track_detail);
        TextView txtStoreName = (TextView) rowView.findViewById(R.id.store_name);
        ImageView imageStatus = (ImageView) rowView.findViewById(R.id.image_circular_status);
        ImageView imageTrackStaus = (ImageView) rowView.findViewById(R.id.image_track_icon);
        TextView textToDeliver = (TextView) rowView.findViewById(R.id.text_to_deliver);
        TextView dateText = (TextView) rowView.findViewById(R.id.text_date);
        TextView byText = (TextView) rowView.findViewById(R.id.text_by);
        TextView timeText = (TextView) rowView.findViewById(R.id.text_time);


        txtTrackTitle.setText(data.getTitle());
        txtTrackMessage.setText(data.getMessage());
        txtStoreName.setText(data.getSupplierName());
        txtTrackTitle.setTypeface(regular);
        txtTrackMessage.setTypeface(robotoLight);
        textToDeliver.setTypeface(robotoLight);
        dateText.setTypeface(regular);
        byText.setTypeface(robotoLight);
        timeText.setTypeface(regular);

        dateText.setText(data.getDeliveryDate());
        timeText.setText(data.getDeliveryTime());



        String status = data.getStatus();
        if (status.equalsIgnoreCase("1")) {
            imageStatus.setImageResource(R.drawable.circle_1);
            imageTrackStaus.setImageResource(R.drawable.track_1);
        } else if (status.equalsIgnoreCase("2")) {
            imageStatus.setImageResource(R.drawable.circle_2);
            imageTrackStaus.setImageResource(R.drawable.track_2);
        } else if (status.equalsIgnoreCase("3")) {
            imageStatus.setImageResource(R.drawable.circle_3);
            imageTrackStaus.setImageResource(R.drawable.track_3);
        } else if (status.equalsIgnoreCase("4")) {
            imageStatus.setImageResource(R.drawable.circle_4);
            imageTrackStaus.setImageResource(R.drawable.track_4);
        }

        return rowView;
    }
}