package com.taraba.gulfoilapp.royalty_user_view.main_activity.nav_drawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.drawerinterface.DrawerCallbacks;
import com.taraba.gulfoilapp.drawerinterface.NavigationItem;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.adapter.RoyaltyNavigationAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class RoyaltyNavigationFragment extends Fragment implements DrawerCallbacks {

    TypedArray navMenuIcons;
    Context context;
    String mStringType = "";
    //    public int count=0;
    private DrawerCallbacks mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private int mCurrentSelectedPosition;
    //  String type;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_royalty_navigation_drawer, container, false);

        if (isDrawerOpen()) {
            // Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_SHORT).show();

            hideKeyboard(getActivity());
        }
        mDrawerList = (RecyclerView) view.findViewById(R.id.royaltyDrawerList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setHasFixedSize(true);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        context = view.getContext();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (DrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar, String type) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);

        //Log.e("","count in setUp:"+count);

        mStringType = type;
        final List<NavigationItem> navigationItems = getMenu(type);
        RoyaltyNavigationAdapter adapter = new RoyaltyNavigationAdapter(navigationItems, context);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        //Commented By Pravin Dharam on 13MAY20200
        //selectItem(mCurrentSelectedPosition);


        mDrawerLayout = drawerLayout;

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
/*

                final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                count=ctdUser.getStatusCount();
                Log.e("notification count11", String.valueOf(count));
                ctdUser.close();
*/

                /*final List<NavigationItem> navigationItems = getMenu(mStringType);
                NavigationAdapter adapter = new NavigationAdapter(navigationItems,context);
                adapter.setNavigationDrawerCallbacks(this);
                mDrawerList.setAdapter(adapter);
                selectItem(mCurrentSelectedPosition);*/


                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
                hideKeyboard(getActivity());

            }
        };
        //mActionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ba8e57"));


        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic__0018_menubar3x, getActivity().getTheme());
        mActionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        mActionBarDrawerToggle.syncState();
        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
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

    public List<NavigationItem> getMenu(String type) {
/*

        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int count=ctdUser.getStatusCount();

        Log.e("","notification count"+count);
        ctdUser.close();
*/

        //Log.e("","count in getMenu"+count);

        List<NavigationItem> items = new ArrayList<NavigationItem>();

        //items.add(new NavigationItem("Home", navMenuIcons.getResourceId(0, -1)));
        items.add(new NavigationItem("Home", R.drawable.home_retail));
        items.add(new NavigationItem("My Profile", R.drawable.ic_profile));
        items.add(new NavigationItem("What's New", R.drawable.ic_whats_new));
        items.add(new NavigationItem("View Rewards", R.drawable.view_reward));
        items.add(new NavigationItem("Scheme Letter", R.drawable.ic_scheme_letter));
        items.add(new NavigationItem("Target Meter", R.drawable.ic_target_meter));
        items.add(new NavigationItem("Digital Rewards", R.drawable.ic__royalty_digital_reward));
        items.add(new NavigationItem("Campaign Rewards", R.drawable.ic_campaign_rewards));
        items.add(new NavigationItem("Royale Benefits", R.drawable.ic_royale_benefit));
        items.add(new NavigationItem("Change Password", R.drawable.ic_change_pass));
        items.add(new NavigationItem("Help", R.drawable.ic_new_help));
        items.add(new NavigationItem("Notification", R.drawable.whats_retail));
        items.add(new NavigationItem("Version", R.drawable.version_retail));
        items.add(new NavigationItem("Logout", R.drawable.logout_retail));

        return items;
    }

    void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((RoyaltyNavigationAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        // Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_SHORT).show();
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
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
}