package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.OrderRecieptActivity;
import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.OrderBean;
import com.heartyy.heartyyfresh.bean.OrderSubstitutionBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay on 3/14/2016.
 */
public class OrderRecieptAdapter extends RecyclerView.Adapter<OrderRecieptAdapter.SimpleViewHolder> {
    private static final int COUNT = 100;
    private final Context mContext;
    Typeface andBold, bold, italic, light;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private SharedPreferences pref;
    private List<OrderBean> orderBeanList;
    OrderRecieptActivity activity;


    public OrderRecieptAdapter(Context mContext, List<OrderBean> orderBeanList, OrderRecieptActivity activity) {
        this.mContext = mContext;
        andBold = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(mContext.getAssets(),
                Fonts.ROBOTO_LIGHT);
        this.orderBeanList = orderBeanList;
        this.activity = activity;
        pref = mContext.getApplicationContext().getSharedPreferences("MyPref",
                mContext.MODE_PRIVATE);


        mItems = new ArrayList<Integer>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, itemName, itemQuantity, amount, itemWeight, discountedPrice, price, outOfStockText, outCount;
        public TextView outAmount, subsItemName, subsItemQuantity, subAmount, subItemWeight, subDiscountedPrice, subPrice, textSubstitutedWith;
        public ImageView line, line2, amntLine, line4;
        public RelativeLayout itemsOutOfStockLayout, substitutinLayout;
        public ImageButton likeBtn;

        public SimpleViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.text_item_name);
            itemQuantity = (TextView) view.findViewById(R.id.text_quant);
            amount = (TextView) view.findViewById(R.id.text_amount);
            itemWeight = (TextView) view.findViewById(R.id.text_item_weight);
            discountedPrice = (TextView) view.findViewById(R.id.text_discounted_price);
            price = (TextView) view.findViewById(R.id.text_price);
            line = (ImageView) view.findViewById(R.id.line);
            line2 = (ImageView) view.findViewById(R.id.line2);
            amntLine = (ImageView) view.findViewById(R.id.line3);
            itemsOutOfStockLayout = (RelativeLayout) view.findViewById(R.id.layout_item_out_of_stock);
            outOfStockText = (TextView) view.findViewById(R.id.text_out_of_stock);
            outCount = (TextView) view.findViewById(R.id.text_out_quant);
            outAmount = (TextView) view.findViewById(R.id.text_out_amount);

            substitutinLayout = (RelativeLayout) view.findViewById(R.id.layout_substitution);
            subsItemName = (TextView) view.findViewById(R.id.text_subs_item_name);
            subsItemQuantity = (TextView) view.findViewById(R.id.text_subs_quant);
            subAmount = (TextView) view.findViewById(R.id.text_subs_amount);
            subItemWeight = (TextView) view.findViewById(R.id.text_subs_item_weight);
            subDiscountedPrice = (TextView) view.findViewById(R.id.text_subs_discounted_price);
            subPrice = (TextView) view.findViewById(R.id.text_subs_price);
            line4 = (ImageView) view.findViewById(R.id.line4);


            textSubstitutedWith = (TextView) view.findViewById(R.id.text_substituted_with);


        }
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.order_reciept_items_list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderRecieptAdapter.SimpleViewHolder holder, int position) {

        final OrderBean data = orderBeanList.get(position);

        holder.itemName.setTypeface(light);
        holder.itemQuantity.setTypeface(light);
        holder.outOfStockText.setTypeface(andBold);
        holder.subsItemName.setTypeface(light);
        holder.textSubstitutedWith.setTypeface(andBold);
        holder.outCount.setTypeface(andBold);
        holder.subAmount.setTypeface(andBold);
        holder.subsItemQuantity.setTypeface(andBold);
        holder.subItemWeight.setTypeface(light);
        holder.itemWeight.setTypeface(light);
        holder.amount.setTypeface(light);
        holder.price.setTypeface(light);

        String isQuantity = data.getIsOrderedSuppliedQuantitySame();
        String count;
        if (isQuantity.equalsIgnoreCase("NO")) {
            holder.itemQuantity.setText(data.getOrderQuantity());
            holder.line2.setVisibility(View.VISIBLE);
            count = data.getSuppliedQuantity();
            holder.amntLine.setVisibility(View.VISIBLE);
        } else {
            holder.itemQuantity.setText(data.getOrderQuantity());
            holder.line2.setVisibility(View.GONE);
            count = data.getOrderQuantity();
            holder.amntLine.setVisibility(View.GONE);
        }

        holder.itemName.setText(data.getItemName());
        if (count.equalsIgnoreCase("0")||count.equalsIgnoreCase("1")) {
            holder.itemWeight.setText(data.getSize() + " " + data.getUom());
        } else {
            holder.itemWeight.setText(data.getSuppliedQuantity() + " X " + data.getSize() + " " + data.getUom());
        }
        String temp1[] = data.getTotal().split("\\.");
        if (temp1.length > 1) {
            holder.amount.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + temp1[1] + "</sup>"));
        } else {
            holder.amount.setText(Html.fromHtml("<sup>$</sup><big>" + temp1[0] + "</big><sup>" + "0" + "</sup>"));
        }


        String finalPrice = data.getFinalPrice();
        if (!finalPrice.equalsIgnoreCase("0")) {
            holder.price.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
            String distemp1[] = data.getFinalPrice().split("\\.");
            if (distemp1.length > 1) {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + distemp1[1] + "</sup>"));
            } else {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

            String regtemp1[] = data.getRegularPrice().split("\\.");
            if (regtemp1.length > 1) {
                holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
            } else {
                holder.price.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }

        } else {
            holder.price.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
            String regtemp1[] = data.getRegularPrice().split("\\.");
            if (regtemp1.length > 1) {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
            } else {
                holder.discountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
            }
        }

        if (data.getOutOfStock().equalsIgnoreCase("YES")) {
            holder.itemsOutOfStockLayout.setVisibility(View.VISIBLE);
            holder.outOfStockText.setVisibility(View.VISIBLE);

            holder.outCount.setText(Html.fromHtml("(" + "0" + ")"));
            holder.outAmount.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + "0" + "</big><sup>" + "00" + "</sup><big>)</big>"));

        } else {
            holder.itemsOutOfStockLayout.setVisibility(View.GONE);
            holder.outOfStockText.setVisibility(View.GONE);
        }

        OrderSubstitutionBean substituionBean = data.getOrderSubstitutionBean();

        if (substituionBean != null) {
            holder.substitutinLayout.setVisibility(View.VISIBLE);


            holder.subsItemName.setText(substituionBean.getItemName());
            holder.subsItemQuantity.setText(Html.fromHtml("(" + substituionBean.getOrderQuantity() + ")"));

            String subAmountTemp[] = substituionBean.getTotal().split("\\.");
            if (holder.subAmount.length() > 1) {
                holder.subAmount.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + subAmountTemp[0] + "</big><sup>" + subAmountTemp[1] + "</sup><big>)</big>"));
            } else {
                holder.subAmount.setText(Html.fromHtml("<big>(</big><sup>$</sup><big>" + subAmountTemp[0] + "</big><sup>" + "0" + "</sup><big>)</big>"));
            }

            String count1 = substituionBean.getOrderQuantity();
            ;
            if (count1.equalsIgnoreCase("0")) {
                holder.subItemWeight.setText(substituionBean.getSize() + " " + substituionBean.getUom());
            } else {
                holder.subItemWeight.setText(substituionBean.getOrderQuantity() + " X " + substituionBean.getSize() + " " + substituionBean.getUom());
            }


            String subfinalPrice = substituionBean.getFinalPrice();
            if (!subfinalPrice.equalsIgnoreCase("0")) {
                holder.subPrice.setVisibility(View.VISIBLE);
                holder.line4.setVisibility(View.VISIBLE);
                String distemp1[] = substituionBean.getFinalPrice().split("\\.");
                if (distemp1.length > 1) {
                    holder.subDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + distemp1[1] + "</sup>"));
                } else {
                    holder.subDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + distemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }

                String regtemp1[] = substituionBean.getRegularPrice().split("\\.");
                if (regtemp1.length > 1) {
                    holder.subPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
                } else {
                    holder.subPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }

            } else {
                holder.subPrice.setVisibility(View.GONE);
                holder.line4.setVisibility(View.GONE);
                String regtemp1[] = substituionBean.getRegularPrice().split("\\.");
                if (regtemp1.length > 1) {
                    holder.subDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + regtemp1[1] + "</sup>"));
                } else {
                    holder.subDiscountedPrice.setText(Html.fromHtml("<sup>$</sup><big>" + regtemp1[0] + "</big><sup>" + "0" + "</sup>"));
                }
            }

        } else {
            holder.substitutinLayout.setVisibility(View.GONE);
        }


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
