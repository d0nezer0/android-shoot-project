package org.ggxz.shoot.mvp.view.activity_view;

import com.example.common_module.base.mvp.BaseView;

public interface TopView<E> extends BaseView {
    void onTopDataSuccess(E e);

    void onTopDataError();

}
