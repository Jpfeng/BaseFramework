package com.jpfeng.framework.data.model;

import com.jpfeng.framework.data.net.util.NetError;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public interface IModelCallback<T> {
    /**
     * 请求开始。
     * 可在此处进行显示加载中等操作。
     */
    void onStart();
    /**
     * 请求成功的回调。
     *
     * @param data 返回的数据
     */
    void onSuccess(T data);
    /**
     * 请求出错的回调。
     *
     * @param error 包装的错误信息，可直接显示给用户。
     */
    void onError(NetError error);
    /**
     * 请求结束。
     * 不管成功与失败均会回调。
     *
     * @param success 是否成功
     */
    void onComplete(boolean success);
}
