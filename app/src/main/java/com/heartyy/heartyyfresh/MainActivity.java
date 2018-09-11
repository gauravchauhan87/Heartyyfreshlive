package com.heartyy.heartyyfresh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;

public class MainActivity extends Activity {
    Typeface andBold, bold, italic;
    TextView freshText,deliveredText,localStoreText;
    Button getStartedBtn, alreadyMemberBtn,browseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.LUCIDA_HANDWRITING);
        ViewGroup root = (ViewGroup) findViewById(R.id.tutorial_main);
        Global.setFont(root, andBold);
        freshText = (TextView) findViewById(R.id.text_fresh);
        getStartedBtn = (Button) findViewById(R.id.button_get_started);
        alreadyMemberBtn = (Button) findViewById(R.id.button_already_member);
        browseBtn = (Button) findViewById(R.id.button_browse);
        deliveredText = (TextView) findViewById(R.id.text_eat_delivered);
        localStoreText = (TextView) findViewById(R.id.text_from_local_store);
        freshText.setTypeface(italic);
        getStartedBtn.setTypeface(andBold);
        deliveredText.setTypeface(andBold);
        browseBtn.setTypeface(andBold);
        alreadyMemberBtn.setTypeface(andBold);
        localStoreText.setTypeface(andBold);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                Global.previousActivity = MainActivity.this.getClass().getName();
            }
        });

        alreadyMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                Global.previousActivity = MainActivity.this.getClass().getName();
            }
        });
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ZipCodeActivity.class);
                startActivity(intent);
            }
        });

    }
}
