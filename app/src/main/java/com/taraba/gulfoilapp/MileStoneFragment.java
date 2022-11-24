package com.taraba.gulfoilapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.MilestoneJourneyRequest;
import com.taraba.gulfoilapp.model.MilestoneJourneyResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MileStoneFragment extends Fragment {
    private static final String TAG = "MileStoneFragment";
    private TextView tvUIN, tvYourGoal;
    private SharedPreferences userPref;
    private int targetValue, achievement;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private boolean isPopUpDisplay;
    private ImageView ivButton4,
            ivRoad4,
            ivButton3,
            ivRoad3,
            ivButton2,
            ivRoad2,
            ivButton1,
            ivRoad1,
            ivButtonStart;
    private TextView tvTag1Title, tvTag2Title, tvTag3Title, tvTag4Title, tvAchievement;
    private int stage = 0;
    private MilestoneJourneyResponse response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetValue = getArguments().getInt("targetValue");
            achievement = getArguments().getInt("achievement");
            isPopUpDisplay = getArguments().getBoolean("isPopUpDisplay");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        AppConfig.goToMilestone = true;
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_milestone_journey));
        }
        init(view);
        setListeners();
        setValues();
        submitMilestoneJourney();

        return view;
    }

    private void init(View view) {
        tvUIN = view.findViewById(R.id.tvUIN);
        tvYourGoal = view.findViewById(R.id.tvYourGoal);
        ivButton4 = view.findViewById(R.id.ivButton4);
        ivRoad4 = view.findViewById(R.id.ivRoad4);
        ivButton3 = view.findViewById(R.id.ivButton3);
        ivRoad3 = view.findViewById(R.id.ivRoad3);
        ivButton2 = view.findViewById(R.id.ivButton2);
        ivRoad2 = view.findViewById(R.id.ivRoad2);
        ivButton1 = view.findViewById(R.id.ivButton1);
        ivRoad1 = view.findViewById(R.id.ivRoad1);
        ivButtonStart = view.findViewById(R.id.ivButtonStart);
        tvTag1Title = view.findViewById(R.id.tvTag1Title);
        tvTag2Title = view.findViewById(R.id.tvTag2Title);
        tvTag3Title = view.findViewById(R.id.tvTag3Title);
        tvTag4Title = view.findViewById(R.id.tvTag4Title);
        tvAchievement = view.findViewById(R.id.tvAchievement);

        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);


    }

    private void setListeners() {
//        ivButtonStart.setOnClickListener(v -> {
//            setRoadMap();
//        });
    }


    private void setValues() {

    }


    private void submitMilestoneJourney() {
        if (ConnectionDetector.isNetworkAvailable(getContext())) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            MilestoneJourneyRequest request = new MilestoneJourneyRequest();
            request.setParticipant_login_id(userPref.getString("usertrno", ""));
            request.setTarget_volume("" + targetValue);
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .submitMilestoreJourney(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::milestoneJourneyResponse, this::milestoreTargetListError);
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void milestoreTargetListError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void milestoneJourneyResponse(MilestoneJourneyResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            this.response = response;
            setRoadMapDetails();
        } else {

            new GulfUnnatiDialog(getContext(), new GulfOilUtils().getUserType())
                    .setTitle(getContext().getString(R.string.str_error))
                    .setDescription(response.getError())
                    .setPosButtonText(getContext().getString(R.string.str_ok), null)
                    .show();
        }
    }

    private void setRoadMapDetails() {
        tvUIN.setText("UIN:" + response.data.getUid());
        tvYourGoal.setText("YOUR GOAL - " + response.data.getTotal_target() + " ltrs");
        tvAchievement.setText(response.data.getAchievement_label());
        tvTag1Title.setText(GulfOilUtils.fromHtml(response.data.get_25().getTitle()));
        tvTag2Title.setText(GulfOilUtils.fromHtml(response.data.get_50().getTitle()));
        tvTag3Title.setText(GulfOilUtils.fromHtml(response.data.get_75().getTitle()));
        tvTag4Title.setText(GulfOilUtils.fromHtml(response.data.get_100().getTitle()));


//        if (!TextUtils.isEmpty(response.data.getTotal_achievement()) && !TextUtils.isEmpty(response.data.getTotal_target())) {
//            double achievementValue = Double.parseDouble(response.data.getTotal_achievement());
//            double targetValue = Double.parseDouble(response.data.getTotal_target());
//            if (achievementValue > 0 && targetValue > 0) {
//                int percentage = (int) ((achievementValue / targetValue) * 100);
//                Log.e(TAG, "RESULT: TARGET =" + this.targetValue + "\nACHIEVEMENT =" + achievementValue + "\nPERCENTAGE =" + percentage);
//                setRoadMap(percentage);
//            } else {
//                Log.e(TAG, "RESULT: TARGET =" + this.targetValue + "\nACHIEVEMENT =" + achievementValue);
//            }
//        }

        if (!TextUtils.isEmpty(response.data.getAchievement_percenatage())) {
            Log.e(TAG, "RESULT: TARGET =" + response.data.getTotal_target() + "\nACHIEVEMENT =" + response.data.getAchievement_percenatage());
            double percentage = Double.parseDouble(response.data.getAchievement_percenatage());
            setRoadMap((int) percentage);
        }

    }


    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_mile_stone_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_mile_stone_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_mile_stone_club;
        else
            return R.layout.fragment_mile_stone_royal;
    }

    private void setRoadMap(int percentage) {

        if (percentage > 0) {
            ivRoad1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_5));
        }
        if (percentage > 5) {
            ivRoad1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_10));
        }
        if (percentage > 10) {
            ivRoad1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_15));
        }
        if (percentage > 15) {
            ivRoad1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_20));
        }
        if (percentage > 20) {
            ivRoad1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_25));
        }
        if (percentage >= 25) {
            ivButton1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.gift_card_right));
        }

        if (percentage > 25) {
            ivRoad2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_30));
        }
        if (percentage > 30) {
            ivRoad2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_35));
        }
        if (percentage > 35) {
            ivRoad2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_40));
        }
        if (percentage > 40) {
            ivRoad2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_45));
        }
        if (percentage > 45) {
            ivRoad2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_50));
        }
        if (percentage >= 50) {
            ivButton2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.gift_card_left));
        }

        if (percentage > 50) {
            ivRoad3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_55));
        }
        if (percentage > 55) {
            ivRoad3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_60));
        }
        if (percentage > 60) {
            ivRoad3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_65));
        }
        if (percentage > 65) {
            ivRoad3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_70));
        }
        if (percentage > 70) {
            ivRoad3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_75));
        }
        if (percentage >= 75) {
            ivButton3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.gift_card_right));
        }
        if (percentage > 75) {
            ivRoad4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_80));
        }
        if (percentage > 80) {
            ivRoad4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_85));
        }
        if (percentage > 85) {
            ivRoad4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_90));
        }
        if (percentage > 90) {
            ivRoad4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_95));
        }
        if (percentage > 95) {
            ivRoad4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.road_100));
        }

        if (percentage >= 100) {
            ivButton4.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.gift_card_left));
        }
    }

}