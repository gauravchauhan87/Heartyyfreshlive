package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.appevents.AppEventsConstants;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.StoresTotalPriceBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

public class CustomStoresTotalListAdapter extends BaseAdapter {
    private final Context mContext;
    private Typeface robotoLight;
    private List<StoresTotalPriceBean> storesTotalPriceBeanList;

    public CustomStoresTotalListAdapter(Context mContext, List<StoresTotalPriceBean> storesTotalPriceBeanList, Activity activity) {
        this.mContext = mContext;
        this.storesTotalPriceBeanList = storesTotalPriceBeanList;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_LIGHT);
    }

    public int getCount() {
        return this.storesTotalPriceBeanList.size();
    }

    public Object getItem(int position) {
        return this.storesTotalPriceBeanList.get(position);
    }

    public long getItemId(int position) {
        return (long) this.storesTotalPriceBeanList.indexOf(this.storesTotalPriceBeanList.get(position));
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.stores_total_list_item, parent, false);
        StoresTotalPriceBean data = (StoresTotalPriceBean) this.storesTotalPriceBeanList.get(position);
        TextView textStorePriceDollor = (TextView) rowView.findViewById(R.id.text_store_price_dollor);
        TextView storeName = (TextView) rowView.findViewById(R.id.text_store_name);
        TextView storePrice = (TextView) rowView.findViewById(R.id.text_store_price);
        TextView storeWarning = (TextView) rowView.findViewById(R.id.text_store_warning);
        TextView textStorePriceDecimal = (TextView) rowView.findViewById(R.id.text_store_price_decimal);
        storeName.setText(data.getStoreName());
        String[] price = data.getStorePrice().split("\\.");

        textStorePriceDollor.setTypeface(robotoLight);
        storeName.setTypeface(robotoLight);
        storePrice.setTypeface(robotoLight);
        storeWarning.setTypeface(robotoLight);
        if (price.length > 1) {
            String temp = price[1];
            if (temp.length() >= 2) {
                storePrice.setText(price[0]);
                textStorePriceDecimal.setText(temp.substring(0, 2));
                storePrice.setTextColor(Color.parseColor("#999999"));
            } else {
                storePrice.setText(price[0]);
                textStorePriceDecimal.setText(temp.substring(0, 1));
                storePrice.setTextColor(Color.parseColor("#999999"));
            }
        } else {
            storePrice.setText(price[0]);
            textStorePriceDecimal.setText("00");
        }
        if (data.getIsAvailable().equalsIgnoreCase("yes")) {
            storeWarning.setVisibility(View.GONE);
            storeName.setTextColor(Color.parseColor("#999999"));
        } else {
            storeWarning.setVisibility(View.VISIBLE);
            storePrice.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            textStorePriceDecimal.setText("00");
            storePrice.setTextColor(Color.parseColor("#FF3333"));
            storeName.setTextColor(Color.parseColor("#FF3333"));
        }
        return rowView;
    }
}
