package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.UnnatiConnectResponse;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class NewWhatsNewAdapter extends RecyclerView.Adapter<NewWhatsNewAdapter.WhatsNewViewHolder> {

    private final Context context;
    private final List<UnnatiConnectResponse.WhatsNew> whatsNewList;
    private RecyclerViewOnClickListener onClickListener;

    public NewWhatsNewAdapter(Context context, List<UnnatiConnectResponse.WhatsNew> whatsNewList) {

        this.context = context;
        this.whatsNewList = whatsNewList;
    }

    @Override
    public WhatsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WhatsNewViewHolder(LayoutInflater.from(context).inflate(getLayoutUserWise(), parent, false));
    }

    @Override
    public void onBindViewHolder(WhatsNewViewHolder holder, int position) {
        holder.tvLabel.setText(whatsNewList.get(position).getName());
        String img_url = whatsNewList.get(position).getOuter_image();
        Glide.with(context)
                .load(img_url)
                .override(900, 600)
                .error(R.drawable.no_image_available)
                .into(holder.ivWhatsNewImage);
    }

    @Override
    public int getItemCount() {
        return whatsNewList.size();
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
            return R.layout.row_whatsnew_item_fls;
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class WhatsNewViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private ImageView ivWhatsNewImage;

        public WhatsNewViewHolder(View itemView) {
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
    }
}
