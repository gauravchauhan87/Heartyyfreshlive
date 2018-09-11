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

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/20/2015.
 */
public class CustomRefineCategoryListAdapter  extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<String> sortTYpeList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;



    public CustomRefineCategoryListAdapter(android.content.Context context,  List<String> sortTYpeList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.sortTYpeList = sortTYpeList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);


    }

    public void setSelection(int i){
        mSelectedItem = i;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sortTYpeList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return sortTYpeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return sortTYpeList.indexOf(sortTYpeList.get(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.refine_category_list_item, parent, false);
        Typeface andBold = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Light.ttf");
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.refine_category_main);
        Global.setFont(root, andBold);
        final String data = sortTYpeList.get(position);
        TextView refineType = (TextView) rowView.findViewById(R.id.text_category);
        refineType.setText(data);
        if(mSelectedItem==position){
            refineType.setBackgroundResource(R.color.hearty_green);
            refineType.setTextColor(Color.WHITE);
            refineType.setTypeface(light);
        }else{
            refineType.setBackgroundResource(R.color.White);
            refineType.setTextColor(Color.parseColor("#808080"));
            refineType.setTypeface(andBold);
        }

        return rowView;
    }
}
