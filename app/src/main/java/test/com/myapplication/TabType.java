package test.com.myapplication;

import android.text.TextUtils;

/**
 * Created by wen on 2018/10/20.
 */

public enum TabType {
    TAKE("0", "取件", TabType.from("0")),

    SEND("1", "派件", TabType.from("1"));

    private String type;

    private String title;

    private int[] status;

    TabType(String type, String title, int[] status) {
        this.type = type;
        this.title = title;
        this.status = status;
    }

    public String type() {
        return this.type;
    }

    public String title() {
        return this.title;
    }

    public int[] status() {
        return this.status;
    }

    public static int[] from(String type) {
        int[] status = null;
        if (TextUtils.equals(type, TabType.TAKE.type())) {
            status = new int[]{OrderStatusType.SEND.status(), OrderStatusType.TAKE.status()};
        } else if (TextUtils.equals(type, TabType.SEND.type())) {
            status = new int[]{OrderStatusType.SEND.status()};
        }
        return status;
    }
}
