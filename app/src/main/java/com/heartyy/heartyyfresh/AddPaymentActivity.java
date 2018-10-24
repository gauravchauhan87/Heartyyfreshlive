package com.heartyy.heartyyfresh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.CardError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.FourDigitCardFormatWatcher;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddPaymentActivity extends AppCompatActivity implements PaymentMethodNonceCreatedListener, BraintreeErrorListener {
    Typeface andBold, bold, italic, light;
    ImageButton paypalBtn;
    EditText editCardLabel, editCardNumber, editCardMonth, editCardYear, editCardCvv, editZipcode;
    TextView nameWarnText, cardNumWarnText, monthWarnText, yearWarnText, cvvWarnText, zipWarnText, primarytext, orText, paymentWarning;
    Button addCardBtn;
    private SharedPreferences pref;
    private String clientToken;
    private String scanCardNumber, scanCardExpiryMonth, scanCardExpiryYear;
    // private Braintree braintree;
    private CheckBox primary;
    private RelativeLayout scanCardLayout;
    private static int MY_SCAN_REQUEST_CODE = 1;
    private static int REQUEST_CODE = 2;
    PaymentCardBean paymentCardBean;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_add_payment));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        ViewGroup root = (ViewGroup) findViewById(R.id.add_payment_main);
        Global.setFont(root, andBold);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        paypalBtn = (ImageButton) findViewById(R.id.button_paypal);
        addCardBtn = (Button) findViewById(R.id.button_add_card);
        editCardLabel = (EditText) findViewById(R.id.edit_label);
        editCardNumber = (EditText) findViewById(R.id.edit_card_number);
        editCardMonth = (EditText) findViewById(R.id.edit_card_month);
        editCardYear = (EditText) findViewById(R.id.edit_card_year);
        editCardCvv = (EditText) findViewById(R.id.edit_card_cvv);
        editZipcode = (EditText) findViewById(R.id.edit_zipcode);
        scanCardLayout = (RelativeLayout) findViewById(R.id.layout_scancard);
        primarytext = (TextView) findViewById(R.id.text_primary);
        orText = (TextView) findViewById(R.id.text_or);
        paymentWarning = (TextView) findViewById(R.id.text_payment_warning);
        primary = (CheckBox) findViewById(R.id.check);
        primary.setChecked(false);

        nameWarnText = (TextView) findViewById(R.id.text_warn_name);
        cardNumWarnText = (TextView) findViewById(R.id.text_warn_card_number);
        monthWarnText = (TextView) findViewById(R.id.text_warn_month);
        yearWarnText = (TextView) findViewById(R.id.text_warn_year);
        cvvWarnText = (TextView) findViewById(R.id.text_warn_cvv);
        zipWarnText = (TextView) findViewById(R.id.text_warn_zip);

        primarytext.setTypeface(light);
        orText.setTypeface(light);
        paymentWarning.setTypeface(italic);


        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editCardLabel.getText().toString();
                String cardNumber = editCardNumber.getText().toString();
                String month = editCardMonth.getText().toString();
                String year = editCardYear.getText().toString();
                String cvv = editCardCvv.getText().toString();
                String zip = editZipcode.getText().toString();
                cardNumber = cardNumber.replace(" ", "");
                boolean check = validate(cardNumber, month, year, cvv, zip);
                if (!check) {
                    if (clientToken == null) {
                        showAlert("Could not add your card ");
                    } else {
                        type = "card";
                        addCardToBraintree();
                    }

                }
            }
        });

        scanCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(AddPaymentActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                type = "card";
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });

        paypalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clientToken != null) {
//                    type = "paypal";
//                    Intent intent = new Intent(AddPaymentActivity.this, BraintreePaymentActivity.class);
//                    intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);
//                    startActivityForResult(intent, REQUEST_CODE);

                    PayPal.authorizeAccount(mBraintreeFragment);
                }
            }
        });

        editCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
    }

    private void addCardToBraintree() {
        Global.showProgress(AddPaymentActivity.this);
        CardBuilder card = new CardBuilder();
        card.cardNumber(editCardNumber.getText().toString());
        card.cvv(editCardCvv.getText().toString());
        card.expirationMonth(editCardMonth.getText().toString());
        card.expirationYear(editCardYear.getText().toString());


        Card.tokenize(mBraintreeFragment, card);
//        braintree = Braintree.getInstance(this, clientToken);
//        braintree.addListener(new Braintree.PaymentMethodNonceListener() {
//            @Override
//            public void onPaymentMethodNonce(String paymentMethodNonce) {
//                Log.d("Nonce...>>", paymentMethodNonce);
//                sendNonceToServer(paymentMethodNonce);
//
//            }
//        });
//
//        braintree.addListener(new Braintree.ErrorListener() {
//
//
//            @Override
//            public void onUnrecoverableError(Throwable throwable) {
//
//            }
//
//            @Override
//
//            public void onRecoverableError(ErrorWithResponse error) {
//                Global.dialog.dismiss();
//                showAlert(error.toString());
//
//                Log.d("error..>>", error.toString());
//
//            }
//
//        });
//
//        braintree.tokenize(card);

    }

    private BraintreeFragment mBraintreeFragment;

    private void brainTreeInitialize() {
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(this, clientToken);
        } catch (InvalidArgumentException e) {
            // the authorization provided was of an invalid form
        }


    }

//    public static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .clientId("..(the client id) ")
//            .merchantName("... (the name) ")
//            .rememberUser(true)
//            .acceptCreditCards(true);

    private void sendNonceToServer(String paymentMethodNonce) {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {
            String primay_check;
            if (primary.isChecked()) {
                primay_check = "YES";
            } else {
                primay_check = "NO";

            }
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("is_primary", primay_check);
            params.put("payment_method_nonce", paymentMethodNonce);
            params.put("given_card_name", editCardLabel.getText().toString());
            params.put("zipcode", editZipcode.getText().toString());
            params.put("type", type);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("card ...>>", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "payment", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("nounce response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Global.dialog.dismiss();
                                paymentCardBean = ConversionHelper.convertPaymentCardJsonToPaymentCardBean(jsonObject);
                                showSuccessAlert(jsonObject.getString("message"));


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                Global.dialog.dismiss();
                                showAlert(jsonObject.getString("message"));
//                                if(jsonObject.getString("message").equalsIgnoreCase("Please provide valid zipcode")){
//                                    editZipcode.requestFocus();
//                                }
//                                Handler handler = new Handler();
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
                                generateClientToken();
//                                    }
//                                });
                            }
                        } catch (JSONException e) {
                            Global.dialog.dismiss();
                            showAlert("Something went wrong! please try again later.");
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Global.dialog.dismiss();
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void generateClientToken() {

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {

            params.put("user_id", pref.getString(Constants.USER_ID, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "payment/token", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                clientToken = dataObj.getString("token");
                                brainTreeInitialize();
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                if (jsonObject.getString("message").equalsIgnoreCase("")) {
                                    finish();
                                } else {
                                    showAlert(jsonObject.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    VolleyLog.d("error", "Error: " + error.getMessage().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);

    }

    private boolean validate(String cardNumber, String month, String year, String cvv, String zip) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        CardError cardError = db.getCardError();
        if (cardNumber.isEmpty() && month.isEmpty() && year.isEmpty() && cvv.isEmpty() && zip.isEmpty()) {
            error = true;
            // nameWarnText.setVisibility(View.VISIBLE);
            cardNumWarnText.setVisibility(View.VISIBLE);
            monthWarnText.setVisibility(View.VISIBLE);
            yearWarnText.setVisibility(View.VISIBLE);
            cvvWarnText.setVisibility(View.VISIBLE);
            zipWarnText.setVisibility(View.VISIBLE);
            cardNumWarnText.setText(cardError.getNumberRequired());
            monthWarnText.setText(cardError.getMonthRequired());
            yearWarnText.setText(cardError.getYearRequired());
            cvvWarnText.setText(cardError.getCvvRequired());
            zipWarnText.setText(cardError.getZipcodeRequired());
        } else if (cardNumber.isEmpty() || month.isEmpty() || year.isEmpty() || cvv.isEmpty() || zip.isEmpty()) {

            if (cardNumber.isEmpty()) {
                error = true;
                cardNumWarnText.setVisibility(View.VISIBLE);
                cardNumWarnText.setText(cardError.getNumberRequired());
            } else {
                cardNumWarnText.setVisibility(View.GONE);
            }

            if (month.isEmpty()) {
                error = true;
                monthWarnText.setVisibility(View.VISIBLE);
                monthWarnText.setText(cardError.getMonthRequired());
            } else {
                monthWarnText.setVisibility(View.GONE);
            }

            if (year.isEmpty()) {
                error = true;
                yearWarnText.setVisibility(View.VISIBLE);
                yearWarnText.setText(cardError.getYearRequired());
            } else {
                yearWarnText.setVisibility(View.GONE);
            }

            if (cvv.isEmpty()) {
                error = true;
                cvvWarnText.setVisibility(View.VISIBLE);
                cvvWarnText.setText(cardError.getCvvRequired());
            } else {
                cvvWarnText.setVisibility(View.GONE);
            }

            if (zip.isEmpty()) {
                error = true;
                zipWarnText.setVisibility(View.VISIBLE);
                zipWarnText.setText(cardError.getZipcodeRequired());
            } else {
                zipWarnText.setVisibility(View.GONE);
            }
        } else {
            nameWarnText.setVisibility(View.GONE);
            cardNumWarnText.setVisibility(View.GONE);
            monthWarnText.setVisibility(View.GONE);
            yearWarnText.setVisibility(View.GONE);
            cvvWarnText.setVisibility(View.GONE);
            zipWarnText.setVisibility(View.GONE);

            if (!AccountChecker.checkcardNo(cardNumber)) {
                error = true;
                //showAlert(cardError.getNumberLength());
                showAlert("Enter valid card number");
                editCardNumber.requestFocus();
            } else if (!AccountChecker.checkMonth(month)) {
                error = true;
                showAlert("Invalid expiry month");
                editCardMonth.requestFocus();
            } else if (!AccountChecker.checkYear(year)) {
                error = true;
                showAlert("Invalid expiry year");
                editCardYear.requestFocus();
            } else if (cvv.length() != 3 && cvv.length() != 4) {
                Log.d("cvv...", String.valueOf(cvv.length()));
                error = true;
                //showAlert(cardError.getCvvLength());
                showAlert("Enter valid cvv number");
                editCardCvv.requestFocus();
            } else if (!AccountChecker.checkZip(zip)) {
                error = true;
                //showAlert(cardError.getZipcodeLength());
                showAlert("Enter valid zip code");
                editZipcode.requestFocus();
            } else if (!AccountChecker.checkExpiryDate(month, year)) {
                error = true;
                showAlert("Invalid expiry date");
                editCardMonth.requestFocus();
            }
        }
        return error;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getFormattedCardNumber() + "\n";

                scanCardNumber = scanResult.getFormattedCardNumber();
                scanCardExpiryMonth = String.valueOf(scanResult.expiryMonth);
                scanCardExpiryYear = String.valueOf(scanResult.expiryYear);


                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

                Log.d("card scan,", resultDisplayStr);
                editCardNumber.setText(scanCardNumber.replace(" ", ""));
                editCardMonth.setText(scanCardExpiryMonth);
                editCardYear.setText(scanCardExpiryYear.substring(2));
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultStr);
        } else if (requestCode == REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                String nonce = data.getStringExtra(
//                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
//                );
//                Log.d("paypal nounce", nonce);
//                Global.showProgress(this);
//                sendNonceToServer(nonce);
//                // Send the nonce to your server.
//            }
        }
        // else handle other activity results
    }


    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(AddPaymentActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddPaymentActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);

        if (msg.equalsIgnoreCase("Please provide valid zipcode")) {
            editZipcode.requestFocus();
        } else if (msg.equalsIgnoreCase("Credit card number is invalid.")) {
            editCardNumber.requestFocus();
        } else if (msg.equalsIgnoreCase("CVV must be 4 digits for American Express and 3 digits for other card types.")) {
            editCardCvv.requestFocus();
        } else editCardNumber.requestFocus();

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
                .from(AddPaymentActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                AddPaymentActivity.this);
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
                if (Global.isCompleteBack) {
                    DatabaseHandler db = new DatabaseHandler(AddPaymentActivity.this);
                    List<PaymentCardBean> paymentCardBeanList = db.getAllPaymentCards();
                    if (paymentCardBeanList.size() == 0) {
                        paymentCardBeanList.add(paymentCardBean);
                        db.addAllPaymentCards(paymentCardBeanList);
                    }

                    Global.paymentCardBean = paymentCardBean;
                    Intent intent = new Intent(AddPaymentActivity.this, CompleteOrderActivity.class);
                    if (Global.isCompleteBack) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }

                    if (db.getOrdersCount() > 0) {
                        startActivity(intent);
                        finish();
                        Global.isCompleteBack = false;
                    } else {
                        finish();
                        Global.isCompleteBack = false;
                    }
                } else {
                    finish();
                }
            }
        });
        dialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
        generateClientToken();
//            }
//        });
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Toast.makeText(AddPaymentActivity.this, "success " + paymentMethodNonce.getNonce(), Toast.LENGTH_SHORT).show();
        //todo send information to our service

        sendNonceToServer(paymentMethodNonce.getNonce());
    }

    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            // there was a validation error the user provided data
            Toast.makeText(AddPaymentActivity.this, "error " + error, Toast.LENGTH_SHORT).show();
            generateClientToken();
        }
    }
}