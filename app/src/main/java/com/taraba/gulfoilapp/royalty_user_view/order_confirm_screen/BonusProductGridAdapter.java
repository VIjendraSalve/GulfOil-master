package com.taraba.gulfoilapp.royalty_user_view.order_confirm_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.Product;

import java.util.ArrayList;

public class BonusProductGridAdapter extends BaseAdapter {
    private static final String TAG = "RedeemProduct";
    ArrayList<Product> prods;
    String comefrom;
    String isDisable = "";
    private Context mContext;

    public BonusProductGridAdapter(Context c, ArrayList<Product> prods, String comefrom, String isDisable) {
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
            grid = inflater.inflate(R.layout.royalty_bonus_product_item, null);
        } else {
            grid = convertView;
        }

        TextView txt_name = (TextView) grid.findViewById(R.id.txt_name);
        TextView txt_code = (TextView) grid.findViewById(R.id.txt_code);
        TextView txt_small_desc = (TextView) grid.findViewById(R.id.txt_small_desc);


        grid.findViewById(R.id.llRedeemRowLayout).setBackgroundColor(mContext.getResources().getColor(prods.get(position).getColor()));


        ImageView imageView = (ImageView) grid.findViewById(R.id.iv_product);


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
        txt_code.setText("" + prods.get(position).getPoints() + "Points|");
        txt_small_desc.setText("Code:" + prods.get(position).getProduct_code());

        Picasso.with(mContext)
                .load(prods.get(position).getSmall_image_link())
                .placeholder(mContext.getResources().getDrawable(R.drawable.loading)).error(mContext.getResources().getDrawable(R.drawable.about1))
                .resize(150, 150)                                // optional
                .into(imageView);
        return grid;
    }
}