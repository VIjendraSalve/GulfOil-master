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
import com.taraba.gulfoilapp.model.YDRResponse;

import java.util.List;

import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD;
import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD_HH_MM_SS;
import static com.taraba.gulfoilapp.util.GulfOilUtils.formatdate;

public class YDRCampaignAdapter extends RecyclerView.Adapter<YDRCampaignAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<YDRResponse.Data.Campagin> campaginList;
    private RecyclerViewOnClickListener onClickListener;

    public YDRCampaignAdapter(Context context, List<YDRResponse.Data.Campagin> campaginList) {
        this.context = context;
        this.campaginList = campaginList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_digital_reward_campagin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(campaginList.get(position));
    }

    @Override
    public int getItemCount() {
        return campaginList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRewardName,
                tvOrderId,
                tvOrderDate,
                tvQuantity,
                tvCampaignName,
                tvViewCode;
        private ImageView ivProduct;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRewardName = itemView.findViewById(R.id.tvRewardName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvViewCode = itemView.findViewById(R.id.tvViewCode);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivProduct.setClipToOutline(true);
            }
            tvViewCode.setOnClickListener(this);
        }

        public void bind(YDRResponse.Data.Campagin campagin) {
            tvCampaignName.setText(campagin.getCampaign_name());
            tvRewardName.setText(campagin.getReward_name());
            tvOrderId.setText(campagin.getOrder_id());
            tvOrderDate.setText(formatdate(campagin.getOrders_record_date(), YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD));
            //tvStatus.setText(campagin.getStatus());
            tvQuantity.setText(campagin.getQuantity());
            Glide.with(context).load(campagin.getProduct_image()).placeholder(R.drawable.no_image_available).into(ivProduct);

            if (campagin.getView_codes().equals("no")) {
                tvViewCode.setVisibility(View.GONE);
            } else {
                tvViewCode.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvViewCode) {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            }
        }
    }
}
