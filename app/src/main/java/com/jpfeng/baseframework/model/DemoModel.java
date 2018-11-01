package com.jpfeng.baseframework.model;

import com.jpfeng.baseframework.bean.ResultBean;
import com.jpfeng.baseframework.net.DemoApi;
import com.jpfeng.framework.data.model.IModelCallback;

import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class DemoModel extends BaseModelImpl<DemoApi> {

    public ResourceSubscriber<Map<String, List<ResultBean>>> getToday(IModelCallback<Map<String, List<ResultBean>>> callback) {
        // 网络请求模板
        return request(mService.getToday(), callback);
    }
}
