package com.jpfeng.baseframework.net;

/**
 * 服务器错误
 * <p>
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/4/26
 */
public class ServerException extends Exception {
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }
}
