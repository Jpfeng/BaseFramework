package com.jpfeng.baseframework;

import com.jpfeng.framework.base.BaseApplication;
import com.jpfeng.framework.data.net.NetClient;
import com.jpfeng.framework.data.net.util.NetConfig;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class DemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetConfig config = new NetConfig.Builder().setBaseUrl("https://gank.io/api/").build();
        NetClient.init(config);
    }
}
