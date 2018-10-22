package com.heartyy.heartyyfresh;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.ValidationError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    private int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    Typeface andBold, bold, italic;
    TextView freshText,deliveredText,localStoreText;
    Button getStartedBtn, alreadyMemberBtn,browseBtn;
    private SharedPreferences pref;
    private ValidationError validationError;
    private List<LocationBean> locationBeanList;
    List<PaymentCardBean> cardBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(), Fonts.LUCIDA_HANDWRITING);
        ViewGroup root = (ViewGroup) findViewById(R.id.tutorial_main);
        Global.setFont(root, andBold);
        freshText = (TextView) findViewById(R.id.text_fresh);
        getStartedBtn = (Button) findViewById(R.id.button_get_started);
        alreadyMemberBtn = (Button) findViewById(R.id.button_already_member);
        browseBtn = (Button) findViewById(R.id.button_browse);
        deliveredText = (TextView) findViewById(R.id.text_eat_delivered);
        localStoreText = (TextView) findViewById(R.id.text_from_local_store);
        freshText.setTypeface(italic);
        getStartedBtn.setTypeface(andBold);
        deliveredText.setTypeface(andBold);
        browseBtn.setTypeface(andBold);
        alreadyMemberBtn.setTypeface(andBold);
        localStoreText.setTypeface(andBold);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                Global.previousActivity = MainActivity.this.getClass().getName();
            }
        });

        alreadyMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                Global.previousActivity = MainActivity.this.getClass().getName();
            }
        });
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ZipCodeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndRequestPermissions();
    }

    private void getErrorMessages() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "config", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                validationError = ConversionHelper.convertErrorMsgsJsonToErrorMsgsBean(jsonObject);
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.addErrors(validationError);
                                getDeliveryAddress();
                                getAllPaymentCard();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());

            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void getDeliveryAddress() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "address?user_id=" + pref.getString(Constants.USER_ID, null), null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                locationBeanList = ConversionHelper.convertDeliveryAddressJsonToDeliveryAddressBean(jsonObject);
                                if (locationBeanList != null && locationBeanList.size() != 0) {
                                    DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                                    int count = db.getDeliveryAddressCount();
                                    if (count == 0) {
                                        db.addDeliveryAddress(locationBeanList);
                                    } else {
                                        db.deleteDeliveryAddress();
                                        db.addDeliveryAddress(locationBeanList);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", "Error: " + error.toString());
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);

    }

    private void getAllPaymentCard() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "payment?user_id=" + pref.getString(Constants.USER_ID, null), null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                cardBeanList = ConversionHelper.covertPaymentCardJsonToPaymentCardApiBean(jsonObject);
                                if(cardBeanList!=null){
                                    if(cardBeanList.size()>0){
                                        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                                        int count = db.getPaymentCardsCount();
                                        if (count == 0) {
                                            db.addAllPaymentCards(cardBeanList);
                                        } else {
                                            db.deletePaymentCards();
                                            db.addAllPaymentCards(cardBeanList);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", error.toString());
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
    }

    private void checkAndRequestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if(requestCode == REQUEST_WRITE_EXTERNAL_STORAGE){
            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        getErrorMessages();
                    }
                });
            }
        }
    }
}
