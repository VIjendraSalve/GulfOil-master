package com.taraba.gulfoilapp.ui.knowledge_corner.webinar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.KnowledgeCornerWebinarAdapter;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.KnowledgeCornerRequest;
import com.taraba.gulfoilapp.model.KnowledgeCornerResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class KnowledgeCornerWebinarFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "KnowledgeCornerWebinarF";
    private RecyclerView rvWebinarList;
    private Disposable disposable;
    private SharedPreferences userPref;
    private List<KnowledgeCornerResponse.Data.Webinar> webinarList;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knowledge_corner_webinar, container, false);
        init(view);
        callKnowledgeCornerAPI();
        return view;
    }


    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        rvWebinarList = view.findViewById(R.id.rvWebinarList);

    }

    private void setAdapter() {
        KnowledgeCornerWebinarAdapter adapter = new KnowledgeCornerWebinarAdapter(getActivity(), webinarList);
        adapter.setOnClickListener(this);
        rvWebinarList.setAdapter(adapter);
    }


    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        if (v.getId() == R.id.tv_link_value) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webinarList.get(position).getWebinar_link())));
        }
    }

    private void callKnowledgeCornerAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            KnowledgeCornerRequest request = new KnowledgeCornerRequest();
            request.setLogin_id(userPref.getString("usertrno", ""));
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getKnowledgeCorner(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::knowledgeCornerResponse, this::knowledgeCornerError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void knowledgeCornerError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void knowledgeCornerResponse(KnowledgeCornerResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            webinarList = response.getData().getWebinar();
            setAdapter();
        } else {
            new GulfUnnatiDialog(getActivity())
                    .setTitle(getString(R.string.str_error))
                    .setDescription(response.getError())
                    .setPosButtonText(getString(R.string.str_ok), null)
                    .show();
        }

    }
}