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
 * Created by android3 on 2/16/16.
 * Modified by Mansi
 */
public class ContactUsFragment extends Fragment {

    TextView txt_contact_us_text, contact_us_doc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_contact_us, container, false);

        Bundle bundle = getArguments();
        String action = bundle.getString("action");

        txt_contact_us_text = (TextView) view.findViewById(R.id.txt_contact_us_text);
        txt_contact_us_text.setText("" + action);
        contact_us_doc = (TextView) view.findViewById(R.id.contact_us_doc);

        // WebView textDesc = (WebView) view.findViewById(R.id.txtDescription);


        if (action.equals(MyTrackConstants._mStringFAQ)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.faqs_data)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);
            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.faqs_data)), "text/html", "utf-8");
        } else if (action.equals(MyTrackConstants._mStringTermsConditions)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.terms_condition)));


            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.terms_condition)), "text/html", "utf-8");

        } else if (action.equals(MyTrackConstants._mStringAboutUs)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.about_program)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.about_program)), "text/html", "utf-8");

        } else if (action.equals(MyTrackConstants._mStringHelp)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.help_doc)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.help_doc)), "text/html", "utf-8");
        } else if (action.equals(MyTrackConstants._mStringRewards)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.commimng_soon)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.commimng_soon)), "text/html", "utf-8");
        } else if (action.equals(MyTrackConstants._mStringTransactions)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.commimng_soon)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.commimng_soon)), "text/html", "utf-8");
        } else if (action.equals(MyTrackConstants._mStringNotification)) {
            //contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.commimng_soon)));
            String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
            WebView v = new WebView(getActivity());
            v.setVerticalScrollBarEnabled(true);

            ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
            v.loadData(String.format(text, getResources().getString(R.string.commimng_soon)), "text/html", "utf-8");
        }
        return view;
    }
}