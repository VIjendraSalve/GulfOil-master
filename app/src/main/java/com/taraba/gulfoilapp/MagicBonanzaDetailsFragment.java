package com.taraba.gulfoilapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.MagicBonanzaListResponse;
import com.taraba.gulfoilapp.model.TargetMeterReqest;
import com.taraba.gulfoilapp.model.TargetMeterResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MagicBonanzaDetailsFragment extends Fragment {
    private MagicBonanzaListResponse.Data data;
    private Disposable disposable;
    private static final String TAG = "MagicBonanzaDetailsFrag";
    private TextView tvTargetAchieved, tvRetailerID, tvUIN, tvheader,
            tvBaseTarget,
            tvStarTarget,
            tvBaseAchieved,
            tvStarAchieved,
            tvBaseBalance,
            tvStarBalance,
            tvTitleStarGrowth, tvTitleStarGrowthTarget;
    private ImageView iv_base_growth_target_meter, iv_star_growth_target_meter;
    private TargetMeterResponse targetMeterResponse;
    private LinearLayout llTargetMeterContainer, llStarTargetMeter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (MagicBonanzaListResponse.Data) getArguments().getSerializable("tmc");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_magic_bonanza));
        }
        init(view);
        return view;
    }

    private void init(View view) {
        tvTargetAchieved = view.findViewById(R.id.tvTargetAchieved);

        tvRetailerID = view.findViewById(R.id.tvRetailerID);
        tvUIN = view.findViewById(R.id.tvUIN);
        tvheader = view.findViewById(R.id.tvheader);
        iv_base_growth_target_meter = view.findViewById(R.id.iv_base_growth_target_meter);
        iv_star_growth_target_meter = view.findViewById(R.id.iv_star_growth_target_meter);
        llTargetMeterContainer = view.findViewById(R.id.llTargetMeterContainer);
        tvBaseTarget = view.findViewById(R.id.tvBaseTarget);
        tvStarTarget = view.findViewById(R.id.tvStarTarget);
        tvBaseAchieved = view.findViewById(R.id.tvBaseAchieved);
        tvStarAchieved = view.findViewById(R.id.tvStarAchieved);
        tvBaseBalance = view.findViewById(R.id.tvBaseBalance);
        tvStarBalance = view.findViewById(R.id.tvStarBalance);
        tvTitleStarGrowth = view.findViewById(R.id.tvTitleStarGrowth);
        tvTitleStarGrowthTarget = view.findViewById(R.id.tvTitleStarGrowthTarget);
        llStarTargetMeter = view.findViewById(R.id.llStarTargetMeter);
        callMagicBonanzaListAPI();
    }

    private void callMagicBonanzaListAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait");
            TargetMeterReqest reqest = new TargetMeterReqest();
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", getActivity().MODE_PRIVATE);
            String loginId = preferences1.getString("usertrno", "");
            reqest.setLogin_id(loginId);
            reqest.setTarget_meter_header_id_pk(data.getTarget_meter_header_id_pk());
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTargetMeter(reqest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::targetMeterResponse, this::targetMeterError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void targetMeterResponse(TargetMeterResponse response) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            if (response.getData().getBase_growth_value() != null) {
                targetMeterResponse = response;
                Log.e(TAG, "targetMeterResponse: " + response.toString());
                setTargetMeterDetails();
            } else {
                new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                        .hideDialogCloseButton(true)
                        .setDescription(getString(R.string.str_data_not_found))
                        .setPosButtonText(getString(R.string.str_ok), dialog -> {
                            dialog.dismiss();
                            Navigation.findNavController(getView()).popBackStack();
                        })
                        .show();
            }
        } else {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setDescription(response.getError())
                    .hideDialogCloseButton(true)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();
                    })
                    .show();
        }
    }

    private void targetMeterError(Throwable throwable) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).popBackStack();
                })
                .show();
    }

    private void setTargetMeterDetails() {
        llTargetMeterContainer.setVisibility(View.VISIBLE);
        tvheader.setText(targetMeterResponse.getData().getHeader());


        tvTargetAchieved.setText("Your Achievement : " + targetMeterResponse.getData().getAchievement());

        tvRetailerID.setText("Retailer ID : " + targetMeterResponse.getData().getRetailer_code());
        tvUIN.setText("UIN: " + targetMeterResponse.getData().getUin());
        /*ADDED BY PRAVIN DHARAM ON 11-Jul-18 */

        showBaseTargetMeterImage("" + targetMeterResponse.getData().getBase_growth_target());
        showStarTargetMeterImage("" + targetMeterResponse.getData().getStar_growth_target());
        //ENDED
        if (targetMeterResponse.getData().getStar_growth_value() != null) {
            tvStarTarget.setText(targetMeterResponse.getData().getStar_growth_value().getTarget());
            tvStarAchieved.setText(targetMeterResponse.getData().getStar_growth_value().getAchieved());
            tvStarBalance.setText(targetMeterResponse.getData().getStar_growth_value().getBalance());

        } else {
            tvStarTarget.setText("");
            tvStarAchieved.setText("");
            tvStarBalance.setText("");
        }
        tvBaseTarget.setText(targetMeterResponse.getData().getBase_growth_value().getTarget());
        tvBaseAchieved.setText(targetMeterResponse.getData().getBase_growth_value().getAchieved());
        tvBaseBalance.setText(targetMeterResponse.getData().getBase_growth_value().getBalance());

        if (!targetMeterResponse.getData().star_growth_meter) {
            tvStarTarget.setVisibility(View.GONE);
            tvStarAchieved.setVisibility(View.GONE);
            tvStarBalance.setVisibility(View.GONE);
            tvTitleStarGrowth.setVisibility(View.GONE);
            tvTitleStarGrowthTarget.setVisibility(View.GONE);
            llStarTargetMeter.setVisibility(View.GONE);
        }

    }


    private void showBaseTargetMeterImage(String percentage) {
        if (percentage != null && !percentage.isEmpty()) {
            double numPercentage = Double.parseDouble(percentage);
            if (numPercentage == 0) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_0_grey));
            } else if (numPercentage > 0 && numPercentage <= 5) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_5));
            } else if (numPercentage > 5 && numPercentage <= 10) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_10));
            } else if (numPercentage > 10 && numPercentage <= 15) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_15));
            } else if (numPercentage > 15 && numPercentage <= 20) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_20));
            } else if (numPercentage > 20 && numPercentage <= 25) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_25));
            } else if (numPercentage > 25 && numPercentage <= 30) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_30));
            } else if (numPercentage > 30 && numPercentage <= 35) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_35));
            } else if (numPercentage > 35 && numPercentage <= 40) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_40));
            } else if (numPercentage > 40 && numPercentage <= 45) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_45));
            } else if (numPercentage > 45 && numPercentage <= 50) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_50));
            } else if (numPercentage > 50 && numPercentage <= 55) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_55));
            } else if (numPercentage > 55 && numPercentage <= 60) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_60));
            } else if (numPercentage > 60 && numPercentage <= 65) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_65));
            } else if (numPercentage > 65 && numPercentage <= 70) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_70));
            } else if (numPercentage > 70 && numPercentage <= 75) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_75));
            } else if (numPercentage > 75 && numPercentage <= 80) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_80));
            } else if (numPercentage > 80 && numPercentage <= 85) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_85));
            } else if (numPercentage > 85 && numPercentage <= 90) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_90));
            } else if (numPercentage > 90 && numPercentage <= 95) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_95));
            } else if (numPercentage > 95 && numPercentage <= 100) {
                iv_base_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_100));
            }
        }
    }

    private void showStarTargetMeterImage(String percentage) {
        if (percentage != null && !percentage.isEmpty()) {
            double numPercentage = Double.parseDouble(percentage);
            if (numPercentage == 0) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_0_grey));
            } else if (numPercentage > 0 && numPercentage <= 5) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_5));
            } else if (numPercentage > 5 && numPercentage <= 10) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_10));
            } else if (numPercentage > 10 && numPercentage <= 15) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_15));
            } else if (numPercentage > 15 && numPercentage <= 20) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_20));
            } else if (numPercentage > 20 && numPercentage <= 25) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_25));
            } else if (numPercentage > 25 && numPercentage <= 30) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_30));
            } else if (numPercentage > 30 && numPercentage <= 35) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_35));
            } else if (numPercentage > 35 && numPercentage <= 40) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_40));
            } else if (numPercentage > 40 && numPercentage <= 45) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_45));
            } else if (numPercentage > 45 && numPercentage <= 50) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_50));
            } else if (numPercentage > 50 && numPercentage <= 55) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_55));
            } else if (numPercentage > 55 && numPercentage <= 60) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_60));
            } else if (numPercentage > 60 && numPercentage <= 65) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_65));
            } else if (numPercentage > 65 && numPercentage <= 70) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_70));
            } else if (numPercentage > 70 && numPercentage <= 75) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_75));
            } else if (numPercentage > 75 && numPercentage <= 80) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_80));
            } else if (numPercentage > 80 && numPercentage <= 85) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_85));
            } else if (numPercentage > 85 && numPercentage <= 90) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_90));
            } else if (numPercentage > 90 && numPercentage <= 95) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_95));
            } else if (numPercentage > 95 && numPercentage <= 100) {
                iv_star_growth_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.icon_100));
            }
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_magic_bonanza_details_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_magic_bonanza_details_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_magic_bonanza_details_club;
        else
            return R.layout.fragment_magic_bonanza_details_royal;
    }
}
