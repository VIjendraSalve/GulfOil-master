package com.taraba.gulfoilapp.util;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewUtils {
    public static String getText(EditText editText) {
        if (editText == null)
            return "";

        return editText.getText().toString().trim();
    }

    public static String getText(Spinner spinner) {
        if (spinner == null)
            return "";

        return spinner.getSelectedItem().toString().trim();
    }

    public static String getText(TextView textView) {
        if (textView == null)
            return "";

        return textView.getText().toString().trim();
    }

    public static String getText(Button button) {
        if (button == null)
            return "";

        return button.getText().toString().trim();
    }
}
