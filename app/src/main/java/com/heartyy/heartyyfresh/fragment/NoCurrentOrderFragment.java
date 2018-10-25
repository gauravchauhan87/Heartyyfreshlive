package com.heartyy.heartyyfresh.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.global.Global;

/**
 * Created by Dheeraj on 12/7/2015.
 */


public class NoCurrentOrderFragment extends Fragment {


    private Context context;

    Typeface andBold;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_current_order, container, false);
        context = getActivity().getApplicationContext();
        andBold = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Light.ttf");
        ViewGroup root = (ViewGroup) v.findViewById(R.id.no_current_order_main);
        Global.setFont(root, andBold);
        return v;
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}
