package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.utils.AppController;

public class NutritionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrition, container, false);
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}