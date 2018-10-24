package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class HelpActivity extends AppCompatActivity {
    Typeface andBold, bold, italic,light;
    TextView experienceText, questionsText, sendUsText,faqText,emailText,commentText;
    private EditText editComment;
    Button sendComment;
    private SharedPreferences pref;
    private RelativeLayout editLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_help));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.help_main);
        Global.setFont(root, andBold);

        experienceText = (TextView) findViewById(R.id.text_experience);
        questionsText = (TextView) findViewById(R.id.text_questoins);
        sendUsText = (TextView) findViewById(R.id.text_sendus);
        editComment = (EditText) findViewById(R.id.edit_comment);
        sendComment = (Button) findViewById(R.id.text_send_comment);
        editLayout = (RelativeLayout) findViewById(R.id.layout_edit_comment);
        faqText = (TextView) findViewById(R.id.text_faq);
        emailText = (TextView) findViewById(R.id.text_email);
        commentText = (TextView) findViewById(R.id.text_comment);
        experienceText.setTypeface(italic);
        faqText.setTypeface(light);
        commentText.setTypeface(light);
        emailText.setTypeface(light);

        SpannableString content = new SpannableString("Send us ");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        sendUsText.setText(content);

        SpannableString content1 = new SpannableString("most frequent questions ");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        questionsText.setText(content1);

        experienceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experienceText.setVisibility(View.GONE);
                editLayout.setVisibility(View.VISIBLE);
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editComment.getText().toString();
                if (comment.isEmpty() || comment == null) {
                    showAlert("Please enter your comment");
                } else {
                    Global.showProgress(HelpActivity.this);
                    sendCommentToHeartyy(comment);

                }
            }
        });

        questionsText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                String url = "https://heartyysupport.zendesk.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });

        sendUsText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@heartyy.com"});
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
                return false;
            }
        });
    }

    private void sendCommentToHeartyy(String comment) {

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("comment", comment);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("support params...",params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + "help", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {


                               Global.hideProgress();
                                showShareAlert();


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                showAlert(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            e.printStackTrace();
                            showAlert("Could not post commnet right now. Please try after sometime");
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void showShareAlert() {
        LayoutInflater layoutInflater = LayoutInflater
                .from(HelpActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_experience_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                HelpActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        TextView titleText1 = (TextView) promptsView.findViewById(R.id.text_title_msg1);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(bold);
        titleText1.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(HelpActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                HelpActivity.this);
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
