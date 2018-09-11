package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.SearchItemsBean;
import com.heartyy.heartyyfresh.bean.SearchTagBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 1/21/2016.
 */
public class CustomSearchItemsAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<SearchTagBean> searchItemsBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;
    private String str;

    public CustomSearchItemsAdapter(Context context, Activity activity,String str) {
        this.context = context;
        this.mSelectedItem = 0;
        this.activity = activity;
        this.str = str;
        searchItemsBeanList = new ArrayList<>();

        mInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }

    public void addItem(SearchTagBean item){
        searchItemsBeanList.add(item);
        notifyDataSetChanged();
    }
    public void removeItem(){
        searchItemsBeanList = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return searchItemsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return searchItemsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return searchItemsBeanList.indexOf(searchItemsBeanList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.search_list_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.text_search_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchTagBean data = searchItemsBeanList.get(position);
        String temp = data.getTag();
        String newText =  temp.replaceAll(str, "<font color=#00CC99>"+str+"</font>");
       // String newText = Global.replace(temp,str,"<font color=#00CC99>"+str+"</font>");
        holder.textView.setText(Html.fromHtml(newText));
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;
    }
}
