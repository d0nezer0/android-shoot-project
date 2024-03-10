package com.example.common_module.base.mvp;

/**
 * 功能   ：所有的Presenter都有绑定和解绑的操作，所以抽取到基类当中
 */

public class BasePresenterImpl<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
