package com.taraba.gulfoilapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.adapter.YDRDetailsRewardListAdapter;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

public class YourDigitalRewardDetailsActivity extends AppCompatActivity implements View.OnClickListener, ViewStub.OnInflateListener {
    private SubmitYDROTPResponse response;
    private ImageView ivProduct;
    private TextView tvRewardName;
    private TextView tvOrderDate;
    private TextView tvOrderId;
    private TextView tvHowToUse;
    private RecyclerView rvRewardList;
    private ViewStub toolbar;
    private TextView tvToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutUserWise());
        init();
        setToolbarUserWise();
        setToolbarTitle(getString(R.string.str_view_code));
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
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvHowToUse = findViewById(R.id.tvHowToUse);
        rvRewardList = findViewById(R.id.rvRewardList);
        tvHowToUse.setOnClickListener(this);
    }

    private void setData() {
        SubmitYDROTPResponse.Data data = response.getData().get(0);
        Glide.with(this)
                .load(data.product_image)
                .placeholder(R.drawable.no_image_available)
                .into(ivProduct);
        tvRewardName.setText(data.getReward_name());
        tvOrderId.setText(data.getOrder_id());
        tvOrderDate.setText(data.getOrders_record_date());

        rvRewardList.setAdapter(new YDRDetailsRewardListAdapter(this, data.getReward_order_detail(), data.getReward_code(), data.getReward_value()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvHowToUse:
              Intent intent = new Intent(this, HowToUseRewardsActivity.class);
            intent.putExtra("submitOTPResponse", response);
            startActivity(intent);
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_your_digital_reward_details_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_your_digital_reward_details_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_your_digital_reward_details_club;
        else
            return R.layout.fragment_your_digital_reward_details_royal;
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
}