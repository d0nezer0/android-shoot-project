package com.example.common_module.base.mvp;

import android.os.Bundle;
import android.widget.Toast;


import com.example.common_module.base.BaseFragment;

import androidx.annotation.Nullable;

/**
 * 功能   ：将dagger2依赖注入通过抽象方法提取出来，由具体的Fragement去实现
 */

public  abstract class BaseMvpFragment<T extends BasePresenter> extends BaseFragment {

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter=initInjector();
        //绑定View
        mPresenter.attachView(this);
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this.getContext(),msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 完成注入并返回注入的Presenter对象
     * @return
     */
    protected abstract T initInjector();

    /**
     * 解绑
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.detachView();
        }
    }
}
