package com.taraba.gulfoilapp.ui.unnati_connect.whats_new;

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
import com.taraba.gulfoilapp.adapter.NewWhatsNewAdapter;
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


public class UCWhatsNewFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "UCWhatsNewFragment";
    private RecyclerView rvWhatsNew;
    private Disposable disposable;
    private SharedPreferences userPref;
    private List<UnnatiConnectResponse.WhatsNew> whatsNewList;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unnati_connect_whats_new, container, false);
        init(view);
        callUnnatiConnectAPI();
        return view;
    }


    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        rvWhatsNew = view.findViewById(R.id.rvWhatsNew);

    }

    private void setAdapter() {
        NewWhatsNewAdapter adapter = new NewWhatsNewAdapter(getActivity(), whatsNewList);
        adapter.setOnClickListener(this);
        rvWhatsNew.setAdapter(adapter);
    }


    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        intent.putExtra("IMG_URL", whatsNewList.get(position).getInner_image());
        intent.putExtra(
                "Title", whatsNewList.get(position).getName());
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
            whatsNewList = response.getData().getWhats_new();
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