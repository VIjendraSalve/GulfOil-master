package com.taraba.gulfoilapp.ui.fls.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.Constants;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.LoginActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.UCActivity;
import com.taraba.gulfoilapp.adapter.FlsUnnatiDashbaordAdapter;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.adapter.SpacesItemDecoration;
import com.taraba.gulfoilapp.constant.SplashPopUpTags;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.dialog.MilestoneTargetDialog;
import com.taraba.gulfoilapp.model.DashboardDataRequest;
import com.taraba.gulfoilapp.model.DashboardDataResponse;
import com.taraba.gulfoilapp.model.FlsUnnatiDashboardModel;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/* renamed from: com.taraba.gulfoilapp.ui.fls.home.FlsHomeFragment */
public class FlsHomeFragment extends Fragment implements View.OnClickListener, RecyclerViewOnClickListener {
    /* access modifiers changed from: private */
    public String currentBalance = "";
    private FlsUnnatiDashbaordAdapter adapter;
    /* renamed from: cd */
    private ConnectionDetector f363cd = new ConnectionDetector(getActivity());
    private List<FlsUnnatiDashboardModel> dashboardModelList;
    private ImageView ivNotification;
    private ImageView ivProfileImage;
    private DashboardDataResponse.Data.ParticipantDashboard participantDashboard;
    private RecyclerView rvFlsDashboard;
    private TextView tvEmailId;
    private TextView tvEmployeeCode;
    private TextView tvPhoneNo;
    private TextView tvUserName;
    private TextView tvVersionName;
    private SharedPreferences userPref;
    private View vNotificationBadge;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home_fls, viewGroup, false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(false);
        }
        init(inflate);
        showUserInfo();
        setDashboardGrid();
        return inflate;
    }

    public void onResume() {
        super.onResume();
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).callSplashPopUp();
        }
        if (isUnReadNotifications()) {
            this.vNotificationBadge.setVisibility(View.VISIBLE);
        } else {
            this.vNotificationBadge.setVisibility(View.GONE);
        }
    }

    private void showUserInfo() {
        ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) Glide.with((Fragment) this).load(this.userPref.getString("profile_image", "")).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).placeholder((int) R.drawable.ic_default_user_avatar)).into(this.ivProfileImage);
        this.tvUserName.setText(this.userPref.getString("fullname", "Not Available"));
        this.tvEmployeeCode.setText(this.userPref.getString("username", ""));
        this.tvEmailId.setText(this.userPref.getString("email", ""));
        this.tvPhoneNo.setText(this.userPref.getString("mobile", ""));
    }

    private void init(View view) {
        this.userPref = getActivity().getSharedPreferences("signupdetails", 0);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivNotification);
        this.ivNotification = imageView;
        imageView.setOnClickListener(this);
        this.vNotificationBadge = view.findViewById(R.id.vNotificationBadge);
        this.ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        this.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        this.tvEmployeeCode = (TextView) view.findViewById(R.id.tvEmployeeCode);
        this.tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
        this.tvPhoneNo = (TextView) view.findViewById(R.id.tvPhoneNo);
        this.tvVersionName = (TextView) view.findViewById(R.id.tvVersionName);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvFlsDashboard);
        this.rvFlsDashboard = recyclerView;
        recyclerView.addItemDecoration(new SpacesItemDecoration(2, 40, false));
        try {
            String str = requireActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            TextView textView = this.tvVersionName;
            textView.setText("Version: " + str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDashboardGrid() {
        this.dashboardModelList = DataProvider.getFlsDashboardList();
        FlsUnnatiDashbaordAdapter flsUnnatiDashbaordAdapter =
                new FlsUnnatiDashbaordAdapter(getActivity(), this.dashboardModelList);
        this.adapter = flsUnnatiDashbaordAdapter;
        flsUnnatiDashbaordAdapter.setOnClickListener(this);
        this.rvFlsDashboard.setAdapter(this.adapter);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ivNotification) {
            Navigation.findNavController(view).navigate((int) R.id.newNotificationFragmentFLS);
        }
    }

    public void onRecyclerViewItemClick(View view, int i) {
        if (view.getId() == R.id.clAchievements) {
            handleAchievementClick(i);
        }
    }

    private void handleAchievementClick(int i) {
        if (i == 0) {
            Bundle bundle = new Bundle();
            bundle.putString("ComesFrom", "MyRetailer");
            Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerSelectionFragment, bundle);
        } else if (i == 1) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("ComesFrom", "MagicBonanza");
            Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerSelectionFragment, bundle2);
        } else if (i == 2) {
            startActivity(new Intent(getActivity(), UCActivity.class));
        } else if (i == 3) {
            navigateToRewardsFragment();
        }
    }

    private void showMileStoneDialog() {
        new MilestoneTargetDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle("Set Your Title").setPosButtonText(getString(R.string.btn_submit), new MilestoneTargetDialog.OnPositiveClickListener() {
            public final void onClick(MilestoneTargetDialog milestoneTargetDialog, String str) {
                FlsHomeFragment.this.lambda$showMileStoneDialog$0$FlsHomeFragment(milestoneTargetDialog, str);
            }
        }).show();
    }

    public /* synthetic */ void lambda$showMileStoneDialog$0$FlsHomeFragment(MilestoneTargetDialog milestoneTargetDialog, String str) {
        openMileStoneScreen(Integer.parseInt(str), 0, true);
    }

    private void openMileStoneScreen(int i, int i2, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("targetValue", i);
        bundle.putInt("achievement", i2);
        bundle.putBoolean("isPopUpDisplay", z);
        Navigation.findNavController(getView()).navigate((int) R.id.mileStoneFragment, bundle);
    }

    private void comingSoonDialog() {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setDescription("Coming soon...").setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
    }

    private void logout() {
        AppConfig.isSplashPopUpSessionActive = true;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("old_usertrno", "" + sharedPreferences.getString("usertrno", ""));
        edit.putString("username", "");
        edit.putString("userimage", "");
        edit.putString("usertrno", "");
        edit.putString("status", "");
        edit.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private boolean isUnReadNotifications() {
        UserTableDatasource userTableDatasource = new UserTableDatasource(getActivity());
        try {
            userTableDatasource.open();
            return userTableDatasource.isUnReadNotifications(getActivity().getSharedPreferences("signupdetails", 0).getString("uname", ""));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDashboardData() {
        DashboardDataRequest dashboardDataRequest = new DashboardDataRequest(this.userPref.getString("usertrno", ""));
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            Disposable disposable = ServiceBuilder.getRetrofit().create(GulfService.class)
                    .getDashboardData(dashboardDataRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::dashboardDataAPIResponse, this::dashboardDataAPIError);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.str_internet_disconnected), Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void dashboardDataAPIResponse(DashboardDataResponse dashboardDataResponse) {
        if (!ServiceBuilder.isSuccess(dashboardDataResponse.getStatus())) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "" + dashboardDataResponse.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: private */
    public void dashboardDataAPIError(Throwable th) {
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void navigateToRewardsFragment() {
        String string = getActivity().getSharedPreferences("signupdetails", 0).getString("usertrno", "");
        Log.e("Login Id :", "Login Id in mechanic dashboard : " + string);
        SharedPreferences.Editor edit = getActivity().getSharedPreferences("userinfo", 0).edit();
        edit.putInt("Mechanic_trno_to_redeem", Integer.parseInt("" + string));
        edit.putString("Mechanic_status", SplashPopUpTags.REWARDS);
        edit.commit();
        FlsHomeFragmentDirections.ActionFlsHomeFragmentToRewardsFragmentFLS actionFlsHomeFragmentToRewardsFragmentFLS = FlsHomeFragmentDirections.actionFlsHomeFragmentToRewardsFragmentFLS();
        actionFlsHomeFragmentToRewardsFragmentFLS.setIsDisable("false");
        actionFlsHomeFragmentToRewardsFragmentFLS.setPath("d");
        Navigation.findNavController(getView()).navigate((int) R.id.rewardsFragmentFLS);
    }

    /* renamed from: com.taraba.gulfoilapp.ui.fls.home.FlsHomeFragment$getCurrentBal */
    private class getCurrentBal extends AsyncTask<Void, Void, JSONObject> {
        private getCurrentBal() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public JSONObject doInBackground(Void... voidArr) {
            Log.e("insert :", "in do in background of GetProductList");
            try {
                return new UserFunctions().getcurrentbal();
            } catch (Exception unused) {
                FlsHomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(FlsHomeFragment.this.getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(JSONObject jSONObject) {
            super.onPostExecute(jSONObject);
            if (jSONObject != null) {
                try {
                    String string = jSONObject.getString("status");
                    if (!string.equals(Constants.FAILURE)) {
                        if (string.equals("success")) {
                            //String unused = FlsHomeFragment.this.currentBalance = jSONObject.getString("data");
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
