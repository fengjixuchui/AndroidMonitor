package com.amlzq.android.monitor;

import android.app.Application;

import com.amlzq.android.crash.CrashHandler;
import com.amlzq.android.log.Log;
import com.amlzq.android.log.LogWrapper;
import com.amlzq.android.util.PrefsUtil;
import com.amlzq.android.util.UtilConfig;

/**
 * Created by amlzq on 2017/5/23.
 * 配置类
 */

public final class MyConfig {

    /**
     * 项目标识
     */
    public static final String IDENTIFY = "AndroidTools";

    /**
     * 调试开关
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void init(Application app) {

        // 日志库
        Log.TAG = IDENTIFY;
        Log.LEVEL = Log.VERBOSE;
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // 工具库
        UtilConfig.DEBUG = DEBUG;
        UtilConfig.init(app, IDENTIFY);

        // 设置项目SP存储文件名
        PrefsUtil.FILE_NAME = IDENTIFY;

        // 崩溃处理类，需要存储权限
        if (!DEBUG) CrashHandler.getInstance().init();
    }

}