package com.heartyy.heartyyfresh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.tasks.Task;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.UserBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.LoginError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.AccountChecker;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.GPSTracker;
import com.heartyy.heartyyfresh.utils.RegisterBackground;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 2;
    Typeface andBold, bold, italic, light;
    Button forgotPasswordBtn;
    private EditText editEmail, editPassword;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private UserBean loginData;
    private SharedPreferences pref;
    ImageView signinFacebookButton;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    private String fbAuthToken;

    GoogleCloudMessaging gcm;

    private static final int RC_SIGN_IN = 0;
    private static final int RECOVERABLE_REQUEST_CODE = -1;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private String googleAuthToken;
    private List<LocationBean> locationBeanList;
    List<PaymentCardBean> cardBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_sign_in));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        andBold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(), Fonts.ROBOTO_LIGHT);
        ViewGroup root = (ViewGroup) findViewById(R.id.signin_main);
        Global.setFont(root, andBold);
        forgotPasswordBtn = (Button) findViewById(R.id.button_forgot);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        emailErrorText = (TextView) findViewById(R.id.text_warn_email);
        passwordErrorText = (TextView) findViewById(R.id.text_warn_password);
        Button loginBtn = (Button) findViewById(R.id.button_login);
        signinFacebookButton = (ImageView) findViewById(R.id.button_facebook_signin);
        ImageView googleBtn = (ImageView) findViewById(R.id.button_google);
        TextView termsBtn = (TextView) findViewById(R.id.button_terms);
        TextView privacyBtn = (TextView) findViewById(R.id.button_policy);
        TextView orText = (TextView) findViewById(R.id.text_or);
        TextView byRegisterText = (TextView) findViewById(R.id.text_by_logging);
        TextView andText = (TextView) findViewById(R.id.text_and);

        orText.setTypeface(light);
        byRegisterText.setTypeface(light);
        andText.setTypeface(light);

        gcm = GoogleCloudMessaging.getInstance(this);

        new RegisterBackground(gcm, this).execute();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gcm = GoogleCloudMessaging.getInstance(SignInActivity.this);

                new RegisterBackground(gcm, SignInActivity.this).execute();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                if (isNetworkConnected()) {
                    boolean check = validate(email, password);
                    if (!check) {
                        loginToHeartyFresh(email, password, "local","","");
                    }
                } else {
                    showAlert(Constants.NO_INTERNET);
                }
            }
        });

        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "tnc");
                startActivity(intent);
                Global.previousActivity = SignInActivity.this.getClass().getName();
            }
        });
        privacyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LegalTermsActivity.class);
                intent.putExtra("terms", "privacy");
                startActivity(intent);
                Global.previousActivity = SignInActivity.this.getClass().getName();
            }
        });


        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signinFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "user_photos", "public_profile"));
            }
        });

        callbackManager = CallbackManager.Factory.create();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();*/

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int accountCheck = ContextCompat.checkSelfPermission(SignInActivity.this,Manifest.permission.GET_ACCOUNTS);
                if (accountCheck == PackageManager.PERMISSION_GRANTED) {
                    signInWithGplus();
                } else {
                    ActivityCompat.requestPermissions(SignInActivity.this,
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
                }

            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                try {
                    fbAuthToken = currentAccessToken.getToken();
                    String fbUserID = currentAccessToken.getUserId();

                    Log.d("User id: ", fbUserID);
                    Log.d("Access token is: ", fbAuthToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("login result..", loginResult.toString());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", object.toString());
                                try {
                                    String name[] = object.getString("name").split(" ");
                                    accessTokenTracker.stopTracking();
                                    LoginManager.getInstance().logOut();

                                    String fbemail = object.getString("email");
                                    if (fbemail == null || fbemail.isEmpty()) {
                                        showAlert("Please provide Facebook Email Id");
                                    } else {
                                        gcm = GoogleCloudMessaging.getInstance(SignInActivity.this);

                                        new RegisterBackground(gcm, SignInActivity.this).execute();
                                        loginToHeartyFresh(object.getString("email"), "", "facebook", name[0], name[1]);
                                    }

                                } catch (JSONException e) {
                                    showAlert("Please provide Facebook Email Id");
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("login result.error.", exception.toString());
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile == null) {
                    Log.d("first name", oldProfile.getFirstName());
                    Log.d("profile name", oldProfile.getName());
                } else {
                    Log.d("first name", currentProfile.getFirstName());
                    Log.d("profile name", currentProfile.getName());
                }

                // App code
            }
        };
    }

    private void loginToHeartyFresh(String email, String password, final String provider,String fname,String lname) {
        Global.showProgress(SignInActivity.this);
        String deviceToken = pref.getString(Constants.DEVICE_TOKEN,null);

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            JSONObject oauthObj = new JSONObject();
            if (provider.equalsIgnoreCase("facebook")) {
                oauthObj.put("oauth_token", fbAuthToken);
                oauthObj.put("oauth_token_secret", "de8392ac9b81ce744543cf1b550b2609");
            } else if (provider.equalsIgnoreCase("google")) {
                oauthObj.put("oauth_token", googleAuthToken);
                oauthObj.put("oauth_token_secret", "oauth_token_secret");
            } else {
                oauthObj.put("oauth_token", "oauth_token");
                oauthObj.put("oauth_token_secret", "oauth_token_secret");
            }
            params.put("firstname", fname);
            params.put("lastname", lname);
            params.put("email", email);
            params.put("password", password);
            params.put("provider", provider);
            params.put("oauth_credential", oauthObj);
            params.put("device_token", deviceToken);
            params.put("device_type", "android");
            if(Global.zip==null){
                params.put("zipcode", "94536");
            }else{
                params.put("zipcode", Global.zip);
            }

            Log.d("json", params.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + "user/login", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                       Global.hideProgress();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                loginData = ConversionHelper.convertLoginUserJsonToLoginUserBean(jsonObject);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(Constants.FIRST_NAME, loginData.getFirstName());
                                editor.putString(Constants.LAST_NAME, loginData.getLastName());
                                editor.putString(Constants.EMAIL, loginData.getEmail());
                                editor.putString(Constants.USER_ID, loginData.getUserId());
                                editor.putString(Constants.ZIP,loginData.getZip());
                                if (loginData.getPhone() != null) {
                                    if (!loginData.getPhone().equalsIgnoreCase("null")) {
                                        if (loginData.getPhone().length() > 0) {
                                            editor.putString(Constants.PHONE, loginData.getPhone());
                                        }
                                    }
                                }

                                if (!loginData.getPicture().equalsIgnoreCase("null")) {
                                    editor.putString(Constants.PICTURE, loginData.getPicture());
                                }
                                if (provider.equalsIgnoreCase("facebook")) {
                                    editor.putString(Constants.SOCIAL, "facebook");
                                } else if (provider.equalsIgnoreCase("google")) {
                                    editor.putString(Constants.SOCIAL, "google");
                                }
                                editor.apply();

                                getDeliveryAddress();
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showAlert(jsonObject.getString("message"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 3000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           Global.hideProgress();

                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.apply();
                            showAlert("Can not login right now. Please try again!");
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
               Global.hideProgress();
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
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

    private void signInWithGplus() {
        /*if (!mGoogleApiClient.isConnecting()) {
            resolveSignInError();
        }*/
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleProfileInformation(GoogleSignInAccount account) {
        try {
            /*if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                com.google.android.gms.plus.model.people.Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("TAG", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                String name[] = personName.split(" ");
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                gcm = GoogleCloudMessaging.getInstance(SignInActivity.this);

                new RegisterBackground(gcm, SignInActivity.this).execute();
                loginToHeartyFresh(email, "", "google",name[0], name[1]);

            } else {
                Log.d("googleapiclient", "null");
            }*/

            if (account != null) {
                String firstName = account.getGivenName();
                String lastName = account.getFamilyName();
                String email = account.getEmail();
                googleAuthToken = account.getIdToken();

                mGoogleSignInClient.signOut();
                mGoogleSignInClient.revokeAccess();

                gcm = GoogleCloudMessaging.getInstance(SignInActivity.this);
                new RegisterBackground(gcm, SignInActivity.this).execute();
//                Toast.makeText(this, personName +" : "+ email +" : "+ googleAuthToken, Toast.LENGTH_LONG).show();

                loginToHeartyFresh(email, "", "google", firstName, lastName);
            } else {
                Log.d("GoogleSignInClient", "null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            getGoogleProfileInformation(account);
        } catch (ApiException e) {
            Log.w("signInResult", "signInResult:failed code=" + e.getStatusCode());
            getGoogleProfileInformation(null);
        }
    }

    private boolean validate(String email, String password) {
        boolean error = false;
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        LoginError loginError = db.getLoginError();
        if (email.isEmpty() && password.isEmpty()) {
            error = true;

            emailErrorText.setVisibility(View.VISIBLE);
            passwordErrorText.setVisibility(View.VISIBLE);

            emailErrorText.setText(loginError.getEmailRequired());
            passwordErrorText.setText(loginError.getPasswordRequired());

        } else if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                error = true;
                emailErrorText.setVisibility(View.VISIBLE);
                emailErrorText.setText(loginError.getEmailRequired());
            } else {
                emailErrorText.setVisibility(View.GONE);
            }

            if (password.isEmpty()) {
                error = true;
                passwordErrorText.setVisibility(View.VISIBLE);
                passwordErrorText.setText(loginError.getPasswordRequired());
            } else {
                passwordErrorText.setVisibility(View.GONE);
            }
        } else {
            emailErrorText.setVisibility(View.GONE);
            passwordErrorText.setVisibility(View.GONE);

            boolean checkEmail = AccountChecker.checkEmail(email);
            if (!checkEmail) {
                error = true;
                emailErrorText.setVisibility(View.VISIBLE);
                emailErrorText.setText(loginError.getEmailInvalid());
                editEmail.requestFocus();
            }
        }
        return error;
    }
     AlertDialog dialog;
    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(SignInActivity.this);
        @SuppressLint("InflateParams")
        View promptsView = layoutInflater.inflate(R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignInActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(andBold);
        okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null&&dialog.isShowing())
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void showSuccessAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater.from(SignInActivity.this);
        @SuppressLint("InflateParams")
        View promptsView = layoutInflater.inflate(R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignInActivity.this);
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
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result code", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            /*if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }*/

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else if (requestCode == RECOVERABLE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            String oneTimeToken = null;
            if (extra != null) {
                oneTimeToken = extra.getString("authtoken");
            }
            Log.d("authtoken...google.", oneTimeToken);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            return cm.getActiveNetworkInfo() != null;
        } else {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_not_member) {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onConnected(Bundle bundle) {
        GetGooglePlusToken token = new GetGooglePlusToken(this, mGoogleApiClient);
        token.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("connectionerror", "Google plus");
        if (!connectionResult.hasResolution()) {
            *//*GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();*//*
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int status = apiAvailability.isGooglePlayServicesAvailable(this);
            if(status != ConnectionResult.SUCCESS) {
                if(apiAvailability.isUserResolvableError(status)) {
                    apiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
                }

            }
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;


        }
    }*/

    /*protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }*/

    class GetGooglePlusToken extends AsyncTask<Void, Void, String> {
        public GetGooglePlusToken(SignInActivity signInActivity, GoogleApiClient mGoogleApiClient) {

        }

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
  /*      Context context;
        private GoogleApiClient mGoogleApiClient;
        private String TAG = this.getClass().getSimpleName();

        private GetGooglePlusToken(Context context, GoogleApiClient mGoogleApiClient) {
            this.context = context;
            this.mGoogleApiClient = mGoogleApiClient;
        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken1 = null;
            try {

                Bundle bundle = new Bundle();

//                String accountname = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Account accountuser = account.getAccount();
//                String scope = "oauth2:" + Scopes.PLUS_LOGIN + " " + "https://www.googleapis.com/auth/userinfo.email" + " https://www.googleapis.com/auth/plus.profile.agerange.read";
                String scope = "oauth2:" + Scopes.PLUS_ME + " " + Scopes.LEGACY_USERINFO_PROFILE;
                if (accountuser != null) {
                    accessToken1 = GoogleAuthUtil.getToken(context, accountuser, scope);
                }
                googleAuthToken = accessToken1;
                Log.d("google auth token..", googleAuthToken);

                return accessToken1;

            } catch (IOException transientEx) {
                // network or server error, the call is expected to succeed if you try again later.
                // Don't attempt to call again immediately - the request is likely to
                // fail, you'll hit quotas or back-off.
                //TODO: HANDLE
                Log.e(TAG, "transientEx");
                transientEx.printStackTrace();
                accessToken1 = null;

            } catch (UserRecoverableAuthException e) {
                // Recover
                Log.e(TAG, "UserRecoverableAuthException");
                startActivityForResult(e.getIntent(), RECOVERABLE_REQUEST_CODE);
                e.printStackTrace();
                accessToken1 = null;

            } catch (GoogleAuthException authEx) {
                // Failure. The call is not expected to ever succeed so it should not be
                // retried.
                Log.e(TAG, "GoogleAuthException");
                authEx.printStackTrace();
                accessToken1 = null;
            } catch (Exception e) {
                Log.e(TAG, "RuntimeException");
                e.printStackTrace();
                accessToken1 = null;
                throw new RuntimeException(e);
            }
            Log.wtf(TAG, "Code should not go here");
            accessToken1 = null;
            return accessToken1;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d(TAG, "Google access token = " + response);
            getGoogleProfileInformation(account);
        }*/
    }


    private void getDeliveryAddress() {

        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + "address?user_id=" + pref.getString(Constants.USER_ID, null), null, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                locationBeanList = ConversionHelper.convertDeliveryAddressJsonToDeliveryAddressBean(jsonObject);
                                if (locationBeanList == null) {

                                } else if (locationBeanList.size() == 0) {

                                } else {

                                    DatabaseHandler db = new DatabaseHandler(SignInActivity.this);
                                    int count = db.getDeliveryAddressCount();
                                    if (count == 0) {
                                        db.addDeliveryAddress(locationBeanList);
                                    } else {
                                        db.deleteDeliveryAddress();
                                        db.addDeliveryAddress(locationBeanList);
                                    }
                                }

                                getAllPaymentCard();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("error", "Error: " + error.toString());
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);

    }

    private void getAllPaymentCard() {

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
                                cardBeanList = ConversionHelper.covertPaymentCardJsonToPaymentCardApiBean(jsonObject);
                                if (cardBeanList != null) {
                                    if (cardBeanList.size() > 0) {
                                        DatabaseHandler db = new DatabaseHandler(SignInActivity.this);
                                        int count = db.getPaymentCardsCount();
                                        if (count == 0) {
                                            db.addAllPaymentCards(cardBeanList);
                                        } else {
                                            db.deletePaymentCards();
                                            db.addAllPaymentCards(cardBeanList);
                                        }
                                    }
                                }

                               /* DatabaseHandler db = new DatabaseHandler(SignInActivity.this);
                                List<OrderBean> orderBeanList = db.getAllOrders();
                                if(orderBeanList.size()>0){
                                    Intent intent = new Intent(SignInActivity.this, CheckoutActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }else{

                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }*/

                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } /*else if (status.equalsIgnoreCase(Constants.ERROR)) {

                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               Global.hideProgress();
                Log.d("response", error.toString());
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void getCurrentLocation() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
// lat,lng, your current location
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(Constants.LATITUDE), Double.parseDouble(Constants.LONGITUDE), 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String postalCode1 = addresses.get(0).getPostalCode();
                    Log.d("latitude...", String.valueOf(latitude));
                    Log.d("longitude...", String.valueOf(longitude));
//                  Log.d("zipcode...", postalCode1);

                    if (postalCode1 != null) {
                        if (postalCode1.length() == 5) {
                            Global.zip = postalCode1;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gps.showSettingsAlert();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();

                }
                break;

            }

            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signInWithGplus();

                }
                break;

            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        gcm = GoogleCloudMessaging.getInstance(this);

        new RegisterBackground(gcm, this).execute();
        int locationCheck = ContextCompat.checkSelfPermission(SignInActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationCheck == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(SignInActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

    }
}
