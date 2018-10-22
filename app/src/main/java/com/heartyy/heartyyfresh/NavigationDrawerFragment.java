package com.heartyy.heartyyfresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.toolbox.ImageLoader;
import com.heartyy.heartyyfresh.global.Global;
import com.heartyy.heartyyfresh.utils.AppController;
import com.heartyy.heartyyfresh.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREFERENCES_FILE = "my_app_settings";
    private Context mContext;
    private NavigationDrawerCallbacks mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private SharedPreferences pref;
    private View view;
    List<NavigationItem> navigationItems = null;
    NavigationDrawerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        pref = mContext.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        Typeface andBold = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/Roboto-Light.ttf");
        String userid = pref.getString(Constants.USER_ID, null);
        if (userid == null) {

            view = inflater.inflate(R.layout.get_account, container, false);
            ViewGroup root = (ViewGroup) view.findViewById(R.id.getaccount_main);
            Global.setFont(root, andBold);
            Button creatAccountBtn = (Button) view.findViewById(R.id.button_create_account);
            Button alreadymemberBtn = (Button) view.findViewById(R.id.button_already_member);

            creatAccountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            alreadymemberBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } else {

            view = inflater.inflate(R.layout.fragment_navigation_google, container, false);
            mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mDrawerList.setLayoutManager(layoutManager);
            mDrawerList.setHasFixedSize(true);
            mDrawerList.bringToFront();

            navigationItems = getMenu();
            adapter = new NavigationDrawerAdapter(mContext,navigationItems);
            adapter.setNavigationDrawerCallbacks(this);
            mDrawerList.setAdapter(adapter);
            selectItem(mCurrentSelectedPosition);
            ImageView profileImage = (ImageView) view.findViewById(R.id.img_profile);
            TextView userText = (TextView) view.findViewById(R.id.txtUsername);
            userText.setText(pref.getString(Constants.FIRST_NAME, null)+" "+pref.getString(Constants.LAST_NAME,null));
            userText.setTypeface(andBold);
            String path = pref.getString(Constants.PICTURE, null);
            if (path != null) {
                setUserProfileImage(profileImage);
            }
        }


        return view;
    }

    private void setUserProfileImage(final ImageView profileImage) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                imageLoader.get(Constants.IMG_URL + pref.getString(Constants.PICTURE, null), ImageLoader.getImageListener(
                        profileImage, R.drawable.user_icon, R.drawable.user_icon));

                Cache cache = AppController.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(Constants.IMG_URL + pref.getString(Constants.PICTURE, null));
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
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity){
            activity=(Activity) context;
            try {
                mCallbacks = (NavigationDrawerCallbacks) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
            }
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }*/

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        if (mFragmentContainerView.getParent() instanceof ScrimInsetsFrameLayout) {
            mFragmentContainerView = (View) mFragmentContainerView.getParent();
        }
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(getActivity().getBaseContext(),R.color.ColorPrimaryDark));
        mDrawerLayout.requestLayout();

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public List<NavigationItem> getMenu() {
        List<NavigationItem> items = new ArrayList<NavigationItem>();
        items.add(new NavigationItem("Home", ContextCompat.getDrawable(mContext,R.drawable.home_icon), "0"));
        items.add(new NavigationItem("Profile", ContextCompat.getDrawable(mContext,R.drawable.profile_icon), "0"));
        items.add(new NavigationItem("Payment", ContextCompat.getDrawable(mContext,R.drawable.payment_icon), "0"));
        items.add(new NavigationItem("Delivery Locations", ContextCompat.getDrawable(mContext,R.drawable.location_icon12), "0"));
        items.add(new NavigationItem("Orders", ContextCompat.getDrawable(mContext,R.drawable.order_icon), "0"));
        items.add(new NavigationItem("Promotions", ContextCompat.getDrawable(mContext,R.drawable.promotion_icon), "0"));
        items.add(new NavigationItem("Saved Items", ContextCompat.getDrawable(mContext,R.drawable.diet_icon), "0"));
        items.add(new NavigationItem("Help", ContextCompat.getDrawable(mContext,R.drawable.help_icon), "0"));
        return items;
    }

    /**
     * Changes the icon of the drawer to back
     */
    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Changes the icon of the drawer to menu
     */
    public void showDrawerButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mActionBarDrawerToggle.syncState();
    }

    void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public void setCount(String promotionCount, String currentOrderCount) {

        if (pref.getString(Constants.USER_ID, null) != null) {
            if (navigationItems == null) {
                navigationItems = getMenu();
            }
            NavigationItem orderItem = navigationItems.get(4);
            NavigationItem promoItem = navigationItems.get(5);
            orderItem.setCount(currentOrderCount);
            promoItem.setCount(promotionCount);

            navigationItems.set(4, orderItem);
            navigationItems.set(5, promoItem);

            adapter.setNewitems(navigationItems);
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        AppController.getRefWatcher(getActivity()).watch(this);
    }*/
}
