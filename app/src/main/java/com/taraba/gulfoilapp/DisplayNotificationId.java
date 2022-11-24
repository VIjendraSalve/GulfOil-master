package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Dell on 6/21/2016.
 */
public class DisplayNotificationId extends Fragment {

    TextView txtId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_notificationid, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("Notification", Context.MODE_PRIVATE);

        String id = preferences.getString("NotificationId", "");

        txtId = (TextView) view.findViewById(R.id.txtId);

        txtId.setText("Notification Id: " + id);

        return view;
    }
}
