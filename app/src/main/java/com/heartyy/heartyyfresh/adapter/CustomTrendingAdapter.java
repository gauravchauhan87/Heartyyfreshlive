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
import com.heartyy.heartyyfresh.bean.TrendingBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Vijay on 2/28/2016.
 */
public class CustomTrendingAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<TrendingBean> trendingBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light;

    public CustomTrendingAdapter(android.content.Context context, List<TrendingBean> trendingBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.trendingBeanList = trendingBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }


    public void setSelected(int pos){
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return trendingBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return trendingBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return trendingBeanList.indexOf(trendingBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recent_search_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.recent);
        Global.setFont(root, light);
        TrendingBean data = trendingBeanList.get(position);
        TextView search = (TextView) rowView.findViewById(R.id.text);
        search.setText(data.getTag());



        return rowView;
    }
}
