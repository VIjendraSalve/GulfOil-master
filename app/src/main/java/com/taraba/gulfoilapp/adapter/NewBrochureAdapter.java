package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.UnnatiConnectResponse;

import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class NewBrochureAdapter extends RecyclerView.Adapter<NewBrochureAdapter.WhatsNewViewHolder> {

    private final Context context;
    private final List<UnnatiConnectResponse.Brochure> brochureList;
    private RecyclerViewOnClickListener onClickListener;

    public NewBrochureAdapter(Context context, List<UnnatiConnectResponse.Brochure> brochureList) {

        this.context = context;
        this.brochureList = brochureList;
    }

    @Override
    public WhatsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WhatsNewViewHolder(LayoutInflater.from(context).inflate(R.layout.row_brochure_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WhatsNewViewHolder holder, int position) {
        String img_url = brochureList.get(position).getOuter_image();
        Glide.with(context)
                .load(img_url)
                .override(600, 900)
                .error(R.drawable.no_image_available)
                .into(holder.ivSchemeLetter);
    }

    @Override
    public int getItemCount() {
        return brochureList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class WhatsNewViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSchemeLetter;

        public WhatsNewViewHolder(View itemView) {
            super(itemView);
            ivSchemeLetter = itemView.findViewById(R.id.ivSchemeLetter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivSchemeLetter.setClipToOutline(true);
            }

            itemView.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onRecyclerViewItemClick(v, getAdapterPosition());
            });
        }
    }
}
