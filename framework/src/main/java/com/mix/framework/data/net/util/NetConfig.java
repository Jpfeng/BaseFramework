package com.mix.framework.data.net.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * 网络设置类
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/4
 */
public class NetConfig {
    private static final int DEFAULT_CONNECT_TIMEOUT = 15;
    private static final int DEFAULT_READ_TIMEOUT = 10;
    private static final int DEFAULT_WRITE_TIMEOUT = 10;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    public static final String LOG_TAG = "App/NetLog";

    private int mConnectTimeout;
    private int mReadTimeout;
    private int mWriteTimeout;
    private TimeUnit mTimeUnit;
    private String mBaseUrl;
    private List<Interceptor> mHttpInterceptors;

    public NetConfig(String baseUrl) {
        mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
        mReadTimeout = DEFAULT_READ_TIMEOUT;
        mWriteTimeout = DEFAULT_WRITE_TIMEOUT;
        mTimeUnit = DEFAULT_TIME_UNIT;
        mBaseUrl = baseUrl;
        mHttpInterceptors = new ArrayList<>();
    }

    public NetConfig(NetConfig config) {
        mConnectTimeout = config.connectTimeout();
        mReadTimeout = config.readTimeout();
        mWriteTimeout = config.writeTimeout();
        mTimeUnit = config.timeUnit();
        mBaseUrl = config.baseUrl();
        mHttpInterceptors = new ArrayList<>(config.httpInterceptors());
    }

    public int connectTimeout() {
        return mConnectTimeout;
    }

    public NetConfig setConnectTimeout(int connectTimeout) {
        mConnectTimeout = connectTimeout;
        return this;
    }

    public int readTimeout() {
        return mReadTimeout;
    }

    public NetConfig setReadTimeout(int readTimeout) {
        mReadTimeout = readTimeout;
        return this;
    }

    public int writeTimeout() {
        return mWriteTimeout;
    }

    public NetConfig setWriteTimeout(int writeTimeout) {
        mWriteTimeout = writeTimeout;
        return this;
    }

    public TimeUnit timeUnit() {
        return mTimeUnit;
    }

    public NetConfig setTimeUnit(TimeUnit timeUnit) {
        mTimeUnit = timeUnit;
        return this;
    }

    public String baseUrl() {
        return mBaseUrl;
    }

    public NetConfig setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public List<Interceptor> httpInterceptors() {
        return mHttpInterceptors;
    }

    public NetConfig addHttpInterceptors(List<Interceptor> httpInterceptors) {
        mHttpInterceptors.addAll(httpInterceptors);
        return this;
    }

    public NetConfig addHttpInterceptor(Interceptor httpInterceptors) {
        mHttpInterceptors.add(httpInterceptors);
        return this;
    }
}
