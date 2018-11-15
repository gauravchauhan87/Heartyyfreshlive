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
import com.heartyy.heartyyfresh.bean.RefineSizeBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/30/2015.
 */
public class CustomRefineSizeAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<RefineSizeBean> sortTYpeList;
    private boolean isClosed = false;
    private String type;
    Typeface andBold, bold, italic,light;


    public CustomRefineSizeAdapter(android.content.Context context, List<RefineSizeBean> sortTYpeList, String type) {
        this.context = context;
        this.mSelectedItem = -1;
        this.sortTYpeList = sortTYpeList;
        this.type = type;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);


    }

    public void setSelected(int positon ) {
        mSelectedItem = positon;
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

    private class ViewHolder {
        TextView refineType;
        ImageView catImg;
        ImageView tickImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.refine_list_item, parent, false);
            holder = new ViewHolder();
            holder.refineType = (TextView) rowView.findViewById(R.id.text_refine_category);
            holder.catImg = (ImageView) rowView.findViewById(R.id.image_category);
            holder.tickImage = (ImageView) rowView.findViewById(R.id.image_tick);
            holder.refineType.setTypeface(light);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        final RefineSizeBean data = sortTYpeList.get(position);

        holder.refineType.setText(data.getSize()+" "+data.getUom());
        if (type.equalsIgnoreCase("brand")) {
            holder.catImg.setVisibility(View.VISIBLE);
        } else {
            holder.catImg.setVisibility(View.GONE);
        }
        if(data.isSelected()){
            holder.tickImage.setVisibility(View.VISIBLE);
        }else{
            holder.tickImage.setVisibility(View.INVISIBLE);
        }


        return rowView;
    }
}
