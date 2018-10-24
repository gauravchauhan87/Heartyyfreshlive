package com.heartyy.heartyyfresh.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.heartyy.heartyyfresh.PaymentActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Gaurav on 11/22/2015.
 */
public class CustomCardListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private PaymentActivity activity;
    private int mSelectedItem;
    private List<PaymentCardBean> cardBeanList;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;

    public CustomCardListAdapter(Context context, List<PaymentCardBean> cardBeanList, PaymentActivity activity) {
        this.context = context;
        this.mSelectedItem = 0;
        this.cardBeanList = cardBeanList;
        this.activity = activity;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cardBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return cardBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return cardBeanList.indexOf(cardBeanList.get(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.card_list_items, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.card_item_main);
        Global.setFont(root, andBold);
        final PaymentCardBean data = cardBeanList.get(position);
        TextView cardType = (TextView) rowView.findViewById(R.id.text_card_type);
        TextView cardNumber = (TextView) rowView.findViewById(R.id.text_card_number);
        TextView primaryText = (TextView) rowView.findViewById(R.id.text_card_primary);
        ImageView imagePaymentCardType = (ImageView) rowView.findViewById(R.id.image_card);
        ImageButton deleteBtn = (ImageButton) rowView.findViewById(R.id.delete);
        ImageView view1 = (ImageView) rowView.findViewById(R.id.view1);
        if(!data.getCardGivenName().equalsIgnoreCase("null")){
            cardType.setText(data.getCardGivenName());
        }


        if(!data.getCardLastFourDigit().equalsIgnoreCase("null")){
            cardNumber.setVisibility(View.VISIBLE);
            cardNumber.setText("XXXX-" + data.getCardLastFourDigit());
        }else{
            cardNumber.setVisibility(View.GONE);
        }
        if (data.getIsPrimary().equalsIgnoreCase("YES")) {
            deleteBtn.setVisibility(View.GONE);
            primaryText.setText("Primary");
            view1.setVisibility(View.VISIBLE);
        } else {
            view1.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
            primaryText.setText("");
        }
        if (data.getCardIssuer().equalsIgnoreCase("Visa")) {
            imagePaymentCardType.setImageResource(R.drawable.visa_2x);
        } else if (data.getCardIssuer().equalsIgnoreCase("MasterCard")) {
            imagePaymentCardType.setImageResource(R.drawable.mastercard_2x);
        } else if (data.getCardIssuer().equalsIgnoreCase("Discover")) {
            imagePaymentCardType.setImageResource(R.drawable.discover_2x);
        } else if (data.getCardIssuer().equalsIgnoreCase("JCB")) {
            imagePaymentCardType.setImageResource(R.drawable.jcb_2x);
        } else if (data.getCardIssuer().equalsIgnoreCase("Maestro")) {
            imagePaymentCardType.setImageResource(R.drawable.maestro_2x);
        } else if (data.getCardIssuer().equalsIgnoreCase("American Express")) {
            imagePaymentCardType.setImageResource(R.drawable.amex_2x);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteAlert(data, position);
            }
        });


        return rowView;
    }

    private void showDeleteAlert(final PaymentCardBean data, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptsView = layoutInflater.inflate(R.layout.confirm_card_delete, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();

        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button buttonClose = (Button) promptsView.findViewById(R.id.button_close);
        Button buttonCancelOrder = (Button) promptsView.findViewById(R.id.button_cancel_order);

        titleText.setTypeface(andBold);
        buttonCancelOrder.setTypeface(andBold);
        buttonClose.setTypeface(andBold);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard(data, position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void deleteCard(final PaymentCardBean data, final int position) {
        Global.showProgress(context);
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref",
                context.MODE_PRIVATE);
        RequestQueue rq = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("token", data.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("delete params..", params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "payment/delete", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                               Global.hideProgress();

                                cardBeanList.remove(position);
                                notifyDataSetChanged();
                                if (cardBeanList.size() == 0) {
                                    Button addPaymentMethodBtn = (Button) activity.findViewById(R.id.button_add_payment_method);
                                    ListView cardListView = (ListView) activity.findViewById(R.id.list_payment_cards);
                                    RelativeLayout noCardLayout = (RelativeLayout) activity.findViewById(R.id.layout_no_card);
                                    Button addNoCardPaymentBtn = (Button) activity.findViewById(R.id.button_add_payment);

                                    addNoCardPaymentBtn.setVisibility(View.VISIBLE);
                                    noCardLayout.setVisibility(View.VISIBLE);
                                    cardListView.setVisibility(View.GONE);
                                    addPaymentMethodBtn.setVisibility(View.GONE);
                                }

                                if(Global.paymentCardBean!=null){
                                    if(Global.paymentCardBean.getUserPaymentMethodId().equalsIgnoreCase(data.getUserPaymentMethodId())){
                                        Global.paymentCardBean = null;
                                    }
                                }

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                // showAlert(message);

                            }

                        } catch (JSONException e) {
                           Global.hideProgress();
                            // showAlert("Something went wrong!");
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("response", error.toString());
                if (error instanceof NoConnectionError) {
                    // showAlert(Constants.NO_INTERNET);
                } else {
                    // showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
    }
}
