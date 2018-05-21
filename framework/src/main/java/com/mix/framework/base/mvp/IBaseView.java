package com.mix.framework.base.mvp;

/**
 * Author: Jpfeng
 * E-mail: fengjp@mixotc.com
 * Date: 2018/4/26
 */
public interface IBaseView {
    void showPageEmpty();
    void showPageError(String msg);
    void showPageLoading();
    void showPageContent();
}
