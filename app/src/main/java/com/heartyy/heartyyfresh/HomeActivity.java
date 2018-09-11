package com.heartyy.heartyyfresh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.heartyy.heartyyfresh.fragment.NeighbourhoodFragment;
import com.heartyy.heartyyfresh.utils.*;
import com.heartyy.heartyyfresh.utils.Constants;


public class HomeActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    Typeface andBold, bold, italic, light;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    Context context;
    private SharedPreferences pref;
    Bundle bundle;
    private String zip;
    private DrawerLayout mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.hamburger);
        andBold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_REGULAR);
        bold = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_BOLD);
        italic = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_ITALIC);
        light = Typeface.createFromAsset(getAssets(),
                Fonts.ROBOTO_LIGHT);
        SpannableString s = new SpannableString(getResources().getString(R.string.title_activity_home));
        s.setSpan(new TypefaceSpan(this, Fonts.HEADER), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            zip = getIntent().getExtras().getString("zip");
        }
        mDrawerToggle = (DrawerLayout) findViewById(R.id.drawer);
        // homeIcon = (ImageView) mToolbar.findViewById(R.id.toolbar_image);
        pref = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerToggle.openDrawer(Gravity.LEFT);
            }
        });


        String userid = pref.getString(Constants.USER_ID, null);
        if (userid == null) {
            Fragment fragment = new NeighbourhoodFragment();
            if (fragment != null) {
                try{
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                Log.e("MainActivity", "Error in creating fragment");
            }
        }

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(final int position) {
        if (position == 0) {
            displayView(position);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayView(position);
                }
            }, 1500);
        }


    }

    private void displayView(int position) {

        Fragment fragment = null;

        switch (position) {

            case 0:
                fragment = new NeighbourhoodFragment();
                break;

            case 1:
                Intent intent1 = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivityForResult(intent1, 0);
                break;
            case 2:
                Intent intent2 = new Intent(HomeActivity.this, PaymentActivity.class);
                startActivityForResult(intent2, 0);
                break;
            case 3:
                Intent intent3 = new Intent(HomeActivity.this, DeliveryLocationActivity.class);
                startActivityForResult(intent3, 0);
                break;

            case 4:
                Intent intent4 = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivityForResult(intent4, 0);
                break;

            case 5:
                Intent intent5 = new Intent(HomeActivity.this, NewPromotionsActivity.class);
                startActivityForResult(intent5, 0);
                break;

            case 6:
                Intent intent6 = new Intent(HomeActivity.this, SavedItemsActivity.class);
                startActivityForResult(intent6, 0);
                break;


            case 7:
                Intent intent7 = new Intent(HomeActivity.this, HelpActivity.class);
                startActivityForResult(intent7, 0);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();

            // update selected item and title, then close the drawer

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void setCount(String promotionCount, String currentOrderCount){
        mNavigationDrawerFragment.setCount(promotionCount,currentOrderCount);
    }

}
