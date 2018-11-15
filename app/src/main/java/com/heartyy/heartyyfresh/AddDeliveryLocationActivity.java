package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.AddressError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AddDeliveryLocationActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;

    private EditText editAddress1;
    private EditText editAptSuiteUnit;
    private EditText editZipCode;
    private EditText editLocationName;
    private EditText editDeliveryInstruction, editState, editCity;
    private CheckBox checkBoxPrimary;
    private Button addLocation, homeBtn, workBtn;
    private CheckBox checkBox;
    private boolean isPrimary = false;
    private SharedPreferences pref;
    private boolean ishome = true;
    private TextView streetErrortText, cityErrorText, stateErrorText, zipErrorText;
    String city;
    String state;
    String country;
    private Class previousClass = null;
    TextView primaryText;
    private LocationBean locationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_location);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_add_delivery_location));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.add_location_main);
        Global.setFont(root, andBold);
        try {
            previousClass = Class.forName(Global.previousActivity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        editAddress1 = (EditText) findViewById(R.id.edit_street);
        editAptSuiteUnit = (EditText) findViewById(R.id.edit_apt);
        editDeliveryInstruction = (EditText) findViewById(R.id.edit_delivery_instructions);
        editLocationName = (EditText) findViewById(R.id.edit_location_name);
        editZipCode = (EditText) findViewById(R.id.edit_zipcode);
        addLocation = (Button) findViewById(R.id.button_add_location);
        checkBox = (CheckBox) findViewById(R.id.check);
        homeBtn = (Button) findViewById(R.id.button_home);
        workBtn = (Button) findViewById(R.id.button_work);
        editState = (EditText) findViewById(R.id.edit_state);
        editCity = (EditText) findViewById(R.id.edit_city);
        streetErrortText = (TextView) findViewById(R.id.text_error_street);
        cityErrorText = (TextView) findViewById(R.id.text_error_city);
        stateErrorText = (TextView) findViewById(R.id.text_error_state);
        zipErrorText = (TextView) findViewById(R.id.text_error_zip);
        primaryText = (TextView) findViewById(R.id.text_primary);

        homeBtn.setTypeface(light);
        workBtn.setTypeface(light);
        primaryText.setTypeface(light);

        Intent intent = getIntent();
        String street = intent.getStringExtra("street");
        String zipcode = intent.getStringExtra("zipcode");
        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");
        country = intent.getStringExtra("country");

        editAddress1.setText(street);
        editZipCode.setText(zipcode);
        state = state.replace(" ","");
        editState.setText(state);
        editCity.setText(city);

        editAddress1.setFocusable(false);
        editState.setFocusable(false);
        editCity.setFocusable(false);

        if(!zipcode.equalsIgnoreCase("")){
            //editZipCode.setFocusable(false);
        }


        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = editZipCode.getText().toString();
                String street = editAddress1.getText().toString();
                String apt = editAptSuiteUnit.getText().toString();
                String locationName = editLocationName.getText().toString();
                city = editCity.getText().toString();
                state = editState.getText().toString();
                String deliveryInstructions = editDeliveryInstruction.getText().toString();
                boolean check = validate(street, city, state, zip);

                if (!check) {
                    Global.showProgress(AddDeliveryLocationActivity.this);
                    addLocation(street, apt, locationName, deliveryInstructions, zip, city, state, country);
                }

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPrimary = true;
                } else {
                    isPrimary = false;
                }
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBtn.setBackgroundResource(R.drawable.button_green);
                homeBtn.setTextColor(Color.WHITE);
                workBtn.setBackgroundResource(R.drawable.edit_border);
                workBtn.setTextColor(Color.parseColor("#333333"));
                ishome = true;
            }
        });

        workBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workBtn.setBackgroundResource(R.drawable.button_green);
                workBtn.setTextColor(Color.WHITE);
                homeBtn.setBackgroundResource(R.drawable.edit_border);
                homeBtn.setTextColor(Color.parseColor("#333333"));
                ishome = false;
            }
        });


    }

    private boolean validate(String street, String city, String state, String zip) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        AddressError addressError = db.getAddressError();
        if ((street.isEmpty() || street == null) && (city.isEmpty() || city == null) && (state.isEmpty() || state == null) && (zip.isEmpty() || zip == null)) {

            error = true;

            streetErrortText.setVisibility(View.VISIBLE);
            cityErrorText.setVisibility(View.VISIBLE);
            stateErrorText.setVisibility(View.VISIBLE);
            zipErrorText.setVisibility(View.VISIBLE);

            streetErrortText.setText(addressError.getStreetRequired());
            cityErrorText.setText(addressError.getCityRequired());
            stateErrorText.setText(addressError.getStateRequired());
            zipErrorText.setText(addressError.getZipcodeRequired());

        } else if ((street.isEmpty() || street == null) || (city.isEmpty() || city == null) || (state.isEmpty() || state == null) || (zip.isEmpty() || zip == null)) {
            error = true;
            if (street.isEmpty() || street == null) {
                streetErrortText.setVisibility(View.VISIBLE);
                streetErrortText.setText(addressError.getStreetRequired());
                editAddress1.setFocusable(true);
            } else {
                streetErrortText.setVisibility(View.GONE);
            }

            if (city.isEmpty() || city == null) {
                cityErrorText.setVisibility(View.VISIBLE);
                cityErrorText.setText(addressError.getCityRequired());
                editCity.setFocusable(true);
            } else {
                cityErrorText.setVisibility(View.GONE);
            }

            if (state.isEmpty() || state == null) {
                stateErrorText.setVisibility(View.VISIBLE);
                stateErrorText.setText(addressError.getStateRequired());
                editState.setFocusable(true);
            } else {
                stateErrorText.setVisibility(View.GONE);
            }

            if (zip.isEmpty() || zip == null) {
                zipErrorText.setVisibility(View.VISIBLE);
                zipErrorText.setText(addressError.getZipcodeRequired());
                editZipCode.setFocusable(true);
            } else {
                zipErrorText.setVisibility(View.GONE);
            }
        } else {
            streetErrortText.setVisibility(View.GONE);
            cityErrorText.setVisibility(View.GONE);
            stateErrorText.setVisibility(View.GONE);
            zipErrorText.setVisibility(View.GONE);

            boolean checkZip = AccountChecker.checkZip(zip);
            if (!checkZip) {
                error = true;
                editZipCode.setFocusable(true);
                showAlert(addressError.getZipcodeInvalid());
            }
        }

        return error;
    }

    private void addLocation(String street, String apt, String locationName, String deliveryInstructions, String zip, String city, String state, String country) {

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();

        try {

            params.put("street_address", street);
            params.put("address_line2", "");
            params.put("apt_suite_unit", apt);
            params.put("zipcode", zip);
            params.put("location_name", locationName);
            params.put("delivery_instructions", deliveryInstructions);
            params.put("city", city);
            state = state.replace(" ", "");
            params.put("state", state);
            params.put("country", country);
            if (isPrimary) {
                params.put("primary", "YES");
            } else {
                params.put("primary", "NO");
            }
            if (ishome) {
                params.put("location_type", "home");
            } else {
                params.put("location_type", "work");
            }
            params.put("user_id", pref.getString(Constants.USER_ID, null));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "address", params, //Not null.
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                locationBean = ConversionHelper.convertLocationJsonToLocationBean(jsonObject);
                                showAlertBack(message);
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(message);
                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            showAlert("Something went wrong!");
                            e.printStackTrace();
                            Log.d("Exceptioon",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("error", "Error: " + error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(AddDeliveryLocationActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddDeliveryLocationActivity.this);
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

    private void showAlertBack(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(AddDeliveryLocationActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddDeliveryLocationActivity.this);
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
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(Global.isCompleteBack){
                    DatabaseHandler db = new DatabaseHandler(AddDeliveryLocationActivity.this);
                    List<LocationBean> locationBeanList = db.getAllDeliveryaddress();
                    if(locationBeanList.size()==0){
                        locationBeanList.add(locationBean);
                        db.addDeliveryAddress(locationBeanList);
                    }
                    if(db.getOrdersCount()>0) {

                        Global.map = new HashMap<>();
                        Global.estimatedFee = "0";
                        Intent intent = new Intent(AddDeliveryLocationActivity.this, CompleteOrderActivity.class);
                        Global.locationBean = locationBean;
                        if (Global.isCompleteBack) {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        Global.isCompleteBack = false;
                        startActivity(intent);
                        finish();
                    }else{
                        Global.isCompleteBack = false;
                        Intent intent = new Intent(getApplicationContext(), previousClass);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Global.isCompleteBack = false;
                    Intent intent = new Intent(getApplicationContext(), previousClass);
                    startActivity(intent);
                    finish();
                }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
