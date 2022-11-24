package com.taraba.gulfoilapp.ui.fls.search_retailer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.R;

/* renamed from: com.taraba.gulfoilapp.ui.fls.search_retailer.NewSearchRetailerSelectionFragment */
public class NewSearchRetailerSelectionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewNotificationFragment";
    private String comesFrom = "";
    private ProgressDialog progressDialog;
    private SharedPreferences userPref;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.comesFrom =
                com.taraba.gulfoilapp.ui.fls.search_retailer
                        .NewSearchRetailerSelectionFragmentArgs
                        .fromBundle(getArguments())
                        .getComesFrom();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.
                inflate(R.layout.fragment_new_search_reatiler_selection,
                        viewGroup,
                        false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            if (this.comesFrom.equals("MyRetailer")) {
                ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_my_retailers));
            } else if (this.comesFrom.equals("MagicBonanza")) {
                ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_magic_bonanza));
            } else {
                ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_my_retailers));
            }
        }
        init(inflate);
        return inflate;
    }

    private void init(View view) {
        this.userPref = getActivity().getSharedPreferences("signupdetails", 0);
        view.findViewById(R.id.cvSearchByMobileNumber).setOnClickListener(this);
        view.findViewById(R.id.cvSearchByRetailerCode).setOnClickListener(this);
        view.findViewById(R.id.cvSearchByRetailerTallyId).setOnClickListener(this);
    }

    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.cvSearchByMobileNumber:
                if (this.comesFrom.equals("MyRetailer")) {
                    bundle.putString("SearchBy", "MOBILE");
                    Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerFragment, bundle);
                    return;
                } else if (this.comesFrom.equals("MagicBonanza")) {
                    bundle.putString("SearchBy", "MOBILE");
                    Navigation.findNavController(getView()).navigate((int) R.id.magicBonanzaSearchRetailerFragment, bundle);
                    return;
                } else {
                    return;
                }
            case R.id.cvSearchByRetailerCode:
                if (this.comesFrom.equals("MyRetailer")) {
                    bundle.putString("SearchBy", "RETAILER_CODE");
                    Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerFragment, bundle);
                    return;
                } else if (this.comesFrom.equals("MagicBonanza")) {
                    bundle.putString("SearchBy", "RETAILER_CODE");
                    Navigation.findNavController(getView()).navigate((int) R.id.magicBonanzaSearchRetailerFragment, bundle);
                    return;
                } else {
                    return;
                }
            case R.id.cvSearchByRetailerTallyId:
                if (this.comesFrom.equals("MyRetailer")) {
                    bundle.putString("SearchBy", "RETAILER_TALLY_ID");
                    Navigation.findNavController(getView()).navigate((int) R.id.newSearchRetailerFragment, bundle);
                    return;
                } else if (this.comesFrom.equals("MagicBonanza")) {
                    bundle.putString("SearchBy", "RETAILER_TALLY_ID");
                    Navigation.findNavController(getView()).navigate((int) R.id.magicBonanzaSearchRetailerFragment, bundle);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
