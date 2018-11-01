package test.com.myapplication;

/**
 * Created by wen on 2018/10/20.
 */

public enum OrderStatusType {
    TAKE(0, "待取件"),

    SEND(1, "已派件");

    private int status;

    private String des;

    OrderStatusType(int status, String des) {
        this.status = status;
        this.des = des;
    }

    public String des() {
        return this.des;
    }

    public int status() {
        return this.status;
    }

}
