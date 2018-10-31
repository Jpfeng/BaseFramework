package com.jpfeng.baseframework;

import android.content.Intent;
import android.widget.TextView;

import com.jpfeng.baseframework.fragment.FragmentTestActivity;
import com.jpfeng.framework.base.ui.BaseStateActivity;

import androidx.annotation.NonNull;
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
        startActivity(new Intent(this, FragmentTestActivity.class));
        finish();
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