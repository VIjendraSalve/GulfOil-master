package com.taraba.gulfoilapp.view.splash_pop_up;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.BuildConfig;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;
import com.taraba.gulfoilapp.view.splash_pop_up.adapter.SplashPopUpAdapter;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import java.util.List;

public class SplashPopUpActivity extends AppCompatActivity implements SplashPopUpAdapter.ClickListener {
    private static final String TAG = "SplashPopUpActivity";
    private ViewPager vpSplashPopUp;
    private SplashPopUpAdapter adapter;
    private Integer[] colors;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private List<SplashPopUpDetails> splashPopUpDetailsList;
    private static SplashPopUpCallback mSplashPopUpCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pop_up);
        init();
    }


    private void init() {
        splashPopUpDetailsList = getIntent().getParcelableArrayListExtra("splash_pop_up_list");
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "init: " + splashPopUpDetailsList.toString());
        }
//        splashPopUpDetailsList = getDummyList();
        adapter = new SplashPopUpAdapter(this, splashPopUpDetailsList);
        adapter.setmClickListener(this);
        vpSplashPopUp = findViewById(R.id.vp_splash_pop_up);
        vpSplashPopUp.setAdapter(adapter);
        vpSplashPopUp.setPadding(100, 0, 100, 0);
    }

    public static void setSplashPopUpCallback(SplashPopUpCallback splashPopUpCallback) {
        mSplashPopUpCallback = splashPopUpCallback;
    }

    @Override
    public void showImageFullSize(SplashPopUpDetails splashPopUpDetails) {
        startActivity(new Intent(this, FullScreenImageActivity.class)
                .putExtra("IMG_URL", splashPopUpDetails.getPopupImage())
                .putExtra("Title", splashPopUpDetails.getTitle()));
    }

    @Override
    public void closeDialog() {
        AppConfig.isSplashPopUpSessionActive = false;
        finish();
    }

    @Override
    public void goToAction(View view, String actionName) {
        if (mSplashPopUpCallback != null) {
            finish();
            mSplashPopUpCallback.tagActionCallback(actionName);
        }
    }

    public interface SplashPopUpCallback {
        void tagActionCallback(String tag_action);
    }
}
