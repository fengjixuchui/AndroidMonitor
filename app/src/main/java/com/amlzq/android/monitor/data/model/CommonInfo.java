package com.amlzq.android.monitor.data.model;

/**
 * Created by amlzq on 2018/8/29.
 * 通用信息
 */

public class CommonInfo {

    public String id = "";
    public String content = "";
    public String details = "";

    public CommonInfo(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    public CommonInfo(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String share() {
        return id + ":" + content + "\n";
    }

    @Override
    public String toString() {
        return "CommonInfo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

}