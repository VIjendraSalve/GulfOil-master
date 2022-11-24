package com.taraba.gulfoilapp.ui.more;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.KnowledgeCornerActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.UCActivity;
import com.taraba.gulfoilapp.adapter.DashboardMenuAdapter;
import com.taraba.gulfoilapp.adapter.DividerItemDecorator;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.DashboardMenuModel;
import com.taraba.gulfoilapp.model.MenuTag;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

import static com.taraba.gulfoilapp.ui.more.MoreFragmentDirections.ActionNavigationMoreToRewardsFragment;

public class MoreFragment extends Fragment implements RecyclerViewOnClickListener {
    private TextView tvUIN;
    private RecyclerView rvMoreMenu;
    private DashboardMenuAdapter menuAdapter;
    private SharedPreferences userPref;
    private List<DashboardMenuModel> menuList;

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

    private void setMenuAdapter() {
        menuAdapter = new DashboardMenuAdapter(getActivity(), menuList);
        menuAdapter.setOnClickListener(this);
        rvMoreMenu.setAdapter(menuAdapter);
    }

    private void init(View root) {
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        menuList = new DataProvider().getMoreMenuList();
        tvUIN = root.findViewById(R.id.tvUIN);
        rvMoreMenu = root.findViewById(R.id.rvMoreMenu);

        DividerItemDecorator itemDecorator = new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.divider), LinearLayout.VERTICAL);
        rvMoreMenu.addItemDecoration(itemDecorator);

        tvUIN.setText("UIN:" + userPref.getString("username", ""));
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        switch (menuList.get(position).getMenuTag()) {
            case MenuTag.UNNATI_CONNECT:
                startActivity(new Intent(getActivity(), UCActivity.class));
                break;
            case MenuTag.REWARDS:
                navigateToRewardsFragment(v);
                break;
            case MenuTag.POINTS_CALCULATOR:
//                comingSoonDialog();
                Navigation.findNavController(v).navigate(R.id.pointCalculatorWebViewFragment);
                break;
            case MenuTag.KNOWLEDGE_CORNER:
//                comingSoonDialog();
                startActivity(new Intent(getActivity(), KnowledgeCornerActivity.class));
                break;
        }
    }

    private void navigateToRewardsFragment(View v) {
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String login_id = preferences1.getString("usertrno", "");
        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

        SharedPreferences pref = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit1 = pref.edit();
        int lid = Integer.parseInt("" + login_id);
        edit1.putInt("Mechanic_trno_to_redeem", lid);
        edit1.putString("Mechanic_status", "rewards");
        edit1.commit();
        ActionNavigationMoreToRewardsFragment rewardsAction = MoreFragmentDirections.actionNavigationMoreToRewardsFragment();
        rewardsAction.setIsDisable("false");
        rewardsAction.setPath("d");
        Navigation.findNavController(v).navigate(R.id.rewardsFragment);
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_more_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_more_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_more_club;
        else
            return R.layout.fragment_more_royal;
    }

    private void comingSoonDialog() {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                .setDescription("Coming soon...")
                .setPosButtonText(getString(R.string.str_ok), null)
                .show();
    }


}