package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.AddDeliveryLocationActivity;
import com.heartyy.heartyyfresh.DeliveryEstimateActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.SignUpActivity;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.errors.AddressError;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class CustomStoreListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    List<String> incredients;
    private List<SuppliersBean> suppliersBeanList;
    private boolean isClosed = false;
    private String hasAddress;
    Typeface andBold, bold, italic, light;
    private SharedPreferences pref;

    public CustomStoreListAdapter(android.content.Context context, List<SuppliersBean> suppliersBeanList, String hasAddress, Activity activity) {
        this.context = context;
        this.mSelectedItem = -1;
        this.suppliersBeanList = suppliersBeanList;
        this.hasAddress = hasAddress;
        this.activity = activity;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);

        pref = this.context.getApplicationContext().getSharedPreferences("MyPref",
                this.context.MODE_PRIVATE);

    }

    public void setSelected(int pos) {
        mSelectedItem = pos;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return suppliersBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return suppliersBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return suppliersBeanList.indexOf(suppliersBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stores_list_items, parent, false);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.store_item_main);
        Global.setFont(root, andBold);
        final SuppliersBean data = suppliersBeanList.get(position);
        TextView storeNameText = (TextView) rowView.findViewById(R.id.text_store_name);
        TextView storeCity = (TextView) rowView.findViewById(R.id.text_store_city);
        TextView storeDeliveryTimeText = (TextView) rowView.findViewById(R.id.text_estimated_delivery_time);
        TextView storeDeliveryFeesText = (TextView) rowView.findViewById(R.id.text_estimated_delivery_fees);
        TextView priceMarkup = (TextView) rowView.findViewById(R.id.text_store_price);
        ImageView checkBox = (ImageView) rowView.findViewById(R.id.check);
        TextView earliestDeliveryText = (TextView) rowView.findViewById(R.id.text_earliest_delivery);
        TextView daytext = (TextView) rowView.findViewById(R.id.text_day);
        TextView byText = (TextView) rowView.findViewById(R.id.text_by);
        ImageView infoImage = (ImageView) rowView.findViewById(R.id.image_info);
        ImageView infoClickImage = (ImageView) rowView.findViewById(R.id.image_info_click);
        LinearLayout estimateLayout = (LinearLayout) rowView.findViewById(R.id.layout_estimate);

        storeCity.setTypeface(light);
        earliestDeliveryText.setTypeface(light);
        byText.setTypeface(light);
        daytext.setTypeface(andBold);
        storeDeliveryTimeText.setTypeface(andBold);
        storeDeliveryFeesText.setTypeface(light);
        priceMarkup.setTypeface(light);

        TextView addDeliveryFeeText = (TextView) rowView.findViewById(R.id.text_add_delivery_fees);
        final SpannableString content = new SpannableString("Add address for delivery fee ");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        addDeliveryFeeText.setText(content);
        storeNameText.setText(data.getSupplierName());
        storeCity.setText(data.getCity());

        String[] time = data.getEstimateDeliveryTime().split(" ");
        daytext.setText(time[0]);
        if (time.length > 2) {
            storeDeliveryTimeText.setText(" " + time[1] + " " + time[2]);
        } else {
            if (time.length > 1)
                storeDeliveryTimeText.setText(" " + time[1]);
        }

        priceMarkup.setText(data.getPriceMarkup());

        if (hasAddress != null) {

            if (hasAddress.equalsIgnoreCase("NO")) {
                storeDeliveryFeesText.setVisibility(View.INVISIBLE);
                addDeliveryFeeText.setVisibility(View.VISIBLE);
                infoImage.setVisibility(View.GONE);
                infoClickImage.setVisibility(View.GONE);
            } else {
                storeDeliveryFeesText.setVisibility(View.VISIBLE);
                addDeliveryFeeText.setVisibility(View.INVISIBLE);
                storeDeliveryFeesText.setText("Est. Delivery $" + data.getEstimateDelivery());
                infoImage.setVisibility(View.VISIBLE);
                infoClickImage.setVisibility(View.VISIBLE);

            }
        } else {
            storeDeliveryFeesText.setVisibility(View.VISIBLE);
            addDeliveryFeeText.setVisibility(View.INVISIBLE);
            infoImage.setVisibility(View.VISIBLE);
            infoClickImage.setVisibility(View.VISIBLE);

        }

        if (data.getComingSoon().equalsIgnoreCase("YES")) {
            estimateLayout.setVisibility(View.GONE);
            rowView.setBackgroundColor(Color.parseColor("#F2F2F2"));
            priceMarkup.setText(data.getComingSoonMessage());
        } else {
            estimateLayout.setVisibility(View.VISIBLE);
        }

        infoClickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.getEstimatedCostBreakDownBean() != null) {

                    Intent intent = new Intent(context, DeliveryEstimateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Global.supplierId = data.getSupplierId();
                    String[] time = data.getEstimateDeliveryTime().split(" ");
                    String day = "";
                    if (time.length > 2) {
                        day = time[0] + " " + "by" + " " + time[1] + " " + time[2];
                    } else {
                        day = time[0] + " " + "by" + " " + time[1];
                    }
                    intent.putExtra("zipcode", Global.zip);
                    intent.putExtra("address_id", "");
                    intent.putExtra("id", data.getSupplierId());
                    intent.putExtra("store", data.getSupplierName());
                    intent.putExtra("day", day);
                    Global.estimatedCostBreakDownBean = data.getEstimatedCostBreakDownBean();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.SUPPLIER_ID, data.getSupplierId());
                    editor.putString(Constants.SUPPLIER_NAME, data.getSupplierName());
                    editor.putString(Constants.APPLICABLE_TAX_RATE, data.getApplicableTaxRate());
                    editor.commit();
                    context.startActivity(intent);
                } else {
                    showAlert(Constants.SERVER_ERROR);
                }
            }
        });

        addDeliveryFeeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userid = pref.getString(Constants.USER_ID, null);
                if (userid != null) {
                    addDeliveryAlert();
                } else {
                    Intent intent = new Intent(context, SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        if (mSelectedItem == position) {
            checkBox.setImageResource(R.drawable.checked_icon);
        } else {
            checkBox.setImageResource(R.drawable.unchecked_icon);
        }


        return rowView;
    }

    private void addDeliveryAlert() {
        LayoutInflater layoutInflater = LayoutInflater
                .from(activity);
        View promptsView = layoutInflater.inflate(
                R.layout.dialog_delivery_estimate, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        final TextView warnText = (TextView) promptsView.findViewById(R.id.text_warn_address);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) promptsView.findViewById(R.id.autoCompleteTextView);

        titleText.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        closeBtn.setTypeface(andBold);
        warnText.setTypeface(andBold);
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(context, activity));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                String str = (String) parent.getItemAtPosition(position);


                String[] stringArray = str.split(",");
                String state = stringArray[stringArray.length - 2];


                String youraddress = str.replaceAll(" ", "+");
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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
                        Intent intent = new Intent(context, AddDeliveryLocationActivity.class);
                        intent.putExtra("street", StreetAddress);
                        intent.putExtra("zipcode", postalCode1);
                        intent.putExtra("city", city);
                        intent.putExtra("state", state);
                        intent.putExtra("country", country);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        autoCompleteTextView.setText("");
                        Global.previousActivity = activity.getClass().getName();


                    } else {

                        String temp[] = str.split(",");
                        Intent intent = new Intent(context, AddDeliveryLocationActivity.class);
                        intent.putExtra("street", temp[0]);
                        intent.putExtra("zipcode", "");
                        intent.putExtra("city", temp[temp.length - 3]);
                        intent.putExtra("state", temp[temp.length - 2]);
                        intent.putExtra("country", temp[temp.length - 1]);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        autoCompleteTextView.setText("");
                        Global.previousActivity = activity.getClass().getName();

                    }
                } catch (IOException e) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    AddressError addressError = db.getAddressError();
                    warnText.setVisibility(View.VISIBLE);
                    warnText.setText(addressError.getNoResult());

                    e.printStackTrace();
                }


            }
        });

        dialog.show();
    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(activity);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
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
}
