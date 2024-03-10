package org.ggxz.shoot.mvp.view.activity_view;

import com.example.common_module.base.mvp.BaseView;

public interface DetailsView<T> extends BaseView {
    void getDetailsDataSuccess(T e);

    void onDetailsDataError(String msg);
}
