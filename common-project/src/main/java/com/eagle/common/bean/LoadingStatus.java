package com.eagle.common.bean;

public enum LoadingStatus {
    LOADING(0, "加载中"),
    FAIL_LOADING(1, "加载失败"),
    SUCCESS(2, "成功"),
    EMPTY(3, "空页面");

    private int value;

    private String desc;

    LoadingStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }

}
