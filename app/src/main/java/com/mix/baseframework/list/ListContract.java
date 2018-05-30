package com.mix.baseframework.list;

import com.mix.framework.base.mvp.IBasePresenter;
import com.mix.framework.base.mvp.IBaseView;

import java.util.ArrayList;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/29
 */
class ListContract {
    interface View extends IBaseView {
        void setNewData(ArrayList<String> data);
        void addData(ArrayList<String> data);
    }

    interface Presenter extends IBasePresenter<View> {
        void refresh();
        void loadMore();
    }
}
