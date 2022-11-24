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
import com.taraba.gulfoilapp.model.KnowledgeCornerResponse;

import java.util.List;

public class KnowledgeCornerWebinarAdapter extends RecyclerView.Adapter<KnowledgeCornerWebinarAdapter.OrderHistoryViewHolder> {
    private Context context;
    private List<KnowledgeCornerResponse.Data.Webinar> webinarList;
    private RecyclerViewOnClickListener onClickListener;

    public KnowledgeCornerWebinarAdapter(Context context, List<KnowledgeCornerResponse.Data.Webinar> webinarList) {
        this.context = context;
        this.webinarList = webinarList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.row_webinar_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(webinarList.get(position));
    }

    @Override
    public int getItemCount() {
        return webinarList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_label, tv_description_value,
                tv_link_value;
        private ImageView iv_webinar_img;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_label = itemView.findViewById(R.id.tv_label);
            iv_webinar_img = itemView.findViewById(R.id.iv_webinar_img);
            tv_description_value = itemView.findViewById(R.id.tv_description_value);
            tv_link_value = itemView.findViewById(R.id.tv_link_value);
            tv_link_value.setOnClickListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_webinar_img.setClipToOutline(true);
            }
        }

        public void bind(KnowledgeCornerResponse.Data.Webinar webinar) {
            tv_label.setText(webinar.getWebinar_name());
            Glide.with(context).load(webinar.getWebinar_banner()).placeholder(R.drawable.no_image_available).into(iv_webinar_img);
            tv_description_value.setText(webinar.getDescription());
            tv_link_value.setText(webinar.getWebinar_link());
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
