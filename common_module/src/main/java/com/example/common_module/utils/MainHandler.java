package com.example.common_module.utils;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MainHandler {
    private static final Handler handler = new Handler(Looper.getMainLooper());


    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void remove(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    public static void postDelay(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 插入消息队列队首  使得这条消息立马执行
     */
    public static void sendAtFrontOfQueue(Runnable runnable) {
        Message msg = Message.obtain(handler, runnable);
        handler.sendMessageAtFrontOfQueue(msg);
    }
}
