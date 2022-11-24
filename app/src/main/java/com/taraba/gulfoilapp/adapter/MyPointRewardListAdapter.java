package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.TranscationHistoryModel;

import java.util.List;

public class MyPointRewardListAdapter extends RecyclerView.Adapter<MyPointRewardListAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<TranscationHistoryModel> transactionHistoryList;
    private RecyclerViewOnClickListener onClickListener;

    public MyPointRewardListAdapter(Context context, List<TranscationHistoryModel> transactionHistoryList) {
        this.context = context;
        this.transactionHistoryList = transactionHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_my_points_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(transactionHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int rotationAngle = 0;
        private TextView tvMonth;
        private ImageView ivDropDown;
        private TableLayout tlTHList;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            ivDropDown = itemView.findViewById(R.id.ivDropDown);
            tlTHList = itemView.findViewById(R.id.tlTHList);
            ivDropDown.setOnClickListener(v -> {
                if (tlTHList.getVisibility() == View.GONE) {
                    tlTHList.setVisibility(View.VISIBLE);
                    ivDropDown.animate().rotationX(180).setDuration(800).start();
                } else {
                    tlTHList.setVisibility(View.GONE);
                    ivDropDown.animate().rotationX(0).setDuration(800).start();
                }
            });

        }

        public void bind(TranscationHistoryModel th) {
            tlTHList.setVisibility(View.GONE);
            tvMonth.setText(th.getMonth());
            tlTHList.removeAllViews();
            for (int i = 0; i < th.getKeyValueList().size(); i++) {
                View chileView = LayoutInflater.from(context).inflate(R.layout.tr_th_list, null);
                TextView tvKey = chileView.findViewById(R.id.tvKey);
                TextView tvValue = chileView.findViewById(R.id.tvValue);
                tvKey.setText(th.getKeyValueList().get(i).getKey());
                tvValue.setText(th.getKeyValueList().get(i).getValue());
                tlTHList.addView(chileView);

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
