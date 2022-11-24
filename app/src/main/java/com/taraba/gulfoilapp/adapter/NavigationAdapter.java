package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.drawerinterface.DrawerCallbacks;
import com.taraba.gulfoilapp.drawerinterface.NavigationItem;

import java.util.List;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.ViewHolder> {

    private List<NavigationItem> mData;
    private DrawerCallbacks mDrawerCallbacks;
    Context context;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;


    public NavigationAdapter(List<NavigationItem> data, Context context) {
        mData = data;
        this.context = context;
    }


    public void setNavigationDrawerCallbacks(DrawerCallbacks drawerCallbacks) {
        mDrawerCallbacks = drawerCallbacks;
    }

    @Override
    public NavigationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NavigationAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.textView.setText(mData.get(i).getText());

        viewHolder.imageView.setImageResource(mData.get(i).getImage());


        // viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(mData.get(i).getDrawable(), null, null, null);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {

                                                       if (mDrawerCallbacks != null)
                                                           mDrawerCallbacks.onNavigationDrawerItemSelected(i);
                                                   }
                                               }
        );


        /*if (mSelectedPosition == i || mTouchedPosition == i) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.selection));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }*/

        if (mSelectedPosition == i || mTouchedPosition == i) {
            viewHolder.itemView.setBackgroundResource(R.drawable.drawer_selector_row);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
        }
    }
}