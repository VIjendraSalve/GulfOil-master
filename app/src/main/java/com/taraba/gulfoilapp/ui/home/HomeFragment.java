package com.taraba.gulfoilapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.HelperNew.SharedPref;
import com.taraba.gulfoilapp.LoginActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.AchievementAdapter;
import com.taraba.gulfoilapp.adapter.DashboardRedeemAdapter;
import com.taraba.gulfoilapp.adapter.DividerItemDecorator;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.adapter.SpacesItemDecoration;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.dialog.MilestoneTargetDialog;
import com.taraba.gulfoilapp.model.AchievementModel;
import com.taraba.gulfoilapp.model.DashboardDataRequest;
import com.taraba.gulfoilapp.model.DashboardDataResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.ui.more.MoreFragmentDirections;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements View.OnClickListener, RecyclerViewOnClickListener {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private AchievementAdapter adapter;
    private DashboardRedeemAdapter redeemAdapter;
    private RecyclerView rvAchievement, rvRedeemRewards;
    private ConnectionDetector cd = new ConnectionDetector(getActivity());
    private ImageView ivNotification, ivProfileImage;
    private View vNotificationBadge;
    private TextView tvUserName,
            tvShopName,
            tvAddress,
            tvRetailerId,
            tvUnnatiTier;
    private SharedPreferences userPref;
    private DashboardDataResponse.Data.ParticipantDashboard participantDashboard;
    private List<AchievementModel> achievementList;
    private String currentBalance = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;

        root = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(false);
        }
        init(root);
        showUserInfo();
        getDashboardData();
        new getCurrentBal().execute();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPref.clearPref1(getActivity(), "flsID");
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).callSplashPopUp();
        }

        if (isUnReadNotifications()) {
            vNotificationBadge.setVisibility(View.VISIBLE);
        } else {
            vNotificationBadge.setVisibility(View.GONE);
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_home_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_home_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_home_club;
        else
            return R.layout.fragment_home_royal;
    }

    private void showUserInfo() {
        tvUserName.setText(userPref.getString("fullname", "Not Available"));
        tvShopName.setText("Shop Name: " + userPref.getString("shopname", "Shop Name: Not Available"));
        String address = userPref.getString("shopcity", "") + ", " + userPref.getString("shopstate", "");
        tvAddress.setText("" + address);
        tvRetailerId.setText(userPref.getString("username", "Not Available"));
        tvUnnatiTier.setText(userPref.getString("retailer_type", "Not Available"));
    }

    private void init(View root) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        ivNotification = root.findViewById(R.id.ivNotification);
        ivNotification.setOnClickListener(this);
        vNotificationBadge = root.findViewById(R.id.vNotificationBadge);
        ivProfileImage = root.findViewById(R.id.ivProfileImage);
        tvUserName = root.findViewById(R.id.tvUserName);
        tvShopName = root.findViewById(R.id.tvShopName);
        tvAddress = root.findViewById(R.id.tvAddress);
        tvRetailerId = root.findViewById(R.id.tvRetailerId);
        tvUnnatiTier = root.findViewById(R.id.tvUnnatiTier);
        root.findViewById(R.id.tvRedeemRewardsViewMore).setOnClickListener(this);
        rvAchievement = root.findViewById(R.id.rvAchievement);
        rvRedeemRewards = root.findViewById(R.id.rvRedeemRewards);
        DividerItemDecorator itemDecorator = new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.divider_horizontal), LinearLayout.HORIZONTAL);
        rvRedeemRewards.addItemDecoration(itemDecorator);
        rvAchievement.addItemDecoration(new SpacesItemDecoration(2, 40, false));
        rvRedeemRewards.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        rvRedeemRewards.setOnClickListener(this);

    }

    private void getDashboardData() {
        DashboardDataRequest request = new DashboardDataRequest(userPref.getString("usertrno", ""));
        if (cd.isNetworkAvailable(getActivity())) {
            ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getDashboardData(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::dashboardDataAPIResponse, this::dashboardDataAPIError);

        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.str_internet_disconnected), Toast.LENGTH_LONG).show();
        }
    }


    private void dashboardDataAPIResponse(DashboardDataResponse dashboardDataResponse) {
        if (ServiceBuilder.isSuccess(dashboardDataResponse.getStatus())) {
            setDashboardDataToUI(dashboardDataResponse.getData());
        } else {
            Toast.makeText(getActivity(), "" + dashboardDataResponse.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDashboardDataToUI(DashboardDataResponse.Data data) {
        if (data.isIs_login()) {
            Log.d(TAG, "getTrending_rewards: "+data.getParticipant_dashboard().getTrending_rewards().size());
            //saved profile img path in preference if updated by user after login
            userPref.edit().putString("profile_image", data.getProfile_image()).apply();
            userPref.edit().putString("full_name", data.getFull_name()).apply();
            try {
                if (!TextUtils.isEmpty(data.getProfile_image()))
                    Glide.with(this).load(data.getProfile_image()).placeholder(R.drawable.ic_default_user_avatar).into(ivProfileImage);
            } catch (Exception e) {
                Log.e(TAG, "" + e.getMessage());
            }
            tvUserName.setText(data.getFull_name());
            setAchievementAdapter(data.getParticipant_dashboard());
            setRedeemAdapter(data.getParticipant_dashboard().getTrending_rewards());
        } else {
            logout();
        }
    }

    private void dashboardDataAPIError(Throwable throwable) {
        try {
            Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
    }

    private void setRedeemAdapter(List<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward> rewardList) {
        // if (redeemAdapter != null) {
        redeemAdapter = new DashboardRedeemAdapter(getActivity(), rewardList);
        redeemAdapter.setOnClickListener(this);
//        } else {
//            redeemAdapter.setList(rewardList);
//        }
        rvRedeemRewards.setAdapter(redeemAdapter);

        try {
            String jsonString = new Gson().toJson(rewardList, new TypeToken<ArrayList<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward>>() {
            }.getType());
            JSONArray jsonArray = new JSONArray(jsonString);
            saveRewardsIntoDB(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveRewardsIntoDB(JSONArray jsonArray) {
        try {
            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //CLEAR ALL PRODUCT FROM TABLE
            ctdUser.deleteAllProduct();

            for (int i = 0; i < jsonArray.length(); i++) {
                ctdUser.insertIntoProduct(jsonArray.getJSONObject(i));
            }
            ctdUser.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAchievementAdapter(DashboardDataResponse.Data.ParticipantDashboard achivevementData) {
        participantDashboard = achivevementData;
        achievementList = getLocalList(
                achivevementData.getPoints(),
                "" + achivevementData.getMagic_bonanza(),
                achivevementData.getMagic_bonanza_title(),
                "" + achivevementData.getMilestone().getAchievement(),
                achivevementData.getRank());
        adapter = new AchievementAdapter(getActivity(), achievementList);
        adapter.setOnClickListener(this);
        rvAchievement.setAdapter(adapter);
    }


    private List<AchievementModel> getLocalList(String points, String magicBonanza, String magicBonanzaDesc, String milestone, String leaderBoard) {
        UserType userType = new GulfOilUtils().getUserType();
        int point_bg = 0;
        int magic_bonanza_bg = 0;
        int milestone_bg = 0;
        int leader_board_bg = 0;
        if (userType == UserType.ROYAL) {
            point_bg = R.drawable.royal_point_bg;
            magic_bonanza_bg = R.drawable.royal_magic_bonanza_bg;
            milestone_bg = R.drawable.royal_milestone_bg;
            leader_board_bg = R.drawable.royal_leaderboard_bg;
        } else if (userType == UserType.ELITE) {
            point_bg = R.drawable.elite_point_bg;
            magic_bonanza_bg = R.drawable.elite_magic_bonanza_bg;
            milestone_bg = R.drawable.elite_milestone_bg;
            leader_board_bg = R.drawable.elite_leaderboard_bg;
        } else if (userType == UserType.CLUB) {
            point_bg = R.drawable.club_point_bg;
            magic_bonanza_bg = R.drawable.club_magic_bonanza_bg;
            milestone_bg = R.drawable.club_milestone_bg;
            leader_board_bg = R.drawable.club_leaderboard_bg;
        } else {
            point_bg = R.layout.fragment_home_royal;
        }

        AchievementModel achievementPoints = new AchievementModel();
        achievementPoints.setTitle("Points");
        achievementPoints.setImg_res(point_bg);
        achievementPoints.setValue(points);
        achievementPoints.setValueDescription("Total Points Balance");
        achievementPoints.setDescription("Your Points & Bonuses");

        AchievementModel achievementMagicBonanza = new AchievementModel();
        achievementMagicBonanza.setTitle("Magic Bonanza");
        achievementMagicBonanza.setImg_res(magic_bonanza_bg);
        achievementMagicBonanza.setValue(magicBonanza + "L");
        achievementMagicBonanza.setValueDescription(magicBonanzaDesc);
        achievementMagicBonanza.setDescription("Quarterly target & achievement");

        AchievementModel achievementMileStone = new AchievementModel();
        achievementMileStone.setTitle("Milestone");
        achievementMileStone.setImg_res(milestone_bg);
        achievementMileStone.setValue(milestone + "%");
        achievementMileStone.setValueDescription("Achieved");
        achievementMileStone.setDescription("Unlock your next milestone now!");

        AchievementModel achievementLeaderBoard = new AchievementModel();
        achievementLeaderBoard.setTitle("Leaderboard");
        achievementLeaderBoard.setImg_res(leader_board_bg);
        String newValue = "";
        if (TextUtils.isEmpty(leaderBoard)) {
            newValue = "_";
        } else if (leaderBoard.trim().equals("0")) {
            newValue = "_";
        } else {
            newValue = leaderBoard;
        }
        achievementLeaderBoard.setValue(newValue);
        achievementLeaderBoard.setValueDescription("Rank");
        achievementLeaderBoard.setDescription("Your summit awaits you");
        List<AchievementModel> list = new ArrayList<>();
        list.add(achievementPoints);
        list.add(achievementMagicBonanza);
        list.add(achievementMileStone);
        list.add(achievementLeaderBoard);
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRedeemRewardsViewMore:
                navigateToRewardsFragment(v);
                break;
            case R.id.ivNotification:
                Navigation.findNavController(v).navigate(R.id.newNotificationFragment);
                break;
        }
    }

    private void navigateToRewardsFragment(View v) {
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String login_id = preferences1.getString("usertrno", "");
        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

        SharedPreferences pref = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = pref.edit();
        int lid = Integer.parseInt("" + login_id);
        edit1.putInt("Mechanic_trno_to_redeem", lid);
        edit1.putString("Mechanic_status", "rewards");
        edit1.commit();
        MoreFragmentDirections.ActionNavigationMoreToRewardsFragment rewardsAction = MoreFragmentDirections.actionNavigationMoreToRewardsFragment();
        rewardsAction.setIsDisable("false");
        rewardsAction.setPath("d");
        Navigation.findNavController(v).navigate(R.id.rewardsFragment);
    }


    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.btnRedeem:
                navigateToConfirmOrderDetailsFragment(v, position);
                break;
            case R.id.clAchievements:
                handleAchievementClick(position);
                break;
        }
    }

    private void navigateToConfirmOrderDetailsFragment(View view, int position) {
        String productCode = redeemAdapter.getList().get(position).getProduct_code();
        HomeFragmentDirections.ActionNavigationHomeToOrderConfirmationDetailsFragment actionConfirmOrderDetails = HomeFragmentDirections.actionNavigationHomeToOrderConfirmationDetailsFragment();
        actionConfirmOrderDetails.setIsDisable("false");
        actionConfirmOrderDetails.setProductCode("" + productCode);
        actionConfirmOrderDetails.setCurrentRedeemPoints(currentBalance);
        Navigation.findNavController(view).navigate(actionConfirmOrderDetails);
    }

    private void handleAchievementClick(int position) {
        switch (position) {
            case 0:
                Navigation.findNavController(getView()).navigate(R.id.myPointsFragment);
                break;
            case 1:
                Navigation.findNavController(getView()).navigate(R.id.magicBonanzaListFragment);
                break;
            case 2:
                if (participantDashboard != null && participantDashboard.getMilestone().isPopup_display())
                    showMileStoneDialog();
                else
                    openMileStoneScreen(participantDashboard.getMilestone().getTarget_volume(), participantDashboard.getMilestone().getAchievement(), false);
                break;
            case 3:
                Navigation.findNavController(getView()).navigate(R.id.leaderBoardFragment);
                break;
        }
    }

    private void showMileStoneDialog() {
        new MilestoneTargetDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle("Set Your Title")
                .setPosButtonText(getString(R.string.btn_submit), (dialog, selectedTarget) -> {
                    openMileStoneScreen(Integer.parseInt(selectedTarget), 0, true);
                }).show();


    }

    private void openMileStoneScreen(int target_volume, int achievement, boolean isPopUpDisplay) {
        Bundle bundle = new Bundle();
        bundle.putInt("targetValue", target_volume);
        bundle.putInt("achievement", achievement);
        bundle.putBoolean("isPopUpDisplay", isPopUpDisplay);
        Navigation.findNavController(getView()).navigate(R.id.mileStoneFragment, bundle);
    }

    private void comingSoonDialog() {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setDescription("Coming soon...")
                .setPosButtonText(getString(R.string.str_ok), null)
                .show();
    }


    //Update By Almas Sayyed
    private class getCurrentBal extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Log.e("insert :", "in do in background of GetProductList");
            JSONObject jObj = null;
            try {

                jObj = new UserFunctions().getcurrentbal();

            } catch (Exception e) {
                // TODO: handle exception

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                try {
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("failure")) {

                    } else if (mStringStatus.equals("success")) {
                        String str = jsonObject.getString("data");
                        currentBalance = str;
//                        txt_currentbal.setText(str);
                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
            /**/
        }

    }

    private void logout() {
        AppConfig.isSplashPopUpSessionActive = true;
        SharedPreferences preferences = getContext().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
        //	Log.e("user id ", "User Id : "+""+preferences.getString("usertrno", ""));
        edit.putString("username", "");
        edit.putString("userimage", "");
        edit.putString("usertrno", "");
        edit.putString("status", "");
        //	edit.putString("logout", "Logout");
        edit.commit();
        //c.finish();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private boolean isUnReadNotifications() {
        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        boolean result = false;
        try {
            ctdUser.open();
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String uname = preferences1.getString("uname", "");
            result = ctdUser.isUnReadNotifications(uname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


}