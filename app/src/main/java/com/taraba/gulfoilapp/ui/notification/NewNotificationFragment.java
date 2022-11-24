package com.taraba.gulfoilapp.ui.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.NewNotificationAdapter;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.NewNotification;
import com.taraba.gulfoilapp.model.YDRRequest;
import com.taraba.gulfoilapp.model.YDRResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.sql.SQLException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewNotificationFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "NewNotificationFragment";
    private RecyclerView rvNotification;
    private Disposable disposable;
    private SharedPreferences userPref;
    private List<NewNotification> notificationList;
    private ProgressDialog progressDialog;
    private FrameLayout fl_container;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_notification, container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_notification));
        } else if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_notification));
        }
        init(view);
        getNotificationListFromLocalDB();
        setAdapter();
        return view;
    }


    private void init(View view) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        rvNotification = view.findViewById(R.id.rvNotification);
        fl_container = view.findViewById(R.id.fl_container);

        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            fl_container.setBackground(getActivity().getResources().getDrawable(R.drawable.royal_mandala_art_background));
        } else if (userType == UserType.ELITE) {
            fl_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        } else if (userType == UserType.CLUB) {
            fl_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        } else {
            fl_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        }

    }

    private void setAdapter() {
        NewNotificationAdapter adapter = new NewNotificationAdapter(getActivity(), notificationList);
        adapter.setOnClickListener(this);
        rvNotification.setAdapter(adapter);
    }


    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARG_PARAM_NOTIFICATION_OBJ", notificationList.get(position));
        Navigation.findNavController(getView()).navigate(R.id.newNotificationDetailsFragment, bundle);
    }

    private void getNotificationListFromLocalDB() {
        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String uname = preferences1.getString("uname", "");
        notificationList = ctdUser.getNewNotificationByUserId("" + uname);
        ctdUser.updateNotification(uname);
        ctdUser.close();

        if (notificationList == null || notificationList.size() == 0) {
            new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                    .setTitle("")
                    .hideDialogCloseButton(true)
                    .setDescription("No Record Found.")
                    .setPosButtonText(getString(R.string.button_ok), dialog -> {
                        dialog.dismiss();
                        Navigation.findNavController(getView()).popBackStack();

                    })
                    .show();
        }
    }

    private void callYDRAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            Log.e(TAG, "callYDRAPI: Unnati Fragment");
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