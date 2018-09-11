package com.heartyy.heartyyfresh;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.TrackOrderAdapter;
import com.heartyy.heartyyfresh.bean.OrderTrackBean;
import com.heartyy.heartyyfresh.bean.TrackOrderBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class TrackOrderActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    private SharedPreferences pref;

    ListView listTrackOrders;
    private TrackOrderAdapter adapter;
    private List<OrderTrackBean> orderTrackBeansList;
    CheckBox checkNotification;
    TextView txtOrderNo, txtOrderNoText, notificationText;
    Button btnTrackDelivery;
    String date, time, orderNo, orderId;
    String[] supplierId;
    TrackOrderBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_track_order));
        checkNotification = (CheckBox) findViewById(R.id.check);
        txtOrderNo = (TextView) findViewById(R.id.text_order_number_display);
        txtOrderNoText = (TextView) findViewById(R.id.text_order_number);
        btnTrackDelivery = (Button) findViewById(R.id.button_track_delivery);
        notificationText = (TextView) findViewById(R.id.text_notifications);

        Intent intent = getIntent();
       // date = getIntent().getExtras().getString("date");
      ///  time = getIntent().getExtras().getString("time");
        orderNo = getIntent().getExtras().getString("displayorderId");
      //  supplierId = intent.getStringArrayExtra("supplierId");
        orderId = getIntent().getExtras().getString("orderId");
        txtOrderNo.setText(orderNo);

        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        ViewGroup root = (ViewGroup) findViewById(R.id.track_main);
        Global.setFont(root, andBold);
        listTrackOrders = (ListView) findViewById(R.id.list_track_orders);
        orderTrackBeansList = new ArrayList<>();


        txtOrderNoText.setTypeface(light);
        txtOrderNo.setTypeface(andBold);
        notificationText.setTypeface(italic);

        getOrderTracks();

        checkNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try{
                    String notificationId = data.getNotificationId();

                    if(data.getNotificationId()!=null) {

                        if (isChecked) {
                            changeNotification("YES", notificationId);

                        } else {
                            changeNotification("NO", notificationId);

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void getOrderTracks() {
        TimeZone tz = TimeZone.getDefault();
        String supId = "";

      /*  for (int i = 0; i < supplierId.length; i++) {
            supId += supplierId[i] + ",";
        }

        supId = supId.substring(0, supId.length() - 1);*/
        RequestQueue rq = Volley.newRequestQueue(TrackOrderActivity.this);
        String url = "user/track/order?&user_id=" + pref.getString(Constants.USER_ID, null) + "&order_id=" + orderId+"&timezone="+tz.getID();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                data = ConversionHelper.convertTrackOrderJsonToTrackOrderBean(jsonObject);
                                orderTrackBeansList = data.getOrderTrackBeansList();
                                if (orderTrackBeansList != null) {
                                    adapter = new TrackOrderAdapter(TrackOrderActivity.this, orderTrackBeansList);
                                    listTrackOrders.setAdapter(adapter);
                                    Global.setListViewHeightBasedOnChildren(listTrackOrders);
                                }
                                String notification = data.getIsOn();
                                if(notification!=null) {
                                    if (notification.equalsIgnoreCase("YES")) {
                                        checkNotification.setChecked(true);
                                    } else {
                                        checkNotification.setChecked(false);
                                    }
                                }else{
                                    checkNotification.setChecked(false);
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("try","catch");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
    }


    public void changeNotification(String check, String id) {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        RequestQueue rq = Volley.newRequestQueue(TrackOrderActivity.this);


        JSONObject params1 = new JSONObject();
        JSONArray params12 = new JSONArray();


        try {
            params1.put("is_on", check);
            params1.put("notification_id", id);
            params12.put(params1);
            // notify[0]=notifcationApiBean;

        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject params = new JSONObject();
        try {


            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("notifications", params12);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "user/notification", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage().toString());
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(TrackOrderActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                TrackOrderActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==android.R.id.home){
            ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

            if(taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                Log.i("last", "This is last activity in the stack");
                Intent intent = new Intent(TrackOrderActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }else{
                return super.onOptionsItemSelected(item);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
