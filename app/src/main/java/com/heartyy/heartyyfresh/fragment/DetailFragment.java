package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.global.Global;

@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment {

    private TextView detailText;
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_detail, container,
                false);
        detailText = (TextView) rootView.findViewById(R.id.text_detail);
        detailText.setText(Global.subAisleItemBean.getDescription());
        return rootView;

    }
}




