package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.AllCreditsBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Gaurav on 11/22/2015.
 */
public class CustomCredistListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<AllCreditsBean> allCreditsBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;

    public CustomCredistListAdapter(Context context, List<AllCreditsBean> allCreditsBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.allCreditsBeanList = allCreditsBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return allCreditsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return allCreditsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return allCreditsBeanList.indexOf(allCreditsBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.credits_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.credits_item_main);
        Global.setFont(root, andBold);
        AllCreditsBean creditData = allCreditsBeanList.get(position);

        TextView creditTitle = (TextView) rowView.findViewById(R.id.text_credit_title);
        TextView creditDescription = (TextView) rowView.findViewById(R.id.text_credit_description);
        TextView creditAmt = (TextView) rowView.findViewById(R.id.text_credit_amt);

        if(!creditData.getCreditDisplayText().equalsIgnoreCase("null")){
            creditTitle.setText(creditData.getCreditDisplayText());
        }else {
            creditTitle.setText("");
        }

        if(!creditData.getCreditReasonDisplay().equalsIgnoreCase("null")){
            creditDescription.setText(creditData.getCreditReasonDisplay());
        }else {
            creditDescription.setText("");
        }

        if(!creditData.getCreditAmount().equalsIgnoreCase("null")){
            creditAmt.setText("$"+creditData.getCreditAmount());
        }else {
            creditAmt.setText("");
        }

        return rowView;
    }
}
