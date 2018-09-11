package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;

import java.util.List;

/**
 * Created by Gaurav on 08/02/2016.
 */
public class OrderDetailShopsAdapter extends BaseAdapter {
    private Context context;
    private List<SuppliersBean> suppliersBeanList;
    private Activity activity;
    public OrderDetailShopsAdapter(Context context, List<SuppliersBean> suppliersBeanList, Activity activity){
        this.context = context;
        this.suppliersBeanList = suppliersBeanList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return suppliersBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return suppliersBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return suppliersBeanList.indexOf(suppliersBeanList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.order_detail_shops_row, parent, false);

        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        final SuppliersBean data = suppliersBeanList.get(position);
        TextView shopName = (TextView) rowView.findViewById(R.id.text_shop);
        TextView shopTotal = (TextView) rowView.findViewById(R.id.text_shop_total);

        shopName.setText(data.getSupplierName());
        String temp[] = data.getSupplierTotal().split("\\.");
        if(temp.length>1) {
            shopTotal.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        }else{
            shopTotal.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + "0" + "</sup>"));
        }
        shopName.setTypeface(robotoLight);
        shopTotal.setTypeface(robotoLight);
        return rowView;
    }
}
