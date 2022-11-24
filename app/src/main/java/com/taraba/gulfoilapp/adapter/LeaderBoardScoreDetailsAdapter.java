package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.LeaderBoardResponse;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LeaderBoardScoreDetailsAdapter extends RecyclerView.Adapter<LeaderBoardScoreDetailsAdapter.ViewHolder> {
    private Context context;
    private List<LeaderBoardResponse.Data.ScoreDetail.ScoreData> scoreDataList;

    public LeaderBoardScoreDetailsAdapter(Context context, List<LeaderBoardResponse.Data.ScoreDetail.ScoreData> scoreDataList) {
        this.context = context;
        this.scoreDataList = scoreDataList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_leader_board_score_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(scoreDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return scoreDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvScoreTitle,
                tvYourScoreLable,
                tvYourScore,
                tvOtherMonthlyVolumeLable,
                tvOtherScoreVolume;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvScoreTitle = itemView.findViewById(R.id.tvScoreTitle);
            tvYourScoreLable = itemView.findViewById(R.id.tvYourScoreLable);
            tvYourScore = itemView.findViewById(R.id.tvYourScore);
            tvOtherMonthlyVolumeLable = itemView.findViewById(R.id.tvOtherMonthlyVolumeLable);
            tvOtherScoreVolume = itemView.findViewById(R.id.tvOtherScoreVolume);
        }

        public void bind(LeaderBoardResponse.Data.ScoreDetail.ScoreData scoreData) {
            tvScoreTitle.setText(scoreData.getTitle());
            for (int i = 0; i < scoreData.getDetail().size(); i++) {
                if (i == 0) {
                    tvYourScoreLable.setText(scoreData.getDetail().get(i).getKey());
                    tvYourScore.setText(scoreData.getDetail().get(i).getValue());
                } else if (i == 1) {
                    tvOtherMonthlyVolumeLable.setText(scoreData.getDetail().get(i).getKey());
                    tvOtherScoreVolume.setText(scoreData.getDetail().get(i).getValue());
                }
            }


        }
    }
}
