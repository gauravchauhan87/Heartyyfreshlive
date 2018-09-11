package com.heartyy.heartyyfresh.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.heartyy.heartyyfresh.CheckoutActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.*;
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
 * Created by Dheeraj on 1/5/2016.
 */
public class CustomCheckoutAdapter extends RecyclerView.Adapter<CustomCheckoutAdapter.SimpleViewHolder> {

    private static final int COUNT = 100;

    private final Context mContext;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private List<OrderBean> orderBeanList;
    private CheckoutActivity activity;
    private SharedPreferences pref;

    public CustomCheckoutAdapter(Context mContext, List<OrderBean> orderBeanList, CheckoutActivity activity) {
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
        public TextView title, itemNameText, price, weight, totalPrice, quantity,substituteByText;
        public ImageView itemImage;
        public ImageButton plusBtn, minusBtn,likeBtn;

        public SimpleViewHolder(View view) {
            super(view);
            itemNameText = (TextView) view.findViewById(R.id.text_item_name);
            price = (TextView) view.findViewById(R.id.text_price);
            weight = (TextView) view.findViewById(R.id.text_weight);
            totalPrice = (TextView) view.findViewById(R.id.text_total_price);
            quantity = (TextView) view.findViewById(R.id.text_quantity);
            itemImage = (ImageView) view.findViewById(R.id.image_item);
            substituteByText = (TextView) view.findViewById(R.id.text_substitute_by);
            plusBtn = (ImageButton) view.findViewById(R.id.button_plus);
            minusBtn = (ImageButton) view.findViewById(R.id.button_minus);
            likeBtn = (ImageButton) view.findViewById(R.id.image_like);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.checkout_list_item, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        final OrderBean data = orderBeanList.get(position);
        holder.itemNameText.setText(data.getItemName());
        holder.weight.setText(data.getSize()+" "+data.getUom());
        holder.quantity.setText(data.getOrderQuantity());

        double finalPrice = Double.parseDouble(data.getFinalPrice());
        String[] temp = String.format("%.2f",finalPrice).split("\\.");
        if(temp.length>1){
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        }else{
            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp[0] + "</big><sup>" + "0" + "</sup>"));
        }

        String[] temp1 = String.format("%.2f", Double.parseDouble(data.getUnitPrice())).split("\\.");
        if(temp1.length>1){
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
        }else{
            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
        }

        holder.substituteByText.setText(data.getSubstitutionWith());
        final DatabaseHandler db = new DatabaseHandler(mContext);
        db.updateOrder(data);

        holder.substituteByText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                List<SubstituionBean> substituionBeanList = new ArrayList<SubstituionBean>();
                substituionBeanList.add(new SubstituionBean("None", false));
                substituionBeanList.add(new SubstituionBean("Store's choice", false));
                substituionBeanList.add(new SubstituionBean("Similar item any brand", false));
                showSubstitutionAlert(data, substituionBeanList, holder.substituteByText);
                return false;
            }
        });

        String isSave = db.getLikeItem(data.getSupplierItemId());
        if (isSave != null) {
            holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean plusOrderBean = db.getOrder(data.getSupplierItemId());
                if (plusOrderBean != null) {
                    int quantity = Integer.parseInt(plusOrderBean.getOrderQuantity());
                    quantity++;
                    int max = Integer.parseInt(plusOrderBean.getMaxQuantity());
                    if (max > 0 && quantity > max) {
                        showAlert(Constants.MAX_QUANTITY);
                    }else {
                        double unitPrice = Double.parseDouble(plusOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * quantity;
                        plusOrderBean.setOrderQuantity(String.valueOf(quantity));
                        plusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(plusOrderBean);
                        holder.quantity.setText(String.valueOf(quantity));

                        String[] itemPrice = String.valueOf(unitPrice).split("\\.");
                        if (itemPrice.length > 1) {
                            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + itemPrice[0] + "</big><sup>" + itemPrice[1] + "</sup>"));
                        } else {
                            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + itemPrice[0] + "</big><sup>" + "0" + "</sup>"));
                        }

                        String[] totalPrice = String.format("%.2f", finalPrice).split("\\.");
                        if (totalPrice.length > 1) {
                            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + totalPrice[0] + "</big><sup>" + totalPrice[1] + "</sup>"));
                        } else {
                            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + totalPrice[0] + "</big><sup>" + "0" + "</sup>"));
                        }
                        // activity.getCheckoutData();
                        activity.checkStorePromotion("adapter");
                    }

                }


            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean minusOrderBean = db.getOrder(data.getSupplierItemId());
                if (minusOrderBean != null) {
                    int quantity = Integer.parseInt(minusOrderBean.getOrderQuantity());
                    if (quantity == 1) {
                        showAlert(minusOrderBean, db);
                    } else {
                        quantity--;
                        double unitPrice = Double.parseDouble(minusOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * quantity;
                        minusOrderBean.setOrderQuantity(String.valueOf(quantity));
                        minusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(minusOrderBean);
                        holder.quantity.setText(String.valueOf(quantity));

                        String[] itemPrice = String.valueOf(unitPrice).split("\\.");
                        if (itemPrice.length > 1) {
                            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + itemPrice[0] + "</big><sup>" + itemPrice[1] + "</sup>"));
                        } else {
                            holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + itemPrice[0] + "</big><sup>" + "0" + "</sup>"));
                        }

                        String[] totalPrice = String.format("%.2f", finalPrice).split("\\.");
                        if (totalPrice.length > 1) {
                            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + totalPrice[0] + "</big><sup>" + totalPrice[1] + "</sup>"));
                        } else {
                            holder.totalPrice.setText(Html.fromHtml("<sup>$</sup><big>" + totalPrice[0] + "</big><sup>" + "0" + "</sup>"));
                        }


                       // activity.getCheckoutData();
                        activity.checkStorePromotion("adapter");
                    }
                }
            }
        });






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
                                              //  holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
                                                activity.checkStorePromotion("adapter");

                                            } else {
                                                db.deleteLikeItem(data.getSupplierItemId());
                                              //  holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                                activity.checkStorePromotion("adapter");
                                            }


                                        } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                            // holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                                            data.setIsSave("NO");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Global.dialog.dismiss();

                                    }

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("error", "Error: " + error.toString());
                            Global.dialog.dismiss();
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

    private void showAlert(final OrderBean bean, final DatabaseHandler db) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptsView = layoutInflater.inflate(R.layout.dialog_no_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_keep_me_infored);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        okBtn.setText("Remove Item");
        closeBtn.setText("Close");
        titleText.setTypeface(regular);
        okBtn.setTypeface(regular);
        titleText.setText("Are you sure you want to remove this item?");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteOrder(bean);
                List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                for (int k = 0; k < suppliersBeanList.size(); k++) {
                    SuppliersBean suppliersBean = suppliersBeanList.get(k);
                    List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                    if (orderBeanList.size()==0) {
                        db.deleteSupplier(suppliersBean);
                    }
                }


                List<OrderBean> orderList = db.getAllOrders();
                if (orderList.size() > 0) {
                    activity.refreshAdapter();
                } else {
                    activity.finish();
                }
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
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
        titleText.setTypeface(regular);
        okBtn.setTypeface(regular);
        titleText.setText(msg);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void showSubstitutionAlert(final OrderBean data, List<SubstituionBean> substituionBeanList, final TextView substituteByText) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptsView = layoutInflater.inflate(R.layout.substitution_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        final ListView substitutionList = (ListView) promptsView.findViewById(R.id.list_substitutions);
        CustomSubstitutionListAdapter sadapter = new CustomSubstitutionListAdapter(mContext,substituionBeanList);
        substitutionList.setAdapter(sadapter);
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button doneBtn = (Button) promptsView.findViewById(R.id.button_done);
        final SubstituionBean[] bean = new SubstituionBean[1];

        substitutionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean[0] = (SubstituionBean) parent.getItemAtPosition(position);
                CustomSubstitutionListAdapter s = (CustomSubstitutionListAdapter) substitutionList.getAdapter();
                s.setSel(position);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    data.setSubstitutionWith(bean[0].getText());
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    db.updateOrder(data);
                    Log.d("aftrSel",data.getSubstitutionWith());
                    substituteByText.setText(data.getSubstitutionWith());
                    dialog.dismiss();
                }catch (Exception e){
                    showAlert("Please make selection");
                }
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return orderBeanList.size();
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        orderBeanList.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, orderBeanList.size());
    }
}
