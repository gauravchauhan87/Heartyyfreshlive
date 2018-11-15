package com.heartyy.heartyyfresh.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.heartyy.heartyyfresh.HomeActivity;
import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.adapter.CustomCurrentOrderListAdapter;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

/**
 * Created by Deepak Sharma on 10/8/2015.
 */

@SuppressLint("ValidFragment")
public class CurrentOrdersFragment extends Fragment {

    private ListView crntOrdersListView;
    private Context context;
    private RelativeLayout layoutNoItem;
    Typeface andBold;
    private CustomCurrentOrderListAdapter adapter;
    Button startShoppingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.current_orders, container, false);
        context = getActivity().getApplicationContext();
        andBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_LIGHT);
        crntOrdersListView = (ListView) v.findViewById(R.id.list_current_orders);
        startShoppingBtn = (Button) v.findViewById(R.id.button_start_shopping);
        if (Global.currentOrderBeansList != null) {
            adapter = new CustomCurrentOrderListAdapter(context, Global.currentOrderBeansList);
            crntOrdersListView.setAdapter(adapter);
        } else {
            crntOrdersListView.setVisibility(View.GONE);
            RelativeLayout noOrderLayout = (RelativeLayout) v.findViewById(R.id.layout_no_order);
            noOrderLayout.setVisibility(View.VISIBLE);
        }

        crntOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CurrentOrderBean order = (CurrentOrderBean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderid", order.getOrderId());
                intent.putExtra("order_supplier_id", order.getCurrentOrderSupplierBeans().get(0).getOrderSupplierId());
                intent.putExtra("order_type", "current");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        startShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}
