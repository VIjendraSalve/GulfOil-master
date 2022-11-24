package com.taraba.gulfoilapp.royalty_user_view.main_activity.interactor;

import com.taraba.gulfoilapp.royalty_user_view.main_activity.presenter.RoyalityMainPresenter;
import com.taraba.gulfoilapp.royalty_user_view.main_activity.presenter.RoyalityMainPresenterImpl;

public class RoyalityMainInteractorImpl implements RoyalityMainInteractor {
    private RoyalityMainPresenter presenter;

    public RoyalityMainInteractorImpl(RoyalityMainPresenterImpl presenter) {
        this.presenter = presenter;
    }
}
