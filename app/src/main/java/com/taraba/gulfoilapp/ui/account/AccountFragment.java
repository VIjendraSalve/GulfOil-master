package com.taraba.gulfoilapp.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.HelpActivity;
import com.taraba.gulfoilapp.LoginActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.YourDigitalRewardsActivity;
import com.taraba.gulfoilapp.adapter.DashboardMenuAdapter;
import com.taraba.gulfoilapp.adapter.DividerItemDecorator;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.DashboardMenuModel;
import com.taraba.gulfoilapp.model.MenuTag;
import com.taraba.gulfoilapp.ui.MyTDS.MyTDSCertificateActivity;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

public class AccountFragment extends Fragment implements RecyclerViewOnClickListener {
    private RecyclerView rvAccountMenu;
    private DashboardMenuAdapter menuAdapter;
    private SharedPreferences userPref;
    private List<DashboardMenuModel> menuList;
    private TextView tvUserName,
            tvShopName,
            tvAddress;
    private ImageView ivProfileImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(false);
        }
        init(root);
        setMenuAdapter();
        return root;
    }

    private void init(View root) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        tvUserName = root.findViewById(R.id.tvUserName);
        tvShopName = root.findViewById(R.id.tvShopName);
        tvAddress = root.findViewById(R.id.tvAddress);
        rvAccountMenu = root.findViewById(R.id.rvAccountMenu);
        ivProfileImage = root.findViewById(R.id.ivProfileImage);

        tvUserName.setText(userPref.getString("full_name", "Not Available"));
        tvShopName.setText("Shop Name: " + userPref.getString("shopname", "Not Available"));
        String address = userPref.getString("shopdistrict", "") + ", " + userPref.getString("shopstate", "");
        tvAddress.setText("" + address);
        Glide.with(this).load(userPref.getString("profile_image", "")).placeholder(R.drawable.ic_default_user_avatar).into(ivProfileImage);

        menuList = new DataProvider().getAccountMenuList();

        DividerItemDecorator itemDecorator = new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.divider), LinearLayout.VERTICAL);
        rvAccountMenu.addItemDecoration(itemDecorator);

    }

    private void setMenuAdapter() {
        menuAdapter = new DashboardMenuAdapter(getActivity(), menuList);
        menuAdapter.setOnClickListener(this);
        rvAccountMenu.setAdapter(menuAdapter);
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {

        switch (menuList.get(position).getMenuTag()) {
            case MenuTag.MY_PROFILE:
                Navigation.findNavController(v).navigate(R.id.participantProfileFragment);
                //comingSoonDialog();
                break;
            case MenuTag.POINTS:
                Navigation.findNavController(v).navigate(R.id.myPointsFragment);
                break;
            case MenuTag.YOUR_DIGITAL_REWARDS:
                //Navigation.findNavController(v).navūigate(R.id.yourDigitalRewardsFragment);
                startActivity(new Intent(getActivity(), YourDigitalRewardsActivity.class));
                break;
            case MenuTag.MyTDSCertificate:
                //Navigation.findNavController(v).navūigate(R.id.yourDigitalRewardsFragment);
                startActivity(new Intent(getActivity(), MyTDSCertificateActivity.class));
                break;
            case MenuTag.HELP:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case MenuTag.CHANGE_PASSWORD:
                Navigation.findNavController(v).navigate(R.id.newChangePasswordFragment);
                break;
            case MenuTag.SIGN_OUT:
                new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                        .setTitle(getString(R.string.str_confirmation))
                        .setDescription(getString(R.string.str_logout_msg))
                        .setPosButtonText(getString(R.string.str_yes), dialog -> {
                            logout();
                        }).setNegButtonText(getString(R.string.str_no), null)
                        .show();
                break;
        }
    }

    private void logout() {
        AppConfig.isSplashPopUpSessionActive = true;
        SharedPreferences preferences = getContext().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
        //	Log.e("user id ", "User Id : "+""+preferences.getString("usertrno", ""));
        edit.putString("username", "");
        edit.putString("userimage", "");
        edit.putString("usertrno", "");
        edit.putString("status", "");
        //	edit.putString("logout", "Logout");
        edit.commit();
        //c.finish();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_account_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_account_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_account_club;
        else
            return R.layout.fragment_account_royal;
    }

    private void comingSoonDialog() {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setDescription("Coming soon...")
                .setPosButtonText(getString(R.string.str_ok), null)
                .show();
    }
}