package com.heartyy.heartyyfresh.fragment;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.OrdersActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.adapter.CustomCurrentOrderListAdapter;
import com.heartyy.heartyyfresh.adapter.CustomPastOrderListAdapter;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.CurrentStoresBean;
import com.heartyy.heartyyfresh.bean.PastOrderBean;
import com.heartyy.heartyyfresh.bean.PastStoresBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Deepak Sharma on 10/8/2015.
 */
public class PastOrdersFragment extends Fragment {

    private ListView pastOrdersListView;
    private Context context;
    private RelativeLayout layoutNoItem;
    Typeface andBold;
    private CustomPastOrderListAdapter adapter;
    private List<PastOrderBean> pastOrderBeanList;
    public static boolean isRate = true;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.past_orders, container, false);
        context = getActivity().getApplicationContext();
        andBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
        pastOrdersListView = (ListView) v.findViewById(R.id.list_past_orders);

        if(Global.pastOrderBeansList!=null) {
            adapter = new CustomPastOrderListAdapter(context, Global.pastOrderBeansList);
            pastOrdersListView.setAdapter(adapter);
        }else {
            pastOrdersListView.setVisibility(View.GONE);
            RelativeLayout noOrderLayout = (RelativeLayout) v.findViewById(R.id.layout_no_order);
            noOrderLayout.setVisibility(View.VISIBLE);
        }
        pastOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PastOrderBean order = (PastOrderBean) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderid",order.getOrderId());
                intent.putExtra("order_supplier_id",order.getPastOrderSupplierBeans().get(0).getOrderSupplierId());
                intent.putExtra("order_type","past");
                OrderDetailActivity.isRate = isRate;
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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

