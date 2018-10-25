package com.heartyy.heartyyfresh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderPlacedActivity extends AppCompatActivity {

    Typeface andBold, bold, italic,light;
    TextView orderIdText,textfaq,textorderid,textid,textitem,textorder,multiplePromotionText;
    Button continueShoppingBtn;
    private String id,notificationId,isOn;
    private CheckBox checkBox;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_order_placed));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);

        id = getIntent().getExtras().getString("order_id");
        notificationId = getIntent().getExtras().getString("notification_id");
        isOn = getIntent().getExtras().getString("is_on");
        orderIdText = (TextView) findViewById(R.id.text_id);
        continueShoppingBtn = (Button) findViewById(R.id.button_continue_shopping);
        checkBox = (CheckBox) findViewById(R.id.check);
        orderIdText.setText(id);

        textfaq = (TextView) findViewById(R.id.text_faq);
        textorderid = (TextView) findViewById(R.id.text_order_id);
        textid = (TextView) findViewById(R.id.text_id);
        textitem = (TextView) findViewById(R.id.text_item);
        textorder = (TextView) findViewById(R.id.text_order);
        continueShoppingBtn= (Button) findViewById(R.id.button_continue_shopping);
        multiplePromotionText = (TextView) findViewById(R.id.text_multiple_promotion);




        textfaq.setTypeface(andBold);
        textorderid.setTypeface(light);
        textid.setTypeface(andBold);
        textitem.setTypeface(light);
        textorder.setTypeface(light);
        continueShoppingBtn.setTypeface(andBold);
        multiplePromotionText.setTypeface(italic);

        if(isOn.equalsIgnoreCase("YES")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.pos = -1;
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.CREDITS_AMOUNT,null);
                editor.commit();
                Intent intent = new Intent(OrderPlacedActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    changeNotification("YES", notificationId);

                } else {
                    changeNotification("NO", notificationId);

                }
            }
        });

    }

    private void changeNotification(String status, String notificationId) {
        pref = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);

        RequestQueue rq = Volley.newRequestQueue(OrderPlacedActivity.this);


        JSONObject params1 = new JSONObject();
        JSONArray params12 = new JSONArray();


        try {
            params1.put("is_on", status);
            params1.put("notification_id", notificationId);
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

                            }else if(status.equalsIgnoreCase(Constants.ERROR)){

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Global.pos = -1;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.CREDITS_AMOUNT,null);
        editor.commit();

        Intent inten = new Intent(OrderPlacedActivity.this,HomeActivity.class);
        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inten);
        finish();
    }
}
