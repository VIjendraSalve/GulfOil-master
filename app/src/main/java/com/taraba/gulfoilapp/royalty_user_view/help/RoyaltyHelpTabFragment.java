package com.taraba.gulfoilapp.royalty_user_view.help;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.royalty_user_view.help.about.RoyaltyAboutFragment;
import com.taraba.gulfoilapp.royalty_user_view.help.broucher.RoyatlyProgramBroucherFragment;
import com.taraba.gulfoilapp.royalty_user_view.help.faq.RoyaltyFAQFragment;
import com.taraba.gulfoilapp.royalty_user_view.help.term_condition.RoyaltyTermsConditionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 4/16/16.
 */
public class RoyaltyHelpTabFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.royalty_tab_layout, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        setupTabLayout();
        int betweenSpace = 10;

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);

//        for (int i=0; i<slidingTabStrip.getChildCount()-1; i++) {
//            View v = slidingTabStrip.getChildAt(i);
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//            params.rightMargin = betweenSpace;
//        }
        /*for(int i=0; i < tabLayout.getTabCount(); i++)
        {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(10, 3, 60, 0);
            tab.requestLayout();


         }*/
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // adapter.addFragment(new HelpFragment(), "Help");
        adapter.addFragment(new RoyaltyFAQFragment(), "FAQ's");
        adapter.addFragment(new RoyaltyTermsConditionFragment(), "Terms & Conditions");
        adapter.addFragment(new RoyaltyAboutFragment(), "Contact Us");
        adapter.addFragment(new RoyatlyProgramBroucherFragment(), "Program Brochure");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {

        TextView customTab1 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab, null);
        TextView customTab2 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab, null);
        TextView customTab3 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab, null);
        TextView customTab4 = (TextView) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_tab, null);

        customTab1.setText("FAQ's");
        customTab1.setTextColor(Color.parseColor("#ba8e57"));
        customTab2.setText("Terms & Conditions");
        customTab2.setTextColor(Color.parseColor("#ba8e57"));
        customTab3.setText("Contact Us");
        customTab3.setTextColor(Color.parseColor("#ba8e57"));
        customTab4.setText("Program Brochure");
        customTab4.setTextColor(Color.parseColor("#ba8e57"));

//        customTab1.setBackgroundColor(Color.parseColor("#cd3301"));
//        customTab2.setBackgroundColor(Color.parseColor("#ff3300"));
//        customTab3.setBackgroundColor(Color.parseColor("#ff6634"));
//        customTab4.setBackgroundColor(Color.parseColor("#ff9a66"));

        customTab1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab4.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        tabLayout.getTabAt(0).setCustomView(customTab1);
        tabLayout.getTabAt(1).setCustomView(customTab2);
        tabLayout.getTabAt(2).setCustomView(customTab3);
        tabLayout.getTabAt(3).setCustomView(customTab4);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}