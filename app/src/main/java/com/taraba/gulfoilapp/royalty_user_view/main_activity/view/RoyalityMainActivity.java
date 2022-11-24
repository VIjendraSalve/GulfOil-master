package com.taraba.gulfoilapp.royalty_user_view.main_activity.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.NotificationDeatailFragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.RoyaltyDisplayVersionDetails;
import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.drawerinterface.DrawerCallbacks;
import com.taraba.gulfoilapp.royalty_user_view.campaign_rewards.CampaignRewardsFragment;
import com.taraba.gulfoilapp.royalty_user_view.change_password.NewChangePasswordFragment;
import com.taraba.gulfoilapp.royalty_user_view.custom_dialog.RoyaltyLogoutDialog;
import com.taraba.gulfoilapp.royalty_user_view.dashboard.RoyaltyDashboardFragment;
import com.taraba.gulfoilapp.royalty_user_view.digital_reward.RoyaltyDigitalOrderHistoryFragment;
import com.taraba.gulfoilapp.royalty_user_view.help.RoyaltyHelpTabFragment;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.nav_drawer.RoyaltyNavigationFragment;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.presenter.RoyalityMainPresenter;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.presenter.RoyalityMainPresenterImpl;
import com.taraba.gulfoilapp.royalty_user_view.profile.RoyaltyMyProfileFragment;
import com.taraba.gulfoilapp.royalty_user_view.royal_benefit.RoyaltyBenefitFragment;
import com.taraba.gulfoilapp.royalty_user_view.scheme_letter.RoyaltySchemeLetterFragment;
import com.taraba.gulfoilapp.royalty_user_view.target_meter.RoyaltyTargetMeterCategoryFragment;
import com.taraba.gulfoilapp.royalty_user_view.view_rewards.RewardsFragment;
import com.taraba.gulfoilapp.royalty_user_view.whats_new.RoyaltyWhatsNewFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class RoyalityMainActivity extends AppCompatActivity implements RoyalityMainView, DrawerCallbacks {
    public android.os.Handler mHandler;
    //    public int count=0;
    String mStringAction;
    ArrayList prgmName;
    TextView txtMsg;
    int strid;
    String loginId;
    private RoyalityMainPresenter presenter;
    private Toolbar toolbar;
    private ListView listView;
    private List<UserModel> circularList;
    private CircularListAdapter adapter;
    private RoyaltyNavigationFragment mNavigationNavDrawerFragment;
    private Thread r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_royality_main);
        presenter = new RoyalityMainPresenterImpl(this);
        init();
        setListeners();
        getIntentData();

        findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(RoyalityMainActivity.this);
            }
        });
    }

    @Override
    public void init() {
        toolbar = findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object

        //txtUnreadCount.setText("100");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Grass Root");
        SharedPreferences preferences2 = RoyalityMainActivity.this.getSharedPreferences(
                "signupdetails", MODE_PRIVATE);
        loginId = preferences2.getString("usertrno", "");


        SharedPreferences preference10 = getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String type = preference10.getString("user_type", "");
        mStringAction = type;
        // mStringAction = getIntent().getStringExtra("action");
        SharedPreferences preferences = (RoyalityMainActivity.this).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        mNavigationNavDrawerFragment = (RoyaltyNavigationFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);

        /*SharedPreferences preferen1=getSharedPreferences("Count",Context.MODE_PRIVATE);

        int cnt=preferen1.getInt("new_count", 0);

        if(cnt==0) {
            mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar, mStringAction, new_check);
        }*/

        mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, mStringAction);


        //LOAD FRAGMENT
        replaceFragment(new RoyaltyDashboardFragment());
        /*Fragment dashboard = new RoyaltyDashboardFragment();
        FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
        ftp.replace(R.id.container_body, dashboard);
        ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ftp.addToBackStack(null);
        ftp.commit();*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void getIntentData() {

    }

    @Override
    public void onBackPressed() {
        if (mNavigationNavDrawerFragment.isDrawerOpen()) {
            mNavigationNavDrawerFragment.closeDrawer();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else
                super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
        ftp.replace(R.id.container_body, fragment);
        ftp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ftp.addToBackStack(null);
        ftp.commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "POSITION " + position, Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                replaceFragment(new RoyaltyDashboardFragment());
                break;
            case 1:
                replaceFragment(new RoyaltyMyProfileFragment());
                break;
            case 2:
                replaceFragment(new RoyaltyWhatsNewFragment());
                break;
            case 3:

                Fragment mech2 = new RewardsFragment();

                Bundle bundleRewards = new Bundle();
                bundleRewards.putString("isDisable", "false");
                bundleRewards.putString("path", "d");
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
                replaceFragment(mech2);
                break;
            case 4:

                replaceFragment(new RoyaltySchemeLetterFragment());
                break;
            case 5:

                SharedPreferences pref1 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String loginId2 = pref1.getString("usertrno", "");
                replaceFragment(RoyaltyTargetMeterCategoryFragment.newInstance(loginId2));
                break;
            case 6:
                SharedPreferences preferences2 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id2 = preferences2.getString("usertrno", "");
                Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id2);

                SharedPreferences pref2 = getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit2 = pref2.edit();
                int lid2 = Integer.parseInt("" + login_id2);
                edit2.putInt("Mechanic_trno_to_redeem", lid2);

//                        edit2.putString("Mechanic_status", "rewards");

                edit2.commit();


                Fragment detailsFragment = new RoyaltyDigitalOrderHistoryFragment();
                FragmentTransaction ftmech = getSupportFragmentManager().beginTransaction();
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();
                break;
            case 7:
                SharedPreferences preferences3 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id3 = preferences3.getString("usertrno", "");
                Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id3);

                SharedPreferences pref3 = getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit3 = pref3.edit();
                int lid3 = Integer.parseInt("" + login_id3);
                edit3.putInt("Mechanic_trno_to_redeem", lid3);
                edit3.commit();

                Fragment campaignRewardsFragment = new CampaignRewardsFragment();
                FragmentTransaction ftmechcampaign = getSupportFragmentManager().beginTransaction();
                ftmechcampaign.replace(R.id.container_body, campaignRewardsFragment);
                ftmechcampaign.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmechcampaign.addToBackStack(null);
                ftmechcampaign.commit();
                //replaceFragment(new RoyaltyChangePasswordFragment());
                break;
            case 8:
                SharedPreferences preferences5 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id5 = preferences5.getString("usertrno", "");
                Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id5);

                SharedPreferences pref5 = getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit5 = pref5.edit();
                int lid5 = Integer.parseInt("" + login_id5);
                edit5.putInt("Mechanic_trno_to_redeem", lid5);
                edit5.commit();

                Fragment royaltyBenefit = new RoyaltyBenefitFragment();
                FragmentTransaction ftRoyaltyBenefit = getSupportFragmentManager().beginTransaction();
                ftRoyaltyBenefit.replace(R.id.container_body, royaltyBenefit);
                ftRoyaltyBenefit.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftRoyaltyBenefit.addToBackStack(null);
                ftRoyaltyBenefit.commit();
                //replaceFragment(new RoyaltyHelpTabFragment());
                break;
            case 9:
                replaceFragment(new NewChangePasswordFragment());
                break;
            case 10:
                replaceFragment(new RoyaltyHelpTabFragment());
                break;
            case 11:

                replaceFragment(new NotificationDeatailFragment());
                break;
            case 12:
                replaceFragment(new RoyaltyDisplayVersionDetails());

                break;
            case 13:
                RoyaltyLogoutDialog cdd = new RoyaltyLogoutDialog(RoyalityMainActivity.this);
                cdd.show();
                break;
        }
    }
}
