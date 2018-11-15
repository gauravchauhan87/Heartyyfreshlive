package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Cache.Entry;
import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.ServerProtocol;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SavedSupplierItemBean;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;
import io.card.payment.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomSavedCardAdapter extends RecyclerView.Adapter<CustomSavedCardAdapter.SimpleViewHolder> {
    private static final int COUNT = 100;
    private Activity activity;
    private final Context mContext;
    private int mCurrentItemId = 0;
    private final List<Integer> mItems;
    private Typeface medium;
    private Typeface meduimItalic;
    private SharedPreferences pref;
    private Typeface regular;
    private Typeface robotoLight;
    private List<SavedSupplierItemBean> savedSupplierItemBeanList;

    public static class SimpleViewHolder extends ViewHolder {
        public TextView brandName;
        public CardView cardView;
        public RelativeLayout cartBtn;
        public TextView cartCount;
        public RelativeLayout cartCountlayout;
        public TextView discountedPrice;
        public ImageView itemImage;
        public ImageButton likeBtn;
        public ImageView lineImage;
        public ImageButton minusBtn;
        public TextView onSale;
        public ImageButton plusBtn;
        public TextView price;
        public TextView size;
        public TextView textDiscountedPriceDecimal;
        public TextView textPriceDecimal;
        public TextView text_price_dollor;
        public TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_item_detail);
            price = (TextView) view.findViewById(R.id.text_price);
            discountedPrice = (TextView) view.findViewById(R.id.text_discounted_price);
            onSale = (TextView) view.findViewById(R.id.text_onsale);
            lineImage = (ImageView) view.findViewById(R.id.image_line);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cartBtn = (RelativeLayout) view.findViewById(R.id.button_cart);
            plusBtn = (ImageButton) view.findViewById(R.id.button_plus);
            minusBtn = (ImageButton) view.findViewById(R.id.button_minus);
            cartCountlayout = (RelativeLayout) view.findViewById(R.id.layout_cart_count);
            cartCount = (TextView) view.findViewById(R.id.text_cart_count);
            itemImage = (ImageView) view.findViewById(R.id.image_item);
            likeBtn = (ImageButton) view.findViewById(R.id.image_like);
            brandName = (TextView) view.findViewById(R.id.text_item_brand);
            size = (TextView) view.findViewById(R.id.text_size);
            textDiscountedPriceDecimal = (TextView) view.findViewById(R.id.text_discounted_price_decimal);
            textPriceDecimal = (TextView) view.findViewById(R.id.text_price_decimal);
            text_price_dollor = (TextView) view.findViewById(R.id.text_price_dollor);
        }
    }

    public CustomSavedCardAdapter(Context context, List<SavedSupplierItemBean> savedSupplierItemBeanList, Activity activity) {
        this.mContext = context;
        this.activity = activity;
        this.robotoLight = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_LIGHT);
        this.regular = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_REGULAR);
        this.meduimItalic = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_MEDIUM_ITALIC);
        this.medium = Typeface.createFromAsset(this.mContext.getAssets(), Fonts.ROBOTO_MEDIUM);
        this.pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        this.savedSupplierItemBeanList = savedSupplierItemBeanList;
        this.mItems = new ArrayList(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.saved_detail_item, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        final SavedSupplierItemBean item = savedSupplierItemBeanList.get(position);
        holder.onSale.setTypeface(meduimItalic);
        holder.title.setTypeface(robotoLight);
        holder.size.setTypeface(robotoLight);
        holder.price.setTypeface(medium);
        holder.discountedPrice.setTypeface(medium);
        holder.brandName.setTypeface(robotoLight);

        holder.title.setText(item.getItemName());
        try {
            holder.brandName.setText(item.getBrand().getBrandName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String quantity = item.getCount();

        if (quantity.equalsIgnoreCase("0")||quantity.equalsIgnoreCase("1")) {
            holder.size.setText(item.getSize() + " " + item.getUom());
        } else {
            holder.size.setText(item.getCount() + " X " + item.getSize() + " " + item.getUom());
        }

        String temp[] = item.getPrice().split("\\.");
        if (temp.length > 1) {
            holder.price.setText(temp[0]);
            holder.textPriceDecimal.setText(temp[1]);
        } else {
            holder.price.setText(temp[0]);
            holder.textPriceDecimal.setText("00");
        }
        final DatabaseHandler db = new DatabaseHandler(mContext);
        OrderBean orderBean = db.getOrder(item.getSupplierItemId());
        if (orderBean != null) {
            holder.cartCountlayout.setVisibility(View.VISIBLE);
            holder.cartCount.setText(orderBean.getOrderQuantity());
            holder.cartBtn.setEnabled(false);
        } else {
            holder.cartCountlayout.setVisibility(View.GONE);
            holder.cartBtn.setEnabled(true);
        }


        String isSave = db.getLikeItem(item.getSupplierItemId());
        if (isSave != null) {
            holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
        }

        holder.likeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String userId = CustomSavedCardAdapter.this.pref.getString(Constants.USER_ID, null);
                String supplierId = item.getSupplierItemId();
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
                Volley.newRequestQueue(CustomSavedCardAdapter.this.mContext).add(new JsonObjectRequest(1, Constants.URL + url, params, new Listener<JSONObject>() {
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("response", jsonObject.toString());
                        try {
                            String status = jsonObject.getString(AnalyticsEvents.PARAMETER_SHARE_DIALOG_CONTENT_STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                if (db.getLikeItem(item.getSupplierItemId()) == null) {
                                    db.addLikeItem(item.getSupplierItemId());
                                    holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
                                    return;
                                }
                                db.deleteLikeItem(item.getSupplierItemId());
                                holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
                            } else if (!status.equalsIgnoreCase(Constants.ERROR)) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }
                    }
                }, new ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Constants.ERROR, "Error: " + error.toString());
                        Global.dialog.dismiss();
                        if (!(error instanceof NoConnectionError)) {
                        }
                    }
                }));
            }
        });
        if (item.getBrand() != null) {
            String brand = item.getBrand().getBrandName();
            if (brand == null) {
                holder.brandName.setVisibility(View.GONE);
            } else {
                holder.brandName.setVisibility(View.VISIBLE);
                holder.brandName.setText("(" + brand + ")");
            }
        } else {
            holder.brandName.setVisibility(View.GONE);
        }
        String onSale = item.getSale();
        String[] temp1;
        String priceTemp;
        if (onSale == null) {
            holder.price.setVisibility(View.GONE);
            holder.text_price_dollor.setVisibility(View.GONE);
            holder.textPriceDecimal.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setText(item.getOffer());
            holder.onSale.setVisibility(View.INVISIBLE);
            temp1 = item.getPrice().split("\\.");
            priceTemp = item.getPrice();
            if (item.getSubIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || item.getIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                item.setTaxAmount(Double.parseDouble(this.pref.getString(Constants.APPLICABLE_TAX_RATE, null)) * Double.parseDouble(priceTemp));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText(temp[1]);
            } else {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText("00");
            }
        } else if (onSale.equalsIgnoreCase("null")) {
            holder.price.setVisibility(View.GONE);
            holder.text_price_dollor.setVisibility(View.GONE);
            holder.textPriceDecimal.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setText(item.getOffer());
            holder.onSale.setVisibility(View.INVISIBLE);
            temp1 = item.getPrice().split("\\.");
            priceTemp = item.getPrice();
            if (item.getSubIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || item.getIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                item.setTaxAmount(Double.parseDouble(this.pref.getString(Constants.APPLICABLE_TAX_RATE, null)) * Double.parseDouble(priceTemp));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText(temp[1]);
            } else {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText("00");
            }
        } else if (onSale.equalsIgnoreCase("no")) {
            holder.price.setVisibility(View.GONE);
            holder.text_price_dollor.setVisibility(View.GONE);
            holder.textPriceDecimal.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            if (item.getOffer().equalsIgnoreCase("null")) {
                holder.onSale.setVisibility(View.INVISIBLE);
            } else {
                holder.onSale.setVisibility(View.VISIBLE);
            }
            holder.onSale.setText(item.getOffer());
            temp1 = item.getPrice().split("\\.");
            priceTemp = item.getPrice();
            if (item.getSubIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || item.getIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                item.setTaxAmount(Double.parseDouble(this.pref.getString(Constants.APPLICABLE_TAX_RATE, null)) * Double.parseDouble(priceTemp));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText(temp[1]);
            } else {
                holder.discountedPrice.setText(temp[0]);
                holder.textDiscountedPriceDecimal.setText("00");
            }
        } else if (onSale.equalsIgnoreCase("yes")) {
            holder.price.setVisibility(View.VISIBLE);
            holder.text_price_dollor.setVisibility(View.GONE);
            holder.textPriceDecimal.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.VISIBLE);
            holder.onSale.setVisibility(View.VISIBLE);
            holder.onSale.setText(item.getOffer());
            if (!item.getSalePrice().equalsIgnoreCase("null")) {
                temp1 = item.getSalePrice().split("\\.");
                priceTemp = item.getSalePrice();
                if (item.getSubIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || item.getIsTaxApplicable().equalsIgnoreCase(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                    item.setTaxAmount(Double.parseDouble(this.pref.getString(Constants.APPLICABLE_TAX_RATE, null)) * Double.parseDouble(priceTemp));
                }
                item.setFinalItemUnitPrice(priceTemp);
                if (temp1.length > 1) {
                    holder.discountedPrice.setText(temp[0]);
                    holder.textDiscountedPriceDecimal.setText(temp[1]);
                } else {
                    holder.discountedPrice.setText(temp[0]);
                    holder.textDiscountedPriceDecimal.setText("00");
                }
            }
        }
        new Handler().post(new Runnable() {
            public void run() {
                AppController.getInstance().getImageLoader().get(Constants.IMG_URL + item.getThumbnail(), ImageLoader.getImageListener(holder.itemImage, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));
                Entry entry = AppController.getInstance().getRequestQueue().getCache().get(Constants.IMG_URL + item.getThumbnail());
                if (entry != null) {
                    try {
                        String str = new String(entry.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = Integer.parseInt(item.getMaxQuantity());
                if(item.getMaxQuantity()==null){
                    item.setMaxQuantity("0");
                } else if(item.getMaxQuantity().equalsIgnoreCase("null")){
                    item.setMaxQuantity("0");
                }
                if (Integer.parseInt(item.getMaxQuantity()) == 0 || Integer.parseInt(item.getMaxQuantity()) > 0) {
                    holder.cartCountlayout.setVisibility(View.VISIBLE);
                    DatabaseHandler db = new DatabaseHandler(mContext);
                    SuppliersBean suppliersBean = db.getSupplier(Global.savedSupplierId);
                    if (suppliersBean == null) {
                        SuppliersBean newSuppliersBean = new SuppliersBean();
                        newSuppliersBean.setSupplierId(Global.savedSupplierId);
                        newSuppliersBean.setSupplierName(Global.savedSupplierName);
                        db.addSupplier(newSuppliersBean);
                    }
                    OrderBean orderBean = db.getOrder(item.getSupplierItemId());
                    if (orderBean == null) {
                        OrderBean newOrder = new OrderBean();
                        newOrder.setSupplierItemId(item.getSupplierItemId());
                        newOrder.setItemName(item.getItemName());
                        newOrder.setSize(item.getSize());
                        newOrder.setShippingWeight(item.getShippingWeight());
                        newOrder.setUnitPrice(item.getFinalItemUnitPrice());
                        newOrder.setFinalPrice(item.getFinalItemUnitPrice());
                        String supplierID = Global.savedSupplierId;
                        newOrder.setThumbnail(item.getThumbnail());
                        newOrder.setSupplierId(supplierID);
                        if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxApplicable().equalsIgnoreCase("true")) {
                            newOrder.setIsTaxable("TRUE");
                            newOrder.setTaxAmount(item.getTaxAmount());
                        } else {
                            newOrder.setIsTaxable("FALSE");
                            newOrder.setTaxAmount(0);
                        }
                        newOrder.setRegularPrice(item.getPrice());
                        newOrder.setSubstitutionWith("Store's choice");
                        if (item.getBrand() != null) {
                            newOrder.setBrandName(item.getBrand().getBrandName());
                        } else {
                            newOrder.setBrandName("");
                        }
                        newOrder.setOrderQuantity("1");
                        newOrder.setApplicableSaleDescription("");
                        newOrder.setUom(item.getUom());
                        newOrder.setMaxQuantity(item.getMaxQuantity());
                        db.addOrderInCart(newOrder);
                    }
                    holder.cartCount.setText("1");
                    item.setIsCartShown(true);
                    holder.cartBtn.setEnabled(false);
                    checkFreeDelivery();
                }else{
                    showAlert(Constants.MAX_QUANTITY);
                }
                CustomSavedCardAdapter.this.showAlert(Constants.MAX_QUANTITY);
            }
        });

        holder.plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = Integer.parseInt(item.getMaxQuantity());
                DatabaseHandler db = new DatabaseHandler(CustomSavedCardAdapter.this.mContext);
                OrderBean updateOrderBean = db.getOrder(item.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity()) + 1;
                    if (max <= 0 || quantity <= max) {
                        holder.cartCount.setText(String.valueOf(Integer.parseInt(holder.cartCount.getText().toString()) + 1));
                        double finalPrice = Double.parseDouble(updateOrderBean.getUnitPrice()) * ((double) quantity);
                        updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                        updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(updateOrderBean);
                    } else {
                        CustomSavedCardAdapter.this.showAlert(Constants.MAX_QUANTITY);
                    }
                    CustomSavedCardAdapter.this.checkFreeDelivery();
                }
            }
        });


        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.cartCount.getText().toString());
                count--;
                holder.cartCount.setText(String.valueOf(count));
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean updateOrderBean = db.getOrder(item.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity());
                    quantity--;
                    double unitPrice = Double.parseDouble(updateOrderBean.getUnitPrice());
                    double finalPrice = unitPrice * quantity;
                    updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                    updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                    db.updateOrder(updateOrderBean);
                }
                OrderBean checkUpdatedOrderBean = db.getOrder(item.getSupplierItemId());
                if (checkUpdatedOrderBean.getOrderQuantity().equalsIgnoreCase("0")) {
                    holder.cartCountlayout.setVisibility(View.GONE);
                    db.deleteOrder(checkUpdatedOrderBean);
                    List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                    for (int k = 0; k < suppliersBeanList.size(); k++) {
                        SuppliersBean suppliersBean = suppliersBeanList.get(k);
                        List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                        if (orderBeanList.size() == 0) {
                            db.deleteSupplier(suppliersBean);
                        }
                    }
                    holder.cartBtn.setEnabled(true);
                }
                checkFreeDelivery();
            }
        });
    }

    private void checkFreeDelivery() {
        DatabaseHandler db = new DatabaseHandler(mContext);
        double orderTotalAmount = 0;
        List<OrderBean> ordersList = db.getAllOrders();
        for (int j = 0; j < ordersList.size(); j++) {
            orderTotalAmount += Double.parseDouble(ordersList.get(j).getFinalPrice());
        }

        if(Global.promotionAvailableBean!=null){
            if(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getFreeDelivery().equalsIgnoreCase("yes")){
                double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());
                if (orderTotalAmount > minamount){
                    Global.isFreeDelivery = true;
                }else {
                    Global.isFreeDelivery = false;
                }

            }
        }
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return savedSupplierItemBeanList.size();
    }

    private void showAlert(String msg) {
        View promptsView = LayoutInflater.from(this.activity).inflate(R.layout.error_dialog, null);
        Builder alertDialogBuilder = new Builder(this.activity);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog dialog = alertDialogBuilder.create();
        TextView titleText = (TextView) promptsView.findViewById(R.id.text_title_msg);
        Button okBtn = (Button) promptsView.findViewById(R.id.button_ok);
        titleText.setTypeface(this.regular);
        okBtn.setTypeface(this.regular);
        titleText.setText(msg);
        okBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}