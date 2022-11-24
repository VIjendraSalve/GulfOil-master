package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.interactor;

import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.presenter.KnowledgeCornerDeailsPresenter;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.presenter.KnowledgeCornerDeailsPresenterImpl;

public class KnowledgeCornerDeailsInteractorImpl implements KnowledgeCornerDeailsInteractor {
    private KnowledgeCornerDeailsPresenter presenter;

    public KnowledgeCornerDeailsInteractorImpl(KnowledgeCornerDeailsPresenterImpl presenter) {
        this.presenter = presenter;
    }
}
