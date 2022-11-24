package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.adapter.LeaderBoardScoreDetailsAdapter;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.LeaderBoardRequest;
import com.taraba.gulfoilapp.model.LeaderBoardResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LeaderBoardFragment extends Fragment {
    private TextView tvHeading,
            tvSubHeading,
            tvRankValue,
            tvRankMessage,
            tvOtherRankTitle,
            tvRank1CongratulationsHeader,
            tvRank1CongratulationsMessage;


    private CardView cvContainer, cvRank1Container;
    private RecyclerView rvScoreDetails;


    private SharedPreferences userPref;
    private LeaderBoardResponse response;
    private LeaderBoardScoreDetailsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_leaderboard));
        }
        init(view);
        getLeaderBoardData();
        return view;
    }

    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        tvHeading = view.findViewById(R.id.tvHeading);
        tvSubHeading = view.findViewById(R.id.tvSubHeading);
        tvRankValue = view.findViewById(R.id.tvRankValue);
        tvRankMessage = view.findViewById(R.id.tvRankMessage);
        tvOtherRankTitle = view.findViewById(R.id.tvOtherRankTitle);
        cvContainer = view.findViewById(R.id.cvContainer);
        rvScoreDetails = view.findViewById(R.id.rvScoreDetails);
        cvRank1Container = view.findViewById(R.id.cvRank1Container);
        tvRank1CongratulationsHeader = view.findViewById(R.id.tvRank1CongratulationsHeader);
        tvRank1CongratulationsMessage = view.findViewById(R.id.tvRank1CongratulationsMessage);

    }


    private void getLeaderBoardData() {
        LeaderBoardRequest request = new LeaderBoardRequest(userPref.getString("usertrno", ""));
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait");
            ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getParticipantLeaderboard(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::leadboardAPIResponse, this::leadboardAPIError);

        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.str_internet_disconnected), Toast.LENGTH_LONG).show();
        }
    }


    private void leadboardAPIResponse(LeaderBoardResponse response) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            this.response = response;
            showLeaderBoardData();
        } else {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setTitle(getString(R.string.str_rank))
                    .setDescription(response.getError())
                    .hideDialogCloseButton(true)
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();
                    })
                    .show();
        }
    }


    private void leadboardAPIError(Throwable throwable) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setTitle(getString(R.string.str_rank))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).popBackStack();
                })
                .show();
    }

    private void showLeaderBoardData() {
        tvSubHeading.setText(response.getData().getRank_title());
        tvRankValue.setText(response.getData().getRank());
        tvRankMessage.setText(response.getData().getRank_message());

        if (response.getData().isScore_flag()) {
            cvContainer.setVisibility(View.VISIBLE);
            cvRank1Container.setVisibility(View.GONE);
            tvOtherRankTitle.setText(response.getData().getScore_detail().getScore_title());
            rvScoreDetails.setAdapter(new LeaderBoardScoreDetailsAdapter(getActivity(), response.getData().getScore_detail().getScore_data()));
        } else {
            cvContainer.setVisibility(View.GONE);
            cvRank1Container.setVisibility(View.VISIBLE);
            tvRank1CongratulationsHeader.setText(response.getData().getMessage_title());
            tvRank1CongratulationsMessage.setText(response.getData().getMessage());
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_leader_board_roayl;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_leader_board_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_leader_board_club;
        else
            return R.layout.fragment_leader_board_roayl;
    }
}