package com.heartyy.heartyyfresh.global;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.AisleBean;
import com.heartyy.heartyyfresh.bean.AllCreditsBean;
import com.heartyy.heartyyfresh.bean.CategoryAisleBean;
import com.heartyy.heartyyfresh.bean.CurrentOrderBean;
import com.heartyy.heartyyfresh.bean.EstimatedCostBreakDownBean;
import com.heartyy.heartyyfresh.bean.GlobalBean;
import com.heartyy.heartyyfresh.bean.LocationBean;
import com.heartyy.heartyyfresh.bean.OperatingHourBean;
import com.heartyy.heartyyfresh.bean.PastOrderBean;
import com.heartyy.heartyyfresh.bean.PastOrderSupplierBean;
import com.heartyy.heartyyfresh.bean.PaymentCardBean;
import com.heartyy.heartyyfresh.bean.PopupRatingBean;
import com.heartyy.heartyyfresh.bean.PromotionsApplicableDaysBean;
import com.heartyy.heartyyfresh.bean.RefineBrandBean;
import com.heartyy.heartyyfresh.bean.RefinePriceBean;
import com.heartyy.heartyyfresh.bean.RefineSizeBean;
import com.heartyy.heartyyfresh.bean.SavedSupplierItemBean;
import com.heartyy.heartyyfresh.bean.SearchProductsBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
import com.heartyy.heartyyfresh.bean.SupplierDeliveryScheduleBean;
import com.heartyy.heartyyfresh.bean.TimeIntervalBean;
import com.heartyy.heartyyfresh.bean.TipBean;
import com.heartyy.heartyyfresh.promotionbean.PromotionAvailableBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.card.payment.BuildConfig;

public class Global {
    public static List<AllCreditsBean> allCreditsBeanList;
    public static int backCount = 0;
    public static String backFrom;
    public static String backTo;
    public static boolean changeZip = false;
    public static String combinedDeliverDay = null;
    public static String congestionCost;
    public static List<CurrentOrderBean> currentOrderBeansList;
    public static int datePos = -1;
    public static String dayOfWeek = null;
    public static String devicetoken;
    public static AlertDialog dialog;
    public static EstimatedCostBreakDownBean estimatedCostBreakDownBean;
    public static String estimatedFee = null;
    public static GlobalBean globalBean;
    public static List<Double> individualDeliveryFeeList = new ArrayList();
    public static double individualDeliveryFeeTotal;
    public static boolean isCompleteBack = false;
    public static boolean isDeliveryBack = false;
    public static boolean isFreeDelivery = false;
    public static boolean isIndividualDelivery = false;
    public static boolean isSearchData = false;
    public static boolean isSearchView = false;
    public static LocationBean locationBean;
    public static Map<String, SupplierDeliveryScheduleBean> map = new HashMap();
    public static OperatingHourBean operatingHourBean;
    public static String orderInstructions;
    public static List<PastOrderBean> pastOrderBeansList;
    public static List<PastOrderSupplierBean> pastOrderSupplierBeanList;
    public static PaymentCardBean paymentCardBean;
    public static String phoneNo;
    public static AisleBean popularAisleBean;
    public static int pos = -1;
    public static String previousActivity;
    public static String primaryAddressId;
    public static Map<String, String> promotionApplicableOnDays = new HashMap();
    public static PromotionAvailableBean promotionAvailableBean;
    public static List<String> promotionDeliveryDates = null;
    public static PromotionsApplicableDaysBean promotionsApplicableDaysBean = null;
    public static PopupRatingBean ratingBean;
    public static List<RefineBrandBean> refineBrandBeanList = new ArrayList();
    public static List<RefinePriceBean> refinePriceBeanList = new ArrayList();
    public static List<RefineSizeBean> refineSizeBeanList = new ArrayList();
    public static CategoryAisleBean refineSubAisleBean;
    public static CategoryAisleBean refineTopAisleBean;
    public static String savedSupplierId;
    public static List<SavedSupplierItemBean> savedSupplierItemBeanList;
    public static String savedSupplierName;
    public static List<SearchProductsBean> searchProductsBeanList;
    public static int slotPos = -1;
    public static String sort;
    public static CategoryAisleBean sortedSubAisleBean;
    public static CategoryAisleBean sortedTopAisleBean;
    public static CategoryAisleBean subAisleBean;
    public static SubAisleItemBean subAisleItemBean;
    public static String subCategoryId;
    public static String supplierId;
    public static double taxAmount = 0.0d;
    public static TimeIntervalBean timeIntervalBean;
    public static TipBean tipBean;
    public static CategoryAisleBean topAisleBean;
    public static String topCategoryId;
    public static int totalCartCount = 0;
    public static String zip;

    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = group.getChildAt(i);
            if ((v instanceof TextView) || (v instanceof EditText) || (v instanceof Button)) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, font);
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0) {
                    view.setLayoutParams(new LayoutParams(desiredWidth, -2));
                }
                view.measure(desiredWidth, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
        }
    }

    public static int setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new LayoutParams(desiredWidth, -2));
            }
            view.measure(desiredWidth, 0);
            totalHeight += view.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
        listView.setLayoutParams(params);
        return params.height;
    }

    public static void showProgress(Context context) {
        Typeface andBold = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
        View promptsView = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        dialog = alertDialogBuilder.create();
        TextView loadingText = (TextView) promptsView.findViewById(R.id.text_loading);
        new ProgressBar(context, null, 16842871).getIndeterminateDrawable().setColorFilter(-1, Mode.MULTIPLY);
        loadingText.setTypeface(andBold);
        dialog.show();
    }


    public static void hideProgress()
    {
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }

    public static String FormatAddress(String[] stringArray) {
        String stringAddress = BuildConfig.VERSION_NAME;
        for (int i = 0; i < stringArray.length; i++) {
            if (!stringArray[i].equals(BuildConfig.VERSION_NAME)) {
                if (i == 0) {
                    stringAddress = stringArray[0];
                } else {
                    stringAddress = stringAddress + ", " + stringArray[i];
                }
            }
        }
        return stringAddress;
    }

    public static String replace(String source, String target, String replacement) {
        StringBuilder sbSource = new StringBuilder(source);
        StringBuilder sbSourceLower = new StringBuilder(source.toLowerCase());
        String searchString = target.toLowerCase();
        int idx = 0;
        while (true) {
            idx = sbSourceLower.indexOf(searchString, idx);
            if (idx != -1) {
                sbSource.replace(idx, searchString.length() + idx, replacement);
                sbSourceLower.replace(idx, searchString.length() + idx, replacement);
                idx += replacement.length();
            } else {
                sbSourceLower.setLength(0);
                sbSourceLower.trimToSize();
                return sbSource.toString();
            }
        }
    }
}