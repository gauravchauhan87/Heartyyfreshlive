package com.heartyy.heartyyfresh.global;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.promotionbean.PromotionAvailableBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gaurav on 11/23/2015.
 */
public class Global {

    public static AlertDialog dialog;
    public static String previousActivity;
    public static String zip;
    public static GlobalBean globalBean;
    public static List<RefineBrandBean> refineBrandBeanList = new ArrayList<>();
    public static List<RefineSizeBean> refineSizeBeanList = new ArrayList<>();
    public static List<RefinePriceBean> refinePriceBeanList = new ArrayList<>();
    public static SubAisleItemBean subAisleItemBean;
    public static AisleBean popularAisleBean;
    public static CategoryAisleBean topAisleBean;
    public static CategoryAisleBean sortedTopAisleBean;
    public static CategoryAisleBean sortedSubAisleBean;
    public static CategoryAisleBean refineTopAisleBean;
    public static CategoryAisleBean refineSubAisleBean;
    public static CategoryAisleBean subAisleBean;
    public static int totalCartCount = 0;
    public static String backFrom;
    public static String backTo;
    public static int backCount=0;
    public static String sort;
    public static boolean isSearchView = false;
    public static List<CurrentOrderBean> currentOrderBeansList;
    public static List<PastOrderBean> pastOrderBeansList;
    public static LocationBean locationBean;
    public static PaymentCardBean paymentCardBean;
    public static TimeIntervalBean timeIntervalBean;
    public static OperatingHourBean operatingHourBean;
    public static boolean isCompleteBack = false;
    public static int datePos = -1;
    public static int slotPos = -1;
    public static int pos = -1;
    public static boolean changeZip = false;
    public static String subCategoryId;
    public static String topCategoryId;
    public static boolean isSearchData = false;
    public static String supplierId;
    public static boolean isDeliveryBack = false;
    public static String orderInstructions;
    public static String phoneNo;
    public static double taxAmount = 0;
    public static double individualDeliveryFeeTotal;
    public static String dayOfWeek = null;
    public static String combinedDeliverDay = null;
    public static boolean isIndividualDelivery = false;

    public static Map<String,String> promotionApplicableOnDays = new HashMap<String,String>();
    public static PromotionsApplicableDaysBean promotionsApplicableDaysBean = null;
    public static Map<String,SupplierDeliveryScheduleBean> map = new HashMap<>();

    public static List<AllCreditsBean> allCreditsBeanList;

    public static List<SavedSupplierItemBean> savedSupplierItemBeanList;
    public static List<SearchProductsBean> searchProductsBeanList;

    public static String savedSupplierId;
    public static String savedSupplierName;
    public static TipBean tipBean;
    public static String estimatedFee = null;
    public static String congestionCost;

    public static EstimatedCostBreakDownBean estimatedCostBreakDownBean;

    public static List<PastOrderSupplierBean> pastOrderSupplierBeanList;
    public static PromotionAvailableBean promotionAvailableBean;
    public static boolean isFreeDelivery = false;

    public static String devicetoken;
    public static PopupRatingBean ratingBean;

    public static List<Double> individualDeliveryFeeList = new ArrayList<Double>();


    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText
                    || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return params.height;
    }



    public static void showProgress(Context context){

        Typeface  andBold = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Light.ttf");
        LayoutInflater layoutInflater = LayoutInflater
                .from(context);
        View promptsView = layoutInflater.inflate(
                R.layout.progress_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView loadingText = (TextView) promptsView.findViewById(R.id.text_loading);
        ProgressBar spinner = new ProgressBar(
                context,
                null,
                android.R.attr.progressBarStyle);
        spinner.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);

        loadingText.setTypeface(andBold);

        dialog.show();
    }

    public static String FormatAddress(String[] stringArray) {
        String stringAddress = "";
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equals("")) {

            } else {
                if (i == 0) {
                    stringAddress = stringArray[0];
                } else {
                    stringAddress = stringAddress + ", " + stringArray[i];
                }
            }
        }


        return stringAddress;
    }

    public static String replace(String source, String target, String replacement)
    {
        StringBuilder sbSource = new StringBuilder(source);
        StringBuilder sbSourceLower = new StringBuilder(source.toLowerCase());
        String searchString = target.toLowerCase();

        int idx = 0;
        while((idx = sbSourceLower.indexOf(searchString, idx)) != -1) {
            sbSource.replace(idx, idx + searchString.length(), replacement);
            sbSourceLower.replace(idx, idx + searchString.length(), replacement);
            idx+= replacement.length();
        }
        sbSourceLower.setLength(0);
        sbSourceLower.trimToSize();
        sbSourceLower = null;

        return sbSource.toString();
    }
}
