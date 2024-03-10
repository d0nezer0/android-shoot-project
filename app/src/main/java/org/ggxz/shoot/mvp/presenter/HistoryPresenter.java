package org.ggxz.shoot.mvp.presenter;

import com.example.common_module.base.mvp.BasePresenter;

import org.ggxz.shoot.mvp.view.activity_view.HistoryView;

public interface HistoryPresenter  extends BasePresenter<HistoryView> {
    void getAllTopData();
}
