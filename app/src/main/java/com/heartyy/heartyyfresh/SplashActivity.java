package com.heartyy.heartyyfresh;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomLocationListAdapter;
import com.heartyy.heartyyfresh.bean.DeliveryEstimateBean;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.ValidationError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private SharedPreferences pref;
    private ValidationError validationError;
    private List<LocationBean> locationBeanList;
    List<PaymentCardBean> cardBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        /*new Handler().post(new Runnable() {
            @Override
            public void run() {
                getErrorMessages();
            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                String userId = pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

        }, SPLASH_DISPLAY_LENGTH);
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
                                    DatabaseHandler db = new DatabaseHandler(SplashActivity.this);
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
                                        DatabaseHandler db = new DatabaseHandler(SplashActivity.this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
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
}
