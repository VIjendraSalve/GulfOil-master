package com.taraba.gulfoilapp.royalty_user_view.scheme_letter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.SchemeLetter;

import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */
public class RoyaltySchemeLetterAdapter extends RecyclerView.Adapter<RoyaltySchemeLetterAdapter.SchemeLetterViewHolder> {
    private final Context context;
    private final List<SchemeLetter> schemeLetterList;
    private MyCallback myCallback;

    public RoyaltySchemeLetterAdapter(Context context, List<SchemeLetter> schemeLetterList) {
        this.context = context;
        this.schemeLetterList = schemeLetterList;
    }

    @Override
    public SchemeLetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SchemeLetterViewHolder(LayoutInflater.from(context).inflate(R.layout.row_royalty_shemeletter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SchemeLetterViewHolder holder, int position) {
        holder.tvLabelsa.setText(schemeLetterList.get(position).getName());
        Picasso.with(context)
                .load(schemeLetterList.get(position).getImageLink())
                .error(R.drawable.no_photo)
                .resize(600, 400)
                .into(holder.ivschemeimage);
    }

    @Override
    public int getItemCount() {
        return schemeLetterList.size();
    }

    public void setMyCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }

    public interface MyCallback {
        public void myClick(int position);
    }

    public class SchemeLetterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvLabelsa;
        private ImageView ivschemeimage;

        public SchemeLetterViewHolder(View itemView) {
            super(itemView);
            tvLabelsa = (TextView) itemView.findViewById(R.id.tv_label_scheme_letter);
            ivschemeimage = (ImageView) itemView.findViewById(R.id.iv_scheme_letter_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myCallback.myClick(getAdapterPosition());
        }
    }
}
