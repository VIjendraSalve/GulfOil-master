package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.MagicBonanzaListResponse;

import java.util.List;

public class MagicBonanzaListAdapter extends RecyclerView.Adapter<MagicBonanzaListAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<MagicBonanzaListResponse.Data> magicBonanzaList;
    private RecyclerViewOnClickListener onClickListener;

    public MagicBonanzaListAdapter(Context context, List<MagicBonanzaListResponse.Data> magicBonanzaList) {
        this.context = context;
        this.magicBonanzaList = magicBonanzaList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_magic_bonanza_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(magicBonanzaList.get(position));
    }

    @Override
    public int getItemCount() {
        return magicBonanzaList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            });

        }

        public void bind(MagicBonanzaListResponse.Data model) {
            tvTitle.setText(model.getHeader());
        }

    }
}
