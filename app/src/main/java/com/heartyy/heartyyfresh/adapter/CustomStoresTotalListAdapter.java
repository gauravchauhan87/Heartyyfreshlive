package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.IndividualDeliveryBean;
import com.heartyy.heartyyfresh.bean.StoresTotalPriceBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.HorizontalListView;

import java.util.List;

/**
 * Created by Dheeraj on 1/27/2016.
 */
public class CustomStoresTotalListAdapter extends BaseAdapter {
    private final Context mContext;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private int mCurrentItemId = 0;
    private Activity activity;
    List<StoresTotalPriceBean> storesTotalPriceBeanList;

    public CustomStoresTotalListAdapter(Context mContext,List<StoresTotalPriceBean> storesTotalPriceBeanList,Activity activity) {
        this.mContext = mContext;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);
        this.activity = activity;
        this.storesTotalPriceBeanList = storesTotalPriceBeanList;


    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return storesTotalPriceBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return storesTotalPriceBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return storesTotalPriceBeanList.indexOf(storesTotalPriceBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stores_total_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.store_total);
        Global.setFont(root, regular);
        StoresTotalPriceBean data = storesTotalPriceBeanList.get(position);
        TextView storeName = (TextView) rowView.findViewById(R.id.text_store_name);
        TextView storePrice = (TextView) rowView.findViewById(R.id.text_store_price);
        TextView storeWarning = (TextView) rowView.findViewById(R.id.text_store_warning);
        storeName.setText(data.getStoreName());
        String price[] = data.getStorePrice().split("\\.");
        if(price.length>1){
            String temp = price[1];
            if(temp.length()>=2){
                storePrice.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + price[0] + "</big><sup>" + temp.substring(0,2) + "</sup>"));
                storePrice.setTextColor(Color.parseColor("#999999"));
            }else{
                storePrice.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + price[0] + "</big><sup>" + temp.substring(0,1) + "</sup>"));
                storePrice.setTextColor(Color.parseColor("#999999"));
            }

        }else{
            storePrice.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + price[0] + "</big><sup>" + "0" + "</sup>"));
        }

        if(data.getIsAvailable().equalsIgnoreCase("yes")){
            storeWarning.setVisibility(View.GONE);
            storeName.setTextColor(Color.parseColor("#999999"));
        }else{
            storeWarning.setVisibility(View.VISIBLE);
            storePrice.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));
            storePrice.setTextColor(Color.parseColor("#FF3333"));
            storeName.setTextColor(Color.parseColor("#FF3333"));
        }


        return rowView;
    }
}
