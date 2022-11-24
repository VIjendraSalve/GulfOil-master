package com.taraba.gulfoilapp.royalty_user_view.proceed_order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.YourDigitalRewardDetailsActivity;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
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
public class ProceedDigitalRewardsOTPActivity extends AppCompatActivity implements ViewStub.OnInflateListener, View.OnClickListener {

    TextView txt_otp_desc, tv_resend_otp;
    Button btn_submit;
    String order_id, otp;
    //    EditText et_otp;
    private PinView pinViewOTP;
    private Disposable disposable;
    private String loginId;
    private ProgressDialog progressDialog;
    private ViewStub toolbar;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());

        order_id = getIntent().getStringExtra("orderDetailId");


        SharedPreferences preferences1 = getSharedPreferences(
                "signupdetails", MODE_PRIVATE);
        loginId = preferences1.getString("usertrno", "");

        initComp();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_your_digital_rewards));
        final ConnectionDetector cd = new ConnectionDetector(
                this);
        if (NetworkUtils.isNetworkAvailable(this)) {
            callSendDigitalRewardOTPAPI("");
        } else {
            Toast.makeText(this, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                otp = et_otp.getText().toString();
                otp = pinViewOTP.getText().toString();

                if (otp.equals("") || otp == null) {

                    new GulfUnnatiDialog(ProceedDigitalRewardsOTPActivity.this, new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription("Please Enter OTP")
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();

                } else {

                    if (NetworkUtils.isNetworkAvailable(ProceedDigitalRewardsOTPActivity.this)) {
                        submitDigitalRewardOTPAPI();

                    } else {
                        Toast.makeText(ProceedDigitalRewardsOTPActivity.this, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
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

        tv_resend_otp.setOnClickListener(v -> callSendDigitalRewardOTPAPI("resend"));

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

    private void callSendDigitalRewardOTPAPI(String type) {
        if (ConnectionDetector.isNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            YDROTPRequest request = new YDROTPRequest();
            request.setOrder_detail_id(order_id);
            request.setParticipant_login_id(loginId);
            request.setType(type);
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .sendDigitalRewardOTP(request)
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
            txt_otp_desc.setText(ydrotpResponse.getMessage());
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

    private void submitDigitalRewardOTPAPI() {
        if (ConnectionDetector.isNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            SubmitYDROTPRequest request = new SubmitYDROTPRequest();
            request.setOrder_detail_id(order_id);
            request.setOtp(pinViewOTP.getText().toString());
            request.setParticipant_login_id(loginId);

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .submitDigitalRewardOTP(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::submitDigitalRewardOTPAPIResponse, this::submitDigitalRewardAPIError);
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void submitDigitalRewardAPIError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void submitDigitalRewardOTPAPIResponse(SubmitYDROTPResponse submitYDROTPResponse) {
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
