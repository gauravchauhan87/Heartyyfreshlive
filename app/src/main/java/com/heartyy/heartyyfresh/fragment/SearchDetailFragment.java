package com.heartyy.heartyyfresh.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heartyy.heartyyfresh.R;
import com.heartyy.heartyyfresh.adapter.SearchDetailAdapter;
import com.heartyy.heartyyfresh.adapter.SearchSectionGridRecyclerViewAdapter;
import com.heartyy.heartyyfresh.bean.SearchProductsBean;
import com.heartyy.heartyyfresh.bean.SubAisleItemBean;
import com.heartyy.heartyyfresh.global.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay on 2/9/2016.
 */
public class SearchDetailFragment extends Fragment {

    private View rootView;
    private Context context;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    SearchDetailAdapter adapter;
    private List<SearchProductsBean> searchProductsBeanList;
    private List<SubAisleItemBean> subAisleItemBeanList;



    public SearchDetailFragment(){

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search_detail, container,false);
        context = getActivity().getApplicationContext();
        searchProductsBeanList = Global.searchProductsBeanList;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context, 3);

        subAisleItemBeanList = new ArrayList<>();
        for (int i = 0; i < searchProductsBeanList.size(); i++) {
            List<SubAisleItemBean> itemBeanList = searchProductsBeanList.get(i).getSubAisleItemBeans();
            for (int j = 0; j < itemBeanList.size(); j++) {
                SubAisleItemBean itemBean = itemBeanList.get(j);
                subAisleItemBeanList.add(itemBean);
            }
        }

        adapter = new SearchDetailAdapter(context,subAisleItemBeanList,getActivity());
        List<SearchSectionGridRecyclerViewAdapter.Section> sections = new ArrayList<SearchSectionGridRecyclerViewAdapter.Section>();

        int j = 0;
        int count = 0;

        for(int i=0;i<searchProductsBeanList.size();i++){
            List<SubAisleItemBean> subList = searchProductsBeanList.get(i).getSubAisleItemBeans();
            count += subList.size();
            sections.add(new SearchSectionGridRecyclerViewAdapter.Section(j,searchProductsBeanList.get(i).getSubCategoryName()));
            j=count;
        }

        SearchSectionGridRecyclerViewAdapter.Section[] dummy = new SearchSectionGridRecyclerViewAdapter.Section[sections.size()];
        SearchSectionGridRecyclerViewAdapter mSectionedAdapter = new SearchSectionGridRecyclerViewAdapter(context, R.layout.search_section, R.id.section_text, recyclerView, adapter, mLayoutManager, getActivity());
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mSectionedAdapter);



        return rootView;

    }
}
