package com.taraba.gulfoilapp.royalty_user_view.order_history;

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
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.OrderHistory;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD;
import static com.taraba.gulfoilapp.util.GulfOilUtils.YYYY_MM_DD_HH_MM_SS;
import static com.taraba.gulfoilapp.util.GulfOilUtils.formatdate;

public class OrderHistoryAdapterNew extends RecyclerView.Adapter<OrderHistoryAdapterNew.OrderHistoryViewHolder> {
    private Context context;
    private List<OrderHistory> orderHistoryList;
    private RecyclerViewOnClickListener onClickListener;

    public OrderHistoryAdapterNew(Context context, List<OrderHistory> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(getLayoutUserWise(), parent, false));
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
                tvOrderDetails;
        private ImageView ivProduct;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRewardName = itemView.findViewById(R.id.tvRewardName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderDetails = itemView.findViewById(R.id.tvOrderDetails);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivProduct.setClipToOutline(true);
            }
            tvOrderDetails.setOnClickListener(this);
        }

        public void bind(OrderHistory orderHistory) {
            tvRewardName.setText(orderHistory.getName());
            tvOrderId.setText(orderHistory.getOrder_id());
            tvOrderDate.setText(formatdate(orderHistory.getDate(), YYYY_MM_DD_HH_MM_SS, YYYY_MM_DD));
            tvStatus.setText(orderHistory.getStatus());
            tvQuantity.setText(orderHistory.getQty());
            Glide.with(context).load(orderHistory.getProduct_image()).placeholder(R.drawable.no_image_available).into(ivProduct);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvOrderDetails) {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            }
        }
    }
}
