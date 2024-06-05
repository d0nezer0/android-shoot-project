package org.ggxz.shoot.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.util.Log;



/**
 * 资源监控类
 */
public class ResourceMonitor {
    /**
     * 内存监控
     */
    public static void printMemoryUsage(Context context,String ClassName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
        System.out.println("Total Private Dirty: " + memoryInfoArray[0].getTotalPrivateDirty() + " KB");
        System.out.println("Total Pss: " + memoryInfoArray[0].getTotalPss() + " KB");
        System.out.println("Total Shared Dirty: " + memoryInfoArray[0].getTotalSharedDirty() + " KB");
        LogUtilsResourceMonitor.i(ClassName,"Total Private Dirty: " + memoryInfoArray[0].getTotalPrivateDirty() + " KB --Total Pss: "+ memoryInfoArray[0].getTotalPss() + " KB --Total Shared Dirty: "+ memoryInfoArray[0].getTotalSharedDirty() + " KB");

        if (memoryInfo.lowMemory) {
            System.out.println("Low Memory: True");
        } else {
            System.out.println("Low Memory: False");
        }
    }
}
