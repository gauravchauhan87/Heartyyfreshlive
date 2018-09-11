package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.RateUsActivity;
import com.heartyy.heartyyfresh.TrackOrderActivity;
import com.heartyy.heartyyfresh.bean.CreditsBean;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.CurrentOrderSupplierBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 12/7/2015.
 */
public class CustomCurrentOrderListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<CurrentOrderBean> currentOrderBeanList;
    private boolean isClosed = false;


    public CustomCurrentOrderListAdapter(Context context, List<CurrentOrderBean> currentOrderBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.currentOrderBeanList = currentOrderBeanList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return currentOrderBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return currentOrderBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return currentOrderBeanList.indexOf(currentOrderBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.current_orders_list_items, parent, false);

        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

        final CurrentOrderBean data = currentOrderBeanList.get(position);
        TextView textOrder = (TextView) rowView.findViewById(R.id.text_order);
        textOrder.setTypeface(robotoLight);
        TextView textOrderId = (TextView) rowView.findViewById(R.id.text_order_id);
        textOrderId.setTypeface(robotoLight);
        TextView priceText = (TextView) rowView.findViewById(R.id.text_price);
        priceText.setTypeface(robotoRegular);
        TextView itemsText = (TextView) rowView.findViewById(R.id.text_items);
        itemsText.setTypeface(robotoLight);
        ListView crntStoreListView = (ListView) rowView.findViewById(R.id.list_current_stores);
        Button trackOrderbtn = (Button) rowView.findViewById(R.id.button_track_delivery);


        crntStoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderid", data.getOrderId());
                intent.putExtra("order_supplier_id", data.getCurrentOrderSupplierBeans().get(0).getOrderSupplierId());
                intent.putExtra("order_type", "current");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        textOrderId.setText(data.getDisplayOrderId());
        priceText.setText("$" + data.getAmount());
        itemsText.setText(data.getItemCount() + "item(s)");

        CustomCurrentStoresListAdapter adapter = new CustomCurrentStoresListAdapter(context, data.getCurrentOrderSupplierBeans());
        crntStoreListView.setAdapter(adapter);
        Global.setListViewHeightBasedOnChildren(crntStoreListView);

        List<CurrentOrderSupplierBean> currentOrderSupplierBeans = data.getCurrentOrderSupplierBeans();
        final String[] suppIds = new String[currentOrderSupplierBeans.size()];
        final String[] suppNames = new String[currentOrderSupplierBeans.size()];
        for (int i = 0; i < currentOrderSupplierBeans.size(); i++){
            suppIds [i] = currentOrderSupplierBeans.get(i).getOrderSupplierId();
        }



        trackOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrackOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("date", data.getDeliveryDate());
                intent.putExtra("time", data.getDeliveryTime());
                intent.putExtra("displayorderId", data.getDisplayOrderId());
                intent.putExtra("orderId", data.getOrderId());
                intent.putExtra("supplierId",suppIds);
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
