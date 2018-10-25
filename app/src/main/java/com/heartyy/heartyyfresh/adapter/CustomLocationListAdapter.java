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
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class CustomLocationListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<LocationBean> locationBeanList;
    private boolean isClosed = false;

    Typeface andBold, bold, italic, light;

    public CustomLocationListAdapter(android.content.Context context, List<LocationBean> locationBeanList) {
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

        LocationBean data = locationBeanList.get(position);
        TextView locationTypeText = (TextView) rowView.findViewById(R.id.text_location_type);
        TextView locationPrimaryText = (TextView) rowView.findViewById(R.id.text_location_primary);
        TextView locationNameText = (TextView) rowView.findViewById(R.id.text_location_name);
        TextView locationNameText2 = (TextView) rowView.findViewById(R.id.text_location_name2);
        ImageView locationImage = (ImageView) rowView.findViewById(R.id.image_location);
        ImageView view1 = (ImageView) rowView.findViewById(R.id.view1);

        locationTypeText.setTypeface(light);
        locationPrimaryText.setTypeface(andBold);
        locationNameText.setTypeface(light);
        locationNameText2.setTypeface(light);


        if (data.getIsPrimaryLocation().equalsIgnoreCase("YES")) {
            locationPrimaryText.setText("Primary");
            view1.setVisibility(View.VISIBLE);
        } else {
            locationPrimaryText.setText("");
            view1.setVisibility(View.GONE);
        }
        if (data.getLocationName().length() > 0) {
            locationTypeText.setText(data.getLocationName());
            if (data.getLocationType().equalsIgnoreCase("home")) {
                locationImage.setImageResource(R.drawable.home_icon);
            } else {
                locationImage.setImageResource(R.drawable.work_icon);
            }
        } else if (data.getLocationType().equalsIgnoreCase("home")) {
            locationTypeText.setText("Home");
            locationImage.setImageResource(R.drawable.home_icon);
        } else {
            locationTypeText.setText("Work");
            locationImage.setImageResource(R.drawable.work_icon);
        }

        String[] address = new String[3];
        String[] address2 = new String[3];
        address[0] = data.getAddress1();
        address[1] = data.getAddress2();
        if (data.getAptSuitUnit().length() > 0) {
            if (data.getAptSuitUnit().contains("#")) {
                data.setAptSuitUnit(data.getAptSuitUnit());
            } else {

                data.setAptSuitUnit("#" + data.getAptSuitUnit());
            }
        }
        address[2] = data.getAptSuitUnit();
        address2[0] = data.getCity();
        address2[1] = data.getState();
        address2[2] = data.getZipcode();
        //address2[3] = data.getZipcode();
        locationNameText.setText(Global.FormatAddress(address) + ",");
        locationNameText2.setText(Global.FormatAddress(address2));


        return rowView;
    }
}
