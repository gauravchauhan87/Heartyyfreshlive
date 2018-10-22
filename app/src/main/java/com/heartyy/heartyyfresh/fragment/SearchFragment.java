package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.utils.AppController;

@SuppressLint("ValidFragment")
public class SearchFragment extends Fragment {

    private View rootView;
    private Context context;
    private Toolbar mToolbar;
    private EditText editSearch;

    public SearchFragment(Context context) {
        this.context = context;

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container,
                false);
        context = getActivity().getApplicationContext();
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar_actionbar1);
        editSearch = (EditText) mToolbar.findViewById(R.id.edit_search);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("search text",editSearch.getText().toString());
            }
        });

        return rootView;

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}
