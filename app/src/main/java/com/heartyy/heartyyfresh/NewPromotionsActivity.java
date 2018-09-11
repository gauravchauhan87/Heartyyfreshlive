package com.heartyy.heartyyfresh;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

public class NewPromotionsActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    TextView noPromotionText,findDealsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_promotions);

        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);

        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_promtion));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        findDealsText = (TextView) findViewById(R.id.find_deals_text);
        noPromotionText = (TextView) findViewById(R.id.no_promotion_text);

        noPromotionText.setTypeface(andBold);
        findDealsText.setTypeface(light);
    }
}
