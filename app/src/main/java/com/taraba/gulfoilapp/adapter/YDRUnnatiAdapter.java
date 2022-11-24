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
import com.taraba.gulfoilapp.model.YDRResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD;
import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD_HH_MM_SS;
import static com.taraba.gulfoilapp.util.GulfOilUtils.formatdate;

public class YDRUnnatiAdapter extends RecyclerView.Adapter<YDRUnnatiAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<YDRResponse.Data.Unnati> orderHistoryList;
    private RecyclerViewOnClickListener onClickListener;

    public YDRUnnatiAdapter(Context context, List<YDRResponse.Data.Unnati> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_unnati_digital_reward, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(orderHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.row_order_history_royal;
        else if (userType == UserType.ELITE)
            return R.layout.row_order_history_elite;
        else if (userType == UserType.CLUB)
            return R.layout.row_order_history_club;
        else
            return R.layout.row_order_history_royal;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvRewardName,
                tvOrderId,
                tvOrderDate,
                tvQuantity,
                tvStatus,
                tvViewCode;
        private ImageView ivProduct;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRewardName = itemView.findViewById(R.id.tvRewardName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvViewCode = itemView.findViewById(R.id.tvViewCode);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivProduct.setClipToOutline(true);
            }
            tvViewCode.setOnClickListener(this);
        }

        public void bind(YDRResponse.Data.Unnati orderHistory) {
            tvRewardName.setText(orderHistory.getReward_name());
            tvOrderId.setText(orderHistory.getOrder_id());
            tvOrderDate.setText(formatdate(orderHistory.getOrders_record_date(), YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD));
            tvStatus.setText(orderHistory.getStatus());
            tvQuantity.setText(orderHistory.getQuantity());
            Glide.with(context).load(orderHistory.getProduct_image()).placeholder(R.drawable.no_image_available).into(ivProduct);

            if (orderHistory.getView_codes().equals("no")) {
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
