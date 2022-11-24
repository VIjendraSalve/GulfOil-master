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
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.DashboardMenuModel;

import java.util.List;

public class DashboardMenuAdapter extends RecyclerView.Adapter<DashboardMenuAdapter.DashboardMenuViewHolder> {
    private Context context;
    private List<DashboardMenuModel> menuList;
    private RecyclerViewOnClickListener onClickListener;

    public DashboardMenuAdapter(Context context, List<DashboardMenuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public DashboardMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardMenuViewHolder(LayoutInflater.from(context).inflate(R.layout.row_dashboard_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardMenuViewHolder holder, int position) {
        holder.bind(menuList.get(position));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class DashboardMenuViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvMenuName;

        public DashboardMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null)
                        onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
                }
            });
        }

        public void bind(DashboardMenuModel menu) {
            Glide.with(context).load(menu.getIconRes()).into(ivIcon);
            tvMenuName.setText(menu.getMenuName());
        }
    }
}
