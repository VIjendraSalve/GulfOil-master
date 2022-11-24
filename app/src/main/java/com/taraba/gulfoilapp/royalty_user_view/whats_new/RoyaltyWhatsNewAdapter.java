package com.taraba.gulfoilapp.royalty_user_view.whats_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.WhatsNew;

import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class RoyaltyWhatsNewAdapter extends RecyclerView.Adapter<RoyaltyWhatsNewAdapter.WhatsNewViewHolder> {

    private final Context context;
    private final List<WhatsNew> whatsNewList;

    public RoyaltyWhatsNewAdapter(Context context, List<WhatsNew> whatsNewList) {

        this.context = context;
        this.whatsNewList = whatsNewList;
    }

    @Override
    public WhatsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WhatsNewViewHolder(LayoutInflater.from(context).inflate(R.layout.row_royalty_whatsnew_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WhatsNewViewHolder holder, int position) {
        holder.tvLabel.setText(whatsNewList.get(position).getName());
        Picasso.with(context)
                .load(whatsNewList.get(position).getImageLink())
                .error(R.drawable.no_photo)
//                .resize(600, 400)
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
