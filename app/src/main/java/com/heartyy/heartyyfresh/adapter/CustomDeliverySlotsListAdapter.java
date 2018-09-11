package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.bean.TimeIntervalBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 1/29/2016.
 */
public class CustomDeliverySlotsListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<TimeIntervalBean> timeIntervalBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light;

    public CustomDeliverySlotsListAdapter(android.content.Context context, List<TimeIntervalBean> timeIntervalBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.timeIntervalBeanList = timeIntervalBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }

    public void changeSlot(List<TimeIntervalBean> timeIntervalBeanList){
        this.timeIntervalBeanList = timeIntervalBeanList;
        mSelectedItem = 0;
        notifyDataSetChanged();
    }


    public void setSelected(int pos){
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return timeIntervalBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return timeIntervalBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return timeIntervalBeanList.indexOf(timeIntervalBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.delivery_slots_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.delivery_slots_main);
        Global.setFont(root, andBold);
        TimeIntervalBean data = timeIntervalBeanList.get(position);
        RelativeLayout textSlotLayout = (RelativeLayout) rowView.findViewById(R.id.layout_text_slot);
        RelativeLayout imageSlotLayout = (RelativeLayout) rowView.findViewById(R.id.layout_image_slot);
        TextView textSlot = (TextView) rowView.findViewById(R.id.text_slot);
        ImageView imageSlot = (ImageView) rowView.findViewById(R.id.image_slot);
        ImageView leftImage = (ImageView) rowView.findViewById(R.id.image_slot_left);
        ImageView rightImage = (ImageView) rowView.findViewById(R.id.image_slot_right);
        RelativeLayout carLayout = (RelativeLayout) rowView.findViewById(R.id.layout_car);


        String startTime[] = data.getStartTimeLabel().split(" ");
        String endTime = data.getEndTimeLabel();
        textSlot.setText(startTime[0]+"-"+endTime);

        if(position==0){
            leftImage.setVisibility(View.GONE);
        }else{
            leftImage.setVisibility(View.VISIBLE);
        }

        if(position==timeIntervalBeanList.size()-1){
            rightImage.setVisibility(View.GONE);
        }else{
            rightImage.setVisibility(View.VISIBLE);
        }

        if(position==mSelectedItem){
            textSlot.setTextColor(Color.parseColor("#00CC99"));
            textSlot.setPadding(0, 0, 0, 0);
            textSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            carLayout.setVisibility(View.VISIBLE);
        }else{
            textSlot.setTextColor(Color.parseColor("#000000"));
            textSlot.setPadding(0, 12, 0, 0);
            textSlot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            carLayout.setVisibility(View.GONE);
        }


        return rowView;
    }
}
