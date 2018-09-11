package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.AddressError;
import com.heartyy.heartyyfresh.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class GooglePlacesAutocompleteAdapter extends BaseAdapter implements
        Filterable {

    private ArrayList resultList;
    private Context context;
    private Activity activity;

    public GooglePlacesAutocompleteAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return (String) resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();



                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                    try {
                        AutoCompleteTextView autoCompView = (AutoCompleteTextView) activity.findViewById(R.id.autoCompleteTextView);
                        if (autoCompView.getText().toString().isEmpty()) {
                            TextView warn = (TextView) activity.findViewById(R.id.text_warn_address);
                            warn.setVisibility(View.GONE);
                        } else {
                            DatabaseHandler db = new DatabaseHandler(context);
                            AddressError error = db.getAddressError();
                            TextView warn = (TextView) activity.findViewById(R.id.text_warn_address);
                            warn.setVisibility(View.VISIBLE);
                            warn.setText(error.getNoResult());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
        return filter;
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE
                    + Constants.TYPE_AUTOCOMPLETE + Constants.OUT_JSON);
            sb.append("?key=" + Constants.API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(Constants.LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString(
                        "description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString(
                        "description"));
            }
        } catch (JSONException e) {
            Log.d(Constants.LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.autocomplete_list_item, null);
        TextView venuetxt = (TextView) rowView.findViewById(R.id.venue_txt);
        TextView addressTxt = (TextView) rowView.findViewById(R.id.address_txt);
        try {
            String[] locations = resultList.get(position).toString().split(",");

            if (locations.length > 1) {
                String address = "";
                for (int i = 0; i < locations.length; i++) {
                    if (i == 0) {
                        venuetxt.setText(locations[0]);
                    } else {
                        addressTxt.setVisibility(View.VISIBLE);
                        address += locations[i] + ",";
                    }

                }
                addressTxt.setText(address);
            } else {
                venuetxt.setText(locations[0]);
                addressTxt.setVisibility(View.GONE);
            }
            if (resultList.size() == 0 || resultList == null) {
                TextView warn = (TextView) activity.findViewById(R.id.text_warn_address);
                warn.setVisibility(View.VISIBLE);
                DatabaseHandler db = new DatabaseHandler(context);
                AddressError addressError = db.getAddressError();
                warn.setText(addressError.getNoResult());
            }else{
                TextView warn = (TextView) activity.findViewById(R.id.text_warn_address);
                warn.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowView;

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

}
