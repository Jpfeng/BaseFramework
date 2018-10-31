package com.mix.baseframework.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mix.baseframework.R;
import com.mix.framework.base.BaseFragment;
import com.mix.framework.util.Logger;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Jpfeng
 * E-mail: fengjupeng@whale.ws
 * Date: 2018/10/29
 */
public class InnerFragment extends BaseFragment {
    private static final String ARG_OUTER_INDEX = "index_outer";
    private static final String ARG_INNER_INDEX = "index_inner";

    @BindView(R.id.tv_inner)
    TextView mTvInner;

    private int mOuterIndex;
    private int mInnerIndex;

    public static InnerFragment newInstance(int outerIndex, int innerIndex) {
        InnerFragment fragment = new InnerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OUTER_INDEX, outerIndex);
        args.putInt(ARG_INNER_INDEX, innerIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mOuterIndex = getArguments().getInt(ARG_OUTER_INDEX);
            mInnerIndex = getArguments().getInt(ARG_INNER_INDEX);
        }
    }

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_inner;
    }

    @Override
    protected void init(View pageView) {
        ButterKnife.bind(this, pageView);
        mTvInner.setText("Page #" + mOuterIndex + " Fragment #" + mInnerIndex);
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        Logger.debug("lifecycle", "Inner #" + mOuterIndex + "," + mInnerIndex + " lazy load");
    }

    @Override
    public void onVisible() {
        super.onVisible();
        Logger.debug("lifecycle", "Inner #" + mOuterIndex + "," + mInnerIndex + " visible");
    }

    @Override
    public void onInvisible() {
        super.onInvisible();
        Logger.debug("lifecycle", "Inner #" + mOuterIndex + "," + mInnerIndex + " invisible");
    }
}
