package com.taraba.gulfoilapp.view.splash_pop_up.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import java.util.List;

public class SplashPopUpAdapter extends PagerAdapter {
    private List<SplashPopUpDetails> splashPopUpDetailsList;
    private Context context;
    private ClickListener mClickListener;

    public SplashPopUpAdapter(Context context, List<SplashPopUpDetails> splashPopUpDetailsList) {
        this.splashPopUpDetailsList = splashPopUpDetailsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return splashPopUpDetailsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(getLayoutUserWise(), container, false);
        SplashPopUpViewHolder viewHolder = new SplashPopUpViewHolder(view);
        viewHolder.bind(splashPopUpDetailsList.get(position));
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setmClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.item_splash_pop_up_royal;
        else if (userType == UserType.ELITE)
            return R.layout.item_splash_pop_up_elite;
        else if (userType == UserType.CLUB)
            return R.layout.item_splash_pop_up_club;
        else
            return R.layout.item_splash_pop_up_royal;
    }

    public interface ClickListener {
        void closeDialog();

        void showImageFullSize(SplashPopUpDetails splashPopUpDetails);

        void goToAction(View view,String actionName);
    }

    class SplashPopUpViewHolder {
        private ImageView ivSplashPopUpImage, ivClose;
        private TextView tvTitle, tvSubTitle, tvDescription;
        private AppCompatButton btnTag;

        public SplashPopUpViewHolder(View view) {
            ivSplashPopUpImage = view.findViewById(R.id.iv_splash_pop_up_image);
            ivClose = view.findViewById(R.id.iv_close);
            tvTitle = view.findViewById(R.id.tv_title);
            tvSubTitle = view.findViewById(R.id.tv_sub_title);
            tvDescription = view.findViewById(R.id.tv_description);
            btnTag = view.findViewById(R.id.btnTag);
        }


        public void bind(final SplashPopUpDetails pop) {
            Picasso.with(context).load(pop.getPopupImage()).into(ivSplashPopUpImage);
            tvTitle.setText(pop.getTitle());
            tvSubTitle.setText(pop.getSubTitle());
            tvDescription.setText(GulfOilUtils.getHtmlText(pop.getDescription()));
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
            if (TextUtils.isEmpty(pop.getTag())) {
                btnTag.setVisibility(View.GONE);
                btnTag.setText("");
            } else {
                btnTag.setVisibility(View.VISIBLE);
                btnTag.setText(pop.getTag_label());
            }
            ivClose.setOnClickListener(v -> {
                if (mClickListener != null)
                    mClickListener.closeDialog();
            });
            btnTag.setOnClickListener(v -> {
                if (mClickListener != null)
                    mClickListener.goToAction(v,pop.getTag());
            });

            ivSplashPopUpImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null)
                        mClickListener.showImageFullSize(pop);
                }
            });
        }


    }
}
