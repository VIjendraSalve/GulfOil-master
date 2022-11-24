package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.AchievementModel;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private Context context;
    private List<AchievementModel> achievementList;
    private RecyclerViewOnClickListener onClickListener;

    public AchievementAdapter(Context context, List<AchievementModel> achievementList) {
        this.context = context;
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchievementViewHolder(LayoutInflater.from(context).inflate(R.layout.row_royal_achievement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        holder.bind(achievementList.get(position));
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAchievementTitle,
                tvAchievementPoints,
                tvAchievementPointsLabel,
                tvAchievementDescription;
        private ImageView ivAchievementImage;


        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAchievementTitle = itemView.findViewById(R.id.tvAchievementTitle);
            tvAchievementPoints = itemView.findViewById(R.id.tvAchievementPoints);
            tvAchievementPointsLabel = itemView.findViewById(R.id.tvAchievementPointsLabel);
            tvAchievementDescription = itemView.findViewById(R.id.tvAchievementDescription);
            ivAchievementImage = itemView.findViewById(R.id.ivAchievementImage);
            itemView.findViewById(R.id.clAchievements).setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            });
        }

        public void bind(AchievementModel model) {
            tvAchievementTitle.setText(model.getTitle());
            tvAchievementPoints.setText(model.getValue());
            tvAchievementPointsLabel.setText(model.getValueDescription());
            tvAchievementDescription.setText(model.getDescription());
            Glide.with(context)
                    .load(model.getImg_res())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ivAchievementImage);
        }

    }
}
