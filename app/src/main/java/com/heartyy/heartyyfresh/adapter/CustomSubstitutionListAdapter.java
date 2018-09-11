package com.heartyy.heartyyfresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.SubstituionBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.List;

/**
 * Created by Dheeraj on 1/25/2016.
 */
public class CustomSubstitutionListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater mInflater;
    private Activity activity;
    private int mSelectedItem;
    private SharedPreferences pref;
    List<SubstituionBean> substituionBeanList;
    List<String> count;
    Typeface andBold, bold, italic, light;

    public CustomSubstitutionListAdapter(Context context, List<SubstituionBean> substituionBeanList) {
        this.context = context;
        this.mSelectedItem = -1;
        this.substituionBeanList = substituionBeanList;

        andBold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_LIGHT);
    }

    public void setSel(int pos){
        mSelectedItem=pos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return substituionBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return substituionBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return substituionBeanList.indexOf(substituionBeanList.get(position));
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_substitution_list, parent, false);
        Typeface andBold = Typeface.createFromAsset(context.getAssets(), Fonts.ROBOTO_REGULAR);
        ViewGroup root = (ViewGroup) rowView.findViewById(R.id.profile_notification_item);
//        Global.setFont(root, andBold);
        final SubstituionBean data = substituionBeanList.get(position);

        TextView notificationText = (TextView) rowView.findViewById(R.id.text_meal_reminders);
        ImageView imageChecked = (ImageView) rowView.findViewById(R.id.image_checked);

        notificationText.setTypeface(andBold);

        if (mSelectedItem==position) {
            imageChecked.setVisibility(View.VISIBLE);
        } else{
            imageChecked.setVisibility(View.GONE);
        }

        notificationText.setText(data.getText());


        return rowView;
    }
}
