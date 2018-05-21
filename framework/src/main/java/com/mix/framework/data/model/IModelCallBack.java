package com.mix.framework.data.model;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/8
 */
public interface IModelCallBack<T> {
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
     * @param msg 包装的错误信息，可直接显示给用户。
     */
    void onError(String msg);
}
