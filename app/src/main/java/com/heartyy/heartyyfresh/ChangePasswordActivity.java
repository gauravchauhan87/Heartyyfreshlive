package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.ChangePasswordError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
    Typeface andBold, bold, italic,light;
    Button forgotPasswordBtn;
    EditText editCurrentPassword, editNewPassword;
    TextView crntPsswdWarnText, newPsswdWarnText;
    Button updatePasswordBtn;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_change_password));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.change_password_main);
        Global.setFont(root, andBold);

        forgotPasswordBtn = (Button) findViewById(R.id.button_reset_password);
        editCurrentPassword = (EditText) findViewById(R.id.edit_current_password);
        editNewPassword = (EditText) findViewById(R.id.edit_new_password);
        crntPsswdWarnText = (TextView) findViewById(R.id.text_warn_current_password);
        newPsswdWarnText = (TextView) findViewById(R.id.text_warn_new_password);
        updatePasswordBtn = (Button) findViewById(R.id.button_change_password);
        if (pref.getString(Constants.SOCIAL, null) != null) {
            editCurrentPassword.setVisibility(View.GONE);
        } else {
            editCurrentPassword.setVisibility(View.VISIBLE);
        }

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check;
                String crntPassword = editCurrentPassword.getText().toString();
                String newPassword = editNewPassword.getText().toString();
                if (pref.getString(Constants.SOCIAL, null) != null) {
                    check = validate(newPassword);
                } else {
                    check = validate(crntPassword, newPassword);

                }
                if (!check) {
                    changePassword(crntPassword, newPassword);
                }
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestPassword(pref.getString(Constants.EMAIL,null));
            }
        });
    }

    private void changePassword(String crntPassword, String newPassword) {
        Global.showProgress(ChangePasswordActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {


            params.put("user_id", pref.getString(Constants.USER_ID, null));
            if (pref.getString(Constants.SOCIAL, null) != null) {
                params.put("current_password", "");
                params.put("provider", pref.getString(Constants.SOCIAL, null));
            } else {
                params.put("current_password", crntPassword);
                params.put("provider", "local");
            }

            params.put("new_password", newPassword);


        } catch (JSONException e) {
           Global.hideProgress();
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "user/change/password", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                showSuccessAlert(jsonObject.getString("message"));
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            showAlert("Something went Wrong!");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("response", error.toString());
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

    private void requestPassword(final String email) {
        Global.showProgress(ChangePasswordActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {

            params.put("email", email);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "request", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                               Global.hideProgress();
                                Intent intent = new Intent(ChangePasswordActivity.this,
                                        ResetPasswordActivity.class);

                                intent.putExtra("resetemail", email);
                                startActivity(intent);

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                showAlert(message);

                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            showAlert("Something went Wrong!");
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("response", error.toString());
                if(error instanceof NoConnectionError){
                    showAlert(Constants.NO_INTERNET);
                }else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private boolean validate(String crntPassword, String newPassword) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        ChangePasswordError changePasswordError = db.getChangePasswordError();
        if (crntPassword.isEmpty() && newPassword.isEmpty()) {
            error = true;
            crntPsswdWarnText.setVisibility(View.VISIBLE);
            newPsswdWarnText.setVisibility(View.VISIBLE);
            crntPsswdWarnText.setText(changePasswordError.getCurrentPasswordRequired());
            newPsswdWarnText.setText(changePasswordError.getNewPasswordrequired());
        } else if (crntPassword.isEmpty() || newPassword.isEmpty()) {
            if (crntPassword.isEmpty()) {
                error = true;
                crntPsswdWarnText.setVisibility(View.VISIBLE);
                crntPsswdWarnText.setText(changePasswordError.getCurrentPasswordRequired());
                editCurrentPassword.requestFocus();
            } else {
                crntPsswdWarnText.setVisibility(View.GONE);
            }

            if (newPassword.isEmpty()) {
                error = true;
                newPsswdWarnText.setVisibility(View.VISIBLE);
                newPsswdWarnText.setText(changePasswordError.getNewPasswordrequired());
                editNewPassword.requestFocus();
            } else {
                newPsswdWarnText.setVisibility(View.GONE);
            }
        } else {
            crntPsswdWarnText.setVisibility(View.GONE);
            newPsswdWarnText.setVisibility(View.GONE);

            boolean checkPassword = AccountChecker.checkPasswordlength(newPassword);

            if (!checkPassword) {
                error = true;
                newPsswdWarnText.setVisibility(View.VISIBLE);
                newPsswdWarnText.setText(changePasswordError.getPasswordLength());
                editNewPassword.requestFocus();
            }else if(crntPassword.equalsIgnoreCase(newPassword)){
                newPsswdWarnText.setVisibility(View.VISIBLE);
                newPsswdWarnText.setText(changePasswordError.getSame());
                editNewPassword.requestFocus();
            }
        }
        return error;
    }

    private boolean validate(String newPassword) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        ChangePasswordError changePasswordError = db.getChangePasswordError();
        if (newPassword.isEmpty()) {
            error = true;
            newPsswdWarnText.setVisibility(View.VISIBLE);
            newPsswdWarnText.setText(changePasswordError.getNewPasswordrequired());
        } else {
            crntPsswdWarnText.setVisibility(View.GONE);
            newPsswdWarnText.setVisibility(View.GONE);

            boolean checkPassword = AccountChecker.checkPasswordlength(newPassword);

            if (!checkPassword) {
                error = true;
                newPsswdWarnText.setVisibility(View.VISIBLE);
                newPsswdWarnText.setText("Password should be minimum of 6 characters");
                editNewPassword.requestFocus();
            }
        }
        return error;
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ChangePasswordActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChangePasswordActivity.this);
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

    private void showSuccessAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ChangePasswordActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChangePasswordActivity.this);
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
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
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
