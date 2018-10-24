package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

public class LegalTermsActivity extends AppCompatActivity {

    Typeface andBold, bold, italic,light;
    private String terms;
    private WebView webViewTermsOfServices;
    private Class previousClass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_legal_terms);

        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        terms = getIntent().getExtras().getString("terms");
       // webViewTermsOfServices = (WebView) findViewById(R.id.web_view_terms);
        if (terms.equalsIgnoreCase("tnc")) {
            SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_legal_terms));
            s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);

          getLegalTerms();

        } else if (terms.equalsIgnoreCase("privacy")) {
            SpannableString s = new SpannableString(getResources().getString(R.string.title_policy));
            s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
            getLegalTerms();
        }

        try {
            previousClass = Class.forName(Global.previousActivity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getLegalTerms() {
        Global.showProgress(LegalTermsActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "terms?type="+terms, null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONObject jsonLEgal = jsonObject.getJSONObject("data");
                                String string = jsonLEgal.getString("legal_term");
                                webViewTermsOfServices = new WebView(LegalTermsActivity.this);
                               // webViewTermsOfServices.loadData(string, "text/html", null);
                                webViewTermsOfServices.getSettings().setJavaScriptEnabled(true);
                               String url = "http://docs.google.com/gview?embedded=true&url=" + Constants.IMG_URL+string;
                                webViewTermsOfServices.getSettings().setPluginState(WebSettings.PluginState.ON);
                                webViewTermsOfServices.setWebViewClient(new Callback());
                                webViewTermsOfServices.loadUrl(url);
                                setContentView(webViewTermsOfServices);

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

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(LegalTermsActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LegalTermsActivity.this);
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
        }else if (id == android.R.id.home) {
            Intent intent = new Intent(LegalTermsActivity.this, previousClass);

            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
