package com.taraba.gulfoilapp.royalty_user_view.view_rewards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.model.Product;
import com.taraba.gulfoilapp.util.GulfOilUtils;

import java.util.ArrayList;

public class RewardsProductGridAdapter extends BaseAdapter {
    private static final String TAG = "RedeemProduct";
    ArrayList<Product> prods;
    String comefrom;
    String isDisable = "";
    private Context mContext;
    private RecyclerViewOnClickListener onClickListener;

    public RewardsProductGridAdapter(Context c, ArrayList<Product> prods, String comefrom, String isDisable) {
        mContext = c;
        this.prods = prods;
        this.comefrom = comefrom;
        this.isDisable = isDisable;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return prods.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return prods.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return prods.get(arg0).getProduct_id_pk();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = inflater.inflate(getLayoutUserWise(), null);
        } else {
            grid = convertView;
        }

        TextView txt_name = (TextView) grid.findViewById(R.id.txt_name);
        TextView txt_code = (TextView) grid.findViewById(R.id.txt_code);
        TextView txt_small_desc = (TextView) grid.findViewById(R.id.txt_small_desc);
        ImageView imageView = (ImageView) grid.findViewById(R.id.iv_product);
        grid.findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
            if (onClickListener != null)
                onClickListener.onRecyclerViewItemClick(v, position);
        });


        // Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.pic1);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.loading);
        //    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
        imageView.setImageBitmap(bitmap);


//commented by Pravin Dharam 18-5-2017
        /*if(isDisable.equals("true"))
            btn_redeem.setVisibility(View.GONE);
        else
            btn_redeem.setVisibility(View.VISIBLE);*/
         /* Picasso.with(mContext)

                .load(R.drawable.loading)
                .resize(150, 150)                                // optional
                .into(imageView);
*/
        if (comefrom.equals("bonus")) {
            txt_small_desc.setVisibility(View.GONE);
            txt_code.setVisibility(View.GONE);
            //commented by Pravin Dharam 18-5-2017
            /* btn_redeem.setVisibility(View.VISIBLE);*/
        } else if (comefrom.equals("product")) {
            txt_small_desc.setVisibility(View.VISIBLE);
            txt_code.setVisibility(View.VISIBLE);
            //commented by Pravin Dharam 18-5-2017
           /* if(isDisable.equals("true"))
                btn_redeem.setVisibility(View.GONE);
            else
                btn_redeem.setVisibility(View.VISIBLE);*/
        }


        txt_name.setText(prods.get(position).getName());
        txt_code.setText("Points:" + String.valueOf(prods.get(position).getPoints()).trim());
        txt_small_desc.setText("Code: " + String.valueOf(prods.get(position).getProduct_code()).trim());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setClipToOutline(true);
        }
        Picasso.with(mContext)
                .load(prods.get(position).getSmall_image_link())
                .placeholder(mContext.getResources().getDrawable(R.drawable.loading))
                .error(mContext.getResources().getDrawable(R.drawable.no_image_available))
                .resize(150, 150)                                // optional
                .into(imageView);
        return grid;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.row_reward_product_item_royal;
        else if (userType == UserType.ELITE)
            return R.layout.row_reward_product_item_elite;
        else if (userType == UserType.CLUB)
            return R.layout.row_reward_product_item_club;
        else
            return R.layout.row_reward_product_item_fls;
    }


    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


}