package com.mix.baseframework;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.mix.framework.base.ui.BaseStateActivity;
import com.mix.framework.util.Logger;

import butterknife.BindView;

public class DemoActivity extends BaseStateActivity<DemoContract.Presenter> implements DemoContract.View {

    @BindView(R.id.tv_demo_tip)
    TextView mTvTip;

    @Override
    protected int getContentView() {
        return R.layout.activity_demo;
    }

    @Override
    protected void initContent() {
    }

    @NonNull
    @Override
    protected DemoContract.Presenter createPresenter() {
        return new DemoPresenter();
    }

    @Override
    public void showTip(String tip) {
        mTvTip.setText(tip);
    }

    @Override
    protected void onErrorViewClicked() {
        super.onErrorViewClicked();
        mPresenter.updateModel();
    }
}
