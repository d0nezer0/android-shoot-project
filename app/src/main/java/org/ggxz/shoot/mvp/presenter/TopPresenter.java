package org.ggxz.shoot.mvp.presenter;

import com.example.common_module.base.mvp.BasePresenter;

import org.ggxz.shoot.mvp.view.activity_view.TopView;

public interface TopPresenter  extends BasePresenter<TopView> {

    void getAllTopData();
}
