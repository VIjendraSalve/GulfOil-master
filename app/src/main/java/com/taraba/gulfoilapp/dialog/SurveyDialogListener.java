package com.taraba.gulfoilapp.dialog;

import com.taraba.gulfoilapp.model.AppSurveyResponse;

public interface SurveyDialogListener {
    void closeDialog();

    void openSurveyQuestionDialog(AppSurveyResponse appSurveyResponse);

    void openSurveyThankYouDialog(AppSurveyResponse appSurveyResponse);
}
