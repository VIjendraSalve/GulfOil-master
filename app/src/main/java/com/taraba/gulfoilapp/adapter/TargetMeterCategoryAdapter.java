package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.TargetMeterCategory;

import java.util.List;


/**
 * Created by AND707 on 06-Jul-18.
 */

public class TargetMeterCategoryAdapter extends RecyclerView.Adapter<TargetMeterCategoryAdapter.TargetMeterCategoryViewHolder> {

    private Context context;
    private List<TargetMeterCategory> targetMeterCategoryList;
    private OnItemClickListner onItemClickListner;

    public TargetMeterCategoryAdapter(Context context, List<TargetMeterCategory> targetMeterCategoryList) {
        this.context = context;
        this.targetMeterCategoryList = targetMeterCategoryList;
    }

    @Override
    public TargetMeterCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TargetMeterCategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_target_meter_category, parent, false));
    }

    @Override
    public void onBindViewHolder(TargetMeterCategoryViewHolder holder, int position) {
        holder.bind(targetMeterCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return targetMeterCategoryList.size();
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner {
        void onClick(int position);
    }

    public class TargetMeterCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_target_meter_category;

        public TargetMeterCategoryViewHolder(View itemView) {
            super(itemView);
            tv_target_meter_category = (TextView) itemView.findViewById(R.id.tv_target_meter_category);
            tv_target_meter_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListner.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(TargetMeterCategory tmc) {
            tv_target_meter_category.setText(tmc.getName());
        }
    }
}
