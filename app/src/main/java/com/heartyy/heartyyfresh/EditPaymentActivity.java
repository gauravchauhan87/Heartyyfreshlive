package com.heartyy.heartyyfresh;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.FourDigitCardFormatWatcher;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

public class EditPaymentActivity extends AppCompatActivity {

    Typeface andBold, bold, italic,light;
    ImageButton paypalBtn;
    EditText editCardLabel, editCardNumber, editCardMonth, editCardYear, editCardCvv, editZipcode;
    TextView nameWarnText, cardNumWarnText, monthWarnText, yearWarnText, cvvWarnText, zipWarnText,primarytext,orText,paymentWarning;
    Button addCardBtn,delCardBtn;
    private SharedPreferences pref;
    private String clientToken;
    private String scanCardNumber, scanCardExpiryMonth, scanCardExpiryYear;
    private CheckBox primary;
    private RelativeLayout scanCardLayout;
    private static int MY_SCAN_REQUEST_CODE = 1;
    private static int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_edit_payment));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.edit_payment_main);
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
        delCardBtn = (Button) findViewById(R.id.button_del_card);
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

        editCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
    }
}
