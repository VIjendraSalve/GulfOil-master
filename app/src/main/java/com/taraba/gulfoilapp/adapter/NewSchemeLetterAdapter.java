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
public class NewSchemeLetterAdapter extends RecyclerView.Adapter<NewSchemeLetterAdapter.WhatsNewViewHolder> {

    private final Context context;
    private final List<UnnatiConnectResponse.SchemeLetter> schemeLetters;
    private RecyclerViewOnClickListener onClickListener;

    public NewSchemeLetterAdapter(Context context, List<UnnatiConnectResponse.SchemeLetter> whatsNewList) {

        this.context = context;
        this.schemeLetters = whatsNewList;
    }

    @Override
    public WhatsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WhatsNewViewHolder(LayoutInflater.from(context).inflate(getLayoutUserWise(), parent, false));
    }

    @Override
    public void onBindViewHolder(WhatsNewViewHolder holder, int position) {
        holder.tvLabel.setText(schemeLetters.get(position).getScheme_name());
        String img_url = schemeLetters.get(position).getOuter_image();
        Glide.with(context)
                .load(img_url)
                .override(900, 600)
                .error(R.drawable.no_image_available)
                .into(holder.ivSchemeLetter);
    }

    @Override
    public int getItemCount() {
        return schemeLetters.size();
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.row_scheme_letter_item_royal;
        else if (userType == UserType.ELITE)
            return R.layout.row_scheme_letter_item_elite;
        else if (userType == UserType.CLUB)
            return R.layout.row_scheme_letter_item_club;
        else
            return R.layout.row_scheme_letter_item_fls;
    }

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class WhatsNewViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLabel;
        private ImageView ivSchemeLetter;

        public WhatsNewViewHolder(View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLable);
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
