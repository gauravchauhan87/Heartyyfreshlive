package com.heartyy.heartyyfresh;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomStoreRatingListAdapter;
import com.heartyy.heartyyfresh.bean.PastOrderSupplierBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RateUsActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    ListView storeRatingListView;
    CustomStoreRatingListAdapter adapter;
    private List<PastOrderSupplierBean> pastOrderSupplierBeanList;
    TextView orderForText, dateText, rateSoreText;
    RatingBar ratingBar;
    String orderId;
    private SharedPreferences pref;
    int overallRating;
    EditText editExperience;
    Button linearSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_rate_us));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.rate_us_main);
        Global.setFont(root, andBold);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        storeRatingListView = (ListView) findViewById(R.id.list_stores_rating);
        orderForText = (TextView) findViewById(R.id.text_your_order);
        dateText = (TextView) findViewById(R.id.text_order_date);
        rateSoreText = (TextView) findViewById(R.id.text_rate_store);
        editExperience = (EditText) findViewById(R.id.edit_experience);
        linearSubmitButton = (Button) findViewById(R.id.button_submit);

        orderForText.setTypeface(light);
        rateSoreText.setTypeface(light);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);

        ratingBar.setRating(0);

        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.hearty_star), mMode);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.hearty_star), mMode);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.edit_line_zip), mMode);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                overallRating = (int) ratingBar.getRating();
            }
        });

        pastOrderSupplierBeanList = Global.pastOrderSupplierBeanList;
        adapter = new CustomStoreRatingListAdapter(RateUsActivity.this, pastOrderSupplierBeanList);
        storeRatingListView.setAdapter(adapter);
        Global.setListViewHeightBasedOnChildren(storeRatingListView);
        Intent intent = getIntent();
        orderId = intent.getExtras().getString("order_id");

        linearSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRating();
            }
        });
    }

    private void sendRating() {
        Global.showProgress(RateUsActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("order_id", orderId);
            params.put("order_rating", overallRating);

            if (Global.pastOrderSupplierBeanList != null) {
                if (Global.pastOrderSupplierBeanList.size() > 0) {
                    JSONArray suppRatingArray = new JSONArray();
                    for (int i = 0; i < Global.pastOrderSupplierBeanList.size(); i++) {
                        JSONObject supObj = new JSONObject();
                        supObj.put("supplier_id", Global.pastOrderSupplierBeanList.get(i).getSupplierId());
                        supObj.put("supplier_rating", Global.pastOrderSupplierBeanList.get(i).getSupplierRating());
                        supObj.put("order_supplier_id",Global.pastOrderSupplierBeanList.get(i).getOrderSupplierId());
                        suppRatingArray.put(supObj);
                    }
                    params.put("suppliers", suppRatingArray);
                }
            }
            params.put("user_comment", editExperience.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "order/review", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                showAlert(msg);

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //showAlert("Can not register right now. Please try again!");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
               Global.hideProgress();
                Global.hideProgress();

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
    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(RateUsActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RateUsActivity.this);
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
                Intent intent = new Intent(RateUsActivity.this,OrdersActivity.class);
                startActivity(intent);
                finish();;
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
            ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

            if(taskList.get(0).numActivities == 1 &&
                    taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                Log.i("last", "This is last activity in the stack");
                Intent intent = new Intent(RateUsActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }else{
                return super.onOptionsItemSelected(item);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
