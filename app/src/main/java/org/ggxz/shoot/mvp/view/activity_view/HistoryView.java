package org.ggxz.shoot.mvp.view.activity_view;

import com.example.common_module.base.mvp.BaseView;

public interface HistoryView<E> extends BaseView {
    void onHistoryDataSuccess(E e);

    void onHistoryDataError();
}
