package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.RefineBrandBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Dheeraj on 12/20/2015.
 */
public class CustomRefineBrandListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<RefineBrandBean> sortTYpeList;
    private boolean isClosed = false;
    private String type;
    Typeface andBold, bold, italic,light;


    public CustomRefineBrandListAdapter(android.content.Context context, List<RefineBrandBean> sortTYpeList, String type) {
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
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.refine_list_item, parent, false);
            ViewGroup root = (ViewGroup) rowView.findViewById(R.id.refine_item_main);
            Global.setFont(root, andBold);
            holder = new ViewHolder();
            holder.refineType = (TextView) rowView.findViewById(R.id.text_refine_category);
            holder.catImg = (ImageView) rowView.findViewById(R.id.image_category);
            holder.tickImage = (ImageView) rowView.findViewById(R.id.image_tick);
            holder.refineType.setTypeface(light);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }


        final RefineBrandBean data = sortTYpeList.get(position);

        holder.refineType.setText(data.getBrandname());
        if (type.equalsIgnoreCase("brand")) {
            holder.catImg.setVisibility(View.VISIBLE);
        } else {
            holder.catImg.setVisibility(View.GONE);
        }

        if(data.isSelected()){
            holder.tickImage.setVisibility(View.VISIBLE);
        }else{
            holder.tickImage.setVisibility(View.GONE);
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + data.getBandLogoThumbnail(), ImageLoader.getImageListener(
                        holder.catImg, R.drawable.brand_placeholder, R.drawable.brand_placeholder));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + data.getBandLogoThumbnail());
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        // handle data, like converting it to xml, json, bitmap etc.,
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    // cached response doesn't exists. Make a network call here
                }

            }
        });


        return rowView;
    }
}
