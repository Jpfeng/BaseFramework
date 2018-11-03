package com.jpfeng.framework.data.net.util;

/**
 * 缺少必要的泛型参数
 * <p>
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/11/1
 */
public class MissingGenericException extends RuntimeException {
    public MissingGenericException() {
    }

    public MissingGenericException(String message) {
        super(message);
    }

    public MissingGenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingGenericException(Throwable cause) {
        super(cause);
    }
}
