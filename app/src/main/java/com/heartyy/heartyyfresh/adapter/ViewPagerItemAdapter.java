package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.ImagesBean;
import com.heartyy.heartyyfresh.fragment.DetailFragment;
import com.heartyy.heartyyfresh.fragment.GoesBestWithFragment;
import com.heartyy.heartyyfresh.fragment.NutritionFragment;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ViewPagerItemAdapter extends PagerAdapter {


    Context context;
    int[] flag;
    String[] rank;
    LayoutInflater inflater;
    private List<String> str;

    public ViewPagerItemAdapter(Context context, List<String> str) {
        this.context = context;
        this.str = str;
    }

    @Override
    public int getCount() {
        return str.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        TextView detail;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fragment_detail, container,
                false);
        final String data = str.get(position);

        detail = (TextView) itemView.findViewById(R.id.text_detail);
        detail.setText(data);


        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((FrameLayout) object);

    }
}
