package com.example.common_module.base;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common_module.base.mvp.BaseView;
import com.example.common_module.view.LoadingPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 功能   ： 所有Fragment的基类
 */

public abstract class BaseFragment extends Fragment implements BaseView {

    protected BaseActivity mBaseActivity;
    LoadingPager mLoadingPager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLoadingPager == null) {
            mLoadingPager = new LoadingPager(getContext()) {
                @Override
                protected View createSuccessView() {
                    return createSuccessView();
                }

                @Override
                protected void load() {
                    BaseFragment.this.load();
                }
            };
        }
        return mLoadingPager;
    }

    /**
     * 调用父类的方法(请求网络 修改状态)
     */
    public void show() {
        if (mLoadingPager != null) {
            mLoadingPager.show();
        }
    }

    /**
     * 调用父类的方法(设置状态)
     *
     * @param result
     */
    public void setState(LoadingPager.LoadResult result) {
        if (mLoadingPager != null) {
            mLoadingPager.setState(result);
        }
    }

    /**
     * 加载成功的界面
     *
     * @return 显示子类相应的布局
     */
    protected abstract View createSuccessView();

    /**
     * 请求网络 根据结果传入相应状态
     */
    protected abstract void load();

    @Override
    public void showToast(String msg) {
        mBaseActivity.showToast(msg);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
}
