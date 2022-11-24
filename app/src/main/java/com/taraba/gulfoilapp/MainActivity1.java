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
import com.taraba.gulfoilapp.util.NetworkUtils;
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

/**
 * Created by android1 on 12/17/15.
 * Modified by Mansi
 */
public class MainActivity1 extends AppCompatActivity implements DrawerCallbacks {

    private Toolbar toolbar;
    //    public int count=0;
    String mStringAction;
    ArrayList prgmName;
    private ListView listView;
    private List<UserModel> circularList;
    private CircularListAdapter adapter;
    TextView txtMsg;
    int strid;
    String mStringMobileNo;
    ImageView imgvw_search;
    private NavigationFragment mNavigationNavDrawerFragment;
    String isOtp = "";
    String DialogStatus = "";


    TextView txtUnreadCount;


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

        SharedPreferences preferences1 = getSharedPreferences("dialoStatus", Context.MODE_PRIVATE);
        String DialogStatus = preferences1.getString("statusDialog", "");
        if (DialogStatus.equals("yes"))
            show_dialog_you_are_not_updated();

        Log.e("", "Main activity oncreate dialog status:" + mStringAction);
        mStringAction = getIntent().getStringExtra("action");
        SharedPreferences preferences = (MainActivity1.this).getSharedPreferences(
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

        if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment || getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {
            /*FragmentManager fm = getSupportFragmentManager();
            int ic=fm.getBackStackEntryCount();
            for(int i = 0; i <ic; ++i)
                fm.popBackStack();*/
        } else {
            getSupportFragmentManager().popBackStack();
        }
        if (mStringAction.equals("participant")) {

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

            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            Fragment mechSearch = new RedeemMainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, mechSearch);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
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

    }

    @Override
    public void onBackPressed() {
        if (mNavigationNavDrawerFragment.isDrawerOpen()) {
            mNavigationNavDrawerFragment.closeDrawer();
        } else {
            if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicRegistrationFragment) {
                SharedPreferences preferences = (MainActivity1.this).getSharedPreferences(
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
/*

            if(RedeemMainFragment.path.equals("") || RedeemMainFragment.path==null)
            {

            }
            else if(RedeemMainFragment.path.equals("dashboard")){
                if(mStringAction.equals("participant")) {
*/

                Fragment mechSearch1 = new MechanicDashboardFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, mechSearch1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
/*
                }
                else
                {
*/

/*
                    Fragment mechSearch1 = new DashboardFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, mechSearch1);
                    ft.setTransitio

                    n(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                }
*/
            } else if (RedeemMainFragment.path.equals("search")) {
                Fragment mechSearch1 = new MechanicalSearchFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container_body, mechSearch1);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
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
            } else if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment) {
             /*   int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 1) {
                    super.onBackPressed();
                    finish();
                } else if(getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment ||
                        getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment){
                    *//*for(int i=0;i<count;i++)
                        getSupportFragmentManager().popBackStack();*//*
                    super.onBackPressed();
                    finish();
                }
                else
                    getSupportFragmentManager().popBackStack();}*/

                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                new AlertDialog.Builder(this)
                        .setTitle("eMPOWER")
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();

                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
// finish();
                                        System.exit(0);

                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                                if (mStringAction.equals("participant")) {

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
        if (getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof DashboardFragment || getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof MechanicDashboardFragment) {

        } else {
            getSupportFragmentManager().popBackStack();
        }
        switch (position) {

            case 0:
                if (mStringAction.equals("participant")) {
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
                if (mStringAction.equals("participant")) {

                    if (NetworkUtils.isNetworkAvailable(this)) {

                        SharedPreferences preferences1 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);

                        mStringMobileNo = preferences1.getString("uname", "");

                        //mStringMobileNo = mEditTextMobNo.getText().toString();
                        new SearchMechanic().execute(new String[]{mStringMobileNo});
                    } else {
                        Toast.makeText(this,
                                "Internet Disconnected", Toast.LENGTH_LONG).show();
                    }
                   /* Fragment claimhitoryFragment = new ClaimHistoryFragment();
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
                    Log.e("Tag ", " Tag : Add Mechanic");
                    SharedPreferences preferences = getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putString("Mechanic_status", "main");
                    edit.commit();
                    Fragment mechanicRegistrationFragment = new MechanicRegistrationFragment();

                    FragmentTransaction ftmech = getSupportFragmentManager().beginTransaction();
                    ftmech.replace(R.id.container_body, mechanicRegistrationFragment);
                    ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ftmech.addToBackStack(null);
                    ftmech.commit();
                }
                break;
            case 3:

               /* Log.e("Tag ", " Tag : Notificsatioon");
                Fragment mechhelp = new ContactUsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("action", MyTrackConstants._mStringNotification);
                mechhelp.setArguments(bundle);
                FragmentTransaction fthelp = getSupportFragmentManager().beginTransaction();
                fthelp.replace(R.id.container_body, mechhelp);
                fthelp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fthelp.addToBackStack(null);
                fthelp.commit();*/
                Fragment notiFrag = new DisplayNotification();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.container_body, notiFrag);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.addToBackStack(null);
                ft1.commit();

                break;
            case 4:
                if (mStringAction.equals("participant")) {
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

                    Fragment mech2 = new MechanicalSearchFragment();
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

                    FragmentTransaction fthelp2 = getSupportFragmentManager().beginTransaction();
                    fthelp2.replace(R.id.container_body, mech2);
                    fthelp2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp2.addToBackStack(null);
                    fthelp2.commit();

                }
                break;
            case 5:
                if (mStringAction.equals("participant")) {
                    Log.e("Tag ", " Tag : Genuine Check");
                    Fragment mechSearch2 = new GenuineCheckFragment();
                    FragmentTransaction ft7 = getSupportFragmentManager().beginTransaction();
                    ft7.replace(R.id.container_body, mechSearch2);
                    ft7.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft7.addToBackStack(null);
                    ft7.commit();

                } else {

                    Fragment mech2 = new RedeemMainFragment();

                    Bundle bundleRewards = new Bundle();
                    bundleRewards.putString("isDisable", "true");
                    mech2.setArguments(bundleRewards);

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
                break;
            case 6:
                if (mStringAction.equals("participant")) {
                    Log.e("Tag ", " Tag : Help");
                    Fragment mechhelp_ = new HelpTabFragment();
                    FragmentTransaction fthelp_ = getSupportFragmentManager().beginTransaction();
                    fthelp_.replace(R.id.container_body, mechhelp_);
                    fthelp_.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp_.addToBackStack(null);
                    fthelp_.commit();
                } else {
                    Log.e("Tag ", " Tag : Genuine Check");
                    Fragment mechSearch2 = new GenuineCheckFragment();
                    FragmentTransaction ft7 = getSupportFragmentManager().beginTransaction();
                    ft7.replace(R.id.container_body, mechSearch2);
                    ft7.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft7.addToBackStack(null);
                    ft7.commit();

                }

                break;

            case 7:
                if (mStringAction.equals("participant")) {

                    Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();
                } else {
                    Log.e("Tag ", " Tag : Help");
                    Fragment mechhelp_ = new HelpTabFragment();
                    FragmentTransaction fthelp_ = getSupportFragmentManager().beginTransaction();
                    fthelp_.replace(R.id.container_body, mechhelp_);
                    fthelp_.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fthelp_.addToBackStack(null);
                    fthelp_.commit();
                }
                break;

            case 8:

                if (mStringAction.equals("participant")) {


                    CustomYesNoDialog cdd = new CustomYesNoDialog(MainActivity1.this);
                    cdd.show();
                } else {
                    Fragment changepass = new ChangePasswordFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.container_body, changepass);
                    ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft5.addToBackStack(null);
                    ft5.commit();


                }

                break;
            case 9:
                if (mStringAction.equals("participant")) {

                    Fragment versionDetails = new DisplayVersionDetails();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                } else {
                    CustomYesNoDialog cdd = new CustomYesNoDialog(MainActivity1.this);
                    cdd.show();
                }
                break;

            case 10:
                if (mStringAction.equals("participant")) {
                    Fragment versionDetails = new DisplayNotificationId();
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

            case 11:
                if (mStringAction.equals("participant")) {

                } else {

                    Fragment versionDetails = new DisplayNotificationId();
                    FragmentTransaction ft9 = getSupportFragmentManager().beginTransaction();
                    ft9.replace(R.id.container_body, versionDetails);
                    ft9.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft9.addToBackStack(null);
                    ft9.commit();
                }
                break;
            default:
                if (mStringAction.equals("participant")) {
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


    /*class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        // private Context mContext;

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
                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetParticipant(""
                        + params[0][0], ""+params[0][1], ""+params[0][2], ""+params[0][3]);
            } catch (Exception e) {
                // TODO: handle exception
                MainActivity.this.runOnUiThread(new Runnable() {
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

            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       *//* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "Number Not Registered");*//*
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);
                        circularList = new ArrayList<UserModel>();
                        UserModel _news = new UserModel();
                        strid = Integer.parseInt(result.getString("login_id_pk"));
                        _news.setId(Integer.parseInt(result.getString("login_id_pk")));
                        if(result.has("image_path")) {
                            _news.setPicture(result.getString("image_path"));
                        }
                        else {
                            _news.setPicture("");
                        }
                        _news.setUserfname(result.getString("first_name"));
                        _news.setUserlname(result.getString("last_name"));
                        _news.setGender(result.getString("gender"));
                        _news.setNomini(result.getString("nominee_name"));
                        _news.setNominirely(result.getString("nominee_relation"));
                        // _news.setMothername(mothername);
                        _news.setWorkshopname(result.getString("workshop_name"));

                        _news.setState(result.getString("state"));
                        _news.setDistrict(result.getString("district"));
                        _news.setPincode(result.getString("pincode"));
                        _news.setTaluka(result.getString("taluka"));
                        _news.setCity(result.getString("city"));
                        _news.setShopadd(result.getString("workshop_address"));
                        _news.setLandmark(result.getString("landmark"));
                        _news.setMobile_no(result.getString("mobile_no"));

                        _news.setEmail(result.getString("email_id"));
                        _news.setSector(result.getString("sector"));
                        _news.setSpecialization(result.getString("specialization"));
                        _news.setDob(result.getString("dob"));
                        _news.setRegdate(result.getString("record_date"));
                        _news.setToatalsperconpermonth(result.getString("total_consumption"));
                        _news.setSperpartconformmvehicpermonth(result.getString("spare_consumption"));
                        _news.setMmgenuspareconpermonth(result.getString("genuine_consumption"));
                        _news.setTotalvehicalpermonth(result.getString("total_vehicles"));

                        _news.setMahindravehicalpermonth(result.getString("client_vehicles"));
                        _news.setNoofmechanics(result.getString("no_of_mechanics"));
                        _news.setBalance_points(result.getString("points"));
                        _news.setStatus(result.getString("status"));

                        _news.setParticipant_code(result.getString("participant_code"));
                        _news.setParticipant_id_pk(result.getString("participant_id_pk"));
                        _news.setForm_fillup_date(result.getString("form_fillup_date"));

                        circularList.add(_news);


                        final UserTableDatasource ctdUser = new UserTableDatasource(MainActivity.this);
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory(result.getString("login_id_pk"));
                            ctdUser.insertIntoHomework(result, jObj.getJSONArray("participant_claim_history").toString());
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), MainActivity.this, result.getString("login_id_pk"));

                            ctdUser.close();
                        } else {
                            ctdUser.insertIntoHomework(result, "");
                            ctdUser.close();
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }
    }*/

    class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity1.this);
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
                        Toast.makeText(MainActivity1.this,
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

                        final UserTableDatasource ctdUser = new UserTableDatasource(MainActivity1.this);
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory("" + strid);
                            ctdUser.insertClaimHistory(result, jObj.getJSONArray("participant_claim_history").toString(), "" + strid);
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), MainActivity1.this, "" + strid);

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

    private void show_dialog_you_are_not_updated() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity1.this);

        alertDialogBuilder.setTitle(getString(R.string.you_are_not_updated_title));
        alertDialogBuilder.setMessage(getString(R.string.you_are_not_updated_message));
        alertDialogBuilder.setIcon(R.drawable.ic_action_collections_cloud_light);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                OrientationUtils.unlockOrientation(MainActivity1.this);
            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                dialog.cancel();
                OrientationUtils.unlockOrientation(MainActivity1.this);
            }
        });
        alertDialogBuilder.show();
    }


}
