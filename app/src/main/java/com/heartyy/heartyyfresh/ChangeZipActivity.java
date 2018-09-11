package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomLocationListAdapter;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.StoreBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;
import com.heartyy.heartyyfresh.utils.ZanyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class ChangeZipActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    private ListView locationListView;
    private List<LocationBean> locationBeanList;
    private CustomLocationListAdapter adapter;
    EditText editZip1, editZip2, editZip3, editZip4;
    ZanyEditText editZip5;
    private SharedPreferences pref;
    private Button startShoppingBtn;
    private LinearLayout orLayout;
    private TextView selectTextAny;
    private String addressId;
    private boolean addressFromlocal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_zip);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_change_zip));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.change_zip_main);
        Global.setFont(root, andBold);
        locationListView = (ListView) findViewById(R.id.list_locations);
        editZip1 = (EditText) findViewById(R.id.edit_text_zip_code1);
        editZip2 = (EditText) findViewById(R.id.edit_text_zip_code2);
        editZip3 = (EditText) findViewById(R.id.edit_text_zip_code3);
        editZip4 = (EditText) findViewById(R.id.edit_text_zip_code4);
        editZip5 = (ZanyEditText) findViewById(R.id.edit_text_zip_code5);
        startShoppingBtn = (Button) findViewById(R.id.button_start_shopping);
        orLayout = (LinearLayout) findViewById(R.id.text_view_or);
        selectTextAny = (TextView) findViewById(R.id.text_select_any);
        String userId = pref.getString(Constants.USER_ID, null);
        if (userId == null) {
            orLayout.setVisibility(View.GONE);
            selectTextAny.setVisibility(View.GONE);
        } else {
            orLayout.setVisibility(View.VISIBLE);
            selectTextAny.setVisibility(View.VISIBLE);
            getDeliveryAddress();
        }

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocationBean location = (LocationBean) adapterView.getItemAtPosition(i);
                String zip = location.getZipcode();
                addressId = location.getUserDeliveryLocationId();
                addressFromlocal = true;
                checkAvailability(zip);
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
                if (str.length() > 0) {
                }
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
                }else if(keyCode==KeyEvent.KEYCODE_ENTER){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editZip5.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
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


        startShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip1 = editZip1.getText().toString();
                String zip2 = editZip2.getText().toString();
                String zip3 = editZip3.getText().toString();
                String zip4 = editZip4.getText().toString();
                String zip5 = editZip5.getText().toString();

                if (zip1.isEmpty() || zip2.isEmpty() || zip3.isEmpty() || zip4.isEmpty() || zip5.isEmpty() || zip1 == null || zip2 == null || zip3 == null || zip4 == null || zip5 == null) {
                    showAlert("Enter a valid zipcode");
                } else {
                    String zipCode = zip1 + zip2 + zip3 + zip4 + zip5;
                    addressId = null;
                    Global.changeZip = true;
                    addressFromlocal = false;
                    checkAvailability(zipCode);
                }


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
        Global.showProgress(ChangeZipActivity.this);
        String url = null;
        if (addressId != null) {
            url = "service/available?zipcode=" + zipCode + "&address_id=" + addressId + "&user_id=" + pref.getString(Constants.USER_ID, null);
        } else {
            url = "service/available?zipcode=" + zipCode + "&user_id=" + pref.getString(Constants.USER_ID, null);
        }
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                StoreBean storeBeanList = ConversionHelper.convertStoreJsonToStoreBeanList(jsonObject);

                                String available = storeBeanList.getAvalilable();

                                if (available.equalsIgnoreCase("YES")) {
                                    Global.zip = zipCode;
                                    if (!addressFromlocal) {
                                        Global.changeZip = true;
                                        Global.pos=-1;
                                        Intent intent = new Intent(ChangeZipActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        editZip1.setText(zipCode.substring(0, 1));
                                        editZip2.setText(zipCode.substring(1, 2));
                                        editZip3.setText(zipCode.substring(2, 3));
                                        editZip4.setText(zipCode.substring(3, 4));
                                        editZip5.setText(zipCode.substring(4, 5));
                                    }


                                } else if (available.equalsIgnoreCase("NO")) {
                                    if (pref.getString(Constants.USER_ID, null) != null) {
                                        showNoEmailAlert(zipCode);
                                    } else {
                                        showEmailAlert(zipCode);
                                    }

                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();

                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            Global.dialog.dismiss();
                            showAlert("Something went wrong.!");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else if (error instanceof ServerError) {
                    showAlert(Constants.SERVER_ERROR);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
                ;
            }
        });
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showEmailAlert(final String zipCode) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ChangeZipActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_email_id, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChangeZipActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        final TextView codeWarnText = (TextView) promptsView.findViewById(R.id.text_code_warn);
        final EditText editemail = (EditText) promptsView.findViewById(R.id.edit_email);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button keepInformedBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        titleText.setText("We are not in " + zipCode + " just yet\nWe are expanding fast. Provider your email and we will keep you informed");
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        keepInformedBtn.setTypeface(andBold);
        codeWarnText.setTypeface(andBold);
        editemail.setTypeface(andBold);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        keepInformedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editemail.getText().toString();
                if (email.isEmpty()) {
                    codeWarnText.setVisibility(View.VISIBLE);
                    codeWarnText.setText("Email address is required");
                } else if (!AccountChecker.checkEmail(email)) {
                    codeWarnText.setVisibility(View.VISIBLE);
                    codeWarnText.setText("Enter valid email address");
                } else {
                    userInterest(zipCode, email);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    private void showNoEmailAlert(final String zipCode) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ChangeZipActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChangeZipActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button keepInformedBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        keepInformedBtn.setTypeface(andBold);
        titleText.setText("We are not in " + zipCode + " just yet\nWe will inform you once we start serving this area");
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        keepInformedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInterest(zipCode, pref.getString(Constants.EMAIL, null));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void userInterest(String zipCode, String email) {
        Global.showProgress(ChangeZipActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {
            String device = "Android";
            String androidOsVersion = getDeviceOs();

            params.put("email", email);
            params.put("zipcode", zipCode);
            params.put("phone_type", device);
            params.put("os_version", androidOsVersion);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "interest", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                showAlert(message);
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                             //   showAlert(message);
                            }
                        } catch (JSONException e) {
                            Global.dialog.dismiss();
                            showAlert("Something went wrong!");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage().toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
    }

    public static String getDeviceOs() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }
        return "OS: " + builder.toString();

    }

    private void getDeliveryAddress() {
        Global.showProgress(ChangeZipActivity.this);


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

                                    locationListView.setVisibility(View.GONE);
                                    orLayout.setVisibility(View.GONE);
                                    selectTextAny.setVisibility(View.GONE);

                                } else if (locationBeanList.size() == 0) {

                                    locationListView.setVisibility(View.GONE);
                                    orLayout.setVisibility(View.GONE);
                                    selectTextAny.setVisibility(View.GONE);

                                } else {

                                    locationListView.setVisibility(View.VISIBLE);
                                    orLayout.setVisibility(View.VISIBLE);
                                    selectTextAny.setVisibility(View.VISIBLE);
                                    adapter = new CustomLocationListAdapter(ChangeZipActivity.this, locationBeanList);
                                    locationListView.setAdapter(adapter);
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
                .from(ChangeZipActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChangeZipActivity.this);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
