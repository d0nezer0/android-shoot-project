package com.example.common_module.base.mvp;

/**
 * 所有Presenter接口的基类
 */

public interface BasePresenter<T extends BaseView> {

    /**
     * 绑定View
     */
    void attachView(T view);

    /**
     * 解绑View
     */
    void detachView();
}
