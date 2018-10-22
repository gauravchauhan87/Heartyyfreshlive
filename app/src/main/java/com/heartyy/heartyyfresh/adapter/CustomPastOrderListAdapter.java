package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.RateUsActivity;
import com.heartyy.heartyyfresh.bean.PastOrderBean;
import com.heartyy.heartyyfresh.fragment.PastOrdersFragment;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 12/7/2015.
 */
public class CustomPastOrderListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private List<PastOrderBean> pastOrderBeanList;
    private boolean isClosed = false;

    public CustomPastOrderListAdapter(Context context, List<PastOrderBean> pastOrderBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.pastOrderBeanList = pastOrderBeanList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pastOrderBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pastOrderBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return pastOrderBeanList.indexOf(pastOrderBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.past_orders_list_items, parent, false);

        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_REGULAR);
        Typeface robotoLight = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_LIGHT);

        final PastOrderBean data = pastOrderBeanList.get(position);



        TextView textOrder = (TextView) rowView.findViewById(R.id.text_order);
        textOrder.setTypeface(robotoLight);
        TextView textOrderId = (TextView) rowView.findViewById(R.id.text_order_id);
        textOrderId.setTypeface(robotoLight);
        TextView priceText = (TextView) rowView.findViewById(R.id.text_price);
        priceText.setTypeface(robotoRegular);
        TextView itemsText = (TextView) rowView.findViewById(R.id.text_items);
        itemsText.setTypeface(robotoLight);
        ListView pastStoreListView = (ListView) rowView.findViewById(R.id.list_past_stores);
        Button rateUsBtn = (Button) rowView.findViewById(R.id.button_rate_us);
        RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        ImageView view4 = (ImageView) rowView.findViewById(R.id.view4);

        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context,R.color.hearty_star), mMode);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context,R.color.hearty_star), mMode);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context,R.color.edit_line_zip), mMode);


        pastStoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderid",data.getOrderId());
                intent.putExtra("order_supplier_id",data.getPastOrderSupplierBeans().get(0).getOrderSupplierId());
                intent.putExtra("order_type","past");
                OrderDetailActivity.isRate = PastOrdersFragment.isRate;
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        textOrderId.setText(data.getDisplayOrderId());
        priceText.setText("$" + data.getAmount());
        itemsText.setText(data.getItemCount() + " item(s)");

        CustomPastStoresListAdapter adapter = new CustomPastStoresListAdapter(context, data.getPastOrderSupplierBeans());
        pastStoreListView.setAdapter(adapter);

        Global.setListViewHeightBasedOnChildren(pastStoreListView);
        Global.pastOrderSupplierBeanList = data.getPastOrderSupplierBeans();



        rateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RateUsActivity.class);
                intent.putExtra("order_id", data.getOrderId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        int flag = 0, flag1 = 0;
        for (int j = 0; j < data.getPastOrderSupplierBeans().size(); j++) {
            if (data.getPastOrderSupplierBeans().get(j).getDeliveryStatus().equalsIgnoreCase("cancelled")) {
                flag++;
            }
        }

        for (int j = 0; j < data.getPastOrderSupplierBeans().size(); j++) {
            if (!data.getPastOrderSupplierBeans().get(j).getSupplierRating().equalsIgnoreCase("null")) {
                flag1++;
            }
        }

        if (flag == data.getPastOrderSupplierBeans().size() || flag1 == data.getPastOrderSupplierBeans().size()) {
            view4.setVisibility(View.GONE);
            rateUsBtn.setVisibility(View.GONE);
            PastOrdersFragment.isRate = false;
            if (!data.getRating().equalsIgnoreCase("null")){
                view4.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(Integer.parseInt(data.getRating()));
            }


        } else {
            if (!data.getRating().equalsIgnoreCase("null")) {
                rateUsBtn.setVisibility(View.GONE);
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(Integer.parseInt(data.getRating()));
                PastOrdersFragment.isRate = false;
            } else {
                rateUsBtn.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                PastOrdersFragment.isRate = true;
            }


        }


        return rowView;
    }
}
