package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.bean.ReturnPolicyBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 2/2/2016.
 */
public class CustomReturnPolicyListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<ReturnPolicyBean> returnPolicyBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light;

    public CustomReturnPolicyListAdapter(android.content.Context context, List<ReturnPolicyBean> returnPolicyBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.returnPolicyBeanList = returnPolicyBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }


    public void setSelected(int pos) {
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return returnPolicyBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return returnPolicyBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return returnPolicyBeanList.indexOf(returnPolicyBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.policy_list_items, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.policy_main);
        Global.setFont(root, andBold);
        ReturnPolicyBean data = returnPolicyBeanList.get(position);
        TextView supplierName = (TextView) rowView.findViewById(R.id.text_heading);
        TextView city = (TextView) rowView.findViewById(R.id.text_city);
        TextView policyContent = (TextView) rowView.findViewById(R.id.text_content);

        supplierName.setText(data.getSupplierName());
        city.setText(data.getCity());
        if(data.getReturnPolicy().equalsIgnoreCase("null")){
            policyContent.setText("No return policy defined by supplier");
        }else {
            policyContent.setText(data.getReturnPolicy());
        }


        return rowView;
    }
}
