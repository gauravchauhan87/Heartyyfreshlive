package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.OrderDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.OrderSubstitutionBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitvashist1 on 01/02/16.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.SimpleViewHolder> {
    private static final int COUNT = 100;
    private final Context mContext;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private List<OrderBean> orderBeanList;
    private OrderDetailActivity activity;
    private SharedPreferences pref;
    private String orderType;

    public OrderDetailAdapter(Context mContext, List<OrderBean> orderBeanList, OrderDetailActivity activity, String orderType) {
        this.mContext = mContext;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);
        this.orderBeanList = orderBeanList;
        this.orderType = orderType;
        this.activity = activity;
        pref = mContext.getApplicationContext().getSharedPreferences("MyPref",
                mContext.MODE_PRIVATE);


        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }

    public void changeList(List<OrderBean> orderBeanList){
        this.orderBeanList = orderBeanList;
        notifyDataSetChanged();
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, itemNameText, price, weight, totalPrice, quantity,pastWeight,pastPrice,pastDiscountedPrice,substituteByText,substitute,txtSubstitutedWith;
        public TextView outOfStockText,outStockDetail,outCount,outAmount,subItemName,subWeight,subDiscountPrice,subPrice,subQuantity,subTotalPrice;
        public ImageView itemImage,quantLine,priceLine,pastPriceLine,subPriceLine;
        public RelativeLayout pastWeightLayout,substitutionLayout;
        public ImageButton likeBtn;

        public SimpleViewHolder(View view) {
            super(view);
            itemNameText = (TextView) view.findViewById(R.id.text_item_name);
            price = (TextView) view.findViewById(R.id.text_price);
            weight = (TextView) view.findViewById(R.id.text_weight);
            totalPrice = (TextView) view.findViewById(R.id.text_total_price);
            quantity = (TextView) view.findViewById(R.id.text_quantity);
            itemImage = (ImageView) view.findViewById(R.id.image_item);
            likeBtn = (ImageButton) view.findViewById(R.id.image_like);
            quantLine = (ImageView) view.findViewById(R.id.line2);
            priceLine = (ImageView) view.findViewById(R.id.line3);
            pastWeightLayout = (RelativeLayout) view.findViewById(R.id.layout_past);
            pastWeight = (TextView) view.findViewById(R.id.text_past_weight);
            pastPrice = (TextView) view.findViewById(R.id.text_past_price);
            pastDiscountedPrice = (TextView) view.findViewById(R.id.text_past_discounted_price);
            pastPriceLine = (ImageView) view.findViewById(R.id.line);
            substituteByText = (TextView) view.findViewById(R.id.text_substitute_by);


            outOfStockText = (TextView) view.findViewById(R.id.text_out_of_stock);
            outStockDetail = (TextView) view.findViewById(R.id.text_out_of_stock_detail);
            outAmount = (TextView) view.findViewById(R.id.text_out_amount);
            outCount = (TextView) view.findViewById(R.id.text_out_quant);
            substitutionLayout = (RelativeLayout) view.findViewById(R.id.layout_substitution);
            subItemName = (TextView) view.findViewById(R.id.text_sub_item_name);
            subWeight = (TextView) view.findViewById(R.id.text_sub_weight);
            subDiscountPrice = (TextView) view.findViewById(R.id.text_sub_discounted_price);
            subPrice = (TextView) view.findViewById(R.id.text_sub_price);
            subQuantity = (TextView) view.findViewById(R.id.text_sub_quantity);
            subTotalPrice = (TextView) view.findViewById(R.id.text_sub_total_price);
            subPriceLine = (ImageView) view.findViewById(R.id.line4);

            substitute = (TextView) view.findViewById(R.id.text_substitute);
            txtSubstitutedWith = (TextView) view.findViewById(R.id.text_substituted_with);


        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_item_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {

        holder.itemNameText.setTypeface(robotoLight);
        holder.weight.setTypeface(robotoLight);
        holder.price.setTypeface(regular);
        holder.quantity.setTypeface(regular);
        holder.totalPrice.setTypeface(regular);
        holder.pastWeight.setTypeface(robotoLight);
        holder.pastPrice.setTypeface(robotoLight);
        holder.pastDiscountedPrice.setTypeface(regular);
        holder.substituteByText.setTypeface(robotoLight);
        holder.substitute.setTypeface(regular);
        holder.outOfStockText.setTypeface(regular);
        holder.outStockDetail.setTypeface(regular);
        holder.outCount.setTypeface(regular);
        holder.outAmount.setTypeface(regular);
        holder.txtSubstitutedWith.setTypeface(regular);
        holder.subItemName.setTypeface(robotoLight);
        holder.subWeight.setTypeface(robotoLight);
        holder.subDiscountPrice.setTypeface(robotoLight);
        holder.subPrice.setTypeface(robotoLight);
        holder.subQuantity.setTypeface(robotoLight);
        holder.subTotalPrice.setTypeface(regular);


        final OrderBean data = orderBeanList.get(position);
        if(orderType.equalsIgnoreCase("current")){
            holder.substitutionLayout.setVisibility(View.GONE);
            holder.priceLine.setVisibility(View.GONE);
            holder.outStockDetail.setVisibility(View.GONE);
            holder.outOfStockText.setVisibility(View.GONE);
            holder.outCount.setVisibility(View.GONE);
            holder.outAmount.setVisibility(View.GONE);
            holder.pastWeightLayout.setVisibility(View.GONE);
            holder.quantLine.setVisibility(View.GONE);
            showCurrentDetail(holder, data,position);
        }else{
            holder.price.setVisibility(View.GONE);
            holder.weight.setVisibility(View.GONE);
            showPastDetail(holder, data,position);
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + data.getThumbnail(), ImageLoader.getImageListener(
                        holder.itemImage, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + data.getThumbnail());
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

    }

    private void showPastDetail(final SimpleViewHolder holder, final OrderBean data, final int position) {
        holder.itemNameText.setText(data.getItemName());
        holder.price.setText(data.getRegularPrice());
        holder.weight.setText(data.getSize()+" "+data.getUom());
        holder.substituteByText.setText(data.getSubstitution());


        double unitPrice = Double.parseDouble(data.getRegularPrice());
        String[] uprice = String.format("%.2f",unitPrice).split("\\.");
        if(uprice.length>1){
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + uprice[0] + "</big><sup>" + uprice[1] + "</sup>"));
        }else
        {
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + uprice[0] + "</big><sup>" + "0" + "</sup>"));
        }

        double finalAmount = Double.parseDouble(data.getTotalPrice());
        String[] fprice = String.format("%.2f",finalAmount).split("\\.");
        if(fprice.length>1){
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + fprice[0] + "</big><sup>" + fprice[1] + "</sup>"));
        }else
        {
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + fprice[0] + "</big><sup>" + "0" + "</sup>"));
        }

        String isQuantity = data.getIsOrderedSuppliedQuantitySame();
        String count;
        if(isQuantity.equalsIgnoreCase("NO")){
            holder.quantity.setText(data.getOrderQuantity());
            holder.quantLine.setVisibility(View.VISIBLE);
            holder.pastPriceLine.setVisibility(View.VISIBLE);
            holder.pastPrice.setVisibility(View.VISIBLE);
            count = data.getSuppliedQuantity();
            holder.priceLine.setVisibility(View.VISIBLE);
        }else{
            holder.quantity.setText(data.getOrderQuantity());
            holder.quantLine.setVisibility(View.GONE);
            count = data.getOrderQuantity();
            holder.priceLine.setVisibility(View.GONE);
            holder.pastPriceLine.setVisibility(View.GONE);
            holder.pastPrice.setVisibility(View.GONE);
        }

        if(count.equalsIgnoreCase("0")||count.equalsIgnoreCase("1")){
            holder.pastWeight.setText(data.getSize() + " " + data.getUom());
        }else{
            holder.pastWeight.setText(data.getSuppliedQuantity() + " X " + data.getSize() + " " + data.getUom());
        }
        final DatabaseHandler db = new DatabaseHandler(mContext);

        String isSave = db.getLikeItem(data.getSupplierItemId());
        if (isSave != null) {
            holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }



        String finalPrice = data.getFinalPrice();
        if(!finalPrice.equalsIgnoreCase("0")){
            holder.pastPrice.setVisibility(View.VISIBLE);
            holder.pastPriceLine.setVisibility(View.VISIBLE);
            String distemp1[] = data.getFinalPrice().split("\\.");
            if (distemp1.length > 1) {
                holder.pastDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + distemp1[1] + "</sup>"));
            } else {
                holder.pastDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

            String regtemp1[] = data.getRegularPrice().split("\\.");
            if (regtemp1.length > 1) {
                holder.pastPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
            } else {
                holder.pastPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        }else{
            holder.pastPrice.setVisibility(View.GONE);
            holder.pastPriceLine.setVisibility(View.GONE);
            String regtemp1[] = data.getRegularPrice().split("\\.");
            if (regtemp1.length > 1) {
                holder.pastDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
            } else {
                holder.pastDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        }

        if(data.getOutOfStock().equalsIgnoreCase("YES")){
            holder.outOfStockText.setVisibility(View.VISIBLE);
            holder.outStockDetail.setVisibility(View.VISIBLE);
            holder.outCount.setVisibility(View.VISIBLE);
            holder.outAmount.setVisibility(View.VISIBLE);

            if(data.getIsSubstituted().equalsIgnoreCase("YES")){
                holder.outStockDetail.setVisibility(View.GONE);
            }else{
                holder.outStockDetail.setVisibility(View.VISIBLE);
            }

            holder.outCount.setText(Html.fromHtml("(" + "0" + ")"));
            holder.outAmount.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));

        }else{
            holder.outOfStockText.setVisibility(View.GONE);
            holder.outStockDetail.setVisibility(View.GONE);
            holder.outCount.setVisibility(View.GONE);
            holder.outAmount.setVisibility(View.GONE);
        }


        OrderSubstitutionBean substituionBean = data.getOrderSubstitutionBean();
        if(substituionBean!=null){
            holder.substitutionLayout.setVisibility(View.VISIBLE);
            holder.subItemName.setText(substituionBean.getItemName());
            holder.subQuantity.setText(Html.fromHtml("(" + substituionBean.getOrderQuantity() + ")"));

            String subAmountTemp[] = substituionBean.getTotal().split("\\.");
            if(subAmountTemp.length>1){
                holder.subTotalPrice.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + subAmountTemp[0] + "</big><sup>" + subAmountTemp[1] + "</sup><big>)</big>"));
            }else{
                holder.subTotalPrice.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + subAmountTemp[0] + "</big><sup>" + "0" + "</sup><big>)</big>"));
            }

            String count1 = substituionBean.getOrderQuantity();;
            if(count1.equalsIgnoreCase("0")||count1.equalsIgnoreCase("1")){
                holder.subWeight.setText(substituionBean.getSize() + " " + substituionBean.getUom());
            }else{
                holder.subWeight.setText(substituionBean.getOrderQuantity() + " X " + substituionBean.getSize() + " " + substituionBean.getUom());
            }

            String subfinalPrice = substituionBean.getFinalPrice();
            if(!subfinalPrice.equalsIgnoreCase("0")){
                holder.subPrice.setVisibility(View.VISIBLE);
                holder.subPriceLine.setVisibility(View.VISIBLE);
                String distemp1[] = substituionBean.getFinalPrice().split("\\.");
                if (distemp1.length > 1) {
                    holder.subDiscountPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + distemp1[1] + "</sup>"));
                } else {
                    holder.subDiscountPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }

                String regtemp1[] = substituionBean.getRegularPrice().split("\\.");
                if (regtemp1.length > 1) {
                    holder.subPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
                } else {
                    holder.subPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }

            } else{
                holder.subPrice.setVisibility(View.GONE);
                holder.subPriceLine.setVisibility(View.GONE);
                String regtemp1[] = substituionBean.getRegularPrice().split("\\.");
                if (regtemp1.length > 1) {
                    holder.subDiscountPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
                } else {
                    holder.subDiscountPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }
            }
        }else{
            holder.substitutionLayout.setVisibility(View.GONE);
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                } else {
                    String url;
                    final String supplierId = data.getSupplierItemId();


                    url = "user/saved";
                    JSONObject params = new JSONObject();
                    try {

                        params.put("user_id", userId);
                        JSONArray supplierArray = new JSONArray();
                        supplierArray.put(supplierId);
                        params.put("supplier_item_id", supplierArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("params---", params.toString());


                    RequestQueue rq = Volley.newRequestQueue(mContext);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("response", jsonObject.toString());

                                    try {
                                        String status = jsonObject.getString("status");
                                        if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                            String save = db.getLikeItem(data.getSupplierItemId());
                                            if (save == null) {
                                                db.addLikeItem(data.getSupplierItemId());
                                                activity.finalOrderBeanList.get(position).setIsSave("YES");
                                                activity.refreshadapter();

                                            } else {
                                                db.deleteLikeItem(data.getSupplierItemId());
                                                activity.finalOrderBeanList.get(position).setIsSave("NO");
                                                activity.refreshadapter();
                                            }


                                        } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                            // holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                            data.setIsSave("NO");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                       Global.hideProgress();

                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "Error: " + error.toString());
                           Global.hideProgress();
                            if (error instanceof NoConnectionError) {
                               //   showAlert(Constants.NO_INTERNET);
                            } else {
                               // showAlert(Constants.REQUEST_TIMED_OUT);
                            }
                        }
                    });

// Adding request to request queue
                    rq.add(jsonObjReq);

                }

            }
        });

    }

    private void showCurrentDetail(final SimpleViewHolder holder, final OrderBean data, final int position) {

        holder.itemNameText.setText(data.getItemName());
        holder.weight.setText(data.getSize()+" "+data.getUom());
        holder.substituteByText.setText(data.getSubstitution());

        double unitPrice = Double.parseDouble(data.getRegularPrice());
        String[] uprice = String.format("%.2f",unitPrice).split("\\.");
        if(uprice.length>1){
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + uprice[0] + "</big><sup>" + uprice[1] + "</sup>"));
        }else
        {
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + uprice[0] + "</big><sup>" + "0" + "</sup>"));
        }


        double finalAmount = Double.parseDouble(data.getTotalPrice());
        String[] fprice = String.format("%.2f",finalAmount).split("\\.");
        if(fprice.length>1){
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + fprice[0] + "</big><sup>" + fprice[1] + "</sup>"));
        }else
        {
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + fprice[0] + "</big><sup>" + "0" + "</sup>"));
        }


        holder.quantity.setText(data.getOrderQuantity());
        final DatabaseHandler db = new DatabaseHandler(mContext);

        //String isSave = db.getLikeItem(data.getSupplierItemId());
        String isSave = db.getLikeItem(data.getSupplierItemId());
        if (isSave != null) {
            holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                } else {
                    String url;
                    final String supplierId = data.getSupplierItemId();


                    url = "user/saved";
                    JSONObject params = new JSONObject();
                    try {

                        params.put("user_id", userId);
                        JSONArray supplierArray = new JSONArray();
                        supplierArray.put(supplierId);
                        params.put("supplier_item_id", supplierArray);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("params---", params.toString());


                    RequestQueue rq = Volley.newRequestQueue(mContext);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("response", jsonObject.toString());

                                    try {
                                        String status = jsonObject.getString("status");
                                        if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                            String save = db.getLikeItem(data.getSupplierItemId());
                                            if (save == null) {
                                                db.addLikeItem(data.getSupplierItemId());
                                                activity.finalOrderBeanList.get(position).setIsSave("YES");
                                                activity.refreshadapter();

                                            } else {
                                                db.deleteLikeItem(data.getSupplierItemId());
                                                activity.finalOrderBeanList.get(position).setIsSave("NO");
                                                activity.refreshadapter();
                                            }


                                        } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                            // holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                            data.setIsSave("NO");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                       Global.hideProgress();

                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "Error: " + error.toString());
                           Global.hideProgress();
                            if (error instanceof NoConnectionError) {
                                //  showAlert(Constants.NO_INTERNET);
                            } else {
                                //showAlert(Constants.REQUEST_TIMED_OUT);
                            }
                        }
                    });

// Adding request to request queue
                    rq.add(jsonObjReq);

                }

            }
        });

    }



    @Override
    public int getItemCount() {
        return orderBeanList.size();
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
    }
}
