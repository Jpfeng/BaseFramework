package com.mix.baseframework;

import com.mix.framework.base.mvp.IBasePresenter;
import com.mix.framework.base.mvp.IBaseView;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
class DemoContract {
    interface View extends IBaseView {
        void showTip(String tip);
    }

    interface Presenter extends IBasePresenter<View> {
        void updateModel();
    }
}
