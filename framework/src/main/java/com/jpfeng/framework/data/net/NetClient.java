package com.jpfeng.framework.data.net;

import com.jpfeng.framework.data.model.ModelManager;
import com.jpfeng.framework.data.net.util.NetConfig;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 网络连接管理类
 * <p>
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/18
 */
public class NetClient {

    private static volatile NetClient mInstance;
    private NetConfig mConfig;

    private NetClient(NetConfig config) {
        mConfig = config;
    }

    /**
     * 初始化。必须首先调用初始化并传入 {@link NetConfig}
     *
     * @param config 设置
     */
    public static void init(@NonNull NetConfig config) {
        if (null == mInstance) {
            synchronized (NetClient.class) {
                if (null == mInstance) {
                    mInstance = new NetClient(config);
                } else {
                    throw new IllegalStateException("NetClient has already been initialized.");
                }
            }
        } else {
            throw new IllegalStateException("NetClient has already been initialized.");
        }
    }

    /**
     * 获取实例。必须在 {@link #init(NetConfig)} 方法之后调用
     *
     * @return 实例
     */
    public static NetClient getInstance() {
        if (null == mInstance) {
            throw new IllegalStateException("NetClient has not yet been initialized.");
        }
        return mInstance;
    }

    /**
     * 获取 httpClient 实例
     *
     * @return httpClient 实例
     */
    public OkHttpClient getHttpClient() {
        return HttpClient.getInstance();
    }

    /**
     * 获取 retrofitClient 实例
     *
     * @return retrofitClient 实例
     */
    public Retrofit getRetrofitClient() {
        return RetrofitClient.getInstance();
    }

    /**
     * 更新网路设置
     *
     * @param config 新的网络设置
     */
    public void updateConfig(NetConfig config) {
        mConfig = config;
        HttpClient.reconstruct();
        RetrofitClient.reconstruct();
        ModelManager.reconstructModels();
    }

    NetConfig getConfig() {
        return mConfig;
    }
}
