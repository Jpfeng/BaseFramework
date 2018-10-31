package com.jpfeng.framework.data.net.util;

/**
 * 服务器返回的错误
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/26
 */
public class ApiException extends Exception {
    /**
     * 错误码
     */
    private int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
