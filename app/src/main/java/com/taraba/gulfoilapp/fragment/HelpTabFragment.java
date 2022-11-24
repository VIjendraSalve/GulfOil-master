package com.taraba.gulfoilapp.fragment;

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
import com.taraba.gulfoilapp.AboutFragment;
import com.taraba.gulfoilapp.FAQFragment;
import com.taraba.gulfoilapp.ProgramBroucherFragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.TermsConditionFragment;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 4/16/16.
 */
public class HelpTabFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_layout, container, false);
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
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // adapter.addFragment(new HelpFragment(), "Help");
        adapter.addFragment(new FAQFragment(), "FAQ's");
        adapter.addFragment(new TermsConditionFragment(), "Terms & Conditions");
        adapter.addFragment(new AboutFragment(), "Contact Us");
        adapter.addFragment(new ProgramBroucherFragment(), "Program Brochure");
        viewPager.setAdapter(adapter);
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
        customTab2.setText("Terms & Conditions");
        customTab3.setText("Contact Us");
        customTab4.setText("Program Brochure");

        customTab1.setBackgroundColor(Color.parseColor("#cd3301"));
        customTab2.setBackgroundColor(Color.parseColor("#ff3300"));
        customTab3.setBackgroundColor(Color.parseColor("#ff6634"));
        customTab4.setBackgroundColor(Color.parseColor("#ff9a66"));

        customTab1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        customTab4.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        tabLayout.getTabAt(0).setCustomView(customTab1);
        tabLayout.getTabAt(1).setCustomView(customTab2);
        tabLayout.getTabAt(2).setCustomView(customTab3);
        tabLayout.getTabAt(3).setCustomView(customTab4);

    }
}