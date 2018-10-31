package com.jpfeng.framework.base.ui;

import com.jpfeng.framework.base.mvp.IBaseView;

/**
 * Author: Jpfeng
 * E-mail: fengjupeng@whale.ws
 * Date: 2018/10/31
 */
public interface IStateView extends IBaseView {
    void showPageEmpty();
    void showPageError(String msg);
    void showPageLoading();
    void showPageContent();
}
