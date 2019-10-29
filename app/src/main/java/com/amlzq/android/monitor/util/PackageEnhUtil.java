package com.amlzq.android.monitor.util;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.content.pm.PackageStatsObserver;
import com.amlzq.android.util.PackageUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amlzq on 2019/10/25.
 * <p>
 * Package enhance util
 */

public class PackageEnhUtil {

    /**
     * @hide
     */
    PackageEnhUtil() {
    }

    /**
     * after invoking, PkgSizeObserver.onGetStatsCompleted() will be called as callback function. <br>
     * About the third parameter ‘Process.myUid() / 100000’，please check:
     * <android_source>/frameworks/base/core/java/android/content/pm/PackageManager.java:
     * getPackageSizeInfo(packageName, UserHandle.myUserId(), observer);
     */
    public static List<Long> getPackageSize(final String packageName) {
        List<Long> longs = new ArrayList<Long>();
        Context cxt = ContextHolder.getContext();
        PackageManager pm = PackageUtil.getPackageManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // requires the android.Manifest.permission#PACKAGE_USAGE_STATS permission
            StorageStatsManager storageStatsManager = (StorageStatsManager) cxt.getSystemService(Context.STORAGE_STATS_SERVICE);
            UserHandle userHandle = Process.myUserHandle();
            try {
                StorageStats storageStats = storageStatsManager.queryStatsForPackage(StorageManager.UUID_DEFAULT, packageName, userHandle);
                longs.add(storageStats.getCacheBytes());
                longs.add(storageStats.getDataBytes());
                longs.add(storageStats.getAppBytes());
                longs.add(storageStats.getCacheBytes() + storageStats.getDataBytes() + storageStats.getAppBytes());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 通过反射机制得到PackageManager类的隐藏函数getPackageSizeInfo
            // Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class, int.class, IPackageStatsObserver.class);
            // 调用该函数，并且给其分配参数 ，待调用流程完成后会回调Observer类的函数
            // getPackageSizeInfo.invoke(pm, packageName, observer);
            try {
                Method getPackageSizeInfo = pm.getClass().getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(pm, packageName, Process.myUid() / 100000, new PackageStatsObserver() {

                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        super.onGetStatsCompleted(pStats, succeeded);
                        longs.add(cacheSize);
                        longs.add(dataSize);
                        longs.add(codeSize);
                        longs.add(totalSize);
                    }
                });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return longs;
    }

}