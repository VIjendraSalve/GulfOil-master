package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.presenter;

import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.interactor.KnowledgeCornerDeailsInteractor;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.interactor.KnowledgeCornerDeailsInteractorImpl;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.view.KnowledgeCornerDeailsView;


public class KnowledgeCornerDeailsPresenterImpl implements KnowledgeCornerDeailsPresenter {
    private KnowledgeCornerDeailsView view;
    private KnowledgeCornerDeailsInteractor interactor;

    public KnowledgeCornerDeailsPresenterImpl(KnowledgeCornerDeailsView view) {
        this.view = view;
        this.interactor = new KnowledgeCornerDeailsInteractorImpl(this);
    }
}
