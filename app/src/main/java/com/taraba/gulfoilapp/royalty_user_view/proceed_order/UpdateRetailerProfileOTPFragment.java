package com.taraba.gulfoilapp.royalty_user_view.proceed_order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.ResendOTPRetailerRequest;
import com.taraba.gulfoilapp.model.ResendOTPRetailerResponse;
import com.taraba.gulfoilapp.model.VerifyUpdateRetailerProfileOTPRequest;
import com.taraba.gulfoilapp.model.VerifyUpdateRetailerProfileOTPResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdateRetailerProfileOTPFragment extends Fragment {
    /* access modifiers changed from: private */
    public PinView pinViewOTP;
    Button btn_submit;
    String otp;
    TextView tv_resend_otp;
    TextView txt_otp_desc;
    private Disposable disposable;
    private boolean isResendOTPAPICall = false;
    private ProgressDialogHelper progressDialogHelper;
    private String retailerLoginId = "";
    private SharedPreferences userPref;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.retailerLoginId = UpdateRetailerProfileOTPFragmentArgs.fromBundle(getArguments()).getRetailerLoginId();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_proceed_order_otp_fls, viewGroup, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_otp_verify_profile));
        }
        initComp(inflate);
        this.btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UpdateRetailerProfileOTPFragment updateRetailerProfileOTPFragment = UpdateRetailerProfileOTPFragment.this;
                updateRetailerProfileOTPFragment.otp = updateRetailerProfileOTPFragment.pinViewOTP.getText().toString();
                if (UpdateRetailerProfileOTPFragment.this.otp.equals("") || UpdateRetailerProfileOTPFragment.this.otp == null) {
                    new GulfUnnatiDialog(UpdateRetailerProfileOTPFragment.this.getActivity(), new GulfOilUtils().getUserType()).setTitle(UpdateRetailerProfileOTPFragment.this.getString(R.string.str_error)).setDescription("Please Enter OTP").setPosButtonText(UpdateRetailerProfileOTPFragment.this.getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
                } else {
                    UpdateRetailerProfileOTPFragment.this.verifyUpdateRetailerProfileOTPAPI();
                }
            }
        });
        return inflate;
    }

    public void initComp(View view) {
        this.progressDialogHelper = new ProgressDialogHelper();
        this.userPref = getActivity().getSharedPreferences("signupdetails", 0);
        this.txt_otp_desc = (TextView) view.findViewById(R.id.txt_desc);
        TextView textView = (TextView) view.findViewById(R.id.tv_resend_otp);
        this.tv_resend_otp = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                UpdateRetailerProfileOTPFragment.this.lambda$initComp$0$UpdateRetailerProfileOTPFragment(view);
            }
        });
        this.pinViewOTP = (PinView) view.findViewById(R.id.pinViewOTP);
        this.btn_submit = (Button) view.findViewById(R.id.btn_submit);
    }

    public /* synthetic */ void lambda$initComp$0$UpdateRetailerProfileOTPFragment(View view) {
        resendOTPAPI();
    }

    /* access modifiers changed from: private */
    public void verifyUpdateRetailerProfileOTPAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            VerifyUpdateRetailerProfileOTPRequest verifyUpdateRetailerProfileOTPRequest = new VerifyUpdateRetailerProfileOTPRequest();
            verifyUpdateRetailerProfileOTPRequest.setFls_login_id(this.userPref.getString("usertrno", ""));
            verifyUpdateRetailerProfileOTPRequest.setParticipant_login_id(this.retailerLoginId);
            verifyUpdateRetailerProfileOTPRequest.setOtp(this.pinViewOTP.getText().toString());
            this.disposable = ((GulfService) ServiceBuilder.getRetrofit().create(GulfService.class)).verifyUpdateRetailerProfile(verifyUpdateRetailerProfileOTPRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                public final void accept(Object obj) {
                    UpdateRetailerProfileOTPFragment.this.verifyUpdateRetailerProfileOTPResponse((VerifyUpdateRetailerProfileOTPResponse) obj);
                }
            }, new Consumer() {
                public final void accept(Object obj) {
                    UpdateRetailerProfileOTPFragment.this.verifyUpdateRetailerProfileOTPError((Throwable) obj);
                }
            });
            return;
        }
        Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void verifyUpdateRetailerProfileOTPError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(getString(R.string.something_went_wrong)).setPosButtonText(getString(R.string.str_ok), null).show();
    }

    /* access modifiers changed from: private */
    public void verifyUpdateRetailerProfileOTPResponse(VerifyUpdateRetailerProfileOTPResponse verifyUpdateRetailerProfileOTPResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(verifyUpdateRetailerProfileOTPResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_success)).hideDialogCloseButton(true).setDescription(verifyUpdateRetailerProfileOTPResponse.getMessage()).setPosButtonText(getString(R.string.str_ok), new GulfUnnatiDialog.OnPositiveClickListener() {
                public final void onClick(GulfUnnatiDialog gulfUnnatiDialog) {
                    UpdateRetailerProfileOTPFragment.this.mo27357x6655ba2f(gulfUnnatiDialog);
                }
            }).show();
        } else {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(verifyUpdateRetailerProfileOTPResponse.getError()).setPosButtonText(getString(R.string.str_ok), null).show();
        }
    }

    /* renamed from: lambda$verifyUpdateRetailerProfileOTPResponse$2$UpdateRetailerProfileOTPFragment */
    public /* synthetic */ void mo27357x6655ba2f(GulfUnnatiDialog gulfUnnatiDialog) {
        gulfUnnatiDialog.dismiss();
        Navigation.findNavController(getView()).popBackStack();
        Navigation.findNavController(getView()).popBackStack();
    }

    private void resendOTPAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            ResendOTPRetailerRequest resendOTPRetailerRequest = new ResendOTPRetailerRequest();
            resendOTPRetailerRequest.setParticipant_login_id(this.retailerLoginId);
            this.disposable = ((GulfService) ServiceBuilder.getRetrofit().create(GulfService.class)).resendOTPRetailer(resendOTPRetailerRequest).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() {
                public final void accept(Object obj) {
                    UpdateRetailerProfileOTPFragment.this.resendOTPAPIResponse((ResendOTPRetailerResponse) obj);
                }
            }, new Consumer() {
                public final void accept(Object obj) {
                    UpdateRetailerProfileOTPFragment.this.resendOTPAPIError((Throwable) obj);
                }
            });
            return;
        }
        Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void resendOTPAPIResponse(ResendOTPRetailerResponse resendOTPRetailerResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(resendOTPRetailerResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_success)).hideDialogCloseButton(true).setDescription(resendOTPRetailerResponse.getMessage()).setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
        } else {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(resendOTPRetailerResponse.getError()).setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
        }
    }

    /* access modifiers changed from: private */
    public void resendOTPAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(getString(R.string.something_went_wrong)).setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
    }
}
