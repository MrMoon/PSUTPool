package com.psut.pool.Shared;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

abstract public class Online {

    public static boolean isOnline(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        if (appProcessInfos != null)
            for (final ActivityManager.RunningAppProcessInfo processInfo : appProcessInfos)
                if (processInfo.processName.equals(packageName)) return true;
        return false;
    }
}
