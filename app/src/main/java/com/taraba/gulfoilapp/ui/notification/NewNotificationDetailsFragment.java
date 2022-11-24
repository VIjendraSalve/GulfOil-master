package com.taraba.gulfoilapp.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.NewNotification;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewNotificationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewNotificationDetailsFragment extends Fragment {

    private static final String ARG_PARAM_NOTIFICATION_OBJ = "ARG_PARAM_NOTIFICATION_OBJ";
    private TextView tvDateTime;
    private TextView tvTitle;
    private TextView tvDesc;
    private ImageView ivImage;
    private ScrollView sv_container;
    // TODO: Rename and change types of parameters
    private NewNotification mNotification;

    public NewNotificationDetailsFragment() {
        // Required empty public constructor
    }

    public static NewNotificationDetailsFragment newInstance(NewNotification notification) {
        NewNotificationDetailsFragment fragment = new NewNotificationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_NOTIFICATION_OBJ, notification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = (NewNotification) getArguments().getSerializable(ARG_PARAM_NOTIFICATION_OBJ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_notification_details, container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_notification_detail));
        } else if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_notification_detail));
        }
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void setData() {
        tvDateTime.setText(mNotification.getMdate());
        tvTitle.setText(mNotification.getTitle());
        tvDesc.setText(mNotification.getDescription());

        Glide.with(this).load(mNotification.getImage())
                .centerCrop()
                .into(ivImage);
    }

    private void init(View view) {
        tvDateTime = view.findViewById(R.id.tvDateTime);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDesc = view.findViewById(R.id.tvDesc);
        ivImage = view.findViewById(R.id.ivImage);
        sv_container = view.findViewById(R.id.sv_container);
        ivImage.setOnClickListener(v -> {
            showFullScreenImage(mNotification.getImage(), "", false);
        });

        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            sv_container.setBackground(getActivity().getResources().getDrawable(R.drawable.royal_mandala_art_background));
        } else if (userType == UserType.ELITE) {
            sv_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        } else if (userType == UserType.CLUB) {
            sv_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        } else {
            sv_container.setBackground(getActivity().getResources().getDrawable(R.drawable.unnati_mandala_art_background));
        }
    }

    private void showFullScreenImage(String imgURL, String toolbarTitle, boolean isLocalImg) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        intent.putExtra("IMG_URL", imgURL);
        intent.putExtra("Title", toolbarTitle);
        intent.putExtra("isLocalImg", isLocalImg);
        startActivity(intent);
    }
}