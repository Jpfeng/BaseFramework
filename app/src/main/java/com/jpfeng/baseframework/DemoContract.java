package com.jpfeng.baseframework;

import com.jpfeng.framework.base.mvp.IBasePresenter;
import com.jpfeng.framework.base.mvp.IBaseView;
import com.jpfeng.framework.base.ui.IStateView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
class DemoContract {
    interface View extends IStateView {
        void showTip(String tip);
    }

    interface Presenter extends IBasePresenter<View> {
        void updateModel();
    }
}
