package com.mix.baseframework;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.mix.baseframework.list.ListActivity;
import com.mix.framework.base.ui.BaseStateActivity;

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
        startActivity(new Intent(this, ListActivity.class));
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
