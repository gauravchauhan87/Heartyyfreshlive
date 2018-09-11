package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.PromotionBean;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Dheeraj on 12/17/2015.
 */
public class CustomPageAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<PromotionBean> promotionBeanList;
    private Typeface regular, meduimItalic, medium, robotoLight;

    public CustomPageAdapter(Context context,List<PromotionBean> promotionBeanList) {
        this.mContext=context;
        this.promotionBeanList = promotionBeanList;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);
    }


    @Override
    public int getCount() {
        return promotionBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.page_item, container, false);
        final PromotionBean data = promotionBeanList.get(position);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.image_banner);
        TextView deliveryText = (TextView) itemView.findViewById(R.id.text_free_delivery);
        //TextView priceText = (TextView) itemView.findViewById(R.id.text_price);
        deliveryText.setText(data.getPromotionTitle());
      //  priceText.setText(data.getPromotionDisplayText());
        deliveryText.setTypeface(regular);
       // priceText.setTypeface(robotoLight);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + data.getBannerBackground(), ImageLoader.getImageListener(
                        imageView, R.drawable.banner_placeholder, R.drawable.banner_placeholder));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + data.getBannerBackground());
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


        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
