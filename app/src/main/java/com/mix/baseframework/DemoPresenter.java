package com.mix.baseframework;

import com.mix.baseframework.model.DemoModel;
import com.mix.framework.data.model.BaseModel;
import com.mix.framework.base.mvp.BasePresenter;
import com.mix.framework.data.model.IModelCallBack;
import com.mix.framework.data.model.ModelManager;
import com.mix.framework.data.net.NetClient;
import com.mix.framework.data.net.util.NetConfig;
import com.mix.framework.util.ToastUtils;

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
        ModelManager.get(DemoModel.class).getTips(new IModelCallBack<String>() {
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
