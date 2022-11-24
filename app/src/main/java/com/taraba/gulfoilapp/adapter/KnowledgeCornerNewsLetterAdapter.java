package com.taraba.gulfoilapp.adapter;

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
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.KnowledgeCornerResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

public class KnowledgeCornerNewsLetterAdapter extends RecyclerView.Adapter<KnowledgeCornerNewsLetterAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<KnowledgeCornerResponse.Data.Newsletter> newsletterList;
    private RecyclerViewOnClickListener onClickListener;

    public KnowledgeCornerNewsLetterAdapter(Context context, List<KnowledgeCornerResponse.Data.Newsletter> newsletterList) {
        this.context = context;
        this.newsletterList = newsletterList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(getLayoutUserWise(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(newsletterList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsletterList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.row_whatsnew_item_royal;
        else if (userType == UserType.ELITE)
            return R.layout.row_whatsnew_item_elite;
        else if (userType == UserType.CLUB)
            return R.layout.row_whatsnew_item_club;
        else
            return R.layout.row_whatsnew_item_royal;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvLabel;
        private ImageView ivWhatsNewImage;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tv_label);
            ivWhatsNewImage = itemView.findViewById(R.id.iv_whats_new_image);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivWhatsNewImage.setClipToOutline(true);
            }

            itemView.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            });
        }

        public void bind(KnowledgeCornerResponse.Data.Newsletter newsletter) {
            tvLabel.setText(newsletter.getNewsletter_name());
            Glide.with(context)
                    .load(newsletter.getOuter_image())
                    .override(900, 600)
                    .error(R.drawable.no_image_available)
                    .into(ivWhatsNewImage);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvViewCode) {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            } else if (v.getId() == R.id.tv_link_value) {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            }
        }
    }
}
