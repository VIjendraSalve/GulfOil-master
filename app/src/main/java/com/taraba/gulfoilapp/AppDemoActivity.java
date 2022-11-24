package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.taraba.gulfoilapp.util.AppDemoConstant;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class AppDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_demo);

        findViewById(R.id.btnAppDemoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(AppDemoActivity.this);
            }
        });

        findViewById(R.id.btn_English).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkENGLISH)));

            }
        });
        findViewById(R.id.btn_Hindi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkHINDI)));

            }
        });
        findViewById(R.id.btn_Tamil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkTAMIL)));

            }
        });
        findViewById(R.id.btn_Telgu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkTELGU)));

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
