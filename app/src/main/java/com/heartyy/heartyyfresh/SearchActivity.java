package com.heartyy.heartyyfresh;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.heartyy.heartyyfresh.adapter.*;
import com.heartyy.heartyyfresh.bean.*;
import com.heartyy.heartyyfresh.fragment.SearchDetailFragment;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.helper.ConversionHelper;
import com.heartyy.heartyyfresh.utils.Constants;
import com.heartyy.heartyyfresh.utils.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SharedPreferences pref;
    private EditText editSearch;
    private RelativeLayout searchItemsLayout, aisleItemslayout;
    private ListView searchItemsListView, aisleItemsListView, recentSearchList, trendingListView;
    private Typeface regular, meduimItalic, medium, robotoLight, bold;
    private SearchBean searchBean;
    private CustomSearchItemsAdapter adapter;
    private CustomSearchAisleItemsAdapter aisleAdapter;
    private List<SearchSubAisleBean> subAisleBeanList;
    private RelativeLayout searchDetaillayout;
    private SearchDetailBean searchDetailBean;
    private ScrollView scrollView;
    private boolean isSearchDetail = false;
    private TextView recentSearchText, trendingText;
    private SearchTrendingBean searchTrendingBean;
    private CustomRecentSearchAdapter recentSearchAdapter;
    private CustomTrendingAdapter trendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        robotoLight = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        regular = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        meduimItalic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_MEDIUM_ITALIC);
        medium = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_MEDIUM);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        editSearch = (EditText) mToolbar.findViewById(R.id.edit_search);
        searchItemsLayout = (RelativeLayout) findViewById(R.id.layout_search_items_text);
        aisleItemslayout = (RelativeLayout) findViewById(R.id.layout_aisle_items_text);
        searchItemsListView = (ListView) findViewById(R.id.list_search_items);
        aisleItemsListView = (ListView) findViewById(R.id.list_aisle_items);
        searchDetaillayout = (RelativeLayout) findViewById(R.id.layout_search_detail_fragment);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        recentSearchText = (TextView) findViewById(R.id.text_recent_search);
        trendingText = (TextView) findViewById(R.id.text_trending_search);
        recentSearchList = (ListView) findViewById(R.id.list_recent_search);
        trendingListView = (ListView) findViewById(R.id.list_trending);

        recentSearchText.setTypeface(regular);
        trendingText.setTypeface(regular);

        getRecentSearches();

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("before search text", editSearch.getText().toString());

                if (editSearch.getText().toString().length() == 1) {
                    recentSearchText = (TextView) findViewById(R.id.text_recent_search);
                    trendingText = (TextView) findViewById(R.id.text_trending_search);
                    recentSearchList = (ListView) findViewById(R.id.list_recent_search);
                    trendingListView = (ListView) findViewById(R.id.list_trending);
                    recentSearchText.setVisibility(View.VISIBLE);
                    recentSearchList.setVisibility(View.VISIBLE);
                    trendingListView.setVisibility(View.VISIBLE);
                    trendingText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             /*   if (editSearch.getText().toString().equalsIgnoreCase("")) {
                    recentSearchText.setVisibility(View.VISIBLE);
                    recentSearchList.setVisibility(View.VISIBLE);
                } else {
                    recentSearchText.setVisibility(View.GONE);
                    recentSearchList.setVisibility(View.GONE);
                }*/
                Log.d("on search text", editSearch.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("search text", editSearch.getText().toString());
                /*Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getSearchItems(editSearch.getText().toString());
                    }
                });*/

                recentSearchText.setVisibility(View.GONE);
                recentSearchList.setVisibility(View.GONE);
                trendingListView.setVisibility(View.GONE);
                trendingText.setVisibility(View.GONE);


                getSearchItems(editSearch.getText().toString());

            }
        });

        searchItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchTagBean searchTagBean = (SearchTagBean) parent.getItemAtPosition(position);
                Log.d("tag", searchTagBean.getTag());
                String st = searchTagBean.getTag();
                String st1 = searchTagBean.getTag();
                st = st.replace(" ", "%20");
                getSearchDetails(st, st1);
            }
        });

        aisleItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SearchSubAisleBean searchSubAisleBean = (SearchSubAisleBean) parent.getItemAtPosition(position);
                Global.subCategoryId = searchSubAisleBean.getSubCategoryId();
                Global.topCategoryId = searchSubAisleBean.getTopCategoryId();
                Global.isSearchData = true;
                finish();

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String temp = null;
                        if (searchSubAisleBean.getSubCategoryName() == null) {
                            temp = searchSubAisleBean.getTopcategoryName();
                        } else {
                            temp = searchSubAisleBean.getSubCategoryName();
                        }
                        SaveSearchItems(temp);
                    }
                });
            }
        });

        recentSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentSearchBean recentSearchBean = (RecentSearchBean) parent.getItemAtPosition(position);
                String st = recentSearchBean.getSearchTag();
                editSearch.setText(st);
            }
        });
        trendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrendingBean trendingBean = (TrendingBean) parent.getItemAtPosition(position);
                String st = trendingBean.getTag();
                editSearch.setText(st);

            }
        });
    }

    private void getRecentSearches() {

        Global.showProgress(this);
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        String userId = pref.getString(Constants.USER_ID, null);

        String url = "user/search_tag?user_id=" + userId;


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("recent search response", jsonObject.toString());
                        Global.dialog.dismiss();
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                searchTrendingBean = ConversionHelper.convertRecentSearchJsonToRecentSearchbean(jsonObject);
                                List<RecentSearchBean> recentSearchBeanList = searchTrendingBean.getRecentSearchBeanList();
                                List<TrendingBean> trendingBeanList = searchTrendingBean.getTrendingBeanList();
                                if (recentSearchBeanList != null) {
                                    if (recentSearchBeanList.size() > 0) {
                                        recentSearchText.setVisibility(View.VISIBLE);
                                        recentSearchList.setVisibility(View.VISIBLE);
                                        recentSearchAdapter = new CustomRecentSearchAdapter(SearchActivity.this, recentSearchBeanList);
                                        recentSearchList.setAdapter(recentSearchAdapter);
                                        Global.setListViewHeightBasedOnChildren(recentSearchList);
                                    } else {
                                        recentSearchText.setVisibility(View.GONE);
                                        recentSearchList.setVisibility(View.GONE);
                                    }
                                } else {
                                    recentSearchText.setVisibility(View.GONE);
                                    recentSearchList.setVisibility(View.GONE);
                                }

                                if (trendingBeanList != null) {
                                    if (trendingBeanList.size() > 0) {
                                        trendingText.setVisibility(View.VISIBLE);
                                        trendingListView.setVisibility(View.VISIBLE);
                                        trendingAdapter = new CustomTrendingAdapter(SearchActivity.this, trendingBeanList);
                                        trendingListView.setAdapter(trendingAdapter);
                                        Global.setListViewHeightBasedOnChildren(trendingListView);
                                    } else {
                                        trendingText.setVisibility(View.GONE);
                                        trendingListView.setVisibility(View.GONE);
                                    }
                                } else {
                                    trendingText.setVisibility(View.GONE);
                                    trendingListView.setVisibility(View.GONE);
                                }


                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getSearchDetails(final String str, final String st1) {
        if (!str.isEmpty()) {
            Global.showProgress(this);
            RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
            String suppliedId = pref.getString(Constants.SUPPLIER_ID, null);

            String url = "search?supplier_id=" + suppliedId + "&tag=" + str + "&type=";


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject jsonObject) {
                            Log.d("response", jsonObject.toString());
                            Global.dialog.dismiss();
                            try {
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                    searchDetailBean = ConversionHelper.convertSearchDetailJsonToSearchDetailBean(jsonObject);
                                    if (searchDetailBean.getSearchProductsBeanList() != null) {
                                        isSearchDetail = true;
                                        scrollView.setVisibility(View.GONE);
                                        searchDetaillayout.setVisibility(View.VISIBLE);
                                        Global.searchProductsBeanList = searchDetailBean.getSearchProductsBeanList();
                                        SearchDetailFragment searchDetailFragment = new SearchDetailFragment();
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.search_detail_fragment, searchDetailFragment).commit();

                                        Handler handler = new Handler();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                SaveSearchItems(st1);
                                            }
                                        });
                                    }

                                } else if (status.equalsIgnoreCase(Constants.ERROR)) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Global.dialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", "Error: " + error.toString());
                    Global.dialog.dismiss();
                    if (error instanceof NoConnectionError) {
                        showAlert(Constants.NO_INTERNET);
                    } else {
                        showAlert(Constants.REQUEST_TIMED_OUT);
                    }
                }
            });

// Adding request to request queue
            rq.add(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    private void SaveSearchItems(String str) {
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", pref.getString(Constants.USER_ID, null));
            JSONArray array = new JSONArray();
            array.put(str);
            params.put("tags", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("params---", params.toString());

        String url = "user/search_tag";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.URL + url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject jsonObject) {
                        Log.d("save search response", jsonObject.toString());

                        try {
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                            } else if (status.equalsIgnoreCase(Constants.ERROR)) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Global.dialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error: " + error.toString());
                Global.dialog.dismiss();
                if (error instanceof NoConnectionError) {
                    showAlert(Constants.NO_INTERNET);
                } else {
                    showAlert(Constants.REQUEST_TIMED_OUT);
                }
            }
        });

// Adding request to request queue
        rq.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getSearchItems(String str) {
        final String temp = str;
        if (str.isEmpty()) {
            recentSearchText.setVisibility(View.VISIBLE);
            recentSearchList.setVisibility(View.VISIBLE);
            trendingListView.setVisibility(View.VISIBLE);
            trendingText.setVisibility(View.VISIBLE);
            adapter = new CustomSearchItemsAdapter(SearchActivity.this, SearchActivity.this, str);
            adapter.removeItem();
            searchItemsListView.setAdapter(adapter);
            Global.setListViewHeightBasedOnChildren(searchItemsListView);
            searchItemsLayout.setVisibility(View.GONE);

            aisleAdapter = new CustomSearchAisleItemsAdapter(SearchActivity.this, SearchActivity.this);
            aisleAdapter.removeItem();
            aisleItemsListView.setAdapter(aisleAdapter);
            Global.setListViewHeightBasedOnChildren(aisleItemsListView);
            aisleItemslayout.setVisibility(View.GONE);


        } else {


            RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
            String suppliedId = pref.getString(Constants.SUPPLIER_ID, null);
            str = str.replace(" ", "%20");
            String url = "search?supplier_id=" + suppliedId + "&tag=" + str;


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constants.URL + url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject jsonObject) {
                            Log.d("response", jsonObject.toString());
                            Global.dialog.dismiss();
                            try {
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                    searchBean = ConversionHelper.convertSearchJsonToSearchBean(jsonObject);
                                    if (temp.equalsIgnoreCase(editSearch.getText().toString())) {
                                        adapter = new CustomSearchItemsAdapter(SearchActivity.this, SearchActivity.this, temp);
                                        aisleAdapter = new CustomSearchAisleItemsAdapter(SearchActivity.this, SearchActivity.this);
                                        if (searchBean.getSearchTagBeanList() != null) {
                                            if (searchBean.getSearchTagBeanList().size() > 0) {
                                                for (int i = 0; i < searchBean.getSearchTagBeanList().size(); i++) {
                                                    adapter.addItem(searchBean.getSearchTagBeanList().get(i));
                                                }
                                                searchItemsListView.setAdapter(adapter);
                                                Global.setListViewHeightBasedOnChildren(searchItemsListView);
                                                searchItemsLayout.setVisibility(View.VISIBLE);
                                            } else {
                                                adapter = new CustomSearchItemsAdapter(SearchActivity.this, SearchActivity.this, temp);
                                                adapter.removeItem();
                                                searchItemsListView.setAdapter(adapter);
                                                Global.setListViewHeightBasedOnChildren(searchItemsListView);
                                                searchItemsLayout.setVisibility(View.GONE);

                                            }

                                        } else {
                                            adapter = new CustomSearchItemsAdapter(SearchActivity.this, SearchActivity.this, temp);
                                            adapter.removeItem();
                                            searchItemsListView.setAdapter(adapter);
                                            Global.setListViewHeightBasedOnChildren(searchItemsListView);
                                            searchItemsLayout.setVisibility(View.GONE);
                                        }


                                        if (searchBean.getSearchSubAisleBeanList() != null) {
                                            if (searchBean.getSearchSubAisleBeanList().size() > 0) {
                                                for (int i = 0; i < searchBean.getSearchSubAisleBeanList().size(); i++) {
                                                    aisleAdapter.addItem(searchBean.getSearchSubAisleBeanList().get(i));
                                                }
                                                aisleItemsListView.setAdapter(aisleAdapter);
                                                Global.setListViewHeightBasedOnChildren(aisleItemsListView);
                                                aisleItemslayout.setVisibility(View.VISIBLE);

                                            } else {
                                                aisleAdapter = new CustomSearchAisleItemsAdapter(SearchActivity.this, SearchActivity.this);
                                                aisleAdapter.removeItem();
                                                aisleItemsListView.setAdapter(aisleAdapter);
                                                Global.setListViewHeightBasedOnChildren(aisleItemsListView);
                                                aisleItemslayout.setVisibility(View.GONE);
                                            }
                                        } else {
                                            aisleAdapter = new CustomSearchAisleItemsAdapter(SearchActivity.this, SearchActivity.this);
                                            aisleAdapter.removeItem();
                                            aisleItemsListView.setAdapter(aisleAdapter);
                                            Global.setListViewHeightBasedOnChildren(aisleItemsListView);
                                            aisleItemslayout.setVisibility(View.GONE);
                                        }


                                    }


                                } else if (status.equalsIgnoreCase(Constants.ERROR)) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Global.dialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", "Error: " + error.toString());
                    Global.dialog.dismiss();
                    if (error instanceof NoConnectionError) {
                        showAlert(Constants.NO_INTERNET);
                    } else {
                        showAlert(Constants.REQUEST_TIMED_OUT);
                    }
                }
            });

// Adding request to request queue
            rq.add(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    }

    private void showAlert(String msg) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(SearchActivity.this);
        View promptsView = layoutInflater.inflate(
                R.layout.error_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SearchActivity.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            if (isSearchDetail) {
                scrollView.setVisibility(View.VISIBLE);
                searchDetaillayout.setVisibility(View.GONE);
                isSearchDetail = false;
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
