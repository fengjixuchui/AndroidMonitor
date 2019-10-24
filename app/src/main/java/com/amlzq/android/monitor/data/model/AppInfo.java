package com.amlzq.android.monitor.data.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by amlzq on 2017/5/23.
 * APP信息
 */

public class AppInfo implements Parcelable {

    // 声明静态常量
    public static final int SYSTEM = 0; // 系统
    public static final int USER = 1; // 用户

    //用一个@IntDef({})将其全部变量包含，其次需要一个Retention声明其保留级别，最后定义其接口名称
    //需要support-annotations library
    @IntDef({SYSTEM, USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataType {
    }

    public String packageName;
    public Drawable icon; // 应用图标
    public String label; // 发射器活动标签
    public int versionCode;
    public String versionName;
    public String location;
    public String signatureByMD5;
    public String signatureBySHA1;
    public String signatureBySHA256;
    public String apkPath;
    public long cacheSize; //缓存大小
    public long codeSize; //数据大小
    public long dataSize; //应用程序大小
    public long totalSize; // 应用大小合计
    public Intent intent; // 应用安装包的Intent，用于启动应用
    @DataType
    public int type;

    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        packageName = in.readString();
        label = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
        location = in.readString();
        signatureByMD5 = in.readString();
        signatureBySHA1 = in.readString();
        signatureBySHA256 = in.readString();
        apkPath = in.readString();
        cacheSize = in.readLong();
        codeSize = in.readLong();
        dataSize = in.readLong();
        totalSize = in.readLong();
        intent = in.readParcelable(Intent.class.getClassLoader());
        type = in.readInt();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(label);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(location);
        dest.writeString(signatureByMD5);
        dest.writeString(signatureBySHA1);
        dest.writeString(signatureBySHA256);
        dest.writeString(apkPath);
        dest.writeLong(cacheSize);
        dest.writeLong(codeSize);
        dest.writeLong(dataSize);
        dest.writeLong(totalSize);
        dest.writeParcelable(intent, flags);
        dest.writeInt(type);
    }

}