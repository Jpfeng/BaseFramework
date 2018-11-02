package com.jpfeng.baseframework;

import com.jpfeng.framework.base.mvp.IBasePresenter;
import com.jpfeng.framework.base.mvp.IBaseView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
class DemoContract {
    interface View extends IBaseView {
        void showData(String tip);
        void showEmpty();
        void showError(String error);
        void showLoading();
    }

    interface Presenter extends IBasePresenter<View> {
        void reload();
    }
}
