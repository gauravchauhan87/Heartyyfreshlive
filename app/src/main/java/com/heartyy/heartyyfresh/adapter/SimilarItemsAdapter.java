package com.heartyy.heartyyfresh.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.ItemDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.bean.BrandBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 1/11/2016.
 */
public class SimilarItemsAdapter extends RecyclerView.Adapter<SimilarItemsAdapter.SimpleViewHolder> {

    private static final int COUNT = 100;

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private ItemDetailActivity activity;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private SharedPreferences pref;
    private List<SimilarItems> similarItemsList;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, packsItem, price, discountedPrice, onSale, cartCount, brandName;
        public ImageView lineImage, itemImage;
        public CardView cardView;
        public ImageButton  plusBtn, minusBtn;
        public RelativeLayout cartCountlayout;
        public LinearLayout cartBtn;
        public ImageView likeBtn;

        public SimpleViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.text_item_detail);
            packsItem = (TextView) view.findViewById(R.id.text_size);
            price = (TextView) view.findViewById(R.id.text_price);
            discountedPrice = (TextView) view.findViewById(R.id.text_discounted_price);
            onSale = (TextView) view.findViewById(R.id.text_onsale);
            lineImage = (ImageView) view.findViewById(R.id.image_line);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cartBtn = (LinearLayout) view.findViewById(R.id.button_cart);
            plusBtn = (ImageButton) view.findViewById(R.id.button_plus);
            minusBtn = (ImageButton) view.findViewById(R.id.button_minus);
            cartCountlayout = (RelativeLayout) view.findViewById(R.id.layout_cart_count);
            cartCount = (TextView) view.findViewById(R.id.text_cart_count);
            itemImage = (ImageView) view.findViewById(R.id.image_item);
            likeBtn = (ImageView) view.findViewById(R.id.image_like);
            brandName = (TextView) view.findViewById(R.id.text_item_brand);
        }
    }

    public SimilarItemsAdapter(Context context, ItemDetailActivity activity, List<SimilarItems> similarItemsList) {
        mContext = context;
        this.similarItemsList = similarItemsList;
        this.activity = activity;
        robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_MEDIUM);

        pref = context.getApplicationContext().getSharedPreferences("MyPref",
                context.MODE_PRIVATE);


        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.similar_item_detail, parent, false);

        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        final SimilarItems item = similarItemsList.get(position);
        holder.title.setTypeface(robotoLight);
        holder.price.setTypeface(medium);
        holder.discountedPrice.setTypeface(regular);
        holder.onSale.setTypeface(meduimItalic);

        holder.title.setText(item.getItemName());
        String temp[] = item.getPrice().split("\\.");
        if (temp.length > 1) {
            holder.price.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));
        } else {
            holder.price.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + "0" + "</sup>"));
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


        String onSale = item.getSale();

        if (onSale == null) {
            holder.price.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setText(item.getOffer());
            holder.onSale.setVisibility(View.INVISIBLE);
            String priceTemp = item.getPrice();
            String temp1[] = String.format("%.2f", Double.parseDouble(item.getPrice())).split("\\.");
            if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxAplicable().equalsIgnoreCase("true")) {
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

        } else if (onSale.equalsIgnoreCase("null")) {
            holder.price.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setText(item.getOffer());
            holder.onSale.setVisibility(View.INVISIBLE);
            String priceTemp = item.getPrice();
            String temp1[] = String.format("%.2f", Double.parseDouble(item.getPrice())).split("\\.");
            if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxAplicable().equalsIgnoreCase("true")) {
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

        } else if (onSale.equalsIgnoreCase("no")) {
            holder.price.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setVisibility(View.VISIBLE);
            holder.onSale.setText(item.getOffer());
            String temp1[] = String.format("%.2f", Double.parseDouble(item.getPrice())).split("\\.");
            String priceTemp = item.getPrice();
            if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxAplicable().equalsIgnoreCase("true")) {
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
            }
            item.setFinalItemUnitPrice(priceTemp);
            if (temp1.length > 1) {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

        } else {
            holder.price.setVisibility(View.VISIBLE);
            holder.lineImage.setVisibility(View.VISIBLE);
            holder.onSale.setVisibility(View.VISIBLE);
            holder.onSale.setText(item.getOffer());
            if (!item.getSalePrice().equalsIgnoreCase("null")) {
                String temp1[] = String.format("%.2f", Double.parseDouble(item.getSalePrice())).split("\\.");
                if (temp1.length > 1) {
                    holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
                } else {
                    holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
                }

                String priceTemp = item.getPrice();
                if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxAplicable().equalsIgnoreCase("true")) {
                    String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE, null);
                    double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                    item.setTaxAmount(temp2);
                    // priceTemp = String.valueOf(String.format("%.2f",temp2));
                }
                item.setFinalItemUnitPrice(priceTemp);

            }
        }
        BrandBean brandBean = item.getBrand();
        if (brandBean != null) {
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

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + item.getThumbnail(), ImageLoader.getImageListener(
                        holder.itemImage, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + item.getThumbnail());
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

        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int max = Integer.parseInt(item.getMaxQuantity());
                if(item.getMaxQuantity()==null){
                    item.setMaxQuantity("0");
                }else if(item.getMaxQuantity().equalsIgnoreCase("null")){
                    item.setMaxQuantity("0");
                }
                if (Integer.parseInt(item.getMaxQuantity()) == 0 || Integer.parseInt(item.getMaxQuantity()) > 0) {
                    holder.cartCountlayout.setVisibility(View.VISIBLE);
                    DatabaseHandler db = new DatabaseHandler(mContext);

                    SuppliersBean suppliersBean = db.getSupplier(pref.getString(Constants.SUPPLIER_ID, null));
                    if (suppliersBean == null) {
                        SuppliersBean newSuppliersBean = new SuppliersBean();
                        newSuppliersBean.setSupplierId(pref.getString(Constants.SUPPLIER_ID, null));
                        newSuppliersBean.setSupplierName(pref.getString(Constants.SUPPLIER_NAME, null));
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
                        String supplierID = pref.getString(Constants.SUPPLIER_ID, null);
                        newOrder.setThumbnail(item.getThumbnail());
                        newOrder.setSupplierId(supplierID);
                        if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxAplicable().equalsIgnoreCase("true")) {
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
                        holder.cartCount.setText("1");
                    }

                    activity.findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.layout_total_cart_count).setVisibility(View.VISIBLE);
                    int ordersCount = db.getOrdersCount();
                    TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                    totalCartCountText.setText(String.valueOf(ordersCount));
                    holder.cartBtn.setEnabled(false);

                    activity.checkAnyOrder();
                    activity.refreshData();
                    setTotalAmount();
                }else{
                    showAlert(Constants.MAX_QUANTITY);
                }
            }
        });

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean updateOrderBean = db.getOrder(item.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity());
                    quantity++;
                    int max = Integer.parseInt(item.getMaxQuantity());
                    if (max > 0 && quantity > max) {
                        showAlert(Constants.MAX_QUANTITY);
                    } else {
                        double unitPrice = Double.parseDouble(updateOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * quantity;
                        updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                        updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(updateOrderBean);
                        holder.cartCount.setText(String.valueOf(quantity));
                    }

                }
                activity.checkAnyOrder();
                activity.refreshData();
                setTotalAmount();
            }
        });


        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean minusOrderBean = db.getOrder(item.getSupplierItemId());
                if (minusOrderBean != null) {
                    int itemQuantity = Integer.parseInt(minusOrderBean.getOrderQuantity());
                    if (itemQuantity == 1) {
                        holder.cartCountlayout.setVisibility(View.GONE);
                        db.deleteOrder(minusOrderBean);
                        holder.cartBtn.setEnabled(true);

                        List<OrderBean> orderList = db.getAllOrders();
                        if (orderList != null) {
                            int cartUpdatedItem = orderList.size();
                            if (cartUpdatedItem == 0) {
                                activity.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
                                activity.findViewById(R.id.layout_total_cart_count).setVisibility(View.GONE);
                            } else {
                                TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                                totalCartCountText.setText(String.valueOf(cartUpdatedItem));
                            }
                        }

                        List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                        for (int k = 0; k < suppliersBeanList.size(); k++) {
                            SuppliersBean suppliersBean = suppliersBeanList.get(k);
                            List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                            if (orderBeanList.size() == 0) {
                                db.deleteSupplier(suppliersBean);
                            }
                        }

                    } else {
                        itemQuantity--;
                        double unitPrice = Double.parseDouble(minusOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * itemQuantity;
                        minusOrderBean.setOrderQuantity(String.valueOf(itemQuantity));
                        minusOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(minusOrderBean);
                        holder.cartCount.setText(String.valueOf(itemQuantity));
                    }
                }
                activity.checkAnyOrder();
                activity.refreshData();
                setTotalAmount();
            }
        });


    }

    private void setTotalAmount() {
        double orderTotalAmount = 0;
        DatabaseHandler db = new DatabaseHandler(mContext);
        List<OrderBean> ordersList = db.getAllOrders();

        for (int j = 0; j < ordersList.size(); j++) {
            orderTotalAmount += Double.parseDouble(ordersList.get(j).getFinalPrice());
        }

        TextView txtOrdersPrice = (TextView) activity.findViewById(R.id.text_order_price);
        txtOrdersPrice.setText("$" + String.format("%.2f", orderTotalAmount));

        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.firstBar);
        TextView txtProgress = (TextView) activity.findViewById(R.id.text_progress);
        String p[] = String.valueOf(orderTotalAmount).split("\\.");
        int temp = Integer.parseInt(p[0]);
        if (Global.promotionAvailableBean != null) {
            if(Global.promotionAvailableBean.getDeliveryBean() != null){
                if(Global.promotionAvailableBean.getDeliveryBean().getFreeDelivery() != null){
                    if (Global.promotionAvailableBean.getDeliveryBean().getFreeDelivery().equalsIgnoreCase("yes")) {
                        progressBar.setVisibility(View.VISIBLE);
                        txtProgress.setVisibility(View.VISIBLE);
                        progressBar.setMax(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                        double minamount = Double.parseDouble(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt());

                        double addMore = 0;
                        if (orderTotalAmount > minamount) {
                            addMore = orderTotalAmount - minamount;
                        } else {
                            addMore = minamount - orderTotalAmount;
                        }

                        if (orderTotalAmount > minamount) {
                            Global.isFreeDelivery = true;
                            progressBar.setProgress(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                            txtProgress.setText("You got free delivery");
                        } else {
                            String text = String.format("%.2f", addMore);
                            progressBar.setProgress(temp);
                            txtProgress.setText("Add $" + text + " for free delivery");
                            Global.isFreeDelivery = false;
                        }
                    }
                }
            } else {
                progressBar.setVisibility(View.GONE);
                txtProgress.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            txtProgress.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return similarItemsList.size();
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
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
}
