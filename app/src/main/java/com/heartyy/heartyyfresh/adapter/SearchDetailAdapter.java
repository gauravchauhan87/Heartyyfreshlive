package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.ItemDetailActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.BrandBean;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
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
 * Created by Vijay on 2/9/2016.
 */
public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.SimpleViewHolder> {

    private static final int COUNT = 100;

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private Typeface regular, meduimItalic, medium, robotoLight;
    private Activity activity;
    private SharedPreferences pref;
    private ProgressBar mProgressBar;
    private List<SubAisleItemBean> subAisleItemBeanList;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, packsItem, price, discountedPrice, onSale, cartCount, brandName, textDiscountedPriceDecimal, textPriceDecimal, text_price_dollor;
        public ImageView lineImage, itemImage;
        public CardView cardView;
        public ImageButton  plusBtn, minusBtn;
        public RelativeLayout cartCountlayout,cartBtn;
        public ImageButton likeBtn;


        public SimpleViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.text_item_detail);
            packsItem = (TextView) view.findViewById(R.id.text_size);
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
            textDiscountedPriceDecimal = (TextView) view.findViewById(R.id.text_discounted_price_decimal);
            textPriceDecimal = (TextView) view.findViewById(R.id.text_price_decimal);
            text_price_dollor = (TextView) view.findViewById(R.id.text_price_dollor);
        }
    }

    public SearchDetailAdapter(Context context, List<SubAisleItemBean> subAisleItemBeanList, Activity activity) {
        mContext = context;
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

        this.subAisleItemBeanList = subAisleItemBeanList;
        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }



    @Override
    public SearchDetailAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.saved_detail_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchDetailAdapter.SimpleViewHolder holder, final int position) {

        final SubAisleItemBean item = subAisleItemBeanList.get(position);
        holder.onSale.setTypeface(meduimItalic);
        holder.title.setTypeface(robotoLight);
        holder.packsItem.setTypeface(robotoLight);
        holder.price.setTypeface(medium);
        holder.discountedPrice.setTypeface(medium);
        holder.brandName.setTypeface(robotoLight);

        holder.title.setText(item.getItemName());
        String temp[] = item.getPrice().split("\\.");
        if (temp.length > 1) {
            holder.price.setText(temp[0]);
            holder.textPriceDecimal.setText(temp[1]);
        } else {
            holder.price.setText(temp[0]);
            holder.textPriceDecimal.setText("00");
        }
        DatabaseHandler db = new DatabaseHandler(mContext);
        OrderBean orderBean = db.getOrder(item.getSupplierItemId());
        if (orderBean != null) {
            holder.cartCountlayout.setVisibility(View.VISIBLE);
            holder.cartCount.setText(orderBean.getOrderQuantity());
            holder.cartBtn.setEnabled(false);
        } else {
            holder.cartCountlayout.setVisibility(View.GONE);
            holder.cartBtn.setEnabled(true);
        }


        String isSave = item.getIsSave();
        if (isSave != null) {
            if (isSave.equalsIgnoreCase("NO")) {
                holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
            } else {
                holder.likeBtn.setBackgroundResource(R.drawable.like_icon);
            }
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.unlike_icon);
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


        String onSale = item.getSale();
        if (onSale == null) {
            holder.price.setVisibility(View.GONE);
            holder.text_price_dollor.setVisibility(View.GONE);
            holder.textPriceDecimal.setVisibility(View.GONE);
            holder.lineImage.setVisibility(View.GONE);
            holder.onSale.setText(item.getOffer());
            holder.onSale.setVisibility(View.INVISIBLE);
            String temp1[] = item.getPrice().split("\\.");
            String priceTemp = item.getPrice();
            if(item.getSubIsTaxApplicable().equalsIgnoreCase("true")||item.getIsTaxApplicable().equalsIgnoreCase("true")){
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE,null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
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
            String temp1[] = item.getPrice().split("\\.");
            String priceTemp = item.getPrice();
            if(item.getSubIsTaxApplicable().equalsIgnoreCase("true")||item.getIsTaxApplicable().equalsIgnoreCase("true")){
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE,null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
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
            holder.onSale.setVisibility(View.VISIBLE);
            holder.onSale.setText(item.getOffer());
            String temp1[] = item.getPrice().split("\\.");
            String priceTemp = item.getPrice();
            if(item.getSubIsTaxApplicable().equalsIgnoreCase("true")||item.getIsTaxApplicable().equalsIgnoreCase("true")){
                String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE,null);
                double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                item.setTaxAmount(temp2);
                // priceTemp = String.valueOf(String.format("%.2f",temp2));
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
                String temp1[] = item.getSalePrice().split("\\.");
                String priceTemp = item.getSalePrice();
                if(item.getSubIsTaxApplicable().equalsIgnoreCase("true")||item.getIsTaxApplicable().equalsIgnoreCase("true")){
                    String taxRate = pref.getString(Constants.APPLICABLE_TAX_RATE,null);
                    double temp2 = (Double.parseDouble(taxRate) * Double.parseDouble(priceTemp));
                    item.setTaxAmount(temp2);
                    // priceTemp = String.valueOf(String.format("%.2f",temp2));
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

        String quantity = item.getCount();
        if (quantity.equalsIgnoreCase("0")||quantity.equalsIgnoreCase("1")) {
            holder.packsItem.setText(item.getSize() + " " + item.getUom());
        } else {
            holder.packsItem.setText(item.getCount() + " X " + item.getSize() + " " + item.getUom());
        }

        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max = Integer.parseInt(item.getMaxQuantity());

                if(item.getMaxQuantity()==null){
                    item.setMaxQuantity("0");
                }else if(item.getMaxQuantity().equalsIgnoreCase("null")){
                    item.setMaxQuantity("0");
                }
                if(Integer.parseInt(item.getMaxQuantity()) == 0 || Integer.parseInt(item.getMaxQuantity()) > 0) {
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
                        if (item.getSubIsTaxApplicable().equalsIgnoreCase("true") || item.getIsTaxApplicable().equalsIgnoreCase("true")) {
                            newOrder.setIsTaxable("TRUE");
                            newOrder.setTaxAmount(item.getTaxAmount());
                        } else {
                            newOrder.setIsTaxable("FALSE");
                            newOrder.setTaxAmount(0);
                        }
                        newOrder.setSupplierId(supplierID);
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
                    // activity.findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
                    // activity.findViewById(R.id.layout_total_cart_count).setVisibility(View.VISIBLE);
                    // int ordersCount = db.getOrdersCount();
                    // TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                    // totalCartCountText.setText(String.valueOf(ordersCount));
                    holder.cartBtn.setEnabled(false);
                    //  setTotalAmount();

                    checkFreeDelivery();
                }else{
                    showAlert(Constants.MAX_QUANTITY);
                }
            }
        });

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseHandler db = new DatabaseHandler(mContext);
                OrderBean updateOrderBean = db.getOrder(item.getSupplierItemId());
                if (updateOrderBean != null) {
                    int quantity = Integer.parseInt(updateOrderBean.getOrderQuantity());
                    quantity++;
                    int max = Integer.parseInt(item.getMaxQuantity());
                    if (max > 0 && quantity > max) {
                        showAlert(Constants.MAX_QUANTITY);
                    }else {
                        int count = Integer.parseInt(holder.cartCount.getText().toString());
                        count++;
                        holder.cartCount.setText(String.valueOf(count));
                        double unitPrice = Double.parseDouble(updateOrderBean.getUnitPrice());
                        double finalPrice = unitPrice * quantity;
                        updateOrderBean.setOrderQuantity(String.valueOf(quantity));
                        updateOrderBean.setFinalPrice(String.valueOf(finalPrice));
                        db.updateOrder(updateOrderBean);
                    }

                    checkFreeDelivery();

                }

               // int ordersCount = db.getOrdersCount();
               // TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);

               // totalCartCountText.setText(String.valueOf(ordersCount));
               // setTotalAmount();


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
                    item.setIsCartShown(false);
                    db.deleteOrder(checkUpdatedOrderBean);
                    List<SuppliersBean> suppliersBeanList = db.getAllSuppliers();
                    for (int k = 0; k < suppliersBeanList.size(); k++) {
                        SuppliersBean suppliersBean = suppliersBeanList.get(k);
                        List<OrderBean> orderBeanList = db.getAllOrders(suppliersBean);
                        if (orderBeanList.size()==0) {
                            db.deleteSupplier(suppliersBean);
                        }
                    }
                    holder.cartBtn.setEnabled(true);
                }

                /*int ordersCount = db.getOrdersCount();
                TextView totalCartCountText = (TextView) activity.findViewById(R.id.text_total_cart_count);
                totalCartCountText.setText(String.valueOf(ordersCount));
                if (ordersCount == 0) {
                    activity.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
                    activity.findViewById(R.id.layout_total_cart_count).setVisibility(View.GONE);
                }*/
               // setTotalAmount();
                checkFreeDelivery();

            }
        });

        holder.cardView.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.SUB_CATEGORY_ID, item.getSubCategoryId());
                editor.putString(Constants.TOP_CATEGORY_ID, item.getTopCategoryId());
                editor.commit();
                Global.subAisleItemBean = item;
                Intent intent = new Intent(mContext, ItemDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", item.getItemName());
                intent.putExtra("supplierItemId", item.getSupplierItemId());
                intent.putExtra("position",position);
                mContext.startActivity(intent);
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

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return subAisleItemBeanList.size();
    }
}
