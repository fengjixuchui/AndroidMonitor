package com.amlzq.android.monitor.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by amlzq on 2017/8/14.
 * 存储工具
 * <p>
 * https://github.com/changjiashuai/Storage
 */

public class StorageUtil {

    /**
     * @hide 隐藏
     */
    private StorageUtil() {
    }

    private static String mAppRootPath = "";

    /**
     * @param dirName 目录名
     * @return 项目根基路径
     */
    public static String setAppRootPath(String dirName) {
        String rootPath = getAvaliableStorage();
        mAppRootPath = rootPath + File.separator + dirName;
        return mAppRootPath;
    }

    public static String getAppRootPath() {
        return mAppRootPath;
    }

    /**
     * @return 崩溃目录
     */
    @Deprecated
    public static File getCrash() {
        String path = getAppRootPath() + File.separator + "Crash";
        return newDir(path);
    }

    /**
     * @return 下载目录
     */
    @Deprecated
    public static File getDownload() {
        String path = getAppRootPath() + File.separator + "Download";
        return newDir(path);
    }

    /**
     * @return 图片目录
     */
    @Deprecated
    public static File getPicture() {
        String path = getAppRootPath() + File.separator + "Picture";
        return newDir(path);
    }

    /**
     * @return 临时目录
     */
    @Deprecated
    public static File getTemp() {
        String path = getAppRootPath() + File.separator + "Temp";
        return newDir(path);
    }

    /**
     * @param path 路径
     * @return 新建目录
     */
    public static File newDir(String path) {
        File dir = new File(path);
        boolean wasSuccessful = true;
        if (!dir.exists()) {
            wasSuccessful = dir.mkdirs();
        }
        if (!wasSuccessful) {
            Log.w("mkdirs was not successful.");
        }
        return dir;
    }

    /**
     * @param dir      目录名
     * @param fileName 文件名
     */
    public static boolean deleteFile(File dir, String fileName) {
        File file = new File(dir.getAbsolutePath() + File.separator + fileName);
        boolean wasSuccessful = false;
        if (file.exists()) {
            wasSuccessful = file.delete();
        }
        if (!wasSuccessful) {
            Log.w("delete not successful.");
        }
        return wasSuccessful;
    }

    /**
     * @return 可用存储大小
     */
    public static long getAvailableBytes() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } else {
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        }
        return bytesAvailable;
    }

    /**
     * @return Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @return Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @param bmp       bitmap
     * @param storePath store path
     * @return 保存文件到指定路径
     */
    public static boolean saveImage(Bitmap bmp, String storePath) {
        // 首先保存图片
        Context context = ContextHolder.getContext();
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            // 通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // 把文件插入到系统图库
            // MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
            // MediaStore.Images.Media.insertImage(context.getContentResolver(), "image path", "title", "description");

            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param bmp bitmap
     * @return 保存文件到相册
     */
    public static boolean saveImage2Gallery(Bitmap bmp) {
        return true;
    }

    /**
     * @param bmp bitmap
     * @return 保存文件到AppPictures
     */
    public static boolean saveImage2AppPictures(Bitmap bmp) {
        Context context = ContextHolder.getContext();
        boolean isSuccess = false;
        // 首先保存图片
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(getPicture(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getPicture().getPath())));
        return isSuccess;
    }

    /**
     * 保存方法
     *
     * @param bmp     bitmap
     * @param picName 图片名
     * @param path    路径
     */
    public static void saveBitmap(Bitmap bmp, String picName, String path) {
        File f = new File(path, picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =======================
    // Storage Info
    // =======================

    /**
     * @return 获取所有挂载的存储介质
     */
    public static String[] getVolumePaths() {
        Context context = ContextHolder.getContext();
        StorageManager mStorageManager = null;
        Method mMethodGetPaths = null;
        if (context != null) {
            mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            try {
                mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        String[] paths = null;
        try {
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * @return 是否挂载可用存储介质(存储媒体已经挂载, 并且挂载点可读or写)<br>
     * false：表示没有可用存储<br>
     */
    public static boolean existStorage() {
        return getVolumePaths().length != 0;
    }

    /**
     * @return 返回任意一个挂载可用存储介质路径(存储媒体已经挂载, 并且挂载点可读或写)
     */
    public static String getAvaliableStorage() {
        List<StorageInfo> storages = StorageInfo.listAvaliableStorage();
        if (storages.size() > 0) {
            return storages.get(0).path;
        }
        return Environment.getExternalStorageDirectory().toString();
    }

    /**
     * @return 是否安装外置储存(存储媒体已经挂载, 并且挂载点可读或写)
     */
    public static boolean existExternalStorage() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * @param path 路径
     * @return 路径是否可用(存储媒体已经挂载, 并且挂载点可读或写)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean existPath(File path) {
        if (Environment.getExternalStorageState(path).equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * @return 存储大小
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * @return 存储大小
     */
    @SuppressWarnings("deprecation")
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    // 在android2.3中，判断内置SD卡是否挂载：
    // if
    // (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    // {
    // 为true的话，内置sd卡存在
    // }

    // 判断外置SD卡是否挂载：
    // if
    // (Environment.getStorageState(Environment.STORAGE_PATH_SD2).equals(Environment.MEDIA_MOUNTED))
    // {
    // 为true的话，外置sd卡存在
    // }

    @TargetApi(Build.VERSION_CODES.O)
    public static void getStorageVolume(final String packageName) {
//        Context cxt = ContextHolder.getContext();
//        final StorageStatsManager storageStatsManager = (StorageStatsManager) cxt.getSystemService(Context.STORAGE_STATS_SERVICE);
//        final StorageManager storageManager = (StorageManager) cxt.getSystemService(Context.STORAGE_SERVICE);
//        final List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
//        final UserHandle user = Process.myUserHandle();
//        try {
//            final int uid = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (StorageVolume storageVolume : storageVolumes) {
//            final String uuidStr = storageVolume.getUuid();
//            final UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
//            try {
//                Log.d("AppLog", "storage:" + uuid + " : " + storageVolume.getDescription(cxt) + " : " + storageVolume.getState());
//                Log.d("AppLog", "getFreeBytes:" + Formatter.formatShortFileSize(cxt, storageStatsManager.getFreeBytes(uuid)));
//                Log.d("AppLog", "getTotalBytes:" + Formatter.formatShortFileSize(cxt, storageStatsManager.getTotalBytes(uuid)));
//
//                final StorageStats storageStats = storageStatsManager.queryStatsForUid(uuid, uid);
//                Log.d("AppLog", "getAppBytes:" + Formatter.formatShortFileSize(cxt, storageStats.getAppBytes()) +
//                        " getCacheBytes:" + Formatter.formatShortFileSize(cxt, storageStats.getCacheBytes()) +
//                        " getDataBytes:" + Formatter.formatShortFileSize(cxt, storageStats.getDataBytes()));
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
}