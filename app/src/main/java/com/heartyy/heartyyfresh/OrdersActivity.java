package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomOrdersViewPagerAdapter;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.CurrentPastOrdersBean;
import com.heartyy.heartyyfresh.fragment.PastOrdersFragment;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.SlidingTabLayout;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    static List<CurrentOrderBean> currentOrders;
    Toolbar toolbar;
    ViewPager pager;
    CharSequence Titles[] = {"Current Orders", "Past Orders"};
    int Numboftabs = 2;
    CustomOrdersViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    private SharedPreferences pref;
    Typeface andBold, bold, italic, light;
    Button startShoppingBtn;
    PastOrdersFragment pastOrdersFragment;
    ProgressBar progressBar;
    Bundle bundle;
    String past;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_orders));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        bundle = getIntent().getExtras();
        if(bundle!=null){
            past = bundle.getString("past");
            Log.d("is past",past);
        }

        ViewGroup root = (ViewGroup) findViewById(R.id.orders_main);
        Global.setFont(root, andBold);
        //startShoppingBtn = (Button) findViewById(R.id.button_start_shopping);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        startShoppingBtn = (Button) findViewById(R.id.button_start_shopping);

        startShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this,HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        getPastAndCurrentOrders();

    }

    public void getPastAndCurrentOrders() {
        RequestQueue rq = Volley.newRequestQueue(OrdersActivity.this);
        String url = "user/placed/order?user_id=" + pref.getString(Constants.USER_ID, null);
        Log.d("url..",Constants.URL+url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                CurrentPastOrdersBean data = ConversionHelper.convertOrderJsonToCurrentPastOrdersBean(jsonObject);

                                Global.currentOrderBeansList = data.getCurrentOrderBeansList();
                                Global.pastOrderBeansList = data.getPastOrderBeansList();


                                progressBar.setVisibility(View.GONE);
                                if (Global.currentOrderBeansList == null && Global.pastOrderBeansList == null) {
                                    LinearLayout hasOrderLayout = (LinearLayout) findViewById(R.id.layout_has_order);
                                    RelativeLayout noOrderLayout = (RelativeLayout) findViewById(R.id.layout_no_order);
                                    hasOrderLayout.setVisibility(View.GONE);
                                    noOrderLayout.setVisibility(View.VISIBLE);

                                }

                                adapter = new CustomOrdersViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

                                // Assigning ViewPager View and setting the adapter
                                pager = (ViewPager) findViewById(R.id.pager);
                                pager.setAdapter(adapter);

                                // Assiging the Sliding Tab Layout View
                                tabs = (SlidingTabLayout) findViewById(R.id.tabs);
                                tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

                                // Setting Custom Color for the Scroll bar indicator of the Tab View
                                tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                    @Override
                                    public int getIndicatorColor(int position) {
                                        return getResources().getColor(R.color.tabsScrollColor);
                                    }
                                });

                                // Setting the ViewPager For the SlidingTabsLayout
                                tabs.setViewPager(pager);

                                if(past!=null){
                                    pager.setCurrentItem(1);
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR))
                                progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });
        rq.add(jsonObjReq);
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(OrdersActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrdersActivity.this);
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
