package com.heartyy.heartyyfresh.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.heartyy.heartyyfresh.bean.BrandBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SimilarItems;
import com.heartyy.heartyyfresh.bean.SuppliersBean;
import com.heartyy.heartyyfresh.database.DatabaseHandler;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay on 8/11/2016.
 */
public class MoreItemsListBaseAdapter extends BaseAdapter {
    private static final int COUNT = 100;
    private Context context;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private ItemDetailActivity activity;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private SharedPreferences pref;
    private List<SimilarItems> similarItemsList;


    public MoreItemsListBaseAdapter(Context context, ItemDetailActivity activity, List<SimilarItems> similarItemsList) {
        this.context = context;
        this.similarItemsList = similarItemsList;
        this.activity = activity;
        robotoLight = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_MEDIUM);

        pref = context.getApplicationContext().getSharedPreferences("MyPref",
                context.MODE_PRIVATE);


        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            //        addItem(i);
        }
    }

    @Override
    public int getCount() {
        return similarItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return similarItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return similarItemsList.indexOf(similarItemsList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.similar_item_detail, parent, false);

        final TextView title, packsItem, price, discountedPrice, onSaleText, cartCount, brandName;
        final ImageView lineImage, itemImage;
        CardView cardView;
        ImageButton plusBtn, minusBtn;
        final RelativeLayout cartCountlayout;
        ImageView likeBtn;
        final LinearLayout cartBtn;

        title = (TextView) rowView.findViewById(R.id.text_item_detail);
        packsItem = (TextView) rowView.findViewById(R.id.text_size);
        price = (TextView) rowView.findViewById(R.id.text_price);
        discountedPrice = (TextView) rowView.findViewById(R.id.text_discounted_price);
        onSaleText = (TextView) rowView.findViewById(R.id.text_onsale);
        lineImage = (ImageView) rowView.findViewById(R.id.image_line);
        cardView = (CardView) rowView.findViewById(R.id.card_view);
        cartBtn = (LinearLayout) rowView.findViewById(R.id.button_cart);
        plusBtn = (ImageButton) rowView.findViewById(R.id.button_plus);
        minusBtn = (ImageButton) rowView.findViewById(R.id.button_minus);
        cartCountlayout = (RelativeLayout) rowView.findViewById(R.id.layout_cart_count);
        cartCount = (TextView) rowView.findViewById(R.id.text_cart_count);
        itemImage = (ImageView) rowView.findViewById(R.id.image_item);
        likeBtn = (ImageView) rowView.findViewById(R.id.image_like);
        brandName = (TextView) rowView.findViewById(R.id.text_item_brand);

        final SimilarItems item = similarItemsList.get(position);

        title.setTypeface(robotoLight);
        price.setTypeface(medium);
        discountedPrice.setTypeface(robotoLight);
        onSaleText.setTypeface(meduimItalic);

        final DatabaseHandler db = new DatabaseHandler(context);
        OrderBean orderBean = db.getOrder(item.getSupplierItemId());
        if (orderBean != null) {
            cartCountlayout.setVisibility(View.VISIBLE);
            cartCount.setText(orderBean.getOrderQuantity());
            cartBtn.setEnabled(false);
        } else {
            cartCountlayout.setVisibility(View.GONE);
            cartBtn.setEnabled(true);
        }


        title.setText(item.getItemName());
        String temp[] = String.format("%.2f", Double.parseDouble(item.getPrice())).split("\\.");
        price.setText(Html.fromHtml("<sup> &nbsp; $</sup><big>" + temp[0] + "</big><sup>" + temp[1] + "</sup>"));


        String onSale = item.getSale();

        if (onSale == null) {
            price.setVisibility(View.GONE);
            lineImage.setVisibility(View.GONE);
            onSaleText.setText(item.getOffer());
            onSaleText.setVisibility(View.INVISIBLE);
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
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        } else if (onSale.equalsIgnoreCase("null")) {
            price.setVisibility(View.GONE);
            lineImage.setVisibility(View.GONE);
            onSaleText.setText(item.getOffer());
            onSaleText.setVisibility(View.INVISIBLE);
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
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

        } else if (onSale.equalsIgnoreCase("no")) {
            price.setVisibility(View.GONE);
            lineImage.setVisibility(View.GONE);
            onSaleText.setVisibility(View.VISIBLE);
            onSaleText.setText(item.getOffer());
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
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
            } else {
                discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        } else {
            price.setVisibility(View.VISIBLE);
            lineImage.setVisibility(View.VISIBLE);
            onSaleText.setVisibility(View.VISIBLE);
            onSaleText.setText(item.getOffer());
            if (!item.getSalePrice().equalsIgnoreCase("null")) {
                String temp1[] = String.format("%.2f", Double.parseDouble(item.getSalePrice())).split("\\.");
                if (temp1.length > 1) {
                    discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
                } else {
                    discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
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
                brandName.setVisibility(View.GONE);

            } else {
                brandName.setVisibility(View.VISIBLE);
                brandName.setText("(" + brand + ")");
            }
        } else {
            brandName.setVisibility(View.GONE);
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + item.getThumbnail(), ImageLoader.getImageListener(
                        itemImage, R.drawable.heartyy_placeholder, R.drawable.heartyy_placeholder));

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


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = Integer.parseInt(item.getMaxQuantity());
                if (item.getMaxQuantity() == null) {
                    item.setMaxQuantity("0");
                } else if (item.getMaxQuantity().equalsIgnoreCase("null")) {
                    item.setMaxQuantity("0");
                }
                if (Integer.parseInt(item.getMaxQuantity()) == 0 || Integer.parseInt(item.getMaxQuantity()) > 0) {
                    cartCountlayout.setVisibility(View.VISIBLE);
                    DatabaseHandler db = new DatabaseHandler(context);
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
                        cartCount.setText("1");
                    }

                    activity.findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
                    activity.findViewById(R.id.layout_total_cart_count).setVisibility(View.VISIBLE);
                    int ordersCount = db.getOrdersCount();
                    TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                    totalCartCountText.setText(String.valueOf(ordersCount));
                    cartBtn.setEnabled(false);
                    activity.refreshData();
                    activity.checkAnyOrder();
                    setTotalAmount();
                    //notifyDataSetChanged();
                } else {
                    showAlert(Constants.MAX_QUANTITY);
                }
            }
        });


        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(context);
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
                        cartCount.setText(String.valueOf(quantity));
                    }

                }
                activity.refreshData();
                activity.checkAnyOrder();
                setTotalAmount();
                //notifyDataSetChanged();
            }
        });


        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(context);
                OrderBean minusOrderBean = db.getOrder(item.getSupplierItemId());
                if (minusOrderBean != null) {
                    int itemQuantity = Integer.parseInt(minusOrderBean.getOrderQuantity());
                    if (itemQuantity == 1) {
                        cartCountlayout.setVisibility(View.GONE);
                        db.deleteOrder(minusOrderBean);
                        cartBtn.setEnabled(true);

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
                        cartCount.setText(String.valueOf(itemQuantity));
                    }
                }
                activity.refreshData();
                activity.checkAnyOrder();
                setTotalAmount();
                //notifyDataSetChanged();
            }
        });
        return rowView;
    }

    private void setTotalAmount() {
        double orderTotalAmount = 0;
        DatabaseHandler db = new DatabaseHandler(context);
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
            if (Global.promotionAvailableBean.getDeliveryBean() != null) {
                if (Global.promotionAvailableBean.getDeliveryBean().getFreeDelivery() != null) {
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
                            progressBar.setProgress(Integer.parseInt(Global.promotionAvailableBean.getFreeDeliveryMinOrder().getMinOrderAmnt()));
                            txtProgress.setText("You got free delivery");
                            Global.isFreeDelivery = true;
                        } else {
                            String text = String.format("%.2f", addMore);
                            progressBar.setProgress(temp);
                            txtProgress.setText("Add $" + text + " for free delivery");
                            Global.isFreeDelivery = false;
                        }
                    }
                }
            }
        } else {
            progressBar.setVisibility(View.GONE);
            txtProgress.setVisibility(View.GONE);
        }
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
