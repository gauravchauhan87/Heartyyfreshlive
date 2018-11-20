package com.heartyy.heartyyfresh;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.heartyy.heartyyfresh.bean.DeliveryEstimateBean;
import com.heartyy.heartyyfresh.bean.EstimatedCostBreakDownBean;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.DirectionsJSONParser;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.heartyy.heartyyfresh.utils.Constants.API_KEY;

public class DeliveryEstimateActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView txtFrom, txtShopName, txtTo, txtZip, txtDist, txtMiles, txtTime, txtMins, txtBreakdown,
            txtChooseStore, txtChangeAdd, txtFuel, txtLabor, txtConvenience, txtEstimate, txtFuelAmt,
            txtLaborAmt, txtConvenienceAmt, txtEstimateAmt;
    TextView textEstimatedDeliveryCost;
    private String supplierId, addressId, id, store, day, zipcode;
    private RelativeLayout chooseStoreLayout, changeAddressLayout;
    Button btnStartShopping;
    DeliveryEstimateBean deliveryEstimateBean;
    EstimatedCostBreakDownBean estimatedCostBreakDownBean;
    Typeface robotoReg, robotoLight;
    private GoogleMap googleMap;
    public static int selectedDeliveryLocationPosition, availableAddressPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_estimate);

        robotoReg = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        robotoLight = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);

        supplierId = Global.supplierId;
        addressId = getIntent().getExtras().getString("address_id");
        id = getIntent().getExtras().getString("id");
        store = getIntent().getExtras().getString("store");
        day = getIntent().getExtras().getString("day");
        if (getIntent().hasExtra("zipcode")) {
            zipcode = getIntent().getExtras().getString("zipcode");
        }

        txtFrom = (TextView) findViewById(R.id.text_from);
        txtShopName = (TextView) findViewById(R.id.text_shop);
        txtChooseStore = (TextView) findViewById(R.id.text_choose_store);
        txtDist = (TextView) findViewById(R.id.text_distance);
        txtMiles = (TextView) findViewById(R.id.text_distance_miles);

        txtTo = (TextView) findViewById(R.id.text_to);
        txtZip = (TextView) findViewById(R.id.text_zip);
        txtChangeAdd = (TextView) findViewById(R.id.text_change_address);
        txtTime = (TextView) findViewById(R.id.text_time);
        txtMins = (TextView) findViewById(R.id.text_time_minuts);

        chooseStoreLayout = (RelativeLayout) findViewById(R.id.layout_store_name);
        changeAddressLayout = (RelativeLayout) findViewById(R.id.layout_change_address);

        txtBreakdown = (TextView) findViewById(R.id.text_breakdown);

        txtFuel = (TextView) findViewById(R.id.text_fuel);
        txtLabor = (TextView) findViewById(R.id.text_labor);
        txtConvenience = (TextView) findViewById(R.id.text_convenience);
        txtEstimate = (TextView) findViewById(R.id.text_estimate);
        txtFuelAmt = (TextView) findViewById(R.id.text_fuel_amt);
        txtLaborAmt = (TextView) findViewById(R.id.text_labor_amt);
        txtConvenienceAmt = (TextView) findViewById(R.id.text_convenience_amt);
        txtEstimateAmt = (TextView) findViewById(R.id.text_estimate_amt);

        textEstimatedDeliveryCost = (TextView) findViewById(R.id.text_estimated_delivery_cost);

        btnStartShopping = (Button) findViewById(R.id.button_start_shopping);

        txtFrom.setTypeface(robotoLight);
        txtShopName.setTypeface(robotoReg);
        txtChooseStore.setTypeface(robotoLight);
        txtDist.setTypeface(robotoLight);
        txtMiles.setTypeface(robotoReg);

        txtTo.setTypeface(robotoLight);
        txtZip.setTypeface(robotoReg);
        txtChangeAdd.setTypeface(robotoLight);
        txtTime.setTypeface(robotoLight);
        txtMins.setTypeface(robotoReg);

        txtBreakdown.setTypeface(robotoLight);

        txtFuel.setTypeface(robotoLight);
        txtFuelAmt.setTypeface(robotoReg);
        txtLabor.setTypeface(robotoLight);
        txtLaborAmt.setTypeface(robotoReg);
        txtConvenience.setTypeface(robotoLight);
        txtConvenienceAmt.setTypeface(robotoReg);
        txtEstimate.setTypeface(robotoLight);
        txtEstimateAmt.setTypeface(robotoReg);

        chooseStoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.isDeliveryBack = true;
                Intent intent = new Intent(DeliveryEstimateActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        changeAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.isDeliveryBack = true;
                Intent intent = new Intent(DeliveryEstimateActivity.this, DeliveryLocationActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("store", store);
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });

        if (!addressId.isEmpty()) {
            showDeliveryDetails(); //if user is coming after changing store or address
        } else { //if user is coming from store listing
            availableAddressPosition = selectedDeliveryLocationPosition;
            setBreakDownDetails(availableAddressPosition);
        }

        btnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DeliveryEstimateActivity.this, StoreDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("store", store);
                intent.putExtra("day", day);
                startActivity(intent);
                finish();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    void setBreakDownDetails(int pos) {
        DatabaseHandler db = new DatabaseHandler(DeliveryEstimateActivity.this);
        LocationBean locationBean = db.getAllDeliveryaddress().get(0);
        estimatedCostBreakDownBean = Global.estimatedCostBreakDownBean;
        txtShopName.setText(estimatedCostBreakDownBean.getFrom());
        //txtZip.setText(estimatedCostBreakDownBean.getTo());
        txtZip.setText(locationBean.getAddress1() + "," + "\n" + locationBean.getCity() + ", " + locationBean.getState() + "," + "\n" +
                estimatedCostBreakDownBean.getTo());

        Double milesDouble = Double.parseDouble(estimatedCostBreakDownBean.getEstimateDistance());
        txtMiles.setText(String.format("%.2f", milesDouble) + " Miles");

        //Double timeDouble = Double.parseDouble(estimatedCostBreakDownBean.getEstimateTime());
        txtMins.setText(estimatedCostBreakDownBean.getEstimateTime() + " min");

        Double estimatedDeliveryCostDouble = Double.parseDouble(estimatedCostBreakDownBean.getDeliveryEstimateCost());
        textEstimatedDeliveryCost.setText("$" + String.format("%.2f", estimatedDeliveryCostDouble));
        setEstimateCostBreakDown();

        /*try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private void setEstimateCostBreakDown() {
        Double fuelAmountDouble = Double.parseDouble(estimatedCostBreakDownBean.getFuelCost());
        txtFuelAmt.setText("$" + String.format("%.2f", fuelAmountDouble));

        Double labourAmountDouble = Double.parseDouble(estimatedCostBreakDownBean.getLabourCost());
        txtLaborAmt.setText("$" + String.format("%.2f", labourAmountDouble));

        Double convenienceAmountDouble = Double.parseDouble(estimatedCostBreakDownBean.getConvenience());
        txtConvenienceAmt.setText("$" + String.format("%.2f", convenienceAmountDouble));

        Double estimateAmountDouble = Double.parseDouble(estimatedCostBreakDownBean.getDeliveryEstimateCost());
        txtEstimateAmt.setText("$" + String.format("%.2f", estimateAmountDouble));
    }

    public void showDeliveryDetails() {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        String url = "delivery/estimate";
        try {
            params.put("supplier_id", supplierId);
            params.put("address_id", addressId);
            params.put("serving_zipcode", zipcode);
            Log.d("json", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                availableAddressPosition = selectedDeliveryLocationPosition;
                                deliveryEstimateBean = ConversionHelper.convertDeliveryEstimateJsonToDeliveryEstimateBean(jsonObject);
                                estimatedCostBreakDownBean = deliveryEstimateBean.getEstimatedCostBreakDownBean();
                                txtShopName.setText(estimatedCostBreakDownBean.getFrom());
                                txtZip.setText(estimatedCostBreakDownBean.getAddressTo());

                                Double milesDouble = Double.parseDouble(estimatedCostBreakDownBean.getEstimateDistance());
                                txtMiles.setText(String.format("%.2f", milesDouble) + " Miles");

                                Double timeDouble = Double.parseDouble(estimatedCostBreakDownBean.getEstimateTime());
                                txtMins.setText(String.format("%.2f", timeDouble) + " min");

                                Double estimatedDeliveryCostDouble = Double.parseDouble(estimatedCostBreakDownBean.getDeliveryEstimateCost());
                                textEstimatedDeliveryCost.setText("$" + String.format("%.2f", estimatedDeliveryCostDouble));

                                try {
                                    initilizeMap();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Global.zip = zipcode;
                                setEstimateCostBreakDown();
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                showAlert(jsonObject.getString("message"));
                                setBreakDownDetails(availableAddressPosition);

                                //btnStartShopping.setEnabled(false);
                                //btnStartShopping.setBackgroundResource(R.drawable.button_disable);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showAlert(Constants.SERVER_ERROR);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
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

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(DeliveryEstimateActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                DeliveryEstimateActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        //titleText.setTypeface(andBold);
        //okBtn.setTypeface(andBold);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initilizeMap() {
        if (googleMap != null) {
            /*SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }*/

            googleMap.setMapType(1);
            if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                try {
                    if (this.estimatedCostBreakDownBean.getLatLongBean() != null) {
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLatitude()), Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLongitude())));
                        marker.icon(BitmapDescriptorFactory.defaultMarker(330));
                        MarkerOptions marker1 = new MarkerOptions().position(new LatLng(Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getDestinationLatitude()), Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getDestinationLongitude())));
                        marker1.icon(BitmapDescriptorFactory.defaultMarker(330));
                        googleMap.addMarker(marker);
                        googleMap.addMarker(marker1);
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLatitude()), Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLongitude()))).zoom(11).build()));
                        String url = getDirectionsUrl(new LatLng(Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLatitude()), Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getSourceLongitude())), new LatLng(Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getDestinationLatitude()), Double.parseDouble(estimatedCostBreakDownBean.getLatLongBean().getDestinationLongitude())));
                        new DownloadTask().execute(new String[]{url});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.googleMap == null) {
                    Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
    }

    public void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        initilizeMap();
    }

    /**
     * A method to download json data from url
     */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";

        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(ContextCompat.getColor(DeliveryEstimateActivity.this,R.color.hearty_green));
                }
            } catch (Exception ex) {
                showAlert("Unable to get the route.");
            }
            // Drawing polyline in the Google Map for the i-th route
            try {
                googleMap.addPolyline(lineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + API_KEY;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("url..", url);

        return url;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
