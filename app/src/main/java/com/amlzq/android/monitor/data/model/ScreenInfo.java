package com.amlzq.android.monitor.data.model;

/**
 * 屏幕信息
 */

public class ScreenInfo {

    public static final int TEXT = 1;
    public static final int IMAGE = 2;

    public String id = "";
    public String content = "";
    public String details = "";
    public int itemType;

    public ScreenInfo(String id, String content, int itemType) {
        this.id = id;
        this.content = content;
        this.itemType = itemType;
    }

    public ScreenInfo(String id, String content) {
        this.id = id;
        this.content = content;
        this.itemType = TEXT;
    }

    public int getItemType() {
        return itemType;
    }

    public String share() {
        return id + ":" + content + "\n";
    }

    @Override
    public String toString() {
        return "ScreenInfo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", details='" + details + '\'' +
                ", itemType=" + itemType +
                '}';
    }

}
