package com.jpfeng.framework.data.net.util;

/**
 * 错误处理类
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/18
 */
public interface ErrorParser {
    void parse(Throwable t, NetError netError);
}
