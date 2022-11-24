package com.taraba.gulfoilapp.ui.your_digital_rewards.campagin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.adapter.YDRCampaignAdapter;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.YDRRequest;
import com.taraba.gulfoilapp.model.YDRResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.royalty_user_view.proceed_order.ProceedDigitalRewardsOTPActivity;
import com.taraba.gulfoilapp.util.ConnectionDetector;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class YDRCampaginFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "YDRCampaginFragment";
    private List<YDRResponse.Data.Campagin> campaignList;
    private RecyclerView rvCampaginList;
    private SharedPreferences userPref;
    private ProgressDialog progressDialog;
    private Disposable disposable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_y_d_r_campagin, container, false);
        init(view);
        callYDRAPI();
        return view;
    }

    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        rvCampaginList = view.findViewById(R.id.rvCampaginList);
    }

    private void setAdapter() {
        YDRCampaignAdapter adapter = new YDRCampaignAdapter(getActivity(), campaignList);
        adapter.setOnClickListener(this);
        rvCampaginList.setAdapter(adapter);
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), ProceedDigitalRewardsOTPActivity.class);
        intent.putExtra("orderDetailId", campaignList.get(position).getOrder_detail_id());
        startActivity(intent);
    }

    private void callYDRAPI() {
        Log.e(TAG, "callYDRAPI: Campaign Fragment");
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            YDRRequest request = new YDRRequest();
            request.setParticipant_login_id(userPref.getString("usertrno", ""));
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getYDRData(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::ydrPIResponse, this::ydrAPIError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void ydrAPIError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void ydrPIResponse(YDRResponse ydrResponse) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(ydrResponse.getStatus())) {
            campaignList = ydrResponse.getData().getCampaignList();
            setAdapter();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(ydrResponse.getError())
                    .setPosButtonText(getString(R.string.str_ok), null)
                    .show();
        }

    }
}