package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;

public class MechTallyInvoiceUploadedfragment extends Fragment implements View.OnClickListener {
    Button btn_view_invoice_1;
    Button btn_view_invoice_2;
    Button btn_view_invoice_3;
    EditText edt_invoice_1;
    EditText edt_invoice_2;
    EditText edt_invoice_3;
    String invoice_1;
    String invoice_2;
    String invoice_3;
    View rootView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_mech_tally_invoice_uploaded_fragment, viewGroup, false);
        initView();
        return this.rootView;
    }

    public void initView() {
        Bundle arguments = getArguments();
        this.invoice_1 = arguments.getString("invoice_1");
        this.invoice_2 = arguments.getString("invoice_2", "empty");
        this.invoice_3 = arguments.getString("invoice_3", "empty");
        this.edt_invoice_1 = (EditText) this.rootView.findViewById(R.id.edt_invoice_1);
        this.edt_invoice_2 = (EditText) this.rootView.findViewById(R.id.edt_invoice_2);
        this.edt_invoice_3 = (EditText) this.rootView.findViewById(R.id.edt_invoice_3);
        try {
            EditText editText = this.edt_invoice_1;
            String str = this.invoice_1;
            editText.setText(str.substring(str.lastIndexOf("/")).replace("/", ""));
            if (!this.invoice_2.equals("empty")) {
                EditText editText2 = this.edt_invoice_2;
                String str2 = this.invoice_2;
                editText2.setText(str2.substring(str2.lastIndexOf("/")).replace("/", ""));
            }
            if (!this.invoice_3.equals("empty")) {
                EditText editText3 = this.edt_invoice_3;
                String str3 = this.invoice_3;
                editText3.setText(str3.substring(str3.lastIndexOf("/")).replace("/", ""));
            }
        } catch (Exception unused) {
            this.edt_invoice_1.setText("invoice_1");
            this.edt_invoice_2.setText("invoice_2");
            this.edt_invoice_3.setText("invoice_3");
        }
        this.btn_view_invoice_1 = (Button) this.rootView.findViewById(R.id.btn_view_invoice_1);
        this.btn_view_invoice_2 = (Button) this.rootView.findViewById(R.id.btn_view_invoice_2);
        this.btn_view_invoice_3 = (Button) this.rootView.findViewById(R.id.btn_view_invoice_3);
        this.btn_view_invoice_1.setOnClickListener(this);
        this.btn_view_invoice_2.setOnClickListener(this);
        this.btn_view_invoice_3.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view_invoice_1 /*2131296454*/:
                if (this.edt_invoice_1.getText().toString().contains("pdf")) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.invoice_1)));
                    return;
                } else {
                    showDialog(getActivity(), this.invoice_1);
                    return;
                }
            case R.id.btn_view_invoice_2 /*2131296455*/:
                if (this.invoice_2.equals("empty")) {
                    new CustomOKDialog(getActivity(), "2nd invoice is not available...", getActivity().getResources().getString(R.string.app_name)).show();
                    return;
                } else if (this.edt_invoice_2.getText().toString().contains("pdf")) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.invoice_2)));
                    return;
                } else {
                    showDialog(getActivity(), this.invoice_2);
                    return;
                }
            case R.id.btn_view_invoice_3 /*2131296456*/:
                if (this.invoice_2.equals("empty")) {
                    new CustomOKDialog(getActivity(), "3rd invoice is not available...", getActivity().getResources().getString(R.string.app_name)).show();
                    return;
                } else if (this.edt_invoice_3.getText().toString().contains("pdf")) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.invoice_3)));
                    return;
                } else {
                    showDialog(getActivity(), this.invoice_3);
                    return;
                }
            default:
                return;
        }
    }

    public void showDialog(Activity activity, String str) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_dialog);
        Picasso.with(getActivity()).load(str).placeholder(getResources().getDrawable(R.drawable.loading)).error(getResources().getDrawable(R.drawable.no_photo)).into((ImageView) dialog.findViewById(R.id.iv_tally_invoice_image));
        ((Button) dialog.findViewById(R.id.btn_ok)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return true;
                }
                dialog.dismiss();
                return true;
            }
        });
        dialog.show();
    }
}
