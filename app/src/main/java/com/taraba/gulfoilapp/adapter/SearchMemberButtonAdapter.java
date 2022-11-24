package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.SearchMemberButton;

import java.util.List;

/**
 * Created by SHINSAN-CONT on 6/9/2017.
 */

class SearchMemberButtonAdapter extends RecyclerView.Adapter<SearchMemberButtonAdapter.SearchMemberButtonViewHolder> {
    private final Context context;
    private final List<SearchMemberButton> listOfButton;
    private SearchMemberButtonCallBack searchMemberButtonCallBack;

    public SearchMemberButtonAdapter(Context context, List<SearchMemberButton> listOfButton) {
        this.context = context;
        this.listOfButton = listOfButton;
    }

    @Override
    public SearchMemberButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchMemberButtonViewHolder(LayoutInflater.from(context).inflate(R.layout.button_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchMemberButtonViewHolder holder, int position) {
        holder.btnAction.setText(listOfButton.get(position).getName());
        holder.btnAction.setBackgroundColor(context.getResources().getColor(listOfButton.get(position).getBackColor()));
    }

    @Override
    public int getItemCount() {
        return listOfButton.size();
    }

    public class SearchMemberButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btnAction;

        public SearchMemberButtonViewHolder(View itemView) {
            super(itemView);
            btnAction = (Button) itemView.findViewById(R.id.btnAction);
            btnAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            searchMemberButtonCallBack.searchMemberButtonClick(getAdapterPosition());
        }
    }

    public void setSearchMemberButtonCallBack(SearchMemberButtonCallBack searchMemberButtonCallBack) {
        this.searchMemberButtonCallBack = searchMemberButtonCallBack;
    }

    public interface SearchMemberButtonCallBack {
        public void searchMemberButtonClick(int poistion);
    }
}
