package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.AddCodeFragment;
import com.taraba.gulfoilapp.ClaimHistoryFragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.contentproviders.UserModel;

import java.util.List;

/**
 * Created by android3 on 1/29/16.
 */
public class ParticipantListAdapter extends ArrayAdapter<UserModel> {

    private final List<UserModel> values;
    private final Context context;

    public ParticipantListAdapter(Context context, List<UserModel> values) {
        super(context, R.layout.participant_list, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public int getPosition(UserModel item) {
        return super.getPosition(item);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.participant_list, parent,
                    false);
        }
        Button btnAccuCode, btnClaimHistory;

        btnAccuCode = (Button) convertView
                .findViewById(R.id.btnAccuCodeParticipant);

        btnClaimHistory = (Button) convertView
                .findViewById(R.id.btnClaimHistoryParticipant);

        TextView txtName = (TextView) convertView
                .findViewById(R.id.lblName);
        TextView txtMobNo = (TextView) convertView
                .findViewById(R.id.lblmobnoparticipant);
        TextView txtWorkshopName = (TextView) convertView
                .findViewById(R.id.lblworkshopnameparticipant);

        txtName.setText("Name: " + values.get(position).getUserfname() + " " + values.get(position).getUserlname());
        txtMobNo.setText("Mobile: " + values.get(position).getMobile_no());
        txtWorkshopName.setText("Workshop: " + values.get(position).getWorkshopname());




       /* btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =((Activity) context).getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("Mechanic_trno_to_Profile", values.get(position).getId());
                edit.putString("Mechanic_status", "profile");
                edit.commit();
                Fragment mechanicRegistrationFragment = new MechanicRegistrationFragment();

                FragmentTransaction ftmech = ((Activity) context).getFragmentManager().beginTransaction();
                ftmech.replace(R.id.container_body, mechanicRegistrationFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ftmech.addToBackStack(null);
                ftmech.commit();
               *//* Bundle bundle = new Bundle();
                bundle.putString("msg", "profile");
                mechanicRegistrationFragment.setArguments(bundle);*//*
            }
        });*/

        btnAccuCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment addCodeFragment = new AddCodeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("participant_login_id", values.get(position).getId());
                bundle.putString("participant_code", values.get(position).getParticipant_code());
                Log.e("part id :", "Part id in adapter : " + values.get(position).getId());
                addCodeFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                ft1.replace(R.id.container_body, addCodeFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.addToBackStack(null);
                ft1.commit();
            }
        });

        btnClaimHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment claimhitoryFragment = new ClaimHistoryFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("participant_login_id", values.get(position).getId());
                bundle.putString("participant_code", values.get(position).getParticipant_code());
                Log.e("part id :", "Part id in adapter : " + values.get(position).getId());
                claimhitoryFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                ft1.replace(R.id.container_body, claimhitoryFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.addToBackStack(null);
                ft1.commit();
            }
        });
        return convertView;

    }
}