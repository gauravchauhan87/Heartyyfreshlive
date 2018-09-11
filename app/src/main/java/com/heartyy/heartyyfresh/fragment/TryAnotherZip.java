package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.ZipCodeActivity;
import com.heartyy.heartyyfresh.adapter.CustomPageAdapter;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Dheeraj on 1/4/2016.
 */

@SuppressLint("ValidFragment")
public class TryAnotherZip extends Fragment {

    private View rootView;
    private Context context;
    Typeface andBold, bold, italic,light;
    TextView thankText,informText,textBrowseAnother;
    Button tryAnotherZipBtn;
    private String email;

    public TryAnotherZip(String email) {
        this.email = email;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_try_another_zip, container,
                false);
        context = getActivity().getApplicationContext();
        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

        thankText = (TextView) rootView.findViewById(R.id.text_thank);
        informText = (TextView) rootView.findViewById(R.id.text_inform);
        textBrowseAnother = (TextView) rootView.findViewById(R.id.text_browse_another);
        tryAnotherZipBtn = (Button) rootView.findViewById(R.id.button_try_another);
        thankText.setTypeface(light);
        informText.setTypeface(light);
        textBrowseAnother.setTypeface(light);
        tryAnotherZipBtn.setTypeface(andBold);

        informText.setText("We are expanding our area of deliveries pretty fast.\nWe'll email you at " + email+ " as\nsoon as we start delivering in your area.");




        tryAnotherZipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout stayLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_staywith);
                RelativeLayout zipLayout = (RelativeLayout) getActivity().findViewById(R.id.layout_zip_fragment);

                Intent intent = new Intent(context, ZipCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return rootView;

    }
}
