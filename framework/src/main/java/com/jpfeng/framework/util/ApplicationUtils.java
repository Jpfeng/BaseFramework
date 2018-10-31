package com.jpfeng.framework.util;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/26
 */
public class ApplicationUtils {

    private ApplicationUtils() {
    }

    /**
     * 检查一个对象是否为 null 。如果为空直接抛出异常。
     *
     * @param target 检查目标
     * @param <T>    目标的泛型
     * @return 目标对象原样返回
     */
    public static <T> T checkNonNull(T target) {
        if (null == target) {
            throw new RuntimeException("target is NULL!");
        }
        return target;
    }
}
