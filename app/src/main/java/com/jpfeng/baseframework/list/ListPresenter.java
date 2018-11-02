package com.jpfeng.baseframework.list;

import com.jpfeng.framework.base.mvp.BasePresenter;

import java.util.ArrayList;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/29
 */
public class ListPresenter extends BasePresenter<ListContract.View> implements ListContract.Presenter {

    private int mIndex = 0;

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        initLoad();
    }

    private void initLoad() {
        refresh();
    }

    @Override
    public void refresh() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("# " + String.valueOf(mIndex));
            mIndex++;
        }
        mView.setNewData(data);
    }

    @Override
    public void loadMore() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add("# " + String.valueOf(mIndex));
            mIndex++;
        }
        mView.addData(data);
    }
}
