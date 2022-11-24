package com.taraba.gulfoilapp.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.AppSurveyResponse;

public class GulfSurveyWelcomeDialog extends AppCompatDialog implements View.OnClickListener {
    private final UserType userType;
    public SurveyDialogListener surveyDialogListener;
    private AppSurveyResponse appSurveyResponse;
    private AppCompatButton btnOk;
    private ImageView ivDialogClose;
    private TextView tvDescriptionSecond;

    public GulfSurveyWelcomeDialog(Context context, UserType userType2, AppSurveyResponse appSurveyResponse2) {
        super(context);
        setCancelable(false);
        this.appSurveyResponse = appSurveyResponse2;
        this.userType = userType2;
        AppConfig.isAppSurveyDialogOpen = true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(getContentType());
        init();
        setListeners();
        setValues();
    }


    private int getContentType() {
        int layoutResource = 0;
        switch (userType) {
            case UNNATI:
                layoutResource = R.layout.dialog_survey_welcome_unnati;
                break;
            case ROYAL:
                layoutResource = R.layout.dialog_survey_welcome_royal;
                break;
            case ELITE:
                layoutResource = R.layout.dialog_survey_welcome_elite;
                break;
            case CLUB:
                layoutResource = R.layout.dialog_survey_welcome_club;
                break;
        }
        return layoutResource;
    }

    private void init() {
        this.tvDescriptionSecond = (TextView) findViewById(R.id.tvDescriptionSecond);
        this.btnOk = (AppCompatButton) findViewById(R.id.btnOk);
        this.ivDialogClose = (ImageView) findViewById(R.id.ivDialogClose);
    }

    private void setValues() {
        if (!TextUtils.isEmpty(this.appSurveyResponse.getStartPage())) {
            this.tvDescriptionSecond.setText(this.appSurveyResponse.getStartPage());
        }
    }

    private void setListeners() {
        this.ivDialogClose.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnOk) {
            openAppSurveyQuestionDialog();
        } else if (id == R.id.ivDialogClose) {
            SurveyDialogListener surveyDialogListener2 = this.surveyDialogListener;
            if (surveyDialogListener2 != null) {
                surveyDialogListener2.closeDialog();
            }
            AppConfig.isAppSurveySessionActive = false;
            AppConfig.isAppSurveyDialogOpen = false;
            dismiss();
        }
    }

    private void openAppSurveyQuestionDialog() {
        SurveyDialogListener surveyDialogListener2 = this.surveyDialogListener;
        if (surveyDialogListener2 != null) {
            surveyDialogListener2.openSurveyQuestionDialog(this.appSurveyResponse);
        }
        dismiss();
    }

    public GulfSurveyWelcomeDialog setSurveyDialogListener(SurveyDialogListener surveyDialogListener2) {
        this.surveyDialogListener = surveyDialogListener2;
        return this;
    }
}
