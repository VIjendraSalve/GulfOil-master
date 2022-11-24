package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.DashboardDataResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

public class DashboardRedeemAdapter extends RecyclerView.Adapter<DashboardRedeemAdapter.DashboardRedeemViewHolder> {
    private Context context;
    private List<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward> list;
    private RecyclerViewOnClickListener onClickListener;

    public DashboardRedeemAdapter(Context context, List<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DashboardRedeemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = 0;
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            layoutRes = R.layout.row_royal_redeem_rewards;
        } else if (userType == UserType.ELITE) {
            layoutRes = R.layout.row_elite_redeem_rewards;
        } else if (userType == UserType.CLUB) {
            layoutRes = R.layout.row_club_redeem_rewards;
        } else {
            layoutRes = R.layout.row_royal_redeem_rewards;
        }
        return new DashboardRedeemViewHolder(LayoutInflater.from(context).inflate(layoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardRedeemViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setList(List<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class DashboardRedeemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivRedeemRewardsImage;
        private TextView tvRedeemRewardsTitle, tvRedeemRewardsPoints;


        public DashboardRedeemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRedeemRewardsImage = itemView.findViewById(R.id.ivRedeemRewardsImage);
            tvRedeemRewardsTitle = itemView.findViewById(R.id.tvRedeemRewardsTitle);
            tvRedeemRewardsPoints = itemView.findViewById(R.id.tvRedeemRewardsPoints);
            itemView.findViewById(R.id.btnRedeem).setOnClickListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivRedeemRewardsImage.setClipToOutline(true);
            }
        }

        public void bind(DashboardDataResponse.Data.ParticipantDashboard.TrendingReward model) {
            tvRedeemRewardsTitle.setText(model.getName());
            tvRedeemRewardsPoints.setText(GulfOilUtils.getHtmlText("Points: <b>" + model.getPoints() + "</b>"));
            Glide.with(context).load(model.getSmall_image_link())
                    .placeholder(R.drawable.no_image_available)
                    .into(ivRedeemRewardsImage);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnRedeem:
                    if (onClickListener != null)
                        onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    public List<DashboardDataResponse.Data.ParticipantDashboard.TrendingReward> getList() {
        return list;
    }
}
