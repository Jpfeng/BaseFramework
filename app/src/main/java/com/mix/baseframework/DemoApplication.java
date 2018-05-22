package com.mix.baseframework;

import com.mix.framework.base.BaseApplication;
import com.mix.framework.data.net.NetClient;
import com.mix.framework.data.net.util.NetConfig;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class DemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        NetConfig config = new NetConfig("http://127.0.0.1/");
        NetClient.init(config);
    }
}
