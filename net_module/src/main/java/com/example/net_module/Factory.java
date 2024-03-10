package com.example.net_module;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;


import com.example.common_module.App;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 功能   ：提供上传逻辑需要的参数
 */

public class Factory {
    private static final String TAG = "Factory";
    //单例模式
    private static final Factory instance;
    //全局的线程池
    private final Executor mExecutor;
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //创建一个4个线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);
        mGson = new GsonBuilder()
                //设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 设置一个过滤器，数据库级别的Model(BaseModel)不进行Json转换
//                .setExclusionStrategies(new DBFlowExclusionStrategies())
                .create();

    }

    /**
     * Factory 中的初始化
     * 主要是SP 与DBflow的初始化
     */
    public static void setup() {

        //DBflow的初始化


        // 持久化的数据进行初始化SP
    }



    /**
     * 异步执行的方法
     *
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单例 拿到线程池 然后异步执行
        instance.mExecutor.execute(runnable);
    }

    /**
     * 返回一个全局的Gson 在这里可以进行Gson的一些全局的初始化
     *
     * @return
     */
    public static Gson getGson() {
        return instance.mGson;
    }



    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {
        Intent intent = new Intent("com.example.broadcastbestpracrice.FORCE_OFFLINE");
        App.getContext().sendBroadcast(intent);

    }

    /**
     * 处理推送来的消息
     */


    /**
     * 这个是真正的获取指定包名的应用程序是否在运行(无论前台还是后台)
     *
     * @return true已启动
     */
    public static boolean getCurrentTask(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appProcessInfos = activityManager.getRunningTasks(50);
        for (ActivityManager.RunningTaskInfo process : appProcessInfos) {

            if (process.baseActivity.getPackageName().equals(context.getPackageName())
                    || process.topActivity.getPackageName().equals(context.getPackageName())) {

                return true;
            }
        }
        return false;
    }

}
