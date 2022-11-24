package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.presenter;

import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.interactor.KnowledgeCornerInteractor;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.interactor.KnowledgeCornerInteractorImpl;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.view.KnowledgeCornerView;


public class KnowledgeCornerPresenterImpl implements KnowledgeCornerPresenter {
    private KnowledgeCornerView view;
    private KnowledgeCornerInteractor interactor;

    public KnowledgeCornerPresenterImpl(KnowledgeCornerView view) {
        this.view = view;
        this.interactor = new KnowledgeCornerInteractorImpl(this);
    }
}
