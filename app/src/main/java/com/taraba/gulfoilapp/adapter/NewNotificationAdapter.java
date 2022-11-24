package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.NewNotification;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

public class NewNotificationAdapter extends RecyclerView.Adapter<NewNotificationAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<NewNotification> notificationList;
    private RecyclerViewOnClickListener onClickListener;

    public NewNotificationAdapter(Context context, List<NewNotification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(getLayoutUserWise(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.row_notification_royal;
        else if (userType == UserType.ELITE)
            return R.layout.row_notification_elite;
        else if (userType == UserType.CLUB)
            return R.layout.row_notification_club;
        else
            return R.layout.row_notification_unati;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,
                tvDateTime, tvDesc;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvDesc = itemView.findViewById(R.id.tvDesc);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            });

        }

        public void bind(NewNotification notification) {
            tvTitle.setText(notification.getTitle());
            tvDateTime.setText(notification.getMdate());
            tvDesc.setText(notification.getDescription());

        }

    }
}
