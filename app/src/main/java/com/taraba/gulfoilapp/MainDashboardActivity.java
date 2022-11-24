package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.databinding.ActivityRoyalDashboardBinding;
import com.taraba.gulfoilapp.royalty_user_view.view_rewards.RewardsFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.view.splash_pop_up.SplashPopUpActivity;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainDashboardActivity extends AppCompatActivity implements View.OnClickListener, ViewStub.OnInflateListener {
    private static final int REQUEST_CODE_SPLASH_POPUP = 1001;
    private ActivityRoyalDashboardBinding binding;
    private BottomNavigationView navView;
    private static final String TAG = "MainDashboardActivity";
    private static final String TAG_NEW = "Vij";
    private ViewStub toolbar;
    private ImageView ivBack;
    private TextView tvToolbarTitle, tvToolbarAction;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoyalDashboardBinding.inflate(getLayoutInflater());
        init();
        setBottomNavUserWise();
        getFCMToken();
//        saveDummyNotifications();
    }

    private void init() {
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnInflateListener(this);
        toolbar.setVisibility(View.GONE);


        navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        //navView.inflateMenu(R.menu.bottom_nav_menu);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_more, R.id.navigation_account)
//                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SplashPopUpActivity.setSplashPopUpCallback(tag_action -> {
            callSplashPopUpAction(tag_action);
        });
    }

    private void setBottomNavUserWise() {
        navView.getMenu().clear();
        UserType userType = new GulfOilUtils().getUserType();

        Log.d(TAG_NEW, "userType: "+userType);
        Log.d(TAG_NEW, "UserType.ROYAL: "+UserType.ROYAL);

        if (userType == UserType.ROYAL) {
            navView.inflateMenu(R.menu.royal_bottom_nav_menu);
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        } else if (userType == UserType.ELITE) {
            navView.inflateMenu(R.menu.elite_bottom_nav_menu);
            toolbar.setLayoutResource(R.layout.tool_bar_elite);
            toolbar.inflate();
        } else if (userType == UserType.CLUB) {
            navView.inflateMenu(R.menu.club_bottom_nav_menu);
            toolbar.setLayoutResource(R.layout.tool_bar_club);
            toolbar.inflate();
        } else {
            navView.inflateMenu(R.menu.royal_bottom_nav_menu);
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        }
    }

    public void hideShowView(boolean hide) {
        navView.setVisibility(hide ? View.GONE : View.VISIBLE);
        toolbar.setVisibility(hide ? View.VISIBLE : View.GONE);
        tvToolbarAction.setVisibility(View.GONE);
    }

    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    public void setToolbarActionTitle(String title, boolean hide) {
        tvToolbarAction.setText(title);
        tvToolbarAction.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    public void callSplashPopUp() {
        if (AppConfig.isSplashPopUpSessionActive)
            callSplashPopUpAPI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                navController.popBackStack();
                break;
            case R.id.tvToolbarAction:
                switch (navController.getCurrentDestination().getId()) {
                    case R.id.rewardsFragment:
                        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        RewardsFragment rewardsFragment = (RewardsFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                        rewardsFragment.showOrderHistory();
                        break;

                }
                break;
        }
    }

    @Override
    public void onInflate(ViewStub stub, View inflated) {
        inflated.findViewById(R.id.ivBack).setOnClickListener(this);
        tvToolbarAction = inflated.findViewById(R.id.tvToolbarAction);
        tvToolbarAction.setOnClickListener(this);
        tvToolbarTitle = inflated.findViewById(R.id.tvToolbarTitle);
    }

    private void callSplashPopUpAPI() {

        if (NetworkUtils.isNetworkAvailable(this)) {
            SharedPreferences preferences1 = this.getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String login_id = preferences1.getString("usertrno", "");
            new CallSplashPopUpAPI().execute(login_id);
        }

    }

    private void showSplashPopUpDialogs(List<SplashPopUpDetails> splashPopUpDetailsList) {

        startActivityForResult(
                new Intent(this, SplashPopUpActivity.class)
                        .putParcelableArrayListExtra("splash_pop_up_list", (ArrayList<? extends Parcelable>) splashPopUpDetailsList)

                , REQUEST_CODE_SPLASH_POPUP);
    }

    class CallSplashPopUpAPI extends AsyncTask<String, Void, JSONObject> {

        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "CallSplashPopUpAPI - onPreExecute() called");
            // super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Log.d(TAG, "CallSplashPopUpAPI - doInBackground() called with: params = [" + params + "]");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().GetSplashPopUpDetails(params[0]);
                Log.e("", "CallSplashPopUpAPI - json:" + jObj);

            } catch (Exception e) {
                // TODO: handle exception
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainDashboardActivity.this,
                                "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            Log.e("insert :", "in post execute of proceed order" + jObj);
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        if (jObj.getBoolean("display_popup")) {
                            JSONArray dataList = jObj.getJSONArray("data");
                            List<SplashPopUpDetails> splashPopUpDetailsList = new Gson().fromJson(dataList.toString(), new TypeToken<List<SplashPopUpDetails>>() {
                            }.getType());
                            if (splashPopUpDetailsList != null && splashPopUpDetailsList.size() > 0)
                                showSplashPopUpDialogs(splashPopUpDetailsList);
                        }

                    } else {
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (navController.getCurrentDestination().getId()) {
            case R.id.participantProfileFragment:
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                ParticipantProfileFragment participantProfileFragment = (ParticipantProfileFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                participantProfileFragment.onActivityResult(requestCode, resultCode, data);
                break;

        }
    }

    private void callSplashPopUpAction(String tag_action) {
        switch (tag_action) {
            case "my_profile":
                navController.navigate(R.id.participantProfileFragment);
                break;
            case "points":
                navController.navigate(R.id.myPointsFragment);
                break;
            case "digital_rewards":
                startActivity(new Intent(this, YourDigitalRewardsActivity.class));
                break;
            case "help":
                navController.navigate(R.id.helpFragment);
                break;
            case "rewards":
                navController.navigate(R.id.rewardsFragment);
                break;
            case "whats_new":
                startActivity(new Intent(this, UCActivity.class));
                break;
            case "scheme_letter":
                startActivity(new Intent(this, UCActivity.class).putExtra("tab", "scheme_letter"));
                break;
            case "brochure":
                startActivity(new Intent(this, UCActivity.class).putExtra("tab", "brochure"));
                break;
            case "pending_orders":
                navController.navigate(R.id.orderHistoryFragment);
                break;
            case "milestone":
                startActivity(new Intent(this, YourDigitalRewardsActivity.class).putExtra("tab", "milestone"));
                break;
            case "campaign":
                startActivity(new Intent(this, YourDigitalRewardsActivity.class).putExtra("tab", "campaign"));
                break;
            case "newsletter":
                startActivity(new Intent(this, KnowledgeCornerActivity.class).putExtra("tab", "newsletter"));
                break;
            case "webinar":
                startActivity(new Intent(this, KnowledgeCornerActivity.class));
                break;
            case "notification":
                navController.navigate(R.id.newNotificationFragment);
                break;
            case "leaderboard":
                break;
            case "points_calculator":
                navController.navigate(R.id.pointCalculatorWebViewFragment);
                break;
        }
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = "FCM TOKEN: " + token;
                    Log.d(TAG, msg);
                    //Toast.makeText(MainDashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDummyNotifications() {
        final UserTableDatasource ctdUser = new UserTableDatasource(this);

        try {
            ctdUser.open();
            SharedPreferences preferences1 = getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String uname = preferences1.getString("uname", "");
            if (ctdUser.getNewNotificationByUserId(uname).size() > 0)
                return;

            for (int i = 1; i <= 5; i++) {
                String type = "text";
                String title = "Congratulation! You have achieved your magic no." + i;
                String description = "Test Descritpion " + i;
                String image = "";
                String channel_type = "Alert";
                ctdUser.insertIntoNotification(type, uname, title,
                        description,
                        image,
                        channel_type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}