package com.jpfeng.baseframework;

import com.jpfeng.baseframework.bean.ResultBean;
import com.jpfeng.baseframework.model.DemoModel;
import com.jpfeng.framework.base.mvp.BasePresenter;
import com.jpfeng.framework.data.model.IModelCallback;
import com.jpfeng.framework.data.model.ModelManager;
import com.jpfeng.framework.data.net.util.NetError;
import com.jpfeng.framework.util.Logger;

import java.util.List;
import java.util.Map;

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
        makeRequest(ModelManager.get(DemoModel.class)
                .getToday(new IModelCallback<Map<String, List<ResultBean>>>() {
                    @Override
                    public void onStart() {
                        mView.showPageLoading();
                    }

                    @Override
                    public void onSuccess(Map<String, List<ResultBean>> data) {
//                        mView.showTip(data);
                        Logger.netDebug(data.toString());
                        mView.showPageContent();
                    }

                    @Override
                    public void onError(NetError error) {
                        mView.showPageError(error.getMessage());
                    }

                    @Override
                    public void onComplete(boolean success) {
                    }
                }));
    }
}
