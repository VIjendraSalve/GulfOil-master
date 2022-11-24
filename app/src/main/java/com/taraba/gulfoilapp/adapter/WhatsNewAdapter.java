package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.WhatsNew;

import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class WhatsNewAdapter extends RecyclerView.Adapter<WhatsNewAdapter.WhatsNewViewHolder> {

    private final Context context;
    private final List<WhatsNew> whatsNewList;

    public WhatsNewAdapter(Context context, List<WhatsNew> whatsNewList) {

        this.context = context;
        this.whatsNewList = whatsNewList;
    }

    @Override
    public WhatsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WhatsNewViewHolder(LayoutInflater.from(context).inflate(R.layout.row_whatsnew_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WhatsNewViewHolder holder, int position) {
        holder.tvLabel.setText(whatsNewList.get(position).getName());
        String img_url = whatsNewList.get(position).getImageLink().replace("/.", "");
        Glide.with(context)
                .load(img_url)
                .override(600, 400)
                .error(R.drawable.no_photo)
                .into(holder.ivWhatsNewImage);
    }

    @Override
    public int getItemCount() {
        return whatsNewList.size();
    }

    public class WhatsNewViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private ImageView ivWhatsNewImage;

        public WhatsNewViewHolder(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_label);
            ivWhatsNewImage = (ImageView) itemView.findViewById(R.id.iv_whats_new_image);
        }
    }
}
