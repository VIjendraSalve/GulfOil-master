package com.taraba.gulfoilapp.royalty_user_view.help.faq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;

//import com.taraba.gulfoilapp.crop.Log;

/**
 * Created by android3 on 4/16/16.
 * Modified by Mansi
 */
public class RoyaltyFAQFragment extends Fragment {

    TextView txtContactUs, contact_us_doc;
    LinearLayout lin_tool_bar_conatiner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_royalty_faq_contact_us, container, false);
        // lin_tool_bar_conatiner = (LinearLayout) view.findViewById(R.id.lin_tool_bar_conatiner);
        txtContactUs = (TextView) view.findViewById(R.id.txt_contact_us_text);
        //view.setBackgroundColor(Color.WHITE);
        txtContactUs.setVisibility(View.GONE);
        //lin_tool_bar_conatiner.setVisibility(View.VISIBLE);

        contact_us_doc = (TextView) view.findViewById(R.id.contact_us_doc);
        String text = "<html><body style=\"text-align:justify;color: #ba8e57; background-color: #000;\"> %s </body></Html>";
        WebView v = new WebView(getActivity());
        v.setVerticalScrollBarEnabled(true);
        ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
        SharedPreferences preferences = getActivity().getSharedPreferences("signupdetails", Context.MODE_PRIVATE);
        String type = preferences.getString("user_type", "");
        android.util.Log.e("user_type : ", "onCreateView: " + type);

        v.loadData(String.format(text, getResources().getString(R.string.gulf_fls_faqs_data)), "text/html", "utf-8");
        return view;
    }
}

