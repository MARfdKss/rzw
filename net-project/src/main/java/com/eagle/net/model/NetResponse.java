package com.eagle.net.model;

import java.io.Serializable;

public class NetResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public int status;
    public String err;
    public T data;
    public boolean success;

}
