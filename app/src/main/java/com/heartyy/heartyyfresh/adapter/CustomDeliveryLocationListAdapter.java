package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class CustomDeliveryLocationListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<LocationBean> locationBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;

    public CustomDeliveryLocationListAdapter(android.content.Context context, List<LocationBean> locationBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.locationBeanList = locationBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return locationBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return locationBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return locationBeanList.indexOf(locationBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.locations_list_items, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.location_item_main);
        Global.setFont(root, andBold);
        LocationBean data = locationBeanList.get(position);
        TextView locationTypeText = (TextView) rowView.findViewById(R.id.text_location_type);
        TextView locationPrimaryText = (TextView) rowView.findViewById(R.id.text_location_primary);
        TextView locationNameText = (TextView) rowView.findViewById(R.id.text_location_name);

        locationTypeText.setTypeface(light);

        if(data.getLocationName().length()>0){
            locationTypeText.setText(data.getLocationName());
        }else{
            locationTypeText.setText(data.getLocationType());
        }


        locationNameText.setText(data.getLocationName());


        return rowView;
    }
}
