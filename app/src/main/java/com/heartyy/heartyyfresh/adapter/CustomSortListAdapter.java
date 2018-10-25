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
import com.heartyy.heartyyfresh.bean.SortBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/20/2015.
 */
public class CustomSortListAdapter  extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<SortBean> sortTYpeList;
    private boolean isClosed = false;



    public CustomSortListAdapter(android.content.Context context,  List<SortBean> sortTYpeList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.sortTYpeList = sortTYpeList;


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
        View rowView = inflater.inflate(R.layout.sort_list_item, parent, false);
        Typeface light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        final SortBean data = sortTYpeList.get(position);
        TextView sortTYpe = (TextView) rowView.findViewById(R.id.text_sort_category);
        sortTYpe.setTypeface(light);
        ImageView sortIcon = (ImageView) rowView.findViewById(R.id.image_sort_icon);
        ImageView tickImage = (ImageView) rowView.findViewById(R.id.image_tick);
        sortTYpe.setText(data.getSortType());
        sortIcon.setImageResource(data.getSortIcon());

        if(Global.sort==null) {
            if (position == 0) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }else if(Global.sort.equalsIgnoreCase("low")){
            if (position == 1) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }else if(Global.sort.equalsIgnoreCase("high")){
            if (position == 2) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }else if(Global.sort.equalsIgnoreCase("onsale")){
            if (position == 3) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }else if(Global.sort.equalsIgnoreCase("saved")){
            if (position == 4) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }else if(Global.sort.equalsIgnoreCase("popular")){
            if (position == 5) {
                tickImage.setVisibility(View.VISIBLE);
            } else {
                tickImage.setVisibility(View.INVISIBLE);

            }
        }



        return rowView;
    }
}
