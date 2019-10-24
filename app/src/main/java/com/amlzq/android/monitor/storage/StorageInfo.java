package com.amlzq.android.monitor.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.amlzq.android.content.ContextHolder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amlzq on 2017/8/14.
 * 存储信息
 * 一些山寨机的存储路径是不规范的
 */

@SuppressWarnings("unused")
public class StorageInfo {
    public String path;
    public String state;
    public boolean isRemoveable;

    public StorageInfo(String path) {
        this.path = path;
    }

    public boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * @return 获取可用的存储介质
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static List<StorageInfo> listAvaliableStorage() {
        Context context = ContextHolder.getContext();
        ArrayList<StorageInfo> storages = new ArrayList<>();
        StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            getVolumeList.setAccessible(true);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(manager, params);
            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);
                    info = new StorageInfo(path);
                    File file = new File(info.path);
                    if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                        Method isRemovable = obj.getClass().getMethod("isRemovable", new Class[0]);
                        String state = null;
                        try {
                            Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                            state = (String) getVolumeState.invoke(manager, info.path);
                            info.state = state;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (info.isMounted()) {
                            info.isRemoveable = ((Boolean) isRemovable.invoke(obj, new Object[0])).booleanValue();
                            // System.out.println(i + "=EE=" + info.path +
                            // "=EE=" + info.isMounted());
                            storages.add(info);
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        storages.trimToSize();
        return storages;
    }

}