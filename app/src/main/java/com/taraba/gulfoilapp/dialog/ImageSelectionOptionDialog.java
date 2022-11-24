package com.taraba.gulfoilapp.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class ImageSelectionOptionDialog extends AppCompatDialog implements View.OnClickListener {
    private String title;
    private UserType userType;
    private OnGalleryClickListener onGalleryClickListener;
    private OnCameraClickListener onCameraClickListener;
    private TextView tvDialogHeader;
    private ImageView ivDialogClose, ivCamera, ivGallery;
    private boolean hideDialogClose;

    public ImageSelectionOptionDialog(Context context) {
        this(context, UserType.UNNATI);
    }

    public ImageSelectionOptionDialog(Context context, UserType userType) {
        super(context);
        setCancelable(false);
        this.userType = userType;
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
                contentType = R.layout.dialog_image_selection_option_unnati;
                break;
            case ROYAL:
                contentType = R.layout.dialog_image_selection_option_royal;
                break;
            case ELITE:
                contentType = R.layout.dialog_image_selection_option_elite;
                break;
            case CLUB:
                contentType = R.layout.dialog_image_selection_option_club;
                break;
        }
        return contentType;
    }

    private void init() {
        tvDialogHeader = findViewById(R.id.tvDialogHeader);
        ivCamera = findViewById(R.id.ivCamera);
        ivGallery = findViewById(R.id.ivGallery);
        ivDialogClose = findViewById(R.id.ivDialogClose);
        if (hideDialogClose)
            ivDialogClose.setVisibility(View.GONE);
    }

    private void showHideButtons() {
        tvDialogHeader.setText(title);

    }

    private void setListeners() {
        ivDialogClose.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivGallery.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDialogClose:
                dismiss();
                break;
            case R.id.ivGallery:
                if (onGalleryClickListener != null)
                    onGalleryClickListener.onClick(this);
                else
                    dismiss();
                break;
            case R.id.ivCamera:
                if (onCameraClickListener != null)
                    onCameraClickListener.onClick(this);
                else
                    dismiss();
                break;
        }
    }

    public ImageSelectionOptionDialog setTitle(String title) {
        this.title = GulfOilUtils.chkNullDefaultValue(title, getContext().getString(R.string.app_name));
        return this;

    }


    public ImageSelectionOptionDialog setOnGalleryClickListener(OnGalleryClickListener positiveClickListener) {
        this.onGalleryClickListener = positiveClickListener;
        return this;
    }

    public ImageSelectionOptionDialog setOnCameraClickListener(OnCameraClickListener negetiveClickListener) {
        this.onCameraClickListener = negetiveClickListener;
        return this;
    }

    public ImageSelectionOptionDialog hideDialogCloseButton(boolean hideDialogClose) {
        this.hideDialogClose = hideDialogClose;
        return this;
    }

    public interface OnGalleryClickListener {
        void onClick(ImageSelectionOptionDialog dialog);
    }

    public interface OnCameraClickListener {
        void onClick(ImageSelectionOptionDialog dialog);
    }
}
