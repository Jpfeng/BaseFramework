package com.jpfeng.baseframework.net;

/**
 * Author: Jpfeng
 * E-mail: fengjupeng@whale.ws
 * Date: 2018/11/1
 */
public class ErrorCode {
    private ErrorCode() {
    }

    public static final int NET_ERR_UNKNOWN = 0;
    public static final int NET_ERR_TIMEOUT = 1;
    public static final int NET_ERR_NETWORK = 2;
    public static final int NET_ERR_HTTP = 3;
    public static final int NET_ERR_PARSE = 4;
    public static final int NET_ERR_SERVER = 4;
}
