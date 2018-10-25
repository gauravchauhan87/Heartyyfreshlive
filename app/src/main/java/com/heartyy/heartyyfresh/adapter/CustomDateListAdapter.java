package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 1/26/2016.
 */
public class CustomDateListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<OperatingHourBean> operatingHourBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light;

    public CustomDateListAdapter(android.content.Context context, List<OperatingHourBean> operatingHourBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.operatingHourBeanList = operatingHourBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }

    public void setList(List<OperatingHourBean> operatingHourBeanList){
        this.operatingHourBeanList = operatingHourBeanList;
        notifyDataSetChanged();
    }

    public void setSelected(int pos){
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return operatingHourBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return operatingHourBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return operatingHourBeanList.indexOf(operatingHourBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.date_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.date_item_main);
        Global.setFont(root, andBold);
        OperatingHourBean data = operatingHourBeanList.get(position);
        TextView day = (TextView) rowView.findViewById(R.id.text_day);
        TextView date = (TextView) rowView.findViewById(R.id.text_date);
        ImageView line = (ImageView) rowView.findViewById(R.id.line);

        day.setText(data.getDayOfWeek());
        String fullDate[] = data.getDate().split("-");
        date.setText(fullDate[2]);
        if(data.getIsClose().equalsIgnoreCase("yes")){
            root.setBackgroundResource(R.color.profile);
            line.setVisibility(View.VISIBLE);
            day.setTextColor(Color.parseColor("#000000"));
            date.setTextColor(Color.parseColor("#000000"));

        }else{
            root.setBackgroundResource(R.color.white);
            line.setVisibility(View.GONE);
            day.setTextColor(Color.parseColor("#000000"));
            date.setTextColor(Color.parseColor("#000000"));
        }
        if(mSelectedItem==position){
            root.setBackgroundResource(R.drawable.select_date_icon);
            day.setTextColor(Color.parseColor("#FFFFFF"));
            date.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            root.setBackgroundResource(R.color.White);
            day.setTextColor(Color.parseColor("#000000"));
            date.setTextColor(Color.parseColor("#000000"));
        }




        return rowView;
    }
}
