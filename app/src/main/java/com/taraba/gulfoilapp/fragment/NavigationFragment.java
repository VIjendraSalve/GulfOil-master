package com.taraba.gulfoilapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.NavigationAdapter;
import com.taraba.gulfoilapp.drawerinterface.DrawerCallbacks;
import com.taraba.gulfoilapp.drawerinterface.NavigationItem;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class NavigationFragment extends Fragment implements DrawerCallbacks {

    //    public int count=0;
    private DrawerCallbacks mCallbacks;
    private RecyclerView mDrawerList;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private int mCurrentSelectedPosition;
    TypedArray navMenuIcons;
    Context context;
    String mStringType = "";
    //  String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        if (isDrawerOpen()) {
            // Toast.makeText(getActivity(),"Drawer open",Toast.LENGTH_SHORT).show();

            hideKeyboard(getActivity());
        }
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);

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
        NavigationAdapter adapter = new NavigationAdapter(navigationItems, context);
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

        if (type.equals("Retail Outlet")) {
            items.add(new NavigationItem(" Home", navMenuIcons.getResourceId(0, -1)));
            items.add(new NavigationItem(" My Profile", navMenuIcons.getResourceId(1, -1)));
            items.add(new NavigationItem("View Transactions", navMenuIcons.getResourceId(2, -1)));
            // items.add(new NavigationItem(" Notifications",navMenuIcons.getResourceId(3, -1)));
            items.add(new NavigationItem(" Redeem", navMenuIcons.getResourceId(4, -1)));
            items.add(new NavigationItem("Change Password", navMenuIcons.getResourceId(13, -1)));

//            items.add(new NavigationItem(" Genuine Verification",navMenuIcons.getResourceId(5, -1)));
            items.add(new NavigationItem(" Help", navMenuIcons.getResourceId(6, -1)));
         /*   items.add(new NavigationItem(" About Us",navMenuIcons.getResourceId(7, -1)));
            items.add(new NavigationItem("Terms and Conditions",navMenuIcons.getResourceId(8, -1)));
            items.add(new NavigationItem(" FAQ's",navMenuIcons.getResourceId(9, -1)));*/
            items.add(new NavigationItem(" Logout", navMenuIcons.getResourceId(12, -1)));
            items.add(new NavigationItem(" Version", navMenuIcons.getResourceId(14, -1)));
            // items.add(new NavigationItem(" Notification Id",navMenuIcons.getResourceId(15, -1)));
            items.add(new NavigationItem(" Notifications", navMenuIcons.getResourceId(3, -1)));
        } else {
            items.add(new NavigationItem(" Home", navMenuIcons.getResourceId(0, -1)));
            items.add(new NavigationItem(" My Profile", navMenuIcons.getResourceId(1, -1)));
            //items.add(new NavigationItem(" Edit Registration", navMenuIcons.getResourceId(10, -1)));
            //items.add(new NavigationItem(" Notifications", navMenuIcons.getResourceId(3, -1)));

            items.add(new NavigationItem(" Member Search", navMenuIcons.getResourceId(11, -1)));
            items.add(new NavigationItem("Change Password", navMenuIcons.getResourceId(13, -1)));
            items.add(new NavigationItem(" Help", navMenuIcons.getResourceId(6, -1)));
            items.add(new NavigationItem(" View Rewards", navMenuIcons.getResourceId(4, -1)));

            //items.add(new NavigationItem(" Genuine Verification", navMenuIcons.getResourceId(5, -1)));

          /*  items.add(new NavigationItem(" About Us", navMenuIcons.getResourceId(7, -1)));
            items.add(new NavigationItem("Terms and Conditions",navMenuIcons.getResourceId(8, -1)));
            items.add(new NavigationItem(" FAQ's",navMenuIcons.getResourceId(9, -1)));*/

            items.add(new NavigationItem(" Logout", navMenuIcons.getResourceId(12, -1)));
            items.add(new NavigationItem(" Notifications", navMenuIcons.getResourceId(3, -1)));
            items.add(new NavigationItem(" Version", navMenuIcons.getResourceId(14, -1)));
            // items.add(new NavigationItem(" Notification Id",navMenuIcons.getResourceId(15, -1)));

        }

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
        ((NavigationAdapter) mDrawerList.getAdapter()).selectPosition(position);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}