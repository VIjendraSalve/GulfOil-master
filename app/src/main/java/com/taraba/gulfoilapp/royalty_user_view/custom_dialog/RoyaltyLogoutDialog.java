package com.taraba.gulfoilapp.royalty_user_view.custom_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.taraba.gulfoilapp.R;

/**
 * Created by taraba on 3/23/16.
 */
public class RoyaltyLogoutDialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public RoyaltyLogoutDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.royalty_logout_dialog);
        yes = (Button) findViewById(R.id.btn_Yes);
        no = (Button) findViewById(R.id.btn_No);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Yes:
                SharedPreferences preferences = getContext().getSharedPreferences(
                        "userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
                //	Log.e("user id ", "User Id : "+""+preferences.getString("usertrno", ""));
                edit.putString("username", "");
                edit.putString("userimage", "");
                edit.putString("usertrno", "");
                edit.putString("status", "");
                //	edit.putString("logout", "Logout");
                edit.commit();

                dismiss();
                //c.finish();
                c.finishAffinity();
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