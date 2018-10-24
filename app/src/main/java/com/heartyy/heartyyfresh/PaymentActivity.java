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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.CustomCardListAdapter;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    Typeface andBold, bold, italic,light;
    Button addPaymentMethodBtn,addNoCardPaymentBtn;
    ListView cardListView;
    CustomCardListAdapter adapter;
    List<PaymentCardBean> cardBeanList;
    private SharedPreferences pref;
    private RelativeLayout noCardLayout;
    TextView warningText;
    private boolean bottomView = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_payment));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.payment_main);
        Global.setFont(root, andBold);
        addPaymentMethodBtn = (Button) findViewById(R.id.button_add_payment_method);
        cardListView = (ListView) findViewById(R.id.list_payment_cards);
        noCardLayout = (RelativeLayout) findViewById(R.id.layout_no_card);
        addNoCardPaymentBtn = (Button) findViewById(R.id.button_add_payment);
        warningText = (TextView) findViewById(R.id.text_warning);
        addPaymentMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, AddPaymentActivity.class);
                startActivity(intent);
            }
        });
        addNoCardPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, AddPaymentActivity.class);
                startActivity(intent);
            }
        });

        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (Global.isCompleteBack) {
                    PaymentCardBean card = (PaymentCardBean) adapterView.getItemAtPosition(i);
                    Global.paymentCardBean = card;
                    Intent intent = new Intent(PaymentActivity.this, CompleteOrderActivity.class);
                    if(Global.isCompleteBack) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    DatabaseHandler db = new DatabaseHandler(PaymentActivity.this);
                    if(db.getOrdersCount()>0){
                        startActivity(intent);
                        finish();
                        Global.isCompleteBack = false;
                    }else{
                        Global.isCompleteBack = false;
                    }



                }
            }
        });


    }

    private void getAllPaymentCard() {
        Global.showProgress(PaymentActivity.this);

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
                               Global.hideProgress();

                                cardBeanList = ConversionHelper.covertPaymentCardJsonToPaymentCardApiBean(jsonObject);
                                setPaymentCard(cardBeanList);


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                showAlert(message);

                            }

                        } catch (JSONException e) {
                           Global.hideProgress();
                            showAlert("Something went wrong!");
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
    }

    private void setPaymentCard(List<PaymentCardBean> cardBeanList) {
        if (cardBeanList == null || cardBeanList.size() == 0) {
            noCardLayout.setVisibility(View.VISIBLE);
            addNoCardPaymentBtn.setVisibility(View.VISIBLE);
            cardListView.setVisibility(View.GONE);
            addPaymentMethodBtn.setVisibility(View.GONE);

        }else {
            noCardLayout.setVisibility(View.GONE);
            addNoCardPaymentBtn.setVisibility(View.GONE);
            cardListView.setVisibility(View.VISIBLE);
            addPaymentMethodBtn.setVisibility(View.VISIBLE);
            adapter = new CustomCardListAdapter(PaymentActivity.this, cardBeanList,PaymentActivity.this);
            cardListView.setAdapter(adapter);
            DatabaseHandler db = new DatabaseHandler(PaymentActivity.this);
            int count = db.getPaymentCardsCount();
            if (count == 0) {
                db.addAllPaymentCards(cardBeanList);
            } else {
                db.deletePaymentCards();
                db.addAllPaymentCards(cardBeanList);
            }
        }
    }


    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(PaymentActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PaymentActivity.this);
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
                Intent intent = new Intent(PaymentActivity.this,CompleteOrderActivity.class);
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
    public void onResume(){
        super.onResume();
        getAllPaymentCard();
    }
}
