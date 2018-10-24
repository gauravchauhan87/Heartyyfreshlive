package com.heartyy.heartyyfresh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.bean.StoreBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.GPSTracker;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ZipCodeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    Typeface andBold, bold, italic, light;
    Button startShoppingBtn;
    EditText editZip1, editZip2, editZip3, editZip4, editZip5;
    TextView enterZipText, zipWarnText;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_code);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_zip_code));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        light = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        ViewGroup root = (ViewGroup) findViewById(R.id.zipcode_main);
        Global.setFont(root, andBold);
        startShoppingBtn = (Button) findViewById(R.id.button_start_shopping);
        editZip1 = (EditText) findViewById(R.id.edit_text_zip_code1);
        editZip2 = (EditText) findViewById(R.id.edit_text_zip_code2);
        editZip3 = (EditText) findViewById(R.id.edit_text_zip_code3);
        editZip4 = (EditText) findViewById(R.id.edit_text_zip_code4);
        editZip5 = (EditText) findViewById(R.id.edit_text_zip_code5);
        zipWarnText = (TextView) findViewById(R.id.text_warn_zip);
        enterZipText = (TextView) findViewById(R.id.text_enter_zipcode);
        enterZipText.setTypeface(light);

        startShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip1 = editZip1.getText().toString();
                String zip2 = editZip2.getText().toString();
                String zip3 = editZip3.getText().toString();
                String zip4 = editZip4.getText().toString();
                String zip5 = editZip5.getText().toString();

                if (zip1.isEmpty() || zip2.isEmpty() || zip3.isEmpty() || zip4.isEmpty() || zip5.isEmpty()) {
                    zipWarnText.setVisibility(View.VISIBLE);
                    zipWarnText.setText(R.string.valid_zipcode);
                } else {
                    zipWarnText.setVisibility(View.GONE);
                    String zipCode = zip1 + zip2 + zip3 + zip4 + zip5;
                    String userid = pref.getString(Constants.USER_ID, null);
                    checkAvailability(zipCode);
                }

            }
        });

        editZip1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (editZip1.getText().toString().length() > 0) {
                        deleteAll();
                    }
                }
            }
        });


        editZip2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (editZip2.getText().toString().length() > 0) {
                        deleteAll();
                    } else {
                        if (editZip1.getText().toString().length() > 0) {

                        } else {
                            deleteAll();
                        }
                    }
            }
        });

        editZip3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (editZip3.getText().toString().length() > 0) {
                        deleteAll();
                    } else {
                        if (editZip2.getText().toString().length() > 0) {

                        } else {
                            deleteAll();
                        }
                    }
            }
        });
        editZip4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    if (editZip4.getText().toString().length() > 0) {
                        deleteAll();
                    } else {
                        if (editZip3.getText().toString().length() > 0) {

                        } else {
                            deleteAll();
                        }
                    }
            }
        });
        editZip5.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (editZip5.getText().toString().length() > 0) {
                        deleteAll();
                    } else {
                        if (editZip4.getText().toString().length() > 0) {

                        } else if (editZip5.getText().toString().length() > 0) {
                            deleteAll();
                        } else {
                            deleteAll();
                        }
                    }
                }
            }
        });

        editZip5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editZip5.getText().toString().length() > 0) {
                    deleteAll();
                }
            }
        });

        editZip1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.length() > 0) {
                    editZip2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editZip2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.length() > 0) {
                    editZip3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editZip3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.length() > 0) {
                    editZip4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editZip4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str.length() > 0) {
                    editZip5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editZip5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editZip5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editZip5.setText("");
                    editZip4.setText("");
                    editZip3.setText("");
                    editZip2.setText("");
                    editZip1.setText("");

                    editZip1.requestFocus();
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editZip5.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    return true;
                }
                return false;
            }
        });
        editZip4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editZip5.setText("");
                    editZip4.setText("");
                    editZip3.setText("");
                    editZip2.setText("");
                    editZip1.setText("");

                    editZip1.requestFocus();
                }
                return false;
            }
        });
        editZip3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editZip5.setText("");
                    editZip4.setText("");
                    editZip3.setText("");
                    editZip2.setText("");
                    editZip1.setText("");

                    editZip1.requestFocus();
                }
                return false;
            }
        });
        editZip2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editZip5.setText("");
                    editZip4.setText("");
                    editZip3.setText("");
                    editZip2.setText("");
                    editZip1.setText("");

                    editZip1.requestFocus();
                }
                return false;
            }
        });
        editZip1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editZip5.setText("");
                    editZip4.setText("");
                    editZip3.setText("");
                    editZip2.setText("");
                    editZip1.setText("");

                    editZip1.requestFocus();
                }
                return false;
            }
        });


    }

    public void deleteAll() {
        editZip5.setText("");
        editZip4.setText("");
        editZip3.setText("");
        editZip2.setText("");
        editZip1.setText("");

        editZip1.requestFocus();
    }

    private void checkAvailability(final String zipCode) {
        Global.showProgress(ZipCodeActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "service/available?zipcode=" + zipCode + "&user_id=" + pref.getString(Constants.USER_ID, null), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.hideProgress();
                        try {

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                StoreBean storeBeanList = ConversionHelper.convertStoreJsonToStoreBeanList(jsonObject);
                                String available = storeBeanList.getAvalilable();

                                if (available.equalsIgnoreCase("YES")) {
                                    Intent intent = new Intent(ZipCodeActivity.this, HomeActivity.class);
                                    intent.putExtra("zip", zipCode);
                                    startActivity(intent);
                                    Global.zip = zipCode;

                                } else if (available.equalsIgnoreCase("NO")) {
                                    Intent intetn = new Intent(ZipCodeActivity.this, StayWithUsActivity.class);
                                    intetn.putExtra("zip", zipCode);
                                    startActivity(intetn);

                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                // Global.hideProgress();
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            // Global.hideProgress();
                            showAlert("Something went wrong.!");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Global.hideProgress();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else if (error instanceof ServerError) {
                    showAlert(Constants.SERVER_ERROR);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }

            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
// lat,lng, your current location
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String postalCode1 = addresses.get(0).getPostalCode();
                    Log.d("latitude...", String.valueOf(latitude));
                    Log.d("longitude...", String.valueOf(longitude));
                    Log.d("zipcode...", postalCode1);

                    if (postalCode1 != null) {
                        if (postalCode1.length() == 5) {
                            editZip1.setText(postalCode1.substring(0, 1));
                            editZip2.setText(postalCode1.substring(1, 2));
                            editZip3.setText(postalCode1.substring(2, 3));
                            editZip4.setText(postalCode1.substring(3, 4));
                            editZip5.setText(postalCode1.substring(4, 5));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gps.showSettingsAlert();
        }
    }

    AlertDialog dialog;

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ZipCodeActivity.this);
        @SuppressLint("InflateParams")
        View promptsView = layoutInflater.inflate(R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ZipCodeActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int locationCheck = ContextCompat.checkSelfPermission(ZipCodeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationCheck == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(ZipCodeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.hideProgress();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}