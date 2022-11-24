package com.taraba.gulfoilapp.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogHelper {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String title) {
        hideProgressDailog();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDailog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }

    }
}
