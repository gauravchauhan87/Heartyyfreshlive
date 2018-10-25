package com.heartyy.heartyyfresh;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private Context context;
    private List<NavigationItem> mData;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;

    public NavigationDrawerAdapter(Context context,List<NavigationItem> data) {
        this.context = context;
        mData = data;
    }

    public void setNewitems(List<NavigationItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public NavigationDrawerCallbacks getNavigationDrawerCallbacks() {
        return mNavigationDrawerCallbacks;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    @NonNull
    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false);
        final ViewHolder viewholder = new ViewHolder(v);
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (mNavigationDrawerCallbacks != null)
                                                           mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(viewholder.getAdapterPosition());
                                                   }
                                               }
        );
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationDrawerAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.textView.setText(mData.get(i).getText());
        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mData.get(i).getDrawable(), null, null, null);
        if (i == 4 || i == 5) {
            String count = mData.get(i).getCount();
            if (count != null) {
                if (!count.equalsIgnoreCase("null")) {
                    if (count.equalsIgnoreCase("0")) {
                        viewHolder.countLayout.setVisibility(View.GONE);
                    } else {
                        viewHolder.countLayout.setVisibility(View.VISIBLE);
                        viewHolder.countText.setText(count);
                    }
                } else {
                    viewHolder.countLayout.setVisibility(View.GONE);
                }
            } else {
                viewHolder.countLayout.setVisibility(View.GONE);
            }
        } else {
            viewHolder.countLayout.setVisibility(View.GONE);
        }

        //TODO: selected menu position, change layout accordingly
        if (mSelectedPosition == i || mTouchedPosition == i) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.selected_gray));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0)
            notifyItemChanged(lastPosition);
        if (position >= 0)
            notifyItemChanged(position);
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, countText;
        private RelativeLayout countLayout;

        private ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
            countLayout = (RelativeLayout) itemView.findViewById(R.id.layout_count);
            countText = (TextView) itemView.findViewById(R.id.text_count);

        }
    }
}
