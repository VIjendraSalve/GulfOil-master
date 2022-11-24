package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Mansi on 4/16/16.
 * Modified by Mansi
 */
public class HelpFragment extends Fragment {

    TextView txtContactUs, contact_us_doc;
    LinearLayout lin_tool_bar_conatiner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_us, container, false);
        // lin_tool_bar_conatiner = (LinearLayout) view.findViewById(R.id.lin_tool_bar_conatiner);
        txtContactUs = (TextView) view.findViewById(R.id.txt_contact_us_text);
        // view.setBackgroundColor(Color.WHITE);
        txtContactUs.setVisibility(View.GONE);
        //   lin_tool_bar_conatiner.setVisibility(View.VISIBLE);

        contact_us_doc = (TextView) view.findViewById(R.id.contact_us_doc);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle("Help");
        }

        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        WebView v = new WebView(getActivity());
        v.setVerticalScrollBarEnabled(true);

        ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
        v.loadData(String.format(text, getResources().getString(R.string.help_doc)), "text/html", "utf-8");

        return view;
    }
}

