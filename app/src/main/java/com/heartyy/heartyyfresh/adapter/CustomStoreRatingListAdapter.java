package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.PastOrderSupplierBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 12/8/2015.
 */
public class CustomStoreRatingListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<PastOrderSupplierBean> pastOrderSupplierBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;

    public CustomStoreRatingListAdapter(Context context, List<PastOrderSupplierBean> pastOrderSupplierBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.pastOrderSupplierBeanList = pastOrderSupplierBeanList;

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
        return pastOrderSupplierBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pastOrderSupplierBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return pastOrderSupplierBeanList.indexOf(pastOrderSupplierBeanList.get(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stores_rating_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.store_rating_main);
        Global.setFont(root, andBold);
        final PastOrderSupplierBean data = pastOrderSupplierBeanList.get(position);
        TextView storeText = (TextView) rowView.findViewById(R.id.text_store_name);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        storeText.setText(data.getSupplierName());
        storeText.setTypeface(andBold);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int suppRating = (int) ratingBar.getRating();
                Global.pastOrderSupplierBeanList.get(position).setSupplierRating(String.valueOf(suppRating));
            }
        });

        ratingBar.setRating(0);
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

        LayerDrawable stars = (LayerDrawable) ratingBar
                .getProgressDrawable();
        stars.getDrawable(2)
                .setColorFilter(context.
                        getResources().getColor(
                        R.color.hearty_star), mMode);
        stars.getDrawable(1).setColorFilter(context.
                getResources().getColor(
                R.color.hearty_star), mMode);
        stars.getDrawable(0).setColorFilter(context.
                getResources().getColor(
                R.color.edit_line_zip), mMode);
        return rowView;
    }
}
