package com.amlzq.android.monitor;

/**
 * Created by amlzq on 2017/10/16.
 * <p>
 * Constants.java
 * C.java
 * <p>
 * 常量：修改小，项目确定这些都是不可变的常量
 * 1.控件常量
 * 2.代码常量
 */

public class MyConstant {

    public static final String ABOUT_US = "https://github.com/amlzq/AndroidTools/blob/master/README.html";

    // 一级fragment处理业务的 ui tag
    /**
     * 支付
     */
    public static final String DESTINATION_PAY = "DESTINATION_PAY";
    /**
     * 登录
     */
    public static final String DESTINATION_LOGIN = "DESTINATION_LOGIN";
    /**
     * 检查更新
     */
    public static final String DESTINATION_UPGRADE = "DESTINATION_UPGRADE";
    /**
     * Wap支付
     */
    public static final String DESTINATION_WAPPAY = "DESTINATION_WAPPAY";

    // startActivityForResult requestCode
    public static final int REQUESTCODE_ACTIVITY = 1001;

    public static final int REQUESTCODE_PAYMENT = 1002;

    public static final int REQUESTCODE_SETPWD = 1003;

    // Business scene

    public static final String SCENE_NORMAL = "SCENE_NORMAL";// 正常入口，手动点击

}
