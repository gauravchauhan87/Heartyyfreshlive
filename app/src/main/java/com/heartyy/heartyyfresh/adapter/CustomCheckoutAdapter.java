package com.heartyy.heartyyfresh.adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache.Entry;
import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.internal.AnalyticsEvents;
import com.heartyy.heartyyfresh.CheckoutActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SubstituionBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
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

public class CustomCheckoutAdapter extends RecyclerView.Adapter<CustomCheckoutAdapter.SimpleViewHolder> {
    private static final int COUNT = 100;
    private CheckoutActivity activity;
    private final Context mContext;
    private int mCurrentItemId = 0;
    private final List<Integer> mItems;
    private Typeface medium;
    private Typeface meduimItalic;
    private List<OrderBean> orderBeanList;
    private SharedPreferences pref;
    private Typeface regular;
    private Typeface robotoLight;

    public static class SimpleViewHolder extends ViewHolder {
        private ImageView itemImage;
        private TextView itemNameText;
        private ImageButton likeBtn, minusBtn, plusBtn;
        private TextView price, quantity, substituteByText, textPriceDecimal, textPriceDollor, textTotalPriceDollor, totalPrice, totalPriceDecimal, weight;

        private SimpleViewHolder(View view) {
            super(view);
            itemNameText = (TextView) view.findViewById(R.id.text_item_name);
            price = (TextView) view.findViewById(R.id.text_price);
            weight = (TextView) view.findViewById(R.id.text_weight);
            totalPrice = (TextView) view.findViewById(R.id.text_total_price);
            totalPriceDecimal = (TextView) view.findViewById(R.id.text_total_price_decimal);
            quantity = (TextView) view.findViewById(R.id.text_quantity);
            itemImage = (ImageView) view.findViewById(R.id.image_item);
            substituteByText = (TextView) view.findViewById(R.id.text_substitute_by);
            plusBtn = (ImageButton) view.findViewById(R.id.button_plus);
            minusBtn = (ImageButton) view.findViewById(R.id.button_minus);
            likeBtn = (ImageButton) view.findViewById(R.id.image_like);
            textPriceDecimal = (TextView) view.findViewById(R.id.text_price_decimal);
            textPriceDollor = (TextView) view.findViewById(R.id.text_price_dollor);
            textTotalPriceDollor = (TextView) view.findViewById(R.id.text_total_price_dollor);
        }
    }

    public CustomCheckoutAdapter(Context mContext, List<OrderBean> orderBeanList, CheckoutActivity activity) {
        this.mContext = mContext;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(), Fonts.ROBOTO_MEDIUM);
        this.orderBeanList = orderBeanList;
        this.activity = activity;
        pref = mContext.getApplicationContext().getSharedPreferences("MyPref", 0);
        mItems = new ArrayList(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }

    public void changeList(List<OrderBean> orderBeanList) {
        this.orderBeanList = orderBeanList;
        notifyDataSetChanged();
    }

    @NonNull
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.checkout_list_item, parent, false));
    }

    public void onBindViewHolder(@NonNull final SimpleViewHolder holder, int position) {
        final OrderBean data = (OrderBean) orderBeanList.get(position);
        holder.itemNameText.setText(data.getItemName() + "  (" + data.getBrandName() + ")");
        holder.weight.setText(data.getSize() + " " + data.getUom());
        holder.quantity.setText(data.getOrderQuantity());
        holder.textPriceDollor.setTypeface(regular);
        holder.price.setTypeface(regular);
        holder.textPriceDecimal.setTypeface(regular);
        holder.textTotalPriceDollor.setTypeface(regular);
        holder.totalPrice.setTypeface(regular);
        holder.totalPriceDecimal.setTypeface(regular);
        double finalPrice = Double.parseDouble(data.getFinalPrice());
        String[] temp = String.format("%.2f", new Object[]{Double.valueOf(finalPrice)}).split("\\.");
        if (temp.length > 1) {
            holder.totalPrice.setText(temp[0]);
            holder.totalPriceDecimal.setText(temp[1]);
        } else {
            holder.totalPrice.setText(temp[0]);
            holder.totalPriceDecimal.setText("00");
        }
        String[] temp1 = String.format("%.2f", new Object[]{Double.valueOf(Double.parseDouble(data.getUnitPrice()))}).split("\\.");
        if (temp1.length > 1) {
            holder.price.setText(temp1[0]);
            holder.textPriceDecimal.setText(temp1[1]);
        } else {
            holder.price.setText(temp1[0]);
            holder.textPriceDecimal.setText("00");
        }
        holder.substituteByText.setText(data.getSubstitutionWith());
        final DatabaseHandler db = new DatabaseHandler(this.mContext);
        db.updateOrder(data);
        holder.substituteByText.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                List<SubstituionBean> substituionBeanList = new ArrayList();
                substituionBeanList.add(new SubstituionBean("None", false));
                substituionBeanList.add(new SubstituionBean("Store's choice", false));
                substituionBeanList.add(new SubstituionBean("Similar item any brand", false));
                CustomCheckoutAdapter.this.showSubstitutionAlert(data, substituionBeanList, holder.substituteByText);
                return false;
            }
        });
        if (db.getLikeItem(data.getSupplierItemId()) != null) {
            holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }
        holder.plusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean plusOrderBean = db.getOrder(data.getSupplierItemId());
                if (plusOrderBean != null) {
                    int quantity = Integer.parseInt(plusOrderBean.getOrderQuantity()) + 1;
                    int max = Integer.parseInt(plusOrderBean.getMaxQuantity());
                    if (max <= 0 || quantity <= max) {
                        double unitPrice = Double.parseDouble(plusOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * ((double) quantity);
                        plusOrderBean.setOrderQuantity(String.valueOf(quantity));
                        plusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(plusOrderBean);
                        holder.quantity.setText(String.valueOf(quantity));
                        String[] itemPrice = String.valueOf(unitPrice).split("\\.");
                        if (itemPrice.length > 1) {
                            holder.price.setText(itemPrice[0]);
                            holder.textPriceDecimal.setText(itemPrice[1]);
                        } else {
                            holder.price.setText(itemPrice[0]);
                            holder.textPriceDecimal.setText("00");
                        }
                        String[] totalPrice = String.format("%.2f", new Object[]{Double.valueOf(finalPrice)}).split("\\.");
                        if (totalPrice.length > 1) {
                            holder.totalPrice.setText(totalPrice[0]);
                            holder.totalPriceDecimal.setText(totalPrice[1]);
                        } else {
                            holder.totalPrice.setText(totalPrice[0]);
                            holder.totalPriceDecimal.setText("00");
                        }
                        activity.checkStorePromotion("adapter");
                        return;
                    }
                    showAlert(Constants.MAX_QUANTITY);
                }
            }
        });

        holder.minusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean minusOrderBean = db.getOrder(data.getSupplierItemId());
                if (minusOrderBean != null) {
                    int quantity = Integer.parseInt(minusOrderBean.getOrderQuantity());
                    if (quantity == 1) {
                        showAlert(minusOrderBean, db);
                        return;
                    }
                    quantity--;
                    double unitPrice = Double.parseDouble(minusOrderBean.getUnitPrice());
                    double finalPrice = unitPrice * ((double) quantity);
                    minusOrderBean.setOrderQuantity(String.valueOf(quantity));
                    minusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                    db.updateOrder(minusOrderBean);
                    holder.quantity.setText(String.valueOf(quantity));
                    String[] itemPrice = String.valueOf(unitPrice).split("\\.");
                    if (itemPrice.length > 1) {
                        holder.price.setText(itemPrice[0]);
                        holder.textPriceDecimal.setText(itemPrice[1]);
                    } else {
                        holder.price.setText(itemPrice[0]);
                        holder.textPriceDecimal.setText("00");
                    }
                    String[] totalPrice = String.format("%.2f", new Object[]{Double.valueOf(finalPrice)}).split("\\.");
                    if (totalPrice.length > 1) {
                        holder.totalPrice.setText(totalPrice[0]);
                        holder.totalPriceDecimal.setText(totalPrice[1]);
                    } else {
                        holder.totalPrice.setText(totalPrice[0]);
                        holder.totalPriceDecimal.setText("00");
                    }
                    activity.checkStorePromotion("adapter");
                }
            }
        });

        new Handler().post(new Runnable() {
            public void run() {
                AppController.getInstance().getImageLoader().get(Constants.IMG_URL + data.getThumbnail(), ImageLoader.getImageListener(holder.itemImage, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));
                Entry entry = AppController.getInstance().getRequestQueue().getCache().get(Constants.IMG_URL + data.getThumbnail());
                if (entry != null) {
                    try {
                        String str = new String(entry.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        holder.likeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String userId = CustomCheckoutAdapter.this.pref.getString(Constants.USER_ID, null);
                if (userId == null) {
                    holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                    return;
                }
                String supplierId = data.getSupplierItemId();
                String url = "user/saved";
                JSONObject params = new JSONObject();
                try {
                    params.put(Constants.USER_ID, userId);
                    JSONArray supplierArray = new JSONArray();
                    supplierArray.put(supplierId);
                    params.put("supplier_item_id", supplierArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("params---", params.toString());
                Volley.newRequestQueue(mContext).add(new JsonObjectRequest(1, Constants.URL + url, params, new Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                if (db.getLikeItem(data.getSupplierItemId()) == null) {
                                    db.addLikeItem(data.getSupplierItemId());
                                    CustomCheckoutAdapter.this.activity.checkStorePromotion("adapter");
                                    return;
                                }
                                db.deleteLikeItem(data.getSupplierItemId());
                                CustomCheckoutAdapter.this.activity.checkStorePromotion("adapter");
                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {
                                data.setIsSave("NO");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           Global.hideProgress();
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.ERROR, "Error: " + error.toString());
                       Global.hideProgress();
                        if (!(error instanceof NoConnectionError)) {
                        }
                    }
                }));
            }
        });
    }

    private void showAlert(final OrderBean bean, final DatabaseHandler db) {
        View promptsView = LayoutInflater.from(activity).inflate(R.layout.dialog_no_email, null);
        Builder alertDialogBuilder = new Builder(activity);
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
        okBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                db.deleteOrder(bean);
                List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                for (int k = 0; k < suppliersBeanList.size(); k++) {
                    SuppliersBean suppliersBean = (SuppliersBean) suppliersBeanList.get(k);
                    if (db.getAllOrders(suppliersBean).size() == 0) {
                        db.deleteSupplier(suppliersBean);
                    }
                }
                if (db.getAllOrders().size() > 0) {
                    activity.refreshAdapter();
                } else {
                    activity.finish();
                }
                dialog.dismiss();
            }
        });
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showAlert(String msg) {
        View promptsView = LayoutInflater.from(activity).inflate(R.layout.error_dialog, null);
        Builder alertDialogBuilder = new Builder(activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(regular);
        okBtn.setTypeface(regular);
        titleText.setText(msg);
        okBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSubstitutionAlert(OrderBean data, List<SubstituionBean> substituionBeanList, TextView substituteByText) {
        View promptsView = LayoutInflater.from(activity).inflate(R.layout.substitution_layout, null);
        Builder alertDialogBuilder = new Builder(activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        final ListView substitutionList = (ListView) promptsView.findViewById(R.id.list_substitutions);
        substitutionList.setAdapter(new CustomSubstitutionListAdapter(mContext, substituionBeanList));
        Button closeBtn = (Button) promptsView.findViewById(R.id.button_close);
        Button doneBtn = (Button) promptsView.findViewById(R.id.button_done);
        final SubstituionBean[] bean = new SubstituionBean[1];
        substitutionList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean[0] = (SubstituionBean) parent.getItemAtPosition(position);
                ((CustomSubstitutionListAdapter) substitutionList.getAdapter()).setSel(position);
            }
        });
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final OrderBean orderBean = data;
        final TextView textView = substituteByText;
        doneBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    orderBean.setSubstitutionWith(bean[0].getText());
                    new DatabaseHandler(mContext).updateOrder(orderBean);
                    Log.d("aftrSel", orderBean.getSubstitutionWith());
                    textView.setText(orderBean.getSubstitutionWith());
                    dialog.dismiss();
                } catch (Exception e) {
                    showAlert("Please make selection");
                }
            }
        });
        dialog.show();
    }

    public int getItemCount() {
        return orderBeanList.size();
    }

    public void addItem(int position) {
        int id = mCurrentItemId;
        mCurrentItemId = id + 1;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        orderBeanList.remove(position);
        notifyItemRemoved(position);
    }
}
