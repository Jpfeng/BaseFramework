package com.jpfeng.framework.data.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/7
 */
class RetrofitClient {

    /**
     * 全局的 RetrofitClient, 用于 http 请求。
     */
    private static volatile Retrofit mInstance;

    public static Retrofit getInstance() {
        if (null == mInstance) {
            synchronized (RetrofitClient.class) {
                if (null == mInstance) {
                    mInstance = createInstance();
                }
            }
        }
        return mInstance;
    }

    private static Retrofit createInstance() {
        return new Retrofit.Builder()
                .baseUrl(NetClient.getInstance().getConfig().baseUrl())
                .client(NetClient.getInstance().getHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static synchronized void reconstruct() {
        mInstance = createInstance();
    }
}
