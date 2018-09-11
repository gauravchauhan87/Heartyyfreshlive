package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.DeliveryTimesActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 2/1/2016.
 */
public class CustomDeliveryStoresDateListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem = -1;
    private List<SuppliersBean> suppliersBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic, light;
    private SharedPreferences pref;

    public CustomDeliveryStoresDateListAdapter(android.content.Context context, List<SuppliersBean> suppliersBeanList) {
        this.context = context;
        this.mSelectedItem = 0;
        this.suppliersBeanList = suppliersBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        pref = context.getApplicationContext().getSharedPreferences("MyPref",
                context.MODE_PRIVATE);

    }


    public void setSelected(int pos) {
        mSelectedItem = pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return suppliersBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return suppliersBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return suppliersBeanList.indexOf(suppliersBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.delivery_store_date_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.delivery_store_date_main);
        Global.setFont(root, andBold);
        SuppliersBean data = suppliersBeanList.get(position);
        TextView storeNameText = (TextView) rowView.findViewById(R.id.text_store_name);
        TextView dateText = (TextView) rowView.findViewById(R.id.text_date);
        TextView timeText = (TextView) rowView.findViewById(R.id.text_time);
        ImageView line = (ImageView) rowView.findViewById(R.id.view0);
        TextView changeDeliveryBtn = (TextView) rowView.findViewById(R.id.button_change_delivery_date);
        TextView storeWarningText = (TextView) rowView.findViewById(R.id.text_store_warning);
        storeNameText.setText(data.getSupplierName());


        storeNameText.setSelected(true);
        storeNameText.setTypeface(null, Typeface.BOLD);
        storeNameText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        storeNameText.setHorizontallyScrolling(true);

        try {
            String type = pref.getString(Constants.DELIVERY_TYPE, null);
            String day = null;
            String date[] = null;
            if (type == null) {
                dateText.setVisibility(View.GONE);
                timeText.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                changeDeliveryBtn.setVisibility(View.GONE);
                storeWarningText.setVisibility(View.GONE);
            } else {
                if (type.equalsIgnoreCase(Constants.COMBINE)) {
                    date = pref.getString(Constants.DELIVERY_DATE, null).split("-");

                    day = Global.operatingHourBean.getDayOfWeek();
                } else {

                    if(Global.map.get(data.getSupplierId())==null){

                    }else{
                        date = Global.map.get(data.getSupplierId()).getDeliveryDate().split("-");
                        day = Global.map.get(data.getSupplierId()).getOperatingHourBean().getDayOfWeek();
                    }


                }
                if(day!=null) {
                    if (day.equalsIgnoreCase("MON")) {
                        day = "Monday";
                    } else if (day.equalsIgnoreCase("TUE")) {
                        day = "Tuesday";
                    } else if (day.equalsIgnoreCase("WED")) {
                        day = "Wednesday";
                    } else if (day.equalsIgnoreCase("THU")) {
                        day = "Thursday";
                    } else if (day.equalsIgnoreCase("FRI")) {
                        day = "Friday";
                    } else if (day.equalsIgnoreCase("SAT")) {
                        day = "Saturday";
                    } else if (day.equalsIgnoreCase("SUN")) {
                        day = "Sunday";
                    }
                }

                if(date!=null) {

                    String dateStr = date[1];
                    if (dateStr.equalsIgnoreCase("01")) {
                        dateStr = "Jan";
                    } else if (dateStr.equalsIgnoreCase("02")) {
                        dateStr = "Feb";
                    } else if (dateStr.equalsIgnoreCase("03")) {
                        dateStr = "Mar";
                    } else if (dateStr.equalsIgnoreCase("04")) {
                        dateStr = "Apr";
                    } else if (dateStr.equalsIgnoreCase("05")) {
                        dateStr = "May";
                    } else if (dateStr.equalsIgnoreCase("06")) {
                        dateStr = "Jun";
                    } else if (dateStr.equalsIgnoreCase("07")) {
                        dateStr = "Jul";
                    } else if (dateStr.equalsIgnoreCase("08")) {
                        dateStr = "Aug";
                    } else if (dateStr.equalsIgnoreCase("09")) {
                        dateStr = "Sep";
                    } else if (dateStr.equalsIgnoreCase("10")) {
                        dateStr = "Oct";
                    } else if (dateStr.equalsIgnoreCase("11")) {
                        dateStr = "Nov";
                    } else if (dateStr.equalsIgnoreCase("12")) {
                        dateStr = "Dec";
                    }

                    dateText.setText(day + "," + dateStr + " " + date[2] + " " + date[0]);
                }

                if (type.equalsIgnoreCase(Constants.COMBINE)) {
                    String startTime[] = Global.timeIntervalBean.getStartTimeLabel().split(" ");
                    String endTime = Global.timeIntervalBean.getEndTimeLabel();
                    timeText.setText(startTime[0] + "-" + endTime);
                } else {
                    if(Global.map.get(data.getSupplierId())==null){

                    }else {
                        String startTime[] = Global.map.get(data.getSupplierId()).getTimeIntervalBean().getStartTimeLabel().split(" ");
                        String endTime = Global.map.get(data.getSupplierId()).getTimeIntervalBean().getEndTimeLabel();
                        timeText.setText(startTime[0] + "-" + endTime);
                    }
                }
            }



            if (data.getSupplierAvailable().equalsIgnoreCase("no")) {
                dateText.setVisibility(View.GONE);
                timeText.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                changeDeliveryBtn.setVisibility(View.GONE);
                storeWarningText.setVisibility(View.VISIBLE);
                storeWarningText.setText(data.getMessage());
            } else {
                if (Global.map.get(data.getSupplierId())==null) {
                    dateText.setVisibility(View.GONE);
                    timeText.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                    storeWarningText.setVisibility(View.GONE);
                    changeDeliveryBtn.setVisibility(View.VISIBLE);
                    changeDeliveryBtn.setText("SELECT DELIVERY TIME");
                } else {
                    dateText.setVisibility(View.VISIBLE);
                    timeText.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    storeWarningText.setVisibility(View.GONE);
                    changeDeliveryBtn.setVisibility(View.VISIBLE);
                    changeDeliveryBtn.setText("CHANGE DELIVERY TIME");
                }

            }



            changeDeliveryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DeliveryTimesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }



        return rowView;
    }
}
