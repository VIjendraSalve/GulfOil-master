package com.taraba.gulfoilapp;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class HowToUseRewardsActivity extends AppCompatActivity implements ViewStub.OnInflateListener, View.OnClickListener {
    private SubmitYDROTPResponse response;
    private ImageView ivProduct;
    private TextView
            tvRewardName,
            tvPoints,
            tvCode,
            tvConfirmOrderDescription;
    private ViewStub toolbar;
    private TextView tvToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());

        init();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_how_to_use));
        response = (SubmitYDROTPResponse) getIntent().getSerializableExtra("submitOTPResponse");
        setData();
    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnInflateListener(this);
        toolbar.setVisibility(View.GONE);
        ivProduct = findViewById(R.id.ivProduct);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivProduct.setClipToOutline(true);
        }
        tvRewardName = findViewById(R.id.tvRewardName);
        tvPoints = findViewById(R.id.tvPoints);
        tvCode = findViewById(R.id.tvCode);
        tvConfirmOrderDescription = findViewById(R.id.tvConfirmOrderDescription);
        tvConfirmOrderDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setData() {
        SubmitYDROTPResponse.Data data = response.getData().get(0);
        Glide.with(this)
                .load(data.product_image)
                .placeholder(R.drawable.no_image_available)
                .into(ivProduct);
        tvRewardName.setText(data.getReward_name());
        tvPoints.setText(data.getReward_value());
        tvCode.setText(data.getReward_code());
        tvConfirmOrderDescription.setText(GulfOilUtils.getHtmlText(data.getProduct_description()));

    }


    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_how_to_use_rewards_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_how_to_use_rewards_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_how_to_use_rewards_club;
        else
            return R.layout.fragment_how_to_use_rewards_royal;
    }


    private void setToolbarUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        } else if (userType == UserType.ELITE) {
            toolbar.setLayoutResource(R.layout.tool_bar_elite);
            toolbar.inflate();
        } else if (userType == UserType.CLUB) {
            toolbar.setLayoutResource(R.layout.tool_bar_club);
            toolbar.inflate();
        } else {
            toolbar.setLayoutResource(R.layout.tool_bar_royal);
            toolbar.inflate();
        }
    }

    public void setToolbarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    @Override
    public void onInflate(ViewStub stub, View inflated) {
        inflated.findViewById(R.id.ivBack).setOnClickListener(this);
        tvToolbarTitle = inflated.findViewById(R.id.tvToolbarTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }
}