package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by android3 on 4/16/16.
 * Modified by Mansi
 */
public class AboutFragment extends Fragment {

    TextView txtContactUs, contact_us_doc;
    LinearLayout lin_tool_bar_conatiner;
    String noteForRetailer = "OR,  you can contact your Gulf Sales representative.";

    private String HTML = "";
    private static final String TEL_PREFIX = "tel:";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_us, container, false);
        // lin_tool_bar_conatiner = (LinearLayout) view.findViewById(R.id.lin_tool_bar_conatiner);
        txtContactUs = (TextView) view.findViewById(R.id.txt_contact_us_text);

        txtContactUs.setVisibility(View.GONE);

        SharedPreferences preference10 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String type = preference10.getString("user_type", "");

        if (type.equals("Retail Outlet")) {
            HTML = "<!DOCTYPE html><html><body>Please contact our  Toll free helpline number <a href='tel:18002094470'>18002094470</a> during 9:00AM to 6:00 PM(Monday - Saturday). Call Center will be closed on Public Holidays. <br><br> " + noteForRetailer + "</body></html>";
        } else {
            HTML = "<!DOCTYPE html><html><body>Please contact our  Toll free helpline number <a href='tel:18002094470'>18002094470</a> during 9:00AM to 6:00 PM(Monday - Saturday). Call Center will be closed on Public Holidays. </body></html>";
        }

        //view.setBackgroundColor(Color.WHITE);
        // lin_tool_bar_conatiner.setVisibility(View.VISIBLE);

        contact_us_doc = (TextView) view.findViewById(R.id.contact_us_doc);
        String text = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        WebView v = new WebView(getActivity());
        v.setVerticalScrollBarEnabled(true);

        ((LinearLayout) view.findViewById(R.id.bText)).addView(v);
        // v.loadData(String.format(text, getResources().getString(R.string.contact_us1)), "text/html", "utf-8");


        v.setWebViewClient(new CustomWebViewClient());
        v.loadData(HTML, "text/html", "utf-8");

        return view;
    }

    class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if (url.startsWith(TEL_PREFIX)) {
               /* Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);*/
                CustomYesNoDialog cd = new CustomYesNoDialog(getActivity());
                cd.show();
                return true;
            }
            return false;
        }
    }

    class CustomYesNoDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        TextView txtMsg;

        public CustomYesNoDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_logout_dialog);
            txtMsg = (TextView) findViewById(R.id.txt_msg);
            txtMsg.setText("Do you really want to call?");
            yes = (Button) findViewById(R.id.btn_Yes);
            no = (Button) findViewById(R.id.btn_No);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Yes:
                    dismiss();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:18002094470"));
                    startActivity(callIntent);
                    break;
                case R.id.btn_No:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}
