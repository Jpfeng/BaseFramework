package com.mix.framework.data.net.util;

/**
 * 错误处理类
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/18
 */
public interface ErrorResolver {
    String resolveError(Throwable t);
}
