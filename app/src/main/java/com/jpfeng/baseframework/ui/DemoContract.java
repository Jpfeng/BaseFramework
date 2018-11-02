package com.jpfeng.baseframework.ui;

import com.jpfeng.baseframework.bean.DemoBean;
import com.jpfeng.baseframework.bean.ResultBean;
import com.jpfeng.framework.base.mvp.IBasePresenter;
import com.jpfeng.framework.base.mvp.IBaseView;

import java.util.List;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/5/21
 */
class DemoContract {
    interface View extends IBaseView {
        void showData(List<DemoBean> data);
        void showEmpty();
        void showError(String error);
        void showLoading();
        void scrollToTop();
        void stopRefresh();
        void toDetail(String url);
    }

    interface Presenter extends IBasePresenter<View> {
        void refresh();
        void reloadError();
        boolean compareItem(DemoBean oldItem, DemoBean newItem);
        boolean compareContent(DemoBean oldItem, DemoBean newItem);
        void onItemClicked(int position);
    }
}
