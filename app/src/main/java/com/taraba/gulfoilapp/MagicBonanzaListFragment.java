package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.adapter.MagicBonanzaListAdapter;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.MagicBonanzaListResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MagicBonanzaListFragment extends Fragment implements RecyclerViewOnClickListener {
    private RecyclerView rvMagicBonanzaList;
    private MagicBonanzaListAdapter adapter;
    private List<MagicBonanzaListResponse.Data> magicBonanzaList;
    private Disposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        callMagicBonanzaListAPI();
        return view;
    }

    private void init(View view) {
        rvMagicBonanzaList = view.findViewById(R.id.rvMagicBonanzaList);
    }

    private void callMagicBonanzaListAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait");

            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getMagicBoanazaList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::magicBonanzaListResponse, this::magicBonanzaListError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void magicBonanzaListError(Throwable throwable) {
        ProgressDialogHelper.hideProgressDailog();
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void magicBonanzaListResponse(MagicBonanzaListResponse response) {
        ProgressDialogHelper.hideProgressDailog();
        magicBonanzaList = response.getData();
        setAdapter();
    }


    private void setAdapter() {
        MagicBonanzaListAdapter adapter = new MagicBonanzaListAdapter(getActivity(), magicBonanzaList);
        adapter.setOnClickListener(this);
        rvMagicBonanzaList.setAdapter(adapter);
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tmc", magicBonanzaList.get(position));
        Navigation.findNavController(v).navigate(R.id.magicBonanzaDetailsFragment, bundle);
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_magic_bonanza_list_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_magic_bonanza_list_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_magic_bonanza_list_club;
        else
            return R.layout.fragment_magic_bonanza_list_royal;
    }
}