package com.taraba.gulfoilapp.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class GulfUnnatiDialog extends AppCompatDialog implements View.OnClickListener {
    private String title, description, posButtonText, negButtonText;
    private UserType userType;
    private OnPositiveClickListener onPositiveClickListener;
    private OnNegetiveClickListener onNegetiveClickListener;
    private AppCompatButton btnCancel, btnOk;
    private TextView tvDialogHeader, tvDescription;
    private ImageView ivDialogClose;
    private boolean hideDialogClose;

    public GulfUnnatiDialog(Context context) {
        this(context, UserType.UNNATI);
    }

    public GulfUnnatiDialog(Context context, UserType userType) {
        super(context);
        setCancelable(false);
        this.userType = userType;
        Log.d("userType", "GulfUnnatiDialog: "+userType);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(getContentType());
        init();
        setListeners();
        showHideButtons();
    }

    private int getContentType() {
        int contentType = 0;
        switch (userType) {
            case UNNATI:
                contentType = R.layout.dialog_unnati;
                break;
            case ROYAL:
                contentType = R.layout.dialog_royal;
                break;
            case ELITE:
                contentType = R.layout.dialog_elite;
                break;
            case CLUB:
                contentType = R.layout.dialog_club;
                break;
            case SIMPLE:
                contentType = R.layout.dialog_reason_for_revalidate;
                break;
        }
        return contentType;
    }

    private void init() {
        tvDialogHeader = findViewById(R.id.tvDialogHeader);
        tvDescription = findViewById(R.id.tvDescription);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);
        ivDialogClose = findViewById(R.id.ivDialogClose);
        if (hideDialogClose)
            ivDialogClose.setVisibility(View.GONE);
    }

    private void showHideButtons() {
        if (TextUtils.isEmpty(negButtonText))
            btnCancel.setVisibility(View.GONE);
        else
            btnCancel.setText(negButtonText);

        if (!TextUtils.isEmpty(title))
            tvDialogHeader.setText(title);

        tvDescription.setText(description);
        btnOk.setText(posButtonText);

    }

    private void setListeners() {
        ivDialogClose.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDialogClose:
                dismiss();
                break;
            case R.id.btnOk:
                if (onPositiveClickListener != null)
                    onPositiveClickListener.onClick(this);
                else
                    dismiss();
                break;
            case R.id.btnCancel:
                if (onNegetiveClickListener != null)
                    onNegetiveClickListener.onClick(this);
                else
                    dismiss();
                break;
        }
    }

    public GulfUnnatiDialog setTitle(String title) {
        this.title = title;/*GulfOilUtils.chkNullDefaultValue(title, getContext().getString(R.string.app_name));*/
        return this;

    }

    public GulfUnnatiDialog setDescription(String description) {
        this.description = GulfOilUtils.chkNullDefaultValue(description, "");
        return this;
    }

    public GulfUnnatiDialog setPosButtonText(String posButtonText, OnPositiveClickListener positiveClickListener) {
        this.posButtonText = GulfOilUtils.chkNullDefaultValue(posButtonText, getContext().getString(R.string.str_ok));
        this.onPositiveClickListener = positiveClickListener;
        return this;
    }

    public GulfUnnatiDialog setNegButtonText(String negButtonText, OnNegetiveClickListener negetiveClickListener) {
        this.negButtonText = GulfOilUtils.chkNullDefaultValue(negButtonText, "");
        this.onNegetiveClickListener = negetiveClickListener;
        return this;
    }

    public interface OnPositiveClickListener {
        void onClick(GulfUnnatiDialog dialog);
    }

    public interface OnNegetiveClickListener {
        void onClick(GulfUnnatiDialog dialog);
    }

    public GulfUnnatiDialog hideDialogCloseButton(boolean hideDialogClose) {
        this.hideDialogClose = hideDialogClose;
        return this;
    }
}
