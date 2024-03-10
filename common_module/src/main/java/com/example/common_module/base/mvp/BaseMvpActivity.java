package com.example.common_module.base.mvp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.common_module.base.BaseActivity;


/**
 * mvp
 */

@SuppressWarnings("unchecked")
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity {


    //通过Dagger2注入的其实是Mvp中的 Presenter
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = initInjector();
        //绑定View
        mPresenter.attachView(this);
        initData();
    }

    /**
     * 加载数据
     */
    protected abstract void initData();



    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 完成注入并返回注入的Presenter对象
     *
     * @return
     */
    protected abstract T initInjector();

    /**
     * 解绑
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }

}
