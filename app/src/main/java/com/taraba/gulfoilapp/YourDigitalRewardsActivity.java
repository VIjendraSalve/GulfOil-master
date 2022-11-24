package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.ui.your_digital_rewards.campagin.YDRCampaginFragment;
import com.taraba.gulfoilapp.ui.your_digital_rewards.milestone.YDRMilestoneFragment;
import com.taraba.gulfoilapp.ui.your_digital_rewards.unnati.YDRUnnatiFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class YourDigitalRewardsActivity extends AppCompatActivity implements View.OnClickListener, ViewStub.OnInflateListener {
    public static final String UNNATILIST = "unnatiList";
    public static final String CAMPAGINLIST = "campaginList";
    public static final String MILESTONELIST = "milestoneList";
    private ViewStub toolbar;
    private TextView tvToolbarTitle;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter adapter;
    private String tab;


//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(getLayoutUserWise(), container, false);
//        init(view);
//        if (getActivity() instanceof MainDashboardActivity) {
//            ((MainDashboardActivity) getActivity()).hideShowView(true);
//            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_your_digital_rewards));
//        }
//
//        return view;
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());
        init();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_your_digital_rewards));
        setUpPager();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnInflateListener(this);
        toolbar.setVisibility(View.GONE);
        viewPager = findViewById(R.id.viewpager);
        viewPagerTab = findViewById(R.id.viewpagertab);

        tab = getIntent().getStringExtra("tab");


    }


    private void setUpPager() {
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Unnati", YDRUnnatiFragment.class)
                .add("Milestone", YDRMilestoneFragment.class)
                .add("Campaign", YDRCampaginFragment.class)
                .create());


        viewPager.setAdapter(adapter);


        viewPagerTab.setViewPager(viewPager);
        if (!TextUtils.isEmpty(tab)) {
            if (tab.equals("milestone"))
                viewPager.setCurrentItem(1, true);
            else if (tab.equals("campaign"))
                viewPager.setCurrentItem(2, true);
        }
    }


    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }


    @Override
    public void onInflate(ViewStub stub, View inflated) {
        inflated.findViewById(R.id.ivBack).setOnClickListener(this);
        tvToolbarTitle = inflated.findViewById(R.id.tvToolbarTitle);
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_your_digital_rewards_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_your_digital_rewards_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_your_digital_rewards_club;
        else
            return R.layout.fragment_your_digital_rewards_royal;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private void setToolbarUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        } else if (userType == UserType.ELITE) {
            toolbar.setLayoutResource(R.layout.tool_bar_elite);
            toolbar.inflate();
        } else if (userType == UserType.CLUB) {
            toolbar.setLayoutResource(R.layout.tool_bar_club);
            toolbar.inflate();
        } else {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        }
    }


}