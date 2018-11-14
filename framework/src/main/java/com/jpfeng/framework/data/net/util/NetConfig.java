package com.jpfeng.framework.data.net.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * 网络设置类
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/4
 */
public class NetConfig implements Serializable {
    public static final int DEFAULT_CONNECT_TIMEOUT = 15;
    public static final int DEFAULT_READ_TIMEOUT = 10;
    public static final int DEFAULT_WRITE_TIMEOUT = 10;
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;
    public static final String NET_TAG = "App/NetLog";

    private int mConnectTimeout;
    private int mReadTimeout;
    private int mWriteTimeout;
    private TimeUnit mTimeUnit;
    private String mBaseUrl;
    private List<Interceptor> mHttpInterceptors;

    private NetConfig() {
        mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
        mReadTimeout = DEFAULT_READ_TIMEOUT;
        mWriteTimeout = DEFAULT_WRITE_TIMEOUT;
        mTimeUnit = DEFAULT_TIME_UNIT;
        mBaseUrl = "";
        mHttpInterceptors = new ArrayList<>();
    }

    private NetConfig(NetConfig config) {
        mConnectTimeout = config.connectTimeout();
        mReadTimeout = config.readTimeout();
        mWriteTimeout = config.writeTimeout();
        mTimeUnit = config.timeUnit();
        mBaseUrl = config.baseUrl();
        mHttpInterceptors = new ArrayList<>(config.httpInterceptors());
    }

    public static NetConfig copy(NetConfig source) {
        return new NetConfig(source);
    }

    public int connectTimeout() {
        return mConnectTimeout;
    }

    public int readTimeout() {
        return mReadTimeout;
    }

    public int writeTimeout() {
        return mWriteTimeout;
    }

    public TimeUnit timeUnit() {
        return mTimeUnit;
    }

    public String baseUrl() {
        return mBaseUrl;
    }

    public List<Interceptor> httpInterceptors() {
        return mHttpInterceptors;
    }

    public static class Builder {
        private final NetConfig mConfig;

        public Builder() {
            mConfig = new NetConfig();
        }

        public Builder setConnectTimeout(int connectTimeout) {
            mConfig.mConnectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            mConfig.mReadTimeout = readTimeout;
            return this;
        }

        public Builder setWriteTimeout(int writeTimeout) {
            mConfig.mWriteTimeout = writeTimeout;
            return this;
        }

        public Builder setTimeUnit(TimeUnit timeUnit) {
            mConfig.mTimeUnit = timeUnit;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            mConfig.mBaseUrl = baseUrl;
            return this;
        }

        public Builder addHttpInterceptors(List<Interceptor> httpInterceptors) {
            mConfig.mHttpInterceptors.addAll(httpInterceptors);
            return this;
        }

        public Builder addHttpInterceptor(Interceptor httpInterceptors) {
            mConfig.mHttpInterceptors.add(httpInterceptors);
            return this;
        }

        public NetConfig build() {
            return mConfig;
        }
    }
}
