package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.presenter.KnowledgeCornerPresenter;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.view.KnowledgeCornerDeailsFragment;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.view.RoyalityMainActivity;

public class KnowledgeCornerFragment extends Fragment implements KnowledgeCornerView {
    private KnowledgeCornerPresenter presenter;
    private RoyalityMainActivity royalityMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_knowledge_corner_fragment, container, false);

        init(view);
        setListeners(view);

        return view;
    }

    private void setListeners(View view) {
        view.findViewById(R.id.ll_two_wheeler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsPage("MCO");
            }
        });
        view.findViewById(R.id.ll_passenger_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsPage("PCMO");
            }
        });
        view.findViewById(R.id.ll_commercial_vehilce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsPage("DEO");
            }
        });
    }

    private void init(View view) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        royalityMainActivity = (RoyalityMainActivity) context;
    }

    private void goToDetailsPage(String type) {
        royalityMainActivity.replaceFragment(KnowledgeCornerDeailsFragment.getInstance(type));
    }
}
