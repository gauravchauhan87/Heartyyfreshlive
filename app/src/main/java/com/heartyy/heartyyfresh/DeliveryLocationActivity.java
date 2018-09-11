package com.heartyy.heartyyfresh;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomLocationListAdapter;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class DeliveryLocationActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    private ListView locationListView;
    private List<LocationBean> locationBeanList;
    private CustomLocationListAdapter adapter;
    private Button addDeliveryLocationBtn, addNoLocationBtn;
    private SharedPreferences pref;
    private RelativeLayout noLocationLayout;
    private String id, store, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_location);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_delivery_location));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        if (getIntent().hasExtra("id")) {
            id = getIntent().getExtras().getString("id");
        }

        if (getIntent().hasExtra("store")) {
            store = getIntent().getExtras().getString("store");
        }

        if (getIntent().hasExtra("day")) {
            day = getIntent().getExtras().getString("day");
        }


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
        ViewGroup root = (ViewGroup) findViewById(R.id.delivery_main);
        Global.setFont(root, andBold);
        locationListView = (ListView) findViewById(R.id.list_delivery_locations);
        addDeliveryLocationBtn = (Button) findViewById(R.id.button_add_delivery_location);
        noLocationLayout = (RelativeLayout) findViewById(R.id.layout_no_location);
        addNoLocationBtn = (Button) findViewById(R.id.button_add_location);


        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocationBean add = (LocationBean) adapterView.getItemAtPosition(i);
                Intent intent = null;
                if (Global.isDeliveryBack) {
                    intent = new Intent(DeliveryLocationActivity.this, DeliveryEstimateActivity.class);

                } else if (!Global.isCompleteBack) {
                    intent = new Intent(DeliveryLocationActivity.this, EditDeliveryLocationActivity.class);
                } else {
                    DatabaseHandler db = new DatabaseHandler(DeliveryLocationActivity.this);
                    int count = db.getOrdersCount();
                    if (count > 0) {
                        Global.map = new HashMap<>();
                        Global.estimatedFee = "0";
                        intent = new Intent(DeliveryLocationActivity.this, CompleteOrderActivity.class);
                        Global.locationBean = add;
                    } else {
                        Global.isCompleteBack = false;
                        intent = new Intent(DeliveryLocationActivity.this, EditDeliveryLocationActivity.class);
                    }

                }
                intent.putExtra("address1", add.getAddress1());
                intent.putExtra("address2", add.getAddress2());
                intent.putExtra("apt_suite_unit", add.getAptSuitUnit());
                intent.putExtra("city", add.getCity());
                intent.putExtra("delivery_instructions", add.getDeliveryInstructions());
                intent.putExtra("state", add.getState());
                intent.putExtra("country", add.getCountry());
                intent.putExtra("is_primary_location", add.getIsPrimaryLocation());
                intent.putExtra("location_name", add.getLocationName());
                intent.putExtra("location_type", add.getLocationType());
                intent.putExtra("user_delivery_location_id", add.getUserDeliveryLocationId());
                intent.putExtra("user_id", add.getUserId());
                intent.putExtra("zipcode", add.getZipcode());
                intent.putExtra("id", id);
                intent.putExtra("store", store);
                intent.putExtra("day", day);
                intent.putExtra("address_id", add.getUserDeliveryLocationId());
                if (Global.isCompleteBack || Global.isDeliveryBack) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                if (Global.isCompleteBack) {
                    finish();
                    Global.isCompleteBack = false;
                } else if (Global.isDeliveryBack) {
                    Global.isDeliveryBack = false;
                    finish();
                }
            }
        });

        addDeliveryLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryLocationActivity.this, SearchAddressActivity.class);
                startActivity(intent);
                Global.previousActivity = DeliveryLocationActivity.this.getClass().getName();
            }
        });
        addNoLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryLocationActivity.this, SearchAddressActivity.class);
                startActivity(intent);
                Global.previousActivity = DeliveryLocationActivity.this.getClass().getName();
            }
        });
    }

    private void getDeliveryAddress() {
        Global.showProgress(DeliveryLocationActivity.this);


        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "address?user_id=" + pref.getString(Constants.USER_ID, null), null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.dialog.dismiss();
                                locationBeanList = ConversionHelper.convertDeliveryAddressJsonToDeliveryAddressBean(jsonObject);
                                if (locationBeanList == null) {
                                    noLocationLayout.setVisibility(View.VISIBLE);
                                    locationListView.setVisibility(View.GONE);
                                    addDeliveryLocationBtn.setVisibility(View.GONE);
                                } else if (locationBeanList.size() == 0) {
                                    noLocationLayout.setVisibility(View.VISIBLE);
                                    locationListView.setVisibility(View.GONE);
                                    addDeliveryLocationBtn.setVisibility(View.GONE);
                                } else {
                                    noLocationLayout.setVisibility(View.GONE);
                                    locationListView.setVisibility(View.VISIBLE);
                                    addDeliveryLocationBtn.setVisibility(View.VISIBLE);
                                    adapter = new CustomLocationListAdapter(DeliveryLocationActivity.this, locationBeanList);
                                    locationListView.setAdapter(adapter);
                                    DatabaseHandler db = new DatabaseHandler(DeliveryLocationActivity.this);
                                    int count = db.getDeliveryAddressCount();
                                    if (count == 0) {
                                        db.addDeliveryAddress(locationBeanList);
                                    } else {
                                        db.deleteDeliveryAddress();
                                        db.addDeliveryAddress(locationBeanList);
                                    }
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();
                                showAlert(message);
                            }

                        } catch (JSONException e) {
                            Global.dialog.dismiss();
                            showAlert("something went wrong!");
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Global.dialog.dismiss();
                Log.d("error", "Error: " + error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else if (error instanceof ServerError) {
                    showAlert(Constants.SERVER_ERROR);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);

    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(DeliveryLocationActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                DeliveryLocationActivity.this);
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
            if(Global.isCompleteBack){
                Intent intent = new Intent(DeliveryLocationActivity.this,CompleteOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Global.isCompleteBack = false;
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDeliveryAddress();
    }
}
