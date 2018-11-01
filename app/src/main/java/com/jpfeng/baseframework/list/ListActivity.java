package com.jpfeng.baseframework.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jpfeng.baseframework.R;
import com.jpfeng.framework.base.ui.BaseStateActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ListActivity extends BaseStateActivity<ListContract.Presenter> implements ListContract.View {

    @BindView(R.id.rv_list)
    RecyclerView mRv;
    private ListAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_list;
    }

    @Override
    protected void initContent() {
        mRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        mAdapter = new ListAdapter();
        mAdapter.setRefreshEnable(true);
        mAdapter.setLoadMoreEnable(true);
        mAdapter.setOnRefreshListener(() -> mPresenter.refresh());
        mAdapter.setOnLoadMoreListener(() -> mPresenter.loadMore());

        View headerView1 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) headerView1.findViewById(R.id.tv_list_item)).setText("header #1");
        headerView1.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        View headerView2 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) headerView2.findViewById(R.id.tv_list_item)).setText("header #2");
        headerView2.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        View headerView3 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) headerView3.findViewById(R.id.tv_list_item)).setText("header #3");
        headerView3.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        View footerView1 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) footerView1.findViewById(R.id.tv_list_item)).setText("footer #1");
        footerView1.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        View footerView2 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) footerView2.findViewById(R.id.tv_list_item)).setText("footer #2");
        footerView2.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        View footerView3 = LayoutInflater.from(this).inflate(R.layout.item_list, null);
        ((TextView) footerView3.findViewById(R.id.tv_list_item)).setText("footer #3");
        footerView3.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 48 * 3));

        mAdapter.addHeader(headerView1);
        mAdapter.addHeader(headerView2);
        mAdapter.addHeader(headerView3);
        mAdapter.addFooter(footerView1);
        mAdapter.addFooter(footerView2);
        mAdapter.addFooter(footerView3);
        mRv.setAdapter(mAdapter);
    }

    @NonNull
    @Override
    protected ListContract.Presenter createPresenter() {
        return new ListPresenter();
    }

    @Override
    public void setNewData(ArrayList<String> data) {
        mRv.postDelayed(() -> {
            mAdapter.setNewData(data);
            mAdapter.refreshEnd();
            showPageContent();
        }, 2000);
    }

    @Override
    public void addData(ArrayList<String> data) {
        mRv.postDelayed(() -> {
            mAdapter.loadMoreEnd();
            mAdapter.addData(data);
        }, 2000);
    }
}
