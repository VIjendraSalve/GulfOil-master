package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomYesNoDialog;
import com.taraba.gulfoilapp.drawerinterface.DrawerCallbacks;
import com.taraba.gulfoilapp.fragment.HelpTabFragment;
import com.taraba.gulfoilapp.fragment.NavigationFragment;
import com.taraba.gulfoilapp.util.MyProfileFragment;
import com.taraba.gulfoilapp.util.OrientationUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Created by android1 on 12/17/15.
 * Modified by Mansi
 */
public class MainActivity extends AppCompatActivity implements DrawerCallbacks {

    public android.os.Handler mHandler;
    //    public int count=0;
    String mStringAction;
    ArrayList prgmName;
    TextView txtMsg;
    int strid;
    String mStringMobileNo;
    ImageView imgvw_search;
    String isOtp = "";
    String DialogStatus = "";
    int old_check = 0;
    int new_check = 0;
    TextView txtUnreadCount;
    String loginId;
    private Toolbar toolbar;
    private ListView listView;
    private List<UserModel> circularList;
    private CircularListAdapter adapter;
    private NavigationFragment mNavigationNavDrawerFragment;
    private Thread r;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object

        txtUnreadCount = (TextView) toolbar.findViewById(R.id.txtUnreadCount);
        txtUnreadCount.setVisibility(View.GONE);
        //txtUnreadCount.setText("100");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Grass Root");
        SharedPreferences preferences2 = MainActivity.this.getSharedPreferences(
                "signupdetails", MainActivity.this.MODE_PRIVATE);
        loginId = preferences2.getString("usertrno", "");


        SharedPreferences preferences1 = getSharedPreferences("dialoStatus", Context.MODE_PRIVATE);
        String DialogStatus = preferences1.getString("statusDialog", "");
        if (DialogStatus.equals("yes"))
            show_dialog_you_are_not_updated();

        Log.e("", "Main activity oncreate dialog status:" + mStringAction);

        SharedPreferences preference10 = getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String type = preference10.getString("user_type", "");
        mStringAction = type;
        // mStringAction = getIntent().getStringExtra("action");
        SharedPreferences preferences = (MainActivity.this).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        mNavigationNavDrawerFragment = (NavigationFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);

        /*SharedPreferences preferen1=getSharedPreferences("Count",Context.MODE_PRIVATE);

        int cnt=preferen1.getInt("new_count", 0);

        if(cnt==0) {
            mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar, mStringAction, new_check);
        }*/

        mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar, mStringAction);

/*
        if(new_check >= old_check) {
            mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar, mStringAction, new_check);
        }
*/


        if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment
                || getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {
            /*FragmentManager fm = getSupportFragmentManager();
            int ic=fm.getBackStackEntryCount();
            for(int i = 0; i <ic; ++i)
                fm.popBackStack();*/
        } else {
            getSupportFragmentManager().popBackStack();
        }
        if (mStringAction.equals("Retail Outlet")) {

            SharedPreferences.Editor e = preferences.edit();
            e.putString("action", mStringAction);
            e.commit();

            Fragment mechSearch = new MechanicDashboardFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, mechSearch);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();

        } else if (mStringAction.equals("otp")) {
            int n = 1;
            if (n == 1) {
                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(this, getString(R.string.window_cannot_open), "Gulf Oil");
                cod.show();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

           /* Fragment mechSearch = new RedeemMainFragment();
            FragmentTransaction ft = gactionetSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, mechSearch);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();*/

                Fragment mech2 = new RedeemMainFragment();
                //Fragment mech2 = new ComingSoonFragment();

                Bundle bundleRewards = new Bundle();
                bundleRewards.putString("isDisable", "false");
                bundleRewards.putString("path", "dashboard");
                mech2.setArguments(bundleRewards);

                SharedPreferences preference44 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id = preference44.getString("usertrno", "");
                Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                SharedPreferences pref = getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit1 = pref.edit();
                int lid = Integer.parseInt("" + login_id);
                // edit1.putInt("Mechanic_trno_to_redeem",lid);
                edit1.putString("Mechanic_status", "rewards");
                edit1.commit();

                FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                fthelp2.replace(R.id.container_body, mech2);
                fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fthelp2.addToBackStack(null);
                fthelp2.commit();

            }

        } else {
            SharedPreferences.Editor e = preferences.edit();
            e.putString("action", mStringAction);
            e.commit();

            Fragment dashboard = new DashboardFragment();
            FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
            ftp.replace(R.id.container_body, dashboard);
            ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftp.addToBackStack(null);
            ftp.commit();
        }
//exportDB();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        if (mNavigationNavDrawerFragment.isDrawerOpen()) {
            mNavigationNavDrawerFragment.closeDrawer();
        } else {
            if (RedeemMainFragment.getAllCategoryAsyncTask != null && RedeemMainFragment.getAllCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
                RedeemMainFragment.getAllCategoryAsyncTask.cancel(true);


            if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicRegistrationFragment) {
                SharedPreferences preferences = (MainActivity.this).getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);

                String mech_status = preferences.getString("Mechanic_status", "");

                Log.e("", "On back pressed mechanic status:__________________________" + mech_status);

                MechanicRegistrationFragment MechanFragment = new MechanicRegistrationFragment();


                if (mech_status.equals("profile")) {

                    SharedPreferences.Editor e = preferences.edit();
                    e.putString("Mechanic_status", "");
                    e.commit();

                    getSupportFragmentManager().popBackStack();
                } else {
                    SharedPreferences.Editor e = preferences.edit();
                    e.putString("Mechanic_status", "");
                    e.commit();

                    getSupportFragmentManager().popBackStack();
                    Fragment dashboard = new DashboardFragment();
                    FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
                    ftp.replace(R.id.container_body, dashboard, "Registration");
                    ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ftp.addToBackStack(null);
                    ftp.commit();
                }
            }
           /* else if(getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof ProceedOrderFragment) {

                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < 3; ++i) {
                    fm.popBackStack();
                }
            }*/
            else if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof RedeemMainFragment) {

                if (RedeemMainFragment.path.equals("") || RedeemMainFragment.path == null) {
                } else if (RedeemMainFragment.path.equals("dashboard")) {
                    if (mStringAction.equals("Retail Outlet")) {

                        Fragment mechSearch1 = new MechanicDashboardFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container_body, mechSearch1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {

                        Fragment mechSearch1 = new DashboardFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container_body, mechSearch1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (RedeemMainFragment.path.equals("search")) {
                    Log.e("main", "back");
                    Fragment mechSearch1 = new MechanicalSearchFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, mechSearch1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                //change by prashant 03-02-2017
                else if (RedeemMainFragment.path.equals("d")) {
                    Log.e("main", "back");
                    Fragment mechSearch1 = new DashboardFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, mechSearch1);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
            //    getSupportFragmentManager().popBackStack();

//            }
            else if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicalSearchFragment) {
                getSupportFragmentManager().popBackStack();
                Fragment mechSearch1 = new DashboardFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, mechSearch1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            } else if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment
                    || getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {


               /* if (RedeemMainFragment.getAllCategoryAsyncTask != null && RedeemMainFragment.getAllCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
                    RedeemMainFragment.getAllCategoryAsyncTask.cancel(true);

                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i){
                    fm.popBackStack();
                }

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(0);
                */
                new AlertDialog.Builder(this)
                        .setTitle("Gulf Oil")
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();

                                        finishAffinity();

                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                if (mStringAction.equals("Retail Outlet")) {
                                    Fragment mechSearch = new MechanicDashboardFragment();
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container_body, mechSearch);
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    ft.addToBackStack(null);
                                    ft.commit();
                                } else {
                                    Fragment mechSearch = new DashboardFragment();
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container_body, mechSearch);
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    ft.addToBackStack(null);
                                    ft.commit();

                                }

                            }
                        }).show();
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 1) {
                    super.onBackPressed();
                    finish();
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment ||
                        getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {
                    /*for(int i=0;i<count;i++)
                        getSupportFragmentManager().popBackStack();*/
                    super.onBackPressed();
                    finish();
                } else
                    getSupportFragmentManager().popBackStack();
            }
        }
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
//            finish();
//        }
//        else {
//            super.onBackPressed();
//        }
    }

    private void exportDB() {
        // get the primary external storage directory
        File sd = Environment.getExternalStorageDirectory();
        // Return the user data directory.
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        // path of current database with database name
        String currentDBPath = "/data/com.taraba.gulfoilapp/databases/GRGDATABASE.db";
        // database name for back up
        String backupDBPath = "GRGDATABASE.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            // transfer database from source to destination
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "item no: " + position + "-Selected", Toast.LENGTH_SHORT).show();

        //Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.linear_dashboard);
        if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment
                || getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {
        } else {
            getSupportFragmentManager().popBackStack();
        }
        switch (position) {

            case 0:
                if (mStringAction.equals("Retail Outlet")) {
                    if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {
                    } else {
                        Fragment mechSearch = new MechanicDashboardFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container_body, mechSearch);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else {
                    if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment) {
                    } else {
                        Fragment dashboard = new DashboardFragment();
                        FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
                        ftp.replace(R.id.container_body, dashboard);
                        ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftp.addToBackStack(null);
                        ftp.commit();

                    }
                }

                break;
            case 1:

                Log.e("Tag ", " Tag : My Profile");
                Fragment mechSearch = new MyProfileFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, mechSearch);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();

                break;
            case 2:
                if (mStringAction.equals("Retail Outlet")) {

                    Fragment notiFrag1 = new ClaimHistoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("participant_login_id", Integer.parseInt(loginId));
                    notiFrag1.setArguments(bundle);
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.container_body, notiFrag1);
                    ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft2.addToBackStack(null);
                    ft2.commit();
                   /* ConnectionDetector cd = new ConnectionDetector(this);
                    if (cd.isConnectingToInternet()) {

                        SharedPreferences preferences1 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);

                        mStringMobileNo=preferences1.getString("uname", "");

                        //mStringMobileNo = mEditTextMobNo.getText().toString();
                        new SearchMechanic().execute(new String[]{mStringMobileNo});
                    } else {
                        Toast.makeText(this,
                                "Internet Disconnected", Toast.LENGTH_LONG).show();
                    }
*/                   /* Fragment claimhitoryFragment = new ClaimHistoryFragment();
                    Bundle bundleclaim = new Bundle();


                    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences preferences1 = getSharedPreferences("signupdetails", Context.MODE_PRIVATE);
                    String strid = preferences1.getString("usertrno", "");

                    Log.e("strid",""+strid);
                    bundleclaim.putInt("participant_login_id", Integer.parseInt(strid));
                    UserTableDatasource ctdUser = new UserTableDatasource(this);
                    try {
                        ctdUser.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String part_code = ctdUser.getParticipantId(Integer.parseInt(strid));
                    bundleclaim.putString("participant_code", part_code);
                    Log.e("part id :", "Part id in adapter : " + strid+" , "+part_code);
                    claimhitoryFragment.setArguments(bundleclaim);
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.container_body, claimhitoryFragment);
                    ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft1.addToBackStack(null);
                    ft1.commit();*/
                } else {
                   /* Log.e("Tag ", " Tag : Add Mechanic");SharedPreferences preferences = getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putString("Mechanic_status", "main");
                    edit.commit();
                    Fragment mechanicRegistrationFragment = new MechanicRegistrationFragment();

                    FragmentTransaction ftmech = getSupportFragmentManager().beginTransaction();
                    ftmech.replace(R.id.container_body, mechanicRegistrationFragment);
                    ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ftmech.addToBackStack(null);
                    ftmech.commit();*/

                    // COMMENTED BY PRAVIN DHARAM ON 02-MAY-2019
                    // TO REMOVE EDIT REGISTRATION MENU FROM NAVI-DRAWER
                   /* SharedPreferences preferences = getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putString("Mechanic_status", "main");
                    edit.commit();
                    //Fragment mechanicRegistrationFragment = new MechanicRegistrationFragment();
                    //Fragment mechanicRegistrationFragment = new EditRegistrationRetailer();
                    Fragment mechanicRegistrationFragment = new RetailerRegistrationFragment();

                    FragmentTransaction ftmech = getSupportFragmentManager().beginTransaction();
                    ftmech.replace(R.id.container_body, mechanicRegistrationFragment, "Registration");
                    ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ftmech.addToBackStack(null);
                    ftmech.commit();*/

                    SharedPreferences preferences_search = getSharedPreferences(
                            "searchdetails", MODE_PRIVATE);
                    SharedPreferences.Editor edit_search = preferences_search.edit();
                    edit_search.putString(MyTrackConstants._mStringSerchData, "");
                    edit_search.putString(MyTrackConstants._mStringSerchtype, "");
                    edit_search.commit();

                    Log.e("Tag ", " Tag : Member Search");
                    Fragment mechSearch1 = new MechanicalSearchFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.container_body, mechSearch1);
                    ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft2.addToBackStack(null);
                    ft2.commit();


                }
                break;

            case 3:
                if (mStringAction.equals("Retail Outlet")) {
                    int n = 1;
                    if (n == 1) {
                        // com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(this, getString(R.string.window_cannot_open), "Gulf Oil");
                        // cod.show();
                        //************* added by sheetal for give link to reward page instead of popup**********
                        Fragment mech2 = new RedeemMainFragment();
                        FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                        fthelp2.replace(R.id.container_body, mech2);
                        fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fthelp2.addToBackStack(null);
                        fthelp2.commit();
                        //***************************
                    } else {
                        Fragment mech2 = new RedeemMainFragment();

                        SharedPreferences preferences = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id = preferences.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                        SharedPreferences pref = getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();

                        int lid = Integer.parseInt("" + login_id);
                        edit.putInt("Mechanic_trno_to_redeem", lid);
                        edit.putString("Mechanic_status", "rewards");
                        edit.commit();


                        FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                        fthelp2.replace(R.id.container_body, mech2);
                        fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fthelp2.addToBackStack(null);
                        fthelp2.commit();
                    }


                   /* Fragment mech2 = new ComingSoonFragment();

                    Bundle bundleRewards = new Bundle();
                    bundleRewards.putString("isDisable", "false");
                    bundleRewards.putString("path", "dashboard");
                    mech2.setArguments(bundleRewards);

                    SharedPreferences preferences1 =getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String login_id=preferences1.getString("usertrno", "");
                    Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                    SharedPreferences pref =getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = pref.edit();
                    int lid=Integer.parseInt(""+login_id);
                    edit1.putInt("Mechanic_trno_to_redeem",lid);
                    edit1.putString("Mechanic_status", "rewards");
                    edit1.commit();

                    FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                    fthelp2.replace(R.id.container_body, mech2);
                    fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp2.addToBackStack(null);
                    fthelp2.commit();*/
                } else {
                 /*   SharedPreferences preferences_search = getSharedPreferences(
                            "searchdetails", MODE_PRIVATE);
                    SharedPreferences.Editor edit_search = preferences_search.edit();
                    edit_search.putString(MyTrackConstants._mStringSerchData, "");
                    edit_search.putString(MyTrackConstants._mStringSerchtype, "");
                    edit_search.commit();

                    Log.e("Tag ", " Tag : Member Search");
                    Fragment mechSearch1 = new MechanicalSearchFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.container_body, mechSearch1);
                    ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft1.addToBackStack(null);
                    ft1.commit();*/

                    //  Fragment mech2 = new MechanicalSearchFragment();
/*

                    SharedPreferences preferences = getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String login_id=preferences.getString("usertrno", "");
                    Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                    SharedPreferences pref =getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();

                    int lid=Integer.parseInt(""+login_id);
                    edit.putInt("Mechanic_trno_to_redeem", lid);
                    edit.putString("Mechanic_status", "rewards");
                    edit.commit();

*/

                   /* SharedPreferences preferences_search = getSharedPreferences(
                            "searchdetails", MODE_PRIVATE);
                    SharedPreferences.Editor edit_search = preferences_search.edit();
                    edit_search.putString(MyTrackConstants._mStringSerchData, "");
                    edit_search.putString(MyTrackConstants._mStringSerchtype, "");
                    edit_search.commit();

                    Log.e("Tag ", " Tag : Member Search");
                    Fragment mechSearch1 = new MechanicalSearchFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.container_body, mechSearch1);
                    ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft2.addToBackStack(null);
                    ft2.commit();*/

                    Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();
                }
                break;
            case 4:
                if (mStringAction.equals("Retail Outlet")) {
                    Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();

                } else {
                    /*Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();*/
                   /* Fragment mech2 = new RedeemMainFragment();

                    Bundle bundleRewards = new Bundle();
                    bundleRewards.putString("isDisable", "true");
                    bundleRewards.putString("path", "dashboard");
                    mech2.setArguments(bundleRewards);

                    SharedPreferences preferences1 =getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String login_id=preferences1.getString("usertrno", "");
                    Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                    SharedPreferences pref =getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = pref.edit();
                    int lid=Integer.parseInt(""+login_id);
                    edit1.putInt("Mechanic_trno_to_redeem",lid);
                    edit1.putString("Mechanic_status", "rewards");
                    edit1.commit();

                    FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                    fthelp2.replace(R.id.container_body, mech2);
                    fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp2.addToBackStack(null);
                    fthelp2.commit();*/

                    Log.e("Tag ", " Tag : Help");
                    Fragment mechhelp_ = new HelpTabFragment();
                    FragmentTransaction fthelp_ = getSupportFragmentManager().beginTransaction();
                    fthelp_.replace(R.id.container_body, mechhelp_);
                    fthelp_.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp_.addToBackStack(null);
                    fthelp_.commit();
                }
                break;
            case 5:
                if (mStringAction.equals("Retail Outlet")) {
                    Log.e("Tag ", " Tag : Help");
                    Fragment mechhelp_ = new HelpTabFragment();
                    FragmentTransaction fthelp_ = getSupportFragmentManager().beginTransaction();
                    fthelp_.replace(R.id.container_body, mechhelp_);
                    fthelp_.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp_.addToBackStack(null);
                    fthelp_.commit();
                } else {
                    /*Log.e("Tag ", " Tag : Help");
                    Fragment mechhelp_ = new HelpTabFragment();
                    FragmentTransaction fthelp_ = getSupportFragmentManager().beginTransaction();
                    fthelp_.replace(R.id.container_body, mechhelp_);
                    fthelp_.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp_.addToBackStack(null);
                    fthelp_.commit();*/


                    Fragment mech2 = new RedeemMainFragment();

                    Bundle bundleRewards = new Bundle();

                    bundleRewards.putString("isDisable", "true");
                    bundleRewards.putString("path", "dashboard");
                    mech2.setArguments(bundleRewards);

                    SharedPreferences preferences1 = getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String login_id = preferences1.getString("usertrno", "");
                    Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                    SharedPreferences pref = getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = pref.edit();
                    int lid = Integer.parseInt("" + login_id);
                    edit1.putInt("Mechanic_trno_to_redeem", lid);
                    edit1.putString("Mechanic_status", "rewards");
                    edit1.commit();

                    FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                    fthelp2.replace(R.id.container_body, mech2);
                    fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp2.addToBackStack(null);
                    fthelp2.commit();

                }

                break;

            case 6:
                if (mStringAction.equals("Retail Outlet")) {
                    CustomYesNoDialog cdd = new CustomYesNoDialog(MainActivity.this);
                    cdd.show();

                /*    Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();*/
                } else {
//                    int n = 1;
//                    if (n == 1) {
//                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(MainActivity.this, getString(R.string.window_cannot_open), "Gulf Oil");
//                        cod.show();
//                    } else {
                   /* Fragment mech2 = new RedeemMainFragment();

                    Bundle bundleRewards = new Bundle();

                    bundleRewards.putString("isDisable", "true");
                    bundleRewards.putString("path", "dashboard");
                    mech2.setArguments(bundleRewards);

                    SharedPreferences preferences1 = getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String login_id = preferences1.getString("usertrno", "");
                    Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                    SharedPreferences pref = getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = pref.edit();
                    int lid = Integer.parseInt("" + login_id);
                    edit1.putInt("Mechanic_trno_to_redeem", lid);
                    edit1.putString("Mechanic_status", "rewards");
                    edit1.commit();

                    FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                    fthelp2.replace(R.id.container_body, mech2);
                    fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp2.addToBackStack(null);
                    fthelp2.commit();*/
                    //}

                    CustomYesNoDialog cdd = new CustomYesNoDialog(MainActivity.this);
                    cdd.show();

                }
                break;

            case 7:

                if (mStringAction.equals("Retail Outlet")) {
                    Fragment versionDetails = new DisplayVersionDetails();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();

                } else {
                    Fragment versionDetails = new DisplayNotification();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                   /* Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();*/


                }

                break;
         /*   case 8:
                if (mStringAction.equals("Retail Outlet")) {

                  *//*  Fragment versionDetails=new DisplayVersionDetails();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();*//*
                } else {
                    Fragment versionDetails = new DisplayVersionDetails();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                   *//* CustomYesNoDialog cdd = new CustomYesNoDialog(MainActivity.this);
                    cdd.show();*//*
                }
                break;*/

            case 9:
                if (mStringAction.equals("Retail Outlet")) {
                    // Fragment versionDetails=new DisplayNotificationId();
                    Fragment versionDetails = new ComingSoonFragment();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                } else {

                    Fragment versionDetails = new DisplayVersionDetails();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                }
                break;

            case 8:
                if (mStringAction.equals("participant")) {

                } else {

                    Fragment versionDetails = new DisplayNotification();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
//                    Fragment versionDetails = new DisplayVersionDetails();
//                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
//                    ft9.replace(R.id.container_body, versionDetails);
//                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft9.addToBackStack(null);
//                    ft9.commit();
                }
                break;
            default:
                if (mStringAction.equals("Retail Outlet")) {
                    if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {

                    } else {
                        Fragment mechSearch3 = new MechanicDashboardFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.container_body, mechSearch3);
                        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft3.addToBackStack(null);
                        ft3.commit();
                    }
                } else {
                    if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment) {

                    } else {
                        Fragment dashboard = new DashboardFragment();
                        FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
                        ftp.replace(R.id.container_body, dashboard);
                        ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftp.addToBackStack(null);
                        ftp.commit();
                    }
                }
                break;


        }
    }

    private void show_dialog_you_are_not_updated() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.you_are_not_updated_title));
        alertDialogBuilder.setMessage(getString(R.string.you_are_not_updated_message));
        alertDialogBuilder.setIcon(R.drawable.ic_action_collections_cloud_light);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                OrientationUtils.unlockOrientation(MainActivity.this);
            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                dialog.cancel();
                OrientationUtils.unlockOrientation(MainActivity.this);
            }
        });
        alertDialogBuilder.show();
    }

    class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {

                SharedPreferences preferences1 = getSharedPreferences(
                        "signupdetails", MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                String user_type = preferences1.getString("user_type", "");

                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetMechaniceParticipant(loginId);
            } catch (Exception e) {
                // TODO: handle exception
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");

            Log.e("", "JSON : " + jObj);
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "Number Not Registered");*/
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);
                        circularList = new ArrayList<UserModel>();
                        UserModel _news = new UserModel();
                        SharedPreferences preferences1 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        strid = Integer.parseInt(preferences1.getString("usertrno", ""));
                        //     strid = Integer.parseInt(result.getString("login_id_pk"));
                        final String total_points = result.getString("earned_points");
                        _news.setTotal_point(result.getString("earned_points"));
                        final String points = result.getString("points");
                        _news.setBalance_points(result.getString("points"));
                        final String redeemed_points = result.getString("redeemed_points");
                        _news.setRedeem_point(result.getString("redeemed_points"));

                        circularList.add(_news);

                        final UserTableDatasource ctdUser = new UserTableDatasource(MainActivity.this);
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory("" + strid);
                            ctdUser.insertClaimHistory(result, jObj.getJSONArray("participant_claim_history").toString(), "" + strid);
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), MainActivity.this, "" + strid);

                            ctdUser.close();
                        } else {
                            ctdUser.insertIntoHomework(result, "");
                            ctdUser.close();
                        }
                        progressDialog.dismiss();

                        new Handler().post(new Runnable() {
                            public void run() {

                                Fragment claimhitoryFragment = new ClaimHistoryFragment();
                                Bundle bundleclaim = new Bundle();

                                bundleclaim.putInt("participant_login_id", strid);
                                try {
                                    ctdUser.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                String part_code = ctdUser.getParticipantId(strid);
                                ctdUser.close();
                                bundleclaim.putString("participant_code", part_code);

                                bundleclaim.putString("action", "participant");
                                bundleclaim.putString("total_points", total_points);
                                bundleclaim.putString("redeem_points", redeemed_points);
                                bundleclaim.putString("balanced_points", points);
                                Log.e("part id :", "Part id in adapter : " + strid + " , " + part_code);
                                claimhitoryFragment.setArguments(bundleclaim);

                                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                                ft1.replace(R.id.container_body, claimhitoryFragment);
                                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft1.addToBackStack(null);
                                ft1.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }
    }


}