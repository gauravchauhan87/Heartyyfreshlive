package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heartyy.heartyyfresh.DeliveryTimesActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.HorizontalListView;

import java.util.List;

/**
 * Created by Dheeraj on 1/26/2016.
 */
public class CustomIndividualStoreAdapter extends BaseAdapter {

    private final Context mContext;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private int mCurrentItemId = 0;
    private Activity activity;
    List<WorkingDaysBean> workingDaysBeanList;

    public CustomIndividualStoreAdapter(Context mContext, List<WorkingDaysBean> workingDaysBeanList, Activity activity) {
        this.mContext = mContext;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);
        this.activity = activity;
        this.workingDaysBeanList = workingDaysBeanList;


    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return workingDaysBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return workingDaysBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return workingDaysBeanList.indexOf(workingDaysBeanList.get(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.individual_list_item, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.individual_main);
        Global.setFont(root, regular);
        final HorizontalListView deliveryListView = (HorizontalListView) rowView.findViewById(R.id.list_delivery_slots);
        TextView supplierName = (TextView) rowView.findViewById(R.id.text_store);
        final TextView storePrice = (TextView) rowView.findViewById(R.id.text_store_price);
        final WorkingDaysBean data = workingDaysBeanList.get(position);
        final String[] deliveryDay = {null};


        int j=0;
        do{
            if(data.getOperatingHourBeanList().get(j).getIsClose().equalsIgnoreCase("YES")){
                j++;
            }
        }while (j == data.getOperatingHourBeanList().size());

        String[] dateTime = workingDaysBeanList.get(position).getOperatingHourBeanList().get(j).getTimeIntervalBeanList().get(0).getStartTime().split(" ");
        String[] startTimeFullString = dateTime[1].split(":");
        String hour = startTimeFullString[0];
        int hrValue = Integer.parseInt(hour);

        deliveryDay[0] = workingDaysBeanList.get(position).getOperatingHourBeanList().get(j).getDayOfWeek();

        if(hrValue>=18){
            System.out.println("start time is after congestion time");
            storePrice.setText("$" + data.getDeliveryEstimatedChargeAfterSix());
        }else{
            System.out.println("You selected regular delivery");
            storePrice.setText("$" + data.getDeliveryEstimatedChargeBeforeSix());
        }


        DatabaseHandler db = new DatabaseHandler(mContext);
        SuppliersBean suppliersBean = db.getSupplier(data.getSupplierId());
        supplierName.setText(suppliersBean.getSupplierName());
        HorizontalListView dateListView = (HorizontalListView) rowView.findViewById(R.id.list_dates);
        List<TimeIntervalBean> timeIntervalBeanList = null;
        final List<OperatingHourBean> operatingHourBeanList = data.getOperatingHourBeanList();
        int pos = -1;
        for (int i = 0; i < operatingHourBeanList.size(); i++) {
            OperatingHourBean operatingHourBean = operatingHourBeanList.get(i);
            if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                pos = i;
                SupplierDeliveryScheduleBean deliveryScheduleBean = Global.map.get(data.getSupplierId());
                if (deliveryScheduleBean == null) {
                    deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                }
                deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                if(hrValue>18){
                    deliveryScheduleBean.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeAfterSix());
                }else {
                    deliveryScheduleBean.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeBeforeSix());
                }
                Global.map.put(data.getSupplierId(), deliveryScheduleBean);
                timeIntervalBeanList = operatingHourBean.getTimeIntervalBeanList();
                break;
            }
        }
        final CustomDateListAdapter dateListAdapter = new CustomDateListAdapter(mContext, operatingHourBeanList);
        dateListView.setAdapter(dateListAdapter);
        dateListAdapter.setSelected(pos);
        SupplierDeliveryScheduleBean deliveryScheduleBean = Global.map.get(data.getSupplierId());
        if (deliveryScheduleBean == null) {
            deliveryScheduleBean = new SupplierDeliveryScheduleBean();
        }
        deliveryScheduleBean.setDeliveryFrom(timeIntervalBeanList.get(0).getStartTime());
        deliveryScheduleBean.setDeliveryTo(timeIntervalBeanList.get(0).getEndTime());
        deliveryScheduleBean.setTimeIntervalBean(timeIntervalBeanList.get(0));
        Global.map.put(data.getSupplierId(), deliveryScheduleBean);


        if (timeIntervalBeanList != null) {
            final CustomDeliverySlotsListAdapter dladapter = new CustomDeliverySlotsListAdapter(mContext, timeIntervalBeanList);
            deliveryListView.setAdapter(dladapter);
            dladapter.setSelected(0);

            dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OperatingHourBean operatingHourBean = (OperatingHourBean) adapterView.getItemAtPosition(i);
                    adapterView.setSelection(i);
                    if (operatingHourBean.getIsClose().equalsIgnoreCase("NO")) {
                        dateListAdapter.setSelected(i);
                        dladapter.changeSlot(operatingHourBean.getTimeIntervalBeanList());
                        SupplierDeliveryScheduleBean deliveryScheduleBean = new SupplierDeliveryScheduleBean();
                        deliveryScheduleBean.setDeliveryDate(operatingHourBean.getDate());
                        deliveryScheduleBean.setDeliveryDayOfWeek(operatingHourBean.getDayOfWeek());
                        deliveryDay[0] = operatingHourBean.getDayOfWeek();
                        deliveryScheduleBean.setDeliveryFrom(operatingHourBean.getTimeIntervalBeanList().get(0).getStartTime());
                        deliveryScheduleBean.setDeliveryTo(operatingHourBean.getTimeIntervalBeanList().get(0).getEndTime());
                        deliveryScheduleBean.setOperatingHourBean(operatingHourBean);
                        deliveryScheduleBean.setTimeIntervalBean(operatingHourBean.getTimeIntervalBeanList().get(0));

                        int j=i;
                        do{
                            if(data.getOperatingHourBeanList().get(j).getIsClose().equalsIgnoreCase("YES")){
                                j++;
                            }
                        }while (j == data.getOperatingHourBeanList().size());

                        String dateTime[] = data.getOperatingHourBeanList().get(j).getTimeIntervalBeanList().get(0).getStartTime().split(" ");
                        String[] startTimeFullString = dateTime[1].split(":");
                        String hour = startTimeFullString[0];
                        int hrValue = Integer.parseInt(hour);

                        if(hrValue>=18){
                            System.out.println("start time is after congestion time");
                            storePrice.setText("$"+data.getDeliveryEstimatedChargeAfterSix());
                            workingDaysBeanList.get(position).setIsCongestionDeliverSelected(true);
                            deliveryScheduleBean.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeAfterSix());
                            ((DeliveryTimesActivity)mContext).setIndividualDeliveryTotal(workingDaysBeanList);
                        }else if(hrValue<18){
                            System.out.println("You selected regular delivery");
                            storePrice.setText("$" + data.getDeliveryEstimatedChargeBeforeSix());
                            deliveryScheduleBean.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeBeforeSix());
                            workingDaysBeanList.get(position).setIsCongestionDeliverSelected(false);
                        }

                        Global.map.put(data.getSupplierId(), deliveryScheduleBean);
                        ((DeliveryTimesActivity)mContext).setIndividualDeliveryTotal(workingDaysBeanList);

                    }
                }
            });
        }


        deliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeIntervalBean timeIntervalBean = (TimeIntervalBean) adapterView.getItemAtPosition(i);
                CustomDeliverySlotsListAdapter adapter = (CustomDeliverySlotsListAdapter) deliveryListView.getAdapter();
                adapter.setSelected(i);
                SupplierDeliveryScheduleBean deliveryScheduleBean1 = Global.map.get(data.getSupplierId());
                deliveryScheduleBean1.setDeliveryFrom(timeIntervalBean.getStartTime());
                deliveryScheduleBean1.setDeliveryTo(timeIntervalBean.getEndTime());
                deliveryScheduleBean1.setTimeIntervalBean(timeIntervalBean);
                deliveryScheduleBean1.setDeliveryDayOfWeek(deliveryDay[0]);
                String[] dateTime =  timeIntervalBean.getStartTime().split(" ");
                String[] startTimeFullString = dateTime[1].split(":");
                String hour = startTimeFullString[0];
                int hrValue = Integer.parseInt(hour);

                if(hrValue>=18){
                    System.out.println("You selected Congestion delivery");
                    storePrice.setText("$" + data.getDeliveryEstimatedChargeAfterSix());
                    workingDaysBeanList.get(position).setIsCongestionDeliverSelected(true);
                    deliveryScheduleBean1.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeAfterSix());
                    ((DeliveryTimesActivity)mContext).setIndividualDeliveryTotal(workingDaysBeanList);
                }else{
                    System.out.println("You selected regular delivery");
                    storePrice.setText("$" + data.getDeliveryEstimatedChargeBeforeSix());
                    workingDaysBeanList.get(position).setIsCongestionDeliverSelected(false);
                    deliveryScheduleBean1.setFinalDeliveryPrice(data.getDeliveryEstimatedChargeBeforeSix());
                    ((DeliveryTimesActivity)mContext).setIndividualDeliveryTotal(workingDaysBeanList);
                }
                Global.map.put(data.getSupplierId(), deliveryScheduleBean1);
            }
        });


        return rowView;
    }


}
