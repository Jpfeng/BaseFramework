package com.jpfeng.baseframework.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.jpfeng.baseframework.R;
import com.jpfeng.framework.base.BaseFragment;
import com.jpfeng.framework.util.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OuterFragment extends BaseFragment {
    private static final String ARG_INDEX = "index";

//    @BindView(R.id.vp_outer)
//    ViewPager mPager;

    @BindView(R.id.rg_fragment)
    RadioGroup mRG;

    private List<InnerFragment> mFragments;
    private FragmentManager mFragmentManager;
    private int mCurrent;

    private int mIndex;

    public static OuterFragment newInstance(int index) {
        OuterFragment fragment = new OuterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    protected int getPageLayoutId() {
        return R.layout.fragment_outer;
    }

    @Override
    protected void init(View pageView) {
        ButterKnife.bind(this, pageView);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InnerFragment fragment = InnerFragment.newInstance(mIndex, i);
            mFragments.add(fragment);
        }

//        mPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragments.size();
//            }
//        });

        mCurrent = 0;
        mRG.check(R.id.rb_fragment_0);
        mFragmentManager = getChildFragmentManager();
        mFragmentManager
                .beginTransaction()
                .add(R.id.fl_container_fragment, mFragments.get(mCurrent), String.valueOf(mCurrent))
                .commit();

        mRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_fragment_0:
                    to(0);
                    break;
                case R.id.rb_fragment_1:
                    to(1);
                    break;
                case R.id.rb_fragment_2:
                    to(2);
                    break;
                case R.id.rb_fragment_3:
                    to(3);
                    break;
                case R.id.rb_fragment_4:
                    to(4);
                    break;
            }
        });
    }

    private void to(int index) {
        if (index == mCurrent) {
            return;
        }

        Fragment current = mFragments.get(mCurrent);
        Fragment target = mFragments.get(index);
        mCurrent = index;

        if (target.isAdded()) {
            mFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .hide(current)
                    .show(target)
                    .commit();

        } else {
            mFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .hide(current)
                    .add(R.id.fl_container_fragment, target, String.valueOf(index))
                    .commit();
        }
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
        Logger.debug("lifecycle", "Outer #" + mIndex + " lazy load");
    }

    @Override
    public void onVisible() {
        super.onVisible();
        Logger.debug("lifecycle", "Outer #" + mIndex + " visible");
    }

    @Override
    public void onInvisible() {
        super.onInvisible();
        Logger.debug("lifecycle", "Outer #" + mIndex + " invisible");
    }
}
