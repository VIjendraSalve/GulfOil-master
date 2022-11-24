package com.taraba.gulfoilapp.ui.fls.search_retailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.VerifyUpdatePanOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateProfileOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateProfileOTPResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mansi on 3/15/16.
 * Modified by Mansi
 */
public class UpdatePanVerifyOTPFragment extends Fragment {

    TextView txt_otp_desc, tv_resend_otp;
    Button btn_submit;
    String otp, fls_login_id="";
    private PinView pinViewOTP;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private SharedPreferences userPref;
    private boolean isResendOTPAPICall = false;
    private View view;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fls_login_id =  com.taraba.gulfoilapp.ui.fls.search_retailer.PanCardDetailsFragmentArgs.fromBundle(getArguments()).getFlsid();
        Log.d("FLSID", "onCreate: "+fls_login_id);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_otp_verify_pan));
        }
        initComp(view);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                otp = et_otp.getText().toString();
                otp = pinViewOTP.getText().toString();

                if (otp.equals("") || otp == null) {

                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription("Please Enter OTP")
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();

                } else {
                    verifyUpdateProfileOTPAPI(false);

                }
            }
        });


        return view;
    }

    public void initComp(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        txt_otp_desc = view.findViewById(R.id.txt_desc);
        tv_resend_otp = view.findViewById(R.id.tv_resend_otp);

        tv_resend_otp.setOnClickListener(v -> {
            verifyUpdateProfileOTPAPI(true);
        });

//        et_otp = view.findViewById(R.id.edt_otp);
        pinViewOTP = view.findViewById(R.id.pinViewOTP);

        btn_submit = view.findViewById(R.id.btn_submit);
    }


    private void gotoDashboard() {
        Navigation.findNavController(getView()).popBackStack();
        Navigation.findNavController(getView()).popBackStack();
//        Intent i = new Intent(getActivity(), MainDashboardActivity.class);
//        startActivity(i);
//        getActivity().finish();
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_proceed_order_otp_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_proceed_order_otp_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_proceed_order_otp_club;
        else
            return R.layout.fragment_proceed_order_otp_elite;
    }

    private void verifyUpdateProfileOTPAPI(boolean isResend) {
        isResendOTPAPICall = isResend;
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            VerifyUpdatePanOTPRequest request = new VerifyUpdatePanOTPRequest();
            request.setParticipant_login_id(fls_login_id);
            request.setFls_login_id(userPref.getString("usertrno", ""));
            if (isResend) {
                request.setType("resend");
            } else {
                request.setOtp(pinViewOTP.getText().toString());
            }
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .verifyUpdatePanOTP(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::verifyUpdateProfileOTPResponse, this::verifyUpdateProfileOTPError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyUpdateProfileOTPError(Throwable throwable) {
        progressDialog.dismiss();
        if (isResendOTPAPICall) {
            isResendOTPAPICall = false;
        }
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void verifyUpdateProfileOTPResponse(VerifyUpdateProfileOTPResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_success))
                    .hideDialogCloseButton(true)
                    .setDescription(response.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        if (isResendOTPAPICall) {
                            isResendOTPAPICall = false;
                        } else {
                            //Navigation.findNavController(getView()).popBackStack();
                            UpdatePanVerifyOTPFragment.this.lambda$init$0$updateVerifyOTPPanFragment(view);
                        }
                    })
                    .show();
        } else {
            if (isResendOTPAPICall) {
                isResendOTPAPICall = false;
            }
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(response.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                    })
                    .show();
        }

    }

    public /* synthetic */ void lambda$init$0$updateVerifyOTPPanFragment(View view) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();
        bundle.putString("ComesFrom", "MyRetailer");
        Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerSelectionFragment, bundle);
    }

}
