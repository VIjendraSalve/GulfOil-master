package com.taraba.gulfoilapp.ui.fls.search_retailer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.GetRetailerProfileDetailsRequest;
import com.taraba.gulfoilapp.model.GetRetailerProfileDetailsResponse;
import com.taraba.gulfoilapp.model.ResendOTPRetailerRequest;
import com.taraba.gulfoilapp.model.ResendOTPRetailerResponse;
import com.taraba.gulfoilapp.model.SearchRetailerResponse;
import com.taraba.gulfoilapp.model.UpdateRetailerProfileRequest;
import com.taraba.gulfoilapp.model.UpdateRetailerProfileResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.GulfValidator;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.cache.DiskLruCache;

/* renamed from: com.taraba.gulfoilapp.ui.fls.search_retailer.EditSearchRetailerFragment */
public class EditSearchRetailerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewNotificationFragment";
    private AppCompatButton btnCancel;
    private AppCompatButton btnSubmit;
    private MaterialCardView cvSearchRetailerDetailsContainer;
    private TextInputEditText edtMobileNo;
    private TextInputEditText edtRetailerId;
    private TextInputEditText edtRetailerUID;
    private AppCompatEditText edtSearchBy;
    private TextInputEditText edtShopName;
    private ImageView ivSearchAction;
    private SearchRetailerResponse participantDetailsresponse;
    private ProgressDialogHelper progressDialogHelper;
    private GetRetailerProfileDetailsResponse response;
    private String searchBy = "";
    private TextInputLayout tilMobileNo;
    private TextInputLayout tilShopName;
    private TextView tvMsg;
    private AppCompatTextView tvSearchedText;
    private SharedPreferences userPref;

    static /* synthetic */ void lambda$init$0(View view) {
    }

    static /* synthetic */ void lambda$init$1(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.searchBy = com.taraba.gulfoilapp.ui.fls.search_retailer
                .EditSearchRetailerFragmentArgs
                .fromBundle(getArguments())
                .getSearchBy();
        this.participantDetailsresponse = com.taraba.gulfoilapp.ui.fls.search_retailer.EditSearchRetailerFragmentArgs.fromBundle(getArguments()).getSearchRetailerResponse();
        Log.e(TAG, "Search By: " + this.searchBy);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_edit_search_reatiler, viewGroup, false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_my_retailers));
        }
        init(inflate);
        getRetailerProfileDetails();
        return inflate;
    }

    private void init(View view) {
        this.progressDialogHelper = new ProgressDialogHelper();
        this.userPref = getActivity().getSharedPreferences("signupdetails", 0);
        this.edtSearchBy = (AppCompatEditText) view.findViewById(R.id.edtSearchBy);
        view.findViewById(R.id.ivSearchAction).setOnClickListener(this);
        this.tvSearchedText = (AppCompatTextView) view.findViewById(R.id.tvSearchedText);
        this.edtRetailerUID = (TextInputEditText) view.findViewById(R.id.edtRetailerUID);
        this.edtRetailerId = (TextInputEditText) view.findViewById(R.id.edtRetailerId);
        this.edtShopName = (TextInputEditText) view.findViewById(R.id.edtShopName);
        this.edtMobileNo = (TextInputEditText) view.findViewById(R.id.edtMobileNo);
        this.cvSearchRetailerDetailsContainer = (MaterialCardView) view.findViewById(R.id.cvSearchRetailerDetailsContainer);
        this.btnCancel = (AppCompatButton) view.findViewById(R.id.btnCancel);
        this.btnSubmit = (AppCompatButton) view.findViewById(R.id.btnSubmit);
        this.tilShopName = (TextInputLayout) view.findViewById(R.id.tilShopName);
        this.tilMobileNo = (TextInputLayout) view.findViewById(R.id.tilMobileNo);
        this.tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        this.btnCancel.setOnClickListener(this);
        this.btnSubmit.setOnClickListener(this);
        this.tilShopName.setEndIconOnClickListener(this);
        this.tilMobileNo.setEndIconOnClickListener(this);
        this.edtSearchBy.setEnabled(false);
        if (this.searchBy.equals("MOBILE")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_mobile_number));
            this.tvSearchedText.setText(this.participantDetailsresponse.getParticpant_data().get(0).getMobile_no());
        } else if (this.searchBy.equals("RETAILER_CODE")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_retailer_uid));
            this.tvSearchedText.setText(this.participantDetailsresponse.getParticpant_data().get(0).getUsername());
        } else if (this.searchBy.equals("RETAILER_TALLY_ID")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_retailer_tally_id));
            this.tvSearchedText.setText(this.participantDetailsresponse.getParticpant_data().get(0).getRetailer_code());
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            Navigation.findNavController(getView()).popBackStack();
        } else if (id != R.id.btnSubmit || !validateField()) {
        } else {
            if (this.response.getData().get(0).getEdit().equals(DiskLruCache.VERSION_1)) {
                updateRetailerProfileAPI();
            } else if (this.response.getData().get(0).getEdit().equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                resendOTPAPI();
            }
        }
    }

    public void onResume() {
        super.onResume();
    }

    private boolean validateField() {
        if (GulfValidator.isEmpty(this.edtShopName.getText().toString())) {
            showErrorDialog("Please enter shop name");
            return false;
        } else if (GulfValidator.isEmpty(this.edtMobileNo.getText().toString())) {
            showErrorDialog("Please enter mobile number");
            return false;
        } else if (!GulfValidator.isNumber(this.edtMobileNo.getText().toString())) {
            showErrorDialog("Please enter valid mobile number");
            return false;
        } else if (GulfValidator.isValidLength(this.edtMobileNo.getText().toString(), 10)) {
            return true;
        } else {
            showErrorDialog("Mobile number should be 10 digit");
            return false;
        }
    }

    private void showErrorDialog(String str) {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(str).setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
    }

    private void getRetailerProfileDetails() {
        GetRetailerProfileDetailsRequest getRetailerProfileDetailsRequest = new GetRetailerProfileDetailsRequest();
        getRetailerProfileDetailsRequest.setParticipant_login_id(this.participantDetailsresponse.getParticpant_data().get(0).getLogin_id_pk());
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            Disposable disposable = ServiceBuilder.getRetrofit().create(GulfService.class)
                    .getRetilerProfileDetails(getRetailerProfileDetailsRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::getRetailerProfileDetailsAPIResponse, this::getRetailerProfileDetailsAPIError);
            return;
        }
        showErrorDialog(getString(R.string.str_internet_disconnected));
    }

    /* access modifiers changed from: private */
    public void getRetailerProfileDetailsAPIResponse(GetRetailerProfileDetailsResponse getRetailerProfileDetailsResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(getRetailerProfileDetailsResponse.getStatus())) {
            this.response = getRetailerProfileDetailsResponse;
            setValues();
            return;
        }
        showErrorDialog(getRetailerProfileDetailsResponse.getError());
    }

    /* access modifiers changed from: private */
    public void getRetailerProfileDetailsAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        showErrorDialog(getString(R.string.something_went_wrong));
    }

    private void setValues() {
        this.cvSearchRetailerDetailsContainer.setVisibility(View.VISIBLE);
        this.edtRetailerUID.setText(this.response.getData().get(0).getRetailer_id());
        this.edtRetailerId.setText(this.participantDetailsresponse.getParticpant_data().get(0).getRetailer_code());
        this.edtShopName.setText(this.response.getData().get(0).getWorkshop_name());
        this.edtMobileNo.setText(this.response.getData().get(0).getMobile_no());
        this.tvMsg.setText(this.response.getData().get(0).getMsg());
        String edit = this.response.getData().get(0).getEdit();
        edit.hashCode();
        char c = 65535;
        switch (edit.hashCode()) {
            case 48:
                if (edit.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (edit.equals(DiskLruCache.VERSION_1)) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (edit.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.edtShopName.setEnabled(false);
                this.edtMobileNo.setEnabled(false);
                this.btnCancel.setVisibility(View.GONE);
                this.btnSubmit.setVisibility(View.GONE);
                return;
            case 1:
                this.edtShopName.setEnabled(true);
                this.edtMobileNo.setEnabled(true);
                this.btnSubmit.setText("Update Profile");
                return;
            case 2:
                this.edtShopName.setEnabled(false);
                this.edtMobileNo.setEnabled(false);
                this.btnSubmit.setText("Resend OTP");
                return;
            default:
                return;
        }
    }

    private void updateRetailerProfileAPI() {
        UpdateRetailerProfileRequest updateRetailerProfileRequest = new UpdateRetailerProfileRequest();
        updateRetailerProfileRequest.setFls_login_id(this.userPref.getString("usertrno", ""));
        updateRetailerProfileRequest.setParticipant_login_id(this.participantDetailsresponse.getParticpant_data().get(0).getLogin_id_pk());
        updateRetailerProfileRequest.setWorkshop_name(this.edtShopName.getText().toString());
        updateRetailerProfileRequest.setMobile_no(this.edtMobileNo.getText().toString());
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            Disposable disposable = ServiceBuilder.getRetrofit().create(GulfService.class)
                    .updateRetailerProfile(updateRetailerProfileRequest)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateRetailerProfileAPIResponse, this::updateRetailerProfileAPIError);
            return;
        }
        showErrorDialog(getString(R.string.str_internet_disconnected));
    }

    /* access modifiers changed from: private */
    public void updateRetailerProfileAPIResponse(UpdateRetailerProfileResponse updateRetailerProfileResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(updateRetailerProfileResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_success)).hideDialogCloseButton(true).setDescription(updateRetailerProfileResponse.getMessage()).setPosButtonText(getString(R.string.str_ok), new GulfUnnatiDialog.OnPositiveClickListener() {
                public final void onClick(GulfUnnatiDialog gulfUnnatiDialog) {
                    EditSearchRetailerFragment.this.mo27617x794021c0(gulfUnnatiDialog);
                }
            }).show();
        } else {
            showErrorDialog(updateRetailerProfileResponse.getError());
        }
    }

    /* renamed from: lambda$updateRetailerProfileAPIResponse$2$EditSearchRetailerFragment */
    public /* synthetic */ void mo27617x794021c0(GulfUnnatiDialog gulfUnnatiDialog) {
        gulfUnnatiDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("retailerLoginId", this.participantDetailsresponse.getParticpant_data().get(0).getLogin_id_pk());
        Navigation.findNavController(getView()).navigate((int) R.id.updateRetailerProfileOTPFragment, bundle);
    }

    /* access modifiers changed from: private */
    public void updateRetailerProfileAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        showErrorDialog(getString(R.string.something_went_wrong));
    }

    private void resendOTPAPI() {
        ResendOTPRetailerRequest resendOTPRetailerRequest = new ResendOTPRetailerRequest();
        resendOTPRetailerRequest.setParticipant_login_id(this.participantDetailsresponse.getParticpant_data().get(0).getLogin_id_pk());
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            Disposable disposable = ServiceBuilder.getRetrofit().create(GulfService.class)
                    .resendOTPRetailer(resendOTPRetailerRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resendOTPAPIResponse, this::resendOTPAPIError);
            return;
        }
        showErrorDialog(getString(R.string.str_internet_disconnected));
    }

    /* access modifiers changed from: private */
    public void resendOTPAPIResponse(ResendOTPRetailerResponse resendOTPRetailerResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(resendOTPRetailerResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_success)).hideDialogCloseButton(true).setDescription(resendOTPRetailerResponse.getMessage()).setPosButtonText(getString(R.string.str_ok), new GulfUnnatiDialog.OnPositiveClickListener() {
                public final void onClick(GulfUnnatiDialog gulfUnnatiDialog) {
                    EditSearchRetailerFragment.this.lambda$resendOTPAPIResponse$3$EditSearchRetailerFragment(gulfUnnatiDialog);
                }
            }).show();
        } else {
            showErrorDialog(resendOTPRetailerResponse.getError());
        }
    }

    public /* synthetic */ void lambda$resendOTPAPIResponse$3$EditSearchRetailerFragment(GulfUnnatiDialog gulfUnnatiDialog) {
        gulfUnnatiDialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("retailerLoginId", this.participantDetailsresponse.getParticpant_data().get(0).getLogin_id_pk());
        Navigation.findNavController(getView()).navigate((int) R.id.updateRetailerProfileOTPFragment, bundle);
    }

    /* access modifiers changed from: private */
    public void resendOTPAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        showErrorDialog(getString(R.string.something_went_wrong));
    }
}
