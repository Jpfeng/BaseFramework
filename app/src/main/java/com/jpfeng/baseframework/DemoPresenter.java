package com.jpfeng.baseframework;

import com.jpfeng.baseframework.model.DemoModel;
import com.jpfeng.framework.base.mvp.BasePresenter;
import com.jpfeng.framework.data.model.IModelCallback;
import com.jpfeng.framework.data.model.ModelManager;
import com.jpfeng.framework.data.net.NetClient;
import com.jpfeng.framework.data.net.util.NetConfig;
import com.jpfeng.baseframework.util.ToastUtils;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
public class DemoPresenter extends BasePresenter<DemoContract.View> implements DemoContract.Presenter {
    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        loadData();
    }

    private void loadData() {
        ModelManager.get(DemoModel.class).getTips(new IModelCallback<String>() {
            @Override
            public void onStart() {
                mView.showPageLoading();
            }

            @Override
            public void onSuccess(String data) {
                mView.showTip(data);
                mView.showPageContent();
            }

            @Override
            public void onError(String msg) {
                mView.showPageError(msg);
            }
        });
    }

    @Override
    public void updateModel() {
        NetConfig config = new NetConfig("http://47.74.244.196/v1/");
        NetClient.getInstance().updateConfig(config);
        ToastUtils.show("config updated");
        loadData();
    }
}
