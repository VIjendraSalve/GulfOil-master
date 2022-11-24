package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Created by Mansi on 6/21/2016.
 */
public class OtpActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object

        //txtUnreadCount.setText("100");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Grass Root");

        Bundle b1 = getIntent().getExtras();
        String product_code = b1.getString(MyTrackConstants._mStringProductCode);
        String multiQty = b1.getString("multi_qty");

        if (b1 != null) {

            Fragment detailsFragment = new ProceedOrderFragment();
            Bundle b = new Bundle();
            b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
            b.putString("multi_qty", "" + multiQty);
            detailsFragment.setArguments(b);
            FragmentTransaction ftmech = getSupportFragmentManager().beginTransaction();
            ftmech.replace(R.id.container_body, detailsFragment);
            ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftmech.addToBackStack(null);
            ftmech.commit();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences1 = getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mStringtype = preferences1.getString("user_type", "");

        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("isOtp", "otp");
        Log.e("", "isOTP:");
        i.putExtra("action", "otp");
        startActivity(i);
        finish();
    }
}