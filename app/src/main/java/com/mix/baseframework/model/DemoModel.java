package com.mix.baseframework.model;

import android.support.annotation.NonNull;

import com.mix.baseframework.net.DemoApi;
import com.mix.framework.data.model.IModelCallBack;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class DemoModel extends BaseModelImpl<DemoApi> {
    @NonNull
    @Override
    protected Class<DemoApi> getService() {
        return DemoApi.class;
    }

    public ResourceSubscriber<String> getTips(IModelCallBack<String> callback) {
        // 网络请求模板
        return request(mService.getSearchTips(), callback);
    }
}
