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
import com.taraba.gulfoilapp.ui.unnati_connect.brochure.UCBrochureFragment;
import com.taraba.gulfoilapp.ui.unnati_connect.scheme_letter.UCSchemeLetterFragment;
import com.taraba.gulfoilapp.ui.unnati_connect.whats_new.UCWhatsNewFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class UCActivity extends AppCompatActivity implements View.OnClickListener, ViewStub.OnInflateListener {
    public static final String UNNATILIST = "unnatiList";
    public static final String CAMPAGINLIST = "campaginList";
    public static final String MILESTONELIST = "milestoneList";
    private ViewStub toolbar;
    private TextView tvToolbarTitle;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter adapter;
    private String tab;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());
        init();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_unnati_connect));
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
                .add("Whats New", UCWhatsNewFragment.class)
                .add("Scheme Letter", UCSchemeLetterFragment.class)
                .add("Brochure", UCBrochureFragment.class)
                .create());


        viewPager.setAdapter(adapter);


        viewPagerTab.setViewPager(viewPager);

        if (!TextUtils.isEmpty(tab)) {
            if (tab.equals("scheme_letter"))
                viewPager.setCurrentItem(1, true);
            else if (tab.equals("brochure"))
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
            return R.layout.fragment_unnati_connect_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_unnati_connect_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_unnati_connect_club;
        else
            return R.layout.fragment_unnati_connect_fls;
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
            this.toolbar.setLayoutResource(R.layout.tool_bar_fls);
            this.toolbar.inflate();
        }
    }


}