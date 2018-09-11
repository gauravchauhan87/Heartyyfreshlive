package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.TrackOrderActivity;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.HeartyPromotionBean;
import com.heartyy.heartyyfresh.bean.PromotionBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 1/4/2016.
 */
public class CustomPromotionListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<PromotionBean> promotionBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;

    public CustomPromotionListAdapter(android.content.Context context, List<PromotionBean> promotionBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.promotionBeanList = promotionBeanList;

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
        return promotionBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return promotionBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return promotionBeanList.indexOf(promotionBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.promotion_list_item, parent, false);
        PromotionBean data = promotionBeanList.get(position);
        TextView titleText = (TextView) rowView.findViewById(R.id.text_promotion);
        TextView detailText = (TextView) rowView.findViewById(R.id.text_weekendstarted);
        TextView checkText = (TextView) rowView.findViewById(R.id.text_promotionauto);
        titleText.setText(data.getPromotionTitle());
        detailText.setText(data.getPromotionDisplayText());
        if(data.getCreditAmount().equalsIgnoreCase("null")){
            checkText.setText("");
        }else{
            checkText.setText(data.getCreditAmount());
        }


        titleText.setTypeface(andBold);
        detailText.setTypeface(light);
        checkText.setTypeface(light);




        return rowView;
    }
}
