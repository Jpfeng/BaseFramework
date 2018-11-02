package com.jpfeng.baseframework;

import android.view.View;
import android.widget.TextView;

import com.jpfeng.framework.base.mvp.BaseMVPActivity;
import com.jpfeng.framework.widget.StateContainerView;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends BaseMVPActivity<DemoContract.Presenter> implements DemoContract.View {

    @BindView(R.id.tv_demo_tip)
    TextView mTvTip;
    @BindView(R.id.scv_container)
    StateContainerView mContainer;

    @Override
    protected int getPageLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initPage() {
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    protected DemoContract.Presenter createPresenter() {
        return new DemoPresenter();
    }

    @Override
    public void showData(String tip) {
        mTvTip.setText(tip);
        mContainer.showContent();
    }

    @Override
    public void showEmpty() {
        mContainer.showEmpty();
    }

    @Override
    public void showError(String error) {
        mContainer.showError();
    }

    @Override
    public void showLoading() {
        mContainer.showLoading();
    }

    @OnClick({R.id.tv_demo_tip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_demo_tip:
                mPresenter.reload();
                break;
            default:
        }
    }
}
