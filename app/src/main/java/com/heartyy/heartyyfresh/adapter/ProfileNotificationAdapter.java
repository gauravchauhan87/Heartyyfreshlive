package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.NotificationUserProfileBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by amitvashist on 10/14/15.
 */
public class ProfileNotificationAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private SharedPreferences pref;
    List<NotificationUserProfileBean> notificationBean;
    List<String> count;
    Typeface andBold, bold, italic,light;

    public ProfileNotificationAdapter(Context context, List<NotificationUserProfileBean> notificationBean) {
        this.context = context;
        this.mSelectedItem = -1;
        this.notificationBean = notificationBean;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return notificationBean.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return notificationBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return notificationBean.indexOf(notificationBean.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate( R.layout.profile_notification_list_item, parent, false);

        Typeface andBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_REGULAR);

        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.profile_notification_item);

        Global.setFont(root, andBold);

        final NotificationUserProfileBean data = notificationBean.get(position);

        TextView notificationText = (TextView) rowView.findViewById(R.id.text_meal_reminders);
        notificationText.setTypeface(andBold);

        CheckBox notificationButton = (CheckBox) rowView.findViewById(R.id.meal_check);
        if(data.getIs_on() != null){
            if (data.getIs_on().equals("YES")) {
                notificationButton.setChecked(true);
            } else {
                notificationButton.setChecked(false);
            }
        }

        notificationButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    changeNotification("YES", data.getNotification_id());

                } else {
                    changeNotification("NO", data.getNotification_id());

                }
            }
        });
        notificationText.setText(data.getNotification_display_text());


        return rowView;
    }

    public void changeNotification(String check, String id) {
        pref = context.getApplicationContext().getSharedPreferences("MyPref", context.MODE_PRIVATE);

        RequestQueue rq = Volley.newRequestQueue(context);


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
}
