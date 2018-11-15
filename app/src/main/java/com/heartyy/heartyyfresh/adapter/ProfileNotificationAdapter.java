package com.heartyy.heartyyfresh.adapter;

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
import com.facebook.internal.AnalyticsEvents;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.NotificationUserProfileBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by amitvashist on 10/14/15.
 */
public class ProfileNotificationAdapter extends BaseAdapter {

    Typeface andBold;
    Typeface bold;
    private Context mContext;
    List<String> count;
    Typeface italic;
    Typeface light;
    LayoutInflater mInflater;
    private int mSelectedItem = -1;
    List<NotificationUserProfileBean> notificationBean;
    private SharedPreferences pref;

    public ProfileNotificationAdapter(Context context, List<NotificationUserProfileBean> notificationBean) {
        this.mContext = context;
        this.notificationBean = notificationBean;
        andBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_LIGHT);
    }


    @Override
    public int getCount() {
        return notificationBean.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notificationBean.indexOf(notificationBean.get(position));
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.profile_notification_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

//        Global.setFont(root, andBold);
        if (notificationBean != null && notificationBean.size() > 0) {
            final NotificationUserProfileBean data = notificationBean.get(position);

            if(data.getIs_on() != null){
                if (data.getIs_on().equals("YES")) {
                    viewHolder.notificationButton.setChecked(true);
                } else {
                    viewHolder.notificationButton.setChecked(false);
                }
            }
            viewHolder.notificationButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        changeNotification("YES", data.getNotification_id());

                    } else {
                        changeNotification("NO", data.getNotification_id());

                    }
                }
            });
            viewHolder.notificationText.setText(data.getNotification_display_text());
        }
        return convertView;
    }*/

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_notification_list_item, parent, false);
        Typeface andBold = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_REGULAR);
        Global.setFont((ViewGroup) rowView.findViewById(R.id.profile_notification_item), andBold);
        final NotificationUserProfileBean data = (NotificationUserProfileBean) this.notificationBean.get(position);
        TextView notificationText = (TextView) rowView.findViewById(R.id.text_meal_reminders);
        notificationText.setTypeface(andBold);
        CheckBox notificationButton = (CheckBox) rowView.findViewById(R.id.meal_check);
        if (data.getIs_on() != null) {
            if (data.getIs_on().equals("YES")) {
                notificationButton.setChecked(true);
            } else {
                notificationButton.setChecked(false);
            }
        }
        notificationButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ProfileNotificationAdapter.this.changeNotification("YES", data.getNotification_id());
                } else {
                    ProfileNotificationAdapter.this.changeNotification("NO", data.getNotification_id());
                }
            }
        });
        notificationText.setText(data.getNotification_display_text());
        return rowView;
    }

    public void changeNotification(String check, String id) {
        Context context = this.mContext;
        this.pref = context.getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        RequestQueue rq = Volley.newRequestQueue(mContext);
        JSONObject params1 = new JSONObject();
        JSONArray params12 = new JSONArray();
        try {
            params1.put("is_on", check);
            params1.put("notification_id", id);
            params12.put(params1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject params = new JSONObject();
        try {
            params.put(Constants.USER_ID, this.pref.getString(Constants.USER_ID, null));
            params.put("notifications", params12);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        Log.d("params---", params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "user/notification", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            if (!jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS).equalsIgnoreCase(Constants.SUCCESS)) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.ERROR, "Error: " + error.getMessage());
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /*public class ViewHolder {
        TextView notificationText;
        CheckBox notificationButton;

        public ViewHolder(View v) {
            notificationText = (TextView) v.findViewById(R.id.text_meal_reminders);
            notificationButton = (CheckBox) v.findViewById(R.id.meal_check);

            Typeface andBold = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_REGULAR);

            notificationText.setTypeface(andBold);
        }
    }*/
}
