package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.FlsUnnatiDashboardModel;

import java.util.List;

public class FlsUnnatiDashbaordAdapter extends RecyclerView.Adapter<FlsUnnatiDashbaordAdapter.AchievementViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<FlsUnnatiDashboardModel> list;
    /* access modifiers changed from: private */
    public RecyclerViewOnClickListener onClickListener;

    public FlsUnnatiDashbaordAdapter(Context context2, List<FlsUnnatiDashboardModel> list2) {
        this.context = context2;
        this.list = list2;
    }

    public AchievementViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AchievementViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_fls_unnati_dashboard, viewGroup, false));
    }

    public void onBindViewHolder(AchievementViewHolder achievementViewHolder, int i) {
        achievementViewHolder.bind(this.list.get(i));
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.onClickListener = recyclerViewOnClickListener;
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAchievementImage;
        private TextView tvAchievementDescription;
        private TextView tvAchievementTitle;

        public AchievementViewHolder(View view) {
            super(view);
            this.tvAchievementTitle = (TextView) view.findViewById(R.id.tvAchievementTitle);
            this.tvAchievementDescription = (TextView) view.findViewById(R.id.tvAchievementDescription);
            this.ivAchievementImage = (ImageView) view.findViewById(R.id.ivAchievementImage);
            view.findViewById(R.id.clAchievements).setOnClickListener(new View.OnClickListener() {
                public final void onClick(View view) {
                    AchievementViewHolder.this.lambda$new$0$FlsUnnatiDashbaordAdapter$AchievementViewHolder(view);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$FlsUnnatiDashbaordAdapter$AchievementViewHolder(View view) {
            if (FlsUnnatiDashbaordAdapter.this.onClickListener != null) {
                FlsUnnatiDashbaordAdapter.this.onClickListener.onRecyclerViewItemClick(view, getAdapterPosition());
            }
        }

        public void bind(FlsUnnatiDashboardModel flsUnnatiDashboardModel) {
            this.tvAchievementTitle.setText(flsUnnatiDashboardModel.getTitle());
            this.tvAchievementDescription.setText(flsUnnatiDashboardModel.getDescription());
            ((RequestBuilder) Glide.with(FlsUnnatiDashbaordAdapter.this.context).load(Integer.valueOf(flsUnnatiDashboardModel.getImg_res())).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).into(this.ivAchievementImage);
        }
    }
}
