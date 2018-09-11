package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.CurrentOrderSupplierBean;
import com.heartyy.heartyyfresh.bean.CurrentStoresBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/7/2015.
 */
public class CustomCurrentStoresListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<CurrentOrderSupplierBean> currentOrderSupplierBeanList;
    private boolean isClosed = false;

    public CustomCurrentStoresListAdapter(Context context, List<CurrentOrderSupplierBean> currentsupplierBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.currentOrderSupplierBeanList = currentsupplierBeanList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return currentOrderSupplierBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return currentOrderSupplierBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return currentOrderSupplierBeanList.indexOf(currentOrderSupplierBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.current_stores_list_items, parent, false);
        Typeface andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        Typeface robotoBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_BOLD);
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

        TextView textDate = (TextView) rowView.findViewById(R.id.text_date);
        textDate.setTypeface(robotoLight);
        TextView textTime = (TextView) rowView.findViewById(R.id.text_time);
        textTime.setTypeface(robotoLight);
        TextView supplierText = (TextView) rowView.findViewById(R.id.text_stores);
        supplierText.setTypeface(robotoBold);
        TextView deliveryStatusText = (TextView) rowView.findViewById(R.id.text_delivery_status);
        deliveryStatusText.setTypeface(robotoLight);

        CurrentOrderSupplierBean data = currentOrderSupplierBeanList.get(position);

        textDate.setText(data.getDeliveryDate());
        textTime.setText(data.getDeliveryTime());
        supplierText.setText(data.getSupplierName());
        deliveryStatusText.setText(data.getDeliveryStatusDisplay());

        return rowView;
    }
}
