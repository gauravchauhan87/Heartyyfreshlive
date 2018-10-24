package com.heartyy.heartyyfresh;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.heartyy.heartyyfresh.adapter.ProfileNotificationAdapter;
import com.heartyy.heartyyfresh.bean.AllCreditsBean;
import com.heartyy.heartyyfresh.bean.SupplierDeliveryScheduleBean;
import com.heartyy.heartyyfresh.bean.UserCreditsBean;
import com.heartyy.heartyyfresh.bean.UserProfileBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.SignupError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ProfileActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    Typeface andBold, bold, italic, light;
    ListView notificationListView;
    private ProfileNotificationAdapter adapter;
    Button priceBtn, changePasswordBtn, promocodeBtn, saveBtn;
    private EditText editFirstName, editLastName, editEmail, editMobile;
    private TextView fnameErrorText, emailErrorText, mobileErrorText, andText, copyRightText, addPhotoText;
    private RelativeLayout logoutLayout;
    private SharedPreferences pref;
    private UserProfileBean userProfileBean;
    private UserCreditsBean userCreditsBean;

    private ImageButton gallerybtn;
    private ImageButton camerabtn;
    private Button cancelCameraBtn;
    private ImageButton deleteBtn;

    private boolean bottomView = false;
    private LinearLayout bottom;
    private ImageView userImage;
    private String base64Image;
    private static int RESULT_LOAD_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri; // file url to store image/video
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private TextView termsBtn, privacyBtn, creditsText, notificationText, personalInfoText;
    private boolean isEdit = false;

    List<AllCreditsBean> allCreditsBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_profile);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_profile));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        ViewGroup root = (ViewGroup) findViewById(R.id.profile_main);
        Global.setFont(root, andBold);


        notificationListView = (ListView) findViewById(R.id.list_meal_notifications);
        priceBtn = (Button) findViewById(R.id.button_price);
        changePasswordBtn = (Button) findViewById(R.id.button_change_password);
        promocodeBtn = (Button) findViewById(R.id.button_promocode);

        editFirstName = (EditText) findViewById(R.id.edit_firstname);
        editLastName = (EditText) findViewById(R.id.edit_lastname);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editMobile = (EditText) findViewById(R.id.edit_phone);
        gallerybtn = (ImageButton) findViewById(R.id.image_button_gallery);
        camerabtn = (ImageButton) findViewById(R.id.image_button_camera);
        deleteBtn = (ImageButton) findViewById(R.id.image_button_delete);
        cancelCameraBtn = (Button) findViewById(R.id.image_button_cancel);
        bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        userImage = (ImageView) findViewById(R.id.image_user);
        addPhotoText = (TextView) findViewById(R.id.text_add_photo);
        creditsText = (TextView) findViewById(R.id.text_credits);
        personalInfoText = (TextView) findViewById(R.id.text_personal_info);
        notificationText = (TextView) findViewById(R.id.text_notifications);

        fnameErrorText = (TextView) findViewById(R.id.text_warn_name);
        emailErrorText = (TextView) findViewById(R.id.text_warn_email);
        mobileErrorText = (TextView) findViewById(R.id.text_warn_mobile);
        saveBtn = (Button) findViewById(R.id.button_save);
        logoutLayout = (RelativeLayout) findViewById(R.id.layout_logout);
        termsBtn = (TextView) findViewById(R.id.button_terms);
        privacyBtn = (TextView) findViewById(R.id.button_policy);
        andText = (TextView) findViewById(R.id.text_and);
        copyRightText = (TextView) findViewById(R.id.text_copy_right);
        termsBtn.setTypeface(andBold);
        privacyBtn.setTypeface(andBold);
        priceBtn.setTypeface(light);
        andText.setTypeface(light);
        copyRightText.setTypeface(light);
        creditsText.setTypeface(light);
        personalInfoText.setTypeface(light);
        notificationText.setTypeface(light);
        getUserProfile();
        saveBtn.setVisibility(View.GONE);
        isEdit = false;

        editEmail.setFocusable(false);

        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "tnc");
                startActivity(intent);
                Global.previousActivity = ProfileActivity.this.getClass().getName();
            }
        });
        privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "privacy");
                startActivity(intent);
                Global.previousActivity = ProfileActivity.this.getClass().getName();
            }
        });

        editFirstName.clearFocus();
        editLastName.clearFocus();
        editEmail.clearFocus();
        editMobile.clearFocus();

        editFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setVisibility(View.VISIBLE);
                isEdit = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setVisibility(View.VISIBLE);
                isEdit = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setVisibility(View.VISIBLE);
                isEdit = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setVisibility(View.VISIBLE);
                isEdit = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = editFirstName.getText().toString();
                String lname = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String mobile = editMobile.getText().toString();
                boolean check = validate(fname, lname, email, mobile);
                if (!check) {
                    Global.showProgress(ProfileActivity.this);
                    Global.phoneNo = editMobile.getText().toString();
                    saveProfileData(fname, lname, email, mobile);
                }
            }
        });


        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Global.allCreditsBeanList != null) {
                    Intent intent = new Intent(ProfileActivity.this, CreditsActivity.class);
                    startActivity(intent);
                }
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        promocodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promocodeAlert();
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.pos = -1;
                LoginManager.getInstance().logOut();
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                DatabaseHandler db = new DatabaseHandler(ProfileActivity.this);
                db.deleteDeliveryAddress();
                db.deleteAllOrders();
                db.deleteAllSuppliers();
                db.deletePaymentCards();
                db.deleteAllLike();
                deleteCache(getApplicationContext());
                Global.locationBean = null;
                Global.paymentCardBean = null;
                Global.datePos = -1;
                Global.slotPos = -1;
                Global.tipBean = null;
                Global.orderInstructions = null;
                Global.phoneNo = null;
                Global.isFreeDelivery = false;
                Global.promotionAvailableBean = null;
                Global.congestionCost = null;
                CompleteOrderActivity.creditAmount = 0;
                CompleteOrderActivity.isCreditsApply = true;
                Global.map = new HashMap<String, SupplierDeliveryScheduleBean>();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bottomView) {
                    bottom.setVisibility(View.VISIBLE);
                    Animation animShow = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.profile_slide_up);
                    bottom.startAnimation(animShow);
                    bottomView = true;
                } else {
                    Animation animHide = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.profile_slide_down);
                    bottom.startAnimation(animHide);
                    bottom.setVisibility(View.GONE);
                    bottomView = false;
                }
            }
        });

        cancelCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animHide = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.profile_slide_down);
                bottom.startAnimation(animHide);
                bottom.setVisibility(View.GONE);
                bottomView = false;

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfileImage();
                userImage.setImageResource(R.drawable.user_icon);
                addPhotoText.setText("ADD PHOTO");
                /*SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PATH_PICTURE, null);
                editor.commit();*/

                //addPhotoText.setText("Add Photo");
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                int cameraCheck = ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.CAMERA);

                int storageCheck = ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (cameraCheck == PackageManager.PERMISSION_GRANTED && storageCheck == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    // start the image capture Intent
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else {
                    Log.d("permission", "not granteed");
                    if (cameraCheck != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_READ_CAMERA);

                    } else if (storageCheck != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, "camera"},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    }

                }

            }
        });
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int storageCheck = ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (storageCheck == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, "photos"},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }


            }
        });
    }


    private void saveProfileData(final String fname, final String lname, String email, final String mobile) {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {

            params.put("firstname", fname);
            params.put("lastname", lname);
            params.put("email", email);
            params.put("phone", mobile);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params--->", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Constants.URL + "profile/" + pref.getString(Constants.USER_ID, null), params, //Not null.
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                isEdit = false;
                                SharedPreferences.Editor editor = pref.edit();
                                if (mobile.length() > 0) {
                                    editor.putString(Constants.PHONE, mobile);
                                    editor.putString(Constants.FIRST_NAME,fname);
                                    editor.putString(Constants.LAST_NAME,lname);
                                    editor.apply();
                                    Global.phoneNo = null;
                                }
                                showAlert("Profile has been updated ");

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(message);

                            }
                        } catch (JSONException e) {
                           Global.hideProgress();
                            e.printStackTrace();
                            showAlert("Updating profile failed. Please try after sometime");
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
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
    }


    private void getUserProfile() {
        String url = "profile?user_id=" + pref.getString(Constants.USER_ID, null);
        Global.showProgress(ProfileActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        Log.d("USER_ID", pref.getString(Constants.USER_ID, null));
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            userProfileBean = ConversionHelper.convertUserProfileJsonToUserProfileBean(response);
                            if (userProfileBean.getUserCreditsBean() != null) {
                                userCreditsBean = userProfileBean.getUserCreditsBean();

                                String userCreditAmt = userCreditsBean.getTotalCreditAmount();
                                String userCreditAmtArray[] = userCreditsBean.getTotalCreditAmount().split("\\.");
                                if(userCreditAmtArray.length>1){
                                    userCreditAmt = userCreditAmtArray[0]+"."+userCreditAmtArray[1].substring(0,2);
                                }
                                //priceBtn.setText("$"+userCreditsBean.getTotalCreditAmount());
                                priceBtn.setText("$"+userCreditAmt);

                                Global.allCreditsBeanList = userCreditsBean.getAllCreditsBeanList();
                                SharedPreferences.Editor editor = pref.edit();
                                if (userProfileBean.getPhone() != null) {
                                    if (!userProfileBean.getPhone().equalsIgnoreCase("null")) {
                                        editor.putString(Constants.PHONE, userProfileBean.getPhone());
                                        editor.apply();
                                    }
                                }
                            } else {
                                priceBtn.setText("No credits applied");
                            }
                            setUserProfileDetails();
                        } catch (JSONException e) {
                           Global.hideProgress();
                            e.printStackTrace();
                            showAlert("Something went wrong!. Try again later");
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("response", error.toString());
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }

            }
        });

        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setUserProfileDetails() {
       Global.hideProgress();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + userProfileBean.getPicture(), ImageLoader.getImageListener(
                        userImage, R.drawable.user_icon, R.drawable.user_icon));

                String path = pref.getString(Constants.PICTURE, null);
                if (path == null) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.PICTURE,userProfileBean.getPicture());
                    editor.apply();
                }

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + userProfileBean.getPicture());
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        // handle data, like converting it to xml, json, bitmap etc.,
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    // cached response doesn't exists. Make a network call here
                }

            }
        });
        if (userProfileBean.getPicture().equalsIgnoreCase("null") || userProfileBean.getPicture().length() == 0) {
            addPhotoText.setText("ADD PHOTO");
        } else {
            addPhotoText.setText("EDIT PHOTO");
        }
        editFirstName.setText(userProfileBean.getFirstName());
        editLastName.setText(userProfileBean.getLastName());
        editEmail.setText(userProfileBean.getEmail());
        if (userProfileBean.getPhone().equalsIgnoreCase("null")) {
            if(pref.getString(Constants.PHONE,null)!=null){
                editMobile.setText(pref.getString(Constants.PHONE, null));
                saveBtn.setVisibility(View.VISIBLE);
                isEdit = true;
            }
        } else {
            editMobile.setText(userProfileBean.getPhone());
        }
        if (userProfileBean.getNotification() != null) {
            notificationListView.setVisibility(View.VISIBLE);
            adapter = new ProfileNotificationAdapter(this, userProfileBean.getNotification());
//            notificationListView.setScrollContainer(false);
            notificationListView.setAdapter(adapter);
//            setListViewHeightBasedOnChildren(notificationListView);
        }else{
            notificationListView.setVisibility(View.GONE);
        }

        saveBtn.setVisibility(View.GONE);
        isEdit = false;
    }

    private boolean validate(String fname, String lname, String email, String mobile) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(ProfileActivity.this);
        SignupError signupError = db.getSignupError();
        if (fname.isEmpty() && email.isEmpty() && mobile.isEmpty() && lname.isEmpty()) {
            error = true;
            fnameErrorText.setVisibility(View.VISIBLE);
            emailErrorText.setVisibility(View.VISIBLE);
            mobileErrorText.setVisibility(View.VISIBLE);
            fnameErrorText.setText(R.string.first_last_name);
            emailErrorText.setText(signupError.getEmailRequired());
            mobileErrorText.setText(signupError.getPhoneRequired());

        } else if (fname.isEmpty() || email.isEmpty() || mobile.isEmpty() || lname.isEmpty()) {
            if (fname.isEmpty() && lname.isEmpty()) {
                error = true;
                fnameErrorText.setVisibility(View.VISIBLE);
                fnameErrorText.setText(R.string.first_last_name);
            } else if (fname.isEmpty() || lname.isEmpty()) {
                error = true;
                if (fname.isEmpty()) {
                    fnameErrorText.setVisibility(View.VISIBLE);
                    fnameErrorText.setText(signupError.getNameRequired());
                } else if (lname.isEmpty()) {
                    fnameErrorText.setVisibility(View.VISIBLE);
                    fnameErrorText.setText(signupError.getLastNameRequired());
                }
            } else {
                fnameErrorText.setVisibility(View.GONE);
            }
            if (email.isEmpty()) {
                error = true;
                emailErrorText.setVisibility(View.VISIBLE);
                emailErrorText.setText(signupError.getEmailRequired());
            } else {
                emailErrorText.setVisibility(View.GONE);
            }

            if (mobile.isEmpty()) {
                error = true;
                mobileErrorText.setVisibility(View.VISIBLE);
                mobileErrorText.setText(signupError.getPhoneRequired());
            } else {
                mobileErrorText.setVisibility(View.GONE);
            }
        } else {

            fnameErrorText.setVisibility(View.GONE);
            emailErrorText.setVisibility(View.GONE);
            mobileErrorText.setVisibility(View.GONE);
            boolean checkLastName = AccountChecker.checkNameLength(lname);
            boolean checkFirstName = AccountChecker.checkNameLength(fname);
            boolean checkEmail = AccountChecker.checkEmail(email);
            boolean checkMobile = AccountChecker.checkPhoneNumber(mobile);

            if (!checkFirstName) {
                error = true;
                fnameErrorText.setVisibility(View.VISIBLE);
                fnameErrorText.setText(signupError.getNameLength());
                editFirstName.requestFocus();
            } else if (!checkLastName) {
                error = true;
                fnameErrorText.setVisibility(View.VISIBLE);
                fnameErrorText.setText(signupError.getLastNameLength());
                editLastName.requestFocus();
            } else if (!checkEmail) {
                error = true;
                emailErrorText.setVisibility(View.VISIBLE);
                emailErrorText.setText(signupError.getEmailInvalid());
                editEmail.requestFocus();
            } else if (!checkMobile) {
                error = true;
                mobileErrorText.setVisibility(View.VISIBLE);
                mobileErrorText.setText(signupError.getPhoneInvalid());
                editMobile.requestFocus();
            }
        }
        return error;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg ");
            Log.d("image path...", mediaFile.getAbsolutePath());
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = null;
            if (selectedImage != null) {
                cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
            }
            if (cursor != null) {
                cursor.moveToFirst();
            }

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.image_user);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
           /* SharedPreferences.Editor editor = pref.edit();
            editor.putString(Constants.PATH_PICTURE,picturePath);
            editor.commit();*/


            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            base64Image = Base64.encodeToString(b, Base64.DEFAULT);
            addPhotoText.setText("EDIT PHOTO");
            Animation animHide = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.profile_slide_down);
            bottom.startAnimation(animHide);
            bottom.setVisibility(View.GONE);
            bottomView = false;
            setuserimage();
            // addPhotoText.setText("Edit Photo");

        }
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
                addPhotoText.setText("EDIT PHOTO");
                // addPhotoText.setText("Edit Photo");
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture

                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
               /* SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PATH_PICTURE,null);
                editor.commit();*/
            }
        }

    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            userImage.setImageBitmap(bitmap);

            Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

            byte[] b = baos.toByteArray();

            base64Image = Base64.encodeToString(b, Base64.DEFAULT);

            final Animation animHide = AnimationUtils.loadAnimation(ProfileActivity.this, R.anim.profile_slide_down);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottom.startAnimation(animHide);
                    bottom.setVisibility(View.GONE);
                    bottomView = false;
                }
            }, 3000);


            setuserimage();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void deleteProfileImage() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "profile/image/delete?user_id=" + pref.getString(Constants.USER_ID, null), null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                userImage.setImageResource(R.drawable.user_icon);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Constants.PICTURE, null);
                                editor.apply();

                            }
                        } catch (JSONException e) {
                            /*SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.PATH_PICTURE, null);
                            editor.commit();*/
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               /* SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PATH_PICTURE, null);
                editor.commit();*/
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

    public void setuserimage() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("picture", base64Image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "profile/image", params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                String pic = dataObj.getString("picture");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Constants.PICTURE, pic);
                                editor.apply();

                            }
                        } catch (JSONException e) {
                            /*SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.PATH_PICTURE, null);
                            editor.commit();*/
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               /* SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PATH_PICTURE, null);
                editor.commit();*/
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

    private void promocodeAlert() {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ProfileActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_coupon_code, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        final TextView codeWarnText = (TextView) promptsView.findViewById(R.id.text_code_warn);
        final EditText editCode = (EditText) promptsView.findViewById(R.id.edit_coupon_code);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button applyBtn = (Button) promptsView.findViewById(R.id.button_apply);
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        applyBtn.setTypeface(andBold);
        codeWarnText.setTypeface(andBold);
        editCode.setTypeface(andBold);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editCode.getText().toString().isEmpty()) {
                    codeWarnText.setVisibility(View.VISIBLE);
                    codeWarnText.setText("Please enter a coupon code");
                } else {
                    codeWarnText.setVisibility(View.GONE);
                    applyPromoCode(editCode.getText().toString());
                    dialog.dismiss();
                }

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void applyPromoCode(String promoCode) {
        Global.showProgress(ProfileActivity.this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());

        TimeZone tz = TimeZone.getDefault();

        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            params.put("zipcode", Global.zip);
            params.put("code", promoCode);
            params.put("timezone", tz.getID());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("params---", params.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL + "user/credit/apply", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("delivery response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                               Global.hideProgress();
                                getUserProfile();
                                showAlert(msg);

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                               Global.hideProgress();
                                showAlert(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           Global.hideProgress();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.toString());
               Global.hideProgress();
                if (error instanceof NoConnectionError) {
                   Global.hideProgress();
                    showAlert(Constants.NO_INTERNET);

                } else {
                   Global.hideProgress();
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    int storageCheck = ContextCompat.checkSelfPermission(ProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (storageCheck == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    } else {

                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, "camera"},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                    }
                    return;

                }

            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (permissions[1].equalsIgnoreCase("camera")) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    } else {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }

                }
//
//                else {
//
//                    String per = permissions[0];
//
//                    ActivityCompat.requestPermissions(ProfileActivity.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, per},
//                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//                }
            }

        }
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ProfileActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileActivity.this);
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

    private void showNoEmailAlert() {
        LayoutInflater layoutInflater = LayoutInflater
                .from(ProfileActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button keepInformedBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        keepInformedBtn.setTypeface(andBold);
        titleText.setText("Do you want to save you changes");
        closeBtn.setText("No");
        keepInformedBtn.setText("Yes");
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        keepInformedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = editFirstName.getText().toString();
                String lname = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String mobile = editMobile.getText().toString();
                boolean check = validate(fname, lname, email, mobile);
                if (!check) {
                    Global.showProgress(ProfileActivity.this);
                    saveProfileData(fname, lname, email, mobile);
                }
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private void showSuccessAlert(String msg) {

        LayoutInflater layoutInflater = LayoutInflater
                .from(ProfileActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ProfileActivity.this);
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
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            if (isEdit) {
                showNoEmailAlert();
                return true;
            } else {

                    ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

                    List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                    if(taskList.get(0).numActivities == 1 &&
                            taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                        Log.i("last", "This is last activity in the stack");
                        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        return super.onOptionsItemSelected(item);
                    }

                return false;
            }

        }

        return super.onOptionsItemSelected(item);
    }

}
