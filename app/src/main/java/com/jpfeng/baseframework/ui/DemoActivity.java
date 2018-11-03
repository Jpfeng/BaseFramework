package com.jpfeng.baseframework.ui;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jpfeng.baseframework.R;
import com.jpfeng.baseframework.bean.DemoBean;
import com.jpfeng.framework.base.mvp.BaseMVPActivity;
import com.jpfeng.framework.base.ui.BaseListAdapter;
import com.jpfeng.framework.widget.StateContainerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2018/5/21
 */
public class DemoActivity extends BaseMVPActivity<DemoContract.Presenter> implements DemoContract.View {

    @BindView(R.id.scv_demo_container)
    StateContainerView mContainer;
    @BindView(R.id.srl_demo_list)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.rv_demo_list)
    RecyclerView mList;
    @BindView(R.id.tv_page_error_hint)
    TextView mErrorHint;
    private DemoAdapter mAdapter;

    @Override
    protected int getPageLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initPage() {
        ButterKnife.bind(this);

        mList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new DemoAdapter(new BaseListAdapter.ItemComparator<DemoBean>() {
            @Override
            public boolean compareItem(DemoBean oldItem, DemoBean newItem) {
                return mPresenter.compareItem(oldItem, newItem);
            }

            @Override
            public boolean compareContent(DemoBean oldItem, DemoBean newItem) {
                return mPresenter.compareContent(oldItem, newItem);
            }
        });
        mAdapter.setOnItemClickListener(position -> mPresenter.onItemClicked(position));
        mList.setAdapter(mAdapter);
        mRefresh.setColorSchemeResources(R.color.colorAccent);
        mRefresh.setOnRefreshListener(() -> mPresenter.refresh());
    }

    @NonNull
    @Override
    protected DemoContract.Presenter createPresenter() {
        return new DemoPresenter();
    }

    @Override
    public void showData(List<DemoBean> data) {
        mAdapter.setData(data);
        mContainer.showContent();
    }

    @Override
    public void showEmpty() {
        mContainer.showEmpty();
    }

    @Override
    public void showError(String error) {
        mErrorHint.setText(error);
        mContainer.showError();
    }

    @Override
    public void showLoading() {
        mContainer.showLoading();
    }

    @Override
    public void scrollToTop() {
        mList.scrollToPosition(0);
    }

    @Override
    public void stopRefresh() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void toDetail(String url) {
        Snackbar.make(mContainer, url, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_page_error_hint})
    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_page_error_hint:
                mPresenter.reloadError();
                break;
            default:
        }
    }
}
