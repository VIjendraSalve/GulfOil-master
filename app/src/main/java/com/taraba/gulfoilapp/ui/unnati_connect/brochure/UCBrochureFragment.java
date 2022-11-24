package com.taraba.gulfoilapp.ui.unnati_connect.brochure;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.NewBrochureAdapter;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.UnnatiConnectRequest;
import com.taraba.gulfoilapp.model.UnnatiConnectResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class UCBrochureFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "UCSchemeLetterFragment";
    private RecyclerView rvBrochure;
    private Disposable disposable;
    private SharedPreferences userPref;
    private List<UnnatiConnectResponse.Brochure> brochureList;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unnati_connect_brochure, container, false);
        init(view);
        callUnnatiConnectAPI();
        return view;
    }


    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        rvBrochure = view.findViewById(R.id.rvBrochure);

    }

    private void setAdapter() {
        NewBrochureAdapter adapter = new NewBrochureAdapter(getActivity(), brochureList);
        adapter.setOnClickListener(this);
        rvBrochure.setAdapter(adapter);
    }


    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        if (!TextUtils.isEmpty(brochureList.get(position).getInner_image()))
            intent.putExtra("IMG_URL", brochureList.get(position).getInner_image());
        else
            intent.putExtra("IMG_URL", brochureList.get(position).getOuter_image());

        intent.putExtra(
                "Title", "Unnati Magic Brochure");
        intent.putExtra("brochurePdfURL", brochureList.get(position).getBrochure_pdf());
        intent.putExtra("isBrochure", true);

        startActivity(intent);
    }

    private void callUnnatiConnectAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            Log.e(TAG, "callUnnatiConnectAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            UnnatiConnectRequest request = new UnnatiConnectRequest();
            request.setLogin_id(userPref.getString("usertrno", ""));
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getUnnatiConnect(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::unnatiConnectResponse, this::unnatiConnectError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void unnatiConnectError(Throwable throwable) {
        progressDialog.dismiss();
        new GulfUnnatiDialog(getActivity())
                .setTitle(getString(R.string.str_error))
                .hideDialogCloseButton(true)
                .setDescription(getString(R.string.something_went_wrong))
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    getActivity().finish();
                })
                .show();
    }

    private void unnatiConnectResponse(UnnatiConnectResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            brochureList = response.getData().getBrochure();
            setAdapter();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .hideDialogCloseButton(true)
                    .setDescription(response.getError())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        dialog.dismiss();
                        getActivity().finish();
                    })
                    .show();
        }

    }
}