package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.constant.SplashPopUpTags;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.databinding.ActivityFlsDashboardBinding;
import com.taraba.gulfoilapp.dialog.GulfSurveyWelcomeDialog;
import com.taraba.gulfoilapp.model.AppSurveyRequest;
import com.taraba.gulfoilapp.model.AppSurveyResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.royalty_user_view.view_rewards.RewardsFragment;
import com.taraba.gulfoilapp.ui.fls.account.FlsAccountFragment;
import com.taraba.gulfoilapp.ui.fls.search_retailer.PanCardDetailsFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.view.splash_pop_up.SplashPopUpActivity;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FlsDashboardActivity extends AppCompatActivity implements View.OnClickListener, ViewStub.OnInflateListener {
    private static final int REQUEST_CODE_SPLASH_POPUP = 1001;
    private static final String TAG = "MainDashboardActivity";
    private ActivityFlsDashboardBinding binding;
    private ImageView ivBack;
    private NavController navController;
    private BottomNavigationView navView;
    private ViewStub toolbar;
    private TextView tvToolbarAction;
    private TextView tvToolbarTitle;
    private Disposable disposable;

    /* access modifiers changed from: private */
    public void appSurveyError(Throwable th) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = ActivityFlsDashboardBinding.inflate(getLayoutInflater());
        init();
        getFCMToken();
    }

    private void init() {
        setContentView((View) this.binding.getRoot());
        ViewStub viewStub = (ViewStub) findViewById(R.id.toolbar);
        this.toolbar = viewStub;
        viewStub.setOnInflateListener(this);
        this.toolbar.inflate();
        this.toolbar.setVisibility(View.GONE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        this.navView = bottomNavigationView;
        bottomNavigationView.setItemIconTintList((ColorStateList) null);
        NavController findNavController = Navigation.findNavController(this, R.id.nav_host_fragment_fls);
        this.navController = findNavController;
        NavigationUI.setupWithNavController(this.navView, findNavController);
        SplashPopUpActivity.setSplashPopUpCallback(new SplashPopUpActivity.SplashPopUpCallback() {
            public final void tagActionCallback(String str) {
                FlsDashboardActivity.this.lambda$init$0$FlsDashboardActivity(str);
            }
        });
    }

    public void hideShowView(boolean z) {
        int i = View.VISIBLE;
        this.navView.setVisibility(z ? View.GONE : View.VISIBLE);
        ViewStub viewStub = this.toolbar;
        if (!z) {
            i = View.GONE;
        }
        viewStub.setVisibility(i);
        this.tvToolbarAction.setVisibility(View.GONE);
    }

    public void setToolbarTitle(String str) {
        this.tvToolbarTitle.setText(str);
    }

    public void setToolbarActionTitle(String str, boolean z) {
        this.tvToolbarAction.setText(str);
        this.tvToolbarAction.setVisibility(z ? View.GONE : View.VISIBLE);
    }

    public void callSplashPopUp() {
        if (AppConfig.isAppSurveySessionActive) {
            callAppSurveyAPI();
        } else if (AppConfig.isSplashPopUpSessionActive) {
            callSplashPopUpAPI();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivBack) {
            this.navController.popBackStack();
        } else if (id == R.id.tvToolbarAction && this.navController.getCurrentDestination().getId() == R.id.rewardsFragment) {
            ((RewardsFragment) ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getChildFragmentManager().getFragments().get(0)).showOrderHistory();
        }
    }

    public void onInflate(ViewStub viewStub, View view) {
        view.findViewById(R.id.ivBack).setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(R.id.tvToolbarAction);
        this.tvToolbarAction = textView;
        textView.setOnClickListener(this);
        this.tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
    }

    private void callSplashPopUpAPI() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            String string = getSharedPreferences("signupdetails", 0).getString("usertrno", "");
            new CallSplashPopUpAPI().execute(new String[]{string});
        }
    }

    /* access modifiers changed from: private */
    public void showSplashPopUpDialogs(List<SplashPopUpDetails> list) {
        startActivityForResult(new Intent(this, SplashPopUpActivity.class).putParcelableArrayListExtra("splash_pop_up_list", (ArrayList) list), 1001);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.navController.getCurrentDestination().getId() == R.id.flsAccountFragment) {
            ((FlsAccountFragment) ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_fls)).getChildFragmentManager().getFragments().get(0)).onActivityResult(i, i2, intent);
        }else if (this.navController.getCurrentDestination().getId() == R.id.fragment_pan_card_details) {
            ((PanCardDetailsFragment) ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_fls)).getChildFragmentManager().getFragments().get(0)).onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: callSplashPopUpAction */
    public void lambda$init$0$FlsDashboardActivity(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -151398512:
                if (str.equals(SplashPopUpTags.WHATS_NEW)) {
                    c = 0;
                    break;
                }
                break;
            case 101142:
                if (str.equals(SplashPopUpTags.FAQ)) {
                    c = 1;
                    break;
                }
                break;
            case 139877149:
                if (str.equals(SplashPopUpTags.CONTACT_US)) {
                    c = 2;
                    break;
                }
                break;
            case 365404196:
                if (str.equals("brochure")) {
                    c = 3;
                    break;
                }
                break;
            case 595233003:
                if (str.equals(SplashPopUpTags.NOTIFICATION)) {
                    c = 4;
                    break;
                }
                break;
            case 798761979:
                if (str.equals(SplashPopUpTags.TERMS_AND_CONDITION)) {
                    c = 5;
                    break;
                }
                break;
            case 1100650276:
                if (str.equals(SplashPopUpTags.REWARDS)) {
                    c = 6;
                    break;
                }
                break;
            case 2028746752:
                if (str.equals(SplashPopUpTags.SCHEME_LETTER)) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                startActivity(new Intent(this, UCActivity.class));
                return;
            case 1:
                startActivity(new Intent(this, HelpActivity.class).putExtra("tab", SplashPopUpTags.FAQ));
                return;
            case 2:
                startActivity(new Intent(this, HelpActivity.class));
                return;
            case 3:
                startActivity(new Intent(this, UCActivity.class).putExtra("tab", "brochure"));
                return;
            case 4:
                this.navController.navigate((int) R.id.newNotificationFragmentFLS);
                return;
            case 5:
                startActivity(new Intent(this, HelpActivity.class).putExtra("tab", SplashPopUpTags.TERMS_AND_CONDITION));
                return;
            case 6:
                this.navController.navigate((int) R.id.rewardsFragmentFLS);
                return;
            case 7:
                startActivity(new Intent(this, UCActivity.class).putExtra("tab", SplashPopUpTags.SCHEME_LETTER));
                return;
            default:
                AppConfig.isSplashPopUpSessionActive = false;
                return;
        }
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            Log.d(TAG, "FCM TOKEN: " + ((String) task.getResult()));
        });
    }

    private void saveDummyNotifications() {
        UserTableDatasource userTableDatasource = new UserTableDatasource(this);
        try {
            userTableDatasource.open();
            String string = getSharedPreferences("signupdetails", 0).getString("uname", "");
            if (userTableDatasource.getNewNotificationByUserId(string).size() <= 0) {
                for (int i = 1; i <= 5; i++) {
                    userTableDatasource.insertIntoNotification(Database.TEXT_COL, string, "Congratulation! You have achieved your magic no." + i, "Test Descritpion " + i, "", "Alert");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void callAppSurveyAPI() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            String string = getSharedPreferences("signupdetails", 0).getString("usertrno", "");
            AppSurveyRequest appSurveyRequest = new AppSurveyRequest();
            appSurveyRequest.setLoginId(string);
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getAppSurvey(appSurveyRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::appSurveyResponse, this::appSurveyError);
        }
    }

    /* access modifiers changed from: private */
    public void appSurveyResponse(AppSurveyResponse appSurveyResponse) {
        if (appSurveyResponse.getStatus().equals("success")) {
            new GulfSurveyWelcomeDialog(this, new GulfOilUtils().getUserType(), appSurveyResponse).show();
        } else if (AppConfig.isSplashPopUpSessionActive) {
            callSplashPopUpAPI();
        }
    }

    class CallSplashPopUpAPI extends AsyncTask<String, Void, JSONObject> {
        private Context mContext;

        CallSplashPopUpAPI() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Log.d(FlsDashboardActivity.TAG, "CallSplashPopUpAPI - onPreExecute() called");
        }

        /* access modifiers changed from: protected */
        public JSONObject doInBackground(String... strArr) {
            Log.d(FlsDashboardActivity.TAG, "CallSplashPopUpAPI - doInBackground() called with: params = [" + strArr + "]");
            JSONObject jSONObject = null;
            try {
                jSONObject = new UserFunctions().GetSplashPopUpDetails(strArr[0]);
                Log.e("", "CallSplashPopUpAPI - json:" + jSONObject);
                return jSONObject;
            } catch (Exception unused) {
                FlsDashboardActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(FlsDashboardActivity.this, "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
                return jSONObject;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(JSONObject jSONObject) {
            super.onPostExecute(jSONObject);
            Log.e("insert :", "in post execute of proceed order" + jSONObject);
            if (jSONObject != null) {
                try {
                    if (jSONObject.getString("status").equals("success") && jSONObject.getBoolean("display_popup")) {
                        List list = (List) new Gson().fromJson(jSONObject.getJSONArray(Constants.ScionAnalytics.MessageType.DATA_MESSAGE).toString(), new TypeToken<List<SplashPopUpDetails>>() {
                        }.getType());
                        if (list != null && list.size() > 0) {
                            FlsDashboardActivity.this.showSplashPopUpDialogs(list);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
