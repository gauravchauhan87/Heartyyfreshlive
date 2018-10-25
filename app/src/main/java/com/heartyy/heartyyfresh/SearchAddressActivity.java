package com.heartyy.heartyyfresh;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.adapter.GooglePlacesAutocompleteAdapter;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.AddressError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Fonts;
import com.heartyy.heartyyfresh.utils.TypefaceSpan;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchAddressActivity extends AppCompatActivity {
    Typeface andBold, bold, italic, light;
    private AutoCompleteTextView autoCompView;
    TextView warnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_search_address));
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
        ViewGroup root = (ViewGroup) findViewById(R.id.search_main);
        Global.setFont(root, andBold);

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        warnText = (TextView) findViewById(R.id.text_warn_address);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(), SearchAddressActivity.this));

        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                String[] stringArray = str.split(",");
                String state = stringArray[stringArray.length - 2];
                String youraddress = str.replaceAll(" ", "+");
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                Double latitude = null;
                Double longtitude = null;
                String postalCode1 = null;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(youraddress, 1);
                    if (addresses.size() > 0) {
                        latitude = addresses.get(0).getLatitude();
                        Double longtitude1 = addresses.get(0).getLongitude();
                        Log.d("latlong...", latitude.toString() + "        " + "          " + longtitude1.toString());

                        addresses = geocoder.getFromLocation(latitude, longtitude1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        // String state = addresses.get(0).getSubAdminArea();
                        String country = addresses.get(0).getCountryName();
                        postalCode1 = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        String StreetAddress = stringArray[0];
                        Intent intent = new Intent(getApplicationContext(), AddDeliveryLocationActivity.class);
                        intent.putExtra("street", StreetAddress);
                        intent.putExtra("zipcode", postalCode1);
                        intent.putExtra("city", city);
                        intent.putExtra("state", state);
                        intent.putExtra("country", country);


                        startActivity(intent);
                        autoCompView.setText("");


                    } else {

                        String temp[] = str.split(",");
                        Intent intent = new Intent(getApplicationContext(), AddDeliveryLocationActivity.class);
                        intent.putExtra("street", temp[0]);
                        intent.putExtra("zipcode", "");
                        intent.putExtra("city", temp[temp.length - 3]);
                        intent.putExtra("state", temp[temp.length - 2]);
                        intent.putExtra("country", temp[temp.length - 1]);

                        startActivity(intent);
                        autoCompView.setText("");


                    }
                } catch (IOException e) {
                    warnText.setVisibility(View.VISIBLE);
                    DatabaseHandler db = new DatabaseHandler(SearchAddressActivity.this);
                    AddressError addressError = db.getAddressError();
                    warnText.setText(addressError.getNoResult());


                    e.printStackTrace();
                }


            }
        });
        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autoCompView.getText().toString().isEmpty()) {
                    TextView warn = (TextView) findViewById(R.id.text_warn_address);
                    warn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (autoCompView.getText().toString().isEmpty()) {
                    TextView warn = (TextView) findViewById(R.id.text_warn_address);
                    warn.setVisibility(View.GONE);
                }
            }
        });
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
