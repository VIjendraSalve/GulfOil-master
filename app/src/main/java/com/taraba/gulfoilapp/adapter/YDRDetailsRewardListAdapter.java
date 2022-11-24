package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.SubmitYDROTPResponse;

import java.util.List;

public class YDRDetailsRewardListAdapter extends RecyclerView.Adapter<YDRDetailsRewardListAdapter.ViewHolder> {
    private Context context;
    private List<SubmitYDROTPResponse.Data.RewardOrderDetail> rewardOrderDetailList;
    private String rewardCode, rewardValue;

    public YDRDetailsRewardListAdapter(Context context, List<SubmitYDROTPResponse.Data.RewardOrderDetail> rewardOrderDetailList, String rewardCode, String rewardValue) {
        this.context = context;
        this.rewardOrderDetailList = rewardOrderDetailList;
        this.rewardCode = rewardCode;
        this.rewardValue = rewardValue;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_ydr_details_reward, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(rewardOrderDetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return rewardOrderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRewardTitle;
        private TextView tvRewardCode;
        private TextView tvRewardValue;
        private TextView tvRewardKey;
        private TextView tvRewardPin;
        private TextView tvExpirtyOfTheCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRewardTitle = itemView.findViewById(R.id.tvRewardTitle);
            tvRewardCode = itemView.findViewById(R.id.tvRewardCode);
            tvRewardValue = itemView.findViewById(R.id.tvRewardValue);
            tvRewardKey = itemView.findViewById(R.id.tvRewardKey);
            tvRewardPin = itemView.findViewById(R.id.tvRewardPin);
            tvExpirtyOfTheCode = itemView.findViewById(R.id.tvExpirtyOfTheCode);
        }

        public void bind(SubmitYDROTPResponse.Data.RewardOrderDetail reward) {
            int number = getAdapterPosition() + 1;
            tvRewardTitle.setText("REWARD CODE " + number);
            tvRewardCode.setText(rewardCode);
            tvRewardValue.setText(rewardValue);
            tvRewardKey.setText(reward.getEv_code());
            tvRewardPin.setText(reward.getEv_pin());
            tvExpirtyOfTheCode.setText(reward.getExpiry_date());
        }
    }
}
