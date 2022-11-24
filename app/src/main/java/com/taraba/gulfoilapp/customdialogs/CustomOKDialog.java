package com.taraba.gulfoilapp.customdialogs;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;


public class CustomOKDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button ok;
    String msg, heading;
    TextView txtMsg, txtHeading;
    String type;
    FragmentManager fm;

    public CustomOKDialog(Context a, String msg, String heading) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.msg = msg;
        this.heading = "GULF UNNATI";
        this.type = type;
        this.fm = fm;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_login_invalid);
        ok = (Button) findViewById(R.id.btn_Ok);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        txtHeading = (TextView) findViewById(R.id.txtHeading);
        txtMsg.setText("" + msg);
        txtHeading.setText("" + heading);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Ok:
                /*if (type.equals("dismiss")) {*/
                dismiss();

                /*}
                else if (type.equals("process")) {
                    c.getFragmentManager().beginTransaction().remove().commit();
                    c.getFragmentManager().popBackStack();
                }*/
                break;
            default:
                break;
        }
        dismiss();
    }
}