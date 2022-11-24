package com.taraba.gulfoilapp.royalty_user_view.main_activity.presenter;

import com.taraba.gulfoilapp.royalty_user_view.main_activity.interactor.RoyalityMainInteractor;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.interactor.RoyalityMainInteractorImpl;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.view.RoyalityMainView;


public class RoyalityMainPresenterImpl implements RoyalityMainPresenter {
    private RoyalityMainView view;
    private RoyalityMainInteractor interactor;

    public RoyalityMainPresenterImpl(RoyalityMainView view) {
        this.view = view;
        this.interactor = new RoyalityMainInteractorImpl(this);
    }
}
