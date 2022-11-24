package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.interactor;

import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.presenter.KnowledgeCornerPresenter;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.presenter.KnowledgeCornerPresenterImpl;

public class KnowledgeCornerInteractorImpl implements KnowledgeCornerInteractor {
    private KnowledgeCornerPresenter presenter;

    public KnowledgeCornerInteractorImpl(KnowledgeCornerPresenterImpl presenter) {
        this.presenter = presenter;
    }
}
