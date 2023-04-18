package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.royalty_user_view.proceed_order.ProceedDigitalRewardsOTPActivity;
import com.taraba.gulfoilapp.royalty_user_view.proceed_order.UpdateRetailerProfileOTPFragment;
import com.taraba.gulfoilapp.ui.fls.search_retailer.PanCardDetailsFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class CheckKYCActivity extends AppCompatActivity {

    private Button btn_update_kyc, btn_dont_updated_kyc;
    private TextView kyc_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_kycactivity);

        initFunction();
    }

    private void initFunction() {

        btn_update_kyc = findViewById(R.id.btn_update_kyc);
        btn_dont_updated_kyc = findViewById(R.id.btn_dont_updated_kyc);
        kyc_text = findViewById(R.id.kyc_text);

        SharedPreferences preference10 = getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);

        String kyc_status = preference10.getString("kyc_status", "");
        String tds_content = preference10.getString("tds_content", "");
        String alert_content = preference10.getString("alert_content", "");

        kyc_text.setText(tds_content);

        View rootView = getWindow().getDecorView().getRootView();

        btn_update_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntentGoToHome;
                SharedPreferences preference10 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String type = preference10.getString("user_type", "");
                if (type.equals("retailer")) {
                    mIntentGoToHome = new Intent(CheckKYCActivity.this,
                            MainDashboardActivity.class);
                } else {
                    mIntentGoToHome = new Intent(CheckKYCActivity.this,
                            FlsDashboardActivity.class);
                }

                Log.e("Type ", "Type : " + type);
                mIntentGoToHome.putExtra("FromNonKYCStatus", "1");
                mIntentGoToHome.putExtra("action", type);
                mIntentGoToHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntentGoToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntentGoToHome);
                finish();
            }
        });

        btn_dont_updated_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new GulfUnnatiDialog(CheckKYCActivity.this, UserType.SIMPLE)
                            .setTitle(getString(R.string.alert))
                            .hideDialogCloseButton(true)
                            .setDescription(alert_content)
                            .setPosButtonText(getString(R.string.yes_i_agree), dialog -> {
                                dialog.dismiss();
                                Intent intent = new Intent(CheckKYCActivity.this, ProceedOTPForKYCActivity.class);
                                intent.putExtra("orderDetailId", "1");
                                startActivity(intent);
                            })
                            .show();
            }
        });


    }


    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CheckKYCActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Yes, I agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent mIntentGoToHome;
                        SharedPreferences preference10 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String type = preference10.getString("user_type", "");
                        if (type.equals("retailer")) {
                            mIntentGoToHome = new Intent(CheckKYCActivity.this,
                                    MainDashboardActivity.class);
                        } else {
                            mIntentGoToHome = new Intent(CheckKYCActivity.this,
                                    FlsDashboardActivity.class);
                        }

                        Log.e("Type ", "Type : " + type);

                        mIntentGoToHome.putExtra("action", type);
                        mIntentGoToHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mIntentGoToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mIntentGoToHome);
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}