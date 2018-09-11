package com.heartyy.heartyyfresh.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.bean.SubAisleBean;
import com.heartyy.heartyyfresh.bean.TopAisleBean;
import com.heartyy.heartyyfresh.utils.Fonts;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<TopAisleBean> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<SubAisleBean>> _listDataChild;
	Typeface andBold, bold, italic,light,fontAwesome;

	public ExpandableListAdapter(Context context, List<TopAisleBean> listDataHeader,
								 HashMap<String, List<SubAisleBean>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataHeader.get(groupPosition).getSubAisleBeanList().get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		andBold = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_REGULAR);
		bold = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_BOLD);
		italic = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_ITALIC);
		light = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_LIGHT);

		final SubAisleBean childText = (SubAisleBean) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		txtListChild.setText(childText.getSubCategoryName());
		txtListChild.setTypeface(andBold);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataHeader.get(groupPosition).getSubAisleBeanList()
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parent) {

		andBold = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_REGULAR);
		bold = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_BOLD);
		italic = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_ITALIC);
		light = Typeface.createFromAsset(_context.getAssets(),
				Fonts.ROBOTO_LIGHT);

		TopAisleBean headerTitle = (TopAisleBean) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		final TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		final RelativeLayout expandButton = (RelativeLayout) convertView.findViewById(R.id.button_expand);
		final TextView textPlusMinus  = (TextView) convertView.findViewById(R.id.text_plus_minus);
		ImageView catImage = (ImageView) convertView.findViewById(R.id.image_category);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle.getTopCategoryName());
		lblListHeader.setTypeface(andBold);
		//textPlusMinus.setTypeface(fontAwesome);

/*
		if(isExpanded){
			lblListHeader.setTextColor(Color.parseColor("#00CC99"));
		}else{
			lblListHeader.setTextColor(Color.parseColor("#999999"));
		}
*/

		expandButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(isExpanded){
					((ExpandableListView) parent).collapseGroup(groupPosition);
					lblListHeader.setTextColor(_context.getResources().getColor(R.color.DimGrey));
					textPlusMinus.setText("+");
					textPlusMinus.setTextColor(_context.getResources().getColor(R.color.hearty_green));
				}else{
					((ExpandableListView) parent).expandGroup(groupPosition, true);
					lblListHeader.setTextColor(_context.getResources().getColor(R.color.hearty_green));
					textPlusMinus.setText("-");
					textPlusMinus.setTextColor(_context.getResources().getColor(R.color.grey_dark));
				}
			}
		});

		if(groupPosition==0){
			catImage.setVisibility(View.VISIBLE);
			catImage.setImageResource(R.drawable.like_icon);
			expandButton.setVisibility(View.GONE);
			lblListHeader.setTextColor(_context.getResources().getColor(R.color.edit_hint_zip));
		}else if(groupPosition==1){
			catImage.setVisibility(View.VISIBLE);
			expandButton.setVisibility(View.GONE);
			catImage.setImageResource(R.drawable.aisle_popular_icon);
			lblListHeader.setTextColor(_context.getResources().getColor(R.color.edit_hint_zip));
		}else{
			catImage.setVisibility(View.INVISIBLE);
			expandButton.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
