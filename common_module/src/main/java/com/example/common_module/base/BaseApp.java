package com.example.common_module.base;

import android.os.Handler;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.Utils;
import com.example.common_module.App;

public class BaseApp extends App {
    public static int mMainThreadId;
    public static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());
        LogUtils.init(true,false,'v',"TAG");
        mHandler=new Handler();


    }
    /**
     * 返回主线程的pid
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }
    /**
     * 返回Handler
     * @return
     */
    public static Handler getHandler() {
        return mHandler;
    }
}
