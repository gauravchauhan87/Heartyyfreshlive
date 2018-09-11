package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.TipBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Vijay on 2/19/2016.
 */
public class CustomTipListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<TipBean> tipBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light, lightItalic;

    public CustomTipListAdapter(Context context, List<TipBean> tipBeanList) {
        this.context = context;
        this.tipBeanList = tipBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        lightItalic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);

    }


    public void setSelected(int pos) {
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tipBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return tipBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return tipBeanList.indexOf(tipBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.tip_list_item, parent, false);
        final TipBean data = tipBeanList.get(position);
        final EditText editOther = (EditText) rowView.findViewById(R.id.text_other);
        final TextView other1 = (TextView) rowView.findViewById(R.id.text_other1);
        final TextView tip = (TextView) rowView.findViewById(R.id.text_tip);
        final RelativeLayout tipMain = (RelativeLayout) rowView.findViewById(R.id.tip_main);
        ViewGroup.LayoutParams params = tipMain.getLayoutParams();
        editOther.setFocusable(true);


        editOther.setTypeface(lightItalic);
        tip.setTypeface(andBold);

        if (position == tipBeanList.size() - 1) {
            other1.setVisibility(View.VISIBLE);
            params.width = 200;
            tipMain.setLayoutParams(params);
        } else {
            other1.setVisibility(View.GONE);
            params.width = 100;
            tipMain.setLayoutParams(params);
        }

        tip.setText(data.getTipName());


        if (position == mSelectedItem) {
            if(position != tipBeanList.size() - 1){
                tipMain.setBackgroundResource(R.color.hearty_green);
                other1.setTextColor(Color.parseColor("#FFFFFF"));
                tip.setTextColor(Color.parseColor("#FFFFFF"));
            }
        } else {
            tipMain.setBackgroundResource(R.color.White);
            other1.setTextColor(Color.parseColor("#333333"));
            tip.setTextColor(Color.parseColor("#333333"));
        }

        if(position==mSelectedItem && position==tipBeanList.size() - 1){
            editOther.setVisibility(View.VISIBLE);
            editOther.setFocusable(true);
            other1.setVisibility(View.GONE);
            tip.setVisibility(View.GONE);
        }



        editOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editOther.setTextSize(14);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String amount = editOther.getText().toString();
                    data.setTipAmount(amount);
                    double amt = Double.parseDouble(amount);
                    data.setTipName("$" + String.format("%.2f", amt));

                    Global.tipBean = data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return rowView;
    }
}
