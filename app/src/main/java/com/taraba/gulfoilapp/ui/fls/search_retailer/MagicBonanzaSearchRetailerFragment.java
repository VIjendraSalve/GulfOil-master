package com.taraba.gulfoilapp.ui.fls.search_retailer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.YourDigitalRewardsActivity;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.SearchRetailerRequest;
import com.taraba.gulfoilapp.model.SearchRetailerResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.GulfValidator;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/* renamed from: com.taraba.gulfoilapp.ui.fls.search_retailer.MagicBonanzaSearchRetailerFragment */
public class MagicBonanzaSearchRetailerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewNotificationFragment";
    private AppCompatButton btnMagicBonanza;
    private AppCompatButton btnPoints;
    private AppCompatButton btnRewardStatus;
    private MaterialCardView cvSearchRetailerDetailsContainer;
    private TextInputEditText edtBankAccountDetails;
    private TextInputEditText edtDistributor;
    private TextInputEditText edtMobileNo;
    private TextInputEditText edtPanCardDetails;
    private TextInputEditText edtProgramStatus;
    private TextInputEditText edtRetailerId;
    private TextInputEditText edtRetailerUID;
    private AppCompatEditText edtSearchBy;
    private TextInputEditText edtShopName;
    private ImageView ivSearchAction;
    private ProgressDialogHelper progressDialogHelper;
    private SearchRetailerResponse response;
    private String searchBy = "";
    private TextInputLayout tilMobileNo;
    private TextInputLayout tilShopName;
    private AppCompatTextView tvSearchedText;
    private SharedPreferences userPref;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.searchBy = com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerFragmentArgs
                .fromBundle(getArguments()).getSearchBy();
        Log.e(TAG, "Search By: " + this.searchBy);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_magic_bonanza_search_reatiler, viewGroup, false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_my_retailers));
        }
        init(inflate);
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
        this.edtProgramStatus = (TextInputEditText) view.findViewById(R.id.edtProgramStatus);
        this.edtPanCardDetails = (TextInputEditText) view.findViewById(R.id.edtPanCardDetails);
        this.edtBankAccountDetails = (TextInputEditText) view.findViewById(R.id.edtBankAccountDetails);
        this.edtDistributor = (TextInputEditText) view.findViewById(R.id.edtDistributor);
        this.cvSearchRetailerDetailsContainer = (MaterialCardView) view.findViewById(R.id.cvSearchRetailerDetailsContainer);
        view.findViewById(R.id.btnPoints).setOnClickListener(this);
        view.findViewById(R.id.btnRewardStatus).setOnClickListener(this);
        view.findViewById(R.id.btnMagicBonanza).setOnClickListener(this);
        this.tilShopName = (TextInputLayout) view.findViewById(R.id.tilShopName);
        this.tilMobileNo = (TextInputLayout) view.findViewById(R.id.tilMobileNo);
        this.tilShopName.setEndIconOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                MagicBonanzaSearchRetailerFragment.this.lambda$init$0$MagicBonanzaSearchRetailerFragment(view);
            }
        });
        this.tilMobileNo.setEndIconOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                MagicBonanzaSearchRetailerFragment.this.lambda$init$1$MagicBonanzaSearchRetailerFragment(view);
            }
        });
        if (this.searchBy.equals("MOBILE")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_mobile_number));
            this.edtSearchBy.setInputType(3);
            this.edtSearchBy.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else if (this.searchBy.equals("RETAILER_CODE")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_retailer_uid));
            this.edtSearchBy.setInputType(16384);
            this.edtSearchBy.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        } else if (this.searchBy.equals("RETAILER_TALLY_ID")) {
            this.edtSearchBy.setHint(getString(R.string.str_search_by_retailer_tally_id));
            this.edtSearchBy.setInputType(16384);
            this.edtSearchBy.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }
    }

    public /* synthetic */ void lambda$init$0$MagicBonanzaSearchRetailerFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("SearchBy", "MOBILE");
        bundle.putParcelable("SearchRetailerResponse", this.response);
        Navigation.findNavController(getView()).navigate((int) R.id.editSearchRetailerFragment, bundle);
    }

    public /* synthetic */ void lambda$init$1$MagicBonanzaSearchRetailerFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("SearchBy", "RETAILER_CODE");
        bundle.putParcelable("SearchRetailerResponse", this.response);
        Navigation.findNavController(getView()).navigate((int) R.id.editSearchRetailerFragment, bundle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMagicBonanza:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromFLS", true);
                bundle.putString("retailerLoginId", this.response.getParticpant_data().get(0).getLogin_id_pk());
                Navigation.findNavController(getView()).navigate((int) R.id.magicBonanzaListFragment, bundle);
                return;
            case R.id.btnPoints:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("isFromFLS", true);
                bundle2.putString("retailerLoginId", this.response.getParticpant_data().get(0).getLogin_id_pk());
                Navigation.findNavController(getView()).navigate((int) R.id.myPointsFragmentFls, bundle2);
                return;
            case R.id.btnRewardStatus:
                Intent intent = new Intent(getActivity(), YourDigitalRewardsActivity.class);
                intent.putExtra("isFromFLS", true);
                intent.putExtra("retailerLoginId", this.response.getParticpant_data().get(0).getLogin_id_pk());
                startActivity(intent);
                return;
            case R.id.ivSearchAction:
                if (validateField()) {
                    searchRetailerAPI();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private boolean validateField() {
        Editable text = this.edtSearchBy.getText();
        text.getClass();
        String obj = text.toString();
        if (GulfValidator.isEmpty(obj)) {
            showErrorDialog("Please enter value");
            return false;
        } else if (this.searchBy.equals("MOBILE") && !GulfValidator.isNumber(obj)) {
            showErrorDialog("Please enter valid mobile number");
            return false;
        } else if (!this.searchBy.equals("MOBILE") || GulfValidator.isValidLength(obj, 10)) {
            return true;
        } else {
            showErrorDialog("Mobile number should be 10 digit");
            return false;
        }
    }

    private void showErrorDialog(String str) {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(str).setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
    }

    private void searchRetailerAPI() {
        SearchRetailerRequest searchRetailerRequest = new SearchRetailerRequest();
        searchRetailerRequest.setLogin_user_type(this.userPref.getString("user_type", ""));
        searchRetailerRequest.setUser_login_id(this.userPref.getString("usertrno", ""));
        if (this.searchBy.equals("MOBILE")) {
            Editable text = this.edtSearchBy.getText();
            text.getClass();
            searchRetailerRequest.setMobile_number(text.toString());
        }
        if (this.searchBy.equals("RETAILER_CODE")) {
            Editable text2 = this.edtSearchBy.getText();
            text2.getClass();
            searchRetailerRequest.setRetailer_code(text2.toString());
        }
        if (this.searchBy.equals("RETAILER_TALLY_ID")) {
            Editable text3 = this.edtSearchBy.getText();
            text3.getClass();
            searchRetailerRequest.setRetailer_tally_id(text3.toString());
        }
        searchRetailerRequest.setSearch_by(getSearchBy());
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            Disposable disposable = ServiceBuilder.getRetrofit().create(GulfService.class)
                    .searchRetailer(searchRetailerRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::searchRetailerAPIResponse, this::searchRetailerAPIError);
            return;
        }
        showErrorDialog(getString(R.string.str_internet_disconnected));
    }

    private String getSearchBy() {
        if (this.searchBy.equals("MOBILE")) {
            return "mobile_number";
        }
        if (this.searchBy.equals("RETAILER_CODE")) {
            return "retailer_code";
        }
        if (this.searchBy.equals("SHOP")) {
            return "workshop_name";
        }
        return this.searchBy.equals("RETAILER_TALLY_ID") ? "retailer_tally_id" : "";
    }

    /* access modifiers changed from: private */
    public void searchRetailerAPIResponse(SearchRetailerResponse searchRetailerResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(searchRetailerResponse.getStatus())) {
            this.response = searchRetailerResponse;
            Bundle bundle = new Bundle();
            bundle.putSerializable("isFromFLS", true);
            bundle.putSerializable("retailerLoginId", searchRetailerResponse.getParticpant_data().get(0).getLogin_id_pk());
            Navigation.findNavController(getView()).navigate((int) R.id.magicBonanzaListFragment, bundle);
            return;
        }
        showErrorDialog(searchRetailerResponse.getError());
    }

    /* access modifiers changed from: private */
    public void searchRetailerAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        showErrorDialog(getString(R.string.something_went_wrong));
    }

    private void setValues() {
        this.cvSearchRetailerDetailsContainer.setVisibility(View.VISIBLE);
        this.edtRetailerUID.setText(this.response.getParticpant_data().get(0).getUsername());
        this.edtRetailerId.setText(this.response.getParticpant_data().get(0).getRetailer_code());
        this.edtShopName.setText(this.response.getParticpant_data().get(0).getWorkshop_name());
        this.edtMobileNo.setText(this.response.getParticpant_data().get(0).getMobile_no());
        this.edtProgramStatus.setText(this.response.getParticpant_data().get(0).getState());
        this.edtPanCardDetails.setText(this.response.getParticpant_data().get(0).getPan_detail().getPan());
        this.edtBankAccountDetails.setText(this.response.getParticpant_data().get(0).getBank_account_details());
        this.edtDistributor.setText(this.response.getParticpant_data().get(0).getDb_name());
    }
}
