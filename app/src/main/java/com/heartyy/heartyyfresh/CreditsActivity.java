package com.heartyy.heartyyfresh;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.widget.ListView;

import com.heartyy.heartyyfresh.adapter.CustomCredistListAdapter;
import com.heartyy.heartyyfresh.bean.AllCreditsBean;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import java.util.List;

public class CreditsActivity extends AppCompatActivity {
    Typeface andBold, bold, italic,light;
    ListView creditsListView;
    List<AllCreditsBean> allCreditsBeanList;
    CustomCredistListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_credits));
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


        creditsListView = (ListView) findViewById(R.id.list_credits);
        adapter = new CustomCredistListAdapter(CreditsActivity.this, Global.allCreditsBeanList);
        creditsListView.setAdapter(adapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
