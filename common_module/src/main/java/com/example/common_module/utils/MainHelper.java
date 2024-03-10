package com.example.common_module.utils;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MainHelper {
    private static Handler handler = new Handler(Looper.getMainLooper());


    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void remove(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    public static void postDelay(long delayMills, Runnable runnable) {
        handler.postDelayed(runnable, delayMills);
    }

    public static void sendAtFrontOfQueue(Runnable runnable) {
        Message msg = Message.obtain(handler, runnable);
        handler.sendMessageAtFrontOfQueue(msg);
    }
}
