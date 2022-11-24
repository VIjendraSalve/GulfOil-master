package com.taraba.gulfoilapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Mansi on 6/9/2016.
 */
public class RoyaltyDisplayVersionDetails extends Fragment {

    TextView txtVersionName, txtVersionCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_royalty_version_details, container, false);

        txtVersionName = (TextView) view.findViewById(R.id.txtVersionName);
        txtVersionCode = (TextView) view.findViewById(R.id.txtVersionCode);


        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        int verCode = pInfo.versionCode;

        Log.e("Version details", "version name:" + version);
        Log.e("Version details", "version code:" + verCode);

        txtVersionName.setText("Version Name: " + version);
        txtVersionCode.setText("Version Code: " + verCode);
        /*view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });*/
        return view;
    }
}
