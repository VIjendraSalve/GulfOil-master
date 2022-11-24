package com.taraba.gulfoilapp.royalty_user_view.app_demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.util.AppDemoConstant;

/**
 * Created by taraba on 5/15/17.
 */
public class RoyaltyAppdemoFragment extends Fragment {
    private String user_type;

    public RoyaltyAppdemoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_royalty_appdemo, container, false);

        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);


        user_type = preferences.getString("user_type", "");

        view.findViewById(R.id.btnAppDemoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();


            }
        });

        view.findViewById(R.id.btnEnglish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("fls")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.FLSLinkENGLISH)));
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkENGLISH)));

                }
            }
        });
        view.findViewById(R.id.btnHindi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("fls")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.FLSLinkHINDI)));

                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkHINDI)));

                }
            }
        });
        view.findViewById(R.id.btnTamil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("fls")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.FLSLinkTAMIL)));

                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkTAMIL)));

                }
            }
        });
        view.findViewById(R.id.btnTelgu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type.equals("fls")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.FLSLinkTELGU)));

                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppDemoConstant.RetailerLinkTELGU)));

                }
            }
        });
        return view;
    }
}
