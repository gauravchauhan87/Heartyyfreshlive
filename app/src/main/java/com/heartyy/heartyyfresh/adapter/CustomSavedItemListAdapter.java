package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.SavedItemsActivity;
import com.heartyy.heartyyfresh.bean.Item;
import com.heartyy.heartyyfresh.bean.SavedSectionItem;
import com.heartyy.heartyyfresh.bean.SavedSupplierItemBean;
import com.heartyy.heartyyfresh.bean.SavedSuppliersBean;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dheeraj on 12/6/2015.
 */
public class CustomSavedItemListAdapter extends ArrayAdapter<Item> {

    private Context context;
    LayoutInflater mInflater;
    private int mSelectedItem;
    private ArrayList<Item> items;
    private boolean isClosed = false;
    Typeface andBold, bold, italic,light;
    private SavedItemsActivity activity;
    private SharedPreferences pref;



    public CustomSavedItemListAdapter(Context context, ArrayList<Item> items, SavedItemsActivity activity) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.activity = activity;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
        pref = context.getSharedPreferences("MyPref",
                context.MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SavedSectionItem si = (SavedSectionItem) i;
                v = inflater.inflate(R.layout.list_item_section, null);

                final TextView sectionView = (TextView) v.findViewById(R.id.text_save_section);
                sectionView.setText(si.getTitle());
                sectionView.setTypeface(light);

            } else {
                final SavedSuppliersBean ei = (SavedSuppliersBean) i;
                v = inflater.inflate(R.layout.saved_items_list_items, null);
                TextView itemText = (TextView) v.findViewById(R.id.text_item);
                TextView saveItemText = (TextView) v.findViewById(R.id.text_items_save);
                TextView city = (TextView) v.findViewById(R.id.text_location);
                Button btn = (Button) v.findViewById(R.id.button_reorder_all);

                itemText.setText(ei.getSupplierName());
                saveItemText.setText("(" + ei.getCount() + ") saved items");
                city.setText(ei.getCity());

                itemText.setTypeface(andBold);
                city.setTypeface(light);
                saveItemText.setTypeface(light);
                btn.setTypeface(andBold);


                final List<SavedSupplierItemBean> savedSupplierItemBeanList = ei.getSavedSupplierItemBeanList();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.APPLICABLE_TAX_RATE,ei.getTaxRate());
                        editor.commit();
                        activity.reOrderSaved(ei.getSupplierId(),savedSupplierItemBeanList,ei.getSupplierName());
                    }
                });

            }
        }
        return v;
    }
}
