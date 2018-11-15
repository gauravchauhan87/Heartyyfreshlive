package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.adapter.CustomPageAdapter;
import com.heartyy.heartyyfresh.bean.PromotionBean;
import com.heartyy.heartyyfresh.viewpagerindicator.CirclePageIndicator;
import com.heartyy.heartyyfresh.viewpagerindicator.PageIndicator;

import java.util.List;


/**
 * Created by Dheeraj on 12/18/2015.
 */
@SuppressLint("ValidFragment")
public class BannerPagerFragment extends Fragment {

    private View rootView;
    CustomPageAdapter pagerAdapter;
    ViewPager pager;
    PageIndicator mIndicator;
    private Context context;
    private List<PromotionBean> promotionBeanList;


   // @SuppressLint("ValidFragment")
    public BannerPagerFragment(Context context, List<PromotionBean> promotionBeanList) {
        this.context = context;
        this.promotionBeanList = promotionBeanList;

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_banner, container,
                false);
        context = getActivity().getApplicationContext();
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        pagerAdapter = new CustomPageAdapter(context,promotionBeanList);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);
        mIndicator.setViewPager(pager);

        return rootView;

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}
