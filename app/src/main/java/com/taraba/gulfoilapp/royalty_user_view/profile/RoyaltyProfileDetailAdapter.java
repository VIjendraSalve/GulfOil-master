package com.taraba.gulfoilapp.royalty_user_view.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.KeyValue;

import java.util.List;

/**
 * Created by SHINSAN-CONT on 5/20/2017.
 */

public class RoyaltyProfileDetailAdapter extends RecyclerView.Adapter<RoyaltyProfileDetailAdapter.ProfileViewHolder> {
    private final Context context;
    private final List<KeyValue> keyValues;
    private boolean flag = true;
    private int darkOrange;
    private int fentOrange;

    public RoyaltyProfileDetailAdapter(Context context, List<KeyValue> keyValues) {

        this.context = context;
        this.keyValues = keyValues;
        this.darkOrange = context.getResources().getColor(R.color.org100);
        this.fentOrange = context.getResources().getColor(R.color.org99);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.row_royalty_profile_details, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.tvLabel.setText(keyValues.get(position).getKey());
        holder.tvText.setText(keyValues.get(position).getValue());

//        holder.tvLabel.setBackgroundColor(flag ? darkOrange : fentOrange);
//        holder.tvText.setBackgroundColor(flag ? fentOrange : darkOrange);
        flag = !flag;
    }

    @Override
    public int getItemCount() {
        return keyValues.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel, tvText;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
