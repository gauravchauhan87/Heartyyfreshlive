package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.HomeActivity;
import com.heartyy.heartyyfresh.ItemDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.ImagesBean;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    int[] flag;
    String[] rank;
    LayoutInflater inflater;
    private List<ImagesBean> imagesBeanList;
    Activity activity;

    public ViewPagerAdapter(Context context, List<ImagesBean> imagesBeanList,Activity activity) {
        this.context = context;
        this.imagesBeanList = imagesBeanList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imagesBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        final ImageView imgflag;
//        TextView text_rank;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);
        final ImagesBean data = imagesBeanList.get(position);

        imgflag = (ImageView) itemView.findViewById(R.id.image_packets);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + data.getImage(), ImageLoader.getImageListener(imgflag, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));


                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + data.getImage());
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

        imgflag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ItemDetailActivity) context).showLargeImage();
            }
        });

        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
