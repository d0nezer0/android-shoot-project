package com.example.common_module.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.example.common_module.base.BaseApp;

/**
 * 功能   ： 界面相关的工具类
 */

public class UIUtils {
    /** dip转换px */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** 获取上下文 */
    public static Context getContext() {
        return BaseApp.getContext();
    }

    /** pxz转换dip */

    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Float px2dip(Context context,int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (px / scale + 0.5f);
    }
    public static Resources getResources() {
        return getContext().getResources();
    }

    /** 获取字符数组 */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /** 获取颜色id */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /** 根据id获取尺寸 */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 在主线程中执行代码
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (isRunOnMainThread()) {
            // 执行代码
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static void post(Runnable runnable) {
        Handler handler = getHandler();
        handler.post(runnable);
    }

    /** 移除一个执行的对象 */
    public static void removeCallBacks(Runnable r) {
        getHandler().removeCallbacks(r);
    }

    /** 延迟执行 */
    public static void postDelay(Runnable runnable, long delay) {
        Handler handler = getHandler();
        handler.postDelayed(runnable, delay);
    }

    private static Handler getHandler() {
        return BaseApp.getHandler();
    }

    public static boolean isRunOnMainThread() {
        return android.os.Process.myTid() == getMainThreadTid();
    }

    private static int getMainThreadTid() {
        return BaseApp.getMainThreadId();
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }
    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }
}
