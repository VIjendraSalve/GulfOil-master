package com.taraba.gulfoilapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.KYCOTPRequest;
import com.taraba.gulfoilapp.model.SubmitKYCOTPRequest;
import com.taraba.gulfoilapp.model.SubmitYDROTPRequest;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;
import com.taraba.gulfoilapp.model.YDROTPRequest;
import com.taraba.gulfoilapp.model.YDROTPResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mansi on 3/15/16.
 * Modified by Mansi
 */
public class ProceedOTPForKYCActivity extends AppCompatActivity implements ViewStub.OnInflateListener, View.OnClickListener {

    TextView txt_otp_desc, tv_resend_otp;
    Button btn_submit;
    String otp;
    //    EditText et_otp;
    private PinView pinViewOTP;
    private Disposable disposable;
    private String order_id, loginId;
    private ProgressDialog progressDialog;
    private ViewStub toolbar;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());

        //order_id = getIntent().getStringExtra("orderDetailId");


        SharedPreferences preferences1 = getSharedPreferences(
                "signupdetails", MODE_PRIVATE);
        loginId = preferences1.getString("usertrno", "");

        initComp();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_kyc_status_otp));
        final ConnectionDetector cd = new ConnectionDetector(
                this);
        if (NetworkUtils.isNetworkAvailable(this)) {
            callSendNonKycOTPAPI("");
        } else {
            Toast.makeText(this, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                otp = et_otp.getText().toString();
                otp = pinViewOTP.getText().toString();

                if (otp.equals("") || otp == null) {

                    new GulfUnnatiDialog(ProceedOTPForKYCActivity.this, new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription("Please Enter OTP")
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();

                } else {

                    if (NetworkUtils.isNetworkAvailable(ProceedOTPForKYCActivity.this)) {
                        submitKYCOTPAPI();

                    } else {
                        Toast.makeText(ProceedOTPForKYCActivity.this, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void initComp() {
        txt_otp_desc = findViewById(R.id.txt_desc);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnInflateListener(this);
        toolbar.setVisibility(View.GONE);

        tv_resend_otp.setOnClickListener(v -> callSendNonKycOTPAPI("resend"));

//        et_otp = findViewById(R.id.edt_otp);
        pinViewOTP = findViewById(R.id.pinViewOTP);

        btn_submit = findViewById(R.id.btn_submit);
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.activity_proceed_order_otp_royal;
        else if (userType == UserType.ELITE)
            return R.layout.activity_proceed_order_otp_elite;
        else if (userType == UserType.CLUB)
            return R.layout.activity_proceed_order_otp_club;
        else
            return R.layout.activity_proceed_order_otp_royal;
    }

    private void callSendNonKycOTPAPI(String type) {
        if (ConnectionDetector.isNetworkAvailable(this)) {
            Log.d("LoginID", "callSendNonKycOTPAPI: "+loginId);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            KYCOTPRequest request = new KYCOTPRequest();
            request.setParticipant_login_id(loginId);
           // request.setType(type);
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .sendKYCOTP(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::ydrOTPAPIResponse, this::ydrAPIError);
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void ydrOTPAPIResponse(YDROTPResponse ydrotpResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(ydrotpResponse.getStatus())) {
            new GulfUnnatiDialog(this,new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_success))
                    .setDescription(ydrotpResponse.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();

                    })
                    .show();
        } else {
            new GulfUnnatiDialog(this,new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(ydrotpResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        }
    }

    private void ydrAPIError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void submitKYCOTPAPI() {
        if (ConnectionDetector.isNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            SubmitKYCOTPRequest request1 = new SubmitKYCOTPRequest();
            //request.setOrder_detail_id(order_id);
            request1.setOtp(pinViewOTP.getText().toString());
            request1.setParticipant_login_id(loginId);

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .submitKYCOTP(request1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::ydrOTPAPIResponse1, this::ydrAPIError1);
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void ydrOTPAPIResponse1(YDROTPResponse ydrotpResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(ydrotpResponse.getStatus())) {
            new GulfUnnatiDialog(this,new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_success))
                    .setDescription(ydrotpResponse.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        SharedPreferences preferences1 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit1 = preferences1.edit();
                        String strType = preferences1.getString("user_type", "");

                        edit1.putString("kyc_status", "true");
                        Intent mIntentGoToHome = null;
                        mIntentGoToHome = new Intent(ProceedOTPForKYCActivity.this,
                                MainDashboardActivity.class);
                        mIntentGoToHome.putExtra("action", strType);
                        mIntentGoToHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mIntentGoToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mIntentGoToHome);
                        finish();

                        finish();

                    })
                    .show();
        } else {
            new GulfUnnatiDialog(this,new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(ydrotpResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
        }
    }

    private void ydrAPIError1(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void submitDigitalRewardAPIError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void submitKYCOTPAPIResponse(SubmitYDROTPResponse submitYDROTPResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(submitYDROTPResponse.getStatus())) {
            finish();
            Intent intent = new Intent(this, YourDigitalRewardDetailsActivity.class);
            intent.putExtra("submitOTPResponse", submitYDROTPResponse);
            startActivity(intent);
        } else {
            new GulfUnnatiDialog(this,new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(submitYDROTPResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), null)
                    .show();
        }
    }


    @Override
    public void onInflate(ViewStub stub, View inflated) {
        inflated.findViewById(R.id.ivBack).setOnClickListener(this);
        tvToolbarTitle = inflated.findViewById(R.id.tvToolbarTitle);
    }

    private void setToolbarUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        } else if (userType == UserType.ELITE) {
            toolbar.setLayoutResource(R.layout.tool_bar_elite);
            toolbar.inflate();
        } else if (userType == UserType.CLUB) {
            toolbar.setLayoutResource(R.layout.tool_bar_club);
            toolbar.inflate();
        } else {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        }
    }

    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }
}
