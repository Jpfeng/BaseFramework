package com.jpfeng.baseframework.ui;

import android.text.TextUtils;

import com.jpfeng.baseframework.bean.DemoBean;
import com.jpfeng.baseframework.model.DemoModel;
import com.jpfeng.framework.base.mvp.BasePresenter;
import com.jpfeng.framework.data.model.IModelCallback;
import com.jpfeng.framework.data.model.ModelManager;
import com.jpfeng.framework.data.net.util.NetError;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/21
 */
public class DemoPresenter extends BasePresenter<DemoContract.View> implements DemoContract.Presenter {

    private List<DemoBean> mData;

    @Override
    protected void onAttach() {
        super.onAttach();
        mData = new ArrayList<>();
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        loadData(false);
    }

    @Override
    public void refresh() {
        loadData(true);
    }

    @Override
    public void reloadError() {
        mView.showLoading();
        loadData(false);
    }

    @Override
    public boolean compareItem(DemoBean oldItem, DemoBean newItem) {
        return TextUtils.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean compareContent(DemoBean oldItem, DemoBean newItem) {
        // 假定 id 相同内容就相同
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        mView.toDetail(mData.get(position).getUrl());
    }

    private void loadData(boolean isRefresh) {
        makeRequest(ModelManager.get(DemoModel.class)
                .getToday(new IModelCallback<List<DemoBean>>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(List<DemoBean> data) {
                        if (null == data || 0 == data.size()) {
                            mData.clear();
                            mView.showEmpty();
                        } else {
                            mData = data;
                            mView.showData(data);
                        }
                    }

                    @Override
                    public void onError(NetError error) {
                        mView.showError(error.getMessage());
                    }

                    @Override
                    public void onComplete(boolean success) {
                        if (isRefresh) {
                            mView.scrollToTop();
                            mView.stopRefresh();
                        }
                    }
                }));
    }
}
