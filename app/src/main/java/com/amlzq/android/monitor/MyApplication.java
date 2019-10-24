package com.amlzq.android.monitor;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.data.model.AppInfo;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.PackageUtil;
import com.amlzq.android.util.SystemUtil;

import java.util.ArrayList;

/**
 * Created by amlzq on 2017/5/22.
 * 全局应用类
 */

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";

    public ArrayList<AppInfo> mAllApps;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this);
        MyConfig.init(this);
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());

        AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAllApps = getInstalledPackages();
            }
        });
    }

    /**
     * @return 所有应用
     */
    public ArrayList<AppInfo> getInstalledPackages() {
        ArrayList<AppInfo> apps = new ArrayList<AppInfo>();
        PackageManager pm = PackageUtil.getPackageManager();

        for (PackageInfo pi : SystemUtil.getInstalledPackages()) {
            AppInfo appInfo = new AppInfo();
            appInfo.packageName = pi.packageName;
            appInfo.versionCode = pi.versionCode;
            appInfo.versionName = pi.versionName;
            appInfo.label = pi.applicationInfo.loadLabel(pm).toString();
            appInfo.intent = pm.getLaunchIntentForPackage(pi.packageName);
            try {
                appInfo.icon = pm.getApplicationIcon(pi.packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 非系统应用
                appInfo.type = AppInfo.USER;
            } else {
                // 系统应用
                appInfo.type = AppInfo.SYSTEM;
            }
            appInfo.signatureByMD5 = PackageUtil.getSignatureDigest(pi.packageName);
            appInfo.signatureBySHA1 = PackageUtil.getSignatureBySHA1(pi.packageName);
            appInfo.signatureBySHA256 = PackageUtil.getSignatureBySHA256(pi.packageName);
//            List<Long> sizes = PackageUtil.getPackageSize(pi.packageName);
//            appInfo.setCacheSize(sizes.get(0));
//            appInfo.setDataSize(sizes.get(1));
//            appInfo.setCodeSize(sizes.get(2));
//            appInfo.setTotalSize(sizes.get(3));
            apps.add(appInfo);
        }
        return apps;
    }

}