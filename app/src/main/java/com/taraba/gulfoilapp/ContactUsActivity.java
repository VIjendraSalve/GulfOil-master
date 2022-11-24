package com.taraba.gulfoilapp;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by android3 on 2/16/16.
 * Modified by Mansi
 */
public class ContactUsActivity extends AppCompatActivity {

    TextView txtContactUs, contact_us_doc;
    LinearLayout lin_tool_bar_conatiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //  lin_tool_bar_conatiner = (LinearLayout) findViewById(R.id.lin_tool_bar_conatiner);
        //txtContactUs = (TextView) findViewById(R.id.txt_contact_us_text);

        //  lin_tool_bar_conatiner.setVisibility(View.VISIBLE);

        contact_us_doc = (TextView) findViewById(R.id.contact_us_doc);
        txtContactUs.setText("" + getIntent().getStringExtra("action"));

        contact_us_doc.setText(Html.fromHtml(getResources().getString(R.string.contact_us)));
    }
}
